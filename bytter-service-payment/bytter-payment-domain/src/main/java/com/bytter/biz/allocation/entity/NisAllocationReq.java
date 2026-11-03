package com.bytter.biz.allocation.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: NIS_ALLOCATION_REQ
 * @date 2017-05-31 10:25:52
 * 资金调拨制单表
 */
@Entity
@Table(name = "NIS_ALLOCATION_REQ", schema = "")
@SuppressWarnings("serial")
public class NisAllocationReq extends IdEntity implements java.io.Serializable {
	/**
	 * 单据号码
	 */
	private java.lang.String billNo;
	/**
	 * 资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
	 */
	private java.lang.Integer type;
	/**
	 * 单位ID
	 */
	private java.lang.String corpId;
	/**
	 * 付方账号ID
	 */
	private java.lang.String payerAccId;
	/**
	 * 付方账号
	 */
	private java.lang.String payerAcc;
	/**
	 * 付方账号名称
	 */
	private java.lang.String payerAccName;
	/**
	 * 收方账号ID
	 */
	private java.lang.String payeeAccId;
	/**
	 * 收方账号
	 */
	private java.lang.String payeeAcc;
	/**
	 * 收方账号名称
	 */
	private java.lang.String payeeAccName;
	/**
	 * 币别ID
	 */
	private java.lang.String curId;
	/**
	 * 金额
	 */
	private java.math.BigDecimal money;
	/**
	 * 期望支付日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date wishPayday;
	/**
	 * 会计凭证号
	 */
	private java.lang.String bursarBillNo;
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
	 * 用途
	 */
	private java.lang.String purpose;
	/**
	 * 记账日期
	 */
	private java.util.Date tallyDate;
	/**
	 * 凭证流水号
	 */
	private java.lang.String orderNo;
	/**
	 * 记账凭证号码
	 */
	private java.lang.String voucherNo;
	/**
	 * 证书信息
	 */
	private java.lang.String caInfoId;
	/**
	 * 状态
	 */
	private java.lang.Integer status;
	/**
	 * 创建人登录名称
	 */
	private java.lang.String createBy;
	/**
	 * 创建日期
	 */
	private java.util.Date createDate;
	/**
	 * 更新人登录名称
	 */
	private java.lang.String updateBy;
	/**
	 * 更新日期
	 */
	private java.util.Date updateDate;
	/**
	 * 附件ID
	 */
	private java.lang.String attachmentId;
	/**
	 * 单据来源
	 */
	private java.lang.String billFrom;
	/**
	 * ERP编号：如果是需求清单生成的调拨单，此字段填写需求清单ID
	 */
	private java.lang.String erpId;
	/**
	 * 记账标志
	 */
	private java.lang.String tallySign;
	/**
	 * 记账员代码
	 */
	private java.lang.String tallyCode;
	/**
	 * 资金池标识 0-非资金池；1-资金池
	 */
	private java.lang.String poolSign;
	/**时间戳*/
	private String timeStamp;
	/**备注说明*/
	private String rmk;
	/**收款账号对应的中心账号*/
	private String payeeCounterAcc;

	private String voucherStat;

