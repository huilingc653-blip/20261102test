package com.bytter.system.api;

import com.bytter.system.api.dto.GroupChatBotMsgDTO;
import com.bytter.system.api.dto.NotificationExtDTO;

import java.util.List;

/**
 * 企业微信-消息推送接口
 * <p>
 * userId一般对应用户的loginName
 *
 * @author chenlw
 * @date 2020/03/02
 */
public interface ICorpWeiXinMessageService {
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
    NotificationExtDTO pushMarkdownMessage(String userId, String title, String content);

    /**
     *
     * @param userId
     * @param content
     * @return
     */
    NotificationExtDTO pushTextMessage(String userId, String content);

    /**
     * 发送待审批工作流的通知到对应审批人-通用链接通知接口（带跳转链接，需处理业务)
     *
     * @param userId               用户ID
     * @param taskId               待办任务ID
     * @param processDefinitionKey 流程分类KEY
     * @param object               模版解析对象
     */
    void pushTodoApprovalLinkNotificationToApprover(String userId, String taskId, String processDefinitionKey, Object object);

    /**
     * 群聊机器人群发消息
     *
     * @param webhook webhook地址
     * @param content 文本内容
     * @param isAllPush 是否发送所有人
     * @param userIds 要@发送的企业微信用户名称列表
     * @param phoneNumbers 要@发送的企业微信手机号列表
     */
    GroupChatBotMsgDTO groupChatRobotPushText(String webhook, String content, boolean isAllPush, List<String> userIds, List<String> phoneNumbers);
}
