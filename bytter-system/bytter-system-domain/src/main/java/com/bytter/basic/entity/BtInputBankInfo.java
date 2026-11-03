package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 银行行号维护
 * @date 2017-05-23 19:06:25
 */
@Entity
@Table(name = "BT_INPUT_BANK_INFO")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BtInputBankInfo extends IdEntity implements java.io.Serializable {
    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 开户行名称
     */
    private String bankName;
    /**
     * 银行类别
     */
    private String bankPrefix;
    /**
     * 地区码
     */
    private String areaCode;
    private String bankCityCode;
    /**
     * 省份
     */
    private String prov;
    /**
     * 市区
     */
    private String city;
    /**
     * 数据来源
     */
    private String ibpsCode;
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
     * swift code
     */
    private String swiftCode;
    /**
     * 方法: 取得String
     *
     * @return: String  开户行行号
     */
    @Column(name = "SYS_BANK_CODE", nullable = false, length = 30)
    public String getBankCode() {
        return this.bankCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  开户行行号
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  开户行名称
     */
    @Column(name = "BANK_NAME", nullable = false, length = 200)
    public String getBankName() {
        return this.bankName;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  开户行名称
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  银行类别
     */
    @Column(name = "BANK_PREFIX", nullable = true, length = 32)
    public String getBankPrefix() {
        return this.bankPrefix;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  银行类别
     */
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  地区码
     */
    @Column(name = "BANK_CITY_CODE", nullable = true, length = 30)
    public String getAreaCode() {
        return this.areaCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  地区码
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  省份
     */
    @Column(name = "PROV", nullable = true, length = 32)
    public String getProv() {
        return this.prov;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  省份
     */
    public void setProv(String prov) {
        this.prov = prov;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  市区
     */
    @Column(name = "CITY", nullable = true, length = 32)
    public String getCity() {
        return this.city;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  市区
     */
    public void setCity(String city) {
        this.city = city;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  数据来源
     */
    @Column(name = "IBPS_CODE", nullable = false, length = 30)
    public String getIbpsCode() {
        return this.ibpsCode;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  数据来源
     */
    public void setIbpsCode(String ibpsCode) {
        this.ibpsCode = ibpsCode;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  创建人登录名称
     */
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得Date
     *
     * @return: Date  创建日期
     */
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置Date
     *
     * @param: Date  创建日期
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得String
     *
     * @return: String  更新人登录名称
     */
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置String
     *
     * @param: String  更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得Date
     *
     * @return: Date  更新日期
     */
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置Date
     *
     * @param: Date  更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "SWIFT_CODE", nullable = false, length = 30)
    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    @Transient
    public String getBankCityCode() {
        return getAreaCode();
    }
    public void setBankCityCode(String bankCityCode) {
        setAreaCode(bankCityCode);
    }

}
