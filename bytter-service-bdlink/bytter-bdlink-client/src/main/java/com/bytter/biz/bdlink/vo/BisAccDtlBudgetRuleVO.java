package com.bytter.biz.bdlink.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.NumberFormat;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 银行账号流水明细表
 * @date 2017-06-29 13:51:01
 */
@SuppressWarnings("serial")
public class BisAccDtlBudgetRuleVO implements java.io.Serializable {
	/**
	 * 序列号
	 */
	private String id;
	/**
	 * 单号
	 */
	private String accNo;
	/**
	 * 交易时间
	 */
	@BtCompositeSearch(fieldName = "t.transTime")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private Date transTime;
	private String transTimeStr;
	/**
	 * 单位ID
	 */
	@BtCompositeSearch(fieldName = "t.corpId")
	private String corpId;
	/**
	 * 单位
	 */
	@BtCompositeSearch(fieldName = "t.corpId")
	private String corpName;
	/**
	 * 收款账号
	 */
	@BtCompositeSearch(fieldName = "t.bankAcc")
	private String bankAcc;
	/**
	 * 收款账户名称
	 */
	@BtCompositeSearch(fieldName = "t.accName")
	private String accName;
	/**
	 * 收款账户开户行
	 */
	private String bankName;
	/**
	 * 币别ID
	 */
	@BtCompositeSearch(fieldName = "t.curId")
	private String curId;
	/**
	 * 币别
	 */
	@BtCompositeSearch(fieldName = "t.curName")
	private String curName;
	/**
	 * 交易金额
	 */
	@BtCompositeSearch(fieldName = "t.amt")
	@NumberFormat(pattern = "#,##0.00")
	private BigDecimal amt;
	/**
	 * 付款账号
	 */
	@BtCompositeSearch(fieldName = "t.oppAccNo")
	private String oppAccNo;
	/**
	 * 付款账户名称
	 */
	@BtCompositeSearch(fieldName = "t.oppAccName")
	private String oppAccName;
	/**
	 * 付款账户开户行
	 */
	@BtCompositeSearch(fieldName = "t.oppAccBank")
	private String oppAccBank;
	/**
	 * 摘要
	 */
	@BtCompositeSearch(fieldName = "t.remark")
	private String remark;
	/**
	 * 收支标志 0-收 1-支
	 */
	@BtCompositeSearch(fieldName = "t.cdSign")
	private String cdSign;
	private String cdSignName;
	/**
	 * 科目信息
	 */
	private String itemInfo;
	/**
	 * 状态 0 - 未确认 1 - 已确认 -2 - 已删除
	 */
	private String fbsStatus;
	private String fbsStatusName;
	/**
	 * 银行类别
	 */
	@BtCompositeSearch(fieldName = "t.bankTypeId")
	private String bankTypeId;
	/**
	 * 执行时期
	 */
	private String processDate;
	private Date processDateTime;
	/**
	 * 预算科目
	 */
	private String budget;
	/**
	 * 结算类型
	 * */
	@Dict(dictGroup = "settlType", inject = "settlTypeName")
	private String settlType;
	private String settlTypeName;
	/**是否隐藏 1：隐藏*/
	private String checkboxHide;
	/**
	 * 币别汇率
	 */
	private BigDecimal curExrate;
	/**
	 * 业务类型
	 */
	private String busType;
	/**
	 * 业务类型名称
	 */
	private String busTypeName;
	/**
	 * 备注
	 */
	private String rmk;
	/**
	 * 单号
	 */
	private String billNo;
	/**
	 * 银行流水号
	 */
	private String bankSerialNo;
	/**
	 * 业务单id
	 */
	private String billId;
	/**
	 * 合并后单据ID
	 */
	private String mergeId;
	/**
	 * 拆分前单据ID
	 */
	private String splitId;
	/**
	 * 状态
	 */
	private Integer status;
	/**
	 * 操作类型
	 */
	private Integer messageType;
	/**
	 * 用途
	 */
	private java.lang.String uses;
	/**
	 * 附言
	 */
	private java.lang.String postscript;
	/**
	 * 单位是否受控
	 */
	private Boolean control;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		if(!EmptyUtils.isEmpty(transTime)) {
			this.transTime = transTime;
			this.transTimeStr = DateUtils.formatDate(transTime, "yyyy-MM-dd HH:mm:ss");
		} else {
			this.transTime = DateUtils.getDate();
			this.transTimeStr = DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd HH:mm:ss");
		}
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
	public BigDecimal getAmt() {
		return amt;
	}
	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}
	public String getOppAccNo() {
		return oppAccNo;
	}
	public void setOppAccNo(String oppAccNo) {
		this.oppAccNo = oppAccNo;
	}
	public String getOppAccName() {
		return oppAccName;
	}
	public void setOppAccName(String oppAccName) {
		this.oppAccName = oppAccName;
	}
	public String getOppAccBank() {
		return oppAccBank;
	}
	public void setOppAccBank(String oppAccBank) {
		this.oppAccBank = oppAccBank;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCdSign() {
		return cdSign;
	}
	public void setCdSign(String cdSign) {
		this.cdSign = cdSign;
	}
	public String getCdSignName() {
		return cdSignName;
	}
	public void setCdSignName(String cdSignName) {
		this.cdSignName = cdSignName;
	}
	public String getItemInfo() {
		return itemInfo;
	}
	public void setItemInfo(String itemInfo) {
		this.itemInfo = itemInfo;
	}
	public String getFbsStatus() {
		return fbsStatus;
	}
	public void setFbsStatus(String fbsStatus) {
		this.fbsStatus = fbsStatus;
		if(EmptyUtils.isEmpty(fbsStatus)){
			this.fbsStatusName = "未确认";
		}
	}
	public String getFbsStatusName() {
		return fbsStatusName;
	}
	public void setFbsStatusName(String fbsStatusName) {
		this.fbsStatusName = fbsStatusName;
	}
	public String getBankTypeId() {
		return bankTypeId;
	}
	public void setBankTypeId(String bankTypeId) {
		this.bankTypeId = bankTypeId;
	}
	public String getProcessDate() {
		if(!EmptyUtils.isEmpty(this.processDateTime)){
			this.processDate = DateUtils.formatDate(this.processDateTime);
		}
		return processDate;
	}
	public void setProcessDate(String processDate) {
		this.processDate = processDate;
	}
	public String getBudget() {
		return budget;
	}
	public void setBudget(String budget) {
		this.budget = budget;
	}
	public Date getProcessDateTime() {
		return processDateTime;
	}
	public void setProcessDateTime(Date processDateTime) {
		this.processDateTime = processDateTime;
		if(!EmptyUtils.isEmpty(this.processDateTime)) {
			this.processDate = DateUtils.formatDate(processDateTime);
		}
	}
	public String getSettlType() {
		return settlType;
	}
	public void setSettlType(String settlType) {
		this.settlType = settlType;
	}
	public String getCheckboxHide() {
		return checkboxHide;
	}
	public void setCheckboxHide(String checkboxHide) {
		this.checkboxHide = checkboxHide;
	}
	public BigDecimal getCurExrate() {
		return curExrate;
	}
	public void setCurExrate(BigDecimal curExrate) {
		this.curExrate = curExrate;
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
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	public String getBillNo() {
		return billNo;
	}
	public String getBankSerialNo() {
		return bankSerialNo;
	}
	public void setBankSerialNo(String bankSerialNo) {
		this.bankSerialNo = bankSerialNo;
	}
	public String getBillId() {
		return billId;
	}
	public void setBillId(String billId) {
		this.billId = billId;
	}
	public String getMergeId() {
		return mergeId;
	}
	public void setMergeId(String mergeId) {
		this.mergeId = mergeId;
	}
	public String getSplitId() {
		return splitId;
	}
	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getUses() {
		return uses;
	}
	public void setUses(String uses) {
		this.uses = uses;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getTransTimeStr() {
		return transTimeStr;
	}
	public void setTransTimeStr(String transTimeStr) {
		this.transTimeStr = transTimeStr;
	}
	public Boolean getControl() {
		return control;
	}
	public void setControl(Boolean control) {
		this.control = control;
	}
	public String getSettlTypeName() {
		return settlTypeName;
	}
	public void setSettlTypeName(String settlTypeName) {
		this.settlTypeName = settlTypeName;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}
}
