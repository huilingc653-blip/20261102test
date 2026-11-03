package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.entity.RatBkrationDelay;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 授信延期手机审批详情页面数据
 */
@Data
public class RatBkrationDelayMobileApproveVO implements Serializable {
    private RatBkrationDelay delay;
    private RatBkrationPageVO ratBkrationPage;
    private String taskId;
    private String isGuarantee;
    private GuaContractUseDTO guaContractUseDTO;
}
