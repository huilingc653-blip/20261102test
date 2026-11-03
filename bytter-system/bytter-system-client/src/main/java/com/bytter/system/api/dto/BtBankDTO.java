package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.Date;
/*
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: BT_BANK
 * @date 2017-07-20 14:28:41
 */
public class BtBankDTO implements Serializable {
    private String id;
    /*
     * 开户行行号
     */
    private String bankCode;
    /*
     * 开户行名称
     */
    private String bankName;
    /*
     * 银行类别名称
     */
    private String bankTypeName;
    /*
     * 银行类别
     */
    private String bankPrefix;
    /*
     * 地区码
     */
    private String areaCode;
    private String bankCityCode;
    /*
     * 省份
     */
    private String prov;
    /*
     * 市区
     */
    private String city;
    /*
     * 数据来源
     */
    private String ibpsCode;
    /*
     * 创建人登录名称
     */
    private String createBy;
    /*
     * 创建日期
     */
    private Date createDate;
    /*
     * 更新人登录名称
     */
    private String updateBy;
    /*
     * 更新日期
     */
    private Date updateDate;
    /*
     * 银行类别code
     */
    private String bankTypeId;
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
    public String getBankPrefix() {
        return bankPrefix;
    }
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    public String getAreaCode() {
        return areaCode;
    }
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    public String getBankCityCode() {
        return getAreaCode();
    }
    public void setBankCityCode(String bankCityCode) {
        setAreaCode(bankCityCode);
    }
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getIbpsCode() {
        return ibpsCode;
    }
    public void setIbpsCode(String ibpsCode) {
        this.ibpsCode = ibpsCode;
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
}
