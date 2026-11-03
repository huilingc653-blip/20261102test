package com.bytter.guarantee.bond.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author luxy
 * @version 1.0
 * @Title:FcBondBal
 * @Description:保证金余额实体类
 * @date 2018年9月4日 下午4:29:04
 */
@Entity
@Table(name = "FC_BOND_BAL")
@SuppressWarnings("serial")
public class FcBondBal extends IdEntity implements java.io.Serializable { 
	/** 保证金ID */
	private String bondId;
	/** 存入金额 */
	private java.math.BigDecimal bondMoney;
	/** 追加金额 */
	private java.math.BigDecimal addMoney;
	/** 占用金额 */
	private java.math.BigDecimal applyMoney;
	/** 在途占用金额 */
	private java.math.BigDecimal freezeMoney;
	/** 转回金额 */
	private java.math.BigDecimal backMoney;
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
	 * @return: java.math.BigDecimal 存入金额
	 */
	@Column(name = "BOND_MONEY", nullable = true, scale = 2, length = 18)
	public java.math.BigDecimal getBondMoney() {
		return this.bondMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             存入金额
	 */
	public void setBondMoney(java.math.BigDecimal bondMoney) {
		this.bondMoney = bondMoney;
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
	 * 方法: 取得java.math.BigDecimal
	 * 
	 * @return: java.math.BigDecimal 占用金额
	 */
	@Column(name = "APPLY_MONEY", nullable = true, scale = 2, length = 18)
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
	 * 方法: 取得java.math.BigDecimal
	 * 
	 * @return: java.math.BigDecimal 在途占用金额
	 */
	@Column(name = "FREEZE_MONEY", nullable = true, scale = 2, length = 18)
	public java.math.BigDecimal getFreezeMoney() {
		return this.freezeMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             在途占用金额
	 */
	public void setFreezeMoney(java.math.BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 * 
	 * @return: java.math.BigDecimal 转回金额
	 */
	@Column(name = "BACK_MONEY", nullable = true, scale = 2, length = 18)
	public java.math.BigDecimal getBackMoney() {
		return this.backMoney;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 * 
	 * @param: java.math.BigDecimal
	 *             转回金额
	 */
	public void setBackMoney(java.math.BigDecimal backMoney) {
		this.backMoney = backMoney;
	}
}
