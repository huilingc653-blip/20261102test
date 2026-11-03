package com.bytter.bill.api.scf.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 51-应付债权凭证-付款审批通过后绑定付款单号入参
 *
 * @author zhukr
 * @since 2026/8/18
 */
@Data
public class ScfPayBindDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 租户ID */
    private String tenantId;

    /** 付款单ID */
    private String payId;

    /** 付款单号 */
    private String payNo;

    /** 关联的应付债权凭证ID集合 */
    private List<String> voucherIds;

    /** 更新人 */
    private String updateBy;

    /** 更新人名称 */
    private String updateName;
}
