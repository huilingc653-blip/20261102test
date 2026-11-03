package com.bytter.report.domain.netbank.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 结算收支分析VO
 *
 * @author huangx 2022-03-28
 */
public class HomeBankAccDtlDistributeVO implements Serializable {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 期初金额
     */
    private BigDecimal startMoney;
    /**
     * 期末金额
     */
    private BigDecimal endMoney;
    /**
     * 流入笔数
     */
    private Integer getNumbers;
    /**
     * 流入金额
     */
    private BigDecimal getMoney;
    /**
     * 流出笔数
     */
    private Integer borrowNumbers;
    /**
     * 流出金额
     */
    private BigDecimal borrowMoney;
    /**
     * 交易日期
     */
    private String transTime;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getStartMoney() {
        return startMoney;
    }
    public void setStartMoney(BigDecimal startMoney) {
        this.startMoney = startMoney;
    }
    public BigDecimal getEndMoney() {
        return endMoney;
    }
    public void setEndMoney(BigDecimal endMoney) {
        this.endMoney = endMoney;
    }
    public Integer getGetNumbers() {
        return getNumbers;
    }
    public void setGetNumbers(Integer getNumbers) {
        this.getNumbers = getNumbers;
    }
    public BigDecimal getGetMoney() {
        return getMoney;
    }
    public void setGetMoney(BigDecimal getMoney) {
        this.getMoney = getMoney;
    }
    public Integer getBorrowNumbers() {
        return borrowNumbers;
    }
    public void setBorrowNumbers(Integer borrowNumbers) {
        this.borrowNumbers = borrowNumbers;
    }
    public BigDecimal getBorrowMoney() {
        return borrowMoney;
    }
    public void setBorrowMoney(BigDecimal borrowMoney) {
        this.borrowMoney = borrowMoney;
    }
    public String getTransTime() {
        return transTime;
    }
    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }
}
