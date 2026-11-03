package com.bytter.biz.fundwarn.spi.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 预警异常提醒信息
 * @author zhengg
 *
 */
public enum FundWarnMessageEnum implements IMessageEnum {
    /** 操作成功*/
    BLANDVOU_MESSAGE_SUCCESS("92000"),
    /** 操作失败*/
    BLANDVOU_MESSAGE_ERROR("92001"),;
	
    private String code;
    FundWarnMessageEnum(String code) {
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
