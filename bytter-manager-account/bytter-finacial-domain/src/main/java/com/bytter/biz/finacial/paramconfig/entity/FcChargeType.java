package com.bytter.biz.finacial.paramconfig.entity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 费用类型表
 * @author onlineGenerator
 * @date 2017-05-15 15:14:21
 * @version V1.0
 *
 */
@Entity
@Table(name = "FC_CHARGE_TYPE", schema = "")
@SuppressWarnings("serial")
public class FcChargeType extends IdEntity implements java.io.Serializable {
	/**费用代码*/
	private java.lang.String chargeTypeCode;
	/**费用名称*/
	private java.lang.String chargeTypeName;
	/**币别*/
	private java.lang.String curCode;
	/**加急标志*/
	private java.lang.String emergencySign;
	/**跨行标志*/
	private java.lang.String difBank;
	/**地域标志*/
	private java.lang.String areaSign;
	/**收取费用*/
	private java.math.BigDecimal charge;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  费用代码
	 */
	@Column(name ="CHARGE_TYPE_CODE")
	public java.lang.String getChargeTypeCode(){
		return this.chargeTypeCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  费用代码
	 */
	public void setChargeTypeCode(java.lang.String chargeTypeCode){
		this.chargeTypeCode = chargeTypeCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  费用名称
	 */
	@Column(name ="CHARGE_TYPE_NAME")
	public java.lang.String getChargeTypeName(){
		return this.chargeTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  费用名称
	 */
	public void setChargeTypeName(java.lang.String chargeTypeName){
		this.chargeTypeName = chargeTypeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加急标志
	 */
	@Column(name ="EMERGENCY_SIGN")
	public java.lang.String getEmergencySign(){
		return this.emergencySign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加急标志
	 */
	public void setEmergencySign(java.lang.String emergencySign){
		this.emergencySign = emergencySign;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  跨行标志
	 */
	@Column(name ="DIF_BANK")
	public java.lang.String getDifBank() {
		return difBank;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  跨行标志
	 */
	public void setDifBank(java.lang.String difBank) {
		this.difBank = difBank;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  地域标志
	 */
	@Column(name ="AREA_SIGN")
	public java.lang.String getAreaSign() {
		return areaSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  地域标志
	 */
	public void setAreaSign(java.lang.String areaSign) {
		this.areaSign = areaSign;
	}
	
	@Column(name ="CHARGE")
	public java.math.BigDecimal getCharge() {
		return charge;
	}
	public void setCharge(java.math.BigDecimal charge) {
		this.charge = charge;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
}
