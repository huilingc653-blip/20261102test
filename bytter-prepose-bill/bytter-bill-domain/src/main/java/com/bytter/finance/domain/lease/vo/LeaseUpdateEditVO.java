package com.bytter.finance.domain.lease.vo;

import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.lease.entity.LeaseUpdate;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 融资租赁变更编辑页初始化返回 VO
 * 旧接口：/cms/lease/update/change
 */
@Data
public class LeaseUpdateEditVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private LeaseUpdate leaseUpdate;

    private BigDecimal repayMoney;

    private CorpDTO corp;

    private String baseCurId;

    private TbBaseRateDTO tbBaseRate;

    private TbCreditPeriodDTO tbCreditPeriod;

    private TbRateMaintenceDTO tbLoanRateMaintence;

    private List<DfIntermediateCostVO> costVOS;
}
