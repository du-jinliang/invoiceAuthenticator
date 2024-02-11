package cn.wenhe.invoice.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author: DuJinliang
 * @description: 发票excel信息dto
 * @date: 2024/2/7
 */
@Data
public class InvoiceExcelRequestDto {
    @ExcelProperty("序号")
    private String index;

    /**
     * 发票类型
     */
    @ExcelProperty("发票类型")
    private String invoiceType;

    /**
     * 发票代码
     */
    @ExcelProperty("发票代码")
    private String invoiceCode;

    /**
     * 发票号码
     */
    @ExcelProperty("发票号码")
    private String invoiceNo;

    /**
     * 发票日期
     */
    @ExcelProperty("发票日期")
    private String invoiceDate;

    /**
     * 发票金额
     */
    @ExcelProperty("发票金额")
    private String invoiceAmount;

    /**
     * 检验码
     */
    @ExcelProperty("检验码")
    private String checkCode;

    /**
     * 税号
     */
    @ExcelProperty("税号")
    private String taxNo;
}
