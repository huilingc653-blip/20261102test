package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/5/17 10:36
 */
public enum NisProjectMessageEnum implements IMessageEnum {
    /** 项目编码必选唯一*/
    NIS_PROJECT_MESSAGE_CODE_UNIQUE("01001");
    NisProjectMessageEnum(String code) {
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
