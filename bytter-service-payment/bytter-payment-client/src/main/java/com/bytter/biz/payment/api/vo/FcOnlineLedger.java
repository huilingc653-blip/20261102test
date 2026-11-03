package com.bytter.biz.payment.api.vo;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_ONLINE_LEDGER
 * @author onlineGenerator
 * @date 2017-06-07 10:03:54
 * @version 1.0
 *
 */
public class FcOnlineLedger extends IdEntity implements java.io.Serializable {
	/**账号*/
	private java.lang.String account;
	/**币别代码*/
	private java.lang.String curCode;
	/**记账总笔数*/
	private java.lang.Integer tallyCnt;
	/**复核总笔数*/
	private java.lang.Integer checkCnt;
	/**记账联机余额*/
	private java.math.BigDecimal onlineBalance;
	/**复核联机余额*/
	private java.math.BigDecimal checkBalance;
	/**当前冻结金额*/
	private java.math.BigDecimal freezeMoney;
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
	 *@return: java.lang.String  账号
	 */
	//@Column(name ="ACCOUNT",nullable=false,length=50)
	public java.lang.String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  账号
	 */
	public void setAccount(java.lang.String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	//@Column(name ="CUR_CODE",nullable=true,length=32)
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  记账总笔数
	 */
	//@Column(name ="TALLY_CNT",nullable=true,length=10)
	public java.lang.Integer getTallyCnt(){
		return this.tallyCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  记账总笔数
	 */
	public void setTallyCnt(java.lang.Integer tallyCnt){
		this.tallyCnt = tallyCnt;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  复核总笔数
	 */
	//@Column(name ="CHECK_CNT",nullable=true,length=10)
	public java.lang.Integer getCheckCnt(){
		return this.checkCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  复核总笔数
	 */
	public void setCheckCnt(java.lang.Integer checkCnt){
		this.checkCnt = checkCnt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  记账联机余额
	 */
	//@Column(name ="ONLINE_BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getOnlineBalance(){
		return this.onlineBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  记账联机余额
	 */
	public void setOnlineBalance(java.math.BigDecimal onlineBalance){
		this.onlineBalance = onlineBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  复核联机余额
	 */
	//@Column(name ="CHECK_BALANCE",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getCheckBalance(){
		return this.checkBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  复核联机余额
	 */
	public void setCheckBalance(java.math.BigDecimal checkBalance){
		this.checkBalance = checkBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  当前冻结金额
	 */
	//@Column(name ="FREEZE_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getFreezeMoney(){
		return this.freezeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  当前冻结金额
	 */
	public void setFreezeMoney(java.math.BigDecimal freezeMoney){
		this.freezeMoney = freezeMoney;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	//@Column(name ="CREATE_TIME",nullable=true)
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
	//@Column(name ="CREATE_BY",nullable=true,length=32)
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
	//@Column(name ="UPDATE_TIME",nullable=true)
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
	//@Column(name ="UPDATE_BY",nullable=true,length=32)
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
