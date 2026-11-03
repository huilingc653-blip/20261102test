package com.bytter.biz.receipt.vo;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: 通知单电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:30:08
 * @version 1.0
 *
 */
public class ENoticeBillVO  {
	private String id;
	/**电子回单号N+年年月月日日+6位序列号(从000001开始)*/
	@BtCompositeSearch(fieldName = "BILL_NO")
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
	/**电子回单类型08-付款通知单09-收款通知单10-内转通知单*/
	@Dict(dictGroup = "eVisaType.billType", inject = "billTypeName")
	@BtCompositeSearch(fieldName = "bill_type")
	private String billType;
	private String billTypeName;
	/**付款单位名称*/
	@BtCompositeSearch(fieldName = "payer_Name")
	private String payerName;
	/**付款单位账号*/
	private String payerAcc;
	/**付款单位开户行*/
	private String payerBank;
	/**收款单位名称*/
	@BtCompositeSearch(fieldName = "payee_Name")
	private String payeeName;
	/**收款单位账号*/
	private String payeeAcc;
	/**收款单位开户行*/
	private String payeeBank;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	private String curName;
	/**金额*/
	@BtCompositeSearch(fieldName = "pay_Money")
	private BigDecimal payMoney;
	/**用途*/
	private String purpose;
	/**系统凭证代码*/
	private String sysVoucherCode;
	/**结算方式*/
	@Dict(tableName="fc_voucher_type",valueFieldName="id",showFiledName="voucher_name", inject = "payTypeName")
	private String payType;
	private String payTypeName;
	/**起息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date accrualDate;
	/**记账日期*/
	@BtCompositeSearch(fieldName = "tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**记账员*/
	@Dict(tableName="sys_user",valueFieldName="id",showFiledName="username", inject = "tallierName")
	private String tallierCode;
	private String tallierName;
	/**复核员*/
	@Dict(tableName="sys_user",valueFieldName="id",showFiledName="username", inject = "checkerName")
	private String checkerCode;
	private String checkerName;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "voucher_No")
	private String voucherNo;
	/**原始凭证号*/
	private String oldVoucher;
	/**原始凭证名称*/
	private String oldVoucherName;
	/**原凭证金额*/
	private BigDecimal oldVoucherMoney;
	/**借方科目*/
	private String debit;
	/**贷方科目*/
	private String credit;
	/**附件张数*/
	private Integer accessory;
	/**产生编码日期*/
	@BtCompositeSearch(fieldName = "encry_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date encryDate;
	/**加密随机数*/
	private String random;
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
	/**
	 * 凭证产生方式
	 */
	private String generateMode;
	/**
	 * 操作日结的用户
	 */
	private String dayOverOperatorUsername;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getTallierName() {
		return tallierName;
	}
	public void setTallierName(String tallierName) {
		this.tallierName = tallierName;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
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
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Date getAccrualDate() {
		return accrualDate;
	}
	public void setAccrualDate(Date accrualDate) {
		this.accrualDate = accrualDate;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getTallierCode() {
		return tallierCode;
	}
	public void setTallierCode(String tallierCode) {
		this.tallierCode = tallierCode;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
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
	public String getOldVoucherName() {
		return oldVoucherName;
	}
	public void setOldVoucherName(String oldVoucherName) {
		this.oldVoucherName = oldVoucherName;
	}
	public BigDecimal getOldVoucherMoney() {
		return oldVoucherMoney;
	}
	public void setOldVoucherMoney(BigDecimal oldVoucherMoney) {
		this.oldVoucherMoney = oldVoucherMoney;
	}
	public String getDebit() {
		return debit;
	}
	public void setDebit(String debit) {
		this.debit = debit;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public Integer getAccessory() {
		return accessory;
	}
	public void setAccessory(Integer accessory) {
		this.accessory = accessory;
	}
	public Date getEncryDate() {
		return encryDate;
	}
	public void setEncryDate(Date encryDate) {
		this.encryDate = encryDate;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
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
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getDayOverOperatorUsername() {
		return dayOverOperatorUsername;
	}
	public void setDayOverOperatorUsername(String dayOverOperatorUsername) {
		this.dayOverOperatorUsername = dayOverOperatorUsername;
	}
	public String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(String generateMode) {
		this.generateMode = generateMode;
	}
}
