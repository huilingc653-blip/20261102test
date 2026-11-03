package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("查询账号信息的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccSearchDTO implements Serializable {
    @ApiModelProperty("账户类别")
    private String accType;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("账户状态")
    private String accState;

    @ApiModelProperty("内部账号")
    private String blnctrAcc;

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("是否注销复核")
    private String isCheck;

    @ApiModelProperty("调用接口类型 如init")
    private String type;

    @ApiModelProperty("业务元素 如chargeAcc手续费收取账号 accForDeduct利息账号 accCussent贷款账号")
    private String elem;

    @ApiModelProperty("资金池标识")
    private String poolSign;

}
