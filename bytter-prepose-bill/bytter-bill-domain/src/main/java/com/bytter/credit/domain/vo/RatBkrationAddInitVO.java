package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.entity.RatBkration;
import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

@Data
public class RatBkrationAddInitVO {
    private RatBkration ratBkrationPage;
    private Integer copmRatSize;
    private String isHangBail;
    private String isGuarantee;
    private ConfigDTO relateCon;
    private ConfigDTO conType;
    //是否敞口
    private String isOpen;
    private String creditContractControl;
}
