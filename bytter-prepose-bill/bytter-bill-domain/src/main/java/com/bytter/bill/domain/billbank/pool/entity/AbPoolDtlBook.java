package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 票据池出票融资登记薄
 * @author zhengg
 * @date 2020-02-24 14:40:40
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_DTL_BOOK", schema = "")
@SuppressWarnings("serial")
public class AbPoolDtlBook extends IdEntity implements java.io.Serializable {
	/**主键ID*/
	private String extId;
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款*/
	private String briefId;
	/**票据号码*/
	private String origNo;
	/**出入池方式：1质押2托管*/
	private String inoutType;
	/**是否可转让：EM00 是；EM01 否*/
	private String bankendrsmtmk;
	/**清算方式：SM00 线上清算、SM01 线下清算*/
	private String sttlmmk;
	/**提示付款逾期原因*/
	private String rsn;
	/**更新日期*/
	private java.util.Date createTime;
	/**更新日期*/
	private java.util.Date updateTime;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  主键ID
	 */
	@Column(name ="EXT_ID",nullable=false,length=50)
	public String getExtId(){
		return this.extId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  主键ID
	 */
	public void setExtId(String extId){
		this.extId = extId;
	}
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
	 *@return: java.lang.String  业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款
	 */
	@Column(name ="BRIEF_ID",nullable=true,length=2)
	public String getBriefId(){
		return this.briefId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；010背书申请；011贴现申请；020提示付款
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否可转让：EM00 是；EM01 否
	 */
	@Column(name ="BANKENDRSMTMK",nullable=true,length=10)
	public String getBankendrsmtmk(){
		return this.bankendrsmtmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否可转让：EM00 是；EM01 否
	 */
	public void setBankendrsmtmk(String bankendrsmtmk){
		this.bankendrsmtmk = bankendrsmtmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  清算方式：SM00 线上清算、SM01 线下清算
	 */
	@Column(name ="STTLMMK",nullable=true,length=10)
	public String getSttlmmk(){
		return this.sttlmmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  清算方式：SM00 线上清算、SM01 线下清算
	 */
	public void setSttlmmk(String sttlmmk){
		this.sttlmmk = sttlmmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  提示付款逾期原因
	 */
	@Column(name ="RSN",nullable=true,length=500)
	public String getRsn(){
		return this.rsn;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  提示付款逾期原因
	 */
	public void setRsn(String rsn){
		this.rsn = rsn;
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
		return "AbPoolDtlBook ["+
		"id=" + 
		getId() + ", "+
		"extId=" + 
		extId + ", "+
		"corpId=" + 
		corpId + ", "+
		"bpId=" + 
		bpId + ", "+
		"briefId=" + 
		briefId + ", "+
		"origNo=" + 
		origNo + ", "+
		"inoutType=" + 
		inoutType + ", "+
		"bankendrsmtmk=" + 
		bankendrsmtmk + ", "+
		"sttlmmk=" + 
		sttlmmk + ", "+
		"rsn=" + 
		rsn + ", "+
		"createTime=" + 
		createTime + ", "+
		"updateTime=" + 
		updateTime + ", "+
		"super.toString()=" + super.toString() + "]";
	}
}
