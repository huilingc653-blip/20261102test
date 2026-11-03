package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 债券发行实体
 *
 * @author ouyj
 */
@Entity
@Table(name = "CMS_BOND_ISSUE_INFO")
@SuppressWarnings("serial")
public class CmsBondIssueInfo extends IdEntity implements Serializable {
    /**
     * 债券注册id
     */
    private String registerId;
    /**
     * 债券发行编号
     */
    private String bondIssueNum;
    /**
     * 批次
     */
    private String batchNo;
    /**
     * 主承销商
     */
    private String leadUnderwriter;
    /**
     * 发行金额
     */
    private BigDecimal issueMoney;
    /**
     * 实际发行总额（折合人民币）
     */
    private BigDecimal rmbIssueTotal;
    /**
     * 起息日
     */
    private Date startDate;
    /**
     * 到期兑付日
     */
    private Date dueDate;
    /**
     * 还款方式
     */
    private String repayType;
    /**
     * 发行开始日
     */
    private Date issueStartDate;
    /**
     * 发行结束日
     */
    private Date issueEndDate;
    /**
     * 分销开始日
     */
    private Date assignStartDate;
    /**
     * 分销结束日
     */
    private Date assignEndDate;
    /**
     * 公告日
     */
    private Date announcementDate;
    /**
     * 承销缴款日
     */
    private Date paymentDate;
    /**
     * 发行价格
     */
    private BigDecimal issuePrice;
    /**
     * 发行范围
     */
    private String issueScope;
    /**
     * 发行备注
     */
    private String issueRmk;
    /**
     * 计息方式
     */
    private String calcuInterestType;
    /**
     * 浮动/固定
     */
    private String isFloat;
    /**
     * 报价日期
     */
    private Date quoteDate;
    /**
     * 利息年度天数
     */
    private String interestYearDays;
    /**
     * 是否闰年
     */
    private String isLeapYear;
    /**
     * 闰年是否计息
     */
    private String leapIsInterest;
    /**
     * 计息实际天数
     */
    private String interestDays;
    /**
     * 定价基准
     */
    private String fixPriceBase;
    /**
     * 利率期限
     */
    private String rateDeadline;
    /**
     * 基准利率
     */
    private BigDecimal baseRate;
    /**
     * 比例%/bp点数
     */
    private BigDecimal basicPoint;
    /**
     * 票面利率
     */
    private BigDecimal faceValueRate;
    /**
     * 付息日
     */
    private Date payDate;
    /**
     * 付息频率
     */
    private String payFrequency;
    /**
     * 利息分配方式
     */
    private String assignType;
    /**
     * 发行时利差
     */
    private BigDecimal spreadIssue;
    /**
     * 利率上限
     */
    private BigDecimal upperLimitRate;
    /**
     * 保底利率
     */
    private BigDecimal lowestRate;
    /**
     * 基准利率生效方式
     */
    private String rateEffectType;
    /**
     * 生效时期
     */
    private String effectPeriod;
    /**
     * 生效条件
     */
    private String effectCondition;
    /**
     * 利率信息说明
     */
    private String interestRmk;
    /**
     * 是否挂接保证金
     */
    private String isHookBail;
    /**
     * 保证金币种
     */
    private String bailCurId;
    /**
     * 保证金账户
     */
    private String bailBankAcc;
    /**
     * 是否挂接担保
     */
    private String isHookGuarantee;
    /**
     * 担保备注
     */
    private String guaranteeRmk;
    /**
     * 担保方式
     */
    private String guaranteeType;
    /**
     * 担保合同
     */
    private String guaranteeContract;
    /**
     * 是否挂接抵质押物
     */
    private String isHookPledge;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 是否删除
     */
    private String isDelete;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 业务状态
     */
    private String bizStatus;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 原币保证金金额
     */
    private BigDecimal oriBailMoney;
    /**
     * 人民币保证金金额
     */
    private BigDecimal rmbBailMoney;
    /**
     * 保证金比例
     */
    private BigDecimal bailMoneyRate;
    /**
     * 担保人
     */
    private String guaranteeMan;
    /**
     * 保证金合同
     */
    private String bailMoneyContract;
    /**
     * 保证金利率
     */
    private BigDecimal bondRate;
    @Column(name = "BOND_RATE")
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    @Column(name = "BAIL_MONEY_CONTRACT")
    public String getBailMoneyContract() {
        return bailMoneyContract;
    }
    public void setBailMoneyContract(String bailMoneyContract) {
        this.bailMoneyContract = bailMoneyContract;
    }
    @Column(name = "ORI_BAIL_MONEY")
    public BigDecimal getOriBailMoney() {
        return oriBailMoney;
    }
    public void setOriBailMoney(BigDecimal oriBailMoney) {
        this.oriBailMoney = oriBailMoney;
    }
    @Column(name = "RMB_BAIL_MONEY")
    public BigDecimal getRmbBailMoney() {
        return rmbBailMoney;
    }
    public void setRmbBailMoney(BigDecimal rmbBailMoney) {
        this.rmbBailMoney = rmbBailMoney;
    }
    @Column(name = "BAIL_MONEY_RATE")
    public BigDecimal getBailMoneyRate() {
        return bailMoneyRate;
    }
    public void setBailMoneyRate(BigDecimal bailMoneyRate) {
        this.bailMoneyRate = bailMoneyRate;
    }
    @Column(name = "GUARANTEE_MAN")
    public String getGuaranteeMan() {
        return guaranteeMan;
    }
    public void setGuaranteeMan(String guaranteeMan) {
        this.guaranteeMan = guaranteeMan;
    }
    @Column(name = "REGISTER_ID")
    public String getRegisterId() {
        return registerId;
    }
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
    @Column(name = "BOND_ISSUE_NUM")
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
    }
    @Column(name = "BATCH_NO")
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    @Column(name = "LEAD_UNDERWRITER")
    public String getLeadUnderwriter() {
        return leadUnderwriter;
    }
    public void setLeadUnderwriter(String leadUnderwriter) {
        this.leadUnderwriter = leadUnderwriter;
    }
    @Column(name = "ISSUE_MONEY")
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
    }
    @Column(name = "RMB_ISSUE_TOTAL")
    public BigDecimal getRmbIssueTotal() {
        return rmbIssueTotal;
    }
    public void setRmbIssueTotal(BigDecimal rmbIssueTotal) {
        this.rmbIssueTotal = rmbIssueTotal;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "DUE_DATE")
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "ISSUE_START_DATE")
    public Date getIssueStartDate() {
        return issueStartDate;
    }
    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }
    @Column(name = "ISSUE_END_DATE")
    public Date getIssueEndDate() {
        return issueEndDate;
    }
    public void setIssueEndDate(Date issueEndDate) {
        this.issueEndDate = issueEndDate;
    }
    @Column(name = "ASSIGN_START_DATE")
    public Date getAssignStartDate() {
        return assignStartDate;
    }
    public void setAssignStartDate(Date assignStartDate) {
        this.assignStartDate = assignStartDate;
    }
    @Column(name = "ASSIGN_END_DATE")
    public Date getAssignEndDate() {
        return assignEndDate;
    }
    public void setAssignEndDate(Date assignEndDate) {
        this.assignEndDate = assignEndDate;
    }
    @Column(name = "ANNOUNCEMENT_DATE")
    public Date getAnnouncementDate() {
        return announcementDate;
    }
    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }
    @Column(name = "PAYMENT_DATE")
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    @Column(name = "ISSUE_PRICE")
    public BigDecimal getIssuePrice() {
        return issuePrice;
    }
    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }
    @Column(name = "ISSUE_SCOPE")
    public String getIssueScope() {
        return issueScope;
    }
    public void setIssueScope(String issueScope) {
        this.issueScope = issueScope;
    }
    @Column(name = "ISSUE_RMK")
    public String getIssueRmk() {
        return issueRmk;
    }
    public void setIssueRmk(String issueRmk) {
        this.issueRmk = issueRmk;
    }
    @Column(name = "CALCU_INTEREST_TYPE")
    public String getCalcuInterestType() {
        return calcuInterestType;
    }
    public void setCalcuInterestType(String calcuInterestType) {
        this.calcuInterestType = calcuInterestType;
    }
    @Column(name = "IS_FLOAT")
    public String getIsFloat() {
        return isFloat;
    }
    public void setIsFloat(String isFloat) {
        this.isFloat = isFloat;
    }
    @Column(name = "QUOTE_DATE")
    public Date getQuoteDate() {
        return quoteDate;
    }
    public void setQuoteDate(Date quoteDate) {
        this.quoteDate = quoteDate;
    }
    @Column(name = "INTEREST_YEAR_DAYS")
    public String getInterestYearDays() {
        return interestYearDays;
    }
    public void setInterestYearDays(String interestYearDays) {
        this.interestYearDays = interestYearDays;
    }
    @Column(name = "IS_LEAP_YEAR")
    public String getIsLeapYear() {
        return isLeapYear;
    }
    public void setIsLeapYear(String isLeapYear) {
        this.isLeapYear = isLeapYear;
    }
    @Column(name = "LEAP_IS_INTEREST")
    public String getLeapIsInterest() {
        return leapIsInterest;
    }
    public void setLeapIsInterest(String leapIsInterest) {
        this.leapIsInterest = leapIsInterest;
    }
    @Column(name = "INTEREST_DAYS")
    public String getInterestDays() {
        return interestDays;
    }
    public void setInterestDays(String interestDays) {
        this.interestDays = interestDays;
    }
    @Column(name = "FIX_PRICE_BASE")
    public String getFixPriceBase() {
        return fixPriceBase;
    }
    public void setFixPriceBase(String fixPriceBase) {
        this.fixPriceBase = fixPriceBase;
    }
    @Column(name = "RATE_DEADLINE")
    public String getRateDeadline() {
        return rateDeadline;
    }
    public void setRateDeadline(String rateDeadline) {
        this.rateDeadline = rateDeadline;
    }
    @Column(name = "BASE_RATE")
    public BigDecimal getBaseRate() {
        return baseRate;
    }
    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }
    @Column(name = "BASIC_POINT")
    public BigDecimal getBasicPoint() {
        return basicPoint;
    }
    public void setBasicPoint(BigDecimal basicPoint) {
        this.basicPoint = basicPoint;
    }
    @Column(name = "FACE_VALUE_RATE")
    public BigDecimal getFaceValueRate() {
        return faceValueRate;
    }
    public void setFaceValueRate(BigDecimal faceValueRate) {
        this.faceValueRate = faceValueRate;
    }
    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    @Column(name = "PAY_FREQUENCY")
    public String getPayFrequency() {
        return payFrequency;
    }
    public void setPayFrequency(String payFrequency) {
        this.payFrequency = payFrequency;
    }
    @Column(name = "ASSIGN_TYPE")
    public String getAssignType() {
        return assignType;
    }
    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }
    @Column(name = "SPREAD_ISSUE")
    public BigDecimal getSpreadIssue() {
        return spreadIssue;
    }
    public void setSpreadIssue(BigDecimal spreadIssue) {
        this.spreadIssue = spreadIssue;
    }
    @Column(name = "UPPER_LIMIT_RATE")
    public BigDecimal getUpperLimitRate() {
        return upperLimitRate;
    }
    public void setUpperLimitRate(BigDecimal upperLimitRate) {
        this.upperLimitRate = upperLimitRate;
    }
    @Column(name = "LOWEST_RATE")
    public BigDecimal getLowestRate() {
        return lowestRate;
    }
    public void setLowestRate(BigDecimal lowestRate) {
        this.lowestRate = lowestRate;
    }
    @Column(name = "RATE_EFFECT_TYPE")
    public String getRateEffectType() {
        return rateEffectType;
    }
    public void setRateEffectType(String rateEffectType) {
        this.rateEffectType = rateEffectType;
    }
    @Column(name = "EFFECT_PERIOD")
    public String getEffectPeriod() {
        return effectPeriod;
    }
    public void setEffectPeriod(String effectPeriod) {
        this.effectPeriod = effectPeriod;
    }
    @Column(name = "EFFECT_CONDITION")
    public String getEffectCondition() {
        return effectCondition;
    }
    public void setEffectCondition(String effectCondition) {
        this.effectCondition = effectCondition;
    }
    @Column(name = "INTEREST_RMK")
    public String getInterestRmk() {
        return interestRmk;
    }
    public void setInterestRmk(String interestRmk) {
        this.interestRmk = interestRmk;
    }
    @Column(name = "IS_HOOK_BAIL")
    public String getIsHookBail() {
        return isHookBail;
    }
    public void setIsHookBail(String isHookBail) {
        this.isHookBail = isHookBail;
    }
    @Column(name = "BAIL_CUR_ID")
    public String getBailCurId() {
        return bailCurId;
    }
    public void setBailCurId(String bailCurId) {
        this.bailCurId = bailCurId;
    }
    @Column(name = "BAIL_BANK_ACC")
    public String getBailBankAcc() {
        return bailBankAcc;
    }
    public void setBailBankAcc(String bailBankAcc) {
        this.bailBankAcc = bailBankAcc;
    }
    @Column(name = "IS_HOOK_GUARANTEE")
    public String getIsHookGuarantee() {
        return isHookGuarantee;
    }
    public void setIsHookGuarantee(String isHookGuarantee) {
        this.isHookGuarantee = isHookGuarantee;
    }
    @Column(name = "GUARANTEE_RMK")
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    @Column(name = "GUARANTEE_TYPE")
    public String getGuaranteeType() {
        return guaranteeType;
    }
    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }
    @Column(name = "GUARANTEE_CONTRACT")
    public String getGuaranteeContract() {
        return guaranteeContract;
    }
    public void setGuaranteeContract(String guaranteeContract) {
        this.guaranteeContract = guaranteeContract;
    }
    @Column(name = "IS_HOOK_PLEDGE")
    public String getIsHookPledge() {
        return isHookPledge;
    }
    public void setIsHookPledge(String isHookPledge) {
        this.isHookPledge = isHookPledge;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "IS_DELETE")
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
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
