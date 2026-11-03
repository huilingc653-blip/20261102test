package com.bytter.biz.netbank.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 自动支付策略表
 * @author huangx
 * @date 2020-04-17 14:38:52
 * @version 1.0
 *
 */
@Entity
@Table(name = "BIS_PAY_POLICY", schema = "")
@SuppressWarnings("serial")
public class BisPayPolicy extends IdEntity implements java.io.Serializable {
    /**单位*/
    private String corpId;
    /**调拨策略代码*/
    private String policyCode;
    /**调拨策略名称*/
    private String policyName;
    /**币别*/
    private String curId;
    /**范围1  10＞、11＞＝、20＜、21＜＝*/
    private String range1;
    /**范围值1*/
    private BigDecimal rangeVal1;
    /**范围拼接 1-并且  2-或者*/
    private String rangeJoin;
    /**范围2  10＞、11＞＝、20＜、21＜＝*/
    private String range2;
    /**范围值2*/
    private BigDecimal rangeVal2;
    /**范围值2*/
    private BigDecimal payTotal;
    /**是否启用自动支付 0-否  1-是*/
    private String validSign;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**是否跳过工作日*/
    private String isWorkDay;
    /**用途*/
    private String noPayPurpose;
    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name ="POLICY_CODE",nullable=false)
    public String getPolicyCode() {
        return policyCode;
    }
    public void setPolicyCode(String policyCode) {
        this.policyCode = policyCode;
    }
    @Column(name ="POLICY_NAME",nullable=true)
    public String getPolicyName() {
        return policyName;
    }
    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }
    @Column(name ="CUR_ID",nullable=true)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="RANGE1",nullable=true,length=1)
    public String getRange1() {
        return range1;
    }
    public void setRange1(String range1) {
        this.range1 = range1;
    }
    @Column(name ="RANGE_VAL1",nullable=true,scale=2,length=15)
    public BigDecimal getRangeVal1() {
        return rangeVal1;
    }
    public void setRangeVal1(BigDecimal rangeVal1) {
        this.rangeVal1 = rangeVal1;
    }
    @Column(name ="RANGE_JOIN",nullable=true,length=1)
    public String getRangeJoin() {
        return rangeJoin;
    }
    public void setRangeJoin(String rangeJoin) {
        this.rangeJoin = rangeJoin;
    }
    @Column(name ="RANGE2",nullable=true,length=1)
    public String getRange2() {
        return range2;
    }
    public void setRange2(String range2) {
        this.range2 = range2;
    }
    @Column(name ="RANGE_VAL2",nullable=true,scale=2,length=15)
    public BigDecimal getRangeVal2() {
        return rangeVal2;
    }
    public void setRangeVal2(BigDecimal rangeVal2) {
        this.rangeVal2 = rangeVal2;
    }
    @Column(name ="PAY_TOTAL",nullable=true,scale=2,length=15)
    public BigDecimal getPayTotal() {
        return payTotal;
    }
    public void setPayTotal(BigDecimal payTotal) {
        this.payTotal = payTotal;
    }
    @Column(name ="VALID_SIGN",nullable=true,length=1)
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name ="CREATE_BY",nullable=true,length=50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name ="CREATE_DATE",nullable=true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name ="UPDATE_BY",nullable=true,length=50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name ="UPDATE_DATE",nullable=true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name ="IS_WORK_DAY")
    public String getIsWorkDay() {
        return isWorkDay;
    }

    public void setIsWorkDay(String isWorkDay) {
        this.isWorkDay = isWorkDay;
    }

    @Column(name ="NO_PAY_PURPOSE")
    public String getNoPayPurpose() {
        return noPayPurpose;
    }

    public void setNoPayPurpose(String noPayPurpose) {
        this.noPayPurpose = noPayPurpose;
    }
}
