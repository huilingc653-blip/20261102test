package com.bytter.biz.vo;

import com.bytter.biz.budget.vo.FbsBudgetProcessCommonVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class FbsBudgetMatchedItemVO {

    @ApiModelProperty(value = "科目信息")
    private FbsBudgetProcessCommonVO itemObject;

    @ApiModelProperty(value = "匹配明细金额总信息")
    private FbsItemMoneyInfoVO detailMoney;

    @ApiModelProperty(value = "匹配科目明细信息")
    private FbsItemMoneyDetailInfoVO detailObject;


}
