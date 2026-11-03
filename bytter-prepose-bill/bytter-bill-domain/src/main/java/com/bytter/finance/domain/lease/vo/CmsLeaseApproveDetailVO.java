package com.bytter.finance.domain.lease.vo;

import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 租赁审批详情页面数据
 */
@Data
public class CmsLeaseApproveDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private CmsFinancingLeaseInfo leaseInfo;
    private String taskId;
}