	@Column(name = "RMK")
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  单据号码
	 */
	@Column(name = "BILL_NO", nullable = false, length = 12)
	public java.lang.String getBillNo() {
		return this.billNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  单据号码
	 */
	public void setBillNo(java.lang.String billNo) {
		this.billNo = billNo;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer  资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
	 */
	@Column(name = "TYPE", nullable = false, length = 4)
	public java.lang.Integer getType() {
		return this.type;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer  资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴
	 */
	public void setType(java.lang.Integer type) {
		this.type = type;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  单位ID
	 */
	@Column(name = "CORP_ID", nullable = false, length = 32)
	public java.lang.String getCorpId() {
		return this.corpId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  单位ID
	 */
	public void setCorpId(java.lang.String corpId) {
		this.corpId = corpId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  付方账号ID
	 */
	@Column(name = "PAYER_ACC_ID", nullable = true, length = 32)
	public java.lang.String getPayerAccId() {
		return this.payerAccId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  付方账号ID
	 */
	public void setPayerAccId(java.lang.String payerAccId) {
		this.payerAccId = payerAccId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  付方账号
	 */
	@Column(name = "PAYER_ACC", nullable = true, length = 50)
	public java.lang.String getPayerAcc() {
		return this.payerAcc;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  付方账号
	 */
	public void setPayerAcc(java.lang.String payerAcc) {
		this.payerAcc = payerAcc;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  付方账号名称
	 */
	@Column(name = "PAYER_ACC_NAME", nullable = true, length = 140)
	public java.lang.String getPayerAccName() {
		return this.payerAccName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  付方账号名称
	 */
	public void setPayerAccName(java.lang.String payerAccName) {
		this.payerAccName = payerAccName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  收方账号ID
	 */
	@Column(name = "PAYEE_ACC_ID", nullable = true, length = 32)
	public java.lang.String getPayeeAccId() {
		return this.payeeAccId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  收方账号ID
	 */
	public void setPayeeAccId(java.lang.String payeeAccId) {
		this.payeeAccId = payeeAccId;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  收方账号
	 */
	@Column(name = "PAYEE_ACC", nullable = true, length = 140)
	public java.lang.String getPayeeAcc() {
		return this.payeeAcc;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  收方账号
	 */
	public void setPayeeAcc(java.lang.String payeeAcc) {
		this.payeeAcc = payeeAcc;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  收方账号名称
	 */
	@Column(name = "PAYEE_ACC_NAME", nullable = true, length = 140)
	public java.lang.String getPayeeAccName() {
		return this.payeeAccName;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  收方账号名称
	 */
	public void setPayeeAccName(java.lang.String payeeAccName) {
		this.payeeAccName = payeeAccName;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  curId
	 */
	@Column(name = "CUR_ID", nullable = true, length = 32)
	public java.lang.String getCurId() {
		return this.curId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  curId
	 */
	public void setCurId(java.lang.String curId) {
		this.curId = curId;
	}
	/**
	 * 方法: 取得java.math.BigDecimal
	 *
	 * @return: java.math.BigDecimal  金额
	 */
	@Column(name = "MONEY", nullable = false, scale = 2, length = 15)
	public java.math.BigDecimal getMoney() {
		return this.money;
	}
	/**
	 * 方法: 设置java.math.BigDecimal
	 *
	 * @param: java.math.BigDecimal  金额
	 */
	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  期望支付日期
	 */
	@Column(name = "WISH_PAYDAY", nullable = true)
	public java.util.Date getWishPayday() {
		return this.wishPayday;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  期望支付日期
	 */
	public void setWishPayday(java.util.Date wishPayday) {
		this.wishPayday = wishPayday;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  会计凭证号
	 */
	@Column(name = "BURSAR_BILL_NO", nullable = true, length = 40)
	public java.lang.String getBursarBillNo() {
		return this.bursarBillNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  会计凭证号
	 */
	public void setBursarBillNo(java.lang.String bursarBillNo) {
		this.bursarBillNo = bursarBillNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  加急标志 1-是 0-否
	 */
	@Column(name = "IS_QUICK", nullable = true, length = 1)
	public java.lang.String getIsQuick() {
		return this.isQuick;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  加急标志 1-是 0-否
	 */
	public void setIsQuick(java.lang.String isQuick) {
		this.isQuick = isQuick;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  跨行标识 0-同行 1-他行
	 */
	@Column(name = "DIF_BANK", nullable = true, length = 1)
	public java.lang.String getDifBank() {
		return this.difBank;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  跨行标识 0-同行 1-他行
	 */
	public void setDifBank(java.lang.String difBank) {
		this.difBank = difBank;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  地域标识 0-同城 1-异地
	 */
	@Column(name = "AREA_SIGN", nullable = true, length = 1)
	public java.lang.String getAreaSign() {
		return this.areaSign;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  地域标识 0-同城 1-异地
	 */
	public void setAreaSign(java.lang.String areaSign) {
		this.areaSign = areaSign;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  用途
	 */
	@Column(name = "PURPOSE", nullable = true, length = 120)
	public java.lang.String getPurpose() {
		return this.purpose;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  用途
	 */
	public void setPurpose(java.lang.String purpose) {
		this.purpose = purpose;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  记账日期
	 */
	@Column(name = "TALLY_DATE", nullable = true)
	public java.util.Date getTallyDate() {
		return this.tallyDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  记账日期
	 */
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  凭证流水号
	 */
	@Column(name = "ORDER_NO", nullable = true, length = 18)
	public java.lang.String getOrderNo() {
		return this.orderNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  凭证流水号
	 */
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  记账凭证号码
	 */
	@Column(name = "VOUCHER_NO", nullable = true, length = 40)
	public java.lang.String getVoucherNo() {
		return this.voucherNo;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  记账凭证号码
	 */
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  证书信息
	 */
	@Column(name = "CA_INFO_ID", nullable = true, length = 32)
	public java.lang.String getCaInfoId() {
		return this.caInfoId;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  证书信息
	 */
	public void setCaInfoId(java.lang.String caInfoId) {
		this.caInfoId = caInfoId;
	}
	/**
	 * 方法: 取得java.lang.Integer
	 *
	 * @return: java.lang.Integer  状态
	 */
	@Column(name = "STATUS", nullable = false, length = 4)
	public java.lang.Integer getStatus() {
		return this.status;
	}
	/**
	 * 方法: 设置java.lang.Integer
	 *
	 * @param: java.lang.Integer  状态
	 */
	public void setStatus(java.lang.Integer status) {
		this.status = status;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  创建人登录名称
	 */
	@Column(name = "CREATE_BY", nullable = true, length = 50)
	public java.lang.String getCreateBy() {
		return this.createBy;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  创建人登录名称
	 */
	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  创建日期
	 */
	@Column(name = "CREATE_DATE", nullable = true)
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  创建日期
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  更新人登录名称
	 */
	@Column(name = "UPDATE_BY", nullable = true, length = 50)
	public java.lang.String getUpdateBy() {
		return this.updateBy;
	}
	/**
	 * 方法: 设置java.lang.String
	 *
	 * @param: java.lang.String  更新人登录名称
	 */
	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 方法: 取得java.util.Date
	 *
	 * @return: java.util.Date  更新日期
	 */
	@Column(name = "UPDATE_DATE", nullable = true)
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	/**
	 * 方法: 设置java.util.Date
	 *
	 * @param: java.util.Date  更新日期
	 */
	public void setUpdateDate(java.util.Date updateDate) {
		this.updateDate = updateDate;
	}
	/**
	 * 方法: 取得java.lang.String
	 *
	 * @return: java.lang.String  附件ID
	 */
	@Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
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
	/**
	 * 方法: 取得java.lang.String billFrom
	 *
	 * @return: java.lang.String billFrom  单据来源
	 */
	@Column(name = "BILL_FROM", nullable = true, length = 2)
	public String getBillFrom() {
		return billFrom;
	}
	/**
	 * 方法: 设置java.lang.String billFrom
	 *
	 * @param: java.lang.String billFrom  单据来源
	 */
	public void setBillFrom(String billFrom) {
		this.billFrom = billFrom;
	}
	/**
	 * 方法: 取得java.lang.String erpId
	 *
	 * @return: java.lang.String erpId  ERP编号
	 */
	@Column(name = "ERP_ID", nullable = true, length = 2)
	public String getErpId() {
		return erpId;
	}
	/**
	 * 方法: 设置java.lang.String erpId
	 *
	 * @param: java.lang.String erpId  ERP编号
	 */
	public void setErpId(String erpId) {
		this.erpId = erpId;
	}
	@Column(name = "TALLY_SIGN", nullable = true, length = 1)
	public String getTallySign() {
		return tallySign;
	}
	/**
	 * 方法: 取得java.lang.String tallySign
	 *
	 * @return: java.lang.String tallySign  记账标志
	 */
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	@Column(name = "TALLY_CODE", nullable = true, length = 32)
	public String getTallyCode() {
		return tallyCode;
	}
	/**
	 * 方法: 设置java.lang.String tallySign
	 *
	 * @return: java.lang.String tallySign  记账员代码
	 */
	public void setTallyCode(String tallyCode) {
		this.tallyCode = tallyCode;
	}
	/**
	 * 方法: 取得java.lang.String poolSign
	 *
	 * @return: java.lang.String poolSign  资金池标识 0-非资金池；1-资金池
	 */
	@Column(name = "POOL_SIGN", nullable = true, length = 1)
	public String getPoolSign() {
		return poolSign;
	}
	/**
	 * 方法: 设置java.lang.String poolSign
	 *
	 * @return: java.lang.String poolSign  资金池标识 0-非资金池；1-资金池
	 */
	public void setPoolSign(String poolSign) {
		this.poolSign = poolSign;
	}
	@Column(name = "TIME_STAMP")
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	@Column(name = "PAYEE_COUNTER_ACC")
	public String getPayeeCounterAcc() {
		return payeeCounterAcc;
	}
	public void setPayeeCounterAcc(String payeeCounterAcc) {
		this.payeeCounterAcc = payeeCounterAcc;
	}

	@Column(name ="VOUCHER_STAT")
	public String getVoucherStat() {
		return voucherStat;
	}
	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}

	@Override
	public String toString() {
		return "NisAllocationReq{" +
				"billNo='" + billNo + '\'' +
				", type=" + type +
				", corpId='" + corpId + '\'' +
				", payerAccId='" + payerAccId + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerAccName='" + payerAccName + '\'' +
				", payeeAccId='" + payeeAccId + '\'' +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeAccName='" + payeeAccName + '\'' +
				", curId='" + curId + '\'' +
				", money=" + money +
				", wishPayday=" + wishPayday +
				", bursarBillNo='" + bursarBillNo + '\'' +
				", isQuick='" + isQuick + '\'' +
				", difBank='" + difBank + '\'' +
				", areaSign='" + areaSign + '\'' +
				", purpose='" + purpose + '\'' +
				", tallyDate=" + tallyDate +
				", orderNo='" + orderNo + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", caInfoId='" + caInfoId + '\'' +
				", status=" + status +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", attachmentId='" + attachmentId + '\'' +
				", billFrom='" + billFrom + '\'' +
				", erpId='" + erpId + '\'' +
				", tallySign='" + tallySign + '\'' +
				", tallyCode='" + tallyCode + '\'' +
				", poolSign='" + poolSign + '\'' +
				", rmk='" + rmk + '\'' +
				'}';
	}
}
