package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 票据池融资申请合同中间表
 * @author zhengg
 * @date 2020-02-24 14:49:13
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_CONTRACT", schema = "")
@SuppressWarnings("serial")
public class AbPoolContract extends IdEntity implements java.io.Serializable {
	/**票据池明细表ID*/
	private String dtlId;
	/**指令状态：0待确认1已确认2重新生成*/
	private String status;
	/**conFtype*/
	private String conFtype;
	/**conName*/
	private String conName;
	/**合同信息*/
	private String conInfo;
	/**合同信息*/
	private String conInfoMd5;
	/**指令状态：交易失败-1，指令待发送16，交易处理中18，交易成功19，交易打回-2*/
	private String optStatus;
	/**创建人名称*/
	private String createId;
	/**更新人名称*/
	private String updateId;
	/**更新日期*/
	private java.util.Date createTime;
	/**更新日期*/
	private java.util.Date updateTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据池明细表ID
	 */
	@Column(name ="DTL_ID",nullable=true,length=32)
	public String getDtlId(){
		return this.dtlId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据池明细表ID
	 */
	public void setDtlId(String dtlId){
		this.dtlId = dtlId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令状态：0待确认1已确认2重新生成
	 */
	@Column(name ="STATUS",nullable=true,length=2)
	public String getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令状态：0待确认1已确认2重新生成
	 */
	public void setStatus(String status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  conFtype
	 */
	@Column(name ="CON_FTYPE",nullable=true,length=10)
	public String getConFtype(){
		return this.conFtype;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  conFtype
	 */
	public void setConFtype(String conFtype){
		this.conFtype = conFtype;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  conName
	 */
	@Column(name ="CON_NAME",nullable=true,length=100)
	public String getConName(){
		return this.conName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  conName
	 */
	public void setConName(String conName){
		this.conName = conName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同信息
	 */
	@Column(name ="CON_INFO",nullable=true)
	public String getConInfo(){
		return this.conInfo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同信息
	 */
	public void setConInfo(String conInfo){
		this.conInfo = conInfo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令状态：交易失败-1，指令待发送16，交易处理中18，交易成功19，交易打回-2
	 */
	@Column(name ="OPT_STATUS",nullable=true,length=2)
	public String getOptStatus(){
		return this.optStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令状态：交易失败-1，指令待发送16，交易处理中18，交易成功19，交易打回-2
	 */
	public void setOptStatus(String optStatus){
		this.optStatus = optStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_ID",nullable=true,length=32)
	public String getCreateId(){
		return this.createId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateId(String createId){
		this.createId = createId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_ID",nullable=true,length=32)
	public String getUpdateId(){
		return this.updateId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateId(String updateId){
		this.updateId = updateId;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="CREATE_TIME",nullable=true,scale=6)
	public java.util.Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setCreateTime(java.util.Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_TIME",nullable=true,scale=6)
	public java.util.Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateTime(java.util.Date updateTime){
		this.updateTime = updateTime;
	}
	@Column(name ="CON_INFO_MD5")
	public String getConInfoMd5() {
		return conInfoMd5;
	}
	public void setConInfoMd5(String conInfoMd5) {
		this.conInfoMd5 = conInfoMd5;
	}
	@Override
	public String toString() {
		return "AbPoolContract ["+
		"id=" + 
		getId() + ", "+
		"dtlId=" + 
		dtlId + ", "+
		"status=" + 
		status + ", "+
		"conFtype=" + 
		conFtype + ", "+
		"conName=" + 
		conName + ", "+
		"conInfo=" + 
		conInfo + ", "+
		"conInfoMd5=" + 
		conInfoMd5 + ", "+
		"optStatus=" + 
		optStatus + ", "+
		"createId=" + 
		createId + ", "+
		"updateId=" + 
		updateId + ", "+
		"createTime=" + 
		createTime + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
