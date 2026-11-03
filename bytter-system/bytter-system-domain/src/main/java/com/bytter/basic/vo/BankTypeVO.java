package com.bytter.basic.vo;
import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.DateUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
public class BankTypeVO implements Serializable {
    @BankType(inject = "typeName")
    private String id;
    @BtCompositeSearch(fieldName = "bank_Type")
    private String bankType;
    /**
     * erp.type_name
     */
    @BtCompositeSearch(fieldName = "type_Name")
    private String typeName;
    /**
     * ep.bank_prefix
     */
    @BtCompositeSearch(fieldName = "bank_Prefix")
    private String bankPrefix;
    /**
     * erp.rmk
     */
    @BtCompositeSearch(fieldName = "rmk")
    private String rmk;
    /**
     * 有效标志
     */
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    @BtCompositeSearch(fieldName = "valid_Sign")
    private String validSign;
    private String validSignName;
    /**
     * 网银证书到期日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "netbank_cfca_date")
    private Date netbankCFCADate;
    private String netbankCFCADateStr;
    /**
     * 是否境内银行
     */
    @BtCompositeSearch(fieldName = "is_domestic_bank")
    private String isDomesticBank;
    /**
     * 开户行名称前缀，针对313和404开头的银行
     */
    @BtCompositeSearch(fieldName = "bank_name_Prefix")
    private String bankNamePrefix;
    /**
     * 银行种类
     */
    @Dict(dictGroup = "sys.bankType", inject = "bankKindName")
    private String bankKind;
    private String bankKindName;
    /**
     * 开户行名称前缀，针对313和402开头的银行
     */
    private String icon;
    /**
     * 将变色
     */
    private String gradient;
    /**
     * 汇款方式
     */
    private String remitMethod;
    private String remitMethodName;

    /**
     * 是否直连银行
     */
    @Dict(dictGroup = "sf_yn", inject = "whetherConnectBankName")
    @BtCompositeSearch(fieldName = "whether_connect_bank")
    private String whetherConnectBank;
    private String whetherConnectBankName;
    /**
     * 是否为第三方平台
     */
    private String isThirdParty;

    /**
     * 授信合同是否必须
     */
    @Dict(dictGroup = "sf_yn", inject = "creditContractRequiredName")
    private String creditContractRequired;
    private String creditContractRequiredName;

    public String getWhetherConnectBank() {
        return whetherConnectBank;
    }

    public void setWhetherConnectBank(String whetherConnectBank) {
        this.whetherConnectBank = whetherConnectBank;
    }

    public String getCreditContractRequired() {
        return creditContractRequired;
    }

    public void setCreditContractRequired(String creditContractRequired) {
        this.creditContractRequired = creditContractRequired;
    }

    public String getNetbankCFCADateStr() {
        return netbankCFCADateStr;
    }
    public void setNetbankCFCADateStr(String netbankCFCADateStr) {
        this.netbankCFCADateStr = netbankCFCADateStr;
    }
    public Date getNetbankCFCADate() {
        return netbankCFCADate;
    }
    public void setNetbankCFCADate(Date netbankCFCADate) {
        this.netbankCFCADate = netbankCFCADate;
        if (null != netbankCFCADate) {
            this.netbankCFCADateStr = DateUtils.formatDate(this.netbankCFCADate);
        }
    }
    private String isSystem;
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
    public String getBankKind() {
        return bankKind;
    }
    public void setBankKind(String bankKind) {
        this.bankKind = bankKind;
    }
    public String getBankKindName() {
        return bankKindName;
    }
    public void setBankKindName(String bankKindName) {
        this.bankKindName = bankKindName;
    }
    public String getIsSystem() {
        return isSystem;
    }
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getBankPrefix() {
        return bankPrefix;
    }
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getValidSignName() {
        return validSignName;
    }
    public void setValidSignName(String validSignName) {
        this.validSignName = validSignName;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getIsDomesticBank() {
        return isDomesticBank;
    }
    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }
    public String getBankNamePrefix() {
        return bankNamePrefix;
    }
    public void setBankNamePrefix(String bankNamePrefix) {
        this.bankNamePrefix = bankNamePrefix;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getGradient() {
        return gradient;
    }
    public void setGradient(String gradient) {
        this.gradient = gradient;
    }
    public String getRemitMethod() {
        return remitMethod;
    }
    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }
    public String getRemitMethodName() {
        return remitMethodName;
    }
    public void setRemitMethodName(String remitMethodName) {
        this.remitMethodName = remitMethodName;
    }

    public String getIsThirdParty() {
        return isThirdParty;
    }

    public void setIsThirdParty(String isThirdParty) {
        this.isThirdParty = isThirdParty;
    }
}
