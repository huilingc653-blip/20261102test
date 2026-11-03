package com.bytter.bill.api.scf.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 付款办理-债权凭证校验结果
 *
 * @author zhukr
 * @since 2026/8/18
 */
@Data
public class ScfVoucherValidateResultDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 是否通过 */
    private boolean valid;

    /** 提示信息 */
    private String message;

    /** 凭证金额合计 */
    private BigDecimal totalMoney;

    /** 平台ID */
    private String platformId;

    /** 融资产品ID */
    private String productId;

    /** 供应商ID */
    private String supplyId;

    /** 供应商名称 */
    private String supplyName;
}
