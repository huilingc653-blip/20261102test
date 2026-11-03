package com.bytter.bill.domain.bill.vo.v1;

import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ApiModel("应收-票据托收-托收到账页面跳转 数据携带")
public class AbBillCollectionAccountListPageVO implements Serializable {

    @ApiModelProperty("回显数据")
    private AbVoucherinfoViceVO billInfo;
    @ApiModelProperty("交易时间")
    private String businessDate;
    @ApiModelProperty("交易金额")
    private BigDecimal billMoney;
}
