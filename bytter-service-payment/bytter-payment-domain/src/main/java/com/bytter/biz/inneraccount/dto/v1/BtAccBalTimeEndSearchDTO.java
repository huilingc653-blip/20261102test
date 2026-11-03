package com.bytter.biz.inneraccount.dto.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("银行账户流水已截止日期维护 查询数据")
@Data
public class BtAccBalTimeEndSearchDTO {

    @ApiModelProperty("单位")
    private String corpId;

    @ApiModelProperty("银行账号")
    private String bankAcc;

    @ApiModelProperty("币别")
    private String curId;

    @ApiModelProperty("银行类型")
    private String bifCode;

}
