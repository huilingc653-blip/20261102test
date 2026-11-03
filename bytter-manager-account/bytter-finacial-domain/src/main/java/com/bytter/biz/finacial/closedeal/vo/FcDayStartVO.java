package com.bytter.biz.finacial.closedeal.vo;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * 日初页面传输视图对象
 */
public class FcDayStartVO implements Serializable {
	
	/**
	 * 网点ID
	 */
	private String netCode;
	
	/**
	 * 下次日初日期(工作日日期)
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	
	private String startDateStr;
	
	/**
	 * 已完成日初日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date lastStartDate;
	
	private String lastStartDateStr;
	/**
	 * 日结天数
	 */
	private Integer startDay;
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getLastStartDateStr() {
		if(EmptyUtils.isNotEmpty(lastStartDateStr)) {
			return lastStartDateStr;
		}
		if (null != lastStartDate) {
			return DateUtils.formatDate(lastStartDate);
		}
		return lastStartDateStr;
	}
	public void setLastStartDateStr(String lastStartDateStr) {
		this.lastStartDateStr = lastStartDateStr;
	}
	public String getStartDateStr() {
		if(EmptyUtils.isNotEmpty(startDateStr)) {
			return startDateStr;
		}
		if (null != startDate) {
			return DateUtils.formatDate(startDate);
		}
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public Date getLastStartDate() {
		return lastStartDate;
	}
	public void setLastStartDate(Date lastStartDate) {
		this.lastStartDate = lastStartDate;
	}
	public Integer getStartDay() {
		return startDay;
	}
	public void setStartDay(Integer startDay) {
		this.startDay = startDay;
	}
}
