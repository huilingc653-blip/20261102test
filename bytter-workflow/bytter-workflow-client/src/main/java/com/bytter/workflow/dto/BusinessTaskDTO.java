package com.bytter.workflow.dto;
import lombok.Data;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/7/29.
 */
@Data
public class BusinessTaskDTO implements Serializable {
    private String taskId;
    private String taskName;
    private String processInstanceId;
    private String businessKey;
    private String category;
    private String assignee;
}
