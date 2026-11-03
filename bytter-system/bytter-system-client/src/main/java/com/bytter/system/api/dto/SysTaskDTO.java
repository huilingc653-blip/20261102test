package com.bytter.system.api.dto;
import com.bytter.core.base.entity.ICurrentUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
/**
 * Created by zhouj on 2018/9/29.
 */
public class SysTaskDTO implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 任务名称
     */
    private String taskname;
    /**
     * 状态
     */
    private Integer active;
    /**
     * 任务类型
     */
    private Integer type;
    /**
     * 任务命令
     */
    private String command;
    /**
     * 任务参数
     */
    private String arguments;
    /**
     * 执行方式
     */
    private Integer period;
    /**
     * 表达式
     */
    private String cron;
    /**
     * 开始日期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    /**
     * 结束日期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;
    private ICurrentUser currentUser;
    /**
     * 有效标志
     */
    private String validSign;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTaskname() {
        return taskname;
    }
    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }
    public Integer getActive() {
        return active;
    }
    public void setActive(Integer active) {
        this.active = active;
    }
    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public String getArguments() {
        return arguments;
    }
    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
    public Integer getPeriod() {
        return period;
    }
    public void setPeriod(Integer period) {
        this.period = period;
    }
    public String getCron() {
        return cron;
    }
    public void setCron(String cron) {
        this.cron = cron;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public ICurrentUser getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(ICurrentUser currentUser) {
        this.currentUser = currentUser;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
}
