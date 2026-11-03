package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 交易管理表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Entity
@Table(name = "FEX_TRAN_REQ", schema = "")
@SuppressWarnings("serial")
public class FexTranReq extends IdEntity implements java.io.Serializable {
    /**流水号*/
    private String billNo;
    /**交易需求申请从表ID*/
    private String tranAppDtlId;
    /**交易类型 1-外汇交易、2-外汇期权交易、3-调拨交易、4-资金收付交易、5-货币市场交易、6-债券交易*/
    private String tranType;
    /**是否掉期交易 1-是、0-否*/
    private String isSwapTran;
    /**产品ID*/
    private String proId;
    /**外汇业务类型 1-结汇、2-售汇、3-外汇买卖*/
    private String fexBusType;
    /**外汇交易类型(FexTranTypeEnum) 0-即期、1-远期、2-掉期*/
    private String fexTranType;
    /**交割类型 0-本金交割、1-无本金交割*/
    private String deliveryType;
    /**交割币种*/
    private String deliveryCurId;
    /**买入币种*/
    private String buyCurId;
    /**卖出币种*/
    private String sellCurId;
    /**买入金额*/
    private BigDecimal buyMoney;
    /**卖出金额*/
    private BigDecimal sellMoney;
    /**合同汇率*/
    private BigDecimal contractRate;
    /**外汇合同号*/
    private String foreignContractNo;
    /**即期汇率*/
    private BigDecimal spotRate;
    /**钞汇标识 1-现钞 2-现汇 */
    private String bankNote;
    /**交易日期*/
    private Date tranDate;
    /**结算日期*/
    private Date settleDate;
    /**交割期限*/
    private String deliveryDeadline;
    /**保证方式,1-定期保证金、2-活期保证金、3-其它保证方式、4-授信额度*/
    private String boodType;
    /**保证金账号*/
    private String boodAccId;
    private String boodAcc;
    /**保证金比例*/
    private BigDecimal boodRatio;
    /**保证金钞汇标识,1-现钞，2-现汇*/
    private String boodBankNote;
    /**保证金币种*/
    private String bondCurId;
    /**合同号*/
    private String contractNo;
    /**远期点数*/
    private BigDecimal forwardPoints;
    /**外汇局批件号/备案表号/业务编号*/
    private String busNo;
    /**结汇资金用途*/
    private String purpose;
    /**结汇资金详细用途*/
    private String purposeDtl;
    /**资金来源,1-货物贸易、2-服务贸易收益与经常转移、3-资本与金融项目*/
    private String fundsSource;
    /**发票号*/
    private String invoiceNo;
    /**账户资金性质 1-国内主账户、2-国外主账户、3-经常项目、4-资本项目、5-NRA账户*/
    private String accNature;
    /**结售汇属性*/
    private String fexProp;
    /**其它保证方式说明*/
    private String boodRmk;
    /**备注（英文）*/
    private String rmk;
    /**备注(本地语言)*/
    private String rmkLocal;
    /**附件*/
    private String attachmentId;
    /**交易对手*/
    private String cotpId;
    /**交易对手银行*/
    private String cotpBank;
    /**交易对手银行账号*/
    private String cotpAccId;
    private String cotpBankAcc;
    private String cotpAccName;
    private String cotpAccCurId;
    /**交易对手国家地区*/
    private String cotpCountry;
    /**交易对手地点*/
    private String cotpAddress;
    /**交易对手省（国家地区为中国时需要）*/
    private String cotpProv;
    /**交易对手市（国家地区为中国时需要）*/
    private String cotpCity;
    /**币种*/
    private String curId;
    /**收付方向 0-收款 1-付款*/
    private String cdSign;
    /**被代付/代付单位*/
    private String payCorpId;
    /**金额*/
    private BigDecimal payMoney;
    /**汇率折算金额*/
    private BigDecimal payMoneyConvert;
    /**加急标志 0-否 1-是*/
    private String isQuick;
    /**主体单位银行账户ID*/
    private String corpAccId;
    /**主体单位银行账户币种*/
    private String corpAccCurId;
    /**主体单位银行*/
    private String corpBank;
    /**主体单位银行账户*/
    private String corpBankAcc;
    private String corpAccName;
    /**汇款类型 0-境内付款 1-境外付款*/
    private String remitType;
    /**汇款方式 0-电汇 1-票汇*/
    private String remitMethod;
    /**主体单位名称（英文）*/
    private String corpNameEn;
    /**主体单位地址（英文）*/
    private String corpAddrEn;
    /**主体单位地址（中文）*/
    private String corpAddr;
    /**主体单位联系人*/
    private String corpContact;
    /**主体单位联系电话*/
    private String corpContactTel;
    /**国内外费用承担,1-汇款人、2-收款人、3-共同*/
    private String costSign;
    /**费用支出账户*/
    private String costAccId;
    private String costAcc;
    /**费用支出账户2*/
    private String costAccId2;
    private String costAcc2;
    /**付款类型,1-预付货款，2-货到付款，3-退款，4-其他*/
    private String payType;
    /**最迟装运日期*/
    private Date shipDate;
    /**是否为保税货物项付款 0-否 1-是*/
    private String isBonded;
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
    /**付汇性质 1-保税区、2-出口加工区、3-钻石交易所、4-其他特殊经济区域、5-深加工结转、6-离岸账户、7-其他*/
    private String remitNature;
    /**状态*/
    private Integer status;
    /**单位/源单位*/
    private String corpId;
    /**资金用途*/
    private String capitalUse;
    /**源银行*/
    private String srcBank;
    /**源银行账户*/
    private String srcAccId;
    private String srcBankAcc;
    private String srcAccName;
    /**目标银行*/
    private String targetBank;
    /**目标银行账户*/
    private String targetAccId;
    private String targetBankAcc;
    private String targetAccName;
    /**目标银行账户币种*/
    private String targetAccCurId;
    /**目标单位*/
    private String targetCorp;
    /**代理行*/
    private String agentBank;
    /**开户行在代理行账户*/
    private String agentBankAcc;
    /**收款人常驻国家或者地区*/
    private String countryId;
    /**交易对手业务编号*/
    private String cotpNo;
    /**交易地点*/
    private String tranAddr;
    /**所属交易组*/
    private String tranGroup;
    /**近/远端  1-近端，2-远端*/
    private String swapType;
    /**近/远端ID*/
    private String partnerId;
    /**交易属性,1-近端交易，2-远端交易*/
    private String tranAttr;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**结汇业务类型*/
    private String settleBusType;
    /**自动计算买入金额*/
    private String autoCal;
    /**固定金额 1-买入金额 2-卖出金额*/
    private String fixedMoney;
    /**波动率*/
    private BigDecimal volatility;
    /**买卖方向*/
    private String busDirection;
    /**涨跌方向*/
    private String updownDirection;
    /**到期日期*/
    private Date expiresDate;
    /**平均期开始日期*/
    private Date avgBeginDate;
    /**平均期结束日期*/
    private Date avgEndDate;
    /**平均期天数*/
    private BigDecimal avgDays;
    /**障碍验证标准*/
    private String validStandard;

