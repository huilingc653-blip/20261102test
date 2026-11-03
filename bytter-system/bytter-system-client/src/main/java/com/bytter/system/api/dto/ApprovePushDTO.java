package com.bytter.system.api.dto;
import com.bytter.system.api.enums.PushCateGoryEnum;
import com.bytter.system.api.enums.PushMessageEnum;
import java.io.Serializable;
/**
 * 推送消息DTO
 * Created by zhouj on 2017/10/14.
 */
public class ApprovePushDTO implements Serializable {
    //发送者id
    private String userId;
    /**
     * 接受者id
     */
    private String targetId;
    /**
     * taskID
     */
    private String taskId;
    /**
     * processInstanceId
     */
    private String processInstanceId;
    /**
     * 推送标题
     */
    private String title;
    /**
     * 推送内容
     */
    private String content;
    /**
     * 消息分类
     */
    private PushCateGoryEnum pushCateGoryEnum;
    /**
     * 消息类型
     */
    private PushMessageEnum pushMessageEnum;
    public ApprovePushDTO() {
    }
    public ApprovePushDTO(String userId, String targetId, String taskId, String processInstanceId, String title, String content, PushCateGoryEnum pushCateGoryEnum, PushMessageEnum pushMessageEnum) {
        super();
        this.userId = userId;
        this.targetId = targetId;
        this.taskId = taskId;
        this.processInstanceId = processInstanceId;
        this.title = title;
        this.content = content;
        this.pushCateGoryEnum = pushCateGoryEnum;
        this.pushMessageEnum = pushMessageEnum;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getTargetId() {
        return targetId;
    }
    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }
    public String getTaskId() {
        return taskId;
    }
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    public String getProcessInstanceId() {
        return processInstanceId;
    }
    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public PushCateGoryEnum getPushCateGoryEnum() {
        return pushCateGoryEnum;
    }
    public void setPushCateGoryEnum(PushCateGoryEnum pushCateGoryEnum) {
        this.pushCateGoryEnum = pushCateGoryEnum;
    }
    public PushMessageEnum getPushMessageEnum() {
        return pushMessageEnum;
    }
    public void setPushMessageEnum(PushMessageEnum pushMessageEnum) {
        this.pushMessageEnum = pushMessageEnum;
    }
}
