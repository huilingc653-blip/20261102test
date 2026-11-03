package com.bytter.subject.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 科目日结单表
 * @author onlineGenerator
 * @date 2017-06-27 16:27:23
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_SUBJECT_DAY", schema = "")
@SuppressWarnings("serial")
public class FcSubjectDay extends IdEntity implements java.io.Serializable {
	/**结算日期*/
	private Date optDate;
	/**所属网点*/
	private String netId;
	/**区域代码*/
	private String areaCode;
	/**行业代码*/
	private String vocationCode;
	/**科目代码*/
	private String subjectId;
	/**币别代码*/
	private String curId;
	/**转账借方金额*/
	private BigDecimal trDebitMoney;
	/**转账借方笔数*/
	private Integer trDebitCnt;
	/**转账贷方金额*/
	private BigDecimal trCreditMoney;
	/**转账贷方笔数*/
	private Integer trCreditCnt;
	/**现金借方金额*/
	private BigDecimal cashDebitMoney;
	/**现金借方笔数*/
	private Integer cashDebitCnt;
	/**现金贷方金额*/
	private BigDecimal cashCreditMoney;
	/**现金贷方笔数*/
	private Integer cashCreditCnt;
	/**昨日余额*/
	private BigDecimal yestBalance;
	/**今日余额*/
	private BigDecimal todayBalance;
	/**创建时间*/
	private Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	/**
	 * 新增冗余字段 net_name、cur_name
	 */
	private String netName;
	private String curName;
	@Column(name = "NET_NAME")
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	@Column(name = "CUR_NAME")
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  结算日期
	 */
	@Column(name ="OPT_DATE")
	public Date getOptDate(){
		return this.optDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  结算日期
	 */
	public void setOptDate(Date optDate){
		this.optDate = optDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  所属网点
	 */
	@Column(name ="NET_ID")
	public String getNetId(){
		return this.netId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  所属网点
	 */
	public void setNetId(String netId){
		this.netId = netId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  区域代码
	 */
	@Column(name ="AREA_CODE")
	public String getAreaCode(){
		return this.areaCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  区域代码
	 */
	public void setAreaCode(String areaCode){
		this.areaCode = areaCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  行业代码
	 */
	@Column(name ="VOCATION_CODE")
	public String getVocationCode(){
		return this.vocationCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  行业代码
	 */
	public void setVocationCode(String vocationCode){
		this.vocationCode = vocationCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  科目代码
	 */
	@Column(name ="SUBJECT_ID")
	public String getSubjectId(){
		return this.subjectId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  科目代码
	 */
	public void setSubjectId(String subjectId){
		this.subjectId = subjectId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别代码
	 */
	@Column(name ="CUR_ID")
	public String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别代码
	 */
	public void setCurId(String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  转账借方金额
	 */
	@Column(name ="TR_DEBIT_MONEY")
	public BigDecimal getTrDebitMoney(){
		return this.trDebitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  转账借方金额
	 */
	public void setTrDebitMoney(BigDecimal trDebitMoney){
		this.trDebitMoney = trDebitMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  转账借方笔数
	 */
	@Column(name ="TR_DEBIT_CNT")
	public Integer getTrDebitCnt(){
		return this.trDebitCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  转账借方笔数
	 */
	public void setTrDebitCnt(Integer trDebitCnt){
		this.trDebitCnt = trDebitCnt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  转账贷方金额
	 */
	@Column(name ="TR_CREDIT_MONEY")
	public BigDecimal getTrCreditMoney(){
		return this.trCreditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  转账贷方金额
	 */
	public void setTrCreditMoney(BigDecimal trCreditMoney){
		this.trCreditMoney = trCreditMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  转账贷方笔数
	 */
	@Column(name ="TR_CREDIT_CNT")
	public Integer getTrCreditCnt(){
		return this.trCreditCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  转账贷方笔数
	 */
	public void setTrCreditCnt(Integer trCreditCnt){
		this.trCreditCnt = trCreditCnt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  现金借方金额
	 */
	@Column(name ="CASH_DEBIT_MONEY")
	public BigDecimal getCashDebitMoney(){
		return this.cashDebitMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  现金借方金额
	 */
	public void setCashDebitMoney(BigDecimal cashDebitMoney){
		this.cashDebitMoney = cashDebitMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  现金借方笔数
	 */
	@Column(name ="CASH_DEBIT_CNT")
	public Integer getCashDebitCnt(){
		return this.cashDebitCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  现金借方笔数
	 */
	public void setCashDebitCnt(Integer cashDebitCnt){
		this.cashDebitCnt = cashDebitCnt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  现金贷方金额
	 */
	@Column(name ="CASH_CREDIT_MONEY")
	public BigDecimal getCashCreditMoney(){
		return this.cashCreditMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  现金贷方金额
	 */
	public void setCashCreditMoney(BigDecimal cashCreditMoney){
		this.cashCreditMoney = cashCreditMoney;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  现金贷方笔数
	 */
	@Column(name ="CASH_CREDIT_CNT")
	public Integer getCashCreditCnt(){
		return this.cashCreditCnt;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  现金贷方笔数
	 */
	public void setCashCreditCnt(Integer cashCreditCnt){
		this.cashCreditCnt = cashCreditCnt;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  昨日余额
	 */
	@Column(name ="YEST_BALANCE")
	public BigDecimal getYestBalance(){
		return this.yestBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  昨日余额
	 */
	public void setYestBalance(BigDecimal yestBalance){
		this.yestBalance = yestBalance;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  今日余额
	 */
	@Column(name ="TODAY_BALANCE")
	public BigDecimal getTodayBalance(){
		return this.todayBalance;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  今日余额
	 */
	public void setTodayBalance(BigDecimal todayBalance){
		this.todayBalance = todayBalance;
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
}
