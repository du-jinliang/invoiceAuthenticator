package cn.wenhe.invoice.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: DuJinliang
 * @description: 发票excel信息dto
 * @date: 2024/2/7
 */
@Data
@NoArgsConstructor
public class InvoiceExcelResponseDto {

    public InvoiceExcelResponseDto(String index, String invoiceType, String invoiceCode, String invoiceNo, String checkResultInfo, String checkResult, String time) {
        this.index = index;
        this.invoiceType = invoiceType;
        this.invoiceCode = invoiceCode;
        this.invoiceNo = invoiceNo;
        this.checkResultInfo = checkResultInfo;
        this.checkResult = checkResult;
        this.time = time;
    }

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
     * 验真结果
     */
    @ExcelProperty("验真结果")
    private String checkResult;

    /**
     * 验真描述
     */
    @ExcelProperty("验真描述")
    private String checkResultInfo;

    /**
     * 验真时间
     */
    @ExcelProperty("验真时间")
    private String time;
}
