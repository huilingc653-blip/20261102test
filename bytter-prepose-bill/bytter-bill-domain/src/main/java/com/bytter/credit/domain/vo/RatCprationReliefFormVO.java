package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddDtlDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 内部授信调剂 新增/编辑/详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class RatCprationReliefFormVO implements Serializable {
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
