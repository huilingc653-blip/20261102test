package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "FEX_TRAN_APP_DTL", schema = "")
public class FexTranAppDtl extends IdEntity implements Serializable {

    /**
     * 主表ID
     */
    private String appId;

    /**
     * 交易类型
     */
    private String tranType;

    /**
     * 是否掉期交易
     */
    private String isSwaptTran;

    /**
     * 产品ID
     */
    private String proId;

    /**
     * 固定金额
     */
    private String fixedMoney;

    /**
     * 外汇业务类型
     */
    private String fexBusType;

    /**
     * 外汇交易类型
     */
    private String fexTranType;

    /**
     * 交割类型
     */
    private String deliveryType;

    /**
     * 交割币种
     */
    private String deliveryCurId;

    /**
     * 买入币种
     */
    private String buyCurId;

    /**
     * 买入金额
     */
    private BigDecimal buyMoney;

    /**
     * 卖出币种
     */
    private String sellCurId;

    /**
     * 卖出金额
     */
    private BigDecimal sellMoney;

    /**
     *期望汇率
     */
    private BigDecimal wishRate;

    /**
     *期望汇率
     */
    private BigDecimal endWishRate;

    /**
     *钞汇标识
     */
    private String bankNote;

    /**
     *交易日期
     */
    private Date tranDate;

    /**
     *结算日期
     */
    private Date settleDate;

    /**
     *交割期限
     */
    private String deliveryDeadline;

    /**
     *保证方式
     */
    private String boodType;

    /**
     *保证金账号
     */
    private String boodAcc;

    /**
     *保证金账号ID
     */
    private String boodAccId;

    /**
     *保证金比例
     */
    private BigDecimal boodRatio;

    /**
     *保证金钞汇标识
     */
    private String boodBankNote;

    /**
     *保证金币种
     */
    private String bondCurId;

    /**
     *合同号
     */
    private String contractNo;

    /**
     *额度
     */
//    private BigDecimal ratMoney;

    /**
     *外汇局批件/备案表号/业务编号
     */
    private String busNo;

    /**
     *结汇资金用途
     */
    private String purpose;

    /**
     *结汇资金详细用途
     */
    private String purposeDtl;

    /**
     *资金来源
     */
    private String fundsSource;

    /**
     *发票号
     */
    private String invoiceNo;

    /**
     *账户资金性质
     */
    private String accNature;

    /**
     *结售汇属性
     */
    private String fexProp;

    /**
     *其它保证方式说明
     */
    private String boodRmk;

    /**
     *备注
     */
    private String rmk;

    /**
     *附件
     */
    private String attachmentId;

    /**
     *交易对手
     */
    private String cotpId;

    /**
     *币种
     */
    private String curId;

    /**
     *收付方向
     */
    private String cdSign;

    /**
     *被代付/代付单位
     */
    private String payCorpId;

    /**
     *金额/额度
     */
    private BigDecimal payMoney;

    /**
     *加急标志
     */
    private String isQuick;

    /**
     *主体单位银行账户ID
     */
    private String corpAccId;

    /**
     *主体单位银行
     */
    private String corpBank;

    /**
     *主体单位银行账户
     */
    private String corpBankAcc;

    /**
     *汇款方式
     */
    private String remitMethod;

    /**
     *主体单位名称（英文）
     */
    private String corpNameEn;

    /**
     *主体单位地址（英文）
     */
    private String corpAddEn;

    /**
     *主体单位地址（中文）
     */
    private String corpAddr;

    /**
     *主体单位联系人
     */
    private String corpContact;

    /**
     *主体单位联系电话
     */
    private String corpContactTel;

    /**
     * 交易对手银行
     */
    private String cotpBank;

    /**
     * 交易对手银行账户
     */
    private String cotpBankAcc;

    /**
     * 交易对手银行账户Id
     */
    private String cotpAccId;

    /**
     * 交易对手银行账户币种Id
     */
    private String cotpAccCurId;

    /**
     *国内外费用承担标志
     */
    private String costSign;

