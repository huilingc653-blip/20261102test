package com.bytter.biz.bdlinks.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外币汇款指令表
 * @author huangx
 * @date 2019-07-18 16:31:56
 * @version 1.0
 *
 */
public class BisForeignExcVO extends ExcelModel implements Serializable {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    private String id;
    /**指令编号*/
    @BtCompositeSearch(fieldName = "b.voucher_No")
    private String voucherNo;
    /**付款单ID*/
    private String cbsBillId;
    /**付款单号*/
    @BtCompositeSearch(fieldName = "b.cbs_Bill_No")
    private String cbsBillNo;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "b.corp_Id")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**申请日期*/
    @BtCompositeSearch(fieldName = "b.req_Date")
    private Date reqDate;
    /**汇款日期*/
    private Date payDate;
    /**汇款类型 0-境内付款 1-境外付款*/
    @BtCompositeSearch(fieldName = "b.remit_Type")
    private String remitType;
    /**银行行别*/
    @BtCompositeSearch(fieldName = "b.bank_Type_Id")
    private String bankTypeId;
    /**银行类别名称*/
    @BtCompositeSearch(fieldName = "b.bank_Type_Id")
    private String bankTypeName;
    /**汇款账号*/
    @BtCompositeSearch(fieldName = "b.payer_Acc")
    private String payerAcc;
    /**汇款账号名称*/
    @BtCompositeSearch(fieldName = "b.payer_Acc_Name")
    private String payerAccName;
    /**汇款开户行名称*/
    @BtCompositeSearch(fieldName = "b.payer_Bank")
    private String payerBank;
    /**汇款方联系人姓名*/
    private String payerName;
    /**汇款方联系人电话*/
    private String payerTel;
    /**汇款币种*/
    private String curId;
    /**币别名称*/
    @BtCompositeSearch(fieldName = "b.cur_Id")
    private String curName;
    @BtCompositeSearch(fieldName = "b.cur_Id")
    private String englishCode;
    /**金额*/
    @BtCompositeSearch(fieldName = "b.pay_Money")
    private BigDecimal payMoney;
    /**收款账号*/
    @BtCompositeSearch(fieldName = "b.payee_Acc")
    private String payeeAcc;
    /**收款账号名称*/
    @BtCompositeSearch(fieldName = "b.payee_Acc_Name")
    private String payeeAccName;
    /**收款开户行swift code*/
    @BtCompositeSearch(fieldName = "b.payee_Swift_Code")
    private String payeeSwiftCode;
    /**收款开户行名称*/
    @BtCompositeSearch(fieldName = "b.payee_Bank")
    private String payeeBank;
    /**收款方国别*/
    private String payeeCountryCode;
    /**收款银行之代理行名称*/
    private String payeeAgentBank;
    /**收款银行之代理行地址*/
    private String payeeAgentBankAdd;
    /**收款人开户行银行在其代理行账号*/
    private String payeeAgentAcc;
    /**发电等级 0-普通 Normal  1-加急 Urgent*/
    private String quickSign;
    /**接口代码*/
    private String bifCode;
    /**接口代码名称*/
    private String bifName;
    /**附言*/
    private String addWord;
    /**备注*/
    private String rmk;
    /**付汇性质 1-保税区、2-出口加工区、3-钻石交易所、4-其他特殊经济区域、5-深加工结转、6-离岸账户、7-其他*/
    private String remitNature;
    /**汇款方式 0-电汇 1-票汇*/
    private String remitMethod;
    /**国内外费用承担标志 1-汇款人 OUR  2-收款人 BEN  3-共同 SHA*/
    private String costSign;
    /**付款类型 1-预付货款 2-货到付款 3-退款 4-其他*/
    private String payType;
    /**最迟装运日期*/
    private Date shipDate;
    /**是否为保税货物项下付款 0-否 1-是*/
    private String isBonded;
    /**外汇局批件/备案表号/业务编号*/
    private String busNo;
    /**合同号*/
    private String contractNo;
    /**发票号*/
    private String invoiceNo;
    /**交易编码1*/
    private String tranCode1;
    /**相应金额1*/
    private BigDecimal tranAmt1;
    /**相应币种1*/
    private String tranCurId1;
    /**交易附言1*/
    private String tranAddWord1;
    /**交易编码2*/
    private String tranCode2;
    /**相应金额2*/
    private BigDecimal tranAmt2;
    /**相应币种2*/
    private String tranCurId2;
    /**交易附言2*/
    private String tranAddWord2;
    /**费用交付方式  1-现金 2-支票 3-账户 默认1*/
    private String costMode;
    /**扣费账号*/
    private String dedAcc;
    /**钞汇代码 1-钞 2-汇 默认2*/
    private String bankCurCode;
    /**起息日*/
    private Date interestDate;
    /**银行流水号*/
    private String bankSerialNo;
    /**交易状态*/
    @BtCompositeSearch(fieldName = "b.voucher_Stat")
    private String voucherStat;
    /**交易时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transTime;
    /**返回时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date returnTime;
    /**返回信息（交易前）*/
    private String firstReturnMsg;
    /**返回信息（交易后）*/
    private String nextReturnMsg;
    /**指令类型*/
    private String cmdType;
    /**消息提醒标志*/
    private String noticeSign;
    /**加密信息*/
    private String verifyCode;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**指令来源 1-外币汇款 2-外汇交易*/
    private String voucherFrom;
    /**跨行标识*/
    private String difBank;
    /**外汇业务类型 1-结汇、2-售汇、3-外汇买卖*/
    private String fexBusType;
    /**汇款方名称（英文）*/
    private String payerNameEn;
    /**汇款方地址（英文）*/
    private String payerAddrEn;
    /**收款方名称（英文）*/
    private String payeeNameEn;
    /**收款方地址（英文）*/
    private String payeeAddrEn;
    /**收款方开户行行号*/
    private String payeeBankCode;
    /**银行组织机构代码*/
    private String bankOrgCode;
    /**付款账号对应的币别*/
    private String payerCurId;
    /**指令重发标志*/
    private String resendSign;
    /**校验密码*/
    private String validPassword;
    /**处理意见*/
    private String handleOpinion;
    /**附件名称*/
    private String fileName;


    /**外汇指令来源：REQ-外汇交易，CASH_FLOW-现金流*/
    private String excSource;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getCbsBillId() {
        return cbsBillId;
    }
    public void setCbsBillId(String cbsBillId) {
        this.cbsBillId = cbsBillId;
    }
    public String getCbsBillNo() {
        return cbsBillNo;
    }
    public void setCbsBillNo(String cbsBillNo) {
        this.cbsBillNo = cbsBillNo;
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
    public Date getReqDate() {
        return reqDate;
    }
    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public String getRemitType() {
        return remitType;
    }
    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
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
    public String getPayerBank() {
        return payerBank;
    }
    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }
    public String getPayerName() {
        return payerName;
    }
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }
    public String getPayerTel() {
        return payerTel;
    }
    public void setPayerTel(String payerTel) {
        this.payerTel = payerTel;
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
    public String getEnglishCode() {
        return englishCode;
    }
    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    public String getPayeeAccName() {
        return payeeAccName;
    }
    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    public String getPayeeSwiftCode() {
        return payeeSwiftCode;
    }
    public void setPayeeSwiftCode(String payeeSwiftCode) {
        this.payeeSwiftCode = payeeSwiftCode;
    }
    public String getPayeeBank() {
        return payeeBank;
    }
    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }
    public String getPayeeCountryCode() {
        return payeeCountryCode;
    }
    public void setPayeeCountryCode(String payeeCountryCode) {
        this.payeeCountryCode = payeeCountryCode;
    }
    public String getPayeeAgentBank() {
        return payeeAgentBank;
    }
    public void setPayeeAgentBank(String payeeAgentBank) {
        this.payeeAgentBank = payeeAgentBank;
    }
    public String getPayeeAgentBankAdd() {
        return payeeAgentBankAdd;
    }
    public void setPayeeAgentBankAdd(String payeeAgentBankAdd) {
        this.payeeAgentBankAdd = payeeAgentBankAdd;
    }
    public String getPayeeAgentAcc() {
        return payeeAgentAcc;
    }
    public void setPayeeAgentAcc(String payeeAgentAcc) {
        this.payeeAgentAcc = payeeAgentAcc;
    }
    public String getQuickSign() {
        return quickSign;
    }
    public void setQuickSign(String quickSign) {
        this.quickSign = quickSign;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBifName() {
        return bifName;
    }
    public void setBifName(String bifName) {
        this.bifName = bifName;
    }
    public String getAddWord() {
        return addWord;
    }
    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getRemitNature() {
        return remitNature;
    }
    public void setRemitNature(String remitNature) {
        this.remitNature = remitNature;
    }
    public String getRemitMethod() {
        return remitMethod;
    }
    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }
    public String getCostSign() {
        return costSign;
    }
    public void setCostSign(String costSign) {
        this.costSign = costSign;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public Date getShipDate() {
        return shipDate;
    }
    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }
    public String getIsBonded() {
        return isBonded;
    }
    public void setIsBonded(String isBonded) {
        this.isBonded = isBonded;
    }
    public String getBusNo() {
        return busNo;
    }
    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getInvoiceNo() {
        return invoiceNo;
    }
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }
    public String getTranCode1() {
        return tranCode1;
    }
    public void setTranCode1(String tranCode1) {
        this.tranCode1 = tranCode1;
    }
    public BigDecimal getTranAmt1() {
        return tranAmt1;
    }
    public void setTranAmt1(BigDecimal tranAmt1) {
        this.tranAmt1 = tranAmt1;
    }
    public String getTranCurId1() {
        return tranCurId1;
    }
    public void setTranCurId1(String tranCurId1) {
        this.tranCurId1 = tranCurId1;
    }
    public String getTranAddWord1() {
        return tranAddWord1;
    }
    public void setTranAddWord1(String tranAddWord1) {
        this.tranAddWord1 = tranAddWord1;
    }
    public String getTranCode2() {
        return tranCode2;
    }
    public void setTranCode2(String tranCode2) {
        this.tranCode2 = tranCode2;
    }
    public BigDecimal getTranAmt2() {
        return tranAmt2;
    }
    public void setTranAmt2(BigDecimal tranAmt2) {
        this.tranAmt2 = tranAmt2;
    }
    public String getTranCurId2() {
        return tranCurId2;
    }
    public void setTranCurId2(String tranCurId2) {
        this.tranCurId2 = tranCurId2;
    }
    public String getTranAddWord2() {
        return tranAddWord2;
    }
    public void setTranAddWord2(String tranAddWord2) {
        this.tranAddWord2 = tranAddWord2;
    }
    public String getCostMode() {
        return costMode;
    }
    public void setCostMode(String costMode) {
        this.costMode = costMode;
    }
    public String getDedAcc() {
        return dedAcc;
    }
    public void setDedAcc(String dedAcc) {
        this.dedAcc = dedAcc;
    }
    public String getBankCurCode() {
        return bankCurCode;
    }
    public void setBankCurCode(String bankCurCode) {
        this.bankCurCode = bankCurCode;
    }
    public Date getInterestDate() {
        return interestDate;
    }
    public void setInterestDate(Date interestDate) {
        this.interestDate = interestDate;
    }
    public String getBankSerialNo() {
        return bankSerialNo;
    }
    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    public Date getTransTime() {
        return transTime;
    }
    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }
    public Date getReturnTime() {
        return returnTime;
    }
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
    public String getFirstReturnMsg() {
        return firstReturnMsg;
    }
    public void setFirstReturnMsg(String firstReturnMsg) {
        this.firstReturnMsg = firstReturnMsg;
    }
    public String getNextReturnMsg() {
        return nextReturnMsg;
    }
    public void setNextReturnMsg(String nextReturnMsg) {
        this.nextReturnMsg = nextReturnMsg;
    }
    public String getCmdType() {
        return cmdType;
    }
    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }
    public String getNoticeSign() {
        return noticeSign;
    }
    public void setNoticeSign(String noticeSign) {
        this.noticeSign = noticeSign;
    }
    public String getVerifyCode() {
        return verifyCode;
    }
    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
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
    public String getVoucherFrom() {
        return voucherFrom;
    }
    public void setVoucherFrom(String voucherFrom) {
        this.voucherFrom = voucherFrom;
    }
    public String getDifBank() {
        return difBank;
    }
    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }
    public String getFexBusType() {
        return fexBusType;
    }
    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
    }
    public String getPayerNameEn() {
        return payerNameEn;
    }
    public void setPayerNameEn(String payerNameEn) {
        this.payerNameEn = payerNameEn;
    }
    public String getPayerAddrEn() {
        return payerAddrEn;
    }
    public void setPayerAddrEn(String payerAddrEn) {
        this.payerAddrEn = payerAddrEn;
    }
    public String getPayeeNameEn() {
        return payeeNameEn;
    }
    public void setPayeeNameEn(String payeeNameEn) {
        this.payeeNameEn = payeeNameEn;
    }
    public String getPayeeAddrEn() {
        return payeeAddrEn;
    }
    public void setPayeeAddrEn(String payeeAddrEn) {
        this.payeeAddrEn = payeeAddrEn;
    }
    public String getPayeeBankCode() {
        return payeeBankCode;
    }
    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode;
    }
    public String getBankOrgCode() {
        return bankOrgCode;
    }
    public void setBankOrgCode(String bankOrgCode) {
        this.bankOrgCode = bankOrgCode;
    }
    public String getPayerCurId() {
        return payerCurId;
    }
    public void setPayerCurId(String payerCurId) {
        this.payerCurId = payerCurId;
    }
    public String getResendSign() {
        return resendSign;
    }
    public void setResendSign(String resendSign) {
        this.resendSign = resendSign;
    }
    public String getValidPassword() {
        return validPassword;
    }
    public void setValidPassword(String validPassword) {
        this.validPassword = validPassword;
    }
    public String getHandleOpinion() {
        return handleOpinion;
    }
    public void setHandleOpinion(String handleOpinion) {
        this.handleOpinion = handleOpinion;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getExcSource() {
        return excSource;
    }

    public void setExcSource(String excSource) {
        this.excSource = excSource;
    }
}
