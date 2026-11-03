package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.Date;
public class SysCountryCustDTO implements Serializable {
    /**
     * ID
     */
    private String id;
    /**
     * 层级 0-国家1-地区
     */
    private String countryLevel;
    /**
     * 国家ID
     */
    private String sysId;
    /**
     * 中文名称
     */
    private String nameZh;
    /**
     * 英文名称
     */
    private String nameEn;
    /**
     * 币种
     */
    private String curId;
    private String englishCode;
    private String curName;
    /**
     * 简称
     */
    private String enCode;
    /**
     * 代码
     */
    private String code;
    /**
     * 时区
     */
    private String timeZoneId;
    private String timeZone;
    /**
     * 有效标志
     */
    private String validSign;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCountryLevel() {
        return countryLevel;
    }
    public void setCountryLevel(String countryLevel) {
        this.countryLevel = countryLevel;
    }
    public String getSysId() {
        return sysId;
    }
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getEnglishCode() {
        return englishCode;
    }
    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getEnCode() {
        return enCode;
    }
    public void setEnCode(String enCode) {
        this.enCode = enCode;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getTimeZoneId() {
        return timeZoneId;
    }
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
    public String getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
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
}
