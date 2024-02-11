package cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils;

import cn.wenhe.invoice.utils.huawei.sdk.http.HttpMethodName;
import okhttp3.Request;

import java.util.Map;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public abstract class AccessServiceOkhttp {
    protected String ak;
    protected String sk;
    protected String messageDigestAlgorithm = "SDK-HMAC-SHA256";

    public AccessServiceOkhttp(String ak, String sk) {
        this.ak = ak;
        this.sk = sk;
    }

    public AccessServiceOkhttp(String ak, String sk, String messageDigestAlgorithm) {
        this.ak = ak;
        this.sk = sk;
        this.messageDigestAlgorithm = messageDigestAlgorithm;
    }

    public abstract okhttp3.Request access(String var1, Map<String, String> var2, String var3, HttpMethodName var4) throws Exception;

    public okhttp3.Request access(String url, Map<String, String> header, HttpMethodName httpMethod) throws Exception {
        return this.access(url, header, (String)null, httpMethod);
    }

    public okhttp3.Request access(String url, String entity, HttpMethodName httpMethod) throws Exception {
        return this.access(url, (Map)null, entity, httpMethod);
    }

    public Request access(String url, HttpMethodName httpMethod) throws Exception {
        return this.access(url, (Map)null, (String)null, httpMethod);
    }

    public String getAk() {
        return this.ak;
    }

    public void setAk(String ak) {
        this.ak = ak;
    }

    public String getSk() {
        return this.sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }
}

