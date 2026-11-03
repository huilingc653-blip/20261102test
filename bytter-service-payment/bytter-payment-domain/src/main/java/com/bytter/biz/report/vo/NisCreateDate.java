package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.util.Date;
/**
 * @author zhuop
 * @Description 资金归集、下拨情况汇总
 * @date 2018.6.8
 */
public class NisCreateDate {
	/**
	 * 开始日期
	 */
	private Date gedCreateDate;
	/**
	 * 结束日期
	 */
	private Date ledCreateDate;
	/**
	 * 创造日期,也即申请日期
	 */
	@BtCompositeSearch(fieldName = "nis.create_Date")
	private Date createDate;
	public Date getGedCreateDate() {
		return gedCreateDate;
	}
	public void setGedCreateDate(Date gedCreateDate) {
		this.gedCreateDate = gedCreateDate;
	}
	public Date getLedCreateDate() {
		return ledCreateDate;
	}
	public void setLedCreateDate(Date ledCreateDate) {
		this.ledCreateDate = ledCreateDate;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
