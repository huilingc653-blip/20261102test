package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 入池申请额度核准 详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolLimitApplyCheckFormVO implements Serializable {
    private AbPoolLimitApplyCheckVO info;
    private List<AbLog> abLogList;
    private String taskId;
    private Boolean isApprove;
}
