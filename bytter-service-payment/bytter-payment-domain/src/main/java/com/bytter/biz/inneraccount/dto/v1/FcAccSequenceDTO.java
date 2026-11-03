package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@ApiModel("生成顺序号的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccSequenceDTO implements Serializable {
    @ApiModelProperty("账户类别")
    private String accType;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("资金池标识")
    private String poolSign;

    @ApiModelProperty("号码")
    private String seqNum;
}
