package com.bytter.biz.receipt.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("承兑汇票电子回单表详情")
public class ECompareBillPageVO {

    private String opType;

    private ECompareBillVO ECompareBillVO;

    private EVisaTypeVO eVisaTypePage;

    private List<ECompareBillVO> list;

    private BigDecimal sumDebitMoney;

    private BigDecimal sumCreditMoney;

    private BigDecimal sumBalance;
}
