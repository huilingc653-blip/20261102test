package com.bytter.finance.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 信贷企业贷款检查Bean
 * 
 * @author liyh
 *
 */
public class CmsLoanSendOutCheckDTO implements Serializable {
    /**
     * 单号
     */
    private String billCode;
    /**
     * 贷款单位
     */
    private String loanCorp;
    /**
     * 录入日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;
    /**
     * 贷款账号
     */
    private String loanAcc;
    /**
     * 币别
     */
    private String curName;
    /**
     * 发放金额
     */
    private BigDecimal loanSendOutMoney;
    /**
     * 发放日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanSendOutDate;
    /**
     * 还款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /**
     * 还款方式
     */
    private String repayType;
    private String repayTypeName;

    /**
     * 贷款种类
     */
    private String loanClass;
    /**
     * 贷款期限
     */
    private Integer loanTerm;
    /**
     * 期限单位
     */
    private String ternUnit;
    /**
     * 状态
     */
    private Integer status;
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getLoanCorp() {
        return loanCorp;
    }
    public void setLoanCorp(String loanCorp) {
        this.loanCorp = loanCorp;
    }
    public Date getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getLoanSendOutMoney() {
        return loanSendOutMoney;
    }
    public void setLoanSendOutMoney(BigDecimal loanSendOutMoney) {
        this.loanSendOutMoney = loanSendOutMoney;
    }
    public Date getLoanSendOutDate() {
        return loanSendOutDate;
    }
    public void setLoanSendOutDate(Date loanSendOutDate) {
        this.loanSendOutDate = loanSendOutDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getLoanClass() {
        return loanClass;
    }
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    public Integer getLoanTerm() {
        return loanTerm;
    }
    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRepayTypeName() {
        return repayTypeName;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
}
