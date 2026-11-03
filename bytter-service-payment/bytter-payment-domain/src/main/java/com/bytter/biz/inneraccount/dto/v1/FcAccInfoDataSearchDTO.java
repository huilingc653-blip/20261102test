package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel("内部账号查询组件查询参数对象")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FcAccInfoDataSearchDTO implements Serializable {
    @ApiModelProperty("账户名称")
    private String accName;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("账号")
    private String bankAcc;

    @ApiModelProperty("银行编码")
    private String bankCode;

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("计息账号")
    private String interestAcc;

    @ApiModelProperty("系统科目代码")
    private String sysSubjectCode;
}
