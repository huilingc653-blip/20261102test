package com.bytter.biz.foreign.enums;

/**
 * 外汇交易类型
 */
public enum FexTranTypeEnum {
    SPOT("spot", "Spot", "外汇即期交易", "0"),
    FORWARD("forward", "Forward", "外汇远期交易", "1"),
    SWAP("swap", "Swap", "外汇掉期交易", "2");

    private final String code;
    private final String forward;
    private final String name;
    private final String fexTranType;

    FexTranTypeEnum(String code, String forward, String name,String fexTranType) {
        this.code = code;
        this.forward = forward;
        this.name = name;
        this.fexTranType = fexTranType;
    }

    public String getCode() {
        return code;
    }

    public String getForward() {
        return forward;
    }

    public String getName() {
        return name;
    }

    public String getFexTranType() {
        return fexTranType;
    }



    /**
     * 通过code获取对应的枚举实例
     * @param code 要匹配的code值
     * @return 匹配的枚举实例，如果没有匹配的则返回null
     */
    public static FexTranTypeEnum getByCode(String code) {
        // 遍历所有枚举值
        for (FexTranTypeEnum type : values()) {
            // 比较code是否匹配
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}
