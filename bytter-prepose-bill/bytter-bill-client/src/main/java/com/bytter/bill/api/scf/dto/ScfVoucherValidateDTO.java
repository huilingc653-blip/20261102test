package com.bytter.bill.api.scf.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 付款办理-债权凭证校验入参
 *
 * @author zhukr
 * @since 2026/8/18
 */
@Data
public class ScfVoucherValidateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 租户ID */
    private String tenantId;

    /** 凭证类型（1=应付，2=应收） */
    private String voucherType;

    /** 选中的凭证ID集合 */
    private List<String> voucherIds;

    /** 付款金额 */
    private BigDecimal payMoney;

    /** 平台ID */
    private String platformId;

    /** 融资产品ID */
    private String productId;

    /** 供应商ID（应付校验供应商一致性用） */
    private String supplyId;
}
