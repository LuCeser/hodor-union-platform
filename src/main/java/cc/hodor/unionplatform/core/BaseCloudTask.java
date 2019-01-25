package cc.hodor.unionplatform.core;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.service.asr.IAsrService;
import lombok.*;
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
 *
 *  云服务基础对象
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/17-15:48
 *
 ****************************************************************************************/
@Slf4j
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class BaseCloudTask {

    /**
     * 提交的任务id，可以用来查询结果
     */
    @Setter
    private String taskId;

    private String appId;
    @NonNull
    private String accessKey;
    @NonNull
    private String accessSecret;
    @NonNull
    private String fileUri;
    @NonNull
    private Long fileId;

    /**
     * expire time
     */
    @Setter
    private Long expireAt;

    @NonNull
    private IAsrService asrService;

    public BaseCloudTask(String appId, @NonNull String accessKey, @NonNull String accessSecret,
                         @NonNull String fileUri, @NonNull Long fileId,
                         @NonNull IAsrService asrService) {
        this.appId = appId;
        this.accessKey = accessKey;
        this.accessSecret = accessSecret;
        this.fileUri = fileUri;
        this.fileId = fileId;
        this.asrService = asrService;
        this.expireAt = 0L;
    }

    public boolean startTask() {

        AsrStatusEnum taskStatus = startRecognition();
        if (taskStatus == AsrStatusEnum.FAILED || taskStatus == AsrStatusEnum.QUOTA_EXCEED) {
            log.warn("task failure, status: {}", taskStatus);
            return false;
        } else {
            long current = System.currentTimeMillis();
            log.info("task status: {}", taskStatus);
            RecognitionResult result;

            while (true) {
                result = getRecognitionResult();
                if (result.getStatus() == AsrStatusEnum.SUCCESS) {
                    long elapse = (System.currentTimeMillis() - current) / 1000;
                    result.setFileId(getFileId());
                    result.setDuration(elapse);
                    log.info("{}: 识别总耗时 {}s", taskId, elapse);
                    boolean saveResult = saveRecognitionResult(result);

                    break;
                } else if (result.getStatus() == AsrStatusEnum.RUNNING) {
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        log.error("", e);
                    }
                } else {
                    log.warn("failure");
                    break;
                }

            }

            log.info("{} task end", taskId);
            return true;
        }
    }

    public abstract void refreshAuth();

    public abstract AsrStatusEnum startRecognition();

    public abstract RecognitionResult getRecognitionResult();


    /**
     * 持久化识别结果
     *
     * @param recognitionResult
     * @return
     */
    public boolean saveRecognitionResult(RecognitionResult recognitionResult) {
        return asrService.saveRecognitionResult(recognitionResult);
    }

}
