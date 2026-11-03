package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("查询菜单下 查询账号全部信息的请求参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccAllSearchDTO implements Serializable {
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

    @ApiModelProperty("资金池标识")
    private String poolSign;

    @ApiModelProperty("开户时间 最小范围")
    private String accOpenTimeStart;

    @ApiModelProperty("开户时间 最大范围")
    private String accOpenTimeEnd;

}
