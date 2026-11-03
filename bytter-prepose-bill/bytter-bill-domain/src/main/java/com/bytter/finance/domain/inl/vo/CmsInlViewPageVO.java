package com.bytter.finance.domain.inl.vo;

import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.dto.ConInfoDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import lombok.Data;

import java.util.Map;

@Data
public class CmsInlViewPageVO {

    private CorpDTO corp;

    private CmsInlInfoVO inlInfo;

    private String flag;

    private TbBaseRateDTO  baseRate;

    private TbCreditPeriodDTO creditPeriod;

    private Map<String, Object> config;

    private GuaContractUseDTO guaInfo;

    private ConInfoDTO conInfo;

    private String conTypeId;

    private RatUrationDTO cpURationDTO;

    private String businessName;

    private String processSysname;
}
