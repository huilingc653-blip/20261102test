package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 功能介绍 : 接口区间传输数据<br>
 * <b>日期：</b>2024/9/14<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CrmSectionDataVO implements Serializable {
    /** 区间左符号*/
    private String leftSymbol;
    /** 区间左值*/
    private BigDecimal leftValue;
    /** 区间右值*/
    private BigDecimal rightValue;
    /** 区间右符号*/
    private String rightSymbol;
    /**指标得分*/
    private BigDecimal grade;
    /** 评分级别*/
    private String scoringLevel;

    public String getLeftSymbol() {
        return leftSymbol;
    }

    public void setLeftSymbol(String leftSymbol) {
        this.leftSymbol = leftSymbol;
    }

    public BigDecimal getLeftValue() {
        return leftValue;
    }

    public void setLeftValue(BigDecimal leftValue) {
        this.leftValue = leftValue;
    }

    public BigDecimal getRightValue() {
        return rightValue;
    }

    public void setRightValue(BigDecimal rightValue) {
        this.rightValue = rightValue;
    }

    public String getRightSymbol() {
        return rightSymbol;
    }

    public void setRightSymbol(String rightSymbol) {
        this.rightSymbol = rightSymbol;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }

    public String getScoringLevel() {
        return scoringLevel;
    }

    public void setScoringLevel(String scoringLevel) {
        this.scoringLevel = scoringLevel;
    }
}
