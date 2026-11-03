package com.bytter.biz.expense.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.NotNull;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.format.annotation.DateTimeFormat;
/**   
 * @Title: Entity
 * @Description: 费用报销VO
 * @author onlineGenerator
 * @date 2017-06-07 15:20:33
 * @version 1.0
 *
 */
@ExcelTarget("NisExpenseDetailVO")
public class NisExpenseDetailVO extends ExcelModel implements Serializable,IBusinessKey {
	private static final long serialVersionUID = -3214334829314904137L;
	@BtCompositeSearch(fieldName = "n.id")
	private String id;
	/**报销编号*/
	@BtCompositeSearch(fieldName = "n.expense_Id")
	private java.lang.String expenseId;
	/**单位*/
	@BtCompositeSearch(fieldName = "n.corp_Id")
	//@Corp(inject="corpName")
	private String corpId;
	/**单位名称*/
	@Excel(name = "*单位名称")
	@NotNull(message = "nis.netbank.message.corpname.null")
	@BtCompositeSearch(fieldName = "n.corp_Id")
	private String corpName;
	/**报账日期*/
	@Excel(name = "*报账日期",format = "yyyy-MM-dd")
	@NotNull(message = "nis.netbank.message.expenseDate.null")
	@BtCompositeSearch(fieldName = "n.expense_Date")
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date expenseDate;
	/**职工编号*/
	private java.lang.String workId;
	/**职工姓名*/
	@Excel(name = "*职工姓名")
	@NotNull(message = "nis.netbank.message.username.null")
	@BtCompositeSearch(fieldName = "n.user_Name")
	private java.lang.String userName;
	/**部门名称*/
	@Excel(name = "部门名称")
	//@NotNull(message = "nis.netbank.message.deptname.null")
	@BtCompositeSearch(fieldName = "n.dept_Name")
	private java.lang.String deptName;
	/**操作类型ID*/
	@Dict(tableName="NIS_OPERATION_TYPE",valueFieldName="id",showFiledName="OP_TYPE_NAME",inject="opTypeName")
	private java.lang.String opTypeId;
	/**操作类型*/
	@BtCompositeSearch(fieldName = "n.op_Type")
	private java.lang.String opType;
	/**操作类型名称*/
	@Excel(name = "*操作类型")
	@NotNull(message = "nis.netbank.message.optypename.null")
	@BtCompositeSearch(fieldName = "o.op_Type_Name")
	private java.lang.String opTypeName;
	/**业务类型ID*/
	//@Dict(tableName="NIS_BUSINESS_TYPE",valueFieldName="id",showFiledName="BUS_TYPE_NAME",inject="busTypeName")
	private java.lang.String busTypeId;
	/**业务类型*/
	@BtCompositeSearch(fieldName = "n.bus_Type")
	private java.lang.String busType;
	/**操作类型名称*/
	@Excel(name = "*业务类型")
	@NotNull(message = "nis.netbank.message.bustypename.null")
	@BtCompositeSearch(fieldName = "t.bus_Type_Name")
	private java.lang.String busTypeName;
	/**接口代码*/
	//@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private java.lang.String bifCode;
	@Excel(name = "接口代码名称")
	@BtCompositeSearch(fieldName = "i.name")
	private java.lang.String bifName;
	/**跨行标识*/
	private java.lang.String difBank;
	/**地域标识*/
	private java.lang.String areaSign;
	/**报账金额*/
	@Excel(name = "*报账金额", align = HorizontalAlignment.RIGHT, numFormat = "##########0.00")
	@BtCompositeSearch(fieldName = "n.expense_Money")
	private java.math.BigDecimal expenseMoney;
	/**开户行行号*/
	@Excel(name = "开户行行号")
	@BtCompositeSearch(fieldName = "n.bank_Code")
	private java.lang.String bankCode;
	/**开户银行名称*/
	@BtCompositeSearch(fieldName = "n.bank_Name")
	private java.lang.String bankName;
	/**银行卡类型*/
	@BtCompositeSearch(fieldName = "n.card_Type")
	private java.lang.String cardType;
	@Excel(name = "*账户类型")
	@NotNull(message = "nis.netbank.message.cardtypename.null")
	private java.lang.String cardTypeName;
	/**银行账号*/
	@Excel(name = "*银行账号")
	@NotNull(message = "nis.netbank.message.bankacc.null")
	@BtCompositeSearch(fieldName = "n.bank_Acc")
	private java.lang.String bankAcc;
	/**账户名*/
	@Excel(name = "*账户名")
	@NotNull(message = "nis.netbank.message.accname.null")
	@BtCompositeSearch(fieldName = "n.acc_Name")
	private java.lang.String accName;
	/**所在省*/
	//@Region(inject="provName")
	private String prov;
	@Excel(name = "*账号所在省")
	@NotNull( message = "nis.netbank.message.provname.null")
	private String provName;
	/**所在市*/
	//@Region(inject="cityName")
	private String city;
	@Excel(name = "*账号所在市")
	@NotNull( message = "nis.netbank.message.cityname.null")
	private String cityName;
	/**用途*/
	@Excel(name = "*用途")
	@NotNull( message = "nis.netbank.message.purpose.null")
	@BtCompositeSearch(fieldName = "n.purpose")
	private java.lang.String purpose;
	/**说明*/
	@Excel(name = "说明")
	@BtCompositeSearch(fieldName = "n.rmk")
	private java.lang.String rmk;
	/**付款单号*/
	@BtCompositeSearch(fieldName = "n.bill_Id")
	private java.lang.String billId;
	@BtCompositeSearch(fieldName = "b.bill_No")
	private java.lang.String billNo;
	/**单据来源*/
	@BtCompositeSearch(fieldName = "n.bill_From")
	private java.lang.String billFrom;
	/**ERP编号*/
	private java.lang.String erpId;
	/**状态*/
	@BtCompositeSearch(fieldName = "n.status")
	private java.lang.Integer status;
	/**交易状态*/
	@BtCompositeSearch(fieldName = "n.voucher_stat")
	private java.lang.String voucherStat;
	/**返回信息（交易前）*/
	@BtCompositeSearch(fieldName = "n.first_Return_Msg")
	private java.lang.String firstReturnMsg;
	/**返回信息（交易后）*/
	@BtCompositeSearch(fieldName = "n.next_Return_Msg")
	private java.lang.String nextReturnMsg;
	/**返回时间*/
	@BtCompositeSearch(fieldName = "n.return_Time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private java.util.Date returnTime;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
	/**附件ID*/
	private String attachmentId;
	/**计划科目*/
	private String[] itemCode;
	/**计划科目名称*/
	private String[] itemName;
	/**执行金额*/
	private BigDecimal[] processMoney;
	/**汇率*/
	private BigDecimal[] exchangeRate;
	/**执行金额（折合本身币别）*/
	private BigDecimal[] processCurMoney;
	@Excel(name = "*计划科目")
	private String itemCodeImp;
	/**指令编号*/
	private String voucherNo;
	/**指令表的交易状态*/
	private String excVoucherStat;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(java.lang.String expenseId) {
		this.expenseId = expenseId;
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
	public java.util.Date getExpenseDate() {
		return expenseDate;
	}
	public void setExpenseDate(java.util.Date expenseDate) {
		this.expenseDate = expenseDate;
	}
	public java.lang.String getWorkId() {
		return workId;
	}
	public void setWorkId(java.lang.String workId) {
		this.workId = workId;
	}
	public java.lang.String getUserName() {
		return userName;
	}
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}
	public java.lang.String getDeptName() {
		return deptName;
	}
	public void setDeptName(java.lang.String deptName) {
		this.deptName = deptName;
	}
	public String getOpTypeId() {
		return opTypeId;
	}
	public void setOpTypeId(String opTypeId) {
		this.opTypeId = opTypeId;
	}
	public java.lang.String getOpType() {
		return opType;
	}
	public void setOpType(java.lang.String opType) {
		this.opType = opType;
	}
	public java.lang.String getOpTypeName() {
		return opTypeName;
	}
	public void setOpTypeName(java.lang.String opTypeName) {
		this.opTypeName = opTypeName;
	}
	public String getBusTypeId() {
		return busTypeId;
	}
	public void setBusTypeId(String busTypeId) {
		this.busTypeId = busTypeId;
	}
	public java.lang.String getBusType() {
		return busType;
	}
	public void setBusType(java.lang.String busType) {
		this.busType = busType;
	}
	public java.lang.String getBusTypeName() {
		return busTypeName;
	}
	public void setBusTypeName(java.lang.String busTypeName) {
		this.busTypeName = busTypeName;
	}
	public java.lang.String getBifCode() {
		return bifCode;
	}
	public void setBifCode(java.lang.String bifCode) {
		this.bifCode = bifCode;
	}
	public java.lang.String getDifBank() {
		return difBank;
	}
	public void setDifBank(java.lang.String difBank) {
		this.difBank = difBank;
	}
	public java.lang.String getAreaSign() {
		return areaSign;
	}
	public void setAreaSign(java.lang.String areaSign) {
		this.areaSign = areaSign;
	}
	public java.math.BigDecimal getExpenseMoney() {
		return expenseMoney;
	}
	public void setExpenseMoney(java.math.BigDecimal expenseMoney) {
		this.expenseMoney = expenseMoney;
	}
	public java.lang.String getBankCode() {
		return bankCode;
	}
	public void setBankCode(java.lang.String bankCode) {
		this.bankCode = bankCode;
	}
	public java.lang.String getBankName() {
		return bankName;
	}
	public void setBankName(java.lang.String bankName) {
		this.bankName = bankName;
	}
	public java.lang.String getCardType() {
		return cardType;
	}
	public void setCardType(java.lang.String cardType) {
		this.cardType = cardType;
	}
	public java.lang.String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(java.lang.String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public java.lang.String getAccName() {
		return accName;
	}
	public void setAccName(java.lang.String accName) {
		this.accName = accName;
	}
	public String getProv() {
		return prov;
	}
	public void setProv(String prov) {
		this.prov = prov;
	}
	public String getProvName() {
		return provName;
	}
	public void setProvName(String provName) {
		this.provName = provName;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public java.lang.String getPurpose() {
		return purpose;
	}
	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}
	public java.lang.String getRmk() {
		return rmk;
	}
	public void setRmk(java.lang.String rmk) {
		this.rmk = rmk;
	}
	public java.lang.String getBillId() {
		return billId;
	}
	public void setBillId(java.lang.String billId) {
		this.billId = billId;
	}
	public java.lang.String getBillNo() {
		return billNo;
	}
	public void setBillNo(java.lang.String billNo) {
		this.billNo = billNo;
	}
	public java.lang.String getBillFrom() {
		return billFrom;
	}
	public void setBillFrom(java.lang.String billFrom) {
		this.billFrom = billFrom;
	}
	public java.lang.String getErpId() {
		return erpId;
	}
	public void setErpId(java.lang.String erpId) {
		this.erpId = erpId;
	}
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public java.lang.String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(java.lang.String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public java.lang.String getFirstReturnMsg() {
		return firstReturnMsg;
	}
	public void setFirstReturnMsg(java.lang.String firstReturnMsg) {
		this.firstReturnMsg = firstReturnMsg;
	}
	public java.lang.String getNextReturnMsg() {
		return nextReturnMsg;
	}
	public void setNextReturnMsg(java.lang.String nextReturnMsg) {
		this.nextReturnMsg = nextReturnMsg;
	}
	public java.util.Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(java.util.Date returnTime) {
		this.returnTime = returnTime;
	}
	public java.lang.String getBifName() {
		return bifName;
	}
	public void setBifName(java.lang.String bifName) {
		this.bifName = bifName;
	}
	public java.lang.String getCardTypeName() {
		return cardTypeName;
	}
	public void setCardTypeName(java.lang.String cardTypeName) {
		this.cardTypeName = cardTypeName;
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
	@Override
	public String getBusinessKey() {
		return getId();
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
		return processMoney;
	}
	public void setProcessMoney(BigDecimal[] processMoney) {
		this.processMoney = processMoney;
	}
	public BigDecimal[] getExchangeRate() {
		return exchangeRate;
	}
	public void setExchangeRate(BigDecimal[] exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
	public BigDecimal[] getProcessCurMoney() {
		return processCurMoney;
	}
	public void setProcessCurMoney(BigDecimal[] processCurMoney) {
		this.processCurMoney = processCurMoney;
	}
	@Override
	public String getBeanName() {
		return "nisExpenseDetailService";
	}
	public String getItemCodeImp() {
		return itemCodeImp;
	}
	public void setItemCodeImp(String itemCodeImp) {
		this.itemCodeImp = itemCodeImp;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getExcVoucherStat() {
		return excVoucherStat;
	}
	public void setExcVoucherStat(String excVoucherStat) {
		this.excVoucherStat = excVoucherStat;
	}
}
