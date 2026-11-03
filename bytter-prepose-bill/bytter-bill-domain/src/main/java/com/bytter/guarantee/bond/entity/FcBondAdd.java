package com.bytter.guarantee.bond.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author luxy
 * @version 1.0
 * @Title:FcBondAdd
 * @Description:保证金追加实体类
 * @date 2018年9月4日 下午4:16:00
 */
@Entity
@Table(name = "FC_BOND_ADD")
@SuppressWarnings("serial")
public class FcBondAdd extends IdEntity implements java.io.Serializable {
	/** 业务单号 */
	private String billNo;
	/** 保证金ID */
	private String bondId;
	/** 追加日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date addDate;
	/** 是否已付款 */
	private String isPayed;
	/** 追加金额 */
	private java.math.BigDecimal addMoney;
	/** 备注 */
	private String remark;
	/** 状态 */
	private Integer status;
	/** 创建人登录名称 */
	private String createBy;
	/** 创建日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date createDate;
	/** 更新人登录名称 */
	private String updateBy;
	/** 更新日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date updateDate;
	/**存出账号*/
	private String outBankAcc;
	/**用途*/
	private String purpose;
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
	 * @return: java.lang.String 保证金ID
	 */
	@Column(name = "BOND_ID", nullable = true, length = 32)
	public String getBondId() {
		return this.bondId;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             保证金ID
	 */
	public void setBondId(String bondId) {
		this.bondId = bondId;
	}
	/**
	 * 方法: 取得java.util.Date
	 * 
	 * @return: java.util.Date 追加日期
	 */
	@Column(name = "ADD_DATE", nullable = true)
	public java.util.Date getAddDate() {
		return this.addDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 * 
	 * @param: java.util.Date
	 *             追加日期
	 */
	public void setAddDate(java.util.Date addDate) {
		this.addDate = addDate;
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
	 * @return: java.math.BigDecimal 追加金额
	 */
	@Column(name = "ADD_MONEY", nullable = true, scale = 2, length = 18)
	public java.math.BigDecimal getAddMoney() {
		return this.addMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             追加金额
	 */
	public void setAddMoney(java.math.BigDecimal addMoney) {
		this.addMoney = addMoney;
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
	@Column(name = "OUT_BANK_ACC")
	public String getOutBankAcc() {
		return outBankAcc;
	}
	public void setOutBankAcc(String outBankAcc) {
		this.outBankAcc = outBankAcc;
	}
	@Column(name = "PURPOSE")
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
}
