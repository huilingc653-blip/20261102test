package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BisPayPolicyPayeeVO implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 策略ID
     */
    private String policyId;
    /**
     * 支付收款人ID
     */
    private String payeeId;
    /**
     * 支付收款人名称
     */
    private String payee;
    /**
     * 不支付收款人账号
     */
    private String payeeAcc;
    /**
     * 不支付收款人户名
     */
    private String payeeName;
    /**
     * 是否支付收款人：1-支付收款人，0-不支付收款人
     */
    private String isPayee;

}
