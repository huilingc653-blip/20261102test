package com.bytter.biz.finacial.interest.entity;
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
 * @Description: FC_LOAN_BALANCE_DTL
 * @author onlineGenerator
 * @date 2017-08-24 19:36:05
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_LOAN_BALANCE_DTL", schema = "")
@SuppressWarnings("serial")
public class FcLoanBalanceDtl extends IdEntity implements java.io.Serializable {
	/**积数ID*/
	private java.lang.String balanceId;
	/**系统代码*/
	private java.lang.String sysCode;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**创建时间*/
	private java.util.Date createTime;
	/**还款ID*/
	private java.lang.String loanRepayId;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  积数ID
	 */
	@Column(name ="BALANCE_ID")
	public java.lang.String getBalanceId(){
		return this.balanceId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  积数ID
	 */
	public void setBalanceId(java.lang.String balanceId){
		this.balanceId = balanceId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统代码
	 */
	@Column(name ="SYS_CODE")
	public java.lang.String getSysCode(){
		return this.sysCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统代码
	 */
	public void setSysCode(java.lang.String sysCode){
		this.sysCode = sysCode;
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
	 *@return: java.lang.String  还款ID
	 */
	@Column(name ="LOAN_REPAY_ID")
	public java.lang.String getLoanRepayId(){
		return this.loanRepayId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  还款ID
	 */
	public void setLoanRepayId(java.lang.String loanRepayId){
		this.loanRepayId = loanRepayId;
	}
}
