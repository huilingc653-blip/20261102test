package com.bytter.biz.foreign.enums;

import java.util.Arrays;

// 定义操作类型枚举
public enum LifeCycleOperationEnum {
    INVALID("1", "invalid", "失效"),/** 生命周期 操作类型：1-失效  */
    AVG_PRICE("2", "avgPrice", "录入均价"),/** 生命周期 操作类型：2-录入均价  */
    EXERCISE("3", "exercise", "行权"),/** 生命周期 操作类型：3-行权  */
    KNOCK_OUT("4", "knockOut", "敲出"), /** 生命周期 操作类型：4-敲出  */
    KNOCK_IN("5", "knockIn", "敲入"),/** 生命周期 操作类型：5-敲入  */
    CLOSE_POSITION("6", "closePosition", "平仓"),/** 生命周期 操作类型：6-平仓  */
    WAIVE_EXERCISE("7", "waiveExercise", "放弃行权"),/** 生命周期 操作类型：7-放弃行权  */
    EARLY_DELIVERY("8", "earlyDelivery", "提前交割"),/** 生命周期 操作类型：8-提前交割  */
    IN_ADVANCE("9", "inAdvance", "提前"),/** 生命周期 操作类型：9-提前  */
    RENEWAL("10", "renewal", "展期"),/** 生命周期 操作类型：10-展期  */
    DEADLINE_DELIVERY("11", "deadlineDelivery", "到期交割"),/** 生命周期 操作类型：11-到期交割  */
    RATE_CONFIRM("12", "rateConfirm", "汇率确认")/** 生命周期 操作类型：12-汇率确认  */
    ;

    private final String opType;
    private final String opTypeName;
    private final String opName;


    LifeCycleOperationEnum(String opType, String opTypeName, String opName) {
        this.opType = opType;
        this.opTypeName = opTypeName;
        this.opName = opName;
    }

    // 通过操作类型获取枚举实例
    public static LifeCycleOperationEnum fromCode(String opType) {
        return Arrays.stream(values())
                .filter(e -> e.opType.equals(opType))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid operation code"));
    }

    public String getOpType() {
        return opType;
    }

    public String getOpTypeName() {
        return opTypeName;
    }

    public String getOpName() {
        return opName;
    }
}


