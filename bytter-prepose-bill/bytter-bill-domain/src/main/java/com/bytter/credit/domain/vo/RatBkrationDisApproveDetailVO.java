package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatBkrationCpAddListDTO;
import lombok.Data;

@Data
public class RatBkrationDisApproveDetailVO {
    private RatBkrationCpAddListDTO ratBkrationPage;
    private String taskId;
    private String viewPage;
}
