package com.bytter.tally.vo;
import java.io.Serializable;
import java.math.BigDecimal;
public class FcAccbookNotifyVO implements Serializable {
	/**
	 * 流水号
	 */
	private String orderNo;
	/**
	 * 金额
	 */
	private BigDecimal money;
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	@Override
	public String toString() {
		return "FcAccbookNotifyVO{" +
				"orderNo='" + orderNo + '\'' +
				", money=" + money +
				'}';
	}
}
