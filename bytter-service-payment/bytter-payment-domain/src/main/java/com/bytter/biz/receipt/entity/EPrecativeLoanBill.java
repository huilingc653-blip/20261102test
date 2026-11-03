package com.bytter.biz.receipt.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**   
 * @Title: Entity
 * @Description: 委托贷款电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:30:40
 * @version 1.0
 *
 */
@Entity
@Table(name = "E_PRECATIVE_LOAN_BILL", schema = "")
@SuppressWarnings("serial")
public class EPrecativeLoanBill extends IdEntity implements java.io.Serializable {
	/**电子回单号码*/
	private java.lang.String billNo;
	/**单位ID*/
	private java.lang.String corpId;
	/**电子回单类型21-委托贷款通知单22-委托贷款还款通知单*/
	private java.lang.String billType;
	/**委托单位*/
	private java.lang.String precativeCorp;
	/**委托账号*/
	private java.lang.String precativeAcc;
	/**借款单位*/
	private java.lang.String loanCorp;
	/**借款账号*/
	private java.lang.String loanAcc;
	/**委托贷款账号*/
	private java.lang.String precativeLoanAcc;
	/**贷款日期*/
	private java.util.Date loanDate;
	/**到期日期*/
	private java.util.Date deadlineDate;
	/**还款日期*/
	private java.util.Date repaymentDate;
	/**利率*/
	private java.math.BigDecimal loanRate;
	/**贷款金额*/
	private java.math.BigDecimal loanMoney;
	/**币别*/
	private java.lang.String curId;
	/**合同号*/
	private java.lang.String contractCode;
	/**借方*/
	private java.lang.String debitAcc;
	/**贷方*/
	private java.lang.String creditAcc;
	/**记账员*/
	private java.lang.String tallyerCode;
	/**复核员*/
	private String checkerCode;
	/**tallyDate*/
	private java.util.Date tallyDate;
	/**凭证号*/
	private java.lang.String voucherNo;
	/**原始凭证号*/
	private java.lang.String oldVoucher;
	/**加密随机数*/
	private java.lang.String random;
	/**产生编码日期*/
	private java.util.Date encryDate;
	/**编码源*/
	private java.lang.String encrySource;
	/**subscriber*/
	private java.lang.String subscriber;
	/**验证码*/
	private java.lang.String validateCode;
	/**验证标志10-已验证11-验证失败12-未验证默认为12*/
	private java.lang.Integer isValidate;
	/**验证人*/
	private java.lang.String validBy;
	/**验证时间*/
	private java.util.Date validDate;
	/**打印次数*/
	private java.lang.Integer prints;
	/**状态1，新输入-1，删除2，冲帐*/
	private java.lang.Integer status;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**全局唯一ID*/
	private String busGlobalId;
	
	
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单号码
	 */
	@Column(name ="BILL_NO",nullable=false,length=13)
	public java.lang.String getBillNo(){
		return this.billNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单号码
	 */
	public void setBillNo(java.lang.String billNo){
		this.billNo = billNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  单位ID
	 */
	@Column(name ="CORP_ID",nullable=false,length=32)
	public java.lang.String getCorpId(){
		return this.corpId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId){
		this.corpId = corpId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  电子回单类型21-委托贷款通知单22-委托贷款还款通知单
	 */
	@Column(name ="BILL_TYPE",nullable=false,length=2)
	public java.lang.String getBillType(){
		return this.billType;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  电子回单类型21-委托贷款通知单22-委托贷款还款通知单
	 */
	public void setBillType(java.lang.String billType){
		this.billType = billType;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托单位
	 */
	@Column(name ="PRECATIVE_CORP",nullable=true,length=60)
	public java.lang.String getPrecativeCorp(){
		return this.precativeCorp;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托单位
	 */
	public void setPrecativeCorp(java.lang.String precativeCorp){
		this.precativeCorp = precativeCorp;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托账号
	 */
	@Column(name ="PRECATIVE_ACC",nullable=true,length=30)
	public java.lang.String getPrecativeAcc(){
		return this.precativeAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托账号
	 */
	public void setPrecativeAcc(java.lang.String precativeAcc){
		this.precativeAcc = precativeAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借款单位
	 */
	@Column(name ="LOAN_CORP",nullable=true,length=60)
	public java.lang.String getLoanCorp(){
		return this.loanCorp;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借款单位
	 */
	public void setLoanCorp(java.lang.String loanCorp){
		this.loanCorp = loanCorp;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借款账号
	 */
	@Column(name ="LOAN_ACC",nullable=true,length=30)
	public java.lang.String getLoanAcc(){
		return this.loanAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借款账号
	 */
	public void setLoanAcc(java.lang.String loanAcc){
		this.loanAcc = loanAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  委托贷款账号
	 */
	@Column(name ="PRECATIVE_LOAN_ACC",nullable=true,length=30)
	public java.lang.String getPrecativeLoanAcc(){
		return this.precativeLoanAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  委托贷款账号
	 */
	public void setPrecativeLoanAcc(java.lang.String precativeLoanAcc){
		this.precativeLoanAcc = precativeLoanAcc;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  贷款日期
	 */
	@Column(name ="LOAN_DATE",nullable=true)
	public java.util.Date getLoanDate(){
		return this.loanDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  贷款日期
	 */
	public void setLoanDate(java.util.Date loanDate){
		this.loanDate = loanDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  到期日期
	 */
	@Column(name ="DEADLINE_DATE",nullable=true)
	public java.util.Date getDeadlineDate(){
		return this.deadlineDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  到期日期
	 */
	public void setDeadlineDate(java.util.Date deadlineDate){
		this.deadlineDate = deadlineDate;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  还款日期
	 */
	@Column(name ="REPAYMENT_DATE",nullable=true)
	public java.util.Date getRepaymentDate(){
		return this.repaymentDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  还款日期
	 */
	public void setRepaymentDate(java.util.Date repaymentDate){
		this.repaymentDate = repaymentDate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  利率
	 */
	@Column(name ="LOAN_RATE",nullable=true,scale=6,length=15)
	public java.math.BigDecimal getLoanRate(){
		return this.loanRate;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  利率
	 */
	public void setLoanRate(java.math.BigDecimal loanRate){
		this.loanRate = loanRate;
	}
	/**
	 *方法: 取得java.math.BigDecimal
	 *@return: java.math.BigDecimal  贷款金额
	 */
	@Column(name ="LOAN_MONEY",nullable=true,scale=2,length=15)
	public java.math.BigDecimal getLoanMoney(){
		return this.loanMoney;
	}
	/**
	 *方法: 设置java.math.BigDecimal
	 *@param: java.math.BigDecimal  贷款金额
	 */
	public void setLoanMoney(java.math.BigDecimal loanMoney){
		this.loanMoney = loanMoney;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  币别
	 */
	@Column(name ="CUR_ID",nullable=true,length=32)
	public java.lang.String getCurId(){
		return this.curId;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  币别
	 */
	public void setCurId(java.lang.String curId){
		this.curId = curId;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  合同号
	 */
	@Column(name ="CONTRACT_CODE",nullable=true,length=30)
	public java.lang.String getContractCode(){
		return this.contractCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  合同号
	 */
	public void setContractCode(java.lang.String contractCode){
		this.contractCode = contractCode;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  借方
	 */
	@Column(name ="DEBIT_ACC",nullable=true,length=500)
	public java.lang.String getDebitAcc(){
		return this.debitAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  借方
	 */
	public void setDebitAcc(java.lang.String debitAcc){
		this.debitAcc = debitAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  贷方
	 */
	@Column(name ="CREDIT_ACC",nullable=true,length=500)
	public java.lang.String getCreditAcc(){
		return this.creditAcc;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  贷方
	 */
	public void setCreditAcc(java.lang.String creditAcc){
		this.creditAcc = creditAcc;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  记账员
	 */
	@Column(name ="TALLYER_CODE",nullable=true,length=32)
	public java.lang.String getTallyerCode(){
		return this.tallyerCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  记账员
	 */
	public void setTallyerCode(java.lang.String tallyerCode){
		this.tallyerCode = tallyerCode;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  tallyDate
	 */
	@Column(name ="TALLY_DATE",nullable=true)
	public java.util.Date getTallyDate(){
		return this.tallyDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  tallyDate
	 */
	public void setTallyDate(java.util.Date tallyDate){
		this.tallyDate = tallyDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  凭证号
	 */
	@Column(name ="VOUCHER_NO",nullable=true,length=40)
	public java.lang.String getVoucherNo(){
		return this.voucherNo;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  凭证号
	 */
	public void setVoucherNo(java.lang.String voucherNo){
		this.voucherNo = voucherNo;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  原始凭证号
	 */
	@Column(name ="OLD_VOUCHER",nullable=true,length=50)
	public java.lang.String getOldVoucher(){
		return this.oldVoucher;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  原始凭证号
	 */
	public void setOldVoucher(java.lang.String oldVoucher){
		this.oldVoucher = oldVoucher;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  加密随机数
	 */
	@Column(name ="RANDOM",nullable=true,length=20)
	public java.lang.String getRandom(){
		return this.random;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  加密随机数
	 */
	public void setRandom(java.lang.String random){
		this.random = random;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  产生编码日期
	 */
	@Column(name ="ENCRY_DATE",nullable=true)
	public java.util.Date getEncryDate(){
		return this.encryDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  产生编码日期
	 */
	public void setEncryDate(java.util.Date encryDate){
		this.encryDate = encryDate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  编码源
	 */
	@Column(name ="ENCRY_SOURCE",nullable=true,length=200)
	public java.lang.String getEncrySource(){
		return this.encrySource;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  编码源
	 */
	public void setEncrySource(java.lang.String encrySource){
		this.encrySource = encrySource;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  subscriber
	 */
	@Column(name ="SUBSCRIBER",nullable=true,length=50)
	public java.lang.String getSubscriber(){
		return this.subscriber;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  subscriber
	 */
	public void setSubscriber(java.lang.String subscriber){
		this.subscriber = subscriber;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  验证码
	 */
	@Column(name ="VALIDATE_CODE",nullable=true,length=100)
	public java.lang.String getValidateCode(){
		return this.validateCode;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  验证码
	 */
	public void setValidateCode(java.lang.String validateCode){
		this.validateCode = validateCode;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  验证标志10-已验证11-验证失败12-未验证默认为12
	 */
	@Column(name ="IS_VALIDATE",nullable=true,length=2)
	public java.lang.Integer getIsValidate(){
		return this.isValidate;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  验证标志10-已验证11-验证失败12-未验证默认为12
	 */
	public void setIsValidate(java.lang.Integer isValidate){
		this.isValidate = isValidate;
	}
	/**
	 *方法: 取得java.lang.String
	 *@return: java.lang.String  验证人
	 */
	@Column(name ="VALID_BY",nullable=true,length=50)
	public java.lang.String getValidBy(){
		return this.validBy;
	}
	/**
	 *方法: 设置java.lang.String
	 *@param: java.lang.String  验证人
	 */
	public void setValidBy(java.lang.String validBy){
		this.validBy = validBy;
	}
	/**
	 *方法: 取得java.util.Date
	 *@return: java.util.Date  验证时间
	 */
	@Column(name ="VALID_DATE",nullable=true)
	public java.util.Date getValidDate(){
		return this.validDate;
	}
	/**
	 *方法: 设置java.util.Date
	 *@param: java.util.Date  验证时间
	 */
	public void setValidDate(java.util.Date validDate){
		this.validDate = validDate;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  打印次数
	 */
	@Column(name ="PRINTS",nullable=true,length=10)
	public java.lang.Integer getPrints(){
		return this.prints;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  打印次数
	 */
	public void setPrints(java.lang.Integer prints){
		this.prints = prints;
	}
	/**
	 *方法: 取得java.lang.Integer
	 *@return: java.lang.Integer  状态1，新输入-1，删除2，冲帐
	 */
	@Column(name ="STATUS",nullable=false,length=10)
	public java.lang.Integer getStatus(){
		return this.status;
	}
	/**
	 *方法: 设置java.lang.Integer
	 *@param: java.lang.Integer  状态1，新输入-1，删除2，冲帐
	 */
	public void setStatus(java.lang.Integer status){
		this.status = status;
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
	@Column(name ="CHECKER_CODE",nullable=true,length=32)
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	@Column(name ="BUS_GLOBAL_ID",nullable=true,length=50)
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	@Override
	public String toString() {
		return "EPrecativeLoanBill [billNo=" + billNo + ", corpId=" + corpId + ", billType=" + billType
				+ ", precativeCorp=" + precativeCorp + ", precativeAcc=" + precativeAcc + ", loanCorp=" + loanCorp
				+ ", loanAcc=" + loanAcc + ", precativeLoanAcc=" + precativeLoanAcc + ", loanDate=" + loanDate
				+ ", deadlineDate=" + deadlineDate + ", repaymentDate=" + repaymentDate + ", loanRate=" + loanRate
				+ ", loanMoney=" + loanMoney + ", curId=" + curId + ", contractCode=" + contractCode + ", debitAcc="
				+ debitAcc + ", creditAcc=" + creditAcc + ", tallyerCode=" + tallyerCode + ", checkerCode="
				+ checkerCode + ", tallyDate=" + tallyDate + ", voucherNo=" + voucherNo + ", random=" + random
				+ ", encryDate=" + encryDate + ", encrySource=" + encrySource + ", subscriber=" + subscriber
				+ ", validateCode=" + validateCode + ", isValidate=" + isValidate + ", validBy=" + validBy
				+ ", validDate=" + validDate + ", prints=" + prints + ", status=" + status + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate
				+ ", busGlobalId=" + busGlobalId + "]";
	}
	
	
}
