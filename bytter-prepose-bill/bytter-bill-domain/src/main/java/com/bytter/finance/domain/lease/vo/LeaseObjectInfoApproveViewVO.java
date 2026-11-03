package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 租赁物审批详情返回 VO
 * 旧接口：/cms/LeaseObjectInfo/viewApproveDate
 */
@Data
public class LeaseObjectInfoApproveViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String flag;

    private LeaseObjectInfoVO leaseObjectInfoVO;
}
