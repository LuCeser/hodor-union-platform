package cc.hodor.unionplatform.core.huaweicloud;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.core.BaseCloudTask;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.service.asr.IAsrService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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
 *   zhanglu               2019/1/18-15:53
 *
 ****************************************************************************************/
@Slf4j
public class HuaweiCloudTask extends BaseCloudTask {

    public HuaweiCloudTask(@NonNull String accessKey, @NonNull String accessSecret,
                           @NonNull String fileUri, @NonNull Long fileId,
                           @NonNull IAsrService asrService) {
        super(accessKey, accessSecret, fileUri, fileId, asrService);
    }

    @Override
    public void refreshAuth() {

    }

    @Override
    public AsrStatusEnum startRecognition() {
        long current = System.currentTimeMillis();

        log.info("提交华为云识别请求");

        String taskId = HuaweiCloudUtils.asr(getAccessKey(), getAccessSecret(), getFileUri());
        log.info("提交华为云识别请求成功, taskId: {}", taskId);
        setTaskId(taskId);
        RecognitionResult ret = getRecognitionResult();
        long elapse = (System.currentTimeMillis() - current) / 1000;
        log.info("{}: 识别总耗时 {}s", taskId, elapse);
        if (ret.getStatus() == AsrStatusEnum.SUCCESS) {
            ret.setEngine(VendorEnum.HUAWEI);
            ret.setRecognitionDuration(elapse);
            saveRecognitionResult(ret);
        }
        return ret.getStatus();
    }

    @Override
    public RecognitionResult getRecognitionResult() {

        RecognitionResult result;
        while (true) {
            result = HuaweiCloudUtils.getAsrResult(getAccessKey(), getAccessSecret(), getTaskId());
            if (result.getStatus() == AsrStatusEnum.SUCCESS) {
//                log.info("{} : 识别成功", getTaskId());
                result.setFileId(getFileId());
                break;
            } else if (result.getStatus() == AsrStatusEnum.FAILED) {
//                log.warn("{} : 识别失败", getTaskId());
                break;
            } else {
//                log.info("{} : 识别中", getTaskId());
                try {
                    Thread.sleep(5 * 1000);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }
        }

        return result;
    }
}
