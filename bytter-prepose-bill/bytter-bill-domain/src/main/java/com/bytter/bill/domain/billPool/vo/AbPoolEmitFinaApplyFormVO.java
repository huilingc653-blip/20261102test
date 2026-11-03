package com.bytter.bill.domain.billPool.vo;

import com.bytter.bill.domain.bill.entity.AbLog;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 融资申请 新增/修改/详情/审批详情/手机审批页面数据(共用)
 */
@Data
public class AbPoolEmitFinaApplyFormVO implements Serializable {
    private AbPoolEmitFinaApplyVO abPoolEmitFinaApplyVO;
    private Integer externalCorpNum;
    private List<AbPoolEmitFinaApplyVO> conInfoList;
    private List<AbLog> abLogList;
    private String taskId;
    private Boolean isApprove;
}
