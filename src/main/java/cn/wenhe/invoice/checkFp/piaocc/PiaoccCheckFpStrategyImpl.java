package cn.wenhe.invoice.checkFp.piaocc;

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

import java.util.Objects;

/**
 * @author: DuJinliang
 * @description: 票查查票据查验
 * @url: https://www.piaocc.com/path/doc.htm
 * @date: 2024/2/7
 */
@Component("piaoccCheckFpStrategyImpl")
public class PiaoccCheckFpStrategyImpl extends CheckFpStrategyAbstract {
    @Override
    public String getType() {
        return "piaocc";
    }

    @Override
    protected CheckFpResponseDto doCheck(CheckFpRequestDto requestDto) {
        try {
            String checkCode = requestDto.getCheckCode();
            if (checkCode.length() > 6) {
                checkCode = checkCode.replace(" ", "");
                checkCode = checkCode.substring(checkCode.length() - 6);
            }
            String url = "https://api.piaocc.com/rest/v_3/common_check.html?authCode=" + PiaoCCSecretProperties.AUTH_CODE + "&invoiceCode="+ requestDto.getInvoiceCode() +"&invoiceNo="+  requestDto.getInvoiceNo() +"&invoiceDate=" + requestDto.getInvoiceDate().replace("-", "") + "&invoiceAmt="+ requestDto.getInvoiceAmount() +"&checkCode="+ checkCode +"&useCache=N";

            RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());

            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            String body = responseEntity.getBody();

            if(StringUtils.hasText(body)) {
                JSONObject jsonObject = JSONObject.parseObject(body);

                String code = jsonObject.getString("code");
                String message = jsonObject.getString("message");

                return new CheckFpResponseDto(message, Objects.equals(code, "200"));
            }
            return new CheckFpResponseDto("没有响应体", false);
        }catch (Exception e) {
            return new CheckFpResponseDto("请求出现异常", false);
        }
    }
}
