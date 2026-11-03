package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁退租新增页初始化返回 VO
 * 旧接口：/cms/afterLease/refund/refundEdit/{id}
 */
@Data
public class LeaseRefundInitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String leaseId;

    private LeaseRefundVO leaseInfo;
}
