package com.bytter.supply.enums;

/**
 * @author ztd
 * @description TODO
 * @since 2026/7/1
 */
public enum VoucherTypeEnum {
    OPEN("open", "开立凭证"),
    TRANSFER("transfer", "流转凭证");

    private final String code;
    private final String desc;

    VoucherTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static VoucherTypeEnum getByCode(String code) {
        for (VoucherTypeEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

    public static VoucherTypeEnum getByDesc(String desc) {
        for (VoucherTypeEnum e : values()) {
            if (e.getDesc().equals(desc)) {
                return e;
            }
        }
        return null;
    }
}
