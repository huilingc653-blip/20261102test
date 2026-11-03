package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 租赁申请新增页面数据
 */
@Data
public class CmsLeaseInfoAddVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String isHookBond;
    private String pledgeConfig;
    private String isGuarantee;
}
