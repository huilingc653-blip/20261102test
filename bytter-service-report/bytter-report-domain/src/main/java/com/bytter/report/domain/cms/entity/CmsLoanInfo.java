package com.bytter.report.domain.cms.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 贷款信息表
 * @author onlineGenerator
 * @date 2017-07-21 09:46:16
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_LOAN_INFO", schema = "")
@SuppressWarnings("serial")
public class CmsLoanInfo extends IdEntity implements java.io.Serializable {
    /** 结算中心代码 */
    private String netCode;
    /** 单位代码 */
    private String corpId;
    /** 贷款流水号 */
    private String billCode;
    /** 借款合同号 */
    private String contractCode;
    /** 贷款金额 */
    private BigDecimal loanMoney;
    /** 贷款期限 */
    private Integer loanTerm;
    /** 期限单位 */
    private String ternUnit;
    /** 放款币种 */
    private String curCode;
    /** 贷款日期 */
    private Date loanDate;
    /** 还款日期 */
    private Date repayDate;
    /** 还款方式 */
    private String repayType;
    /** 贷款种类 */
    private String loanClass;
    /** 贷款类型 */
    private String loanGenre;
    /** 担保方式 */
    private String loanType;
    /** 担保说明 */
    private String suretyExplain;
    /** 担保品价值 */
    private BigDecimal suretyMoney;
    /** 放款账号 */
    private String provideLoanAcc;
    /** 放款利率 */
    private BigDecimal loanRate;
    /** 逾期利率 */
    private BigDecimal overdueRate;
    /** 展期利率 */
    private BigDecimal deferredRate;
    /** 补录员 */
    private String repairOp;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 借款级次 */
    private String loanGrade;
    /** 用途 */
    private String purpose;
    /** 备注 */
    private String rmk;
    /** 打回原因 */
    private String refuseWhy;
    /** 是否挂授信 */
    private String isHangCredit;
    /** 额度编号 */
    private String rationCode;
    /** 授信额度品种代码 */
    private String itemCode;
    /** 贷款发放方式 */
    private String loanSendOutType;
    /** 浮动方式 */
    private String floatWay;
    /** 点数或浮动比例 */
    private String pointsProportionText;
    /** 点数或者比例 */
    private String pointsProportionSelect;
    /** 利率调整生效日期 */
    private Date rateEntriesEffectDate;
    /** 计息方式 */
    private String interestWay;
    /** 基准利率类型 */
    private String referenceRateType;
    /** 贷款周期 */
    private String repayCycle;
    /** 利率复核生效日期 */
    private Date ratetimeoftakeeffect;
    /** 发放次数 */
    private Integer sendTime;
    /** 协定费率 */
    private BigDecimal clbAgreementRate;
    /** 借款单位 */
    private String borrowCorpId;
    /** 支付方式 */
    private String payType;
    /** 固定费率 */
    private BigDecimal clbFixedRate;
    /** 借贷关联的借款单号 */
    private String ploadcode;
    /** 行业代码 */
    private String industryCode;
    /** 附件ID */
    private String attachmentId;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /** 浮动二级方式 **/
    public String floatWayNext;
    /** 是否挂接担保 **/
    public String isHangGuarantee;
    /** 担保合同 **/
    public String guaContract;
    /** 对接的合同信息 */
    /** 对接的合同类型id */
    private String conTypeId;
    /** 对接的合同id */
    private String conId;
    /** 是否自动还款 */
    private String isAutoRepay;
    /** 是否跳过发放 */
    private String isOverDistribute;
    /** 是否跳过补录 */
    private String isOverRepair;
    /** 所属资金中心名称 */
    private String netName;
    /** 申请单位名称 */
    private String corpName;
    /** 币别名称 */
    private String curName;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 结算中心代码
     */
    @Column(name = "NET_CODE")
    public String getNetCode() {
        return this.netCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             结算中心代码
     */
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款流水号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款流水号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 借款合同号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             借款合同号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 贷款金额
     */
    @Column(name = "LOAN_MONEY")
    public BigDecimal getLoanMoney() {
        return this.loanMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             贷款金额
     */
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 贷款期限
     */
    @Column(name = "LOAN_TERM")
    public Integer getLoanTerm() {
        return this.loanTerm;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             贷款期限
     */
    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
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
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 放款币种
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             放款币种
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 贷款日期
     */
    @Column(name = "LOAN_DATE")
    public Date getLoanDate() {
        return this.loanDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             贷款日期
     */
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 还款日期
     */
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return this.repayDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             还款日期
     */
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
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
     * @return: java.lang.String 贷款种类
     */
    @Column(name = "LOAN_CLASS")
    public String getLoanClass() {
        return this.loanClass;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款种类
     */
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款类型
     */
    @Column(name = "LOAN_GENRE")
    public String getLoanGenre() {
        return this.loanGenre;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款类型
     */
    public void setLoanGenre(String loanGenre) {
        this.loanGenre = loanGenre;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保方式
     */
    @Column(name = "LOAN_TYPE")
    public String getLoanType() {
        return this.loanType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             担保方式
     */
    public void setLoanType(String loanType) {
        this.loanType = loanType;
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
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 担保品价值
     */
    @Column(name = "SURETY_MONEY")
    public BigDecimal getSuretyMoney() {
        return this.suretyMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             担保品价值
     */
    public void setSuretyMoney(BigDecimal suretyMoney) {
        this.suretyMoney = suretyMoney;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 放款账号
     */
    @Column(name = "PROVIDE_LOAN_ACC")
    public String getProvideLoanAcc() {
        return this.provideLoanAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             放款账号
     */
    public void setProvideLoanAcc(String provideLoanAcc) {
        this.provideLoanAcc = provideLoanAcc;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 放款利率
     */
    @Column(name = "LOAN_RATE")
    public BigDecimal getLoanRate() {
        return this.loanRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             放款利率
     */
    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 逾期利率
     */
    @Column(name = "OVERDUE_RATE")
    public BigDecimal getOverdueRate() {
        return this.overdueRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             逾期利率
     */
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 展期利率
     */
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return this.deferredRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             展期利率
     */
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
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
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 借款级次
     */
    @Column(name = "LOAN_GRADE")
    public String getLoanGrade() {
        return this.loanGrade;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             借款级次
     */
    public void setLoanGrade(String loanGrade) {
        this.loanGrade = loanGrade;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 用途
     */
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return this.purpose;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             用途
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
     * @return: java.lang.String 是否挂授信
     */
    @Column(name = "IS_HANG_CREDIT")
    public String getIsHangCredit() {
        return this.isHangCredit;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             是否挂授信
     */
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 额度编号
     */
    @Column(name = "RATION_CODE")
    public String getRationCode() {
        return this.rationCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             额度编号
     */
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信额度品种代码
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return this.itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信额度品种代码
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款发放方式
     */
    @Column(name = "LOAN_SEND_OUT_TYPE")
    public String getLoanSendOutType() {
        return this.loanSendOutType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款发放方式
     */
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
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
     * @return: java.lang.String 点数或浮动比例
     */
    @Column(name = "POINTS_PROPORTION_TEXT")
    public String getPointsProportionText() {
        return this.pointsProportionText;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             点数或浮动比例
     */
    public void setPointsProportionText(String pointsProportionText) {
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
    public Date getRateEntriesEffectDate() {
        return this.rateEntriesEffectDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             利率调整生效日期
     */
    public void setRateEntriesEffectDate(Date rateEntriesEffectDate) {
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
    public Date getRatetimeoftakeeffect() {
        return this.ratetimeoftakeeffect;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             利率复核生效日期
     */
    public void setRatetimeoftakeeffect(Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 发放次数
     */
    @Column(name = "SEND_TIME")
    public Integer getSendTime() {
        return this.sendTime;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             发放次数
     */
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 协定费率
     */
    @Column(name = "CLB_AGREEMENT_RATE")
    public BigDecimal getClbAgreementRate() {
        return this.clbAgreementRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             协定费率
     */
    public void setClbAgreementRate(BigDecimal clbAgreementRate) {
        this.clbAgreementRate = clbAgreementRate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 借款单位
     */
    @Column(name = "BORROW_CORP_ID")
    public String getBorrowCorpId() {
        return this.borrowCorpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             借款单位
     */
    public void setBorrowCorpId(String borrowCorpId) {
        this.borrowCorpId = borrowCorpId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 支付方式
     */
    @Column(name = "PAY_TYPE")
    public String getPayType() {
        return this.payType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             支付方式
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 固定费率
     */
    @Column(name = "CLB_FIXED_RATE")
    public BigDecimal getClbFixedRate() {
        return this.clbFixedRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             固定费率
     */
    public void setClbFixedRate(BigDecimal clbFixedRate) {
        this.clbFixedRate = clbFixedRate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 借贷关联的借款单号
     */
    @Column(name = "PLOADCODE")
    public String getPloadcode() {
        return this.ploadcode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             借贷关联的借款单号
     */
    public void setPloadcode(String ploadcode) {
        this.ploadcode = ploadcode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 行业代码
     */
    @Column(name = "INDUSTRY_CODE")
    public String getIndustryCode() {
        return this.industryCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             行业代码
     */
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
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
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(Date createDate) {
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
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
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
     * @return: java.lang.String 是否挂接担保
     */
    @Column(name = "IS_HANG_GUARANTEE")
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 是否挂接担保
     */
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保合同
     */
    @Column(name = "GUA_CONTRACT")
    public String getGuaContract() {
        return guaContract;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @return: java.lang.String 担保合同
     */
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
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
    @Column(name = "IS_AUTO_REPAY")
    public String getIsAutoRepay() {
        return isAutoRepay;
    }
    public void setIsAutoRepay(String isAutoRepay) {
        this.isAutoRepay = isAutoRepay;
    }
    @Column(name = "IS_OVER_DISTRIBUTE")
    public String getIsOverDistribute() {
        return isOverDistribute;
    }
    public void setIsOverDistribute(String isOverDistribute) {
        this.isOverDistribute = isOverDistribute;
    }
    @Column(name = "IS_OVER_REPAIR")
    public String getIsOverRepair() {
        return isOverRepair;
    }
    public void setIsOverRepair(String isOverRepair) {
        this.isOverRepair = isOverRepair;
    }
    @Column(name = "NET_NAME")
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
