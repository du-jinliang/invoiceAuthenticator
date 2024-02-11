package cn.wenhe.invoice.checkFp.piaocc;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 票查查密钥属性类
 * @date: 2024/2/11
 */
@Component("piaoCCSecretProperties")
public class PiaoCCSecretProperties implements InitializingBean {

    @Value("${piaocc.authCode}")
    private String authCode;

    protected static String AUTH_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        PiaoCCSecretProperties.AUTH_CODE = this.authCode;
    }
}
