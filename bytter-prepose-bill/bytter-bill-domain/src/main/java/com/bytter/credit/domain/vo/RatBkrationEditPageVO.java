package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.vm.RatBkRationVm;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class RatBkrationEditPageVO {
    private RatBkRationVm ratBkrationPage;
    private Integer singItemSize;
    private Integer compItemSize;
    private Integer itemSize;
    private Integer copmRatSize;
    private FcBondApplyDTO dto;
    private String isHangBail;
    private ConfigDTO relateCon;
    private ConfigDTO conType;
    private String conId;
    private String isGuarantee;
    private String creditContractControl;
    private String isHangGuarantee;
    private String contractIds;
    private List<String> idList;
    private String contractCode;
    private String guaWayNames;
    private String guaWay;
    private String guaranteeRmk;
}
