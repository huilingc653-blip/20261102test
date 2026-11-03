package com.bytter.workflow.domain.vo;
import com.bytter.core.utils.DateUtils;
import lombok.Data;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.history.HistoricTaskInstance;
import org.camunda.bpm.engine.task.Task;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
@Data
/**
 * Created by zhoul on 2017/10/11.
 */
public class TaskDetailVo implements Serializable {
    private String taskId;
    private String taskName;
    private String taskDefinitionKey;
    private String userId;
    //委托人,当owner不为空，则userId是 被委托人
    private String owner;
    private String ownerName;
    private String userName;
    private String commentType;
    private String comment;
    private Date startTime;
    private Date endTime;
    private Boolean activity;
    private String taskPassFlag;
    /**
     * 耗时
     */
    private Long durationTime;
    private Set<String> roleNames;
    public static TaskDetailVo from(HistoricTaskInstance historicTaskInstance) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        taskDetailVo.taskName = historicTaskInstance.getName();
        taskDetailVo.taskId = historicTaskInstance.getId();
        taskDetailVo.taskDefinitionKey = historicTaskInstance.getTaskDefinitionKey();
        taskDetailVo.startTime = historicTaskInstance.getStartTime();
        taskDetailVo.userId = historicTaskInstance.getAssignee();
        taskDetailVo.endTime = historicTaskInstance.getEndTime();
        taskDetailVo.activity = false;
        taskDetailVo.owner = historicTaskInstance.getOwner();
        taskDetailVo.setDurationTime(0L);
        if (taskDetailVo.endTime != null && taskDetailVo.startTime != null) {
            taskDetailVo.setDurationTime(DateUtils.getOffsetSeconds(taskDetailVo.startTime, taskDetailVo.endTime) * 1000L);
        }
        return taskDetailVo;
    }
    public static TaskDetailVo from(HistoricProcessInstance historicProcessInstance) {
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        taskDetailVo.startTime = historicProcessInstance.getStartTime();
        taskDetailVo.userId = historicProcessInstance.getStartUserId();
        taskDetailVo.endTime = historicProcessInstance.getEndTime();
        taskDetailVo.taskName = historicProcessInstance.getProcessDefinitionName();
        taskDetailVo.activity = Boolean.FALSE;
        taskDetailVo.setDurationTime(0L);
        if (taskDetailVo.endTime != null && taskDetailVo.startTime != null) {
            taskDetailVo.setDurationTime(DateUtils.getOffsetSeconds(taskDetailVo.startTime, taskDetailVo.endTime) * 1000L);
        }
        return taskDetailVo;
    }
    public  static TaskDetailVo from(Task task){
        TaskDetailVo taskDetailVo = new TaskDetailVo();
        taskDetailVo.taskName  = task.getName();
        taskDetailVo.taskId = task.getId();
        taskDetailVo.taskDefinitionKey = task.getTaskDefinitionKey();
        taskDetailVo.userId = task.getAssignee();
        taskDetailVo.activity = true;
        taskDetailVo.startTime = task.getCreateTime();
        taskDetailVo.owner = task.getOwner();
        return taskDetailVo;
    }
}
