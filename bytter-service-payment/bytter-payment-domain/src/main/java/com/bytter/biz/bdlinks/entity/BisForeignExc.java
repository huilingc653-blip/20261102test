package com.bytter.biz.bdlinks.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Entity
@Table(name = "BIS_FOREIGN_EXC", schema = "")
@SuppressWarnings("serial")
public class BisForeignExc extends IdEntity implements java.io.Serializable {
    /**指令编号*/
    private String voucherNo;
    /**付款单ID*/
    private String cbsBillId;
    /**付款单号*/
    private String cbsBillNo;
    /**单位ID*/
    private String corpId;
    /**申请日期*/
    private Date reqDate;
    /**汇款日期*/
    private Date payDate;
    /**汇款类型 0-境内付款 1-境外付款*/
    private String remitType;
    /**银行行别*/
    private String bankTypeId;
    /**汇款账号*/
    private String payerAcc;
    /**汇款账号名称*/
    private String payerAccName;
    /**汇款开户行名称*/
    private String payerBank;
    /**汇款方联系人姓名*/
    private String payerName;
    /**汇款方开户行swift code*/
    private String payerSwiftCode;
    /**汇款方联系人电话*/
    private String payerTel;
    /**汇款币种*/
    private String curId;
    /**金额*/
    private BigDecimal payMoney;
    /**收款账号*/
    private String payeeAcc;
    /**收款账号名称*/
    private String payeeAccName;
    /**收款开户行swift code*/
    private String payeeSwiftCode;
    /**收款开户行名称*/
    private String payeeBank;
    /**收款方国别*/
    private String payeeCountryCode;
    /**付款方国别*/
    private String payerCountryCode;
    /**收款银行之代理行swift code*/
    private String payeeAgentSwiftCode;
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
    /**附言*/
    private String addWord;
    /**备注(英文)*/
    private String rmk;
    /**备注(本地语言)*/
    private String rmkLocal;
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
    private String voucherStat;
    /**交易时间*/
    private Date transTime;
    /**返回时间*/
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
    /**跨行标识 0-同行 1-他行*/
    private String difBank;
    /**外汇业务类型 1-结汇、2-售汇、3-外汇买卖*/
    private String fexBusType;
    /**汇款方名称（英文）*/
    private String payerNameEn;
    /**汇款方地址（英文）*/
    private String payerAddrEn;
    /**汇款方地址（中文）*/
    private String payerAddr;
    /**收款方名称（英文）*/
    private String payeeNameEn;
    /**收款方地址（英文）*/
    private String payeeAddrEn;
    /**收款方银行地址*/
    private String payeeBankAdd;
    /**收款方开户行行号*/
    private String payeeBankCode;
    /**银行组织机构代码*/
    private String bankOrgCode;
    /**付款账号对应的币别*/
    private String payerCurId;
    /**收款账号对应的币种*/
    private String payeeCurId;
    /**指令重发标志*/
    private String resendSign;
    /**账户类别：汇款账号对应的“账户类别”*/
    private String accType;
    /**收款人类型：0-对公居民、1-对公非居民、2-对私居民、3-对私非居民*/
    private String payeeType;
    /**汇款去向：1 境外电汇、2 港澳台电汇、4 境内系统外同城、5 境内系统内异地、6 境内系统外异地*/
    private String remitRoute;
    /**汇款用途*/
    private String remitUse;
    /**附件名称*/
    private String fileName;
    /**单位证件类型*/
    private String certType;
    /**单位证件号码*/
    private String certNo;
    /**开户银行代码类型*/
    private String clsFlag;
    /**进口日期*/
    private Date arrivalDate;
    /**报关单号*/
    private String declareNo;
    /**资金性质 1-经常项目 2-资本与金融项目 3-服务贸易*/
    private String accNature;
    /**合同汇率*/
    private BigDecimal contractRate;
    /**外汇合同号*/
    private String foreignContractNo;
    /**实际买入（卖出）金额*/
    private BigDecimal actualTransMoney;
    /**实际成交汇率*/
    private BigDecimal actualTransRate;
    /**收款方省*/
    private String payeeProv;
    /**收款方市*/
    private String payeeCity;
    /**结售汇属性*/
    private String fexProp;
    /**原指令编号*/
    private String oldVoucherNo;
    /**签名*/
    private String signatureCode;
    /**费用币种*/
    private String remChgTolAmtCur;
    /**费用总额*/
    private BigDecimal remChgTolAmt;
    /**本地清算号*/
    private String localClearNo;
    /**支付类型（渣打银行）*/
    private String bankPayType;
    /**收款方联系人电话*/
    private String payeeTel;
    /**起运国（兴业银行）*/
    private String originCountry;
    /**买入币种*/
    private String buyCurId;
    /**卖出币种*/
    private String sellCurId;
    /**固定金额 1-买入金额 2-卖出金额*/
    private String fixedMoney;
    /**交易对手账号（目前只针对外汇买卖交易）*/
    private String cotpBankAcc;
    /**交易对手银行账号保存字段*/
    private String saveFields;
    /**结汇资金用途(针对外汇买卖交易)*/
    private String purpose;
    /**受益人关联关系*/
    private String bnfRelate;
    /**监管类型*/
    private String regulatoryType;

