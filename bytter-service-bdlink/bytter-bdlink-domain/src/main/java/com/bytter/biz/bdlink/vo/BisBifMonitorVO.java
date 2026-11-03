package com.bytter.biz.bdlink.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;

import java.io.Serializable;
import java.util.Date;

/**
 * @author huangx
 * @version 1.0
 * @Title: VO
 * @Description: 银行接口监控预警表
 * @date 2022-07-07 17:07:12
 */
public class BisBifMonitorVO implements Serializable {
    private String id;
    /**
     * 接口代码ID
     */
    @BtCompositeSearch(fieldName = "b.bis_Bif_Id")
    private String bisBifId;
    private String bifCode;
    private String bifName;
    /**
     * 任务类型
     */
    @Dict(dictGroup = "bisBifMonitor.taskType", inject = "taskTypeName")
    private String taskType;
    private String taskTypeName;
    /**
     * 角色ID
     */
    private String roleIds;
    private String roleNames;
    /**
     * 监控频率
     */
    private Integer monitorHz;
    /**
     * 状态
     */
    @Dict(dictGroup = "fcCorpPool.status", inject = "statusName")
    @BtCompositeSearch(fieldName = "b.status")
    private String status;
    private String statusName;
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
    public String getBisBifId() {
        return bisBifId;
    }
    public void setBisBifId(String bisBifId) {
        this.bisBifId = bisBifId;
    }
    public String getBifCode() {
        return bifCode;
    }
    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }
    public String getBifName() {
        return bifName;
    }
    public void setBifName(String bifName) {
        this.bifName = bifName;
    }
    public String getTaskType() {
        return taskType;
    }
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }
    public String getTaskTypeName() {
        return taskTypeName;
    }
    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }
    public String getRoleIds() {
        return roleIds;
    }
    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }
    public String getRoleNames() {
        return roleNames;
    }
    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }
    public Integer getMonitorHz() {
        return monitorHz;
    }
    public void setMonitorHz(Integer monitorHz) {
        this.monitorHz = monitorHz;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
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
}
