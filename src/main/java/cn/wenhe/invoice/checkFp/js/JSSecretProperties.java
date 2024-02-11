package cn.wenhe.invoice.checkFp.js;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 航天金税密钥属性类
 * @date: 2024/2/11
 */
@Component("jsSecretProperties")
public class JSSecretProperties implements InitializingBean {

    @Value("${js.key}")
    private String key;

    @Value("${js.secret}")
    private String secret;

    @Value("${js.code}")
    private String code;

    @Value("${js.appId}")
    private String appId;

    protected static String KEY;

    protected static String SECRET;

    protected static String CODE;

    protected static String APP_ID;

    @Override
    public void afterPropertiesSet() throws Exception {
        JSSecretProperties.KEY = this.key;
        JSSecretProperties.SECRET = this.secret;
        JSSecretProperties.CODE = this.code;
        JSSecretProperties.APP_ID = this.appId;
    }
}
