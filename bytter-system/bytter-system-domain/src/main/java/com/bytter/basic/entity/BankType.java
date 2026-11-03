package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.Cascade;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 银行类型维护
 * @date 2017-05-17 18:07:34
 */
@Entity
@Table(name = "BT_BANK_TYPE")
public class BankType extends IdEntity implements java.io.Serializable {
    /**
     * erp.bank_type
     */
    private String bankType;
    /**
     * erp.type_name
     */
    private String typeName;
    /**
     * ep.bank_prefix
     */
    private String bankPrefix;
    /**
     * erp.rmk
     */
    private String rmk;
    /**
     * erp.valid_sign
     */
    private String validSign;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 是否系统初始化 1 ：是；0：否
     */
    private String isSystem;
    /**
     * 网银证书到期日
     */
    private Date netbankCFCADate;
    /**
     * 是否境内银行
     */
    private String isDomesticBank;
    /**
     * 开户行名称前缀，针对313和402开头的银行
     */
    private String bankNamePrefix;
    /**
     * 开户行名称前缀，针对313和402开头的银行
     */
    private String icon;
    /**
     * 将变色
     */
    private String gradient;
    /**
     * 银行种类
     */
    private String bankKind;
    /**
     * 是否直连银行
     */
    private String whetherConnectBank;
    /**
     * 授信合同是否必须
     */
    private String creditContractRequired;
    /**
     * 汇款方式
     */
    private String remitMethod;
    private Set<BisBifInit> bisBifInitSet = new HashSet<BisBifInit>();
    /**
     * 是否为第三方平台
     */
    private String isThirdParty;

    @Column(name = "WHETHER_CONNECT_BANK")
    public String getWhetherConnectBank() {
        return whetherConnectBank;
    }

    public void setWhetherConnectBank(String whetherConnectBank) {
        this.whetherConnectBank = whetherConnectBank;
    }

    @Column(name = "CREDIT_CONTRACT_REQUIRED")
    public String getCreditContractRequired() {
        return creditContractRequired;
    }

    public void setCreditContractRequired(String creditContractRequired) {
        this.creditContractRequired = creditContractRequired;
    }

    @OneToMany
    @Cascade(value = {org.hibernate.annotations.CascadeType.REFRESH})
    @JoinColumn(name = "id", insertable = false, updatable = false)
    public Set<BisBifInit> getBisBifInitSet() {
        return bisBifInitSet;
    }
    public void setBisBifInitSet(Set<BisBifInit> bisBifInitSet) {
        this.bisBifInitSet = bisBifInitSet;
    }
    @Column(name = "BANK_KIND")
    public String getBankKind() {
        return bankKind;
    }
    public void setBankKind(String bankKind) {
        this.bankKind = bankKind;
    }
    @Column(name = "IS_SYSTEM", nullable = true, length = 1)
    public String getIsSystem() {
        return isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  erp.bank_type
     */
    @Column(name = "BANK_TYPE", nullable = true, length = 4)
    public String getBankType() {
        return this.bankType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  erp.bank_type
     */
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  erp.type_name
     */
    @Column(name = "TYPE_NAME", nullable = false, length = 30)
    public String getTypeName() {
        return this.typeName;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  erp.type_name
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  ep.bank_prefix
     */
    @Column(name = "BANK_PREFIX", nullable = false, length = 20)
    public String getBankPrefix() {
        return this.bankPrefix;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  ep.bank_prefix
     */
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  erp.rmk
     */
    @Column(name = "RMK", nullable = true, length = 100)
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  erp.rmk
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  erp.valid_sign
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return this.validSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  erp.valid_sign
     */
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  创建人登录名称
     */
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  更新人登录名称
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "NETBANK_CFCA_DATE")
    public Date getNetbankCFCADate() {
        return netbankCFCADate;
    }
    public void setNetbankCFCADate(Date netbankCFCADate) {
        this.netbankCFCADate = netbankCFCADate;
    }
    @Column(name = "IS_DOMESTIC_BANK", nullable = true, length = 1)
    public String getIsDomesticBank() {
        return isDomesticBank;
    }
    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }
    @Column(name = "BANK_NAME_PREFIX")
    public String getBankNamePrefix() {
        return bankNamePrefix;
    }
    public void setBankNamePrefix(String bankNamePrefix) {
        this.bankNamePrefix = bankNamePrefix;
    }
    @Column(name = "ICON")
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    @Column(name = "GRADIENT")
    public String getGradient() {
        return gradient;
    }
    public void setGradient(String gradient) {
        this.gradient = gradient;
    }
    @Column(name = "REMIT_METHOD")
    public String getRemitMethod() {
        return remitMethod;
    }
    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }

    @Column(name = "IS_THIRD_PARTY")
    public String getIsThirdParty() {
        return isThirdParty;
    }

    public void setIsThirdParty(String isThirdParty) {
        this.isThirdParty = isThirdParty;
    }
}
