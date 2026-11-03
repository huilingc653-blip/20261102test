package com.bytter.biz.report.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
/**
 * @author zhoup
 * @Description 内部银行账户余额分析情况表
 * @date 2018.6.8
 */
public class InternalAndBankAccBalVO implements Serializable{
	/**
	 * 网点ID
	 */
	@BtCompositeSearch(fieldName="c.netId")
	private String netId;
	/**
	 * 网点名称
	 */
	@BtCompositeSearch(fieldName="c.netName")
	private String netName;
	/**
	 * 单位ID
	 */
	@BtCompositeSearch(fieldName="c.corpId")
	private String corpId;
	/**
	 * 单位名称
	 */
	@BtCompositeSearch(fieldName="c.corpName")
	private String corpName;
	/**
	 * 币别ID
	 */
	@BtCompositeSearch(fieldName="c.curId")
	private String curId;
	/**
	 * 币别名称
	 */
	private String curName;
    /**
     * 银行类别
     */
    @BtCompositeSearch(fieldName = "c.bankType")
    private String bankType;
	/**
	 * 银行账号
	 */
	@BtCompositeSearch(fieldName="c.bankAcc")
	private String bankAcc;
	/**
	 * 银行账号名称
	 */
	@BtCompositeSearch(fieldName="c.bankName")
	private String bankName;
	/**
	 * 银行账户余额
	 */
	private BigDecimal bal;
	/**
	 * 银行账户冻结金额
	 */
	private BigDecimal frzBal;
	/**
	 * 银行账户可用余额
	 */
	private BigDecimal useBal;
	/**
	 * 内部账号
	 */
	@BtCompositeSearch(fieldName="c.accountAcc")
	private String accountAcc;
	/**
	 * 内部账号名称
	 */
	private String accountName;
	/**
	 * 内部账号余额
	 */
	private BigDecimal balance;
	/**
	 * 内部账号冻结金额
	 */
	private BigDecimal freezeMoney;
	/**
	 * 内部账号可用余额
	 */
	private BigDecimal useMoney;
	/**
	 * 是否在线  0-离线 1-在线
	 */
	private String isOnline;
	/**
	 * 日期
	 */
	private Date balDate;
	/**
	 * 判断是不是初次加载
	 */
	private String isNotFirst;
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
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public BigDecimal getBal() {
		return bal;
	}
	public void setBal(BigDecimal bal) {
		this.bal = bal;
	}
	public BigDecimal getFrzBal() {
		return frzBal;
	}
	public void setFrzBal(BigDecimal frzBal) {
		this.frzBal = frzBal;
	}
	public BigDecimal getUseBal() {
		return useBal;
	}
	public void setUseBal(BigDecimal useBal) {
		this.useBal = useBal;
	}
	public String getAccountAcc() {
		return accountAcc;
	}
	public void setAccountAcc(String accountAcc) {
		this.accountAcc = accountAcc;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public BigDecimal getUseMoney() {
		return useMoney;
	}
	public void setUseMoney(BigDecimal useMoney) {
		this.useMoney = useMoney;
	}
	public String getIsOnline() {
		return isOnline;
	}
	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
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
	public Date getBalDate() {
		return balDate;
	}
	public void setBalDate(Date balDate) {
		this.balDate = balDate;
	}
	public String getIsNotFirst() {
		return isNotFirst;
	}
	public void setIsNotFirst(String isNotFirst) {
		this.isNotFirst = isNotFirst;
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
