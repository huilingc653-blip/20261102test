package com.bytter.biz.receipt.vo;
import com.bytter.core.annotation.*;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: 账户转换电子回单
 * @author onlineGenerator
 * @date 2017-07-04 15:30:19
 * @version 1.0
 *
 */
public class ETransBillVO  {
	@BtCompositeSearch(fieldName = "a.id")
	private String id;
	/**电子回单号*/
	private String billNo;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "a.corp_id")
	@Corp(inject="corpName")
	@Dict(tableName="sys_corp",valueFieldName="id",showFiledName="net_Id", inject = "netId")
	private String corpId;
	/**单位名称*/
	private String corpName;
	@Corp(inject="netName")
	private String netId;
	private String netName;
	/**电子回单类型*/
	@BtCompositeSearch(fieldName = "a.bill_type")
	private String billType;
	/**账号*/
	private String accNo;
	/**账户名称*/
	private String accName;
	/**起存日期*/
	private java.util.Date startDate;
	/**到期日期*/
	private java.util.Date endDate;
	/**币别*/
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	private String curName;
	/**利率*/
	private java.math.BigDecimal rate;
	/**期限*/
	private Integer dayLimit;
	/**通知类型*/
	private Integer notifyType;
	/**活期账号*/
	private String currentAcc;
	/**活期账户名称*/
	private String currentAccName;
	/**存款日期*/
	private java.util.Date depositDate;
	/**凭证号*/
	@BtCompositeSearch(fieldName = "a.voucher_No")
	private String voucherNo;
	/**存款利率*/
	private java.math.BigDecimal depositRate;
	/**money*/
	@BtCompositeSearch(fieldName = "a.money")
	private java.math.BigDecimal money;
	/**记账员*/
	@User(inject = "tallierName")
	private String tallierCode;
	private String tallierName;
	/**复核员*/
	@User(inject = "checkerName")
	private String checkerCode;
	private String checkerName;
	/**记账日期*/
	@BtCompositeSearch(fieldName = "a.tally_date")
	private java.util.Date tallyDate;
	/**产生编码日期*/
	@BtCompositeSearch(fieldName = "a.encry_Date")
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
	/**验证标志*/
	private Integer isValidate;
	/**验证人*/
	private String validBy;
	/**验证时间*/
	private java.util.Date validDate;
	/**打印次数*/
	private Integer prints;
	/**状态*/
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
	/**摘要*/
	private String summaryContent;
	/**存款类型*/
	@BtCompositeSearch(fieldName = "c.id")
	private String depositRateType;
	/**存款类型名称*/
	private String depositRateName;
	/**原始凭证号*/
	private String origVoucherNo;
	/**定期期限/通知类型*/
	private java.lang.Integer period;
	/**期限周期类型*/
	private java.lang.Integer periodCycle;
	/**
	 * 操作日结的用户
	 */
	private String dayOverOperatorUsername;
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
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public Integer getDayLimit() {
		return dayLimit;
	}
	public void setDayLimit(Integer dayLimit) {
		this.dayLimit = dayLimit;
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
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public BigDecimal getDepositRate() {
		return depositRate;
	}
	public void setDepositRate(BigDecimal depositRate) {
		this.depositRate = depositRate;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
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
	public String getDepositRateName() {
		return depositRateName;
	}
	public void setDepositRateName(String depositRateName) {
		this.depositRateName = depositRateName;
	}
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
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
	public Integer getPeriod() {
		return period;
	}
	public void setPeriod(Integer period) {
		this.period = period;
	}
	public Integer getPeriodCycle() {
		return periodCycle;
	}
	public void setPeriodCycle(Integer periodCycle) {
		this.periodCycle = periodCycle;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
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
