package com.bytter.increment.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: DTO
 * @Description: 内部理财信息表
 * @author huangx
 * @date 2019-11-1 14:03:30
 * @version 1.0
 *
 */
public class ChoInnerFinanceDTO implements Serializable {
    private String id;
    /**业务单号*/
    private String billNo;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "f.corp_Id")
    @Corp(inject="corpName")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约*/
    private Integer busType;
    /**付款账号ID*/
    private String payerAccId;
    /**付款账号*/
    @BtCompositeSearch(fieldName = "f.payer_Acc")
    private String payerAcc;
    /**付款账户名称*/
    private String payerAccName;
    /**收款账号ID*/
    private String payeeAccId;
    /**收款账号*/
    private String payeeAcc;
    /**本金存入账号ID*/
    private String capitalAccId;
    /**本金存入账号*/
    private String capitalAcc;
    /**本金存入账户名称*/
    private String capitalAccName;
    /**利息存入账号ID*/
    private String interestAccId;
    /**利息存入账号*/
    private String interestAcc;
    /**利息存入账户名称*/
    private String interestAccName;
    /**存款类型*/
    @BtCompositeSearch(fieldName = "f.deposit_rate_type")
    private String depositRateType;
    /**存款类型名称*/
    private String depositRateTypeName;
    /**币别*/
    @BtCompositeSearch(fieldName = "f.cur_id")
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    private String curName;
    /**金额*/
    @BtCompositeSearch(fieldName = "f.pay_Money")
    private BigDecimal payMoney;
    /**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
    private Integer dayLimit;
    /**日期单位 Y-年、M-月、D-日*/
    private String dateUnit;
    /**通知类型*/
    private String noticeType;
    /**到期处理方式 0-自动终止、1-本金续存、2-本息续存*/
    private Integer certificateWay;
    /**起息日期*/
    @BtCompositeSearch(fieldName = "f.interest_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName = "f.deadline_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /**用途*/
    private String purpose;
    /**备注*/
    private String addWord;
    /**利率*/
    private BigDecimal interestRate;
    /**取款日期*/
    @BtCompositeSearch(fieldName = "f.pay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    /**关联单据ID*/
    private String relatedBillId;
    /**关联单据号*/
    private String relatedBillNo;
    /**预计利息*/
    private BigDecimal expectInterest;
    /**状态*/
    private Integer status;
    /**记账日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;
    /**凭证流水号*/
    private String orderNo;
    /**记账凭证号码*/
    private String voucherNo;
    /**记账标志*/
    private String tallySign;
    /**记账员代码*/
    private String tallyCode;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    
    /**冲预提金额*/
    private BigDecimal rushMoney;
    /**网点*/
    private String netId;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public Integer getBusType() {
        return busType;
    }
    public void setBusType(Integer busType) {
        this.busType = busType;
    }
    public String getPayerAccId() {
        return payerAccId;
    }
    public void setPayerAccId(String payerAccId) {
        this.payerAccId = payerAccId;
    }
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    public String getPayerAccName() {
        return payerAccName;
    }
    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }
    public String getPayeeAccId() {
        return payeeAccId;
    }
    public void setPayeeAccId(String payeeAccId) {
        this.payeeAccId = payeeAccId;
    }
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    public String getCapitalAccId() {
        return capitalAccId;
    }
    public void setCapitalAccId(String capitalAccId) {
        this.capitalAccId = capitalAccId;
    }
    public String getCapitalAcc() {
        return capitalAcc;
    }
    public void setCapitalAcc(String capitalAcc) {
        this.capitalAcc = capitalAcc;
    }
    public String getCapitalAccName() {
        return capitalAccName;
    }
    public void setCapitalAccName(String capitalAccName) {
        this.capitalAccName = capitalAccName;
    }
    public String getInterestAccId() {
        return interestAccId;
    }
    public void setInterestAccId(String interestAccId) {
        this.interestAccId = interestAccId;
    }
    public String getInterestAcc() {
        return interestAcc;
    }
    public void setInterestAcc(String interestAcc) {
        this.interestAcc = interestAcc;
    }
    public String getInterestAccName() {
        return interestAccName;
    }
    public void setInterestAccName(String interestAccName) {
        this.interestAccName = interestAccName;
    }
    public String getDepositRateType() {
        return depositRateType;
    }
    public void setDepositRateType(String depositRateType) {
        this.depositRateType = depositRateType;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    public String getDateUnit() {
        return dateUnit;
    }
    public void setDateUnit(String dateUnit) {
        this.dateUnit = dateUnit;
    }
    public String getNoticeType() {
        return noticeType;
    }
    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }
    public Integer getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getAddWord() {
        return addWord;
    }
    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public String getRelatedBillId() {
        return relatedBillId;
    }
    public void setRelatedBillId(String relatedBillId) {
        this.relatedBillId = relatedBillId;
    }
    public String getRelatedBillNo() {
        return relatedBillNo;
    }
    public void setRelatedBillNo(String relatedBillNo) {
        this.relatedBillNo = relatedBillNo;
    }
    public BigDecimal getExpectInterest() {
        return expectInterest;
    }
    public void setExpectInterest(BigDecimal expectInterest) {
        this.expectInterest = expectInterest;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getTallySign() {
        return tallySign;
    }
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    public String getTallyCode() {
        return tallyCode;
    }
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
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
    public BigDecimal getRushMoney() {
        return rushMoney;
    }
    public void setRushMoney(BigDecimal rushMoney) {
        this.rushMoney = rushMoney;
    }
    public String getDepositRateTypeName() {
        return depositRateTypeName;
    }
    public void setDepositRateTypeName(String depositRateTypeName) {
        this.depositRateTypeName = depositRateTypeName;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
}
