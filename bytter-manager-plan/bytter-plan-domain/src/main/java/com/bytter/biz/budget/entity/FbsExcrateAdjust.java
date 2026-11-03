package com.bytter.biz.budget.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**   
 * @Title: Entity
 * @Description: FBS_EXCRATE_ADJUST
 * @author wangzx
 * @date 2019-08-15 9:25:46
 * @version 1.0
 *
 */
@Entity
@Table(name = "FBS_EXCRATE_ADJUST", schema = "")
@SuppressWarnings("serial")
public class FbsExcrateAdjust extends IdEntity implements java.io.Serializable {
    /**原始货币*/
    private java.lang.String curId;
    /**目标货币*/
    private java.lang.String referCurId;
    /**计划汇率*/
    private java.math.BigDecimal exchangeRate;
    /**调整时间*/
    private java.util.Date preTime;
    /**示例说明*/
    private java.lang.String example;
    /**备注*/
    private java.lang.String rmk;;
    /**创建人登录名称*/
    private java.lang.String createBy;
    /**创建日期*/
    private java.util.Date createDate;
    /**更新人登录名称*/
    private java.lang.String updateBy;
    /**更新日期*/
    private java.util.Date updateDate;
    @Column(name ="CUR_ID",nullable=true,length=32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="REFER_CUR_ID",nullable=true,length=32)
    public String getReferCurId() {
        return referCurId;
    }
    public void setReferCurId(String referCurId) {
        this.referCurId = referCurId;
    }
    @Column(name ="EXCHANGE_RATE",nullable=true)
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    @Column(name ="PRE_TIME",nullable=true)
    public Date getPreTime() {
        return preTime;
    }
    public void setPreTime(Date preTime) {
        this.preTime = preTime;
    }
    @Column(name ="EXAMPLE",nullable=true,length=100)
    public String getExample() {
        return example;
    }
    public void setExample(String example) {
        this.example = example;
    }
    @Column(name ="RMK",nullable=true,length=100)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name ="CREATE_BY",nullable=true)
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
    @Column(name ="UPDATE_BY",nullable=true)
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
}
