package com.bytter.finance.domain.basedata.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: CMS_REPAY_TYPE
 * @date 2022-08-26 14:32:39
 */
@Entity
@Table(name = "CMS_REPAY_TYPE", schema = "")
@JsonIgnoreProperties(value = { "handler", "hibernateLazyInitializer", "fieldHandler" })
@SuppressWarnings("serial")
public class RepayType extends IdEntity implements java.io.Serializable {


    /**
     * 还款方式编码
     */
    private String repayCode;
    /**
     * 还款方式名称
     */
    private String repayName;
    /**
     * 还款方式分类(D:按时间周期；C：按次数；P：不规则（有计划）；N随借随还（无计划）；示例：DD)
     */
    private String repayClass;
    /**
     * 本金支付频率(第一列)
     */
    private String capitalFrequencyType;
    /**
     * 本金支付频率(第二列)
     */
    private String capitalFrequencyNext;
    /**
     * 本金支付频率(第三列年专用)
     */
    private String capitalFrequencyThird;
    /**
     * 本金支付频率(第三列)
     */
    private String capitalFrequency;
    /**
     * 本金支付频率(第四列)
     */
    private String capitalFrequencyNum;
    /**
     * 本金支付频率详情
     */
    private String capitalFrequencyDetail;
    /**
     * 利息支付频率(第一列)
     */
    private String interestFrequencyType;
    /**
     * 利息支付频率(第二列)
     */
    private String interestFrequencyNext;

    /**
     * 利息支付频率(第三列年专用)
     */
    private String interestFrequencyThird;

    /**
     * 利息支付频率(第三列)
     */
    private String interestFrequency;

    /**
     * 利息支付频率(第四列)
     */
    private String interestFrequencyNum;

    /**
     * 是否当月/当季计息
     */
    private String interestFrequencyThis;

    /**
     * 利息支付频率是否同本金支付频率
     */
    private String isFrequencySame;
    /**
     * 本息关系
     */
    private String capitalAndInterest;
    /**
     * 还本时还息
     */
    private String interestRepayType;
    /**
     * 还本时还息详情
     */
    private String interestRepayTypeNext;
    /**
     * 利息计算方式
     */
    private String interestWay;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 状态
     */
    private Integer status;

    /**
     * 计息日
     */
    private Integer interestDay;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新日期
     */
    private Date updateDate;

    @Column(name = "REPAY_CODE")
    public String getRepayCode() {
        return repayCode;
    }

    public void setRepayCode(String repayCode) {
        this.repayCode = repayCode;
    }

    @Column(name = "REPAY_NAME")
    public String getRepayName() {
        return repayName;
    }

    public void setRepayName(String repayName) {
        this.repayName = repayName;
    }

    @Column(name = "REPAY_CLASS")
    public String getRepayClass() {
        return repayClass;
    }

    public void setRepayClass(String repayClass) {
        this.repayClass = repayClass;
    }

    @Column(name = "CAPITAL_FREQUENCY_TYPE")
    public String getCapitalFrequencyType() {
        return capitalFrequencyType;
    }

    public void setCapitalFrequencyType(String capitalFrequencyType) {
        this.capitalFrequencyType = capitalFrequencyType;
    }

    @Column(name = "CAPITAL_FREQUENCY_NEXT")
    public String getCapitalFrequencyNext() {
        return capitalFrequencyNext;
    }

    public void setCapitalFrequencyNext(String capitalFrequencyNext) {
        this.capitalFrequencyNext = capitalFrequencyNext;
    }

    @Column(name = "CAPITAL_FREQUENCY_THIRD")
    public String getCapitalFrequencyThird() {
        return capitalFrequencyThird;
    }

    public void setCapitalFrequencyThird(String capitalFrequencyThird) {
        this.capitalFrequencyThird = capitalFrequencyThird;
    }

    @Column(name = "CAPITAL_FREQUENCY_NUM")
    public String getCapitalFrequencyNum() {
        return capitalFrequencyNum;
    }

    public void setCapitalFrequencyNum(String capitalFrequencyNum) {
        this.capitalFrequencyNum = capitalFrequencyNum;
    }

    @Column(name = "CAPITAL_FREQUENCY_DETAIL")
    public String getCapitalFrequencyDetail() {
        return capitalFrequencyDetail;
    }

    public void setCapitalFrequencyDetail(String capitalFrequencyDetail) {
        this.capitalFrequencyDetail = capitalFrequencyDetail;
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

    @Column(name = "INTEREST_FREQUENCY_THIRD")
    public String getInterestFrequencyThird() {
        return interestFrequencyThird;
    }

    public void setInterestFrequencyThird(String interestFrequencyThird) {
        this.interestFrequencyThird = interestFrequencyThird;
    }

    @Column(name = "INTEREST_FREQUENCY_NUM")
    public String getInterestFrequencyNum() {
        return interestFrequencyNum;
    }

    public void setInterestFrequencyNum(String interestFrequencyNum) {
        this.interestFrequencyNum = interestFrequencyNum;
    }

    @Column(name = "INTEREST_FREQUENCY_THIS")
    public String getInterestFrequencyThis() {
        return interestFrequencyThis;
    }

    public void setInterestFrequencyThis(String interestFrequencyThis) {
        this.interestFrequencyThis = interestFrequencyThis;
    }

    @Column(name = "IS_FREQUENCY_SAME")
    public String getIsFrequencySame() {
        return isFrequencySame;
    }

    public void setIsFrequencySame(String isFrequencySame) {
        this.isFrequencySame = isFrequencySame;
    }

    @Column(name = "CAPITAL_AND_INTEREST")
    public String getCapitalAndInterest() {
        return capitalAndInterest;
    }

    public void setCapitalAndInterest(String capitalAndInterest) {
        this.capitalAndInterest = capitalAndInterest;
    }

    @Column(name = "INTEREST_REPAY_TYPE")
    public String getInterestRepayType() {
        return interestRepayType;
    }

    public void setInterestRepayType(String interestRepayType) {
        this.interestRepayType = interestRepayType;
    }

    @Column(name = "INTEREST_REPAY_TYPE_NEXT")
    public String getInterestRepayTypeNext() {
        return interestRepayTypeNext;
    }

    public void setInterestRepayTypeNext(String interestRepayTypeNext) {
        this.interestRepayTypeNext = interestRepayTypeNext;
    }

    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return interestWay;
    }

    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }

    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "CAPITAL_FREQUENCY")
    public String getCapitalFrequency() {
        return capitalFrequency;
    }

    public void setCapitalFrequency(String capitalFrequency) {
        this.capitalFrequency = capitalFrequency;
    }

    @Column(name = "INTEREST_FREQUENCY")
    public String getInterestFrequency() {
        return interestFrequency;
    }

    public void setInterestFrequency(String interestFrequency) {
        this.interestFrequency = interestFrequency;
    }

    @Column(name = "INTEREST_DAY")
    public Integer getInterestDay() {
        return interestDay;
    }

    public void setInterestDay(Integer interestDay) {
        this.interestDay = interestDay;
    }
}
