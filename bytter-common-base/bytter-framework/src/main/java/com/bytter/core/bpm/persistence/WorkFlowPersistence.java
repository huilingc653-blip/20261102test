package com.bytter.core.bpm.persistence;
import com.bytter.core.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
@Slf4j
/**
 * @author zhoul
 * @since 2018/1/5.
 */
public class WorkFlowPersistence extends HashMap<String,Object> implements Serializable {
    public static final String TASK_ID = "taskId";
    public static final String TASK_NAME = "taskName";
    public static final String PROCESS_INSTANCE_ID = "processInstanceId";
    public static final String BUSINESS_KEY = "businessKey";
    public WorkFlowPersistence() {
    }
    public WorkFlowPersistence(String processInstanceId, String businessKey) {
        put(PROCESS_INSTANCE_ID, processInstanceId);
        put(BUSINESS_KEY, businessKey);
    }
    public WorkFlowPersistence(String taskId, String taskName, String processInstanceId, String businessKey) {
        put(TASK_ID, taskId);
        put(TASK_NAME, taskName);
        put(PROCESS_INSTANCE_ID, processInstanceId);
        put(BUSINESS_KEY, businessKey);
    }
    public String getTaskId() {
        return (String) get(TASK_ID);
    }
    public void setTaskId(String taskId) {
        put(TASK_ID, taskId);
    }
    public String getTaskName() {
        return (String) get(TASK_NAME);
    }
    public void setTaskName(String taskName) {
        put(TASK_NAME, taskName);
    }
    public String getProcessInstanceId() {
        return (String) get(PROCESS_INSTANCE_ID);
    }
    public void setProcessInstanceId(String processInstanceId) {
        put(PROCESS_INSTANCE_ID, processInstanceId);
    }
    public String getBusinessKey() {
        return (String) get(BUSINESS_KEY);
    }
    public void setBusinessKey(String businessKey) {
        put(BUSINESS_KEY, businessKey);
    }
    public void setData(Object data) {
        try {
            String jsonString = JsonUtils.toJSONStringWithNull(data);
            Map<String, Object> map= JsonUtils.json2MapWithNull(jsonString);
            putAll(map);
        } catch (Exception e) {
            log.error("数据序列化出错：：", e);
        }
    }
}
