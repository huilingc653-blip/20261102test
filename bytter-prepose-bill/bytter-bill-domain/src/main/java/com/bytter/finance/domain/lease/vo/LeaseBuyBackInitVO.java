package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁回购新增页初始化返回 VO
 * 旧接口：/cms/afterLease/buyBack/repoLease/{id}
 *
 * @author bytter
 */
@Data
public class LeaseBuyBackInitVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租赁主表 ID
     */
    private String leaseId;

    /**
     * 租赁主信息
     */
    private CmsAfterLeaseInfoVO leaseInfo;
}
