package cn.wenhe.invoice.checkFp.anna;

import com.alibaba.fastjson.JSONObject;
import cn.wenhe.invoice.checkFp.CheckFpStrategyAbstract;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils.*;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;

/**
 * @author: DuJinliang
 * @description: 聚美智数票据验真策略实现类
 * @url: https://marketplace.huaweicloud.com/product/OFFI894049145675956224#productid=OFFI801756315509215232
 * @date: 2024/2/7
 */
@Component("annaCheckFpStrategyImpl")
public class AnnaCheckFpStrategyImpl extends CheckFpStrategyAbstract {
    @Override
    public String getType() {
        return "anna";
    }

    @Override
    protected CheckFpResponseDto doCheck(CheckFpRequestDto requestDto) {
        Request OkHttpRequest = new Request();
        try {
            String checkCode = requestDto.getCheckCode();
            if (checkCode.length() > 6) {
                checkCode = checkCode.replace(" ", "");
                checkCode = checkCode.substring(checkCode.length() - 6);
            }

            String url = "https://invoicevalidate.apistore.huaweicloud.com/invoice/validate?bhsje=" + requestDto.getInvoiceNo() + "&fpdm=" + requestDto.getInvoiceCode() + "&fphm=" + requestDto.getInvoiceNo() + "&kprq=" + requestDto.getInvoiceDate().replace("-", "") + "&xym=" + checkCode;

            // 认证用的ak和sk硬编码到代码中或者明文存储都有很大的安全风险，建议在配置文件或者环境变量中密文存放，使用时解密，确保安全；
            // 本示例以ak和sk保存在环境变量中为例，运行本示例前请先在本地环境中设置环境变量HUAWEICLOUD_SDK_AK和HUAWEICLOUD_SDK_SK。
            OkHttpRequest.setKey(AnnaSecretProperties.KEY);
            OkHttpRequest.setSecret(AnnaSecretProperties.SECRET);
            OkHttpRequest.setMethod("POST");
            OkHttpRequest.setUrl(url);
            OkHttpRequest.addHeader("Content-Type", "application/json");
        } catch (Exception e) {
            return new CheckFpResponseDto("请求参数错误", false);
        }
        try {
            // Sign the request.
            okhttp3.Request signedRequest = Client.signOkhttp(OkHttpRequest, Constant.SIGNATURE_ALGORITHM_SDK_HMAC_SHA256);
            OkHttpClient client;
            if (Constant.DO_VERIFY) {
                // creat okhttpClient and verify ssl certificate
                HostName.setUrlHostName(OkHttpRequest.getHost());
                client = SSLCipherSuiteUtil.createOkHttpClientWithVerify(Constant.INTERNATIONAL_PROTOCOL);
            } else {
                // creat okhttpClient and do not verify ssl certificate
                client = SSLCipherSuiteUtil.createOkHttpClient(Constant.INTERNATIONAL_PROTOCOL);
            }
            // Send the request.
            Response response = client.newCall(signedRequest).execute();

            ResponseBody body = response.body();

            if(Objects.nonNull(body)) {
                JSONObject jsonObject = JSONObject.parseObject(body.string());
                JSONObject data = jsonObject.getJSONObject("data");
                String resultCode = data.getString("result");
                String message = data.getString("message");

                return new CheckFpResponseDto(message, Objects.equals(resultCode, "1"));
            }
            return new CheckFpResponseDto("没有响应体", false);
        } catch (Exception e) {
            return new CheckFpResponseDto("请求异常", false);
        }
    }
}
