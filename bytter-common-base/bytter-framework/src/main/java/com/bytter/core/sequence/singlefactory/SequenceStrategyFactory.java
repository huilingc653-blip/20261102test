package com.bytter.core.sequence.singlefactory;
import org.springframework.util.StringUtils;
import com.bytter.core.sequence.interfaces.ISequenceEnum;
import com.bytter.core.sequence.strategy.BankSequenceStrategy;
import com.bytter.core.sequence.strategy.BaseSequenceStrategy;
import com.bytter.core.sequence.strategy.NormalSequenceStrategy;
import com.bytter.core.sequence.strategy.SpecialPrefixSequenceStrategy;
import java.util.HashMap;
import java.util.Map;
/**
 * 流水策略获取工厂类
 *
 * @author zhoul
 */
public class SequenceStrategyFactory {
    private static Map<String, BaseSequenceStrategy> strategyCache = new HashMap<>();
    /**
     * 根据流水枚举获取流水生成策略
     *
     * @param sequenceEnum 流水枚举
     * @param specialCode  特殊编码
     * @return 流水生成策略
     */
    public static BaseSequenceStrategy createSequenceStrategy(ISequenceEnum sequenceEnum, String specialCode) {
        String cacheKey = sequenceEnum.toString() + (StringUtils.hasText(specialCode) ? specialCode : "");
        BaseSequenceStrategy sequenceStrategy = strategyCache.get(cacheKey);
        if (sequenceStrategy == null) {
            switch (sequenceEnum.getSequenceTypeEnum()) {
                case NORMAL:
                    sequenceStrategy = new NormalSequenceStrategy(sequenceEnum);
                    break;
                case SPECIAL_PREFIX:
                    sequenceStrategy = new SpecialPrefixSequenceStrategy(sequenceEnum, specialCode);
                    break;
                default:
                    sequenceStrategy = new BankSequenceStrategy(sequenceEnum);
            }
            strategyCache.put(cacheKey, sequenceStrategy);
        }
        return sequenceStrategy;
    }
}
