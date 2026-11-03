package com.bytter.biz.netbank.vo;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author wangzx
 * @Description 委托收款
 * @date 2017.12.14
 */
@SuppressWarnings("serial")
@ExcelTarget("CgsBillVO")
public class CgsBillVO extends ExcelModel implements Serializable,IBusinessKey {
	private String id;
	/**委托日期*/
	@BtCompositeSearch(fieldName = "c.consign_Date")
	@Excel(name = "*托收日期")
	@NotNull(message = "nis.cgsbill.message.consigndate.null")
	@JsonFormat(pattern="yyyy-MM-dd")
	private java.util.Date consignDate;
	/**委托号码*/
	@BtCompositeSearch(fieldName = "c.consign_Code")
	private java.lang.String consignCode;
	@Excel(name = "*付款单位")
	@NotNull(message = "nis.cgsbill.message.payercorpid.null")
	private String payerCorpName;
	/**付款账号*/
	private java.lang.String payerAcc;
	/**付款单位ID*/
	@BtCompositeSearch(fieldName = "c.payer_Corp_Id")
	@Corp(inject="payerCorpName")
	private java.lang.String payerCorpId;
	/**付款账号名称*/
	private java.lang.String payerAccName;
	/**收款单位ID*/
	@BtCompositeSearch(fieldName = "c.payee_Corp_Id")
	@Corp(inject="payeeCorpName")
	private java.lang.String payeeCorpId;
	@Excel(name = "*收款单位")
	@NotNull(message = "nis.cgsbill.message.payercorpid.null")
	private String payeeCorpName;
	/**收款账号*/
	@BtCompositeSearch(fieldName = "c.payee_Acc")
	@Excel(name = "*收款账号")
	@NotNull(message = "nis.cgsbill.message.payeeAcc.null")
	private java.lang.String payeeAcc;
	/**收款账号名称*/
	private java.lang.String payeeAccName;
	/**金额*/
	@BtCompositeSearch(fieldName = "c.money")
	@Excel(name = "*金额",numFormat = "##########.0")
	@NotNull(message = "nis.cgsbill.message.money.null")
	private java.math.BigDecimal money;
	/**合同号*/
	@BtCompositeSearch(fieldName = "c.pact_Code")
	@Excel(name = "合同号")
	private java.lang.String pactCode;
	/**发票号*/
	@Excel(name = "发票号")
	private java.lang.String invoiceCode;
	/**摘要*/
	@Excel(name = "摘要")
	@BtCompositeSearch(fieldName = "c.abs")
	private java.lang.String abs;
	/**结算中心手续费*/
	private java.math.BigDecimal chargeMoney;
	/**状态*/
	@Dict(dictGroup = "cgsBill.status", inject = "statusName")
	@BtCompositeSearch(fieldName = "c.status")
	private java.lang.Integer status;
	private String statusName;
	
	private String createBy;
	private String attachmentId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.util.Date getConsignDate() {
		return consignDate;
	}
	public void setConsignDate(java.util.Date consignDate) {
		this.consignDate = consignDate;
	}
	public java.lang.String getConsignCode() {
		return consignCode;
	}
	public void setConsignCode(java.lang.String consignCode) {
		this.consignCode = consignCode;
	}
	public java.lang.String getPayerCorpId() {
		return payerCorpId;
	}
	public void setPayerCorpId(java.lang.String payerCorpId) {
		this.payerCorpId = payerCorpId;
	}
	public String getPayerCorpName() {
		return payerCorpName;
	}
	public void setPayerCorpName(String payerCorpName) {
		this.payerCorpName = payerCorpName;
	}
	public java.lang.String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(java.lang.String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public java.lang.String getPayerAccName() {
		return payerAccName;
	}
	public void setPayerAccName(java.lang.String payerAccName) {
		this.payerAccName = payerAccName;
	}
	public java.lang.String getPayeeCorpId() {
		return payeeCorpId;
	}
	public void setPayeeCorpId(java.lang.String payeeCorpId) {
		this.payeeCorpId = payeeCorpId;
	}
	public String getPayeeCorpName() {
		return payeeCorpName;
	}
	public void setPayeeCorpName(String payeeCorpName) {
		this.payeeCorpName = payeeCorpName;
	}
	public java.lang.String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(java.lang.String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public java.lang.String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(java.lang.String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public java.math.BigDecimal getMoney() {
		return money;
	}
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	public java.lang.String getPactCode() {
		return pactCode;
	}
	public void setPactCode(java.lang.String pactCode) {
		this.pactCode = pactCode;
	}
	public java.lang.String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(java.lang.String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public java.lang.String getAbs() {
		return abs;
	}
	public void setAbs(java.lang.String abs) {
		this.abs = abs;
	}
	public java.math.BigDecimal getChargeMoney() {
		return chargeMoney;
	}
	public void setChargeMoney(java.math.BigDecimal chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	@Override
	public String getBeanName() {
		return "cgsBillService";
	}
}
