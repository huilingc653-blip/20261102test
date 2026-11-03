package com.bytter.credit.domain.vo;

import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RatBkrationApproveDetailVO {
    private RatBkrationPageVO ratBkrationPage;
    private String taskId;
    private String isHangBail;
    private String isGuarantee;
    private FcBondApplyDTO dto;
    private BigDecimal addMoney;
    private ConfigDTO relateCon;
    private ConfigDTO conType;
    private String conId;
}
