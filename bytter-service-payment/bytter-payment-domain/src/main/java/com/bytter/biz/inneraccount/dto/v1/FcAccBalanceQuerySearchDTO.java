package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@ApiModel("查询账号余额的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccBalanceQuerySearchDTO implements Serializable {

    @ApiModelProperty("资金池标识")
    private String poolSign;
    @ApiModelProperty("账号")
    private String account;
    @ApiModelProperty("币别")
    private String curId;
    @ApiModelProperty("单位")
    private String corpId;
    @ApiModelProperty("科目")
    private String subjectId;
}
