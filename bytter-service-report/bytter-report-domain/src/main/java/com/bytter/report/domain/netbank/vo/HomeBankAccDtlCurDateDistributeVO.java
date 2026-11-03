package com.bytter.report.domain.netbank.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 结算收支分析VO
 *
 * @author huangx 2022-03-28
 */
public class HomeBankAccDtlCurDateDistributeVO implements Serializable {
    /**
     * 金额
     */
    private BigDecimal totalMoney;
    /**
     * 数量
     */
    private Integer totalNum;
    /**
     * 交易状态
     */
    private String voucherStat;
    public BigDecimal getTotalMoney() {
        return totalMoney;
    }
    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }
    public Integer getTotalNum() {
        return totalNum;
    }
    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
}