    /**
     *费用支出账户
     */
    private String costAcc;

    /**
     *付款类型
     */
    private String payType;

    /**
     *最迟装运日期
     */
    private Date shipDate;

    /**
     *是否为保税货物项下付款
     */
    private String isBonded;

    /**
     *交易属性
     */
    private String tranAttr;

    /**
     *交易编码1
     */
    private String tranCode1;

    /**
     *相应金额1
     */
    private BigDecimal tranAmt1;

    /**
     *相应币种1
     */
    private String tranCurId1;

    /**
     *交易附言1
     */
    private String tranAddWord1;

    /**
     *交易编码2
     */
    private String tranCode2;

    /**
     *相应金额2
     */
    private BigDecimal tranAmt2;

    /**
     *相应币种2
     */
    private String tranCurId2;

    /**
     *交易附言2
     */
    private String tranAddWord2;

    /**
     * 近/远端  1-近端，2-远端
     */
    private String swapType;

    /**
     * 近/远端 id
     */
    private String partnerId;

    /**
     * 自动计算买入金额
     */
    private String autoCal;

    /**
     * 汇款类型
     */
    private String remitType;

    /**
     * 结汇业务类型
     */
    private String settleBusType;

    /**
     * 单位id
     */
    private String corpId;

    /**
     * 买卖方向 1-买入，2-卖出
     */
    private String busDirection;

    /**
     *涨跌方向 0-看涨，1-看跌
     */
    private String updownDirection;

    /**
     *平均期开始日期
     */
    private Date avgBeginDate;

    /**
     *平均期结束日期
     */
    private Date avgEndDate;

    /**
     *平均期天数
     */
    private Integer avgDays;

    /**
     *交易时障碍验证标准 0-即期汇率、1-即期汇率对应的货币价值
     */
    private String validStandard;

    /**
     *期望低障碍值
     */
    private BigDecimal exceptLowVal;

    /**
     *期望高障碍值
     */
    private BigDecimal exceptHignVal;

    /**
     *到期日期
     */
    private Date expiresDate;

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

    /**交易对手国家地区*/
    private String cotpCountry;
    /**交易对手地点*/
    private String cotpAddress;

    /**收款人类型：0-对公居民、1-对公非居民、2-对私居民、3-对私非居民*/
    private String payeeType;

    /**主体单位账号币种*/
    private String corpAccCurId;
    /**是否线下付款*/
    private String isPayed;

    /**
     * 交易货币对
     */
    private String tranCurId;

    /**
     * 报价方式
     */
    private String quotationMethod;

    /**
     * 金额是否自动计算
     */
    private String isPaymentAmount;

    /**
     * 期权费点数最小值
     */
    private BigDecimal premiumPointMin;

    /**
     * 期权费点数最大值
     */
    private BigDecimal premiumPointMax;

    /**
     * 期权费金额最小值
     */
    private BigDecimal premiumMoneyMin;

    /**
     * 期权费金额最大值
     */
    private BigDecimal premiumMoneyMax;

    /**
     * 掉期点最大值
     */
    private BigDecimal swapPointMin;

    /**
     * 掉期点最小值
     */
    private BigDecimal swapPointMax;

    /**
     * 远端期望汇率
     */
    private BigDecimal remoteEndWishRateMin;

    /**
     * 远端期望汇率
     */
    private BigDecimal remoteEndWishRateMax;

    /**
     * 远端结算日期
     */
    private Date endSettleDate;

    /**
     * 远端交易类型
     */
    private String fexTranTypeRemoteEnd;

    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;

    /**
     * 支付类型
     */
    private String bankPayTypeId;
    private String bankPayType;
    private String bankPayTypeName;


    /**
     * 模板ID
     */
    private String templateId;
    private String templateName;


    @Column(name = "BANK_PAY_TYPE_NAME")
    public String getBankPayTypeName() {
        return bankPayTypeName;
    }

    public void setBankPayTypeName(String bankPayTypeName) {
        this.bankPayTypeName = bankPayTypeName;
    }

