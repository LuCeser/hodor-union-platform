package cc.hodor.unionplatform.service.asr;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.core.BaseCloudTask;
import cc.hodor.unionplatform.core.TaskConsumer;
import cc.hodor.unionplatform.core.TaskProducer;
import cc.hodor.unionplatform.core.alicloud.AliCloudTask;
import cc.hodor.unionplatform.core.entity.OSSResult;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.core.huaweicloud.HuaweiCloudTask;
import cc.hodor.unionplatform.dao.RecognitionResultMapper;
import cc.hodor.unionplatform.dao.RecordMapper;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.model.RecognitionResultDO;
import cc.hodor.unionplatform.model.RecordDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.authentication.IAuthenticationService;
import cc.hodor.unionplatform.util.FileUtils;
import cc.hodor.unionplatform.util.OSSUtils;
import cc.hodor.unionplatform.web.asr.AsrDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *
 *  Description: 
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/9-8:56
 *
 ****************************************************************************************/
@Slf4j
@RequiredArgsConstructor
@Service("asrService")
public class AsrServiceImpl implements IAsrService {

    @NonNull
    private IAuthenticationService authenticationService;

    @NonNull
    private RecognitionResultMapper recognitionResultMapper;

    @NonNull
    private RecordMapper recordMapper;

    private static ConcurrentHashMap<VendorEnum, List<TaskConsumer>> engineTaskConsumers = new ConcurrentHashMap<>(6);

    private ExecutorService executorService = Executors.newFixedThreadPool(64);


    @Override
    public ServiceResult startAsr(AsrDTO asrDTO) {

        ServiceResult ret = new ServiceResult(false);

        ServiceResult authServiceRet = authenticationService.getAuthentication(asrDTO.getEngine());
        if (authServiceRet.isSuccess()) {

            TaskProducer taskProducer = new TaskProducer(asrDTO, this);
            executorService.execute(taskProducer);

        } else {
            log.warn("未找到引擎: {} 配置信息", asrDTO.getEngine());
        }

        return ret;
    }


    @Override
    public ServiceResult stopAsr(VendorEnum vendorEnum) {
        ServiceResult ret = new ServiceResult(false);

        List<TaskConsumer> consumerList = engineTaskConsumers.remove(vendorEnum);
        if (null != consumerList) {
            for (TaskConsumer taskConsumer : consumerList) {
                taskConsumer.setShutdown(true);
            }
            ret.setSuccess(true);
        } else {
            log.info("{}: 没有找到正在运行的活动", vendorEnum);
        }

        return ret;
    }


    @Override
    public void longSentenceRecognition(AsrDTO asrDTO) {

        ServiceResult authServiceRet = authenticationService.getAuthentication(asrDTO.getEngine());
        AuthenticationDO authenticationDO = (AuthenticationDO) authServiceRet.getData();

        if (asrDTO.getEngine() == VendorEnum.ALI) {
            aliAsr(asrDTO, authenticationDO);
        } else if (asrDTO.getEngine() == VendorEnum.HUAWEI) {
            huaweiAsr(asrDTO, authenticationDO);
        }

    }

    private void aliAsr(AsrDTO asrDTO, AuthenticationDO authenticationDO) {

        String appId = authenticationDO.getAppId();
        String appAccessKey = authenticationDO.getAppAccessKey();
        String appAccesSecret = authenticationDO.getAppAccessSecret();

        String endpoint = (String) authenticationDO.getExtendInfo().get("endpoint");
        String bucketName = (String) authenticationDO.getExtendInfo().get("bucket");
        Date expiration = new Date(new Date().getTime() + 3600 * 1000); // 设置临时URL有效时间为1小时
        OSSResult ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
                bucketName, asrDTO.getMarker(), asrDTO.getConcurrentNumber(), expiration);

        // 创建阻塞队列，控制长语音识别任务并发
        // 考虑到并发限制只存在于引擎，因此队列只需要在不同引擎下新建
        BlockingQueue<BaseCloudTask> taskQueue = new LinkedBlockingQueue<>(asrDTO.getConcurrentNumber());
        List<TaskConsumer> consumerList = new ArrayList<>(asrDTO.getConcurrentNumber());
        for (int i = 0; i < asrDTO.getConcurrentNumber(); i++) {
            TaskConsumer taskConsumer = new TaskConsumer(taskQueue);
            executorService.execute(taskConsumer);
            consumerList.add(taskConsumer);
        }

