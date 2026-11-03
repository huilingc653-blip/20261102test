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
 * @Description: FC_INCOME_STATEMENT
 * @author onlineGenerator
 * @date 2017-07-17 18:37:10
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_INCOME_STATEMENT", schema = "")
@SuppressWarnings("serial")
public class FcIncomeStatement extends IdEntity implements java.io.Serializable {
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**币别*/
	private java.lang.String curCode;
	/**网点*/
	private java.lang.String netCode;
	/**年月*/
	private java.lang.String yearMonth2;
	/**行次*/
	private java.lang.Integer orderNo;
	/**项目*/
	private java.lang.String item;
	/**本期数*/
	private java.math.BigDecimal balance;
	/**本年累计数*/
	private java.math.BigDecimal yearTotal;
	/**创建时间*/
	private java.util.Date createTime;
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
	 *@return: java.lang.String  年月
	 */
	@Column(name ="YEAR_MONTH2")
	public java.lang.String getYearMonth2(){
		return this.yearMonth2;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  年月
	 */
	public void setYearMonth2(java.lang.String yearMonth2){
		this.yearMonth2 = yearMonth2;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  行次
	 */
	@Column(name ="ORDER_NO")
	public java.lang.Integer getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  行次
	 */
	public void setOrderNo(java.lang.Integer orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  项目
	 */
	@Column(name ="ITEM")
	public java.lang.String getItem(){
		return this.item;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  项目
	 */
	public void setItem(java.lang.String item){
		this.item = item;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  本期数
	 */
	@Column(name ="BALANCE")
	public java.math.BigDecimal getBalance(){
		return this.balance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  本期数
	 */
	public void setBalance(java.math.BigDecimal balance){
		this.balance = balance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  本年累计数
	 */
	@Column(name ="YEAR_TOTAL")
	public java.math.BigDecimal getYearTotal(){
		return this.yearTotal;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  本年累计数
	 */
	public void setYearTotal(java.math.BigDecimal yearTotal){
		this.yearTotal = yearTotal;
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
