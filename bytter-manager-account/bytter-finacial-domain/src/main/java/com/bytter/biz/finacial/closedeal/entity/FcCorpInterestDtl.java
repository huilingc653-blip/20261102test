package com.bytter.biz.finacial.closedeal.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_CORP_INTEREST_DTL
 * @author onlineGenerator
 * @date 2017-07-12 14:23:15
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CORP_INTEREST_DTL", schema = "")
@SuppressWarnings("serial")
public class FcCorpInterestDtl extends IdEntity implements java.io.Serializable {
	/**计息时间*/
	private java.util.Date interestDate;
	/**所属网点*/
	private java.lang.String netId;
	/**单位代码*/
	private java.lang.String corpId;
	/**币别代码*/
	private java.lang.String curCode;
	/**单位名称*/
	private java.lang.String corpName;
	/**系统科目代码*/
	private java.lang.String sysSubjectCode;
	/**银行类别*/
	private java.lang.String bankType;
	/**银行类别名称*/
	private java.lang.String bankTypeName;
	/**科目代码*/
	private java.lang.String subjectCode;
	/**利息金额*/
	private java.lang.Integer interestMoney;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计息时间
	 */
	@Column(name ="INTEREST_DATE")
	public java.util.Date getInterestDate(){
		return this.interestDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计息时间
	 */
	public void setInterestDate(java.util.Date interestDate){
		this.interestDate = interestDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public java.lang.String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(java.lang.String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位代码
	 */
	@Column(name ="CORP_ID")
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位代码
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位名称
	 */
	@Column(name ="CORP_NAME")
	public java.lang.String getCorpName(){
		return this.corpName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位名称
	 */
	public void setCorpName(java.lang.String corpName){
		this.corpName = corpName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统科目代码
	 */
	@Column(name ="SYS_SUBJECT_CODE")
	public java.lang.String getSysSubjectCode(){
		return this.sysSubjectCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统科目代码
	 */
	public void setSysSubjectCode(java.lang.String sysSubjectCode){
		this.sysSubjectCode = sysSubjectCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行类别
	 */
	@Column(name ="BANK_TYPE")
	public java.lang.String getBankType(){
		return this.bankType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行类别
	 */
	public void setBankType(java.lang.String bankType){
		this.bankType = bankType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  银行类别名称
	 */
	@Column(name ="BANK_TYPE_NAME")
	public java.lang.String getBankTypeName(){
		return this.bankTypeName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  银行类别名称
	 */
	public void setBankTypeName(java.lang.String bankTypeName){
		this.bankTypeName = bankTypeName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目代码
	 */
	@Column(name ="SUBJECT_CODE")
	public java.lang.String getSubjectCode(){
		return this.subjectCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目代码
	 */
	public void setSubjectCode(java.lang.String subjectCode){
		this.subjectCode = subjectCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  利息金额
	 */
	@Column(name ="INTEREST_MONEY")
	public java.lang.Integer getInterestMoney(){
		return this.interestMoney;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  利息金额
	 */
	public void setInterestMoney(java.lang.Integer interestMoney){
		this.interestMoney = interestMoney;
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
}
