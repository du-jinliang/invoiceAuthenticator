package cn.wenhe.invoice.controller;

import cn.wenhe.invoice.checkFp.CheckFpStrategyFactory;
import com.alibaba.excel.EasyExcel;
import cn.wenhe.invoice.checkFp.CheckFpStrategy;
import cn.wenhe.invoice.domain.InvoiceExcelRequestDto;
import cn.wenhe.invoice.linstener.InvoiceAnalyseListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * @author: DuJinliang
 * @description: 票据controller
 * @date: 2024/2/7
 */
@Slf4j
@RestController
public class InvoiceController {

    private final CheckFpStrategyFactory checkFpStrategyFactory;

    public InvoiceController(
            @Qualifier("checkFpStrategyFactory")
            CheckFpStrategyFactory checkFpStrategyFactory
    ) {
        this.checkFpStrategyFactory = checkFpStrategyFactory;
    }

    @PostMapping("/upload")
    public String upload(
            @RequestParam("type") String type,
            @RequestParam("file") MultipartFile file
    ) {
        CheckFpStrategy checkFpStrategy = checkFpStrategyFactory.getCheckFpStrategy(type);

        return Optional.ofNullable(checkFpStrategy)
                .map(checkFp -> {
                    try {
                        EasyExcel.read(file.getInputStream(), InvoiceExcelRequestDto.class, new InvoiceAnalyseListener(checkFpStrategy)).sheet().doRead();
                    } catch (IOException e) {
                        log.error("读取票据excel出现异常: {}", e.getMessage());
                        return "error";
                    }
                    return "success";
                })
                .orElseGet(() -> {
                    log.error("读取票据excel出现异常: {}", "未找到对应票据类型");
                    return "error";
                });
    }
}
