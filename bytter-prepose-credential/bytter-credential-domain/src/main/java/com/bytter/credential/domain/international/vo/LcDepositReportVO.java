package com.bytter.credential.domain.international.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 信用证押汇表
 **/
public class LcDepositReportVO implements java.io.Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 押汇流水号
     **/
    private String depositNo;
    /**
     * 币别
     **/
//    @Currency(inject = "curName")
    private String curId;
    /*币别*/
    private String curName;
    /**
     * 押汇金额
     **/
    private BigDecimal depositMoney;
    /**
     * 押汇日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date depositDate;
    /**
     * 到期日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    /**
     * 备注
     **/
    private String rmk;
    /**
     * 状态
     **/
    private Integer status;
    /**
     * 还押汇总额
     **/
    private BigDecimal reapyMoney;
    /**
     * 剩余金额
     **/
    private BigDecimal leftMoney;
    /*信用证号*/
    @BtCompositeSearch(fieldName = "lc.credit_no")
    private String creditNo;
    /*到单号*/
    private String arrivalNo;
    /*本次来单金额*/
    private BigDecimal billMoney;
    /*需付押汇金额*/
    private BigDecimal needPayMoney;
    /*到单日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;
    /*到单付汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    /*到单银行*/
    private String handDocBank;
    /*受益人*/
    private String beneficiary;
    /*合同号*/
    private String contractNo;
    /*信用证类型*/
    @BtCompositeSearch(fieldName = "lc.lc_inter")
    private String lcInter;
    /**
     * 已还押汇金额
     */
    private BigDecimal repayDepositMoney;
    /**
     * 待还押汇金额
     */
    private BigDecimal leftRepayDepositMoney;
    public String getDepositNo() {
        return depositNo;
    }
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getDepositMoney() {
        return depositMoney;
    }
    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }
    public Date getDepositDate() {
        return depositDate;
    }
    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public BigDecimal getReapyMoney() {
        return reapyMoney;
    }
    public void setReapyMoney(BigDecimal reapyMoney) {
        this.reapyMoney = reapyMoney;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    public String getArrivalNo() {
        return arrivalNo;
    }
    public void setArrivalNo(String arrivalNo) {
        this.arrivalNo = arrivalNo;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public BigDecimal getNeedPayMoney() {
        return needPayMoney;
    }
    public void setNeedPayMoney(BigDecimal needPayMoney) {
        this.needPayMoney = needPayMoney;
    }
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public String getHandDocBank() {
        return handDocBank;
    }
    public void setHandDocBank(String handDocBank) {
        this.handDocBank = handDocBank;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getLcInter() {
        return lcInter;
    }
    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }
    public BigDecimal getRepayDepositMoney() {
        return repayDepositMoney;
    }
    public void setRepayDepositMoney(BigDecimal repayDepositMoney) {
        this.repayDepositMoney = repayDepositMoney;
    }
    public BigDecimal getLeftRepayDepositMoney() {
        return leftRepayDepositMoney;
    }
    public void setLeftRepayDepositMoney(BigDecimal leftRepayDepositMoney) {
        this.leftRepayDepositMoney = leftRepayDepositMoney;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
}
