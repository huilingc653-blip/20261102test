package com.bytter.finance.domain.equity.vo;

import com.bytter.finance.domain.equity.entity.CmsEquityRepo;
import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资回购-审批详情页面数据
 */
@Data
public class CmsEquityRepoApproveVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 回购信息 */
    private CmsEquityRepo repo;
    /** 审批任务id */
    private String taskId;
}
