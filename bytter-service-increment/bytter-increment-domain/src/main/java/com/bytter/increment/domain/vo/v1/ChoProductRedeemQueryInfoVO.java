package com.bytter.increment.domain.vo.v1;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("理财产品赎回可赎回信息")
public class ChoProductRedeemQueryInfoVO {

    private BigDecimal maxRedeemMoney;

    private BigDecimal maxRedeemPortion;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastRedeemDate;
}
