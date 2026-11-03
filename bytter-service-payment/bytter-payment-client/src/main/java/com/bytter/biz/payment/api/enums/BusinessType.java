package com.bytter.biz.payment.api.enums;

/**
 * @author luoyj
 * @date 2024/3/5.
 */
public enum BusinessType {

    AUTO_PAYMENT_INSTRUCTIONS("自动付款指令"), // TODO 核算发消息给结算 支付的统一使用这个类型
    MANUAL_PAYMENT_INSTRUCTIONS("手动付款指令"), // TODO 核算发消息给结算 支付的统一使用这个类型
    FUND_ALLOCATION("资金调拨"),// TODO 结算发消息给核算 支付管理、费用报销
    BILL_ENDORSEMENT("应收票据联动背书"),// TODO 结算发消息给核算 支付管理、费用报销
    UNKNOWN("未知"),// 缺租户头等前置校验失败、无法定位业务类型时，落失败记录占位用
//    ALLOCATION_TYPE_UP("上划"),
//    ALLOCATION_TYPE_DOWN("下拨"),
//    ALLOCATION_TYPE_ASK("请款"),
//    ALLOCATION_TYPE_POS("头寸调拨"),
//    ALLOCATION_TYPE_TURN("上缴")
    ;

    private final String name;

    BusinessType(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
