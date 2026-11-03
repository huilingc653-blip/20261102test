package com.bytter.cashVoucher.vo;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**   
 * @Title: Entity
 * @Description: 凭证入库VO
 * @author luop
 * @date 2017-05-26 14:06:00
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
@ExcelTarget("BvVouInputVo")
public class BvVouInputVo extends ExcelModel implements Serializable {
	private String id;
	/**所属单位ID*/
	@BtCompositeSearch(fieldName="b.corp_id")
	private String corpId;
	/**所属库箱ID*/
	@BtCompositeSearch(fieldName="b.SBOX_ID")
	// @Dict(tableName="BV_STORAGE_BOX",valueFieldName="id",showFiledName="name",inject="storageBoxName")
	private String sboxId;
	/**所属账户*/
	@BtCompositeSearch(fieldName="b.BANK_ACC")
	@Excel(name = "所属账户", isImportField = "true")
	private String bankAcc;
	/**所属银行ID*/
	private String bankId;
	/**凭证种类ID*/
	@BtCompositeSearch(fieldName="b.VOU_TID")
	// @Dict(tableName="BV_VOUCHER_TYPE",valueFieldName="id",showFiledName="name",inject="voucherTypeName")
	private String vouTid;
	/**编码规则*/
	@NotNull(message = BlankvouConstant.BLANKVOU_BVVOUINPUT_RULENO_ISNOTNULL)
	@BtCompositeSearch(fieldName="b.rule_no")
	@Excel(name = "*编码规则", isImportField = "true")
	private String ruleNo;
	/**起始号码*/
	@NotNull(message = BlankvouConstant.BLANKVOU_BVVOUINPUT_BEGINNO_ISNOTNULL)
	@BtCompositeSearch(fieldName=" b.begin_no")
	@Excel(name = "*起始号码", isImportField = "true")
	private String beginNo;
	/**截止号码*/
	@NotNull(message = BlankvouConstant.BLANKVOU_BVVOUINPUT_ENDNO_ISNOTNULL)
	@BtCompositeSearch(fieldName="b.end_no")
	@Excel(name = "*截止号码", isImportField = "true")
	private String endNo;
	/**份数*/
	@BtCompositeSearch(fieldName="b.num_size")
	@Excel(name = "份数", isImportField = "true")
	private Integer numSize;
	/**购入日期*/
	@NotNull(message = BlankvouConstant.BLANKVOU_BVVOUINPUT_INPUTDATE_ISNOTNULL)
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="b.INPUT_DATE")
	@Excel(name = "*购入日期", isImportField = "true")
	private java.util.Date inputDate;
	/**状态 */
	// @Dict(dictGroup = "bvvouinput_status", inject = "statusName")
	private Integer status;
	@BtCompositeSearch(fieldName="b.status")
	private String statusName;
	/**备注*/
	@BtCompositeSearch(fieldName="b.rmk")
	@Excel(name = "备注", isImportField = "true")
	private String rmk;
	/**凭证种类名称*/
	@NotNull(message = BlankvouConstant.BLANKVOU_BVVOUINPUT_VOUTNAME_ISNOTNULL)
	@BtCompositeSearch(fieldName="v.name")
	@Excel(name = "*凭证种类", isImportField = "true")
	private String voucherTypeName;
	/**所属库箱名称*/
	@NotNull(message=BlankvouConstant.BLANKVOU_BVVOUINPUT_SBOXNAME_ISNOTNULL)
	@BtCompositeSearch(fieldName="c.name")
	@Excel(name = "*所属库箱", isImportField = "true")
	private String storageBoxName;
	/**单位代码*/
	@NotNull(message = BlankvouConstant.BLANKVOU_BVVOUINPUT_CORPCODE_ISNOTNULL)
	@Excel(name = "*所属单位代码", isImportField = "true")
	private String corpCode;
	/**单位名称*/
	@BtCompositeSearch(fieldName="b.corp_name")
	@Excel(name = "所属单位名称", isImportField = "true")
	private String corpName;
	/**所属银行名称*/
	@BtCompositeSearch(fieldName="b.bank_name")
	private String bankName;
	/**创建人名称*/
	private String createName;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**是否隐藏复选框 1为是*/
	private String checkboxHide;
	/** 币别代码 */
	// @Dict(tableName="BT_CURRENCY",valueFieldName="id",showFiledName="CUR_NAME",inject="curName")
	@BtCompositeSearch(fieldName="b.cur_Id")
	private String curId;
	@BtCompositeSearch(fieldName="b.cur_name")
	private String curName;
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
	public String getVouTid() {
		return vouTid;
	}
	public void setVouTid(String vouTid) {
		this.vouTid = vouTid;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	public String getBeginNo() {
		return beginNo;
	}
	public void setBeginNo(String beginNo) {
		this.beginNo = beginNo;
	}
	public String getEndNo() {
		return endNo;
	}
	public void setEndNo(String endNo) {
		this.endNo = endNo;
	}
	public Integer getNumSize() {
		return numSize;
	}
	public void setNumSize(Integer numSize) {
		this.numSize = numSize;
	}
	public java.util.Date getInputDate() {
		return inputDate;
	}
	public void setInputDate(java.util.Date inputDate) {
		this.inputDate = inputDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
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
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
		if(this.getStatus()!=null){
			if(BlankvouConstant.BVVOUINPUT_STATUS_CREATE==this.getStatus()){
				setCheckboxHide("0");
			}else{
				setCheckboxHide("1");
			}
		}
		return checkboxHide;
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
	@Override
	public String getBeanName() {
		return "bvVouInputService";
	}
	@Override
	public String toString() {
		return "BvVouInputVo [id=" + id + ", corpId=" + corpId + ", sboxId=" + sboxId + ", bankAcc=" + bankAcc
				+ ", bankId=" + bankId + ", vouTid=" + vouTid + ", ruleNo=" + ruleNo + ", beginNo=" + beginNo
				+ ", endNo=" + endNo + ", numSize=" + numSize + ", inputDate=" + inputDate + ", status=" + status
				+ ", statusName=" + statusName + ", rmk=" + rmk + ", voucherTypeName=" + voucherTypeName
				+ ", storageBoxName=" + storageBoxName + ", corpName=" + corpName + ", bankName=" + bankName
				+ ", createName=" + createName + ", createBy=" + createBy + ", createDate=" + createDate
				+ ", checkboxHide=" + checkboxHide + "]";
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
}
