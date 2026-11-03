package com.bytter.check.vo;

import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@ApiModel("银行账户流水认领数据查询")
@Data
public class BisAccDtlBudgetRuleViewPageVO implements Serializable {

    @ApiModelProperty("执行业务名称：bisaccdtl-银行流水")
    private String businessName;

    @ApiModelProperty("执行系统名称")
    private String processSysname;

    @ApiModelProperty("原始流水明细")
    private List<BisAccDtlBudgetRuleDTO> dtlList;

    @ApiModelProperty("流水明细数据")
    private BisAccDtlBudgetRuleDTO bisAccDtlBudgetRulePage;
}

