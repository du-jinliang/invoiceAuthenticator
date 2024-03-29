package cn.wenhe.invoice.utils.huawei.sdk.vo;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public class SignResult {
    private Map<String, String> headers = new HashMap();
    private URL url;
    private Map<String, String> parameters = new HashMap();
    private InputStream inputStream;

    public SignResult() {
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public URL getUrl() {
        return this.url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
