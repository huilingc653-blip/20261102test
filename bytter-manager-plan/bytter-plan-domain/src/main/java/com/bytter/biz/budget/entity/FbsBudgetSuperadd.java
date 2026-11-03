package com.bytter.biz.budget.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FBS_BUDGET_SUPERADD
 * @author onlineGenerator
 * @date 2017-07-28 11:36:40
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_BUDGET_SUPERADD", schema = "")
@SuppressWarnings("serial")
public class FbsBudgetSuperadd extends IdEntity implements java.io.Serializable {
	/**计划ID*/
	private java.lang.String budgetId;
	/**单位ID*/
	private java.lang.String corpId;
	/**需审批金额*/
	private java.math.BigDecimal checkMoney;
	/**状态*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**附件ID*/
	private java.lang.String attachmentId;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计划ID
	 */
	@Column(name ="BUDGET_ID",nullable=true,length=32)
	public java.lang.String getBudgetId(){
		return this.budgetId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计划ID
	 */
	public void setBudgetId(java.lang.String budgetId){
		this.budgetId = budgetId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  需审批金额
	 */
	@Column(name ="CHECK_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getCheckMoney(){
		return this.checkMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  需审批金额
	 */
	public void setCheckMoney(java.math.BigDecimal checkMoney){
		this.checkMoney = checkMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
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
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  附件ID
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public java.lang.String getAttachmentId() {
		return attachmentId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  附件ID
	 */
	public void setAttachmentId(java.lang.String attachmentId) {
		this.attachmentId = attachmentId;
	}
}
