package com.bytter.biz.report.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author shenc
 * @Description 银行余额统计报表
 * @date 2018.12.14
 */
public class BankBalDistributeVo implements Serializable {
    /**单位ID*/
    @Corp(inject="corpName")
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "b.corp_id")
    private String corpName;
    @BtCompositeSearch(fieldName = "c.cur_id")
    private String curName;
    @Currency(inject="curName")
    @BtCompositeSearch(fieldName = "c.cur_id")
    private String curId;
    /**账户类别*/
    @Dict(dictGroup = "btBankAcc.accType", inject = "accTypeName")
    @BtCompositeSearch(fieldName = "b.acc_type")
    private String accType;
    @BtCompositeSearch(fieldName = "b.acc_type")
    private String accTypeName;
    /**账户性质*/
    @BtCompositeSearch(fieldName = "b.nature_id")
    private String natureId;
    @BtCompositeSearch(fieldName = "b.nature_id")
    private String natureName;
    /**银行类别*/
    @BtCompositeSearch(fieldName = "b.bank_type_id")
    private String bankTypeId;
    @BtCompositeSearch(fieldName = "b.bank_type_id")
    private String bankTypeName;
    /**总计余额*/
    private BigDecimal balMoney;
    private Date regDate;
    private String regDateStr;
    private Date startDate;
    private Date endDate;
    /**是否折合币种*/
    private String changeCur;
    /**折合币种*/
    private String reducedCurId;
    /**折算汇率类型*/
    private String rateType;
    /**汇率日期*/
    private Date rateDate;
    /**折合汇率*/
    private BigDecimal curExrate;
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
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
    public String getAccTypeName() {
        return accTypeName;
    }
    public void setAccTypeName(String accTypeName) {
        this.accTypeName = accTypeName;
    }
    public String getNatureId() {
        return natureId;
    }
    public void setNatureId(String natureId) {
        this.natureId = natureId;
    }
    public String getNatureName() {
        return natureName;
    }
    public void setNatureName(String natureName) {
        this.natureName = natureName;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public BigDecimal getBalMoney() {
        return balMoney;
    }
    public void setBalMoney(BigDecimal balMoney) {
        this.balMoney = balMoney;
    }
    public Date getRegDate() {
        return regDate;
    }
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    public String getRegDateStr() {
        return regDateStr;
    }
    public void setRegDateStr(String regDateStr) {
        this.regDateStr = regDateStr;
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
    public String getChangeCur() {
        return changeCur;
    }
    public void setChangeCur(String changeCur) {
        this.changeCur = changeCur;
    }
    public String getReducedCurId() {
        return reducedCurId;
    }
    public void setReducedCurId(String reducedCurId) {
        this.reducedCurId = reducedCurId;
    }
    public String getRateType() {
        return rateType;
    }
    public void setRateType(String rateType) {
        this.rateType = rateType;
    }
    public Date getRateDate() {
        return rateDate;
    }
    public void setRateDate(Date rateDate) {
        this.rateDate = rateDate;
    }
    public BigDecimal getCurExrate() {
        return curExrate;
    }
    public void setCurExrate(BigDecimal curExrate) {
        this.curExrate = curExrate;
    }
}
