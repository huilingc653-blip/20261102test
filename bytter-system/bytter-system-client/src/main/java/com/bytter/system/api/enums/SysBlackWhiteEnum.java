package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 黑白名单消息提醒
 *
 * @author caojy
 */
public enum SysBlackWhiteEnum implements IMessageEnum {
    /**
     * 重复
     */
    SYS_BLACK_WHITE_REPEAT("03500"),
    ;
    private String code;
    SysBlackWhiteEnum(String code) {
        this.code = code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
