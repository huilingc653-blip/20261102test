package com.bytter.biz.finacial.accbook.vo;
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
     * 租户ID
     */
    private String tenantId;
    /**
     * 所属网点
     */
    private java.lang.String netId;
    /**
     * 所属网点
     */
    private java.lang.String netName;
    /**
     * 流水号
     */
    private java.lang.String orderNo;
    /**
     * 凭证号
     */
    private java.lang.String voucherNo;
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
    private java.lang.String voucherType;
    /**
     * 凭证种类名称
     */
    private String voucherId;
    private java.lang.String voucherName;
    /**
     * 原凭证号
     */
    private java.lang.String origVoucherNo;
    /**
     * 加急标志
     */
    private java.lang.String emergencySign;
    /**
     * 记账员代码
     */
    private java.lang.String operatorCode;
    /**
     * 记账员名称
     */
    private String operatorName;
    /**
     * 复核员代码
     */
    private java.lang.String checkerCode;
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
    private java.lang.Integer affixAmount;
    /**
     * 红蓝标志
     */
    private java.lang.String redblueSign;
    /**
     * 凭证状态
     */
    private java.lang.String voucherState;
    /**
     * 已计手续费标志
     */
    private java.lang.String chargedSign;
    /**
     * 冲账标志
     */
    private java.lang.String rushSign;
    /**
     * 产生方式
     */
    private java.lang.String generateMode;
    /**
     * 业务来源类型
     */
    private java.lang.Integer busOriType;
    /**
     * 业务种类
     */
    @Dict(tableName = "FC_BUS_RACE", valueFieldName = "id", showFiledName = "RACE_NAME", inject = "raceName")
    private java.lang.String busRaceId;
    /**
     * 业务种类
     */
    @Dict(tableName = "FC_BUS_RACE", valueFieldName = "id", showFiledName = "RACE_NAME", inject = "raceName")
    private String raceId;
    private java.lang.String raceName;
    /**
     * 备注
     */
    private java.lang.String rmk;
    /**
     * 凭证附言
     */
    private java.lang.String postscript;
    /**
     * 币别代码
     */
    private java.lang.String curCode;
    private java.lang.String curName;
    /**
     * 全局业务ID
     */
    private java.lang.String busGlobalId;
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
    public java.lang.String getNetId() {
        return netId;
    }
    public void setNetId(java.lang.String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public java.lang.String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(java.lang.String voucherType) {
        this.voucherType = voucherType;
    }
    public java.lang.String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(java.lang.String voucherName) {
        this.voucherName = voucherName;
    }
    public java.lang.String getOrigVoucherNo() {
        return origVoucherNo;
    }
    public void setOrigVoucherNo(java.lang.String origVoucherNo) {
        this.origVoucherNo = origVoucherNo;
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
    public java.util.Date getCheckTime() {
        return checkTime;
    }
    public void setCheckTime(java.util.Date checkTime) {
        this.checkTime = checkTime;
    }
    public java.lang.Integer getAffixAmount() {
        return affixAmount;
    }
    public void setAffixAmount(java.lang.Integer affixAmount) {
        this.affixAmount = affixAmount;
    }
    public java.lang.String getRedblueSign() {
        return redblueSign;
    }
    public void setRedblueSign(java.lang.String redblueSign) {
        this.redblueSign = redblueSign;
    }
    public java.lang.String getVoucherState() {
        return voucherState;
    }
    public void setVoucherState(java.lang.String voucherState) {
        this.voucherState = voucherState;
    }
    public java.lang.String getChargedSign() {
        return chargedSign;
    }
    public void setChargedSign(java.lang.String chargedSign) {
        this.chargedSign = chargedSign;
    }
    public java.lang.String getRushSign() {
        return rushSign;
    }
    public void setRushSign(java.lang.String rushSign) {
        this.rushSign = rushSign;
    }
    public java.lang.String getGenerateMode() {
        return generateMode;
    }
    public void setGenerateMode(java.lang.String generateMode) {
        this.generateMode = generateMode;
    }
    public java.lang.Integer getBusOriType() {
        return busOriType;
    }
    public void setBusOriType(java.lang.Integer busOriType) {
        this.busOriType = busOriType;
    }
    public java.lang.String getBusRaceId() {
        return busRaceId;
    }
    public void setBusRaceId(java.lang.String busRaceId) {
        this.busRaceId = busRaceId;
    }
    public java.lang.String getRmk() {
        return rmk;
    }
    public void setRmk(java.lang.String rmk) {
        this.rmk = rmk;
    }
    public java.lang.String getPostscript() {
        return postscript;
    }
    public void setPostscript(java.lang.String postscript) {
        this.postscript = postscript;
    }
    public java.lang.String getCurCode() {
        return curCode;
    }
    public void setCurCode(java.lang.String curCode) {
        this.curCode = curCode;
    }
    public java.lang.String getCurName() {
        return curName;
    }
    public void setCurName(java.lang.String curName) {
        this.curName = curName;
    }
    public java.lang.String getRaceName() {
        return raceName;
    }
    public void setRaceName(java.lang.String raceName) {
        this.raceName = raceName;
    }
    public java.lang.String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(java.lang.String busGlobalId) {
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
