package com.bytter.credit.domain.vo;

import com.bytter.credit.domain.entity.RatBkrationAdjust;
import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import lombok.Data;

@Data
public class RatBkrationAdjustApproveDetailVO {
    private RatBkrationPageVO ratBkrationPageVO;
    private RatBkrationCpAddListDTO ratBkrationCpPage;
    private RatBkrationAdjust ratBkrationAdjust;
    private String taskId;
}
