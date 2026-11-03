package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 资金调拨表
 *
 * @author zuoy
 * @date 2017-06-02
 */
@Data
public class NisAllocationReqListDTO implements Serializable,  IBusinessKey {
	private String id;
	/**
	 * 单据号码
	 */
	@BtCompositeSearch(fieldName = "ar.bill_No")
	private java.lang.String billNo;
	/**
	 * 单位ID
	 */
	@BtCompositeSearch(fieldName = "ar.corp_Id")
	private java.lang.String corpId;
	/**
	 * 单位名称
	 */
	@BtCompositeSearch(fieldName = "ar.corp_Id")
	private java.lang.String corpName;
	/**
	 * 资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
	 */
	@BtCompositeSearch(fieldName = "ar.type")
	private java.lang.Integer type;
	private java.lang.String typeName;
	/**
	 * 付方账号ID
	 */
	@BtCompositeSearch(fieldName = "ar.payer_acc_id")
	private java.lang.String payerAccId;
	/**
	 * 付方账号
	 */
	@BtCompositeSearch(fieldName = "ar.payer_acc")
	private java.lang.String payerAcc;
	/**
	 * 付方账号名称(付方户名)
	 */
	@BtCompositeSearch(fieldName = "ar.payer_acc_name")
	private java.lang.String payerAccName;
	/**
	 * 付方开户行
	 */
	@BtCompositeSearch(fieldName = "ba.bank_name")
	private java.lang.String payerBankName;
	/**
	 * 收方账号ID
	 */
	@BtCompositeSearch(fieldName = "ar.payee_acc_id")
	private java.lang.String payeeAccId;
	/**
	 * 收方账号
	 */
	@BtCompositeSearch(fieldName = "ar.payee_acc")
	private java.lang.String payeeAcc;
	/**
	 * 收方账号名称(收方户名)
	 */
	@BtCompositeSearch(fieldName = "ar.payee_acc_name")
	private java.lang.String payeeAccName;
	/**
	 * 收方开户行
	 */
	@BtCompositeSearch(fieldName = "acc.bank_name")
	private java.lang.String payeeBankName;
	/**
	 * 币别Id
	 */
	@BtCompositeSearch(fieldName = "ar.cur_id")
	private java.lang.String curId;
	/**
	 * 币别
	 */
	@BtCompositeSearch(fieldName = "ar.cur_id")
	private java.lang.String curName;
	/**
	 * 用途
	 */
	@BtCompositeSearch(fieldName = "ar.purpose")
	private java.lang.String purpose;
	/**
	 * 金额
	 */
	@BtCompositeSearch(fieldName = "ar.money")
	private java.math.BigDecimal money;
	/**
	 * 期望支付日期
	 */
	@BtCompositeSearch(fieldName = "ar.wish_payday")
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date wishPayday;
	/**
	 * 加急标志 1-是 0-否
	 */
	private java.lang.String isQuick;
	/**
	 * 跨行标识 0-同行 1-他行
	 */
	private java.lang.String difBank;
	/**
	 * 地域标识 0-同城 1-异地
	 */
	private java.lang.String areaSign;
	/**
	 * 创建人登录名称
	 */
	@BtCompositeSearch(fieldName = "ar.create_by")
	private java.lang.String createBy;

