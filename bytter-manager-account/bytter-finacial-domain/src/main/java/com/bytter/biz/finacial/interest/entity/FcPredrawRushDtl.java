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
 * @Description: 冲预提信息表
 * @author onlineGenerator
 * @date 2017-07-19 16:26:26
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_PREDRAW_RUSH_DTL", schema = "")
@SuppressWarnings("serial")
public class FcPredrawRushDtl extends IdEntity implements java.io.Serializable {
	/**预提ID*/
	private java.lang.String predrawDtlId;
	/**冲预提日期*/
	private java.util.Date strikeDate;
	/**冲预提利息*/
	private java.math.BigDecimal strikeInterest;
	/**操作人*/
	private java.lang.String operatorCode;
	/**状态*/
	private java.lang.Integer status;
	/**冲预提流水号*/
	private java.lang.String orderNo;
	/**取款主键ID*/
	private java.lang.String depositManagerId;
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
	 *@return: java.lang.String  预提ID
	 */
	@Column(name ="PREDRAW_DTL_ID")
	public java.lang.String getPredrawDtlId(){
		return this.predrawDtlId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  预提ID
	 */
	public void setPredrawDtlId(java.lang.String predrawDtlId){
		this.predrawDtlId = predrawDtlId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  冲预提日期
	 */
	@Column(name ="STRIKE_DATE")
	public java.util.Date getStrikeDate(){
		return this.strikeDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  冲预提日期
	 */
	public void setStrikeDate(java.util.Date strikeDate){
		this.strikeDate = strikeDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  冲预提利息
	 */
	@Column(name ="STRIKE_INTEREST")
	public java.math.BigDecimal getStrikeInterest(){
		return this.strikeInterest;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  冲预提利息
	 */
	public void setStrikeInterest(java.math.BigDecimal strikeInterest){
		this.strikeInterest = strikeInterest;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  操作人
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  操作人
	 */
	public void setOperatorCode(java.lang.String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS")
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
	 *@return: java.lang.String  冲预提流水号
	 */
	@Column(name ="ORDER_NO")
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  冲预提流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  取款主键ID
	 */
	@Column(name ="DEPOSIT_MANAGER_ID")
	public java.lang.String getDepositManagerId(){
		return this.depositManagerId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  取款主键ID
	 */
	public void setDepositManagerId(java.lang.String depositManagerId){
		this.depositManagerId = depositManagerId;
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
