package com.bytter.cashVoucher.vo;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: BvPayeeChequeDao  
 * @Description: 收款支票对应VO对象
 * @author Jinyong.Cao
 * @date 2018-06-13 13:17:17
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@Data
@ToString
public class BvPayeeChequeVO extends ExcelModel implements Serializable {
	private String id;
	/**所属单位ID*/
	@BtCompositeSearch(fieldName="bpc.corp_id")
	private String corpId;
	/**单位代码*/
	@Excel(name = "*所属单位代码",isImportField="true")
	@NotNull(message = BlankvouConstant.BLANKVOU_BVVOUINPUT_CORPCODE_ISNOTNULL)
	private String corpCode;
	/**所属单位名称*/
	@BtCompositeSearch(fieldName="bpc.corp_name")
	private String corpName;
	/**票据号码*/
	@Excel(name = "*票据（包）号码",isImportField="true")
	@BtCompositeSearch(fieldName="bpc.orig_no")
	private String origNo;
	/**票据金额*/
	@Excel(name = "*票据金额",isImportField="true")
	@BtCompositeSearch(fieldName="bpc.bill_money")
	private BigDecimal billMoney;

	private String billMoneyFormatError;
	/**购入日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="bpc.INPUT_DATE")
	@Excel(name = "*购入日期",isImportField="true")
	private Date inputDate;

	private String inputDateFormatError;
	/**付款人*/
	@BtCompositeSearch(fieldName="bpc.payer")
	@Excel(name = "*付款人",isImportField="true")
	private String payer;
	/**付款人账户*/
	@BtCompositeSearch(fieldName="bpc.payer_acc")
	@Excel(name = "*付款人账户",isImportField="true")
	private String payerAcc;
	/**付款人银行账户*/
//	@Excel(name = "*付款人银行账户",isImportField="true")
	private String payerBankNo;
	/**付款人开户行名称*/
	@Excel(name = "*付款人开户行名称",isImportField="true")
	private String payerBankName;
	/**收款人*/
	@BtCompositeSearch(fieldName="bpc.payee")
	@Excel(name = "*收款人",isImportField="true")
	private String payee;
	/**收款人账户*/
	@BtCompositeSearch(fieldName="bpc.payee_acc")
	@Excel(name = "*收款人账户",isImportField="true")
	private String payeeAcc;
	/**收款人银行账户*/
//	@Excel(name = "*收款人银行账户",isImportField="true")
	private String payeeBankNo;
	/**收款人开户行名称*/
	@Excel(name = "*收款人开户行名称",isImportField="true")
	private String payeeBankName;
	/**状态 */
	// @Dict(dictGroup = "BV_PAYEECHEQUE_STATUS", inject = "statusName")
	@BtCompositeSearch(fieldName="bpc.status")
	private Integer status;
	private String statusName;
	/**用途*/
	@Excel(name = "用途",isImportField="false")
	private String uses;
	/** 币别代码 */
	@Dict(tableName="BT_CURRENCY",valueFieldName="id",showFiledName="CUR_NAME",inject="curName")
	private String curId;
	private String curName;
	/**汇率*/
	private BigDecimal curExrate;
	/**折合本位币*/
	private BigDecimal nativeCreditMoney;
	/**开票日期*/
	private Date startDate;
	/**到期日期*/
	private Date endDate;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
//	public String getCorpId() {
//		return corpId;
//	}
//	public void setCorpId(String corpId) {
//		this.corpId = corpId;
//	}
//	public String getCorpName() {
//		return corpName;
//	}
//	public void setCorpName(String corpName) {
//		this.corpName = corpName;
//	}
//	public String getCorpCode() {
//		return corpCode;
//	}
//	public void setCorpCode(String corpCode) {
//		this.corpCode = corpCode;
//	}
//	public String getOrigNo() {
//		return origNo;
//	}
//	public void setOrigNo(String origNo) {
//		this.origNo = origNo;
//	}
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
		this.setNativeCredit();
	}
