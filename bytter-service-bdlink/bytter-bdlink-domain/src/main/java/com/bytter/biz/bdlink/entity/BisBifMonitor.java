package com.bytter.biz.bdlink.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: 银行接口监控预警表
 * @date 2022-07-07 17:07:12
 */
@Entity
@Table(name = "BIS_BIF_MONITOR", schema = "")
@SuppressWarnings("serial")
public class BisBifMonitor extends IdEntity implements java.io.Serializable {
    /**
     * 接口代码ID
     */
    private String bisBifId;
    /**
     * 任务类型
     */
    private String taskType;
    /**
     * 角色ID
     */
    private String roleIds;
    /**
     * 监控频率
     */
    private Integer monitorHz;
    /**
     * 状态
     */
    private String status;
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
    @Column(name = "BIS_BIF_ID")
    public String getBisBifId() {
        return bisBifId;
    }
    public void setBisBifId(String bisBifId) {
        this.bisBifId = bisBifId;
    }
    @Column(name = "TASK_TYPE")
    public String getTaskType() {
        return taskType;
    }
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    @Column(name = "ROLE_IDS")
    public String getRoleIds() {
        return roleIds;
    }
    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
    @Column(name = "MONITOR_HZ")
    public Integer getMonitorHz() {
        return monitorHz;
    }
    public void setMonitorHz(Integer monitorHz) {
        this.monitorHz = monitorHz;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "CREATE_BY", nullable = true, length = 50)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE", nullable = true)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY", nullable = true, length = 50)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE", nullable = true)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
