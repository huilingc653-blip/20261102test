package com.bytter.report.domain.stock.vo;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class AbBillVO implements Serializable {
	private static final long serialVersionUID = -3029431221385579311L;
	/**票据主表id*/
    private String vid;
    /**票据从表id*/
    private String vvid;
    /**票据流水id*/
    private String aid;
    /**票据签收表id*/
    private String recid;
    /**应收，应付标志:1:应付票据 */
    private String type;
    /**持票单位*/
    private String corpId;
    /**持票单位名称*/
    private String corpName;
    /**票据所属账号*/
    private String bankAcc;
    /**业务类型*/
    @Dict(dictGroup = "AB_BILL_BUS_TYPE", inject = "busTypeName")
    private String busType;
    private String busTypeName;
    /**票据来源*/
    @Dict(dictGroup = "AB_BILL_SOURCE_QUERY", inject = "sourceName")
    private String source;
    /**票据来源*/
    private String sourceName;
    /**票据号码*/
    private String origNo;
    /**流通介质：10 纸票、11 电票*/
    @Dict(dictGroup = "AB_BILL_FLAG", inject = "billFlagName")
    private Integer billFlag;
    /**流通介质名称：10 纸票、11 电票*/
    private String billFlagName;
    /**票据类型：10 银票、11 商票*/
    @Dict(dictGroup = "AB_BILL_TYPE", inject = "billTypeName")
    private Integer billType;
    /**票据类型名称：10 银票、11 商票*/
    private String billTypeName;
    /**票据金额*/
    private BigDecimal billMoney;
    /**可用余额增加额*/
    private BigDecimal addMoney;
    /**可用余额减少额*/
    private BigDecimal reduceMoney;
    /**申请人*/
    private String applyName;
    /**申请日期*/
    private Date applyDate;
    /**业务归属日期*/
    private Date businessDate;
    /**出票日期*/
    private Date startDate;
    /**到期日期*/
    private Date endDate;
    /**贴现利息*/
    private BigDecimal interest;
    /**贴现利率*/
    private BigDecimal interestRates;
    /**出票人*/
    private String payer;
    /**收票人全称*/
    private String payee;
    /**前手*/
    private String cnAccrecords;
    /**后手*/
    private String rearAccrecords;
    /**业务状态*/
    @Dict(dictGroup = "AB_BILL_BUSSTATUS", inject = "busStatusName")
    private Integer busStatus;
    /**业务状态名称*/
    private String busStatusName;
    /**指令状态*/
    @Dict(dictGroup = "AB_BILL_INSOPTSTATUS", inject = "instructStatusName")
    private String inoptStatus;
    /**指令状态名称*/
    private String instructStatusName;
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
    public String getAid() {
        return aid;
    }
    public void setAid(String aid) {
        this.aid = aid;
    }
    public String getRecid() {
        return recid;
    }
    public void setRecid(String recid) {
        this.recid = recid;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    public String getOrigNo() {
        return origNo;
    }
    public void setOrigNo(String origNo) {
        this.origNo = origNo;
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
    public BigDecimal getBillMoney() {
        return billMoney;
    }
    public void setBillMoney(BigDecimal billMoney) {
        this.billMoney = billMoney;
    }
    public BigDecimal getAddMoney() {
        return addMoney;
    }
    public void setAddMoney(BigDecimal addMoney) {
        this.addMoney = addMoney;
    }
    public BigDecimal getReduceMoney() {
        return reduceMoney;
    }
    public void setReduceMoney(BigDecimal reduceMoney) {
        this.reduceMoney = reduceMoney;
    }
    public String getApplyName() {
        return applyName;
    }
    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }
    public Date getApplyDate() {
        return applyDate;
    }
    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }
    public Date getBusinessDate() {
        return businessDate;
    }
    public void setBusinessDate(Date businessDate) {
        this.businessDate = businessDate;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public BigDecimal getInterestRates() {
        return interestRates;
    }
    public void setInterestRates(BigDecimal interestRates) {
        this.interestRates = interestRates;
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
    public String getCnAccrecords() {
        return cnAccrecords;
    }
    public void setCnAccrecords(String cnAccrecords) {
        this.cnAccrecords = cnAccrecords;
    }
    public String getRearAccrecords() {
        return rearAccrecords;
    }
    public void setRearAccrecords(String rearAccrecords) {
        this.rearAccrecords = rearAccrecords;
    }
    public Integer getBusStatus() {
        return busStatus;
    }
    public void setBusStatus(Integer busStatus) {
        this.busStatus = busStatus;
    }
    public String getBusStatusName() {
        return busStatusName;
    }
    public void setBusStatusName(String busStatusName) {
        this.busStatusName = busStatusName;
    }
    public String getInoptStatus() {
        return inoptStatus;
    }
    public void setInoptStatus(String inoptStatus) {
        this.inoptStatus = inoptStatus;
    }
    public String getInstructStatusName() {
        return instructStatusName;
    }
    public void setInstructStatusName(String instructStatusName) {
        this.instructStatusName = instructStatusName;
    }
    @Override
    public String toString() {
        return "AbBillVO{" +
                "vid='" + vid + '\'' +
                ", vvid='" + vvid + '\'' +
                ", aid='" + aid + '\'' +
                ", recid='" + recid + '\'' +
                ", type='" + type + '\'' +
                ", corpId='" + corpId + '\'' +
                ", corpName='" + corpName + '\'' +
                ", bankAcc='" + bankAcc + '\'' +
                ", busType='" + busType + '\'' +
                ", busTypeName='" + busTypeName + '\'' +
                ", source='" + source + '\'' +
                ", sourceName='" + sourceName + '\'' +
                ", origNo='" + origNo + '\'' +
                ", billFlag=" + billFlag +
                ", billFlagName='" + billFlagName + '\'' +
                ", billType=" + billType +
                ", billTypeName='" + billTypeName + '\'' +
                ", billMoney=" + billMoney +
                ", addMoney=" + addMoney +
                ", reduceMoney=" + reduceMoney +
                ", applyName='" + applyName + '\'' +
                ", applyDate=" + applyDate +
                ", businessDate=" + businessDate +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", interest=" + interest +
                ", interestRates=" + interestRates +
                ", payer='" + payer + '\'' +
                ", payee='" + payee + '\'' +
                ", cnAccrecords='" + cnAccrecords + '\'' +
                ", rearAccrecords='" + rearAccrecords + '\'' +
                ", busStatus=" + busStatus +
                ", busStatusName='" + busStatusName + '\'' +
                ", inoptStatus='" + inoptStatus + '\'' +
                ", instructStatusName='" + instructStatusName + '\'' +
                '}';
    }
}
