package com.bytter.biz.finacial.paramconfig.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;
import javax.xml.soap.Text;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 费用类型明细
 * @author onlineGenerator
 * @date 2017-05-19 11:10:13
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CHARGE_TYPE_DTL", schema = "")
@SuppressWarnings("serial")
public class FcChargeTypeDtl extends IdEntity implements java.io.Serializable {
	/**费用类别ID*/
	private java.lang.String chargeTypeId;
	/**下限金额*/
	private java.math.BigDecimal minRangeMoney;
	/**上限金额*/
	private java.math.BigDecimal maxRangeMoney;
	/**计算方法*/
	private java.lang.Integer calcType;
	/**比例*/
	private java.math.BigDecimal percent;
	/**固定费用*/
	private java.math.BigDecimal fixChargeMoney;
	/**最多收费*/
	private java.math.BigDecimal maxCharge;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  费用类别ID
	 */
	@Column(name ="CHARGE_TYPE_ID")
	public java.lang.String getChargeTypeId(){
		return this.chargeTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  费用类别ID
	 */
	public void setChargeTypeId(java.lang.String chargeTypeId){
		this.chargeTypeId = chargeTypeId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  下限金额
	 */
	@Column(name ="MIN_RANGE_MONEY")
	public java.math.BigDecimal getMinRangeMoney(){
		return this.minRangeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  下限金额
	 */
	public void setMinRangeMoney(java.math.BigDecimal minRangeMoney){
		this.minRangeMoney = minRangeMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  上限金额
	 */
	@Column(name ="MAX_RANGE_MONEY")
	public java.math.BigDecimal getMaxRangeMoney(){
		return this.maxRangeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  上限金额
	 */
	public void setMaxRangeMoney(java.math.BigDecimal maxRangeMoney){
		this.maxRangeMoney = maxRangeMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  计算方法
	 */
	@Column(name ="CALC_TYPE")
	public java.lang.Integer getCalcType(){
		return this.calcType;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  计算方法
	 */
	public void setCalcType(java.lang.Integer calcType){
		this.calcType = calcType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  比例
	 */
	@Column(name ="PERCENT")
	public java.math.BigDecimal getPercent(){
		return this.percent;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  比例
	 */
	public void setPercent(java.math.BigDecimal percent){
		this.percent = percent;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  固定费用
	 */
	@Column(name ="FIX_CHARGE_MONEY")
	public java.math.BigDecimal getFixChargeMoney(){
		return this.fixChargeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  固定费用
	 */
	public void setFixChargeMoney(java.math.BigDecimal fixChargeMoney){
		this.fixChargeMoney = fixChargeMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  最多收费
	 */
	@Column(name ="MAX_CHARGE")
	public java.math.BigDecimal getMaxCharge(){
		return this.maxCharge;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  最多收费
	 */
	public void setMaxCharge(java.math.BigDecimal maxCharge){
		this.maxCharge = maxCharge;
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
}
