package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 银行中心余额初始表
 * @author onlineGenerator
 * @date 2017-05-31 11:15:24
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CHECK_INIT", schema = "")
@SuppressWarnings("serial")
public class FcCheckInit extends IdEntity implements java.io.Serializable {
	/**对账类型*/
	private String checkType;
	/**企业账号*/
	private String account;
	/**银行账号*/
	private String bankAcc;
	/**币别代码*/
	private String curId;
	/**对账初始日期*/
	private Date checkDate;
	/**企业余额*/
	private BigDecimal blnBalance;
	/**银行余额*/
	private BigDecimal bankBalance;
	/**备注*/
	private String rmk;
	/**结转日期*/
	private Date carryDate;
	/**最后更新用户代码*/
	private String updateBy;
	/**最后更新时间*/
	private Date updateTime;
	/**创建用户代码*/
	private String createBy;
	/**创建时间*/
	private Date createTime;
	/**中心账号账号名称*/
	private String accountAccName;
	/**银行账号名称*/
	private String bankAccName;
	/**银行账号开户行*/
	private String bankName;
	/**网点id*/
	private String netId;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  对账类型
	 */
	@Column(name ="CHECK_TYPE")
	public String getCheckType(){
		return this.checkType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  对账类型
	 */
	public void setCheckType(String checkType){
		this.checkType = checkType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  企业账号
	 */
	@Column(name ="ACCOUNT")
	public String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  企业账号
	 */
	public void setAccount(String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行账号
	 */
	@Column(name ="BANK_ACC")
	public String getBankAcc(){
		return this.bankAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行账号
	 */
	public void setBankAcc(String bankAcc){
		this.bankAcc = bankAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_ID")
	public String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  对账初始日期
	 */
	@Column(name ="CHECK_DATE")
	public Date getCheckDate(){
		return this.checkDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  对账初始日期
	 */
	public void setCheckDate(Date checkDate){
		this.checkDate = checkDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  企业余额
	 */
	@Column(name ="BLN_BALANCE")
	public BigDecimal getBlnBalance(){
		return this.blnBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  企业余额
	 */
	public void setBlnBalance(BigDecimal blnBalance){
		this.blnBalance = blnBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  银行余额
	 */
	@Column(name ="BANK_BALANCE")
	public BigDecimal getBankBalance(){
		return this.bankBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  银行余额
	 */
	public void setBankBalance(BigDecimal bankBalance){
		this.bankBalance = bankBalance;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
	public String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结转日期
	 */
	@Column(name ="CARRY_DATE")
	public Date getCarryDate(){
		return this.carryDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结转日期
	 */
	public void setCarryDate(Date carryDate){
		this.carryDate = carryDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
}
