package com.bytter.credit.domain.vo;

import com.bytter.guarantee.dto.FcBondApplyDTO;
import lombok.Data;

@Data
public class RatBkrationAddFormInitVO {
    private RatBkrationAddPageVO ratBkrationPage;
    private FcBondApplyDTO dto;
    private String flag;
}
