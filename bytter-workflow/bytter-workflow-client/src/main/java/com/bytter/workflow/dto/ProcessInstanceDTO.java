package com.bytter.workflow.dto;
import java.util.Date;
import java.util.Map;
/**
 * 用于dubbo参数传递，其他模块不用引入flowable模块
 */
public interface ProcessInstanceDTO{
    String getId();
    boolean isEnded();
    String getActivityId();
    String getProcessInstanceId();
    String getParentId();
    String getSuperExecutionId();
    String getRootProcessInstanceId();
    String getReferenceId();
    String getReferenceType();
    String getPropagatedStageInstanceId();
    String getProcessDefinitionId();
    String getProcessDefinitionName();
    String getProcessDefinitionKey();
    Integer getProcessDefinitionVersion();
    String getDeploymentId();
    String getBusinessKey();
    String getBusinessStatus();
    boolean isSuspended();
    Map<String, Object> getProcessVariables();
    String getTenantId();
    String getName();
    String getDescription();
    String getLocalizedName();
    String getLocalizedDescription();
    Date getStartTime();
    String getStartUserId();
    String getCallbackId();
    String getCallbackType();
}
