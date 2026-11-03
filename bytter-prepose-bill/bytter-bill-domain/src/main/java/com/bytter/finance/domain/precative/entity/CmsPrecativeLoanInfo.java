package com.bytter.finance.domain.precative.entity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 委托贷款信息表
 * @author onlineGenerator
 * @date 2017-07-20 14:00:39
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PRECATIVE_LOAN_INFO", schema = "")
@SuppressWarnings("serial")
public class CmsPrecativeLoanInfo extends IdEntity implements java.io.Serializable {
    /** 委托人单位代码 */
    private String precativeId;
    /** 受托人单位代码 */
    private String corpId;
    /** 是否银行办理 */
    private String isBank;
    /** 受托行 */
    private String bankCode;
    /** 委托贷款合同号 */
    private String contractCode;
    /** 委托贷款票据号 */
    private String billCode;
    /** 贷款对象 */
    private String loanOpp;
    /** 贷款币种 */
    private String curId;
    /** 开始日期 */
    private java.util.Date startDate;
    /** 结束日期 */
    private java.util.Date endDate;
    /** 贷款期限 */
    private Integer term;
    /** 期限单位 */
    private String ternUnit;
    /** 贷款利率 */
    private BigDecimal rate;
    /** 逾期利率 */
    private BigDecimal overdueRate;
    /** 贷款金额 */
    private BigDecimal money;
    /** 手续费率 */
    private BigDecimal poundageRate;
    /** 手续费 */
    private BigDecimal poundage;
    /** 担保方式 */
    private String plType;
    /** 担保说明 */
    private String suretyExplain;
    /** 担保合同编码 */
    private String guaSuretyCode;
    /** 补录员 */
    private String repairOp;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 贷款用途 */
    private String purpose;
    /** 备注 */
    private String rmk;
    /** 打回原因 */
    private String refuseWhy;
    /** 上传文件信息 */
    private String annexName;
    /** 还款方式 */
    private String repayType;
    /** 发放方式 */
    private String loanSendOutType;
    /** 是否跳过发放 */
    private String skipLoanSendOut;
    /** 浮动方式 */
    private String floatWay;
    /** 浮动二级方式 */
    private String floatWayNext;
    /** 点数或浮动比例 */
    private BigDecimal pointsProportionText;
    /** 点数或者比例 */
    private String pointsProportionSelect;
    /** 利率调整生效日期 */
    private java.util.Date rateEntriesEffectDate;
    /** 计息方式 */
    private String interestWay;
    /** 基准利率类型 */
    private String referenceRateType;
    /** 贷款周期 */
    private String repayCycle;
    /** 利率复核生效日期 */
    private java.util.Date ratetimeoftakeeffect;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 还款计划Id */
    private String planId;
    /** 开户行名称 */
    private String bankName;
    /** 币种名称 */
    private String curName;
    /** 委托人单位名称 */
    private String precativeName;
    /** 借款人单位名称 */
    private String loanOppName;
    /** 受托人单位名称 */
    private String corpName;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 委托人单位代码
     */
    @Column(name = "PRECATIVE_ID")
    public String getPrecativeId() {
        return this.precativeId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委托人单位代码
     */
    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 受托人单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             受托人单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /** 对接的合同信息 */
    /** 对接的合同类型id */
    private String conTypeId;
    /** 对接的合同id */
    private String conId;
    /**
     * 方法:
     * 
     * @param: java.lang.String
     *             是否银行办理
     */
    @Column(name = "IS_BANK")
    public String getIsBank() {
        return isBank;
    }
    public void setIsBank(String isBank) {
        this.isBank = isBank;
    }
    /**
     * 方法: java.lang.String
     * 
     * @param: java.lang.String
     *             受托行
     */
    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 委托贷款合同号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委托贷款合同号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款对象
     */
    @Column(name = "LOAN_OPP")
    public String getLoanOpp() {
        return this.loanOpp;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款对象
     */
    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款币种
     */
    @Column(name = "CUR_ID")
    public String getCurId() {
        return this.curId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款币种
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 开始日期
     */
    @Column(name = "START_DATE")
    public java.util.Date getStartDate() {
        return this.startDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             开始日期
     */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 结束日期
     */
    @Column(name = "END_DATE")
    public java.util.Date getEndDate() {
        return this.endDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             结束日期
     */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 贷款期限
     */
    @Column(name = "TERM")
    public Integer getTerm() {
        return this.term;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             贷款期限
     */
    public void setTerm(Integer term) {
        this.term = term;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 期限单位
     */
    @Column(name = "TERN_UNIT")
    public String getTernUnit() {
        return this.ternUnit;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             期限单位
     */
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 贷款利率
     */
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return this.rate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             贷款利率
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 逾期利率
     */
    @Column(name = "OVERDUE_RATE")
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 贷款金额
     */
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             贷款金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 手续费率
     */
    @Column(name = "POUNDAGE_RATE")
    public BigDecimal getPoundageRate() {
        return this.poundageRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             手续费率
     */
    public void setPoundageRate(BigDecimal poundageRate) {
        this.poundageRate = poundageRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 手续费
     */
    @Column(name = "POUNDAGE")
    public BigDecimal getPoundage() {
        return this.poundage;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             手续费
     */
    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保方式
     */
    @Column(name = "PL_TYPE")
    public String getPlType() {
        return this.plType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             担保方式
     */
    public void setPlType(String plType) {
        this.plType = plType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保说明
     */
    @Column(name = "SURETY_EXPLAIN")
    public String getSuretyExplain() {
        return this.suretyExplain;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             担保说明
     */
    public void setSuretyExplain(String suretyExplain) {
        this.suretyExplain = suretyExplain;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保合同编码
     */
    @Column(name = "GUA_SURETY_CODE")
    public String getGuaSuretyCode() {
        return this.guaSuretyCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             担保合同编码
     */
    public void setGuaSuretyCode(String guaSuretyCode) {
        this.guaSuretyCode = guaSuretyCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 补录员
     */
    @Column(name = "REPAIR_OP")
    public String getRepairOp() {
        return this.repairOp;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             补录员
     */
    public void setRepairOp(String repairOp) {
        this.repairOp = repairOp;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务状态
     */
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.Integer
     *             业务状态
     */
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款用途
     */
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return this.purpose;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款用途
     */
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 打回原因
     */
    @Column(name = "REFUSE_WHY")
    public String getRefuseWhy() {
        return this.refuseWhy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             打回原因
     */
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 上传文件信息
     */
    @Column(name = "ANNEX_NAME")
    public String getAnnexName() {
        return this.annexName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             上传文件信息
     */
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款方式
     */
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return this.repayType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款方式
     */
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 发放方式
     */
    @Column(name = "LOAN_SEND_OUT_TYPE")
    public String getLoanSendOutType() {
        return this.loanSendOutType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             发放方式
     */
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    @Column(name = "SKIP_LOAN_SEND_OUT")
    public String getSkipLoanSendOut() {
        return skipLoanSendOut;
    }
    public void setSkipLoanSendOut(String skipLoanSendOut) {
        this.skipLoanSendOut = skipLoanSendOut;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 浮动方式
     */
    @Column(name = "FLOAT_WAY")
    public String getFloatWay() {
        return this.floatWay;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             浮动方式
     */
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 浮动方式
     */
    @Column(name = "FLOAT_WAY_NEXT")
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 点数或浮动比例
     */
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return this.pointsProportionText;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             点数或浮动比例
     */
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 点数或者比例
     */
    @Column(name = "POINTS_PROPORTION_SELECT")
    public String getPointsProportionSelect() {
        return this.pointsProportionSelect;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             点数或者比例
     */
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 利率调整生效日期
     */
    @Column(name = "RATE_ENTRIES_EFFECT_DATE")
    public java.util.Date getRateEntriesEffectDate() {
        return this.rateEntriesEffectDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             利率调整生效日期
     */
    public void setRateEntriesEffectDate(java.util.Date rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 计息方式
     */
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return this.interestWay;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             计息方式
     */
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 基准利率类型
     */
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return this.referenceRateType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             基准利率类型
     */
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款周期
     */
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return this.repayCycle;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款周期
     */
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 利率复核生效日期
     */
    @Column(name = "RATETIMEOFTAKEEFFECT")
    public java.util.Date getRatetimeoftakeeffect() {
        return this.ratetimeoftakeeffect;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             利率复核生效日期
     */
    public void setRatetimeoftakeeffect(java.util.Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 合同类型id
     */
    @Column(name = "CON_TYPE_ID")
    public String getConTypeId() {
        return conTypeId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 合同类型id
     */
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 合同id
     */
    @Column(name = "CON_ID")
    public String getConId() {
        return conId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 合同id
     */
    public void setConId(String conId) {
        this.conId = conId;
    }
    @Transient
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "PRECATIVE_NAME")
    public String getPrecativeName() {
        return precativeName;
    }
    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
    }
    @Column(name = "LOANOPP_NAME")
    public String getLoanOppName() {
        return loanOppName;
    }
    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
}
