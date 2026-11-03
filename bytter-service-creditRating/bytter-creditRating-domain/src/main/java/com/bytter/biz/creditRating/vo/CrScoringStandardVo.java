package com.bytter.biz.creditRating.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class CrScoringStandardVo implements Serializable {
    /**
     * 评分方法code
     */
    private Integer code;
    /**
     * 评分方法名称
     */
    private String methodName;
    /**
     * 评分细则左边符号
     */
    private Integer valueDirection;
    /**
     * 评分细则右边符号
     */
    private Integer otherValueDirection;
    /**
     * 评分细则左边的值
     */
    private BigDecimal value;
    /**
     * 评分细则右边的值
     */
    private BigDecimal othervalue;
    /**
     * 评分细则对应的分数
     */
    private BigDecimal grade;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Integer getValueDirection() {
        return valueDirection;
    }

    public void setValueDirection(Integer valueDirection) {
        this.valueDirection = valueDirection;
    }

    public Integer getOtherValueDirection() {
        return otherValueDirection;
    }

    public void setOtherValueDirection(Integer otherValueDirection) {
        this.otherValueDirection = otherValueDirection;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getOthervalue() {
        return othervalue;
    }

    public void setOthervalue(BigDecimal othervalue) {
        this.othervalue = othervalue;
    }

    public BigDecimal getGrade() {
        return grade;
    }

    public void setGrade(BigDecimal grade) {
        this.grade = grade;
    }
}
