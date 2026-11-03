package com.bytter.workflow.domain.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.camunda.bpm.engine.task.Task;
import java.io.Serializable;
import java.util.Date;
@Data
/**
 * @author Frederik Heremans
 */
public class TaskVo implements Serializable {
    protected String id;
    protected String url;
    protected String owner;
    protected String assignee;
    private String assigneeName;
    protected String delegationState;
    protected String name;
    protected String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date dueDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date claimTime;
    protected int priority;
    protected boolean suspended;
    protected String taskDefinitionKey;
    protected String tenantId;
    protected String category;
    protected String formKey;
    // References to other resources
    protected String parentTaskId;
    protected String parentTaskUrl;
    protected String executionId;
    protected String executionUrl;
    protected String processInstanceId;
    protected String processInstanceUrl;
    protected String processDefinitionId;
    protected String processDefinitionUrl;
    private String comment;
    private String drawComment;
    private Integer status;
    public static TaskVo from(Task task) {
        TaskVo taskVo = new TaskVo();
        taskVo.setId(task.getId());
        taskVo.setOwner(task.getOwner());
        taskVo.setName(task.getName());
        taskVo.setDescription(task.getDescription());
        taskVo.setProcessDefinitionId(task.getProcessDefinitionId());
        taskVo.setProcessInstanceId(task.getProcessInstanceId());
        taskVo.setAssignee(task.getAssignee());
        taskVo.setExecutionId(task.getExecutionId());
        taskVo.setTaskDefinitionKey(task.getTaskDefinitionKey());
        return taskVo;
    }
}
