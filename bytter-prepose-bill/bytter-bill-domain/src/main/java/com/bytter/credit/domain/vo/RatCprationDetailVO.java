package com.bytter.credit.domain.vo;

import com.bytter.system.api.dto.ConfigDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 内部授信录入 详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class RatCprationDetailVO implements Serializable {
    private RatBkrationPageVO ratCprationPage;
    private String viewFlag;
    private String taskId;
    private String conId;
    private ConfigDTO relateCon;
    private ConfigDTO conType;
}
