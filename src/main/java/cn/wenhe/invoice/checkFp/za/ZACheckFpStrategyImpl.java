package cn.wenhe.invoice.checkFp.za;

import com.alibaba.fastjson.JSONObject;
import cn.wenhe.invoice.checkFp.CheckFpStrategyAbstract;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import org.apache.http.HttpResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: DuJinliang
 * @description: 中安未来票据验真策略实现类
 * @url: https://market.aliyun.com/products/57000002/cmapi028399.html
 * @date: 2024/2/8
 */
@Component("zaCheckFpStrategyImpl")
public class ZACheckFpStrategyImpl extends CheckFpStrategyAbstract {
    @Override
    public String getType() {
        return "za";
    }

    @Override
    protected CheckFpResponseDto doCheck(CheckFpRequestDto requestDto) {
        try {
            String checkCode = requestDto.getCheckCode();
            if (checkCode.length() > 6) {
                checkCode = checkCode.replace(" ", "");
                checkCode = checkCode.substring(checkCode.length() - 6);
            }

            String host = "http://verinvoice.sinosecu.com.cn";
            String path = "/verapi/verInvoice.do";
            String appcode = ZASecretProperties.APP_CODE;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "APPCODE " + appcode);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            requestBody.add("invoiceCode", requestDto.getInvoiceCode());
            requestBody.add("invoiceNumber", requestDto.getInvoiceNo());
            requestBody.add("billingDate", requestDto.getInvoiceDate());
            requestBody.add("totalAmount", requestDto.getInvoiceAmount());
            requestBody.add("checkCode", checkCode);
            requestBody.add("salesTaxNo", requestDto.getTaxNo());

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate1 = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate1.exchange(host + path, HttpMethod.POST, requestEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = responseEntity.getBody();

                if(Objects.nonNull(body)) {
                    JSONObject message = JSONObject.parseObject(responseEntity.getBody()).getJSONObject("message");
                    String resultCode = message.getString("status");
                    String value = message.getString("value");

                    return new CheckFpResponseDto(value, Objects.equals(resultCode, "2"));
                }
                return new CheckFpResponseDto("没有响应体", false);
            } else {
                return new CheckFpResponseDto("请求出现异常", false);
            }
        }catch (Exception e) {
            return new CheckFpResponseDto(e.getMessage(), false);
        }
    }
}
