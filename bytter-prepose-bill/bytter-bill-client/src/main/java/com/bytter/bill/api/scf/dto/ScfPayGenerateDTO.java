package com.bytter.bill.api.scf.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 51-应付债权凭证-付款审批通过后生成暂存凭证入参
 *
 * @author zhukr
 * @since 2026/8/18
 */
@Data
public class ScfPayGenerateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 租户ID */
    private String tenantId;

    /** 付款单ID */
    private String payId;

    /** 付款单号 */
    private String payNo;

    /** 平台ID */
    private String platformId;

    /** 融资产品ID */
    private String productId;

    /** 开立单位ID（=付款单的付款单位，生成的应付凭证归属） */
    private String corpId;

    /** 币种ID */
    private String curId;

    /** 凭证金额（=付款金额，不可修改） */
    private BigDecimal voucherMoney;

    /** 供应商ID（=付款单收款单位） */
    private String supplyId;

    /** 供应商名称 */
    private String supplyName;

    /** 供应商统一社会信用代码（=付款单收款人统一社会信用代码） */
    private String supplyCreditCode;

    /** 收款账号 */
    private String receiveAccount;

    /** 收款银行 */
    private String receiveBank;

    /** 融资付息方式（1:卖方付息，2:买方付息） */
    private String interestMode;

    /** 承诺付款日 */
    private Date promisePayDate;

    /** 线上线下标志（0=线下，1=线上） */
    private String onlineSign;

    /** 创建人 */
    private String createBy;

    /** 创建人名称 */
    private String createName;
}
