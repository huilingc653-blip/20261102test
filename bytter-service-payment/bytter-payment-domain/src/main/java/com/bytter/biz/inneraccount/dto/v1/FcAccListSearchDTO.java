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
public class FcAccListSearchDTO implements Serializable {
    @ApiModelProperty("账户类别")
    private String accType;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("网点")
    private String netId;

    @ApiModelProperty("银行Code")
    private String bankCode;

    @ApiModelProperty("系统代码")
    private String sysSubjectCode;

    @ApiModelProperty("当前查询页")
    private Integer currentPage;

    @ApiModelProperty("每页显示数量")
    private Integer pageSize;

    @ApiModelProperty("资金池标识")
    private String poolSign;

}