    @Column(name = "BANK_PAY_TYPE")
    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="FEX_TRAN_TYPE_REMOTE_END")
    public String getFexTranTypeRemoteEnd() {
        return fexTranTypeRemoteEnd;
    }

    public void setFexTranTypeRemoteEnd(String fexTranTypeRemoteEnd) {
        this.fexTranTypeRemoteEnd = fexTranTypeRemoteEnd;
    }

    @Column(name ="END_SETTLE_DATE")
    public Date getEndSettleDate() {
        return endSettleDate;
    }

    public void setEndSettleDate(Date endSettleDate) {
        this.endSettleDate = endSettleDate;
    }

    @Column(name ="SWAP_POINT_MIN")
    public BigDecimal getSwapPointMin() {
        return swapPointMin;
    }

    public void setSwapPointMin(BigDecimal swapPointMin) {
        this.swapPointMin = swapPointMin;
    }

    @Column(name ="SWAP_POINT_MAX")
    public BigDecimal getSwapPointMax() {
        return swapPointMax;
    }

    public void setSwapPointMax(BigDecimal swapPointMax) {
        this.swapPointMax = swapPointMax;
    }

    @Column(name ="REMOTE_END_WISH_RATE_MIN")
    public BigDecimal getRemoteEndWishRateMin() {
        return remoteEndWishRateMin;
    }

    public void setRemoteEndWishRateMin(BigDecimal remoteEndWishRateMin) {
        this.remoteEndWishRateMin = remoteEndWishRateMin;
    }

    @Column(name ="REMOTE_END_WISH_RATE_MAX")
    public BigDecimal getRemoteEndWishRateMax() {
        return remoteEndWishRateMax;
    }

    public void setRemoteEndWishRateMax(BigDecimal remoteEndWishRateMax) {
        this.remoteEndWishRateMax = remoteEndWishRateMax;
    }

    @Column(name ="TRAN_CUR_ID")
    public String getTranCurId() {
        return tranCurId;
    }

    public void setTranCurId(String tranCurId) {
        this.tranCurId = tranCurId;
    }

    @Column(name ="QUOTATION_METHOD")
    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    @Column(name ="IS_PAYMENT_AMOUNT")
    public String getIsPaymentAmount() {
        return isPaymentAmount;
    }

    public void setIsPaymentAmount(String isPaymentAmount) {
        this.isPaymentAmount = isPaymentAmount;
    }

    @Column(name ="PREMIUM_POINT_MIN")
    public BigDecimal getPremiumPointMin() {
        return premiumPointMin;
    }

    public void setPremiumPointMin(BigDecimal premiumPointMin) {
        this.premiumPointMin = premiumPointMin;
    }

    @Column(name ="PREMIUM_POINT_MAX")
    public BigDecimal getPremiumPointMax() {
        return premiumPointMax;
    }

    public void setPremiumPointMax(BigDecimal premiumPointMax) {
        this.premiumPointMax = premiumPointMax;
    }

    @Column(name ="PREMIUM_MONEY_MIN")
    public BigDecimal getPremiumMoneyMin() {
        return premiumMoneyMin;
    }

    public void setPremiumMoneyMin(BigDecimal premiumMoneyMin) {
        this.premiumMoneyMin = premiumMoneyMin;
    }

    @Column(name ="PREMIUM_MONEY_MAX")
    public BigDecimal getPremiumMoneyMax() {
        return premiumMoneyMax;
    }

    public void setPremiumMoneyMax(BigDecimal premiumMoneyMax) {
        this.premiumMoneyMax = premiumMoneyMax;
    }

    @Column(name ="END_WISH_RATE")
    public BigDecimal getEndWishRate() {
        return endWishRate;
    }

    public void setEndWishRate(BigDecimal endWishRate) {
        this.endWishRate = endWishRate;
    }

    @Column(name ="APP_ID")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name ="TRAN_TYPE")
    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    @Column(name ="IS_SWAPT_TRAN")
    public String getIsSwaptTran() {
        return isSwaptTran;
    }

    public void setIsSwaptTran(String isSwaptTran) {
        this.isSwaptTran = isSwaptTran;
    }

    @Column(name ="PRO_ID")
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    @Column(name ="FIXED_MONEY")
    public String getFixedMoney() {
        return fixedMoney;
    }

    public void setFixedMoney(String fixedMoney) {
        this.fixedMoney = fixedMoney;
    }

    @Column(name ="FEX_BUS_TYPE")
    public String getFexBusType() {
        return fexBusType;
    }

    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
    }

    @Column(name ="FEX_TRAN_TYPE")
    public String getFexTranType() {
        return fexTranType;
    }

    public void setFexTranType(String fexTranType) {
        this.fexTranType = fexTranType;
    }

    @Column(name ="DELIVERY_TYPE")
    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    @Column(name ="DELIVERY_CUR_ID")
    public String getDeliveryCurId() {
        return deliveryCurId;
    }

    public void setDeliveryCurId(String deliveryCurId) {
        this.deliveryCurId = deliveryCurId;
    }

    @Column(name ="BUY_CUR_ID")
    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    @Column(name ="BUY_MONEY")
    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    @Column(name ="SELL_CUR_ID")
    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    @Column(name ="SELL_MONEY")
    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    @Column(name ="WISH_RATE")
    public BigDecimal getWishRate() {
        return wishRate;
    }

    public void setWishRate(BigDecimal wishRate) {
        this.wishRate = wishRate;
    }

    @Column(name ="BANK_NOTE")
    public String getBankNote() {
        return bankNote;
    }

    public void setBankNote(String bankNote) {
        this.bankNote = bankNote;
    }

    @Column(name ="TRAN_DATE")
    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    @Column(name ="SETTLE_DATE")
    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    @Column(name ="DELIVERY_DEADLINE")
    public String getDeliveryDeadline() {
        return deliveryDeadline;
    }

    public void setDeliveryDeadline(String deliveryDeadline) {
        this.deliveryDeadline = deliveryDeadline;
    }

    @Column(name ="BOOD_TYPE")
    public String getBoodType() {
        return boodType;
    }

    public void setBoodType(String boodType) {
        this.boodType = boodType;
    }

    @Column(name ="BOOD_ACC")
    public String getBoodAcc() {
        return boodAcc;
    }

    public void setBoodAcc(String boodAcc) {
        this.boodAcc = boodAcc;
    }

    @Column(name ="BOOD_RATIO")
    public BigDecimal getBoodRatio() {
        return boodRatio;
    }

    public void setBoodRatio(BigDecimal boodRatio) {
        this.boodRatio = boodRatio;
    }

    @Column(name ="BOOD_BANK_NOTE")
    public String getBoodBankNote() {
        return boodBankNote;
    }

    public void setBoodBankNote(String boodBankNote) {
        this.boodBankNote = boodBankNote;
    }

    @Column(name ="BOND_CUR_ID")
    public String getBondCurId() {
        return bondCurId;
    }

    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }

    @Column(name ="CONTRACT_NO")
    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

