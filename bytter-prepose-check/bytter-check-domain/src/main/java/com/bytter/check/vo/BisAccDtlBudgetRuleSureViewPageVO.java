package com.bytter.check.vo;

import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("银行账户流水补录List配置参数查询")
@Data
public class BisAccDtlBudgetRuleSureViewPageVO {

    @ApiModelProperty("收款认领")
    private String budgetSel;

    @ApiModelProperty("执行业务名称：bisaccdtl-银行流水")
    private String businessName;

    @ApiModelProperty("执行系统名称")
    private String processSysname;

    @ApiModelProperty("是否认领到业务类别")
    private String isClaimBusType;

    @ApiModelProperty("流水明细数据")
    private BisAccDtlBudgetRuleDTO bisAccDtl;
}

