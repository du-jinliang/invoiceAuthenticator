package cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils;

import cn.wenhe.invoice.utils.huawei.sdk.http.HttpMethodName;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.Map;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public class Client {
    public Client() {
    }

    public static HttpRequestBase sign(Request request, String messageDigestAlgorithm) throws Exception {
        String appKey = request.getKey();
        String appSecrect = request.getSecrect();
        AccessService accessService = new AccessServiceImpl(appKey, appSecrect, messageDigestAlgorithm);
        String url = request.getUrl();
        String body = request.getBody();
        if (body == null) {
            body = "";
        }

        Map<String, String> headers = request.getHeaders();
        switch (request.getMethod()) {
            case GET:
                return accessService.access(url, headers, "", HttpMethodName.GET);
            case POST:
                return accessService.access(url, headers, body, HttpMethodName.POST);
            case PUT:
                return accessService.access(url, headers, body, HttpMethodName.PUT);
            case PATCH:
                return accessService.access(url, headers, body, HttpMethodName.PATCH);
            case DELETE:
                return accessService.access(url, headers, "", HttpMethodName.DELETE);
            case HEAD:
                return accessService.access(url, headers, "", HttpMethodName.HEAD);
            case OPTIONS:
                return accessService.access(url, headers, "", HttpMethodName.OPTIONS);
            default:
                throw new IllegalArgumentException(String.format("unsupported method:%s", request.getMethod().name()));
        }
    }

    public static synchronized HttpRequestBase sign(Request request) throws Exception {
        return sign(request, "SDK-HMAC-SHA256");
    }

    public static okhttp3.Request signOkhttp(Request request, String messageDigestAlgorithm) throws Exception {
        ParamsEntity pe = new ParamsEntity(request.getKey(), request.getSecrect(), request.getUrl(), request.getBody(), messageDigestAlgorithm);
        return okhttpRequest(request.getMethod(), request.getHeaders(), pe);
    }

    public static okhttp3.Request signOkhttp(Request request) throws Exception {
        return okhttpRequest(request.getMethod(), request.getKey(), request.getSecrect(), request.getUrl(), request.getHeaders(), request.getBody());
    }

    public static HttpRequestBase put(String ak, String sk, String requestUrl, Map<String, String> headers, String putBody) throws Exception {
        if (putBody == null) {
            putBody = "";
        }

        AccessService accessService = new AccessServiceImpl(ak, sk);
        return accessService.access(requestUrl, headers, putBody, HttpMethodName.PUT);
    }

    public static HttpRequestBase patch(String ak, String sk, String requestUrl, Map<String, String> headers, String body) throws Exception {
        if (body == null) {
            body = "";
        }

        AccessService accessService = new AccessServiceImpl(ak, sk);
        return accessService.access(requestUrl, headers, body, HttpMethodName.PATCH);
    }

    public static HttpRequestBase delete(String ak, String sk, String requestUrl, Map<String, String> headers) throws Exception {
        AccessService accessService = new AccessServiceImpl(ak, sk);
        return accessService.access(requestUrl, headers, HttpMethodName.DELETE);
    }

    public static HttpRequestBase get(String ak, String sk, String requestUrl, Map<String, String> headers) throws Exception {
        AccessService accessService = new AccessServiceImpl(ak, sk);
        return accessService.access(requestUrl, headers, HttpMethodName.GET);
    }

    public static HttpRequestBase post(String ak, String sk, String requestUrl, Map<String, String> headers, String postbody) throws Exception {
        if (postbody == null) {
            postbody = "";
        }

        AccessService accessService = new AccessServiceImpl(ak, sk);
        return accessService.access(requestUrl, headers, postbody, HttpMethodName.POST);
    }

    public static HttpRequestBase head(String ak, String sk, String requestUrl, Map<String, String> headers) throws Exception {
        AccessService accessService = new AccessServiceImpl(ak, sk);
        return accessService.access(requestUrl, headers, HttpMethodName.HEAD);
    }

    public static HttpRequestBase options(String ak, String sk, String requestUrl, Map<String, String> headers) throws Exception {
        AccessService accessService = new AccessServiceImpl(ak, sk);
        return accessService.access(requestUrl, headers, HttpMethodName.OPTIONS);
    }

    private static okhttp3.Request okhttpRequest(HttpMethodName httpMethod, Map<String, String> headers, ParamsEntity pe) throws Exception {
        switch (httpMethod) {
            case GET:
            case HEAD:
            case OPTIONS:
                pe.body = "";
            case POST:
            case PUT:
            case PATCH:
            case DELETE:
                if (pe.body == null) {
                    pe.body = "";
                }

                AccessServiceOkhttp accessServiceOkhttp = new AccessServiceOkhttpImpl(pe.appKey, pe.secretKeyk, pe.algorithm);
                return accessServiceOkhttp.access(pe.requestUrl, headers, pe.body, httpMethod);
            default:
                throw new UnknownHttpMethodException("Unknown HTTP method name: " + httpMethod);
        }
    }

    public static okhttp3.Request okhttpRequest(HttpMethodName httpMethod, String ak, String sk, String requestUrl, Map<String, String> headers, String body) throws Exception {
        ParamsEntity pe = new ParamsEntity(ak, sk, requestUrl, body, "SDK-HMAC-SHA256");
        return okhttpRequest(httpMethod, headers, pe);
    }

    private static class ParamsEntity {
        String appKey;
        String secretKeyk;
        String requestUrl;
        String body;
        String algorithm;

        public ParamsEntity(String ak, String sk, String url, String bd, String algo) {
            this.appKey = ak;
            this.secretKeyk = sk;
            this.requestUrl = url;
            this.body = bd;
            this.algorithm = algo;
        }
    }
}

