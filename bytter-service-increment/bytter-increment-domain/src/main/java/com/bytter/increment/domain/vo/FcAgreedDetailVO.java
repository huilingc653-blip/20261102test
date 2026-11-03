package com.bytter.increment.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * @Title: Entity
 * @Description: 协定存款登记明细表
 * @author wangzx
 * @date 2019-05-10 16:44:38
 * @version 1.0
 *
 */
public class FcAgreedDetailVO implements Serializable {
    private static final long serialVersionUID = 2313478363314999846L;
    private String id;
    /**资金池id*/
    private String agreedDepositId;
    /**单位IDid*/
    private BigDecimal minMoney;
    /**账号id*/
    private BigDecimal maxMoney;
    /**账号*/
    private BigDecimal agreeRate;
    /**开始日期*/
    private Integer lineNo;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAgreedDepositId() {
        return agreedDepositId;
    }
    public void setAgreedDepositId(String agreedDepositId) {
        this.agreedDepositId = agreedDepositId;
    }
    public BigDecimal getMinMoney() {
        return minMoney;
    }
    public void setMinMoney(BigDecimal minMoney) {
        this.minMoney = minMoney;
    }
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public BigDecimal getAgreeRate() {
        return agreeRate;
    }
    public void setAgreeRate(BigDecimal agreeRate) {
        this.agreeRate = agreeRate;
    }
    public Integer getLineNo() {
        return lineNo;
    }
    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
}
