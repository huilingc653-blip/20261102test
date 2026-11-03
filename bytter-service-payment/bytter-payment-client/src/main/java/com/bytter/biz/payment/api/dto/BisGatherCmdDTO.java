package com.bytter.biz.payment.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * Title: DTO
 * Description: BIS_GATHER_CMD
 * @author huangx
 * @since 2017年7月5日 08:53:28
 * @version 1.0
 *
 */
public class BisGatherCmdDTO implements Serializable {
	/**指令ID*/
	private String id;
	/**指令编号*/
	private String voucherNo;
	/**接口代码*/
	private String bifCode;
	/**调拨单ID*/
	private String cbsBillId;
	/**调拨单号*/
	private String cbsBillNo;
	/**子账号*/
	private String childAccId;
	private String childAcc;
	/**付款账号名称*/
	private String childAccName;
	/**付款开户行名称*/
	private String childBank;
	/**父账号*/
	private String parentAccId;
	private String parentAcc;
	/**付款账号名称*/
	private String parentAccName;
	/**付款开户行名称*/
	private String parentBank;
	/**金额*/
	private BigDecimal gatherAmt;
	/**调拨策略ID*/
	private String gatherPolicyId;
	/**币别*/
	private String curId;
	/**币别名称*/
	private String curName;
	/**执行日期*/
	private Date runTime;
	/**加急标志 1-是 0-否*/
	private String isQuick;
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
	/**业务类型*/
	private Integer busOriType;
	/**指令类型*/
	private String cmdType;
	/**消息提醒标志*/
	private String noticeSign;
	/**银行流水ID*/
	private String dtlId;
	/**银行流水号*/
	private String bankSerialNo;
	/**交易状态*/
	private String cmdStat;
	/**交易时间*/
	private Date transTime;
	/**返回时间*/
	private Date returnTime;
	/**返回信息（交易前）*/
	private String firstReturnMsg;
	/**返回信息（交易后）*/
	private String nextReturnMsg;
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**退回原因*/
	private String backMsg;
	/**
	 * 单位ID
	 */
	private String corpId;

	/**指令重发标志*/
	private String resendSign;
	/**签名*/
	private String signatureCode;
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
	public String getBifCode() {
		return bifCode;
	}
	public void setBifCode(String bifCode) {
		this.bifCode = bifCode;
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
	public String getChildAccId() {
		return childAccId;
	}
	public void setChildAccId(String childAccId) {
		this.childAccId = childAccId;
	}
	public String getChildAcc() {
		return childAcc;
	}
	public void setChildAcc(String childAcc) {
		this.childAcc = childAcc;
	}
	public String getChildAccName() {
		return childAccName;
	}
	public void setChildAccName(String childAccName) {
		this.childAccName = childAccName;
	}
	public String getChildBank() {
		return childBank;
	}
	public void setChildBank(String childBank) {
		this.childBank = childBank;
	}
	public String getParentAccId() {
		return parentAccId;
	}
	public void setParentAccId(String parentAccId) {
		this.parentAccId = parentAccId;
	}
	public String getParentAcc() {
		return parentAcc;
	}
	public void setParentAcc(String parentAcc) {
		this.parentAcc = parentAcc;
	}
	public String getParentAccName() {
		return parentAccName;
	}
	public void setParentAccName(String parentAccName) {
		this.parentAccName = parentAccName;
	}
	public String getParentBank() {
		return parentBank;
	}
	public void setParentBank(String parentBank) {
		this.parentBank = parentBank;
	}
	public BigDecimal getGatherAmt() {
		return gatherAmt;
	}
	public void setGatherAmt(BigDecimal gatherAmt) {
		this.gatherAmt = gatherAmt;
	}
	public String getGatherPolicyId() {
		return gatherPolicyId;
	}
	public void setGatherPolicyId(String gatherPolicyId) {
		this.gatherPolicyId = gatherPolicyId;
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
	public Date getRunTime() {
		return runTime;
	}
	public void setRunTime(Date runTime) {
		this.runTime = runTime;
	}
	public String getIsQuick() {
		return isQuick;
	}
	public void setIsQuick(String isQuick) {
		this.isQuick = isQuick;
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
	public String getNoticeSign() {
		return noticeSign;
	}
	public void setNoticeSign(String noticeSign) {
		this.noticeSign = noticeSign;
	}
	public String getDtlId() {
		return dtlId;
	}
	public void setDtlId(String dtlId) {
		this.dtlId = dtlId;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public String getCmdStat() {
		return cmdStat;
	}
	public void setCmdStat(String cmdStat) {
		this.cmdStat = cmdStat;
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
	public String getBackMsg() {
		return backMsg;
	}
	public void setBackMsg(String backMsg) {
		this.backMsg = backMsg;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getResendSign() {
		return resendSign;
	}

	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
	}

	public String getSignatureCode() {
		return signatureCode;
	}

	public void setSignatureCode(String signatureCode) {
		this.signatureCode = signatureCode;
	}
}
