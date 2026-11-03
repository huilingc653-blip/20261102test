package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import lombok.Data;

@Data
public class RatBkrationAddCpDetailVO {
    private RatBkrationCpAddListDTO ratBkrationPage;
    private String addCode;
    private String flag;
    private String viewFlag;
    private FcBondApplyDTO dto;
}
