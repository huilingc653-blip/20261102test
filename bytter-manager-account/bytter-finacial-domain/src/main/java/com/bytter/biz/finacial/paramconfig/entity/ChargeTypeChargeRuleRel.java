package com.bytter.biz.finacial.paramconfig.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 费用类型与费用计提维护
 * @author onlineGenerator
 * @date 2017-05-22 19:25:29
 * @version 1.0
 *
 */
@Entity
@Table(name = "CHARGE_TYPE_CHARGE_RULE_REL", schema = "")
@SuppressWarnings("serial")
public class ChargeTypeChargeRuleRel extends IdEntity implements java.io.Serializable {
	/**费用类别ID*/
	private java.lang.String chargeTypeId;
	/**计提ID*/
	private java.lang.String chargeRuleId;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计提ID
	 */
	@Column(name ="CHARGE_RULE_ID")
	public java.lang.String getChargeRuleId(){
		return this.chargeRuleId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计提ID
	 */
	public void setChargeRuleId(java.lang.String chargeRuleId){
		this.chargeRuleId = chargeRuleId;
	}
}
