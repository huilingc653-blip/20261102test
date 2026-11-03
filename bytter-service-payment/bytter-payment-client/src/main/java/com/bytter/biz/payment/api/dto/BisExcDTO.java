package com.bytter.biz.payment.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * Title: DTO
 * Description: BIS_EXC
 * @author huangx
 * @since 2017-6-30 09:49:44
 * @version 1.0
 */
public class BisExcDTO implements Serializable {
	/**ID*/
	private String id;


	/**指令编号*/
	private String voucherNo;
	/**付款单ID*/
	private String cbsBillId;
	/**付款单号*/
	private String cbsBillNo;
	/**单位ID*/
	private String corpId;
	/**单位名称*/
	private String corpName;
	/**申请日期*/
	private Date reqDate;
	/**中心账号*/
	private String counterAcc;
	/**付款账号*/
	private String payerAcc;
	/**付款账号名称*/
	private String payerAccName;
	/**付款开户行名称*/
	private String payerBank;
	/**付方市*/
	private String payerCity;
	private String payerCityName;
	/**付方省*/
	private String payerProv;
	private String payerProvName;
	/**币别*/
	private String curId;
	/**币别名称*/
	private String curName;
	/**金额*/
	private BigDecimal payMoney;
	/**付款方式*/
	private String voucherTypeId;
	/**付款方式名称*/
	private String voucherTypeName;
	/**收款账号*/
	private String payeeAcc;
	/**收款账号名称*/
	private String payeeAccName;
	/**收款方开户行行号*/
	private String payeeBankCode;
	/**收款开户行名称*/
	private String payeeBank;
	/**收方省*/
	private String payeeProv;
	private String payeeProvName;
	/**收方市*/
	private String payeeCity;
	private String payeeCityName;
	/**期望支付日期*/
	private Date wishPayday;
	/**加急标志 1-是 0-否*/
	private String isQuick;
	/**交易模式 3-对公 4-对私*/
	private String transMode;
	/**接口代码*/
	private String bifCode;
	/**接口代码名称*/
	private String bifName;
	/**跨行标识 0-同行 1-他行*/
	private String difBank;
	/**地域标识 0-同城 1-异地*/
	private String areaSign;
	/**用途*/
	private String purpose;
	/**附言*/
	private String addWord;
	/**指令来源*/
	private String voucherFrom;
	/**对私付款指令来源 0-付款办理 1-费用报销*/
	private String voucherFromPrivate;
	/**审批标识*/
	private String approveSign;
	/**业务类型*/
	private Integer busOriType;
	/**指令类型*/
	private String cmdType;
	/**调拨策略ID*/
	private String policyId;
	/**记账标志*/
	private String tallySign;
	/**记账员代码*/
	private String tallyCode;
	/**业务全局ID*/
	private String busGlobalId;
	/**消息提醒标志*/
	private String noticeSign;
	/**银行流水号*/
	private String bankSerialNo;
	/**交易状态*/
	private String voucherStat;
	/**交易状态*/
	private String voucherStatName;
	/**交易时间*/
	private Date transTime;

	private Boolean transTimeNull;
	/**返回时间*/
	private Date returnTime;
	/**返回信息（交易前）*/
	private String firstReturnMsg;
	/**返回信息（交易后）*/
	private String nextReturnMsg;
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
	/**付款账号*/
	private String midPayerAccNo;
	/**付款人*/
	private String midPayerAccName;
	/**付款开户行*/
	private String midPayerBankName;
	/**退回节点 0-制单处，1-记账*/
	private String backNode;
	/**退回原因*/
	private String backMsg;
	/**处理意见*/
	private String handleOpinion;
	/**等待结果明细ID*/
	private String expenseDtlIds;
	/**退回ID集合（制单处）*/
	private String backIds;
	/**退回ID集合（选择退回环节）*/
	private String backNodeIds;
	/**指令数据来源：0-bis_Exc 1-bis_Exc_Proc*/
	private String excFrom;
	/**跨行转付在下拨指令成功后延迟支付时间*/
	private Integer delayTime;

