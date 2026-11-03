package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BisPayPolicyBusTypeVO implements Serializable {
    /**
     * id
     */
    private String id;
    /**
     * 策略ID
     */
    private String policyId;
    /**
     * 业务类别ID
     */
    private String busTypeId;
    /**
     * 业务类别名称
     */
    private String busTypeName;
    /**
     * 共享业务类别ID
     */
    private String shareBusTypeId;
    /**
     * 共享业务类别名称
     */
    private String shareBusTypeName;
}
