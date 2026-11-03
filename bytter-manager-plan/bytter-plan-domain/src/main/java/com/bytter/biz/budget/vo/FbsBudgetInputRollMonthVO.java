package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 计划上报VO - 月滚动
 * @author
 *
 */
@SuppressWarnings("serial")
public class FbsBudgetInputRollMonthVO extends FbsBudgetInputVO implements Serializable{
	/**本月上报金额*/
	private BigDecimal[] bMoney1;
	/**下月上报金额*/
	private BigDecimal[] bMoney2;
	/**下下月上报金额*/
	private BigDecimal[] bMoney3;
	/**下月明细id*/
	private String[] nextDetailId;
	/**下下月明细id*/
	private String[] superNextDetailId;
	/**下月预算id*/
	private String nextId;
	/**下下月预算id*/
	private String superNextId;
	
	
	public String getNextId() {
		return nextId;
	}
	public void setNextId(String nextId) {
		this.nextId = nextId;
	}
	public String getSuperNextId() {
		return superNextId;
	}
	public void setSuperNextId(String superNextId) {
		this.superNextId = superNextId;
	}
	
	
	public BigDecimal[] getbMoney1() {
		return bMoney1;
	}
	public void setbMoney1(BigDecimal[] bMoney1) {
		this.bMoney1 = bMoney1;
	}
	public BigDecimal[] getbMoney2() {
		return bMoney2;
	}
	public void setbMoney2(BigDecimal[] bMoney2) {
		this.bMoney2 = bMoney2;
	}
	public BigDecimal[] getbMoney3() {
		return bMoney3;
	}
	public void setbMoney3(BigDecimal[] bMoney3) {
		this.bMoney3 = bMoney3;
	}
	public String[] getNextDetailId() {
		return nextDetailId;
	}
	public void setNextDetailId(String[] nextDetailId) {
		this.nextDetailId = nextDetailId;
	}
	public String[] getSuperNextDetailId() {
		return superNextDetailId;
	}
	public void setSuperNextDetailId(String[] superNextDetailId) {
		this.superNextDetailId = superNextDetailId;
	}
}
