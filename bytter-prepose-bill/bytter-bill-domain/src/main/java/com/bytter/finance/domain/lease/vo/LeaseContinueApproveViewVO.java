package com.bytter.finance.domain.lease.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 续租审批详情返回 VO
 * 旧接口：/cms/afterLease/continue/viewApproveDate
 *
 * @author bytter
 */
@Data
public class LeaseContinueApproveViewVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 页面标识
     */
    private String flag;

    /**
     * 续租审批详情
     */
    private CmsAfterLeaseInfoVO cmsAfterLeaseInfoVO;
}
