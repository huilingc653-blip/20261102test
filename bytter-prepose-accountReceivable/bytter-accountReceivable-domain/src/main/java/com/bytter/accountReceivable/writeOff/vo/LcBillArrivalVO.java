package com.bytter.accountReceivable.writeOff.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title Entity
 * @Description 信用证到单Vo
 */


public class LcBillArrivalVO implements Serializable,IBusinessKey {

    private String id;

    /*到单流水号*/
    @BtCompositeSearch(fieldName = "lb.arrival_code")
    private String arrivalCode;

    /*到单类型*/
    private Integer arrivalType;

    /*到单号*/
    @BtCompositeSearch(fieldName = "lb.arrival_no")
    private String arrivalNo;

    /*信用证登记ID*/
    @BtCompositeSearch(fieldName = "lb.lc_id")
    private String lcId;

    /*单位编号*/
    @BtCompositeSearch(fieldName = "lb.corp_Id")
    @Corp(inject = "corpName")
    private String corpId;


    /*本次来单金额*/
    private BigDecimal billMoney;

    /*本次付款金额*/
    @BtCompositeSearch(fieldName = "lb.pay_money")
    private BigDecimal payMoney;

    /*币别*/
    @BtCompositeSearch(fieldName = "lb.cur_id")
    @Currency(inject = "curName")
    private String curId;
    private String enCode;

    /*汇率*/
    private BigDecimal exchange;

    /*手续费*/
    private BigDecimal handlingCharge;

