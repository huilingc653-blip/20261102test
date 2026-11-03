package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.math.BigDecimal;
public class FcIncomStatementVO implements Serializable{
	 /**项目*/
     private String item;
     /**行次*/
     @BtCompositeSearch(fieldName="fis.order_no")
     private Integer orderNo;
     /**本期数*/
     private BigDecimal balance;
     /**本年累计数*/
     private BigDecimal yearTotal;
     /**币别编码*/
     @BtCompositeSearch(fieldName="fis.cur_code")
     private String  curCode;
     /**币别名称*/
     private String  curName;
     /**币别ID*/
     private String  curId;
     /**网点Id*/
     @BtCompositeSearch(fieldName="fis.net_code")
     private String  netCode;
     /**网点名称*/
     private String  netName;
     /**年月*/
     @BtCompositeSearch(fieldName="fis.year_month2")
     private String  yearMonth2;
     
     private String eqs_netCode;
     private String eqs_curCode;
     private String checkDate;
	private String isConvert;
	private String convertCurId;
	private String preDate;
	/**
	 * 合计标志
	 */
	private boolean totalFlag = false;
	/**
	 * 利率类型
	 */
	private String rateType;
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getYearTotal() {
		return yearTotal;
	}
	public void setYearTotal(BigDecimal yearTotal) {
		this.yearTotal = yearTotal;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getYearMonth2() {
		return yearMonth2;
	}
	public void setYearMonth2(String yearMonth2) {
		this.yearMonth2 = yearMonth2;
	}
	public String getEqs_netCode() {
		return eqs_netCode;
	}
	public void setEqs_netCode(String eqs_netCode) {
		this.eqs_netCode = eqs_netCode;
	}
	public String getEqs_curCode() {
		return eqs_curCode;
	}
	public void setEqs_curCode(String eqs_curCode) {
		this.eqs_curCode = eqs_curCode;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getIsConvert() {
		return isConvert;
	}
	public void setIsConvert(String isConvert) {
		this.isConvert = isConvert;
	}
	public String getPreDate() {
		return preDate;
	}
	public void setPreDate(String preDate) {
		this.preDate = preDate;
	}
	public boolean isTotalFlag() {
		return totalFlag;
	}
	public void setTotalFlag(boolean totalFlag) {
		this.totalFlag = totalFlag;
	}
	public String getConvertCurId() {
		return convertCurId;
	}
	public void setConvertCurId(String convertCurId) {
		this.convertCurId = convertCurId;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
}
