package cc.hodor.unionplatform.core;

import cc.hodor.unionplatform.base.constant.AsrStatusEnum;
import cc.hodor.unionplatform.base.constant.VendorEnum;
import cc.hodor.unionplatform.base.entity.Sentence;
import cc.hodor.unionplatform.core.entity.RecognitionResult;
import cc.hodor.unionplatform.util.http.AisHttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.common.AuthInfo;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.util.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *  Description:
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/14-17:01
 *
 ****************************************************************************************/
@Slf4j
public class HuaweiCloudUtils {

    private static final String uri = "/v1.0/voice/asr/long-sentence";

    private static final int connectionTimeout = 5000; //连接目标url超时限制
    private static final int connectionRequestTimeout = 1000;//连接池获取可用连接超时限制
    private static final int socketTimeout = 20000;//获取服务器响应数据超时限制

    public static AisAccess getAccessService(String accessKeyId, String accessKeySecret) {
        AuthInfo authInfo = new AuthInfo(
                /*  语音识别服务的服务端点, 该服务端口信息可以从如下地址查询
                 *  http://developer.huaweicloud.com/dev/endpoint
                 */
                "https://ais.cn-north-1.myhuaweicloud.com",
                "cn-north-1",  /* 图像处理服务的区域信息, 可以在上面的地址中查询 */
                accessKeyId,
                accessKeySecret);

        AisAccess service = new AisAccess(authInfo, connectionTimeout, connectionRequestTimeout, socketTimeout);

        return service;
    }

    public static String asr(AisAccess aisService, String file) {
        byte[] fileData = new byte[0];
        try {
            fileData = FileUtils.readFileToByteArray(new File(file));

            String fileBase64Str = Base64.encodeBase64String(fileData);
            JSONObject json = new JSONObject();
            json.put("data", fileBase64Str); //如果音频Base64编码超过10MB（对应音频本身约6MB），请使用OBS方式，注释掉此行

            AisHttpRequest request = new AisHttpRequest();

            HttpResponse response = aisService.post(uri, json.toJSONString());
            if (response.getStatusLine().getStatusCode() != 200) {
                log.warn("调用语音识别服务失败");
            }

            if (response.getEntity().getContentLength() > 0 || response.getEntity().isChunked()) {
                try {
                    String responseEntity = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                    JSONObject resultObject = JSON.parseObject(responseEntity);
                    String jobId = resultObject.getJSONObject("result").getString("job_id");
                    return jobId;
                } catch (IOException e) {
                    log.error("", e);
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }

        return null;
    }

    public static List<String> asr(AisAccess aisService, List<String> fileList) {

        List<String> jobIds = new ArrayList<>(fileList.size());
        for (String filePath : fileList) {
            String jobId = asr(aisService, filePath);
            if (!StringUtils.isEmpty(jobId)) {
                jobIds.add(jobId);
            }
        }

        return jobIds;
    }

    public static RecognitionResult getAsrResult(AisAccess service, String jobId) {

        RecognitionResult recognitionResult = new RecognitionResult();

        String url = uri + "?job_id=" + jobId;

        HttpResponse getResponse = null;
        String result = null;
        try {

            JSONObject resp = null;
            JSONObject jsonObject = null;
            int status = -1;
            while (true) {
                getResponse = service.get(url);
                result = HttpClientUtils.convertStreamToString(getResponse.getEntity().getContent());

                resp = JSON.parseObject(result);
                jsonObject = (JSONObject) resp.get("result");
                status = (int) jsonObject.get("status_code");
                if (status == -1) {
                    log.warn("{}: 任务识别失败", jobId);
                    recognitionResult.setStatus(AsrStatusEnum.FAILED);
                    break;
                } else if (status == 2) {
                    // 7. 处理服务返回的字符流，输出识别结果。
                    result = (String) jsonObject.get("words");
                    log.info("{}: 任务识别完成", jobId);

                    recognitionResult.setStatus(AsrStatusEnum.SUCCESS);
                    recognitionResult.setEngine(VendorEnum.HUAWEI);

                    Sentence sentence = new Sentence();

                    sentence.setText("");

                    //可选动作，音频识别结束，将音频从obs中删除
                    //obsFileHandle.delete();
                    break;
                }
                // status == 0 || status == 1
                else {
                    // 6.1 如果没有返回，等待一段时间，继续进行轮询。
                    log.info("{}: 任务识别中", jobId);
                    Thread.sleep(5 * 1000);
                    continue;
                }
            }

        } catch (IOException e) {
            log.error("", e);
        } catch (InterruptedException e) {
            log.error("", e);
        }

        log.info(result);

        return recognitionResult;
    }
}
