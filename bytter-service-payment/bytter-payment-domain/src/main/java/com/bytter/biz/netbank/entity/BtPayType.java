package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 系统结算方式表
 * @author onlineGenerator
 * @date 2017-05-12 13:44:54
 * @version V1.0
 *
 */
@Entity
@Table(name = "BT_PAY_TYPE", schema = "")
@SuppressWarnings("serial")
public class BtPayType extends IdEntity implements java.io.Serializable {
	/**结算方式编码*/
	private java.lang.String sysVoucherCode;
	/**结算方式名称*/
	private java.lang.String sysVoucherName;
	/**备注*/
	private java.lang.String rmk;
	/**有效标志*/
	private java.lang.String validSign;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结算方式编码
	 */
	@Column(name ="SYS_VOUCHER_CODE",nullable=false,length=2)
	public java.lang.String getSysVoucherCode(){
		return this.sysVoucherCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结算方式编码
	 */
	public void setSysVoucherCode(java.lang.String sysVoucherCode){
		this.sysVoucherCode = sysVoucherCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结算方式名称
	 */
	@Column(name ="SYS_VOUCHER_NAME",nullable=false,length=20)
	public java.lang.String getSysVoucherName(){
		return this.sysVoucherName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结算方式名称
	 */
	public void setSysVoucherName(java.lang.String sysVoucherName){
		this.sysVoucherName = sysVoucherName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=40)
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
	}
}
