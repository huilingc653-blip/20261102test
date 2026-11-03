package com.bytter.report.domain.netbank.vo;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class HomeBankAccBalVO implements Serializable {
    /**
     * 账号
     */
    private String bankAcc;
    /**
     * 币种ID
     */
    @Currency(inject="curName")
    private String curId;
    private String curName;
    /**
     * 单位ID
     */
    @Corp(inject="corpName")
    private String corpId;
    private String corpName;
    /**
     * 银行类别
     */
    @Dict(tableName="BT_BANK_TYPE",valueFieldName="id",showFiledName="TYPE_NAME",inject="bankTypeName")
    private String bankTypeId;
    private String bankTypeName;
    /**
     * 余额
     */
    private BigDecimal bal;
    /**
     * 余额占比
     */
    private BigDecimal balRatio;
    /**
     * 可用余额
     */
    private BigDecimal availBal;
    /**
     * 冻结金额
     */
    private BigDecimal frzBal;
    /**
     * 境内余额（人民币）
     */
    private BigDecimal standardBal;
    /**
     * 境外余额（外币）
     */
    private BigDecimal foreignBal;
    /**
     * 账户属性
     */
    @Dict(tableName="BIS_ACC_ATTRIBUTE",valueFieldName="id",showFiledName="ATTRIBUTE_NAME",inject="attributeName")
    private String attributeId;
    private String attributeName;
    /**
     * 账户性质
     */
    @Dict(tableName="BT_ACC_NATURE",valueFieldName="id",showFiledName="NATURE_NAME",inject="natureName")
    private String natureId;
    private String natureName;
    /**
     * 汇率
     */
    private BigDecimal exchangeRate;
    /**
     * 余额
     */
    private Date balDate;
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
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
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    public BigDecimal getBalRatio() {
        return balRatio;
    }
    public void setBalRatio(BigDecimal balRatio) {
        this.balRatio = balRatio;
    }
    public BigDecimal getAvailBal() {
        return availBal;
    }
    public void setAvailBal(BigDecimal availBal) {
        this.availBal = availBal;
    }
    public BigDecimal getFrzBal() {
        return frzBal;
    }
    public void setFrzBal(BigDecimal frzBal) {
        this.frzBal = frzBal;
    }
    public BigDecimal getStandardBal() {
        return standardBal;
    }
    public void setStandardBal(BigDecimal standardBal) {
        this.standardBal = standardBal;
    }
    public BigDecimal getForeignBal() {
        return foreignBal;
    }
    public void setForeignBal(BigDecimal foreignBal) {
        this.foreignBal = foreignBal;
    }
    public String getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
    public String getAttributeName() {
        return attributeName;
    }
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
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
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public Date getBalDate() {
        return balDate;
    }
    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }
}
