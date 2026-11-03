package com.bytter.workflow.dto;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
/**
 * @author zuoy 2020-08-12
 */
public class TaskResponseDTO implements Serializable {
    private String taskId;
    private String taskName;
    private String taskDefinitionKey;
    private String userId;
    private String owner;
    private String ownerName;
    private String userName;
    private String commentType;
    private String comment;
    private Date startTime;
    private Date endTime;
    private Boolean activity;
    private String taskPassFlag;
    private Set<String> roleNames;
    /**
     * 耗时
     */
    private Long durationTime;
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getTaskDefinitionKey() {
        return taskDefinitionKey;
    }
    public void setTaskDefinitionKey(String taskDefinitionKey) {
        this.taskDefinitionKey = taskDefinitionKey;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwnerName() {
        return ownerName;
    }
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getCommentType() {
        return commentType;
    }
    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public Boolean getActivity() {
        return activity;
    }
    public void setActivity(Boolean activity) {
        this.activity = activity;
    }
    public String getTaskPassFlag() {
        return taskPassFlag;
    }
    public void setTaskPassFlag(String taskPassFlag) {
        this.taskPassFlag = taskPassFlag;
    }
    public Long getDurationTime() {
        return durationTime;
    }
    public void setDurationTime(Long durationTime) {
        this.durationTime = durationTime;
    }
    public Set<String> getRoleNames() {
        return roleNames;
    }
    public void setRoleNames(Set<String> roleNames) {
        this.roleNames = roleNames;
    }
}
