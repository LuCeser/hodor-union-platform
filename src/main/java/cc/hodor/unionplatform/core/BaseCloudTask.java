package cc.hodor.unionplatform.core;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.service.asr.IAsrService;
import lombok.*;

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

        asrService.saveRecognitionResult(recognitionResult);

        return false;
    }

}
