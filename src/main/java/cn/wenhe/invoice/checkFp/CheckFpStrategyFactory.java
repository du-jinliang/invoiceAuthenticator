package cn.wenhe.invoice.checkFp;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: DuJinliang
 * @description: 发票查验策略工厂类
 * @date: 2024/2/11
 */
@Component("checkFpStrategyFactory")
public class CheckFpStrategyFactory {
    @Resource
    private List<CheckFpStrategy> checkFpStrategyList;

    public CheckFpStrategy getCheckFpStrategy(String type) {
        for (CheckFpStrategy checkFpStrategy : checkFpStrategyList) {
            if (checkFpStrategy.getType().equals(type)) {
                return checkFpStrategy;
            }
        }
        return null;
    }
}
