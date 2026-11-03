package com.bytter.tally.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.core.annotation.User;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 账务凭证保存对象
 *
 * @author liyh
 */
public class FcAccbookVO implements java.io.Serializable {
    private String id;
    /**
     * 所属网点
     */
    @Corp(inject = "netName")
    private String netId;
    /**
     * 所属网点
     */
    private String netName;
    /**
     * 流水号
     */
    private String orderNo;
    /**
     * 凭证号
     */
    private String voucherNo;
    /**
     * 录入日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date enrolTime;
    /**
     * 记账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date tallyDate;
    /**
     * 起息日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date interestStartDate;
    /**
     * 凭证种类代码
     */
    private String voucherType;
    /**
     * 凭证种类名称
     */
    @Dict(tableName = "FC_VOUCHER_TYPE", valueFieldName = "id", showFiledName = "VOUCHER_NAME", inject = "voucherName")
    private String voucherId;
    private String voucherName;
    /**
     * 原凭证号
     */
    private String origVoucherNo;
    /**
     * 加急标志
     */
    private String emergencySign;
    /**
     * 记账员代码
     */
    @User(inject = "operatorName")
    private String operatorCode;
    /**
     * 记账员名称
     */
    private String operatorName;
    /**
     * 复核员代码
     */
    @User(inject = "checkerName")
    private String checkerCode;
    /**
     * 复核员名称
     */
    private String checkerName;
    /**
     * 复核日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date checkTime;
    /**
     * 附件张数
     */
    private Integer affixAmount;
    /**
     * 红蓝标志
     */
    private String redblueSign;
    /**
     * 凭证状态
     */
    private String voucherState;
    /**
     * 已计手续费标志
     */
    private String chargedSign;
    /**
     * 冲账标志
     */
    private String rushSign;
    /**
     * 产生方式
     */
    private String generateMode;
    /**
     * 业务来源类型
     */
    private Integer busOriType;
    /**
     * 业务种类
     */
    @Dict(tableName = "FC_BUS_RACE", valueFieldName = "id", showFiledName = "RACE_NAME", inject = "raceName")
    private String busRaceId;
    /**
     * 业务种类
     */
    @Dict(tableName = "FC_BUS_RACE", valueFieldName = "id", showFiledName = "RACE_NAME", inject = "raceName")
    private String raceId;
    private String raceName;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 凭证附言
     */
    private String postscript;
    /**
     * 币别代码
     */
    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    /**
     * 全局业务ID
     */
    private String busGlobalId;
    private String busRaceName;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 是否对现金流项目做过更改
     */
    private String isChangeCashflux;
    /**
     * 管理员名称
     */
    private String manageName;
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
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getOrigVoucherNo() {
        return origVoucherNo;
    }
    public void setOrigVoucherNo(String origVoucherNo) {
        this.origVoucherNo = origVoucherNo;
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
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getRaceName() {
        return raceName;
    }
    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
    public String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
    public String getVoucherId() {
        return voucherId;
    }
    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }
    public String getRaceId() {
        return raceId;
    }
    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }
    public String getBusRaceName() {
        return busRaceName;
    }
    public void setBusRaceName(String busRaceName) {
        this.busRaceName = busRaceName;
    }
    /**
     * 附件ID
     */
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getIsChangeCashflux() {
        return isChangeCashflux;
    }
    public void setIsChangeCashflux(String isChangeCashflux) {
        this.isChangeCashflux = isChangeCashflux;
    }
    public String getManageName() {
        return manageName;
    }
    public void setManageName(String manageName) {
        this.manageName = manageName;
    }
    public String getOperatorName() {
        return operatorName;
    }
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    public String getCheckerName() {
        return checkerName;
    }
    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }
    @Override
    public String toString() {
        return "FcAccbookVO{" +
                "id='" + id + '\'' +
                ", netId='" + netId + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", voucherNo='" + voucherNo + '\'' +
                ", enrolTime=" + enrolTime +
                ", tallyDate=" + tallyDate +
                ", interestStartDate=" + interestStartDate +
                ", voucherType='" + voucherType + '\'' +
                ", voucherName='" + voucherName + '\'' +
                ", origVoucherNo='" + origVoucherNo + '\'' +
                ", emergencySign='" + emergencySign + '\'' +
                ", operatorCode='" + operatorCode + '\'' +
                ", checkerCode='" + checkerCode + '\'' +
                ", checkTime=" + checkTime +
                ", affixAmount=" + affixAmount +
                ", redblueSign='" + redblueSign + '\'' +
                ", voucherState='" + voucherState + '\'' +
                ", chargedSign='" + chargedSign + '\'' +
                ", rushSign='" + rushSign + '\'' +
                ", generateMode='" + generateMode + '\'' +
                ", busOriType=" + busOriType +
                ", busRaceId='" + busRaceId + '\'' +
                ", raceName='" + raceName + '\'' +
                ", rmk='" + rmk + '\'' +
                ", postscript='" + postscript + '\'' +
                ", curCode='" + curCode + '\'' +
                ", curName='" + curName + '\'' +
                ", raceId='" + raceId + '\'' +
                ", voucherId='" + voucherId + '\'' +
                ", busGlobalId='" + busGlobalId + '\'' +
                ", busRaceName='" + busRaceName + '\'' +
                ", attachmentId='" + attachmentId + '\'' +
                '}';
    }
}
