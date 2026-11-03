package com.bytter.biz.finacial.closedeal.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_DAY_CLOSE
 * @author onlineGenerator
 * @date 2017-07-06 14:51:02
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_DAY_CLOSE", schema = "")
@SuppressWarnings("serial")
public class FcDayClose extends IdEntity implements java.io.Serializable {
	/**上次日结日期*/
	private java.util.Date lastCloseDate;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**所属网点*/
	private java.lang.String netCode;
	
	/**结转类型*/
	private String overType;
	
	/**日结日期*/
	private java.util.Date closeDate;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  上次日结日期
	 */
	@Column(name ="LAST_CLOSE_DATE")
	public java.util.Date getLastCloseDate(){
		return this.lastCloseDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  上次日结日期
	 */
	public void setLastCloseDate(java.util.Date lastCloseDate){
		this.lastCloseDate = lastCloseDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
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
	@Temporal(TemporalType.TIMESTAMP)
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
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_CODE")
	public java.lang.String getNetCode(){
		return this.netCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetCode(java.lang.String netCode){
		this.netCode = netCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结转类型
	 */
	@Column(name ="OVER_TYPE")
	public java.lang.String getOverType(){
		return this.overType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结转类型
	 */
	public void setOverType(java.lang.String overType){
		this.overType = overType;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  日结日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name ="CLOSE_DATE")
	public java.util.Date getCloseDate(){
		return this.closeDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  日结日期
	 */
	public void setCloseDate(java.util.Date closeDate){
		this.closeDate = closeDate;
	}
}
