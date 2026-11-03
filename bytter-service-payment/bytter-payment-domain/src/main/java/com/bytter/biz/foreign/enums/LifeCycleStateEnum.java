package com.bytter.biz.foreign.enums;

public enum LifeCycleStateEnum {
    INIT("INIT", "初始化"),
    TOBE_CONFIRM_RATE("TOBE_CONFIRM_RATE", "待汇率确认"),
    ONGOING("ONGOING", "续存");

    private final String code;
    private final String name;


    LifeCycleStateEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }


    public String getName() {
        return name;
    }
}



