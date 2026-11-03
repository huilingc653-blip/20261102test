package com.bytter.system.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 银行账号关系策略维护表
 * @author onlineGenerator
 * @date 2017-06-20 16:44:38
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_ACC_GATHER_POLICY", schema = "")
@SuppressWarnings("serial")
public class BisAccGatherPolicy extends IdEntity implements java.io.Serializable {
	/**账号*/
	private java.lang.String bankAccId;
	/**调拨策略*/
	private java.lang.String gatherPolicyId;
	/**接口代码*/
	private java.lang.String bifCode;
	/**父账号*/
	private java.lang.String parentAccId;
	/**首次归集时间*/
	private java.util.Date firstTime;
	/**下次归集时间*/
	private java.util.Date nextTime;
	/**下次下拔时间*/
	private java.util.Date appNextTime;
	/**开户行行号*/
	private java.lang.String bankSourcecode;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  账号
	 */
	@Column(name ="BANK_ACC_ID",nullable=true,length=32)
	public java.lang.String getBankAccId(){
		return this.bankAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setBankAccId(java.lang.String bankAccId){
		this.bankAccId = bankAccId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  调拨策略
	 */
	@Column(name ="GATHER_POLICY_ID",nullable=false,length=32)
	public java.lang.String getGatherPolicyId(){
		return this.gatherPolicyId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  调拨策略
	 */
	public void setGatherPolicyId(java.lang.String gatherPolicyId){
		this.gatherPolicyId = gatherPolicyId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  接口代码
	 */
	@Column(name ="BIF_CODE",nullable=true,length=32)
	public java.lang.String getBifCode(){
		return this.bifCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  接口代码
	 */
	public void setBifCode(java.lang.String bifCode){
		this.bifCode = bifCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  父账号
	 */
	@Column(name ="PARENT_ACC_ID",nullable=true,length=32)
	public java.lang.String getParentAccId(){
		return this.parentAccId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  父账号
	 */
	public void setParentAccId(java.lang.String parentAccId){
		this.parentAccId = parentAccId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  首次归集时间
	 */
	@Column(name ="FIRST_TIME",nullable=false)
	public java.util.Date getFirstTime(){
		return this.firstTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  首次归集时间
	 */
	public void setFirstTime(java.util.Date firstTime){
		this.firstTime = firstTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  下次归集时间
	 */
	@Column(name ="NEXT_TIME",nullable=true)
	public java.util.Date getNextTime(){
		return this.nextTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  下次归集时间
	 */
	public void setNextTime(java.util.Date nextTime){
		this.nextTime = nextTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  下次下拔时间
	 */
	@Column(name ="APP_NEXT_TIME",nullable=true)
	public java.util.Date getAppNextTime(){
		return this.appNextTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  下次下拔时间
	 */
	public void setAppNextTime(java.util.Date appNextTime){
		this.appNextTime = appNextTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  开户行行号
	 */
	@Column(name ="BANK_SOURCECODE",nullable=true,length=30)
	public java.lang.String getBankSourcecode(){
		return this.bankSourcecode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  开户行行号
	 */
	public void setBankSourcecode(java.lang.String bankSourcecode){
		this.bankSourcecode = bankSourcecode;
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
