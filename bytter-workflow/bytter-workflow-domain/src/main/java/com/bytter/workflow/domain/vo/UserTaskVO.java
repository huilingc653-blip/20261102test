package com.bytter.workflow.domain.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserTaskVO {

    private String taskName;

    @JsonIgnore
    private String taskDefinitionKey;

    private List<TaskVo> taskList = new ArrayList<>();

    private Date startTime;

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

    public List<TaskVo> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<TaskVo> taskList) {
        this.taskList = taskList;
    }

    public void addTask(TaskVo taskVO){
        this.taskList.add(taskVO);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
