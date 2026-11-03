package com.bytter.biz.bdlink.vo;
import java.io.Serializable;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @Title: Entity
 * @Description: 查询指令VO
 * @author onlineGenerator
 * @date 2017-12-29 15:20:33
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class BisQueryCmdVO implements Serializable  {
	/**ID*/
	private String id;
	/**指令编号*/
	@BtCompositeSearch(fieldName = "b.voucher_no")
	private String voucherNo;
	/**交易类型*/
	@BtCompositeSearch(fieldName = "b.trans_Id")
	//@Dict(dictGroup = "bisQueryCmd.transId", inject = "transName")
	private String transId;

	private String transName;
	/**发送日期*/
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@BtCompositeSearch(fieldName = "b.send_Time")
	private Date sendTime;
	/**账号ID*/
	private String bankAccId;
	/**账号*/
	@BtCompositeSearch(fieldName = "b.bank_Acc")
	private String bankAcc;
	/**账号名称*/
	@BtCompositeSearch(fieldName = "b.acc_Name")
	private String accName;
	/**账号开户行*/
	@BtCompositeSearch(fieldName = "b.bank_Name")
	private String bankName;
	/**币别*/
	//@Currency(inject="curName")
	@BtCompositeSearch(fieldName = "b.cur_id")
	private String curId;
	/**币别名称*/
	@BtCompositeSearch(fieldName = "b.cur_id")
	private String curName;
	/**开始日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "b.start_Time")
	private Date startTime;
	/**结束日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	@BtCompositeSearch(fieldName = "b.end_Time")
	private Date endTime;
	/**接口代码*/
	@BtCompositeSearch(fieldName = "b.bif_Code")
	////@Dict(tableName="BIS_BIF_INIT",valueFieldName="id",showFiledName="NAME",inject="bifName")
	private String bifCode;
	/**接口代码名称*/
	@BtCompositeSearch(fieldName = "b.bif_Code")
	private String bifName;
	/**指令类型*/
	//@Dict(dictGroup = "bisExc.cmdType", inject = "cmdTypeName")
	private String cmdType;
	private String cmdTypeName;
	/**指令状态*/
	@BtCompositeSearch(fieldName = "b.cmd_Stat")
//	//@Dict(dictGroup = "bisExc.voucherStat", inject = "cmdStatName")
	private String cmdStat;
	@BtCompositeSearch(fieldName = "b.cmd_Stat")
	private String cmdStatName;
	/**备注*/
	private String rmk;
	/**交易时间*/
	private Date transTime;
	/**返回时间*/
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
	/**账号ID*/
	private String bankAccIds;
	/**账号*/
	private String bankAccs;
	/**币别*/
	private String curIds;
	/**接口代码*/
	private String bifCodes;
	/**单位ID*/
	@BtCompositeSearch(fieldName = "b.corp_Id")
//	@Corp(inject="corpName")
	private String corpId;
	/**单位名称*/
	@BtCompositeSearch(fieldName = "b.corp_Id")
	private String corpName;

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
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getBankAccId() {
		return bankAccId;
	}
	public void setBankAccId(String bankAccId) {
		this.bankAccId = bankAccId;
	}
	public String getBankAcc() {
		return bankAcc;
	}
	public void setBankAcc(String bankAcc) {
		this.bankAcc = bankAcc;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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
	public String getCmdType() {
		return cmdType;
	}
	public void setCmdType(String cmdType) {
		this.cmdType = cmdType;
	}
	public String getCmdStat() {
		return cmdStat;
	}
	public void setCmdStat(String cmdStat) {
		this.cmdStat = cmdStat;
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
	public String getBankAccIds() {
		return bankAccIds;
	}
	public void setBankAccIds(String bankAccIds) {
		this.bankAccIds = bankAccIds;
	}
	public String getBankAccs() {
		return bankAccs;
	}
	public void setBankAccs(String bankAccs) {
		this.bankAccs = bankAccs;
	}
	public String getCurIds() {
		return curIds;
	}
	public void setCurIds(String curIds) {
		this.curIds = curIds;
	}
	public String getBifCodes() {
		return bifCodes;
	}
	public void setBifCodes(String bifCodes) {
		this.bifCodes = bifCodes;
	}
	public String getTransName() {
		return transName;
	}
	public void setTransName(String transName) {
		this.transName = transName;
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
	@Override
	public String toString() {
		return "BisQueryCmdVO{" +
				"id='" + id + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", transId='" + transId + '\'' +
				", sendTime=" + sendTime +
				", bankAccId='" + bankAccId + '\'' +
				", bankAcc='" + bankAcc + '\'' +
				", accName='" + accName + '\'' +
				", bankName='" + bankName + '\'' +
				", curId='" + curId + '\'' +
				", curName='" + curName + '\'' +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", bifCode='" + bifCode + '\'' +
				", bifName='" + bifName + '\'' +
				", cmdType='" + cmdType + '\'' +
				", cmdStat='" + cmdStat + '\'' +
				", rmk='" + rmk + '\'' +
				", transTime=" + transTime +
				", returnTime=" + returnTime +
				", firstReturnMsg='" + firstReturnMsg + '\'' +
				", nextReturnMsg='" + nextReturnMsg + '\'' +
				", status=" + status +
				", createBy='" + createBy + '\'' +
				", createDate=" + createDate +
				", updateBy='" + updateBy + '\'' +
				", updateDate=" + updateDate +
				", bankAccIds='" + bankAccIds + '\'' +
				", bankAccs='" + bankAccs + '\'' +
				", curIds='" + curIds + '\'' +
				", bifCodes='" + bifCodes + '\'' +
				'}';
	}
}
