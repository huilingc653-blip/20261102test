package com.bytter.biz.inneraccount.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 内部账号冻结解冻
 * @author onlineGenerator
 * @date 2017-06-16 17:31:26
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_FREEZE", schema = "")
@SuppressWarnings("serial")
public class FcFreeze extends IdEntity implements java.io.Serializable {
	/**更新日期*/
	private Date updateDate;
	/**账号*/
	private String blnctrAcc;
	/**可解冻金额*/
	private BigDecimal unFreezeMoney;
	/**付款人账号*/
	private String payerAccNo;
	/**冻结金额*/
	private BigDecimal freezeMoney;
	/**累计冻结金额*/
	private BigDecimal accuFreezeMoney;
	/**冻结(解冻)类型*/
	private String freezeType;
	/**操作员*/
	private String freezeOperator;
	/**操作日期*/
	private Date operateTime;
	/**复核员代码*/
	private String checkerCode;
	/**复核日期*/
	private Date checkTime;
	/**通知书编号*/
	private String freezeNo;
	/**冻结/解冻机构*/
	private String freezeOrg;
	/**冻结开始时间*/
	private Date beginDate;
	/**冻结结束时间*/
	private Date endDate;
	/**执行人公务证*/
	private String execOfficialNo;
	/**执行人省份证*/
	private String execCardNo;
	/**冻结/解冻原因*/
	private String freezeReason;
	/**状态 */
	private Integer status;
	/**解冻数据*/
	private String unFreezeId;
	/**打回原因*/
	private String refuseWhy;
	/**冻结解除标志*/
	private String freezeEndSign;
	/**冻结已提醒标志  1：已提醒；0空未提醒*/
	private String deductSign;
	/**账户状态*/
	private String curAccState;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**
	 * 冻结(解冻)标志
	 */
	private String freezeSign;
	private String opType;
	@Transient
	public String getOpType() {
		return opType;
	}
	public void setOpType(String opType) {
		this.opType = opType;
	}
	@Column(name ="FREEZE_SIGN",nullable=true)
	public String getFreezeSign() {
		return freezeSign;
	}
	public void setFreezeSign(String freezeSign) {
		this.freezeSign = freezeSign;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置Date
	 *@param: Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  账号
	 */
	@Column(name ="BLNCTR_ACC",nullable=false,length=50)
	public String getBlnctrAcc(){
		return this.blnctrAcc;
	}
	/**
	 *方法: 设置String
	 *@param: String  账号
	 */
	public void setBlnctrAcc(String blnctrAcc){
		this.blnctrAcc = blnctrAcc;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  可解冻金额
	 */
	@Column(name ="UN_FREEZE_MONEY",nullable=true,scale=2,length=15)
	public BigDecimal getUnFreezeMoney(){
		return this.unFreezeMoney;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  可解冻金额
	 */
	public void setUnFreezeMoney(BigDecimal unFreezeMoney){
		this.unFreezeMoney = unFreezeMoney;
	}
	/**
	 *方法: 取得String
	 *@return: String  付款人账号
	 */
	@Column(name ="PAYER_ACC_NO",nullable=true,length=50)
	public String getPayerAccNo(){
		return this.payerAccNo;
	}
	/**
	 *方法: 设置String
	 *@param: String  付款人账号
	 */
	public void setPayerAccNo(String payerAccNo){
		this.payerAccNo = payerAccNo;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  冻结金额
	 */
	@Column(name ="FREEZE_MONEY",nullable=false,scale=2,length=15)
	public BigDecimal getFreezeMoney(){
		return this.freezeMoney;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  冻结金额
	 */
	public void setFreezeMoney(BigDecimal freezeMoney){
		this.freezeMoney = freezeMoney;
	}
	/**
	 *方法: 取得BigDecimal
	 *@return: BigDecimal  累计冻结金额
	 */
	@Column(name ="ACCU_FREEZE_MONEY",nullable=false,scale=2,length=15)
	public BigDecimal getAccuFreezeMoney(){
		return this.accuFreezeMoney;
	}
	/**
	 *方法: 设置BigDecimal
	 *@param: BigDecimal  累计冻结金额
	 */
	public void setAccuFreezeMoney(BigDecimal accuFreezeMoney){
		this.accuFreezeMoney = accuFreezeMoney;
	}
	/**
	 *方法: 取得String
	 *@return: String  冻结(解冻)类型
	 */
	@Column(name ="FREEZE_TYPE",nullable=false,length=1)
	public String getFreezeType(){
		return this.freezeType;
	}
	/**
	 *方法: 设置String
	 *@param: String  冻结(解冻)类型
	 */
	public void setFreezeType(String freezeType){
		this.freezeType = freezeType;
	}
	/**
	 *方法: 取得String
	 *@return: String  操作员
	 */
	@Column(name ="FREEZE_OPERATOR",nullable=false,length=20)
	public String getFreezeOperator(){
		return this.freezeOperator;
	}
	/**
	 *方法: 设置String
	 *@param: String  操作员
	 */
	public void setFreezeOperator(String freezeOperator){
		this.freezeOperator = freezeOperator;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  操作日期
	 */
	@Column(name ="OPERATE_TIME",nullable=false)
	public Date getOperateTime(){
		return this.operateTime;
	}
	/**
	 *方法: 设置Date
	 *@param: Date  操作日期
	 */
	public void setOperateTime(Date operateTime){
		this.operateTime = operateTime;
	}
	/**
	 *方法: 取得String
	 *@return: String  复核员代码
	 */
	@Column(name ="CHECKER_CODE",nullable=true,length=20)
	public String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置String
	 *@param: String  复核员代码
	 */
	public void setCheckerCode(String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  复核日期
	 */
	@Column(name ="CHECK_TIME",nullable=true)
	public Date getCheckTime(){
		return this.checkTime;
	}
	/**
	 *方法: 设置Date
	 *@param: Date  复核日期
	 */
	public void setCheckTime(Date checkTime){
		this.checkTime = checkTime;
	}
	/**
	 *方法: 取得String
	 *@return: String  通知书编号
	 */
	@Column(name ="FREEZE_NO",nullable=true,length=20)
	public String getFreezeNo(){
		return this.freezeNo;
	}
	/**
	 *方法: 设置String
	 *@param: String  通知书编号
	 */
	public void setFreezeNo(String freezeNo){
		this.freezeNo = freezeNo;
	}
	/**
	 *方法: 取得String
	 *@return: String  冻结/解冻机构
	 */
	@Column(name ="FREEZE_ORG",nullable=true,length=200)
	public String getFreezeOrg(){
		return this.freezeOrg;
	}
	/**
	 *方法: 设置String
	 *@param: String  冻结/解冻机构
	 */
	public void setFreezeOrg(String freezeOrg){
		this.freezeOrg = freezeOrg;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  冻结开始时间
	 */
	@Column(name ="BEGIN_DATE",nullable=true)
	public Date getBeginDate(){
		return this.beginDate;
	}
	/**
	 *方法: 设置Date
	 *@param: Date  冻结开始时间
	 */
	public void setBeginDate(Date beginDate){
		this.beginDate = beginDate;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  冻结结束时间
	 */
	@Column(name ="END_DATE",nullable=true)
	public Date getEndDate(){
		return this.endDate;
	}
	/**
	 *方法: 设置Date
	 *@param: Date  冻结结束时间
	 */
	public void setEndDate(Date endDate){
		this.endDate = endDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  执行人公务证
	 */
	@Column(name ="EXEC_OFFICIAL_NO",nullable=true,length=50)
	public String getExecOfficialNo(){
		return this.execOfficialNo;
	}
	/**
	 *方法: 设置String
	 *@param: String  执行人公务证
	 */
	public void setExecOfficialNo(String execOfficialNo){
		this.execOfficialNo = execOfficialNo;
	}
	/**
	 *方法: 取得String
	 *@return: String  执行人省份证
	 */
	@Column(name ="EXEC_CARD_NO",nullable=true,length=50)
	public String getExecCardNo(){
		return this.execCardNo;
	}
	/**
	 *方法: 设置String
	 *@param: String  执行人省份证
	 */
	public void setExecCardNo(String execCardNo){
		this.execCardNo = execCardNo;
	}
	/**
	 *方法: 取得String
	 *@return: String  冻结/解冻原因
	 */
	@Column(name ="FREEZE_REASON",nullable=true,length=200)
	public String getFreezeReason(){
		return this.freezeReason;
	}
	/**
	 *方法: 设置String
	 *@param: String  冻结/解冻原因
	 */
	public void setFreezeReason(String freezeReason){
		this.freezeReason = freezeReason;
	}
	/**
	 *方法: 取得Integer
	 *@return: Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置Integer
	 *@param: Integer  状态
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得String
	 *@return: String  解冻数据
	 */
	@Column(name ="UN_FREEZE_ID",nullable=true,length=32)
	public String getUnFreezeId(){
		return this.unFreezeId;
	}
	/**
	 *方法: 设置String
	 *@param: String  解冻数据
	 */
	public void setUnFreezeId(String unFreezeId){
		this.unFreezeId = unFreezeId;
	}
	/**
	 *方法: 取得String
	 *@return: String  打回原因
	 */
	@Column(name ="REFUSE_WHY",nullable=true,length=200)
	public String getRefuseWhy(){
		return this.refuseWhy;
	}
	/**
	 *方法: 设置String
	 *@param: String  打回原因
	 */
	public void setRefuseWhy(String refuseWhy){
		this.refuseWhy = refuseWhy;
	}
	/**
	 *方法: 取得String
	 *@return: String  冻结解除标志
	 */
	@Column(name ="FREEZE_END_SIGN",nullable=true,length=1)
	public String getFreezeEndSign(){
		return this.freezeEndSign;
	}
	/**
	 *方法: 设置String
	 *@param: String  冻结解除标志
	 */
	public void setFreezeEndSign(String freezeEndSign){
		this.freezeEndSign = freezeEndSign;
	}
	/**
	 *方法: 取得String
	 *@return: String  冻结已提醒标志
	 */
	@Column(name ="DEDUCT_SIGN",nullable=true,length=1)
	public String getDeductSign(){
		return this.deductSign;
	}
	/**
	 *方法: 设置String
	 *@param: String  冻结已提醒标志
	 */
	public void setDeductSign(String deductSign){
		this.deductSign = deductSign;
	}
	/**
	 *方法: 取得String
	 *@return: String  账户状态
	 */
	@Column(name ="CUR_ACC_STATE",nullable=true,length=1)
	public String getCurAccState(){
		return this.curAccState;
	}
	/**
	 *方法: 设置String
	 *@param: String  账户状态
	 */
	public void setCurAccState(String curAccState){
		this.curAccState = curAccState;
	}
	/**
	 *方法: 取得String
	 *@return: String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置String
	 *@param: String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得Date
	 *@return: Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置Date
	 *@param: Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得String
	 *@return: String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置String
	 *@param: String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
}
