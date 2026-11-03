package com.bytter.bill.api.scf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款办理-应付债权凭证选票结果
 *
 * @author zhukr
 * @since 2026/8/18
 */
@Data
public class ScfPayableVoucherSelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 凭证ID */
    private String id;

    /** 单据编号 */
    private String voucherNo;

    /** 债权凭证编号 */
    private String voucherCode;

    /** 凭证金额 */
    private BigDecimal voucherMoney;

    /** 币种ID */
    private String curId;

    /** 币种名称 */
    private String curName;

    /** 平台ID */
    private String platformId;

    /** 平台名称 */
    private String platformName;

    /** 融资产品ID */
    private String productId;

    /** 融资产品名称 */
    private String productName;

    /** 开立单位ID */
    private String corpId;

    /** 开立单位名称 */
    private String corpName;

    /** 供应商ID */
    private String supplyId;

    /** 供应商名称 */
    private String supplyName;

    /** 线上线下标志（0=线下，1=线上） */
    private String onlineSign;

    /** 开立日期 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date openDate;

    /** 到期日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expireDate;

    /** 承诺付款日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date promisePayDate;

    /** 融资付息方式（1:卖方付息，2:买方付息） */
    private String interestMode;

    /** 业务状态 */
    private String voucherStatus;

    /** 业务状态名称 */
    private String voucherStatusName;

    /** 单据状态 */
    private String approvalStatus;

    /** 单据状态名称 */
    private String approvalStatusName;

    /** 关联付款单ID（付款单审批通过后写入，非空表示已被付款单关联） */
    private String payId;

    /** 关联付款单号（付款单审批通过后写入，非空表示已被付款单关联） */
    private String payNo;
}
