package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhouj on 2017/10/14.
 */
public enum PushMessageEnum implements IMessageEnum {
    NEED_APPROVE("0"),//审批 需要审批
    PASS("1"),//审批 审批通过
    BACK("3"),//审批 审批打回上一级
    BACK_TOUCHING("4")//审批 审批打回制单处
    ;
    private String code;
    PushMessageEnum(String code) {
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
