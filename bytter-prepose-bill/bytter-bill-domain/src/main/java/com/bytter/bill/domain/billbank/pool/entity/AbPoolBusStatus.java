package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 票据池业务状态信息
 * @author zhengg
 * @date 2020-02-24 14:40:39
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_BUS_STATUS", schema = "")
@SuppressWarnings("serial")
public class AbPoolBusStatus extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；8001出票融资申请；010背书申请；011贴现申请；020提示付款*/
	private String briefId;
	/**票据号码*/
	private String origNo;
	/**指令状态：待发送16，发送失败-1，银行处理中18，银行交易成功19，银行交易失败-3*/
	private String optStatus;
	/**处理结果*/
	private String returnCode;
	/**处理描述*/
	private String returnMsg;
	/**更新日期*/
	private java.util.Date createTime;
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
	 *@return: java.lang.String  业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；8001出票融资申请；010背书申请；011贴现申请；020提示付款
	 */
	@Column(name ="BRIEF_ID",nullable=true,length=2)
	public String getBriefId(){
		return this.briefId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；8001出票融资申请；010背书申请；011贴现申请；020提示付款
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  处理结果
	 */
	@Column(name ="RETURN_CODE",nullable=true,length=20)
	public String getReturnCode(){
		return this.returnCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  处理结果
	 */
	public void setReturnCode(String returnCode){
		this.returnCode = returnCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  处理描述
	 */
	@Column(name ="RETURN_MSG",nullable=true,length=500)
	public String getReturnMsg(){
		return this.returnMsg;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  处理描述
	 */
	public void setReturnMsg(String returnMsg){
		this.returnMsg = returnMsg;
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
	
	@Override
	public String toString() {
		return "AbPoolBusStatus ["+
		"id=" + 
		getId() + ", "+
		"corpId=" + 
		corpId + ", "+
		"bpId=" + 
		bpId + ", "+
		"briefId=" + 
		briefId + ", "+
		"origNo=" + 
		origNo + ", "+
		"optStatus=" + 
		optStatus + ", "+
		"returnCode=" + 
		returnCode + ", "+
		"returnMsg=" + 
		returnMsg + ", "+
		"createTime=" + 
		createTime + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
