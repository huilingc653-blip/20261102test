package com.bytter.finance.domain.lease.vo;

import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 融资租赁展期新增/编辑页初始化返回 VO
 * 旧接口：/cms/LeaseExtend/update/{id}、/cms/LeaseExtend/extendUpdate/{id}
 */
@Data
public class LeaseExtendUpdatePageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String leaseId;

    private CmsFinancingLeaseInfo leaseInfo;

    private TbRateMaintenceDTO tbLoanRateMaintence;

    /**
     * 编辑页才有；新增页为空
     */
    private LeaseExtendVO leaseExtendVO;
}
