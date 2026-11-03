package com.bytter.biz.finacial.closedeal.entity;
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
 * @Description: FC_CASH_FLOW
 * @author onlineGenerator
 * @date 2017-07-17 18:36:08
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CASH_FLOW", schema = "")
@SuppressWarnings("serial")
public class FcCashFlow extends IdEntity implements java.io.Serializable {
	/**日期*/
	private java.util.Date tallyDate;
	/**网点*/
	private java.lang.String netCode;
	/**币别*/
	private java.lang.String curCode;
	/**项目*/
	private java.lang.String cashItem;
	/**金额*/
	private java.math.BigDecimal money;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日期
	 */
	@Column(name ="TALLY_DATE")
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日期
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  网点
	 */
	@Column(name ="NET_CODE")
	public java.lang.String getNetCode(){
		return this.netCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  网点
	 */
	public void setNetCode(java.lang.String netCode){
		this.netCode = netCode;
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
	 *@return: java.lang.String  项目
	 */
	@Column(name ="CASH_ITEM")
	public java.lang.String getCashItem(){
		return this.cashItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目
	 */
	public void setCashItem(java.lang.String cashItem){
		this.cashItem = cashItem;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="MONEY")
	public java.math.BigDecimal getMoney(){
		return this.money;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setMoney(java.math.BigDecimal money){
		this.money = money;
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
