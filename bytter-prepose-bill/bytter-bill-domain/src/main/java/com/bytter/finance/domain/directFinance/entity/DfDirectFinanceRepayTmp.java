package com.bytter.finance.domain.directFinance.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-10 10:03
 * @Version 1.0
 */
@Entity
@Table(name = "DF_DIRECT_FINANCE_REPAY_TMP", schema = "")
@SuppressWarnings("serial")
public class DfDirectFinanceRepayTmp extends IdEntity implements Serializable {
    /**
     * 流水号
     */
    private String billCode;
    /**
     * 主表id
     */
    private String directFinanceId;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 还款金额
     */
    private BigDecimal repayMoney;
    /**
     * 利息
     */
    private BigDecimal interest;
    /**
     * 手续费
     */
    private BigDecimal serviceCharge;
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    @Column(name = "DIRECT_FINANCE_ID")
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "INTEREST")
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "SERVICE_CHARGE")
    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }
    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
}
