package com.bytter.biz.finacial.closedeal.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.utils.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 凭证发生额余额VO
 * 
 * @author liyh
 *
 */
public class OutAccbookBalanceVO implements Serializable {
	/**
	 * 账务明细ID
	 */
	private String dtlId;
	/**
	 * 流水号
	 */
	private String orderNo;
	/**
	 * 凭证号
	 */
	private String voucherNo;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 科目
	 */
	private String subjectId;
	/**
	 * 币别
	 */
	private String curId;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 余额方向
	 */
	private Integer balanceWay;
	/**
	 * 记账日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	/**
	 * 借方金额
	 */
	private BigDecimal debitMoney;
	/**
	 * 贷方金额
	 */
	private BigDecimal creditMoney;
	/**
	 * 借方金额
	 */
	private Integer debitNum;
	/**
	 * 贷方金额
	 */
	private Integer creditNum;
	/**
	 * 账户余额
	 */
	private BigDecimal balance;
	public String getDtlId() {
		return dtlId;
	}
	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public Integer getBalanceWay() {
		return balanceWay;
	}
	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
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
	public Integer getDebitNum() {
		if (EmptyUtils.isEmpty(debitNum)) {
			debitNum = Integer.valueOf(0);
		}
		return debitNum;
	}
	public void setDebitNum(Integer debitNum) {
		this.debitNum = debitNum;
	}
	public Integer getCreditNum() {
		if (EmptyUtils.isEmpty(creditNum)) {
			creditNum = Integer.valueOf(0);
		}
		return creditNum;
	}
	public void setCreditNum(Integer creditNum) {
		this.creditNum = creditNum;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
}
