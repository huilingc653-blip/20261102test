package com.bytter.increment.domain.vo.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("理财产品赎回待赎回金额信息")
public class ChoProductRedeemLeftMoneyVO {

    private BigDecimal leftMoney;

    private BigDecimal leftRedeemPortion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastRedeemDate;
}
