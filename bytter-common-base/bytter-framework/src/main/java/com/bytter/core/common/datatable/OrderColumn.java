package com.bytter.core.common.datatable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 排除规则描述
 */
@ApiModel("排除规则描述")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderColumn implements Serializable {
    @ApiModelProperty("属性名称")
    private String field;
    @ApiModelProperty("排序规则：acs , desc")
    private String order;
}
