package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 境外银行行号信息维护异常枚举
 *
 * @author qinl
 * @date 2019-07-18
 */
public enum SwiftInfoMessageEnum implements IMessageEnum {
    /**
     * 境外银行编号已存在
     */
    TABLE_BTSWIFTINFO_SWIFTCODE_EXIST("01001"),
    /**
     * 机构名称已存在
     */
    TABLE_BTSWIFTINFO_BANKNAME_EXIST("01002"),
    ;
    SwiftInfoMessageEnum(String code) {
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