    /**障碍值*/
    private BigDecimal exceptVal;
    /**低障碍值*/
    private BigDecimal exceptLowVal;
    /**高障碍值*/
    private BigDecimal exceptHignVal;
    /**结算方式*/
    private String tranSettleMethod;
    /**交易地点ID*/
    private String tranAddrId;
	 /**期权费日期*/
    private Date premiumDate;
    /**期权费币种*/
    private String premiumCur;
    /**期权费点数*/
    private BigDecimal premiumPoint;
    /**期权费百分比*/
    private BigDecimal premiumRatio;
    /**期权费折算汇率*/
    private BigDecimal premiumRate;
    /**期权费金额（结算币种）*/
    private BigDecimal premiumMoney;
    /**期权费金额（期权费币种）*/
    private BigDecimal premiumMoneyOfPremiumCur;
    /**是否涉及退款*/
    private String isRefund;
    /**退款点数*/
    private BigDecimal refundPoint;
    /**退款百分比*/
    private BigDecimal refundRatio;
    /**退款金额*/
    private BigDecimal refundMoney;
    /**退款支付时间*/
    private String refundTime;
    /**经纪人*/
    private String cotpAgent;
    /**清算所*/
    private String cotpClearHouse;
    /**跨行标识*/
    private String difBank;
    /**是否线下付款*/
    private String isPayed;
    /**会计凭证号*/
    private String bursarBillNo;

    /**账户类别：汇款账号对应的“账户类别”*/
    private String accType;
    /**收款人类型：0-对公居民、1-对公非居民、2-对私居民、3-对私非居民*/
    private String payeeType;
    /**汇款去向：1 境外电汇、2 港澳台电汇、4 境内系统外同城、5 境内系统内异地、6 境内系统外异地*/
    private String remitRoute;
    /**汇款用途*/
    private String remitUse;

    /**关联交易号码（外汇买卖交易-远期-已违约）*/
    private String relBillNo;
    /**办理机构*/
    private String branchCode;
    /**办理机构名称*/
    private String branchName;
    /**进口日期*/
    private Date arrivalDate;
    /**报关单号*/
    private String declareNo;
    /**汇款附言*/
    private String addWord;
    /**费用币种*/
    private String costCurId;
    /**费用总额*/
    private BigDecimal costMoney;
    /**支付类型（渣打银行）*/
    private String bankPayType;
    private String bankPayTypeName;
    private String bankPayTypeId;
    /**起运国（兴业银行）*/
    private String originCountry;
    /**拆分前单据号*/
    private String splitId;
    /**合并后单据号*/
    private String mergeId;
    /**原单据号*/
    private String origBillNo;
    /**现金流项目*/
    private String cashItem;
    /**发票日期*/
    private Date invoiceDate;
    /**发票总额*/
    private BigDecimal invoiceTotal;
    /**抵扣金额*/
    private BigDecimal deductionAmount;
    /**发票净额*/
    private BigDecimal invoiceAmount;
    /**交易对手银行账号保存字段*/
    private String saveFields;

