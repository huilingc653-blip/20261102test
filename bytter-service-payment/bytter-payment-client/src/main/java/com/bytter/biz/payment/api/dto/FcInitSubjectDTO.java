package com.bytter.biz.payment.api.dto;

import cn.hutool.core.convert.Convert;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 账户初始化数据
 * 
 * @author liyh
 *
 */
public class FcInitSubjectDTO implements Serializable {

	/**
	 * 帐号信息
	 */
	private String account;

	/** 网点代码 */
	private java.lang.String netId;
	/** 科目代码 */
	private java.lang.String subjectId;
	/** 币别代码 */
	private java.lang.String curId;
	/** 年余额 */
	private BigDecimal yearBalance;
	/** 年合计 */
	private BigDecimal yearTotal;

	private BigDecimal debitMoney;

	private BigDecimal creditMoney;

	private Integer balanceWay;


	public Integer getBalanceWay() {
		return balanceWay;
	}

	public void setBalanceWay(Integer balanceWay) {
		this.balanceWay = balanceWay;
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

	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(java.lang.String subjectId) {
		this.subjectId = subjectId;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public BigDecimal getYearBalance() {
		return yearBalance;
	}
	public void setYearBalance(BigDecimal yearBalance) {
		this.yearBalance = yearBalance;
	}
	public BigDecimal getYearTotal() {
		return yearTotal;
	}
	public void setYearTotal(BigDecimal yearTotal) {
		this.yearTotal = yearTotal;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public static FcInitSubjectDTO merge(FcInitSubjectDTO pre, FcInitSubjectDTO current) {
		FcInitSubjectDTO fcInitSubjectDTO = new FcInitSubjectDTO();
		fcInitSubjectDTO.setSubjectId(current.getSubjectId());
		fcInitSubjectDTO.setCurId(current.getCurId());
		BigDecimal preTotal = Convert.toBigDecimal(pre.getYearTotal(), BigDecimal.ZERO);
		BigDecimal curTotal = Convert.toBigDecimal(current.getYearTotal(), BigDecimal.ZERO);
		fcInitSubjectDTO.setYearTotal(preTotal.add(curTotal));
		fcInitSubjectDTO.setYearBalance(pre.getYearBalance().add(current.getYearBalance()));
		return fcInitSubjectDTO;
	}

}
