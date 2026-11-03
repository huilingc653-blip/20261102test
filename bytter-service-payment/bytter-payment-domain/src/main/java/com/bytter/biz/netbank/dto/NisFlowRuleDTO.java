package com.bytter.biz.netbank.dto;

import com.bytter.biz.netbank.vo.NisFlowRuleVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 添加资金调拨流水规则参数
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NisFlowRuleDTO extends NisFlowRuleVO implements Serializable {
    private String[] bankTypeIds;
    private String[] purpose1;
}
