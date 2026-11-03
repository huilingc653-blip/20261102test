package com.bytter.finance.domain.security.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 资产证券化发行表
 * @date 2022-08-12
 */
@Entity
@Table(name = "CMS_SECURITY_ISSUE", schema = "")
public class CmsSecurityIssue extends IdEntity implements java.io.Serializable {
    /**
     * 登记主信息表id
     */
    private String securityInfoId;
    /**
     * 证券发行编号
     */
    private String securityIssueCode;
    /**
     * 批次
     */
    private String batchNo;
    /**
     * 层级
     */
    private String levelName;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 发行金额
     */
    private BigDecimal issueMoney;
    /**
     * 面值
     */
    private BigDecimal faceValue;
    /**
     * 发行价格
     */
    private BigDecimal issuePrice;
    /**
     * 本次发行金额
     */
    private BigDecimal thisIssueMoney;
    /**
     * 发行日期
     */
    private Date issueDate;
    /**
     * 发行折合人名币金额
     */
    private BigDecimal issueRmbMoney;
    /**
     * 预期到期日
     */
    private Date expectEndDate;
    /**
     * 法定到期日
     */
    private Date legalEndDate;
    /**
     * 期限
     */
    private BigDecimal timeLimit;
    /**
     * 还款方式
     */
    private String repayType;
    /**
     * 发行方式
     */
    private String issueWay;
    /**
     * 发行市场
     */
    private String issueMarket;
    /**
     * 出表比例
     */
    private BigDecimal tablePortion;
    /**
     * 信用评级
     */
    private String creditRating;
    /**
     * 信用评级机构
     */
    private String creditRatingIns;
    /**
     * 主承销商
     */
    private String underwriter;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 剩余应到账金额
     */
    private BigDecimal reToAccountMoney;
    /**
     * 息票类型
     */
    private String couponType;
    /**
     * 浮动/固定
     */
    private String isFloat;
    /**
     * 票面利率
     */
    private BigDecimal parRate;
    /**
     * 利息年度天数
     */
    private String rateBase;
    /**
     * 起息日
     */
    private Date startDate;
    /**
     * 利息分配方式
     */
    private String interestWay;
    /**
     * 付息频率
     */
    private String interestFre;
    /**
     * 首次付息日
     */
    private Date beginPayinterestDate;
    /**
     * 本金支付频率
     */
    private String paymoneyFre;
    /**
     * 首次本金支付日
     */
    private Date beginPaymoneyDate;
    /**
     * 定价基准
     */
    private String referenceRateType;
    /**
     * 利率期限
     */
    private String repayCycle;
    /**
     * 报价日期
     */
    private Date quoDate;
    /**
     * 基准利差
     */
    private BigDecimal pointsProportionText;
    /**
     * 首次基准利率
     */
    private BigDecimal baseRate;
    /**
     * 利率上限
     */
    private BigDecimal upperRate;
    /**
     * 利率下限
     */
    private BigDecimal lowestRate;
    /**
     * 利率备注
     */
    private String rateRmk;
    /**
     * 基准利率确定规则
     */
    private String baseRateRules;
    /**
     * 是否挂保证金
     */
    private String isHookBond;
    /**
     * 保证金账户
     */
    private String bondBankacc;
    /**
     * 保证金币种
     */
    private String bondCurId;
    /**
     * 保证金比例
     */
    private BigDecimal bailmoneyRate;
    /**
     * 保证金原币金额
     */
    private BigDecimal oriBailmoney;
    /**
     * 保证金人名币金额
     */
    private BigDecimal rmbBailmoney;
    /**
     * 保证金利率
     */
    private BigDecimal bondRate;
    /**
     * 保证金合同
     */
    private String bailmoneyContract;
    /**
     * 是否挂担保
     */
    private String isHangGuarantee;
    /**
     * 担保合同编号
     */
    private String guaContract;
    /**
     * 担保人
     */
    private String guaMan;
    /**
     * 担保方式
     */
    private String guaWay;
    /**
     * 担保备注
     */
    private String guaRmk;
    /**
     * 是否挂抵质押物
     */
    private String isRelatePledge;
    /**
     * 附件ID
     */
    private String uploadId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    @Column(name = "SECURITY_INFO_ID")
    public String getSecurityInfoId() {
        return securityInfoId;
    }
    public void setSecurityInfoId(String securityInfoId) {
        this.securityInfoId = securityInfoId;
    }
    @Column(name = "SECURITY_ISSUE_CODE")
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    @Column(name = "BATCH_NO")
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    @Column(name = "LEVEL_NAME")
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    @Column(name = "PRODUCT_NAME")
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    @Column(name = "ISSUE_MONEY")
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
    }
    @Column(name = "FACE_VALUE")
    public BigDecimal getFaceValue() {
        return faceValue;
    }
    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }
    @Column(name = "ISSUE_PRICE")
    public BigDecimal getIssuePrice() {
        return issuePrice;
    }
    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }
    @Column(name = "THIS_ISSUE_MONEY")
    public BigDecimal getThisIssueMoney() {
        return thisIssueMoney;
    }
    public void setThisIssueMoney(BigDecimal thisIssueMoney) {
        this.thisIssueMoney = thisIssueMoney;
    }
    @Column(name = "ISSUE_DATE")
    public Date getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    @Column(name = "ISSUE_RMB_MONEY")
    public BigDecimal getIssueRmbMoney() {
        return issueRmbMoney;
    }
    public void setIssueRmbMoney(BigDecimal issueRmbMoney) {
        this.issueRmbMoney = issueRmbMoney;
    }
    @Column(name = "EXPECT_END_DATE")
    public Date getExpectEndDate() {
        return expectEndDate;
    }
    public void setExpectEndDate(Date expectEndDate) {
        this.expectEndDate = expectEndDate;
    }
    @Column(name = "LEGAL_END_DATE")
    public Date getLegalEndDate() {
        return legalEndDate;
    }
    public void setLegalEndDate(Date legalEndDate) {
        this.legalEndDate = legalEndDate;
    }
    @Column(name = "TIME_LIMIT")
    public BigDecimal getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(BigDecimal timeLimit) {
        this.timeLimit = timeLimit;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "ISSUE_WAY")
    public String getIssueWay() {
        return issueWay;
    }
    public void setIssueWay(String issueWay) {
        this.issueWay = issueWay;
    }
    @Column(name = "ISSUE_MARKET")
    public String getIssueMarket() {
        return issueMarket;
    }
    public void setIssueMarket(String issueMarket) {
        this.issueMarket = issueMarket;
    }
    @Column(name = "TABLE_PROPORTION")
    public BigDecimal getTablePortion() {
        return tablePortion;
    }
    public void setTablePortion(BigDecimal tablePortion) {
        this.tablePortion = tablePortion;
    }
    @Column(name = "CREDIT_RATING")
    public String getCreditRating() {
        return creditRating;
    }
    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }
    @Column(name = "CREDIT_RATING_INS")
    public String getCreditRatingIns() {
        return creditRatingIns;
    }
    public void setCreditRatingIns(String creditRatingIns) {
        this.creditRatingIns = creditRatingIns;
    }
    @Column(name = "UNDERWRITER")
    public String getUnderwriter() {
        return underwriter;
    }
    public void setUnderwriter(String underwriter) {
        this.underwriter = underwriter;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "RE_TO_ACCOUNT_MONEY")
    public BigDecimal getReToAccountMoney() {
        return reToAccountMoney;
    }
    public void setReToAccountMoney(BigDecimal reToAccountMoney) {
        this.reToAccountMoney = reToAccountMoney;
    }
    @Column(name = "COUPON_TYPE")
    public String getCouponType() {
        return couponType;
    }
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
    @Column(name = "IS_FLOAT")
    public String getIsFloat() {
        return isFloat;
    }
    public void setIsFloat(String isFloat) {
        this.isFloat = isFloat;
    }
    @Column(name = "PAR_RATE")
    public BigDecimal getParRate() {
        return parRate;
    }
    public void setParRate(BigDecimal parRate) {
        this.parRate = parRate;
    }
    @Column(name = "RATE_BASE")
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    @Column(name = "INTEREST_FRE")
    public String getInterestFre() {
        return interestFre;
    }
    public void setInterestFre(String interestFre) {
        this.interestFre = interestFre;
    }
    @Column(name = "BEGIN_PAYINTEREST_DATE")
    public Date getBeginPayinterestDate() {
        return beginPayinterestDate;
    }
    public void setBeginPayinterestDate(Date beginPayinterestDate) {
        this.beginPayinterestDate = beginPayinterestDate;
    }
    @Column(name = "PAYMONEY_FRE")
    public String getPaymoneyFre() {
        return paymoneyFre;
    }
    public void setPaymoneyFre(String paymoneyFre) {
        this.paymoneyFre = paymoneyFre;
    }
    @Column(name = "BEGIN_PAYMONEY_DATE")
    public Date getBeginPaymoneyDate() {
        return beginPaymoneyDate;
    }
    public void setBeginPaymoneyDate(Date beginPaymoneyDate) {
        this.beginPaymoneyDate = beginPaymoneyDate;
    }
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    @Column(name = "QUO_DATE")
    public Date getQuoDate() {
        return quoDate;
    }
    public void setQuoDate(Date quoDate) {
        this.quoDate = quoDate;
    }
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    @Column(name = "BASE_RATE")
    public BigDecimal getBaseRate() {
        return baseRate;
    }
    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }
    @Column(name = "UPPER_RATE")
    public BigDecimal getUpperRate() {
        return upperRate;
    }
    public void setUpperRate(BigDecimal upperRate) {
        this.upperRate = upperRate;
    }
    @Column(name = "LOWEST_RATE")
    public BigDecimal getLowestRate() {
        return lowestRate;
    }
    public void setLowestRate(BigDecimal lowestRate) {
        this.lowestRate = lowestRate;
    }
    @Column(name = "RATE_RMK")
    public String getRateRmk() {
        return rateRmk;
    }
    public void setRateRmk(String rateRmk) {
        this.rateRmk = rateRmk;
    }
    @Column(name = "BASE_RATE_RULES")
    public String getBaseRateRules() {
        return baseRateRules;
    }
    public void setBaseRateRules(String baseRateRules) {
        this.baseRateRules = baseRateRules;
    }
    @Column(name = "IS_HOOK_BOND")
    public String getIsHookBond() {
        return isHookBond;
    }
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    @Column(name = "BOND_BANKACC")
    public String getBondBankacc() {
        return bondBankacc;
    }
    public void setBondBankacc(String bondBankacc) {
        this.bondBankacc = bondBankacc;
    }
    @Column(name = "BOND_CUR_ID")
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    @Column(name = "BAILMONEY_RATE")
    public BigDecimal getBailmoneyRate() {
        return bailmoneyRate;
    }
    public void setBailmoneyRate(BigDecimal bailmoneyRate) {
        this.bailmoneyRate = bailmoneyRate;
    }
    @Column(name = "ORI_BAILMONEY")
    public BigDecimal getOriBailmoney() {
        return oriBailmoney;
    }
    public void setOriBailmoney(BigDecimal oriBailmoney) {
        this.oriBailmoney = oriBailmoney;
    }
    @Column(name = "RMB_BAILMONEY")
    public BigDecimal getRmbBailmoney() {
        return rmbBailmoney;
    }
    public void setRmbBailmoney(BigDecimal rmbBailmoney) {
        this.rmbBailmoney = rmbBailmoney;
    }
    @Column(name = "BOND_RATE")
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    @Column(name = "BAILMONEY_CONTRACT")
    public String getBailmoneyContract() {
        return bailmoneyContract;
    }
    public void setBailmoneyContract(String bailmoneyContract) {
        this.bailmoneyContract = bailmoneyContract;
    }
    @Column(name = "IS_HANG_GUARANTEE")
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    @Column(name = "GUA_CONTRACT")
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    @Column(name = "GUA_MAN")
    public String getGuaMan() {
        return guaMan;
    }
    public void setGuaMan(String guaMan) {
        this.guaMan = guaMan;
    }
    @Column(name = "GUA_WAY")
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    @Column(name = "GUA_RMK")
    public String getGuaRmk() {
        return guaRmk;
    }
    public void setGuaRmk(String guaRmk) {
        this.guaRmk = guaRmk;
    }
    @Column(name = "IS_RELATE_PLEDGE")
    public String getIsRelatePledge() {
        return isRelatePledge;
    }
    public void setIsRelatePledge(String isRelatePledge) {
        this.isRelatePledge = isRelatePledge;
    }
    @Column(name = "UPLOAD_ID")
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
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
}
