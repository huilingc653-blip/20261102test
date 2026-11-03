package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 租赁审批列表页面数据
 */
@Data
public class CmsLeaseApproveListVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String flowStatus;
    private String cycleTime;
}
