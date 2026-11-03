package com.bytter.guarantee.bond.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:FcBondApply
 * @Description:保证金占用实体类
 * @date 2018年9月4日 下午5:07:59
 */
@Entity
@Table(name = "FC_BOND_APPLY")
@SuppressWarnings("serial")
public class FcBondApply extends IdEntity implements java.io.Serializable { 
	/** 业务单号 */
	private String billNo;
	/** 占用类型 */
	private String applyType;
	/** 项目类型 */
	private String itemType;
	/** 项目单号 */
	private String itemNo;
	/** 单位ID */
	private String corpId;
	/** 银行ID */
	private String bankId;
	/** 项目金额 */
	private java.math.BigDecimal itemMoney;
	/** 保证金比例 */
	private java.math.BigDecimal bondPercent;
	/** 项目开始日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startDate;
	/** 项目结束日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endDate;
	/** 项目期限 */
	private Integer itemUnit;
	/** 保证金合同 */
	private String bondContract;
	/** 保证金ID */
	private String bondId;
	/** 占用金额 */
	private java.math.BigDecimal applyMoney;
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
	/** 占用保证金的项目ID */
	private String itemId;

	/** 释放日期 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date releaseDate;
	/** 自动回转标志*/
	private String autoSign;
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
	 * @return: java.lang.String 占用类型
	 */
	@Column(name = "APPLY_TYPE", nullable = true, length = 1)
	public String getApplyType() {
		return this.applyType;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             占用类型
	 */
	public void setApplyType(String applyType) {
		this.applyType = applyType;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 项目类型
	 */
	@Column(name = "ITEM_TYPE", nullable = true, length = 1)
	public String getItemType() {
		return this.itemType;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer
	 *             项目类型
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 项目单号
	 */
	@Column(name = "ITEM_NO", nullable = true, length = 50)
	public String getItemNo() {
		return this.itemNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             项目单号
	 */
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
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
	 * @return: java.lang.String 银行ID
	 */
	@Column(name = "BANK_ID", nullable = true, length = 12)
	public String getBankId() {
		return this.bankId;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             银行ID
	 */
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 * 
	 * @return: java.math.BigDecimal 项目金额
	 */
	@Column(name = "ITEM_MONEY",scale = 2, length = 18)
	public java.math.BigDecimal getItemMoney() {
		return this.itemMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             项目金额
	 */
	public void setItemMoney(java.math.BigDecimal itemMoney) {
		this.itemMoney = itemMoney;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 * 
	 * @return: java.math.BigDecimal 保证金比例
	 */
	@Column(name = "BOND_PERCENT",scale = 4, length = 8)
	public java.math.BigDecimal getBondPercent() {
		return this.bondPercent;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             保证金比例
	 */
	public void setBondPercent(java.math.BigDecimal bondPercent) {
		this.bondPercent = bondPercent;
	}
	/**
	 * 方法: 取得java.util.Date
	 * 
	 * @return: java.util.Date 项目开始日期
	 */
	@Column(name = "START_DATE", nullable = true)
	public Date getStartDate() {
		return this.startDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 * 
	 * @param: java.util.Date
	 *             项目开始日期
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * 方法: 取得java.util.Date
	 * 
	 * @return: java.util.Date 项目结束日期
	 */
	@Column(name = "END_DATE", nullable = true)
	public Date getEndDate() {
		return this.endDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 * 
	 * @param: java.util.Date
	 *             项目结束日期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 * 
	 * @return: java.lang.Integer 项目期限
	 */
	@Column(name = "ITEM_UNIT", nullable = true, length = 3)
	public Integer getItemUnit() {
		return this.itemUnit;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 * 
	 * @param: java.lang.Integer
	 *             项目期限
	 */
	public void setItemUnit(Integer itemUnit) {
		this.itemUnit = itemUnit;
	}
	/**
	 * 方法: 取得java.lang.String
	 * 
	 * @return: java.lang.String 保证金合同
	 */
	@Column(name = "BOND_CONTRACT", nullable = true, length = 50)
	public String getBondContract() {
		return this.bondContract;
	}
	/**
	 * 方法: 设置java.lang.String
	 * 
	 * @param: java.lang.String
	 *             保证金合同
	 */
	public void setBondContract(String bondContract) {
		this.bondContract = bondContract;
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
	 * 方法: 取得java.math.BigDecimal
	 * 
	 * @return: java.math.BigDecimal 占用金额
	 */
	@Column(name = "APPLY_MONEY",scale = 2, length = 18)
	public java.math.BigDecimal getApplyMoney() {
		return this.applyMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             占用金额
	 */
	public void setApplyMoney(java.math.BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
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
	 * 方法: 取得java.lang.String
	 * @return java.lang.String  占用保证金的项目id
	 */
	@Column(name = "ITEM_ID", nullable = true,length = 32)
	public String getItemId() {
		return itemId;
	}
	/**
	 * 方法: 设置java.lang.String
	 * @param 占用保证金的项目id
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	@Column(name = "RELEASE_DATE")
	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	@Column(name = "AUTO_SIGN")
	public String getAutoSign() {
		return autoSign;
	}

	public void setAutoSign(String autoSign) {
		this.autoSign = autoSign;
	}
}