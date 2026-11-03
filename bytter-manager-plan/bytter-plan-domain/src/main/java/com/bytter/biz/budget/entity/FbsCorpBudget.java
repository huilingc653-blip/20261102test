package com.bytter.biz.budget.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FBS_CORP_ITEM
 * @date 2017-07-11 10:46:42
 */
@Entity
@Table(name = "FBS_CORP_BUDGET", schema = "")
public class FbsCorpBudget extends IdEntity implements java.io.Serializable {
	/**
	 * 单位ID
	 */
	private String corpId;
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
	@Column(name = "CORP_ID")
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	@Column(name = "COLLECT_FLAG")
	public Integer getCollectFlag() {
		return collectFlag;
	}
	public void setCollectFlag(Integer collectFlag) {
		this.collectFlag = collectFlag;
	}
	@Column(name = "CONTROL_BUDGET")
	public Integer getControlBudget() {
		return controlBudget;
	}
	public void setControlBudget(Integer controlBudget) {
		this.controlBudget = controlBudget;
	}
	@Column(name = "REPORT_BUDGET")
	public Integer getReportBudget() {
		return reportBudget;
	}
	public void setReportBudget(Integer reportBudget) {
		this.reportBudget = reportBudget;
	}
}
