package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.check.vo.IBaseDataInject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Title: VO
 * @Description: 结算现金流表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
public class FexCashFlowVO implements Serializable, IBaseDataInject, IBusinessKey {
    private static final long serialVersionUID = 3650731181127660208L;
    /**单号*/
    @BtCompositeSearch(fieldName = "t.id")
    private String id;
    /**流水号*/
    @BtCompositeSearch(fieldName = "t.bill_No")
    private String billNo;
    /**交易ID*/
    private String tranBillId;
    @BtCompositeSearch(fieldName = "r.bill_No")
    private String tranBillNo;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    @BtCompositeSearch(fieldName = "r.tran_Type")
    @Dict(dictGroup = "fexCorp.tranType", inject = "tranTypeName")
    private String tranType;
    @BtCompositeSearch(fieldName = "r.tran_Type")
    private String tranTypeName;
    /**现金流日期*/
    @BtCompositeSearch(fieldName = "t.cash_Flow_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date cashFlowDate;
    /**结算类型：1-全额，2-净额*/
    @BtCompositeSearch(fieldName = "t.settle_Type")
    @Dict(dictGroup = "fexCashFlow.settleType", inject = "settleTypeName")
    private String settleType;
    @BtCompositeSearch(fieldName = "t.settle_Type")
    private String settleTypeName;
    /**单位/源单位*/
    @BtCompositeSearch(fieldName = "t.corp_Id")
    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "t.corp_Id")
    private String corpName;
    /**现金流类型：0本金，1利息*/
    @BtCompositeSearch(fieldName = "t.cash_Flow_Type")
    @Dict(dictGroup = "fexCorp.cashFlowType", inject = "cashFlowTypeName")
    private String cashFlowType;
    @BtCompositeSearch(fieldName = "t.cash_Flow_Type")
    private String cashFlowTypeName;
    /**主体单位银行账户ID*/
    private String corpAccId;
    /**主体单位银行*/
    @BtCompositeSearch(fieldName = "t.corp_Bank")
    @BankType(inject = "corpBankName")
    private String corpBank;
    @BtCompositeSearch(fieldName = "t.corp_Bank")
    private String corpBankName;
    /**主体单位银行账户*/
    @BtCompositeSearch(fieldName = "t.corp_Bank_Acc")
    private String corpBankAcc;
    @BtCompositeSearch(fieldName = "t.corp_Acc_Name")
    private String corpAccName;
    /**交易对手*/
    @BtCompositeSearch(fieldName = "t.cotp_Id")
    private String cotpId;
    @BtCompositeSearch(fieldName = "t.cotp_Id")
    private String cotpName;
    /**交易对手银行*/
    @BtCompositeSearch(fieldName = "t.cotp_Bank")
    private String cotpBank;
    @BtCompositeSearch(fieldName = "t.cotp_Bank")
    private String cotpBankName;
    /**交易对手银行账号*/
    private String cotpAccId;
    @BtCompositeSearch(fieldName = "t.cotp_Bank_Acc")
    private String cotpBankAcc;
    @BtCompositeSearch(fieldName = "t.cotp_Acc_Name")
    private String cotpAccName;
    /**币别*/
    @BtCompositeSearch(fieldName = "t.cur_Id")
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    private String curId;
    @BtCompositeSearch(fieldName = "t.cur_Id")
    private String curName;
    @BtCompositeSearch(fieldName = "t.cur_Id")
    private String englishCode;
    /**金额*/
    @BtCompositeSearch(fieldName = "t.pay_Money")
    private BigDecimal payMoney;
    /**收付方向 0-收款 1-付款*/
    @BtCompositeSearch(fieldName = "t.cd_Sign")
    @Dict(dictGroup = "nisBusType.cdSign", inject = "cdSignName")
    private String cdSign;
    @BtCompositeSearch(fieldName = "t.cd_Sign")
    private String cdSignName;
    /**结算日期*/
    @BtCompositeSearch(fieldName = "t.settle_Date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date settleDate;
    /**支付方式*/
    @BtCompositeSearch(fieldName = "t.voucher_Type_Id")
    @Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherTypeName")
    private String voucherTypeId;
    @BtCompositeSearch(fieldName = "t.voucher_Type_Id")
    private String voucherTypeName;
    /**实际收款金额*/
    @BtCompositeSearch(fieldName = "t.facts_Money")
    private BigDecimal factsMoney;
    /**实际收款日期*/
    @BtCompositeSearch(fieldName = "t.facts_Date")
    private Date factsDate;
    /**净额结算号*/
    @BtCompositeSearch(fieldName = "t.settle_No")
    private String settleNo;
    /**备注*/
    private String rmk;
    /**附件*/
    private String attachmentId;
    /**状态*/
    @BtCompositeSearch(fieldName = "t.status")
    private Integer status;
    @BtCompositeSearch(fieldName = "t.status")
    private String statusStr;
    /**银行落地限额处理结果：0待执行/A同意/R拒绝；未涉及落地为 null*/
    @BtCompositeSearch(fieldName = "t.LANDING_LIMIT_RESULT")
    private String landingLimitResult;
    /**银行落地限额处理结果名称（service 按当前语言查字典回填，供前后端分离接口直接展示）*/
    @BtCompositeSearch(fieldName = "t.LANDING_LIMIT_RESULT")
    private String landingLimitResultName;
    /**支付状态 0-待付款 1-已付款*/
    @BtCompositeSearch(fieldName = "t.pay_Status")
    private String payStatus;
    /**交易状态*/
    @Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
    @BtCompositeSearch(fieldName = "exc.voucher_Stat")
    private String voucherStat;
    /**交易状态*/
    @BtCompositeSearch(fieldName = "exc.voucher_Stat")
    private String voucherStatName;
    /**指令ID*/
    private String bisForeignExcId;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**操作类型 0-结算处理、1-修改支付方式、2-修改单位账户、3-交易对手账户、4-更新支付状态*/
    private String opType;
    /**产品ID*/
    @BtCompositeSearch(fieldName = "r.pro_Id")
    private String proId;
    /**产品名称*/
    @BtCompositeSearch(fieldName = "r.pro_Id")
    private String proName;
    /**合同汇率*/
    private BigDecimal contractRate;
    /**交易组号码*/
    @BtCompositeSearch(fieldName = "g.bill_No")
    private String tranGroupBillNo;
    /**交易组名称*/
    private String tranGroupName;
    /**交易日期*/
    @BtCompositeSearch(fieldName = "r.tran_date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date tranDate;
    /**资金性质*/
    private String accNature;

    /**交易对手-资金调拨交易*/
    @Corp(inject = "allocatCotpName")
    @BtCompositeSearch(fieldName = "t.cotp_id")
    private String allocatCotpId;
    private String allocatCotpName;
    /**交易对手银行-资金调拨交易*/
    @BankType(inject = "allocatCotpBankName")
    @BtCompositeSearch(fieldName = "t.cotp_bank")
    private String allocatCotpBank;
    private String allocatCotpBankName;
    /**汇款类型 0-境内付款 1-境外付款*/
    private String remitType;
    /**保证方式,1-定期保证金、2-活期保证金、3-其它保证方式、4-授信额度*/
    private String boodType;
    /**保证金账号*/
    private String boodAcc;
    /**保证金比例*/
    private BigDecimal boodRatio;
    /**保证金钞汇标识,1-现钞，2-现汇*/
    private String boodBankNote;
    /**保证金币种*/
    private String bondCurId;
    /**加急标志 0-否 1-是*/
    @Dict(dictGroup = "sf_yn", inject = "isQuickName")
    private String isQuick;
    private String isQuickName;
    /**跨行标识*/
    private String difBank;
    /** 银行流水认领功能挂接标记，未认领=0,认领=1*/
    private String bankFlowSign;
    /** 银行流水认领前状态*/
    private Integer oldStatus;
    /**付款账号对应的币别*/
    private String payerCurId;
    /**收款账号对应的币种*/
    @Currencys(
            {
                    @Currency(inject="payeeCurName"),
                    @Currency(inject="payeeEnglishCode", field = "englishCode")
            }
    )
    private String payeeCurId;
    private String payeeCurName;
    private String payeeEnglishCode;
    /**会计凭证号*/
    @BtCompositeSearch(fieldName = "r.bursar_Bill_No")
    private String bursarBillNo;

    /**是否线下付款*/
    @BtCompositeSearch(fieldName = "r.is_Payed")
    private String isPayed;
    /**现金流项目*/
    private String cashItem;
    private String cashItemName;
    /**银行流水号*/
    private String bankSerialNo;
    /**单据来源*/
    @BtCompositeSearch(fieldName = "t.bill_From")
    @Dict(dictGroup = "foreign.billFrom", inject = "billFromName")
    private String billFrom;
    @BtCompositeSearch(fieldName = "t.bill_From")
    private String billFromName;
    /**关联现金流号码*/
    @BtCompositeSearch(fieldName = "t.rel_Bill_No")
    private String relBillNo;

    /**交易对手数据源id*/
    private String oppId;

    /** swiftCode*/
    private String bicCode;

    /** 中转行*/
    private String tranBank;

    /** 中转行swiftCode*/
    private String tranBicCode;

    /** 支付类型*/
    private String payType;

    /** 中转行地址*/
    private String tranBankAddr;

    /** 在中转行的银行账户*/
    private String tranBankAcc;

    /** 银行地址*/
    private String bankAddr;

    /** 银行信息备注*/
    private String bankRmk;

    /**
     * 付款勾兑状态
     */
    private String blendSign;


    public String getBicCode() {
        return bicCode;
    }

    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    public String getTranBank() {
        return tranBank;
    }

    public void setTranBank(String tranBank) {
        this.tranBank = tranBank;
    }

    public String getTranBicCode() {
        return tranBicCode;
    }

    public void setTranBicCode(String tranBicCode) {
        this.tranBicCode = tranBicCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTranBankAddr() {
        return tranBankAddr;
    }

    public void setTranBankAddr(String tranBankAddr) {
        this.tranBankAddr = tranBankAddr;
    }

    public String getTranBankAcc() {
        return tranBankAcc;
    }

    public void setTranBankAcc(String tranBankAcc) {
        this.tranBankAcc = tranBankAcc;
    }

    public String getBankAddr() {
        return bankAddr;
    }

    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }

    public String getBankRmk() {
        return bankRmk;
    }

    public void setBankRmk(String bankRmk) {
        this.bankRmk = bankRmk;
    }

    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    private List<FexCashFlowCustInfoVO> fexCashFlowCustInfos;

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

    public String getTranBillId() {
        return tranBillId;
    }

    public void setTranBillId(String tranBillId) {
        this.tranBillId = tranBillId;
    }

    public String getTranBillNo() {
        return tranBillNo;
    }

    public void setTranBillNo(String tranBillNo) {
        this.tranBillNo = tranBillNo;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getTranTypeName() {
        return tranTypeName;
    }

    public void setTranTypeName(String tranTypeName) {
        this.tranTypeName = tranTypeName;
    }

    public Date getCashFlowDate() {
        return cashFlowDate;
    }

    public void setCashFlowDate(Date cashFlowDate) {
        this.cashFlowDate = cashFlowDate;
    }

    public String getSettleType() {
        return settleType;
    }

    public void setSettleType(String settleType) {
        this.settleType = settleType;
    }

    public String getSettleTypeName() {
        return settleTypeName;
    }

    public void setSettleTypeName(String settleTypeName) {
        this.settleTypeName = settleTypeName;
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

    public String getCashFlowType() {
        return cashFlowType;
    }

    public void setCashFlowType(String cashFlowType) {
        this.cashFlowType = cashFlowType;
    }

    public String getCashFlowTypeName() {
        return cashFlowTypeName;
    }

    public void setCashFlowTypeName(String cashFlowTypeName) {
        this.cashFlowTypeName = cashFlowTypeName;
    }

    public String getCorpAccId() {
        return corpAccId;
    }

    public void setCorpAccId(String corpAccId) {
        this.corpAccId = corpAccId;
    }

    public String getCorpBank() {
        return corpBank;
    }

    public void setCorpBank(String corpBank) {
        this.corpBank = corpBank;
    }

    public String getCorpBankName() {
        return corpBankName;
    }

    public void setCorpBankName(String corpBankName) {
        this.corpBankName = corpBankName;
    }

    public String getCorpBankAcc() {
        return corpBankAcc;
    }

    public void setCorpBankAcc(String corpBankAcc) {
        this.corpBankAcc = corpBankAcc;
    }

    public String getCorpAccName() {
        return corpAccName;
    }

    public void setCorpAccName(String corpAccName) {
        this.corpAccName = corpAccName;
    }

    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    public String getCotpName() {
        return cotpName;
    }

    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    public String getCotpBank() {
        return cotpBank;
    }

    public void setCotpBank(String cotpBank) {
        this.cotpBank = cotpBank;
    }

    public String getCotpBankName() {
        return cotpBankName;
    }

    public void setCotpBankName(String cotpBankName) {
        this.cotpBankName = cotpBankName;
    }

    public String getCotpAccId() {
        return cotpAccId;
    }

    public void setCotpAccId(String cotpAccId) {
        this.cotpAccId = cotpAccId;
    }

    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    public String getCotpAccName() {
        return cotpAccName;
    }

    public void setCotpAccName(String cotpAccName) {
        this.cotpAccName = cotpAccName;
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

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getCdSignName() {
        return cdSignName;
    }

    public void setCdSignName(String cdSignName) {
        this.cdSignName = cdSignName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    public String getVoucherTypeId() {
        return voucherTypeId;
    }

    public void setVoucherTypeId(String voucherTypeId) {
        this.voucherTypeId = voucherTypeId;
    }

    public String getVoucherTypeName() {
        return voucherTypeName;
    }

    public void setVoucherTypeName(String voucherTypeName) {
        this.voucherTypeName = voucherTypeName;
    }

    public BigDecimal getFactsMoney() {
        return factsMoney;
    }

    public void setFactsMoney(BigDecimal factsMoney) {
        this.factsMoney = factsMoney;
    }

    public Date getFactsDate() {
        return factsDate;
    }

    public void setFactsDate(Date factsDate) {
        this.factsDate = factsDate;
    }

    public String getSettleNo() {
        return settleNo;
    }

    public void setSettleNo(String settleNo) {
        this.settleNo = settleNo;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLandingLimitResult() {
        return landingLimitResult;
    }

    public void setLandingLimitResult(String landingLimitResult) {
        this.landingLimitResult = landingLimitResult;
    }

    public String getLandingLimitResultName() {
        return landingLimitResultName;
    }

    public void setLandingLimitResultName(String landingLimitResultName) {
        this.landingLimitResultName = landingLimitResultName;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getVoucherStat() {
        return voucherStat;
    }

    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }

    public String getVoucherStatName() {
        return voucherStatName;
    }

    public void setVoucherStatName(String voucherStatName) {
        this.voucherStatName = voucherStatName;
    }

    public String getBisForeignExcId() {
        return bisForeignExcId;
    }

    public void setBisForeignExcId(String bisForeignExcId) {
        this.bisForeignExcId = bisForeignExcId;
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

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public BigDecimal getContractRate() {
        return contractRate;
    }

    public void setContractRate(BigDecimal contractRate) {
        this.contractRate = contractRate;
    }

    public String getTranGroupBillNo() {
        return tranGroupBillNo;
    }

    public void setTranGroupBillNo(String tranGroupBillNo) {
        this.tranGroupBillNo = tranGroupBillNo;
    }

    public String getTranGroupName() {
        return tranGroupName;
    }

    public void setTranGroupName(String tranGroupName) {
        this.tranGroupName = tranGroupName;
    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    public List<FexCashFlowCustInfoVO> getFexCashFlowCustInfos() {
        return fexCashFlowCustInfos;
    }

    public void setFexCashFlowCustInfos(List<FexCashFlowCustInfoVO> fexCashFlowCustInfos) {
        this.fexCashFlowCustInfos = fexCashFlowCustInfos;
    }

    public String getAllocatCotpId() {
        return allocatCotpId;
    }

    public void setAllocatCotpId(String allocatCotpId) {
        this.allocatCotpId = allocatCotpId;
    }

    public String getAllocatCotpName() {
        return allocatCotpName;
    }

    public void setAllocatCotpName(String allocatCotpName) {
        this.allocatCotpName = allocatCotpName;
    }

    public String getAllocatCotpBank() {
        return allocatCotpBank;
    }

    public void setAllocatCotpBank(String allocatCotpBank) {
        this.allocatCotpBank = allocatCotpBank;
    }

    public String getAllocatCotpBankName() {
        return allocatCotpBankName;
    }

    public void setAllocatCotpBankName(String allocatCotpBankName) {
        this.allocatCotpBankName = allocatCotpBankName;
    }

    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public String getBoodType() {
        return boodType;
    }

    public void setBoodType(String boodType) {
        this.boodType = boodType;
    }

    public String getBoodAcc() {
        return boodAcc;
    }

    public void setBoodAcc(String boodAcc) {
        this.boodAcc = boodAcc;
    }

    public BigDecimal getBoodRatio() {
        return boodRatio;
    }

    public void setBoodRatio(BigDecimal boodRatio) {
        this.boodRatio = boodRatio;
    }

    public String getBoodBankNote() {
        return boodBankNote;
    }

    public void setBoodBankNote(String boodBankNote) {
        this.boodBankNote = boodBankNote;
    }

    public String getBondCurId() {
        return bondCurId;
    }

    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }

    public String getIsQuick() {
        return isQuick;
    }

    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
    }

    public String getIsQuickName() {
        return isQuickName;
    }

    public void setIsQuickName(String isQuickName) {
        this.isQuickName = isQuickName;
    }

    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    @Override
    public String getBusinessKey() {
        return getId();
    }

    public String getBankFlowSign() {
        return bankFlowSign;
    }

    public void setBankFlowSign(String bankFlowSign) {
        this.bankFlowSign = bankFlowSign;
    }

    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getPayerCurId() {
        return payerCurId;
    }

    public void setPayerCurId(String payerCurId) {
        this.payerCurId = payerCurId;
    }

    public String getPayeeCurId() {
        return payeeCurId;
    }

    public void setPayeeCurId(String payeeCurId) {
        this.payeeCurId = payeeCurId;
    }

    public String getPayeeCurName() {
        return payeeCurName;
    }

    public void setPayeeCurName(String payeeCurName) {
        this.payeeCurName = payeeCurName;
    }

    public String getPayeeEnglishCode() {
        return payeeEnglishCode;
    }

    public void setPayeeEnglishCode(String payeeEnglishCode) {
        this.payeeEnglishCode = payeeEnglishCode;
    }

    public String getBursarBillNo() {
        return bursarBillNo;
    }

    public void setBursarBillNo(String bursarBillNo) {
        this.bursarBillNo = bursarBillNo;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
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

    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    public String getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }

    public String getBillFromName() {
        return billFromName;
    }

    public void setBillFromName(String billFromName) {
        this.billFromName = billFromName;
    }

    public String getRelBillNo() {
        return relBillNo;
    }

    public void setRelBillNo(String relBillNo) {
        this.relBillNo = relBillNo;
    }

    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }
}
