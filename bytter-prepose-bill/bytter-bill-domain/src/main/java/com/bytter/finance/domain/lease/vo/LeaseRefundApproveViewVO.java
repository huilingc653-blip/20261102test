package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁退租审批详情返回 VO
 * 旧接口：/cms/afterLease/refund/viewApproveDate
 */
@Data
public class LeaseRefundApproveViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String flag;

    private CmsAfterLeaseInfoVO cmsAfterLeaseInfoVO;
}
