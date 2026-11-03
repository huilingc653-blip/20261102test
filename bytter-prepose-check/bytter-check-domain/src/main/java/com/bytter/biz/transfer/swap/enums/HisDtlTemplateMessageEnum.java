package com.bytter.biz.transfer.swap.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * @author shenc
 * @version 1.0
 * @Title
 * @Description:
 * @date 2018/5/17 10:36
 */
public enum HisDtlTemplateMessageEnum implements IMessageEnum {
    /**单据的状态已经发生改变,请刷新页面后重试*/
    HIS_DTL_TEMPLATE_STATUS_CHANGED("01001");
    HisDtlTemplateMessageEnum(String code) {
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
