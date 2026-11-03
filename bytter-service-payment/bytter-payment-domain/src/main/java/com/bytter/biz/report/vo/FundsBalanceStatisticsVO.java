package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 资金余额统计表（报表）
 *
 * @author wangzx
 * @date 2018-4-13
 */
public class FundsBalanceStatisticsVO implements Serializable {
	/**
	 *单位
	 */
	@BtCompositeSearch(fieldName = "f.corpId")
	@Corp(inject = "corpName")
	private String corpId;
	@BtCompositeSearch(fieldName = "f.corpId")
	private String corpName;
	/**
	 * 币别
	 */
	@BtCompositeSearch(fieldName = "f.curId")
	@Currency(inject = "curName")
	private String curId;
	@BtCompositeSearch(fieldName = "f.curId")
	private String curName;
	/**
	 * 账户类型
	 */
	private String accType;
	/**
	 * 余额
	 */
	private BigDecimal balance;
	/**
	 * 银行合计
	 */
	private BigDecimal bankTotal;
	/**
	 * 内部合计
	 */
	private BigDecimal fcTotal;
	/**
	 * 银行存款 - 活期
	 */
	private BigDecimal accType01;
	/**
	 * 银行存款 - 定期
	 */
	private BigDecimal accType02;
	/**
	 * 银行存款 - 贷款户
	 */
	private BigDecimal accType03;
	/**
	 * 银行存款 - 保证金
	 */
	private BigDecimal accType04;
	/**
	 * 银行存款 - 专用户
	 */
	private BigDecimal accType05;
	/**
	 * 银行存款 - 通知
	 */
	private BigDecimal accType06;
	/**
	 * 银行存款 - 协定
	 */
	private BigDecimal accType07;
	/**
	 * 银行存款 - 虚拟户
	 */
	private BigDecimal accType09;
	/**
	 * 银行存款 - 票据专户
	 */
	private BigDecimal accType10;
	/**
	 * 内部存款 - 现金
	 */
	private BigDecimal accType11;
	/**
	 * 内部存款 - 活期
	 */
	private BigDecimal accType12;
	/**
	 * 内部存款 - 定期
	 */
	private BigDecimal accType13;
	/**
	 * 内部存款 - 通知
	 */
	private BigDecimal accType14;
	/**是否折合币种*/
	private String changeCur;
	/**折合币种*/
	private String reducedCurId;
	/**折算汇率类型*/
	private String rateType;
	/**汇率日期*/
	private Date rateDate;
	/**折合汇率*/
	private BigDecimal curExrate;
	/**金额单位*/
	private Integer unit;
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
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
	public String getAccType() {
		return accType;
	}
	public void setAccType(String accType) {
		this.accType = accType;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getBankTotal() {
		return bankTotal;
	}
	public void setBankTotal(BigDecimal bankTotal) {
		this.bankTotal = bankTotal;
	}
	public BigDecimal getFcTotal() {
		return fcTotal;
	}
	public void setFcTotal(BigDecimal fcTotal) {
		this.fcTotal = fcTotal;
	}
	public BigDecimal getAccType01() {
		return accType01;
	}
	public void setAccType01(BigDecimal accType01) {
		this.accType01 = accType01;
	}
	public BigDecimal getAccType02() {
		return accType02;
	}
	public void setAccType02(BigDecimal accType02) {
		this.accType02 = accType02;
	}
	public BigDecimal getAccType03() {
		return accType03;
	}
	public void setAccType03(BigDecimal accType03) {
		this.accType03 = accType03;
	}
	public BigDecimal getAccType04() {
		return accType04;
	}
	public void setAccType04(BigDecimal accType04) {
		this.accType04 = accType04;
	}
	public BigDecimal getAccType05() {
		return accType05;
	}
	public void setAccType05(BigDecimal accType05) {
		this.accType05 = accType05;
	}
	public BigDecimal getAccType06() {
		return accType06;
	}
	public void setAccType06(BigDecimal accType06) {
		this.accType06 = accType06;
	}
	public BigDecimal getAccType07() {
		return accType07;
	}
	public void setAccType07(BigDecimal accType07) {
		this.accType07 = accType07;
	}
	public BigDecimal getAccType09() {
		return accType09;
	}
	public void setAccType09(BigDecimal accType09) {
		this.accType09 = accType09;
	}
	public BigDecimal getAccType10() {
		return accType10;
	}
	public void setAccType10(BigDecimal accType10) {
		this.accType10 = accType10;
	}
	public BigDecimal getAccType11() {
		return accType11;
	}
	public void setAccType11(BigDecimal accType11) {
		this.accType11 = accType11;
	}
	public BigDecimal getAccType12() {
		return accType12;
	}
	public void setAccType12(BigDecimal accType12) {
		this.accType12 = accType12;
	}
	public BigDecimal getAccType13() {
		return accType13;
	}
	public void setAccType13(BigDecimal accType13) {
		this.accType13 = accType13;
	}
	public BigDecimal getAccType14() {
		return accType14;
	}
	public void setAccType14(BigDecimal accType14) {
		this.accType14 = accType14;
	}
	public String getChangeCur() {
		return changeCur;
	}
	public void setChangeCur(String changeCur) {
		this.changeCur = changeCur;
	}
	public String getReducedCurId() {
		return reducedCurId;
	}
	public void setReducedCurId(String reducedCurId) {
		this.reducedCurId = reducedCurId;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public Date getRateDate() {
		return rateDate;
	}
	public void setRateDate(Date rateDate) {
		this.rateDate = rateDate;
	}
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
	}
	public Integer getUnit() {
		return unit;
	}
	public void setUnit(Integer unit) {
		this.unit = unit;
	}
}
