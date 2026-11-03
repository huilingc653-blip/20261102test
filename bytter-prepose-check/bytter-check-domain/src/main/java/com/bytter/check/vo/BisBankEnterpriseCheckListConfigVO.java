package com.bytter.check.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("银企对账 银企余额对账页面Config配置信息")
@Data
public class BisBankEnterpriseCheckListConfigVO {

    @ApiModelProperty("币别ID")
    private String curId;

    @ApiModelProperty("默认开始时间")
    private String dateStart;

    @ApiModelProperty("默认结束时间")
    private String dateEnd;
}
