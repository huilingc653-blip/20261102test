package com.bytter.bill.api.scf.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款办理（付款方式52）-应收债权凭证回显结果
 * <p>由付款单编辑回显反查：按转让草稿ID反查应收债权凭证，凭证信息 + 转让暂存信息（接收方/金额/日期/用途）
 *
 * @author zhukr
 * @since 2026/8/25
 */
@Data
public class ScfReceivableVoucherSelDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 凭证ID */
    private String id;

    /** 单据编号 */
    private String voucherNo;

    /** 债权凭证编号 */
    private String voucherCode;

    /** 凭证金额 */
    private BigDecimal voucherAmt;

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

    /** 持有单位ID */
    private String corpId;

    /** 持有单位名称 */
    private String corpName;

    /** 开立/转让单位 */
    private String issueTransferCompany;

    /** 开立/转让单位ID */
    private String issueTransferCompanyId;

    /** 线上线下标志（0=线下，1=线上） */
    private String onlineSign;

    /** 业务状态 */
    private String voucherStatus;

    /** 单据状态 */
    private String approvalStatus;

    /** 到期日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date expireDate;

    /** 承诺付款日 */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date promisePayDate;

    /** 转让金额（转让草稿） */
    private BigDecimal transferAmt;

    /** 接收单位（转让草稿） */
    private String receiveCompany;

    /** 接收单位ID（转让草稿） */
    private String receiveCompanyId;

    /** 转让日期（转让草稿） */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date transferDate;

    /** 用途（转让草稿） */
    private String purpose;

    /** 转让标识（转让草稿）：1-单个转让，2-批量转让（多对一），供付款单编辑回显后前端按标识调对应转让查看接口 */
    private String transferFlag;
}
