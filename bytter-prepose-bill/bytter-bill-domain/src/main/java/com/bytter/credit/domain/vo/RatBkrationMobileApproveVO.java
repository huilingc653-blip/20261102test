package com.bytter.credit.domain.vo;

import com.bytter.guarantee.dto.GuaContractUseDTO;
import lombok.Data;

@Data
public class RatBkrationMobileApproveVO {
    private RatBkrationPageVO ratBkrationPage;
    private String taskId;
    private String isGuarantee;
    private GuaContractUseDTO guaContractUseDTO;
}
