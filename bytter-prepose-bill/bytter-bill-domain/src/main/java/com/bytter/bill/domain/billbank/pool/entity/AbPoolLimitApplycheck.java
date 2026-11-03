package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 额度申请核准
 * @author zhengg
 * @date 2020-02-24 14:40:42
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_LIMIT_APPLYCHECK", schema = "")
@SuppressWarnings("serial")
public class AbPoolLimitApplycheck extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**业务类型ID*/
	private String bustypeId;
	/**额度种类:1：低风险， 2：非低风险
*/
	private String limtType;
	/**申请额度*/
	private java.math.BigDecimal applyMoney;
	/**到期日期*/
	private java.util.Date endDate;
	/**核准类型：1通过0不通过*/
	private String checkType;
	/**核准信息*/
	private String checkMsg;
	/**指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3*/
	private String optStatus;
	/**审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过*/
	private Integer status;
	/**备注*/
	private String rmk;
	/**创建人名称*/
	private String createId;
	/**更新日期*/
	private java.util.Date createTime;
	/**更新人名称*/
	private String updateId;
	/**更新日期*/
	private java.util.Date updateTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=true,length=32)
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据池ID
	 */
	@Column(name ="BP_ID",nullable=true,length=32)
	public String getBpId(){
		return this.bpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据池ID
	 */
	public void setBpId(String bpId){
		this.bpId = bpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型ID
	 */
	@Column(name ="BUSTYPE_ID",nullable=true,length=32)
	public String getBustypeId(){
		return this.bustypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型ID
	 */
	public void setBustypeId(String bustypeId){
		this.bustypeId = bustypeId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  额度种类:1：低风险， 2：非低风险
	 */
	@Column(name ="LIMT_TYPE",nullable=true,length=2)
	public String getLimtType(){
		return this.limtType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  额度种类:1：低风险， 2：非低风险
	 */
	public void setLimtType(String limtType){
		this.limtType = limtType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  申请额度
	 */
	@Column(name ="APPLY_MONEY",nullable=true,precision=15,scale=2)
	public java.math.BigDecimal getApplyMoney(){
		return this.applyMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  申请额度
	 */
	public void setApplyMoney(java.math.BigDecimal applyMoney){
		this.applyMoney = applyMoney;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  到期日期
	 */
	@Column(name ="END_DATE",nullable=true)
	public java.util.Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  到期日期
	 */
	public void setEndDate(java.util.Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  核准类型：1通过0不通过
	 */
	@Column(name ="CHECK_TYPE",nullable=true,length=2)
	public String getCheckType(){
		return this.checkType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  核准类型：1通过0不通过
	 */
	public void setCheckType(String checkType){
		this.checkType = checkType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  核准信息
	 */
	@Column(name ="CHECK_MSG",nullable=true,length=200)
	public String getCheckMsg(){
		return this.checkMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  核准信息
	 */
	public void setCheckMsg(String checkMsg){
		this.checkMsg = checkMsg;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3
	 */
	@Column(name ="OPT_STATUS",nullable=true,length=2)
	public String getOptStatus(){
		return this.optStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3
	 */
	public void setOptStatus(String optStatus){
		this.optStatus = optStatus;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过
	 */
	@Column(name ="STATUS",nullable=true,precision=6,scale=0)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审批状态：0暂存，-1打回，-2删除，11审批中，95审批通过
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK",nullable=true,length=200)
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
	
	@Override
	public String toString() {
		return "AbPoolLimitApplycheck ["+
		"id=" + 
		getId() + ", "+
		"corpId=" + 
		corpId + ", "+
		"bpId=" + 
		bpId + ", "+
		"bustypeId=" + 
		bustypeId + ", "+
		"limtType=" + 
		limtType + ", "+
		"applyMoney=" + 
		applyMoney + ", "+
		"endDate=" + 
		endDate + ", "+
		"checkType=" + 
		checkType + ", "+
		"checkMsg=" + 
		checkMsg + ", "+
		"optStatus=" + 
		optStatus + ", "+
		"status=" + 
		status + ", "+
		"rmk=" + 
		rmk + ", "+
		"createId=" + 
		createId + ", "+
		"createTime=" + 
		createTime + ", "+
		"updateId=" + 
		updateId + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
