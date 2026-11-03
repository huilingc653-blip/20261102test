package com.bytter.biz.finacial.closedeal.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class FcSubjectDayVo implements Serializable {
	 private String id;
	/**结算日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName="fs.optDate")
	private java.util.Date optDate;
	/**所属网点*/
	@BtCompositeSearch(fieldName="fs.netid")
	private java.lang.String netId;
	private java.lang.String netName;
	/**区域代码*/
	private java.lang.String areaCode;
	/**行业代码*/
	private java.lang.String vocationCode;
	/**科目Id*/
	@BtCompositeSearch(fieldName="fs.subjectId")
	private java.lang.String subjectId;
	/**科目Code*/
	private java.lang.String subjectCode;
	/**科目Name*/
	@BtCompositeSearch(fieldName="fs.subjectName")
	private java.lang.String subjectName;
	/**币别代码*/
	@BtCompositeSearch(fieldName="fs.curId")
	private java.lang.String curId;
	private java.lang.String curName;
	/**转账借方金额*/
	private java.math.BigDecimal trDebitMoney;
	private java.math.BigDecimal trDebitMoneySum;
	/**转账借方笔数*/
	private java.lang.Integer trDebitCnt;
	private java.lang.Integer trDebitCntSum;
	/**转账贷方金额*/
	private java.math.BigDecimal trCreditMoney;
	private java.math.BigDecimal trCreditMoneySum;
	/**转账贷方笔数*/
	private java.lang.Integer trCreditCnt;
	private java.lang.Integer trCreditCntSum;
	/**现金借方金额*/
	private java.math.BigDecimal cashDebitMoney;
	private java.math.BigDecimal cashDebitMoneySum;
	/**现金借方笔数*/
	private java.lang.Integer cashDebitCnt;
	private java.lang.Integer cashDebitCntSum;
	/**现金贷方金额*/
	private java.math.BigDecimal cashCreditMoney;
	private java.math.BigDecimal cashCreditMoneySum;
	/**现金贷方笔数*/
	private java.lang.Integer cashCreditCnt;
	private java.lang.Integer cashCreditCntSum;
	/**昨日余额*/
	private java.math.BigDecimal yestBalance;
	private java.math.BigDecimal yestBalanceSum;
	/**今日余额*/
	private java.math.BigDecimal todayBalance;
	private java.math.BigDecimal todayBalanceSum;
	
	private List<FcSubjectDayVo> fsd = new ArrayList<FcSubjectDayVo>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.util.Date getOptDate() {
		return optDate;
	}
	public void setOptDate(java.util.Date optDate) {
		this.optDate = optDate;
	}
	public java.lang.String getNetId() {
		return netId;
	}
	public void setNetId(java.lang.String netId) {
		this.netId = netId;
	}
	public java.lang.String getNetName() {
		return netName;
	}
	public void setNetName(java.lang.String netName) {
		this.netName = netName;
	}
	public java.lang.String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(java.lang.String areaCode) {
		this.areaCode = areaCode;
	}
	public java.lang.String getVocationCode() {
		return vocationCode;
	}
	public void setVocationCode(java.lang.String vocationCode) {
		this.vocationCode = vocationCode;
	}
	public java.lang.String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(java.lang.String subjectId) {
		this.subjectId = subjectId;
	}
	public java.lang.String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(java.lang.String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public java.lang.String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(java.lang.String subjectName) {
		this.subjectName = subjectName;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.math.BigDecimal getTrDebitMoney() {
		return trDebitMoney;
	}
	public void setTrDebitMoney(java.math.BigDecimal trDebitMoney) {
		this.trDebitMoney = trDebitMoney;
	}
	public java.math.BigDecimal getTrDebitMoneySum() {
		return trDebitMoneySum;
	}
	public void setTrDebitMoneySum(java.math.BigDecimal trDebitMoneySum) {
		this.trDebitMoneySum = trDebitMoneySum;
	}
	public java.lang.Integer getTrDebitCnt() {
		return trDebitCnt;
	}
	public void setTrDebitCnt(java.lang.Integer trDebitCnt) {
		this.trDebitCnt = trDebitCnt;
	}
	public java.lang.Integer getTrDebitCntSum() {
		return trDebitCntSum;
	}
	public void setTrDebitCntSum(java.lang.Integer trDebitCntSum) {
		this.trDebitCntSum = trDebitCntSum;
	}
	public java.math.BigDecimal getTrCreditMoney() {
		return trCreditMoney;
	}
	public void setTrCreditMoney(java.math.BigDecimal trCreditMoney) {
		this.trCreditMoney = trCreditMoney;
	}
	public java.math.BigDecimal getTrCreditMoneySum() {
		return trCreditMoneySum;
	}
	public void setTrCreditMoneySum(java.math.BigDecimal trCreditMoneySum) {
		this.trCreditMoneySum = trCreditMoneySum;
	}
	public java.lang.Integer getTrCreditCnt() {
		return trCreditCnt;
	}
	public void setTrCreditCnt(java.lang.Integer trCreditCnt) {
		this.trCreditCnt = trCreditCnt;
	}
	public java.lang.Integer getTrCreditCntSum() {
		return trCreditCntSum;
	}
	public void setTrCreditCntSum(java.lang.Integer trCreditCntSum) {
		this.trCreditCntSum = trCreditCntSum;
	}
	public java.math.BigDecimal getCashDebitMoney() {
		return cashDebitMoney;
	}
	public void setCashDebitMoney(java.math.BigDecimal cashDebitMoney) {
		this.cashDebitMoney = cashDebitMoney;
	}
	public java.math.BigDecimal getCashDebitMoneySum() {
		return cashDebitMoneySum;
	}
	public void setCashDebitMoneySum(java.math.BigDecimal cashDebitMoneySum) {
		this.cashDebitMoneySum = cashDebitMoneySum;
	}
	public java.lang.Integer getCashDebitCnt() {
		return cashDebitCnt;
	}
	public void setCashDebitCnt(java.lang.Integer cashDebitCnt) {
		this.cashDebitCnt = cashDebitCnt;
	}
	public java.lang.Integer getCashDebitCntSum() {
		return cashDebitCntSum;
	}
	public void setCashDebitCntSum(java.lang.Integer cashDebitCntSum) {
		this.cashDebitCntSum = cashDebitCntSum;
	}
	public java.math.BigDecimal getCashCreditMoney() {
		return cashCreditMoney;
	}
	public void setCashCreditMoney(java.math.BigDecimal cashCreditMoney) {
		this.cashCreditMoney = cashCreditMoney;
	}
	public java.math.BigDecimal getCashCreditMoneySum() {
		return cashCreditMoneySum;
	}
	public void setCashCreditMoneySum(java.math.BigDecimal cashCreditMoneySum) {
		this.cashCreditMoneySum = cashCreditMoneySum;
	}
	public java.lang.Integer getCashCreditCnt() {
		return cashCreditCnt;
	}
	public void setCashCreditCnt(java.lang.Integer cashCreditCnt) {
		this.cashCreditCnt = cashCreditCnt;
	}
	public java.lang.Integer getCashCreditCntSum() {
		return cashCreditCntSum;
	}
	public void setCashCreditCntSum(java.lang.Integer cashCreditCntSum) {
		this.cashCreditCntSum = cashCreditCntSum;
	}
	public java.math.BigDecimal getYestBalance() {
		return yestBalance;
	}
	public void setYestBalance(java.math.BigDecimal yestBalance) {
		this.yestBalance = yestBalance;
	}
	public java.math.BigDecimal getTodayBalance() {
		return todayBalance;
	}
	public void setTodayBalance(java.math.BigDecimal todayBalance) {
		this.todayBalance = todayBalance;
	}
	public List<FcSubjectDayVo> getFsd() {
		return fsd;
	}
	public void setFsd(List<FcSubjectDayVo> fsd) {
		this.fsd = fsd;
	}
	public java.math.BigDecimal getYestBalanceSum() {
		return yestBalanceSum;
	}
	public void setYestBalanceSum(java.math.BigDecimal yestBalanceSum) {
		this.yestBalanceSum = yestBalanceSum;
	}
	public java.math.BigDecimal getTodayBalanceSum() {
		return todayBalanceSum;
	}
	public void setTodayBalanceSum(java.math.BigDecimal todayBalanceSum) {
		this.todayBalanceSum = todayBalanceSum;
	}
	
	
}
