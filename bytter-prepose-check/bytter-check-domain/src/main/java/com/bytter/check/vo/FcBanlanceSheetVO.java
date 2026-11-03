package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.math.BigDecimal;
public class FcBanlanceSheetVO implements Serializable{
	 /**币别编码*/
	 @BtCompositeSearch(fieldName="fis.cur_code")
    private String  curCode;
    /**币别名称*/
    private String  curName;
    /**币别ID*/
    @BtCompositeSearch(fieldName="bc.id")
    private String  curId;
    /**网点Id*/
    @BtCompositeSearch(fieldName="fis.net_code")
    private String  netCode;
    /**网点名称*/
    private String  netName;
    /**资产项目名*/
    private String  aItem;
    /**资产行次*/
    @BtCompositeSearch(fieldName="fis.a_order_no")
    private Integer  aOrderNo;
    /**资产年初数*/
    private BigDecimal aYearBalance;
    /**资产期末数*/
    private BigDecimal aMonthBalance;
    /**负债项目名*/
    private String  lItem;
    /**负债行次*/
    private Integer  lOrderNo;
    /**负债年初数*/
    private BigDecimal lYearBalance;
    /**负债期末数*/
    private BigDecimal lMonthBalance;
    /**年月*/
    @BtCompositeSearch(fieldName="fis.year_month2")
    private String yearMonth2;
	private boolean totalFlag = false;
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
	public String getaItem() {
		return aItem;
	}
	public void setaItem(String aItem) {
		this.aItem = aItem;
	}
	public Integer getaOrderNo() {
		return aOrderNo;
	}
	public void setaOrderNo(Integer aOrderNo) {
		this.aOrderNo = aOrderNo;
	}
	public BigDecimal getaYearBalance() {
		return aYearBalance;
	}
	public void setaYearBalance(BigDecimal aYearBalance) {
		this.aYearBalance = aYearBalance;
	}
	public BigDecimal getaMonthBalance() {
		return aMonthBalance;
	}
	public void setaMonthBalance(BigDecimal aMonthBalance) {
		this.aMonthBalance = aMonthBalance;
	}
	public String getlItem() {
		return lItem;
	}
	public void setlItem(String lItem) {
		this.lItem = lItem;
	}
	public Integer getlOrderNo() {
		return lOrderNo;
	}
	public void setlOrderNo(Integer lOrderNo) {
		this.lOrderNo = lOrderNo;
	}
	public BigDecimal getlYearBalance() {
		return lYearBalance;
	}
	public void setlYearBalance(BigDecimal lYearBalance) {
		this.lYearBalance = lYearBalance;
	}
	public BigDecimal getlMonthBalance() {
		return lMonthBalance;
	}
	public void setlMonthBalance(BigDecimal lMonthBalance) {
		this.lMonthBalance = lMonthBalance;
	}
	public String getYearMonth2() {
		return yearMonth2;
	}
	public void setYearMonth2(String yearMonth2) {
		this.yearMonth2 = yearMonth2;
	}
	public boolean isTotalFlag() {
		return totalFlag;
	}
	public void setTotalFlag(boolean totalFlag) {
		this.totalFlag = totalFlag;
	}
}
