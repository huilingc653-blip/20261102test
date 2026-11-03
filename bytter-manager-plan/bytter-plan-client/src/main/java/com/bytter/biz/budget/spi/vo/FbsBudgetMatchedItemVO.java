package com.bytter.biz.budget.spi.vo;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FbsBudgetMatchedItemVO {

    @ApiModelProperty(value = "科目信息")
    private FbsBudgetProcessCommonDTO itemObject;

    @ApiModelProperty(value = "匹配明细金额总信息")
    private FbsItemMoneyInfoVO detailMoney;

    @ApiModelProperty(value = "匹配科目明细信息")
    private FbsItemMoneyDetailInfoVO detailObject;


}
