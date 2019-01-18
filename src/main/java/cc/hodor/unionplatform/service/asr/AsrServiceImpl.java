package cc.hodor.unionplatform.service.asr;

import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.core.TaskProducer;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.dao.RecognitionResultMapper;
import cc.hodor.unionplatform.dao.RecordMapper;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.model.RecognitionResultDO;
import cc.hodor.unionplatform.model.RecordDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.authentication.IAuthenticationService;
import cc.hodor.unionplatform.web.asr.AsrDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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


    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    private static final String PATTERN = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}&";


    @Override
    public ServiceResult startAsr(AsrDTO asrDTO) {
//        Pattern pattern = Pattern.compile(PATTERN);

        ServiceResult ret = new ServiceResult(false);

        ServiceResult authServiceRet = authenticationService.getAuthentication(asrDTO.getEngine());
        if (authServiceRet.isSuccess()) {


//            latch = new CountDownLatch(asrDTO.getConcurrentNumber());

            AuthenticationDO authenticationDO = (AuthenticationDO) authServiceRet.getData();
            TaskProducer taskProducer = new TaskProducer(asrDTO, authenticationDO, this);
            executorService.execute(taskProducer);

//            String appAccessKey = authenticationDO.getAppAccessKey();
//            String appAccesSecret = authenticationDO.getAppAccessSecret();
//            String appId = authenticationDO.getAppId();
//            if (asrDTO.getEngine() == VendorEnum.ALI) {
//
//                // 创建阻塞队列，控制长语音识别任务并发
//                // 考虑到并发限制只存在于引擎，因此队列只需要在不同引擎下新建
//                BlockingQueue<BaseCloudTask> taskQueue = new LinkedBlockingQueue<>(asrDTO.getConcurrentNumber());
//
//                String endpoint = (String) authenticationDO.getExtendInfo().get("endpoint");
//                String bucketName = (String) authenticationDO.getExtendInfo().get("bucket");
//                Date expiration = new Date(new Date().getTime() + 3600 * 1000); // 设置临时URL有效时间为1小时
//                OSSResult ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
//                        bucketName, "", asrDTO.getConcurrentNumber(), expiration);
//
//                List<TaskConsumer> consumerList = new ArrayList<>(asrDTO.getConcurrentNumber());
//                for (int i = 0; i < asrDTO.getConcurrentNumber(); i++) {
//                    TaskConsumer taskConsumer = new TaskConsumer(taskQueue);
//                    executorService.execute(taskConsumer);
//                }
//
//                while (!StringUtils.isEmpty(ossResult.getNextMarker())) {
//                    for (Map fileUrlMap : ossResult.getFileUrls()) {
//                        String fileName = (String) fileUrlMap.get("filename");
//
//                        String uid = fileName.substring(15, 51);
//                        RecordDO recordDO = recordMapper.findByUid(uid);
//                        String fileUri = (String) fileUrlMap.get(fileName);
//
//                        BaseCloudTask cloudTask = new AliCloudTask(appId, appAccessKey, appAccesSecret, fileUri, recordDO.getId(), this);
//
//                        try {
//                            taskQueue.put(cloudTask);
//                            log.info("任务队列大小: {}", taskQueue.size());
//                        } catch (InterruptedException e) {
//                            log.error("任务无法放入队列", e);
//                        }
//
//                    }
//                    ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
//                            bucketName, ossResult.getNextMarker(), asrDTO.getConcurrentNumber(), expiration);
//                }
//
//                for (TaskConsumer taskConsumer:consumerList) {
//                    taskConsumer.setShutdown(true);
//                }
//
//            }
        } else {
            log.warn("未找到引擎: {} 配置信息", asrDTO.getEngine());
        }

        return ret;
    }


    @Override
    public ServiceResult stopAsr(VendorEnum vendorEnum) {
        return null;
    }

    public void saveRecognitionResult(RecognitionResult recognitionResult) {

        RecognitionResultDO recognitionResultDO = new RecognitionResultDO();
        recognitionResultDO.setDuration(recognitionResult.getDuration());
        recognitionResultDO.setEngine(VendorEnum.ALI);
        recognitionResultDO.setFileId(recognitionResult.getFileId());
        recognitionResultDO.setResult(recognitionResult.getSentences());

        log.info("识别结果插入数据库");

        int updates = recognitionResultMapper.insertResult(recognitionResultDO);
    }

    @Override
    public RecordDO findFileId(String uid) {
        return recordMapper.findByUid(uid);
    }
}

/*
@Slf4j
@AllArgsConstructor
class AsrResultTask implements Runnable {

    private CountDownLatch latch;

    private String taskId;
    private long fileId;

    private IAcsClient acsClient;
    private IAsrService asrService;

    @Override
    public void run() {

        RecognitionResult result;

        try {
            while (true) {
                result = AliCloudUtils.getAsrResult(acsClient, taskId);
                if (result.getStatus() == AsrStatusEnum.SUCCESS) {
                    log.info("{} : 识别成功", taskId);
                    result.setFileId(fileId);
                    this.asrService.saveRecognitionResult(result);
                    break;
                } else if (result.getStatus() == AsrStatusEnum.FAILED) {
                    log.warn("{} : 识别失败", taskId);
                    break;
                } else {
                    log.info("{} : 识别中", taskId);
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (latch != null) {
                latch.countDown();
            }
        }
    }

}*/
