package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhuop
 * @Description 客商收入资金量统计情况
 * @date 2018.8.15
 */
public class NisBillheadVO implements Serializable {
	
	/**单位名称*/
	@BtCompositeSearch(fieldName="d.corpId")
	public String corpName;
	/**单位ID*/
	@BtCompositeSearch(fieldName="d.corpId")
	public String corpId;
	/**币别名称*/
	@BtCompositeSearch(fieldName="bt.cur_name")
	public String curName;
	/**币别ID*/
	@BtCompositeSearch(fieldName="d.curId")
	public String curId;
	@BtCompositeSearch(fieldName="sc.id")
	private String id;
	/**客商编码*/
	@BtCompositeSearch(fieldName="sc.code")
	public String code;
	/**客商名称*/
	@BtCompositeSearch(fieldName="sc.name")
	public String merchantName;
	/**客商简称*/
	@BtCompositeSearch(fieldName="sc.abbreviate")
	public String abbreviate;
	/**交易金额*/
	@BtCompositeSearch(fieldName="payMoney")
	public BigDecimal payMoney;
	/**付款笔数*/
	@BtCompositeSearch(fieldName="payNumber")
	public BigDecimal payNumber;
	/**付款账号*/
	public String payerAcc;
	/**付款账号名称*/
	public String payerAccName;
	/**付款开户行名称*/
	public String payerBank;
	/**付款方式*/
	public String voucherName;
	/**备注*/
	public  String addWord;
	/**收款账号*/
	public String payeeAcc;
	/**收款账号名称*/
	public String payeeAccName;
	/**收款开户行名称*/
	public String payeeBank;
	/**申请日期*/
	public Date createDate;
	/**期望支付日期*/
	public Date wishPayDay;
	public String startDate;
	public String endDate;
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
	
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getAbbreviate() {
		return abbreviate;
	}
	public void setAbbreviate(String abbreviate) {
		this.abbreviate = abbreviate;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public BigDecimal getPayNumber() {
		return payNumber;
	}
	public void setPayNumber(BigDecimal payNumber) {
		this.payNumber = payNumber;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerAccName() {
		return payerAccName;
	}
	public void setPayerAccName(String payerAccName) {
		this.payerAccName = payerAccName;
	}
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getWishPayDay() {
		return wishPayDay;
	}
	public void setWishPayDay(Date wishPayDay) {
		this.wishPayDay = wishPayDay;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
}
