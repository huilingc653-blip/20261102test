package com.bytter.report.domain.synthesize.vo;
import com.bytter.core.annotation.*;
import java.math.BigDecimal;
/**
 * 资金地图报表VO对象
 *
 * @author zuoy
 * @since 2020-02-11
 */
public class MoneyMapVO  {
    /**
     * 单位ID
     */
    @Corp(inject="corpName")
   private String corpId;
    /**
     * 单位名称
     */
   private String corpName;
    /**
     * 余额类型 1-内部存款、2-银行存款
     */
    @Dict(dictGroup = "bi.money.map.balance.type", inject = "balanceTypeName")
    private String balanceType;
    /**
     * 余额类型 1-内部存款、2-银行存款
     */
    private String balanceTypeName;
    /**
     * 所属国家
     */
    private String countryName;
    /**
     * 省份
     */
    @Region(inject="provName")
    private String prov;
    /**
     * 省份名称
     */
    private String provName;
    /**
     * 账号
     */
   private String bankAcc;
    /**
     * 账户类型
     */
   private String accType;
    /**
     * 账户类型名称
     */
   private String accTypeName;
    /**账户属性*/
    @Dict(tableName="BIS_ACC_ATTRIBUTE",valueFieldName="id",showFiledName="ATTRIBUTE_NAME",inject="attributeName")
    private String attributeId;
    @BtCompositeSearch(fieldName = "acc.attribute_id")
    private String attributeName;
    /**账户性质*/
    @Dict(tableName="BT_ACC_NATURE",valueFieldName="id",showFiledName="NATURE_NAME",inject="natureName")
    private String natureId;
    @BtCompositeSearch(fieldName = "acc.nature_id")
    private String natureName;
    /**
     * 币别ID
     */
    @Currency(inject="curName")
   private String curId;
    /**
     * 币别ID
     */
   private String curName;
    /**
     * 余额
     */
   private BigDecimal balance;
    /**
     * 余额_折合人民币
     */
   private BigDecimal balanceRmb;
    /**
     * 银行类型
     */
   private String  bankType;
    /**
     * 银行类型名称
     */
   private String  bankTypeName;
    /**
     * 开户行名称
     */
   private String bankName;
    /**
     * 是否在校
     */
    @Dict(dictGroup = "sf_yn", inject = "isOnlineStr")
   private String isOnline;
    private String isOnlineStr;
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
    public String getBalanceType() {
        return balanceType;
    }
    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
    public String getBalanceTypeName() {
        return balanceTypeName;
    }
    public void setBalanceTypeName(String balanceTypeName) {
        this.balanceTypeName = balanceTypeName;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
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
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getBalanceRmb() {
        return balanceRmb;
    }
    public void setBalanceRmb(BigDecimal balanceRmb) {
        this.balanceRmb = balanceRmb;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getIsOnline() {
        return isOnline;
    }
    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }
    public String getIsOnlineStr() {
        return isOnlineStr;
    }
    public void setIsOnlineStr(String isOnlineStr) {
        this.isOnlineStr = isOnlineStr;
    }
}
