package com.bytter.biz.inneraccount.dto.v1;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("查询账号相关信息的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccCommonDTO implements Serializable {

    @ApiModelProperty("账户主键ID")
    private String id;

    @ApiModelProperty("账户")
    private String account;
}
