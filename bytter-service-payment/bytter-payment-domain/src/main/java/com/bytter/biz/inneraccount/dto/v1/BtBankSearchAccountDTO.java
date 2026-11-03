package com.bytter.biz.inneraccount.dto.v1;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("获取银行账户相关信息的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BtBankSearchAccountDTO implements Serializable {

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("银行账户")
    private String bankAcc;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("银行类别")
    private String bankType;

    @ApiModelProperty("账户性质")
    private String natureId;
}
