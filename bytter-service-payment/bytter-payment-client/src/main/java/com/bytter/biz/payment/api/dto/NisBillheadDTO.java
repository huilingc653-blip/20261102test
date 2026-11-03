package com.bytter.biz.payment.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Title: DTO
 * Description: NIS_BILLHEAD
 * @author huangx
 * @since 2017-09-02 11:35:12
 * @version 1.0
 *
 */
public class NisBillheadDTO implements Serializable {
	private String id;
	/**付款单号*/
	private String billNo;
	/**单位*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**付款方式*/
	private String voucherTypeId;
	/**系统结算方式*/
	private String sysVoucherCode;
	/**付款方式名称*/
	private String voucherName;
	/**账号*/
	private String accId;
	/**付款账号*/
	private String payerAcc;
	/**付款账号名称*/
	private String payerAccName;
	/**付款开户行名称*/
	private String payerBank;
	/**下拨账号ID*/
	private String downAccId;
	/**下拨账号*/
	private String downAcc;
	/**下拨账号户名*/
	private String downAccName;
	/**币别*/
	private String curId;
	/**币别名称*/
	private String curName;
	/**金额*/
	private BigDecimal payMoney;
	/**票据金额*/
	private BigDecimal billMoney;
	/**收款账号*/
	private String payeeAcc;
	/**收款账号名称*/
	private String payeeAccName;
	/**收款方开户行行号*/
	private String payeeBankCode;
	/**收款开户行名称*/
	private String payeeBank;
	/**收款行别*/
	private String bankTypeId;
	/**所在省*/
	private String prov;
	private String provName;
	/**所在市*/
	private String city;
	private String cityName;

	/**期望支付日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date wishPayday;
	/**加急标志*/
	private String isQuick;
	/**是否对私付款*/
	private String isForindividual;
	/**接口代码*/
	private String bifCode;
	/**跨行标识*/
	private String difBank;
	/**地域标识*/
	private String areaSign;
	/**会计凭证号*/
	private String bursarBillNo;
	/**用途*/
	private String purpose;
	/**附言*/
	private String addWord;
	/**是否直接付款*/
	private String directPayFlag;

	/**证书信息*/
	private String caInfoId;
	/**产生编码日期*/
    private Date encryDate;
    /**加密随机数*/
    private String random;
    /**编码源*/
    private String encrySource;
    /**CA编码*/
    private String caCoding;

	/**记账日期*/
	private Date tallyDate;
	/**凭证流水号*/
	private String orderNo;
	/**记账凭证号码*/
	private String voucherNo;
	/**票据信息*/
	private String billId;
	/**ERP编号*/
	private String erpId;
	/**业务类别*/
	private String busType;
	/**业务类别名称*/
	private String busTypeName;
	/**单据来源*/
	private String billFrom;
	/**拆分前单据号*/
	private String splitId;
	/**合并后单据号*/
	private String mergeId;
	/**状态*/
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**附件ID*/
	private String attachmentId;
	/**是否已付款*/
	private java.lang.String isPayed;
	/**记账标志**/
	private java.lang.String tallySign;
	/**银行流水号**/
	private java.lang.String bankSerialNo;
	/**是否自动支付*/
	private String isAutoPay;
	private String isAutoPayName;
	/**策略ID*/
	private String policyId;
	/**票据类型*/
	private Integer billType;
	/**票据流通介质*/
	private Integer billFlag;
	/**票据类型名称*/
	private String billTypeName;
	/**票据流通介质名称*/
	private String billFlagName;
	/**业务单位（票据付款专用）*/
	private String busCorpId;
	private String busCorpName;
	/**业务人员（票据付款专用）*/
	private String busPersonnel;
	/**备注说明*/
	private String rmk;

	/**
	 * 交易日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date transTime;

	/**
	 * 交易状态
	 */
	private String voucherStat;

