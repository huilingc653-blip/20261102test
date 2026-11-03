package com.bytter.biz.budget.vo;
/**
 * @author qijh
 * @date 2017-08-03
 */
public class FbsCorpBudgetVO implements java.io.Serializable {
	private String id;
	/**
	 * 单位ID
	 */
	private String corpId;
	private String corpName;
	private String corpCode;
	private String corpParentId;
	private Integer corpType;
	private String corpNetId;
	/**
	 * 单位是否汇总 0-否 1-是
	 */
	private Integer collectFlag;
	/**
	 * 单位是否受控 0-否 1-是
	 */
	private Integer controlBudget;
	/**
	 * 单位是否上报  0-否 1-是
	 */
	private Integer reportBudget;
	/**
	 * 下级单位数量
	 */
	private Integer childCount;
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
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpParentId() {
		return corpParentId;
	}
	public void setCorpParentId(String corpParentId) {
		this.corpParentId = corpParentId;
	}
	public Integer getCorpType() {
		return corpType;
	}
	public void setCorpType(Integer corpType) {
		this.corpType = corpType;
	}
	public String getCorpNetId() {
		return corpNetId;
	}
	public void setCorpNetId(String corpNetId) {
		this.corpNetId = corpNetId;
	}
	public Integer getCollectFlag() {
		return collectFlag;
	}
	public void setCollectFlag(Integer collectFlag) {
		this.collectFlag = collectFlag;
	}
	public Integer getControlBudget() {
		return controlBudget;
	}
	public void setControlBudget(Integer controlBudget) {
		this.controlBudget = controlBudget;
	}
	public Integer getReportBudget() {
		return reportBudget;
	}
	public void setReportBudget(Integer reportBudget) {
		this.reportBudget = reportBudget;
	}
	public Integer getChildCount() {
		return childCount;
	}
	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}
}
