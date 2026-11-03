package com.bytter.report.domain.cms.vo;

import java.math.BigDecimal;

/**
 * @ClassName MobileCharDataVO
 * @Description TODO
 * @Author fusb
 * @Date 2020/5/18 20:19
 * @Version 1.0
 **/
public class MobileCharDataVO {

    private String xValue;

    private BigDecimal yValue;

    private String categoryName;

    public String getxValue() {
        return xValue;
    }

    public void setxValue(String xValue) {
        this.xValue = xValue;
    }

    public BigDecimal getyValue() {
        return yValue;
    }

    public void setyValue(BigDecimal yValue) {
        this.yValue = yValue;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
