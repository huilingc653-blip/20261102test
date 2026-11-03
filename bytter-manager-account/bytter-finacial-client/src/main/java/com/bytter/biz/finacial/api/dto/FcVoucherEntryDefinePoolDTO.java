package com.bytter.biz.finacial.api.dto;
/**
 * Title: Entity
 * Description: FC_VOUCHER_ENTRY_DEFINE_POOL
 * @author onlineGenerator
 * @since 2019-5-14
 * @version 1.0
 *
 */
public class FcVoucherEntryDefinePoolDTO implements java.io.Serializable{
	private String id;
	/** 业务规则ID */
	private String businessRuleDefineId;
	/** 所属类型 */
	private Integer belongType;
	/** 借贷标识 */
	private Integer balanceWay;
	/** 科目 */
	private String subjectCode;
	/** 凭证种类 */
	private String voucherType;
	/** 账号 */
	private String account;
	/** 币别代码 */
	private String curId;
	/** 摘要 */
	private String summary;
	/** 分录类型 */
	private Integer entryType;
	/** 组号 */
	private Integer groupNum;
	/** 临时组号 */
	private Integer groupNums;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBusinessRuleDefineId() {
		return businessRuleDefineId;
	}
	public void setBusinessRuleDefineId(String businessRuleDefineId) {
		this.businessRuleDefineId = businessRuleDefineId;
	}
	public Integer getBelongType() {
		return belongType;
	}
	public void setBelongType(Integer belongType) {
		this.belongType = belongType;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getEntryType() {
		return entryType;
	}
	public void setEntryType(Integer entryType) {
		this.entryType = entryType;
	}
	public Integer getGroupNum() {
		return groupNum;
	}
	public void setGroupNum(Integer groupNum) {
		this.groupNum = groupNum;
	}
	public Integer getGroupNums() {
		return groupNums;
	}
	public void setGroupNums(Integer groupNums) {
		this.groupNums = groupNums;
	}
}
