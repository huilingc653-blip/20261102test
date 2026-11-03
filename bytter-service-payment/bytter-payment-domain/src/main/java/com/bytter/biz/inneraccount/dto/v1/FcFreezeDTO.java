package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("活期存款冻结单个数据查询参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcFreezeDTO implements Serializable {

    @ApiModelProperty("数据ID")
    private String id;

    @ApiModelProperty("账户状态")
    private String status;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("内部账号")
    private String blnctrAcc;

    @ApiModelProperty("操作类型")
    private String opType;
}
