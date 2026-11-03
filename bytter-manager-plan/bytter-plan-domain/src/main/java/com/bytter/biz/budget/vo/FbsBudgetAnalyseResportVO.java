package com.bytter.biz.budget.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title: Entity
 * @Description: 资金计划主表VO
 * @author onlineGenerator
 * @date 2017-07-17 15:20:33
 * @version 1.0
 *
 */
public class FbsBudgetAnalyseResportVO implements Serializable {
	private String id;
	/**单位ID*/
	/*@Corps({
		@Corp(inject = "corpName"),
		@Corp(inject = "corpCode",field = "code")
	})*/
	private String corpId;
	private String corpCode;
	private String corpName;
	/**编制时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date enrolDate;
	/**计划开始时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date beginDate;
	/**计划结束时间*/
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date endDate;
	/**计划周期类型 1-年 2-季 3-月 4-周 5-天*/
	private String timeType;
	/**计划金额*/
	private BigDecimal planMoney;
	/**实际金额*/
	private BigDecimal factMoney;
	/**差异金额*/
	private BigDecimal diffMoney;
	/**差异率*/
	private BigDecimal diffRatio;
	/**执行率*/
	private BigDecimal processRatio;
	/**币别*/
	private String curId;
	/**币别*/
	private String curName;
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
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public Date getEnrolDate() {
		return enrolDate;
	}
	public void setEnrolDate(Date enrolDate) {
		this.enrolDate = enrolDate;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public BigDecimal getPlanMoney() {
		return planMoney;
	}
	public void setPlanMoney(BigDecimal planMoney) {
		this.planMoney = planMoney;
	}
	public BigDecimal getFactMoney() {
		return factMoney;
	}
	public void setFactMoney(BigDecimal factMoney) {
		this.factMoney = factMoney;
	}
	public BigDecimal getDiffMoney() {
		return diffMoney;
	}
	public void setDiffMoney(BigDecimal diffMoney) {
		this.diffMoney = diffMoney;
	}
	public BigDecimal getDiffRatio() {
		return diffRatio;
	}
	public void setDiffRatio(BigDecimal diffRatio) {
		this.diffRatio = diffRatio;
	}
	public BigDecimal getProcessRatio() {
		return processRatio;
	}
	public void setProcessRatio(BigDecimal processRatio) {
		this.processRatio = processRatio;
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
}
