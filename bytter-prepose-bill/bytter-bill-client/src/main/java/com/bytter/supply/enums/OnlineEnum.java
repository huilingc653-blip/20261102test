package com.bytter.supply.enums;

/**
 * @author ztd
 * @description 线上线下标识枚举
 * @since 2026/7/3
 */
public enum OnlineEnum {
    OFFLINE("0", "线下"),
    ONLINE("1", "线上");

    private final String code;
    private final String desc;

    OnlineEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static OnlineEnum getByCode(String code) {
        for (OnlineEnum e : values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
