package com.bytter.biz.budget.vo;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 计划上报VO - 月分解
 * @author huangx
 *
 */
public class FbsBudgetInputResolveMonthVO extends FbsBudgetInputVO implements Serializable {
	/**第1周上报金额*/
	private BigDecimal[] bMoney1;
	/**第2周上报金额*/
	private BigDecimal[] bMoney2;
	/**第3周上报金额*/
	private BigDecimal[] bMoney3;
	/**第4周上报金额*/
	private BigDecimal[] bMoney4;
	/**第5周上报金额*/
	private BigDecimal[] bMoney5;
	/**第6周上报金额*/
	private BigDecimal[] bMoney6;
	/**分解ID*/
	private String[] resolveId;
	/**第1周新追加金额*/
	private BigDecimal[] btempMoney1;
	/**第2周新追加金额*/
	private BigDecimal[] btempMoney2;
	/**第3周新追加金额*/
	private BigDecimal[] btempMoney3;
	/**第4周新追加金额*/
	private BigDecimal[] btempMoney4;
	/**第5周新追加金额*/
	private BigDecimal[] btempMoney5;
	/**第6周新追加金额*/
	private BigDecimal[] btempMoney6;
	
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
	public BigDecimal[] getbMoney4() {
		return bMoney4;
	}
	public void setbMoney4(BigDecimal[] bMoney4) {
		this.bMoney4 = bMoney4;
	}
	public BigDecimal[] getbMoney5() {
		return bMoney5;
	}
	public void setbMoney5(BigDecimal[] bMoney5) {
		this.bMoney5 = bMoney5;
	}
	public BigDecimal[] getbMoney6() {
		return bMoney6;
	}
	public void setbMoney6(BigDecimal[] bMoney6) {
		this.bMoney6 = bMoney6;
	}
	public String[] getResolveId() {
		return resolveId;
	}
	public void setResolveId(String[] resolveId) {
		this.resolveId = resolveId;
	}
	public BigDecimal[] getBtempMoney1() {
		return btempMoney1;
	}
	public void setBtempMoney1(BigDecimal[] btempMoney1) {
		this.btempMoney1 = btempMoney1;
	}
	public BigDecimal[] getBtempMoney2() {
		return btempMoney2;
	}
	public BigDecimal[] getBMoney1() {
		return bMoney1;
	}
	public void setBMoney1(BigDecimal[] bMoney1) {
		this.bMoney1 = bMoney1;
	}
	public BigDecimal[] getBMoney2() {
		return bMoney2;
	}
	public void setBMoney2(BigDecimal[] bMoney2) {
		this.bMoney2 = bMoney2;
	}
	public BigDecimal[] getBMoney3() {
		return bMoney3;
	}
	public void setBMoney3(BigDecimal[] bMoney3) {
		this.bMoney3 = bMoney3;
	}
	public BigDecimal[] getBMoney4() {
		return bMoney4;
	}
	public void setBMoney4(BigDecimal[] bMoney4) {
		this.bMoney4 = bMoney4;
	}
	public BigDecimal[] getBMoney5() {
		return bMoney5;
	}
	public void setBMoney5(BigDecimal[] bMoney5) {
		this.bMoney5 = bMoney5;
	}
	public BigDecimal[] getBMoney6() {
		return bMoney6;
	}
	public void setBMoney6(BigDecimal[] bMoney6) {
		this.bMoney6 = bMoney6;
	}
	public void setBtempMoney2(BigDecimal[] btempMoney2) {
		this.btempMoney2 = btempMoney2;
	}
	public BigDecimal[] getBtempMoney3() {
		return btempMoney3;
	}
	public void setBtempMoney3(BigDecimal[] btempMoney3) {
		this.btempMoney3 = btempMoney3;
	}
	public BigDecimal[] getBtempMoney4() {
		return btempMoney4;
	}
	public void setBtempMoney4(BigDecimal[] btempMoney4) {
		this.btempMoney4 = btempMoney4;
	}
	public BigDecimal[] getBtempMoney5() {
		return btempMoney5;
	}
	public void setBtempMoney5(BigDecimal[] btempMoney5) {
		this.btempMoney5 = btempMoney5;
	}
	public BigDecimal[] getBtempMoney6() {
		return btempMoney6;
	}
	public void setBtempMoney6(BigDecimal[] btempMoney6) {
		this.btempMoney6 = btempMoney6;
	}
}
