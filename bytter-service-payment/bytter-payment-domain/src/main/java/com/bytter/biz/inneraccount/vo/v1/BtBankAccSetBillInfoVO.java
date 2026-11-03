package com.bytter.biz.inneraccount.vo.v1;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("批量设置票据账户 返回参数")
@Data
public class BtBankAccSetBillInfoVO implements Serializable {

    @ApiModelProperty("是否为电票")
    private String electricBill;

    @ApiModelProperty("流通方式 0-线下 1-线上")
    private String circulationMode;
}
