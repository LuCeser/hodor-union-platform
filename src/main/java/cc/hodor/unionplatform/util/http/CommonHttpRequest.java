package cc.hodor.unionplatform.util.http;

import cc.hodor.unionplatform.util.SignUtil;
import com.alibaba.fastjson.JSONObject;
import io.undertow.util.Headers;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Setter
@Getter
public class CommonHttpRequest {

    private HashMap<String, String> headers;
    private HashMap<String, String> params;
    private HashMap<String, Object> body;
    private URI uri;
    private EBodyFormatENUM bodyFormat;
    private String contentEncoding;

    public CommonHttpRequest() {
        headers = new HashMap<>();
        params = new HashMap<>();
        body = new HashMap<>();
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
            log.error("", e);
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

}
