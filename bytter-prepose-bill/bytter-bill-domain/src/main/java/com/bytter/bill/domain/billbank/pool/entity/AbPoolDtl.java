package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * @Title: Entity
 * @Description: 票据池交易明细表
 * @author zhengg
 * @date 2020-02-24 14:40:40
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_DTL", schema = "")
@SuppressWarnings("serial")
public class AbPoolDtl extends IdEntity implements java.io.Serializable {
	
	/**上一个池流水ID*/
	private String lastid;
	/**流水上一流水ID*/
	private String accLastid;
	/**主表ID*/
	private String vid;
	/**从表ID*/
	private String vvId;
	/**票据池ID*/
	private String bpId;
	/**申请单号*/
	private String appNo;
	/**业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款；*/
	private String briefId;
	/**票据号码*/
	private String origNo;
	/**出入池方式：1质押2托管*/
	private String inoutType;
	/**status*/
	private Integer status;
	/**指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2*/
	private String optStatus;
    /**
     * 状态：1启用、0在途、2作废
     */
    private String validSign;
	/**创建人名称*/
	private String createId;
	/**更新人名称*/
	private String updateId;
	/**更新日期*/
	private java.util.Date createTime;
	/**更新日期*/
	private java.util.Date updateTime;
	/**
	 * 入池撤销id
	 */
	private String repealId;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  从表ID
	 */
	@Column(name ="VV_ID",nullable=true,length=32)
	public String getVvId(){
		return this.vvId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  从表ID
	 */
	public void setVvId(String vvId){
		this.vvId = vvId;
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
	 *@return: java.lang.String  申请单号
	 */
	@Column(name ="APP_NO",nullable=true,length=32)
	public String getAppNo(){
		return this.appNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  申请单号
	 */
	public void setAppNo(String appNo){
		this.appNo = appNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款；
	 */
	@Column(name ="BRIEF_ID",nullable=true,length=2)
	public String getBriefId(){
		return this.briefId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款；
	 */
	public void setBriefId(String briefId){
		this.briefId = briefId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  票据号码
	 */
	@Column(name ="ORIG_NO",nullable=true,length=50)
	public String getOrigNo(){
		return this.origNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  票据号码
	 */
	public void setOrigNo(String origNo){
		this.origNo = origNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  出入池方式：1质押2托管
	 */
	@Column(name ="INOUT_TYPE",nullable=true,length=10)
	public String getInoutType(){
		return this.inoutType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  出入池方式：1质押2托管
	 */
	public void setInoutType(String inoutType){
		this.inoutType = inoutType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  status
	 */
	@Column(name ="STATUS",nullable=true,precision=6,scale=0)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  status
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2
	 */
	@Column(name ="OPT_STATUS",nullable=true,length=2)
	public String getOptStatus(){
		return this.optStatus;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  指令状态：交易失败-1，交易处理中18，交易成功19，交易打回-2
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
	@Column(name ="LASTID")
	public String getLastid() {
		return lastid;
	}
	public void setLastid(String lastid) {
		this.lastid = lastid;
	}
	@Column(name ="VALID_SIGN")
	public String getValidSign() {
		return validSign;
	}
	public void setValidSign(String validSign) {
		this.validSign = validSign;
	}
	@Transient
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	@Transient
	public String getAccLastid() {
		return accLastid;
	}
	public void setAccLastid(String accLastid) {
		this.accLastid = accLastid;
	}
	@Column(name ="REPEAL_ID")
	public String getRepealId() {
		return repealId;
	}
	public void setRepealId(String repealId) {
		this.repealId = repealId;
	}
	@Override
	public String toString() {
		return "AbPoolDtl ["+
		"id=" + 
		getId() + ", "+
		"vvId=" + 
		vvId + ", "+
		"bpId=" + 
		bpId + ", "+
		"appNo=" + 
		appNo + ", "+
		"briefId=" + 
		briefId + ", "+
		"origNo=" + 
		origNo + ", "+
		"inoutType=" + 
		inoutType + ", "+
		"status=" + 
		status + ", "+
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
