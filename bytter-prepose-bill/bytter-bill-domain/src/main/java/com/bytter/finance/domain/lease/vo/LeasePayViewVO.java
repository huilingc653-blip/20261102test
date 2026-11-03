package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 租金支付查看/审批详情返回 VO
 * 旧接口：/cms/lease/leasepay/viewLeasePay、/viewLeasePayApprove
 */
@Data
public class LeasePayViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<LeasePayVO> leasePayVOList;

    private CmsFinancingLeaseInfoVO leaseInfoVo;

    private String taskId;
}
