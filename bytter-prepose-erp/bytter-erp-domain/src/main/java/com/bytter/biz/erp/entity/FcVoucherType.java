package com.bytter.biz.erp.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description: 付款方式
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/12O
 */
@Data
public class FcVoucherType implements Serializable {
    /**
     * 主键ID
     */
    private String id;
    /**
     * 租户ID
     */
    private String tenantId;

    /**
     * 付款方式编码
     */
    private String voucherType;

    /**
     * 付款方式名称
     */
    private String voucherName;


    /**
     * 现金转账标志
     */
    private String cashTransSign;

    /**
     * 结算类型
     */
    private String settlType;

    /**
     * 是否走银企接口
     */
    private String isElectronPay;

    /**
     * 是否记账
     */
    private String isAccount;

    /**
     * 是否挂预算
     */
    private String isBudget;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 有效标志
     */
    private String validSign;

    /**
     * 是否系统初始化
     */
    private String isSystem;

    /**
     * 发送模式
     */
    private String sendMode;

}
