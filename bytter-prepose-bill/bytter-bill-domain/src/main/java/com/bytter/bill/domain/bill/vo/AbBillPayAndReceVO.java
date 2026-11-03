package com.bytter.bill.domain.bill.vo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.Lang;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: AbBillPayAndReceVO
 * @Description: 应收应付票据VO
 * @author wangzy
 * @date 2019/7/17 14:02
 * @version V1.0
 *
 */
public class AbBillPayAndReceVO implements Serializable {
    @BtCompositeSearch(fieldName="id")
    private String id;
    /**票据从表id*/
    @BtCompositeSearch(fieldName="viceId")
    private String viceId;
    /** 付款日期*/
    @BtCompositeSearch(fieldName="payDate")
    private Date payDate;
    @BtCompositeSearch(fieldName="payDate")
    private String payDateStr;
    /**转让方式*/
    // @Dict(dictGroup = "AB_BILL_TRANSFERWAY", inject ="transferWayName")
    @BtCompositeSearch(fieldName="transferWay")
    private String transferWay;
    @BtCompositeSearch(fieldName="transferWay")
    private String transferWayName;
    /**持票人银行*/
    @BtCompositeSearch(fieldName="bankName")
    private String bankName;
    /**出票人银行*/
    @BtCompositeSearch(fieldName="payerBankName")
    private String payerBankName;
    /**收票日期*/
    @Excel(name = "*收票日期",format = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="receDate")
    private Date receDate;
    /**持票单位*/
    @BtCompositeSearch(fieldName="corpId")
    // @Corp(inject="corpName")
    private String corpId;
    private String corpName;
    /**持票单位代码加名称*/
    @BtCompositeSearch(fieldName="corpCodeAndName")
    private String corpCodeAndName;
    /**转让日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="transferDate")
    private Date transferDate;
    /**出票日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="startDate")
    private Date startDate;
    /**票据类型：10 银票、11 商票*/
    // @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    @BtCompositeSearch(fieldName="billType")
    private Integer billType;
    /**票据类型名称：10 银票、11 商票*/
    @BtCompositeSearch(fieldName="billType")
    private String billTypeName;
    /**流通介质：10 纸票、11 电票*/
    // @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    @BtCompositeSearch(fieldName="billFlag")
    private Integer billFlag;
    /**流通介质名称：10 纸票、11 电票*/
    @BtCompositeSearch(fieldName="billFlag")
    private String billFlagName;
    /**票据号码*/
    @BtCompositeSearch(fieldName="origNo")
    private String origNo;
    /**出票人*/
    @BtCompositeSearch(fieldName="payer")
    private String payer;
    /**出票人帐号*/
    @BtCompositeSearch(fieldName="payerAcc")
    private String payerAcc;
    /**承兑人*/
    @BtCompositeSearch(fieldName="acceptor")
    private String acceptor;
    /**到期日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="endDate")
    private Date endDate;
    /**前手*/
    @BtCompositeSearch(fieldName="cnAccrecords")
    private String cnAccrecords;
    /**前手帐号*/
    @BtCompositeSearch(fieldName="cnAccrecordsAcc")
    private String cnAccrecordsAcc;
    /**前手开户行*/
    @BtCompositeSearch(fieldName="cnAccrecordsBankName")
    private String cnAccrecordsBankName;
    /**票据金额*/
    @BtCompositeSearch(fieldName="billMoney")
    private BigDecimal billMoney;
    /**到期解付金额*/
    @BtCompositeSearch(fieldName="payMoney")
    private BigDecimal payMoney;
    /**后手*/
    @BtCompositeSearch(fieldName="rearAccrecords")
    private String rearAccrecords;
    /**贴现利率*/
    @BtCompositeSearch(fieldName="interestRates")
    private BigDecimal interestRates;
    private String interestRatesStr;
    /**贴现利息*/
    @BtCompositeSearch(fieldName="interest")
    private BigDecimal interest;
    /**贴现金额*/
    @BtCompositeSearch(fieldName="realMoney")
    private BigDecimal realMoney;
    /**收票人全称*/
    @BtCompositeSearch(fieldName="payee")
    private String payee;
    /**收票人全称*/
    @BtCompositeSearch(fieldName="payeeacc")
    private String payeeacc;
    @BtCompositeSearch(fieldName="payeebankno")
    private String payeebankno;
    @BtCompositeSearch(fieldName="payeeBankName")
    private String payeeBankName;
    /**到期提醒*/
    @Lang(inject = "dueRemindStr")
    private String dueRemind;
    private String dueRemindStr;
    /**到期天数*/
    @BtCompositeSearch(fieldName="expireDays")
    private String expireDays;
    /**票据异常情况*/
    @Lang(inject = "billAnomaliesStr")
    private String billAnomalies;
    private String billAnomaliesStr;
    /**保证金*/
    @BtCompositeSearch(fieldName="bailMoney")
    private BigDecimal bailMoney;
    /**保证金比率*/
    @BtCompositeSearch(fieldName="bailPercent")
    private BigDecimal bailPercent;
    /**业务类型*/
    // @Dict(dictGroup = "AB_BRIEF_STATUS", inject ="briefIdName")
    @BtCompositeSearch(fieldName="briefId")
    private String briefId;
    @BtCompositeSearch(fieldName="briefId")
    private String briefIdName;
    /**开票方式*/
    // @Dict(dictGroup = "ACCEBILL_EMIT_TYPE", inject ="emitTypeName")
    @BtCompositeSearch(fieldName="emitType")
    private String emitType;
    @BtCompositeSearch(fieldName="emitType")
    private String emitTypeName;
    /**是否开票保贴(1是0否) */
    // @Dict(dictGroup="sf_yn",inject = "emitGuandisSignName")
    @BtCompositeSearch(fieldName="emitGuandisSign")
    private String emitGuandisSign;
    private String emitGuandisSignName;
    /**开票完成(1是0否) */
    // @Dict(dictGroup="sf_yn",inject = "emitOpenOkName")
    @BtCompositeSearch(fieldName="emitOpenOk")
    private String emitOpenOk;
    @BtCompositeSearch(fieldName="emitOpenOk")
    private String emitOpenOkName;
    /**开票完成日期 */
    @BtCompositeSearch(fieldName="emitOpenDate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date emitOpenDate;
    /**承兑合同号 */
    private String contractNo;
    /**是否已付款 */
    // @Dict(dictGroup="sf_yn",inject = "isPayName")
    @BtCompositeSearch(fieldName="isPay")
    private String isPay;
    @BtCompositeSearch(fieldName="isPay")
    private String isPayName;
    /**票据状态*/
    // @Dict(dictGroup = "AB_BILL_STATUS", inject="billStatusName")
    @BtCompositeSearch(fieldName="billStatus")
    private String billStatus;
    @BtCompositeSearch(fieldName="billStatus")
    private String billStatusName;
    /**流通状态*/
    @BtCompositeSearch(fieldName="ebillBlCrclIndc")
    private String ebillBlCrclIndc;
    @BtCompositeSearch(fieldName="ebillBlCrclIndc")
    private String ebillBlCrclIndcName;
    @BtCompositeSearch(fieldName="banktypeid")
    private String bankTypeId;
    /**是否发送指令标志：0否； 1是 ；*/
    @BtCompositeSearch(fieldName="sendSign")
    // @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
    private String sendSign;
    @BtCompositeSearch(fieldName="sendSign")
    private String sendSignName;
    /**
     * 背书转让次数
     */
    @BtCompositeSearch(fieldName="endorsementCount")
    private String endorsementCount;
    /** 申请单位 */
    @BtCompositeSearch(fieldName="applyCorpId")
    // @Corp(inject="applyCorpName")
    private String applyCorpId;
    /**申请单位名称*/
    @BtCompositeSearch(fieldName="applyCorpName")
    private String applyCorpName;
    /**申请单位代码*/
    private String applyCorpCode;
    /**申请人*/
    @BtCompositeSearch(fieldName="applyName")
    private String applyName;
    /** 业务单位 */
    // @Corp(inject="busCorpName")
    @BtCompositeSearch(fieldName="busCorpId")
    private String busCorpId;
    /** 业务单位名称 */
    @BtCompositeSearch(fieldName="busCorpName")
    private String busCorpName;
    /** 业务人员 */
    @BtCompositeSearch(fieldName="busPersonnel")
    private String busPersonnel;
    /**
     * 票据单位账号
     */
    @BtCompositeSearch(fieldName="bankAcc")
    private String bankAcc;
    /**
     * 票据池明细id
     */
    @BtCompositeSearch(fieldName="pDID")
    private String pDID;
    public String getpDID() {
        return pDID;
    }
    public void setpDID(String pDID) {
        this.pDID = pDID;
    }
    /**内部业务标志：0  否；1 是*/
    private String insideSing;
    /**
     * 出票标志
     */
    @BtCompositeSearch(fieldName = "newStmIssuofpprInd")
    @Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
    private String newStmIssuofpprInd;
    private String newStmIssuofpprIndName;
    /**
     * 是否允许分包
     */
    @BtCompositeSearch(fieldName = "blgrdbagcrclPrmtInd")
    @Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;
    private String blgrdbagcrclPrmtIndName;
    /**
     * 子票区间开始序号
     */
    @BtCompositeSearch(fieldName = "sbbllRngStrtSn")
    private String sbbllRngStrtSn;
    /**
     * 子票区间结束序号
     */
    @BtCompositeSearch(fieldName = "sbbllRngEndSn")
    private String sbbllRngEndSn;
    /**
     * 标准金额
     */
    private BigDecimal blStdAmt;
    /**
     * 出票人统一信用代码
     */
    private String blRmtrUnnIdNo;
    /**
     * 备注
     */
    @BtCompositeSearch(fieldName = "rmk")
    private String rmk;
    /**
     * 备注说明
     */
    @BtCompositeSearch(fieldName = "rmkDescr")
    private String rmkDescr;
    /**
     * 付款单号
     */
    @BtCompositeSearch(fieldName = "payNo")
    private String payNo;
    /**
     * 付款ID
     */
    private String payId;
    /**
     * 共享单号
     */
    @BtCompositeSearch(fieldName = "bursarBillNo")
    private String bursarBillNo;

    private BigDecimal sumPayMoney;

    private String bankCode;

    private String recAcctId;

    private String recAcctsvcr;

    private String recAcctBankCode;

    private String cnAccrecordsBankCode;

    @BtCompositeSearch(fieldName = "optStatus")
    private String optStatus;
    private String optStatusName;

    @BtCompositeSearch(fieldName = "status")
    private String status;
    private String statusName;

    /**
     * 最终审批人
     */
    private String finalApprover;

    public String getFinalApprover() {
        return finalApprover;
    }

    public void setFinalApprover(String finalApprover) {
        this.finalApprover = finalApprover;
    }

    public String getOptStatus() {
        return optStatus;
    }

    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(pattern="yyyy-MM-dd")
    @BtCompositeSearch(fieldName="applyDate")
    private Date applyDate;

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getCnAccrecordsBankCode() {
        return cnAccrecordsBankCode;
    }

    public void setCnAccrecordsBankCode(String cnAccrecordsBankCode) {
        this.cnAccrecordsBankCode = cnAccrecordsBankCode;
    }

    public String getRecAcctBankCode() {
        return recAcctBankCode;
    }

    public void setRecAcctBankCode(String recAcctBankCode) {
        this.recAcctBankCode = recAcctBankCode;
    }

    public String getRecAcctId() {
        return recAcctId;
    }

    public void setRecAcctId(String recAcctId) {
        this.recAcctId = recAcctId;
    }

    public String getRecAcctsvcr() {
        return recAcctsvcr;
    }

    public void setRecAcctsvcr(String recAcctsvcr) {
        this.recAcctsvcr = recAcctsvcr;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public BigDecimal getSumPayMoney() {
        return sumPayMoney;
    }

    public void setSumPayMoney(BigDecimal sumPayMoney) {
        this.sumPayMoney = sumPayMoney;
    }

    //    private String ebillBlCrclIndc;

    public String getEbillBlCrclIndc() {
        return ebillBlCrclIndc;
    }

    public void setEbillBlCrclIndc(String ebillBlCrclIndc) {
        this.ebillBlCrclIndc = ebillBlCrclIndc;
    }

    public BigDecimal getBailPercent() {
        return bailPercent;
    }

    public void setBailPercent(BigDecimal bailPercent) {
        this.bailPercent = bailPercent;
    }

    public String getPayerAcc() {
        return payerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    public String getExpireDays() {
        return expireDays;
    }

    public void setExpireDays(String expireDays) {
        this.expireDays = expireDays;
    }

    public String getCnAccrecordsAcc() {
        return cnAccrecordsAcc;
    }

    public void setCnAccrecordsAcc(String cnAccrecordsAcc) {
        this.cnAccrecordsAcc = cnAccrecordsAcc;
    }

    public String getCnAccrecordsBankName() {
        return cnAccrecordsBankName;
    }

    public void setCnAccrecordsBankName(String cnAccrecordsBankName) {
        this.cnAccrecordsBankName = cnAccrecordsBankName;
    }

    public String getNewStmIssuofpprInd() {
        return newStmIssuofpprInd;
    }
    public void setNewStmIssuofpprInd(String newStmIssuofpprInd) {
        this.newStmIssuofpprInd = newStmIssuofpprInd;
    }
    public String getNewStmIssuofpprIndName() {
        return newStmIssuofpprIndName;
    }
    public void setNewStmIssuofpprIndName(String newStmIssuofpprIndName) {
        this.newStmIssuofpprIndName = newStmIssuofpprIndName;
    }
    public String getBlgrdbagcrclPrmtInd() {
        return blgrdbagcrclPrmtInd;
    }
    public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
        this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
    }
    public String getBlgrdbagcrclPrmtIndName() {
        return blgrdbagcrclPrmtIndName;
    }
    public void setBlgrdbagcrclPrmtIndName(String blgrdbagcrclPrmtIndName) {
        this.blgrdbagcrclPrmtIndName = blgrdbagcrclPrmtIndName;
    }
    public String getSbbllRngStrtSn() {
        return sbbllRngStrtSn;
    }
    public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
        this.sbbllRngStrtSn = sbbllRngStrtSn;
    }
    public String getSbbllRngEndSn() {
        return sbbllRngEndSn;
    }
    public void setSbbllRngEndSn(String sbbllRngEndSn) {
        this.sbbllRngEndSn = sbbllRngEndSn;
    }
    public BigDecimal getBlStdAmt() {
        return blStdAmt;
    }
    public void setBlStdAmt(BigDecimal blStdAmt) {
        this.blStdAmt = blStdAmt;
    }
    public String getBlRmtrUnnIdNo() {
        return blRmtrUnnIdNo;
    }
    public void setBlRmtrUnnIdNo(String blRmtrUnnIdNo) {
        this.blRmtrUnnIdNo = blRmtrUnnIdNo;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getEndorsementCount() {
        return endorsementCount;
    }
    public void setEndorsementCount(String endorsementCount) {
        this.endorsementCount = endorsementCount;
    }
    public String getDueRemind() {
        return dueRemind;
    }
    public void setDueRemind(String dueRemind) {
        this.dueRemind = dueRemind;
    }
    public String getBillAnomalies() {
        return billAnomalies;
    }
    public void setBillAnomalies(String billAnomalies) {
        this.billAnomalies = billAnomalies;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        if(payDate != null){
            this.setPayDateStr(DateUtils.formatDate(payDate));
        }
        this.payDate = payDate;
        this.setBillInfo();
        this.setDueInfo();
    }
    public String getPayDateStr() {
        return payDateStr;
    }
    public void setPayDateStr(String payDateStr) {
        this.payDateStr = payDateStr;
    }
    public String getTransferWay() {
        return transferWay;
    }
    public void setTransferWay(String transferWay) {
        this.transferWay = transferWay;
        this.setBillInfo();
    }
    public String getTransferWayName() {
        return transferWayName;
    }
    public void setTransferWayName(String transferWayName) {
        this.transferWayName = transferWayName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getPayerBankName() {
        return payerBankName;
    }
    public void setPayerBankName(String payerBankName) {
        this.payerBankName = payerBankName;
    }
    public Date getReceDate() {
        return receDate;
    }
    public void setReceDate(Date receDate) {
        this.receDate = receDate;
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
    public String getCorpCodeAndName() {
        return corpCodeAndName;
    }
    public void setCorpCodeAndName(String corpCodeAndName) {
        this.corpCodeAndName = corpCodeAndName;
    }
    public Date getTransferDate() {
        return transferDate;
    }
    public void setTransferDate(Date transferDate) {
        this.transferDate = transferDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Integer getBillType() {
        return billType;
    }
    public void setBillType(Integer billType) {
        this.billType = billType;
    }
    public String getBillTypeName() {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }
    public Integer getBillFlag() {
        return billFlag;
    }
    public void setBillFlag(Integer billFlag) {
        this.billFlag = billFlag;
    }
    public String getBillFlagName() {
        return billFlagName;
    }
    public void setBillFlagName(String billFlagName) {
        this.billFlagName = billFlagName;
    }
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
    }
    public String getPayer() {
        return payer;
    }
    public void setPayer(String payer) {
        this.payer = payer;
    }
    public String getAcceptor() {
        return acceptor;
    }
    public void setAcceptor(String acceptor) {
        this.acceptor = acceptor;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
        this.setBillInfo();
        this.setDueInfo();
    }
    public String getCnAccrecords() {
        return cnAccrecords;
    }
    public void setCnAccrecords(String cnAccrecords) {
        this.cnAccrecords = cnAccrecords;
    }
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        if(StringUtils.isNotEmpty(billMoney)){
            this.billMoney = new BigDecimal(NumberUtil.formatNumeric(billMoney,"#0.00"));
        }else {
            this.billMoney = billMoney;
        }
    }
    public String getRearAccrecords() {
        return rearAccrecords;
    }
    public void setRearAccrecords(String rearAccrecords) {
        this.rearAccrecords = rearAccrecords;
    }
    public BigDecimal getInterestRates() {
        return interestRates;
    }
    public void setInterestRates(BigDecimal interestRates) {
        if(interestRates != null){
            this.setInterestRatesStr(NumberUtil.formatNumeric(interestRates.multiply(new BigDecimal(100)),"#0.0000")+"%");
        }
        this.interestRates = interestRates;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        if(StringUtils.isNotEmpty(interest)){
            this.interest = new BigDecimal(NumberUtil.formatNumeric(interest,"#0.00"));
        }else {
            this.interest = interest;
        }
    }
    public BigDecimal getRealMoney() {
        return realMoney;
    }
    public void setRealMoney(BigDecimal realMoney) {
        if(StringUtils.isNotEmpty(realMoney)){
            this.realMoney = new BigDecimal(NumberUtil.formatNumeric(realMoney,"#0.00"));
        }else {
            this.realMoney = realMoney;
        }
    }
    public String getDueRemindStr() {
        return dueRemind != null ? DateUtils.dayDiffs(endDate, DateUtils.getDateYMD())+"天" : null;
    }
    public void setDueRemindStr(String dueRemindStr) {
        this.dueRemindStr = dueRemindStr;
    }
    public String getBillAnomaliesStr() {
        return billAnomaliesStr;
    }
    public void setBillAnomaliesStr(String billAnomaliesStr) {
        this.billAnomaliesStr = billAnomaliesStr;
    }
    public String getPayee() {
        return payee;
    }
    public void setPayee(String payee) {
        this.payee = payee;
    }
    public String getInterestRatesStr() {
        return interestRatesStr;
    }
    public void setInterestRatesStr(String interestRatesStr) {
        this.interestRatesStr = interestRatesStr;
    }
    public BigDecimal getBailMoney() {
        return bailMoney;
    }
    public void setBailMoney(BigDecimal bailMoney) {
        if(StringUtils.isNotEmpty(bailMoney)){
            this.bailMoney = new BigDecimal(NumberUtil.formatNumeric(bailMoney,"#0.00"));
        }else {
            this.bailMoney = bailMoney;
        }
    }
    public String getBriefId() {
        return briefId;
    }
    public void setBriefId(String briefId) {
        this.briefId = briefId;
    }
    public String getBriefIdName() {
        return briefIdName;
    }
    public void setBriefIdName(String briefIdName) {
        this.briefIdName = briefIdName;
    }
    public String getEmitType() {
        return emitType;
    }
    public void setEmitType(String emitType) {
        this.emitType = emitType;
    }
    public String getEmitTypeName() {
        return emitTypeName;
    }
    public void setEmitTypeName(String emitTypeName) {
        this.emitTypeName = emitTypeName;
    }
    public String getEmitGuandisSign() {
        return emitGuandisSign;
    }
    public void setEmitGuandisSign(String emitGuandisSign) {
        this.emitGuandisSign = emitGuandisSign;
    }
    public String getEmitGuandisSignName() {
        return emitGuandisSignName;
    }
    public void setEmitGuandisSignName(String emitGuandisSignName) {
        this.emitGuandisSignName = emitGuandisSignName;
    }
    public String getViceId() {
        return viceId;
    }
    public void setViceId(String viceId) {
        this.viceId = viceId;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getIsPay() {
        return isPay;
    }
    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }
    /**处理逾期情况*/
    private void setBillInfo(){
        if(endDate == null){
            return;
        }
        if(endDate.getTime() < DateUtils.getDateYMD().getTime() && payDate == null && transferWay == null
                && !AccebillConstant.AB_BILL_STATUS_000002.equals(billStatus)){
            this.setBillAnomalies("accbill.overdue");
        }else{
            this.setBillAnomalies(null);
        }
    }
    /**到期提醒*/
    private void setDueInfo(){
        if(endDate == null){
            return;
        }
        if(endDate.getTime() > DateUtils.getDateYMD().getTime() && payDate == null){
            this.setDueRemind("accbill.day");
        }else {
            this.setDueRemind(null);
        }
    }
    public String getBillStatus() {
        return billStatus;
    }
    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }
    public String getBillStatusName() {
        return billStatusName;
    }
    public void setBillStatusName(String billStatusName) {
        this.billStatusName = billStatusName;
    }
    public String getEmitOpenOk() {
        return emitOpenOk;
    }
    public void setEmitOpenOk(String emitOpenOk) {
        this.emitOpenOk = emitOpenOk;
    }
    public String getEmitOpenOkName() {
        return emitOpenOkName;
    }
    public void setEmitOpenOkName(String emitOpenOkName) {
        this.emitOpenOkName = emitOpenOkName;
    }
    public String getIsPayName() {
        return isPayName;
    }
    public void setIsPayName(String isPayName) {
        this.isPayName = isPayName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getEmitOpenDate() {
        return emitOpenDate;
    }
    public void setEmitOpenDate(Date emitOpenDate) {
        this.emitOpenDate = emitOpenDate;
    }
    public String getSendSign() {
        return sendSign;
    }
    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
    }
    public String getSendSignName() {
        return sendSignName;
    }
    public void setSendSignName(String sendSignName) {
        this.sendSignName = sendSignName;
    }
    public String getApplyCorpId() {
        return applyCorpId;
    }
    public void setApplyCorpId(String applyCorpId) {
        this.applyCorpId = applyCorpId;
    }
    public String getApplyCorpName() {
        return applyCorpName;
    }
    public void setApplyCorpName(String applyCorpName) {
        this.applyCorpName = applyCorpName;
    }
    public String getApplyCorpCode() {
        return applyCorpCode;
    }
    public void setApplyCorpCode(String applyCorpCode) {
        this.applyCorpCode = applyCorpCode;
    }
    public String getBusCorpId() {
        return busCorpId;
    }
    public void setBusCorpId(String busCorpId) {
        this.busCorpId = busCorpId;
    }
    public String getBusCorpName() {
        return busCorpName;
    }
    public void setBusCorpName(String busCorpName) {
        this.busCorpName = busCorpName;
    }
    public String getBusPersonnel() {
        return busPersonnel;
    }
    public void setBusPersonnel(String busPersonnel) {
        this.busPersonnel = busPersonnel;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getInsideSing() {
        return insideSing;
    }
    public void setInsideSing(String insideSing) {
        this.insideSing = insideSing;
    }

    public String getPayeeacc() {
        return payeeacc;
    }

    public void setPayeeacc(String payeeacc) {
        this.payeeacc = payeeacc;
    }

    public String getPayeebankno() {
        return payeebankno;
    }

    public void setPayeebankno(String payeebankno) {
        this.payeebankno = payeebankno;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getEbillBlCrclIndcName() {
        return ebillBlCrclIndcName;
    }

    public void setEbillBlCrclIndcName(String ebillBlCrclIndcName) {
        this.ebillBlCrclIndcName = ebillBlCrclIndcName;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getBursarBillNo() {
        return bursarBillNo;
    }

    public void setBursarBillNo(String bursarBillNo) {
        this.bursarBillNo = bursarBillNo;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getRmkDescr() {
        return rmkDescr;
    }

    public void setRmkDescr(String rmkDescr) {
        this.rmkDescr = rmkDescr;
    }

    public String getOptStatusName() {
        return optStatusName;
    }

    public void setOptStatusName(String optStatusName) {
        this.optStatusName = optStatusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return "AbBillPayAndReceVO{" +
                "id='" + id + '\'' +
                ", viceId='" + viceId + '\'' +
                ", payDate=" + payDate +
                ", payDateStr='" + payDateStr + '\'' +
                ", transferWay='" + transferWay + '\'' +
                ", transferWayName='" + transferWayName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", payerBankName='" + payerBankName + '\'' +
                ", receDate=" + receDate +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", corpCodeAndName='" + corpCodeAndName + '\'' +
                ", transferDate=" + transferDate +
                ", startDate=" + startDate +
                ", billType=" + billType +
                ", billTypeName='" + billTypeName + '\'' +
                ", billFlag=" + billFlag +
                ", billFlagName='" + billFlagName + '\'' +
                ", origNo='" + origNo + '\'' +
                ", payer='" + payer + '\'' +
                ", payerAcc='" + payerAcc + '\'' +
                ", acceptor='" + acceptor + '\'' +
                ", endDate=" + endDate +
                ", cnAccrecords='" + cnAccrecords + '\'' +
                ", cnAccrecordsAcc='" + cnAccrecordsAcc + '\'' +
                ", cnAccrecordsBankName='" + cnAccrecordsBankName + '\'' +
                ", billMoney=" + billMoney +
                ", payMoney=" + payMoney +
                ", rearAccrecords='" + rearAccrecords + '\'' +
                ", interestRates=" + interestRates +
                ", interestRatesStr='" + interestRatesStr + '\'' +
                ", interest=" + interest +
                ", realMoney=" + realMoney +
                ", payee='" + payee + '\'' +
                ", payeeacc='" + payeeacc + '\'' +
                ", payeebankno='" + payeebankno + '\'' +
                ", payeeBankName='" + payeeBankName + '\'' +
                ", dueRemind='" + dueRemind + '\'' +
                ", dueRemindStr='" + dueRemindStr + '\'' +
                ", expireDays='" + expireDays + '\'' +
                ", billAnomalies='" + billAnomalies + '\'' +
                ", billAnomaliesStr='" + billAnomaliesStr + '\'' +
                ", bailMoney=" + bailMoney +
                ", bailPercent=" + bailPercent +
                ", briefId='" + briefId + '\'' +
                ", briefIdName='" + briefIdName + '\'' +
                ", emitType='" + emitType + '\'' +
                ", emitTypeName='" + emitTypeName + '\'' +
                ", emitGuandisSign='" + emitGuandisSign + '\'' +
                ", emitGuandisSignName='" + emitGuandisSignName + '\'' +
                ", emitOpenOk='" + emitOpenOk + '\'' +
                ", emitOpenOkName='" + emitOpenOkName + '\'' +
                ", emitOpenDate=" + emitOpenDate +
                ", contractNo='" + contractNo + '\'' +
                ", isPay='" + isPay + '\'' +
                ", isPayName='" + isPayName + '\'' +
                ", billStatus='" + billStatus + '\'' +
                ", billStatusName='" + billStatusName + '\'' +
                ", ebillBlCrclIndc='" + ebillBlCrclIndc + '\'' +
                ", ebillBlCrclIndcName='" + ebillBlCrclIndcName + '\'' +
                ", bankTypeId='" + bankTypeId + '\'' +
                ", sendSign='" + sendSign + '\'' +
                ", sendSignName='" + sendSignName + '\'' +
                ", endorsementCount='" + endorsementCount + '\'' +
                ", applyCorpId='" + applyCorpId + '\'' +
                ", applyCorpName='" + applyCorpName + '\'' +
                ", applyCorpCode='" + applyCorpCode + '\'' +
                ", applyName='" + applyName + '\'' +
                ", busCorpId='" + busCorpId + '\'' +
                ", busCorpName='" + busCorpName + '\'' +
                ", busPersonnel='" + busPersonnel + '\'' +
                ", bankAcc='" + bankAcc + '\'' +
                ", pDID='" + pDID + '\'' +
                ", insideSing='" + insideSing + '\'' +
                ", newStmIssuofpprInd='" + newStmIssuofpprInd + '\'' +
                ", newStmIssuofpprIndName='" + newStmIssuofpprIndName + '\'' +
                ", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
                ", blgrdbagcrclPrmtIndName='" + blgrdbagcrclPrmtIndName + '\'' +
                ", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
                ", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
                ", blStdAmt=" + blStdAmt +
                ", blRmtrUnnIdNo='" + blRmtrUnnIdNo + '\'' +
                ", rmk='" + rmk + '\'' +
                ", rmkDescr='" + rmkDescr + '\'' +
                ", payNo='" + payNo + '\'' +
                ", payId='" + payId + '\'' +
                ", bursarBillNo='" + bursarBillNo + '\'' +
                ", sumPayMoney=" + sumPayMoney +
                ", bankCode='" + bankCode + '\'' +
                ", recAcctId='" + recAcctId + '\'' +
                ", recAcctsvcr='" + recAcctsvcr + '\'' +
                ", recAcctBankCode='" + recAcctBankCode + '\'' +
                ", cnAccrecordsBankCode='" + cnAccrecordsBankCode + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", status='" + status + '\'' +
                ", applyDate=" + applyDate +
                '}';
    }
}
