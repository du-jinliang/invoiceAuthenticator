package cn.wenhe.invoice.utils.huawei.sdk.util;

import cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils.Client;
import cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils.Request;
import cn.wenhe.invoice.utils.huawei.sdk.vo.SignResult;
import org.apache.http.Header;
import org.apache.http.client.methods.HttpRequestBase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public class SignUtils {
    private static final String SDKSIGNINGSHA256 = "SDK-HMAC-SHA256";

    public SignUtils() {
    }

    public static SignResult sign(Request request, String algorithm) throws Exception {
        SignResult result = new SignResult();
        HttpRequestBase signedRequest = Client.sign(request, algorithm);
        Header[] headers = signedRequest.getAllHeaders();
        Map<String, String> headerMap = new HashMap();
        Header[] var6 = headers;
        int var7 = headers.length;

        for (int var8 = 0; var8 < var7; ++var8) {
            Header header = var6[var8];
            headerMap.put(header.getName(), header.getValue());
        }

        result.setUrl(signedRequest.getURI().toURL());
        result.setHeaders(headerMap);
        return result;
    }

    public static SignResult sign(Request request) throws Exception {
        return sign(request, "SDK-HMAC-SHA256");
    }
}
