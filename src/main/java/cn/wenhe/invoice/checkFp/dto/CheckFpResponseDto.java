package cn.wenhe.invoice.checkFp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: DuJinliang
 * @description: 验真结果返回dto
 * @date: 2024/2/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckFpResponseDto {
    private String resultTip;

    private Boolean resultFlag;
}
