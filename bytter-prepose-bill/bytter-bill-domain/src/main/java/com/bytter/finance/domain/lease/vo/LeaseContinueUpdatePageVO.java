package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁续租编辑页返回 VO
 * 旧接口：/cms/afterLease/continue/continueUpdate/{id}
 *
 * @author bytter
 */
@Data
public class LeaseContinueUpdatePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租赁主表 ID
     */
    private String leaseId;

    /**
     * 租赁主信息
     */
    private CmsAfterLeaseInfoVO leaseInfo;

    /**
     * 待编辑的续租信息（含续租主键 id）
     */
    private CmsAfterLeaseInfoVO updateVO;
}
