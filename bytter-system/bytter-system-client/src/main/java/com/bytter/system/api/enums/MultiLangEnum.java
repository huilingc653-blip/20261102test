package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/5/18.
 */
public enum MultiLangEnum implements IMessageEnum {
    /**
     * 字典代码已存在
     */
    SYS_MULTILANG_CODE_EXISTS("03001"),
    ;
    MultiLangEnum(String code) {
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
