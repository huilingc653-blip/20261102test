package com.bytter.guarantee.domain.vo;

import lombok.Data;

import java.util.List;

@Data
public class GuaChangeGuaContractVO {

    private ParamsVO paramsVO;

    private List<GuaContractInfoVO> contractlist;

    private String businessId;

    private String sysCode;

    private String billCode;

    private String corpId;
}
