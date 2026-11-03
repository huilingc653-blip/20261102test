package com.bytter.biz.budget.spi.vo;

import com.bytter.biz.budget.spi.dto.FbsBudgetDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("科目明细金额信息")
public class FbsItemMoneyDetailInfoVO {

    @ApiModelProperty(value = "明细显示列")
    private List<String> dtlColSel;      //明细显示列

    @ApiModelProperty(value = "科目明细信息")
    private List<FbsBudgetDetailDTO> detailInfo;
}
