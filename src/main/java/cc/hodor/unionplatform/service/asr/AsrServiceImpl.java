package cc.hodor.unionplatform.service.asr;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.core.AliCloudUtils;
import cc.hodor.unionplatform.core.entity.OSSResult;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.dao.RecognitionResultMapper;
import cc.hodor.unionplatform.dao.RecordMapper;
import cc.hodor.unionplatform.model.AuthenticationDO;
import cc.hodor.unionplatform.model.RecognitionResultDO;
import cc.hodor.unionplatform.model.RecordDO;
import cc.hodor.unionplatform.service.ServiceResult;
import cc.hodor.unionplatform.service.authentication.IAuthenticationService;
import cc.hodor.unionplatform.util.OSSUtils;
import cc.hodor.unionplatform.web.asr.AsrDTO;
import com.aliyuncs.IAcsClient;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
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

    private static CountDownLatch latch;

    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    private static final String PATTERN = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}&";


    @Override
    public ServiceResult startAsr(AsrDTO asrDTO) {
//        Pattern pattern = Pattern.compile(PATTERN);

        ServiceResult ret = new ServiceResult(false);

        ServiceResult authServiceRet = authenticationService.getAuthentication(asrDTO.getEngine());
        if (authServiceRet.isSuccess()) {

            latch = new CountDownLatch(asrDTO.getConcurrentNumber());

            AuthenticationDO authenticationDO = (AuthenticationDO) authServiceRet.getData();
            String appAccessKey = authenticationDO.getAppAccessKey();
            String appAccesSecret = authenticationDO.getAppAccessSecret();
            String appId = authenticationDO.getAppId();
            if (asrDTO.getEngine() == VendorEnum.ALI) {
                String endpoint = (String) authenticationDO.getExtendInfo().get("endpoint");
                String bucketName = (String) authenticationDO.getExtendInfo().get("bucket");
                Date expiration = new Date(new Date().getTime() + 3600 * 1000); // 设置临时URL有效时间为1小时
                OSSResult ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
                        bucketName, null, asrDTO.getConcurrentNumber(), expiration);
                IAcsClient acsClient = AliCloudUtils.getAliClient(appAccessKey, appAccesSecret);


                while (!StringUtils.isEmpty(ossResult.getNextMarker())) {
                    for (Map fileUrlMap : ossResult.getFileUrls()) {
                        String fileName = (String) fileUrlMap.get("filename");

                        String uid = fileName.substring(15, 51);
                        RecordDO recordDO = recordMapper.findByUid(uid);

                        String url = (String) fileUrlMap.get(fileName);
                        String taskId = AliCloudUtils.asr(acsClient, appId, url);
                        executorService.execute(new AsrResultTask(latch, taskId, recordDO.getId(), acsClient, this));
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            log.error("", e);
                        }
                    }
                    ossResult = OSSUtils.generatePresignedUrls(endpoint, appAccessKey, appAccesSecret,
                            bucketName, ossResult.getNextMarker(), asrDTO.getConcurrentNumber(), expiration);
                }

            }
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
}

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

}