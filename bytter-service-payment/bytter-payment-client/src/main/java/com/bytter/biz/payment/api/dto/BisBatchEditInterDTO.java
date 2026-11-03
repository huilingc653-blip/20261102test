package com.bytter.biz.payment.api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BisBatchEditInterDTO {

    private String bondIds;

    private BigDecimal interestRate;

}
