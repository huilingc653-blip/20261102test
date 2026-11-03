package com.bytter.tally.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 贷款发放表
 * @author onlineGenerator
 * @date 2017-07-28 11:27:00
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_LOAN_SEND", schema = "")
@SuppressWarnings("serial")
public class TallyCmsLoanSend extends IdEntity implements java.io.Serializable {
	private String billCode;
	/**发放合同*/
	private String contractCode;
	/**贷款信息编号*/
	private String loanCode;
	/**单位代码*/
	private String corpId;
	/**发放金额*/
	private BigDecimal loanMoney;
	/**贷款账号*/
	private String loanAcc;
	/**发放日期*/
	private Date loanDate;
	/**还款日期*/
	private Date repayDate;
	/**放款利率*/
	private BigDecimal loanRate;
	/**放款账号*/
	private String provideLoanAcc;
	/**放款币种*/
	private String curCode;
	/**逾期利率*/
	private BigDecimal overdueRate;
	/**展期利率*/
	private BigDecimal deferredRate;
	/**增值税税率*/
	private BigDecimal texRate;
	/**往来账号*/
	private String netAcc;
	/**状态*/
	private Integer status;
	/**业务状态*/
	private String bizStatus;
	/**记账凭证号*/
	private String voucherNo;
	/**记账流水号*/
	private String orderNo;
	/**记账跨中心凭证号*/
	private String voucherNo1;
	/**记账跨中心流水号*/
	private String orderNo1;
	/**打回原因*/
	private String refuseWhy;
	/**贷款发放方式*/
	private String loanSendOutType;
	/**浮动方式*/
	private String floatWay;
	/**点数或浮动比例*/
	private String pointsProportionText;
	/**点数或者比例*/
	private String pointsProportionSelect;
	/**利率调整生效日期*/
	private Date rateEntriesEffectDate;
	/**计息方式*/
	private String interestWay;
	/**基准利率类型*/
	private String referenceRateType;
	/**贷款周期*/
	private String repayCycle;
	/**利率复核生效日期*/
	private Date ratetimeoftakeeffect;
	/**支付方式*/
	private String payType;
	/**发放次数*/
	private Integer sendTime;
	/**借款单位*/
	private String borrowCorpId;
	/**还款方式*/
	private String repayType;
	/**协定费率*/
	private BigDecimal clbAgreementRate;
	/**固定费率*/
	private BigDecimal clbFixedRate;
	/**借贷关联的借款单号*/
	private String ploadcode;
	/**附件ID*/
	private String attachmentId;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人名称*/
	private String updateName;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**展期到期日期*/
	private Date deferredDate;
	/**发送标识*/
	private String sendSign;
	
	/**还款计划Id*/
	private String planId;
	/**待记账提醒标志*/
	private String remindSign;
	
