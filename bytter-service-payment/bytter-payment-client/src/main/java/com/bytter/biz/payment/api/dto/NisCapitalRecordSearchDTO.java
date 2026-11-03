package com.bytter.biz.payment.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 付款办理查询条件
 */
@ApiModel("NisCapitalRecordSearchDTO")
@Data
public class NisCapitalRecordSearchDTO implements Serializable {
    private String corpId;
    private String payeeAccName;
    private BigDecimal minPayMoney;
    private BigDecimal maxPayMoney;
    private Date minWishPayDay;
    private Date maxWishPayDay;
    private String voucherType;
    private String contractName;
    private String contractCode;
    private String purpose;
    private String statusStr;
    private String busType;
}
