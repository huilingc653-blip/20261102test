package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
/**   
 * @Title: Entity
 * @Description: 资金计划主表VO
 * @author onlineGenerator
 * @date 2017-07-17 15:20:33
 * @version 1.0
 *
 */
public class FbsBudgetSuperAddApproveVO extends FbsBudgetVO implements Serializable,IBusinessKey {
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "fbs.enrol_date")
	private Date enrolDate;
	@BtCompositeSearch(fieldName = "fbs.time_type")
	private String timeType;
	@Override
	public String getBusinessKey() {
		return super.getSuperaddId();
	}
	@Override
	public Date getEnrolDate() {
		return enrolDate;
	}
	@Override
	public void setEnrolDate(Date enrolDate) {
		this.enrolDate = enrolDate;
	}
	@Override
	public String getTimeType() {
		return timeType;
	}
	@Override
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
}
