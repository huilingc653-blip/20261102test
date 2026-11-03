package com.bytter.finance.domain.directFinance.vo;
import com.bytter.core.base.entity.IdEntity;
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
public class DfDirectFinanceRepayTmpVO extends IdEntity implements Serializable {
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
    /**
     * 币种
     */
    private String curId;
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getDirectFinanceId() {
        return directFinanceId;
    }
    public void setDirectFinanceId(String directFinanceId) {
        this.directFinanceId = directFinanceId;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }
    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
}
