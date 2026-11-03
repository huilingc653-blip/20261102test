package com.bytter.finance.domain.security.vo;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 资产证券化发行表
 *
 * @author tongqr
 */
public class CmsSecurityIssueVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 登记主信息表id
     */
    private String securityInfoId;
    /**
     * 证券发行编号
     */
    @BtCompositeSearch(fieldName = "iss.SECURITY_ISSUE_CODE")
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
    private String guaranteeRmk;
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
    /**
     * 第三方费用数据
     */
    private List<DfIntermediateCost> costTypeDtl;
    /**
     * 抵质押物id
     */
    private String[] pledgeId;
    /**
     * 占用抵质押物额度
     */
    private BigDecimal[] pledgeMoney;
    /**
     * 原始权益人
     */
//    @Corp(inject = "corpName")
    private String corpId;
    /**
     * 单位名称
     */
    private String corpName;
    /**
     * 证券原币币种
     */
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 计划
     */
    private String planId;
    /**
     * 保证金id
     */
    private String bondId;
    /**
     * 上一笔保证金id
     */
    private String bondIdPre;
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSecurityInfoId() {
        return securityInfoId;
    }
    public void setSecurityInfoId(String securityInfoId) {
        this.securityInfoId = securityInfoId;
    }
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
    }
    public BigDecimal getFaceValue() {
        return faceValue;
    }
    public void setFaceValue(BigDecimal faceValue) {
        this.faceValue = faceValue;
    }
    public BigDecimal getIssuePrice() {
        return issuePrice;
    }
    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }
    public BigDecimal getThisIssueMoney() {
        return thisIssueMoney;
    }
    public void setThisIssueMoney(BigDecimal thisIssueMoney) {
        this.thisIssueMoney = thisIssueMoney;
    }
    public Date getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    public BigDecimal getIssueRmbMoney() {
        return issueRmbMoney;
    }
    public void setIssueRmbMoney(BigDecimal issueRmbMoney) {
        this.issueRmbMoney = issueRmbMoney;
    }
    public Date getExpectEndDate() {
        return expectEndDate;
    }
    public void setExpectEndDate(Date expectEndDate) {
        this.expectEndDate = expectEndDate;
    }
    public Date getLegalEndDate() {
        return legalEndDate;
    }
    public void setLegalEndDate(Date legalEndDate) {
        this.legalEndDate = legalEndDate;
    }
    public BigDecimal getTimeLimit() {
        return timeLimit;
    }
    public void setTimeLimit(BigDecimal timeLimit) {
        this.timeLimit = timeLimit;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getIssueWay() {
        return issueWay;
    }
    public void setIssueWay(String issueWay) {
        this.issueWay = issueWay;
    }
    public String getIssueMarket() {
        return issueMarket;
    }
    public void setIssueMarket(String issueMarket) {
        this.issueMarket = issueMarket;
    }
    public BigDecimal getTablePortion() {
        return tablePortion;
    }
    public void setTablePortion(BigDecimal tablePortion) {
        this.tablePortion = tablePortion;
    }
    public String getCreditRating() {
        return creditRating;
    }
    public void setCreditRating(String creditRating) {
        this.creditRating = creditRating;
    }
    public String getCreditRatingIns() {
        return creditRatingIns;
    }
    public void setCreditRatingIns(String creditRatingIns) {
        this.creditRatingIns = creditRatingIns;
    }
    public String getUnderwriter() {
        return underwriter;
    }
    public void setUnderwriter(String underwriter) {
        this.underwriter = underwriter;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public BigDecimal getReToAccountMoney() {
        return reToAccountMoney;
    }
    public void setReToAccountMoney(BigDecimal reToAccountMoney) {
        this.reToAccountMoney = reToAccountMoney;
    }
    public String getCouponType() {
        return couponType;
    }
    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }
    public String getIsFloat() {
        return isFloat;
    }
    public void setIsFloat(String isFloat) {
        this.isFloat = isFloat;
    }
    public BigDecimal getParRate() {
        return parRate;
    }
    public void setParRate(BigDecimal parRate) {
        this.parRate = parRate;
    }
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getInterestFre() {
        return interestFre;
    }
    public void setInterestFre(String interestFre) {
        this.interestFre = interestFre;
    }
    public Date getBeginPayinterestDate() {
        return beginPayinterestDate;
    }
    public void setBeginPayinterestDate(Date beginPayinterestDate) {
        this.beginPayinterestDate = beginPayinterestDate;
    }
    public String getPaymoneyFre() {
        return paymoneyFre;
    }
    public void setPaymoneyFre(String paymoneyFre) {
        this.paymoneyFre = paymoneyFre;
    }
    public Date getBeginPaymoneyDate() {
        return beginPaymoneyDate;
    }
    public void setBeginPaymoneyDate(Date beginPaymoneyDate) {
        this.beginPaymoneyDate = beginPaymoneyDate;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public Date getQuoDate() {
        return quoDate;
    }
    public void setQuoDate(Date quoDate) {
        this.quoDate = quoDate;
    }
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    public BigDecimal getBaseRate() {
        return baseRate;
    }
    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }
    public BigDecimal getUpperRate() {
        return upperRate;
    }
    public void setUpperRate(BigDecimal upperRate) {
        this.upperRate = upperRate;
    }
    public BigDecimal getLowestRate() {
        return lowestRate;
    }
    public void setLowestRate(BigDecimal lowestRate) {
        this.lowestRate = lowestRate;
    }
    public String getRateRmk() {
        return rateRmk;
    }
    public void setRateRmk(String rateRmk) {
        this.rateRmk = rateRmk;
    }
    public String getBaseRateRules() {
        return baseRateRules;
    }
    public void setBaseRateRules(String baseRateRules) {
        this.baseRateRules = baseRateRules;
    }
    public String getIsHookBond() {
        return isHookBond;
    }
    public void setIsHookBond(String isHookBond) {
        this.isHookBond = isHookBond;
    }
    public String getBondBankacc() {
        return bondBankacc;
    }
    public void setBondBankacc(String bondBankacc) {
        this.bondBankacc = bondBankacc;
    }
    public String getBondCurId() {
        return bondCurId;
    }
    public void setBondCurId(String bondCurId) {
        this.bondCurId = bondCurId;
    }
    public BigDecimal getBailmoneyRate() {
        return bailmoneyRate;
    }
    public void setBailmoneyRate(BigDecimal bailmoneyRate) {
        this.bailmoneyRate = bailmoneyRate;
    }
    public BigDecimal getOriBailmoney() {
        return oriBailmoney;
    }
    public void setOriBailmoney(BigDecimal oriBailmoney) {
        this.oriBailmoney = oriBailmoney;
    }
    public BigDecimal getRmbBailmoney() {
        return rmbBailmoney;
    }
    public void setRmbBailmoney(BigDecimal rmbBailmoney) {
        this.rmbBailmoney = rmbBailmoney;
    }
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    public String getBailmoneyContract() {
        return bailmoneyContract;
    }
    public void setBailmoneyContract(String bailmoneyContract) {
        this.bailmoneyContract = bailmoneyContract;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getGuaMan() {
        return guaMan;
    }
    public void setGuaMan(String guaMan) {
        this.guaMan = guaMan;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    public String getIsRelatePledge() {
        return isRelatePledge;
    }
    public void setIsRelatePledge(String isRelatePledge) {
        this.isRelatePledge = isRelatePledge;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
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
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    public List<DfIntermediateCost> getCostTypeDtl() {
        return costTypeDtl;
    }
    public void setCostTypeDtl(List<DfIntermediateCost> costTypeDtl) {
        this.costTypeDtl = costTypeDtl;
    }
    public String[] getPledgeId() {
        return pledgeId;
    }
    public void setPledgeId(String[] pledgeId) {
        this.pledgeId = pledgeId;
    }
    public BigDecimal[] getPledgeMoney() {
        return pledgeMoney;
    }
    public void setPledgeMoney(BigDecimal[] pledgeMoney) {
        this.pledgeMoney = pledgeMoney;
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
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getBondId() {
        return bondId;
    }
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    public String getBondIdPre() {
        return bondIdPre;
    }
    public void setBondIdPre(String bondIdPre) {
        this.bondIdPre = bondIdPre;
    }
}
