package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 打印场景信息
 * @author zhengg
 * @date 2020-04-03 15:44:39
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_VOUTYPE_SCENE", schema = "")
@SuppressWarnings("serial")
public class BvVoutypeScene extends IdEntity implements java.io.Serializable {
	/** 凭证种类表ID */
	private String vouTid;
	/** 场景名称 */
	private String name;
	/** 场景说明 */
	private String rmk;
	/** 有效标志：0，否 1，是  */
	private String validSign;
	/** 排序字段 */
	private Integer ordernum;
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 凭证种类表ID
	 */
	@Column(name = "VOU_TID", nullable = true, length = 32)
	public String getVouTid() {
		return this.vouTid;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             凭证种类表ID
	 */
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 场景名称
	 */
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 场景说明
	 */
	@Column(name = "RMK")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer 排序字段
	 */
	@Column(name = "ORDERNUM", nullable = true, length = 10)
	public Integer getOrdernum() {
		return this.ordernum;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer
	 *             排序字段
	 */
	public void setOrdernum(Integer ordernum) {
		this.ordernum = ordernum;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 有效标志：0，否 1，是 2，已删除
	 */
	@Column(name = "VALID_SIGN", nullable = true, length = 1)
	public String getValidSign() {
		return this.validSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             有效标志：0，否 1，是 2，已删除
	 */
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Override
	public String toString() {
		return "BvVoutypeScene [vouTid=" + vouTid + ", name=" + name + ", rmk=" + rmk + ", validSign=" + validSign
				+ ", ordernum=" + ordernum + ", getVouTid()=" + getVouTid() + ", getName()=" + getName() + ", getRmk()="
				+ getRmk() + ", getOrdernum()=" + getOrdernum() + ", getValidSign()=" + getValidSign() + "]";
	}
}
