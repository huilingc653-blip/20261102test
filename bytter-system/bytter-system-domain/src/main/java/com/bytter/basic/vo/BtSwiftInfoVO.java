package com.bytter.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
/**
 * 境外银行行号信息维护
 *
 * @author qinl
 * @date 2019-07-15
 */
@ExcelTarget("BtSwiftInfoVO")
public class BtSwiftInfoVO extends ExcelModel implements Serializable {
    private String id;
    /**
     * swift code
     */
    @Excel(name = "*SWIFT CODE")
    @NotNull(message = "table.btswiftinfo.swiftcode.null")
    @BtCompositeSearch(fieldName = "t.swift_code")
    private String swiftCode;
    /**
     * 机构名称
     */
    @Excel(name = "*机构名称")
    @NotNull(message = "table.btswiftinfo.bankname.null")
    @BtCompositeSearch(fieldName = "t.bank_name")
    private String bankName;
    /**
     * 所在国家/地区
     */
    @BtCompositeSearch(fieldName = "t.country_code")
    private String countryCode;

    @Excel(name = "*所在国家/地区")
    @NotNull(message = "table.btswiftinfo.country.null")
    @BtCompositeSearch(fieldName = "c.name_zh")
    private String nameZh;
    private String nameEn;
    /**
     * 所在城市
     */
    @BtCompositeSearch(fieldName = "t.city_name")
    private String cityName;
    @BtCompositeSearch(fieldName = "a.name_Zh")
    @Excel(name = "*所在城市")
    @NotNull(message = "table.btswiftinfo.cityname.null")
    private String cityNameZh;
    /**
     * 地址
     */
    @Excel(name = "地址")
    @BtCompositeSearch(fieldName = "t.bank_add")
    private String bankAdd;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新人
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateDate;
    /**
     * 开户银行代码类型
     */
    private String clsFlag;

    /**
     * 银行类型id
     */
    @BtCompositeSearch(fieldName = "t.bank_type_id")
    private String bankTypeId;

    /**
     * 银行类型名称
     */
    @Excel(name = "*银行类型")
    private String bankTypeName;


    /**
     * 本地清算号
     */
    @BtCompositeSearch(fieldName = "t.local_Clear_No")
    private String localClearNo;


    public String getNameZh() {
        return nameZh;
    }
    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSwiftCode() {
        return swiftCode;
    }
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getCityNameZh() {
        return cityNameZh;
    }
    public void setCityNameZh(String cityNameZh) {
        this.cityNameZh = cityNameZh;
    }
    public String getBankAdd() {
        return bankAdd;
    }
    public void setBankAdd(String bankAdd) {
        this.bankAdd = bankAdd;
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
    public String getClsFlag() {
        return clsFlag;
    }
    public void setClsFlag(String clsFlag) {
        this.clsFlag = clsFlag;
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

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    @Override
    public String getBeanName() {
        return "btSwiftInfoServiceBase";
    }
}
