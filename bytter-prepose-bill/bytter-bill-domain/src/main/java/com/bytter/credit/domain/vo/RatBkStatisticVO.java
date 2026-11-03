package com.bytter.credit.domain.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.poi.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 银行额度统计VO
 * 
 * @author yangl
 *
 */
public class RatBkStatisticVO implements Serializable {
    /** 单位代码 */
    private String corpId;
    /** 单位代码 */
    @Excel(name = "授信单位")
    private String corpName;
    /** 单位代码 */
    private String corpNo;
    /** 银行类型 */
    private String bankType;
    /** 银行编号 */
    private String bankCode;
    /** 银行编号 */
    @Excel(name = "授信银行")
    private String bankName;
    /** 授信币种 */
//    @Currency(inject = "curName")
    private String curCode;
    /** 授信币种 */
    @Excel(name = "币别")
    private String curName;
    /** 授信开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginDate;
    /** 授信开始时间 */
    private String beginDateStr;
    /** 授信结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /** 授信结束时间 */
    private String endDateStr;
    /** 授信总额度 **/
    @Excel(name = "授信额度")
    private BigDecimal money;
    private String moneyStr;
    /** 占用额度 */
    @Excel(name = "占用额度")
    private BigDecimal useMoney;
    /** 在审占用额度 **/
    @Excel(name = "在审占用额度")
    private BigDecimal approveMoney;
    /** 可用额度 **/
    @Excel(name = "可用额度")
    private BigDecimal overPlusMoney;
    /** 剩余额度 **/
    @Excel(name = "剩余额度")
    private BigDecimal leftMoney;
    /** 分配方式 */
    @Dict(dictGroup = "RatBkrationDis.DistributeWay", inject = "disWayName")
    private String disWay;
    @Excel(name = "分配方式")
    private String disWayName;
    /** 授信有效日期 */
    @Excel(name = "授信有效日期")
    private String rationDateStr;
    // 冻结额度
    private BigDecimal freezeMoney;
    // 追加在途额度
    private BigDecimal addMoney;
    // 虚拟占用额度
    private BigDecimal virtualUration;
    // 虚拟调剂额度
    private BigDecimal virtualRelief;
    // 虚拟占用额度
    private BigDecimal reliefAndUrationMoney;
    private String ratCode;
    // 统计标识
    private String flagSign;
    // 授信模式
    @Dict(dictGroup = "RatBkration.RatMode", inject = "ratModeName")
    private String ratMode;
    @Excel(name = "授信模式")
    private String ratModeName;
    /** 保证金比例 **/
    private BigDecimal assurepercent;
    /** 费率 **/
    private BigDecimal procedureFeeRate;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpNo() {
        return corpNo;
    }
    public void setCorpNo(String corpNo) {
        this.corpNo = corpNo;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public BigDecimal getUseMoney() {
        return useMoney;
    }
    public void setUseMoney(BigDecimal useMoney) {
        this.useMoney = useMoney;
    }
    public String getDisWay() {
        return disWay;
    }
    public void setDisWay(String disWay) {
        this.disWay = disWay;
    }
    public String getRationDateStr() {
        return rationDateStr;
    }
    public void setRationDateStr(String rationDateStr) {
        this.rationDateStr = rationDateStr;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getApproveMoney() {
        return approveMoney;
    }
    public void setApproveMoney(BigDecimal approveMoney) {
        this.approveMoney = approveMoney;
    }
    public BigDecimal getOverPlusMoney() {
        if (null == overPlusMoney) {
            overPlusMoney = BigDecimal.ZERO;
        }
        return overPlusMoney;
    }
    public void setOverPlusMoney(BigDecimal overPlusMoney) {
        this.overPlusMoney = overPlusMoney;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }
    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }
    public BigDecimal getAddMoney() {
        return addMoney;
    }
    public void setAddMoney(BigDecimal addMoney) {
        this.addMoney = addMoney;
    }
    public BigDecimal getVirtualUration() {
        if (null == virtualUration) {
            virtualUration = BigDecimal.ZERO;
        }
        return virtualUration;
    }
    public void setVirtualUration(BigDecimal virtualUration) {
        this.virtualUration = virtualUration;
    }
    public BigDecimal getVirtualRelief() {
        if (null == virtualRelief) {
            virtualRelief = BigDecimal.ZERO;
        }
        return virtualRelief;
    }
    public void setVirtualRelief(BigDecimal virtualRelief) {
        this.virtualRelief = virtualRelief;
    }
    public BigDecimal getReliefAndUrationMoney() {
        if (null == reliefAndUrationMoney) {
            reliefAndUrationMoney = BigDecimal.ZERO;
        }
        return reliefAndUrationMoney;
    }
    public void setReliefAndUrationMoney(BigDecimal reliefAndUrationMoney) {
        this.reliefAndUrationMoney = reliefAndUrationMoney;
    }
    public String getMoneyStr() {
        return moneyStr;
    }
    public void setMoneyStr(String moneyStr) {
        this.moneyStr = moneyStr;
    }
    public String getRatCode() {
        return ratCode;
    }
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    public String getFlagSign() {
        return flagSign;
    }
    public void setFlagSign(String flagSign) {
        this.flagSign = flagSign;
    }
    public String getBeginDateStr() {
        return beginDateStr;
    }
    public void setBeginDateStr(String beginDateStr) {
        this.beginDateStr = beginDateStr;
    }
    public String getEndDateStr() {
        return endDateStr;
    }
    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }
    public String getRatMode() {
        return ratMode;
    }
    public void setRatMode(String ratMode) {
        this.ratMode = ratMode;
    }
    public String getRatModeName() {
        return ratModeName;
    }
    public void setRatModeName(String ratModeName) {
        this.ratModeName = ratModeName;
    }
    public String getDisWayName() {
        return disWayName;
    }
    public void setDisWayName(String disWayName) {
        this.disWayName = disWayName;
    }
    public BigDecimal getAssurepercent() {
        return assurepercent;
    }
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    public BigDecimal getProcedureFeeRate() {
        return procedureFeeRate;
    }
    public void setProcedureFeeRate(BigDecimal procedureFeeRate) {
        this.procedureFeeRate = procedureFeeRate;
    }
}
