package com.bytter.workflow.domain.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
@Data
public class HistoricActivityInstanceVO implements Serializable {
    protected String id;
    protected String activityId;
    protected String activityName;
    protected String activityType;
    protected String processInstanceId;
    protected String processDefinitionId;
    protected String executionId;
    protected String taskId;
    protected String calledProcessInstanceId;
    protected String assignee;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date endTime;
    protected Long durationInMillis;
    protected String deleteReason;
    protected String tenantId = "";
    private String username;
}
