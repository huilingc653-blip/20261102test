package com.bytter.biz.bdlink.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum ReceiptMessageEnum implements IMessageEnum  {
	
	/** 此网点日结日期获取失败！ */
	RECEIPT_NET_DAYOVER_MESSAGE_ERROR("10001"),
    ;
	
	ReceiptMessageEnum(String code) {
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
