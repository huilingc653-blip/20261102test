package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @Title: Entity
 * @Description: 自定义付款方式表
 * @author onlineGenerator
 * @date 2017-05-12 17:18:41
 * @version V1.0
 *
 */
@ApiModel("自定义付款方式表")
@Entity
@Table(name = "FC_VOUCHER_TYPE", schema = "")
@SuppressWarnings("serial")
public class FcVoucherType extends IdEntity implements java.io.Serializable {
	/**付款方式编码*/
	@ApiModelProperty("付款方式编码")
	private java.lang.String voucherType;

	/**付款方式名称*/
	@ApiModelProperty("付款方式名称")
	private java.lang.String voucherName;

	/**系统结算方式*/
	@ApiModelProperty("系统结算方式")
	private java.lang.String sysVoucherCode;

	/**现金转账标志*/
	@ApiModelProperty("现金转账标志")
	private java.lang.String cashTransSign;

	/**结算类型*/
	@ApiModelProperty("结算类型")
	private java.lang.String settlType;

	/**是否走银企接口*/
	@ApiModelProperty("是否走银企接口")
	private java.lang.String isElectronPay;

	/**是否记账*/
	@ApiModelProperty("是否记账")
	private java.lang.String isAccount;

	/**是否挂资金计划*/
	@ApiModelProperty("是否挂资金计划")
	private java.lang.String isBudget;

	/**指令发送模式*/
	@ApiModelProperty("指令发送模式")
	private java.lang.String sendMode;

	/**备注*/
	@ApiModelProperty("备注")
	private java.lang.String rmk;

	/**是否系统付款方式*/
	@ApiModelProperty("是否系统付款方式")
	private java.lang.String isSystem;

	/**有效标志*/
	@ApiModelProperty("有效标志")
	private java.lang.String validSign;

	/**创建人登录名称*/
	@ApiModelProperty("创建人登录名称")
	private java.lang.String createBy;

	/**创建日期*/
	@ApiModelProperty("创建日期")
	private java.util.Date createDate;

	/**更新人登录名称*/
	@ApiModelProperty("更新人登录名称")
	private java.lang.String updateBy;

	/**更新日期*/
	@ApiModelProperty("更新日期")
	private java.util.Date updateDate;

	/**开票区间控制标识 1-三个月 2-六个月*/
	@ApiModelProperty("开票区间控制标识 1-三个月 2-六个月")
	private java.lang.String billSectionControl;
	@Column(name ="BIll_SECTION_CONTROL",nullable=true,length=1)
	public String getBillSectionControl() {
		return billSectionControl;
	}

	public void setBillSectionControl(String billSectionControl) {
		this.billSectionControl = billSectionControl;
	}

	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款方式编码
	 */
	@Column(name ="VOUCHER_TYPE",nullable=false,length=2)
	public java.lang.String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款方式编码
	 */
	public void setVoucherType(java.lang.String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款方式名称
	 */
	@Column(name ="VOUCHER_NAME",nullable=false,length=20)
	public java.lang.String getVoucherName(){
		return this.voucherName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款方式名称
	 */
	public void setVoucherName(java.lang.String voucherName){
		this.voucherName = voucherName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统结算方式
	 */
	@Column(name ="SYS_VOUCHER_CODE",nullable=true,length=2)
	public java.lang.String getSysVoucherCode(){
		return this.sysVoucherCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统结算方式
	 */
	public void setSysVoucherCode(java.lang.String sysVoucherCode){
		this.sysVoucherCode = sysVoucherCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  现金转账标志
	 */
	@Column(name ="CASH_TRANS_SIGN",nullable=true,length=1)
	public java.lang.String getCashTransSign(){
		return this.cashTransSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  现金转账标志
	 */
	public void setCashTransSign(java.lang.String cashTransSign){
		this.cashTransSign = cashTransSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结算类型
	 */
	@Column(name ="SETTL_TYPE",nullable=true,length=1)
	public java.lang.String getSettlType(){
		return this.settlType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结算类型
	 */
	public void setSettlType(java.lang.String settlType){
		this.settlType = settlType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否走银企接口
	 */
	@Column(name ="IS_ELECTRON_PAY",nullable=true,length=1)
	public java.lang.String getIsElectronPay(){
		return this.isElectronPay;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否走银企接口
	 */
	public void setIsElectronPay(java.lang.String isElectronPay){
		this.isElectronPay = isElectronPay;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否记账
	 */
	@Column(name ="IS_ACCOUNT",nullable=true,length=1)
	public java.lang.String getIsAccount(){
		return this.isAccount;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否记账
	 */
	public void setIsAccount(java.lang.String isAccount){
		this.isAccount = isAccount;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String   是否挂预算
	 */
	@Column(name ="IS_BUDGET",nullable=true,length=1)
	public java.lang.String getIsBudget(){
		return this.isBudget;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String   是否挂预算
	 */
	public void setIsBudget(java.lang.String isBudget){
		this.isBudget = isBudget;
	}
    @Column(name ="SEND_MODE",nullable=true,length=1)
    public String getSendMode() {
        return sendMode;
    }
    public void setSendMode(String sendMode) {
        this.sendMode = sendMode;
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
	 *@return: java.lang.String  是否系统付款方式
	 */
	@Column(name ="IS_SYSTEM",nullable=true,length=1)
	public java.lang.String getIsSystem(){
		return this.isSystem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否系统付款方式
	 */
	public void setIsSystem(java.lang.String isSystem){
		this.isSystem = isSystem;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
}
