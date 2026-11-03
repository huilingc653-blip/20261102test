package com.bytter.credit.domain.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 内部授信追加 新增/编辑/详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class RatCprationAddDetailVO implements Serializable {
    private RatBkrationAddPageVO ratCprationPage;
    private String flag;
    private String viewFlag;
    private String taskId;
}
