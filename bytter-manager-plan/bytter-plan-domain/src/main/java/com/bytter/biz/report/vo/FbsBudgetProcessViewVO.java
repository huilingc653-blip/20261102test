package com.bytter.biz.report.vo;
import com.bytter.biz.budget.vo.FbsItemVO;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 计划执行报表VO
 * @date 2018-03-13 15:06:43
 */
public class FbsBudgetProcessViewVO implements Serializable {
	/**
	 * 周期
	 */
	private String period;
	/**
	 * 单位ID
	 */
	@BtCompositeSearch(fieldName = "t.corp_id")
	//@Corp(inject = "corpName")
	private String corpId;
	private String corpName;
	/**
	 * 开始时间
	 */
	private String beginDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	/**
	 * 预算周期类型
	 */
	private String timeType;
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
}
