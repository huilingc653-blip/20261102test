package com.bytter.report.domain.stock.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 账户冻结明细VO
 * @author zuoy 2017-10-25
 */
public class BtBankAccFreezeVO implements Serializable {
	private String id;
	/**
	 * 单位ID
	 */
	@Corp(inject = "corpName")
	private String corpId;
	/**
	 * 单位名称
	 */
	private String corpName;
	/**
	 * 币别ID
	 */
	@Currency(inject = "curName")
	private String curId;
	/**
	 * 币别名称
	 */
	private String curName;
	/**
	 * 银行类型ID
	 */
	private String bankTypeId;
	/**
	 * 银行类型名称
	 */
	private String bankTypeName;
	/**
	 * 账号
	 */
	private String bankAcc;
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 开户行
	 */
	private String bankName;
	/**
	 * 冻结金额
	 */
	private BigDecimal freezeMoney;
	/**
	 * 冻结日期
	 */
	private Date freezeDate;
	/**
	 * 冻结原因
	 */
	private String freezeRession;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getBankTypeName() {
		return bankTypeName;
	}
	public void setBankTypeName(String bankTypeName) {
		this.bankTypeName = bankTypeName;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public BigDecimal getFreezeMoney() {
		return freezeMoney;
	}
	public void setFreezeMoney(BigDecimal freezeMoney) {
		this.freezeMoney = freezeMoney;
	}
	public Date getFreezeDate() {
		return freezeDate;
	}
	public void setFreezeDate(Date freezeDate) {
		this.freezeDate = freezeDate;
	}
	public String getFreezeRession() {
		return freezeRession;
	}
	public void setFreezeRession(String freezeRession) {
		this.freezeRession = freezeRession;
	}
}
