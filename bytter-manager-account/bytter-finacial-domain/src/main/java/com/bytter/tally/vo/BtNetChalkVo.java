package com.bytter.tally.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @Title: Entity
 * @Description: BT_NET_CHALK
 * @author lixi
 * @date 2017-07-04 14:58:22
 * @version 1.0
 *
 */
public class BtNetChalkVo implements java.io.Serializable  {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	/**交易日期*/
	@BtCompositeSearch(fieldName = "vbn.trans_Time")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date transTime;
	/**单位代码*/
	@BtCompositeSearch(fieldName = "vbn.corp_Id")
	@Corp(inject = "corpName")
	private String corpId;
	private String corpName;
	/**收支标识*/
	private String cdSign;
	/**币别代码*/
	@BtCompositeSearch(fieldName = "vbn.cur_Id")
	@Currency(inject="curName")
	private String curId;
	private String curName;
	/**本方账号ID*/
	private String bankAccId;
	/**本方账号*/
	@BtCompositeSearch(fieldName = "vbn.bank_Acc")
	private String bankAcc;
	/**本方账号开户行*/
	private String bankName;
	/**本方账号名*/
	@BtCompositeSearch(fieldName = "vbn.acc_Name")
	private String accName;
	/**本方内部账号*/
	private String cntAccount;
	/**对方账号*/
	@BtCompositeSearch(fieldName = "vbn.opp_Acc_No")
	private String oppAccNo;
	/**对方账号开户行*/
	@BtCompositeSearch(fieldName = "vbn.opp_Bank_Name")
	private String oppBankName;
	/**对方账户名*/
	@BtCompositeSearch(fieldName = "vbn.opp_Acc_Name")
	private String oppAccName;
	/**摘要*/
	@BtCompositeSearch(fieldName = "vbn.purpose")
	private String purpose;
	/**金额*/
	@BtCompositeSearch(fieldName = "vbn.money")
	private java.math.BigDecimal money;
	/**状态*/
	@BtCompositeSearch(fieldName = "vbn.estate")
	@Dict(dictGroup="btNetChalk.estate" , inject="state")
	private String estate;
	private String state;
	/**记账员代码*/
	private String operatorCode;
	/**记账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/**流水号*/
	private String orderNo;
	/**凭证号*/
	private String voucherNo;
	/**复核员代码*/
	private String checkerCode;
	/**复核日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date checkTime;
	/**银行流水号*/
	private String bankSerial;
	/**取消原因*/
	private String refuseWhy;
	/**是否勾兑标识*/
	private String blend;
	/**退款勾兑原因*/
	private String blendInfo;
	private String bookId;
	private String busGlobalId;
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.util.Date getTransTime() {
		return transTime;
	}
	public void setTransTime(java.util.Date transTime) {
		this.transTime = transTime;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
	public String getCurId() {
		return curId;
	}
	public void setCurId(String curId) {
		this.curId = curId;
	}
	public String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
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
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getCntAccount() {
		return cntAccount;
	}
	public void setCntAccount(String cntAccount) {
		this.cntAccount = cntAccount;
	}
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public String getOppBankName() {
		return oppBankName;
	}
	public void setOppBankName(String oppBankName) {
		this.oppBankName = oppBankName;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public java.math.BigDecimal getMoney() {
		return money;
	}
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	public String getEstate() {
		return estate;
	}
	public void setEstate(String estate) {
		this.estate = estate;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public java.util.Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(java.util.Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getBankSerial() {
		return bankSerial;
	}
	public void setBankSerial(String bankSerial) {
		this.bankSerial = bankSerial;
	}
	public String getRefuseWhy() {
		return refuseWhy;
	}
	public void setRefuseWhy(String refuseWhy) {
		this.refuseWhy = refuseWhy;
	}
	public String getBlend() {
		return blend;
	}
	public void setBlend(String blend) {
		this.blend = blend;
	}
	public String getBlendInfo() {
		return blendInfo;
	}
	public void setBlendInfo(String blendInfo) {
		this.blendInfo = blendInfo;
	}
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCurName() {
		return curName;
	}
	public void setCurName(String curName) {
		this.curName = curName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
