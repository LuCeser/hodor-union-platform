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

        AsrStatusEnum status = AsrStatusEnum.FAILED;

        log.info("提交华为云识别请求");
        String taskId = HuaweiCloudUtils.asr(getAccessKey(), getAccessSecret(), getFileUri());
        if (taskId != null) {
            log.info("提交华为云识别请求成功, taskId: {}", taskId);
            setTaskId(taskId);
            RecognitionResult ret = getRecognitionResult();

            status = ret.getStatus();
        } else {
            log.warn("cannot get taskId, task failed");
        }

        return status;
    }

    @Override
    public RecognitionResult getRecognitionResult() {

        RecognitionResult result;
        while (true) {
            result = HuaweiCloudUtils.getAsrResult(getAccessKey(), getAccessSecret(), getTaskId());
            if (result.getStatus() == AsrStatusEnum.SUCCESS) {
                result.setFileId(getFileId());
                break;
            } else if (result.getStatus() == AsrStatusEnum.FAILED) {
                break;
            } else {
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
