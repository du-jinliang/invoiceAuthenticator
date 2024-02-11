package cn.wenhe.invoice.checkFp.rh;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 广州税行密钥属性类
 * @date: 2024/2/11
 */
@Component("rhSecretProperties")
public class RHSecretProperties implements InitializingBean {

    @Value("${rh.key}")
    private String key;

    @Value("${rh.secret}")
    private String secret;

    public static String KEY;

    public static String SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        RHSecretProperties.KEY = this.key;
        RHSecretProperties.SECRET = this.secret;
    }
}
