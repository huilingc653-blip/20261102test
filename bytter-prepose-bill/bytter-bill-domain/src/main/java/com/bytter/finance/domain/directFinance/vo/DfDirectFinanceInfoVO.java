package com.bytter.finance.domain.directFinance.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.User;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 10:55
 * @Version 1.0
 */
public class DfDirectFinanceInfoVO implements Serializable, IBusinessKey {
    /**
     * 主键id
     */
    private String id;
    /**
     * 流水号
     */
    private String billCode;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "t.status")
    private Integer status;
    /**
     * 单位id
     */
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 是否挂接资金计划
     */
    private String isBudget;
    /**
     * 证券名称
     */
    @BtCompositeSearch(fieldName = "t.STOCK_NAME")
    private String stockName;
    /**
     * 证券代码
     */
    @BtCompositeSearch(fieldName = "t.STOCK_CODE")
    private String stockCode;
    /**
     * 发行日期/开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date listingDate;
    /**
     * 到期日期/结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 是否包含回售条款
     */
    private String isResaleTerms;
    /**
     * 回售日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reSaleDate;
    /**
     * 是否包含回赎回款
     */
    private String isRedemptionProvision;
    /**
     * 赎回日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @BtCompositeSearch(fieldName = "t.DF_BUSINESS_TYPE_ID")
    private String dfBusinessTypeId;
    private String BusinessName;
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
    @User(inject = "createName")
    private String createBy;
    private String createName;
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
    /**
     * 累计还款金额
     */
    private BigDecimal allRepayMoney;
    @BtCompositeSearch(fieldName = "repayBizStatus")
    private Integer repayBizStatus;
    private List<DfThirdPartyAgencyVO> dfThirdPartyAgencyVOS;
    private List<DfIntermediateCostVO> dfIntermediateCostVOS;
    private List<DfDirectFinanceRepayTmpVO> dfDirectFinanceRepayTmpVOS;
    /** 资金预算 */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    public String getStockName() {
        return stockName;
    }
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
    public String getStockCode() {
        return stockCode;
    }
    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getBondRating() {
        return bondRating;
    }
    public void setBondRating(String bondRating) {
        this.bondRating = bondRating;
    }
    public String getBondMainRating() {
        return bondMainRating;
    }
    public void setBondMainRating(String bondMainRating) {
        this.bondMainRating = bondMainRating;
    }
    public String getPeroid() {
        return peroid;
    }
    public void setPeroid(String peroid) {
        this.peroid = peroid;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }
    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }
    public String getDocNo() {
        return DocNo;
    }
    public void setDocNo(String docNo) {
        DocNo = docNo;
    }
    public Date getListingDate() {
        return listingDate;
    }
    public void setListingDate(Date listingDate) {
        this.listingDate = listingDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getIsResaleTerms() {
        return isResaleTerms;
    }
    public void setIsResaleTerms(String isResaleTerms) {
        this.isResaleTerms = isResaleTerms;
    }
    public Date getReSaleDate() {
        return reSaleDate;
    }
    public void setReSaleDate(Date reSaleDate) {
        this.reSaleDate = reSaleDate;
    }
    public String getIsRedemptionProvision() {
        return isRedemptionProvision;
    }
    public void setIsRedemptionProvision(String isRedemptionProvision) {
        this.isRedemptionProvision = isRedemptionProvision;
    }
    public Date getRedemptionProvisionDate() {
        return redemptionProvisionDate;
    }
    public void setRedemptionProvisionDate(Date redemptionProvisionDate) {
        this.redemptionProvisionDate = redemptionProvisionDate;
    }
    public String getListingPlace() {
        return listingPlace;
    }
    public void setListingPlace(String listingPlace) {
        this.listingPlace = listingPlace;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getDfBusinessTypeId() {
        return dfBusinessTypeId;
    }
    public void setDfBusinessTypeId(String dfBusinessTypeId) {
        this.dfBusinessTypeId = dfBusinessTypeId;
    }
    public String getReleaseMainType() {
        return releaseMainType;
    }
    public void setReleaseMainType(String releaseMainType) {
        this.releaseMainType = releaseMainType;
    }
    public String getReleaseMainName() {
        return releaseMainName;
    }
    public void setReleaseMainName(String releaseMainName) {
        this.releaseMainName = releaseMainName;
    }
    public String getUnderwriter() {
        return underwriter;
    }
    public void setUnderwriter(String underwriter) {
        this.underwriter = underwriter;
    }
    public String getLeaderUnderwriter() {
        return leaderUnderwriter;
    }
    public void setLeaderUnderwriter(String leaderUnderwriter) {
        this.leaderUnderwriter = leaderUnderwriter;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCouponType() {
        return couponType;
    }
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
    public String getFrequencyInterestPayments() {
        return frequencyInterestPayments;
    }
    public void setFrequencyInterestPayments(String frequencyInterestPayments) {
        this.frequencyInterestPayments = frequencyInterestPayments;
    }
    public BigDecimal getFaceInterestRate() {
        return faceInterestRate;
    }
    public void setFaceInterestRate(BigDecimal faceInterestRate) {
        this.faceInterestRate = faceInterestRate;
    }
    public BigDecimal getFloatInterestRat() {
        return floatInterestRat;
    }
    public void setFloatInterestRat(BigDecimal floatInterestRat) {
        this.floatInterestRat = floatInterestRat;
    }
    public BigDecimal getInterestRat() {
        return interestRat;
    }
    public void setInterestRat(BigDecimal interestRat) {
        this.interestRat = interestRat;
    }
    public BigDecimal getCompositeCostRate() {
        return compositeCostRate;
    }
    public void setCompositeCostRate(BigDecimal compositeCostRate) {
        this.compositeCostRate = compositeCostRate;
    }
    public BigDecimal getCompositeCost() {
        return compositeCost;
    }
    public void setCompositeCost(BigDecimal compositeCost) {
        this.compositeCost = compositeCost;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    public List<DfThirdPartyAgencyVO> getDfThirdPartyAgencyVOS() {
        return dfThirdPartyAgencyVOS;
    }
    public void setDfThirdPartyAgencyVOS(List<DfThirdPartyAgencyVO> dfThirdPartyAgencyVOS) {
        this.dfThirdPartyAgencyVOS = dfThirdPartyAgencyVOS;
    }
    public List<DfIntermediateCostVO> getDfIntermediateCostVOS() {
        return dfIntermediateCostVOS;
    }
    public void setDfIntermediateCostVOS(List<DfIntermediateCostVO> dfIntermediateCostVOS) {
        this.dfIntermediateCostVOS = dfIntermediateCostVOS;
    }
    public List<DfDirectFinanceRepayTmpVO> getDfDirectFinanceRepayTmpVOS() {
        return dfDirectFinanceRepayTmpVOS;
    }
    public void setDfDirectFinanceRepayTmpVOS(List<DfDirectFinanceRepayTmpVO> dfDirectFinanceRepayTmpVOS) {
        this.dfDirectFinanceRepayTmpVOS = dfDirectFinanceRepayTmpVOS;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getBusinessName() {
        return BusinessName;
    }
    public void setBusinessName(String businessName) {
        BusinessName = businessName;
    }
    public BigDecimal getAllRepayMoney() {
        return allRepayMoney;
    }
    public void setAllRepayMoney(BigDecimal allRepayMoney) {
        this.allRepayMoney = allRepayMoney;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public Integer getRepayBizStatus() {
        return repayBizStatus;
    }
    public void setRepayBizStatus(Integer repayBizStatus) {
        this.repayBizStatus = repayBizStatus;
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
    @Override
    public String getBusinessKey() {
        return id;
    }
}
