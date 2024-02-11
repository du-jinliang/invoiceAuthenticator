package cn.wenhe.invoice.checkFp.baidu;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 百度密钥属性类
 * @date: 2024/2/11
 */
@Component("baiduSecretProperties")
public class BaiduSecretProperties implements InitializingBean {

    @Value("${baidu.key}")
    private String key;

    @Value("${baidu.secret}")
    private String secret;

    public static String KEY;

    public static String SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        BaiduSecretProperties.KEY = this.key;
        BaiduSecretProperties.SECRET = this.secret;
    }
}
