package cn.wenhe.invoice.checkFp.rh;

import com.alibaba.fastjson.JSONObject;
import cn.wenhe.invoice.checkFp.CheckFpStrategyAbstract;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils.*;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author: DuJinliang
 * @description: 广州税行票据验真策略实现类
 * @url: https://marketplace.huaweicloud.com/product/OFFI739024636407074816#productid=OFFI739024636407074816
 * @date: 2024/2/7
 */
@Deprecated
@Component("rhCheckFpStrategyImpl")
public class RHCheckFpStrategyImpl extends CheckFpStrategyAbstract {
    @Override
    public String getType() {
        return "rh";
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

            String url = "https://fpcy.apistore.huaweicloud.com/mycst.cn/newkp/JXFPAPI/FPCY?BHSJE=" + requestDto.getInvoiceNo() + "&FPDM=" + requestDto.getInvoiceCode() + "&FPHM=" + requestDto.getInvoiceNo() + "&KPRQ=" + requestDto.getInvoiceDate() + "&XYM=" + checkCode;

            // 认证用的ak和sk硬编码到代码中或者明文存储都有很大的安全风险，建议在配置文件或者环境变量中密文存放，使用时解密，确保安全；
            // 本示例以ak和sk保存在环境变量中为例，运行本示例前请先在本地环境中设置环境变量HUAWEICLOUD_SDK_AK和HUAWEICLOUD_SDK_SK。
            OkHttpRequest.setKey(RHSecretProperties.KEY);
            OkHttpRequest.setSecret(RHSecretProperties.SECRET);
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
                String resultCode = jsonObject.getString("resultCode");
                String resultTip = jsonObject.getString("resultTip");

                return new CheckFpResponseDto(resultTip, Objects.equals(resultCode, "0000"));
            }
            return new CheckFpResponseDto("没有响应体", false);
        } catch (Exception e) {
            return new CheckFpResponseDto("请求出现异常", false);
        }
    }
}
