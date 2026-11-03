package com.bytter.biz.netbank.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 委托收款
 * @author onlineGenerator
 * @date 2017-05-26 11:14:59
 * @version 1.0
 *
 */
@Entity
@Table(name = "CGS_BILL", schema = "")
@SuppressWarnings("serial")
public class CgsBill extends IdEntity implements java.io.Serializable {
	/**委托日期*/
	private java.util.Date consignDate;
	/**委托号码*/
	private java.lang.String consignCode;
	/**付款单位ID*/
	private java.lang.String payerCorpId;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**付款账号名称*/
	private java.lang.String payerAccName;
	/**收款单位ID*/
	private java.lang.String payeeCorpId;
	/**收款账号*/
	private java.lang.String payeeAcc;
	/**收款账号名称*/
	private java.lang.String payeeAccName;
	/**金额*/
	private java.math.BigDecimal money;
	/**合同号*/
	private java.lang.String pactCode;
	/**发票号*/
	private java.lang.String invoiceCode;
	/**摘要*/
	private java.lang.String abs;
	/**结算中心手续费*/
	private java.math.BigDecimal chargeMoney;
	/**证书信息*/
	private java.lang.String caInfoId;
	/**下一步审核人*/
	private java.lang.String gatherNextAuditors;
	/**当前审核步骤*/
	private java.lang.Integer gatherAuditStep;
	/**审核状态*/
	private java.lang.Integer gatherAuditState;
	/**最后审核日期*/
	private java.util.Date gatherAuditDate;
	/**审核人列表*/
	private java.lang.String gatherAuditors;
	/**安全认证结果*/
	private java.lang.Integer judgeResult;
	/**认证说明*/
	private java.lang.String judgeNarrate;
	/**认证人代码*/
	private java.lang.String judgerCode;
	/**认证人名称*/
	private java.lang.String judgerName;
	/**认证日期*/
	private java.util.Date judgeDate;
	/**签收标识*/
	private java.lang.Integer signFlag;
	/**签收日期*/
	private java.util.Date signDate;
	/**签收人代码*/
	private java.lang.String signerCode;
	/**签收人名称*/
	private java.lang.String signerName;
	/**下一步审核人*/
	private java.lang.String confirmNextAuditors;
	/**当前审核步骤*/
	private java.lang.Integer confirmAuditStep;
	/**审核状态*/
	private java.lang.Integer confirmAuditState;
	/**最后审核日期*/
	private java.util.Date confirmAuditDate;
	/**审核人列表*/
	private java.lang.String confirmAuditors;
	/**记账标识*/
	private java.lang.Integer tallyFlag;
	/**记账说明*/
	private java.lang.String tallyNarrate;
	/**记账员代码*/
	private java.lang.String tallierCode;
	/**记账员名称*/
	private java.lang.String tallierName;
	/**记账日期*/
	private java.util.Date tallyDate;
	/**复核日期*/
	private java.util.Date checkDate;
	/**复核员代码*/
	private java.lang.String checkCode;
	/**记账凭证号*/
	private java.lang.String voucherNo;
	/**业务结束标志*/
	private java.lang.Integer finishFlag;
	/**业务结束说明*/
	private java.lang.String finishNarrate;
	/**网点账号*/
	private java.lang.String netAcc;
	/**状态*/
	private java.lang.Integer status;
	/**记账流水号*/
	private java.lang.String orderNo;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**附件*/
	private java.lang.String attachmentId;
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  委托日期
	 */
	@Column(name ="CONSIGN_DATE",nullable=false)
	public java.util.Date getConsignDate(){
		return this.consignDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  委托日期
	 */
	public void setConsignDate(java.util.Date consignDate){
		this.consignDate = consignDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托号码
	 */
	@Column(name ="CONSIGN_CODE",nullable=false,length=100)
	public java.lang.String getConsignCode(){
		return this.consignCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托号码
	 */
	public void setConsignCode(java.lang.String consignCode){
		this.consignCode = consignCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款单位ID
	 */
	@Column(name ="PAYER_CORP_ID",nullable=false,length=32)
	public java.lang.String getPayerCorpId(){
		return this.payerCorpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款单位ID
	 */
	public void setPayerCorpId(java.lang.String payerCorpId){
		this.payerCorpId = payerCorpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号
	 */
	@Column(name ="PAYER_ACC",nullable=true,length=40)
	public java.lang.String getPayerAcc(){
		return this.payerAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号
	 */
	public void setPayerAcc(java.lang.String payerAcc){
		this.payerAcc = payerAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  付款账号名称
	 */
	@Column(name ="PAYER_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayerAccName(){
		return this.payerAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  付款账号名称
	 */
	public void setPayerAccName(java.lang.String payerAccName){
		this.payerAccName = payerAccName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款单位ID
	 */
	@Column(name ="PAYEE_CORP_ID",nullable=false,length=32)
	public java.lang.String getPayeeCorpId(){
		return this.payeeCorpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款单位ID
	 */
	public void setPayeeCorpId(java.lang.String payeeCorpId){
		this.payeeCorpId = payeeCorpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号
	 */
	@Column(name ="PAYEE_ACC",nullable=true,length=40)
	public java.lang.String getPayeeAcc(){
		return this.payeeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc){
		this.payeeAcc = payeeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  收款账号名称
	 */
	@Column(name ="PAYEE_ACC_NAME",nullable=true,length=150)
	public java.lang.String getPayeeAccName(){
		return this.payeeAccName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  收款账号名称
	 */
	public void setPayeeAccName(java.lang.String payeeAccName){
		this.payeeAccName = payeeAccName;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  金额
	 */
	@Column(name ="MONEY",nullable=false,scale=2,length=15)
	public java.math.BigDecimal getMoney(){
		return this.money;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  金额
	 */
	public void setMoney(java.math.BigDecimal money){
		this.money = money;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同号
	 */
	@Column(name ="PACT_CODE",nullable=true,length=100)
	public java.lang.String getPactCode(){
		return this.pactCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同号
	 */
	public void setPactCode(java.lang.String pactCode){
		this.pactCode = pactCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  发票号
	 */
	@Column(name ="INVOICE_CODE",nullable=true,length=100)
	public java.lang.String getInvoiceCode(){
		return this.invoiceCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  发票号
	 */
	public void setInvoiceCode(java.lang.String invoiceCode){
		this.invoiceCode = invoiceCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  摘要
	 */
	@Column(name ="ABS",nullable=true,length=100)
	public java.lang.String getAbs(){
		return this.abs;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  摘要
	 */
	public void setAbs(java.lang.String abs){
		this.abs = abs;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  结算中心手续费
	 */
	@Column(name ="CHARGE_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getChargeMoney(){
		return this.chargeMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  结算中心手续费
	 */
	public void setChargeMoney(java.math.BigDecimal chargeMoney){
		this.chargeMoney = chargeMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  证书信息
	 */
	@Column(name ="CA_INFO_ID",nullable=true,length=32)
	public java.lang.String getCaInfoId(){
		return this.caInfoId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  证书信息
	 */
	public void setCaInfoId(java.lang.String caInfoId){
		this.caInfoId = caInfoId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  下一步审核人
	 */
	@Column(name ="GATHER_NEXT_AUDITORS",nullable=true,length=200)
	public java.lang.String getGatherNextAuditors(){
		return this.gatherNextAuditors;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  下一步审核人
	 */
	public void setGatherNextAuditors(java.lang.String gatherNextAuditors){
		this.gatherNextAuditors = gatherNextAuditors;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  当前审核步骤
	 */
	@Column(name ="GATHER_AUDIT_STEP",nullable=true,length=10)
	public java.lang.Integer getGatherAuditStep(){
		return this.gatherAuditStep;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  当前审核步骤
	 */
	public void setGatherAuditStep(java.lang.Integer gatherAuditStep){
		this.gatherAuditStep = gatherAuditStep;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核状态
	 */
	@Column(name ="GATHER_AUDIT_STATE",nullable=false,length=10)
	public java.lang.Integer getGatherAuditState(){
		return this.gatherAuditState;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核状态
	 */
	public void setGatherAuditState(java.lang.Integer gatherAuditState){
		this.gatherAuditState = gatherAuditState;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后审核日期
	 */
	@Column(name ="GATHER_AUDIT_DATE",nullable=true)
	public java.util.Date getGatherAuditDate(){
		return this.gatherAuditDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后审核日期
	 */
	public void setGatherAuditDate(java.util.Date gatherAuditDate){
		this.gatherAuditDate = gatherAuditDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审核人列表
	 */
	@Column(name ="GATHER_AUDITORS",nullable=true,length=200)
	public java.lang.String getGatherAuditors(){
		return this.gatherAuditors;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审核人列表
	 */
	public void setGatherAuditors(java.lang.String gatherAuditors){
		this.gatherAuditors = gatherAuditors;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  安全认证结果
	 */
	@Column(name ="JUDGE_RESULT",nullable=true,length=10)
	public java.lang.Integer getJudgeResult(){
		return this.judgeResult;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  安全认证结果
	 */
	public void setJudgeResult(java.lang.Integer judgeResult){
		this.judgeResult = judgeResult;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  认证说明
	 */
	@Column(name ="JUDGE_NARRATE",nullable=true,length=200)
	public java.lang.String getJudgeNarrate(){
		return this.judgeNarrate;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  认证说明
	 */
	public void setJudgeNarrate(java.lang.String judgeNarrate){
		this.judgeNarrate = judgeNarrate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  认证人代码
	 */
	@Column(name ="JUDGER_CODE",nullable=true,length=10)
	public java.lang.String getJudgerCode(){
		return this.judgerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  认证人代码
	 */
	public void setJudgerCode(java.lang.String judgerCode){
		this.judgerCode = judgerCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  认证人名称
	 */
	@Column(name ="JUDGER_NAME",nullable=true,length=30)
	public java.lang.String getJudgerName(){
		return this.judgerName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  认证人名称
	 */
	public void setJudgerName(java.lang.String judgerName){
		this.judgerName = judgerName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  认证日期
	 */
	@Column(name ="JUDGE_DATE",nullable=true)
	public java.util.Date getJudgeDate(){
		return this.judgeDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  认证日期
	 */
	public void setJudgeDate(java.util.Date judgeDate){
		this.judgeDate = judgeDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  签收标识
	 */
	@Column(name ="SIGN_FLAG",nullable=false,length=10)
	public java.lang.Integer getSignFlag(){
		return this.signFlag;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  签收标识
	 */
	public void setSignFlag(java.lang.Integer signFlag){
		this.signFlag = signFlag;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  签收日期
	 */
	@Column(name ="SIGN_DATE",nullable=true)
	public java.util.Date getSignDate(){
		return this.signDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  签收日期
	 */
	public void setSignDate(java.util.Date signDate){
		this.signDate = signDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  签收人代码
	 */
	@Column(name ="SIGNER_CODE",nullable=true,length=10)
	public java.lang.String getSignerCode(){
		return this.signerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  签收人代码
	 */
	public void setSignerCode(java.lang.String signerCode){
		this.signerCode = signerCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  签收人名称
	 */
	@Column(name ="SIGNER_NAME",nullable=true,length=30)
	public java.lang.String getSignerName(){
		return this.signerName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  签收人名称
	 */
	public void setSignerName(java.lang.String signerName){
		this.signerName = signerName;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  下一步审核人
	 */
	@Column(name ="CONFIRM_NEXT_AUDITORS",nullable=true,length=200)
	public java.lang.String getConfirmNextAuditors(){
		return this.confirmNextAuditors;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  下一步审核人
	 */
	public void setConfirmNextAuditors(java.lang.String confirmNextAuditors){
		this.confirmNextAuditors = confirmNextAuditors;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  当前审核步骤
	 */
	@Column(name ="CONFIRM_AUDIT_STEP",nullable=true,length=10)
	public java.lang.Integer getConfirmAuditStep(){
		return this.confirmAuditStep;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  当前审核步骤
	 */
	public void setConfirmAuditStep(java.lang.Integer confirmAuditStep){
		this.confirmAuditStep = confirmAuditStep;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  审核状态
	 */
	@Column(name ="CONFIRM_AUDIT_STATE",nullable=false,length=10)
	public java.lang.Integer getConfirmAuditState(){
		return this.confirmAuditState;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  审核状态
	 */
	public void setConfirmAuditState(java.lang.Integer confirmAuditState){
		this.confirmAuditState = confirmAuditState;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  最后审核日期
	 */
	@Column(name ="CONFIRM_AUDIT_DATE",nullable=true)
	public java.util.Date getConfirmAuditDate(){
		return this.confirmAuditDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  最后审核日期
	 */
	public void setConfirmAuditDate(java.util.Date confirmAuditDate){
		this.confirmAuditDate = confirmAuditDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  审核人列表
	 */
	@Column(name ="CONFIRM_AUDITORS",nullable=true,length=200)
	public java.lang.String getConfirmAuditors(){
		return this.confirmAuditors;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  审核人列表
	 */
	public void setConfirmAuditors(java.lang.String confirmAuditors){
		this.confirmAuditors = confirmAuditors;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  记账标识
	 */
	@Column(name ="TALLY_FLAG",nullable=false,length=10)
	public java.lang.Integer getTallyFlag(){
		return this.tallyFlag;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  记账标识
	 */
	public void setTallyFlag(java.lang.Integer tallyFlag){
		this.tallyFlag = tallyFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账说明
	 */
	@Column(name ="TALLY_NARRATE",nullable=true,length=200)
	public java.lang.String getTallyNarrate(){
		return this.tallyNarrate;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账说明
	 */
	public void setTallyNarrate(java.lang.String tallyNarrate){
		this.tallyNarrate = tallyNarrate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员代码
	 */
	@Column(name ="TALLIER_CODE",nullable=true,length=10)
	public java.lang.String getTallierCode(){
		return this.tallierCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员代码
	 */
	public void setTallierCode(java.lang.String tallierCode){
		this.tallierCode = tallierCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员名称
	 */
	@Column(name ="TALLIER_NAME",nullable=true,length=30)
	public java.lang.String getTallierName(){
		return this.tallierName;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员名称
	 */
	public void setTallierName(java.lang.String tallierName){
		this.tallierName = tallierName;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  记账日期
	 */
	@Column(name ="TALLY_DATE",nullable=true)
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  记账日期
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  复核日期
	 */
	@Column(name ="CHECK_DATE",nullable=true)
	public java.util.Date getCheckDate(){
		return this.checkDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  复核日期
	 */
	public void setCheckDate(java.util.Date checkDate){
		this.checkDate = checkDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  复核员代码
	 */
	@Column(name ="CHECK_CODE",nullable=true,length=10)
	public java.lang.String getCheckCode(){
		return this.checkCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  复核员代码
	 */
	public void setCheckCode(java.lang.String checkCode){
		this.checkCode = checkCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账凭证号
	 */
	@Column(name ="VOUCHER_NO",nullable=true,length=30)
	public java.lang.String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账凭证号
	 */
	public void setVoucherNo(java.lang.String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  业务结束标志
	 */
	@Column(name ="FINISH_FLAG",nullable=false,length=10)
	public java.lang.Integer getFinishFlag(){
		return this.finishFlag;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  业务结束标志
	 */
	public void setFinishFlag(java.lang.Integer finishFlag){
		this.finishFlag = finishFlag;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  业务结束说明
	 */
	@Column(name ="FINISH_NARRATE",nullable=true,length=200)
	public java.lang.String getFinishNarrate(){
		return this.finishNarrate;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  业务结束说明
	 */
	public void setFinishNarrate(java.lang.String finishNarrate){
		this.finishNarrate = finishNarrate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  网点账号
	 */
	@Column(name ="NET_ACC",nullable=true,length=16)
	public java.lang.String getNetAcc(){
		return this.netAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  网点账号
	 */
	public void setNetAcc(java.lang.String netAcc){
		this.netAcc = netAcc;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态
	 */
	@Column(name ="STATUS",nullable=true,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账流水号
	 */
	@Column(name ="ORDER_NO",nullable=true,length=18)
	public java.lang.String getOrderNo(){
		return this.orderNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账流水号
	 */
	public void setOrderNo(java.lang.String orderNo){
		this.orderNo = orderNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  创建人登录名称
	 */
	@Column(name ="CREATE_BY",nullable=true,length=50)
	public java.lang.String getCreateBy(){
		return this.createBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy){
		this.createBy = createBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  创建日期
	 */
	@Column(name ="CREATE_DATE",nullable=true)
	public java.util.Date getCreateDate(){
		return this.createDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate){
		this.createDate = createDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  更新人登录名称
	 */
	@Column(name ="UPDATE_BY",nullable=true,length=50)
	public java.lang.String getUpdateBy(){
		return this.updateBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy){
		this.updateBy = updateBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  更新日期
	 */
	@Column(name ="UPDATE_DATE",nullable=true)
	public java.util.Date getUpdateDate(){
		return this.updateDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate){
		this.updateDate = updateDate;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: 设置java.lang.String  附件
	 */
	@Column(name ="ATTACHMENT_ID",nullable=true,length=32)
	public String getAttachmentId() {
		return this.attachmentId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@param: 取得java.lang.String  附件
	 */
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
}
