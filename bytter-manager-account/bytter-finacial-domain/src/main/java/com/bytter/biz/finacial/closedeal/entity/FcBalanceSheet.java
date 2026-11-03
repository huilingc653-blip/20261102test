package com.bytter.biz.finacial.closedeal.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_BALANCE_SHEET
 * @author onlineGenerator
 * @date 2017-07-17 18:39:24
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_BALANCE_SHEET", schema = "")
@SuppressWarnings("serial")
public class FcBalanceSheet extends IdEntity implements java.io.Serializable {
	/**yearMonth*/
	private java.lang.String yearMonth2;
	/**网点*/
	private java.lang.String netCode;
	/**币别*/
	private java.lang.String curCode;
	/**资产项目名*/
	private java.lang.String aItem;
	/**资产行次*/
	private java.lang.Integer aOrderNo;
	/**资产年初数*/
	private java.math.BigDecimal aYearBalance;
	/**资产期末数*/
	private java.math.BigDecimal aMonthBalance;
	/**负债项目名*/
	private java.lang.String lItem;
	/**负债行次*/
	private java.lang.Integer lOrderNo;
	/**负债年初数*/
	private java.math.BigDecimal lYearBalance;
	/**负债期末数*/
	private java.math.BigDecimal lMonthBalance;
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
	 *@return: java.lang.String  yearMonth
	 */
	@Column(name ="YEAR_MONTH2")
	public java.lang.String getYearMonth2(){
		return this.yearMonth2;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  yearMonth
	 */
	public void setYearMonth2(java.lang.String yearMonth2){
		this.yearMonth2 = yearMonth2;
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
	 *@return: java.lang.String  资产项目名
	 */
	@Column(name ="A_ITEM")
	public java.lang.String getAItem(){
		return this.aItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  资产项目名
	 */
	public void setAItem(java.lang.String aItem){
		this.aItem = aItem;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  资产行次
	 */
	@Column(name ="A_ORDER_NO")
	public java.lang.Integer getAOrderNo(){
		return this.aOrderNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  资产行次
	 */
	public void setAOrderNo(java.lang.Integer aOrderNo){
		this.aOrderNo = aOrderNo;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  资产年初数
	 */
	@Column(name ="A_YEAR_BALANCE")
	public java.math.BigDecimal getAYearBalance(){
		return this.aYearBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  资产年初数
	 */
	public void setAYearBalance(java.math.BigDecimal aYearBalance){
		this.aYearBalance = aYearBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  资产期末数
	 */
	@Column(name ="A_MONTH_BALANCE")
	public java.math.BigDecimal getAMonthBalance(){
		return this.aMonthBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  资产期末数
	 */
	public void setAMonthBalance(java.math.BigDecimal aMonthBalance){
		this.aMonthBalance = aMonthBalance;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  负债项目名
	 */
	@Column(name ="L_ITEM")
	public java.lang.String getLItem(){
		return this.lItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  负债项目名
	 */
	public void setLItem(java.lang.String lItem){
		this.lItem = lItem;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  负债行次
	 */
	@Column(name ="L_ORDER_NO")
	public java.lang.Integer getLOrderNo(){
		return this.lOrderNo;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  负债行次
	 */
	public void setLOrderNo(java.lang.Integer lOrderNo){
		this.lOrderNo = lOrderNo;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  负债年初数
	 */
	@Column(name ="L_YEAR_BALANCE")
	public java.math.BigDecimal getLYearBalance(){
		return this.lYearBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  负债年初数
	 */
	public void setLYearBalance(java.math.BigDecimal lYearBalance){
		this.lYearBalance = lYearBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  负债期末数
	 */
	@Column(name ="L_MONTH_BALANCE")
	public java.math.BigDecimal getLMonthBalance(){
		return this.lMonthBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  负债期末数
	 */
	public void setLMonthBalance(java.math.BigDecimal lMonthBalance){
		this.lMonthBalance = lMonthBalance;
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