    /**
     * FPS ID
     */
    private String fpsId;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 国家代码
     */
    private String isdCode;

    /**
     * 电话号码
     */
    private String phoneCode;
    /**
     * 信用卡号
     */
    private String aiIn;

    /**
     * 香港身份证号
     */
    private String hkId;

    /**
     * 受益人关联关系
     */
    private String bnfRelate;

    /**
     * 监管类型
     */
    private String regulatoryType;

    /**
     * 单据来源
     */
    private String billFrom;

    /**时间戳*/
    private String timeStamp;

    /**
     * 魔板ID
     */
    private String templateId;
    private String templateName;
    /**
     * 模板版本号
     */
    private String versionNo;



    /**货物贸易金额*/
    private BigDecimal goodsAmount;
    /**预付货款项下金额*/
    private BigDecimal advanceChargeAmount;
    /**预付货款项下所占合同金额比例*/
    private BigDecimal advanceContractRate;
    /**预计X天后报关*/
    private Integer rptGateDate;
    /**报关经营单位ID*/
    private String rptGateEntId;
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
    private BigDecimal advanceRate;
    /**结账期*/
    private Integer checkDay;



    @Column(name ="ORIG_BILL_NO",nullable=true,length=4000)
    public String getOrigBillNo() {
        return origBillNo;
    }

    public void setOrigBillNo(String origBillNo) {
        this.origBillNo = origBillNo;
    }

    @Column(name ="SPLIT_ID",nullable=true,length=32)
    public String getSplitId() {
        return splitId;
    }

    public void setSplitId(String splitId) {
        this.splitId = splitId;
    }

    @Column(name ="MERGE_ID",nullable=true,length=500)
    public String getMergeId() {
        return mergeId;
    }

    public void setMergeId(String mergeId) {
        this.mergeId = mergeId;
    }

    @Column(name ="BILL_NO",nullable=false,length=50)
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @Column(name ="TRAN_APP_DTL_ID",nullable=true,length=32)
    public String getTranAppDtlId() {
        return tranAppDtlId;
    }

    public void setTranAppDtlId(String tranAppDtlId) {
        this.tranAppDtlId = tranAppDtlId;
    }

    @Column(name ="TRAN_TYPE",nullable=true,length=1)
    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Column(name ="IS_SWAP_TRAN",nullable=true,length=1)
    public String getIsSwapTran() {
        return isSwapTran;
    }

    public void setIsSwapTran(String isSwapTran) {
        this.isSwapTran = isSwapTran;
    }
    @Column(name ="PREMIUM_MONEY_OF_PREMIUM_CUR")
    public BigDecimal getPremiumMoneyOfPremiumCur() {
        return premiumMoneyOfPremiumCur;
    }

    public void setPremiumMoneyOfPremiumCur(BigDecimal premiumMoneyOfPremiumCur) {
        this.premiumMoneyOfPremiumCur = premiumMoneyOfPremiumCur;
    }

    @Column(name ="PRO_ID",nullable=true,length=32)
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    @Column(name ="FEX_BUS_TYPE",nullable=true,length=1)
    public String getFexBusType() {
        return fexBusType;
    }

    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
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

    @Column(name ="DELIVERY_CUR_ID",nullable=true,length=32)
    public String getDeliveryCurId() {
        return deliveryCurId;
    }

