package cn.infin.invoice.other;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.wenhe.invoice.checkFp.CheckFpStrategy;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.config.http.HttpsClientRequestFactory;
import cn.wenhe.invoice.domain.InvoiceExcelResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author: DuJinliang
 * @description: 冒烟测试
 * @date: 2024/2/6
 */
@SpringBootTest
public class SmokeTest {

    /**
     * 1+1=2
     */
    @Test
    public void one_plus_one_should_equals_two() {
        assertEquals(2, 1+1);
    }
}