	/**指令重发标志*/
	private String resendSign;
	/**原指令编号*/
	private String oldVoucherNo;
	/**工资发放年月*/
	private String wageDayMonth;
	/**签名*/
	private String signatureCode;
	/**明细数量*/
	private Integer detailNum;
	/**付款方国别*/
	private String payerCountryCode;
	/**收款方国别*/
	private String payeeCountryCode;
	/**来账订单号*/
	private String busAcctNo;

	/**支付类型（渣打银行）*/
	private String bankPayType;
	/**汇款人类型(渣打银行，默认1-Individual)*/
	private String remitterType;
	/**银行组织机构代码*/
	private String bankOrgCode;


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getCbsBillId() {
		return cbsBillId;
	}
	public void setCbsBillId(String cbsBillId) {
		this.cbsBillId = cbsBillId;
	}
	public String getCbsBillNo() {
		return cbsBillNo;
	}
	public void setCbsBillNo(String cbsBillNo) {
		this.cbsBillNo = cbsBillNo;
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
	public Date getReqDate() {
		return reqDate;
	}
	public void setReqDate(Date reqDate) {
		this.reqDate = reqDate;
	}
	public String getCounterAcc() {
		return counterAcc;
	}
	public void setCounterAcc(String counterAcc) {
		this.counterAcc = counterAcc;
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
	public String getPayerCity() {
		return payerCity;
	}
	public void setPayerCity(String payerCity) {
		this.payerCity = payerCity;
	}
	public String getPayerCityName() {
		return payerCityName;
	}
	public void setPayerCityName(String payerCityName) {
		this.payerCityName = payerCityName;
	}
	public String getPayerProv() {
		return payerProv;
	}
	public void setPayerProv(String payerProv) {
		this.payerProv = payerProv;
	}
	public String getPayerProvName() {
		return payerProvName;
	}
	public void setPayerProvName(String payerProvName) {
		this.payerProvName = payerProvName;
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
	public String getVoucherTypeId() {
		return voucherTypeId;
	}
	public void setVoucherTypeId(String voucherTypeId) {
		this.voucherTypeId = voucherTypeId;
	}
	public String getVoucherTypeName() {
		return voucherTypeName;
	}
	public void setVoucherTypeName(String voucherTypeName) {
		this.voucherTypeName = voucherTypeName;
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
	public String getPayeeProv() {
		return payeeProv;
	}
	public void setPayeeProv(String payeeProv) {
		this.payeeProv = payeeProv;
	}
	public String getPayeeProvName() {
		return payeeProvName;
	}
	public void setPayeeProvName(String payeeProvName) {
		this.payeeProvName = payeeProvName;
	}
	public String getPayeeCity() {
		return payeeCity;
	}
	public void setPayeeCity(String payeeCity) {
		this.payeeCity = payeeCity;
	}
	public String getPayeeCityName() {
		return payeeCityName;
	}
	public void setPayeeCityName(String payeeCityName) {
		this.payeeCityName = payeeCityName;
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
	public String getTransMode() {
		return transMode;
	}
	public void setTransMode(String transMode) {
		this.transMode = transMode;
	}
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
	}
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
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
	public String getVoucherFrom() {
		return voucherFrom;
	}
	public void setVoucherFrom(String voucherFrom) {
		this.voucherFrom = voucherFrom;
	}
	public String getVoucherFromPrivate() {
		return voucherFromPrivate;
	}
	public void setVoucherFromPrivate(String voucherFromPrivate) {
		this.voucherFromPrivate = voucherFromPrivate;
	}
	public String getApproveSign() {
		return approveSign;
	}
	public void setApproveSign(String approveSign) {
		this.approveSign = approveSign;
	}
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getCmdType() {
		return cmdType;
	}
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}
	public String getPolicyId() {
		return policyId;
	}
	public void setPolicyId(String policyId) {
		this.policyId = policyId;
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
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	public String getNoticeSign() {
		return noticeSign;
	}
	public void setNoticeSign(String noticeSign) {
		this.noticeSign = noticeSign;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
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
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public Date getReturnTime() {
		return returnTime;
	}
	public void setReturnTime(Date returnTime) {
		this.returnTime = returnTime;
	}
	public String getFirstReturnMsg() {
		return firstReturnMsg;
	}
	public void setFirstReturnMsg(String firstReturnMsg) {
		this.firstReturnMsg = firstReturnMsg;
	}
	public String getNextReturnMsg() {
		return nextReturnMsg;
	}
	public void setNextReturnMsg(String nextReturnMsg) {
		this.nextReturnMsg = nextReturnMsg;
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
	public String getMidPayerAccNo() {
		return midPayerAccNo;
	}
	public void setMidPayerAccNo(String midPayerAccNo) {
		this.midPayerAccNo = midPayerAccNo;
	}
	public String getMidPayerAccName() {
		return midPayerAccName;
	}
	public void setMidPayerAccName(String midPayerAccName) {
		this.midPayerAccName = midPayerAccName;
	}
	public String getMidPayerBankName() {
		return midPayerBankName;
	}
	public void setMidPayerBankName(String midPayerBankName) {
		this.midPayerBankName = midPayerBankName;
	}
	public String getBackNode() {
		return backNode;
	}
	public void setBackNode(String backNode) {
		this.backNode = backNode;
	}
	public String getBackMsg() {
		return backMsg;
	}
	public void setBackMsg(String backMsg) {
		this.backMsg = backMsg;
	}
	public String getHandleOpinion() {
		return handleOpinion;
	}
	public void setHandleOpinion(String handleOpinion) {
		this.handleOpinion = handleOpinion;
	}
	public String getExpenseDtlIds() {
		return expenseDtlIds;
	}
	public void setExpenseDtlIds(String expenseDtlIds) {
		this.expenseDtlIds = expenseDtlIds;
	}
	public String getBackIds() {
		return backIds;
	}
	public void setBackIds(String backIds) {
		this.backIds = backIds;
	}
	public String getBackNodeIds() {
		return backNodeIds;
	}
	public void setBackNodeIds(String backNodeIds) {
		this.backNodeIds = backNodeIds;
	}
	public String getExcFrom() {
		return excFrom;
	}
	public void setExcFrom(String excFrom) {
		this.excFrom = excFrom;
	}
	public Integer getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	public Boolean getTransTimeNull() {
		return transTimeNull;
	}

	public void setTransTimeNull(Boolean transTimeNull) {
		this.transTimeNull = transTimeNull;
	}

	public String getResendSign() {
		return resendSign;
	}

	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
	}

	public String getOldVoucherNo() {
		return oldVoucherNo;
	}

	public void setOldVoucherNo(String oldVoucherNo) {
		this.oldVoucherNo = oldVoucherNo;
	}

	public String getWageDayMonth() {
		return wageDayMonth;
	}

	public void setWageDayMonth(String wageDayMonth) {
		this.wageDayMonth = wageDayMonth;
	}

	public String getSignatureCode() {
		return signatureCode;
	}

	public void setSignatureCode(String signatureCode) {
		this.signatureCode = signatureCode;
	}

	public Integer getDetailNum() {
		return detailNum;
	}

	public void setDetailNum(Integer detailNum) {
		this.detailNum = detailNum;
	}

	public String getPayerCountryCode() {
		return payerCountryCode;
	}

	public void setPayerCountryCode(String payerCountryCode) {
		this.payerCountryCode = payerCountryCode;
	}

	public String getPayeeCountryCode() {
		return payeeCountryCode;
	}

	public void setPayeeCountryCode(String payeeCountryCode) {
		this.payeeCountryCode = payeeCountryCode;
	}

	public String getBusAcctNo() {
		return busAcctNo;
	}

	public void setBusAcctNo(String busAcctNo) {
		this.busAcctNo = busAcctNo;
	}

	public String getBankPayType() {
		return bankPayType;
	}

	public void setBankPayType(String bankPayType) {
		this.bankPayType = bankPayType;
	}

	public String getRemitterType() {
		return remitterType;
	}

	public void setRemitterType(String remitterType) {
		this.remitterType = remitterType;
	}

	public String getBankOrgCode() {
		return bankOrgCode;
	}

	public void setBankOrgCode(String bankOrgCode) {
		this.bankOrgCode = bankOrgCode;
	}

	@Override
	public String toString() {
		return "BisExcDTO{" +
				"id='" + id + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", cbsBillId='" + cbsBillId + '\'' +
				", cbsBillNo='" + cbsBillNo + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", reqDate=" + reqDate +
				", counterAcc='" + counterAcc + '\'' +
				", payerAcc='" + payerAcc + '\'' +
				", payerAccName='" + payerAccName + '\'' +
				", payerBank='" + payerBank + '\'' +
				", payerCity='" + payerCity + '\'' +
				", payerCityName='" + payerCityName + '\'' +
				", payerProv='" + payerProv + '\'' +
				", payerProvName='" + payerProvName + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", payMoney=" + payMoney +
				", voucherTypeId='" + voucherTypeId + '\'' +
				", voucherTypeName='" + voucherTypeName + '\'' +
				", payeeAcc='" + payeeAcc + '\'' +
				", payeeAccName='" + payeeAccName + '\'' +
				", payeeBankCode='" + payeeBankCode + '\'' +
				", payeeBank='" + payeeBank + '\'' +
				", payeeProv='" + payeeProv + '\'' +
				", payeeProvName='" + payeeProvName + '\'' +
				", payeeCity='" + payeeCity + '\'' +
				", payeeCityName='" + payeeCityName + '\'' +
				", wishPayday=" + wishPayday +
				", isQuick='" + isQuick + '\'' +
				", transMode='" + transMode + '\'' +
				", bifCode='" + bifCode + '\'' +
				", bifName='" + bifName + '\'' +
				", difBank='" + difBank + '\'' +
				", areaSign='" + areaSign + '\'' +
				", purpose='" + purpose + '\'' +
				", addWord='" + addWord + '\'' +
				", voucherFrom='" + voucherFrom + '\'' +
				", voucherFromPrivate='" + voucherFromPrivate + '\'' +
				", approveSign='" + approveSign + '\'' +
				", busOriType=" + busOriType +
				", cmdType='" + cmdType + '\'' +
				", policyId='" + policyId + '\'' +
				", tallySign='" + tallySign + '\'' +
				", tallyCode='" + tallyCode + '\'' +
				", busGlobalId='" + busGlobalId + '\'' +
				", noticeSign='" + noticeSign + '\'' +
				", bankSerialNo='" + bankSerialNo + '\'' +
				", voucherStat='" + voucherStat + '\'' +
				", voucherStatName='" + voucherStatName + '\'' +
				", transTime=" + transTime +
				", returnTime=" + returnTime +
				", firstReturnMsg='" + firstReturnMsg + '\'' +
				", nextReturnMsg='" + nextReturnMsg + '\'' +
				", status=" + status +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", midPayerAccNo='" + midPayerAccNo + '\'' +
				", midPayerAccName='" + midPayerAccName + '\'' +
				", midPayerBankName='" + midPayerBankName + '\'' +
				", backNode='" + backNode + '\'' +
				", backMsg='" + backMsg + '\'' +
				", handleOpinion='" + handleOpinion + '\'' +
				", expenseDtlIds='" + expenseDtlIds + '\'' +
				", backIds='" + backIds + '\'' +
				", backNodeIds='" + backNodeIds + '\'' +
				", excFrom='" + excFrom + '\'' +
				", delayTime=" + delayTime +
				'}';
	}
}
