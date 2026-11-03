package com.bytter.workflow.dto;
import com.bytter.core.annotation.User;
import java.io.Serializable;
import java.util.Date;
/**
 * @author Frederik Heremans
 */
public class CommentDTO implements Serializable {
    private String id;
    private String author;
    @User(inject = "username")
    private String userId;
    private String username;
    private String message;
    private String fullMessage;
    private Date time;
    private String taskId;
    private String taskName;
    private String taskUrl;
    private String processInstanceId;
    private String processInstanceUrl;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Date getTime() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getTaskUrl() {
        return taskUrl;
    }
    public void setTaskUrl(String taskUrl) {
        this.taskUrl = taskUrl;
    }
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    public String getProcessInstanceUrl() {
        return processInstanceUrl;
    }
    public void setProcessInstanceUrl(String processInstanceUrl) {
        this.processInstanceUrl = processInstanceUrl;
    }
    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getFullMessage() {
        return fullMessage;
    }
    public void setFullMessage(String fullMessage) {
        this.fullMessage = fullMessage;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
