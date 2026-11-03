package com.bytter.increment.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 理财产品枚举类
 * @author sench
 * @since 2018/7/12
 */
public enum ChoProductMessageEnum implements IMessageEnum {
    /** 投资理财产品代码必须唯一 */
    CHO_PRODUCT_TYPE_CODE_UNIQUE("01001"),
    /** 赎回金额超过了该产品的可赎回金额 */
    CHO_PRODUCT_REDEEM_REDEEMMONEY_EXCEED_LEFTMONEY("01002"),
    /** 该理财产品有未完成的赎回记录,当前不能进行赎回操作 */
    CHO_PRODUCT_REDEEM_HAVE_UNCOMPLATE_RECORD("01003"),
    /**申请单位未开设-企业活期存款-账号 */
    CHO_INNER_CURRENT_ACCOUNT_IS_NOT("01004"),
    /**申请单位未开设-企业通知存款-账号 */
    CHO_INNER_INFORM_ACCOUNT_IS_NOT("01005"),
    ;
    ChoProductMessageEnum(String code){
        this.code = code;
    }
    private String code;
    public void setCode(String code){
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
