package cn.wenhe.invoice.checkFp.js;

import com.alibaba.fastjson.JSONArray;
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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;
import java.util.Random;

/**
 * @author: DuJinliang
 * @description: 航天金税票据查验策略实现类
 * @date: 2024/2/7
 */
@Component("jsCheckFpStrategyImpl")
public class JSCheckFpStrategyImpl extends CheckFpStrategyAbstract {
    @Override
    public String getType() {
        return "js";
    }

    @Override
    protected CheckFpResponseDto doCheck(CheckFpRequestDto requestDto) {
        try {
            String checkCode = requestDto.getCheckCode();
            String invoiceNo = requestDto.getInvoiceNo();
            String invoiceCode = requestDto.getInvoiceCode();
            String invoiceType = requestDto.getInvoiceType();
            String invoiceDate = requestDto.getInvoiceDate();
            String invoiceAmount = requestDto.getInvoiceAmount();
            String taxNo = requestDto.getTaxNo();

            String appSecId = JSSecretProperties.KEY;
            String appSecKey = JSSecretProperties.SECRET;
            String enterpriseCode = JSSecretProperties.CODE;
            String appId = JSSecretProperties.APP_ID;
            String version = "V1.0";
            String interfaceCode = "CHECK.SINGLE";
            String dataExchangeId = enterpriseCode + GetNowTime() + (new Random().nextInt(999 - 100) + 100);

            JSONObject root = new JSONObject();
            JSONObject globalInfo = new JSONObject();
            globalInfo.put("appId", appId);
            globalInfo.put("version", version);
            globalInfo.put("interfaceCode", interfaceCode);
            globalInfo.put("enterpriseCode", enterpriseCode);
            globalInfo.put("dataExchangeId", dataExchangeId);
            root.put("globalInfo", globalInfo);

            JSONObject dataObj = new JSONObject();
            if (checkCode.length() > 6)
                checkCode = checkCode.substring(checkCode.length() - 6);
            dataObj.put("checkCode", checkCode);
            dataObj.put("invoiceAmount", invoiceAmount);
            dataObj.put("invoiceCode", invoiceCode);
            dataObj.put("invoiceDate", invoiceDate.replace("-", ""));
            dataObj.put("invoiceNo", invoiceNo);
            dataObj.put("invoiceType", invoiceType);
            dataObj.put("taxNo", taxNo);

            JSONArray dataArray = new JSONArray();
            dataArray.add(dataObj);
            root.put("data", Base64.getEncoder().encodeToString(dataArray.toString().getBytes()));

            JSONObject authorize = new JSONObject();
            String appSec = "POST/recipt/checkFp/getFPInfoByNSRSBH?authorize={\"appSecId\":\"" + appSecId + "\"}&globalInfo={\"appId\":\"" + appId + "\",\"version\":\"" + version + "\",\"interfaceCode\":\"" + interfaceCode + "\",\"enterpriseCode\":\"" + enterpriseCode + "\",\"dataExchangeId\":\"" + dataExchangeId + "\"}&data=" + Base64.getEncoder().encodeToString(dataArray.toString().getBytes());
            authorize.put("appSec", Base64.getEncoder().encodeToString(hmacEncode(appSecKey, appSec.getBytes())));
            authorize.put("appSecId", appSecId);
            root.put("authorize", authorize);

            String params = root.toString();

            RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(params, httpHeaders);

            ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://cy.5dfp.com/recipt/checkFp/getFPInfoByNSRSBH", entity, String.class);

            String body = responseEntity.getBody();
            if(StringUtils.hasText(body)) {
                JSONObject jsonObject2 = (JSONObject) JSONObject.parseObject(new String(Base64.getDecoder().decode(JSONObject.parseObject(body).getString("data").getBytes()))).getJSONArray("invoiceList").get(0);

                JSONObject invoiceInfo = jsonObject2.getJSONObject("invoiceInfo");

                String resultCode = invoiceInfo.getString("resultCode");
                String resultTip = invoiceInfo.getString("resultTip");

                return new CheckFpResponseDto(resultTip, Objects.equals("0001", resultCode));
            }
            return new CheckFpResponseDto("没有响应体", false);
        } catch (Exception e) {
            return new CheckFpResponseDto("请求发生异常", false);
        }
    }


    public static String GetNowTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return now.format(formatter);
    }

    public static byte[] hmacEncode(String key, byte[] input) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha1 = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        hmacSha1.init(secretKeySpec);
        return hmacSha1.doFinal(input);
    }
}
