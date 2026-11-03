package com.bytter.biz.netbank.vo;

import lombok.Data;

import java.util.List;

/**
 *三单关联执行策略
 */
@Data
public class BisBankExecutionStrategyUpdateVO {
    BisBankExecutionStrategyVO bisBankExecutionStrategy;
    List<NisBusTypeRuleVO> nisBusTypeRuleVOList;
    Integer length;
}
