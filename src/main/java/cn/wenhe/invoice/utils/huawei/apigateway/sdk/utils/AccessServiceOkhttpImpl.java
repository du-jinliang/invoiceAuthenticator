package cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils;

import cn.wenhe.invoice.utils.huawei.sdk.auth.signer.Signer;
import cn.wenhe.invoice.utils.huawei.sdk.http.HttpMethodName;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public class AccessServiceOkhttpImpl extends AccessServiceOkhttp {
    private static final String UTF8 = "UTF-8";
    private static final String OPTIONS = "OPTIONS";

    public AccessServiceOkhttpImpl(String ak, String sk) {
        super(ak, sk);
    }

    public AccessServiceOkhttpImpl(String ak, String sk, String messageDigestAlgorithm) {
        super(ak, sk, messageDigestAlgorithm);
    }

    public okhttp3.Request access(String url, Map<String, String> headers, String entity, HttpMethodName httpMethod) throws Exception {
        cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils.Request request = new cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils.Request();
        request.setAppKey(this.ak);
        request.setAppSecrect(this.sk);
        request.setMethod(httpMethod.name());
        request.setUrl(url);
        Iterator var6 = headers.entrySet().iterator();

        while(var6.hasNext()) {
            Map.Entry<String, String> map = (Map.Entry)var6.next();
            request.addHeader((String)map.getKey(), (String)map.getValue());
        }

        request.setBody(entity);
        Signer signer = new Signer(this.messageDigestAlgorithm);
        signer.sign(request);
        return createRequest(url, request.getHeaders(), entity, httpMethod);
    }

    public okhttp3.Request access(String url, Map<String, String> headers, InputStream content, Long contentLength, HttpMethodName httpMethod) throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        int length;
        while((length = content.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        String body = result.toString("UTF-8");
        return this.access(url, headers, body, httpMethod);
    }

    private static okhttp3.Request createRequest(String url, Map<String, String> headers, String body, HttpMethodName httpMethod) throws Exception {
        if (body == null) {
            body = "";
        }

        RequestBody entity = RequestBody.create(MediaType.parse(""), body.getBytes("UTF-8"));
        okhttp3.Request httpRequest;
        if (httpMethod == HttpMethodName.POST) {
            httpRequest = (new okhttp3.Request.Builder()).url(url).post(entity).build();
        } else if (httpMethod == HttpMethodName.PUT) {
            httpRequest = (new okhttp3.Request.Builder()).url(url).put(entity).build();
        } else if (httpMethod == HttpMethodName.PATCH) {
            httpRequest = (new okhttp3.Request.Builder()).url(url).patch(entity).build();
        } else if (httpMethod == HttpMethodName.DELETE) {
            httpRequest = (new okhttp3.Request.Builder()).url(url).delete(entity).build();
        } else if (httpMethod == HttpMethodName.GET) {
            httpRequest = (new okhttp3.Request.Builder()).url(url).get().build();
        } else if (httpMethod == HttpMethodName.HEAD) {
            httpRequest = (new okhttp3.Request.Builder()).url(url).head().build();
        } else {
            if (httpMethod != HttpMethodName.OPTIONS) {
                throw new UnknownHttpMethodException("Unknown HTTP method name: " + httpMethod);
            }

            httpRequest = (new Request.Builder()).url(url).method("OPTIONS", (RequestBody)null).build();
        }

        Map.Entry map;
        for(Iterator var6 = headers.entrySet().iterator(); var6.hasNext(); httpRequest = httpRequest.newBuilder().addHeader((String)map.getKey(), (String)map.getValue()).build()) {
            map = (Map.Entry)var6.next();
        }

        return httpRequest;
    }
}
