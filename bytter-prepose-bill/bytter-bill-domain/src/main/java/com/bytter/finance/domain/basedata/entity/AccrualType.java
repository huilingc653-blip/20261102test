package com.bytter.finance.domain.basedata.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: CMS_ACCRUAL_TYPE
 * @author onlineGenerator
 * @date 2022-08-10 14:32:39
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_ACCRUAL_TYPE", schema = "")
@SuppressWarnings("serial")
public class AccrualType extends IdEntity implements java.io.Serializable {
    /** 计提方式编码 */
    private String accrualCode;
    /** 计提方式名称 */
    private String accrualName;
    /** 计提频率(第一列) */
    private String frequencyType;
    /**
     * 计提频率(第二列)
     */
    private String frequency;
    /** 计提频率(第三列) */
    private String frequencyDailyDate;
    /**
     * 计提日
     */
    private Integer accruedDailyDate;
    /** 备注 */
    private String rmk;
    /** 有效标志 */
    private String validSign;
    /** 状态 */
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    @Column(name = "ACCRUAL_CODE")
    public String getAccrualCode() {
        return accrualCode;
    }
    public void setAccrualCode(String accrualCode) {
        this.accrualCode = accrualCode;
    }
    @Column(name = "ACCRUAL_NAME")
    public String getAccrualName() {
        return accrualName;
    }
    public void setAccrualName(String accrualName) {
        this.accrualName = accrualName;
    }
    @Column(name = "FREQUENCY_TYPE")
    public String getFrequencyType() {
        return frequencyType;
    }
    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }
    @Column(name = "FREQUENCY_DAILY_DATE")
    public String getFrequencyDailyDate() {
        return frequencyDailyDate;
    }
    public void setFrequencyDailyDate(String frequencyDailyDate) {
        this.frequencyDailyDate = frequencyDailyDate;
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
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "FREQUENCY")
    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    @Column(name = "ACCRUED_DAILY_DATE")
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
}
