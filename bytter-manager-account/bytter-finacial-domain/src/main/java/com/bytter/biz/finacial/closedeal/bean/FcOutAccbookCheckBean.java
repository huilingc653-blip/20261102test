package com.bytter.biz.finacial.closedeal.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 表外账务检查Bean
 * 
 * @author liyh
 *
 */
public class FcOutAccbookCheckBean implements Serializable {
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date tallyDate;
	
	/** 凭证流水号 */
	private String orderNo;
	/** 账号 */
	private String account;
	/** 摘要 */
	private String summaryContent;
	/** 收入 */
	private BigDecimal debitMoney;
	/** 支出 */
	private BigDecimal creditMoney;
	/** 状态 */
	private Integer status;
	/** 记账员 */
	private String operator;
	private String accName;
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
