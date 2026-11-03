package com.bytter.biz.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("预算明细")
public class FbsBudgetSaveDetailDTO {

    private String id;

    private String itemCode;

    private String itemName;

    private BigDecimal bMoney;

    private BigDecimal btempMoney;

    private String superaddDtlId;

    private String rmk;

    public BigDecimal getbMoney() {
        return this.bMoney;
    }

    public void setBMoney(final BigDecimal bMoney) {
        this.bMoney = bMoney;
    }

}
