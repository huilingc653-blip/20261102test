package com.bytter.system.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("父子关系Table查询数据")
@Data
public class RelationTableSearchDTO implements Serializable {

    @ApiModelProperty("单位ID")
    private String corpId;

    @ApiModelProperty("网点ID")
    private String nes_id;

    @ApiModelProperty("币别ID")
    private String curId;

    @ApiModelProperty("银行类别")
    private String bankTypeId;

    @ApiModelProperty("账号属性")
    private String attributeId;

    @ApiModelProperty("银行账号")
    private String bankAcc;
    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    @ApiModelProperty("资金池标识")
    private String poolSign;


    @ApiModelProperty("资金池Id")
    private String poolId;
}
