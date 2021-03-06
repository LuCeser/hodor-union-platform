package cc.hodor.unionplatform.core.alicloud;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.base.entity.Sentence;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

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
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2018/12/31-17:07
 *
 ****************************************************************************************/
@Slf4j
public class AliCloudUtils {

    public static final String ENDPOINT_NAME = "cn-shanghai";
    public static final String REGIN_ID = "cn-shanghai";
    public static final String PRODUCT = "nls-filetrans";
    public static final String DOMAIN = "filetrans.cn-shanghai.aliyuncs.com";

    public static final String API_VERSION = "2018-08-17";
    public static final String POST_REQUEST_ACTION = "SubmitTask";

    private static IAcsClient client;


    /**
     * 阿里云权限认证, 调用阿里云SDK
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    public static IAcsClient getAliClient(String accessKeyId, String accessKeySecret) {

        IAcsClient aliClient = null;

        try {
            // 设置endpoint
            DefaultProfile.addEndpoint(ENDPOINT_NAME, REGIN_ID, PRODUCT, DOMAIN);
            DefaultProfile profile = DefaultProfile.getProfile(REGIN_ID, accessKeyId, accessKeySecret);
            aliClient = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            log.error("设置阿里云EndPoint信息失败");
        }

        return aliClient;
    }

    /**
     * 调用阿里云语音转写服务
     *
     * @param appId
     * @param fileUrl
     * @return 任务id
     */
    public static String asr(String appId, String accessKey, String accessSecret,
                             String fileUrl) {

        CommonRequest postRequest = new CommonRequest();
        postRequest.setDomain(DOMAIN); // 设置域名，固定值
        postRequest.setVersion(API_VERSION);         // 设置API的版本号，固定值
        postRequest.setAction(POST_REQUEST_ACTION);          // 设置action，固定值
        postRequest.setProduct(PRODUCT);      // 设置产品名称，固定值

        JSONObject taskObject = new JSONObject();
        taskObject.put("app_key", appId);
        // 录音文件路径
        taskObject.put("file_link", fileUrl);
        String task = taskObject.toJSONString();
        postRequest.putBodyParameter("Task", task);
        postRequest.setMethod(MethodType.POST);

        String taskId = "";
        try {
            if (client == null) {
                client = getAliClient(accessKey, accessSecret);
            }
            CommonResponse postResponse = client.getCommonResponse(postRequest);
            if (postResponse.getHttpStatus() == 200) {
                JSONObject result = JSONObject.parseObject(postResponse.getData());
                String statusText = result.getString("StatusText");
                if (statusText.equals("SUCCESS")) {
                    log.info("录音文件识别请求成功响应： {}", result.toJSONString());
                    taskId = result.getString("TaskId");
                } else {
                    log.warn("录音文件识别请求失败： {}", result.toJSONString());
                }
            } else {
                log.warn("录音文件识别请求失败，Http错误码：{}, 响应：{}", postResponse.getHttpStatus(), JSONObject.toJSONString(postResponse));
            }
        } catch (ClientException e) {
            log.error("提交阿里云asr识别请求失败");
        }

        return taskId;
    }


    public static RecognitionResult getAsrResult(String accessKey, String accessSecret, String taskId) {

        RecognitionResult recognitionResult = new RecognitionResult();

        CommonRequest getRequest = new CommonRequest();
        getRequest.setDomain(DOMAIN);   // 设置域名，固定值
        getRequest.setVersion(API_VERSION);             // 设置API版本，固定值
        getRequest.setAction("GetTaskResult");           // 设置action，固定值
        getRequest.setProduct(PRODUCT);          // 设置产品名称，固定值
        getRequest.putQueryParameter("TaskId", taskId);  // 设置任务ID为查询参数，传入任务ID
        getRequest.setMethod(MethodType.GET);            // 设置为GET方式的请求

        CommonResponse getResponse = null;
        String statusText = "";
        try {
            if (client == null) {
                client = getAliClient(accessKey, accessSecret);
            }
            getResponse = client.getCommonResponse(getRequest);
            if (getResponse.getHttpStatus() != 200) {
                log.warn("{} : 识别结果查询请求失败，Http错误码： {}, {}", taskId, getResponse.getHttpStatus(), getResponse.getData());

                recognitionResult.setStatus(AsrStatusEnum.FAILED);
                return recognitionResult;
            }
            JSONObject result = JSONObject.parseObject(getResponse.getData());
            statusText = result.getString("StatusText");
            if (statusText.equals("RUNNING") || statusText.equals("QUEUEING")) {
                log.info("{} : 任务正在运行中 {}", taskId, statusText);
                recognitionResult.setStatus(AsrStatusEnum.RUNNING);
                return recognitionResult;
            } else if (StringUtils.equals("SUCCESS", statusText)){
                recognitionResult.setStatus(AsrStatusEnum.SUCCESS);
                recognitionResult.setDuration(result.getLong("BizDuration") != null ? result.getLong("BizDuration") : 0);
                JSONArray sentenceArray = result.getJSONObject("Result").getJSONArray("Sentences");
                String sentenceStr = JSONObject.toJSONString(sentenceArray);
                List<Sentence> sentences = JSONArray.parseArray(sentenceStr, Sentence.class);

                recognitionResult.setEngine(VendorEnum.ALI);
                recognitionResult.setSentences(sentences);
            } else if (StringUtils.equals("USER_BIZDURATION_QUOTA_EXCEED", statusText)){
                recognitionResult.setStatus(AsrStatusEnum.QUOTA_EXCEED);
                log.warn("{} : 语音识别调用超限: {}", taskId, result.toJSONString());
            } else {
                recognitionResult.setStatus(AsrStatusEnum.FAILED);
                log.warn("{} : 获取语音识别结果失败: {}", taskId, result.toJSONString());
            }
        } catch (ClientException e) {
            log.error("获取语音识别结果失败", e);
        }

        if (statusText.equals("SUCCESS") || statusText.equals("SUCCESS_WITH_NO_VALID_FRAGMENT")) {
            log.info("录音文件识别成功！");
        } else {
            log.error("录音文件识别失败！");
        }

        return recognitionResult;
    }
}
