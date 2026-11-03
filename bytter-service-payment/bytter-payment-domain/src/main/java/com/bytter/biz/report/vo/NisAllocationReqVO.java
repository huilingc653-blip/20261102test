package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 上划，下拨，上缴，请款在途以及已结金额
 *
 * @author zhoup
 */
public class NisAllocationReqVO extends ExcelModel implements Serializable {
	/**
	 * 网点ID
	 */
	@BtCompositeSearch(fieldName = "m.netId")
	private String netId;
	/**
	 * 网点名称
	 */
	@BtCompositeSearch(fieldName = "sc.name")
	private String netName;
	/**
	 * 单位IDs
	 */
	@BtCompositeSearch(fieldName = "m.corpId")
	private String corpId;
	/**
	 * 单位名称
	 */
	@Excel(name = "*单位名称")
	@BtCompositeSearch(fieldName = "m.corpName")
	private String corpName;
	/**
	 * 币别ID
	 */
	private String curId;
	/**
	 * 币别名称
	 */
	@Excel(name = "*币别")
	private String curName;
	/**
	 * 上划在途金额
	 */
	private BigDecimal onDraw;
	/**
	 * 已上划金额
	 */
	private BigDecimal hadDraw;
	/**
	 * 上缴在途金额
	 */
	private BigDecimal onHanded;
	/**
	 * 已上缴金额
	 */
	private BigDecimal hadHanded;
	/**
	 * 下拨在途金额
	 */
	private BigDecimal onDial;
	/**
	 * 已下拨金额
	 */
	private BigDecimal hadDial;
	/**
	 * 请款在途金额
	 */
	private BigDecimal onRequest;
	/**
	 * 已请款金额
	 */
	private BigDecimal hadRequest;
	/**
	 * 开始日期
	 */
	private Date gedCreateDate;
	/**
	 * 结束日期
	 */
	private Date ledCreateDate;
	/**
	 * 创造日期
	 */
	private Date createDate;
	/**
	 * 判断条件
	 */
	private String option;
	/**
	 * 在途还是已完成  on-在途  had-已完成
	 */
	private String statuss;
	/**
	 * 业务单号
	 */
	@BtCompositeSearch(fieldName = "aa.billNo ")
	private String billNo;
	/**
	 * 业务类型 上划_10,下拨_11,请款_12,头寸调拨_13,上缴_14
	 */
	@BtCompositeSearch(fieldName = "aa.type ")
	private Integer type;

	@Excel(name = "*业务类型")
	private String typeName;
	/**
	 * 付方账号ID
	 */
	private String payerAccId;
	/**
	 * 付款账号
	 */
	@Excel(name = "*付款账号")
	@BtCompositeSearch(fieldName = "aa.payeracc")
	private String payerAcc;
	/**
	 * 付款账户名
	 */
	@Excel(name = "付款账号名称")
	@BtCompositeSearch(fieldName = "acc.acc_name")
	private String payerAccName;
	/**
	 * 付款账号开户行
	 */
	@BtCompositeSearch(fieldName = "acc.bank_name")
	private String payerBankName;
	/**
	 * 收方账号ID
	 */
	private String payeeAccId;
	/**
	 * 收款账号
	 */
	@Excel(name = "*收款账号")
	@BtCompositeSearch(fieldName = "aa.payeeacc")
	private String payeeAcc;
	/**
	 * 收款账户名
	 */
	@Excel(name = "收款账号名称")
	@BtCompositeSearch(fieldName = "bba.acc_name")
	private String payeeAccName;
	/**
	 * 收款账号开户行
	 */
	@BtCompositeSearch(fieldName = "bba.bank_name")
	private String payeeBankName;
	/**
	 * 金额（单条数据）
	 */
	@Excel(name = "*金额")
	private BigDecimal money;
	/**
	 * 业务状态
	 */
	private Integer status;
	/**
	 * 交易状态
	 */
	private String voucherState;
	/**
	 * 记账状态
	 */
	private String tallySign;
	/**是否折合币种*/
	private String changeCur;
	/**折合币种*/
	private String reducedCurId;
	/**折算汇率类型*/
	private String rateType;
	/**汇率日期*/
	private Date rateDate;
	/**折合汇率*/
	private BigDecimal curExrate;

	/**
	 * 期望支付日期
	 */
	@Excel(name = "*期望支付日期")
	@BtCompositeSearch(fieldName = "ar.wish_payday")
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date wishPayday;

	/**
	 * 加急标志 1-是 0-否
	 */
	private java.lang.String isQuick;
	/**
	 * 加急标志 1-是 0-否
	 */
	@Excel(name = "*是否加急")
	private java.lang.String isQuickName;

