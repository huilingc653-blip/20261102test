package com.bytter.biz.finacial.paramconfig.entity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 费用计提维护表
 * @author onlineGenerator
 * @date 2017-05-16 09:06:35
 * @version V1.0
 *
 */
@Entity
@Table(name = "FC_CHARGE_RULE", schema = "")
@SuppressWarnings("serial")
public class FcChargeRule extends IdEntity implements java.io.Serializable {
	/**费用计提凭证分录*/
	private List<FcVoucherEntryDefine> fcVoucherEntryDefines = new ArrayList<FcVoucherEntryDefine>();
	/**费用类型与费用计提*/
	private List<ChargeTypeChargeRuleRel> chargeTypeChargeRuleRels = new ArrayList<ChargeTypeChargeRuleRel>();
	/**业务种类*/
	private java.lang.String voucherType;
	/**凭证种类代码*/
	private java.lang.String raceCode;
	/**收费借方科目*/
	private java.lang.String debitSubjCode;
	/**收费贷方科目*/
	private java.lang.String creditSubjCode;
	/**摘要*/
	private java.lang.String summary;
	/**所属网点*/
	private java.lang.String netId;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**币别*/
	private java.lang.String curCode;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务种类
	 */
	@Column(name ="VOUCHER_TYPE")
	public java.lang.String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务种类
	 */
	public void setVoucherType(java.lang.String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证种类代码
	 */
	@Column(name ="RACE_CODE")
	public java.lang.String getRaceCode(){
		return this.raceCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类代码
	 */
	public void setRaceCode(java.lang.String raceCode){
		this.raceCode = raceCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收费借方科目
	 */
	@Column(name ="DEBIT_SUBJ_CODE")
	public java.lang.String getDebitSubjCode(){
		return this.debitSubjCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收费借方科目
	 */
	public void setDebitSubjCode(java.lang.String debitSubjCode){
		this.debitSubjCode = debitSubjCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收费贷方科目
	 */
	@Column(name ="CREDIT_SUBJ_CODE")
	public java.lang.String getCreditSubjCode(){
		return this.creditSubjCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收费贷方科目
	 */
	public void setCreditSubjCode(java.lang.String creditSubjCode){
		this.creditSubjCode = creditSubjCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="SUMMARY")
	public java.lang.String getSummary(){
		return this.summary;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setSummary(java.lang.String summary){
		this.summary = summary;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  所属网点
	 */
	@Column(name ="NET_ID")
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  所属网点
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
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
	
	@Transient
	public List<FcVoucherEntryDefine> getFcVoucherEntryDefines() {
		return fcVoucherEntryDefines;
	}
	public void setFcVoucherEntryDefines(List<FcVoucherEntryDefine> fcVoucherEntryDefines) {
		this.fcVoucherEntryDefines = fcVoucherEntryDefines;
	}
	@Transient
	public List<ChargeTypeChargeRuleRel> getChargeTypeChargeRuleRels() {
		return chargeTypeChargeRuleRels;
	}
	public void setChargeTypeChargeRuleRels(List<ChargeTypeChargeRuleRel> chargeTypeChargeRuleRels) {
		this.chargeTypeChargeRuleRels = chargeTypeChargeRuleRels;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	
}
