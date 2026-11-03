package com.bytter.accountReceivable.writeOff.vo;

import com.bytter.core.annotation.*;
import com.bytter.core.constant.AccebillConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.NumberUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;

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
public class AbBillPayAndReceVO implements java.io.Serializable {
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
    @Dict(dictGroup = "AB_BILL_TRANSFERWAY", inject ="transferWayName")
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
    @Corp(inject="corpName")
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
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    @BtCompositeSearch(fieldName="billType")
    private Integer billType;

    /**票据类型名称：10 银票、11 商票*/
    @BtCompositeSearch(fieldName="billType")
    private String billTypeName;

    /**流通介质：10 纸票、11 电票*/
    @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
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

    /**票据金额*/
    @BtCompositeSearch(fieldName="billMoney")
    private BigDecimal billMoney;

    /**后手*/
    @BtCompositeSearch(fieldName="rearAccrecords")
    private String rearAccrecords;

    /**贴现利率*/
    @BtCompositeSearch(fieldName="interestRates")
    private BigDecimal interestRates;

    @BtCompositeSearch(fieldName="interestRates")
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

    /**到期提醒*/
    @Lang(inject = "dueRemindStr")
    private String dueRemind;

    private String dueRemindStr;

    /**票据异常情况*/
    @Lang(inject = "billAnomaliesStr")
    private String billAnomalies;

    private String billAnomaliesStr;

    /**保证金*/
    @BtCompositeSearch(fieldName="bailMoney")
    private BigDecimal bailMoney;

    /**业务类型*/
    @Dict(dictGroup = "AB_BRIEF_STATUS", inject ="briefIdName")
    private String briefId;
    @BtCompositeSearch(fieldName="briefId")
    private String briefIdName;

    /**开票方式*/
    @Dict(dictGroup = "ACCEBILL_EMIT_TYPE", inject ="emitTypeName")
    @BtCompositeSearch(fieldName="emitType")
    private String emitType;
    @BtCompositeSearch(fieldName="emitType")
    private String emitTypeName;

    /**是否开票保贴(1是0否) */
    @Dict(dictGroup="sf_yn",inject = "emitGuandisSignName")
    private String emitGuandisSign;
    private String emitGuandisSignName;

    /**开票完成(1是0否) */
    @Dict(dictGroup="sf_yn",inject = "emitOpenOkName")
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
    @Dict(dictGroup="sf_yn",inject = "isPayName")
    @BtCompositeSearch(fieldName="isPay")
    private String isPay;
    @BtCompositeSearch(fieldName="isPay")
    private String isPayName;

    /**票据状态*/
    @Dict(dictGroup = "AB_BILL_STATUS", inject="billStatusName")
    @BtCompositeSearch(fieldName="billStatus")
    private String billStatus;

    @BtCompositeSearch(fieldName="billStatus")
    private String billStatusName;

    @BtCompositeSearch(fieldName="banktypeid")
    private String bankTypeId;

    /**是否发送指令标志：0否； 1是 ；*/
    @BtCompositeSearch(fieldName="sendSign")
    @Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
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
    @Corp(inject="applyCorpName")
    private String applyCorpId;

    /**申请单位名称*/
    @BtCompositeSearch(fieldName="applyCorpName")
    private String applyCorpName;

    /**申请单位代码*/
    private String applyCorpCode;

    /** 业务单位 */
    @Corp(inject="busCorpName")
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

    @BtCompositeSearch(fieldName = "newStmIssuofpprInd")
    private String newStmIssuofpprIndName;

    /**
     * 是否允许分包
     */
    @BtCompositeSearch(fieldName = "blgrdbagcrclPrmtInd")
    @Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
    private String blgrdbagcrclPrmtInd;

    @BtCompositeSearch(fieldName = "blgrdbagcrclPrmtInd")
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
    @BtCompositeSearch(fieldName = "blRmtrUnnIdNo")
    private String blRmtrUnnIdNo;

    private String ebillBlCrclIndc;