	@BtCompositeSearch(fieldName = "user.USERNAME")
	private String createByName;
	/**
	 * 创建日期
	 */
	@BtCompositeSearch(fieldName = "ar.create_date")
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date createDate;
	/**
	 * 更新人登录名称
	 */
	private java.lang.String updateBy;
	/**
	 * 更新日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private java.util.Date updateDate;
	/**
	 * 状态
	 */
	@BtCompositeSearch(fieldName = "ar.status")
	private java.lang.Integer status;
	private java.lang.String statusName;
	/**
	 * 资金池标识 0-非资金池；1-资金池
	 */
	private java.lang.String poolSign;
	private List<Map<String, Object>> nisAllocationList;
	/**
	 * 计划科目
	 */
	private String[] itemCode;
	/**
	 * 计划科目名称
	 */
	private String[] itemName;
	/**
	 * 执行金额
	 */
	private BigDecimal[] processMoney;
	/**
	 * 汇率
	 */
	private BigDecimal[] exchangeRate;
	/**
	 * 执行金额（折合本身币别
	 */
	private BigDecimal[] processCurMoney;
	/**
	 * 银行账户余额
	 */
	private BigDecimal bankAccBalance;
	/**
	 * 附件ID
	 */
	private java.lang.String attachmentId;
	/**
	 * 付方账号单位ID
	 */
	private java.lang.String payerCorpId;
	/**
	 * 收方账号单位ID
	 */
	private java.lang.String payeeCorpId;
	/**
	 * 付方账号单位
	 */
	private java.lang.String payerCorpName;
	/**
	 * 收方账号单位
	 */
	private java.lang.String payeeCorpName;
	/**
	 * 付方账户账户余额
	 */
	private BigDecimal payerAccBalance;

	/**
	 * 付方账户 内部账户余额
	 */
	private BigDecimal payerFcAccBalance;

	/**
	 * 收方账号账户余额
	 */
	private BigDecimal payeeAccBalance;

	/**
	 * 收方账户 内部账户余额
	 */
	private BigDecimal payeeFcAccBalance;

	/**证书信息*/
	private String caInfoId;
	/**产生编码日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
    private Date encryDate;
    /**加密随机数*/
    private String random;
    /**编码源*/
    private String encrySource;
    /**CA编码*/
    private String caCoding;