        engineTaskConsumers.put(VendorEnum.ALI, consumerList);

        while (!StringUtils.isEmpty(ossResult.getNextMarker())) {
            for (Map fileUrlMap : ossResult.getFileUrls()) {
                String fileName = (String) fileUrlMap.get("filename");

                String uid = fileName.substring(15, 51);
                RecordDO recordDO = findFileId(uid);
                String fileUri = (String) fileUrlMap.get(fileName);

                BaseCloudTask cloudTask = new AliCloudTask(appId, appAccessKey, appAccesSecret, fileUri, recordDO.getId(), this);

                try {
                    taskQueue.put(cloudTask);
                    log.info("任务队列大小: {}", taskQueue.size());
                } catch (InterruptedException e) {
                    log.error("任务无法放入队列", e);
                }

            }
            ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
                    bucketName, ossResult.getNextMarker(), asrDTO.getConcurrentNumber(), expiration);
        }

        for (TaskConsumer taskConsumer : consumerList) {
            taskConsumer.setShutdown(true);
        }

        engineTaskConsumers.remove(VendorEnum.ALI);

    }


    private void huaweiAsr(AsrDTO asrDTO, AuthenticationDO authenticationDO) {

        String makrer = "";
        boolean markerFlag = false;
        if (asrDTO.getMarker() != null) {
            makrer = asrDTO.getFileDirectory().trim() + File.separator + asrDTO.getMarker().trim();
            markerFlag = true;
        }

        String appAccessKey = authenticationDO.getAppAccessKey();
        String appAccesSecret = authenticationDO.getAppAccessSecret();

        List<String> files = FileUtils.getListFiles(asrDTO.getFileDirectory(), "", false);

        BlockingQueue<BaseCloudTask> taskQueue = new LinkedBlockingQueue<>(asrDTO.getConcurrentNumber());
        List<TaskConsumer> consumerList = new ArrayList<>(asrDTO.getConcurrentNumber());
        for (int i = 0; i < asrDTO.getConcurrentNumber(); i++) {
            TaskConsumer taskConsumer = new TaskConsumer(taskQueue);
            executorService.execute(taskConsumer);
            consumerList.add(taskConsumer);
        }

        engineTaskConsumers.put(VendorEnum.HUAWEI, consumerList);


        for (String filePath : files) {


            if (markerFlag == false) {
                int pos = filePath.lastIndexOf("_");
                int end = filePath.lastIndexOf(".");
                String uid = filePath.substring(pos + 1, end);

                RecordDO recordDO = findFileId(uid);
                long recordId = 0;
                if (recordDO != null) {
                    recordId = recordDO.getId();
                }

                BaseCloudTask cloudTask = new HuaweiCloudTask(appAccessKey, appAccesSecret, filePath, recordId, this);

                try {
                    taskQueue.put(cloudTask);
                    log.info("任务队列大小: {}", taskQueue.size());
                } catch (InterruptedException e) {
                    log.error("任务无法放入队列", e);
                }

            } else {
                if (StringUtils.equals(filePath, makrer)) {
                    log.info("find marker {}", makrer);
                    markerFlag = false;
                }
            }

        }

    }

    public void saveRecognitionResult(RecognitionResult recognitionResult) {

        RecognitionResultDO recognitionResultDO = new RecognitionResultDO();
        recognitionResultDO.setDuration(recognitionResult.getDuration());
        recognitionResultDO.setFileId(recognitionResult.getFileId());
        recognitionResultDO.setResult(recognitionResult.getSentences());
        recognitionResultDO.setEngine(recognitionResult.getEngine());
        recognitionResultDO.setRecognitionDuration(recognitionResult.getRecognitionDuration());
        recognitionResultDO.setCreateTime(new Date());

        log.info("识别结果插入数据库");

        int updates = recognitionResultMapper.insertResult(recognitionResultDO);
    }

    @Override
    public RecordDO findFileId(String uid) {
        return recordMapper.findByUid(uid);
    }
}