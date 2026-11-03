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
 * @Description: 对账单电子回单表
 * @author onlineGenerator
 * @date 2017-07-04 15:30:28
 * @version 1.0
 *
 */
public class ECompareBillVO   {
	private String id;
	/**billNo*/
	@BtCompositeSearch(fieldName = "a.bill_no")
	private String billNo;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "a.corp_Id")
	@Corp(inject="corpName")
	@Dict(tableName="sys_corp",valueFieldName="id",showFiledName="net_Id", inject = "netId")
	private String corpId;
	@BtCompositeSearch(fieldName = "a.corp_Id")
	private String corpName;
	@Corp(inject="netName")
	private String netId;
	private String netName;
	/**电子回单类型17-对账单电子回单*/
	@BtCompositeSearch(fieldName = "a.bill_type")
	@Dict(dictGroup = "eVisaType.billType", inject = "billTypeName")
	private String billType;
	@BtCompositeSearch(fieldName = "a.bill_type")
	private String billTypeName;
	/**内部账号*/
	@BtCompositeSearch(fieldName = "a.acc_no")
	private String accNo;
	/**账户名称*/
	@BtCompositeSearch(fieldName = "a.acc_name")
	private String accName;
	/**summaryComment*/
	private String summaryComment;
	/**记账员*/
	private String tallyerCode;
	/**tallyDate*/
	@BtCompositeSearch(fieldName = "a.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	@BtCompositeSearch(fieldName = "a.cur_id")
	private String curName;
	/**对账单票据号码*/
	private String billCode;
	/**interestStartDate*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	/**借方金额*/
	private BigDecimal debitMoney;
	/**贷方金额*/
	private BigDecimal creditMoney;
	/**余额*/
	@BtCompositeSearch(fieldName = "a.balance")
	private BigDecimal balance;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "a.voucher_No")
	private String voucherNo;
	/**红蓝标志（R -红，B 蓝）*/
	private String redblueSign;
	/**产生编码日期*/
	@BtCompositeSearch(fieldName = "encryDateStr")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date encryDate;
	@BtCompositeSearch(fieldName = "encryDateStr")
	private String encryDateStr;
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
	private Date validDate;
	/**打印次数*/
	private Integer prints;
	private Integer prints2;
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
	/**期初金额*/
	private BigDecimal beginBalanceWay;
	private BigDecimal sumDebitMoney;
	private BigDecimal sumCreditMoney;
	private BigDecimal sumBalance;
	/**期末金额*/
	private BigDecimal endBalance;
	/**行号*/
	private Integer lineNo;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public BigDecimal getSumDebitMoney() {
		return sumDebitMoney;
	}
	public void setSumDebitMoney(BigDecimal sumDebitMoney) {
		this.sumDebitMoney = sumDebitMoney;
	}
	public BigDecimal getSumCreditMoney() {
		return sumCreditMoney;
	}
	public void setSumCreditMoney(BigDecimal sumCreditMoney) {
		this.sumCreditMoney = sumCreditMoney;
	}
	public BigDecimal getSumBalance() {
		return sumBalance;
	}
	public void setSumBalance(BigDecimal sumBalance) {
		this.sumBalance = sumBalance;
	}
	public BigDecimal getBeginBalanceWay() {
		return beginBalanceWay;
	}
	public void setBeginBalanceWay(BigDecimal beginBalanceWay) {
		this.beginBalanceWay = beginBalanceWay;
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
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getSummaryComment() {
		return summaryComment;
	}
	public void setSummaryComment(String summaryComment) {
		this.summaryComment = summaryComment;
	}
	public String getTallyerCode() {
		return tallyerCode;
	}
	public void setTallyerCode(String tallyerCode) {
		this.tallyerCode = tallyerCode;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
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
	public String getBillCode() {
		return billCode;
	}
	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getRedblueSign() {
		return redblueSign;
	}
	public void setRedblueSign(String redblueSign) {
		this.redblueSign = redblueSign;
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
	public Integer getLineNo() {
		return lineNo;
	}
	public void setLineNo(Integer lineNo) {
		this.lineNo = lineNo;
	}
	public Integer getPrints2() {
		return prints2;
	}
	public void setPrints2(Integer prints2) {
		this.prints2 = prints2;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public BigDecimal getEndBalance() {
		return endBalance;
	}
	public void setEndBalance(BigDecimal endBalance) {
		this.endBalance = endBalance;
	}
	public String getEncryDateStr() {
		return encryDateStr;
	}
	public void setEncryDateStr(String encryDateStr) {
		this.encryDateStr = encryDateStr;
	}
}