    /**
     * 指令操作业务状态
     */
    @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "optStatusName")
    private String optStatus;
    @BtCompositeSearch(fieldName = "optStatus")
    private String optStatusName;

    /**
     * 业务状态
     */
    @Dict(dictGroup = "AB_BILL_RECE_STATUS", inject = "statusName")
    private String status;
    @BtCompositeSearch(fieldName = "status")
    private String statusName;

    /**
     * 票据业务状态
     */
    private String businessStatus;

    /**
     * 银行返回信息
     */
    @BtCompositeSearch(fieldName = "returnMsg")
    private String returnMsg;

    /**
     * 是否结清
     */
    @BtCompositeSearch(fieldName = "isSettle")
    private String isSettle;

    /**
     * 清算方式：SM00 开启线上清算、SM01 关闭线上清算
     */
    private String sttlmmk;

    /**
     * 交易标识 1-有交易流水 0-无交易流水
     */
    @BtCompositeSearch(fieldName = "transSign")
    private String transSign;

    /**
     * 核销状态
     */
    @BtCompositeSearch(fieldName = "writeOffStatus")
    @Dict(dictGroup = "writeOff.status", inject = "writeOffStatusName")
    private String writeOffStatus;
    private String writeOffStatusName;
    /**
     * 核销类型
     */
    @BtCompositeSearch(fieldName = "writeOffType")
    @Dict(dictGroup = "writeOff.type", inject = "writeOffTypeName")
    private String writeOffType;
    private String writeOffTypeName;

    @BtCompositeSearch(fieldName="writeOffBal")
    private BigDecimal writeOffBal;

    /**所属库存*/
    @BtCompositeSearch(fieldName="enterStock")
    @Dict(dictGroup = "AB_ENTER_STOCK", inject = "enterStockName")
    private java.lang.String enterStock;
    private java.lang.String enterStockName;

    @Currency(inject = "curName")
    private String curId;
    private String enCode;
    private String curName;

    /**业务日期 */
    @BtCompositeSearch(fieldName="receDate")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date businessDate;

    private String customCol1;
    private String customCol2;
    private String customCol3;
    private String customCol4;
    private String customCol5;
    private String customCol6;
    private String customCol7;

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

    public String getCustomCol6() {
        return customCol6;
    }

    public void setCustomCol6(String customCol6) {
        this.customCol6 = customCol6;
    }

    public String getCustomCol7() {
        return customCol7;
    }

    public void setCustomCol7(String customCol7) {
        this.customCol7 = customCol7;
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

    public String getEnCode() {
        return enCode;
    }

    public void setEnCode(String enCode) {
        this.enCode = enCode;
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

    public void setCustomCol5(String customCol5) {
        this.customCol5 = customCol5;
    }

    public Date getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }

    public String getTransSign() {
        return transSign;
    }

    public void setTransSign(String transSign) {
        this.transSign = transSign;
    }

    public String getSttlmmk() {
        return sttlmmk;
    }

    public void setSttlmmk(String sttlmmk) {
        this.sttlmmk = sttlmmk;
    }

    public String getIsSettle() {
        return isSettle;
    }

    public void setIsSettle(String isSettle) {
        this.isSettle = isSettle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getBusinessStatus() {
        //根据业务状态和指令状态获取票据业务状态
        if(StringUtils.isNotEmpty(transferWay) && StringUtils.isNotEmpty(status) && StringUtils.isNotEmpty(optStatus)){
            if(AccebillConstant.BRIEF_STATUS_6011.equals(briefId) || String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_2).equals(status) || String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS11).equals(status)
                    || AccebillConstant.AB_BILL_OPT_STATUS15.equals(optStatus) || AccebillConstant.AB_BILL_OPT_STATUS16.equals(optStatus) || AccebillConstant.AB_BILL_OPT_STATUS_2.equals(optStatus)
                    || AccebillConstant.AB_BILL_OPT_STATUS17.equals(optStatus) || AccebillConstant.AB_BILL_OPT_STATUS_1.equals(optStatus) ){
                return "";
            }
            if(String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95).equals(status) && AccebillConstant.AB_BILL_OPT_STATUS18.equals(optStatus)){
                return "待签收";
            }else if(!AccebillConstant.AB_BILL_TRANSFERWAY_HOLD.equals(transferWay) && String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95).equals(status)&& AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus) && !AccebillConstant.AB_BILL_IS_SETTLE_1.equals(isSettle)) {
                return "已签收";
            }else if(!AccebillConstant.AB_BILL_TRANSFERWAY_HOLD.equals(transferWay) &&  String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95).equals(status) && AccebillConstant.AB_BILL_OPT_STATUS19.equals(optStatus) && AccebillConstant.AB_BILL_IS_SETTLE_1.equals(isSettle) ){
                return "已结清";
            }else if(AccebillConstant.AB_BILL_OPT_STATUS_3.equals(optStatus)){
                return "撤回";
            }else if(AccebillConstant.AB_BILL_OPT_STATUS_4.equals(optStatus)){
                return "拒签";
            }else{
                return "已收票";
            }
        }
        return "已收票";
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    public String getOptStatus() {
        return optStatus;
    }

    public void setOptStatus(String optStatus) {
        this.optStatus = optStatus;
    }

    public String getOptStatusName() {
        return optStatusName;
    }

    public void setOptStatusName(String optStatusName) {
        this.optStatusName = optStatusName;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getEbillBlCrclIndc() {
        return ebillBlCrclIndc;
    }

    public void setEbillBlCrclIndc(String ebillBlCrclIndc) {
        this.ebillBlCrclIndc = ebillBlCrclIndc;
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
        return dueRemind != null ? DateUtils.dayDiffs(endDate, DateUtils.getDateYMD())+dueRemindStr : null;
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
                && !AccebillConstant.AB_BILL_STATUS_000002.equals(billStatus) && !AccebillConstant.AB_BILL_STATUS_000000.equals(billStatus)
                && !AccebillConstant.NEWBILL_STATUS_CS05.equals(billStatus) && !AccebillConstant.NEWBILL_STATUS_CS06.equals(billStatus)){
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

    public String getEnterStock() {
        return enterStock;
    }

    public void setEnterStock(String enterStock) {
        this.enterStock = enterStock;
    }

    public String getEnterStockName() {
        return enterStockName;
    }

    public void setEnterStockName(String enterStockName) {
        this.enterStockName = enterStockName;
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
                ", acceptor='" + acceptor + '\'' +
                ", endDate=" + endDate +
                ", cnAccrecords='" + cnAccrecords + '\'' +
                ", billMoney=" + billMoney +
                ", rearAccrecords='" + rearAccrecords + '\'' +
                ", interestRates=" + interestRates +
                ", interestRatesStr='" + interestRatesStr + '\'' +
                ", interest=" + interest +
                ", realMoney=" + realMoney +
                ", payee='" + payee + '\'' +
                ", dueRemind='" + dueRemind + '\'' +
                ", dueRemindStr='" + dueRemindStr + '\'' +
                ", billAnomalies='" + billAnomalies + '\'' +
                ", billAnomaliesStr='" + billAnomaliesStr + '\'' +
                ", bailMoney=" + bailMoney +
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
                ", bankTypeId='" + bankTypeId + '\'' +
                ", sendSign='" + sendSign + '\'' +
                ", sendSignName='" + sendSignName + '\'' +
                ", endorsementCount='" + endorsementCount + '\'' +
                ", applyCorpId='" + applyCorpId + '\'' +
                ", applyCorpName='" + applyCorpName + '\'' +
                ", applyCorpCode='" + applyCorpCode + '\'' +
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
                ", ebillBlCrclIndc='" + ebillBlCrclIndc + '\'' +
                ", optStatus='" + optStatus + '\'' +
                ", optStatusName='" + optStatusName + '\'' +
                ", status='" + status + '\'' +
                ", statusName='" + statusName + '\'' +
                ", businessStatus='" + businessStatus + '\'' +
                ", returnMsg='" + returnMsg + '\'' +
                ", isSettle='" + isSettle + '\'' +
                ", sttlmmk='" + sttlmmk + '\'' +
                ", transSign='" + transSign + '\'' +
                ", writeOffStatus='" + writeOffStatus + '\'' +
                ", writeOffStatusName='" + writeOffStatusName + '\'' +
                ", writeOffType='" + writeOffType + '\'' +
                ", writeOffTypeName='" + writeOffTypeName + '\'' +
                ", writeOffBal=" + writeOffBal +
                ", enterStock='" + enterStock + '\'' +
                ", enterStockName='" + enterStockName + '\'' +
                ", curId='" + curId + '\'' +
                ", curName='" + curName + '\'' +
                ", businessDate='" + businessDate + '\'' +
                '}';
    }

}
