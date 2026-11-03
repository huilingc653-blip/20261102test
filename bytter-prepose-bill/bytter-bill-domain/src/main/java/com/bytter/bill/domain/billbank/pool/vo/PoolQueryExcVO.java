package com.bytter.bill.domain.billbank.pool.vo;
import java.io.Serializable;
import java.util.Date;
/**
 * 票据池查询业务指令VO
 * 
 */
public class PoolQueryExcVO extends PoolBaseVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 查询开始日期
	 */
	private Date beginDate;
	
	/**
	 * 查询结束日期
	 */
	private Date endDate;
	
	/**
	 * 查询类型（票据池额度01\出入池登记簿02\在池登记簿03\保证金交易明细04\额度分配历史记录交易明细05）
	 */
	private String queryType;
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
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	@Override
	public String toString() {
		return "PoolQueryExcVO [beginDate=" + beginDate + ", endDate="
				+ endDate + ", queryType=" + queryType+ ", super.toString()="
						+ super.toString() + "]";
	}
	
}
