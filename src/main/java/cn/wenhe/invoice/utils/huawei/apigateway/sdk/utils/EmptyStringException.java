package cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public class EmptyStringException extends RuntimeException {
    private static final long serialVersionUID = 4312820110480855928L;
    private String retCd;
    private String msgDes;

    public EmptyStringException() {
    }

    public EmptyStringException(String message) {
        super(message);
        this.msgDes = message;
    }
}
