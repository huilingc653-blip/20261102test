package com.bytter.biz.finacial.api.vo;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Title: Entity
 * @Description: FC_ACCBOOK_BLNED
 * @author onlineGenerator
 * @date 2017-06-19 11:28:14
 * @version 1.0
 *
 */
public class EAccbookBlnedVo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	/**所属网点*/
	private String netId;
	/**流水号*/
	private String orderNo;
	/**凭证号*/
	private String voucherNo;
	/**录入日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date enrolTime;
	/**记账日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/**起息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	/**结息日期*/
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date blnInstDate;
	/**凭证种类代码*/
	private String voucherType;
	/**凭证种类ID*/
	private String voucherTypeId;
	/**原凭证号*/
	private String origVoucherNo;
	/**原始凭证Id*/
	private String origVoucherId;
	/**加急标志*/
	private String emergencySign;
	/**记账员代码*/
	private String operatorCode;
	/**复核员代码*/
	private String checkerCode;
	/**复核日期*/
	private java.util.Date checkTime;
	/**附件张数*/
	private Integer affixAmount;
	/**红蓝标志*/
	private String redblueSign;
	/**凭证状态*/
	private String voucherState;
	/**已计手续费标志*/
	private String chargedSign;
	/**冲账标志*/
	private String rushSign;
	/**产生方式*/
	private String generateMode;
	/**业务来源类型*/
	private Integer busOriType;
	/**业务种类*/
	private String busRaceId;
	/**备注*/
	private String rmk;
	/**凭证附言*/
	private String postscript;
	/**币别代码*/
	private String curCode;
	/**创建时间*/
	private java.util.Date createTime;
	/**创建用户代码*/
	private String createBy;
	/**最后更新时间*/
	private java.util.Date updateTime;
	/**最后更新用户代码*/
	private String updateBy;
	
	/**全局业务ID*/
	private String busGlobalId;
	
	/**外部单位名称*/
	private String externalCorp;
	/**外部单位id*/
	private String externalId;
	/**外部单位账户*/
	private String externalAcc;
	
	/**外部单位银行*/
	private String externalBank;
	
	/**账号*/
	private String accNo;
	/**摘要*/
	private String summaryContent;
	
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNetId() {
		return netId;
	}
	public void setNetId(String netId) {
		this.netId = netId;
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
	public java.util.Date getEnrolTime() {
		return enrolTime;
	}
	public void setEnrolTime(java.util.Date enrolTime) {
		this.enrolTime = enrolTime;
	}
	public java.util.Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(java.util.Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public java.util.Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(java.util.Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public String getOrigVoucherId(){
		return this.origVoucherId;
	}
	public void setOrigVoucherId(String origVoucherId){
		this.origVoucherId = origVoucherId;
	}
	public String getEmergencySign() {
		return emergencySign;
	}
	public void setEmergencySign(String emergencySign) {
		this.emergencySign = emergencySign;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public java.util.Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(java.util.Date checkTime) {
		this.checkTime = checkTime;
	}
	public Integer getAffixAmount() {
		return affixAmount;
	}
	public void setAffixAmount(Integer affixAmount) {
		this.affixAmount = affixAmount;
	}
	public String getRedblueSign() {
		return redblueSign;
	}
	public void setRedblueSign(String redblueSign) {
		this.redblueSign = redblueSign;
	}
	public String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}
	public String getChargedSign() {
		return chargedSign;
	}
	public void setChargedSign(String chargedSign) {
		this.chargedSign = chargedSign;
	}
	public String getRushSign() {
		return rushSign;
	}
	public void setRushSign(String rushSign) {
		this.rushSign = rushSign;
	}
	public String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(String generateMode) {
		this.generateMode = generateMode;
	}
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getBusRaceId() {
		return busRaceId;
	}
	public void setBusRaceId(String busRaceId) {
		this.busRaceId = busRaceId;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public java.util.Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public java.util.Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	public java.util.Date getBlnInstDate() {
		return blnInstDate;
	}
	public void setBlnInstDate(java.util.Date blnInstDate) {
		this.blnInstDate = blnInstDate;
	}
	public String getExternalCorp() {
		return externalCorp;
	}
	public void setExternalCorp(String externalCorp) {
		this.externalCorp = externalCorp;
	}
	public String getExternalId() {
		return externalId;
	}
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}
	public String getExternalBank() {
		return externalBank;
	}
	public void setExternalBank(String externalBank) {
		this.externalBank = externalBank;
	}
	public String getVoucherTypeId() {
		return voucherTypeId;
	}
	public void setVoucherTypeId(String voucherTypeId) {
		this.voucherTypeId = voucherTypeId;
	}
	public String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(String summaryContent) {
		this.summaryContent = summaryContent;
	}
}
