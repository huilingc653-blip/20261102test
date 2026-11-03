package com.bytter.basic.vo;

import com.bytter.basic.entity.BtInputBankInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("银行行号数据")
@Data
public class BtInputBankInfoPageVO {

    @ApiModelProperty("页面信息")
    private BtInputBankInfoVO btInputBankInfoPage;
}
