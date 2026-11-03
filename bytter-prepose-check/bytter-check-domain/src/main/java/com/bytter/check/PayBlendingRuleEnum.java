package com.bytter.check;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 勾兑规则
 * todo 用于改动勾兑一块业务使用
 */
@AllArgsConstructor
@Getter
public enum PayBlendingRuleEnum {

    AMT("1","金额","eqg_payMoney"),
    PAYER_ACC("2","付款账号","eqs_payerAcc"),
    PAYEE_ACC("3","收款账号","eqs_payeeAcc"),
    WISH_PAYDAY("4","交易日期","wishPayday"),
    CUR_ID("5","币别","eqs_curId"),;

    private final String code;
    private final String desc;
    private final String cond;

    public static List<PayBlendingRuleEnum> chooseRules(String ruleStr){
        return Arrays.stream(PayBlendingRuleEnum.values())
                .filter(item -> ruleStr.contains(item.getCode()))
                .collect(Collectors.toList());
    }
}
