package com.bytter.biz.finacial.paramconfig.entity;
import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.sql.Blob;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.annotations.GenericGenerator;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.base.entity.TreeEntity;
/**   
 * @Title: Entity
 * @Description: 会计科目
 * @author 吕伟林
 * @date 2017-05-12 14:18:39
 * @version V1.0
 * 实体已迁移到sys模块
 */
@SuppressWarnings("serial")
public class FcSubject extends TreeEntity<FcSubject> implements java.io.Serializable {
	/**科目代码*/
	private java.lang.String subjectCode;
	/**科目名称*/
	private java.lang.String subjectName;
	/**余额方向*/
	private java.lang.Integer balanceWay;
	/**计息标志*/
	private java.lang.String interestSign;
	/**利率*/
	private java.math.BigDecimal interestRate;
	/**显示级次*/
	private java.lang.Integer showLevel;
	/**科目级次*/
	private java.lang.Integer levelNum;
	/**开户种类*/
	private java.lang.String accType;
	/**末级标志*/
	private java.lang.String leafSign;
	/**科目父ID*/
	private java.lang.String fatherId;
	/**有效标志*/
	private java.lang.String validSign;
	/**系统科目代码*/
	private java.lang.String sysSubjectCode;
	/**系统科目实体*/
	private FcSubjectSort sysSubject;
	/**科目类别*/
	private java.lang.String subjectSort;
	/**备注*/
	private java.lang.String rmk;
	/**计量单位*/
	private java.lang.String measureUnit;
	/**结息方式*/
	private java.lang.String predrawSign;
	/**是否现金流科目*/
	private java.lang.Integer isCashItem;
	/**借方现金流量项目*/
	private java.lang.String debitCashItem;
	/**贷方现金流量项目*/
	private java.lang.String creditCashItem;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private java.lang.String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private java.lang.String updateBy;
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
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目名称
	 */
	@Column(name ="SUBJECT_NAME")
	public java.lang.String getSubjectName(){
		return this.subjectName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目名称
	 */
	public void setSubjectName(java.lang.String subjectName){
		this.subjectName = subjectName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  余额方向
	 */
	@Column(name ="BALANCE_WAY")
	public java.lang.Integer getBalanceWay(){
		return this.balanceWay;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  余额方向
	 */
	public void setBalanceWay(java.lang.Integer balanceWay){
		this.balanceWay = balanceWay;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计息标志
	 */
	@Column(name ="INTEREST_SIGN")
	public java.lang.String getInterestSign(){
		return this.interestSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计息标志
	 */
	public void setInterestSign(java.lang.String interestSign){
		this.interestSign = interestSign;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利率
	 */
	@Column(name ="INTEREST_RATE")
	public java.math.BigDecimal getInterestRate(){
		return this.interestRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利率
	 */
	public void setInterestRate(java.math.BigDecimal interestRate){
		this.interestRate = interestRate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  显示级次
	 */
	@Column(name ="SHOW_LEVEL")
	public java.lang.Integer getShowLevel(){
		return this.showLevel;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  显示级次
	 */
	public void setShowLevel(java.lang.Integer showLevel){
		this.showLevel = showLevel;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  科目级次
	 */
	@Column(name ="LEVEL_NUM")
	public java.lang.Integer getLevelNum(){
		return this.levelNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  科目级次
	 */
	public void setLevelNum(java.lang.Integer levelNum){
		this.levelNum = levelNum;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  末级标志
	 */
	@Column(name ="LEAF_SIGN")
	public java.lang.String getLeafSign(){
		return this.leafSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  末级标志
	 */
	public void setLeafSign(java.lang.String leafSign){
		this.leafSign = leafSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目父ID
	 */
	@Column(name ="FATHER_ID")
	public java.lang.String getFatherId(){
		return this.fatherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目父ID
	 */
	public void setFatherId(java.lang.String fatherId){
		this.fatherId = fatherId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志
	 */
	@Column(name ="VALID_SIGN")
	public java.lang.String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志
	 */
	public void setValidSign(java.lang.String validSign){
		this.validSign = validSign;
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
	 *方法: 取得FcSubjectSort
	 *@return: FcSubjectSort  系统科目
	 */
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "SYS_SUBJECT_CODE",referencedColumnName = "SYS_SUBJECT_CODE",insertable=false,updatable=false)
	public FcSubjectSort getSysSubject() {
		return sysSubject;
	}
	/**
	 *方法: 设置FcSubjectSort
	 *@param: FcSubjectSort  系统科目
	 */
	public void setSysSubject(FcSubjectSort sysSubject) {
		this.sysSubject = sysSubject;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目类别
	 */
	@Column(name ="SUBJECT_SORT")
	public java.lang.String getSubjectSort(){
		return this.subjectSort;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目类别
	 */
	public void setSubjectSort(java.lang.String subjectSort){
		this.subjectSort = subjectSort;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
	public java.lang.String getRmk(){
		return this.rmk;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  备注
	 */
	public void setRmk(java.lang.String rmk){
		this.rmk = rmk;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计量单位
	 */
	@Column(name ="MEASURE_UNIT")
	public java.lang.String getMeasureUnit(){
		return this.measureUnit;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计量单位
	 */
	public void setMeasureUnit(java.lang.String measureUnit){
		this.measureUnit = measureUnit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结息方式
	 */
	@Column(name ="PREDRAW_SIGN")
	public java.lang.String getPredrawSign(){
		return this.predrawSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结息方式
	 */
	public void setPredrawSign(java.lang.String predrawSign){
		this.predrawSign = predrawSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否现金流科目
	 */
	@Column(name ="IS_CASH_ITEM")
	public java.lang.Integer getIsCashItem(){
		return this.isCashItem;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否现金流科目
	 */
	public void setIsCashItem(java.lang.Integer isCashItem){
		this.isCashItem = isCashItem;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借方现金流量项目
	 */
	@Column(name ="DEBIT_CASH_ITEM")
	public java.lang.String getDebitCashItem(){
		return this.debitCashItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方现金流量项目
	 */
	public void setDebitCashItem(java.lang.String debitCashItem){
		this.debitCashItem = debitCashItem;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷方现金流量项目
	 */
	@Column(name ="CREDIT_CASH_ITEM")
	public java.lang.String getCreditCashItem(){
		return this.creditCashItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷方现金流量项目
	 */
	public void setCreditCashItem(java.lang.String creditCashItem){
		this.creditCashItem = creditCashItem;
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
	
	
	/**
	 * 方法: 取得java.lang.String
	 * @return java.lang.String  开户种类
	 */
	@Column(name ="ACC_TYPE")
	public java.lang.String getAccType() {
		return accType;
	}
	/**
	 * 方法: 设置java.lang.String
	 * @param accType java.lang.String  开户种类
	 */
	public void setAccType(java.lang.String accType) {
		this.accType = accType;
	}
	@Override
	@Transient
	public String getParentId() {
		return this.fatherId;
	}
	
}
