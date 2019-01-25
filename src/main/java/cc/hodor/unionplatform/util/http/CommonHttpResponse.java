package cc.hodor.unionplatform.util.http;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

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
 *   zhanglu               2019/1/16-14:51
 *
 ****************************************************************************************/
public class CommonHttpResponse {

    private Map<String, String> header;
    private byte[] body;
    private String charset;
    private int status;

    public CommonHttpResponse() {
        status = 0;
        charset = "UTF-8";
        header = new TreeMap<>();
    }

    public String getBodyStr() {
        if (body == null) {
            return "";
        }
        try {
            return new String(body, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(body);
        }
    }

    public void setHeader(Map<String, String> header) {
        header.putAll(header);
    }


    public void addHeader(String headerName, String headerValue) {
        header.put(headerName, headerValue);
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getHeader(String name) {
        return header.get(name);
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