    public void setDeliveryCurId(String deliveryCurId) {
        this.deliveryCurId = deliveryCurId;
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

    @Column(name ="BUY_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    @Column(name ="SELL_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    @Column(name ="CONTRACT_RATE",nullable=true,scale=4,length=12)
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

    @Column(name ="SPOT_RATE",nullable=true,scale=4,length=12)
    public BigDecimal getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(BigDecimal spotRate) {
        this.spotRate = spotRate;
    }

    @Column(name ="BANK_NOTE",nullable=true,length=1)
    public String getBankNote() {
        return bankNote;
    }

    public void setBankNote(String bankNote) {
        this.bankNote = bankNote;
    }

    @Column(name ="TRAN_DATE",nullable=true)
    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    @Column(name ="SETTLE_DATE",nullable=true)
    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    @Column(name ="DELIVERY_DEADLINE",nullable=true,length=50)
    public String getDeliveryDeadline() {
        return deliveryDeadline;
    }

    public void setDeliveryDeadline(String deliveryDeadline) {
        this.deliveryDeadline = deliveryDeadline;
    }

    @Column(name ="BOOD_TYPE",nullable=true,length=1)
    public String getBoodType() {
        return boodType;
    }

    public void setBoodType(String boodType) {
        this.boodType = boodType;
    }

    @Column(name ="BOOD_ACC_ID",nullable=true,length=32)
    public String getBoodAccId() {
        return boodAccId;
    }

    public void setBoodAccId(String boodAccId) {
        this.boodAccId = boodAccId;
    }

    @Column(name ="BOOD_ACC",nullable=true,length=50)
    public String getBoodAcc() {
        return boodAcc;
    }

    public void setBoodAcc(String boodAcc) {
        this.boodAcc = boodAcc;
    }

    @Column(name ="BOOD_RATIO",nullable=true,scale=6,length=8)
    public BigDecimal getBoodRatio() {
        return boodRatio;
    }

    public void setBoodRatio(BigDecimal boodRatio) {
        this.boodRatio = boodRatio;
    }

    @Column(name ="BOOD_BANK_NOTE",nullable=true,length=1)
    public String getBoodBankNote() {
        return boodBankNote;
    }

    public void setBoodBankNote(String boodBankNote) {
        this.boodBankNote = boodBankNote;
    }

    @Column(name ="BOND_CUR_ID",nullable=true,length=1)
    public String getBondCurId() {
        return bondCurId;
    }

    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }

    @Column(name ="CONTRACT_NO",nullable=true,length=50)
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Column(name ="FORWARD_POINTS",nullable=true,scale=2,length=15)
    public BigDecimal getForwardPoints() {
        return forwardPoints;
    }

    public void setForwardPoints(BigDecimal forwardPoints) {
        this.forwardPoints = forwardPoints;
    }

    @Column(name ="BUS_NO",nullable=true,length=50)
    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    @Column(name ="PURPOSE",nullable=true,length=50)
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Column(name ="PURPOSE_DTL",nullable=true,length=50)
    public String getPurposeDtl() {
        return purposeDtl;
    }

    public void setPurposeDtl(String purposeDtl) {
        this.purposeDtl = purposeDtl;
    }

    @Column(name ="FUNDS_SOURCE",nullable=true,length=1)
    public String getFundsSource() {
        return fundsSource;
    }

    public void setFundsSource(String fundsSource) {
        this.fundsSource = fundsSource;
    }

    @Column(name ="INVOICE_NO",nullable=true,length=60)
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Column(name ="ACC_NATURE",nullable=true,length=1)
    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    @Column(name ="FEX_PROP",nullable=true,length=10)
    public String getFexProp() {
        return fexProp;
    }

    public void setFexProp(String fexProp) {
        this.fexProp = fexProp;
    }

    @Column(name ="BOOD_RMK",nullable=true,length=200)
    public String getBoodRmk() {
        return boodRmk;
    }

    public void setBoodRmk(String boodRmk) {
        this.boodRmk = boodRmk;
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

    @Column(name ="ATTACHMENT_ID",nullable=true,length=32)
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name ="COTP_ID",nullable=true,length=32)
    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    @Column(name ="COTP_BANK",nullable=true,length=32)
    public String getCotpBank() {
        return cotpBank;
    }

    public void setCotpBank(String cotpBank) {
        this.cotpBank = cotpBank;
    }

    @Column(name ="COTP_ACC_ID",nullable=true,length=32)
    public String getCotpAccId() {
        return cotpAccId;
    }

    public void setCotpAccId(String cotpAccId) {
        this.cotpAccId = cotpAccId;
    }

    @Column(name ="COTP_BANK_ACC",nullable=true,length=50)
    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    @Column(name ="COTP_ACC_NAME",nullable=true,length=150)
    public String getCotpAccName() {
        return cotpAccName;
    }

    public void setCotpAccName(String cotpAccName) {
        this.cotpAccName = cotpAccName;
    }

    @Column(name ="COTP_ACC_CUR_ID",nullable=true,length=32)
    public String getCotpAccCurId() {
        return cotpAccCurId;
    }

    public void setCotpAccCurId(String cotpAccCurId) {
        this.cotpAccCurId = cotpAccCurId;
    }

    @Column(name ="COTP_COUNTRY")
    public String getCotpCountry() {
        return cotpCountry;
    }

    public void setCotpCountry(String cotpCountry) {
        this.cotpCountry = cotpCountry;
    }

    @Column(name ="COTP_ADDRESS")
    public String getCotpAddress() {
        return cotpAddress;
    }

    public void setCotpAddress(String cotpAddress) {
        this.cotpAddress = cotpAddress;
    }

    @Column(name ="COTP_PROV")
    public String getCotpProv() {
        return cotpProv;
    }

    public void setCotpProv(String cotpProv) {
        this.cotpProv = cotpProv;
    }

    @Column(name ="COTP_CITY")
    public String getCotpCity() {
        return cotpCity;
    }

    public void setCotpCity(String cotpCity) {
        this.cotpCity = cotpCity;
    }

    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="CD_SIGN",nullable=true,length=1)
    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    @Column(name ="PAY_CORP_ID",nullable=true,length=32)
    public String getPayCorpId() {
        return payCorpId;
    }

