package com.bytter.finance.domain.directFinance.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 10:55
 * @Version 1.0
 */
@Entity
@Table(name = "DF_DIRECT_FINANCE_INFO", schema = "")
@SuppressWarnings("serial")
public class DfDirectFinanceInfo extends IdEntity implements Serializable {
    /**
     * 流水号
     */
    private String billCode;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 单位id
     */
    private String corpId;
    /**
     * 是否挂接资金计划
     */
    private String isBudget;
    /**
     * 证券名称
     */
    private String stockName;
    /**
     * 证券代码
     */
    private String stockCode;
    /**
     * 发行日期/开始日期
     */
    private Date startDate;
    /**
     * 币种
     */
    private String curId;
    /**
     * 币别名称
     */
    private String curName;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 债券评级
     */
    private String bondRating;
    /**
     * 主体评级
     */
    private String bondMainRating;
    /**
     * 期限
     */
    private String peroid;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 发行规模
     */
    private BigDecimal money;
    /**
     * 余额
     */
    private BigDecimal surplusMoney;
    /**
     * 发行批文编号
     */
    private String DocNo;
    /**
     * 上市日期
     */
    private Date listingDate;
    /**
     * 到期日期/结束日期
     */
    private Date endDate;
    /**
     * 是否包含回售条款
     */
    private String isResaleTerms;
    /**
     * 回售日
     */
    private Date reSaleDate;
    /**
     * 是否包含回赎回款
     */
    private String isRedemptionProvision;
    /**
     * 赎回日
     */
    private Date redemptionProvisionDate;
    /**
     * 上市地点
     */
    private String listingPlace;
    /**
     * 约定用途
     */
    private String purpose;
    /**
     * 直融类别
     */
    private String dfBusinessTypeId;
    /**
     * 发行主体类型
     */
    private String releaseMainType;
    /**
     * 发行主体名称
     */
    private String releaseMainName;
    /**
     * 承销商
     */
    private String underwriter;
    /**
     * 主承销商
     */
    private String leaderUnderwriter;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 息票类型
     */
    private String couponType;
    /**
     * 付息频率
     */
    private String frequencyInterestPayments;
    /**
     * 票面利率
     */
    private BigDecimal faceInterestRate;
    /**
     * 浮动利率
     */
    private BigDecimal floatInterestRat;
    /**
     * 利率值
     */
    private BigDecimal interestRat;
    /**
     * 综合成本年化利率
     */
    private BigDecimal compositeCostRate;
    /**
     * 综合成本
     */
    private BigDecimal compositeCost;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 附件id
     */
    private String uploadId;
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "IS_BUDGET")
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    @Column(name = "STOCK_NAME")
    public String getStockName() {
        return stockName;
    }
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
    @Column(name = "STOCK_CODE")
    public String getStockCode() {
        return stockCode;
    }
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "BOND_RATING")
    public String getBondRating() {
        return bondRating;
    }
    public void setBondRating(String bondRating) {
        this.bondRating = bondRating;
    }
    @Column(name = "BOND_MAIN_RATING")
    public String getBondMainRating() {
        return bondMainRating;
    }
    public void setBondMainRating(String bondMainRating) {
        this.bondMainRating = bondMainRating;
    }
    @Column(name = "PEROID")
    public String getPeroid() {
        return peroid;
    }
    public void setPeroid(String peroid) {
        this.peroid = peroid;
    }
    @Column(name = "UNIT")
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    @Column(name = "SURPLUS_MONEY")
    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }
    @Column(name = "DOC_NO")
    public String getDocNo() {
        return DocNo;
    }
    public void setDocNo(String docNo) {
        DocNo = docNo;
    }
    @Column(name = "LISTING_DATE")
    public Date getListingDate() {
        return listingDate;
    }
    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "IS_RESALE_TERMS")
    public String getIsResaleTerms() {
        return isResaleTerms;
    }
    public void setIsResaleTerms(String isResaleTerms) {
        this.isResaleTerms = isResaleTerms;
    }
    @Column(name = "RESALE_DATE")
    public Date getReSaleDate() {
        return reSaleDate;
    }
    public void setReSaleDate(Date reSaleDate) {
        this.reSaleDate = reSaleDate;
    }
    @Column(name = "IS_REDEMPTION_PROVISION")
    public String getIsRedemptionProvision() {
        return isRedemptionProvision;
    }
    public void setIsRedemptionProvision(String isRedemptionProvision) {
        this.isRedemptionProvision = isRedemptionProvision;
    }
    @Column(name = "REDEMPTION_PROVISION_DATE")
    public Date getRedemptionProvisionDate() {
        return redemptionProvisionDate;
    }
    public void setRedemptionProvisionDate(Date redemptionProvisionDate) {
        this.redemptionProvisionDate = redemptionProvisionDate;
    }
    @Column(name = "LISTING_PLACE")
    public String getListingPlace() {
        return listingPlace;
    }
    public void setListingPlace(String listingPlace) {
        this.listingPlace = listingPlace;
    }
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "DF_BUSINESS_TYPE_ID")
    public String getDfBusinessTypeId() {
        return dfBusinessTypeId;
    }
    public void setDfBusinessTypeId(String dfBusinessTypeId) {
        this.dfBusinessTypeId = dfBusinessTypeId;
    }
    @Column(name = "RELEASE_MAIN_TYPE")
    public String getReleaseMainType() {
        return releaseMainType;
    }
    public void setReleaseMainType(String releaseMainType) {
        this.releaseMainType = releaseMainType;
    }
    @Column(name = "RELEASE_MAIN_NAME")
    public String getReleaseMainName() {
        return releaseMainName;
    }
    public void setReleaseMainName(String releaseMainName) {
        this.releaseMainName = releaseMainName;
    }
    @Column(name = "LEADER_UNDERWRITER")
    public String getUnderwriter() {
        return underwriter;
    }
    public void setUnderwriter(String underwriter) {
        this.underwriter = underwriter;
    }
    @Column(name = "UNDERWRITER")
    public String getLeaderUnderwriter() {
        return leaderUnderwriter;
    }
    public void setLeaderUnderwriter(String leaderUnderwriter) {
        this.leaderUnderwriter = leaderUnderwriter;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "COUPON_TYPE")
    public String getCouponType() {
        return couponType;
    }
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
    @Column(name = "FREQUENCY_INTEREST_PAYMENTS")
    public String getFrequencyInterestPayments() {
        return frequencyInterestPayments;
    }
    public void setFrequencyInterestPayments(String frequencyInterestPayments) {
        this.frequencyInterestPayments = frequencyInterestPayments;
    }
    @Column(name = "FACE_INTEREST_RATE")
    public BigDecimal getFaceInterestRate() {
        return faceInterestRate;
    }
    public void setFaceInterestRate(BigDecimal faceInterestRate) {
        this.faceInterestRate = faceInterestRate;
    }
    @Column(name = "FLOAT_INTEREST_RAT")
    public BigDecimal getFloatInterestRat() {
        return floatInterestRat;
    }
    public void setFloatInterestRat(BigDecimal floatInterestRat) {
        this.floatInterestRat = floatInterestRat;
    }
    @Column(name = "INTEREST_RAT")
    public BigDecimal getInterestRat() {
        return interestRat;
    }
    public void setInterestRat(BigDecimal interestRat) {
        this.interestRat = interestRat;
    }
    @Column(name = "COMPOSITE_COST_RATE")
    public BigDecimal getCompositeCostRate() {
        return compositeCostRate;
    }
    public void setCompositeCostRate(BigDecimal compositeCostRate) {
        this.compositeCostRate = compositeCostRate;
    }
    @Column(name = "COMPOSITE_COST")
    public BigDecimal getCompositeCost() {
        return compositeCost;
    }
    public void setCompositeCost(BigDecimal compositeCost) {
        this.compositeCost = compositeCost;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPLOAD_ID")
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
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