	/**必填项目*/
	@Excel(name = "*备注说明")
	private String rmk;
	/**
	 * 用途
	 */
	private java.lang.String purpose;
	/**
	 * 跨行标识 0-同行 1-他行
	 */
	private java.lang.String difBank;

	/**收款账号对应的中心账号*/
	private String payeeCounterAcc;

	public String getPayeeCounterAcc() {
		return payeeCounterAcc;
	}

	public void setPayeeCounterAcc(String payeeCounterAcc) {
		this.payeeCounterAcc = payeeCounterAcc;
	}

	public String getIsQuickName() {
		return isQuickName;
	}

	public void setIsQuickName(String isQuickName) {
		this.isQuickName = isQuickName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getBeanName() {
		return "nisAllocationReqService";
	}

	public String getRmk() {
		return rmk;
	}

	public void setRmk(String rmk) {
		this.rmk = rmk;
	}

	public Date getWishPayday() {
		return wishPayday;
	}

	public void setWishPayday(Date wishPayday) {
		this.wishPayday = wishPayday;
	}

	public String getIsQuick() {
		return isQuick;
	}

	public void setIsQuick(String isQuick) {
		this.isQuick = isQuick;
	}

	public String getPayerAccId() {
		return payerAccId;
	}

	public void setPayerAccId(String payerAccId) {
		this.payerAccId = payerAccId;
	}

	public String getPayeeAccId() {
		return payeeAccId;
	}

	public void setPayeeAccId(String payeeAccId) {
		this.payeeAccId = payeeAccId;
	}

	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
	}
	public String getNetName() {
		return netName;
	}
	public void setNetName(String netName) {
		this.netName = netName;
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
	public BigDecimal getOnDraw() {
		return onDraw;
	}
	public void setOnDraw(BigDecimal onDraw) {
		this.onDraw = onDraw;
	}
	public BigDecimal getHadDraw() {
		return hadDraw;
	}
	public void setHadDraw(BigDecimal hadDraw) {
		this.hadDraw = hadDraw;
	}
	public BigDecimal getOnHanded() {
		return onHanded;
	}
	public void setOnHanded(BigDecimal onHanded) {
		this.onHanded = onHanded;
	}
	public BigDecimal getHadHanded() {
		return hadHanded;
	}
	public void setHadHanded(BigDecimal hadHanded) {
		this.hadHanded = hadHanded;
	}
	public BigDecimal getOnDial() {
		return onDial;
	}
	public void setOnDial(BigDecimal onDial) {
		this.onDial = onDial;
	}
	public BigDecimal getHadDial() {
		return hadDial;
	}
	public void setHadDial(BigDecimal hadDial) {
		this.hadDial = hadDial;
	}
	public BigDecimal getOnRequest() {
		return onRequest;
	}
	public void setOnRequest(BigDecimal onRequest) {
		this.onRequest = onRequest;
	}
	public BigDecimal getHadRequest() {
		return hadRequest;
	}
	public void setHadRequest(BigDecimal hadRequest) {
		this.hadRequest = hadRequest;
	}
	public Date getGedCreateDate() {
		return gedCreateDate;
	}
	public void setGedCreateDate(Date gedCreateDate) {
		this.gedCreateDate = gedCreateDate;
	}
	public Date getLedCreateDate() {
		return ledCreateDate;
	}
	public void setLedCreateDate(Date ledCreateDate) {
		this.ledCreateDate = ledCreateDate;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
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
	public String getPayerBankName() {
		return payerBankName;
	}
	public void setPayerBankName(String payerBankName) {
		this.payerBankName = payerBankName;
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
	public String getPayeeBankName() {
		return payeeBankName;
	}
	public void setPayeeBankName(String payeeBankName) {
		this.payeeBankName = payeeBankName;
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
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getStatuss() {
		return statuss;
	}
	public void setStatuss(String statuss) {
		this.statuss = statuss;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}
	public String getTallySign() {
		return tallySign;
	}
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	public String getChangeCur() {
		return changeCur;
	}
	public void setChangeCur(String changeCur) {
		this.changeCur = changeCur;
	}
	public String getReducedCurId() {
		return reducedCurId;
	}
	public void setReducedCurId(String reducedCurId) {
		this.reducedCurId = reducedCurId;
	}
	public String getRateType() {
		return rateType;
	}
	public void setRateType(String rateType) {
		this.rateType = rateType;
	}
	public Date getRateDate() {
		return rateDate;
	}
	public void setRateDate(Date rateDate) {
		this.rateDate = rateDate;
	}
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getDifBank() {
		return difBank;
	}

	public void setDifBank(String difBank) {
		this.difBank = difBank;
	}
}
