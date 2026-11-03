package com.bytter.credential.domain.international.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**信用证押汇展期表**/
public class LcDepositDelayVO implements Serializable, IBusinessKey {
    private String id;
    /**单位编号**/
    private String corpId;
    /**展期流水号**/
    @BtCompositeSearch(fieldName = "ldd.delay_no")
    private  String delayNo;
    /**押汇ID**/
    private  String depositId;
    /**押汇流水号**/
    private  String depositNo;
    /**展期天数**/
    private Integer delayDays;
    /**展期日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ldd.delay_date")
    private Date delayDate;
    /**展期日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ld.expiry_date")
    private Date expiryDate;
    /**利率**/
    private BigDecimal interestRate;
    /**备注**/
    private String rmk;
    /**状态**/
    @BtCompositeSearch(fieldName = "ldd.status")
    private Integer status;
    /**创建日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ldd.create_date")
    private Date createDate;
    /**创建人登录名称**/
    private String createBy;
    /**更新日期**/
    private Date updateDate;
    /**更新人登录名称**/
    private String updateBy;
    /*合同号证号*/
    @BtCompositeSearch(fieldName = "lc.contract_no")
    private String contractNo;
    /*信用证类型*/
    @BtCompositeSearch(fieldName = "lc.lc_inter")
    private String lcInter;
    /*到单号*/
    @BtCompositeSearch(fieldName = "lb.arrival_no")
    private String arrivalNo;
    /*到单日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lb.payment_date")
    private Date paymentDate;
    /**
     * 押汇日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ld.deposit_date")
    private Date depositDate;
    /**
     * 押汇金额
     **/
    @BtCompositeSearch(fieldName = "ld.deposit_money")
    private BigDecimal depositMoney;
    /*受益人*/
    @BtCompositeSearch(fieldName = "lb.beneficiary")
    private String beneficiary;
    /*到单银行*/
    @BtCompositeSearch(fieldName = "lb.hand_doc_bank")
    private String handDocBank;
    /*信用证号*/
    @BtCompositeSearch(fieldName = "lc.credit_no")
    private String creditNo;
    /**币别id*/
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "ld.cur_id")
    private String curId;
    /*币别*/
    @BtCompositeSearch(fieldName = "ld.cur_name")
    private String curName;
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
    
    public String getDelayNo() {
        return delayNo;
    }
    public void setDelayNo(String delayNo) {
        this.delayNo = delayNo;
    }
    public String getDepositId() {
        return depositId;
    }
    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }
    public String getDepositNo() {
        return depositNo;
    }
    public void setDepositNo(String depositNo) {
        this.depositNo = depositNo;
    }
    public Integer getDelayDays() {
        return delayDays;
    }
    public void setDelayDays(Integer delayDays) {
        this.delayDays = delayDays;
    }
    public Date getDelayDate() {
        return delayDate;
    }
    public void setDelayDate(Date delayDate) {
        this.delayDate = delayDate;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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
    public Date getCreateDate() {
        return createDate;
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
    public String getArrivalNo() {
        return arrivalNo;
    }
    public void setArrivalNo(String arrivalNo) {
        this.arrivalNo = arrivalNo;
    }
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public Date getDepositDate() {
        return depositDate;
    }
    public void setDepositDate(Date depositDate) {
        this.depositDate = depositDate;
    }
    public BigDecimal getDepositMoney() {
        return depositMoney;
    }
    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public String getHandDocBank() {
        return handDocBank;
    }
    public void setHandDocBank(String handDocBank) {
        this.handDocBank = handDocBank;
    }
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
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
}
