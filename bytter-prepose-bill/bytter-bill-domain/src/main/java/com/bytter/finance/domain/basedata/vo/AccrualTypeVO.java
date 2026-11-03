package com.bytter.finance.domain.basedata.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.util.Date;
public class AccrualTypeVO implements Serializable {
    private String id;
    /**
     * 计提方式编码
     */
    @BtCompositeSearch(fieldName = "t.accrual_code")
    private String accrualCode;
    /**
     * 计提方式名称
     */
    @BtCompositeSearch(fieldName = "t.accrual_name")
    private String accrualName;
    /**
     * 计提频率(第一列)
     */
    private String frequencyType;
    /**
     * 计提频率(第二列)
     */
    private String frequency;
    /**
     * 计提频率(第三列)
     */
    private String frequencyDailyDate;
    /**
     * 计提日
     */
    private Integer accruedDailyDate;
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
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccrualCode() {
        return accrualCode;
    }
    public void setAccrualCode(String accrualCode) {
        this.accrualCode = accrualCode;
    }
    public String getAccrualName() {
        return accrualName;
    }
    public void setAccrualName(String accrualName) {
        this.accrualName = accrualName;
    }
    public String getFrequencyType() {
        return frequencyType;
    }
    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }
    public String getFrequencyDailyDate() {
        return frequencyDailyDate;
    }
    public void setFrequencyDailyDate(String frequencyDailyDate) {
        this.frequencyDailyDate = frequencyDailyDate;
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
    public String getFrequency() {
        return frequency;
    }
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
}
