package cn.wenhe.invoice.linstener;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import cn.wenhe.invoice.checkFp.CheckFpStrategy;
import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;
import cn.wenhe.invoice.domain.InvoiceExcelRequestDto;
import cn.wenhe.invoice.domain.InvoiceExcelResponseDto;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


/**
 * @author: DuJinliang
 * @description: 票据解析监听器
 * @date: 2024/2/7
 */
@Slf4j
public class InvoiceAnalyseListener implements ReadListener<InvoiceExcelRequestDto> {

    private CheckFpStrategy checkFpStrategy;

    private List<InvoiceExcelResponseDto> responseDtoList = new ArrayList<>();

    String[][] keyValuePairs = {
            {"01", "增值税专用发票"},
            {"02", "货物运输业增值税专用发票"},
            {"03", "机动车销售统一发票"},
            {"04", "增值税普通发票"},
            {"10", "增值税普通发票（电子）"},
            {"11", "增值税普通发票（卷式）"},
            {"14", "通行费发票"},
            {"15", "二手车发票"},
            {"16", "区块链电子发票"},
            {"21", "全电发票（专用发票）"},
            {"22", "全电发票（普通发票）"},
    };

    Map<String, String> invoiceTypeMap = Arrays.stream(keyValuePairs)
            .collect(Collectors.toMap(
                    pair -> pair[0],
                    pair -> pair[1]
            ));

    public InvoiceAnalyseListener(CheckFpStrategy checkFpStrategy) {
        this.checkFpStrategy = checkFpStrategy;
    }

    @Override
    public void invoke(InvoiceExcelRequestDto invoiceExcelDto, AnalysisContext analysisContext) {
        CheckFpRequestDto requestDto = new CheckFpRequestDto(
                invoiceExcelDto.getInvoiceCode(),
                invoiceExcelDto.getInvoiceNo(),
                invoiceExcelDto.getInvoiceDate(),
                invoiceExcelDto.getInvoiceAmount(),
                invoiceExcelDto.getCheckCode(),
                invoiceExcelDto.getTaxNo()
        );

        long start = System.currentTimeMillis();
        CheckFpResponseDto checkResult = checkFpStrategy.checkFp(requestDto);
        long end = System.currentTimeMillis();
        InvoiceExcelResponseDto responseDto = new InvoiceExcelResponseDto(
                invoiceExcelDto.getIndex(),
                invoiceTypeMap.getOrDefault(requestDto.getInvoiceType(), ""),
                requestDto.getInvoiceCode(),
                requestDto.getInvoiceNo(),
                checkResult.getResultTip(),
                checkResult.getResultFlag() ? "验真成功" : "验真失败",
                end - start + "ms"
        );
        responseDtoList.add(responseDto);
        log.info("一张票据验真完成，发票号码：{}，验真结果：{}", requestDto.getInvoiceNo(), checkResult.getResultFlag() ? "验真成功" : "验真失败");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        EasyExcel.write("票据测试结果.xlsx", InvoiceExcelResponseDto.class)
                .sheet("票据测试")
                .doWrite(responseDtoList);

        double sum = responseDtoList.stream()
                .mapToLong(dto -> Long.parseLong(dto.getTime().replace("ms", "")))
                .sum();

        double average = responseDtoList.isEmpty() ? 0 : sum / responseDtoList.size();

        responseDtoList.clear();
        log.info("查验结束，已生成测试文件，平均用时：{}", average);
    }
}
