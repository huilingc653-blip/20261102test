package com.bytter.cashVoucher.vo;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
/**
 * @Title: Entity
 * @Description: 凭证入库明细VO
 * @author luop
 * @date 2017-05-26 14:06:00
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BvVouInfoDtlVo implements Serializable {
	private String id;
	/**入库信息ID*/
	private String inputId;
	/**所属单位ID*/
	@BtCompositeSearch(fieldName="b.corp_id")
	private String corpId;
	/**所属库箱ID*/
	@BtCompositeSearch(fieldName="b.sbox_id")
	// @Dict(tableName="BV_STORAGE_BOX",valueFieldName="id",showFiledName="name",inject="storageBoxName")
	private String sboxId;
	/**所属账户*/
	@BtCompositeSearch(fieldName="b.bank_acc")
	private String bankAcc;
	/**所属银行ID*/
	private String bankId;
	@BtCompositeSearch(fieldName="b.bank_name")
	private String bankName;
	/**凭证种类*/
	@BtCompositeSearch(fieldName="b.vou_tid")
	// @Dict(tableName="BV_VOUCHER_TYPE",valueFieldName="id",showFiledName="name",inject="voucherTypeName")
	private String vouTid;
	/**凭证编号*/
	@BtCompositeSearch(fieldName="b.vou_no")
	private String vouNo;
	/**购入日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="b.input_date")
	private java.util.Date inputDate;
	/**凭证状态*/
	// @Dict(dictGroup = "bvvouinfodtl_voustatus", inject = "vouStatusName")
	private String vouStatus;
	@BtCompositeSearch(fieldName="b.vou_status")
	private String vouStatusName;
	/**备注*/
	private String rmk;
	/**退票或作废原因*/
	private String reason;
	/**凭证种类名称*/
	@BtCompositeSearch(fieldName="VOU_TID")
	private String voucherTypeName;
	/**所属库箱名称*/
	@BtCompositeSearch(fieldName="SBOX_ID")
	private String storageBoxName;
	/**单位代码*/
	private String corpCode;
	/**单位名称*/
	@BtCompositeSearch(fieldName="b.corp_name")
	private String corpName;
	/**创建人名称*/
	@BtCompositeSearch(fieldName="create_name")
	private String createName;
	/**创建人登录名称*/
	@BtCompositeSearch(fieldName="create_by")
	private String createBy;
	/**创建日期*/
	@BtCompositeSearch(fieldName="create_date")
	private java.util.Date createDate;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/** 币别代码 */
	// @Dict(tableName="BT_CURRENCY",valueFieldName="id",showFiledName="CUR_NAME",inject="curName")
	private String curId;
	private String curName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInputId() {
		return inputId;
	}
	public void setInputId(String inputId) {
		this.inputId = inputId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getSboxId() {
		return sboxId;
	}
	public void setSboxId(String sboxId) {
		this.sboxId = sboxId;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	public String getVouNo() {
		return vouNo;
	}
	public void setVouNo(String vouNo) {
		this.vouNo = vouNo;
	}
	public java.util.Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(java.util.Date inputDate) {
		this.inputDate = inputDate;
	}
	public String getVouStatus() {
		return vouStatus;
	}
	public void setVouStatus(String vouStatus) {
		this.vouStatus = vouStatus;
	}
	public String getVouStatusName() {
		return vouStatusName;
	}
	public void setVouStatusName(String vouStatusName) {
		this.vouStatusName = vouStatusName;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getVoucherTypeName() {
		return voucherTypeName;
	}
	public void setVoucherTypeName(String voucherTypeName) {
		this.voucherTypeName = voucherTypeName;
	}
	public String getStorageBoxName() {
		return storageBoxName;
	}
	public void setStorageBoxName(String storageBoxName) {
		this.storageBoxName = storageBoxName;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getCreateName() {
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	public String getCheckboxHide() {
		if(BlankvouConstant.BVVOUINFODTL_STATUS_SELL.equals(this.getVouStatus())||BlankvouConstant.BVVOUINFODTL_STATUS_TOVOID.equals(this.getVouStatus())){
			return "1";
		}else{
			return "0";
		}
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
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
	@Override
	public String toString() {
		return "BvVouInfoDtlVo [id=" + id + ", inputId=" + inputId + ", corpId=" + corpId + ", sboxId=" + sboxId
				+ ", bankAcc=" + bankAcc + ", bankId=" + bankId + ", vouTid=" + vouTid + ", vouNo=" + vouNo
				+ ", inputDate=" + inputDate + ", vouStatus=" + vouStatus + ", vouStatusName=" + vouStatusName
				+ ", rmk=" + rmk + ", reason=" + reason + ", voucherTypeName=" + voucherTypeName + ", storageBoxName="
				+ storageBoxName + ", corpName=" + corpName + ", createName=" + createName + ", createBy=" + createBy
				+ ", createDate=" + createDate + ", checkboxHide=" + checkboxHide + "]";
	}
}
