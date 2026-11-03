package com.bytter.check.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.util.Date;
public class FcAutoCheckConfigVO implements java.io.Serializable {
    private String id;
    /**
     * 自动对账策略代码
     */
    @BtCompositeSearch(fieldName = "fac.AUTO_CHECK_CODE")
    private String autoCheckCode;
    /**
     * 自动对账策略名称
     */
    @BtCompositeSearch(fieldName = "fac.AUTO_CHECK_NAME")
    private String autoCheckName;
    /**
     * 对账类型
     */
    @BtCompositeSearch(fieldName = "fac.CHECK_TYPE")
    private String checkType;
    /**
     * 策略生效时间
     */
    private Date launchTime;
    /**
     * 对账周期
     */
    @BtCompositeSearch(fieldName = "fac.CHECK_CYCLE")
    private String checkCycle;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 定时任务id
     */
    private String taskId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAutoCheckCode() {
        return autoCheckCode;
    }
    public void setAutoCheckCode(String autoCheckCode) {
        this.autoCheckCode = autoCheckCode;
    }
    public String getAutoCheckName() {
        return autoCheckName;
    }
    public void setAutoCheckName(String autoCheckName) {
        this.autoCheckName = autoCheckName;
    }
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public Date getLaunchTime() {
        return launchTime;
    }
    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }
    public String getCheckCycle() {
        return checkCycle;
    }
    public void setCheckCycle(String checkCycle) {
        this.checkCycle = checkCycle;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
