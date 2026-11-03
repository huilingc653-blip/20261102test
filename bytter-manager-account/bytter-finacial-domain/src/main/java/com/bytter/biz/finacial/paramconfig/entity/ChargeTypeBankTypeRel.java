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
 * @Description: 费用类型与银行类型关联表
 * @author onlineGenerator
 * @date 2017-05-23 11:50:22
 * @version 1.0
 *
 */
@Entity
@Table(name = "CHARGE_TYPE_BANK_TYPE_REL", schema = "")
@SuppressWarnings("serial")
public class ChargeTypeBankTypeRel extends IdEntity implements java.io.Serializable {
	/**费用类别ID*/
	private java.lang.String chargeTypeId;
	/**银行类别*/
	private java.lang.String bankType;
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
	 *@return: java.lang.String  银行类别
	 */
	@Column(name ="BANK_TYPE")
	public java.lang.String getBankType(){
		return this.bankType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行类别
	 */
	public void setBankType(java.lang.String bankType){
		this.bankType = bankType;
	}
}
