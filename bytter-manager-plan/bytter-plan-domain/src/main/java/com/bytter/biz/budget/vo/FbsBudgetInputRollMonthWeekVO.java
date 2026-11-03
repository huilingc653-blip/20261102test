package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 计划上报VO - 月滚动双周
 * @author qijh
 *
 */
@SuppressWarnings("serial")
public class FbsBudgetInputRollMonthWeekVO extends FbsBudgetInputRollMonthVO implements Serializable{
    /**第1周上报金额*/
    private BigDecimal[] bMoneyWeek1;
    /**第2周上报金额*/
    private BigDecimal[] bMoneyWeek2;
    /**第3周上报金额*/
    private BigDecimal[] bMoneyWeek3;
    /**第4周上报金额*/
    private BigDecimal[] bMoneyWeek4;
    /**第5周上报金额*/
    private BigDecimal[] bMoneyWeek5;
    /**第6周上报金额*/
    private BigDecimal[] bMoneyWeek6;
    /**周明细ID*/
    private String[] weekDetailId1;
    /**周明细ID*/
    private String[] weekDetailId2;
    /**周明细ID*/
    private String[] weekDetailId3;
    /**周明细ID*/
    private String[] weekDetailId4;
    /**周明细ID*/
    private String[] weekDetailId5;
    /**周明细ID*/
    private String[] weekDetailId6;
    public BigDecimal[] getbMoneyWeek1() {
        return bMoneyWeek1;
    }
    public void setbMoneyWeek1(BigDecimal[] bMoneyWeek1) {
        this.bMoneyWeek1 = bMoneyWeek1;
    }
    public BigDecimal[] getbMoneyWeek2() {
        return bMoneyWeek2;
    }
    public void setbMoneyWeek2(BigDecimal[] bMoneyWeek2) {
        this.bMoneyWeek2 = bMoneyWeek2;
    }
    public BigDecimal[] getbMoneyWeek3() {
        return bMoneyWeek3;
    }
    public void setbMoneyWeek3(BigDecimal[] bMoneyWeek3) {
        this.bMoneyWeek3 = bMoneyWeek3;
    }
    public BigDecimal[] getbMoneyWeek4() {
        return bMoneyWeek4;
    }
    public void setbMoneyWeek4(BigDecimal[] bMoneyWeek4) {
        this.bMoneyWeek4 = bMoneyWeek4;
    }
    public BigDecimal[] getbMoneyWeek5() {
        return bMoneyWeek5;
    }
    public void setbMoneyWeek5(BigDecimal[] bMoneyWeek5) {
        this.bMoneyWeek5 = bMoneyWeek5;
    }
    public BigDecimal[] getbMoneyWeek6() {
        return bMoneyWeek6;
    }
    public void setbMoneyWeek6(BigDecimal[] bMoneyWeek6) {
        this.bMoneyWeek6 = bMoneyWeek6;
    }
    public String[] getWeekDetailId1() {
        return weekDetailId1;
    }
    public void setWeekDetailId1(String[] weekDetailId1) {
        this.weekDetailId1 = weekDetailId1;
    }
    public String[] getWeekDetailId2() {
        return weekDetailId2;
    }
    public void setWeekDetailId2(String[] weekDetailId2) {
        this.weekDetailId2 = weekDetailId2;
    }
    public String[] getWeekDetailId3() {
        return weekDetailId3;
    }
    public void setWeekDetailId3(String[] weekDetailId3) {
        this.weekDetailId3 = weekDetailId3;
    }
    public String[] getWeekDetailId4() {
        return weekDetailId4;
    }
    public void setWeekDetailId4(String[] weekDetailId4) {
        this.weekDetailId4 = weekDetailId4;
    }
    public String[] getWeekDetailId5() {
        return weekDetailId5;
    }
    public void setWeekDetailId5(String[] weekDetailId5) {
        this.weekDetailId5 = weekDetailId5;
    }
    public String[] getWeekDetailId6() {
        return weekDetailId6;
    }
    public void setWeekDetailId6(String[] weekDetailId6) {
        this.weekDetailId6 = weekDetailId6;
    }
}
