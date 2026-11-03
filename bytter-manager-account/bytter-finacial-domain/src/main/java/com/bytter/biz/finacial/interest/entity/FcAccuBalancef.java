package com.bytter.biz.finacial.interest.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "FC_ACCU_BALANCE", schema = "")
@SuppressWarnings("serial")
public class FcAccuBalancef extends IdEntity implements Serializable {
    /**
     * ID
     **/
    private String id;
    /**
     * 积数日期
     */
    private Date balanceDate;
    /**
     * 账号
     */
    private String account;
    /**
     * 币别代码
     */
    private String curCode;
    /**
     * 当天积数
     */
    private BigDecimal cumu;
    /**
     * 当天调整积数
     */
    private BigDecimal adjustCumu;
    /**
     * 协定存款标志
     */
    private String agreedDepositSign;
    /**
     * 计息标志
     */
    private String interestSign;
    /**
     * 预提标志
     */
    private String preSign;
    /**
     * 计息日期
     */
    private Date interestTime;
    /**
     * 计息日期
     */
    private BigDecimal aAdjusted;
    /**
     * 协定存款登记ID
     */
    private String agreedDepositId;
    /**
     * 积数类型
     */
    private Integer agreedType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 最后更新时间
     */
    private Date updateTime;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    /**
     * 业务ID(还款ID)
     */
    private String billNo;

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  积数日期
     */
    @Column(name = "BALANCE_DATE")
    public Date getBalanceDate() {
        return this.balanceDate;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  积数日期
     */
    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  账号
     */
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return this.account;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  币别代码
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  币别代码
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  当天积数
     */
    @Column(name = "CUMU")
    public BigDecimal getCumu() {
        return this.cumu;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  当天积数
     */
    public void setCumu(BigDecimal cumu) {
        this.cumu = cumu;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  当天调整积数
     */
    @Column(name = "ADJUST_CUMU")
    public BigDecimal getAdjustCumu() {
        return this.adjustCumu;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  当天调整积数
     */
    public void setAdjustCumu(BigDecimal adjustCumu) {
        this.adjustCumu = adjustCumu;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  协定存款标志
     */
    @Column(name = "AGREED_DEPOSIT_SIGN")
    public String getAgreedDepositSign() {
        return this.agreedDepositSign;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  协定存款标志
     */
    public void setAgreedDepositSign(String agreedDepositSign) {
        this.agreedDepositSign = agreedDepositSign;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  计息标志
     */
    @Column(name = "INTEREST_SIGN")
    public String getInterestSign() {
        return this.interestSign;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  计息标志
     */
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  预提标志
     */
    @Column(name = "PRE_SIGN")
    public String getPreSign() {
        return this.preSign;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  预提标志
     */
    public void setPreSign(String preSign) {
        this.preSign = preSign;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  计息日期
     */
    @Column(name = "INTEREST_TIME")
    public Date getInterestTime() {
        return this.interestTime;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  计息日期
     */
    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }

    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal  计息日期
     */
    @Column(name = "A_ADJUSTED")
    public BigDecimal getAAdjusted() {
        return this.aAdjusted;
    }

    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal  计息日期
     */
    public void setAAdjusted(BigDecimal aAdjusted) {
        this.aAdjusted = aAdjusted;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  协定存款登记ID
     */
    @Column(name = "AGREED_DEPOSIT_ID")
    public String getAgreedDepositId() {
        return this.agreedDepositId;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  协定存款登记ID
     */
    public void setAgreedDepositId(String agreedDepositId) {
        this.agreedDepositId = agreedDepositId;
    }

    /**
     * 方法: 取得java.lang.Integer
     *
     * @return: java.lang.Integer  积数类型
     */
    @Column(name = "AGREED_TYPE")
    public Integer getAgreedType() {
        return this.agreedType;
    }

    /**
     * 方法: 设置java.lang.Integer
     *
     * @param: java.lang.Integer  积数类型
     */
    public void setAgreedType(Integer agreedType) {
        this.agreedType = agreedType;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建时间
     */
    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return this.createTime;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建用户代码
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建用户代码
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  最后更新时间
     */
    @Column(name = "UPDATE_TIME")
    public Date getUpdateTime() {
        return this.updateTime;
    }

    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  最后更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  最后更新用户代码
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }

    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  最后更新用户代码
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

		@Column(name ="BILL_NO")
		public java.lang.String getBillNo() {
			return billNo;
		}
		public void setBillNo(java.lang.String billNo) {
			this.billNo = billNo;
		}
		/**
		 * 计算标志
		 */
	    private String calSign = "0";
	    @Transient
	    public String getCalSign() {
	        return calSign;
	    }
	    public void setCalSign(String calSign) {
	        this.calSign = calSign;
	    }
	}
