package com.bytter.biz.budget.spi.dto;
public class FbsCorpBudgetDTO implements java.io.Serializable {
	/**
	 * 单位ID
	 */
	private String corpId;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 单位编码
	 */
	private String corpCode;
	/**
	 * 父账户ID
	 */
	private String corpParentId;
	/**
	 * 单位类型
	 */
	private Integer corpType;
	/**
	 * 网点ID
	 */
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
	/**
	 * 是否是虚拟单位
	 */
	private String fictitiousCorp;
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
	public String getFictitiousCorp() {
		return fictitiousCorp;
	}
	public void setFictitiousCorp(String fictitiousCorp) {
		this.fictitiousCorp = fictitiousCorp;
	}
}
