package com.bytter.finance.domain.basedata.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
public class RepayTypeVO implements Serializable {

    /**
     * 主键id
     */
    private String id;

    /**
     * 还款方式编码
     */
    @BtCompositeSearch(fieldName = "t.repay_code")
    private String repayCode;
    /**
     * 还款方式名称
     */
    @BtCompositeSearch(fieldName = "t.repay_name")
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
     * 本金支付频率(详细值)
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
     * 计息日
     */
    private Integer interestDay;

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
    @BtCompositeSearch(fieldName = "t.CREATE_DATE")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepayCode() {
        return repayCode;
    }

    public void setRepayCode(String repayCode) {
        this.repayCode = repayCode;
    }

    public String getRepayName() {
        return repayName;
    }

    public void setRepayName(String repayName) {
        this.repayName = repayName;
    }

    public String getRepayClass() {
        return repayClass;
    }

    public void setRepayClass(String repayClass) {
        this.repayClass = repayClass;
    }

    public String getCapitalFrequencyType() {
        return capitalFrequencyType;
    }

    public void setCapitalFrequencyType(String capitalFrequencyType) {
        this.capitalFrequencyType = capitalFrequencyType;
    }

    public String getCapitalFrequencyNext() {
        return capitalFrequencyNext;
    }

    public void setCapitalFrequencyNext(String capitalFrequencyNext) {
        this.capitalFrequencyNext = capitalFrequencyNext;
    }

    public String getCapitalFrequencyThird() {
        return capitalFrequencyThird;
    }

    public void setCapitalFrequencyThird(String capitalFrequencyThird) {
        this.capitalFrequencyThird = capitalFrequencyThird;
    }

    public String getCapitalFrequencyNum() {
        return capitalFrequencyNum;
    }

    public void setCapitalFrequencyNum(String capitalFrequencyNum) {
        this.capitalFrequencyNum = capitalFrequencyNum;
    }

    public String getCapitalFrequencyDetail() {
        return capitalFrequencyDetail;
    }

    public void setCapitalFrequencyDetail(String capitalFrequencyDetail) {
        this.capitalFrequencyDetail = capitalFrequencyDetail;
    }

    public String getInterestFrequencyType() {
        return interestFrequencyType;
    }

    public void setInterestFrequencyType(String interestFrequencyType) {
        this.interestFrequencyType = interestFrequencyType;
    }

    public String getInterestFrequencyNext() {
        return interestFrequencyNext;
    }

    public void setInterestFrequencyNext(String interestFrequencyNext) {
        this.interestFrequencyNext = interestFrequencyNext;
    }

    public String getInterestFrequencyThird() {
        return interestFrequencyThird;
    }

    public void setInterestFrequencyThird(String interestFrequencyThird) {
        this.interestFrequencyThird = interestFrequencyThird;
    }

    public String getInterestFrequencyNum() {
        return interestFrequencyNum;
    }

    public void setInterestFrequencyNum(String interestFrequencyNum) {
        this.interestFrequencyNum = interestFrequencyNum;
    }

    public String getInterestFrequencyThis() {
        return interestFrequencyThis;
    }

    public void setInterestFrequencyThis(String interestFrequencyThis) {
        this.interestFrequencyThis = interestFrequencyThis;
    }

    public String getIsFrequencySame() {
        return isFrequencySame;
    }

    public void setIsFrequencySame(String isFrequencySame) {
        this.isFrequencySame = isFrequencySame;
    }

    public String getCapitalAndInterest() {
        return capitalAndInterest;
    }

    public void setCapitalAndInterest(String capitalAndInterest) {
        this.capitalAndInterest = capitalAndInterest;
    }

    public String getInterestRepayType() {
        return interestRepayType;
    }

    public void setInterestRepayType(String interestRepayType) {
        this.interestRepayType = interestRepayType;
    }

    public String getInterestRepayTypeNext() {
        return interestRepayTypeNext;
    }

    public void setInterestRepayTypeNext(String interestRepayTypeNext) {
        this.interestRepayTypeNext = interestRepayTypeNext;
    }

    public String getInterestWay() {
        return interestWay;
    }

    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
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

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCapitalFrequency() {
        return capitalFrequency;
    }

    public void setCapitalFrequency(String capitalFrequency) {
        this.capitalFrequency = capitalFrequency;
    }

    public String getInterestFrequency() {
        return interestFrequency;
    }

    public void setInterestFrequency(String interestFrequency) {
        this.interestFrequency = interestFrequency;
    }

    public Integer getInterestDay() {
        return interestDay;
    }

    public void setInterestDay(Integer interestDay) {
        this.interestDay = interestDay;
    }
}
