package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("内部账户初始化页面查询")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcInitAccSearchDTO implements Serializable {

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("状态")
    private String status;
}
