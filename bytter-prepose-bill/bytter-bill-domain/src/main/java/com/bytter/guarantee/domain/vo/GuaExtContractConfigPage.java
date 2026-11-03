package com.bytter.guarantee.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("对外担保合同登记新增配置获取")
public class GuaExtContractConfigPage {

    @ApiModelProperty("对外担保是否对接合同管理")
    private String relateCon;

    @ApiModelProperty("对外担保合同对接合同管理模式")
    private String conType;
}
