package com.bytter.credit.domain.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class RatItemSumVO implements Serializable {

    /**
     * 汇总业务名称
     */
    private String sumBusName;

    /**
     * 汇总金额
     */
    private BigDecimal sumMoney;

    public String getSumBusName() {
        return sumBusName;
    }

    public void setSumBusName(String sumBusName) {
        this.sumBusName = sumBusName;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }
}
