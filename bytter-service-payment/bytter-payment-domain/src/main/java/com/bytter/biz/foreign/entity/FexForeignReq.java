package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外币汇款申请表
 * @author huangx
 * @date 2019-07-15 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_FOREIGN_REQ", schema = "")
@SuppressWarnings("serial")
public class FexForeignReq extends IdEntity implements java.io.Serializable {
    /**汇款单号*/
    private String billNo;
    /**单位ID*/
    private String corpId;
    /**汇款类型 0-境内付款 1-境外付款*/
    private String remitType;
    /**汇款日期*/
    private Date payDate;
    /**汇款币种*/
    private String curId;
    /**银行行别*/
    private String bankTypeId;
    /**账号ID*/
    private String accId;
    /**汇款账号*/
    private String payerAcc;
    /**汇款账号名称*/
    private String payerAccName;
    /**汇款开户行名称*/
    private String payerBank;
    /**汇款方联系人姓名*/
    private String payerName;
    /**汇款方联系人电话*/
    private String payerTel;
    /**汇款人名称（英文）*/
    private String payerNameEn;
    /**汇款人地址（英文）*/
    private String payerAddEn;
    /**收款账号*/
    private String payeeAcc;
    /**收款账号名称*/
    private String payeeAccName;
    /**收款人地址*/
    private String payeeAdd;
    /**收款开户行swift code*/
    private String payeeSwiftCode;
    /**收款开户行名称*/
    private String payeeBank;
    /**收款开户行地址*/
    private String payeeBankAdd;
    /**收款方国别*/
    private String payeeCountryCode;
    /**收款银行之代理行swift code*/
    private String payeeAgentSwiftCode;
    /**收款银行之代理行名称*/
    private String payeeAgentBank;
    /**收款银行之代理行地址*/
    private String payeeAgentBankAdd;
    /**收款人开户行银行在其代理行账号*/
    private String payeeAgentAcc;
    /**汇款金额*/
    private BigDecimal payMoney;
    /**购汇汇率*/
    private BigDecimal foreignExrate;
    /**购汇金额*/
    private BigDecimal foreignMoney;
    /**汇款附言*/
    private String addWord;
    /**备注*/
    private String rmk;
    /**发电等级 0-普通 Normal  1-加急 Urgent*/
    private String quickSign;
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
    /**状态*/
    private Integer status;
    /**附件ID*/
    private String attachmentId;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    /**账户类别：汇款账号对应的“账户类别”*/
    private String accType;
    /**收款人类型：0-对公居民、1-对公非居民、2-对私居民、3-对私非居民*/
    private String payeeType;
    /**汇款去向：1 境外电汇、2 港澳台电汇、4 境内系统外同城、5 境内系统内异地、6 境内系统外异地*/
    private String remitRoute;
    /**汇款用途*/
    private String remitUse;
    /**办理机构*/
    private String branchCode;
    /**办理机构名称*/
    private String branchName;
    /**进口日期*/
    private Date arrivalDate;
    /**报关单号*/
    private String declareNo;
    /**资金性质 1-经常项目 2-资本与金融项目 3-服务贸易*/
    private String accNature;
    /**单据来源*/
    private String billFrom;
    /**ERP编号*/
    private String erpId;
    /**是否已付款*/
    private String isPayed;
    /**跨行标识*/
    private String difBank;
    /**支付类型*/
    private String bankPayType;
    /**现金流项目*/
    private String cashItem;
    /**受益人关联关系*/
    private String bnfRelate;

    /**交易状态*/
    private String voucherStat;

    @Column(name ="BILL_NO",nullable=false,length=30)
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="REMIT_TYPE",nullable=false,length=10)
    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    @Column(name ="PAY_DATE",nullable=true)
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="BANK_TYPE_ID",nullable=true,length=32)
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name ="ACC_ID",nullable=true,length=32)
    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
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

    @Column(name ="PAYER_TEL",nullable=true,length=30)
    public String getPayerTel() {
        return payerTel;
    }

    public void setPayerTel(String payerTel) {
        this.payerTel = payerTel;
    }

    @Column(name ="PAYER_NAME_EN",nullable=true,length=50)
    public String getPayerNameEn() {
        return payerNameEn;
    }

    public void setPayerNameEn(String payerNameEn) {
        this.payerNameEn = payerNameEn;
    }

    @Column(name ="PAYER_ADD_EN",nullable=true,length=200)
    public String getPayerAddEn() {
        return payerAddEn;
    }

    public void setPayerAddEn(String payerAddEn) {
        this.payerAddEn = payerAddEn;
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

    @Column(name ="PAYEE_ADD",nullable=true,length=300)
    public String getPayeeAdd() {
        return payeeAdd;
    }

    public void setPayeeAdd(String payeeAdd) {
        this.payeeAdd = payeeAdd;
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

    @Column(name ="PAYEE_BANK_ADD",nullable=true,length=300)
    public String getPayeeBankAdd() {
        return payeeBankAdd;
    }

    public void setPayeeBankAdd(String payeeBankAdd) {
        this.payeeBankAdd = payeeBankAdd;
    }

    @Column(name ="PAYEE_COUNTRY_CODE",nullable=true,length=30)
    public String getPayeeCountryCode() {
        return payeeCountryCode;
    }

    public void setPayeeCountryCode(String payeeCountryCode) {
        this.payeeCountryCode = payeeCountryCode;
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

    @Column(name ="PAY_MONEY",nullable=false,scale=2,length=15)
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name ="FOREIGN_EXRATE",nullable=false,scale=4,length=12)
    public BigDecimal getForeignExrate() {
        return foreignExrate;
    }

    public void setForeignExrate(BigDecimal foreignExrate) {
        this.foreignExrate = foreignExrate;
    }

    @Column(name ="FOREIGN_MONEY",nullable=false,scale=2,length=15)
    public BigDecimal getForeignMoney() {
        return foreignMoney;
    }

    public void setForeignMoney(BigDecimal foreignMoney) {
        this.foreignMoney = foreignMoney;
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

    @Column(name ="QUICK_SIGN",nullable=true,length=10)
    public String getQuickSign() {
        return quickSign;
    }

    public void setQuickSign(String quickSign) {
        this.quickSign = quickSign;
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

    @Column(name ="STATUS",nullable=true,length=10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="ATTACHMENT_ID",nullable=true,length=32)
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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

    @Column(name ="BRANCH_CODE",nullable=true)
    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    @Column(name ="BRANCH_NAME",nullable=true)
    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    @Column(name ="BILL_FROM")
    public String getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }

    @Column(name ="ERP_ID")
    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    @Column(name ="IS_PAYED")
    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    @Column(name = "DIF_BANK")
    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    @Column(name = "BANK_PAY_TYPE")
    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    @Column(name ="CASH_ITEM",nullable=true)
    public String getCashItem() {
        return cashItem;
    }

    public void setCashItem(String cashItem) {
        this.cashItem = cashItem;
    }

    @Column(name ="BNF_RELATE",nullable=true)
    public String getBnfRelate() {
        return bnfRelate;
    }

    public void setBnfRelate(String bnfRelate) {
        this.bnfRelate = bnfRelate;
    }

    @Column(name ="voucher_stat",nullable=false,length=30)
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

}