    /**付款方省份名称*/
    private String payerProvName;
    /**付款方城镇名称*/
    private String payerCityName;
    /**付款方邮政编号*/
    private String payerPostCode;
    /**付款方建筑编号*/
    private String payerBuildingNumber;
    /**付款方街道名称*/
    private String payerStreetName;

    /**收款方省份名称*/
    private String payeeProvName;
    /**收款方城镇名称*/
    private String payeeCityName;
    /**收款方邮政编号*/
    private String payeePostCode;
    /**收款方建筑编号*/
    private String payeeBuildingNumber;
    /**收款方街道名称*/
    private String payeeStreetName;
    /**付款清算号*/
    private String payerLocalClearNo;

    /**结汇业务类型*/
    private String settleBusType;
    /**外汇交易类型 0-即期、1-远期*/
    private String fexTranType;
    /**交割类型 0-本金交割、1-无本金交割*/
    private String deliveryType;
    /**交割期限*/
    private String deliveryDeadline;
    /**扣费币种*/
    private String dedAccCurId;
    /**交易汇率类型*/
    private String tranRateType;

    /**外汇指令来源：REQ-外汇交易，CASH_FLOW-现金流*/
    private String excSource;

    /**
     * 银行额外参数
     */
    private String bankExtra;


    /**货物贸易金额*/
    private BigDecimal goodsAmount;
    /**预付货款项下金额*/
    private BigDecimal advanceChargeAmount;
    /**预付货款项下所占合同金额比例*/
    private BigDecimal advanceContractRate;
    /**预计X天后报关*/
    private Integer rptGateDate;
    /**报关经营单位名称*/
    private String rptGateEntName;
    /**组织机构代码*/
    private String rptGateEntNo;
    /**报关方式 R-人民币；O-外币*/
    private String rptGateType;
    /**一般贸易金额*/
    private BigDecimal generalTradeAmount;
    /**进料加工金额*/
    private BigDecimal feedAmount;
    /**其他贸易金额*/
    private BigDecimal otherTradeAmount;
    /**边境贸易金额*/
    private BigDecimal borderTradeAmount;
    /**海关特殊监管区域及保税监管场所进出境物流货物金额*/
    private BigDecimal customAmount;
    /**离岸转手买卖金额*/
    private BigDecimal offshoreAmount;
    /**其他金额*/
    private BigDecimal otherAmount;
    /**投资收益金额*/
    private BigDecimal investAmt;
    /**投资收益-国际收支编码*/
    private String investNo;
    /**投资收益-批准证书号*/
    private String investApplyNbr;
    /**经常转移金额*/
    private BigDecimal oftenAmt;
    /**经常转移-国际收支编码*/
    private String oftenNo;
    /**直接投资-批准证书号*/
    private String directInvestmentCertNo;
    /**预收款比例*/
    private Integer advanceRate;
    /**结账期*/
    private Integer checkDay;


    @Column(name ="VOUCHER_NO",nullable=false,length=12)
    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    @Column(name ="CBS_BILL_ID",nullable=true,length=100)
    public String getCbsBillId() {
        return cbsBillId;
    }

    public void setCbsBillId(String cbsBillId) {
        this.cbsBillId = cbsBillId;
    }

    @Column(name ="CBS_BILL_NO",nullable=true,length=30)
    public String getCbsBillNo() {
        return cbsBillNo;
    }

