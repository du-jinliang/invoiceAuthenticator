package cn.wenhe.invoice.checkFp.sz;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author: DuJinliang
 * @description: 深智恒际密钥属性类
 * @date: 2024/2/11
 */
@Component("szSecretProperties")
public class SZSecretProperties implements InitializingBean {

    @Value("${sz.appCode}")
    private String appCode;

    protected static String APP_CODE;

    @Override
    public void afterPropertiesSet() throws Exception {
        SZSecretProperties.APP_CODE = this.appCode;
    }
}
