package com.bytter.biz.finacial.accbook.vo;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.List;
/**
 * 记账凭证查询(日结、未日结)
 * @author yangl
 *
 */
public class VFcAccbookAndBlnedsVO implements Serializable {
	/** 记账日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date tallyDate;
	/** 起息日期 */
	@JsonFormat(pattern = "yyyy-MM-dd")
	private java.util.Date interestStartDate;
	/** 业务种类 */
	private java.lang.String busRaceId;
	/** 业务种类 */
	private java.lang.String sysRaceCode;
	/** 流水号 */
	private java.lang.String orderNo;
	/** 凭证号 */
	private java.lang.String voucherNo;
	/** 账务明细 */
	private List<FcAccbookDtlVO> accbookDtl;
	/** 加急标志 */
	private java.lang.String emergencySign;
	/** 记账员代码 */
	private java.lang.String operatorCode;
	/** 复核员代码 */
	private java.lang.String checkerCode;
	/** 凭证状态 */
	private java.lang.String voucherState;
	/** 产生方式 */
	private java.lang.String generateMode;
	/** 外单位帐号 *//*
	private String externalAcc;
	*//** 外单位名称 *//*
	private String externalCorp;*/
	/** 业务来源类型 */
	private Integer busOriType;
	/** 备注 */
	private String rmk;
	/** 凭证附言 */
	private String postsCript;
	/** 附件数 */
	private Integer affixAmount;
	/**凭证种类代码*/
	private String voucherType;
	/**凭证种类名称*/
	private String voucherName;
	/**全局业务ID*/
	private java.lang.String busGlobalId;
	/**原凭证号*/
	private java.lang.String origVoucherNo;
	//查询条件
	private String id;
	private String opt;
	private String netCode;
	private java.util.Date gedTallyDate;
	private java.util.Date ledTallyDate;
	private String account;
	private String subjCode;
	private String gesVoucherNo;
	private String lesVoucherNo;
	private String sysVoucherCode;
	private String curCode;
	private String gesOrderNo;
	private String lesOrderNo;
	/** 借方发生额 */
	private java.math.BigDecimal gesDebitMoney;
	private java.math.BigDecimal lesDebitMoney;
	/** 贷方发生额 */
	private java.math.BigDecimal gesCreditMoney;
	private java.math.BigDecimal lesCreditMoney;
	private java.math.BigDecimal gegMoney;
	private java.math.BigDecimal legMoney;
	/** 摘要 */
	private java.lang.String summaryContent;
	private int dtlSize;
	@Dict(tableName="FC_BUS_RACE",valueFieldName="id",showFiledName="RACE_NAME",inject="raceName")
	private String raceId;
	/** 业务种类 */
	private java.lang.String raceName;
	/**账户名称*/
	private String accName;
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
	public java.lang.String getBusRaceId() {
		return busRaceId;
	}
	public void setBusRaceId(java.lang.String busRaceId) {
		this.busRaceId = busRaceId;
	}
	public java.lang.String getSysRaceCode() {
		return sysRaceCode;
	}
	public void setSysRaceCode(java.lang.String sysRaceCode) {
		this.sysRaceCode = sysRaceCode;
	}
	public java.lang.String getRaceName() {
		return raceName;
	}
	public void setRaceName(java.lang.String raceName) {
		this.raceName = raceName;
	}
	public java.lang.String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(java.lang.String orderNo) {
		this.orderNo = orderNo;
	}
	public java.lang.String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(java.lang.String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public List<FcAccbookDtlVO> getAccbookDtl() {
		return accbookDtl;
	}
	public void setAccbookDtl(List<FcAccbookDtlVO> accbookDtl) {
		this.accbookDtl = accbookDtl;
	}
	public java.lang.String getEmergencySign() {
		return emergencySign;
	}
	public void setEmergencySign(java.lang.String emergencySign) {
		this.emergencySign = emergencySign;
	}
	public java.lang.String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(java.lang.String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public java.lang.String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(java.lang.String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public java.lang.String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(java.lang.String voucherState) {
		this.voucherState = voucherState;
	}
	public java.lang.String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(java.lang.String generateMode) {
		this.generateMode = generateMode;
	}
	/*public String getExternalAcc() {
		return externalAcc;
	}
	public void setExternalAcc(String externalAcc) {
		this.externalAcc = externalAcc;
	}
	public String getExternalCorp() {
		return externalCorp;
	}
	public void setExternalCorp(String externalCorp) {
		this.externalCorp = externalCorp;
	}*/
	public Integer getBusOriType() {
		return busOriType;
	}
	public void setBusOriType(Integer busOriType) {
		this.busOriType = busOriType;
	}
	public String getRmk() {
		return rmk;
	}
	public void setRmk(String rmk) {
		this.rmk = rmk;
	}
	public String getPostsCript() {
		return postsCript;
	}
	public void setPostsCript(String postsCript) {
		this.postsCript = postsCript;
	}
	public Integer getAffixAmount() {
		return affixAmount;
	}
	public void setAffixAmount(Integer affixAmount) {
		this.affixAmount = affixAmount;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public String getVoucherName() {
		return voucherName;
	}
	public void setVoucherName(String voucherName) {
		this.voucherName = voucherName;
	}
	public java.lang.String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(java.lang.String busGlobalId) {
		this.busGlobalId = busGlobalId;
	}
	public java.lang.String getOrigVoucherNo() {
		return origVoucherNo;
	}
	public void setOrigVoucherNo(java.lang.String origVoucherNo) {
		this.origVoucherNo = origVoucherNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getNetCode() {
		return netCode;
	}
	public void setNetCode(String netCode) {
		this.netCode = netCode;
	}
	public java.util.Date getGedTallyDate() {
		return gedTallyDate;
	}
	public void setGedTallyDate(java.util.Date gedTallyDate) {
		this.gedTallyDate = gedTallyDate;
	}
	public java.util.Date getLedTallyDate() {
		return ledTallyDate;
	}
	public void setLedTallyDate(java.util.Date ledTallyDate) {
		this.ledTallyDate = ledTallyDate;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getSubjCode() {
		return subjCode;
	}
	public void setSubjCode(String subjCode) {
		this.subjCode = subjCode;
	}
	public String getGesVoucherNo() {
		return gesVoucherNo;
	}
	public void setGesVoucherNo(String gesVoucherNo) {
		this.gesVoucherNo = gesVoucherNo;
	}
	public String getLesVoucherNo() {
		return lesVoucherNo;
	}
	public void setLesVoucherNo(String lesVoucherNo) {
		this.lesVoucherNo = lesVoucherNo;
	}
	public String getSysVoucherCode() {
		return sysVoucherCode;
	}
	public void setSysVoucherCode(String sysVoucherCode) {
		this.sysVoucherCode = sysVoucherCode;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getGesOrderNo() {
		return gesOrderNo;
	}
	public void setGesOrderNo(String gesOrderNo) {
		this.gesOrderNo = gesOrderNo;
	}
	public String getLesOrderNo() {
		return lesOrderNo;
	}
	public void setLesOrderNo(String lesOrderNo) {
		this.lesOrderNo = lesOrderNo;
	}
	public java.math.BigDecimal getGesDebitMoney() {
		return gesDebitMoney;
	}
	public void setGesDebitMoney(java.math.BigDecimal gesDebitMoney) {
		this.gesDebitMoney = gesDebitMoney;
	}
	public java.math.BigDecimal getLesDebitMoney() {
		return lesDebitMoney;
	}
	public void setLesDebitMoney(java.math.BigDecimal lesDebitMoney) {
		this.lesDebitMoney = lesDebitMoney;
	}
	public java.math.BigDecimal getGesCreditMoney() {
		return gesCreditMoney;
	}
	public void setGesCreditMoney(java.math.BigDecimal gesCreditMoney) {
		this.gesCreditMoney = gesCreditMoney;
	}
	public java.math.BigDecimal getLesCreditMoney() {
		return lesCreditMoney;
	}
	public void setLesCreditMoney(java.math.BigDecimal lesCreditMoney) {
		this.lesCreditMoney = lesCreditMoney;
	}
	public int getDtlSize() {
		return dtlSize;
	}
	public void setDtlSize(int dtlSize) {
		this.dtlSize = dtlSize;
	}
	public java.lang.String getSummaryContent() {
		return summaryContent;
	}
	public void setSummaryContent(java.lang.String summaryContent) {
		this.summaryContent = summaryContent;
	}
	public java.math.BigDecimal getGegMoney() {
		return gegMoney;
	}
	public void setGegMoney(java.math.BigDecimal gegMoney) {
		this.gegMoney = gegMoney;
	}
	public java.math.BigDecimal getLegMoney() {
		return legMoney;
	}
	public void setLegMoney(java.math.BigDecimal legMoney) {
		this.legMoney = legMoney;
	}
	public String getRaceId() {
		return raceId;
	}
	public void setRaceId(String raceId) {
		this.raceId = raceId;
	}
	public String getAccName() {
		return accName;
	}
	public void setAccName(String accName) {
		this.accName = accName;
	}
	@Override
	public String toString() {
		return "VFcAccbookAndBlnedsVO{" +
				"tallyDate=" + tallyDate +
				", interestStartDate=" + interestStartDate +
				", busRaceId='" + busRaceId + '\'' +
				", sysRaceCode='" + sysRaceCode + '\'' +
				", raceName='" + raceName + '\'' +
				", orderNo='" + orderNo + '\'' +
				", voucherNo='" + voucherNo + '\'' +
				", accbookDtl=" + accbookDtl +
				", emergencySign='" + emergencySign + '\'' +
				", operatorCode='" + operatorCode + '\'' +
				", checkerCode='" + checkerCode + '\'' +
				", voucherState='" + voucherState + '\'' +
				", generateMode='" + generateMode + '\'' +
				", busOriType=" + busOriType +
				", rmk='" + rmk + '\'' +
				", postsCript='" + postsCript + '\'' +
				", affixAmount=" + affixAmount +
				", voucherType='" + voucherType + '\'' +
				", voucherName='" + voucherName + '\'' +
				", busGlobalId='" + busGlobalId + '\'' +
				", origVoucherNo='" + origVoucherNo + '\'' +
				", id='" + id + '\'' +
				", opt='" + opt + '\'' +
				", netCode='" + netCode + '\'' +
				", gedTallyDate=" + gedTallyDate +
				", ledTallyDate=" + ledTallyDate +
				", account='" + account + '\'' +
				", subjCode='" + subjCode + '\'' +
				", gesVoucherNo='" + gesVoucherNo + '\'' +
				", lesVoucherNo='" + lesVoucherNo + '\'' +
				", sysVoucherCode='" + sysVoucherCode + '\'' +
				", curCode='" + curCode + '\'' +
				", gesOrderNo='" + gesOrderNo + '\'' +
				", lesOrderNo='" + lesOrderNo + '\'' +
				", gesDebitMoney=" + gesDebitMoney +
				", lesDebitMoney=" + lesDebitMoney +
				", gesCreditMoney=" + gesCreditMoney +
				", lesCreditMoney=" + lesCreditMoney +
				", gegMoney=" + gegMoney +
				", legMoney=" + legMoney +
				", summaryContent='" + summaryContent + '\'' +
				", raceId='" + raceId + '\'' +
				", accName='" + accName + '\'' +
				", dtlSize=" + dtlSize +
				'}';
	}
}
