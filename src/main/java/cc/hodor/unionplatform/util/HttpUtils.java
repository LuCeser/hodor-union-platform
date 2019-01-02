package cc.hodor.unionplatform.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

/***************************************************************************************
 *
 *  Project:        hodor
 *
 *  Copyright ©     
 *
 ***************************************************************************************
 *
 *  Header Name: WellJoint
 *
 *  Description: 
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2018/4/23-16:03
 *
 ****************************************************************************************/
@Slf4j
public class HttpUtils {

    private static final int DEFAULT_TIMEOUT = 2000;
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_CONTENT_TYPE = "application/json";

    private static CloseableHttpClient httpClient = null;

    static {

        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        // Increase max total connection to 200
        cm.setMaxTotal(500);
        // Increase default max connection per route to 20
        cm.setDefaultMaxPerRoute(100);
        // Increase max connections for localhost:80 to 50

        ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                // Honor 'keep-alive' header
                HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {
                        }
                    }
                }
                return 60 * 1000;
            }
        };

        RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_TIMEOUT).build();

        httpClient = HttpClients.custom().setDefaultRequestConfig(config).setConnectionManager(cm).setKeepAliveStrategy(myStrategy).build();

    }

    private static boolean isSuccess(int statusCode) {
        if (200 == statusCode || 201 == statusCode || 202 == statusCode || 204 == statusCode)
            return true;
        return false;
    }

    /**
     * Http post method
     *
     * @param url
     * @param json
     * @param responseClass
     * @param <T>
     * @return
     */
    public static <T> T doPost(String url, String json, Class<T> responseClass) {
        T result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;

        log.info("doPost url={}, content={}", url, json);

        try {
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(DEFAULT_TIMEOUT)
                    .setConnectionRequestTimeout(DEFAULT_TIMEOUT)
                    .setSocketTimeout(DEFAULT_TIMEOUT)
                    .build();

            client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpPost post = new HttpPost(url);
            if (null != json) {
                StringEntity responseEntity = new StringEntity(json, DEFAULT_CHARSET);
                responseEntity.setContentEncoding(DEFAULT_CHARSET);
                responseEntity.setContentType(DEFAULT_CHARSET);
                post.setEntity(responseEntity);
            }

            response = client.execute(post);

            if (response.getStatusLine().getStatusCode() != 200) {
                log.warn("doPost request failed [{} - {}]: {}", response.getStatusLine().getReasonPhrase(), response.getStatusLine().getStatusCode());
                return result;
            }

            String responStr = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
            result = JSON.parseObject(responStr, responseClass);
            return result;
        } catch (Throwable e) {
            log.error("post request failed", e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }

            try {
                if (null != client) {
                    client.close();
                }
            } catch (IOException e) {
                log.error("", e);
            }
        }

        return null;
    }



    public static <T> T doGet(String url, Class<T> responseClass) {
        long start = System.currentTimeMillis();
        T result = null;
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;

        log.info("DoGet from url={}", url);

        try {

            RequestConfig config = RequestConfig.custom().setConnectTimeout(DEFAULT_TIMEOUT).setConnectionRequestTimeout(DEFAULT_TIMEOUT).setSocketTimeout(DEFAULT_TIMEOUT).build();
            client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
            HttpGet e = new HttpGet(url);
            response = client.execute(e);
            if (response.getStatusLine().getStatusCode() == 200) {
                String responseEntity = EntityUtils.toString(response.getEntity());
                result = JSON.parseObject(responseEntity, responseClass);
            } else {
                url = URLDecoder.decode(url, "UTF-8");
                log.warn("doGet 请求提交失败[{}- {}]: {}", response.getStatusLine().getReasonPhrase(), url);
            }
        } catch (Exception e) {
            log.error("doGet 请求提交失败:" + url + "，异常信息：" + e.getMessage(), e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
            }
            try {
                if (client != null)
                    client.close();
            } catch (Exception e) {
            }
        }
        log.info("httpclient doget cost:{},default_timeout:1500,", System.currentTimeMillis() - start);
        return result;
    }

    private static StringEntity createRequestEntity(Object obj) {
        if (obj != null) {
            String json = JSON.toJSONString(obj);
            log.debug("RequestEntity json : " + json.toString());
            StringEntity requestEntity = new StringEntity(json.toString(), Charset.forName("UTF-8"));
            requestEntity.setContentEncoding("UTF-8");
            requestEntity.setContentType("application/json");
            return requestEntity;
        }
        return null;
    }

    /**
     * Http get method
     *
     * @param url
     * @param responseClass
     * @return
     */
    public static <T> T doGet(String url, Type responseClass) {
        HttpGet get = new HttpGet(url);
        return execute(get, responseClass);
    }

    public static <T> T doGet(String url, Map<String, String> header, Type responseClass) {
        HttpGet get = new HttpGet(url);
        for (Map.Entry<String, String> entry : header.entrySet()) {
            get.setHeader(entry.getKey(), entry.getValue());
        }
        return execute(get, responseClass);
    }

    /**
     * Http post method
     *
     * @param url
     * @param reqObj        request
     * @param responseClass
     * @return
     */
    public static <T> T doPost(String url, Object reqObj, Type responseClass) {

        HttpPost post = new HttpPost(url);

        StringEntity reqEntity = createRequestEntity(reqObj);
        if (reqEntity != null) {
            log.debug("DoPost reqEntity : " + reqEntity.toString());
            post.setEntity(reqEntity);
        }
        return execute(post, responseClass);
    }

    public static <T> T doPost(String url, Map<String, String> header,
                               Object reqObj, Type responseClass) {

        HttpPost post = new HttpPost(url);

        for (Map.Entry<String, String> entry : header.entrySet()) {
            post.setHeader(entry.getKey(), entry.getValue());
        }
        StringEntity reqEntity = createRequestEntity(reqObj);
        if (reqEntity != null) {
            log.debug("DoPost reqEntity : " + reqEntity.toString());
            post.setEntity(reqEntity);
        }
        return execute(post, responseClass);
    }


    /**
     * Http put method
     *
     * @param url
     * @param reqObj
     * @param responseClass
     * @return
     */
    public static <T> T doPut(String url, Object reqObj, Type responseClass) {
        HttpPut put = new HttpPut(url);

        StringEntity reqEntity = createRequestEntity(reqObj);
        if (reqEntity != null) {
            put.setEntity(reqEntity);
        }

        return execute(put, responseClass);
    }

    /**
     * Http delete method
     *
     * @param url
     * @param responseClass
     * @return
     */
    public static <T> T doDelete(String url, Type responseClass) {
        HttpDelete delete = new HttpDelete(url);
        return execute(delete, responseClass);
    }


    private static <T> T execute(HttpUriRequest request, Type responseClass) {

        long beginTime = System.currentTimeMillis();

        T result = null;
        CloseableHttpResponse response = null;

        String url = request.getURI().toString();
        String method = request.getMethod();
        log.debug("开始调用 " + method + ", url=" + url);
        try {
            response = httpClient.execute(request);
            log.debug("调用完成，处理结果... " + response.getStatusLine());
            if (response.getEntity() != null && (response.getEntity().getContentLength() > 0 || response.getEntity().isChunked())) {
                String responseEntity = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                log.debug("response message : " + responseEntity);
                result = JSON.parseObject(responseEntity, responseClass);
            } else {
                log.debug(method + "请求成功，没有返回任何内容");
            }
            if (isSuccess(response.getStatusLine().getStatusCode())) {
                if (response.getEntity() != null && (response.getEntity().getContentLength() > 0 || response.getEntity().isChunked())) {
                    String responseEntity = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                    log.debug("response message : " + responseEntity);
                    result = JSON.parseObject(responseEntity, responseClass);
                } else {
                    log.debug(method + "请求成功，没有返回任何内容");
                }
            } else {
                log.error(method + " 请求提交失败[" + response.getStatusLine() + "], url=" + url);
            }

        } catch (Exception e) {
            log.error(method + " 请求提交失败:" + url + "，异常信息：" + e.getMessage(), e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
            }

        }

        long delayTime = System.currentTimeMillis() - beginTime;
        log.debug("调用 " + url + " over, elapsed: " + delayTime);

        return result;

    }

    public static <T> T doGetWithSession(String urlOld, Type responseClass, String sessionId) {
        HttpGet request = new HttpGet(urlOld);
        long beginTime = System.currentTimeMillis();

        T result = null;
        CloseableHttpResponse response = null;

        String url = request.getURI().toString();
        String method = request.getMethod();
        request.setHeader("sessionId", sessionId);
        log.debug("开始调用 " + method + ", url=" + url);
        try {
            response = httpClient.execute(request);
            log.debug("调用完成，处理结果... " + response.getStatusLine());
            if (isSuccess(response.getStatusLine().getStatusCode())) {
                if (response.getEntity().getContentLength() > 0 || response.getEntity().isChunked()) {
                    String responseEntity = EntityUtils.toString(response.getEntity(), Charset.forName("UTF-8"));
                    log.debug("response message : " + responseEntity);
                    result = JSON.parseObject(responseEntity, responseClass);
                } else {
                    log.debug(method + "请求成功，没有返回任何内容");
                }
            } else {
                log.error(method + " 请求提交失败[" + response.getStatusLine() + "], url=" + url);
            }

        } catch (Exception e) {
            log.error(method + " 请求提交失败:" + url + "，异常信息：" + e.getMessage(), e);
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (Exception e) {
            }

        }

        long delayTime = System.currentTimeMillis() - beginTime;
        log.debug("调用 " + url + " over, elapsed: " + delayTime);
        return result;
    }

}
