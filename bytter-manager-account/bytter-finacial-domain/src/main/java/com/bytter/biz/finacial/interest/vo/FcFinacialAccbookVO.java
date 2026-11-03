package com.bytter.biz.finacial.interest.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.bytter.biz.finacial.interest.entity.FcInterestDtlTemp;
import com.bytter.biz.payment.api.dto.FcVoucherTypeDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
public class FcFinacialAccbookVO implements Serializable {
	/**
	 * 账务明细
	 */
	private List<FcFinacialAccbookDtlVO> fcAccbookDtls;
	/**
	 * 利息明细
	 */
	private List<FcInterestDtlTemp> interestDtls;
	/**
     * 是否冲账凭证
     */
    private boolean isRush = false;
    /**
     * 是否冲账录入凭证
     */
    private boolean isNewRush = false;
    /**借方汇总  */
    private BigDecimal debitSum;
    /** 贷方汇总 */
    private BigDecimal creditSum;
    /**
     * 外单位银行类型
     */
    private String bankType;
    
    /**
     * 跨行标志
     */
    private String difBank;
    
    /**
     * 地域标志
     */
    private String areaSign;
    
    /**
     * 产生方式
     */
    private String generateMode;
    
    private String orderNo;
    /**
     * 打回原因
     */
    private String backReason;
    /**
     * 打回标志
     */
    private String backSign;
    /**
     * 开户行
     */
    private String bank;
    /**
     * 业务全局id
     */
    private String busGlobalId;
    /**
     * 业务来源类型
     */
    private Integer busOriType;
    /**
     * 业务品种Id
     */
    private String busRaceId;
    
    /**
     * 业务品种名称
     */
    private String busRaceName;
    
    /**
     * 是否已计提
     */
    private String chargedSign = "0";
    /**
     * 复核日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTime;
    /**
     * 复核代码
     */
    private String checkerCode;
    /**
     * 币别
     */
    private String curCode;
    /**
     * 冲账原因
     */
    private String desReason;
    /**
     * 加急标志
     */
    private String emergencySign;
    /**
     * 录入日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date enrolTime;
    /**
     * 起息日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**
     * 操作人
     */
    private String operatorCode;
    /**
     * 记账日期
     */
	@JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;
    /**
     * 凭证号
     */
    private String voucherNo;
    /**
     * 凭证状态
     */
    private String voucherState;
    /**
     * 凭证类型
     */
    private String voucherType;
    
    /**
     * 业务品种
     */
    private FcVoucherTypeDTO voucherTypeInst ;
    /**附件张数*/
    private java.lang.Integer affixAmount;
	public List<FcFinacialAccbookDtlVO> getFcAccbookDtls() {
		return fcAccbookDtls;
	}
	public void setFcAccbookDtls(List<FcFinacialAccbookDtlVO> fcAccbookDtls) {
		this.fcAccbookDtls = fcAccbookDtls;
	}
	
	public List<FcInterestDtlTemp> getInterestDtls() {
		return interestDtls;
	}
	public void setInterestDtls(List<FcInterestDtlTemp> interestDtls) {
		this.interestDtls = interestDtls;
	}
	public boolean isRush() {
		return isRush;
	}
	public void setRush(boolean isRush) {
		this.isRush = isRush;
	}
	public boolean isNewRush() {
		return isNewRush;
	}
	public void setNewRush(boolean isNewRush) {
		this.isNewRush = isNewRush;
	}
	public BigDecimal getDebitSum() {
		return debitSum;
	}
	public void setDebitSum(BigDecimal debitSum) {
		this.debitSum = debitSum;
	}
	public BigDecimal getCreditSum() {
		return creditSum;
	}
	public void setCreditSum(BigDecimal creditSum) {
		this.creditSum = creditSum;
	}
	public String getBankType() {
		return bankType;
	}
	public void setBankType(String bankType) {
		this.bankType = bankType;
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
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getBackReason() {
		return backReason;
	}
	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	public String getBackSign() {
		return backSign;
	}
	public void setBackSign(String backSign) {
		this.backSign = backSign;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBusGlobalId() {
		return busGlobalId;
	}
	public void setBusGlobalId(String busGlobalId) {
		this.busGlobalId = busGlobalId;
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
	public String getChargedSign() {
		return chargedSign;
	}
	public void setChargedSign(String chargedSign) {
		this.chargedSign = chargedSign;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public String getCheckerCode() {
		return checkerCode;
	}
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}
	public String getCurCode() {
		return curCode;
	}
	public void setCurCode(String curCode) {
		this.curCode = curCode;
	}
	public String getDesReason() {
		return desReason;
	}
	public void setDesReason(String desReason) {
		this.desReason = desReason;
	}
	public String getEmergencySign() {
		return emergencySign;
	}
	public void setEmergencySign(String emergencySign) {
		this.emergencySign = emergencySign;
	}
	public Date getEnrolTime() {
		return enrolTime;
	}
	public void setEnrolTime(Date enrolTime) {
		this.enrolTime = enrolTime;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public Date getTallyDate() {
		return tallyDate;
	}
	public void setTallyDate(Date tallyDate) {
		this.tallyDate = tallyDate;
	}
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	public String getVoucherState() {
		return voucherState;
	}
	public void setVoucherState(String voucherState) {
		this.voucherState = voucherState;
	}
	public String getVoucherType() {
		return voucherType;
	}
	public void setVoucherType(String voucherType) {
		this.voucherType = voucherType;
	}
	public FcVoucherTypeDTO getVoucherTypeInst() {
		return voucherTypeInst;
	}
	public void setVoucherTypeInst(FcVoucherTypeDTO voucherTypeInst) {
		this.voucherTypeInst = voucherTypeInst;
	}
	public String getGenerateMode() {
		return generateMode;
	}
	public void setGenerateMode(String generateMode) {
		this.generateMode = generateMode;
	}
	public String getBusRaceName() {
		return busRaceName;
	}
	public void setBusRaceName(String busRaceName) {
		this.busRaceName = busRaceName;
	}
    public Integer getAffixAmount() {
        return affixAmount;
    }
    public void setAffixAmount(Integer affixAmount) {
        this.affixAmount = affixAmount;
    }
}
