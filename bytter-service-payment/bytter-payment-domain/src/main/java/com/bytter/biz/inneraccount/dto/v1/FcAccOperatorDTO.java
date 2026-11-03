package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("查询账号注销的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccOperatorDTO implements Serializable {
    @ApiModelProperty("账户ID")
    private String id;
    @ApiModelProperty("账户类别")
    private String accTypeCode;

    @ApiModelProperty("操作类型 add/update")
    private String type;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("账号操作类型 改户时为change")
    private String opType;

    @ApiModelProperty("资金池标识")
    private String poolSign;

}