    public void setCbsBillNo(String cbsBillNo) {
        this.cbsBillNo = cbsBillNo;
    }

    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="REQ_DATE",nullable=true)
    public Date getReqDate() {
        return reqDate;
    }

    public void setReqDate(Date reqDate) {
        this.reqDate = reqDate;
    }

    @Column(name ="PAY_DATE",nullable=true)
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @Column(name ="REMIT_TYPE",nullable=false,length=10)
    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    @Column(name ="BANK_TYPE_ID",nullable=true,length=32)
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name ="PAYER_ACC",nullable=true,length=30)
    public String getPayerAcc() {
        return payerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    @Column(name ="PAYER_ACC_NAME",nullable=true,length=150)
    public String getPayerAccName() {
        return payerAccName;
    }

    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }

    @Column(name ="PAYER_BANK",nullable=true,length=150)
    public String getPayerBank() {
        return payerBank;
    }

    public void setPayerBank(String payerBank) {
        this.payerBank = payerBank;
    }

    @Column(name ="PAYER_NAME",nullable=true,length=50)
    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    @Column(name ="PAYER_SWIFT_CODE")
    public String getPayerSwiftCode() {
        return payerSwiftCode;
    }

    public void setPayerSwiftCode(String payerSwiftCode) {
        this.payerSwiftCode = payerSwiftCode;
    }

    @Column(name ="PAYER_TEL",nullable=true,length=30)
    public String getPayerTel() {
        return payerTel;
    }

    public void setPayerTel(String payerTel) {
        this.payerTel = payerTel;
    }

    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="PAY_MONEY",nullable=false,scale=2,length=15)
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name ="PAYEE_ACC",nullable=true,length=40)
    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

    @Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
    public String getPayeeAccName() {
        return payeeAccName;
    }

    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }

    @Column(name ="PAYEE_SWIFT_CODE",nullable=true,length=100)
    public String getPayeeSwiftCode() {
        return payeeSwiftCode;
    }

    public void setPayeeSwiftCode(String payeeSwiftCode) {
        this.payeeSwiftCode = payeeSwiftCode;
    }

    @Column(name ="PAYEE_BANK",nullable=true,length=150)
    public String getPayeeBank() {
        return payeeBank;
    }

    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }

    @Column(name ="PAYEE_COUNTRY_CODE",nullable=true,length=30)
    public String getPayeeCountryCode() {
        return payeeCountryCode;
    }

    public void setPayeeCountryCode(String payeeCountryCode) {
        this.payeeCountryCode = payeeCountryCode;
    }

    @Column(name ="PAYER_COUNTRY_CODE",nullable=true,length=30)
    public String getPayerCountryCode() {
        return payerCountryCode;
    }

    public void setPayerCountryCode(String payerCountryCode) {
        this.payerCountryCode = payerCountryCode;
    }

    @Column(name ="PAYEE_AGENT_SWIFT_CODE")
    public String getPayeeAgentSwiftCode() {
        return payeeAgentSwiftCode;
    }

    public void setPayeeAgentSwiftCode(String payeeAgentSwiftCode) {
        this.payeeAgentSwiftCode = payeeAgentSwiftCode;
    }

    @Column(name ="PAYEE_AGENT_BANK",nullable=true,length=200)
    public String getPayeeAgentBank() {
        return payeeAgentBank;
    }

    public void setPayeeAgentBank(String payeeAgentBank) {
        this.payeeAgentBank = payeeAgentBank;
    }

    @Column(name ="PAYEE_AGENT_BANK_ADD",nullable=true,length=300)
    public String getPayeeAgentBankAdd() {
        return payeeAgentBankAdd;
    }

    public void setPayeeAgentBankAdd(String payeeAgentBankAdd) {
        this.payeeAgentBankAdd = payeeAgentBankAdd;
    }

    @Column(name ="PAYEE_AGENT_ACC",nullable=true,length=30)
    public String getPayeeAgentAcc() {
        return payeeAgentAcc;
    }

    public void setPayeeAgentAcc(String payeeAgentAcc) {
        this.payeeAgentAcc = payeeAgentAcc;
    }
    @Column(name ="QUICK_SIGN",nullable=true,length=10)
    public String getQuickSign() {
        return quickSign;
    }

    public void setQuickSign(String quickSign) {
        this.quickSign = quickSign;
    }

    @Column(name ="BIF_CODE",nullable=true,length=32)
    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    @Column(name ="ADD_WORD",nullable=true,length=100)
    public String getAddWord() {
        return addWord;
    }

    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }

    @Column(name ="RMK",nullable=true,length=200)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="RMK_LOCAL",nullable=true,length=200)
    public String getRmkLocal() {
        return rmkLocal;
    }

    public void setRmkLocal(String rmkLocal) {
        this.rmkLocal = rmkLocal;
    }

    @Column(name ="REMIT_NATURE",nullable=true,length=10)
    public String getRemitNature() {
        return remitNature;
    }

    public void setRemitNature(String remitNature) {
        this.remitNature = remitNature;
    }

    @Column(name ="REMIT_METHOD",nullable=true,length=10)
    public String getRemitMethod() {
        return remitMethod;
    }

    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }

    @Column(name ="COST_SIGN",nullable=true,length=10)
    public String getCostSign() {
        return costSign;
    }

    public void setCostSign(String costSign) {
        this.costSign = costSign;
    }

    @Column(name ="PAY_TYPE",nullable=true,length=10)
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name ="SHIP_DATE",nullable=true)
    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    @Column(name ="IS_BONDED",nullable=true,length=10)
    public String getIsBonded() {
        return isBonded;
    }

    public void setIsBonded(String isBonded) {
        this.isBonded = isBonded;
    }

    @Column(name ="BUS_NO",nullable=true,length=60)
    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    @Column(name ="CONTRACT_NO",nullable=true,length=60)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Column(name ="INVOICE_NO",nullable=true,length=60)
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Column(name ="TRAN_CODE1",nullable=true,length=60)
    public String getTranCode1() {
        return tranCode1;
    }

    public void setTranCode1(String tranCode1) {
        this.tranCode1 = tranCode1;
    }

    @Column(name ="TRAN_AMT1",nullable=false,scale=2,length=15)
    public BigDecimal getTranAmt1() {
        return tranAmt1;
    }

    public void setTranAmt1(BigDecimal tranAmt1) {
        this.tranAmt1 = tranAmt1;
    }

    @Column(name ="TRAN_CUR_ID1",nullable=true,length=32)
    public String getTranCurId1() {
        return tranCurId1;
    }

    public void setTranCurId1(String tranCurId1) {
        this.tranCurId1 = tranCurId1;
    }

    @Column(name ="TRAN_ADD_WORD1",nullable=true,length=100)
    public String getTranAddWord1() {
        return tranAddWord1;
    }

    public void setTranAddWord1(String tranAddWord1) {
        this.tranAddWord1 = tranAddWord1;
    }

    @Column(name ="TRAN_CODE2",nullable=true,length=60)
    public String getTranCode2() {
        return tranCode2;
    }

    public void setTranCode2(String tranCode2) {
        this.tranCode2 = tranCode2;
    }

    @Column(name ="TRAN_AMT2",nullable=false,scale=2,length=15)
    public BigDecimal getTranAmt2() {
        return tranAmt2;
    }

    public void setTranAmt2(BigDecimal tranAmt2) {
        this.tranAmt2 = tranAmt2;
    }

    @Column(name ="TRAN_CUR_ID2",nullable=true,length=32)
    public String getTranCurId2() {
        return tranCurId2;
    }

    public void setTranCurId2(String tranCurId2) {
        this.tranCurId2 = tranCurId2;
    }

    @Column(name ="TRAN_ADD_WORD2",nullable=true,length=100)
    public String getTranAddWord2() {
        return tranAddWord2;
    }

    public void setTranAddWord2(String tranAddWord2) {
        this.tranAddWord2 = tranAddWord2;
    }

    @Column(name ="COST_MODE",nullable=true,length=10)
    public String getCostMode() {
        return costMode;
    }

    public void setCostMode(String costMode) {
        this.costMode = costMode;
    }

    @Column(name ="DED_ACC",nullable=true,length=30)
    public String getDedAcc() {
        return dedAcc;
    }

    public void setDedAcc(String dedAcc) {
        this.dedAcc = dedAcc;
    }

    @Column(name ="BANK_CUR_CODE",nullable=true,length=10)
    public String getBankCurCode() {
        return bankCurCode;
    }

    public void setBankCurCode(String bankCurCode) {
        this.bankCurCode = bankCurCode;
    }

    @Column(name ="INTEREST_DATE",nullable=true)
    public Date getInterestDate() {
        return interestDate;
    }

    public void setInterestDate(Date interestDate) {
        this.interestDate = interestDate;
    }

    @Column(name ="BANK_SERIAL_NO",nullable=true,length=60)
    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    @Column(name ="VOUCHER_STAT",nullable=true,length=1)
    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    @Column(name ="TRANS_TIME",nullable=true)
    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    @Column(name ="RETURN_TIME",nullable=true)
    public Date getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }

    @Column(name ="FIRST_RETURN_MSG",nullable=true,length=2000)
    public String getFirstReturnMsg() {
        return firstReturnMsg;
    }

    public void setFirstReturnMsg(String firstReturnMsg) {
        this.firstReturnMsg = firstReturnMsg;
    }

    @Column(name ="NEXT_RETURN_MSG",nullable=true,length=2000)
    public String getNextReturnMsg() {
        return nextReturnMsg;
    }

    public void setNextReturnMsg(String nextReturnMsg) {
        this.nextReturnMsg = nextReturnMsg;
    }

    @Column(name ="CMD_TYPE",nullable=true)
    public String getCmdType() {
        return cmdType;
    }

    public void setCmdType(String cmdType) {
        this.cmdType = cmdType;
    }

    @Column(name ="NOTICE_SIGN",nullable=true,length=1)
    public String getNoticeSign() {
        return noticeSign;
    }

    public void setNoticeSign(String noticeSign) {
        this.noticeSign = noticeSign;
    }

    @Column(name ="VERIFY_CODE",nullable=true,length=3000)
    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="VOUCHER_FROM",nullable=true,length=2)
    public String getVoucherFrom(){
        return this.voucherFrom;
    }

    public void setVoucherFrom(String voucherFrom){
        this.voucherFrom = voucherFrom;
    }

    @Column(name ="DIF_BANK",nullable=true,length=1)
    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    @Column(name ="FEX_BUS_TYPE",nullable=true,length=1)
    public String getFexBusType() {
        return fexBusType;
    }

    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
    }

    @Column(name ="PAYER_NAME_EN")
    public String getPayerNameEn() {
        return payerNameEn;
    }

    public void setPayerNameEn(String payerNameEn) {
        this.payerNameEn = payerNameEn;
    }

    @Column(name ="PAYER_ADDR_EN")
    public String getPayerAddrEn() {
        return payerAddrEn;
    }

    public void setPayerAddrEn(String payerAddrEn) {
        this.payerAddrEn = payerAddrEn;
    }

    @Column(name ="PAYER_ADDR")
    public String getPayerAddr() {
        return payerAddr;
    }

    public void setPayerAddr(String payerAddr) {
        this.payerAddr = payerAddr;
    }

    @Column(name ="PAYEE_NAME_EN")
    public String getPayeeNameEn() {
        return payeeNameEn;
    }

    public void setPayeeNameEn(String payeeNameEn) {
        this.payeeNameEn = payeeNameEn;
    }

    @Column(name ="PAYEE_ADDR_EN")
    public String getPayeeAddrEn() {
        return payeeAddrEn;
    }

    public void setPayeeAddrEn(String payeeAddrEn) {
        this.payeeAddrEn = payeeAddrEn;
    }

    @Column(name ="PAYEE_BANK_ADD")
    public String getPayeeBankAdd() {
        return payeeBankAdd;
    }

    public void setPayeeBankAdd(String payeeBankAdd) {
        this.payeeBankAdd = payeeBankAdd;
    }

    @Column(name ="PAYEE_BANK_CODE")
    public String getPayeeBankCode() {
        return payeeBankCode;
    }

    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode;
    }

    @Column(name ="BANK_ORG_CODE")
    public String getBankOrgCode() {
        return bankOrgCode;
    }

    public void setBankOrgCode(String bankOrgCode) {
        this.bankOrgCode = bankOrgCode;
    }

    @Column(name ="PAYER_CUR_ID")
    public String getPayerCurId() {
        return payerCurId;
    }

    public void setPayerCurId(String payerCurId) {
        this.payerCurId = payerCurId;
    }

    @Column(name ="PAYEE_CUR_ID")
    public String getPayeeCurId() {
        return payeeCurId;
    }

    public void setPayeeCurId(String payeeCurId) {
        this.payeeCurId = payeeCurId;
    }

    @Column(name ="RESEND_SIGN")
    public String getResendSign() {
        return resendSign;
    }

    public void setResendSign(String resendSign) {
        this.resendSign = resendSign;
    }

    @Column(name ="ACC_TYPE",nullable=true)
    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    @Column(name ="PAYEE_TYPE",nullable=true)
    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    @Column(name ="REMIT_ROUTE",nullable=true)
    public String getRemitRoute() {
        return remitRoute;
    }

    public void setRemitRoute(String remitRoute) {
        this.remitRoute = remitRoute;
    }

    @Column(name ="REMIT_USE",nullable=true)
    public String getRemitUse() {
        return remitUse;
    }

    public void setRemitUse(String remitUse) {
        this.remitUse = remitUse;
    }

    @Column(name ="FILE_NAME",nullable=true)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name ="CERT_TYPE")
    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    @Column(name ="CERT_NO")
    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    @Column(name ="CLS_FLAG")
    public String getClsFlag() {
        return clsFlag;
    }

    public void setClsFlag(String clsFlag) {
        this.clsFlag = clsFlag;
    }

    @Column(name ="ARRIVAL_DATE")
    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    @Column(name ="DECLARE_NO")
    public String getDeclareNo() {
        return declareNo;
    }

    public void setDeclareNo(String declareNo) {
        this.declareNo = declareNo;
    }

    @Column(name ="ACC_NATURE")
    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    @Column(name ="CONTRACT_RATE")
    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    @Column(name ="FOREIGN_CONTRACT_NO")
    public String getForeignContractNo() {
        return foreignContractNo;
    }

    public void setForeignContractNo(String foreignContractNo) {
        this.foreignContractNo = foreignContractNo;
    }

    @Column(name ="ACTUAL_TRANS_MONEY")
    public BigDecimal getActualTransMoney() {
        return actualTransMoney;
    }

    public void setActualTransMoney(BigDecimal actualTransMoney) {
        this.actualTransMoney = actualTransMoney;
    }

    @Column(name ="ACTUAL_TRANS_RATE")
    public BigDecimal getActualTransRate() {
        return actualTransRate;
    }

    public void setActualTransRate(BigDecimal actualTransRate) {
        this.actualTransRate = actualTransRate;
    }

    @Column(name ="PAYEE_PROV")
    public String getPayeeProv() {
        return payeeProv;
    }

    public void setPayeeProv(String payeeProv) {
        this.payeeProv = payeeProv;
    }

    @Column(name ="PAYEE_CITY")
    public String getPayeeCity() {
        return payeeCity;
    }

    public void setPayeeCity(String payeeCity) {
        this.payeeCity = payeeCity;
    }

    @Column(name ="fex_Prop")
    public String getFexProp() {
        return fexProp;
    }

    public void setFexProp(String fexProp) {
        this.fexProp = fexProp;
    }

    @Column(name ="OLD_VOUCHER_NO")
    public String getOldVoucherNo() {
        return oldVoucherNo;
    }

    public void setOldVoucherNo(String oldVoucherNo) {
        this.oldVoucherNo = oldVoucherNo;
    }

    @Column(name ="SIGNATURE_CODE")
    public String getSignatureCode() {
        return signatureCode;
    }

    public void setSignatureCode(String signatureCode) {
        this.signatureCode = signatureCode;
    }

    @Column(name ="REM_CHG_TOL_AMT_CUR")
    public String getRemChgTolAmtCur() {
        return remChgTolAmtCur;
    }

    public void setRemChgTolAmtCur(String remChgTolAmtCur) {
        this.remChgTolAmtCur = remChgTolAmtCur;
    }

    @Column(name ="REM_CHG_TOL_AMT")
    public BigDecimal getRemChgTolAmt() {
        return remChgTolAmt;
    }

    public void setRemChgTolAmt(BigDecimal remChgTolAmt) {
        this.remChgTolAmt = remChgTolAmt;
    }

    @Column(name ="LOCAL_CLEAR_NO")
    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    @Column(name ="BANK_PAY_TYPE")
    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    @Column(name ="PAYEE_TEL")
    public String getPayeeTel() {
        return payeeTel;
    }

    public void setPayeeTel(String payeeTel) {
        this.payeeTel = payeeTel;
    }

    @Column(name ="ORIGIN_COUNTRY")
    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    @Column(name ="BUY_CUR_ID",nullable=true,length=32)
    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    @Column(name ="SELL_CUR_ID",nullable=true,length=32)
    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    @Column(name ="FIXED_MONEY")
    public String getFixedMoney() {
        return fixedMoney;
    }

    public void setFixedMoney(String fixedMoney) {
        this.fixedMoney = fixedMoney;
    }

    @Column(name ="COTP_BANK_ACC")
    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    @Column(name ="SAVE_FIELDS")
    public String getSaveFields() {
        return saveFields;
    }

    public void setSaveFields(String saveFields) {
        this.saveFields = saveFields;
    }

    @Column(name ="PURPOSE")
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Column(name ="BNF_RELATE")
    public String getBnfRelate() {
        return bnfRelate;
    }

    public void setBnfRelate(String bnfRelate) {
        this.bnfRelate = bnfRelate;
    }

    @Column(name ="REGULATORY_TYPE")
    public String getRegulatoryType() {
        return regulatoryType;
    }

    public void setRegulatoryType(String regulatoryType) {
        this.regulatoryType = regulatoryType;
    }

    @Column(name ="PAYER_PROV_NAME")
    public String getPayerProvName() {
        return payerProvName;
    }

    public void setPayerProvName(String payerProvName) {
        this.payerProvName = payerProvName;
    }

    @Column(name ="PAYER_CITY_NAME")
    public String getPayerCityName() {
        return payerCityName;
    }

    public void setPayerCityName(String payerCityName) {
        this.payerCityName = payerCityName;
    }

    @Column(name ="PAYER_POST_CODE")
    public String getPayerPostCode() {
        return payerPostCode;
    }

    public void setPayerPostCode(String payerPostCode) {
        this.payerPostCode = payerPostCode;
    }

    @Column(name ="PAYER_BUILDING_NUMBER")
    public String getPayerBuildingNumber() {
        return payerBuildingNumber;
    }

    public void setPayerBuildingNumber(String payerBuildingNumber) {
        this.payerBuildingNumber = payerBuildingNumber;
    }

    @Column(name ="PAYER_STREET_NAME")
    public String getPayerStreetName() {
        return payerStreetName;
    }

    public void setPayerStreetName(String payerStreetName) {
        this.payerStreetName = payerStreetName;
    }

    @Column(name ="PAYEE_PROV_NAME")
    public String getPayeeProvName() {
        return payeeProvName;
    }

    public void setPayeeProvName(String payeeProvName) {
        this.payeeProvName = payeeProvName;
    }

    @Column(name ="PAYEE_CITY_NAME")
    public String getPayeeCityName() {
        return payeeCityName;
    }

    public void setPayeeCityName(String payeeCityName) {
        this.payeeCityName = payeeCityName;
    }

    @Column(name ="PAYEE_POST_CODE")
    public String getPayeePostCode() {
        return payeePostCode;
    }

    public void setPayeePostCode(String payeePostCode) {
        this.payeePostCode = payeePostCode;
    }

    @Column(name ="PAYEE_BUILDING_NUMBER")
    public String getPayeeBuildingNumber() {
        return payeeBuildingNumber;
    }

    public void setPayeeBuildingNumber(String payeeBuildingNumber) {
        this.payeeBuildingNumber = payeeBuildingNumber;
    }

    @Column(name ="PAYEE_STREET_NAME ")
    public String getPayeeStreetName() {
        return payeeStreetName;
    }

    public void setPayeeStreetName(String payeeStreetName) {
        this.payeeStreetName = payeeStreetName;
    }

    @Column(name ="PAYER_LOCAL_CLEAR_NO")
    public String getPayerLocalClearNo() {
        return payerLocalClearNo;
    }

    public void setPayerLocalClearNo(String payerLocalClearNo) {
        this.payerLocalClearNo = payerLocalClearNo;
    }

    @Column(name ="SETTLE_BUS_TYPE")
    public String getSettleBusType() {
        return settleBusType;
    }

    public void setSettleBusType(String settleBusType) {
        this.settleBusType = settleBusType;
    }

    @Column(name ="FEX_TRAN_TYPE",nullable=true,length=1)
    public String getFexTranType() {
        return fexTranType;
    }

    public void setFexTranType(String fexTranType) {
        this.fexTranType = fexTranType;
    }

    @Column(name ="DELIVERY_TYPE",nullable=true,length=1)
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Column(name ="DELIVERY_DEADLINE",nullable=true,length=50)
    public String getDeliveryDeadline() {
        return deliveryDeadline;
    }

    public void setDeliveryDeadline(String deliveryDeadline) {
        this.deliveryDeadline = deliveryDeadline;
    }

    @Column(name ="DED_ACC_CUR_ID")
    public String getDedAccCurId() {
        return dedAccCurId;
    }

    public void setDedAccCurId(String dedAccCurId) {
        this.dedAccCurId = dedAccCurId;
    }

    @Column(name ="TRAN_RATE_TYPE",nullable=true,length=10)
    public String getTranRateType() {
        return tranRateType;
    }

    public void setTranRateType(String tranRateType) {
        this.tranRateType = tranRateType;
    }

    @Column(name ="EXC_SOURCE",nullable=true,length=10)
    public String getExcSource() {
        return excSource;
    }

    public void setExcSource(String excSource) {
        this.excSource = excSource;
    }

    @Column(name ="BANK_EXTRA")
    public String getBankExtra() {
        return bankExtra;
    }

    public void setBankExtra(String bankExtra) {
        this.bankExtra = bankExtra;
    }

    @Column(name ="GOODS_AMOUNT")
    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    @Column(name ="ADVANCE_CHARGE_AMOUNT")
    public BigDecimal getAdvanceChargeAmount() {
        return advanceChargeAmount;
    }

    public void setAdvanceChargeAmount(BigDecimal advanceChargeAmount) {
        this.advanceChargeAmount = advanceChargeAmount;
    }

    @Column(name ="ADVANCE_CONTRACT_RATE")
    public BigDecimal getAdvanceContractRate() {
        return advanceContractRate;
    }

    public void setAdvanceContractRate(BigDecimal advanceContractRate) {
        this.advanceContractRate = advanceContractRate;
    }

    @Column(name ="RPT_GATE_DATE")
    public Integer getRptGateDate() {
        return rptGateDate;
    }

    public void setRptGateDate(Integer rptGateDate) {
        this.rptGateDate = rptGateDate;
    }

    @Column(name ="RPT_GATE_ENT_NAME")
    public String getRptGateEntName() {
        return rptGateEntName;
    }

    public void setRptGateEntName(String rptGateEntName) {
        this.rptGateEntName = rptGateEntName;
    }

    @Column(name ="RPT_GATE_ENT_NO")
    public String getRptGateEntNo() {
        return rptGateEntNo;
    }

    public void setRptGateEntNo(String rptGateEntNo) {
        this.rptGateEntNo = rptGateEntNo;
    }

    @Column(name ="RPT_GATE_TYPE")
    public String getRptGateType() {
        return rptGateType;
    }

    public void setRptGateType(String rptGateType) {
        this.rptGateType = rptGateType;
    }

    @Column(name ="GENERAL_TRADE_AMOUNT")
    public BigDecimal getGeneralTradeAmount() {
        return generalTradeAmount;
    }

    public void setGeneralTradeAmount(BigDecimal generalTradeAmount) {
        this.generalTradeAmount = generalTradeAmount;
    }

    @Column(name ="FEED_AMOUNT")
    public BigDecimal getFeedAmount() {
        return feedAmount;
    }

    public void setFeedAmount(BigDecimal feedAmount) {
        this.feedAmount = feedAmount;
    }

    @Column(name ="OTHER_TRADE_AMOUNT")
    public BigDecimal getOtherTradeAmount() {
        return otherTradeAmount;
    }

    public void setOtherTradeAmount(BigDecimal otherTradeAmount) {
        this.otherTradeAmount = otherTradeAmount;
    }

    @Column(name ="BORDER_TRADE_AMOUNT")
    public BigDecimal getBorderTradeAmount() {
        return borderTradeAmount;
    }

    public void setBorderTradeAmount(BigDecimal borderTradeAmount) {
        this.borderTradeAmount = borderTradeAmount;
    }

    @Column(name ="CUSTOM_AMOUNT")
    public BigDecimal getCustomAmount() {
        return customAmount;
    }

    public void setCustomAmount(BigDecimal customAmount) {
        this.customAmount = customAmount;
    }

    @Column(name ="OFFSHORE_AMOUNT")
    public BigDecimal getOffshoreAmount() {
        return offshoreAmount;
    }

    public void setOffshoreAmount(BigDecimal offshoreAmount) {
        this.offshoreAmount = offshoreAmount;
    }

    @Column(name ="OTHER_AMOUNT")
    public BigDecimal getOtherAmount() {
        return otherAmount;
    }

    public void setOtherAmount(BigDecimal otherAmount) {
        this.otherAmount = otherAmount;
    }

    @Column(name ="INVEST_AMT")
    public BigDecimal getInvestAmt() {
        return investAmt;
    }

    public void setInvestAmt(BigDecimal investAmt) {
        this.investAmt = investAmt;
    }

    @Column(name ="INVEST_NO")
    public String getInvestNo() {
        return investNo;
    }

    public void setInvestNo(String investNo) {
        this.investNo = investNo;
    }

    @Column(name ="INVEST_APPLY_NBR")
    public String getInvestApplyNbr() {
        return investApplyNbr;
    }

    public void setInvestApplyNbr(String investApplyNbr) {
        this.investApplyNbr = investApplyNbr;
    }

    @Column(name ="OFTEN_AMT")
    public BigDecimal getOftenAmt() {
        return oftenAmt;
    }

    public void setOftenAmt(BigDecimal oftenAmt) {
        this.oftenAmt = oftenAmt;
    }

    @Column(name ="OFTEN_NO")
    public String getOftenNo() {
        return oftenNo;
    }

    public void setOftenNo(String oftenNo) {
        this.oftenNo = oftenNo;
    }

    @Column(name ="DIRECT_INVESTMENT_CERT_NO")
    public String getDirectInvestmentCertNo() {
        return directInvestmentCertNo;
    }

    public void setDirectInvestmentCertNo(String directInvestmentCertNo) {
        this.directInvestmentCertNo = directInvestmentCertNo;
    }

    @Column(name ="ADVANCE_RATE")
    public Integer getAdvanceRate() {
        return advanceRate;
    }

    public void setAdvanceRate(Integer advanceRate) {
        this.advanceRate = advanceRate;
    }

    @Column(name ="CHECK_DAY")
    public Integer getCheckDay() {
        return checkDay;
    }

    public void setCheckDay(Integer checkDay) {
        this.checkDay = checkDay;
    }
}
