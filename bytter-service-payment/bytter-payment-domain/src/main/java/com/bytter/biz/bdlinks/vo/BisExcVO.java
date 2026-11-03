package com.bytter.biz.bdlinks.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 付款指令VO
 * @author onlineGenerator
 * @date 2017-06-29 15:20:33
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BisExcVO implements Serializable,IBusinessKey {
	/**ID*/
	private String id;
	/**指令编号*/
	@BtCompositeSearch(fieldName = "b.voucher_No")
	private String voucherNo;
	/**付款单ID*/
	private String cbsBillId;
	/**付款单号*/
	@BtCompositeSearch(fieldName = "b.cbs_Bill_No")
	private String cbsBillNo;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "b.corp_Id")
	//@Corp(inject="corpName")
	private String corpId;
	/**付款账号对应的单位ID*/
	private String payerCorpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName = "b.corp_Id")
	private String corpName;
	/**中心账号*/
	private String counterAcc;
	/**申请日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "b.req_Date")
	private Date reqDate;
	/**付款账号*/
	@BtCompositeSearch(fieldName = "b.payer_Acc")
	private String payerAcc;
	/**付款账号名称*/
	@BtCompositeSearch(fieldName = "b.payer_Acc_Name")
	private String payerAccName;
	/**付款开户行名称*/
	@BtCompositeSearch(fieldName = "b.payer_Bank")
	private String payerBank;
	/**付方市*/
	//@Region(inject="payerCityName")
	private String payerCity;
	private String payerCityName;
	/**付方省*/
	//@Region(inject="payerProvName")
	private String payerProv;
	private String payerProvName;
	/**币别*/
	//@Currency(inject="curName")
	@BtCompositeSearch(fieldName = "b.cur_id")
	private String curId;
	/**币别名称*/
	@BtCompositeSearch(fieldName = "b.cur_id")
	private String curName;
	/**金额*/
	@BtCompositeSearch(fieldName = "b.pay_Money")
	private java.math.BigDecimal payMoney;
	/**付款方式*/
	@BtCompositeSearch(fieldName = "b.voucher_type_id")
	//@Dict(tableName="FC_VOUCHER_TYPE",valueFieldName="id",showFiledName="VOUCHER_NAME",inject="voucherTypeName")
	private String voucherTypeId;
	/**付款方式名称*/
	@BtCompositeSearch(fieldName = "b.voucher_type_id")
	private String voucherTypeName;
	/**收款账号*/
	@BtCompositeSearch(fieldName = "b.payee_Acc")
	private String payeeAcc;
	/**收款账号名称*/
	@BtCompositeSearch(fieldName = "b.payee_Acc_Name")
	private String payeeAccName;
	/**收款方开户行行号*/
	private String payeeBankCode;
	/**收款开户行名称*/
	@BtCompositeSearch(fieldName = "b.payee_Bank")
	private String payeeBank;
	/**收方省*/
//	@Region(inject="payeeProvName")
	private String payeeProv;
	private String payeeProvName;
	/**收方市*/
//	@Region(inject="payeeCityName")
	private String payeeCity;
	private String payeeCityName;
	/**期望支付日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "b.wish_Payday")
	private Date wishPayday;
	/**加急标志 1-是 0-否*/
	private String isQuick;
	/**交易模式 3-对公 4-对私*/
	private String transMode;
	/**接口代码*/
	@BtCompositeSearch(fieldName = "b.bif_Code")
	//@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifCode;
	/**接口代码名称*/
