package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 审批列表页面通用数据(各审批入口共用)
 */
@Data
public class ApproveListVO implements Serializable {
    private String flowStatus;
    private String cycleTime;
    private String flag;
}
