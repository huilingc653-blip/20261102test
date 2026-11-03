package com.bytter.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Region;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
@ExcelTarget("BtInputBankInfoVO")
public class BtInputBankInfoVO extends ExcelModel implements Serializable {
    private String id;
    /**
     * 开户行行号
     */
    @Excel(name = "*开户行行号")
    @NotNull(message = "table.btInputBankInfo.bankCode.null")
    @BtCompositeSearch(fieldName = "ibi.sys_bank_code")
    private String bankCode;
    /**
     * 开户行名称
     */
    @Excel(name = "*开户行名称")
    @NotNull(message = "table.btInputBankInfo.bankName.null")
    @BtCompositeSearch(fieldName = "ibi.bank_name")
    private String bankName;
    /**
     * 省份
     */
    @BtCompositeSearch(fieldName = "r.parent_id")
    @Region(inject = "provName")
    private String prov;
    @BtCompositeSearch(fieldName = "r.parent_id")
    private String provName;
    /**
     * 市区
     */
    @BtCompositeSearch(fieldName = "r.id")
    @Region(inject = "cityName")
    private String city;
    @BtCompositeSearch(fieldName = "r.id")
    private String cityName;
    private String bankPrefix;
    private String bankCityCode;
    /**
     * 银行类型
     */
    private String bankTypeId;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 授信合同是否必须
     */
    private String creditContractRequired;
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "ibi.update_Date")
    private Date updateDate;
    /**
     * 是否第三方平台
     */
    private String isthirdparty;

    /**
     * swift code
     */

    @BtCompositeSearch(fieldName = "bs.SWIFT_CODE")
    private String swiftCode;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getBankPrefix() {
        return bankPrefix;
    }
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    public String getBankCityCode() {
        return bankCityCode;
    }
    public void setBankCityCode(String bankCityCode) {
        this.bankCityCode = bankCityCode;
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
    public String getCreditContractRequired() {
        return creditContractRequired;
    }
    public void setCreditContractRequired(String creditContractRequired) {
        this.creditContractRequired = creditContractRequired;
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

    public String getIsthirdparty() {
        return isthirdparty;
    }

    public void setIsthirdparty(String isthirdparty) {
        this.isthirdparty = isthirdparty;
    }


    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    @Override
    public String getBeanName() {
        return "btInputBankInfoService";
    }
}
