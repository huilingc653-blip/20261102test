package com.bytter.biz.report.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 资金调拔明细表（报表）
 *
 * @author zuoy
 * @date 2017-10-09
 */
public class NisAllocationReportVO implements Serializable {
	/**
	 * 网点ID
	 */
	@BtCompositeSearch(fieldName = "t.netId")
	private String netId;
	/**
	 * 单位ID
	 */
	@Corp(inject = "corpName")
	@BtCompositeSearch(fieldName = "t.corpId")
	private String corpId;
	/**
	 * 单位代码
	 */
	private String corpCode;
	/**
	 * 单位名称
	 */
	@BtCompositeSearch(fieldName = "t.corpName")
	private String corpName;
	/**
	 * 业务类型
	 */
	@BtCompositeSearch(fieldName = "t.voucherFrom")
	@Dict(dictGroup = "nisAllocationReport.voucherFrom" , inject = "voucherFromName")
	private String voucherFrom;
	/**
	 * 业务类型名称
	 */
	@BtCompositeSearch(fieldName = "t.voucherFrom")
	private String voucherFromName;
	/**
	 * 制单日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "t.reqDate")
	private Date reqDate;

	@BtCompositeSearch(fieldName = "t.curId")
	private String curId;
	/**
	 * 币别名称
	 */
//	@BtCompositeSearch(fieldName = "t.curId")
	private String curName;
	/**
	 * 金额
	 */
	@BtCompositeSearch(fieldName = "t.payMoney")
	private BigDecimal payMoney;
	/**
	 * 用途
	 */
	@BtCompositeSearch(fieldName = "t.purpose")
	private String purpose;
	/**
	 * 付款账号
	 */
	@BtCompositeSearch(fieldName = "t.payerAcc")
	private String payerAcc;
	/**
	 * 付款户名
	 */
	@BtCompositeSearch(fieldName = "t.payerAccName")
	private String payerAccName;
	/**
	 * 付款开户行
	 */
	@BtCompositeSearch(fieldName = "t.payerBank")
	private String payerBank;
	/**
	 * 收款账号
	 */
	@BtCompositeSearch(fieldName = "t.payeeAcc")
	private String payeeAcc;
	/**
	 * 收款户名
	 */
	@BtCompositeSearch(fieldName = "t.payeeAccName")
	private String payeeAccName;
	/**
	 * 收款开户行
	 */
	@BtCompositeSearch(fieldName = "t.payeeBank")
	private String payeeBank;
	/**
	 * 对方单位
	 */
	@BtCompositeSearch(fieldName = "t.oppCorpId")
	private String oppCorpId;
	/**
	 * 开始制单日期-查询条件
	 */
	private Date beginReqDate;
	/**
	 * 结束制单日期-查询条件
	 */
	private Date endReqDate;
	/**
	 * 归集/上划汇总金额
	 */
	private BigDecimal upPayMoney;
	/**
	 * 下拨汇总金额
	 */
	private BigDecimal downPayMoney;
	/**
	 * 请款汇总金额
	 */
	private BigDecimal askPayMoney;
	/**
	 * 上缴汇总金额
	 */
	private BigDecimal turnPayMoney;
	/**
	 * 币别符号
	 */
	private String curSign;
	/**
	 * 付款金额字符串
	 */
	private String payMoneyStr;
	/**
	 * 交易日期字符串
	 */
	private String reqDateStr;
	/**
	 * 业务id
	 */
	private String id;
	/**
	 * 网点单位标识
	 */
	private String corpType;
	/**
	 * 开始时间
	 */
	private String startDateStr;
	/**
	 * 结束时间
	 */
	private String endDateStr;
	/**
	 * 刻度标识
	 */
	private String sign;
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public String getCorpName() {
		return corpName;
	}
	public void setCorpName(String corpName) {
		this.corpName = corpName;
	}
	public String getVoucherFrom() {
		return voucherFrom;
	}
	public void setVoucherFrom(String voucherFrom) {
		this.voucherFrom = voucherFrom;
	}
	public String getVoucherFromName() {
		return voucherFromName;
	}
	public void setVoucherFromName(String voucherFromName) {
		this.voucherFromName = voucherFromName;
	}
	public Date getReqDate() {
		return reqDate;
	}
	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
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
	public BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(BigDecimal payMoney) {
		this.payMoney = payMoney;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getPayerAcc() {
		return payerAcc;
	}
	public void setPayerAcc(String payerAcc) {
		this.payerAcc = payerAcc;
	}
	public String getPayerAccName() {
		return payerAccName;
	}
	public void setPayerAccName(String payerAccName) {
		this.payerAccName = payerAccName;
	}
	public String getPayerBank() {
		return payerBank;
	}
	public void setPayerBank(String payerBank) {
		this.payerBank = payerBank;
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
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String getOppCorpId() {
		return oppCorpId;
	}
	public void setOppCorpId(String oppCorpId) {
		this.oppCorpId = oppCorpId;
	}
	public Date getBeginReqDate() {
		return beginReqDate;
	}
	public void setBeginReqDate(Date beginReqDate) {
		this.beginReqDate = beginReqDate;
	}
	public Date getEndReqDate() {
		return endReqDate;
	}
	public void setEndReqDate(Date endReqDate) {
		this.endReqDate = endReqDate;
	}
	public BigDecimal getUpPayMoney() {
		return upPayMoney;
	}
	public void setUpPayMoney(BigDecimal upPayMoney) {
		this.upPayMoney = upPayMoney;
	}
	public BigDecimal getDownPayMoney() {
		return downPayMoney;
	}
	public void setDownPayMoney(BigDecimal downPayMoney) {
		this.downPayMoney = downPayMoney;
	}
	public BigDecimal getAskPayMoney() {
		return askPayMoney;
	}
	public void setAskPayMoney(BigDecimal askPayMoney) {
		this.askPayMoney = askPayMoney;
	}
	public BigDecimal getTurnPayMoney() {
		return turnPayMoney;
	}
	public void setTurnPayMoney(BigDecimal turnPayMoney) {
		this.turnPayMoney = turnPayMoney;
	}
	public String getCurSign() {
		return curSign;
	}
	public void setCurSign(String curSign) {
		this.curSign = curSign;
	}
	public String getPayMoneyStr() {
		return payMoneyStr;
	}
	public void setPayMoneyStr(String payMoneyStr) {
		this.payMoneyStr = payMoneyStr;
	}
	public String getReqDateStr() {
		return reqDateStr;
	}
	public void setReqDateStr(String reqDateStr) {
		this.reqDateStr = reqDateStr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCorpType() {
		return corpType;
	}
	public void setCorpType(String corpType) {
		this.corpType = corpType;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}
