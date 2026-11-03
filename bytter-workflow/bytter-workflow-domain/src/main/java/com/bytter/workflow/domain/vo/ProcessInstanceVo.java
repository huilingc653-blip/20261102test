package com.bytter.workflow.domain.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
/**
 * Modified to add a "completed" flag, which lets the caller know if the process instance has run to completion without encountering a wait state or experiencing an error/ exception.
 *
 * @author Frederik Heremans
 * @author Ryan Johnston (@rjfsu)
 */
public class ProcessInstanceVo implements Serializable {
    protected String id;
    protected String url;
    protected String businessKey;
    protected boolean suspended;
    protected boolean ended;
    protected String processDefinitionId;
    protected String processDefinitionUrl;
    private String processDefinitionName;
    private String processDefinitionKey;
    private String processInstanceId;
    protected String activityId;
    private String flowCategoryId;
    protected String tenantId;
    protected String deploymentId;
    protected String executionName;
    // Added by Ryan Johnston
    protected boolean completed;
    //增加以下属性
    protected TaskVo taskVO;
    protected List<TaskVo> taskVoList;
    protected String startUserId;
    private String username;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date nowTime = new Date();
    private String corpName;
    private String corpCode;
    private String messageTemplate;
}
