package com.bytter.supply.enums;

/**
 * @author ztd
 * @description 业务状态枚举
 * @since 2026/7/3
 */
public enum BusinessStatusEnum {
    UNSIGNED("unsigned", "未签收"),
    SIGNED("signed", "已签收"),
    REJECTED("rejected", "已拒签"),
    CANCEL("cancel", "已作废"),

    SPLIT("split", "已拆分"),
    UN_TRANSFER("un_transfer", "待转让"),
    TRANSFERRED("transferred", "已转让"),
    TRANSFER_ING("transfer_ing", "转让中"),


    UN_LOAN("un_loan", "待融资"),
    LOAN_ING("loan_ing", "融资中"),
    LOAN_SUCCESS("loan_success", "已融资"),


    UN_PAYMENT("un_payment", "待收款"),
    PAYMENT_ING("payment_ing", "收款中"),
    PAYMENT_SUCCESS("payment_success", "已收款"),

    DATA_SUPPLEMENT("data_supplement","数据待补录");
    

    private final String code;
    private final String desc;

    BusinessStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static BusinessStatusEnum getByCode(String code) {
        for (BusinessStatusEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
