package com.bytter.biz.finacial.api.dto;
import java.io.Serializable;
import java.util.Date;
/**
 * 账务信息DTO
 * 
 * @author liyh
 *
 */
public class FcAccbookCloseDTO implements Serializable {
	/**
	 * 凭证ID
	 */
	private String bookId;
	/**
	 * 全局ID
	 */
	private String busGlobalId;
	/**
	 * 是否已日结 true :已日结 false ： 未日结
	 */
	private boolean closeFlag = false;
	/**
	 * 日结日期
	 */
	private Date closeDate;
	/**
	 * 记账凭证的记账日期
	 */
	private Date tallyDate;
	/**
	 * 是否已作废或已冲账 0 : 已冲账 已作废， 1：凭证正在作废流程中 2：凭证正在冲账流程中 3：可冲账或作废处理
	 */
	private String dealFlag;
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	public boolean isCloseFlag() {
		return closeFlag;
	}
	public void setCloseFlag(boolean closeFlag) {
		this.closeFlag = closeFlag;
	}
	public Date getCloseDate() {
		return closeDate;
	}
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getDealFlag() {
		return dealFlag;
	}
	public void setDealFlag(String dealFlag) {
		this.dealFlag = dealFlag;
	}
}
