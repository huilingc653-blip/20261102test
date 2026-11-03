package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 租金支付编辑页初始化返回 VO
 * 旧接口：/cms/lease/leasepay/edit
 */
@Data
public class LeasePayEditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String onlineSign;

    private CmsFinancingLeaseInfoVO leaseInfoVo;

    private LeasePayVO leasePayVo;

    private String downSign;

    private BigDecimal sendBalance;

    private String tempOrSubmitSign;
}
