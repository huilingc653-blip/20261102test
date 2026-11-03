package com.bytter.biz.finacial.accbook.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class FcChargeAccruePageVO implements Serializable {
	
	@BtCompositeSearch(fieldName="charge.id")
	private String id;
	/**
	 * 计提账号
	 */
	@BtCompositeSearch(fieldName="charge.account")
	private String account;
	/**
	 * 凭证号
	 */
	@BtCompositeSearch(fieldName="voucherNo")
	private String voucherNo;
	
	/**
	 * 户名
	 */
	private String accName;
	/**
	 * 凭证种类
	 */
	@BtCompositeSearch(fieldName="charge.voucher_type")
	private String voucherType;

	private String voucherName;
	/**
	 * 计提标识
	 */
	@BtCompositeSearch(fieldName="charge.tally_sign")
	private String tallySign;
	/**
	 * 计提笔数
	 */
	private Integer cnt;
	/**
	 * 计提金额
	 */
	private BigDecimal chargeMoney;
	
	/**
	 * 开始记账日期
	 */
	@BtCompositeSearch(operCode = ">=", fieldName = "book.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDateBegin;
	
	/**
	 * 结束记账日期
	 */
    @BtCompositeSearch(operCode = "<=", fieldName = "book.tally_Date")
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDateEnd;
    
    /**
	 * 币别
	 */
    @BtCompositeSearch(fieldName = "acc.cur_id")
    private String curCode;
    
    /**
	 * 网点
	 */
    @BtCompositeSearch(fieldName = "acc.net_id")
    private String netCode;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getTallySign() {
		return tallySign;
	}
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public BigDecimal getChargeMoney() {
		return chargeMoney;
	}
	public void setChargeMoney(BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public Date getTallyDateBegin() {
		return tallyDateBegin;
	}
	public void setTallyDateBegin(Date tallyDateBegin) {
		this.tallyDateBegin = tallyDateBegin;
	}
	public Date getTallyDateEnd() {
		return tallyDateEnd;
	}
	public void setTallyDateEnd(Date tallyDateEnd) {
		this.tallyDateEnd = tallyDateEnd;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	@Override
	public String toString() {
		return "FcChargeAccruePageVO{" +
				"id='" + id + '\'' +
				", account='" + account + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", accName='" + accName + '\'' +
				", voucherType='" + voucherType + '\'' +
				", voucherName='" + voucherName + '\'' +
				", tallySign='" + tallySign + '\'' +
				", cnt=" + cnt +
				", chargeMoney=" + chargeMoney +
				", tallyDateBegin=" + tallyDateBegin +
				", tallyDateEnd=" + tallyDateEnd +
				", curCode='" + curCode + '\'' +
				", netCode='" + netCode + '\'' +
				'}';
	}
}
