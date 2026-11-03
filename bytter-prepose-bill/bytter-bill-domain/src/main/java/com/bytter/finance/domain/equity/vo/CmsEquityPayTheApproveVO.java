package com.bytter.finance.domain.equity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 股权融资缴付到账-审批详情页面数据
 */
@Data
public class CmsEquityPayTheApproveVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /** 缴付到账信息 */
    private CmsEquityPayTheVO payThe;
    /** 审批任务id */
    private String taskId;
}
