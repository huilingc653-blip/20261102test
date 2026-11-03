package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * Created by zhoul on 2017/9/27.
 */
public enum DualMessageEnum implements IMessageEnum {
    /**
     * 操作成功，请等待另一位管理员审批！
     */
    SYS_DUAL_SUCCESS("01001"),
    /**
     * 双管理员流程不存在！
     */
    SYS_DUAL_FLOW_NOT_EXIST("00100"),
    /**
     * 该单位未分配2名管理员
     */
    SYS_DUAL_CURRENT_CORP_NOT_ASSIGN_TWO_MANAGER("0001"),
    /**
     * 该流程正在审批中，请勿重复提交！
     */
    SYS_DUAL_CURRENT_FLOW_IS_APPROVE("0012"),
    ;
    DualMessageEnum(String code) {
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
