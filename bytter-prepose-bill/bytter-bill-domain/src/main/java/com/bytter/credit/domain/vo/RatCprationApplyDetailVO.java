package com.bytter.credit.domain.vo;

import com.bytter.credit.dto.RatUrationApplyListDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * 内部授信占用 新增/编辑/详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class RatCprationApplyDetailVO implements Serializable {
    private RatBkrationDisVO ratCprationPage;
    private RatUrationApplyListDTO applyDTO;
    private String viewFlag;
    private String taskId;
}
