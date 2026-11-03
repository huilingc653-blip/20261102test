package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 借款利息预计明细
 * @author onlineGenerator
 * @date 2017-08-24 15:50:26
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PROVIDE_INTEREST", schema = "")
@SuppressWarnings("serial")
public class CmsProvideInterest extends IdEntity implements java.io.Serializable {
    /** 生成利息预计的还款计划ID */
    private String repaymentScheduleId;
    /** 计息时间 */
    private Date interestTime;
    /** 合同号 */
    private String contractCode;
    /** 单据编号 */
    private String billCode;
    /** 业务类型 */
    private String billType;
    /** 网点 */
    private String netId;
    /** 单位 */
    private String corpId;
    /** 币别 */
    private String curId;
    /** 计息类型 */
    private String interestType;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /** 起息日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date blnInstDate;
    /** 计息天数 */
    private Integer days;
    /** 积数 */
    private java.math.BigDecimal cumulation;
    /** 基准利率 */
    private java.math.BigDecimal rate;
    /** 浮动比例点数 */
    private java.math.BigDecimal floatPoint;
    /** 计息利率 */
    private java.math.BigDecimal interestRate;
    /** 税率 */
    private java.math.BigDecimal rateFax;
    /** 本金 */
    private java.math.BigDecimal loanMoney;
    /** 归还本金 */
    private java.math.BigDecimal repayMoney;
    /** 剩余本金 */
    private java.math.BigDecimal leftMoney;
    /** 归还本金利息 */
    private java.math.BigDecimal interest;
    /** 支付利息 */
    private java.math.BigDecimal payInterest;
    /** 全部利息 */
    private java.math.BigDecimal totalInterest;
    /** 未结利息 */
    private java.math.BigDecimal unPayInterest;
    /** 利息税率 */
    private java.math.BigDecimal interestFax;
    /** 实际付息 */
    private java.math.BigDecimal realInterest;
    /** 计息标识 */
    private String interestSign;
    /** 还款状态 */
    private Integer status;
    /** 备注 */
    private String rmk;
    /** 还款日期标识。标识分段属于这个时间的还款 */
    private Date signRepayDate;
    /** 还款到期提醒标志 **/
    private String repaySign;
    @Column(name = "REPAY_SIGN")
    public String getRepaySign() {
        return repaySign;
    }
    public void setRepaySign(String repaySign) {
        this.repaySign = repaySign;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 计息时间
     */
    @Column(name = "INTEREST_TIME")
    public Date getInterestTime() {
        return this.interestTime;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             计息时间
     */
    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 合同号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             合同号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单据编号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单据编号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务类型
     */
    @Column(name = "BILL_TYPE")
    public String getBillType() {
        return this.billType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             业务类型
     */
    public void setBillType(String billType) {
        this.billType = billType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 网点
     */
    @Column(name = "NET_ID")
    public String getNetId() {
        return this.netId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             网点
     */
    public void setNetId(String netId) {
        this.netId = netId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单位
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 币别
     */
    @Column(name = "CUR_ID")
    public String getCurId() {
        return this.curId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             币别
     */
    public void setCurId(String curId) {
        this.curId = curId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 计息类型
     */
    @Column(name = "INTEREST_TYPE")
    public String getInterestType() {
        return this.interestType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             计息类型
     */
    public void setInterestType(String interestType) {
        this.interestType = interestType;
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
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 起息日期
     */
    @Column(name = "INTEREST_START_DATE")
    public Date getInterestStartDate() {
        return this.interestStartDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             起息日期
     */
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 结束日期
     */
    @Column(name = "BLN_INST_DATE")
    public Date getBlnInstDate() {
        return this.blnInstDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             结束日期
     */
    public void setBlnInstDate(Date blnInstDate) {
        this.blnInstDate = blnInstDate;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 计息天数
     */
    @Column(name = "DAYS")
    public Integer getDays() {
        return this.days;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             计息天数
     */
    public void setDays(Integer days) {
        this.days = days;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 积数
     */
    @Column(name = "CUMULATION")
    public java.math.BigDecimal getCumulation() {
        return this.cumulation;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             积数
     */
    public void setCumulation(java.math.BigDecimal cumulation) {
        this.cumulation = cumulation;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 基准利率
     */
    @Column(name = "RATE")
    public java.math.BigDecimal getRate() {
        return this.rate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             基准利率
     */
    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 浮动比例点数
     */
    @Column(name = "FLOAT_POINT")
    public java.math.BigDecimal getFloatPoint() {
        return this.floatPoint;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             浮动比例点数
     */
    public void setFloatPoint(java.math.BigDecimal floatPoint) {
        this.floatPoint = floatPoint;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 计息利率
     */
    @Column(name = "INTEREST_RATE")
    public java.math.BigDecimal getInterestRate() {
        return this.interestRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             计息利率
     */
    public void setInterestRate(java.math.BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 税率
     */
    @Column(name = "RATE_FAX")
    public java.math.BigDecimal getRateFax() {
        return this.rateFax;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             税率
     */
    public void setRateFax(java.math.BigDecimal rateFax) {
        this.rateFax = rateFax;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 本金
     */
    @Column(name = "LOAN_MONEY")
    public java.math.BigDecimal getLoanMoney() {
        return this.loanMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             本金
     */
    public void setLoanMoney(java.math.BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 归还本金
     */
    @Column(name = "REPAY_MONEY")
    public java.math.BigDecimal getRepayMoney() {
        return this.repayMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             归还本金
     */
    public void setRepayMoney(java.math.BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 剩余本金
     */
    @Column(name = "LEFT_MONEY")
    public java.math.BigDecimal getLeftMoney() {
        return this.leftMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             剩余本金
     */
    public void setLeftMoney(java.math.BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 归还本金利息
     */
    @Column(name = "INTEREST")
    public java.math.BigDecimal getInterest() {
        return this.interest;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             归还本金利息
     */
    public void setInterest(java.math.BigDecimal interest) {
        this.interest = interest;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 支付利息
     */
    @Column(name = "PAY_INTEREST")
    public java.math.BigDecimal getPayInterest() {
        return this.payInterest;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             支付利息
     */
    public void setPayInterest(java.math.BigDecimal payInterest) {
        this.payInterest = payInterest;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 全部利息
     */
    @Column(name = "TOTAL_INTEREST")
    public java.math.BigDecimal getTotalInterest() {
        return this.totalInterest;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             全部利息
     */
    public void setTotalInterest(java.math.BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 未结利息
     */
    @Column(name = "UN_PAY_INTEREST")
    public java.math.BigDecimal getUnPayInterest() {
        return this.unPayInterest;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             未结利息
     */
    public void setUnPayInterest(java.math.BigDecimal unPayInterest) {
        this.unPayInterest = unPayInterest;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 利息税率
     */
    @Column(name = "INTEREST_FAX")
    public java.math.BigDecimal getInterestFax() {
        return this.interestFax;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             利息税率
     */
    public void setInterestFax(java.math.BigDecimal interestFax) {
        this.interestFax = interestFax;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 实际付息
     */
    @Column(name = "REAL_INTEREST")
    public java.math.BigDecimal getRealInterest() {
        return this.realInterest;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             实际付息
     */
    public void setRealInterest(java.math.BigDecimal realInterest) {
        this.realInterest = realInterest;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 计息标识
     */
    @Column(name = "INTEREST_SIGN")
    public String getInterestSign() {
        return this.interestSign;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             计息标识
     */
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 还款状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             还款状态
     */
    public void setStatus(Integer status) {
        this.status = status;
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
    @Column(name = "REPAYMENT_SCHEDULE_ID")
    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    @Column(name = "SIGN_REPAY_DATE")
    public Date getSignRepayDate() {
        return signRepayDate;
    }
    public void setSignRepayDate(Date signRepayDate) {
        this.signRepayDate = signRepayDate;
    }
}
