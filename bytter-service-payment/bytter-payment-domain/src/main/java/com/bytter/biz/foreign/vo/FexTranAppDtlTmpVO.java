package com.bytter.biz.foreign.vo;

import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Currencys;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class FexTranAppDtlTmpVO implements Serializable {

    /**
     * 主键id
     */
    private String id;

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
    private String proName;

    /**
     * 模板ID
     */
    private String templateId;
    private String templateName;

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
    @Currencys(
            {
                    @Currency(inject="deliveryCurName"),
                    @Currency(inject="deliveryEnglishCode", field = "englishCode")
            }
    )
    private String deliveryCurId;
    private String deliveryCurName;
    private String deliveryEnglishCode;

    /**
     * 买入币种
     */
    @Currencys(
            {
                    @Currency(inject="buyCurName"),
                    @Currency(inject="buyCurEnCode", field = "englishCode")
            }
    )
    private String buyCurId;
    private String buyCurName;
    private String buyCurEnCode;

    /**
     * 买入金额
     */
    private BigDecimal buyMoney;

    /**
     * 卖出币种
     */
    @Currencys(
            {
                    @Currency(inject="sellCurName"),
                    @Currency(inject="sellCurEnCode", field = "englishCode")
            }
    )
    private String sellCurId;
    private String sellCurName;
    private String sellCurEnCode;

    /**
     * 卖出金额
     */
    private BigDecimal sellMoney;

    /**
     * 期望汇率
     */
    private BigDecimal wishRate;


    /**
     * 钞汇标识
     */
    private String bankNote;

    /**
     * 交易日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tranDate;

    /**
     * 结算日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date settleDate;

    /**
     * 远端结算日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endSettleDate;

    /**
     * 交割期限
     */
    private String deliveryDeadline;

    /**
     * 保证方式
     */
    private String boodType;

    /**
     * 保证金账号
     */
    private String boodAcc;

    /**
     * 保证金比例
     */
    private BigDecimal boodRatio;

    /**
     * 保证金钞汇标识
     */
    private String boodBankNote;

    /**
     * 保证金币种
     */
    @Currencys(
            {
                    @Currency(inject="bondCurName"),
                    @Currency(inject="bondCurEnCode", field = "englishCode")
            }
    )
    private String bondCurId;
    private String bondCurName;
    private String bondCurEnCode;

    /**
     * 合同号
     */
    private String contractNo;


    /**
     * 外汇局批件/备案表号/业务编号
     */
    private String busNo;

    /**
     * 结汇资金用途
     */
    private String purpose;

    /**
     * 结汇资金详细用途
     */
    private String purposeDtl;

    /**
     * 资金来源
     */
    private String fundsSource;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 账户资金性质
     */
    private String accNature;

    /**
     * 结售汇属性
     */
    private String fexProp;

    /**
     * 其它保证方式说明
     */
    private String boodRmk;

    /**
     * 备注
     */
    private String rmk;

    /**
     * 附件
     */
    private String attachmentId;

    /**
     * 交易对手
     */
    private String cotpId;
    private String cotpName;

    /**
     * 交易对手原id
     */
    private String oppId;

    /**
     * 交易对手(英文)
     */
    private String cotpNameEn;

    /**
     * 本地清算号
     */
    private String localClearNo;

    /**
     * 币种
     */
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="curEnCode", field = "englishCode")
            }
    )
    private String curId;
    private String curName;
    private String curEnCode;

    /**
     * 收付方向
     */
    private String cdSign;

    /**
     * 被代付/代付单位
     */
    private String payCorpId;

    /**
     * 金额/额度
     */
    private BigDecimal payMoney;

    /**
     * 加急标志
     */
    private String isQuick;




    /**
     * 主体单位银行账户ID
     */
    private String corpAccId;

    /**
     * 主体单位银行
     */
    private String corpBank;

    /**
     * 主体单位银行账户
     */
    private String corpBankAcc;
    private String corpBankAccName;

    /**
     * 汇款类型
     */
    private String remitMethod;

    /**
     * 主体单位名称（英文）
     */
    private String corpNameEn;

    /**
     * 主体单位地址（英文）
     */
    private String corpAddEn;

    /**
     *主体单位地址（中文）
     */
    private String corpAddr;

    /**
     * 主体单位联系人
     */
    private String corpContact;

    /**
     * 主体单位联系电话
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

    @Currencys(
            {
                    @Currency(inject="cotpAccCurName"),
                    @Currency(inject="cotpAccEnglishCode", field = "englishCode")
            }
    )
    private String cotpAccCurId;
    private String cotpAccCurName;
    private String cotpAccEnglishCode;

    /**
     * 国内外费用承担标志
     */
    private String costSign;

    /**
     * 费用支出账户
     */
    private String costAcc;

    /**
     * 付款类型
     */
    private String payType;

    /**
     * 最迟装运日期
     */
    private Date shipDate;

    /**
     * 是否为保税货物项下付款
     */
    private String isBonded;

    /**
     * 交易属性
     */
    private String tranAttr;

    /**
     * 交易编码1
     */
    private String tranCode1;
    private String tranCode1Name;

    /**
     * 相应金额1
     */
    private BigDecimal tranAmt1;

    /**
     * 相应币种1
     */
    @Currencys(
            {
                    @Currency(inject="tranCur1Name"),
                    @Currency(inject="tranCur1EnCode", field = "englishCode")
            }
    )
    private String tranCurId1;
    private String tranCur1Name;
    private String tranCur1EnCode;

    /**
     * 交易附言1
     */
    private String tranAddWord1;

    /**
     * 交易编码2
     */
    private String tranCode2;
    private String tranCode2Name;

    /**
     * 相应金额2
     */
    private BigDecimal tranAmt2;

    /**
     * 相应币种2
     */
    @Currencys(
            {
                    @Currency(inject="tranCur1Name"),
                    @Currency(inject="tranCur1EnCode", field = "englishCode")
            }
    )
    private String tranCurId2;
    private String tranCur2Name;
    private String tranCur3EnCode;

    /**
     * 交易附言2
     */
    private String tranAddWord2;

    /**
     * 交易对手地址
     */
    private String cotpPostAddr;

    /**
     * 交易对手国家地区
     */
    private String countryArea;

    /**
     * 交易对手银行账户
     */
    private FexCotpBankAccVO fexCotpBankAccVO;

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
    @Currencys(
            {
                    @Currency(inject="corpAccCurName"),
                    @Currency(inject="corpAccEnglishCode", field = "englishCode")
            }
    )
    private String corpAccCurId;
    private String corpAccCurName;
    private String corpAccEnglishCode;
    /**是否线下付款*/
    private String isPayed;

    //fps字段存储

    /**
     * 交易对手
     */
    private FexCotpVO fexCotpVO;

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
     * 期望汇率
     */
    private BigDecimal endWishRate;

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

    public String getFexTranTypeRemoteEnd() {
        return fexTranTypeRemoteEnd;
    }

    public void setFexTranTypeRemoteEnd(String fexTranTypeRemoteEnd) {
        this.fexTranTypeRemoteEnd = fexTranTypeRemoteEnd;
    }

    public BigDecimal getRemoteEndWishRateMin() {
        return remoteEndWishRateMin;
    }

    public void setRemoteEndWishRateMin(BigDecimal remoteEndWishRateMin) {
        this.remoteEndWishRateMin = remoteEndWishRateMin;
    }

    public BigDecimal getRemoteEndWishRateMax() {
        return remoteEndWishRateMax;
    }

    public void setRemoteEndWishRateMax(BigDecimal remoteEndWishRateMax) {
        this.remoteEndWishRateMax = remoteEndWishRateMax;
    }

    public BigDecimal getSwapPointMin() {
        return swapPointMin;
    }

    public void setSwapPointMin(BigDecimal swapPointMin) {
        this.swapPointMin = swapPointMin;
    }

    public BigDecimal getSwapPointMax() {
        return swapPointMax;
    }

    public void setSwapPointMax(BigDecimal swapPointMax) {
        this.swapPointMax = swapPointMax;
    }

    public Date getEndSettleDate() {
        return endSettleDate;
    }

    public void setEndSettleDate(Date endSettleDate) {
        this.endSettleDate = endSettleDate;
    }

    public BigDecimal getEndWishRate() {
        return endWishRate;
    }

    public void setEndWishRate(BigDecimal endWishRate) {
        this.endWishRate = endWishRate;
    }

    public String getTranCurId() {
        return tranCurId;
    }

    public void setTranCurId(String tranCurId) {
        this.tranCurId = tranCurId;
    }

    public String getQuotationMethod() {
        return quotationMethod;
    }

    public void setQuotationMethod(String quotationMethod) {
        this.quotationMethod = quotationMethod;
    }

    public String getIsPaymentAmount() {
        return isPaymentAmount;
    }

    public void setIsPaymentAmount(String isPaymentAmount) {
        this.isPaymentAmount = isPaymentAmount;
    }

    public BigDecimal getPremiumPointMin() {
        return premiumPointMin;
    }

    public void setPremiumPointMin(BigDecimal premiumPointMin) {
        this.premiumPointMin = premiumPointMin;
    }

    public BigDecimal getPremiumPointMax() {
        return premiumPointMax;
    }

    public void setPremiumPointMax(BigDecimal premiumPointMax) {
        this.premiumPointMax = premiumPointMax;
    }

    public BigDecimal getPremiumMoneyMin() {
        return premiumMoneyMin;
    }

    public void setPremiumMoneyMin(BigDecimal premiumMoneyMin) {
        this.premiumMoneyMin = premiumMoneyMin;
    }

    public BigDecimal getPremiumMoneyMax() {
        return premiumMoneyMax;
    }

    public void setPremiumMoneyMax(BigDecimal premiumMoneyMax) {
        this.premiumMoneyMax = premiumMoneyMax;
    }

    public String getTranType() {
        return tranType;
    }

    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

    public String getIsSwaptTran() {
        return isSwaptTran;
    }

    public void setIsSwaptTran(String isSwaptTran) {
        this.isSwaptTran = isSwaptTran;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getFixedMoney() {
        return fixedMoney;
    }

    public void setFixedMoney(String fixedMoney) {
        this.fixedMoney = fixedMoney;
    }

    public String getFexBusType() {
        return fexBusType;
    }

    public void setFexBusType(String fexBusType) {
        this.fexBusType = fexBusType;
    }

    public String getFexTranType() {
        return fexTranType;
    }

    public void setFexTranType(String fexTranType) {
        this.fexTranType = fexTranType;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getDeliveryCurId() {
        return deliveryCurId;
    }

    public void setDeliveryCurId(String deliveryCurId) {
        this.deliveryCurId = deliveryCurId;
    }

    public String getBuyCurId() {
        return buyCurId;
    }

    public void setBuyCurId(String buyCurId) {
        this.buyCurId = buyCurId;
    }

    public BigDecimal getBuyMoney() {
        return buyMoney;
    }

    public void setBuyMoney(BigDecimal buyMoney) {
        this.buyMoney = buyMoney;
    }

    public String getSellCurId() {
        return sellCurId;
    }

    public void setSellCurId(String sellCurId) {
        this.sellCurId = sellCurId;
    }

    public BigDecimal getSellMoney() {
        return sellMoney;
    }

    public void setSellMoney(BigDecimal sellMoney) {
        this.sellMoney = sellMoney;
    }

    public BigDecimal getWishRate() {
        return wishRate;
    }

    public void setWishRate(BigDecimal wishRate) {
        this.wishRate = wishRate;
    }

    public String getBankNote() {
        return bankNote;
    }

    public void setBankNote(String bankNote) {
        this.bankNote = bankNote;
    }

    public Date getTranDate() {
        return tranDate;
    }

    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
    }

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    public String getDeliveryDeadline() {
        return deliveryDeadline;
    }

    public void setDeliveryDeadline(String deliveryDeadline) {
        this.deliveryDeadline = deliveryDeadline;
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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getBusNo() {
        return busNo;
    }

    public void setBusNo(String busNo) {
        this.busNo = busNo;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPurposeDtl() {
        return purposeDtl;
    }

    public void setPurposeDtl(String purposeDtl) {
        this.purposeDtl = purposeDtl;
    }

    public String getFundsSource() {
        return fundsSource;
    }

    public void setFundsSource(String fundsSource) {
        this.fundsSource = fundsSource;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getAccNature() {
        return accNature;
    }

    public void setAccNature(String accNature) {
        this.accNature = accNature;
    }

    public String getFexProp() {
        return fexProp;
    }

    public void setFexProp(String fexProp) {
        this.fexProp = fexProp;
    }

    public String getBoodRmk() {
        return boodRmk;
    }

    public void setBoodRmk(String boodRmk) {
        this.boodRmk = boodRmk;
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

    public String getCotpId() {
        return cotpId;
    }

    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public String getPayCorpId() {
        return payCorpId;
    }

    public void setPayCorpId(String payCorpId) {
        this.payCorpId = payCorpId;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getIsQuick() {
        return isQuick;
    }

    public void setIsQuick(String isQuick) {
        this.isQuick = isQuick;
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

    public String getCorpBankAcc() {
        return corpBankAcc;
    }

    public void setCorpBankAcc(String corpBankAcc) {
        this.corpBankAcc = corpBankAcc;
    }

    public String getRemitMethod() {
        return remitMethod;
    }

    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }

    public String getCorpNameEn() {
        return corpNameEn;
    }

    public void setCorpNameEn(String corpNameEn) {
        this.corpNameEn = corpNameEn;
    }

    public String getCorpAddEn() {
        return corpAddEn;
    }

    public void setCorpAddEn(String corpAddEn) {
        this.corpAddEn = corpAddEn;
    }

    public String getCorpAddr() {
        return corpAddr;
    }

    public void setCorpAddr(String corpAddr) {
        this.corpAddr = corpAddr;
    }

    public String getCorpContact() {
        return corpContact;
    }

    public void setCorpContact(String corpContact) {
        this.corpContact = corpContact;
    }

    public String getCorpContactTel() {
        return corpContactTel;
    }

    public void setCorpContactTel(String corpContactTel) {
        this.corpContactTel = corpContactTel;
    }

    public String getCostSign() {
        return costSign;
    }

    public void setCostSign(String costSign) {
        this.costSign = costSign;
    }

    public String getCostAcc() {
        return costAcc;
    }

    public void setCostAcc(String costAcc) {
        this.costAcc = costAcc;
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

    public String getTranAttr() {
        return tranAttr;
    }

    public void setTranAttr(String tranAttr) {
        this.tranAttr = tranAttr;
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


    public String getCotpBank() {
        return cotpBank;
    }

    public void setCotpBank(String cotpBank) {
        this.cotpBank = cotpBank;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getCotpName() {
        return cotpName;
    }

    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTranCode1Name() {
        return tranCode1Name;
    }

    public void setTranCode1Name(String tranCode1Name) {
        this.tranCode1Name = tranCode1Name;
    }

    public String getTranCode2Name() {
        return tranCode2Name;
    }

    public void setTranCode2Name(String tranCode2Name) {
        this.tranCode2Name = tranCode2Name;
    }

    public String getCotpBankAcc() {
        return cotpBankAcc;
    }

    public void setCotpBankAcc(String cotpBankAcc) {
        this.cotpBankAcc = cotpBankAcc;
    }

    public String getCotpAccId() {
        return cotpAccId;
    }

    public void setCotpAccId(String cotpAccId) {
        this.cotpAccId = cotpAccId;
    }

    public String getCotpAccCurId() {
        return cotpAccCurId;
    }

    public void setCotpAccCurId(String cotpAccCurId) {
        this.cotpAccCurId = cotpAccCurId;
    }

    public String getCotpAccCurName() {
        return cotpAccCurName;
    }

    public void setCotpAccCurName(String cotpAccCurName) {
        this.cotpAccCurName = cotpAccCurName;
    }

    public String getCotpAccEnglishCode() {
        return cotpAccEnglishCode;
    }

    public void setCotpAccEnglishCode(String cotpAccEnglishCode) {
        this.cotpAccEnglishCode = cotpAccEnglishCode;
    }

    public String getCotpPostAddr() {
        return cotpPostAddr;
    }

    public void setCotpPostAddr(String cotpPostAddr) {
        this.cotpPostAddr = cotpPostAddr;
    }

    public String getCountryArea() {
        return countryArea;
    }

    public void setCountryArea(String countryArea) {
        this.countryArea = countryArea;
    }

    public FexCotpBankAccVO getFexCotpBankAccVO() {
        return fexCotpBankAccVO;
    }

    public void setFexCotpBankAccVO(FexCotpBankAccVO fexCotpBankAccVO) {
        this.fexCotpBankAccVO = fexCotpBankAccVO;
    }

    public String getCorpBankAccName() {
        return corpBankAccName;
    }

    public void setCorpBankAccName(String corpBankAccName) {
        this.corpBankAccName = corpBankAccName;
    }

    public String getSwapType() {
        return swapType;
    }

    public void setSwapType(String swapType) {
        this.swapType = swapType;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getDeliveryCurName() {
        return deliveryCurName;
    }

    public void setDeliveryCurName(String deliveryCurName) {
        this.deliveryCurName = deliveryCurName;
    }

    public String getDeliveryEnglishCode() {
        return deliveryEnglishCode;
    }

    public void setDeliveryEnglishCode(String deliveryEnglishCode) {
        this.deliveryEnglishCode = deliveryEnglishCode;
    }

    public String getBuyCurName() {
        return buyCurName;
    }

    public void setBuyCurName(String buyCurName) {
        this.buyCurName = buyCurName;
    }

    public String getBuyCurEnCode() {
        return buyCurEnCode;
    }

    public void setBuyCurEnCode(String buyCurEnCode) {
        this.buyCurEnCode = buyCurEnCode;
    }

    public String getSellCurName() {
        return sellCurName;
    }

    public void setSellCurName(String sellCurName) {
        this.sellCurName = sellCurName;
    }

    public String getSellCurEnCode() {
        return sellCurEnCode;
    }

    public void setSellCurEnCode(String sellCurEnCode) {
        this.sellCurEnCode = sellCurEnCode;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getCurEnCode() {
        return curEnCode;
    }

    public void setCurEnCode(String curEnCode) {
        this.curEnCode = curEnCode;
    }

    public String getTranCur1Name() {
        return tranCur1Name;
    }

    public void setTranCur1Name(String tranCur1Name) {
        this.tranCur1Name = tranCur1Name;
    }

    public String getTranCur1EnCode() {
        return tranCur1EnCode;
    }

    public void setTranCur1EnCode(String tranCur1EnCode) {
        this.tranCur1EnCode = tranCur1EnCode;
    }

    public String getTranCur2Name() {
        return tranCur2Name;
    }

    public void setTranCur2Name(String tranCur2Name) {
        this.tranCur2Name = tranCur2Name;
    }

    public String getTranCur3EnCode() {
        return tranCur3EnCode;
    }

    public void setTranCur3EnCode(String tranCur3EnCode) {
        this.tranCur3EnCode = tranCur3EnCode;
    }

    public String getBondCurId() {
        return bondCurId;
    }

    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }

    public String getBondCurName() {
        return bondCurName;
    }

    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }

    public String getBondCurEnCode() {
        return bondCurEnCode;
    }

    public void setBondCurEnCode(String bondCurEnCode) {
        this.bondCurEnCode = bondCurEnCode;
    }

    public String getAutoCal() {
        return autoCal;
    }

    public void setAutoCal(String autoCal) {
        this.autoCal = autoCal;
    }

    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public String getSettleBusType() {
        return settleBusType;
    }

    public void setSettleBusType(String settleBusType) {
        this.settleBusType = settleBusType;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getBusDirection() {
        return busDirection;
    }

    public void setBusDirection(String busDirection) {
        this.busDirection = busDirection;
    }

    public String getUpdownDirection() {
        return updownDirection;
    }

    public void setUpdownDirection(String updownDirection) {
        this.updownDirection = updownDirection;
    }

    public Date getAvgBeginDate() {
        return avgBeginDate;
    }

    public void setAvgBeginDate(Date avgBeginDate) {
        this.avgBeginDate = avgBeginDate;
    }

    public Date getAvgEndDate() {
        return avgEndDate;
    }

    public void setAvgEndDate(Date avgEndDate) {
        this.avgEndDate = avgEndDate;
    }

    public Integer getAvgDays() {
        return avgDays;
    }

    public void setAvgDays(Integer avgDays) {
        this.avgDays = avgDays;
    }

    public String getValidStandard() {
        return validStandard;
    }

    public void setValidStandard(String validStandard) {
        this.validStandard = validStandard;
    }

    public BigDecimal getExceptLowVal() {
        return exceptLowVal;
    }

    public void setExceptLowVal(BigDecimal exceptLowVal) {
        this.exceptLowVal = exceptLowVal;
    }

    public BigDecimal getExceptHignVal() {
        return exceptHignVal;
    }

    public void setExceptHignVal(BigDecimal exceptHignVal) {
        this.exceptHignVal = exceptHignVal;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public BigDecimal getInvoiceTotal() {
        return invoiceTotal;
    }

    public void setInvoiceTotal(BigDecimal invoiceTotal) {
        this.invoiceTotal = invoiceTotal;
    }

    public BigDecimal getDeductionAmount() {
        return deductionAmount;
    }

    public void setDeductionAmount(BigDecimal deductionAmount) {
        this.deductionAmount = deductionAmount;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getSaveFields() {
        return saveFields;
    }

    public void setSaveFields(String saveFields) {
        this.saveFields = saveFields;
    }

    public String getFpsId() {
        return fpsId;
    }

    public void setFpsId(String fpsId) {
        this.fpsId = fpsId;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getAiIn() {
        return aiIn;
    }

    public void setAiIn(String aiIn) {
        this.aiIn = aiIn;
    }

    public String getHkId() {
        return hkId;
    }

    public void setHkId(String hkId) {
        this.hkId = hkId;
    }

    public String getCotpCountry() {
        return cotpCountry;
    }

    public void setCotpCountry(String cotpCountry) {
        this.cotpCountry = cotpCountry;
    }

    public String getCotpAddress() {
        return cotpAddress;
    }

    public void setCotpAddress(String cotpAddress) {
        this.cotpAddress = cotpAddress;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getCorpAccCurId() {
        return corpAccCurId;
    }

    public void setCorpAccCurId(String corpAccCurId) {
        this.corpAccCurId = corpAccCurId;
    }

    public String getCorpAccCurName() {
        return corpAccCurName;
    }

    public void setCorpAccCurName(String corpAccCurName) {
        this.corpAccCurName = corpAccCurName;
    }

    public String getCorpAccEnglishCode() {
        return corpAccEnglishCode;
    }

    public void setCorpAccEnglishCode(String corpAccEnglishCode) {
        this.corpAccEnglishCode = corpAccEnglishCode;
    }

    public String getIsPayed() {
        return isPayed;
    }

    public void setIsPayed(String isPayed) {
        this.isPayed = isPayed;
    }

    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    public String getCotpNameEn() {
        return cotpNameEn;
    }

    public void setCotpNameEn(String cotpNameEn) {
        this.cotpNameEn = cotpNameEn;
    }

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    public FexCotpVO getFexCotpVO() {
        return fexCotpVO;
    }

    public void setFexCotpVO(FexCotpVO fexCotpVO) {
        this.fexCotpVO = fexCotpVO;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }


    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    public String getBankPayTypeName() {
        return bankPayTypeName;
    }

    public void setBankPayTypeName(String bankPayTypeName) {
        this.bankPayTypeName = bankPayTypeName;
    }


    public String getBankPayTypeId() {
        return bankPayTypeId;
    }

    public void setBankPayTypeId(String bankPayTypeId) {
        this.bankPayTypeId = bankPayTypeId;
    }
}
