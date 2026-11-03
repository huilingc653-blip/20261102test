package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import lombok.Data;

@Data
public class RatBkrationAddApproveDetailVO {
    private RatBkrationAddPageVO bankPage;
    private RatBkrationCpAddListDTO cpPage;
    private String addCode;
    private String taskId;
    private FcBondApplyDTO dto;
    private String viewPage;
}
