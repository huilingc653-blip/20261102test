package com.bytter.biz.receipt.enums;

/**
 *
 */
public enum IsFromEnum {
	//0-银企直联，1-OCR识别，默认0-银企直联
    FROM_BANK_LINK("0", "银企直联"),
    FROM_OCR("1", "人脸图像已通过复核"),
	// /**借贷标志 0-收入 1-支出
	CREDIT_FLAG_IN("0", "收入"),
	CREDIT_FLAG_OUT("1", "支出");
   
    private String value;
    private String name;
    IsFromEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
