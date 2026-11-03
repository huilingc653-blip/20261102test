package com.bytter.finance.domain.bond.vo;
import com.bytter.finance.domain.directFinance.entity.DfIntermediateCost;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 债券发行vo
 *
 * @author ouyj
 */
@SuppressWarnings("serial")
public class CmsBondIssueInfoVo implements Serializable, IBusinessKey {
    private String id;
    /**
     * 债券注册id
     */
    private String registerId;
    /**
     * 债券发行编号
     */
    @BtCompositeSearch(fieldName = "i.BOND_ISSUE_NUM")
    private String bondIssueNum;
    /**
     * 批次
     */
    @BtCompositeSearch(fieldName = "i.BATCH_NO")
    private String batchNo;
    /**
     * 主承销商
     */
    @BtCompositeSearch(fieldName = "i.LEAD_UNDERWRITER")
    private String leadUnderwriter;
    /**
     * 发行金额
     */
    @BtCompositeSearch(fieldName = "i.ISSUE_MONEY")
    private BigDecimal issueMoney;
    /**
     * 未还本金
     */
    private BigDecimal unpaidMoney;
    /**
     * 实际发行总额（折合人民币）
     */
    @BtCompositeSearch(fieldName = "i.RMB_ISSUE_TOTAL")
    private BigDecimal rmbIssueTotal;
    /**
     * 起息日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.START_DATE")
    private Date startDate;
    /**
     * 到期兑付日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.DUE_DATE")
    private Date dueDate;
    /**
     * 还款方式
     */
    private String repayType;
    /**
     * 发行开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.ISSUE_START_DATE")
    private Date issueStartDate;
    /**
     * 发行结束日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.ISSUE_END_DATE")
    private Date issueEndDate;
    /**
     * 分销开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date assignStartDate;
    /**
     * 分销结束日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date assignEndDate;
    /**
     * 公告日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date announcementDate;
    /**
     * 承销缴款日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date paymentDate;
    /**
     * 发行价格
     */
    @BtCompositeSearch(fieldName = "i.ISSUE_PRICE")
    private BigDecimal issuePrice;
    /**
     * 发行范围
     */
    private String issueScope;
    /**
     * 发行备注
     */
    @BtCompositeSearch(fieldName = "i.ISSUE_RMK")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 保证金id
     */
    private String bondId;
    /**
     * 上一笔保证金id
     */
    private String bondIdPre;
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
     * 保证金利率
     */
    private BigDecimal bondRate;
    /**
     * 发行人/债务人
     */
    @BtCompositeSearch(fieldName = "r.corp_id")
    private String corpId;
    /**
     * 单位名称
     */
    @BtCompositeSearch(fieldName = "r.corp_name")
    private String corpName;
    /**
     * 债券类型id
     */
    @BtCompositeSearch(fieldName = "r.type_id")
    private String typeId;
    /**
     * 债券类型名
     */
    @BtCompositeSearch(fieldName = "r.type_id")
    private String bondTypeName;
    /**
     * 发行批文名称
     */
    private String issueName;
    /**
     * 债券代码
     */
    @BtCompositeSearch(fieldName = "r.bond_code")
    private String bondCode;
    /**
     * 批准文号
     */
    private String approvalNumber;
    /**
     * 债券全称
     */
    @BtCompositeSearch(fieldName = "r.bond_full_name")
    private String bondFullName;
    /**
     * 债券简称
     */
    @BtCompositeSearch(fieldName = "r.bond_short_name")
    private String bondShortName;
    /**
     * 债券期限
     */
    @BtCompositeSearch(fieldName = "r.bond_deadline")
    private Integer bondDeadline;
    /**
     * 债券期限单位
     */
    private String deadlineUnit;
    /**
     * 债券原币币种
     */
//    @Currency(inject = "bondCurName")
    @BtCompositeSearch(fieldName = "r.bond_cur")
    private String bondCur;
    @BtCompositeSearch(fieldName = "r.bond_cur")
    private String bondCurName;
    /**
     * 计划发行原币总额
     */
    @BtCompositeSearch(fieldName = "r.issue_cur_total")
    private BigDecimal issueCurTotal;
    /**
     * 计划发行人民币总额
     */
    @BtCompositeSearch(fieldName = "r.issue_rmb_total")
    private BigDecimal issueRmbTotal;
    /**
     * 债券信用评级
     */
    private String bondCreditRate;
    /**
     * 发行人信用评级
     */
    private String debtorCreditRate;
    /**
     * 可转换开始日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date convertStartDate;
    /**
     * 可转换结束日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date convertEndDate;
    /**
     * 转换比例
     */
    private BigDecimal convertRate;
    /**
     * 融资用途
     */
    @BtCompositeSearch(fieldName = "r.purpose")
    private String purpose;
    /**
     * 债券备注
     */
    @BtCompositeSearch(fieldName = "r.rmk")
    private String rmk;
    /**
     * 境内/境外
     */
    private String isTerritory;
    /**
     * 状态
     */
    @Dict(dictGroup = "AB_BILL_EMITBILL_STATUS", inject = "statusName")
    private Integer status;
    /**
     * 状态名
     */
    private String statusName;
    /**
     * 是否保存操作
     */
    private String isSave;
    /**
     * 还款计划状态
     */
    private Integer planStatus;
    /**
     * 还款计划id
     */
    private String planId;
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getBondTypeName() {
        return bondTypeName;
    }
    public void setBondTypeName(String bondTypeName) {
        this.bondTypeName = bondTypeName;
    }
    public String getIssueName() {
        return issueName;
    }
    public void setIssueName(String issueName) {
        this.issueName = issueName;
    }
    public String getBondCode() {
        return bondCode;
    }
    public void setBondCode(String bondCode) {
        this.bondCode = bondCode;
    }
    public String getApprovalNumber() {
        return approvalNumber;
    }
    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }
    public String getBondFullName() {
        return bondFullName;
    }
    public void setBondFullName(String bondFullName) {
        this.bondFullName = bondFullName;
    }
    public String getBondShortName() {
        return bondShortName;
    }
    public void setBondShortName(String bondShortName) {
        this.bondShortName = bondShortName;
    }
    public Integer getBondDeadline() {
        return bondDeadline;
    }
    public void setBondDeadline(Integer bondDeadline) {
        this.bondDeadline = bondDeadline;
    }
    public String getDeadlineUnit() {
        return deadlineUnit;
    }
    public void setDeadlineUnit(String deadlineUnit) {
        this.deadlineUnit = deadlineUnit;
    }
    public String getBondCurName() {
        return bondCurName;
    }
    public void setBondCurName(String bondCurName) {
        this.bondCurName = bondCurName;
    }
    public BigDecimal getIssueCurTotal() {
        return issueCurTotal;
    }
    public void setIssueCurTotal(BigDecimal issueCurTotal) {
        this.issueCurTotal = issueCurTotal;
    }
    public BigDecimal getIssueRmbTotal() {
        return issueRmbTotal;
    }
    public void setIssueRmbTotal(BigDecimal issueRmbTotal) {
        this.issueRmbTotal = issueRmbTotal;
    }
    public String getBondCreditRate() {
        return bondCreditRate;
    }
    public void setBondCreditRate(String bondCreditRate) {
        this.bondCreditRate = bondCreditRate;
    }
    public String getDebtorCreditRate() {
        return debtorCreditRate;
    }
    public void setDebtorCreditRate(String debtorCreditRate) {
        this.debtorCreditRate = debtorCreditRate;
    }
    public Date getConvertStartDate() {
        return convertStartDate;
    }
    public void setConvertStartDate(Date convertStartDate) {
        this.convertStartDate = convertStartDate;
    }
    public Date getConvertEndDate() {
        return convertEndDate;
    }
    public void setConvertEndDate(Date convertEndDate) {
        this.convertEndDate = convertEndDate;
    }
    public BigDecimal getConvertRate() {
        return convertRate;
    }
    public void setConvertRate(BigDecimal convertRate) {
        this.convertRate = convertRate;
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
    public String getIsTerritory() {
        return isTerritory;
    }
    public void setIsTerritory(String isTerritory) {
        this.isTerritory = isTerritory;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public BigDecimal getBondRate() {
        return bondRate;
    }
    public void setBondRate(BigDecimal bondRate) {
        this.bondRate = bondRate;
    }
    public String getBondCur() {
        return bondCur;
    }
    public void setBondCur(String bondCur) {
        this.bondCur = bondCur;
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
    public List<DfIntermediateCost> getCostTypeDtl() {
        return costTypeDtl;
    }
    public void setCostTypeDtl(List<DfIntermediateCost> costTypeDtl) {
        this.costTypeDtl = costTypeDtl;
    }
    public String getBailMoneyContract() {
        return bailMoneyContract;
    }
    public void setBailMoneyContract(String bailMoneyContract) {
        this.bailMoneyContract = bailMoneyContract;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public BigDecimal getOriBailMoney() {
        return oriBailMoney;
    }
    public void setOriBailMoney(BigDecimal oriBailMoney) {
        this.oriBailMoney = oriBailMoney;
    }
    public BigDecimal getRmbBailMoney() {
        return rmbBailMoney;
    }
    public void setRmbBailMoney(BigDecimal rmbBailMoney) {
        this.rmbBailMoney = rmbBailMoney;
    }
    public BigDecimal getBailMoneyRate() {
        return bailMoneyRate;
    }
    public void setBailMoneyRate(BigDecimal bailMoneyRate) {
        this.bailMoneyRate = bailMoneyRate;
    }
    public String getGuaranteeMan() {
        return guaranteeMan;
    }
    public void setGuaranteeMan(String guaranteeMan) {
        this.guaranteeMan = guaranteeMan;
    }
    public String getBondIdPre() {
        return bondIdPre;
    }
    public void setBondIdPre(String bondIdPre) {
        this.bondIdPre = bondIdPre;
    }
    public String getBondId() {
        return bondId;
    }
    public void setBondId(String bondId) {
        this.bondId = bondId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRegisterId() {
        return registerId;
    }
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
    }
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getLeadUnderwriter() {
        return leadUnderwriter;
    }
    public void setLeadUnderwriter(String leadUnderwriter) {
        this.leadUnderwriter = leadUnderwriter;
    }
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
    }
    public BigDecimal getUnpaidMoney() {
        return unpaidMoney;
    }
    public void setUnpaidMoney(BigDecimal unpaidMoney) {
        this.unpaidMoney = unpaidMoney;
    }
    public BigDecimal getRmbIssueTotal() {
        return rmbIssueTotal;
    }
    public void setRmbIssueTotal(BigDecimal rmbIssueTotal) {
        this.rmbIssueTotal = rmbIssueTotal;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public Date getIssueStartDate() {
        return issueStartDate;
    }
    public void setIssueStartDate(Date issueStartDate) {
        this.issueStartDate = issueStartDate;
    }
    public Date getIssueEndDate() {
        return issueEndDate;
    }
    public void setIssueEndDate(Date issueEndDate) {
        this.issueEndDate = issueEndDate;
    }
    public Date getAssignStartDate() {
        return assignStartDate;
    }
    public void setAssignStartDate(Date assignStartDate) {
        this.assignStartDate = assignStartDate;
    }
    public Date getAssignEndDate() {
        return assignEndDate;
    }
    public void setAssignEndDate(Date assignEndDate) {
        this.assignEndDate = assignEndDate;
    }
    public Date getAnnouncementDate() {
        return announcementDate;
    }
    public void setAnnouncementDate(Date announcementDate) {
        this.announcementDate = announcementDate;
    }
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public BigDecimal getIssuePrice() {
        return issuePrice;
    }
    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }
    public String getIssueScope() {
        return issueScope;
    }
    public void setIssueScope(String issueScope) {
        this.issueScope = issueScope;
    }
    public String getIssueRmk() {
        return issueRmk;
    }
    public void setIssueRmk(String issueRmk) {
        this.issueRmk = issueRmk;
    }
    public String getCalcuInterestType() {
        return calcuInterestType;
    }
    public void setCalcuInterestType(String calcuInterestType) {
        this.calcuInterestType = calcuInterestType;
    }
    public String getIsFloat() {
        return isFloat;
    }
    public void setIsFloat(String isFloat) {
        this.isFloat = isFloat;
    }
    public Date getQuoteDate() {
        return quoteDate;
    }
    public void setQuoteDate(Date quoteDate) {
        this.quoteDate = quoteDate;
    }
    public String getInterestYearDays() {
        return interestYearDays;
    }
    public void setInterestYearDays(String interestYearDays) {
        this.interestYearDays = interestYearDays;
    }
    public String getIsLeapYear() {
        return isLeapYear;
    }
    public void setIsLeapYear(String isLeapYear) {
        this.isLeapYear = isLeapYear;
    }
    public String getLeapIsInterest() {
        return leapIsInterest;
    }
    public void setLeapIsInterest(String leapIsInterest) {
        this.leapIsInterest = leapIsInterest;
    }
    public String getInterestDays() {
        return interestDays;
    }
    public void setInterestDays(String interestDays) {
        this.interestDays = interestDays;
    }
    public String getFixPriceBase() {
        return fixPriceBase;
    }
    public void setFixPriceBase(String fixPriceBase) {
        this.fixPriceBase = fixPriceBase;
    }
    public String getRateDeadline() {
        return rateDeadline;
    }
    public void setRateDeadline(String rateDeadline) {
        this.rateDeadline = rateDeadline;
    }
    public BigDecimal getBaseRate() {
        return baseRate;
    }
    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }
    public BigDecimal getBasicPoint() {
        return basicPoint;
    }
    public void setBasicPoint(BigDecimal basicPoint) {
        this.basicPoint = basicPoint;
    }
    public BigDecimal getFaceValueRate() {
        return faceValueRate;
    }
    public void setFaceValueRate(BigDecimal faceValueRate) {
        this.faceValueRate = faceValueRate;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public String getPayFrequency() {
        return payFrequency;
    }
    public void setPayFrequency(String payFrequency) {
        this.payFrequency = payFrequency;
    }
    public String getAssignType() {
        return assignType;
    }
    public void setAssignType(String assignType) {
        this.assignType = assignType;
    }
    public BigDecimal getSpreadIssue() {
        return spreadIssue;
    }
    public void setSpreadIssue(BigDecimal spreadIssue) {
        this.spreadIssue = spreadIssue;
    }
    public BigDecimal getUpperLimitRate() {
        return upperLimitRate;
    }
    public void setUpperLimitRate(BigDecimal upperLimitRate) {
        this.upperLimitRate = upperLimitRate;
    }
    public BigDecimal getLowestRate() {
        return lowestRate;
    }
    public void setLowestRate(BigDecimal lowestRate) {
        this.lowestRate = lowestRate;
    }
    public String getRateEffectType() {
        return rateEffectType;
    }
    public void setRateEffectType(String rateEffectType) {
        this.rateEffectType = rateEffectType;
    }
    public String getEffectPeriod() {
        return effectPeriod;
    }
    public void setEffectPeriod(String effectPeriod) {
        this.effectPeriod = effectPeriod;
    }
    public String getEffectCondition() {
        return effectCondition;
    }
    public void setEffectCondition(String effectCondition) {
        this.effectCondition = effectCondition;
    }
    public String getInterestRmk() {
        return interestRmk;
    }
    public void setInterestRmk(String interestRmk) {
        this.interestRmk = interestRmk;
    }
    public String getIsHookBail() {
        return isHookBail;
    }
    public void setIsHookBail(String isHookBail) {
        this.isHookBail = isHookBail;
    }
    public String getBailCurId() {
        return bailCurId;
    }
    public void setBailCurId(String bailCrId) {
        this.bailCurId = bailCrId;
    }
    public String getBailBankAcc() {
        return bailBankAcc;
    }
    public void setBailBankAcc(String bailBankAcc) {
        this.bailBankAcc = bailBankAcc;
    }
    public String getIsHookGuarantee() {
        return isHookGuarantee;
    }
    public void setIsHookGuarantee(String isHookGuarantee) {
        this.isHookGuarantee = isHookGuarantee;
    }
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    public String getGuaranteeType() {
        return guaranteeType;
    }
    public void setGuaranteeType(String guaranteeType) {
        this.guaranteeType = guaranteeType;
    }
    public String getGuaranteeContract() {
        return guaranteeContract;
    }
    public void setGuaranteeContract(String guaranteeContract) {
        this.guaranteeContract = guaranteeContract;
    }
    public String getIsHookPledge() {
        return isHookPledge;
    }
    public void setIsHookPledge(String isHookPledge) {
        this.isHookPledge = isHookPledge;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
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
    public String getIsSave() {
        return isSave;
    }
    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }
    public Integer getPlanStatus() {
        return planStatus;
    }
    public void setPlanStatus(Integer planStatus) {
        this.planStatus = planStatus;
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
    @Override
    public String getBusinessKey() {
        return id;
    }
}