	/**退款勾兑原单ID：只有退款勾兑新生成得单据才会有这个值*/
	private String refundOrgId;

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
	public String getVoucherTypeId() {
		return voucherTypeId;
	}
	public void setVoucherTypeId(String voucherTypeId) {
		this.voucherTypeId = voucherTypeId;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public String getAccId() {
		return accId;
	}
	public void setAccId(String accId) {
		this.accId = accId;
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
	public String getDownAccId() {
		return downAccId;
	}
	public void setDownAccId(String downAccId) {
		this.downAccId = downAccId;
	}
	public String getDownAcc() {
		return downAcc;
	}
	public void setDownAcc(String downAcc) {
		this.downAcc = downAcc;
	}
	public String getDownAccName() {
		return downAccName;
	}
	public void setDownAccName(String downAccName) {
		this.downAccName = downAccName;
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
	public BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(BigDecimal billMoney) {
		this.billMoney = billMoney;
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
	public String getPayeeBankCode() {
		return payeeBankCode;
	}
	public void setPayeeBankCode(String payeeBankCode) {
		this.payeeBankCode = payeeBankCode;
	}
	public String getPayeeBank() {
		return payeeBank;
	}
	public void setPayeeBank(String payeeBank) {
		this.payeeBank = payeeBank;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
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
	public String getIsForindividual() {
		return isForindividual;
	}
	public void setIsForindividual(String isForindividual) {
		this.isForindividual = isForindividual;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getDifBank() {
		return difBank;
	}
	public void setDifBank(String difBank) {
		this.difBank = difBank;
	}
	public String getAreaSign() {
		return areaSign;
	}
	public void setAreaSign(String areaSign) {
		this.areaSign = areaSign;
	}
	public String getBursarBillNo() {
		return bursarBillNo;
	}
	public void setBursarBillNo(String bursarBillNo) {
		this.bursarBillNo = bursarBillNo;
	}
	public String getPurpose() {
		return purpose;
	}
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	public String getAddWord() {
		return addWord;
	}
	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}
	public String getDirectPayFlag() {
		return directPayFlag;
	}
	public void setDirectPayFlag(String directPayFlag) {
		this.directPayFlag = directPayFlag;
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
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getErpId() {
		return erpId;
	}
	public void setErpId(String erpId) {
		this.erpId = erpId;
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
	public String getBillFrom() {
		return billFrom;
	}
	public void setBillFrom(String billFrom) {
		this.billFrom = billFrom;
	}
	public String getSplitId() {
		return splitId;
	}
	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	public String getMergeId() {
		return mergeId;
	}
	public void setMergeId(String mergeId) {
		this.mergeId = mergeId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public String getIsPayed() {
		return isPayed;
	}
	public void setIsPayed(String isPayed) {
		this.isPayed = isPayed;
	}
	public java.lang.String getTallySign() {
		return tallySign;
	}
	public void setTallySign(java.lang.String tallySign) {
		this.tallySign = tallySign;
	}
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public String getIsAutoPay() {
		return isAutoPay;
	}
	public void setIsAutoPay(String isAutoPay) {
		this.isAutoPay = isAutoPay;
	}
	public String getIsAutoPayName() {
		return isAutoPayName;
	}
	public void setIsAutoPayName(String isAutoPayName) {
		this.isAutoPayName = isAutoPayName;
	}
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getBillFlagName() {
		return billFlagName;
	}
	public void setBillFlagName(String billFlagName) {
		this.billFlagName = billFlagName;
	}
	public String getBusCorpId() {
		return busCorpId;
	}
	public void setBusCorpId(String busCorpId) {
		this.busCorpId = busCorpId;
	}
	public String getBusCorpName() {
		return busCorpName;
	}
	public void setBusCorpName(String busCorpName) {
		this.busCorpName = busCorpName;
	}
	public String getBusPersonnel() {
		return busPersonnel;
	}
	public void setBusPersonnel(String busPersonnel) {
		this.busPersonnel = busPersonnel;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getVoucherStat() {
		return voucherStat;
	}

	public void setVoucherStat(String voucherStat) {
		this.voucherStat = voucherStat;
	}

	public String getRefundOrgId() {
		return refundOrgId;
	}

	public void setRefundOrgId(String refundOrgId) {
		this.refundOrgId = refundOrgId;
	}
}
