package cn.wenhe.invoice.utils.huawei.apigateway.sdk.utils;

/**
 * @author: DuJinliang
 * @description:
 * @date: 2024/2/8
 */
public class UnknownHttpMethodException extends RuntimeException {
    private static final long serialVersionUID = 4L;
    private String retCd;
    private String msgDes;

    public UnknownHttpMethodException() {
    }

    public UnknownHttpMethodException(String message) {
        super(message);
        this.msgDes = message;
    }

    public UnknownHttpMethodException(String retCd, String msgDes) {
        this.retCd = retCd;
        this.msgDes = msgDes;
    }

    public String getRetCd() {
        return this.retCd;
    }

    public String getMsgDes() {
        return this.msgDes;
    }
}
