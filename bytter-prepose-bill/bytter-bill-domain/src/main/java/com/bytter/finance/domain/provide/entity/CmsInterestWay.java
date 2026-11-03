package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: CMS_INTEREST_WAY
 * @author onlineGenerator
 * @date 2017-08-24 11:09:43
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_INTEREST_WAY", schema = "")
@SuppressWarnings("serial")
public class CmsInterestWay extends IdEntity implements java.io.Serializable {
    /** 发放ID */
    private String sendCode;
    /** 计息方式 */
    private String interestWay;
    /** 计息日 */
    private Integer expiryDay;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    /** 执行日期 */
    private java.util.Date activeDate;
    /** 上次更新日期 */
    private java.util.Date lastUpdateTime;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 预提日 */
    private Integer withholdingDay;
    /**
     * 利息设置第一列
     */
    private String interestFrequencyType;
    /**
     * 利息设置第二列
     */
    private String interestFrequencyNext;
    /**
     * 利息设置第三列
     */
    private String interestFrequency;
    /**
     * 利息设置第四列
     */
    private String interestFrequencyNum;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 发放ID
     */
    @Column(name = "SEND_CODE")
    public String getSendCode() {
        return this.sendCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             发放ID
     */
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 计息方式
     */
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return this.interestWay;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             计息方式
     */
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 计息日
     */
    @Column(name = "EXPIRY_DAY")
    public Integer getExpiryDay() {
        return this.expiryDay;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             计息日
     */
    public void setExpiryDay(Integer expiryDay) {
        this.expiryDay = expiryDay;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 执行日期
     */
    @Column(name = "ACTIVE_DATE")
    public java.util.Date getActiveDate() {
        return this.activeDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             执行日期
     */
    public void setActiveDate(java.util.Date activeDate) {
        this.activeDate = activeDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 上次更新日期
     */
    @Column(name = "LAST_UPDATE_TIME")
    public java.util.Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             上次更新日期
     */
    public void setLastUpdateTime(java.util.Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "WITHHOLDING_DAY")
    public Integer getWithholdingDay() {
        return withholdingDay;
    }
    public void setWithholdingDay(Integer withholdingDay) {
        this.withholdingDay = withholdingDay;
    }
    @Column(name = "INTEREST_FREQUENCY_TYPE")
    public String getInterestFrequencyType() {
        return interestFrequencyType;
    }
    public void setInterestFrequencyType(String interestFrequencyType) {
        this.interestFrequencyType = interestFrequencyType;
    }
    @Column(name = "INTEREST_FREQUENCY_NEXT")
    public String getInterestFrequencyNext() {
        return interestFrequencyNext;
    }
    public void setInterestFrequencyNext(String interestFrequencyNext) {
        this.interestFrequencyNext = interestFrequencyNext;
    }
    @Column(name = "INTEREST_FREQUENCY")
    public String getInterestFrequency() {
        return interestFrequency;
    }
    public void setInterestFrequency(String interestFrequency) {
        this.interestFrequency = interestFrequency;
    }
    @Column(name = "INTEREST_FREQUENCY_NUM")
    public String getInterestFrequencyNum() {
        return interestFrequencyNum;
    }
    public void setInterestFrequencyNum(String interestFrequencyNum) {
        this.interestFrequencyNum = interestFrequencyNum;
    }
}
