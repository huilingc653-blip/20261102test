package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.pool.vo.PoolBillVoucherVO;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
/**
 * @Title: Entity
 * @Description: 票据池明细登记薄
 * @author zhengg
 * @date 2020-02-24 14:40:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_HOLD_BOOK", schema = "")
@SuppressWarnings("serial")
public class AbPoolHoldBook extends IdEntity implements java.io.Serializable {
	/**主键ID*/
	private String extId;
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**票据号码*/
	private String origNo;
	/**出入池方式：1质押2托管*/
	private String inoutType;
	/**更新日期*/
	private java.util.Date createTime;
	/**更新日期*/
	private java.util.Date updateTime;
	private PoolBillVoucherVO voucherVO;
	/** 票据融资额度 bill_money number(15,2) */
	private BigDecimal limitMoney;
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
	
	@Transient
	public PoolBillVoucherVO getVoucherVO() {
		return voucherVO;
	}
	public void setVoucherVO(PoolBillVoucherVO voucherVO) {
		this.voucherVO = voucherVO;
	}
	@Column(name ="LIMIT_MONEY")
	public BigDecimal getLimitMoney() {
		return limitMoney;
	}
	public void setLimitMoney(BigDecimal limitMoney) {
		this.limitMoney = limitMoney;
	}
	@Override
	public String toString() {
		return "AbPoolHoldBook [extId=" + extId + ", corpId=" + corpId
				+ ", bpId=" + bpId + ", origNo=" + origNo + ", inoutType="
				+ inoutType + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", voucherVO=" + voucherVO + ", limitMoney="
				+ limitMoney + "]";
	}
}
