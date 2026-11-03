package com.bytter.subject.entity;
import com.bytter.core.base.entity.TreeEntity;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 会计科目
 * @author 吕伟林
 * @date 2017-05-12 14:18:39
 * @version V1.0
 *
 */
@Entity
@Table(name = "FC_SUBJECT", schema = "")
@SuppressWarnings("serial")
public class FcSubject extends TreeEntity<FcSubject> implements java.io.Serializable {
	/**科目代码*/
	private String subjectCode;
	/**科目名称*/
	private String subjectName;
	/**余额方向*/
	private Integer balanceWay;
	/**计息标志*/
	private String interestSign;
	/**利率*/
	private BigDecimal interestRate;
	/**显示级次*/
	private Integer showLevel;
	/**科目级次*/
	private Integer levelNum;
	/**开户种类*/
	private String accType;
	/**末级标志*/
	private String leafSign;
	/**科目父ID*/
	private String fatherId;
	/**有效标志*/
	private String validSign;
	/**系统科目代码*/
	private String sysSubjectCode;
	/**系统科目实体*/
	private FcSubjectSort sysSubject;
	/**科目类别*/
	private String subjectSort;
	/**备注*/
	private String rmk;
	/**计量单位*/
	private String measureUnit;
	/**结息方式*/
	private String predrawSign;
	/**是否现金流科目*/
	private Integer isCashItem;
	/**借方现金流量项目*/
	private String debitCashItem;
	/**贷方现金流量项目*/
	private String creditCashItem;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目代码
	 */
	@Column(name ="SUBJECT_CODE")
	public String getSubjectCode(){
		return this.subjectCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目代码
	 */
	public void setSubjectCode(String subjectCode){
		this.subjectCode = subjectCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目名称
	 */
	@Column(name ="SUBJECT_NAME")
	public String getSubjectName(){
		return this.subjectName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目名称
	 */
	public void setSubjectName(String subjectName){
		this.subjectName = subjectName;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  余额方向
	 */
	@Column(name ="BALANCE_WAY")
	public Integer getBalanceWay(){
		return this.balanceWay;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  余额方向
	 */
	public void setBalanceWay(Integer balanceWay){
		this.balanceWay = balanceWay;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计息标志
	 */
	@Column(name ="INTEREST_SIGN")
	public String getInterestSign(){
		return this.interestSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计息标志
	 */
	public void setInterestSign(String interestSign){
		this.interestSign = interestSign;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利率
	 */
	@Column(name ="INTEREST_RATE")
	public BigDecimal getInterestRate(){
		return this.interestRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利率
	 */
	public void setInterestRate(BigDecimal interestRate){
		this.interestRate = interestRate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  显示级次
	 */
	@Column(name ="SHOW_LEVEL")
	public Integer getShowLevel(){
		return this.showLevel;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  显示级次
	 */
	public void setShowLevel(Integer showLevel){
		this.showLevel = showLevel;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  科目级次
	 */
	@Column(name ="LEVEL_NUM")
	public Integer getLevelNum(){
		return this.levelNum;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  科目级次
	 */
	public void setLevelNum(Integer levelNum){
		this.levelNum = levelNum;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  末级标志
	 */
	@Column(name ="LEAF_SIGN")
	public String getLeafSign(){
		return this.leafSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  末级标志
	 */
	public void setLeafSign(String leafSign){
		this.leafSign = leafSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目父ID
	 */
	@Column(name ="FATHER_ID")
	public String getFatherId(){
		return this.fatherId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目父ID
	 */
	public void setFatherId(String fatherId){
		this.fatherId = fatherId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  有效标志
	 */
	@Column(name ="VALID_SIGN")
	public String getValidSign(){
		return this.validSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  有效标志
	 */
	public void setValidSign(String validSign){
		this.validSign = validSign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  系统科目代码
	 */
	@Column(name ="SYS_SUBJECT_CODE")
	public String getSysSubjectCode(){
		return this.sysSubjectCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  系统科目代码
	 */
	public void setSysSubjectCode(String sysSubjectCode){
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
	public String getSubjectSort(){
		return this.subjectSort;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目类别
	 */
	public void setSubjectSort(String subjectSort){
		this.subjectSort = subjectSort;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  备注
	 */
	@Column(name ="RMK")
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
	 *@return: java.lang.String  计量单位
	 */
	@Column(name ="MEASURE_UNIT")
	public String getMeasureUnit(){
		return this.measureUnit;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计量单位
	 */
	public void setMeasureUnit(String measureUnit){
		this.measureUnit = measureUnit;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  结息方式
	 */
	@Column(name ="PREDRAW_SIGN")
	public String getPredrawSign(){
		return this.predrawSign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  结息方式
	 */
	public void setPredrawSign(String predrawSign){
		this.predrawSign = predrawSign;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  是否现金流科目
	 */
	@Column(name ="IS_CASH_ITEM")
	public Integer getIsCashItem(){
		return this.isCashItem;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  是否现金流科目
	 */
	public void setIsCashItem(Integer isCashItem){
		this.isCashItem = isCashItem;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借方现金流量项目
	 */
	@Column(name ="DEBIT_CASH_ITEM")
	public String getDebitCashItem(){
		return this.debitCashItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方现金流量项目
	 */
	public void setDebitCashItem(String debitCashItem){
		this.debitCashItem = debitCashItem;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷方现金流量项目
	 */
	@Column(name ="CREDIT_CASH_ITEM")
	public String getCreditCashItem(){
		return this.creditCashItem;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷方现金流量项目
	 */
	public void setCreditCashItem(String creditCashItem){
		this.creditCashItem = creditCashItem;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建时间
	 */
	@Column(name ="CREATE_TIME")
	public Date getCreateTime(){
		return this.createTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建用户代码
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建用户代码
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后更新时间
	 */
	@Column(name ="UPDATE_TIME")
	public Date getUpdateTime(){
		return this.updateTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后更新时间
	 */
	public void setUpdateTime(Date updateTime){
		this.updateTime = updateTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  最后更新用户代码
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  最后更新用户代码
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	
	
	/**
	 * 方法: 取得java.lang.String
	 * @return java.lang.String  开户种类
	 */
	@Column(name ="ACC_TYPE")
	public String getAccType() {
		return accType;
	}
	/**
	 * 方法: 设置java.lang.String
	 * @param accType java.lang.String  开户种类
	 */
	public void setAccType(String accType) {
		this.accType = accType;
	}
	@Override
	@Transient
	public String getParentId() {
		return this.fatherId;
	}
	
}
