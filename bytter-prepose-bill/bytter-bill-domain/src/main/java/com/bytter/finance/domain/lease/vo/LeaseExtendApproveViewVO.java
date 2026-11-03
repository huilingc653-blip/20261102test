package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁展期审批详情返回 VO
 * 旧接口：/cms/LeaseExtend/viewApproveDate
 */
@Data
public class LeaseExtendApproveViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String flag;

    private LeaseExtendVO leaseExtendVO;
}
