package com.bytter.biz.finacial.accbook.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: FC_CHARGE_ACCRUE
 * @author onlineGenerator
 * @date 2017-07-04 15:14:11
 * @version 1.0
 *
 */
@Entity
@Table(name = "FC_CHARGE_ACCRUE", schema = "")
@SuppressWarnings("serial")
public class FcChargeAccrue extends IdEntity implements java.io.Serializable {
	/**借方账号*/
	private java.lang.String account;
	/**凭证种类代码*/
	private java.lang.String voucherType;
	/**是否入账*/
	private java.lang.String tallySign;
	/**费用规则*/
	private java.lang.String chargeRuleId;
	/**费用类型*/
	private java.lang.String chargeTypeId;
	/**费用金额*/
	private java.math.BigDecimal chargeMoney;
	/**计提时间*/
	private java.util.Date accrueTime;
	/**经办人代码*/
	private java.lang.String operatorCode;
	/**复核时间*/
	private java.util.Date checkTime;
	/**复核人代码*/
	private java.lang.String checkerCode;
	/**关联流水号*/
	private java.lang.String targetOrderNo;
	/**流水号*/
	private java.lang.String orderNo;
	/**原凭证的记账日期*/
	private java.util.Date targetTallyDate;
	
	/**
	 * 状态（0 删除  1正常）
	 */
	private String status;
	/**
	 * 新增冗余字段 cur_name、cur_code、acc_name
	 * @return
	 */
	private java.lang.String curCode;
	private java.lang.String curName;
	private java.lang.String accName;
	/* userName 不能随便加，有2个 人员
	private String userName;
	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}*/
	@Column(name ="ACC_NAME")
	public java.lang.String getAccName(){
		return this.accName;
	}
	public void setAccName(java.lang.String accName){
		this.accName = accName;
	}
	@Column(name ="CUR_CODE")
	public java.lang.String getCurCode(){
		return this.curCode;
	}
	public void setCurCode(java.lang.String curCode){
		this.curCode = curCode;
	}
	@Column(name ="CUR_NAME")
	public java.lang.String getCurName(){
		return this.curName;
	}
	public void setCurName(java.lang.String curName){
		this.curName = curName;
	}
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
	 *@return: java.lang.String  借方账号
	 */
	@Column(name ="ACCOUNT")
	public java.lang.String getAccount(){
		return this.account;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方账号
	 */
	public void setAccount(java.lang.String account){
		this.account = account;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证种类代码
	 */
	@Column(name ="VOUCHER_TYPE")
	public java.lang.String getVoucherType(){
		return this.voucherType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证种类代码
	 */
	public void setVoucherType(java.lang.String voucherType){
		this.voucherType = voucherType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  是否入账
	 */
	@Column(name ="TALLY_SIGN")
	public java.lang.String getTallySign(){
		return this.tallySign;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  是否入账
	 */
	public void setTallySign(java.lang.String tallySign){
		this.tallySign = tallySign;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  费用规则
	 */
	@Column(name ="CHARGE_RULE_ID")
	public java.lang.String getChargeRuleId(){
		return this.chargeRuleId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  费用规则
	 */
	public void setChargeRuleId(java.lang.String chargeRuleId){
		this.chargeRuleId = chargeRuleId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  费用类型
	 */
	@Column(name ="CHARGE_TYPE_ID")
	public java.lang.String getChargeTypeId(){
		return this.chargeTypeId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  费用类型
	 */
	public void setChargeTypeId(java.lang.String chargeTypeId){
		this.chargeTypeId = chargeTypeId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  费用金额
	 */
	@Column(name ="CHARGE_MONEY")
	public java.math.BigDecimal getChargeMoney(){
		return this.chargeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  费用金额
	 */
	public void setChargeMoney(java.math.BigDecimal chargeMoney){
		this.chargeMoney = chargeMoney;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  计提时间
	 */
	@Column(name ="ACCRUE_TIME")
	public java.util.Date getAccrueTime(){
		return this.accrueTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  计提时间
	 */
	public void setAccrueTime(java.util.Date accrueTime){
		this.accrueTime = accrueTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  经办人代码
	 */
	@Column(name ="OPERATOR_CODE")
	public java.lang.String getOperatorCode(){
		return this.operatorCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  经办人代码
	 */
	public void setOperatorCode(java.lang.String operatorCode){
		this.operatorCode = operatorCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核时间
	 */
	@Column(name ="CHECK_TIME")
	public java.util.Date getCheckTime(){
		return this.checkTime;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核时间
	 */
	public void setCheckTime(java.util.Date checkTime){
		this.checkTime = checkTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核人代码
	 */
	@Column(name ="CHECKER_CODE")
	public java.lang.String getCheckerCode(){
		return this.checkerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核人代码
	 */
	public void setCheckerCode(java.lang.String checkerCode){
		this.checkerCode = checkerCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关联流水号
	 */
	@Column(name ="TARGET_ORDER_NO")
	public java.lang.String getTargetOrderNo(){
		return this.targetOrderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关联流水号
	 */
	public void setTargetOrderNo(java.lang.String targetOrderNo){
		this.targetOrderNo = targetOrderNo;
	}
	
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  原凭证记账日期
	 */
	@Column(name ="TARGET_TALLY_DATE")
	public java.util.Date getTargetTallyDate() {
		return this.targetTallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建时间
	 */
	public void setTargetTallyDate(java.util.Date targetTallyDate){
		this.targetTallyDate = targetTallyDate;
	}
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  关联流水号
	 */
	@Column(name ="ORDER_NO")
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  关联流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  状态
	 */
	@Column(name ="STATUS")
	public java.lang.String getStatus(){
		return this.status;
	}
	
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String 状态
	 */
	public void setStatus(java.lang.String status){
		this.status = status;
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
