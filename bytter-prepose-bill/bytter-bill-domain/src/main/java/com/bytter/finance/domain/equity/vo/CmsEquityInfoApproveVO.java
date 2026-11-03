package com.bytter.finance.domain.equity.vo;

import com.bytter.finance.domain.equity.entity.CmsEquityInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资登记-审批详情页面数据
 */
@Data
public class CmsEquityInfoApproveVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 股权融资主信息 */
    private CmsEquityInfo equityInfo;
    /** 审批任务id */
    private String taskId;
}
