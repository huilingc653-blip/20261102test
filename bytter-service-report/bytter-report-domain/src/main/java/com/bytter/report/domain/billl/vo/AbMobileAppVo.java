package com.bytter.report.domain.billl.vo;
import java.math.BigDecimal;
/**
 * 到期票据分析
 * @author caojy
 *
 */
public class AbMobileAppVo implements java.io.Serializable{
	
	/** 备用ID**/
	private String id;
	/** 单位ID**/
	private String corpId;
	
	/** 单位代码**/
	private String corpCode;
	
	/** 单位名称**/
	private String corpName;
	
	/** 票据类型值**/
	private String billType;
	
	/** 票据类型名称**/
	private String billTypeName;
	
	/** 商票金额**/
	private BigDecimal businessMoney;
	
	/** 商票金额格式化**/
	private String businessMoneyStr;
	
	
	/** 银票金额**/
	private BigDecimal silverMoney;
	/** 银票金额格式化**/
	private String silverMoneyStr;
	
	/** 期限值 **/
	private String timeLimit;
	
	/** 剩余期限显示**/
	private String timeLimitName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
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
	public BigDecimal getBusinessMoney() {
		return businessMoney;
	}
	public void setBusinessMoney(BigDecimal businessMoney) {
		this.businessMoney = businessMoney;
	}
	public BigDecimal getSilverMoney() {
		return silverMoney;
	}
	public void setSilverMoney(BigDecimal silverMoney) {
		this.silverMoney = silverMoney;
	}
	public String getTimeLimit() {
		return timeLimit;
	}
	public void setTimeLimit(String timeLimit) {
		this.timeLimit = timeLimit;
	}
	public String getTimeLimitName() {
		return timeLimitName;
	}
	public void setTimeLimitName(String timeLimitName) {
		this.timeLimitName = timeLimitName;
	}
	
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getBusinessMoneyStr() {
		return businessMoneyStr;
	}
	public void setBusinessMoneyStr(String businessMoneyStr) {
		this.businessMoneyStr = businessMoneyStr;
	}
	public String getSilverMoneyStr() {
		return silverMoneyStr;
	}
	public void setSilverMoneyStr(String silverMoneyStr) {
		this.silverMoneyStr = silverMoneyStr;
	}
}
