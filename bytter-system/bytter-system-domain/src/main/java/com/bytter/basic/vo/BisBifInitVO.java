package com.bytter.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
public class BisBifInitVO implements Serializable {
    private String id;
    /**
     * 接口代码
     */
    @BtCompositeSearch(fieldName = "bif_Code")
    private String bifCode;
    /**
     * 接口代码名称
     */
    @BtCompositeSearch(fieldName = "name")
    private String name;
    /**
     * 银行类别编码
     */
    @Dict(tableName = "bt_bank_type", valueFieldName = "id", showFiledName = "type_NAME", inject = "bankTypeName")
    private String bankTypeId;
    @BtCompositeSearch(fieldName = "bank_Type_Id")
    private String bankTypeName;
    /**
     * 有效标志
     */
    @Dict(dictGroup = "sf_yn", inject = "validSignName")
    @BtCompositeSearch(fieldName = "valid_Sign")
    private String validSign;
    private String validSignName;
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
    private String isSystem;
    /**
     * 是否境内银行
     */
    @BtCompositeSearch(fieldName = "is_domestic_bank")
    private String isDomesticBank;
    /**
     * 是否支持银行外汇牌价查询
     */
    @BtCompositeSearch(fieldName = "IS_SUPPORT_BANK_FOREIGN")
    private String isSupportBankForeign;
    /**
     * 掉线提醒
     */
    private String disConnectionAlert;
    public String getIsSupportBankForeign() {
        return isSupportBankForeign;
    }
    public void setIsSupportBankForeign(String isSupportBankForeign) {
        this.isSupportBankForeign = isSupportBankForeign;
    }
    public String getIsDomesticBank() {
        return isDomesticBank;
    }
    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
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
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
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
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getDisConnectionAlert() {
        return disConnectionAlert;
    }
    public void setDisConnectionAlert(String disConnectionAlert) {
        this.disConnectionAlert = disConnectionAlert;
    }
}