    public void setPayCorpId(String payCorpId) {
        this.payCorpId = payCorpId;
    }

    @Column(name ="PAY_MONEY",nullable=true,scale=2,length=15)
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name ="PAY_MONEY_CONVERT",nullable=true,scale=2,length=15)
    public BigDecimal getPayMoneyConvert() {
        return payMoneyConvert;
    }

    public void setPayMoneyConvert(BigDecimal payMoneyConvert) {
        this.payMoneyConvert = payMoneyConvert;
    }

    @Column(name ="IS_QUICK",nullable=true,length=1)
    public String getIsQuick() {
        return isQuick;
    }

    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
    }

    @Column(name ="CORP_ACC_ID",nullable=true,length=32)
    public String getCorpAccId() {
        return corpAccId;
    }

    public void setCorpAccId(String corpAccId) {
        this.corpAccId = corpAccId;
    }

    @Column(name ="CORP_ACC_CUR_ID",nullable=true,length=32)
    public String getCorpAccCurId() {
        return corpAccCurId;
    }

    public void setCorpAccCurId(String corpAccCurId) {
        this.corpAccCurId = corpAccCurId;
    }

    @Column(name ="CORP_BANK",nullable=true,length=100)
    public String getCorpBank() {
        return corpBank;
    }

    public void setCorpBank(String corpBank) {
        this.corpBank = corpBank;
    }

    @Column(name ="CORP_BANK_ACC",nullable=true,length=50)
    public String getCorpBankAcc() {
        return corpBankAcc;
    }

    public void setCorpBankAcc(String corpBankAcc) {
        this.corpBankAcc = corpBankAcc;
    }

    @Column(name ="CORP_ACC_NAME",nullable=true,length=150)
    public String getCorpAccName() {
        return corpAccName;
    }

    public void setCorpAccName(String corpAccName) {
        this.corpAccName = corpAccName;
    }

    @Column(name ="REMIT_TYPE",nullable=true,length=1)
    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    @Column(name ="REMIT_METHOD",nullable=true,length=1)
    public String getRemitMethod() {
        return remitMethod;
    }

    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }

    @Column(name ="CORP_NAME_EN",nullable=true,length=1)
    public String getCorpNameEn() {
        return corpNameEn;
    }

    public void setCorpNameEn(String corpNameEn) {
        this.corpNameEn = corpNameEn;
    }

    @Column(name ="CORP_ADDR_EN",nullable=true,length=1)
    public String getCorpAddrEn() {
        return corpAddrEn;
    }

    public void setCorpAddrEn(String corpAddrEn) {
        this.corpAddrEn = corpAddrEn;
    }

    @Column(name ="CORP_ADDR")
    public String getCorpAddr() {
        return corpAddr;
    }

    public void setCorpAddr(String corpAddr) {
        this.corpAddr = corpAddr;
    }

    @Column(name ="CORP_CONTACT",nullable=true,length=1)
    public String getCorpContact() {
        return corpContact;
    }

    public void setCorpContact(String corpContact) {
        this.corpContact = corpContact;
    }

    @Column(name ="CORP_CONTACT_TEL",nullable=true,length=1)
    public String getCorpContactTel() {
        return corpContactTel;
    }

    public void setCorpContactTel(String corpContactTel) {
        this.corpContactTel = corpContactTel;
    }

    @Column(name ="COST_SIGN",nullable=true,length=1)
    public String getCostSign() {
        return costSign;
    }

    public void setCostSign(String costSign) {
        this.costSign = costSign;
    }

    @Column(name ="COST_ACC_ID",nullable=true,length=32)
    public String getCostAccId() {
        return costAccId;
    }

    public void setCostAccId(String costAccId) {
        this.costAccId = costAccId;
    }

    @Column(name ="COST_ACC",nullable=true,length=50)
    public String getCostAcc() {
        return costAcc;
    }

    public void setCostAcc(String costAcc) {
        this.costAcc = costAcc;
    }

    @Column(name ="COST_ACC_ID2",nullable=true,length=32)
    public String getCostAccId2() {
        return costAccId2;
    }

    public void setCostAccId2(String costAccId2) {
        this.costAccId2 = costAccId2;
    }

    @Column(name ="COST_ACC2",nullable=true,length=50)
    public String getCostAcc2() {
        return costAcc2;
    }

    public void setCostAcc2(String costAcc2) {
        this.costAcc2 = costAcc2;
    }

    @Column(name ="PAY_TYPE",nullable=true,length=1)
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

    @Column(name ="IS_BONDED",nullable=true,length=1)
    public String getIsBonded() {
        return isBonded;
    }

    public void setIsBonded(String isBonded) {
        this.isBonded = isBonded;
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

    @Column(name ="REMIT_NATURE",nullable=true,length=10)
    public String getRemitNature() {
        return remitNature;
    }

    public void setRemitNature(String remitNature) {
        this.remitNature = remitNature;
    }

    @Column(name ="STATUS",nullable=true,length=10)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="CORP_ID",nullable=true,length=32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="CAPITAL_USE",nullable=true,length=100)
    public String getCapitalUse() {
        return capitalUse;
    }

    public void setCapitalUse(String capitalUse) {
        this.capitalUse = capitalUse;
    }

    @Column(name ="SRC_BANK",nullable=true,length=150)
    public String getSrcBank() {
        return srcBank;
    }

    public void setSrcBank(String srcBank) {
        this.srcBank = srcBank;
    }

    @Column(name ="SRC_ACC_ID",nullable=true,length=32)
    public String getSrcAccId() {
        return srcAccId;
    }

    public void setSrcAccId(String srcAccId) {
        this.srcAccId = srcAccId;
    }

    @Column(name ="SRC_BANK_ACC",nullable=true,length=50)
    public String getSrcBankAcc() {
        return srcBankAcc;
    }

    public void setSrcBankAcc(String srcBankAcc) {
        this.srcBankAcc = srcBankAcc;
    }

    @Column(name ="SRC_ACC_NAME",nullable=true,length=150)
    public String getSrcAccName() {
        return srcAccName;
    }

    public void setSrcAccName(String srcAccName) {
        this.srcAccName = srcAccName;
    }

    @Column(name ="TARGET_BANK",nullable=true,length=150)
    public String getTargetBank() {
        return targetBank;
    }

    public void setTargetBank(String targetBank) {
        this.targetBank = targetBank;
    }

    @Column(name ="TARGET_ACC_ID",nullable=true,length=32)
    public String getTargetAccId() {
        return targetAccId;
    }

    public void setTargetAccId(String targetAccId) {
        this.targetAccId = targetAccId;
    }

    @Column(name ="TARGET_BANK_ACC",nullable=true,length=50)
    public String getTargetBankAcc() {
        return targetBankAcc;
    }

    public void setTargetBankAcc(String targetBankAcc) {
        this.targetBankAcc = targetBankAcc;
    }

    @Column(name ="TARGET_ACC_NAME",nullable=true,length=150)
    public String getTargetAccName() {
        return targetAccName;
    }

    public void setTargetAccName(String targetAccName) {
        this.targetAccName = targetAccName;
    }

    @Column(name ="TARGET_ACC_CUR_ID",nullable=true,length=150)
    public String getTargetAccCurId() {
        return targetAccCurId;
    }

    public void setTargetAccCurId(String targetAccCurId) {
        this.targetAccCurId = targetAccCurId;
    }

    @Column(name ="TARGET_CORP",nullable=true,length=50)
    public String getTargetCorp() {
        return targetCorp;
    }

    public void setTargetCorp(String targetCorp) {
        this.targetCorp = targetCorp;
    }

    @Column(name ="AGENT_BANK",nullable=true,length=150)
    public String getAgentBank() {
        return agentBank;
    }

    public void setAgentBank(String agentBank) {
        this.agentBank = agentBank;
    }

    @Column(name ="AGENT_BANK_ACC",nullable=true,length=50)
    public String getAgentBankAcc() {
        return agentBankAcc;
    }

    public void setAgentBankAcc(String agentBankAcc) {
        this.agentBankAcc = agentBankAcc;
    }

    @Column(name ="COUNTRY_ID",nullable=true,length=32)
    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Column(name ="COTP_NO",nullable=true,length=32)
    public String getCotpNo() {
        return cotpNo;
    }

    public void setCotpNo(String cotpNo) {
        this.cotpNo = cotpNo;
    }

    @Column(name ="TRAN_ADDR",nullable=true,length=32)
    public String getTranAddr() {
        return tranAddr;
    }

    public void setTranAddr(String tranAddr) {
        this.tranAddr = tranAddr;
    }

    @Column(name ="TRAN_GROUP",nullable=true,length=32)
    public String getTranGroup() {
        return tranGroup;
    }

    public void setTranGroup(String tranGroup) {
        this.tranGroup = tranGroup;
    }

    @Column(name ="SWAP_TYPE",nullable=true,length=32)
    public String getSwapType() {
        return swapType;
    }

    public void setSwapType(String swapType) {
        this.swapType = swapType;
    }

    @Column(name ="PARTNER_ID",nullable=true,length=32)
    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Column(name ="TRAN_ATTR",nullable=true,length=1)
    public String getTranAttr() {
        return tranAttr;
    }

    public void setTranAttr(String tranAttr) {
        this.tranAttr = tranAttr;
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

    @Column(name ="SETTLE_BUS_TYPE",nullable=true,length=10)
    public String getSettleBusType() {
        return settleBusType;
    }

    public void setSettleBusType(String settleBusType) {
        this.settleBusType = settleBusType;
    }

    @Column(name = "AUTO_CAL")
    public String getAutoCal() {
        return autoCal;
    }

    public void setAutoCal(String autoCal) {
        this.autoCal = autoCal;
    }

    @Column(name ="FIXED_MONEY")
    public String getFixedMoney() {
        return fixedMoney;
    }

    public void setFixedMoney(String fixedMoney) {
        this.fixedMoney = fixedMoney;
    }

    @Column(name = "VOLATILITY")
    public BigDecimal getVolatility() {
        return volatility;
    }

    public void setVolatility(BigDecimal volatility) {
        this.volatility = volatility;
    }

    @Column(name = "BUS_DIRECTION")
    public String getBusDirection() {
        return busDirection;
    }

    public void setBusDirection(String busDirection) {
        this.busDirection = busDirection;
    }

    @Column(name = "UPDOWN_DIRECTION")
    public String getUpdownDirection() {
        return updownDirection;
    }

    public void setUpdownDirection(String updownDirection) {
        this.updownDirection = updownDirection;
    }

    @Column(name = "EXPIRES_DATE")
    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    @Column(name = "AVG_BEGIN_DATE")
    public Date getAvgBeginDate() {
        return avgBeginDate;
    }

    public void setAvgBeginDate(Date avgBeginDate) {
        this.avgBeginDate = avgBeginDate;
    }

    @Column(name = "AVG_END_DATE")
    public Date getAvgEndDate() {
        return avgEndDate;
    }

    public void setAvgEndDate(Date avgEndDate) {
        this.avgEndDate = avgEndDate;
    }

    @Column(name = "AVG_DAYS")
    public BigDecimal getAvgDays() {
        return avgDays;
    }

    public void setAvgDays(BigDecimal avgDays) {
        this.avgDays = avgDays;
    }

    @Column(name = "VALID_STANDARD")
    public String getValidStandard() {
        return validStandard;
    }

    public void setValidStandard(String validStandard) {
        this.validStandard = validStandard;
    }

    @Column(name = "EXCEPT_VAL")
    public BigDecimal getExceptVal() {
        return exceptVal;
    }

    public void setExceptVal(BigDecimal exceptVal) {
        this.exceptVal = exceptVal;
    }

    @Column(name = "EXCEPT_LOW_VAL")
    public BigDecimal getExceptLowVal() {
        return exceptLowVal;
    }

    public void setExceptLowVal(BigDecimal exceptLowVal) {
        this.exceptLowVal = exceptLowVal;
    }

    @Column(name = "EXCEPT_HIGN_VAL")
    public BigDecimal getExceptHignVal() {
        return exceptHignVal;
    }

    public void setExceptHignVal(BigDecimal exceptHignVal) {
        this.exceptHignVal = exceptHignVal;
    }

    @Column(name = "TRAN_SETTLE_METHOD")
    public String getTranSettleMethod() {
        return tranSettleMethod;
    }

    public void setTranSettleMethod(String tranSettleMethod) {
        this.tranSettleMethod = tranSettleMethod;
    }

    @Column(name = "TRAN_ADDR_ID")
    public String getTranAddrId() {
        return tranAddrId;
    }

    public void setTranAddrId(String tranAddrId) {
        this.tranAddrId = tranAddrId;
    }

    @Column(name = "PREMIUM_DATE")
    public Date getPremiumDate() {
        return premiumDate;
    }

    public void setPremiumDate(Date premiumDate) {
        this.premiumDate = premiumDate;
    }

    @Column(name = "PREMIUM_CUR")
    public String getPremiumCur() {
        return premiumCur;
    }

    public void setPremiumCur(String premiumCur) {
        this.premiumCur = premiumCur;
    }

    @Column(name = "PREMIUM_POINT")
    public BigDecimal getPremiumPoint() {
        return premiumPoint;
    }

    public void setPremiumPoint(BigDecimal premiumPoint) {
        this.premiumPoint = premiumPoint;
    }

    @Column(name = "PREMIUM_RATIO")
    public BigDecimal getPremiumRatio() {
        return premiumRatio;
    }

    public void setPremiumRatio(BigDecimal premiumRatio) {
        this.premiumRatio = premiumRatio;
    }

    @Column(name = "PREMIUM_RATE")
    public BigDecimal getPremiumRate() {
        return premiumRate;
    }

    public void setPremiumRate(BigDecimal premiumRate) {
        this.premiumRate = premiumRate;
    }

    @Column(name = "PREMIUM_MONEY")
    public BigDecimal getPremiumMoney() {
        return premiumMoney;
    }

    public void setPremiumMoney(BigDecimal premiumMoney) {
        this.premiumMoney = premiumMoney;
    }

    @Column(name = "IS_REFUND")
    public String getIsRefund() {
        return isRefund;
    }

    public void setIsRefund(String isRefund) {
        this.isRefund = isRefund;
    }

    @Column(name = "REFUND_POINT")
    public BigDecimal getRefundPoint() {
        return refundPoint;
    }

    public void setRefundPoint(BigDecimal refundPoint) {
        this.refundPoint = refundPoint;
    }

    @Column(name = "REFUND_RATIO")
    public BigDecimal getRefundRatio() {
        return refundRatio;
    }

    public void setRefundRatio(BigDecimal refundRatio) {
        this.refundRatio = refundRatio;
    }

    @Column(name = "REFUND_MONEY")
    public BigDecimal getRefundMoney() {
        return refundMoney;
    }

    public void setRefundMoney(BigDecimal refundMoney) {
        this.refundMoney = refundMoney;
    }

    @Column(name = "REFUND_TIME")
    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
    }

    @Column(name = "COTP_AGENT")
    public String getCotpAgent() {
        return cotpAgent;
    }

    public void setCotpAgent(String cotpAgent) {
        this.cotpAgent = cotpAgent;
    }

    @Column(name = "COTP_CLEAR_HOUSE")
    public String getCotpClearHouse() {
        return cotpClearHouse;
    }

    public void setCotpClearHouse(String cotpClearHouse) {
        this.cotpClearHouse = cotpClearHouse;
    }

    @Column(name = "DIF_BANK")
    public String getDifBank() {
        return difBank;
    }

    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }

    @Column(name = "IS_PAYED")
    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    @Column(name ="BURSAR_BILL_NO")
    public String getBursarBillNo() {
        return bursarBillNo;
    }

    public void setBursarBillNo(String bursarBillNo) {
        this.bursarBillNo = bursarBillNo;
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

    @Column(name ="REL_BILL_NO",nullable=true,length=4000)
    public String getRelBillNo() {
        return relBillNo;
    }

    public void setRelBillNo(String relBillNo) {
        this.relBillNo = relBillNo;
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

    @Column(name ="ADD_WORD")
    public String getAddWord() {
        return addWord;
    }

    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }

    @Column(name ="COST_CUR_ID")
    public String getCostCurId() {
        return costCurId;
    }

    public void setCostCurId(String costCurId) {
        this.costCurId = costCurId;
    }

    @Column(name ="COST_MONEY")
    public BigDecimal getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(BigDecimal costMoney) {
        this.costMoney = costMoney;
    }

    @Column(name ="BANK_PAY_TYPE")
    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    @Column(name ="ORIGIN_COUNTRY")
    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    @Column(name ="CASH_ITEM",nullable=true)
    public String getCashItem() {
        return cashItem;
    }

    public void setCashItem(String cashItem) {
        this.cashItem = cashItem;
    }

    @Column(name ="INVOICE_DATE")
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    @Column(name ="INVOICE_TOTAL")
    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(BigDecimal invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    @Column(name ="DEDUCTION_AMOUNT")
    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    @Column(name ="INVOICE_AMOUNT")
    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    @Column(name ="SAVE_FIELDS")
    public String getSaveFields() {
        return saveFields;
    }

    public void setSaveFields(String saveFields) {
        this.saveFields = saveFields;
    }

    @Column(name ="FPS_ID")
    public String getFpsId() {
        return fpsId;
    }

    public void setFpsId(String fpsId) {
        this.fpsId = fpsId;
    }

    @Column(name ="E_MAIL")
    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Column(name ="ISD_CODE")
    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    @Column(name ="PHONE_CODE")
    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    @Column(name ="AIIN")
    public String getAiIn() {
        return aiIn;
    }

    public void setAiIn(String aiIn) {
        this.aiIn = aiIn;
    }


    @Column(name ="HKID")
    public String getHkId() {
        return hkId;
    }

    public void setHkId(String hkId) {
        this.hkId = hkId;
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

    @Column(name ="BILL_FROM")
    public String getBillFrom() {
        return billFrom;
    }

    public void setBillFrom(String billFrom) {
        this.billFrom = billFrom;
    }

    @Column(name ="TIME_STAMP")
    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Column(name ="TEMPLATE_ID")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Column(name ="VERSION_NO")
    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }



    @Column(name ="TEMPLATE_NAME ")
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


    @Column(name = "BANK_PAY_TYPE_NAME")
    public String getBankPayTypeName() {
        return bankPayTypeName;
    }

    public void setBankPayTypeName(String bankPayTypeName) {
        this.bankPayTypeName = bankPayTypeName;
    }


    @Column(name = "BANK_PAY_TYPE_ID")
    public String getBankPayTypeId() {
        return bankPayTypeId;
    }

    public void setBankPayTypeId(String bankPayTypeId) {
        this.bankPayTypeId = bankPayTypeId;
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

    @Column(name ="RPT_GATE_ENT_ID")
    public String getRptGateEntId() {
        return rptGateEntId;
    }

    public void setRptGateEntId(String rptGateEntId) {
        this.rptGateEntId = rptGateEntId;
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
    public BigDecimal getAdvanceRate() {
        return advanceRate;
    }

    public void setAdvanceRate(BigDecimal advanceRate) {
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
