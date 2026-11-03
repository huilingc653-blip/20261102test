package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 授信延期审批列表页面数据
 */
@Data
public class RatBkrationDelayApproveListVO implements Serializable {
    private String flowStatus;
    private String cycleTime;
}
