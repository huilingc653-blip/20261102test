package com.bytter.credential.domain.international.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title Entity
 * @Description 信用证到单Vo
 * @Author liusy
 * @Date 2018-11-28 19:15
 * @Version 1.0
 */
@Data
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
//    @Corp(inject = "corpName")
    private String corpId;
    /*本次来单金额*/
    @BtCompositeSearch(fieldName = "lb.bill_money")
    private BigDecimal billMoney;
    /*本次付款金额*/
    @BtCompositeSearch(fieldName = "lb.pay_money")
    private BigDecimal payMoney;
    /*币别*/
    @BtCompositeSearch(fieldName = "lb.cur_id")
//    @Currency(inject = "curName")
    private String curId;
    /*汇率*/
    private BigDecimal exchange;

    private String exchangeShow;
    /*手续费*/
    private BigDecimal handlingCharge;
    /*到单日期*/
    @BtCompositeSearch(fieldName = "lb.payment_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentEndDate;
    /*期限*/
    private Integer period;
    /*交单银行*/
    @BtCompositeSearch(fieldName = "lb.hand_doc_bank")
    private String handDocBank;
    /*交单银行编号*/
    private String docBankNo;
    /*付汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lb.pay_date")
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
    private String delayDates;
    /*受益人*/
    @BtCompositeSearch(fieldName = "lc.beneficiary")
    private String beneficiary;
    /*备注*/
    private String rmk;
    /*状态*/
    @BtCompositeSearch(fieldName = "lb.status")
    private String status;
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
    @BtCompositeSearch(fieldName = "lc.cur_name")
    private String curName;

    /**抵押状态**/
    private Integer lmStatus;
    /**抵押Id**/
    private String  lmId;
    /*单位*/
    private String corpName;
    /**信用证类型**/
    @BtCompositeSearch(fieldName = "lc.lc_inter")
    private String lcInter;
    /*合同号*/
    @BtCompositeSearch(fieldName = "lc.contract_no")
    private String contractNo;
    /*开证银行显示名*/
    @BtCompositeSearch(fieldName = "lc.bank_name")
    private String issuingBankName;
    /*开证人*/
    @BtCompositeSearch(fieldName = "sc.name")
    private String applicant;
    @BtCompositeSearch(fieldName = "lc.credit_money")
    private BigDecimal creditMoney;
    /*押汇金额*/
//    @BtCompositeSearch(fieldName = "ld.deposit_money")
    private BigDecimal depositMoney;
    /*押汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ld.deposit_date")
    private Date depositDate;
    /*押汇到期日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "ld.expiry_date")
    private Date expiryDate;
    /*还押汇金额*/
//    @BtCompositeSearch(fieldName ="ld.reapy_money")
    private BigDecimal repayMoney;
    /*还押汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /*关单号*/
    @BtCompositeSearch(fieldName = "lb.close_lc_no")
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
//    public String getLmId() {
//        return lmId;
//    }
//    public void setLmId(String lmId) {
//        this.lmId = lmId;
//    }
//    public Integer getLmStatus() {
//        return lmStatus;
//    }
//    public void setLmStatus(Integer lmStatus) {
//        this.lmStatus = lmStatus;
//    }
//    public String getCurName() {
//        return curName;
//    }
//    public void setCurName(String curName) {
//        this.curName = curName;
//    }
//    public String getCreditNo() {
//        return creditNo;
//    }
//    public void setCreditNo(String creditNo) {
//        this.creditNo = creditNo;
//    }
//    public String getArrivalCode() {
//        return arrivalCode;
//    }
//    public void setArrivalCode(String arrivalCode) {
//        this.arrivalCode = arrivalCode;
//    }
//    public Integer getArrivalType() {
//        return arrivalType;
//    }
//    public void setArrivalType(Integer arrivalType) {
//        this.arrivalType = arrivalType;
//    }
//    public String getArrivalNo() {
//        return arrivalNo;
//    }
//    public void setArrivalNo(String arrivalNo) {
//        this.arrivalNo = arrivalNo;
//    }
//    public String getLcId() {
//        return lcId;
//    }
//    public void setLcId(String lcId) {
//        this.lcId = lcId;
//    }
//    public String getCorpId() {
//        return corpId;
//    }
//    public void setCorpId(String corpId) {
//        this.corpId = corpId;
//    }
//    public BigDecimal getBillMoney() {
//        return billMoney;
//    }
//    public void setBillMoney(BigDecimal billMoney) {
//        this.billMoney = billMoney;
//    }
//    public BigDecimal getPayMoney() {
//        return payMoney;
//    }
//    public void setPayMoney(BigDecimal payMoney) {
//        this.payMoney = payMoney;
//    }
//    public String getCurId() {
//        return curId;
//    }
//    public void setCurId(String curId) {
//        this.curId = curId;
//    }
//    public BigDecimal getExchange() {
//        return exchange;
//    }
//    public void setExchange(BigDecimal exchange) {
//        this.exchange = exchange;
//    }
//    public BigDecimal getHandlingCharge() {
//        return handlingCharge;
//    }
//    public void setHandlingCharge(BigDecimal handlingCharge) {
//        this.handlingCharge = handlingCharge;
//    }
//    public Date getPaymentDate() {
//        return paymentDate;
//    }
//    public void setPaymentDate(Date paymentDate) {
//        this.paymentDate = paymentDate;
//    }
//    public Integer getPeriod() {
//        return period;
//    }
//    public void setPeriod(Integer period) {
//        this.period = period;
//    }
//    public String getHandDocBank() {
//        return handDocBank;
//    }
//    public void setHandDocBank(String handDocBank) {
//        this.handDocBank = handDocBank;
//    }
//    public String getDocBankNo() {
//        return docBankNo;
//    }
//    public void setDocBankNo(String docBankNo) {
//        this.docBankNo = docBankNo;
//    }
//    public Date getPayDate() {
//        return payDate;
//    }
//    public void setPayDate(Date payDate) {
//        this.payDate = payDate;
//    }
//    public BigDecimal getAddTaxMoney() {
//        return addTaxMoney;
//    }
//    public void setAddTaxMoney(BigDecimal addTaxMoney) {
//        this.addTaxMoney = addTaxMoney;
//    }
//    public BigDecimal getTaxMoney() {
//        return taxMoney;
//    }
//    public void setTaxMoney(BigDecimal taxMoney) {
//        this.taxMoney = taxMoney;
//    }
//    public Date getTaxDate() {
//        return taxDate;
//    }
//    public void setTaxDate(Date taxDate) {
//        this.taxDate = taxDate;
//    }
//    public String getTaxType() {
//        return taxType;
//    }
//    public void setTaxType(String taxType) {
//        this.taxType = taxType;
//    }
//    public String getVbCode() {
//        return vbCode;
//    }
//    public void setVbCode(String vbCode) {
//        this.vbCode = vbCode;
//    }
//    public Date getGetDate() {
//        return getDate;
//    }
//    public void setGetDate(Date getDate) {
//        this.getDate = getDate;
//    }
//    public Integer getDelayDates() {
//        return delayDates;
//    }
//    public void setDelayDates(Integer delayDates) {
//        this.delayDates = delayDates;
//    }
//    public String getBeneficiary() {
//        return beneficiary;
//    }
//    public void setBeneficiary(String beneficiary) {
//        this.beneficiary = beneficiary;
//    }
//    public String getRmk() {
//        return rmk;
//    }
//    public void setRmk(String rmk) {
//        this.rmk = rmk;
//    }
////    public Integer getStatus() {
////        return status;
////    }
////    public void setStatus(Integer status) {
////        this.status = status;
////    }
//
//    public String getCreateBy() {
//        return createBy;
//    }
//    public void setCreateBy(String createBy) {
//        this.createBy = createBy;
//    }
//    public Date getCreateDate() {
//        return createDate;
//    }
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }
//    public String getUpdateBy() {
//        return updateBy;
//    }
//    public void setUpdateBy(String updateBy) {
//        this.updateBy = updateBy;
//    }
//    public Date getUpdateDate() {
//        return updateDate;
//    }
//    public void setUpdateDate(Date updateDate) {
//        this.updateDate = updateDate;
//    }
//    public String getContractNo() {
//        return contractNo;
//    }
//    public void setContractNo(String contractNo) {
//        this.contractNo = contractNo;
//    }
//    public String getIssuingBankName() {
//        return issuingBankName;
//    }
//    public void setIssuingBankName(String issuingBankName) {
//        this.issuingBankName = issuingBankName;
//    }
//    public String getApplicant() {
//        return applicant;
//    }
//    public void setApplicant(String applicant) {
//        this.applicant = applicant;
//    }
//    public BigDecimal getCreditMoney() {
//        return creditMoney;
//    }
//    public void setCreditMoney(BigDecimal creditMoney) {
//        this.creditMoney = creditMoney;
//    }
//    public BigDecimal getDepositMoney() {
//        return depositMoney;
//    }
//    public void setDepositMoney(BigDecimal depositMoney) {
//        this.depositMoney = depositMoney;
//    }
//    public Date getDepositDate() {
//        return depositDate;
//    }
//    public void setDepositDate(Date depositDate) {
//        this.depositDate = depositDate;
//    }
//    public Date getExpiryDate() {
//        return expiryDate;
//    }
//    public void setExpiryDate(Date expiryDate) {
//        this.expiryDate = expiryDate;
//    }
//    public BigDecimal getRepayMoney() {
//        return repayMoney;
//    }
//    public void setRepayMoney(BigDecimal repayMoney) {
//        this.repayMoney = repayMoney;
//    }
//    public Date getRepayDate() {
//        return repayDate;
//    }
//    public void setRepayDate(Date repayDate) {
//        this.repayDate = repayDate;
//    }
//    public String getCloseLcNo() {
//        return closeLcNo;
//    }
//    public void setCloseLcNo(String closeLcNo) {
//        this.closeLcNo = closeLcNo;
//    }
//    public Integer getStatus2() {
//        return status2;
//    }
//    public void setStatus2(Integer status2) {
//        this.status2 = status2;
//    }
//    public String getOpeningBankName() {
//        return openingBankName;
//    }
//    public void setOpeningBankName(String openingBankName) {
//        this.openingBankName = openingBankName;
//    }
//    public String getInvoiceNo() {
//        return invoiceNo;
//    }
//    public void setInvoiceNo(String invoiceNo) {
//        this.invoiceNo = invoiceNo;
//    }
//    public String getBankFlowSign() {
//        return bankFlowSign;
//    }
//    public void setBankFlowSign(String bankFlowSign) {
//        this.bankFlowSign = bankFlowSign;
//    }
//    public String getAttachmentId() {
//        return attachmentId;
//    }
//    public void setAttachmentId(String attachmentId) {
//        this.attachmentId = attachmentId;
//    }
}
