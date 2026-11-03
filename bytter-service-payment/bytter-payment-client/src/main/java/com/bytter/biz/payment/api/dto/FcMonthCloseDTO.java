package com.bytter.biz.payment.api.dto;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import java.io.Serializable;
import java.util.Date;
/**
 * 月结页面传输视图对象
 * @author liyh
 *
 */
public class FcMonthCloseDTO implements Serializable {
	
	/**
	 * 网点ID
	 */
	private String netCode;
	
	/**
	 * 结转年份
	 */
	private Integer closeYear;
	/**
	 * 结转月份
	 */
	private Integer closeMonth;
	/**
	 * 结转日期
	 */
	private Date closeDate;
	
	private String closeDatStr;
	
	/**
	 * 月结结束日期
	 */
	private Date endDate;
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public Integer getCloseYear() {
		return closeYear;
	}
	public void setCloseYear(Integer closeYear) {
		this.closeYear = closeYear;
	}
	public Integer getCloseMonth() {
		return closeMonth;
	}
	public void setCloseMonth(Integer closeMonth) {
		this.closeMonth = closeMonth;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public String getCloseDatStr() {
		if (null != closeDate) {
			return DateUtils.formatDate(closeDate);
		}
		return closeDatStr;
	}
	public void setCloseDatStr(String closeDatStr) {
		this.closeDatStr = closeDatStr;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * 月结年月
	 */
	private String yearMonth2;
	
	public String getYearMonth2() {
		if (!StringUtils.isEmpty(yearMonth2)) {
			return yearMonth2;
		}
		String mm = "";
		if (String.valueOf(closeMonth).length() < 2) {
			mm = "0" + String.valueOf(closeMonth);
		} else {
			mm = String.valueOf(closeMonth);
		}
		yearMonth2 = closeYear + mm;
		return yearMonth2;
	}
	public void setYearMonth2(String yearMonth2) {
		this.yearMonth2 = yearMonth2;
	}
}
