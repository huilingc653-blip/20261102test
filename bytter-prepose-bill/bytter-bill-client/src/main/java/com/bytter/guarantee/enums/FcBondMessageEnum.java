package com.bytter.guarantee.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Title: FcBondMessageEnum Description: TODO
 * 
 * @author luxy
 * @since 2019/4/15 17:21
 */
public enum FcBondMessageEnum implements IMessageEnum {
    /** 保证金占用金额不能大于可用余额! */
    FCBOND_APPLYMONEY_CHECK("01001"),
    /** 保证金占用金额不能大于可用余额{0} */
    FCBOND_APPLYMONEY_CHECKCANUSE("01002"),
    /** 调整后保证金不能大于保证金账户可用余额{0} */
    FCBOND_APPLYMONEY_CHANGECHECK("01003"),
    /** 保证金类型代码已存在 */
    FCBOND_BONDTYPE_NOT_NULL("01004");
    FcBondMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
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
