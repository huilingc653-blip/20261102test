package com.bytter.credential.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum LcMessageEnum implements IMessageEnum{
	/**
	 * 国际结算 01 开证  02 收证
	 */
	
	/**信用证号已存在*/
	LC_RECEIVE_CREDIT_NO("02001"),
	/**该收单登记票据有抵押未结束的业务！*/
	LC_RECEIVE_ARRIVAL_NO_RESET("02002"),
	/**信用证号已存在*/
	LC_CREATE_CREDIT_NO("01001"),
	DZ_EXIST("1001","来单金额或付汇金额不能小于0")
	;
	
	LcMessageEnum(String code) {
		this.code = code;
	}
	
	LcMessageEnum(String code,String name) {
		this.code = code;
	}
	private String code;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
