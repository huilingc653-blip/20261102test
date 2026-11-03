package com.bytter.guarantee.bond.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:Entity
 * @Description:保证金管理
 * @date 2018年9月4日 上午10:36:04
 */
@Entity
@Table(name = "FC_BOND")
@SuppressWarnings("serial")
public class FcBond extends IdEntity implements java.io.Serializable { 
	/** 业务单号 */
	private String billNo;
	/** 单位ID */
	private String corpId;
	/** 银行账号ID */
	private String bankAccId;
	/** 存入日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
	/** 是否已付款 */
	private String isPayed;
	/** 保证金金额 */
	private BigDecimal bondMoney;
	/** 备注 */
	private String remark;
	/** 状态 */
	private Integer status;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	/**币别*/
	private String curId;
	/** 附件ID */
	private String attachmentId;
	/**存出账号*/
	private String outBankAcc;
	/**保证金类型*/
	private String bondTypeId;
	/**利率*/
	private BigDecimal interestRate;
	/**到期日期*/
	private Date endDate;
	/**是否存入定期*/
	private String isRegular;
	/**期限*/
	private String dayLimit;
	/**用途*/
	private String purpose;
	/**单位名称*/
	private String corpName;
	/**币别名称*/
	private String curName;
	/**银行账号*/
	private String bankAcc;
	/**账户名称*/
	private String accName;
	/**开户行行号*/
	private String bankCode;
	/**存入银行名称*/
	private String bankName;
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 业务单号
	 */
	@Column(name = "BILL_NO", nullable = true, length = 20)
	public String getBillNo() {
		return this.billNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             业务单号
	 */
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 单位ID
	 */
	@Column(name = "CORP_ID", nullable = true, length = 32)
	public String getCorpId() {
		return this.corpId;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             单位ID
	 */
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 银行账号ID
	 */
	@Column(name = "BANK_ACC_ID", nullable = true, length = 32)
	public String getBankAccId() {
		return this.bankAccId;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             银行账号ID
	 */
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	/**
	 * 方法: 取得java.util.Date
	 * 
	 * @return: java.util.Date 存入日期
	 */
	@Column(name = "START_DATE", nullable = true)
	public Date getStartDate() {
		return this.startDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 * 
	 * @param: java.util.Date
	 *             存入日期
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 是否已付款
	 */
	@Column(name = "IS_PAYED", nullable = true, length = 1)
	public String getIsPayed() {
		return this.isPayed;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             是否已付款
	 */
	public void setIsPayed(String isPayed) {
		this.isPayed = isPayed;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 * 
	 * @return: java.math.BigDecimal 保证金金额
	 */
	@Column(name = "BOND_MONEY", nullable = true, scale = 2, length = 18)
	public BigDecimal getBondMoney() {
		return this.bondMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             保证金金额
	 */
	public void setBondMoney(BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
	}
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 备注
	 */
	@Column(name = "REMARK", nullable = true, length = 200)
	public String getRemark() {
		return this.remark;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 状态
	 */
	@Column(name = "STATUS", nullable = true, length = 2)
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
	 * @return: java.lang.String 创建人登录名称
	 */
	@Column(name = "CREATE_BY", nullable = true, length = 50)
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
	@Column(name = "CREATE_DATE", nullable = true)
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
	 * @return: java.lang.String 更新人登录名称
	 */
	@Column(name = "UPDATE_BY", nullable = true, length = 50)
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
	@Column(name = "UPDATE_DATE", nullable = true)
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
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public String getCurId(){
		return this.curId;
	}
	
	/**
	 * 方法：设置java.lang.String
	 * @param java.lang.String curId 币别 
	 */
	public void setCurId(String curId) {
		this.curId = curId;
	}
	
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return:java.lang.String 附件ID
	 */
	@Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
	public String getAttachmentId() {
		return attachmentId;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             附件ID
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Column(name = "OUT_BANK_ACC")
	public String getOutBankAcc() {
		return outBankAcc;
	}
	public void setOutBankAcc(String outBankAcc) {
		this.outBankAcc = outBankAcc;
	}
	@Column(name = "BOND_TYPE_ID")
	public String getBondTypeId() {
		return bondTypeId;
	}
	public void setBondTypeId(String bondTypeId) {
		this.bondTypeId = bondTypeId;
	}
	@Column(name = "INTEREST_RATE")
	public BigDecimal getInterestRate() {
		return interestRate;
	}
	public void setInterestRate(BigDecimal interestRate) {
		this.interestRate = interestRate;
	}
	@Column(name = "END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Column(name = "IS_REGULAR")
	public String getIsRegular() {
		return isRegular;
	}
	public void setIsRegular(String isRegular) {
		this.isRegular = isRegular;
	}
	@Column(name = "DAY_LIMIT")
	public String getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(String dayLimit) {
		this.dayLimit = dayLimit;
	}
	@Column(name = "PURPOSE")
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
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
	@Column(name = "BANK_ACC")
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	@Column(name = "ACC_NAME")
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Column(name = "BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	@Column(name = "BANK_NAME")
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
}
