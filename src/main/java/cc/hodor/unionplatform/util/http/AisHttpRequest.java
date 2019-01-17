package cc.hodor.unionplatform.util.http;

import cc.hodor.unionplatform.util.SignUtil;
import com.alibaba.fastjson.JSONObject;
import io.undertow.util.Headers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
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
 *  ${DESCRIPTION}
 *
 *  Revision History:
 *                                   Modification
 *   Author                  Date(MM/DD/YYYY)             JiraID            Description of Changes
 *   ----------------      ------------------------       -------------     ----------------------
 *   zhanglu               2019/1/16-14:51
 *
 ****************************************************************************************/
public class AisHttpRequest {

    private HashMap<String, String> headers;
    private HashMap<String, String> params;
    private HashMap<String, Object> body;
    private URI uri;
    private HttpMethodNameENUM httpMethod;
    private EBodyFormatENUM bodyFormat;
    private String contentEncoding;

    public AisHttpRequest() {
        headers = new HashMap<>();
        params = new HashMap<>();
        body = new HashMap<>();
        httpMethod = HttpMethodNameENUM.POST;
        bodyFormat = EBodyFormatENUM.FORM_KV;
        contentEncoding = HttpCharacterEncoding.DEFAULT_ENCODING;
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
        if (key.equals(Headers.CONTENT_ENCODING)) {
            this.contentEncoding = value;
        }
    }

    public void addParam(String key, String value) {
        params.put(key, value);
    }

    public void addBody(String key, Object value) {
        body.put(key, value);
    }

    public void addBody(HashMap other) {
        if (other != null) {
            body.putAll(other);
        }
    }

    public String getBodyStr() {
        ArrayList<String> arr = new ArrayList<String>();
        if (bodyFormat.equals(EBodyFormatENUM.FORM_KV)) {
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                if (entry.getValue() == null || entry.getValue().equals("")) {
                    arr.add(SignUtil.uriEncode(entry.getKey(), true));
                } else {
                    arr.add(String.format("%s=%s", SignUtil.uriEncode(entry.getKey(), true),
                            SignUtil.uriEncode(entry.getValue().toString(), true)));
                }
            }
            return SignUtil.mkString(arr.iterator(), '&');
        } else if (bodyFormat.equals(EBodyFormatENUM.RAW_JSON)) {
            JSONObject json = new JSONObject();
            // TODO
            for (Map.Entry<String, Object> entry : body.entrySet()) {
//                json.put(entry.getKey(), entry.getValue());
            }
            return json.toString();
        } else if (bodyFormat.equals(EBodyFormatENUM.FORM_DATA)) {
            // TODO multipart data transfer
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                return (String) entry.getValue();
            }
        }
        return "";
    }

    public void setUri(String url) {
        try {
            this.uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public String getParamStr() {
        StringBuffer buffer = new StringBuffer();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            buffer.append(String.format("%s=%s&", entry.getKey(), entry.getValue()));
        }
        if (buffer.length() > 0) {
            buffer.deleteCharAt(buffer.length() - 1);
        }
        return buffer.toString();
    }


    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public HashMap<String, Object> getBody() {
        return body;
    }

    public void setBody(HashMap<String, Object> body) {
        this.body = body;
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public HttpMethodNameENUM getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethodNameENUM httpMethod) {
        this.httpMethod = httpMethod;
    }

    public EBodyFormatENUM getBodyFormat() {
        return bodyFormat;
    }

    public void setBodyFormat(EBodyFormatENUM bodyFormat) {
        this.bodyFormat = bodyFormat;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }
}
