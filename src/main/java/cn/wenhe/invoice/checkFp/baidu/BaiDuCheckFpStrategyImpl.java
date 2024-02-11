package cn.wenhe.invoice.checkFp.baidu;

import com.alibaba.fastjson.JSONObject;
import cn.wenhe.invoice.checkFp.CheckFpStrategyAbstract;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import cn.wenhe.invoice.config.http.HttpsClientRequestFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: DuJinliang
 * @description: 百度票据查验策略实现类
 * @url: https://console.bce.baidu.com/ai/?fromai=1#/ai/ocr/purchaseAll/index~apiId=807&type=package
 * @date: 2024/2/7
 */
@Component("baiduCheckFpStrategyImpl")
public class BaiDuCheckFpStrategyImpl extends CheckFpStrategyAbstract {

    String[][] keyValuePairs = {
            {"01", "special_vat_invoice"},
            {"02", "special_freight_transport_invoice"},
            {"03", "motor_vehicle_invoice"},
            {"04", "normal_invoice"},
            {"10", "elec_normal_invoice"},
            {"11", "roll_normal_invoice"},
            {"14", "toll_elec_normal_invoice"},
            {"15", "used_vehicle_invoice"},
            {"16", "blockchain_invoice"},
            {"21", "elec_invoice_special"},
            {"22", "elec_invoice_normal"},
    };

    Map<String, String> invoiceTypeMap = Arrays.stream(keyValuePairs)
            .collect(Collectors.toMap(
                    pair -> pair[0],
                    pair -> pair[1]
            ));

    @Override
    public String getType() {
        return "baidu";
    }

    @Override
    protected CheckFpResponseDto doCheck(CheckFpRequestDto requestDto) {
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            String checkCode = requestDto.getCheckCode();
            if (checkCode.length() > 6) {
                checkCode = checkCode.replace(" ", "");
                checkCode = checkCode.substring(checkCode.length() - 6);
            }

            String requestBody = "invoice_code="+ requestDto.getInvoiceCode() +"&invoice_num="+requestDto.getInvoiceNo()+"&invoice_date="+requestDto.getInvoiceDate().replace("-", "")+"&invoice_type="+invoiceTypeMap.get(requestDto.getInvoiceType())+"&check_code="+checkCode+"&total_amount="+requestDto.getInvoiceAmount();

            HttpEntity<String> httpEntity = new HttpEntity<>(
                    requestBody,
                    httpHeaders
            );

            RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
            ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://aip.baidubce.com/rest/2.0/ocr/v1/vat_invoice_verification?access_token=" + getAccessToken(), httpEntity, String.class);

            String body = responseEntity.getBody();

            if(StringUtils.hasText(body)) {
                JSONObject jsonObject = JSONObject.parseObject(body);
                String verifyResult = jsonObject.getString("VerifyResult");
                String verifyMessage = jsonObject.getString("VerifyMessage");

                return new CheckFpResponseDto(verifyMessage, Objects.equals("0001", verifyResult));
            }
            return new CheckFpResponseDto("没有响应体", false);
        }catch (Exception e) {
            return new CheckFpResponseDto("请求出现异常", false);
        }
    }


    /**
     * 从用户的AK，SK生成鉴权签名（Access Token）
     *
     * @return 鉴权签名（Access Token）
     * @throws IOException IO异常
     */
    static String getAccessToken() throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> httpEntity = new HttpEntity<>(
                "grant_type=client_credentials&client_id=" + BaiduSecretProperties.KEY
                        + "&client_secret=" + BaiduSecretProperties.SECRET,
                httpHeaders
        );

        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://aip.baidubce.com/oauth/2.0/token", httpEntity, String.class);

        String body = responseEntity.getBody();

        JSONObject jsonObject = JSONObject.parseObject(body);

        return jsonObject.getString("access_token");
    }
}
