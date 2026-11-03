package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁回购编辑页返回 VO
 * 旧接口：/cms/afterLease/buyBack/repoUpdate/{id}
 *
 * @author bytter
 */
@Data
public class LeaseBuyBackUpdatePageVO implements Serializable {

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
     * 待编辑的回购信息（含回购主键 id）
     */
    private CmsAfterLeaseInfoVO updateVO;
}
