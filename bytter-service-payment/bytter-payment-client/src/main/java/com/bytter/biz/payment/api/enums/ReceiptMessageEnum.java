package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum ReceiptMessageEnum implements IMessageEnum  {
	
	/** 此网点日结日期获取失败！ */
	RECEIPT_NET_DAYOVER_MESSAGE_ERROR("10001"),
    /**回单文件错误！只支持pdf和ofd文件类型*/
    RECEIPT_FILE_TYPE_ERROR("R00001"),
    /**文件异常*/
    RECEIPT_FILE_ERROR("R00003"),

    /**文件名称：{0},可能不是账号：{1}的回单，请注意！*/
    RECEIPT_BANKACC_NOT_MATCH("R00002")
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
