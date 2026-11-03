package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 内部授信分配 详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class RatCprationDisDetailVO implements Serializable {
    private RatBkrationPageVO ratCprationPage;
    private String viewFlag;
    private String taskId;
}
