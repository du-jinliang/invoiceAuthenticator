package cn.wenhe.invoice.checkFp.za;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 中安未来密钥属性类
 * @date: 2024/2/11
 */
@Component("zaSecretProperties")
public class ZASecretProperties implements InitializingBean {

    @Value("${za.appCode}")
    private String appCode;

    protected static String APP_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        ZASecretProperties.APP_CODE = this.appCode;
    }
}
