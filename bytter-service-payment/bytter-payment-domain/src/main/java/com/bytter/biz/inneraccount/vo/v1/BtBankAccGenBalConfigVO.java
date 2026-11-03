package com.bytter.biz.inneraccount.vo.v1;


import com.bytter.system.api.dto.BtCurrencyDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@ApiModel("银行账户冻结查询数据")
@Data
public class BtBankAccGenBalConfigVO implements Serializable {

    @ApiModelProperty("本位币")
    private BtCurrencyDTO natualCur;

    @ApiModelProperty("当前日期")
    private String curDate;
}
