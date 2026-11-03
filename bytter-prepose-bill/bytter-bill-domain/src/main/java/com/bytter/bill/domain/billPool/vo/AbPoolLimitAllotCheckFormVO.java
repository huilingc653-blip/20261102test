package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 额度分配核准 详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolLimitAllotCheckFormVO implements Serializable {
    private AbPoolLimitAllotCheckVO info;
    private List<AbLog> abLogList;
    private String taskId;
    private Boolean isApprove;
}
