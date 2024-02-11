package cn.wenhe.invoice.checkFp;

import cn.wenhe.invoice.checkFp.dto.CheckFpRequestDto;
import cn.wenhe.invoice.checkFp.dto.CheckFpResponseDto;

/**
 * @author: DuJinliang
 * @description: 票据查验策略类
 * @date: 2024/2/7
 */
public interface CheckFpStrategy {
    CheckFpResponseDto checkFp(CheckFpRequestDto requestDto);

    String getType();
}
