package com.bytter.biz.budget.entity;
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
 * @Description: FBS_BUDGET_SUPERADD_DETAIL
 * @author onlineGenerator
 * @date 2017-07-28 11:36:59
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET_SUPERADD_DETAIL", schema = "")
@SuppressWarnings("serial")
public class FbsBudgetSuperaddDetail extends IdEntity implements java.io.Serializable {
	/**计划追加ID*/
	private java.lang.String superaddId;
	/**科目代码*/
	private java.lang.String itemCode;
	/**科目名称*/
	private java.lang.String itemName;
	/**追加金额*/
	private java.math.BigDecimal bsuperMoney;
	/**备注*/
	private String rmk;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计划追加ID
	 */
	@Column(name ="SUPERADD_ID",nullable=true,length=32)
	public java.lang.String getSuperaddId(){
		return this.superaddId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计划追加ID
	 */
	public void setSuperaddId(java.lang.String superaddId){
		this.superaddId = superaddId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目代码
	 */
	@Column(name ="ITEM_CODE",nullable=false,length=40)
	public java.lang.String getItemCode(){
		return this.itemCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目代码
	 */
	public void setItemCode(java.lang.String itemCode){
		this.itemCode = itemCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目名称
	 */
	@Column(name ="ITEM_NAME",nullable=true,length=100)
	public java.lang.String getItemName(){
		return this.itemName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目名称
	 */
	public void setItemName(java.lang.String itemName){
		this.itemName = itemName;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  追加金额
	 */
	@Column(name ="BSUPER_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getBsuperMoney(){
		return this.bsuperMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  追加金额
	 */
	public void setBsuperMoney(java.math.BigDecimal bsuperMoney){
		this.bsuperMoney = bsuperMoney;
	}
	@Column(name ="RMK")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
}
