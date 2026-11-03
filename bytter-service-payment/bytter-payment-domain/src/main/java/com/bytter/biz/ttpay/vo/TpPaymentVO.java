package com.bytter.biz.ttpay.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.NumberFormat;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: VO
 * @Description: 第三方支付VO类
 * @author chenf
 * @date 2022-5-9 15:43:05
 * @version 1.0
 */
public class TpPaymentVO extends ExcelModel implements Serializable,  IBusinessKey {
	private static final long serialVersionUID = 1L;
	private String id;
	/**付款单号*/
	@BtCompositeSearch(fieldName = "n.bill_No")
	private String billNo;
	/**单位*/
	@BtCompositeSearch(fieldName = "n.corp_Id")
	@Corp(inject="corpName")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName = "n.corp_Id")
	@Excel(name = "*单位名称")
	@NotNull( message = "nis.netbank.message.corpname.null")
	private String corpName;
	@Excel(name = "*支付商户号")
	@BtCompositeSearch(fieldName = "n.payer_Acc")
	private String payerAcc;
	/**金额*/
	@Excel(name = "*金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	@BtCompositeSearch(fieldName = "n.pay_Money")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal payMoney;
	/**收款商户号*/
	@Excel(name = "*收款商户号")
	@NotNull( message = "nis.netbank.message.payeeacc.null")
	@BtCompositeSearch(fieldName = "n.payee_Acc")
	private String payeeAcc;
	/**收款商户名称*/
	@Excel(name = "*收款商户名称")
	@NotNull( message = "nis.netbank.message.payeeaccname.null")
	@BtCompositeSearch(fieldName = "n.payee_Acc_Name")
	private String payeeAccName;
	/**支付方式**/
	@BtCompositeSearch(fieldName = "n.pay_type")
	//@Dict(dictGroup = "TTPAY_PAY_TYPE", inject = "payTypeName")
	private String payType;
	/**支付方式**/
	@Excel(name = "*支付方式")
	@NotNull( message = "ttpay.message.paytype.null")
	private String payTypeName;
	@Excel(name = "*期望支付日期",format = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "n.wish_Payday")
	@JsonFormat(pattern="yyyy-MM-dd")
	@NotNull( message = "nis.capitalRecord.message.wishPayDay.isnull")
	private Date wishPayday;
	@Excel(name = "*是否对私付款", replace={"否_0","是_1"})
	@NotNull( message = "nis.netbank.message.isforindividual.null")
	//@Dict(dictGroup = "nisBillhead.isPayed", inject = "isForindividualName")
	@BtCompositeSearch(fieldName = "n.is_Forindividual")
	private String isForindividual;
	private String isForindividualName;
	/**业务类别*/
	@BtCompositeSearch(fieldName = "n.bus_Type")
	private String busType;
	/**业务类别名称*/
	@Excel(name = "业务类别")
	@BtCompositeSearch(fieldName = "t.bus_Type_Name")
	@NotNull( message = "nis.netbank.message.bustype.null")
	private String busTypeName;
	/**附言*/
	@Excel(name = "附言")
	@BtCompositeSearch(fieldName = "n.add_Word")
	private String addWord;
	/**业务标题*/
	@Excel(name = "业务标题")
	@NotNull( message = "ttpay.message.busTitle.null")
	private String busTitle;
	/**状态*/
	@Dict(dictGroup = "nisBillhead.status", inject = "statusName")
	@BtCompositeSearch(fieldName = "n.status")
	private Integer status;
	@BtCompositeSearch(fieldName = "n.status")
	private String statusName;
	/**创建人登录名称*/
	@BtCompositeSearch(fieldName = "n.create_by")
	private String createBy;
	@BtCompositeSearch(fieldName = "n.create_username")
	private String createUserName;
	/**创建日期*/
	@BtCompositeSearch(fieldName = "n.create_Date")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**附件ID*/
	private String attachmentId;
	/**备注说明*/
	private String rmk;
	/**商户订单号**/
	private String trandNo;
	/**币别*/
	@BtCompositeSearch(fieldName = "n.cur_Id")
	@Currency(inject="curName")
	private String curId;
	/**币别名称*/
	@BtCompositeSearch(fieldName = "n.cur_Id")
	private String curName;
	/**挂预算科目 code**/
	private String[] itemCode;
	/**挂预算科目 名称**/
	private String[] itemName;
	private BigDecimal[] ProcessMoney;
	private String timeStamp;
	private String isBudget;
	@Excel(name = "*计划科目")
	private String itemCodeImp;
	/**交易状态*/
	//@Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
	@BtCompositeSearch(fieldName = "n.voucher_Stat")
	private String voucherStat;
	private String voucherStatName;
	/**是否批量操作*/
	private Boolean isBatch;
	/**
	 * 交易日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date transTime;
	/**
	 * 账户余额
	 */
	private BigDecimal balance;
	/**
	 * 可用余额
	 */
	private BigDecimal availBal;
	/**收款账户类型 apple_id、手机号、邮箱**/
	@Dict(dictGroup = "ttpay.payeeMode", inject = "payeeModeName")
	private String payeeMode;
	@Excel(name = "*收款账户类型")
	private String payeeModeName;
	/**付款用途**/
	@Excel(name = "*用途")
	@NotNull( message = "nis.netbank.message.purpose.null")
	@BtCompositeSearch(fieldName = "n.purpose")
	private String purpose;
	/**银行交易订单号**/
	private String bankOrderNo;
	/**银行交易付款流水号**/
	private String bankPayNo;
	/**银行返回消息**/
	private String returnMsg;
	/**银行返回消息时间**/
	private Date returnTime;
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
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayeeAcc() {
		return payeeAcc;
	}
	public void setPayeeAcc(String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	public String getPayeeAccName() {
		return payeeAccName;
	}
	public void setPayeeAccName(String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public String getIsForindividual() {
		return isForindividual;
	}
	public void setIsForindividual(String isForindividual) {
		this.isForindividual = isForindividual;
	}
	public String getIsForindividualName() {
		return isForindividualName;
	}
	public void setIsForindividualName(String isForindividualName) {
		this.isForindividualName = isForindividualName;
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
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	public String getBusTitle() {
		return busTitle;
	}
	public void setBusTitle(String busTitle) {
		this.busTitle = busTitle;
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
	public String getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getTrandNo() {
		return trandNo;
	}
	public void setTrandNo(String trandNo) {
		this.trandNo = trandNo;
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
	public String[] getItemCode() {
		return itemCode;
	}
	public void setItemCode(String[] itemCode) {
		this.itemCode = itemCode;
	}
	public String[] getItemName() {
		return itemName;
	}
	public void setItemName(String[] itemName) {
		this.itemName = itemName;
	}
	public BigDecimal[] getProcessMoney() {
		return ProcessMoney;
	}
	public void setProcessMoney(BigDecimal[] processMoney) {
		ProcessMoney = processMoney;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getIsBudget() {
		return isBudget;
	}
	public void setIsBudget(String isBudget) {
		this.isBudget = isBudget;
	}
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public String getVoucherStatName() {
		return voucherStatName;
	}
	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
	}
	public Boolean getBatch() {
		return isBatch;
	}
	public void setBatch(Boolean batch) {
		isBatch = batch;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getAvailBal() {
		return availBal;
	}
	public void setAvailBal(BigDecimal availBal) {
		this.availBal = availBal;
	}
	public String getPayeeMode() {
		return payeeMode;
	}
	public void setPayeeMode(String payeeMode) {
		this.payeeMode = payeeMode;
	}
	public String getBankOrderNo() {
		return bankOrderNo;
	}
	public void setBankOrderNo(String bankOrderNo) {
		this.bankOrderNo = bankOrderNo;
	}
	public String getBankPayNo() {
		return bankPayNo;
	}
	public void setBankPayNo(String bankPayNo) {
		this.bankPayNo = bankPayNo;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getCreateUserName() {
		return createUserName;
	}
	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	public String getItemCodeImp() {
		return itemCodeImp;
	}
	public void setItemCodeImp(String itemCodeImp) {
		this.itemCodeImp = itemCodeImp;
	}
	public String getPayeeModeName() {
		return payeeModeName;
	}
	public void setPayeeModeName(String payeeModeName) {
		this.payeeModeName = payeeModeName;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	@Override
	public String getBusinessKey() {
		return getId();
	}
	@Override
	public String getBeanName() {
		return "tpPaymentService";
	}
}
