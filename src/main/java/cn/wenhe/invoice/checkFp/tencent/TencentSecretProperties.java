package cn.wenhe.invoice.checkFp.tencent;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 腾讯密钥属性类
 * @date: 2024/2/11
 */
@Component("tencentSecretProperties")
public class TencentSecretProperties implements InitializingBean {

    @Value("${tencent.key}")
    private String key;

    @Value("${tencent.secret}")
    private String secret;

    public static String KEY;

    public static String SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        TencentSecretProperties.KEY = this.key;
        TencentSecretProperties.SECRET = this.secret;
    }
}
