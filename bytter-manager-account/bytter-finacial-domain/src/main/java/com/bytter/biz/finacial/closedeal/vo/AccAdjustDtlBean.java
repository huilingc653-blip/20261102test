package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 需要调整积数的账户明细Bean
 * @author liyh
 *
 */
public class AccAdjustDtlBean implements Serializable {
	
	/**
	 * 账号
	 */
	private String account;
	
	/**
	 * 币别
	 */
	private String curId;
	
	/**
	 * 记账日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**
	 * 起息日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date interestStartDate;
	
	/**
	 * 借方金额
	 */
	private BigDecimal debitMoney;
	
	/**
	 * 贷方金额
	 */
	private BigDecimal creditMoney;
	
	/**
	 * 余额方向
	 */
	private Integer balanceWay;
	
	/**
	 * 记账日期与起息日期之间的天数间隔
	 */
	private Integer days;
	
	/**
	 * 调整金额
	 */
	private BigDecimal adjustCumu;
	/**
	 * 发生额
	 */
	private BigDecimal money;
	public BigDecimal getMoney() {
		if(money!=null){
			return money;
		}else {
			return BigDecimal.valueOf(0);
		}

	}
	public void setMoney(BigDecimal money) {
		this.money = money;
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
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public BigDecimal getDebitMoney() {
		return debitMoney;
	}
	public void setDebitMoney(BigDecimal debitMoney) {
		this.debitMoney = debitMoney;
	}
	public BigDecimal getCreditMoney() {
		return creditMoney;
	}
	public void setCreditMoney(BigDecimal creditMoney) {
		this.creditMoney = creditMoney;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public Integer getDays() {
		return days;
	}
	public void setDays(Integer days) {
		this.days = days;
	}
	public BigDecimal getAdjustCumu() {
		if (null != this.debitMoney && null != this.creditMoney) {
			// (借-贷)*余额方向
			this.adjustCumu = this.debitMoney.subtract(this.creditMoney).multiply(new BigDecimal(this.balanceWay));
		}
		return adjustCumu;
	}
	public void setAdjustCumu(BigDecimal adjustCumu) {
		this.adjustCumu = adjustCumu;
	}
}
