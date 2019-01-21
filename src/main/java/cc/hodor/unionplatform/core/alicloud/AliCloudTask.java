package cc.hodor.unionplatform.core.alicloud;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.core.BaseCloudTask;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.service.asr.IAsrService;
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

        long current = System.currentTimeMillis();

        log.info("提交阿里识别请求");
        String taskId = AliCloudUtils.asr(getAppId(), getAccessKey(), getAccessSecret(), getFileUri());
        log.info("提交识别请求成功, taskId: {}", taskId);
        setTaskId(taskId);
        RecognitionResult ret = getRecognitionResult();
        long elapse = (System.currentTimeMillis() - current) / 1000;
        log.info("{}: 识别总耗时 {}s", taskId, elapse);
        if (ret.getStatus() == AsrStatusEnum.SUCCESS) {
            ret.setEngine(VendorEnum.ALI);
            ret.setRecognitionDuration(elapse);
            saveRecognitionResult(ret);
        }
        return ret.getStatus();
    }

    @Override
    public RecognitionResult getRecognitionResult() {
        RecognitionResult result;
        while (true) {
            result = AliCloudUtils.getAsrResult(getAccessKey(), getAccessSecret(), getTaskId());
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
