package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 保证金存取 详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolBailFormVO implements Serializable {
    private AbPoolBailDtlVO info;
    private List<AbLog> abLogList;
    private String taskId;
    private Boolean isApprove;
    private Boolean inBail;
}