//	@BtCompositeSearch(fieldName = "t.name")
	@BtCompositeSearch(fieldName = "b.bif_Code")
	private String bifName;
	/**跨行标识 0-同行 1-他行*/
	private String difBank;
	/**地域标识 0-同城 1-异地*/
	private String areaSign;
	/**用途*/

	@BtCompositeSearch(fieldName = "b.purpose")
	private String purpose;
	/**附言*/
	@BtCompositeSearch(fieldName = "b.add_word")
	private String addWord;
	/**指令来源*/
	@BtCompositeSearch(fieldName = "b.voucher_From")
	//@Dict(dictGroup = "bisExc.voucherFrom", inject = "voucherFromName")
	private String voucherFrom;
	private String voucherFromName;
	/**对私付款指令来源 0-付款办理 1-费用报销*/
	private String voucherFromPrivate;
	/**审批标识*/
	private String approveSign;
	/**业务类型*/
	@BtCompositeSearch(fieldName = "b.bus_Ori_Type")
	//@Dict(dictGroup = "bisExc.busOriType", inject = "busOriTypeName")
	private Integer busOriType;
	private String busOriTypeName;
	/**指令类型*/
	@BtCompositeSearch(fieldName = "b.cmd_Type")
	//@Dict(dictGroup = "bisExc.cmdType", inject = "cmdTypeName")
	private String cmdType;
	@BtCompositeSearch(fieldName = "b.cmd_Type")
	private String cmdTypeName;
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
	@BtCompositeSearch(fieldName = "b.voucher_Stat")
	//@Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
	private String voucherStat;
	private String voucherStatName;
	/**交易时间*/
	@BtCompositeSearch(fieldName = "b.trans_Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date transTime;
	/**返回时间*/
	@BtCompositeSearch(fieldName = "b.return_Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date returnTime;
	/**返回信息（交易前）*/
	@BtCompositeSearch(fieldName = "b.first_Return_Msg")
	private String firstReturnMsg;
	/**返回信息（交易后）*/
	@BtCompositeSearch(fieldName = "b.next_Return_Msg")
	private String nextReturnMsg;
	/**状态*/
	@BtCompositeSearch(fieldName = "b.status")
	private Integer status;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**失败原因*/
	private String failReason;
	/**时间戳*/
	private String timeStamp;
	/**加密信息*/
	private String verifyCode;
	/**指令重发标志*/
	private String resendSign;
	/**退回节点 0-制单处，1-记账*/
	private String backNode;
	/**退回原因*/
	private String backMsg;
	/**校验密码*/
	private String validPassword;
	/**处理意见*/
	private String handleOpinion;
	/**是否禁用*/
	private String checkboxHide;
	/**等待结果明细ID*/
	private String expenseDtlIds;
	/**退回ID集合（制单处）*/
	private String backIds;
	/**退回ID集合（选择退回环节）*/
	private String backNodeIds;
	/**系统结算方式*/
	private String sysVoucherCode;
	/**是否是特殊情况（下拨成功，外付失败）*/
	private String isSpecFailForTranPay;
	/**中间付款帐号*/
	private String midPayerAccNo;
	/**中间付款人*/
	private String midPayerAccName;
	/**中间付款开户行*/
	private String midPayerBankName;
	/**指令数据来源：0-bis_Exc 1-bis_Exc_Proc*/
	private String excFrom;
	/**跨行转付在下拨指令成功后延迟支付时间*/
	private Integer delayTime;
	private String opCode;
	/**付款单位*/
	private String payerCorp;
	/**收款单位*/
	private String payeeCorp;
	/**付款银行类型*/
	private String payerBankType;
	/**收款银行类型*/
	private String payeeBankType;
	/**需求单类型*/
	private String demandType;
	/**共享单号*/
	private String bursarBillNo;

	/**需求单金额*/
	private BigDecimal demandMoney;
	/**需求单生成了多少调拨单*/
	private Integer posNum;

	private String tenantId;

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
	public java.lang.String getCbsBillNo() {
		return cbsBillNo;
	}
	public void setCbsBillNo(java.lang.String cbsBillNo) {
		this.cbsBillNo = cbsBillNo;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getPayerCorpId() {
		return payerCorpId;
	}
	public void setPayerCorpId(String payerCorpId) {
		this.payerCorpId = payerCorpId;
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
	public java.math.BigDecimal getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(java.math.BigDecimal payMoney) {
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
	public String getVoucherFromName() {
		return voucherFromName;
	}
	public void setVoucherFromName(String voucherFromName) {
		this.voucherFromName = voucherFromName;
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
	public String getFailReason() {
		return failReason;
	}
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getResendSign() {
		return resendSign;
	}
	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
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
	public String getValidPassword() {
		return validPassword;
	}
	public void setValidPassword(String validPassword) {
		this.validPassword = validPassword;
	}
	public String getHandleOpinion() {
		return handleOpinion;
	}
	public void setHandleOpinion(String handleOpinion) {
		this.handleOpinion = handleOpinion;
	}
	public String getCheckboxHide() {
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
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
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public String getIsSpecFailForTranPay() {
		return isSpecFailForTranPay;
	}
	public void setIsSpecFailForTranPay(String isSpecFailForTranPay) {
		this.isSpecFailForTranPay = isSpecFailForTranPay;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
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
	public String getBusOriTypeName() {
		return busOriTypeName;
	}
	public void setBusOriTypeName(String busOriTypeName) {
		this.busOriTypeName = busOriTypeName;
	}
	public String getCmdTypeName() {
		return cmdTypeName;
	}
	public void setCmdTypeName(String cmdTypeName) {
		this.cmdTypeName = cmdTypeName;
	}
	public String getVoucherStatName() {
		return voucherStatName;
	}
	public void setVoucherStatName(String voucherStatName) {
		this.voucherStatName = voucherStatName;
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

	public String getOpCode() {
		return opCode;
	}

	public void setOpCode(String opCode) {
		this.opCode = opCode;
	}

	public String getPayerCorp() {
		return payerCorp;
	}

	public void setPayerCorp(String payerCorp) {
		this.payerCorp = payerCorp;
	}

	public String getPayeeCorp() {
		return payeeCorp;
	}

	public void setPayeeCorp(String payeeCorp) {
		this.payeeCorp = payeeCorp;
	}

	public String getPayerBankType() {
		return payerBankType;
	}

	public void setPayerBankType(String payerBankType) {
		this.payerBankType = payerBankType;
	}

	public String getPayeeBankType() {
		return payeeBankType;
	}

	public void setPayeeBankType(String payeeBankType) {
		this.payeeBankType = payeeBankType;
	}

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public String getBursarBillNo() {
		return bursarBillNo;
	}

	public void setBursarBillNo(String bursarBillNo) {
		this.bursarBillNo = bursarBillNo;
	}

	public BigDecimal getDemandMoney() {
		return demandMoney;
	}

	public void setDemandMoney(BigDecimal demandMoney) {
		this.demandMoney = demandMoney;
	}

	@Override
	public String getBusinessKey() {
		return getId();
	}

	public Integer getPosNum() {
		return posNum;
	}

	public void setPosNum(Integer posNum) {
		this.posNum = posNum;
	}

	@Override
	public String toString() {
		return "BisExcVO{" +
				"id='" + id + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", cbsBillId='" + cbsBillId + '\'' +
				", cbsBillNo='" + cbsBillNo + '\'' +
				", corpId='" + corpId + '\'' +
				", corpName='" + corpName + '\'' +
				", counterAcc='" + counterAcc + '\'' +
				", reqDate=" + reqDate +
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
				", transTime=" + transTime +
				", returnTime=" + returnTime +
				", firstReturnMsg='" + firstReturnMsg + '\'' +
				", nextReturnMsg='" + nextReturnMsg + '\'' +
				", status=" + status +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", demandMoney=" + demandMoney +
				", posNum=" + posNum +
				'}';
	}
}
