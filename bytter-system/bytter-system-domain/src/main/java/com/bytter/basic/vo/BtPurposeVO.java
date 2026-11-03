package com.bytter.basic.vo;
import com.bytter.core.annotation.BankType;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import javax.validation.constraints.NotNull;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 用途维护表
 * @date 2017-05-24 09:57:15
 */
@SuppressWarnings("serial")
public class BtPurposeVO extends ExcelModel implements java.io.Serializable {
    private String id;
    /**
     * 用途
     */
    @Excel(name = "*用途")
    @NotNull(message = "nis.netbank.message.purpose.null")
    @BtCompositeSearch(fieldName = "p.purpose")
    private String purpose;
    /**
     * 所属类别
     */
    @BtCompositeSearch(fieldName = "p.sys_bus_type")
    private String sysBusType;
    @Excel(name = "*所属类别")
    @NotNull(message = "bt.purpose.message.sysbustype.null")
    private String sysBusTypeName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private java.util.Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private java.util.Date updateDate;
    /**
     * 用途单位
     */
    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "pc.corp_id")
    private String corpId;
    @Excel(name = "*共享范围")
    @BtCompositeSearch(fieldName = "corpId")
    private String corpName;
    /**
     * 用途币种
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "pc.cur_id")
    private String curId;
    @Excel(name = "币别")
    private String curName;
    /**
     * 用途银行类别
     */
    @BankType(inject = "bankTypeName")
    private String bankTypeId;
    @Excel(name = "银行类别")
    private String bankTypeName;
    /**
     * 用途地区
     */
    private String countryId;
    @Excel(name = "国家/地区")
    private String countryName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getSysBusType() {
        return sysBusType;
    }
    public void setSysBusType(String sysBusType) {
        this.sysBusType = sysBusType;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
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
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getCountryId() {
        return countryId;
    }
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }
    public String getSysBusTypeName() {
        return sysBusTypeName;
    }
    public void setSysBusTypeName(String sysBusTypeName) {
        this.sysBusTypeName = sysBusTypeName;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getCountryName() {
        return countryName;
    }
    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    @Override
    public String getBeanName() {
        return "btPurposeService";
    }
}
