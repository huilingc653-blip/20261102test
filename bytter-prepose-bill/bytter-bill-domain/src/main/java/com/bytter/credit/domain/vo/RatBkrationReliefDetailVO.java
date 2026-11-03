package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import lombok.Data;

import java.util.List;

@Data
public class RatBkrationReliefDetailVO {
    private RatBkrationReliefPageVO ratBkrationReliefPage;
    private RatBkrationPageVO ratBkrationPage;
    private List<RatItemDisVO> reliefItemFlags;
    private List<RatItemDisVO> reliefItems;
    private RatBkrationCpAddDtlDTO disMoneyDTO;
    private List<RatItemDisVO> byReliefItemFlags;
    private List<RatItemDisVO> byReliefItems;
    private RatItemDisVO byItemAssure;
    private String flag;
    private String viewFlag;
    private String taskId;
    private String viewPage;
}
