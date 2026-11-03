package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.annotation.TranCode;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 外币汇款申请VO
 * @author huangx
 * @date 2019-07-15 15:20:33
 * @version 1.0
 *
 */
public class FexForeignReqVO extends ExcelModel implements Serializable,IBaseDataInject,IBusinessKey {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    private String id;
    /**汇款单号*/
    @BtCompositeSearch(fieldName = "f.bill_No")
    private String billNo;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "f.corp_Id")
    @Corp(inject="corpName")
    private String corpId;
    /**单位名称*/
    @Excel(name = "*单位名称")
    @NotNull( message = "nis.netbank.message.corpname.null")
    private String corpName;
    /**汇款类型 0-境内汇款 1-境外汇款*/
    @Excel(name = "*汇款类型", replace={"境内汇款_0","境外汇款_1"})
    @NotNull( message = "fex.foreign.message.remittype.null")
    @BtCompositeSearch(fieldName = "f.remit_Type")
    private String remitType;
    /**汇款日期*/
    @BtCompositeSearch(fieldName = "f.pay_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date payDate;
    /**汇款币种*/
    @BtCompositeSearch(fieldName = "f.cur_Id")
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="curEnglishCode", field = "englishCode")
            }
    )
    private String curId;
    /**币别名称*/
    @Excel(name = "*汇款币种")
    @NotNull( message = "fex.foreign.message.curname.null")
    private String curName;
    /**汇款币种英文简码*/
    private String curEnglishCode;
    /**银行类别*/
    @BtCompositeSearch(fieldName = "f.bank_Type_Id")
    @BankType(inject="bankTypeName")
    private String bankTypeId;
    /**银行类别名称*/
    @Excel(name = "*银行类别")
    @NotNull( message = "nis.netbank.message.banktypename.null")
    private String bankTypeName;
    /**账号ID*/
    private String accId;
    /**汇款账号*/
    @Excel(name = "*汇款账号")
    @NotNull( message = "fex.foreign.message.payeracc.null")
    @BtCompositeSearch(fieldName = "f.payer_Acc")
    private String payerAcc;
    /**汇款账号名称*/
    private String payerAccName;
    /**汇款开户行名称*/
    private String payerBank;
    /**汇款方联系人姓名*/
    @Excel(name = "*汇款方联系人姓名")
    @NotNull( message = "fex.foreign.message.payername.null")
    private String payerName;
    /**汇款方联系人电话*/
    @Excel(name = "*汇款方联系人电话")
    @NotNull( message = "fex.foreign.message.payertel.null")
    private String payerTel;
    /**汇款人名称（英文）*/
    @Excel(name = "*汇款人名称（英文）")
    private String payerNameEn;
    /**汇款人地址（英文）*/
    @Excel(name = "汇款人地址（英文）")
    private String payerAddEn;
    /**收款账号*/
    @Excel(name = "*收款账号")
    @NotNull( message = "nis.netbank.message.payeeacc.null")
    private String payeeAcc;
    /**收款账号名称*/
    @Excel(name = "*收款人名称")
    @NotNull( message = "fex.foreign.message.payeeaccname.null")
    private String payeeAccName;
    /**收款人地址*/
    @Excel(name = "收款人地址")
    private String payeeAdd;
    /**收款开户行swift code*/
    @Excel(name = "收款人开户行SWIFT CODE")
    private String payeeSwiftCode;
    /**收款开户行名称*/
    @Excel(name = "收款开户行名称")
    private String payeeBank;
    /**收款开户行地址*/
    @Excel(name = "收款开户行地址")
    private String payeeBankAdd;
    /**收款方国别*/
    private String payeeCountryCode;
    /**收款方国别名称*/
    @Excel(name = "*收款方国别")
    private String payeeCountryName;
    /**收款银行之代理行swift code*/
    private String payeeAgentSwiftCode;
    /**收款银行之代理行名称*/
    @Excel(name = "*收款银行之代理行名称")
    private String payeeAgentBank;
    /**收款银行之代理行地址*/
    @Excel(name = "收款银行之代理行地址")
    private String payeeAgentBankAdd;
    /**收款人开户行银行在其代理行账号*/
    @Excel(name = "收款人开户行银行在其代理行账号")
    private String payeeAgentAcc;
    /**汇款金额*/
    @Excel(name = "*汇款金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NotNull( message = "fex.foreign.message.paymoney.null")
    @BtCompositeSearch(fieldName = "f.pay_Money")
    private BigDecimal payMoney;
    /**购汇汇率*/
    @Excel(name = "购汇汇率", align = HorizontalAlignment.RIGHT, numFormat = "##########0.000000")
    private BigDecimal foreignExrate;
    /**购汇金额*/
    private BigDecimal foreignMoney;
    /**汇款附言*/
    @Excel(name = "汇款附言")
    private String addWord;
    /**备注*/
    @Excel(name = "备注")
    private String rmk;
    /**发电等级 0-普通 Normal  1-加急 Urgent*/
    @Excel(name = "*发电等级", replace={"普通 Normal_0","加急 Urgent_1"})
    @NotNull( message = "fex.foreign.message.quicksign.null")
    private String quickSign;
    /**付汇性质 1-保税区、2-出口加工区、3-钻石交易所、4-其他特殊经济区域、5-深加工结转、6-离岸账户、7-其他*/
    @Excel(name = "付汇性质", replace={"保税区_1","出口加工区_2","钻石交易所_3","其他特殊经济区域_4","深加工结转_5","离岸账户_6","其他_7"})
    private String remitNature;
    /**汇款方式 0-电汇 1-票汇*/
    @Excel(name = "*汇款方式", replace={"电汇_0","票汇_1"})
    @NotNull( message = "fex.foreign.message.remitmethod.null")
    private String remitMethod;
    /**国内外费用承担标志 1-汇款人 OUR  2-收款人 BEN  3-共同 SHA*/
    @Excel(name = "*国内外费用承担标志", replace={"汇款人 OUR_1","收款人 BEN_2","共同 SHA_3"})
    @NotNull( message = "fex.foreign.message.costsign.null")
    private String costSign;
    /**付款类型 1-预付货款 2-货到付款 3-退款 4-其他*/
    @Excel(name = "*付款类型", replace={"预付货款_1","货到付款_2","退款_3","其他_4"})
    @NotNull( message = "fex.foreign.message.paytype.null")
    private String payType;
    /**最迟装运日期*/
    @Excel(name = "*最迟装运日期",format = "yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date shipDate;
    /**是否为保税货物项下付款 0-否 1-是*/
    @Excel(name = "*是否为保税货物项下付款", replace={"否_0","是_1"})
    @NotNull( message = "fex.foreign.message.isbonded.null")
    private String isBonded;
    /**外汇局批件/备案表号/业务编号*/
    @Excel(name = "外汇局批件/备案表号/业务编号")
    private String busNo;
    /**合同号*/
    @Excel(name = "合同号")
    private String contractNo;
    /**发票号*/
    @Excel(name = "发票号")
    private String invoiceNo;
    /**交易编码1*/
    @TranCode(inject="tranName1")
    private String tranCode1;
    /**项目名称1*/
    @Excel(name = "*交易编码1")
    @NotNull( message = "fex.foreign.message.trancode1.null")
    private String tranName1;
    /**相应金额1*/
    @Excel(name = "*相应金额1", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    @NotNull( message = "fex.foreign.message.tranamt1.null")
    private BigDecimal tranAmt1;
    /**相应币种1*/
    @Currency(inject="tranCurName1")
    private String tranCurId1;
    /**相应币种名称1*/
    private String tranCurName1;
    /**交易附言1*/
    @Excel(name = "*交易附言1")
    @NotNull( message = "fex.foreign.message.tranaddword1.null")
    private String tranAddWord1;
    /**交易编码2*/
    @TranCode(inject="tranName2")
    private String tranCode2;
    /**项目名称2*/
    @Excel(name = "交易编码2")
    private String tranName2;
    /**相应金额2*/
    @Excel(name = "相应金额2", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
    private BigDecimal tranAmt2;
    /**相应币种2*/
    @Currency(inject="tranCurName2")
    private String tranCurId2;
    /**相应币种名称2*/
    private String tranCurName2;
    /**交易附言2*/
    @Excel(name = "交易附言2")
    private String tranAddWord2;
    @Excel(name = "*计划科目")
    private String itemCodeImp;
    /**状态*/
    @BtCompositeSearch(fieldName = "f.status")
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
    /**账户余额*/
    private BigDecimal balance;
    /**是否上线*/
    private String isOnline;
    /**交易状态*/
    @BtCompositeSearch(fieldName = "c.voucher_Stat")
    private String voucherStat;
    /**指令ID*/
    private String bisForeignExcId;
    /**计划科目*/
    private String[] itemCode;
    /**计划科目名称*/
    private String[] itemName;
    /**执行金额*/
    private BigDecimal[] processMoney;
    /**汇率*/
    private BigDecimal[] exchangeRate;
    /**执行金额（折合本身币别）*/
    private BigDecimal[] processCurMoney;
    /**计划上报单位*/
    private String[] budgetCorpId;
    /**执行明细信息*/
    private String[] processDetailInfo;
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
    @BtCompositeSearch(fieldName = "f.bill_From")
    private String billFrom;
    /**ERP编号*/
    private String erpId;
    /**是否已付款*/
    private String isPayed;
    /**跨行标识*/
    @Excel(name = "*跨行标识")
    private String difBank;
    /**支付类型*/
    private String bankPayType;
    /**现金流项目*/
    private String cashItem;
    private String cashItemName;
    /**受益人关联关系*/
    private String bnfRelate;

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

    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
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

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
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

    public String getPayerNameEn() {
        return payerNameEn;
    }

    public void setPayerNameEn(String payerNameEn) {
        this.payerNameEn = payerNameEn;
    }

    public String getPayerAddEn() {
        return payerAddEn;
    }

    public void setPayerAddEn(String payerAddEn) {
        this.payerAddEn = payerAddEn;
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

    public String getPayeeAdd() {
        return payeeAdd;
    }

    public void setPayeeAdd(String payeeAdd) {
        this.payeeAdd = payeeAdd;
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

    public String getPayeeBankAdd() {
        return payeeBankAdd;
    }

    public void setPayeeBankAdd(String payeeBankAdd) {
        this.payeeBankAdd = payeeBankAdd;
    }

    public String getPayeeCountryCode() {
        return payeeCountryCode;
    }

    public void setPayeeCountryCode(String payeeCountryCode) {
        this.payeeCountryCode = payeeCountryCode;
    }

    public String getPayeeCountryName() {
        return payeeCountryName;
    }

    public void setPayeeCountryName(String payeeCountryName) {
        this.payeeCountryName = payeeCountryName;
    }

    public String getPayeeAgentSwiftCode() {
        return payeeAgentSwiftCode;
    }

    public void setPayeeAgentSwiftCode(String payeeAgentSwiftCode) {
        this.payeeAgentSwiftCode = payeeAgentSwiftCode;
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

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getForeignExrate() {
        return foreignExrate;
    }

    public void setForeignExrate(BigDecimal foreignExrate) {
        this.foreignExrate = foreignExrate;
    }

    public BigDecimal getForeignMoney() {
        return foreignMoney;
    }

    public void setForeignMoney(BigDecimal foreignMoney) {
        this.foreignMoney = foreignMoney;
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

    public String getQuickSign() {
        return quickSign;
    }

    public void setQuickSign(String quickSign) {
        this.quickSign = quickSign;
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

    public String getTranName2() {
        return tranName2;
    }

    public void setTranName2(String tranName2) {
        this.tranName2 = tranName2;
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

    public String getTranCurName1() {
        return tranCurName1;
    }

    public void setTranCurName1(String tranCurName1) {
        this.tranCurName1 = tranCurName1;
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

    public String getTranName1() {
        return tranName1;
    }

    public void setTranName1(String tranName1) {
        this.tranName1 = tranName1;
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

    public String getTranCurName2() {
        return tranCurName2;
    }

    public void setTranCurName2(String tranCurName2) {
        this.tranCurName2 = tranCurName2;
    }

    public String getTranAddWord2() {
        return tranAddWord2;
    }

    public void setTranAddWord2(String tranAddWord2) {
        this.tranAddWord2 = tranAddWord2;
    }

    public String getItemCodeImp() {
        return itemCodeImp;
    }

    public void setItemCodeImp(String itemCodeImp) {
        this.itemCodeImp = itemCodeImp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    public String getBisForeignExcId() {
        return bisForeignExcId;
    }

    public void setBisForeignExcId(String bisForeignExcId) {
        this.bisForeignExcId = bisForeignExcId;
    }

    public String[] getItemCode() {
        return itemCode;
    }

    public void setItemCode(String[] itemCode) {
        this.itemCode = itemCode;
    }

    public String[] getItemName() {
        return itemName;
    }

    public void setItemName(String[] itemName) {
        this.itemName = itemName;
    }

    public BigDecimal[] getProcessMoney() {
        return processMoney;
    }

    public void setProcessMoney(BigDecimal[] processMoney) {
        this.processMoney = processMoney;
    }

    public BigDecimal[] getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal[] exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal[] getProcessCurMoney() {
        return processCurMoney;
    }

    public void setProcessCurMoney(BigDecimal[] processCurMoney) {
        this.processCurMoney = processCurMoney;
    }

    public String[] getBudgetCorpId() {
        return budgetCorpId;
    }

    public void setBudgetCorpId(String[] budgetCorpId) {
        this.budgetCorpId = budgetCorpId;
    }

    public String[] getProcessDetailInfo() {
        return processDetailInfo;
    }

    public void setProcessDetailInfo(String[] processDetailInfo) {
        this.processDetailInfo = processDetailInfo;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getRemitRoute() {
        return remitRoute;
    }

    public void setRemitRoute(String remitRoute) {
        this.remitRoute = remitRoute;
    }

    public String getRemitUse() {
        return remitUse;
    }

    public void setRemitUse(String remitUse) {
        this.remitUse = remitUse;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDeclareNo() {
        return declareNo;
    }

    public void setDeclareNo(String declareNo) {
        this.declareNo = declareNo;
    }

    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    public String getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }

    public String getErpId() {
        return erpId;
    }

    public void setErpId(String erpId) {
        this.erpId = erpId;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    public String getCurEnglishCode() {
        return curEnglishCode;
    }

    public void setCurEnglishCode(String curEnglishCode) {
        this.curEnglishCode = curEnglishCode;
    }

    public String getCashItem() {
        return cashItem;
    }

    public void setCashItem(String cashItem) {
        this.cashItem = cashItem;
    }

    public String getCashItemName() {
        return cashItemName;
    }

    public void setCashItemName(String cashItemName) {
        this.cashItemName = cashItemName;
    }

    public String getBnfRelate() {
        return bnfRelate;
    }

    public void setBnfRelate(String bnfRelate) {
        this.bnfRelate = bnfRelate;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }

    @Override
    public String getBeanName() {
        return "fexForeignReqService";
    }
}
