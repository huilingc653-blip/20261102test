package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhouj on 2017/10/16.
 */
public enum PushCateGoryEnum implements IMessageEnum {
    SYSTEM("0"),//系统消息
    APPROVE("1"),//审批
    ;
    private String code;
    PushCateGoryEnum(String code) {
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
