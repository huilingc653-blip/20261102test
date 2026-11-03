package com.bytter.biz.bdlinks.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.Region;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @Title: Entity
 * @Description: 收款指令VO
 * @author onlineGenerator
 * @date 2017年7月4日 16:02:15
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BisGatherCmdVO implements Serializable {
	/**指令ID*/
	private String id;
	/**指令编号*/
	@BtCompositeSearch(fieldName = "b.voucher_No")
	private String voucherNo;
	/**接口代码*/
	@BtCompositeSearch(fieldName = "b.bif_Code")
	//@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifCode;
	/**接口代码名称*/
	//@BtCompositeSearch(fieldName = "t.name")
	@BtCompositeSearch(fieldName = "b.bif_Code")
	private String bifName;
	/**调拨单ID*/
	private String cbsBillId;
	/**调拨单号*/
	@BtCompositeSearch(fieldName = "b.cbs_Bill_No")
	private String cbsBillNo;
	/**子账号*/
	@BtCompositeSearch(fieldName = "b.child_Acc_Id")
	private String childAccId;
	@BtCompositeSearch(fieldName = "b.child_Acc")
	private String childAcc;
	/**付款账号名称*/
	@BtCompositeSearch(fieldName = "b.child_Acc_Name")
	private String childAccName;
	/**付款开户行名称*/
	@BtCompositeSearch(fieldName = "b.child_bank")
	private String childBank;
	/**收方省*/
	//@Region(inject="childProvName")
	private String childProv;

	private String childProvName;
	/**收方市*/
	//@Region(inject="childCityName")
	private String childCity;

	private String childCityName;
	/**父账号*/
	@BtCompositeSearch(fieldName = "b.parent_Acc_Id")
	private String parentAccId;
	@BtCompositeSearch(fieldName = "b.parent_Acc")
	private String parentAcc;
	/**付款账号名称*/
	@BtCompositeSearch(fieldName = "b.parent_Acc_Name")
	private String parentAccName;
	/**付款开户行名称*/
	@BtCompositeSearch(fieldName = "b.parent_Bank")
	private String parentBank;
	/**付方省*/
	//@Region(inject="parentProvName")
	private String parentProv;

	private String parentProvName;
	/**付方市*/
	//@Region(inject="parentCityName")
	private String parentCity;

	private String parentCityName;
	/**金额*/
	@BtCompositeSearch(fieldName = "b.gather_amt")
	private BigDecimal gatherAmt;
	/**调拨策略ID*/
	private String gatherPolicyId;
	/**币别*/
//	@Currency(inject="curName")
	@BtCompositeSearch(fieldName = "b.cur_Id")
	private String curId;
	/**币别名称*/
	@BtCompositeSearch(fieldName = "b.cur_Id")
	private String curName;
	/**执行日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "b.run_time")
	private Date runTime;
	/**加急标志 1-是 0-否*/
	private String isQuick;
	/**跨行标识 0-同行 1-他行*/
	private String difBank;
	/**地域标识 0-同城 1-异地*/
	private String areaSign;
	/**用途*/
	@BtCompositeSearch(fieldName = "b.purpose")
	private String purpose;
	/**附言*/
	@BtCompositeSearch(fieldName = "b.add_Word")
	private String addWord;
	/**指令来源*/
	@BtCompositeSearch(fieldName = "b.voucher_From")
	//@Dict(dictGroup = "bisExc.voucherFrom", inject = "voucherFromName")
	private String voucherFrom;
	@BtCompositeSearch(fieldName = "b.voucher_From")
	private String voucherFromName;
	/**业务类型*/
	@BtCompositeSearch(fieldName = "b.bus_Ori_Type")
	//@Dict(dictGroup = "bisExc.busOriType", inject = "busOriTypeName")
	private Integer busOriType;
	@BtCompositeSearch(fieldName = "b.bus_Ori_Type")
	private String busOriTypeName;
	/**指令类型*/
	@BtCompositeSearch(fieldName = "b.cmd_Type")
	//@Dict(dictGroup = "bisExc.cmdType", inject = "cmdTypeName")
	private String cmdType;
	@BtCompositeSearch(fieldName = "b.cmd_Type")
	private String cmdTypeName;
	/**消息提醒标志*/
	private String noticeSign;
	/**银行流水ID*/
	private String dtlId;
	/**银行流水号*/
	@BtCompositeSearch(fieldName = "b.bank_Serial_No")
	private String bankSerialNo;
	/**交易状态*/
	@BtCompositeSearch(fieldName = "b.cmd_stat")
	//@Dict(dictGroup = "bisExc.voucherStat", inject = "cmdStatName")
	private String cmdStat;
	@BtCompositeSearch(fieldName = "b.cmd_stat")
	private String cmdStatName;
	/**交易时间*/
	@BtCompositeSearch(fieldName = "b.trans_Time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	/**创建人登录名称*/
	private String createBy;
	/**创建日期*/
	private Date createDate;
	/**更新人登录名称*/
	private String updateBy;
	/**更新日期*/
	private Date updateDate;
	/**指令重发标志*/
	private String resendSign;
	/**校验密码*/
	private String validPassword;
	/**处理意见*/
	private String handleOpinion;
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
	public String getBifName() {
		return bifName;
	}
	public void setBifName(String bifName) {
		this.bifName = bifName;
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
	public String getVoucherFromName() {
		return voucherFromName;
	}
	public void setVoucherFromName(String voucherFromName) {
		this.voucherFromName = voucherFromName;
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
	public String getChildProv() {
		return childProv;
	}
	public void setChildProv(String childProv) {
		this.childProv = childProv;
	}
	public String getChildProvName() {
		return childProvName;
	}
	public void setChildProvName(String childProvName) {
		this.childProvName = childProvName;
	}
	public String getChildCity() {
		return childCity;
	}
	public void setChildCity(String childCity) {
		this.childCity = childCity;
	}
	public String getChildCityName() {
		return childCityName;
	}
	public void setChildCityName(String childCityName) {
		this.childCityName = childCityName;
	}
	public String getParentProv() {
		return parentProv;
	}
	public void setParentProv(String parentProv) {
		this.parentProv = parentProv;
	}
	public String getParentProvName() {
		return parentProvName;
	}
	public void setParentProvName(String parentProvName) {
		this.parentProvName = parentProvName;
	}
	public String getParentCity() {
		return parentCity;
	}
	public void setParentCity(String parentCity) {
		this.parentCity = parentCity;
	}
	public String getParentCityName() {
		return parentCityName;
	}
	public void setParentCityName(String parentCityName) {
		this.parentCityName = parentCityName;
	}
	public String getResendSign() {
		return resendSign;
	}
	public void setResendSign(String resendSign) {
		this.resendSign = resendSign;
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
	public String getCmdStatName() {
		return cmdStatName;
	}
	public void setCmdStatName(String cmdStatName) {
		this.cmdStatName = cmdStatName;
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

	@Override
	public String toString() {
		return "BisGatherCmdVO{" +
				"id='" + id + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", bifCode='" + bifCode + '\'' +
				", bifName='" + bifName + '\'' +
				", cbsBillId='" + cbsBillId + '\'' +
				", cbsBillNo='" + cbsBillNo + '\'' +
				", childAccId='" + childAccId + '\'' +
				", childAcc='" + childAcc + '\'' +
				", childAccName='" + childAccName + '\'' +
				", childBank='" + childBank + '\'' +
				", childProv='" + childProv + '\'' +
				", childProvName='" + childProvName + '\'' +
				", childCity='" + childCity + '\'' +
				", childCityName='" + childCityName + '\'' +
				", parentAccId='" + parentAccId + '\'' +
				", parentAcc='" + parentAcc + '\'' +
				", parentAccName='" + parentAccName + '\'' +
				", parentBank='" + parentBank + '\'' +
				", parentProv='" + parentProv + '\'' +
				", parentProvName='" + parentProvName + '\'' +
				", parentCity='" + parentCity + '\'' +
				", parentCityName='" + parentCityName + '\'' +
				", gatherAmt=" + gatherAmt +
				", gatherPolicyId='" + gatherPolicyId + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", runTime=" + runTime +
				", isQuick='" + isQuick + '\'' +
				", difBank='" + difBank + '\'' +
				", areaSign='" + areaSign + '\'' +
				", purpose='" + purpose + '\'' +
				", addWord='" + addWord + '\'' +
				", voucherFrom='" + voucherFrom + '\'' +
				", busOriType=" + busOriType +
				", cmdType='" + cmdType + '\'' +
				", noticeSign='" + noticeSign + '\'' +
				", dtlId='" + dtlId + '\'' +
				", bankSerialNo='" + bankSerialNo + '\'' +
				", cmdStat='" + cmdStat + '\'' +
				", transTime=" + transTime +
				", returnTime=" + returnTime +
				", firstReturnMsg='" + firstReturnMsg + '\'' +
				", nextReturnMsg='" + nextReturnMsg + '\'' +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				'}';
	}
}
