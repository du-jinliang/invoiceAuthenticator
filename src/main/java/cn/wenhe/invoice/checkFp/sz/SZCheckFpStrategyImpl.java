package cn.wenhe.invoice.checkFp.sz;

import com.alibaba.fastjson.JSONObject;
import cn.wenhe.invoice.checkFp.CheckFpStrategyAbstract;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import org.apache.http.HttpResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: DuJinliang
 * @description: 深智票据验真策略类
 * @url: https://market.aliyun.com/products/57000002/cmapi00060485.html
 * @date: 2024/2/8
 */
@Component("szCheckFpStrategyImpl")
public class SZCheckFpStrategyImpl extends CheckFpStrategyAbstract {
    @Override
    public String getType() {
        return "sz";
    }

    @Override
    protected CheckFpResponseDto doCheck(CheckFpRequestDto requestDto) {
        try {
            String checkCode = requestDto.getCheckCode();
            if (checkCode.length() > 6) {
                checkCode = checkCode.replace(" ", "");
                checkCode = checkCode.substring(checkCode.length() - 6);
            }

            String host = "https://verifyvat.market.alicloudapi.com";
            String path = "/data/verify_vat_invoice";
            String method = "POST";
            String appcode = SZSecretProperties.APP_CODE;

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "APPCODE " + appcode);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
            if(StringUtils.hasText(requestDto.getInvoiceCode())) {
                requestBody.add("note_code", requestDto.getInvoiceCode());
            }
            if(StringUtils.hasText(requestDto.getInvoiceNo())) {
                requestBody.add("note_no", requestDto.getInvoiceNo());
            }
            if (StringUtils.hasText(requestDto.getInvoiceDate())) {
                requestBody.add("billing_date", requestDto.getInvoiceDate());
            }
            if(StringUtils.hasText(requestDto.getInvoiceAmount())) {
                requestBody.add("total_cost", requestDto.getInvoiceAmount());
            }
            if(StringUtils.hasText(checkCode)) {
                requestBody.add("check_code", checkCode);
            }

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

            RestTemplate restTemplate1 = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate1.exchange(host + path, HttpMethod.POST, requestEntity, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String body = responseEntity.getBody();

                if(Objects.nonNull(body)) {
                    JSONObject resBody = JSONObject.parseObject(responseEntity.getBody());
                    String resultCode = resBody.getString("status");
                    String reason = resBody.getString("reason");

                    return new CheckFpResponseDto(reason, Objects.equals(resultCode, "OK"));
                }
                return new CheckFpResponseDto("没有响应体", false);
            } else {
                return new CheckFpResponseDto("请求出现异常", false);
            }
        }catch (Exception e) {
            String reason;
            try {
                reason = JSONObject.parseObject(e.getMessage().substring(e.getMessage().indexOf("{"), e.getMessage().indexOf("}") + 1)).getString("reason");
            }catch (Exception e1) {
                reason = e.getMessage();
            }
            return new CheckFpResponseDto(reason, false);
        }
    }
}
