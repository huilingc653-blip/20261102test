package com.bytter.credential.domain.international.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title Entity
 * @Description 信用证开证表
 * @Author  liusy
 * @Date 2018-11-28 16:51
 * @Version 1.0
 */
@Entity
@Table(name = "LC_ENROL")
public class LcEnrol extends IdEntity implements Serializable {
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
    /**币种名称 */
    private String curName;
    /*汇率*/
    private BigDecimal exchangeRate;
    /*开证日期*/
    private Date issuingDate;
    /*信用证有效期*/
    private Date expiryDate;
    /*开证金额*/
    private BigDecimal creditMoney;
    /*开证人*/
    private String applicant;
    /*开证人账号ID*/
    private String applicantAccId;
    /*开证人账号*/
    private String applicantAcc;
    /*开证银行*/
    private String issuingBank;
    private String bankName;
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
    private String chargeCurId;
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
    private Date createDate;
    /*更新人登录名称*/
    private String updateBy;
    /*更新日期*/
    private Date updateDate;
    /**
     * 是否挂授信
     */
    private String isHangCredit;
    /**
     * 授信模式0单一授信，1综合授信
     */
    private String ratmodel;
    /**
     * 授信单号
     */
    private String rationCode;
    /**
     * 占用授信品种代码
     */
    private String itemCode;
    /**
     * 授信剩余额度
     */
    private BigDecimal itemMoney;
    /**
     * 是否挂接保证金（只是保存保证金信息，未真正挂接）
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
    private String bondContract;
    /**
     * 保证金账户
     */
    private String bondBankAcc;
    /**
     * 保证金id
     */
    private String bondId;
    /**
     * 是否代开信用证
     */
    private String isReCredit;
    /**
     * 被代开单位
     */
    private String reCorpCode;
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
    private String bondCurId;
    /**
     *是否启用授信敞口
     */
    private String isOpenRat;
    /**
     *业务品种
     */
    private String billName;
    /**
     * 是否挂接担保
     */
    private String isHangGuarantee;
    /**
     * 担保备注
     */
    private String guaranteeRmk;
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
     * 转换币种
     */
    private String convertCurId;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 单位名称
     */
    private String corpName;


    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "LC_INTER")
    public String getLcInter() {
        return lcInter;
    }
    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }
    @Column(name = "LC_TYPE")
    public String getLcType() {
        return lcType;
    }
    public void setLcType(String lcType) {
        this.lcType = lcType;
    }
    @Column(name = "LC_NO")
    public String getLcNo() {
        return lcNo;
    }
    public void setLcNo(String lcNo) {
        this.lcNo = lcNo;
    }
    @Column(name = "CREDIT_NO")
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    @Column(name = "CONTRACT_NO")
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    @Column(name = "ISSUING_CUR_ID")
    public String getIssuingCurId() {
        return issuingCurId;
    }
    public void setIssuingCurId(String issuingCurId) {
        this.issuingCurId = issuingCurId;
    }
    @Column(name = "EXCHANGE_RATE")
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    @Column(name = "ISSUING_DATE")
    public Date getIssuingDate() {
        return issuingDate;
    }
    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }
    @Column(name = "EXPIRY_DATE")
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    @Column(name = "CREDIT_MONEY")
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    @Column(name = "APPLICANT")
    public String getApplicant() {
        return applicant;
    }
    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }
    @Column(name = "APPLICANT_ACC_ID")
    public String getApplicantAccId() {
        return applicantAccId;
    }
    public void setApplicantAccId(String applicantAccId) {
        this.applicantAccId = applicantAccId;
    }
    @Column(name = "APPLICANT_ACC")
    public String getApplicantAcc() {
        return applicantAcc;
    }
    public void setApplicantAcc(String applicantAcc) {
        this.applicantAcc = applicantAcc;
    }
    @Column(name = "ISSUING_BANK")
    public String getIssuingBank() {
        return issuingBank;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setIssuingBank(String issuingBank) {
        this.issuingBank = issuingBank;
    }
    @Column(name = "ADDR")
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    @Column(name = "POST_NUM")
    public String getPostNum() {
        return postNum;
    }
    public void setPostNum(String postNum) {
        this.postNum = postNum;
    }
    @Column(name = "BENEFICIARY")
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    @Column(name = "BENEFICIARY_ACC_ID")
    public String getBeneficiaryAccId() {
        return beneficiaryAccId;
    }
    public void setBeneficiaryAccId(String beneficiaryAccId) {
        this.beneficiaryAccId = beneficiaryAccId;
    }
    @Column(name = "BENEFICIARY_ACC")
    public String getBeneficiaryAcc() {
        return beneficiaryAcc;
    }
    public void setBeneficiaryAcc(String beneficiaryAcc) {
        this.beneficiaryAcc = beneficiaryAcc;
    }
    @Column(name = "BENEFICIARY_BANK")
    public String getBeneficiaryBank() {
        return beneficiaryBank;
    }
    public void setBeneficiaryBank(String beneficiaryBank) {
        this.beneficiaryBank = beneficiaryBank;
    }
    @Column(name = "ADDR_BEN")
    public String getAddrBen() {
        return addrBen;
    }
    public void setAddrBen(String addrBen) {
        this.addrBen = addrBen;
    }
    @Column(name = "POST_NUM_BEN")
    public String getPostNumBen() {
        return postNumBen;
    }
    public void setPostNumBen(String postNumBen) {
        this.postNumBen = postNumBen;
    }
    @Column(name = "TIME_LIMIT")
    public String getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }
    @Column(name = "DELAY_DATES")
    public Integer getDelayDates() {
        return delayDates;
    }
    public void setDelayDates(Integer delayDates) {
        this.delayDates = delayDates;
    }
    @Column(name = "PAY_CHARGE_ACC")
    public String getPayChargeAacc() {
        return payChargeAacc;
    }
    public void setPayChargeAacc(String payChargeAacc) {
        this.payChargeAacc = payChargeAacc;
    }
    @Column(name = "PAY_CHARGE_ACC_ID")
    public String getPayChargeAccId() {
        return payChargeAccId;
    }
    public void setPayChargeAccId(String payChargeAccId) {
        this.payChargeAccId = payChargeAccId;
    }
    @Column(name = "CHARGE_CUR_ID")
    public String getChargeCurId() {
        return chargeCurId;
    }
    public void setChargeCurId(String chargeCurId) {
        this.chargeCurId = chargeCurId;
    }
    @Column(name = "CHARGE_MONEY")
    public BigDecimal getChargeMoney() {
        return chargeMoney;
    }
    public void setChargeMoney(BigDecimal chargeMoney) {
        this.chargeMoney = chargeMoney;
    }
    @Column(name = "ACCEPTANCE_MONEY")
    public BigDecimal getAcceptanceMoney() {
        return acceptanceMoney;
    }
    public void setAcceptanceMoney(BigDecimal acceptanceMoney) {
        this.acceptanceMoney = acceptanceMoney;
    }
    @Column(name = "ACCEPTANCE_FEE")
    public BigDecimal getAcceptanceFee() {
        return acceptanceFee;
    }
    public void setAcceptanceFee(BigDecimal acceptanceFee) {
        this.acceptanceFee = acceptanceFee;
    }
    @Column(name = "OTHER_FEE")
    public BigDecimal getOtherFee() {
        return otherFee;
    }
    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }
    @Column(name = "DISCOUNT_RATE")
    public BigDecimal getDiscountRate() {
        return discountRate;
    }
    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
    @Column(name = "DISCOUNT_INTEREST")
    public BigDecimal getDiscountInterest() {
        return discountInterest;
    }
    public void setDiscountInterest(BigDecimal discountInterest) {
        this.discountInterest = discountInterest;
    }
    @Column(name = "TRADE_DEFICIT")
    public BigDecimal getTradeDeficit() {
        return tradeDeficit;
    }
    public void setTradeDeficit(BigDecimal tradeDeficit) {
        this.tradeDeficit = tradeDeficit;
    }
    @Column(name = "COMP_COST")
    public BigDecimal getCompCost() {
        return compCost;
    }
    public void setCompCost(BigDecimal compCost) {
        this.compCost = compCost;
    }
    @Column(name = "REBACK_MONEY")
    public BigDecimal getRebackMoney() {
        return rebackMoney;
    }
    public void setRebackMoney(BigDecimal rebackMoney) {
        this.rebackMoney = rebackMoney;
    }
    @Column(name = "OPEN_MONEY")
    public BigDecimal getOpenMoney() {
        return openMoney;
    }
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    @Column(name = "GOODS_DESCRIPT")
    public String getGoodsDescript() {
        return goodsDescript;
    }
    public void setGoodsDescript(String goodsDescript) {
        this.goodsDescript = goodsDescript;
    }
    @Column(name = "EXPIRY_PLACE")
    public String getExpiryPlace() {
        return expiryPlace;
    }
    public void setExpiryPlace(String expiryPlace) {
        this.expiryPlace = expiryPlace;
    }
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "RE_CREDIT_NO")
    public String getReCreditNo() {
        return reCreditNo;
    }
    public void setReCreditNo(String reCreditNo) {
        this.reCreditNo = reCreditNo;
    }
    @Column(name = "RE_BANK")
    public String getReBank() {
        return reBank;
    }
    public void setReBank(String reBank) {
        this.reBank = reBank;
    }
    @Column(name = "NEED_LICENCE")
    public String getNeedLicence() {
        return needLicence;
    }
    public void setNeedLicence(String needLicence) {
        this.needLicence = needLicence;
    }
    @Column(name = "LICENCE_CODE")
    public String getLicenceCode() {
        return licenceCode;
    }
    public void setLicenceCode(String licenceCode) {
        this.licenceCode = licenceCode;
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
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "IS_HANG_CREDIT")
    public String getIsHangCredit() {
        return isHangCredit;
    }
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    @Column(name = "RATMODEL")
    public String getRatmodel() {
        return ratmodel;
    }
    public void setRatmodel(String ratmodel) {
        this.ratmodel = ratmodel;
    }
    @Column(name = "RATION_CODE")
    public String getRationCode() {
        return rationCode;
    }
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    @Column(name = "BOND_PERCENT")
    public BigDecimal getBondPercent() {
        return bondPercent;
    }
    public void setBondPercent(BigDecimal bondPercent) {
        this.bondPercent = bondPercent;
    }
    @Column(name = "BOND_MONEY")
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    @Column(name = "BOND_CONTRACT")
    public String getBondContract() {
        return bondContract;
    }
    public void setBondContract(String bondContract) {
        this.bondContract = bondContract;
    }
    @Column(name = "IS_HANG_BOND")
    public String getIsHangBond() {
        return isHangBond;
    }
    public void setIsHangBond(String isHangBond) {
        this.isHangBond = isHangBond;
    }
    @Column(name = "BOND_BANK_ACC")
    public String getBondBankAcc() {
        return bondBankAcc;
    }
    public void setBondBankAcc(String bondBankAcc) {
        this.bondBankAcc = bondBankAcc;
    }
    @Column(name = "BOND_ID")
    public String getBondId() {
        return bondId;
    }
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    @Column(name = "ITEM_MONEY")
    public BigDecimal getItemMoney() {
        return itemMoney;
    }
    public void setItemMoney(BigDecimal itemMoney) {
        this.itemMoney = itemMoney;
    }
    @Column(name = "is_re_credit")
    public String getIsReCredit() {
        return isReCredit;
    }
    public void setIsReCredit(String isReCredit) {
        this.isReCredit = isReCredit;
    }
    @Column(name = "re_corp_code")
    public String getReCorpCode() {
        return reCorpCode;
    }
    public void setReCorpCode(String reCorpCode) {
        this.reCorpCode = reCorpCode;
    }
    @Column(name = "IS_SPILL_OVER")
    public String getIsSpillOver() {
        return isSpillOver;
    }
    public void setIsSpillOver(String isSpillOver) {
        this.isSpillOver = isSpillOver;
    }
    @Column(name = "SPILL_OVER_RATE")
    public BigDecimal getSpillOverRate() {
        return spillOverRate;
    }
    public void setSpillOverRate(BigDecimal spillOverRate) {
        this.spillOverRate = spillOverRate;
    }
    @Column(name = "IS_NATIVE_CUR")
    public String getIsNativeCur() {
        return isNativeCur;
    }
    public void setIsNativeCur(String isNativeCur) {
        this.isNativeCur = isNativeCur;
    }
    @Column(name = "NATIVE_CREDIT_MONEY")
    public BigDecimal getNativeCreditMoney() {
        return nativeCreditMoney;
    }
    public void setNativeCreditMoney(BigDecimal nativeCreditMoney) {
        this.nativeCreditMoney = nativeCreditMoney;
    }
    @Column(name = "BOND_CUR_ID")
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    @Column(name = "IS_OPEN_RAT")
    public String getIsOpenRat() {
        return isOpenRat;
    }
    public void setIsOpenRat(String isOpenRat) {
        this.isOpenRat = isOpenRat;
    }
    @Column(name = "BILL_NAME")
    public String getBillName() {
        return billName;
    }
    public void setBillName(String billName) {
        this.billName = billName;
    }
    @Column(name = "IS_HANG_GUARANTEE")
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    @Column(name = "GUARANTEE_RMK")
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    @Column(name = "BOND_RATE")
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    @Column(name = "OPENING_BANK")
    public String getOpeningBank() {
        return openingBank;
    }
    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }
    @Column(name = "IS_IN_COUNTRY")
    public String getIsInCountry() {
        return isInCountry;
    }
    public void setIsInCountry(String isInCountry) {
        this.isInCountry = isInCountry;
    }
    @Column(name = "OPENING_BANK_NAME")
    public String getOpeningBankName() {
        return openingBankName;
    }
    public void setOpeningBankName(String openingBankName) {
        this.openingBankName = openingBankName;
    }
    @Column(name = "CONVERT_CUR_ID")
    public String getConvertCurId() {
        return convertCurId;
    }
    public void setConvertCurId(String convertCurId) {
        this.convertCurId = convertCurId;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
