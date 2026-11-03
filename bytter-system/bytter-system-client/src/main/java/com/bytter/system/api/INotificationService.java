package com.bytter.system.api;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.dto.SocketMsg;

import java.util.List;
import java.util.Map;
/**
 * 消息推送通知
 * Created by zhoul on 2017/10/27.
 */
public interface INotificationService {
    String getTagById(String id);
    /**
     * 指定用户发送消息
     *
     * @param businessId 业务ID
     * @param userId     用户ID
     * @param tag        标签
     * @see NotificationConstant
     */
    void sendToUser(String businessId, String userId, String tag);
    void sendToUser(String businessId, String userId, String tag, Object object);
    /**
     * @param businessId 业务ID
     * @param userId     用户ID
     * @param tag        标签
     * @param type       类型：1、通知。2、待处理
     * @param object     模版解析对象
     */
    void sendToUser(String businessId, String userId, String tag, Integer type, Object object);
    /**
     * 给多个用户发送消息
     *
     * @param businessId 业务ID
     * @param userIds    用户ID结合
     * @param tag        标签 @see com.bytter.facade.sys.constant.NotificationConstant
     */
    void sendToUser(String businessId, List<String> userIds, String tag);
    void sendToUser(String businessId, List<String> userIds, String tag, Object object);
    /**
     * 根据资源权限发通知给有资源权限的用户
     * Created by chenlw on 2019/04/25.
     *
     * @param resPermission 资源权限
     * @param tag           标签 @see com.bytter.facade.sys.constant.NotificationConstant
     * @param object        消息模版解析对象
     */
    void sendToUserByResPermission(String resPermission, String tag, Object object);
    /**
     * 根据资源权限发通知给有资源权限的用户
     *
     * @param businessId    业务ID
     * @param resPermission 资源权限
     * @param tag           标签 @see com.bytter.facade.sys.constant.NotificationConstant
     * @param object        消息模版解析对象
     * @param sendCorpId    发送单位ID
     */
    void sendToUserByResPermission(String businessId, String resPermission, String tag, Object object, String sendCorpId);
    //void sendToUserByResPermission(String businessId, String resPermission, String tag, Object object);
//    void sendToUserIgnoreMySelf(String businessId, List<String> userIds, String tag);
//
//    void sendToUserIgnoreMySelf(String businessId, List<String> userIds, String tag, Object object);
    void sendToUserByLoginName(String businessId, String loginName, String tag);
    void sendToUserByLoginName(String businessId, String loginName, String tag, Object object);
    void sendToUserByLoginName(String businessId, List<String> loginName, String tag);
    void sendToUserByLoginName(String businessId, List<String> loginName, String tag, Object object);
    /**
     * 发送消息提醒，忽略自身
     *
     * @param businessId
     * @param loginName
     * @param tag
     */
    void sendToUserByLoginNameIgnoreMySelf(String businessId, List<String> loginName, String tag, String ignoreUserId);
    void sendToUserByLoginNameIgnoreMySelf(String businessId, List<String> loginName, String tag, Object object, String ignoreUserId);
    /**
     * 根据分类中的资源和菜单查找对应的用户
     * 1、优先根据分类中的资源查找用户
     * 2、否则根据菜单查找用户
     *
     * @param businessId 业务ID
     * @param tag        分类 @see com.bytter.facade.sys.constant.NotificationConstant
     */
    void send(String businessId, String corpId, String tag);
    void send(String businessId, String corpId, String tag, Object object);
    void sendIgnoreMySelf(String businessId, String corpId, String tag, String ignoreUserId);
    void sendIgnoreMySelf(String businessId, String corpId, String tag, Object object, String ignoreUserId);
    /**
     * 完成通知提醒
     *
     * @param tag        标签  @see com.bytter.facade.sys.constant.NotificationConstant
     * @param businessId 业务ID
     */
    void complete(String tag, String businessId, String currentUserId);
    /**
     * 根据tag消息类型清除所有的这类型的消息提醒
     * Created by chenlw on 2019/04/25.
     *
     * @param tag 分类 @see com.bytter.facade.sys.constant.NotificationConstant
     */
    void completeListByTag(String tag);
    /**
     * 获取并解析流程的模板消息内容
     *
     * @param notificationId 消息分类ID
     * @param object         参数对象
     * @return 模板消息内容
     */
    String getNotificationMessageTemplateContent(String notificationId, Object object);
    /**
     * 获取待办数量
     *
     * @param userId
     * @return
     */
    Integer getUserNotificationCount(String userId);

    /**
     * 服务端给某用户推送socket消息
     * @param userId 用户Id
     */
    Boolean sendSocketMsg(String userId, SocketMsg socketMsg);

    /**
     * 服务端给某用户推送socket消息
     * @param userIds 用户Id
     */
    Boolean sendSocketMsg(SocketMsg socketMsg,String ...userIds);

    /**
     * 广播消息
     * @param socketMsg 消息体
     */
    Boolean broadcastMessage(SocketMsg socketMsg);

    /**
     * 能否查询到相关资源，相关业务的消息记录
     *
     * @param businessId
     * @param tag
     * @return
     */
    Boolean listByBusinessIdAndTag(String businessId, String tag);
    
    /**
     * 代办数据
     * @param userId
     * @return
     */
    String getUserNotification(String userId);
}
