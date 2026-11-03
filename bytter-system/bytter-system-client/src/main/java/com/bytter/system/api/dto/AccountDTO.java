package com.bytter.system.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 银行账户
 *
 * @author zuoy    2017/7/13
 */
public class AccountDTO implements Serializable {
    /**
     * 账户ID
     */
    private String bankAccId;
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 单位
     */
    private String corpName;
    /**
     * 账户
     */
    private String bankAcc;
    /**
     * 户名
     */
    private String accName;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 币别ID
     */
    private String curId;
    /**
     * 币别
     */
    private String curName;
    /**
     * 银行接口
     */
    private String bifCode;
    /**
     * 账户属性
     */
    private String attributeId;
    /**
     * 父账户信息
     */
    private AccountDTO parentAccount;
    /**
     * 银行账户余额
     */
    private BigDecimal balance;
    /**
     * 内部账户余额
     */
    private BigDecimal fcBalance;
    /**
     * 所在省
     */
    private String prov;
    private String provName;
    /**
     * 所在市
     */
    private String city;
    private String cityName;
    /***营收稽核新增***/
    /**
     * 实体账户ID
     */
    private String entityAccId;
    /**
     * 实体账号
     */
    private String entityAccNo;
    /**外汇管理新增 **/
    /**
     * 币别英文编码
     */
    private String curEnglishCode;
    /**
     * 银行类型ID
     */
    private String bankTypeId;
    private String bankTypeName;
    private String bankPrefix;
    private String bankNamePrefix;
    /**
     * 流通方式 0-线下 1-线上
     **/
    private String circulationMode;
    /**
     * 结算中心账号
     */
    private String counterAccId;
    /**
     * 结算中心父账号
     */
    private String superCounterAcc;
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
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
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
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
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public AccountDTO getParentAccount() {
        return parentAccount;
    }
    public String getAttributeId() {
        return attributeId;
    }
    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }
    public void setParentAccount(AccountDTO parentAccount) {
        this.parentAccount = parentAccount;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public BigDecimal getFcBalance() {
        return fcBalance;
    }
    public void setFcBalance(BigDecimal fcBalance) {
        this.fcBalance = fcBalance;
    }
    public String getCirculationMode() {
        return circulationMode;
    }
    public void setCirculationMode(String circulationMode) {
        this.circulationMode = circulationMode;
    }
    public String getEntityAccId() {
        return entityAccId;
    }
    public void setEntityAccId(String entityAccId) {
        this.entityAccId = entityAccId;
    }
    public String getEntityAccNo() {
        return entityAccNo;
    }
    public void setEntityAccNo(String entityAccNo) {
        this.entityAccNo = entityAccNo;
    }
    public String getCurEnglishCode() {
        return curEnglishCode;
    }
    public void setCurEnglishCode(String curEnglishCode) {
        this.curEnglishCode = curEnglishCode;
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
    public String getBankPrefix() {
        return bankPrefix;
    }
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    public String getBankNamePrefix() {
        return bankNamePrefix;
    }
    public void setBankNamePrefix(String bankNamePrefix) {
        this.bankNamePrefix = bankNamePrefix;
    }
    public String getCounterAccId() {
        return counterAccId;
    }
    public void setCounterAccId(String counterAccId) {
        this.counterAccId = counterAccId;
    }
    public String getSuperCounterAcc() {
        return superCounterAcc;
    }
    public void setSuperCounterAcc(String superCounterAcc) {
        this.superCounterAcc = superCounterAcc;
    }
}
