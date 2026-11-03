package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 提示付款 新增/详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolCollectionApplyFormVO implements Serializable {
    private AbPoolCollectionApplyVO abPoolCollectionApplyVO;
    private String businessDate;
    private List<AbLog> abLogList;
    private String taskId;
    private Boolean isApprove;
}