    /**交易状态*/
	@BtCompositeSearch(fieldName = "ar.voucher_Stat")
	private String voucherStat;
	private String voucherStatName;
	/**付款指令ID*/
	private String bisExcId;
	/**收款指令交易状态*/
	@BtCompositeSearch(fieldName = "g.cmd_Stat")
	private String cmdStat;
	/**收款指令ID*/
	private String bisGatherCmdId;
	/**单据来源*/
	@BtCompositeSearch(fieldName = "ar.bill_from")
	private String billFrom;
	/**ERP编号*/
	@BtCompositeSearch(fieldName = "ar.erp_id")
	private String erpId;
	/**记账标志*/
	private java.lang.String tallySign;
	/**记账员代码*/
	private java.lang.String tallyCode;
	/**凭证流水号*/
	private java.lang.String orderNo;
	/**记账凭证号码*/
	private java.lang.String voucherNo;
	/**时间戳*/
	private String timeStamp;
	/**
	 * 会计凭证号
	 */
	private String bursarBillNo;
	/**
	 * 记账日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date tallyDate;
	/**
	 * 交易日期
	 */
    @JsonFormat(pattern = "yyyy-MM-dd")
	private Date transTime;
    /**备注说明*/
    private String rmk;
	/**收款账号对应的中心账号*/
	private String payeeCounterAcc;
	/**付款账号对应的中心账号*/
	private String payerCounterAcc;
	/**收款账号联行号*/
	private String payeeBankCode;
	/**收款账号银行类型*/
	private String payeeBankTypeId;
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public List<Map<String, Object>> getNisAllocationList() {
		return nisAllocationList;
	}
	public void setNisAllocationList(List<Map<String, Object>> nisAllocationList) {
		this.nisAllocationList = nisAllocationList;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public java.lang.String getBillNo() {
		return billNo;
	}
	public void setBillNo(java.lang.String billNo) {
		this.billNo = billNo;
	}
	public java.lang.String getCorpId() {
		return corpId;
	}
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	public java.lang.String getCorpName() {
		return corpName;
	}
	public void setCorpName(java.lang.String corpName) {
		this.corpName = corpName;
	}
	public java.lang.Integer getType() {
		return type;
	}
	public void setType(java.lang.Integer type) {
		this.type = type;
	}
	public java.lang.String getPayerAccId() {
		return payerAccId;
	}
	public void setPayerAccId(java.lang.String payerAccId) {
		this.payerAccId = payerAccId;
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
	public java.lang.String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(java.lang.String payerBankName) {
		this.payerBankName = payerBankName;
	}
	public java.lang.String getPayeeAccId() {
		return payeeAccId;
	}
	public void setPayeeAccId(java.lang.String payeeAccId) {
		this.payeeAccId = payeeAccId;
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
	public java.lang.String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(java.lang.String payeeBankName) {
		this.payeeBankName = payeeBankName;
	}
	public java.lang.String getCurId() {
		return curId;
	}
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	public java.lang.String getCurName() {
		return curName;
	}
	public void setCurName(java.lang.String curName) {
		this.curName = curName;
	}
	public java.lang.String getPurpose() {
		return purpose;
	}
	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}
	public java.math.BigDecimal getMoney() {
		return money;
	}
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	public java.util.Date getWishPayday() {
		return wishPayday;
	}
	public void setWishPayday(java.util.Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	public java.lang.String getIsQuick() {
		return isQuick;
	}
	public void setIsQuick(java.lang.String isQuick) {
		this.isQuick = isQuick;
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
	public java.lang.String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(java.util.Date createDate) {
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
	public java.lang.Integer getStatus() {
		return status;
	}
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	public BigDecimal getBankAccBalance() {
		return bankAccBalance;
	}
	public void setBankAccBalance(BigDecimal bankAccBalance) {
		this.bankAccBalance = bankAccBalance;
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
	public String getBusinessKey() {
		return getId();
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  附件ID
	 */
	public java.lang.String getAttachmentId() {
		return attachmentId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  附件ID
	 */
	public void setAttachmentId(java.lang.String attachmentId) {
		this.attachmentId = attachmentId;
	}
	public String getPayerCorpId() {
		return payerCorpId;
	}
	public void setPayerCorpId(String payerCorpId) {
		this.payerCorpId = payerCorpId;
	}
	public String getPayeeCorpId() {
		return payeeCorpId;
	}
	public void setPayeeCorpId(String payeeCorpId) {
		this.payeeCorpId = payeeCorpId;
	}
	public String getPayerCorpName() {
		return payerCorpName;
	}
	public void setPayerCorpName(String payerCorpName) {
		this.payerCorpName = payerCorpName;
	}
	public String getPayeeCorpName() {
		return payeeCorpName;
	}
	public void setPayeeCorpName(String payeeCorpName) {
		this.payeeCorpName = payeeCorpName;
	}
	public BigDecimal getPayerAccBalance() {
		return payerAccBalance;
	}
	public void setPayerAccBalance(BigDecimal payerAccBalance) {
		this.payerAccBalance = payerAccBalance;
	}
	public BigDecimal getPayeeAccBalance() {
		return payeeAccBalance;
	}
	public void setPayeeAccBalance(BigDecimal payeeAccBalance) {
		this.payeeAccBalance = payeeAccBalance;
	}
	public String getCaInfoId() {
		return caInfoId;
	}
	public void setCaInfoId(String caInfoId) {
		this.caInfoId = caInfoId;
	}
	public Date getEncryDate() {
		return encryDate;
	}
	public void setEncryDate(Date encryDate) {
		this.encryDate = encryDate;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getEncrySource() {
		return encrySource;
	}
	public void setEncrySource(String encrySource) {
		this.encrySource = encrySource;
	}
	public String getCaCoding() {
		return caCoding;
	}
	public void setCaCoding(String caCoding) {
		this.caCoding = caCoding;
	}
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}
	public String getBisExcId() {
		return bisExcId;
	}
	public void setBisExcId(String bisExcId) {
		this.bisExcId = bisExcId;
	}

	public String getCmdStat() {
		return cmdStat;
	}

	public void setCmdStat(String cmdStat) {
		this.cmdStat = cmdStat;
	}

	public String getBisGatherCmdId() {
		return bisGatherCmdId;
	}
	public void setBisGatherCmdId(String bisGatherCmdId) {
		this.bisGatherCmdId = bisGatherCmdId;
	}
	public String getBillFrom() {
		return billFrom;
	}
	public void setBillFrom(String billFrom) {
		this.billFrom = billFrom;
	}
	public String getErpId() {
		return erpId;
	}
	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	public String getTallySign() {
		return tallySign;
	}
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	public String getTallyCode() {
		return tallyCode;
	}
	public void setTallyCode(String tallyCode) {
		this.tallyCode = tallyCode;
	}

	public java.lang.String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getBursarBillNo() {
		return bursarBillNo;
	}
	public void setBursarBillNo(String bursarBillNo) {
		this.bursarBillNo = bursarBillNo;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public String getPoolSign() {
		return poolSign;
	}
	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public String getPayeeCounterAcc() {
		return payeeCounterAcc;
	}
	public void setPayeeCounterAcc(String payeeCounterAcc) {
		this.payeeCounterAcc = payeeCounterAcc;
	}

	public String getPayeeBankCode() {
		return payeeBankCode;
	}

	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
	}

	public String getPayeeBankTypeId() {
		return payeeBankTypeId;
	}

	public void setPayeeBankTypeId(String payeeBankTypeId) {
		this.payeeBankTypeId = payeeBankTypeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getVoucherStatName() {
		return voucherStatName;
	}

	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
	}

	@Override
	public String toString() {
		return "NisAllocationReqVO{" +
				"id='" + id + '\'' +
				", billNo='" + billNo + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", type=" + type +
				", payerAccId='" + payerAccId + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerAccName='" + payerAccName + '\'' +
				", payerBankName='" + payerBankName + '\'' +
				", payeeAccId='" + payeeAccId + '\'' +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeAccName='" + payeeAccName + '\'' +
				", payeeBankName='" + payeeBankName + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", purpose='" + purpose + '\'' +
				", money=" + money +
				", wishPayday=" + wishPayday +
				", isQuick='" + isQuick + '\'' +
				", difBank='" + difBank + '\'' +
				", areaSign='" + areaSign + '\'' +
				", createBy='" + createBy + '\'' +
				", createByName='" + createByName + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", status=" + status +
				", poolSign='" + poolSign + '\'' +
				", nisAllocationList=" + nisAllocationList +
				", itemCode=" + Arrays.toString(itemCode) +
				", itemName=" + Arrays.toString(itemName) +
				", processMoney=" + Arrays.toString(processMoney) +
				", bankAccBalance=" + bankAccBalance +
				", attachmentId='" + attachmentId + '\'' +
				", payerCorpId='" + payerCorpId + '\'' +
				", payeeCorpId='" + payeeCorpId + '\'' +
				", payerCorpName='" + payerCorpName + '\'' +
				", payeeCorpName='" + payeeCorpName + '\'' +
				", payerAccBalance=" + payerAccBalance +
				", payeeAccBalance=" + payeeAccBalance +
				", caInfoId='" + caInfoId + '\'' +
				", encryDate=" + encryDate +
				", random='" + random + '\'' +
				", encrySource='" + encrySource + '\'' +
				", caCoding='" + caCoding + '\'' +
				", voucherStat='" + voucherStat + '\'' +
				", bisExcId='" + bisExcId + '\'' +
				", bisGatherCmdId='" + bisGatherCmdId + '\'' +
				", billFrom='" + billFrom + '\'' +
				", erpId='" + erpId + '\'' +
				", tallySign='" + tallySign + '\'' +
				", tallyCode='" + tallyCode + '\'' +
				", orderNo='" + orderNo + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", rmk='" + rmk + '\'' +
				'}';
	}
}
