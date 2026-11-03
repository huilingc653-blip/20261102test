package com.bytter.system.api;
import com.bytter.system.api.dto.AppNotificationPushDTO;
/**
 * Created by zhouj on 2017/10/14.
 */
public interface IPushMessageService {
    boolean isPushSwitch();
    /**
     * 发送待审批工作流的通知到对应审批人-通知接口（需处理业务)
     *
     * @param userId               用户ID
     * @param taskId               待办任务ID
     * @param processDefinitionKey 流程分类KEY
     * @param object               模版解析对象
     */
    void pushTodoApprovalNotificationToApprover(String userId, String taskId, String processDefinitionKey, Object object);
    /**
     * 推送 普通通知 到APP
     *
     * @param appNotificationPushDTO DTO
     */
    void pushNotification(AppNotificationPushDTO appNotificationPushDTO);
}
