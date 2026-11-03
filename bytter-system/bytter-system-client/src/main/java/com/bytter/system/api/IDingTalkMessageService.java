package com.bytter.system.api;

import com.bytter.system.api.dto.GroupChatBotMsgDTO;

import java.util.List;

/**
 * 钉钉-消息推送接口
 *
 * @author chenlw  2020-11-18
 * @since 3.4
 */
public interface IDingTalkMessageService {
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

    /**
     * 群聊机器人群发消息
     *
     * @param token 机器人token
     * @param secret 机器人安全验证secrest
     * @param content 文本内容
     * @param isAllPush 是否发送所有人
     * @param userIds 要@发送的钉钉用户id列表
     * @param phoneNumbers 要@发送的手机号列表
     */
    GroupChatBotMsgDTO groupChatRobotPushText(String token, String secret, String content, boolean isAllPush, List<String> userIds, List<String> phoneNumbers);
}
