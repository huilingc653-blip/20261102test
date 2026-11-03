package com.bytter.biz.inneraccount.vo.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("银行账户余额信息 查询数据")
@Data
public class BtBankAccBalQueryVO implements Serializable {


    @ApiModelProperty("单位ID")
    private String corpId;

    @ApiModelProperty("单位名称")
    private String corpName;

    @ApiModelProperty("查询时间")
    private String queryDate;

    @ApiModelProperty("类型")
    private String accGroup;
}
