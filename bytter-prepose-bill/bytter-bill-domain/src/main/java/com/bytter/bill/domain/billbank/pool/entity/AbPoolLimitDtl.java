package com.bytter.bill.domain.billbank.pool.entity;
import com.bytter.bill.domain.billbank.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 额度调剂明细
 * @author zhengg
 * @date 2021-03-04 14:40:41
 * @version V1.0   
 *
 */
@Entity
@Table(name = "AB_POOL_LIMIT_DTL", schema = "")
@SuppressWarnings("serial")
public class AbPoolLimitDtl extends IdEntity implements java.io.Serializable {
	/**单位ID*/
	private String corpId;
	/**票据池ID*/
	private String bpId;
	/**票据池ID*/
	private String erpId;
	/**业务类型ID*/
	private String bustypeId;
	/**额度种类:1：低风险， 2：非低风险
*/
	private String limtType;
	/**调剂前额度*/
	private java.math.BigDecimal oldMoney;
	/**调剂后额度*/
	private java.math.BigDecimal poolMoney;
	/**调剂额度*/
	private java.math.BigDecimal applyMoney;
	/**到期日期*/
	private java.util.Date endDate;
	/**备注*/
	private String rmk;
	/**创建人名称*/
	private String createId;
	/**调剂日期*/
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
	 *@return: java.math.BigDecimal  总额度
	 */
	@Column(name ="POOL_MONEY",nullable=true,precision=15,scale=2)
	public java.math.BigDecimal getPoolMoney(){
		return this.poolMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  总额度
	 */
	public void setPoolMoney(java.math.BigDecimal poolMoney){
		this.poolMoney = poolMoney;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  调剂前额度
	 */
	@Column(name ="OLD_MONEY",nullable=true,precision=15,scale=2)
	public java.math.BigDecimal getOldMoney(){
		return this.oldMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  调剂前额度
	 */
	public void setOldMoney(java.math.BigDecimal oldMoney){
		this.oldMoney = oldMoney;
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
	@Column(name ="ERP_ID")
	public String getErpId() {
		return erpId;
	}
	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	@Override
	public String toString() {
		return "AbPoolLimitDtl [corpId=" + corpId + ", bpId=" + bpId
				+ ", erpId=" + erpId + ", bustypeId=" + bustypeId
				+ ", limtType=" + limtType + ", oldMoney=" + oldMoney
				+ ", poolMoney=" + poolMoney + ", applyMoney=" + applyMoney
				+ ", endDate=" + endDate + ", rmk=" + rmk + ", createId="
				+ createId + ", createTime=" + createTime + ", updateId="
				+ updateId + ", updateTime=" + updateTime + "]";
	}
	
	
}
