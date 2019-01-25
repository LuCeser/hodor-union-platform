package cc.hodor.unionplatform.core.alicloud;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.core.BaseCloudTask;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.service.asr.IAsrService;
import cc.hodor.unionplatform.util.http.CommonHttpRequest;
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
 *   zhanglu               2019/1/17-17:34
 *
 ****************************************************************************************/
@Slf4j
public class AliCloudTask extends BaseCloudTask {


    public AliCloudTask(String appId, String appAccessKey, String appAccesSecret, String fileUri, long id, IAsrService asrService) {
        super(appId, appAccessKey, appAccesSecret, fileUri, id, asrService);
    }

    @Override
    public void refreshAuth() {

    }

    @Override
    public AsrStatusEnum startRecognition() {

        RecognitionResult ret = new RecognitionResult();
        long current = System.currentTimeMillis();
        log.info("提交阿里识别请求");

        CommonHttpRequest commonHttpRequest = new CommonHttpRequest();


        String taskId = AliCloudUtils.asr(getAppId(), getAccessKey(), getAccessSecret(), getFileUri());
        if (taskId != null) {
            log.info("提交识别请求成功, taskId: {}", taskId);
            setTaskId(taskId);
            ret = getRecognitionResult();
        } else {
            log.error("failed");
            ret.setStatus(AsrStatusEnum.FAILED);
        }
        return ret.getStatus();
    }

    @Override
    public RecognitionResult getRecognitionResult() {
        RecognitionResult result;
        while (true) {
            result = AliCloudUtils.getAsrResult(getAccessKey(), getAccessSecret(), getTaskId());
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
