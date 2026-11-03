package com.bytter.biz.receipt.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 贷款电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:30:01
 * @version 1.0
 *
 */
public class ELoanBillVO  {
	private String id;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "voucher_No")
	private String voucherNo;
	/**原始凭证号*/
	private String oldVoucher;
	/**加密随机数*/
	private String random;
	/**产生编码日期*/
	@BtCompositeSearch(fieldName = "encry_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date encryDate;
	/**编码源*/
	private String encrySource;
	/**subscriber*/
	private String subscriber;
	/**验证码*/
	private String validateCode;
	/**验证标志10-已验证11-验证失败12-未验证默认为12*/
	private Integer isValidate;
	/**验证人*/
	private String validBy;
	/**验证时间*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date validDate;
	/**打印次数*/
	private Integer prints;
	/**状态1，新输入-1，删除2，冲帐*/
	@BtCompositeSearch(fieldName = "status")
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**billNo*/
	@BtCompositeSearch(fieldName = "bill_No")
	private String billNo;
	/**单位ID*/
	@Corp(inject="corpName")
	@BtCompositeSearch(fieldName = "corp_Id")
	@Dict(tableName="sys_corp",valueFieldName="id",showFiledName="net_Id", inject = "netId")
	private String corpId;
	private String corpName;
	@Corp(inject="netName")
	private String netId;
	private String netName;
	/**电子回单类型07-贷款通知单*/
	@BtCompositeSearch(fieldName = "bill_type")
	@Dict(dictGroup = "eVisaType.billType", inject = "billTypeName")
	private String billType;
	private String billTypeName;
	/**
	 * 贷款和还款类型，1贷款2还款
	 */
	@BtCompositeSearch(fieldName = "loan_Type")
	@Dict(dictGroup = "eLoanBill.loanType", inject = "loanTypeName")
	private String loanType;
	private String loanTypeName;
	/**贷款单位*/
	@BtCompositeSearch(fieldName = "loan_Corp")
	private String loanCorp;
	/**利率*/
	@BtCompositeSearch(fieldName = "loan_Rate")
	private BigDecimal loanRate;
	/**贷款日期*/
	@BtCompositeSearch(fieldName = "loan_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date loanDate;
	/**币别*/
	@BtCompositeSearch(fieldName = "cur_Id")
	@Currency(inject="curName")
	private String curId;
	private String curName;
	/**存款账号*/
	@BtCompositeSearch(fieldName = "deposit_Acc")
	private String depositAcc;
	/**合同号*/
	@BtCompositeSearch(fieldName = "contract_Code")
	private String contractCode;
	/**还款日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date returnDate;
	/**贷款金额*/
	@BtCompositeSearch(fieldName = "loan_Money")
	private BigDecimal loanMoney;
	/**借方*/
	private String debitAcc;
	/**贷方*/
	private String creditAcc;
	/**记账员*/
	private String tallyerCode;
	/**复核员*/
	private String checkerCode;
	/**tallyDate*/
	@BtCompositeSearch(fieldName = "tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**起息日期*/
	private Date accrualDate;
	/**还款金额*/
	private BigDecimal returnMoney;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getLoanType() {
		return loanType;
	}
	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	public String getLoanTypeName() {
		return loanTypeName;
	}
	public void setLoanTypeName(String loanTypeName) {
		this.loanTypeName = loanTypeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getOldVoucher() {
		return oldVoucher;
	}
	public void setOldVoucher(String oldVoucher) {
		this.oldVoucher = oldVoucher;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public Date getEncryDate() {
		return encryDate;
	}
	public void setEncryDate(Date encryDate) {
		this.encryDate = encryDate;
	}
	public String getEncrySource() {
		return encrySource;
	}
	public void setEncrySource(String encrySource) {
		this.encrySource = encrySource;
	}
	public String getSubscriber() {
		return subscriber;
	}
	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
	}
	public String getValidateCode() {
		return validateCode;
	}
	public void setValidateCode(String validateCode) {
		this.validateCode = validateCode;
	}
	public Integer getIsValidate() {
		return isValidate;
	}
	public void setIsValidate(Integer isValidate) {
		this.isValidate = isValidate;
	}
	public String getValidBy() {
		return validBy;
	}
	public void setValidBy(String validBy) {
		this.validBy = validBy;
	}
	public Date getValidDate() {
		return validDate;
	}
	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}
	public Integer getPrints() {
		return prints;
	}
	public void setPrints(Integer prints) {
		this.prints = prints;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getLoanCorp() {
		return loanCorp;
	}
	public void setLoanCorp(String loanCorp) {
		this.loanCorp = loanCorp;
	}
	public BigDecimal getLoanRate() {
		return loanRate;
	}
	public void setLoanRate(BigDecimal loanRate) {
		this.loanRate = loanRate;
	}
	public Date getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getDepositAcc() {
		return depositAcc;
	}
	public void setDepositAcc(String depositAcc) {
		this.depositAcc = depositAcc;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public BigDecimal getLoanMoney() {
		return loanMoney;
	}
	public void setLoanMoney(BigDecimal loanMoney) {
		this.loanMoney = loanMoney;
	}
	public String getDebitAcc() {
		return debitAcc;
	}
	public void setDebitAcc(String debitAcc) {
		this.debitAcc = debitAcc;
	}
	public String getCreditAcc() {
		return creditAcc;
	}
	public void setCreditAcc(String creditAcc) {
		this.creditAcc = creditAcc;
	}
	public String getTallyerCode() {
		return tallyerCode;
	}
	public void setTallyerCode(String tallyerCode) {
		this.tallyerCode = tallyerCode;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public Date getAccrualDate() {
		return accrualDate;
	}
	public void setAccrualDate(Date accrualDate) {
		this.accrualDate = accrualDate;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public BigDecimal getReturnMoney() {
		return returnMoney;
	}
	public void setReturnMoney(BigDecimal returnMoney) {
		this.returnMoney = returnMoney;
	}
}
