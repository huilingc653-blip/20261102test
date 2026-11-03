package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
@ApiModel("内部流水资金计划批量处理DTO")
public class FcAccDetailBudgetBatchDTO {

    @ApiModelProperty(value = "ids")
    private String ids;

    @ApiModelProperty(value = "操作类型")
    private String type;

    @ApiModelProperty(value = "科目代码")
    private String itemCode;

    @ApiModelProperty(value = "科目名称")
    private String itemName;

    @ApiModelProperty(value = "执行时间")
    private String processDate;
}
