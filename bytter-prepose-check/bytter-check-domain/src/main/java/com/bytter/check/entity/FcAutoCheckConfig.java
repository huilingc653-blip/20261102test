package com.bytter.check.entity;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 自动对账配置
 * @date 2022-09-20
 */
@Entity
@Table(name = "FC_AUTO_CHECK_CONFIG", schema = "")
public class FcAutoCheckConfig extends IdEntity implements java.io.Serializable {
    /**
     * 自动对账策略代码
     */
    private String autoCheckCode;
    /**
     * 自动对账策略名称
     */
    private String autoCheckName;
    /**
     * 对账类型
     */
    private String checkType;
    /**
     * 策略生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date launchTime;
    /**
     * 对账周期
     */
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
    @Column(name = "AUTO_CHECK_CODE")
    public String getAutoCheckCode() {
        return autoCheckCode;
    }
    public void setAutoCheckCode(String autoCheckCode) {
        this.autoCheckCode = autoCheckCode;
    }
    @Column(name = "AUTO_CHECK_NAME")
    public String getAutoCheckName() {
        return autoCheckName;
    }
    public void setAutoCheckName(String autoCheckName) {
        this.autoCheckName = autoCheckName;
    }
    @Column(name = "CHECK_TYPE")
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    @Column(name = "LAUNCH_TIME")
    public Date getLaunchTime() {
        return launchTime;
    }
    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }
    @Column(name = "CHECK_CYCLE")
    public String getCheckCycle() {
        return checkCycle;
    }
    public void setCheckCycle(String checkCycle) {
        this.checkCycle = checkCycle;
    }
    @Column(name = "VALID_SIGN")
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
    @Column(name = "TASK_ID")
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
