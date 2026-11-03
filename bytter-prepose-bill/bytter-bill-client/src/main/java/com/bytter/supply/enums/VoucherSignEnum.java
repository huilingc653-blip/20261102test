package com.bytter.supply.enums;

/**
 * @author ztd
 * @description 凭证签收状态枚举
 * @since 2026/7/1
 */
public enum VoucherSignEnum {


    SIGNED("signed", "签收"),
    REJECTED("rejected", "拒签"),
    CANCELLED("cancelled", "作废");

    private final String code;
    private final String desc;

    VoucherSignEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }



    public static VoucherSignEnum getByCode(String code) {
        for (VoucherSignEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
