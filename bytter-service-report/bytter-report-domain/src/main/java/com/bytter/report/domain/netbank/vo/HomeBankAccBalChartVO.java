package com.bytter.report.domain.netbank.vo;
import java.io.Serializable;
import java.math.BigDecimal;
public class HomeBankAccBalChartVO implements Serializable {
    /**
     * 取值
     */
    private BigDecimal value;
    /**
     * 名称
     */
    private String name;
    public BigDecimal getValue() {
        return value;
    }
    public void setValue(BigDecimal value) {
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
