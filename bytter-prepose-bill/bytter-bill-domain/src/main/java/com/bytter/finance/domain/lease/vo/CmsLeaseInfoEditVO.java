package com.bytter.finance.domain.lease.vo;

import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaPledgeOccupyDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 租赁申请修改/补录页面数据
 */
@Data
public class CmsLeaseInfoEditVO implements Serializable {
    private static final long serialVersionUID = 1L;
    private CmsFinancingLeaseInfo leaseInfo;
    private CorpDTO corp;
    private String baseCurId;
    private TbBaseRateDTO tbBaseRate;
    private TbCreditPeriodDTO tbCreditPeriod;
    private TbRateMaintenceDTO tbLoanRateMaintence;
    private List<DfIntermediateCostVO> costVOS;
    /** 保证金参数 */
    private String configIsHookBond;
    private String isHookBond;
    private FcBondApplyDTO dto;
    private String bondBankAcc;
    private String bondCurId;
    private BigDecimal bailmoneyRate;
    private BigDecimal bailmoney;
    private String bailmoneyContract;
    private BigDecimal bondRate;
    /** 担保参数 */
    private String viewFlag;
    private String isGuarantee;
    private String isHangGuarantee;
    private String contractIds;
    private List<String> idList;
    private String guaranteeRmk;
    private String guaWayNames;
    private String guaWay;
    private String contractCode;
    /** 抵质押参数 */
    private String pledgeConfig;
    private String isRelatePledge;
    private List<GuaPledgeOccupyDTO> pledgeDTOS;
    private String itemNo;
    private String businessId;
}
