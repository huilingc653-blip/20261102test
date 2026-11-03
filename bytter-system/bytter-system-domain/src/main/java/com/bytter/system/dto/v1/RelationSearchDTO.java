package com.bytter.system.dto.v1;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("父子关系查询数据")
@Data
public class RelationSearchDTO implements Serializable {

    @ApiModelProperty("单位ID")
    private String corpId;

    @ApiModelProperty("网点ID")
    private String netId;

    @ApiModelProperty("币别ID")
    private String curId;

    /**
     * 资金池标识 0-非资金池；1-资金池
     */
    @ApiModelProperty("资金池标识")
    private String poolSign;
}
