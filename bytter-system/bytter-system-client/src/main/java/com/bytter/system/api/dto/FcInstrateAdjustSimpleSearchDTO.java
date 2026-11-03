package com.bytter.system.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 资金池账户利率调整复核内部table列表查询条件
 */
@ApiModel("FcInstrateAdjustSimpleSearchDTO")
@Data
public class FcInstrateAdjustSimpleSearchDTO {
    /**
     * 账户
     */
    @ApiModelProperty("账户")
    String account;
    /**
     * 网点ID
     */
    @ApiModelProperty("网点ID")
    String netId;
    /**
     * 单位ID
     */
    @ApiModelProperty("单位ID")
    String corpId;
    /**
     * 科目ID
     */
    @ApiModelProperty("科目ID")
    String subjectId;
    /**
     * 币种ID
     */
    @ApiModelProperty("币种ID")
    String curId;
    /**
     *利率类型
     */
    @ApiModelProperty("利率类型")
    String instrtType;
}
