package com.bytter.biz.netbank.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.biz.finacial.api.dto.FcAccbookExtDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
/**   
 * @Title: Entity
 * @Description: 结算挂记账
 * @author huangx
 * @date 2017-09-02 15:20:33
 * @version 1.0
 *
 */
public class NisAccbookVO implements Serializable {
	/**业务单号*/
	private String billNo;
	private String billId;
	/**内部账户*/
	private FcAccDTO fcAcc;
	/**内部账号对应的银行账号*/
	private BtBankAccDTO bankAcc;
	/**收款账号（只用于内转）*/
	private String payeeAcc;
	/**金额*/
	private BigDecimal payMoney;
	/**付款方式*/
	private String voucherTypeId;
	/**期望支付日期*/
	private Date wishPayday;
	/**加急标志*/
	private String isQuick;
	/**用途*/
	private String purpose;
	/**业务来源类型*/ 
	private Integer busOriType;
	/**系统业务种类编码*/
	private String sysRaceCode;
	
	/**外单位ID */
	private String externalId;
	/**外单位账号*/
	private String externalAcc;
	/**外单位信息*/
	private String externalCorp;
	/**外单位开户行*/
	private String bank;
	/**省*/
	private String prov;
	/**市*/
	private String city;
	/**联行号*/
	private String sysBankCode;
	/**
     * 成员单位内部资金计划标识,默认为非内转
     */
    private String innerSign = BudgetConstant.INNER_SIGN_0;
	/**
	 * 账务凭证扩展属性
	 */
	private FcAccbookExtDTO bookExt;
	
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public FcAccDTO getFcAcc() {
		return fcAcc;
	}
	public void setFcAcc(FcAccDTO fcAcc) {
		this.fcAcc = fcAcc;
	}
	public BtBankAccDTO getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(BtBankAccDTO bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getVoucherTypeId() {
		return voucherTypeId;
	}
	public void setVoucherTypeId(String voucherTypeId) {
		this.voucherTypeId = voucherTypeId;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public String getIsQuick() {
		return isQuick;
	}
	public void setIsQuick(String isQuick) {
		this.isQuick = isQuick;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}
	public String getExternalCorp() {
		return externalCorp;
	}
	public void setExternalCorp(String externalCorp) {
		this.externalCorp = externalCorp;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getSysBankCode() {
		return sysBankCode;
	}
	public void setSysBankCode(String sysBankCode) {
		this.sysBankCode = sysBankCode;
	}
	public String getInnerSign() {
		return innerSign;
	}
	public void setInnerSign(String innerSign) {
		this.innerSign = innerSign;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public FcAccbookExtDTO getBookExt() {
		return bookExt;
	}
	public void setBookExt(FcAccbookExtDTO bookExt) {
		this.bookExt = bookExt;
	}
}
