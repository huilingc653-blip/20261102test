package com.bytter.finance.domain.precative.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 审核列表页面配置数据
 */
@Data
public class CmsPrecativeLoanApproveListVo implements Serializable {

    private String flowStatus;
    private String cycleTime;
    private String opType;
}
