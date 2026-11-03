package com.bytter.biz.receipt.vo;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: 利息清单电子回单
 * @author onlineGenerator
 * @date 2017-07-04 15:27:13
 * @version 1.0
 *
 */
public class EAccrualBillVO  {
	@BtCompositeSearch(fieldName = "a.id")
	private String id;
	/**打印次数*/
	private Integer prints;
	/**状态1，新输入-1，删除2，冲帐*/
	@BtCompositeSearch(fieldName = "a.status")
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**电子回单号A+年年月月日日+6位序列号(从000001开始)*/
	@BtCompositeSearch(fieldName = "bill_No")
	private String billNo;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "a.corp_Id")
	@Corp(inject="corpName")
	@Dict(tableName="sys_corp",valueFieldName="id",showFiledName="net_Id", inject = "netId")
	private String corpId;
	private String corpName;
	@Corp(inject="netName")
	private String netId;
	private String netName;
	/**电子回单类型01-存款利息单02-贷款利息单03-委托贷款利息清单04-定期利息单05-通知利息单19-活期保证金利息单20-定期保证金利息单*/
	@BtCompositeSearch(fieldName = "a.bill_type")
	@Dict(dictGroup = "eVisaType.billType", inject = "billTypeName")
	private String billType;
	private String billTypeName;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	private String curName;
	/**利息产生日期（记账日期）*/
	@BtCompositeSearch(fieldName = "a.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**账号*/
	@BtCompositeSearch(fieldName = "acc_No")
	private String accNo;
	/**户名*/
	@BtCompositeSearch(fieldName = "acc_Name")
	private String accName;
	/**开始日期*/
	@BtCompositeSearch(fieldName = "start_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	/**结束日期*/
	@BtCompositeSearch(fieldName = "end_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date endDate;
	/**利息*/
	@BtCompositeSearch(fieldName = "a.accrual")
	private BigDecimal accrual;
	/**积数*/
	private BigDecimal cumu;
	/**利率*/
	private BigDecimal interest;
	/**天数*/
	private Integer days;
	/**对方科目*/
	@BtCompositeSearch(fieldName = "other_Subject")
	private String otherSubject;
	/**本方科目*/
	@BtCompositeSearch(fieldName = "home_Subject")
	private String homeSubject;
	/**本金*/
	private BigDecimal money;
	/**本息合计*/
	private BigDecimal nummoney;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "a.voucher_No")
	private String voucherNo;
	/**备注*/
	private String rmk;
	/**产生编码日期*/
	@BtCompositeSearch(fieldName = "a.encry_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date encryDate;
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
	private java.util.Date validDate;
	/**利息清单明细ID*/
	private String interestDtlId;
	/**复核员*/
	@User(inject = "checkerName")
	private String checkerCode;
	private String checkerName;
	/**记账员*/
	@User(inject = "tallierName")
	private String tallierCode;
	private String tallierName;
	/**合同金额*/
	private java.math.BigDecimal totolMoney;
	/**已还款金额*/
	private java.math.BigDecimal repayMoney;
	/**摘要*/
	private java.lang.String summaryContent;
	/**存款类型*/
	@BtCompositeSearch(fieldName = "e.id")
	private java.lang.String depositRateType;
	/**存款类型名称*/
	private java.lang.String depositName;
	/**原始凭证号*/
	private java.lang.String origVoucherNo;
	/**期限*/
	private java.lang.Integer limit;
	/**
	 * 操作日结的用户
	 */
	private String dayOverOperatorUsername;
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(Integer notifyType) {
		this.notifyType = notifyType;
	}
	public String getCurrentAcc() {
		return currentAcc;
	}
	public void setCurrentAcc(String currentAcc) {
		this.currentAcc = currentAcc;
	}
	public String getCurrentAccName() {
		return currentAccName;
	}
	public void setCurrentAccName(String currentAccName) {
		this.currentAccName = currentAccName;
	}
	public Date getDepositDate() {
		return depositDate;
	}
	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}
	public BigDecimal getDepositRate() {
		return depositRate;
	}
	public void setDepositRate(BigDecimal depositRate) {
		this.depositRate = depositRate;
	}
	/**通知类型*/
	private java.lang.Integer notifyType;
	/**活期账号*/
	private java.lang.String currentAcc;
	/**活期账户名称*/
	private java.lang.String currentAccName;
	/**存款日期*/
	private java.util.Date depositDate;
	/**存款利率*/
	private java.math.BigDecimal depositRate;
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public String getDepositRateType() {
		return depositRateType;
	}
	public void setDepositRateType(String depositRateType) {
		this.depositRateType = depositRateType;
	}
	public String getDepositName() {
		return depositName;
	}
	public void setDepositName(String depositName) {
		this.depositName = depositName;
	}
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public EAccrualBillVO() {
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
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
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public BigDecimal getAccrual() {
		return accrual;
	}
	public void setAccrual(BigDecimal accrual) {
		this.accrual = accrual;
	}
	public BigDecimal getCumu() {
		return cumu;
	}
	public void setCumu(BigDecimal cumu) {
		this.cumu = cumu;
	}
	public BigDecimal getInterest() {
		return interest;
	}
	public void setInterest(BigDecimal interest) {
		this.interest = interest;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public String getOtherSubject() {
		return otherSubject;
	}
	public void setOtherSubject(String otherSubject) {
		this.otherSubject = otherSubject;
	}
	public String getHomeSubject() {
		return homeSubject;
	}
	public void setHomeSubject(String homeSubject) {
		this.homeSubject = homeSubject;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getNummoney() {
		return nummoney;
	}
	public void setNummoney(BigDecimal nummoney) {
		this.nummoney = nummoney;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
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
	public String getInterestDtlId() {
		return interestDtlId;
	}
	public void setInterestDtlId(String interestDtlId) {
		this.interestDtlId = interestDtlId;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public String getTallierCode() {
		return tallierCode;
	}
	public void setTallierCode(String tallierCode) {
		this.tallierCode = tallierCode;
	}
	public BigDecimal getTotolMoney() {
		return totolMoney;
	}
	public void setTotolMoney(BigDecimal totolMoney) {
		this.totolMoney = totolMoney;
	}
	public BigDecimal getRepayMoney() {
		return repayMoney;
	}
	public void setRepayMoney(BigDecimal repayMoney) {
		this.repayMoney = repayMoney;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	public String getTallierName() {
		return tallierName;
	}
	public void setTallierName(String tallierName) {
		this.tallierName = tallierName;
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
}
