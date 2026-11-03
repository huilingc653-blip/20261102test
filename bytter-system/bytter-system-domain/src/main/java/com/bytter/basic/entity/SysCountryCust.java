package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 国家地区表
 * @date 2020-1-19 11:14:22
 */
@Entity
@Table(name = "SYS_COUNTRY_CUST")
public class SysCountryCust extends IdEntity implements java.io.Serializable {
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
    @Column(name = "COUNTRY_LEVEL", nullable = false, length = 1)
    public String getCountryLevel() {
        return countryLevel;
    }
    public void setCountryLevel(String countryLevel) {
        this.countryLevel = countryLevel;
    }
    @Column(name = "SYS_ID", nullable = false, length = 32)
    public String getSysId() {
        return sysId;
    }
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
    @Column(name = "NAME_ZH", nullable = false, length = 32)
    public String getNameZh() {
        return nameZh;
    }
    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
    @Column(name = "NAME_EN", nullable = false, length = 32)
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    @Column(name = "CUR_ID", nullable = false, length = 32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "EN_CODE", nullable = false, length = 30)
    public String getEnCode() {
        return enCode;
    }
    public void setEnCode(String enCode) {
        this.enCode = enCode;
    }
    @Column(name = "CODE", nullable = false, length = 30)
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "TIME_ZONE_ID", nullable = false, length = 32)
    public String getTimeZoneId() {
        return timeZoneId;
    }
    public void setTimeZoneId(String timeZoneId) {
        this.timeZoneId = timeZoneId;
    }
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
