package com.bytter.check.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("银行账户流水补录List配置参数查询")
@Data
public class PaymentBlendingConfigVO {

    @ApiModelProperty("是否挂接资金计划")
    private String budgetSel;

    @ApiModelProperty("执行业务名称：bisaccdtl-银行流水")
    private String businessName;

    @ApiModelProperty("执行系统名称")
    private String processSysname;
}