//	public Date getInputDate() {
//		return inputDate;
//	}
//	public void setInputDate(Date inputDate) {
//		this.inputDate = inputDate;
//	}
//	public String getPayer() {
//		return payer;
//	}
//	public void setPayer(String payer) {
//		this.payer = payer;
//	}
//	public String getPayerAcc() {
//		return payerAcc;
//	}
//	public void setPayerAcc(String payerAcc) {
//		this.payerAcc = payerAcc;
//	}
//	public String getPayerBankNo() {
//		return payerBankNo;
//	}
//	public void setPayerBankNo(String payerBankNo) {
//		this.payerBankNo = payerBankNo;
//	}
//	public String getPayerBankName() {
//		return payerBankName;
//	}
//	public void setPayerBankName(String payerBankName) {
//		this.payerBankName = payerBankName;
//	}
//	public String getPayee() {
//		return payee;
//	}
//	public void setPayee(String payee) {
//		this.payee = payee;
//	}
//	public String getPayeeBankNo() {
//		return payeeBankNo;
//	}
//	public void setPayeeBankNo(String payeeBankNo) {
//		this.payeeBankNo = payeeBankNo;
//	}
//	public String getPayeeBankName() {
//		return payeeBankName;
//	}
//	public void setPayeeBankName(String payeeBankName) {
//		this.payeeBankName = payeeBankName;
//	}
//	public Integer getStatus() {
//		return status;
//	}
//	public void setStatus(Integer status) {
//		this.status = status;
//	}
//	public String getUse() {
//		return use;
//	}
//	public void setUse(String use) {
//		this.use = use;
//	}
//	public String getPayeeAcc() {
//		return payeeAcc;
//	}
//	public void setPayeeAcc(String payeeAcc) {
//		this.payeeAcc = payeeAcc;
//	}
//	public String getStatusName() {
//		return statusName;
//	}
//	public void setStatusName(String statusName) {
//		this.statusName = statusName;
//	}
//	public String getCurId() {
//		return curId;
//	}
//	public void setCurId(String curId) {
//		this.curId = curId;
//	}
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
		this.setNativeCredit();
	}
//	public BigDecimal getNativeCreditMoney() {
//		return nativeCreditMoney;
//	}
//	public void setNativeCreditMoney(BigDecimal nativeCreditMoney) {
//		this.nativeCreditMoney = nativeCreditMoney;
//	}
//	public String getCurName() {
//		return curName;
//	}
//	public void setCurName(String curName) {
//		this.curName = curName;
//	}
//	public Date getStartDate() {
//		return startDate;
//	}
//	public void setStartDate(Date startDate) {
//		this.startDate = startDate;
//	}
//	public Date getEndDate() {
//		return endDate;
//	}
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
//	public String getCheckboxHide() {
//		return checkboxHide;
//	}
//	public void setCheckboxHide(String checkboxHide) {
//		this.checkboxHide = checkboxHide;
//	}
	@Override
	public String getBeanName() {
		return "bvPayeeChequeService";
	}
//	@Override
//	public String toString() {
//		return "BvPayeeChequeVO [id=" + id + ", corpId=" + corpId
//				+ ", corpCode=" + corpCode + ", corpName=" + corpName
//				+ ", origNo=" + origNo + ", billMoney=" + billMoney
//				+ ", inputDate=" + inputDate + ", payer=" + payer
//				+ ", payerAcc=" + payerAcc + ", payerBankNo=" + payerBankNo
//				+ ", payerBankName=" + payerBankName + ", payee=" + payee
//				+ ", payeeAcc=" + payeeAcc + ", payeeBankNo=" + payeeBankNo
//				+ ", payeeBankName=" + payeeBankName + ", status=" + status
//				+ ", statusName=" + statusName + ", use=" + use
//				+ ", checkboxHide=" + checkboxHide + "]";
//	}
	/**设置折合本位比*/
	private void setNativeCredit(){
		// 折合本位币
		if(StringUtils.isNotEmpty(curExrate) && StringUtils.isNotEmpty(billMoney)){
			this.setNativeCreditMoney(billMoney.multiply(curExrate));
		}
	}
}