    /*到单日期*/
    @BtCompositeSearch(fieldName = "lb.payment_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;

    /*期限*/
    private Integer period;

    /*交单银行*/
    private String handDocBank;

    /*交单银行编号*/
    private String docBankNo;

    /*付汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;

    /*增值税款金额*/
    private BigDecimal addTaxMoney;

    /*关税款金额*/
    private BigDecimal taxMoney;

    /*缴税日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date taxDate;

    /*税款种类*/
    private String taxType;

    /*核销单号*/
    private String vbCode;

    /*取回日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date getDate;

    /*远期天数*/
    private Integer delayDates;

    /*受益人*/
    private String beneficiary;

    /*备注*/
    private String rmk;

    /*状态*/
    private Integer status;
    /*用于其他状态显示，如报表业务状态*/
    private Integer status2;

    /*创建人登录名称*/
    private String createBy;

    /*创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    /*更新人登录名称*/
    private String updateBy;

    /*更新日期*/
    private Date updateDate;

    /**信用证号**/
    @BtCompositeSearch(fieldName = "lc.credit_no")
    private String creditNo;

    /**币别**/
    private String curName;

    /**抵押状态**/
    private Integer lmStatus;
    /**抵押Id**/
    private String  lmId;

    /**议付状态**/
    private Integer negotiationStatus;
    /**议付Id**/
    private String  negotiationId;
    /**议付金额**/
    private BigDecimal  negotiationMoney;
    /**收款金额**/
    private BigDecimal  incomingMoney;
    /**押汇金额**/
    private BigDecimal  mortgageMoney;
    /*单位*/
    private String corpName;

    /**信用证类型**/
    @BtCompositeSearch(fieldName = "lc.lc_inter")
    @Dict(dictGroup = "letter_of_credit_inter", inject ="lcInterName")
    private String lcInter;

    /**信用证类型**/
    private String lcInterName;

    /*合同号*/
    private String contractNo;

    /*开证银行显示名*/
    private String issuingBankName;

    /*开证人*/
    private String applicant;

    private BigDecimal creditMoney;

    /*押汇金额*/
    private BigDecimal depositMoney;

    /*押汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date depositDate;

    /*押汇到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    /*还押汇金额*/
    private BigDecimal repayMoney;

    /*还押汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    /*关单号*/
    private String closeLcNo;

    /**
     * 境内或者境外银行名称
     */
    private String openingBankName;


    /*发票号*/
    private String invoiceNo;

    /**
     * 银行流水认领功能挂接标记，未认领=0,认领=1
     */
    private String bankFlowSign;

    /**
     * 附件id
     */
    private String attachmentId;

    /**
     * 核销状态
     */
    @BtCompositeSearch(fieldName = "lb.write_off_status")
    @Dict(dictGroup = "writeOff.status", inject = "writeOffStatusName")
    private String writeOffStatus;
    private String writeOffStatusName;
    /**
     * 核销类型
     */
    @BtCompositeSearch(fieldName = "lb.write_off_type")
    @Dict(dictGroup = "writeOff.type", inject = "writeOffTypeName")
    private String writeOffType;
    private String writeOffTypeName;
    /**
     * 应收余额
     */
    private BigDecimal writeOffBal;

    private String customCol1;
    private String customCol2;
    private String customCol3;
    private String customCol4;
    private String customCol5;

    private BigDecimal executeMoney;
    private BigDecimal newWriteOffBal;
    private String newWriteOffStatusName;

    private String flag;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    public String getCheckboxHide() {
        if(!"1".equals(this.getFlag())){
            return "1";
        }
        return checkboxHide;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public BigDecimal getExecuteMoney() {
        return executeMoney;
    }

    public void setExecuteMoney(BigDecimal executeMoney) {
        this.executeMoney = executeMoney;
    }

    public BigDecimal getNewWriteOffBal() {
        return newWriteOffBal;
    }

    public void setNewWriteOffBal(BigDecimal newWriteOffBal) {
        this.newWriteOffBal = newWriteOffBal;
    }

    public String getNewWriteOffStatusName() {
        return newWriteOffStatusName;
    }

    public void setNewWriteOffStatusName(String newWriteOffStatusName) {
        this.newWriteOffStatusName = newWriteOffStatusName;
    }

    public String getCustomCol1() {
        return customCol1;
    }

    public void setCustomCol1(String customCol1) {
        this.customCol1 = customCol1;
    }

    public String getCustomCol2() {
        return customCol2;
    }

    public void setCustomCol2(String customCol2) {
        this.customCol2 = customCol2;
    }

    public String getCustomCol3() {
        return customCol3;
    }

    public void setCustomCol3(String customCol3) {
        this.customCol3 = customCol3;
    }

    public String getCustomCol4() {
        return customCol4;
    }

    public void setCustomCol4(String customCol4) {
        this.customCol4 = customCol4;
    }

    public String getCustomCol5() {
        return customCol5;
    }

    public String getLcInterName() {
        return lcInterName;
    }

    public void setLcInterName(String lcInterName) {
        this.lcInterName = lcInterName;
    }

    public void setCustomCol5(String customCol5) {
        this.customCol5 = customCol5;
    }

    public String getLcInter() {
        return lcInter;
    }

    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getBusinessKey() {
        return id;
    }

    public String getLmId() {
        return lmId;
    }

    public void setLmId(String lmId) {
        this.lmId = lmId;
    }

    public Integer getLmStatus() {
        return lmStatus;
    }

    public void setLmStatus(Integer lmStatus) {
        this.lmStatus = lmStatus;
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

    public String getArrivalCode() {
        return arrivalCode;
    }

    public void setArrivalCode(String arrivalCode) {
        this.arrivalCode = arrivalCode;
    }

    public Integer getArrivalType() {
        return arrivalType;
    }

    public void setArrivalType(Integer arrivalType) {
        this.arrivalType = arrivalType;
    }

    public String getArrivalNo() {
        return arrivalNo;
    }

    public void setArrivalNo(String arrivalNo) {
        this.arrivalNo = arrivalNo;
    }

    public String getLcId() {
        return lcId;
    }

    public void setLcId(String lcId) {
        this.lcId = lcId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public BigDecimal getExchange() {
        return exchange;
    }

    public void setExchange(BigDecimal exchange) {
        this.exchange = exchange;
    }

    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }

    public void setHandlingCharge(BigDecimal handlingCharge) {

        this.handlingCharge = handlingCharge;
    }


    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getHandDocBank() {
        return handDocBank;
    }

    public void setHandDocBank(String handDocBank) {
        this.handDocBank = handDocBank;
    }

    public String getDocBankNo() {
        return docBankNo;
    }

    public void setDocBankNo(String docBankNo) {
        this.docBankNo = docBankNo;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public BigDecimal getAddTaxMoney() {
        return addTaxMoney;
    }

    public void setAddTaxMoney(BigDecimal addTaxMoney) {
        this.addTaxMoney = addTaxMoney;
    }

    public BigDecimal getTaxMoney() {
        return taxMoney;
    }

    public void setTaxMoney(BigDecimal taxMoney) {
        this.taxMoney = taxMoney;
    }

    public Date getTaxDate() {
        return taxDate;
    }

    public void setTaxDate(Date taxDate) {
        this.taxDate = taxDate;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getVbCode() {
        return vbCode;
    }

    public void setVbCode(String vbCode) {
        this.vbCode = vbCode;
    }

    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    public Integer getDelayDates() {
        return delayDates;
    }

    public void setDelayDates(Integer delayDates) {
        this.delayDates = delayDates;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getIssuingBankName() {
        return issuingBankName;
    }

    public void setIssuingBankName(String issuingBankName) {
        this.issuingBankName = issuingBankName;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public BigDecimal getCreditMoney() {
        return creditMoney;
    }

    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }

    public BigDecimal getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }

    public String getEnCode() {
        return enCode;
    }

    public void setEnCode(String enCode) {
        this.enCode = enCode;
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

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getCloseLcNo() {
        return closeLcNo;
    }

    public void setCloseLcNo(String closeLcNo) {
        this.closeLcNo = closeLcNo;
    }

    public Integer getStatus2() {
        return status2;
    }

    public void setStatus2(Integer status2) {
        this.status2 = status2;
    }

    public String getOpeningBankName() {
        return openingBankName;
    }

    public void setOpeningBankName(String openingBankName) {
        this.openingBankName = openingBankName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBankFlowSign() {
        return bankFlowSign;
    }

    public void setBankFlowSign(String bankFlowSign) {
        this.bankFlowSign = bankFlowSign;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getWriteOffStatus() {
        return writeOffStatus;
    }

    public void setWriteOffStatus(String writeOffStatus) {
        this.writeOffStatus = writeOffStatus;
    }

    public String getWriteOffStatusName() {
        return writeOffStatusName;
    }

    public void setWriteOffStatusName(String writeOffStatusName) {
        this.writeOffStatusName = writeOffStatusName;
    }

    public String getWriteOffType() {
        return writeOffType;
    }

    public void setWriteOffType(String writeOffType) {
        this.writeOffType = writeOffType;
    }

    public String getWriteOffTypeName() {
        return writeOffTypeName;
    }

    public void setWriteOffTypeName(String writeOffTypeName) {
        this.writeOffTypeName = writeOffTypeName;
    }

    public BigDecimal getWriteOffBal() {
        return writeOffBal;
    }

    public void setWriteOffBal(BigDecimal writeOffBal) {
        this.writeOffBal = writeOffBal;
    }

    public Integer getNegotiationStatus() {
        return negotiationStatus;
    }

    public void setNegotiationStatus(Integer negotiationStatus) {
        this.negotiationStatus = negotiationStatus;
    }

    public String getNegotiationId() {
        return negotiationId;
    }

    public void setNegotiationId(String negotiationId) {
        this.negotiationId = negotiationId;
    }

    public BigDecimal getNegotiationMoney() {
        return negotiationMoney;
    }

    public void setNegotiationMoney(BigDecimal negotiationMoney) {
        this.negotiationMoney = negotiationMoney;
    }

    public BigDecimal getIncomingMoney() {
        return incomingMoney;
    }

    public void setIncomingMoney(BigDecimal incomingMoney) {
        this.incomingMoney = incomingMoney;
    }

    public BigDecimal getMortgageMoney() {
        return mortgageMoney;
    }

    public void setMortgageMoney(BigDecimal mortgageMoney) {
        this.mortgageMoney = mortgageMoney;
    }
}
