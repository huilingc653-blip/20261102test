package com.bytter.subject.vo;
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
	private String netId;
	private String netName;
	/**区域代码*/
	private String areaCode;
	/**行业代码*/
	private String vocationCode;
	/**科目Id*/
	@BtCompositeSearch(fieldName="fs.subjectId")
	@Dict(tableName="FC_SUBJECT",valueFieldName="id",showFiledName="SUBJECT_NAME",inject="subjectName")
	private String subjectId;
	/**科目Code*/
	private String subjectCode;
	/**科目Name*/
	@BtCompositeSearch(fieldName="fs.subjectName")
	private String subjectName;
	/**币别代码*/
	@BtCompositeSearch(fieldName="fs.curId")
	@Dict(tableName="BT_CURRENCY",valueFieldName="id",showFiledName="CUR_NAME",inject="curName")
	private String curId;
	private String curName;
	/**转账借方金额*/
	private java.math.BigDecimal trDebitMoney;
	private java.math.BigDecimal trDebitMoneySum;
	/**转账借方笔数*/
	private Integer trDebitCnt;
	private Integer trDebitCntSum;
	/**转账贷方金额*/
	private java.math.BigDecimal trCreditMoney;
	private java.math.BigDecimal trCreditMoneySum;
	/**转账贷方笔数*/
	private Integer trCreditCnt;
	private Integer trCreditCntSum;
	/**现金借方金额*/
	private java.math.BigDecimal cashDebitMoney;
	private java.math.BigDecimal cashDebitMoneySum;
	/**现金借方笔数*/
	private Integer cashDebitCnt;
	private Integer cashDebitCntSum;
	/**现金贷方金额*/
	private java.math.BigDecimal cashCreditMoney;
	private java.math.BigDecimal cashCreditMoneySum;
	/**现金贷方笔数*/
	private Integer cashCreditCnt;
	private Integer cashCreditCntSum;
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
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getVocationCode() {
		return vocationCode;
	}
	public void setVocationCode(String vocationCode) {
		this.vocationCode = vocationCode;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSubjectCode() {
		return subjectCode;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
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
	public Integer getTrDebitCnt() {
		return trDebitCnt;
	}
	public void setTrDebitCnt(Integer trDebitCnt) {
		this.trDebitCnt = trDebitCnt;
	}
	public Integer getTrDebitCntSum() {
		return trDebitCntSum;
	}
	public void setTrDebitCntSum(Integer trDebitCntSum) {
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
	public Integer getTrCreditCnt() {
		return trCreditCnt;
	}
	public void setTrCreditCnt(Integer trCreditCnt) {
		this.trCreditCnt = trCreditCnt;
	}
	public Integer getTrCreditCntSum() {
		return trCreditCntSum;
	}
	public void setTrCreditCntSum(Integer trCreditCntSum) {
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
	public Integer getCashDebitCnt() {
		return cashDebitCnt;
	}
	public void setCashDebitCnt(Integer cashDebitCnt) {
		this.cashDebitCnt = cashDebitCnt;
	}
	public Integer getCashDebitCntSum() {
		return cashDebitCntSum;
	}
	public void setCashDebitCntSum(Integer cashDebitCntSum) {
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
	public Integer getCashCreditCnt() {
		return cashCreditCnt;
	}
	public void setCashCreditCnt(Integer cashCreditCnt) {
		this.cashCreditCnt = cashCreditCnt;
	}
	public Integer getCashCreditCntSum() {
		return cashCreditCntSum;
	}
	public void setCashCreditCntSum(Integer cashCreditCntSum) {
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