/*    @Column(name ="RAT_MONEY")
    public BigDecimal getRatMoney() {
        return ratMoney;
    }

    public void setRatMoney(BigDecimal ratMoney) {
        this.ratMoney = ratMoney;
    }*/

    @Column(name ="BUS_NO")
    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    @Column(name ="PURPOSE")
    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    @Column(name ="PURPOSE_DTL")
    public String getPurposeDtl() {
        return purposeDtl;
    }

    public void setPurposeDtl(String purposeDtl) {
        this.purposeDtl = purposeDtl;
    }

    @Column(name ="FUNDS_SOURCE")
    public String getFundsSource() {
        return fundsSource;
    }

    public void setFundsSource(String fundsSource) {
        this.fundsSource = fundsSource;
    }

    @Column(name ="INVOICE_NO")
    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    @Column(name ="ACC_NATURE")
    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    @Column(name ="FEX_PROP")
    public String getFexProp() {
        return fexProp;
    }

    public void setFexProp(String fexProp) {
        this.fexProp = fexProp;
    }

    @Column(name ="BOOD_RMK")
    public String getBoodRmk() {
        return boodRmk;
    }

    public void setBoodRmk(String boodRmk) {
        this.boodRmk = boodRmk;
    }

    @Column(name ="RMK")
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name ="COTP_ID")
    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="CD_SIGN")
    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    @Column(name ="PAY_CORP_ID")
    public String getPayCorpId() {
        return payCorpId;
    }

    public void setPayCorpId(String payCorpId) {
        this.payCorpId = payCorpId;
    }

    @Column(name ="PAY_MONEY")
    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    @Column(name ="IS_QUICK")
    public String getIsQuick() {
        return isQuick;
    }

    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
    }

    @Column(name ="CORP_ACC_ID")
    public String getCorpAccId() {
        return corpAccId;
    }

    public void setCorpAccId(String corpAccId) {
        this.corpAccId = corpAccId;
    }

    @Column(name ="CORP_BANK")
    public String getCorpBank() {
        return corpBank;
    }

    public void setCorpBank(String corpBank) {
        this.corpBank = corpBank;
    }

    @Column(name ="CORP_BANK_ACC")
    public String getCorpBankAcc() {
        return corpBankAcc;
    }

    public void setCorpBankAcc(String corpBankAcc) {
        this.corpBankAcc = corpBankAcc;
    }

    @Column(name ="REMIT_METHOD")
    public String getRemitMethod() {
        return remitMethod;
    }

    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }

    @Column(name ="CORP_NAME_EN")
    public String getCorpNameEn() {
        return corpNameEn;
    }

    public void setCorpNameEn(String corpNameEn) {
        this.corpNameEn = corpNameEn;
    }

    @Column(name ="CORP_ADD_EN")
    public String getCorpAddEn() {
        return corpAddEn;
    }

    public void setCorpAddEn(String corpAddEn) {
        this.corpAddEn = corpAddEn;
    }

    @Column(name ="CORP_ADDR")
    public String getCorpAddr() {
        return corpAddr;
    }

    public void setCorpAddr(String corpAddr) {
        this.corpAddr = corpAddr;
    }

    @Column(name ="CORP_CONTACT")
    public String getCorpContact() {
        return corpContact;
    }

    public void setCorpContact(String corpContact) {
        this.corpContact = corpContact;
    }

    @Column(name ="CORP_CONTACT_TEL")
    public String getCorpContactTel() {
        return corpContactTel;
    }

    public void setCorpContactTel(String corpContactTel) {
        this.corpContactTel = corpContactTel;
    }



    @Column(name ="COST_SIGN")
    public String getCostSign() {
        return costSign;
    }

    public void setCostSign(String costSign) {
        this.costSign = costSign;
    }

    @Column(name ="COST_ACC")
    public String getCostAcc() {
        return costAcc;
    }

    public void setCostAcc(String costAcc) {
        this.costAcc = costAcc;
    }

    @Column(name ="PAY_TYPE")
    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name ="SHIP_DATE")
    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    @Column(name ="IS_BONDED")
    public String getIsBonded() {
        return isBonded;
    }

    public void setIsBonded(String isBonded) {
        this.isBonded = isBonded;
    }

    @Column(name ="TRAN_ATTR")
    public String getTranAttr() {
        return tranAttr;
    }

    public void setTranAttr(String tranAttr) {
        this.tranAttr = tranAttr;
    }

    @Column(name ="TRAN_CODE1")
    public String getTranCode1() {
        return tranCode1;
    }

    public void setTranCode1(String tranCode1) {
        this.tranCode1 = tranCode1;
    }

    @Column(name ="TRAN_AMT1")
    public BigDecimal getTranAmt1() {
        return tranAmt1;
    }

    public void setTranAmt1(BigDecimal tranAmt1) {
        this.tranAmt1 = tranAmt1;
    }

    @Column(name ="TRAN_CUR_ID1")
    public String getTranCurId1() {
        return tranCurId1;
    }

    public void setTranCurId1(String tranCurId1) {
        this.tranCurId1 = tranCurId1;
    }

    @Column(name ="TRAN_ADD_WORD1")
    public String getTranAddWord1() {
        return tranAddWord1;
    }

    public void setTranAddWord1(String tranAddWord1) {
        this.tranAddWord1 = tranAddWord1;
    }

    @Column(name ="TRAN_CODE2")
    public String getTranCode2() {
        return tranCode2;
    }

    public void setTranCode2(String tranCode2) {
        this.tranCode2 = tranCode2;
    }

    @Column(name ="TRAN_AMT2")
    public BigDecimal getTranAmt2() {
        return tranAmt2;
    }

    public void setTranAmt2(BigDecimal tranAmt2) {
        this.tranAmt2 = tranAmt2;
    }

    @Column(name ="TRAN_CUR_ID2")
    public String getTranCurId2() {
        return tranCurId2;
    }

    public void setTranCurId2(String tranCurId2) {
        this.tranCurId2 = tranCurId2;
    }

    @Column(name ="TRAN_ADD_WORD2")
    public String getTranAddWord2() {
        return tranAddWord2;
    }

    public void setTranAddWord2(String tranAddWord2) {
        this.tranAddWord2 = tranAddWord2;
    }

    @Column(name = "COTP_BANK")
    public String getCotpBank() {
        return cotpBank;
    }

    public void setCotpBank(String cotpBank) {
        this.cotpBank = cotpBank;
    }

    @Column(name = "BOOD_ACC_ID")
    public String getBoodAccId() {
        return boodAccId;
    }

    public void setBoodAccId(String boodAccId) {
        this.boodAccId = boodAccId;
    }

    @Column(name = "COTP_BANK_ACC")
    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    @Column(name = "COTP_ACC_ID")
    public String getCotpAccId() {
        return cotpAccId;
    }

    public void setCotpAccId(String cotpAccId) {
        this.cotpAccId = cotpAccId;
    }

    @Column(name = "COTP_ACC_CUR_ID")
    public String getCotpAccCurId() {
        return cotpAccCurId;
    }

    public void setCotpAccCurId(String cotpAccCurId) {
        this.cotpAccCurId = cotpAccCurId;
    }

    @Column(name = "SWAP_TYPE")
    public String getSwapType() {
        return swapType;
    }

    public void setSwapType(String swapType) {
        this.swapType = swapType;
    }

    @Column(name = "PARTNER_ID")
    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Column(name = "AUTO_CAL")
    public String getAutoCal() {
        return autoCal;
    }

    public void setAutoCal(String autoCal) {
        this.autoCal = autoCal;
    }

    @Column(name = "REMIT_TYPE")
    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    @Column(name = "SETTLE_BUS_TYPE")
    public String getSettleBusType() {
        return settleBusType;
    }

    public void setSettleBusType(String settleBusType) {
        this.settleBusType = settleBusType;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
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
    public Integer getAvgDays() {
        return avgDays;
    }

    public void setAvgDays(Integer avgDays) {
        this.avgDays = avgDays;
    }

    @Column(name = "VALID_STANDARD")
    public String getValidStandard() {
        return validStandard;
    }

    public void setValidStandard(String validStandard) {
        this.validStandard = validStandard;
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

    @Column(name = "EXPIRES_DATE")
    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
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

    @Column(name ="PAYEE_TYPE")
    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    @Column(name ="CORP_ACC_CUR_ID")
    public String getCorpAccCurId() {
        return corpAccCurId;
    }

    public void setCorpAccCurId(String corpAccCurId) {
        this.corpAccCurId = corpAccCurId;
    }

    @Column(name ="IS_PAYED")
    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }


        @Column(name ="TEMPLATE_ID")
    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Column(name ="TEMPLATE_NAME")
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Column(name ="BANK_PAY_TYPE_ID")
    public String getBankPayTypeId() {
        return bankPayTypeId;
    }

    public void setBankPayTypeId(String bankPayTypeId) {
        this.bankPayTypeId = bankPayTypeId;
    }
}
