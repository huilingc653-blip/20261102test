package com.bytter.biz.compareBill.vo.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("成员单位对账Table 配置获取")
public class CorpCompareBillConfigVO {

    @ApiModelProperty("当前年份")
    private Integer currentYear;

    @ApiModelProperty("当前月份")
    private Integer currentMonth;

    @ApiModelProperty("查询类型")
    private String type;

    @ApiModelProperty("是否是工作流")
    private String flowStatus;
}