	@Column(name ="BILL_CODE")
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发放合同
	 */
	@Column(name ="CONTRACT_CODE")
	public String getContractCode(){
		return this.contractCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发放合同
	 */
	public void setContractCode(String contractCode){
		this.contractCode = contractCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷款信息编号
	 */
	@Column(name ="LOAN_CODE")
	public String getLoanCode(){
		return this.loanCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷款信息编号
	 */
	public void setLoanCode(String loanCode){
		this.loanCode = loanCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位代码
	 */
	@Column(name ="CORP_ID")
	public String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位代码
	 */
	public void setCorpId(String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  发放金额
	 */
	@Column(name ="LOAN_MONEY")
	public BigDecimal getLoanMoney(){
		return this.loanMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  发放金额
	 */
	public void setLoanMoney(BigDecimal loanMoney){
		this.loanMoney = loanMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷款账号
	 */
	@Column(name ="LOAN_ACC")
	public String getLoanAcc(){
		return this.loanAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷款账号
	 */
	public void setLoanAcc(String loanAcc){
		this.loanAcc = loanAcc;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  发放日期
	 */
	@Column(name ="LOAN_DATE")
	public Date getLoanDate(){
		return this.loanDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  发放日期
	 */
	public void setLoanDate(Date loanDate){
		this.loanDate = loanDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  还款日期
	 */
	@Column(name ="REPAY_DATE")
	public Date getRepayDate(){
		return this.repayDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  还款日期
	 */
	public void setRepayDate(Date repayDate){
		this.repayDate = repayDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  放款利率
	 */
	@Column(name ="LOAN_RATE")
	public BigDecimal getLoanRate(){
		return this.loanRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  放款利率
	 */
	public void setLoanRate(BigDecimal loanRate){
		this.loanRate = loanRate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  放款账号
	 */
	@Column(name ="PROVIDE_LOAN_ACC")
	public String getProvideLoanAcc(){
		return this.provideLoanAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  放款账号
	 */
	public void setProvideLoanAcc(String provideLoanAcc){
		this.provideLoanAcc = provideLoanAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  放款币种
	 */
	@Column(name ="CUR_CODE")
	public String getCurCode(){
		return this.curCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  放款币种
	 */
	public void setCurCode(String curCode){
		this.curCode = curCode;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  逾期利率
	 */
	@Column(name ="OVERDUE_RATE")
	public BigDecimal getOverdueRate(){
		return this.overdueRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  逾期利率
	 */
	public void setOverdueRate(BigDecimal overdueRate){
		this.overdueRate = overdueRate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  展期利率
	 */
	@Column(name ="DEFERRED_RATE")
	public BigDecimal getDeferredRate(){
		return this.deferredRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  展期利率
	 */
	public void setDeferredRate(BigDecimal deferredRate){
		this.deferredRate = deferredRate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  增值税税率
	 */
	@Column(name ="TEX_RATE")
	public BigDecimal getTexRate(){
		return this.texRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  增值税税率
	 */
	public void setTexRate(BigDecimal texRate){
		this.texRate = texRate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  往来账号
	 */
	@Column(name ="NET_ACC")
	public String getNetAcc(){
		return this.netAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  往来账号
	 */
	public void setNetAcc(String netAcc){
		this.netAcc = netAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS")
	public Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(Integer status){
		this.status = status;
	}
	
	@Column(name ="BIZ_STATUS")
	public String getBizStatus() {
		return bizStatus;
	}
	public void setBizStatus(String bizStatus) {
		this.bizStatus = bizStatus;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账凭证号
	 */
	@Column(name ="VOUCHER_NO")
	public String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账凭证号
	 */
	public void setVoucherNo(String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账流水号
	 */
	@Column(name ="ORDER_NO")
	public String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账流水号
	 */
	public void setOrderNo(String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账跨中心凭证号
	 */
	@Column(name ="VOUCHER_NO1")
	public String getVoucherNo1(){
		return this.voucherNo1;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账跨中心凭证号
	 */
	public void setVoucherNo1(String voucherNo1){
		this.voucherNo1 = voucherNo1;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账跨中心流水号
	 */
	@Column(name ="ORDER_NO1")
	public String getOrderNo1(){
		return this.orderNo1;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账跨中心流水号
	 */
	public void setOrderNo1(String orderNo1){
		this.orderNo1 = orderNo1;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  打回原因
	 */
	@Column(name ="REFUSE_WHY")
	public String getRefuseWhy(){
		return this.refuseWhy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  打回原因
	 */
	public void setRefuseWhy(String refuseWhy){
		this.refuseWhy = refuseWhy;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷款发放方式
	 */
	@Column(name ="LOAN_SEND_OUT_TYPE")
	public String getLoanSendOutType(){
		return this.loanSendOutType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷款发放方式
	 */
	public void setLoanSendOutType(String loanSendOutType){
		this.loanSendOutType = loanSendOutType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  浮动方式
	 */
	@Column(name ="FLOAT_WAY")
	public String getFloatWay(){
		return this.floatWay;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  浮动方式
	 */
	public void setFloatWay(String floatWay){
		this.floatWay = floatWay;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  点数或浮动比例
	 */
	@Column(name ="POINTS_PROPORTION_TEXT")
	public String getPointsProportionText(){
		return this.pointsProportionText;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  点数或浮动比例
	 */
	public void setPointsProportionText(String pointsProportionText){
		this.pointsProportionText = pointsProportionText;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  点数或者比例
	 */
	@Column(name ="POINTS_PROPORTION_SELECT")
	public String getPointsProportionSelect(){
		return this.pointsProportionSelect;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  点数或者比例
	 */
	public void setPointsProportionSelect(String pointsProportionSelect){
		this.pointsProportionSelect = pointsProportionSelect;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  利率调整生效日期
	 */
	@Column(name ="RATE_ENTRIES_EFFECT_DATE")
	public Date getRateEntriesEffectDate(){
		return this.rateEntriesEffectDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  利率调整生效日期
	 */
	public void setRateEntriesEffectDate(Date rateEntriesEffectDate){
		this.rateEntriesEffectDate = rateEntriesEffectDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  计息方式
	 */
	@Column(name ="INTEREST_WAY")
	public String getInterestWay(){
		return this.interestWay;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  计息方式
	 */
	public void setInterestWay(String interestWay){
		this.interestWay = interestWay;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  基准利率类型
	 */
	@Column(name ="REFERENCE_RATE_TYPE")
	public String getReferenceRateType(){
		return this.referenceRateType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  基准利率类型
	 */
	public void setReferenceRateType(String referenceRateType){
		this.referenceRateType = referenceRateType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷款周期
	 */
	@Column(name ="REPAY_CYCLE")
	public String getRepayCycle(){
		return this.repayCycle;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷款周期
	 */
	public void setRepayCycle(String repayCycle){
		this.repayCycle = repayCycle;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  利率复核生效日期
	 */
	@Column(name ="RATETIMEOFTAKEEFFECT")
	public Date getRatetimeoftakeeffect(){
		return this.ratetimeoftakeeffect;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  利率复核生效日期
	 */
	public void setRatetimeoftakeeffect(Date ratetimeoftakeeffect){
		this.ratetimeoftakeeffect = ratetimeoftakeeffect;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  支付方式
	 */
	@Column(name ="PAY_TYPE")
	public String getPayType(){
		return this.payType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  支付方式
	 */
	public void setPayType(String payType){
		this.payType = payType;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  发放次数
	 */
	@Column(name ="SEND_TIME")
	public Integer getSendTime(){
		return this.sendTime;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  发放次数
	 */
	public void setSendTime(Integer sendTime){
		this.sendTime = sendTime;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借款单位
	 */
	@Column(name ="BORROW_CORP_ID")
	public String getBorrowCorpId(){
		return this.borrowCorpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借款单位
	 */
	public void setBorrowCorpId(String borrowCorpId){
		this.borrowCorpId = borrowCorpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  还款方式
	 */
	@Column(name ="REPAY_TYPE")
	public String getRepayType(){
		return this.repayType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  还款方式
	 */
	public void setRepayType(String repayType){
		this.repayType = repayType;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  协定费率
	 */
	@Column(name ="CLB_AGREEMENT_RATE")
	public BigDecimal getClbAgreementRate(){
		return this.clbAgreementRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  协定费率
	 */
	public void setClbAgreementRate(BigDecimal clbAgreementRate){
		this.clbAgreementRate = clbAgreementRate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  固定费率
	 */
	@Column(name ="CLB_FIXED_RATE")
	public BigDecimal getClbFixedRate(){
		return this.clbFixedRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  固定费率
	 */
	public void setClbFixedRate(BigDecimal clbFixedRate){
		this.clbFixedRate = clbFixedRate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借贷关联的借款单号
	 */
	@Column(name ="PLOADCODE")
	public String getPloadcode(){
		return this.ploadcode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借贷关联的借款单号
	 */
	public void setPloadcode(String ploadcode){
		this.ploadcode = ploadcode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人名称
	 */
	@Column(name ="CREATE_NAME")
	public String getCreateName(){
		return this.createName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人名称
	 */
	public void setCreateName(String createName){
		this.createName = createName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY")
	public String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE")
	public Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人名称
	 */
	@Column(name ="UPDATE_NAME")
	public String getUpdateName(){
		return this.updateName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人名称
	 */
	public void setUpdateName(String updateName){
		this.updateName = updateName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY")
	public String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE")
	public Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(Date updateDate){
		this.updateDate = updateDate;
	}
	
	@Column(name ="ATTACHMENT_ID")
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	
	@Column(name ="DEFERRED_DATE")
	public Date getDeferredDate() {
		return deferredDate;
	}
	
	public void setDeferredDate(Date deferredDate) {
		this.deferredDate = deferredDate;
	}
	
	@Column(name ="SEND_SIGN")
	public String getSendSign() {
		return sendSign;
	}
	public void setSendSign(String sendSign) {
		this.sendSign = sendSign;
	}
	@Transient
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	@Column(name ="REMIND_SIGN")
	public String getRemindSign() {
		return remindSign;
	}
	public void setRemindSign(String remindSign) {
		this.remindSign = remindSign;
	}
}
