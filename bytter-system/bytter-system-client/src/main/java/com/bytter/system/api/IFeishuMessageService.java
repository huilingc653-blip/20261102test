package com.bytter.system.api;
/**
 * 飞书-消息推送接口
 *
 * @author liyh  2022-11-10
 * @since 3.4
 */
public interface IFeishuMessageService {
    /**
     * 是否推送消息到ERP接口
     *
     * @return true表示允许推送；false表示不允许推送
     */
    boolean isAllowedPushMessage();
    /**
     * 是否推送审批办结消息给审批发起人
     *
     * @return true表示允许推送；false表示不允许推送
     */
    boolean isPushApprovalCompletedMsgToApplicant();
    /**
     * 推送Markdown语法的消息
     *
     * @param userId  用户ID
     * @param title   标题
     * @param content 消息内容
     */
    void pushMarkdownMessage(String userId, String title, String content);
//    /**
//     * 发送待审批工作流的通知到对应审批人-通用链接通知接口（带跳转链接，需处理业务)
//     *
//     * @param userId               用户ID
//     * @param taskId               待办任务ID
//     * @param processDefinitionKey 流程分类KEY
//     * @param object               模版解析对象
//     */
//    void pushTodoApprovalLinkNotificationToApprover(String userId, String taskId, String processDefinitionKey, Object object);
    /**
     * 发送待审批工作流的通知到对应审批人-通用链接通知接口
     *
     * @param userId               用户ID
     * @param processDefinitionKey 流程分类KEY
     * @param businessKey          业务ID
     * @param object               模版解析对象
     */
    void pushTodoNotificationToApprover(String userId, String processDefinitionKey, String businessKey, Object object);
}
