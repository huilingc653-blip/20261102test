package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: DTO
 * @Description: NIS_RECEIVABLE
 * @author wangzx
 * @date 2019-07-05 11:35:12
 * @version 1.0
 *
 */
public class NisReceivableDTO implements Serializable {
	private String id;
	/**收款款单号*/
	private java.lang.String billNo;
	/**单位*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**业务类别*/
	private java.lang.String busType;
	/**业务类别名称*/
	private java.lang.String busTypeName;
	/**本方账号*/
	private java.lang.String bankAcc;
	/**本方账户名*/
	private java.lang.String accName;
	/**本方账户开户行*/
	private java.lang.String bankName;
	/**金额*/
	private java.math.BigDecimal amt;
	/**币别*/
	private String curId;
	/**币别名称*/
	private String curName;
	/**对方账号*/
	private java.lang.String oppAccNo;
	/**对方账户名*/
	private java.lang.String oppAccName;
	/**对方开户行行号*/
	private java.lang.String oppBankCode;
	/**对方账户开户行*/
	private java.lang.String oppAccBank;
	/**备注*/
	private java.lang.String rmk;
	/**单据来源 1-收款管理 2-流水认领 3-ERP系统*/
	private java.lang.String billFrom;
	/**状态*/
	private java.lang.Integer status;
	/**银行流水号*/
	private java.lang.String bankSerialNo;
	/**交易时间*/
	private java.util.Date transTime;
	/**附件ID*/
	private java.lang.String attachmentId;
	/**创建人登录名称*/
	private java.lang.String createBy;
	/**创建日期*/
	private java.util.Date createDate;
	/**更新人登录名称*/
	private java.lang.String updateBy;
	/**更新日期*/
	private java.util.Date updateDate;
	/**业务单号ID*/
	private String billId;
	/**系统业务类别*/
	private String sysBusType;
	/**关联单号*/
	private String busBillNo;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
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
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(String busTypeName) {
		this.busTypeName = busTypeName;
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
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
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
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public String getOppBankCode() {
		return oppBankCode;
	}
	public void setOppBankCode(String oppBankCode) {
		this.oppBankCode = oppBankCode;
	}
	public String getOppAccBank() {
		return oppAccBank;
	}
	public void setOppAccBank(String oppAccBank) {
		this.oppAccBank = oppAccBank;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getBillFrom() {
		return billFrom;
	}
	public void setBillFrom(String billFrom) {
		this.billFrom = billFrom;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getSysBusType() {
		return sysBusType;
	}
	public void setSysBusType(String sysBusType) {
		this.sysBusType = sysBusType;
	}
	public String getBusBillNo() {
		return busBillNo;
	}
	public void setBusBillNo(String busBillNo) {
		this.busBillNo = busBillNo;
	}
}
