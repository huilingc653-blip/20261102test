package com.bytter.supply.domain.payable.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("应付债权模板产品VO")
public class ScfPayTemplateProductVO {

    @ApiModelProperty("主键ID")
    private String id;

    @ApiModelProperty("模板编码")
    private String templateCode;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("平台编码")
    private String platformCode;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("有效标志")
    private String validSign;
}
