package com.bytter.increment.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class ChoDepositRateDTO implements Serializable{
    private String corpId;
    private String bankAcc;
    private Date interestStartDate;
    private Date deadlineDate;
    private BigDecimal rate;
}
