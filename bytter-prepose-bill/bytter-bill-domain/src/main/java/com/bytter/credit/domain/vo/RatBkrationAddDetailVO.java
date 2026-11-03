package com.bytter.credit.domain.vo;

import com.bytter.guarantee.dto.FcBondApplyDTO;
import lombok.Data;

@Data
public class RatBkrationAddDetailVO {
    private RatBkrationAddPageVO ratBkrationPage;
    private String viewFlag;
    private FcBondApplyDTO dto;
}
