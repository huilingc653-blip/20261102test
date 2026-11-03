package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("查询账号复核的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccCheckDTO implements Serializable {

    @ApiModelProperty("账户ID")
    private String id;

    @ApiModelProperty("注销ID")
    private String cancelId;

    @ApiModelProperty("全局流水号ID")
    private String busGlobalId;
}
