package com.bytter.increment.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Entity
@Table(name = "FC_AGREED_DETAIL", schema = "")
@SuppressWarnings("serial")
public class FcAgreedDetail extends IdEntity implements Serializable {
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
    @Column(name ="AGREED_DEPOSIT_ID",nullable=false,length=32)
    public String getAgreedDepositId() {
        return agreedDepositId;
    }
    public void setAgreedDepositId(String agreedDepositId) {
        this.agreedDepositId = agreedDepositId;
    }
    @Column(name ="MIN_MONEY",nullable=true)
    public BigDecimal getMinMoney() {
        return minMoney;
    }
    public void setMinMoney(BigDecimal minMoney) {
        this.minMoney = minMoney;
    }
    @Column(name ="MAX_MONEY",nullable=true)
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    @Column(name ="AGREE_RATE",nullable=true)
    public BigDecimal getAgreeRate() {
        return agreeRate;
    }
    public void setAgreeRate(BigDecimal agreeRate) {
        this.agreeRate = agreeRate;
    }
    @Column(name ="LINE_NO",nullable=true)
    public Integer getLineNo() {
        return lineNo;
    }
    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
}
