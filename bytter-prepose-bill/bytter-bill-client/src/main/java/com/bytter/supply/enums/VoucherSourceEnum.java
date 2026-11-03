package com.bytter.supply.enums;

/**
 * @author ztd
 * @description  凭证来源
 * @since 2026/7/13
 */
public enum VoucherSourceEnum {
    MANUAL("MANUAL", "手工新增"),
    ERP("ERP", "ERP"),
    SUPPLY_FIN_PLATFORM("SUPPLY_PLATFORM", "供应链金融平台");

    /** 编码（存库/接口传输值） */
    private final String code;
    /** 展示名称 */
    private final String desc;

    VoucherSourceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * 根据编码匹配枚举
     */
    public static VoucherSourceEnum getByCode(String code) {
        if (code == null) {
            // 默认返回手工新增
            return MANUAL;
        }
        for (VoucherSourceEnum item : values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        // 无匹配则默认手工新增
        return MANUAL;
    }
}
