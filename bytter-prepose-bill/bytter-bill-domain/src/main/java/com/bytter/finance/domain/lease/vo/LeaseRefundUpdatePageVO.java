package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁退租编辑页返回 VO
 * 旧接口：/cms/afterLease/refund/refundUpdate/{id}
 *
 * @author bytter
 */
@Data
public class LeaseRefundUpdatePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租赁主表 ID
     */
    private String leaseId;

    /**
     * 租赁主信息
     */
    private LeaseRefundVO leaseInfo;

    /**
     * 待编辑的退租信息
     */
    private LeaseRefundUpdateVO updateVO;
}
