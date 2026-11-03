package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum EVisaTypeMessageEnum implements IMessageEnum{
	
	/**网点不能为空*/
	NIS_EVISATYPE_MESSAGE_NETID_NULL("01001"),
	/**电子回单类型不能为空*/
	NIS_EVISATYPE_MESSAGE_BILLTYPE_NULL("01002"),
	/**网点已维护此电子回单类型,请选择其他类型*/
	NIS_EVISATYPE_MESSAGE_BILLTYPE_UNIQUE("01003");
	EVisaTypeMessageEnum(String code) {
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
