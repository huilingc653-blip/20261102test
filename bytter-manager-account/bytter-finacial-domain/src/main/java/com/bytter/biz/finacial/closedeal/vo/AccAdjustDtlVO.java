package com.bytter.biz.finacial.closedeal.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 需要调整积数的账户明细Bean
 * @author liyh
 *
 */
public class AccAdjustDtlVO implements Serializable {
	
	/**
	 * 账号
	 */
	private String account;
	
	/**
	 * 币别
	 */
	private String curId;
	
	/**
	 * 调整日期
	 */
	private Date adjustDate;
	/**
	 * 余额方向
	 */
	private Integer balanceWay;
	
	/**
	 * 调整金额
	 */
	private BigDecimal adjustCumu;
	public AccAdjustDtlVO(String account, String curId, Date adjustDate, Integer balanceWay) {
		this.account = account;
		this.curId = curId;
		this.adjustDate = adjustDate;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public Date getAdjustDate() {
		return adjustDate;
	}
	public void setAdjustDate(Date adjustDate) {
		this.adjustDate = adjustDate;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public BigDecimal getAdjustCumu() {
		return adjustCumu;
	}
	public void setAdjustCumu(BigDecimal adjustCumu) {
		this.adjustCumu = adjustCumu;
	}
}
