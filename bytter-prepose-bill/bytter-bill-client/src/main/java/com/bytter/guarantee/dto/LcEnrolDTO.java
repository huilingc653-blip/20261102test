package com.bytter.guarantee.dto;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LcEnrolDTO implements Serializable {


    private String id;

    /*单位编号*/

    private String corpId;

    /*信用证类型*/

    private String lcInter;

    /*信用证业务*/
    private String lcType;

    /*信用证单号*/

    private String lcNo;

    /*信用证号*/

    private String creditNo;

    /*合同号*/

    private String contractNo;

    /*开证币别*/

    private String issuingCurId;

    /*汇率*/
    private BigDecimal ratExchangeRate;

    /*开证日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")

    private Date issuingDate;

    /*信用证有效期*/

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    /*开证金额*/

    private BigDecimal creditMoney;

    /*开证人*/

    private String applicant;

    private String applicantName;

    /*开证人账号ID*/
    private String applicantAccId;

    /*开证人账号*/
    private String applicantAcc;

    /*开证银行*/
    private String issuingBank;

    /*开证银行显示名*/
    private String issuingBankName;

    /*开证人地址*/
    private String addr;

    /*开证人邮编*/
    private String postNum;

    /*受益人*/
    private String beneficiary;

    /*受益人账号ID*/
    private String beneficiaryAccId;


    /*受益人账号*/
    private String beneficiaryAcc;

    /*受益人开证行*/
    private String beneficiaryBank;

    /*受益人地址*/
    private String addrBen;

    /*受益人邮编*/
    private String postNumBen;

    /*期限*/
    private String timeLimit;

    /*远期天数*/
    private Integer delayDates;

    /*手续费账号*/
    private String payChargeAacc;

    /*手续费账号ID*/
    private String payChargeAccId;

    /*手续费币别*/
    @Currency(inject = "chargeCurName")
    private String chargeCurId;
    private String chargeCurName;

    /*手续费金额*/
    private BigDecimal chargeMoney;

    /*承兑金额*/
    private BigDecimal acceptanceMoney;

    /*承兑费*/
    private BigDecimal acceptanceFee;

    /*其他费用*/
    private BigDecimal otherFee;

    /*贴现费率*/
    private BigDecimal discountRate;

    /*贴现息*/
    private BigDecimal discountInterest;

    /*贸易亏损*/
    private BigDecimal tradeDeficit;

    /*综合成本*/
    private BigDecimal compCost;

    /*应回款金额*/
    private BigDecimal rebackMoney;

    /*敞口金额*/
    private BigDecimal openMoney;

    /*货物描述*/
    private String goodsDescript;

    /*有效地*/
    private String expiryPlace;

    /*用途*/
    private String purpose;

    /*备注*/
    private String rmk;

    /*状态*/
    private Integer status;

    /*代开信用证号*/
    private String reCreditNo;

    /*代开证银行*/
    private String reBank;

    /*是否需要许可证*/
    private String needLicence;

    /*许可证号码*/
    private String licenceCode;

    /*创建人登录名称*/
    private String createBy;

    /*创建日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;

    /*更新人登录名称*/
    private String updateBy;

    private String updateName;

    /*更新日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;

    /**
     * 业务状态
     **/
    private String bizStatus;


    /**
     * 币别名称
     **/
    private String curName;

    /**
     * 交单金额
     **/
    @BtCompositeSearch(fieldName = "t1.bill_money")
    private BigDecimal billMoney;

    private String corpName;

    /*到单手续费*/
    private BigDecimal handlingCharge;
    /*到单日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "t1.payment_date")
    private Date arrivalBillDate;
    /*付汇金额*/
    @BtCompositeSearch(fieldName = "t2.payment_money")
    private BigDecimal paymentMoney;
    /*付汇日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "t2.payment_date")
    private Date paymentDate;

    /*押汇金额*/
    private BigDecimal depositMoney;

    /*还押汇金额*/
    private BigDecimal reapyMoney;

    /*议付金额*/
    private BigDecimal negotiationMoney;


//    授信部分字段

    /**
     * 是否挂授信
     */
    private String isHangCredit;
    /**
     * 授信模式
     */
    private String ratmodel;
    /**
     * 授信单号
     */
    private String rationCode;
    /**
     * 占用授信品种代码
     */
    private String ratItemCode;
    /**
     * 授信剩余额度
     */
    private BigDecimal itemMoney;

    /**
     * 是否挂接保证金
     */
    private String isHangBond;
    /**
     * 保证金比例
     */
    private BigDecimal bondPercent;
    /**
     * 保证金
     */
    private BigDecimal bondMoney;
    /**
     * 保证金合同号
     */
    private String bondMoneyContract;
    /**
     * 保证金账户
     */
    private String bondBankAcc;
    /**
     * 保证金id
     */
    private String bondId;

    /**
     * 是否溢短装
     */
    private String isSpillOver;

    /**
     * 溢短装比例
     */
    private BigDecimal spillOverRate;

    /**
     * 是否折算为本币位
     */
    private String isNativeCur;

    /**
     * 这算本币位金额
     */
    private BigDecimal nativeCreditMoney;

    /**
     * 保证金币别
     */
    @Currency(inject = "bondCurName")
    private String bondCurId;
    private String bondCurName;

    /**
     * 是否挂接担保
     */
    private String isHangGuarantee;

    /**
     * 担保备注
     */
    private String guaranteeRmk;

    /**
     * 担保占用额度
     */
    private List<BigDecimal> occupMoneys;

    /**
     * 保证金账户利率
     */
    private BigDecimal bondRate;

    /**
     * 开证银行 境内或者境外银行
     */
    private String openingBank;

    /**
     * 开证银行境内/境外 1=境内，2=境外
     */
    private String isInCountry;

    /**
     * 境内或者境外银行名称
     */
    private String openingBankName;

    /**
     *是否启用授信敞口
     */
    private String isOpenRat;

    /**
     * 转换币种
     */
    @Currency(inject = "convertCurName")
    private String convertCurId;
    private String convertCurName;

    /**
     * 附件id
     */
    private String attachmentId;

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

    /**跳过补录 1-是 0-否*/
    private String isSkipSupplement;

    /**
     * 信用证闭卷状态:
     * 未交单0
     * 未闭卷1
     * 待闭卷2
     * 已过效期3
     * 闭卷待审批11
     * 已闭卷95
     */
    private String closingStatus;

    /**
     *待收款金额
     */
    private BigDecimal sumPayMoney;

    /**
     *收款合计金额
     */
    private BigDecimal sumIncomingMoney;

    /**
     *押汇金额
     */
    private BigDecimal sumMortgageMoney;

    /**
     *解押汇金额
     */
    private BigDecimal sumMortgageCancelMoney;

    /**
     *议付金额
     */
    private BigDecimal sumNegotiationMoney;

    public String getIsSkipSupplement() {
        return isSkipSupplement;
    }

    public void setIsSkipSupplement(String isSkipSupplement) {
        this.isSkipSupplement = isSkipSupplement;
    }

    public BigDecimal getNegotiationMoney() {
        return negotiationMoney;
    }

    public void setNegotiationMoney(BigDecimal negotiationMoney) {
        this.negotiationMoney = negotiationMoney;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

//    @Override
//    public String getBusinessKey() {
//        return id;
//    }

    public BigDecimal getBillMoney() {
        return billMoney;
    }

    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getLcInter() {
        return lcInter;
    }

    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }

    public String getLcType() {
        return lcType;
    }

    public void setLcType(String lcType) {
        this.lcType = lcType;
    }

    public String getLcNo() {
        return lcNo;
    }

    public void setLcNo(String lcNo) {
        this.lcNo = lcNo;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getIssuingCurId() {
        return issuingCurId;
    }

    public void setIssuingCurId(String issuingCurId) {
        this.issuingCurId = issuingCurId;
    }

    public BigDecimal getRatExchangeRate() {
        return ratExchangeRate;
    }

    public void setRatExchangeRate(BigDecimal ratExchangeRate) {
        this.ratExchangeRate = ratExchangeRate;
    }

    public Date getIssuingDate() {
        return issuingDate;
    }

    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public BigDecimal getCreditMoney() {
        return creditMoney;
    }

    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public String getApplicantAccId() {
        return applicantAccId;
    }

    public void setApplicantAccId(String applicantAccId) {
        this.applicantAccId = applicantAccId;
    }

    public String getApplicantAcc() {
        return applicantAcc;
    }

    public void setApplicantAcc(String applicantAcc) {
        this.applicantAcc = applicantAcc;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPostNum() {
        return postNum;
    }

    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getBeneficiaryAccId() {
        return beneficiaryAccId;
    }

    public void setBeneficiaryAccId(String beneficiaryAccId) {
        this.beneficiaryAccId = beneficiaryAccId;
    }

    public String getBeneficiaryAcc() {
        return beneficiaryAcc;
    }

    public void setBeneficiaryAcc(String beneficiaryAcc) {
        this.beneficiaryAcc = beneficiaryAcc;
    }

    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }

    public void setBeneficiaryBank(String beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }

    public String getAddrBen() {
        return addrBen;
    }

    public void setAddrBen(String addrBen) {
        this.addrBen = addrBen;
    }

    public String getPostNumBen() {
        return postNumBen;
    }

    public void setPostNumBen(String postNumBen) {
        this.postNumBen = postNumBen;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Integer getDelayDates() {
        return delayDates;
    }

    public void setDelayDates(Integer delayDates) {
        this.delayDates = delayDates;
    }

    public String getPayChargeAacc() {
        return payChargeAacc;
    }

    public void setPayChargeAacc(String payChargeAacc) {
        this.payChargeAacc = payChargeAacc;
    }

    public String getPayChargeAccId() {
        return payChargeAccId;
    }

    public void setPayChargeAccId(String payChargeAccId) {
        this.payChargeAccId = payChargeAccId;
    }

    public String getChargeCurId() {
        return chargeCurId;
    }

    public void setChargeCurId(String chargeCurId) {
        this.chargeCurId = chargeCurId;
    }

    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public BigDecimal getAcceptanceMoney() {
        return acceptanceMoney;
    }

    public void setAcceptanceMoney(BigDecimal acceptanceMoney) {
        this.acceptanceMoney = acceptanceMoney;
    }

    public BigDecimal getAcceptanceFee() {
        return acceptanceFee;
    }

    public void setAcceptanceFee(BigDecimal acceptanceFee) {
        this.acceptanceFee = acceptanceFee;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public BigDecimal getDiscountInterest() {
        return discountInterest;
    }

    public void setDiscountInterest(BigDecimal discountInterest) {
        this.discountInterest = discountInterest;
    }

    public BigDecimal getTradeDeficit() {
        return tradeDeficit;
    }

    public void setTradeDeficit(BigDecimal tradeDeficit) {
        this.tradeDeficit = tradeDeficit;
    }

    public BigDecimal getCompCost() {
        return compCost;
    }

    public void setCompCost(BigDecimal compCost) {
        this.compCost = compCost;
    }

    public BigDecimal getRebackMoney() {
        return rebackMoney;
    }

    public void setRebackMoney(BigDecimal rebackMoney) {
        this.rebackMoney = rebackMoney;
    }

    public BigDecimal getOpenMoney() {
        return openMoney;
    }

    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }

    public String getGoodsDescript() {
        return goodsDescript;
    }

    public void setGoodsDescript(String goodsDescript) {
        this.goodsDescript = goodsDescript;
    }

    public String getExpiryPlace() {
        return expiryPlace;
    }

    public void setExpiryPlace(String expiryPlace) {
        this.expiryPlace = expiryPlace;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getReCreditNo() {
        return reCreditNo;
    }

    public void setReCreditNo(String reCreditNo) {
        this.reCreditNo = reCreditNo;
    }

    public String getReBank() {
        return reBank;
    }

    public void setReBank(String reBank) {
        this.reBank = reBank;
    }

    public String getNeedLicence() {
        return needLicence;
    }

    public void setNeedLicence(String needLicence) {
        this.needLicence = needLicence;
    }

    public String getLicenceCode() {
        return licenceCode;
    }

    public void setLicenceCode(String licenceCode) {
        this.licenceCode = licenceCode;
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

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getIssuingBankName() {
        return issuingBankName;
    }

    public void setIssuingBankName(String issuingBankName) {
        this.issuingBankName = issuingBankName;
    }

    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }

    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }

    public Date getArrivalBillDate() {
        return arrivalBillDate;
    }

    public void setArrivalBillDate(Date arrivalBillDate) {
        this.arrivalBillDate = arrivalBillDate;
    }

    public BigDecimal getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(BigDecimal paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(BigDecimal depositMoney) {
        this.depositMoney = depositMoney;
    }

    public BigDecimal getReapyMoney() {
        return reapyMoney;
    }

    public void setReapyMoney(BigDecimal reapyMoney) {
        this.reapyMoney = reapyMoney;
    }

    public String getIsHangCredit() {
        return isHangCredit;
    }

    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }

    public String getRatmodel() {
        return ratmodel;
    }

    public void setRatmodel(String ratmodel) {
        this.ratmodel = ratmodel;
    }

    public String getRationCode() {
        return rationCode;
    }

    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }

    public String getRatItemCode() {
        return ratItemCode;
    }

    public void setRatItemCode(String ratItemCode) {
        this.ratItemCode = ratItemCode;
    }

    public String getIsHangBond() {
        return isHangBond;
    }

    public void setIsHangBond(String isHangBond) {
        this.isHangBond = isHangBond;
    }

    public BigDecimal getBondPercent() {
        return bondPercent;
    }

    public void setBondPercent(BigDecimal bondPercent) {
        this.bondPercent = bondPercent;
    }

    public BigDecimal getBondMoney() {
        return bondMoney;
    }

    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }

    public String getBondMoneyContract() {
        return bondMoneyContract;
    }

    public void setBondMoneyContract(String bondMoneyContract) {
        this.bondMoneyContract = bondMoneyContract;
    }

    public String getBondBankAcc() {
        return bondBankAcc;
    }

    public void setBondBankAcc(String bondBankAcc) {
        this.bondBankAcc = bondBankAcc;
    }

    public String getBondId() {
        return bondId;
    }

    public void setBondId(String bondId) {
        this.bondId = bondId;
    }

    public BigDecimal getItemMoney() {
        return itemMoney;
    }

    public void setItemMoney(BigDecimal itemMoney) {
        this.itemMoney = itemMoney;
    }

    public String getIsSpillOver() {
        return isSpillOver;
    }

    public void setIsSpillOver(String isSpillOver) {
        this.isSpillOver = isSpillOver;
    }

    public BigDecimal getSpillOverRate() {
        return spillOverRate;
    }

    public void setSpillOverRate(BigDecimal spillOverRate) {
        this.spillOverRate = spillOverRate;
    }

    public String getIsNativeCur() {
        return isNativeCur;
    }

    public void setIsNativeCur(String isNativeCur) {
        this.isNativeCur = isNativeCur;
    }

    public BigDecimal getNativeCreditMoney() {
        return nativeCreditMoney;
    }

    public void setNativeCreditMoney(BigDecimal nativeCreditMoney) {
        this.nativeCreditMoney = nativeCreditMoney;
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

    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }

    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }

    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }

    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }

    public List<BigDecimal> getOccupMoneys() {
        return occupMoneys;
    }

    public void setOccupMoneys(List<BigDecimal> occupMoneys) {
        this.occupMoneys = occupMoneys;
    }

    public BigDecimal getBondRate() {
        return bondRate;
    }

    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }

    public String getChargeCurName() {
        return chargeCurName;
    }

    public void setChargeCurName(String chargeCurName) {
        this.chargeCurName = chargeCurName;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getIsInCountry() {
        return isInCountry;
    }

    public void setIsInCountry(String isInCountry) {
        this.isInCountry = isInCountry;
    }

    public String getOpeningBankName() {
        return openingBankName;
    }

    public void setOpeningBankName(String openingBankName) {
        this.openingBankName = openingBankName;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getIsOpenRat() {
        return isOpenRat;
    }

    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }

    public String getConvertCurId() {
        return convertCurId;
    }

    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }

    public String getConvertCurName() {
        return convertCurName;
    }

    public void setConvertCurName(String convertCurName) {
        this.convertCurName = convertCurName;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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

    public String getClosingStatus() {
        return closingStatus;
    }

    public void setClosingStatus(String closingStatus) {
        this.closingStatus = closingStatus;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public BigDecimal getSumPayMoney() {
        return sumPayMoney;
    }

    public void setSumPayMoney(BigDecimal sumPayMoney) {
        this.sumPayMoney = sumPayMoney;
    }

    public BigDecimal getSumIncomingMoney() {
        return sumIncomingMoney;
    }

    public void setSumIncomingMoney(BigDecimal sumIncomingMoney) {
        this.sumIncomingMoney = sumIncomingMoney;
    }

    public BigDecimal getSumMortgageMoney() {
        return sumMortgageMoney;
    }

    public void setSumMortgageMoney(BigDecimal sumMortgageMoney) {
        this.sumMortgageMoney = sumMortgageMoney;
    }

    public BigDecimal getSumNegotiationMoney() {
        return sumNegotiationMoney;
    }

    public void setSumNegotiationMoney(BigDecimal sumNegotiationMoney) {
        this.sumNegotiationMoney = sumNegotiationMoney;
    }

    public BigDecimal getSumMortgageCancelMoney() {
        return sumMortgageCancelMoney;
    }

    public void setSumMortgageCancelMoney(BigDecimal sumMortgageCancelMoney) {
        this.sumMortgageCancelMoney = sumMortgageCancelMoney;
    }
}
