package com.bytter.biz.budget.spi.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("科目明细金额信息")
public class FbsItemMoneyInfoVO {

    @ApiModelProperty(value = "明细金额合计")
    private BigDecimal detailTotal;      // 明细金额合计

    @ApiModelProperty(value = "明细执行金额合计")
    private BigDecimal processTotal;     // 明细执行金额合计

    @ApiModelProperty(value = "明细余额")
    private BigDecimal balance;          // 明细余额
}
