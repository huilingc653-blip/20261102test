package com.bytter.check.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel("银行账户流水补录List配置参数查询")
@Data
public class BisAccDtlBudgetRuleListConfigVO {

    @ApiModelProperty("是否挂接资金计划")
    private String budgetSel;

    @ApiModelProperty("执行业务名称：bisaccdtl-银行流水")
    private String businessName;

    @ApiModelProperty("执行系统名称")
    private String processSysname;

    @ApiModelProperty("是否启用拆分")
    private String isEnableSplit;

    @ApiModelProperty("是否启用合并")
    private String isEnableMerge;

    @ApiModelProperty("是否认领到业务类别")
    private String isClaimBusType;

    @ApiModelProperty("交易时间默认一周")
    private Date beginTransTime;

    @ApiModelProperty("当前使用的本位币")
    private String natualCurId;
    @ApiModelProperty("汇率")
    private BigDecimal natualCurExrate;
}

