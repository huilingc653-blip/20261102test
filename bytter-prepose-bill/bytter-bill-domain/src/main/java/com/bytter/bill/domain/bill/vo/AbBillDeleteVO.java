package com.bytter.bill.domain.bill.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
/**   
 * @Title: Vo
 * @Description: 票据删除
 * @author onlineGenerator
 * @date 2020-06-01
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class AbBillDeleteVO extends AbAccrecordsVO implements Serializable,IBusinessKey {
	private String id;
	/**票据主表id*/
	private String vid;
	/**票据从表id*/
	private String vvid;
	/**票据号码*/
	@BtCompositeSearch(fieldName="z.orig_No")
	private String origNo;
	/**票据类型：10 银票、11 商票*/
	@BtCompositeSearch(fieldName="z.bill_Type")
	@Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
	private Integer billType;
	/**票据类型名称：10 银票、11 商票*/
	private String billTypeName;
	/**持票单位*/
	@BtCompositeSearch(fieldName="s.id")
	private String corpId;
	@BtCompositeSearch(fieldName="c.corp_name")
	private String corpIdName;
	private String billStatus;
	/**开票数据来源*/
	@Dict(dictGroup = "AB_EMITBILL_SOURCE", inject = "sourceName")
	private String source;
	@BtCompositeSearch(fieldName="c.SOURCE")
	private String sourceName;
	/**流通介质：10 纸票、11 电票*/
	@Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
	@BtCompositeSearch(fieldName="z.bill_Flag")
	private Integer billFlag;
	/**流通介质名称：10 纸票、11 电票*/
	private String billFlagName;
	/**票据金额*/
	@BtCompositeSearch(fieldName="z.bill_Money")
	private java.math.BigDecimal billMoney;
	/**出票日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="z.start_Date")
	private java.util.Date startDate;
	/**到期日期*/
	@JsonFormat(pattern="yyyy-MM-dd")
	@BtCompositeSearch(fieldName="z.end_Date")
	private java.util.Date endDate;
	/**出票人*/
	@BtCompositeSearch(fieldName="z.payer")
	private String payer;
	/**收票人全称*/
	@BtCompositeSearch(fieldName="z.payee")
	private String payee;
	/**承兑人*/
	@BtCompositeSearch(fieldName="z.acceptor")
	private String acceptor;
	/**开出方式：0  对外开出；1 对内自开*/
	@Dict(dictGroup = "AB_BILL_OPENWAY", inject = "insideSingName")
	@BtCompositeSearch(fieldName="c.inside_Sing")
	private Integer insideSing;
	/**业务处理状态*/
	@Dict(dictGroup = "AB_BILL_DELETE_STATUS", inject = "statusName")
	@BtCompositeSearch(fieldName="l.status")
	private Integer status;
	private String statusName;
	/**业务状态*/
	@Dict(dictGroup = "AB_BRIEF_STATUS", inject = "briefIdName")
	@BtCompositeSearch(fieldName="l.brief_Id")
	private String briefId ;
	@BtCompositeSearch(fieldName="l.brief_Id")
	private String briefIdName;
	private java.util.Date date;
	private String ids;
	private String vids;
	private String vvids;
	private Integer checkboxHide;
	/**是否发送指令标志：0否； 1是 ；*/
	@BtCompositeSearch(fieldName="l.send_Sign")
	@Dict(dictGroup="AB_BILL_SEND_SIGN",inject = "sendSignName")
	private String sendSign;
	@BtCompositeSearch(fieldName="l.send_Sign")
	private String sendSignName;
	/** 申请单位 */
	@BtCompositeSearch(fieldName="c.APPLY_CORP_ID")
	@Corp(inject="applyCorpName")
	private String applyCorpId;
	/**申请单位名称*/
	@BtCompositeSearch(fieldName="c.APPLY_CORP_NAME")
	private String applyCorpName;
	/**申请单位代码*/
	private String applyCorpCode;
	/** 业务单位 */
	@Corp(inject="busCorpName")
	@BtCompositeSearch(fieldName="c.BUS_CORP_ID")
	private String busCorpId;
	/** 业务单位名称 */
	private String busCorpName;
	/** 业务人员 */
	@BtCompositeSearch(fieldName="c.BUS_PERSONNEL")
	private String busPersonnel;
	@BtCompositeSearch(fieldName="z.NEW_STM_ISSUOFPPR_IND")
	@Dict(dictGroup = "AB_BILL_NEW_STM_ISSUOFPPR_IND", inject = "newStmIssuofpprIndName")
	private String newStmIssuofpprInd;
	private String newStmIssuofpprIndName;
	/** 是否允许分包 */
	@BtCompositeSearch(fieldName="z.BLGRDBAGCRCL_PRMT_IND")
	@Dict(dictGroup = "AB_BILL_BLGRDBAGCRCL_PRMT_IND", inject = "blgrdbagcrclPrmtIndName")
	private String blgrdbagcrclPrmtInd;
	private String blgrdbagcrclPrmtIndName;
	/** 子票区间开始序号*/
	private String sbbllRngStrtSn;
	/** 子票区间结束序号 */
	private String sbbllRngEndSn;
	/** 标准金额 */
	private BigDecimal blStdAmt;
	/** 票据单位社会统一识别代码 */
	private String blHoldUnnIdNo;
	/**业务批次号 */
	@BtCompositeSearch(fieldName="c.BATCH_NO")
	private String batchNo;
	/**出票人统一信用代码 */
	private String blRmtrUnnIdNo;
	@BtCompositeSearch(fieldName="c.bank_type_id")
	private String bankTypeId;
	public String getSendSign() {
		return sendSign;
	}
	public void setSendSign(String sendSign) {
		this.sendSign = sendSign;
	}
	public String getSendSignName() {
		return sendSignName;
	}
	public void setSendSignName(String sendSignName) {
		this.sendSignName = sendSignName;
	}
	public Integer getCheckboxHide() {
		return checkboxHide;
	}
	public void setCheckboxHide(Integer checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getVvids() {
		return vvids;
	}
	public void setVvids(String vvids) {
		this.vvids = vvids;
	}
	public String getVids() {
		return vids;
	}
	public void setVids(String vids) {
		this.vids = vids;
	}
	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public String getVvid() {
		return vvid;
	}
	public void setVvid(String vvid) {
		this.vvid = vvid;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getBriefIdName() {
		return briefIdName;
	}
	public void setBriefIdName(String briefIdName) {
		this.briefIdName = briefIdName;
	}
	public String getCorpId() {
		return corpId;
	}
	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}
	public String getCorpIdName() {
		return corpIdName;
	}
	public void setCorpIdName(String corpIdName) {
		this.corpIdName = corpIdName;
	}
	public String getAcceptor() {
		return acceptor;
	}
	public void setAcceptor(String acceptor) {
		this.acceptor = acceptor;
	}
	public Integer getInsideSing() {
		return insideSing;
	}
	public void setInsideSing(Integer insideSing) {
		this.insideSing = insideSing;
	}
	public String getInsideSingName() {
		return insideSingName;
	}
	public void setInsideSingName(String insideSingName) {
		this.insideSingName = insideSingName;
	}
	private String insideSingName;
	public String getOrigNo() {
		return origNo;
	}
	public void setOrigNo(String origNo) {
		this.origNo = origNo;
	}
	public Integer getBillType() {
		return billType;
	}
	public void setBillType(Integer billType) {
		this.billType = billType;
	}
	public String getBillTypeName() {
		return billTypeName;
	}
	public void setBillTypeName(String billTypeName) {
		this.billTypeName = billTypeName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public Integer getBillFlag() {
		return billFlag;
	}
	public void setBillFlag(Integer billFlag) {
		this.billFlag = billFlag;
	}
	public String getBillFlagName() {
		return billFlagName;
	}
	public void setBillFlagName(String billFlagName) {
		this.billFlagName = billFlagName;
	}
	public java.math.BigDecimal getBillMoney() {
		return billMoney;
	}
	public void setBillMoney(java.math.BigDecimal billMoney) {
		this.billMoney = billMoney;
	}
	public java.util.Date getStartDate() {
		return startDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getBriefId() {
		return briefId;
	}
	public void setBriefId(String briefId) {
		this.briefId = briefId;
	}
	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}
	public java.util.Date getEndDate() {
		return endDate;
	}
	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	@Override
	public String getBusinessKey() {
		return id;
	}
	@Override
	public String getApplyCorpId() {
		return applyCorpId;
	}
	@Override
	public void setApplyCorpId(String applyCorpId) {
		this.applyCorpId = applyCorpId;
	}
	@Override
	public String getApplyCorpName() {
		return applyCorpName;
	}
	@Override
	public void setApplyCorpName(String applyCorpName) {
		this.applyCorpName = applyCorpName;
	}
	@Override
	public String getApplyCorpCode() {
		return applyCorpCode;
	}
	@Override
	public void setApplyCorpCode(String applyCorpCode) {
		this.applyCorpCode = applyCorpCode;
	}
	@Override
	public String getBusCorpId() {
		return busCorpId;
	}
	@Override
	public void setBusCorpId(String busCorpId) {
		this.busCorpId = busCorpId;
	}
	@Override
	public String getBusCorpName() {
		return busCorpName;
	}
	@Override
	public void setBusCorpName(String busCorpName) {
		this.busCorpName = busCorpName;
	}
	@Override
	public String getBusPersonnel() {
		return busPersonnel;
	}
	@Override
	public void setBusPersonnel(String busPersonnel) {
		this.busPersonnel = busPersonnel;
	}

	@Override
	public String getNewStmIssuofpprInd() {
		return newStmIssuofpprInd;
	}

	@Override
	public void setNewStmIssuofpprInd(String newStmIssuofpprInd) {
		this.newStmIssuofpprInd = newStmIssuofpprInd;
	}

	@Override
	public String getNewStmIssuofpprIndName() {
		return newStmIssuofpprIndName;
	}

	@Override
	public void setNewStmIssuofpprIndName(String newStmIssuofpprIndName) {
		this.newStmIssuofpprIndName = newStmIssuofpprIndName;
	}

	@Override
	public String getBlgrdbagcrclPrmtInd() {
		return blgrdbagcrclPrmtInd;
	}

	@Override
	public void setBlgrdbagcrclPrmtInd(String blgrdbagcrclPrmtInd) {
		this.blgrdbagcrclPrmtInd = blgrdbagcrclPrmtInd;
	}

	@Override
	public String getBlgrdbagcrclPrmtIndName() {
		return blgrdbagcrclPrmtIndName;
	}

	@Override
	public void setBlgrdbagcrclPrmtIndName(String blgrdbagcrclPrmtIndName) {
		this.blgrdbagcrclPrmtIndName = blgrdbagcrclPrmtIndName;
	}

	@Override
	public String getSbbllRngStrtSn() {
		return sbbllRngStrtSn;
	}

	@Override
	public void setSbbllRngStrtSn(String sbbllRngStrtSn) {
		this.sbbllRngStrtSn = sbbllRngStrtSn;
	}

	@Override
	public String getSbbllRngEndSn() {
		return sbbllRngEndSn;
	}

	@Override
	public void setSbbllRngEndSn(String sbbllRngEndSn) {
		this.sbbllRngEndSn = sbbllRngEndSn;
	}

	@Override
	public BigDecimal getBlStdAmt() {
		return blStdAmt;
	}

	@Override
	public void setBlStdAmt(BigDecimal blStdAmt) {
		this.blStdAmt = blStdAmt;
	}

	@Override
	public String getBlHoldUnnIdNo() {
		return blHoldUnnIdNo;
	}

	@Override
	public void setBlHoldUnnIdNo(String blHoldUnnIdNo) {
		this.blHoldUnnIdNo = blHoldUnnIdNo;
	}

	@Override
	public String getBatchNo() {
		return batchNo;
	}

	@Override
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@Override
	public String getBlRmtrUnnIdNo() {
		return blRmtrUnnIdNo;
	}

	@Override
	public void setBlRmtrUnnIdNo(String blRmtrUnnIdNo) {
		this.blRmtrUnnIdNo = blRmtrUnnIdNo;
	}

	@Override
	public String getBankTypeId() {
		return bankTypeId;
	}

	@Override
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}

	@Override
	public String toString() {
		return "AbBillDeleteVO{" +
				"id='" + id + '\'' +
				", vid='" + vid + '\'' +
				", vvid='" + vvid + '\'' +
				", origNo='" + origNo + '\'' +
				", billType=" + billType +
				", billTypeName='" + billTypeName + '\'' +
				", corpId='" + corpId + '\'' +
				", corpIdName='" + corpIdName + '\'' +
				", billStatus='" + billStatus + '\'' +
				", source='" + source + '\'' +
				", sourceName='" + sourceName + '\'' +
				", billFlag=" + billFlag +
				", billFlagName='" + billFlagName + '\'' +
				", billMoney=" + billMoney +
				", startDate=" + startDate +
				", endDate=" + endDate +
				", payer='" + payer + '\'' +
				", payee='" + payee + '\'' +
				", acceptor='" + acceptor + '\'' +
				", insideSing=" + insideSing +
				", status=" + status +
				", statusName='" + statusName + '\'' +
				", briefId='" + briefId + '\'' +
				", briefIdName='" + briefIdName + '\'' +
				", date=" + date +
				", ids='" + ids + '\'' +
				", vids='" + vids + '\'' +
				", vvids='" + vvids + '\'' +
				", checkboxHide=" + checkboxHide +
				", sendSign='" + sendSign + '\'' +
				", sendSignName='" + sendSignName + '\'' +
				", applyCorpId='" + applyCorpId + '\'' +
				", applyCorpName='" + applyCorpName + '\'' +
				", applyCorpCode='" + applyCorpCode + '\'' +
				", busCorpId='" + busCorpId + '\'' +
				", busCorpName='" + busCorpName + '\'' +
				", busPersonnel='" + busPersonnel + '\'' +
				", newStmIssuofpprInd='" + newStmIssuofpprInd + '\'' +
				", newStmIssuofpprIndName='" + newStmIssuofpprIndName + '\'' +
				", blgrdbagcrclPrmtInd='" + blgrdbagcrclPrmtInd + '\'' +
				", blgrdbagcrclPrmtIndName='" + blgrdbagcrclPrmtIndName + '\'' +
				", sbbllRngStrtSn='" + sbbllRngStrtSn + '\'' +
				", sbbllRngEndSn='" + sbbllRngEndSn + '\'' +
				", blStdAmt=" + blStdAmt +
				", blHoldUnnIdNo='" + blHoldUnnIdNo + '\'' +
				", batchNo='" + batchNo + '\'' +
				", blRmtrUnnIdNo='" + blRmtrUnnIdNo + '\'' +
				", bankTypeId='" + bankTypeId + '\'' +
				", insideSingName='" + insideSingName + '\'' +
				'}';
	}
}
