package com.bytter.erp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**   
 * @Title: DTO
 * @Description: NIS_BILLHEAD
 * @author huangx
 * @date 2017-09-02 11:35:12
 * @version 1.0
 *
 */
public class ErpNisBillheadDTO {
	private String id;
	/**付款单号*/
	private String billNo;
	/**单位*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**单位编码*/
	private String corpCode;
	/**付款方式*/
	private String voucherTypeId;
	/**付款方式编码*/
	private String voucherTypeCode;
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
	/**付款开户行行号*/
	private String payerBankCode;
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
	/**币别编码*/
	private String curCode;
	/**金额*/
	private BigDecimal payMoney;
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
	@JsonFormat(pattern = "yyyy-MM-dd")
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
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date encryDate;
    /**加密随机数*/
    private String random;
    /**编码源*/
    private String encrySource;
    /**CA编码*/
    private String caCoding;

	/**记账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
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
	private String isPayed;
	/**记账标志**/
	private String tallySign;

	/**外部系统id**/
	private String serialNoErp;
	/**外部系统凭证编号**/
	private String voucherNoErp;

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
	public String getTallySign() {
		return tallySign;
	}
	public void setTallySign(String tallySign) {
		this.tallySign = tallySign;
	}
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}

	public String getSerialNoErp() {
		return serialNoErp;
	}

	public void setSerialNoErp(String serialNoErp) {
		this.serialNoErp = serialNoErp;
	}

	public String getVoucherNoErp() {
		return voucherNoErp;
	}

	public void setVoucherNoErp(String voucherNoErp) {
		this.voucherNoErp = voucherNoErp;
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public String getVoucherTypeCode() {
		return voucherTypeCode;
	}

	public void setVoucherTypeCode(String voucherTypeCode) {
		this.voucherTypeCode = voucherTypeCode;
	}

	public String getCurCode() {
		return curCode;
	}

	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}

	public String getPayerBankCode() {
		return payerBankCode;
	}

	public void setPayerBankCode(String payerBankCode) {
		this.payerBankCode = payerBankCode;
	}
}
