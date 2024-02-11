package cn.wenhe.invoice.checkFp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

/**
 * @author: DuJinliang
 * @description: 票据查验请求参数dto
 * @date: 2024/2/7
 */
@Data
@NoArgsConstructor
public class CheckFpRequestDto {

    public CheckFpRequestDto(String invoiceCode, String invoiceNo, String invoiceDate, String invoiceAmount, String checkCode, String taxNo) {
        this.invoiceCode = invoiceCode;
        this.invoiceNo = invoiceNo;
        this.invoiceDate = invoiceDate;
        this.invoiceAmount = invoiceAmount;
        this.checkCode = checkCode;
        this.taxNo = taxNo;
    }

    /**
     * 发票代码
     */
    private String invoiceCode;

    /**
     * 发票号码
     */
    private String invoiceNo;

    /**
     * 发票日期
     */
    private String invoiceDate;

    /**
     * 发票金额
     */
    private String invoiceAmount;

    /**
     * 检验码
     */
    private String checkCode;

    /**
     * 发票类型
     */
    private String invoiceType;

    /**
     * 税号
     */
    private String taxNo;

    public String getInvoiceCode() {
        if (StringUtils.hasText(invoiceCode)) {
            return invoiceCode;
        }
        return "";
    }

    public String getInvoiceNo() {
        if (StringUtils.hasText(invoiceNo)) {
            return invoiceNo;
        }
        return "";
    }

    public String getInvoiceDate() {
        if (StringUtils.hasText(invoiceDate)) {
            return invoiceDate;
        }
        return "";
    }

    public String getInvoiceAmount() {
        if (StringUtils.hasText(invoiceAmount)) {
            return invoiceAmount;
        }
        return "";
    }

    public String getCheckCode() {
        if (StringUtils.hasText(checkCode)) {
            return checkCode;
        }
        return "";
    }

    public String getInvoiceType() {
        if (StringUtils.hasText(invoiceType)) {
            return invoiceType;
        }
        return "";
    }

    public String getTaxNo() {
        if (StringUtils.hasText(taxNo)) {
            return taxNo;
        }
        return "";
    }

}
