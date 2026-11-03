package com.bytter.credential.domain.international.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 信用证押汇表
 **/
public class LcDepositVO implements java.io.Serializable , IBusinessKey {
    private String id;
    /**
     * 单位编号
     **/
    private String corpId;
    /**
     * 到单ID
     **/
    private String arrivalId;
    /**
     * 信用证登记ID
     **/
    private String lcId;
    /**
     * 押汇流水号
     **/
    @BtCompositeSearch(fieldName = "ld.deposit_no")
    private String depositNo;
    /**
     * 币别
     **/
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "ld.cur_id")
    private String curId;
    /*币别*/
    @BtCompositeSearch(fieldName = "ld.cur_name")
    private String curName;
    /**
     * 汇率
     **/
    private BigDecimal exchangeRate;
    /**
     * 利率
     **/
    private BigDecimal interestRate;
    /**
     * 押汇金额
     **/
    @BtCompositeSearch(fieldName = "ld.deposit_money")
    private BigDecimal depositMoney;
    /**
     * 押汇日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ld.deposit_date")
    private Date depositDate;
    /**
     * 到期日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ld.expiry_date")
    private Date expiryDate;
    /**
     * 期限
     **/
    private Integer timeLimit;
    /**
     * 期限单位
     **/
    private String termUnit;
    /**
     * 是否补录
     **/
    private String isSkipProcess;
    /**
     * 备注
     **/
    private String rmk;
    /**
     * 状态
     **/
    @BtCompositeSearch(fieldName = "ld.status")
    private Integer status;
    /**
     * 还押汇总额
     **/
    private BigDecimal reapyMoney;
    /**
     * 剩余金额
     **/
    private BigDecimal leftMoney;
    /**
     * 创建日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ld.create_date")
    private Date createDate;
    /**
     * 创建人登录名称
     **/
    private String createBy;
    /**
     * 更新日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 更新人登录名称
     **/
    private String updateBy;
    /*信用证号*/
    @BtCompositeSearch(fieldName = "lc.credit_no")
    private String creditNo;
    /*到单号*/
    @BtCompositeSearch(fieldName = "lb.arrival_no")
    private String arrivalNo;
    /*本次来单金额*/
    @BtCompositeSearch(fieldName = "lb.bill_money")
    private BigDecimal billMoney;
    /*需付押汇金额*/
    private BigDecimal needPayMoney;
    /*到单日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lb.payment_date")
    private Date paymentDate;
    /*到单银行*/
    @BtCompositeSearch(fieldName = "lb.hand_doc_bank")
    private String handDocBank;
    /*受益人*/
    @BtCompositeSearch(fieldName = "lb.beneficiary")
    private String beneficiary;
    /*合同号*/
    @BtCompositeSearch(fieldName = "lc.contract_no")
    private String contractNo;
    /*信用证类型*/
    @BtCompositeSearch(fieldName = "lc.lc_inter")
    private String lcInter;
    /*展期到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ldd.expiry_date")
    private Date depositDelayExpiryDate;
    /**付息日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payInterestDate;
    /**利息**/
    private BigDecimal payInterestMoney;
    /**
     * 附件id
     */
    private String attachmentId;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getArrivalId() {
        return arrivalId;
    }
    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }
    public String getLcId() {
        return lcId;
    }
    public void setLcId(String lcId) {
        this.lcId = lcId;
    }
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
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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
    public Integer getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }
    public String getTermUnit() {
        return termUnit;
    }
    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }
    public String getIsSkipProcess() {
        return isSkipProcess;
    }
    public void setIsSkipProcess(String isSkipProcess) {
        this.isSkipProcess = isSkipProcess;
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
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public Date getCreateDate() {
        return createDate;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLcInter() {
        return lcInter;
    }
    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }
    public Date getDepositDelayExpiryDate() {
        return depositDelayExpiryDate;
    }
    public void setDepositDelayExpiryDate(Date depositDelayExpiryDate) {
        this.depositDelayExpiryDate = depositDelayExpiryDate;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public Date getPayInterestDate() {
        return payInterestDate;
    }
    public void setPayInterestDate(Date payInterestDate) {
        this.payInterestDate = payInterestDate;
    }
    public BigDecimal getPayInterestMoney() {
        return payInterestMoney;
    }
    public void setPayInterestMoney(BigDecimal payInterestMoney) {
        this.payInterestMoney = payInterestMoney;
    }
}
