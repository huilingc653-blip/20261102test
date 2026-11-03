package com.bytter.bill.domain.bill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**   
 * @Title: Entity
 * @Description: 票据中间表
 * @author onlineGenerator
 * @date 2017-11-13 11:00:24
 * @version 1.0
 *
 */
@Entity
@Table(name = "AB_BILL_MIDDLE", schema = "")
@SuppressWarnings("serial")
public class AbBillMiddle extends IdEntity implements java.io.Serializable {
	/**业务ID*/
	private String buId;
	/**中间表类型:1票据签收消息提醒2 票据到期提醒3 到期拒付消息提醒4 收到待清偿数据提醒5 票据到期未兑付提醒6 线上电票提示收票完成补录提醒*/
	private String type;
	/**处理标志：0不可用；1可用*/
	private String validSign;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务ID
	 */
	@Column(name ="BU_ID",nullable=true,length=32)
	public String getBuId(){
		return this.buId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务ID
	 */
	public void setBuId(String buId){
		this.buId = buId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  中间表类型:1票据签收消息提醒2 票据到期提醒3 到期拒付消息提醒4 收到待清偿数据提醒5 票据到期未兑付提醒6 线上电票提示收票完成补录提醒
	 */
	@Column(name ="TYPE",nullable=true,length=10)
	public String getType(){
		return this.type;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  中间表类型:1票据签收消息提醒2 票据到期提醒3 到期拒付消息提醒4 收到待清偿数据提醒5 票据到期未兑付提醒6 线上电票提示收票完成补录提醒
	 */
	public void setType(String type){
		this.type = type;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  处理标志：0不可用；1可用
	 */
	@Column(name ="VALID_SIGN",nullable=true,length=1)
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  处理标志：0不可用；1可用
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
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
}
