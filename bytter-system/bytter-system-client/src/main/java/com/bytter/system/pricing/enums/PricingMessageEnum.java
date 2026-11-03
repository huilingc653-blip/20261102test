package com.bytter.system.pricing.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Title: PricingMessageEnum
 * Description: 定价管理模块消息提示
 *
 * @author luxy
 * @since 2019/5/24 14:50
 */
public enum PricingMessageEnum implements IMessageEnum {
    /**
     * {0}生效日期不能小于上一次维护的生效日期{1}
     */
    PRICING_DEPOSITRATE_EFFECTTIMELELAST("01003"),
    /**
     * {0}在{1}已存在数据
     */
    PRICING_DEPOSITRATE_DATAREPEAT("01004"),
    /**
     * 存款利率正在引用利率类型
     */
    PRICING_DEPOSITRATETYPE_ATUSE("01005");
    PricingMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
