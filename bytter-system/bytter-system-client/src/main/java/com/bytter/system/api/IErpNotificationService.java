package com.bytter.system.api;
import java.util.List;
/**
 * ERP通知接口service
 * - 通用通知接口（无需处理消息）
 * - 链接通知接口（带跳转链接，需处理业务）
 *
 * @author chenlw
 * @date 2020/03
 */
public interface IErpNotificationService {
    /**
     * 消息类型：0，文本消息（无需处理消息）
     */
    String UNIVERSAL_NOTIFICATION = "0";
    /**
     * 消息类型：1，带链接消息（带跳转链接，需处理业务）
     */
    String UNIVERSAL_LINK_NOTIFICATION = "1";
    /**
     * 是否推送消息到ERP接口
     *
     * @return true表示允许推送；false表示不允许推送
     */
    boolean isAllowedPushNotification();
    /***************************  通用通知接口（无需处理消息） start *******************************/
    void sendUniversalNotificationToUserByUserId(String userId, String notificationContent);
    void sendUniversalNotificationToUserByUserId(String userId, String notificationCategoryId, String notificationContent);
    void sendUniversalNotificationToUserByUserIdList(List<String> userIds, String notificationContent);
    void sendUniversalNotificationToUserByUserIdList(List<String> userIds, String notificationCategoryId, String notificationContent);
    /***************************  通用通知接口（无需处理消息） end *******************************/
    /***************************  通用链接通知接口（带跳转链接，需处理业务) start *******************************/
    /**
     * 通用链接通知（带跳转链接，需处理业务)
     *
     * @param userId              通知接收者ID
     * @param notificationContent 通知内容
     * @param url                 跳转链接
     */
    void sendUniversalLinkNotificationToUserByUserId(String userId, String notificationContent, String url);
    /**
     * 发送待审批工作流的通知到对应审批人-通用链接通知接口（带跳转链接，需处理业务)
     *
     * @param processDefinitionKey 流程分类KEY
     * @param businessKey          业务ID
     * @param object               模版解析对象
     */
    void sendTodoApprovalLinkNotificationToApprover(String processDefinitionKey, String businessKey, Object object);
    /***************************  通用链接通知接口（带跳转链接，需处理业务) end *******************************/
}
