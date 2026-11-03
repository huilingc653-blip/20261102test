package com.bytter.cashVoucher.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * @Title: Entity
 * @Description: BV_VOUTYPE_DATA_FIELD
 * @author onlineGenerator
 * @date 2017-06-07 15:44:39
 * @version 1.0
 *
 */
@Entity
@Table(name = "BV_VOUTYPE_DATA_FIELD", schema = "")
@SuppressWarnings("serial")
public class BvVoutypeDataField extends IdEntity implements java.io.Serializable {
	/** 凭证种类表ID */
	private String vouTid;
	/** 取数字段代码 */
	private String fieldCode;
	/** 取数字段名称 */
	private String fieldName;
	/** 数据类型：0无，1金额，2 银行账户，3银行大类 */
	private String dataType;
	/** 字典组类型 **/
	private String dictGroup;
	/** 打印列表是否显示：0否，1是 */
	private String isShow;
	/** 有效标志：0，否 1，是 2，已删除 */
	private String validSign;
	/** 排序字段 */
	private Integer ordernum;
	private String fieldVal;
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
	 * @return: java.lang.String 取数字段代码
	 */
	@Column(name = "FIELD_CODE", nullable = true, length = 10)
	public String getFieldCode() {
		return this.fieldCode;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             取数字段代码
	 */
	public void setFieldCode(String fieldCode) {
		this.fieldCode = fieldCode;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 取数字段名称
	 */
	@Column(name = "FIELD_NAME", nullable = true, length = 50)
	public String getFieldName() {
		return this.fieldName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             取数字段名称
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 数据类型：0无，1金额，2 银行账户，3银行大类
	 */
	@Column(name = "DATA_TYPE", nullable = true, length = 1)
	public String getDataType() {
		return dataType;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             数据类型：0无，1金额，2 银行账户，3银行大类
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String 打印列表是否显示：0否，1是
	 */
	@Column(name = "IS_SHOW", nullable = true, length = 1)
	public String getIsShow() {
		return this.isShow;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String
	 *             打印列表是否显示：0否，1是
	 */
	public void setIsShow(String isShow) {
		this.isShow = isShow;
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
	@Transient
	public String getFieldVal() {
		return fieldVal;
	}
	public void setFieldVal(String fieldVal) {
		this.fieldVal = fieldVal;
	}
	@Column(name = "DICT_GROUP")
	public String getDictGroup() {
		return dictGroup;
	}
	public void setDictGroup(String dictGroup) {
		this.dictGroup = dictGroup;
	}
	@Override
	public String toString() {
		return "BvVoutypeDataField [vouTid=" + vouTid + ", fieldCode=" + fieldCode + ", fieldName=" + fieldName
				+ ", dataType=" + dataType + ", dictGroup=" + dictGroup + ", isShow=" + isShow + ", validSign="
				+ validSign + ", ordernum=" + ordernum + ", fieldVal=" + fieldVal + "]";
	}
}
