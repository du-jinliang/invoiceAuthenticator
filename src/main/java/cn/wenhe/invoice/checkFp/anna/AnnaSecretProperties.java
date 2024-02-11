package cn.wenhe.invoice.checkFp.anna;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 聚美智数密钥属性类
 * @date: 2024/2/11
 */
@Component("annaSecretProperties")
public class AnnaSecretProperties implements InitializingBean {

    @Value("${anna.key}")
    private String key;

    @Value("${anna.secret}")
    private String secret;

    public static String KEY;

    public static String SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        AnnaSecretProperties.KEY = this.key;
        AnnaSecretProperties.SECRET = this.secret;
    }
}
