package com.bytter.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import javax.persistence.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 接口代码维护
 * @date 2017-05-19 17:07:12
 */
@Entity
@Table(name = "BIS_BIF_INIT", schema = "")
@SuppressWarnings("serial")
public class BisBifInit extends IdEntity implements java.io.Serializable {
    /**
     * 接口代码
     */
    private String bifCode;
    /**
     * 接口代码名称
     */
    private String name;
    /**
     * 银行类别编码
     */
    private String bankTypeId;
    /**
     * 有效标志
     */
    private String validSign;
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
     * 是否系统初始化 1 ：是；0：否
     */
    private String isSystem;
    private BankType bankType;
    /**
     * 是否境内银行
     */
    private String isDomesticBank;
    /**
     * 是否支持银行外汇牌价查询
     */
    private String isSupportBankForeign;
    /**
     * 掉线提醒
     */
    private String disConnectionAlert;
    @Column(name = "IS_SUPPORT_BANK_FOREIGN", nullable = true, length = 1)
    public String getIsSupportBankForeign() {
        return isSupportBankForeign;
    }
    public void setIsSupportBankForeign(String isSupportBankForeign) {
        this.isSupportBankForeign = isSupportBankForeign;
    }
    @Column(name = "IS_DOMESTIC_BANK", nullable = true, length = 1)
    public String getIsDomesticBank() {
        return isDomesticBank;
    }
    public void setIsDomesticBank(String IsDomesticBank) {
        this.isDomesticBank = IsDomesticBank;
    }
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(value = {CascadeType.REFRESH})
    @JoinColumn(name = "bank_Type_Id", insertable = false, updatable = false)
    public BankType getBankType() {
        return bankType;
    }
    @JsonManagedReference
    public void setBankType(BankType bankType) {
        this.bankType = bankType;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  是否系统初始化
     */
    @Column(name = "IS_SYSTEM", nullable = true, length = 1)
    public String getIsSystem() {
        return this.isSystem;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  是否系统初始化
     */
    public void setIsSystem(String isSystem) {
        this.isSystem = isSystem;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  接口代码
     */
    @Column(name = "BIF_CODE", nullable = false, length = 4)
    public String getBifCode() {
        return this.bifCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  接口代码
     */
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  接口代码名称
     */
    @Column(name = "NAME", nullable = false, length = 30)
    public String getName() {
        return this.name;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  接口代码名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  银行类别
     */
    @Column(name = "BANK_TYPE_ID", nullable = true, length = 4)
    public String getBankTypeId() {
        return this.bankTypeId;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  银行类别
     */
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String  有效标志
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 1)
    public String getValidSign() {
        return this.validSign;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String  有效标志
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
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
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
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date  更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "DIS_CONNECTION_ALERT")
    public String getDisConnectionAlert() {
        return disConnectionAlert;
    }
    public void setDisConnectionAlert(String disConnectionAlert) {
        this.disConnectionAlert = disConnectionAlert;
    }
}
