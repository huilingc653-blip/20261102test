package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 日结页面传输视图对象
 * @author liyh
 *
 */
public class FcDayCloseVO implements Serializable {
	
	/**
	 * 网点ID
	 */
	private String netCode;
	
	/**
	 * 下次日结日期(工作日日期)
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date closeDate;
	
	private String closeDateStr;
	
	/**
	 * 已完成日结日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date lastCloseDate;
	
	private String lastCloseDateStr;
	/**
	 * 日结天数
	 */
	private Integer closeDay;
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public Date getLastCloseDate() {
		return lastCloseDate;
	}
	public void setLastCloseDate(Date lastCloseDate) {
		this.lastCloseDate = lastCloseDate;
	}
	public Integer getCloseDay() {
		return closeDay;
	}
	public void setCloseDay(Integer closeDay) {
		this.closeDay = closeDay;
	}
	public String getCloseDateStr() {
        if(EmptyUtils.isNotEmpty(closeDateStr)) {
            return closeDateStr;
        }
		if (null != closeDate) {
			return DateUtils.formatDate(closeDate);
		}
		return closeDateStr;
	}
	public void setCloseDateStr(String closeDateStr) {
		this.closeDateStr = closeDateStr;
	}
	public String getLastCloseDateStr() {
		if(EmptyUtils.isNotEmpty(lastCloseDateStr)) {
		    return lastCloseDateStr;
        }
	    if (null != lastCloseDate) {
			return DateUtils.formatDate(lastCloseDate);
		}
		return lastCloseDateStr;
	}
	public void setLastCloseDateStr(String lastCloseDateStr) {
		this.lastCloseDateStr = lastCloseDateStr;
	}
	
}
