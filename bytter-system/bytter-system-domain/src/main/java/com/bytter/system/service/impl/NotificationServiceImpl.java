package com.bytter.system.service.impl;

import com.bytter.core.base.AbstractService;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.I18nDTO;
import com.bytter.system.api.dto.SocketMsg;
import com.bytter.system.dao.CorpUserResourceDao;
import com.bytter.system.dao.NotificationDao;
import com.bytter.system.entity.NotificationCategory;
import com.bytter.system.entity.User;
import com.bytter.system.entity.UserRole;
import com.bytter.system.service.NotificationCategoryService;
import com.bytter.system.service.NotificationService;
import com.bytter.system.service.UserService;
import com.bytter.system.vo.NotificationCategoryVO;
import com.bytter.system.vo.NotificationResponseVO;
import com.bytter.velocity.VelocityUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Created by zhoul on 2017/10/27.
 */
@Service("notificationServiceI")
@DubboService
public class NotificationServiceImpl extends AbstractService implements INotificationService {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationCategoryService notificationCategoryService;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private UserService userService;
    @Autowired
    private CorpUserResourceDao corpUserResourceDao;
    @Autowired
    private NotificationDao notificationDao;
    

    @Override
    public String getTagById(String id) {
        NotificationCategory notificationCategory = notificationCategoryService.get(id);
        if (notificationCategory != null) {
            return notificationCategory.getTag();
        }
        return null;
    }
    @Override
    public void sendToUser(String businessId, String userId, String category) {
        try {
            notificationService.sendToUser(businessId, userId, category, null);
        } catch (Exception e) {
            logger.info("sendToUser failure", e);
        }
    }
    @Override
    public void sendToUser(String businessId, String userId, String tag, Object object) {
        try {
            notificationService.sendToUser(businessId, userId, tag, object);
        } catch (Exception e) {
            logger.info("sendToUser failure", e);
        }
    }
    @Override
    public void sendToUser(String businessId, String userId, String tag, Integer type, Object object) {
        try {
            notificationService.sendToUser(businessId, userId, tag, type, object);
        } catch (Exception e) {
            logger.info("sendToUser failure", e);
        }
    }
    @Override
    public void sendToUser(String businessId, List<String> userIds, String category) {
        try {
            notificationService.sendToUser(businessId, userIds, category, null);
        } catch (Exception e) {
            logger.info("sendToUser failure", e);
        }
    }
    @Override
    public void sendToUser(String businessId, List<String> userIds, String tag, Object object) {
        try {
            notificationService.sendToUser(businessId, userIds, tag, object);
        } catch (Exception e) {
            logger.info("sendToUser failure", e);
        }
    }
//    @Override
//    public void sendToUserIgnoreMySelf(String businessId, List<String> userIds, String tag) {
//        if (EmptyUtils.isEmpty(userIds)) {
//            return;
//        }
//        String userId = getLoginName();
//        userIds.remove(userId);
//        try {
//            notificationService.sendToUser(businessId, userIds, tag, null);
//        } catch (Exception e) {
//            logger.info("sendToUserIgnoreMySelf failure", e);
//        }
//    }
//
//    @Override
//    public void sendToUserIgnoreMySelf(String businessId, List<String> userIds, String tag, Object object) {
//        if (EmptyUtils.isEmpty(userIds)) {
//            return;
//        }
//        String userId = getLoginName();
//        userIds.remove(userId);
//        try {
//            notificationService.sendToUser(businessId, userIds, tag, object);
//        } catch (Exception e) {
//            logger.info("sendToUserIgnoreMySelf failure", e);
//        }
//    }
    @Override
    public void sendToUserByLoginName(String businessId, String loginName, String tag) {
        try {
            notificationService.sendToUserByLoginName(businessId, loginName, tag, null);
        } catch (Exception e) {
            logger.info("sendToUserByLoginName failure", e);
        }
    }
    @Override
    public void sendToUserByLoginName(String businessId, String loginName, String tag, Object object) {
        try {
            notificationService.sendToUserByLoginName(businessId, loginName, tag, object);
        } catch (Exception e) {
            logger.info("sendToUserByLoginName failure", e);
        }
    }
    @Override
    public void sendToUserByLoginName(String businessId, List<String> loginNameList, String tag) {
        try {
            notificationService.sendToUserByLoginName(businessId, loginNameList, tag, null);
        } catch (Exception e) {
            logger.info("sendToUserByLoginName failure", e);
        }
    }
    @Override
    public void sendToUserByLoginName(String businessId, List<String> loginNameList, String tag, Object object) {
        try {
            notificationService.sendToUserByLoginName(businessId, loginNameList, tag, object);
        } catch (Exception e) {
            logger.info("sendToUserByLoginName failure", e);
        }
    }
    @Override
    public void sendToUserByLoginNameIgnoreMySelf(String businessId, List<String> loginNameList, String tag, String ignoreUserId) {
        try {
            notificationService.sendToUserByLoginNameIgnoreMySelf(businessId, loginNameList, tag, null, ignoreUserId);
        } catch (Exception e) {
            logger.info("sendToUserByLoginNameIgnoreMySelf failure", e);
        }
    }
    @Override
    public void sendToUserByLoginNameIgnoreMySelf(String businessId, List<String> loginNameList, String tag, Object object, String ignoreUserId) {
        try {
            notificationService.sendToUserByLoginNameIgnoreMySelf(businessId, loginNameList, tag, object, ignoreUserId);
        } catch (Exception e) {
            logger.info("sendToUserByLoginNameIgnoreMySelf failure", e);
        }
    }
    @Override
    public void send(String businessId, String corpId, String category) {
        try {
            notificationService.send(businessId, corpId, category, null);
        } catch (Exception e) {
            logger.info("send failure", e);
        }
    }
    @Override
    public void send(String businessId, String corpId, String tag, Object object) {
        try {
            notificationService.send(businessId, corpId, tag, object);
        } catch (Exception e) {
            logger.info("send failure", e);
        }
    }
    @Override
    public void sendIgnoreMySelf(String businessId, String corpId, String tag, String ignoreUserId) {
        try {
            notificationService.sendIgnoreMySelf(businessId, corpId, tag, ignoreUserId);
        } catch (Exception e) {
            logger.info("send failure", e);
        }
    }
    @Override
    public void sendIgnoreMySelf(String businessId, String corpId, String tag, Object object, String ignoreUserId) {
        try {
            notificationService.sendIgnoreMySelf(businessId, corpId, tag, object, ignoreUserId);
        } catch (Exception e) {
            logger.info("send failure", e);
        }
    }
    /**
     * 根据资源权限发通知给有资源权限的用户
     * Created by chenlw on 2019/04/25.
     *
     * @param resPermission 资源权限
     * @param tag           标签 @see com.bytter.facade.sys.constant.NotificationConstant
     * @param object        消息模版解析对象
     */
    @Override
    public void sendToUserByResPermission(String resPermission, String tag, Object object) {
        try {
            notificationService.sendToUserByResPermission(null, resPermission, tag, object, null);
        } catch (Exception e) {
            logger.info("send failure", e);
        }
    }
    /**
     * 根据资源权限发通知给有资源权限的用户
     *
     * @param businessId    业务ID
     * @param resPermission 资源权限
     * @param tag           标签 @see com.bytter.facade.sys.constant.NotificationConstant
     * @param object        消息模版解析对象
     * @param sendCorpId    发送单位ID
     */
    @Override
    public void sendToUserByResPermission(String businessId, String resPermission, String tag, Object object, String sendCorpId) {
        try {
            notificationService.sendToUserByResPermission(businessId, resPermission, tag, object, sendCorpId);
        } catch (Exception e) {
            logger.info("send failure", e);
        }
    }
    @Override
    @Async("asyncThreadPoolExecutor")
    public void complete(String tag, String businessId, String currentUserId) {
        try {
            notificationService.complete(tag, businessId, currentUserId);

        } catch (Exception e) {
            logger.info("complete failure", e);
        }
    }
    /**
     * 根据tag消息类型清除所有的这类型的消息提醒
     * Created by chenlw on 2019/04/25.
     *
     * @param tag
     */
    @Override
    @Async("asyncThreadPoolExecutor")
    public void completeListByTag(String tag) {
        try {
            notificationService.completeListByTag(tag);
        } catch (Exception e) {
            logger.info("complete failure", e);
        }
    }
    @Override
    public String getNotificationMessageTemplateContent(String notificationId, Object object) {
        NotificationCategory notificationCategory = notificationCategoryService.get(notificationId, true);
        if (notificationCategory == null) {
            return null;
        }
        String messageContent = null;
        String logTag = "notification.messageTemplate." + notificationCategory.getId();
        List<I18nDTO> i18nDTOList = i18nServiceI.listBusinessBaseByKeyId(notificationCategory.getId() + "CategoryTpl");
        if (!CollectionUtils.isEmpty(i18nDTOList)) {
            for (I18nDTO i18nDTO : i18nDTOList) {
                messageContent = getTemplateParams(object, logTag, i18nDTO.getContext());
            }
        } else {
            messageContent = getTemplateParams(object, logTag, notificationCategory.getContent());
        }
        return messageContent;
    }
    /**
     * 获取待办数量
     *
     * @param userId
     * @return
     */
    @Override
    public Integer getUserNotificationCount(String userId) {
        return this.notificationService.getUserNotificationCount(userId);
    }
    /**
     * 解析流程的模板消息。
     *
     * @param params
     * @param logTag
     * @param message
     * @return
     */
    private String getTemplateParams(Object params, String logTag, String message) {
        if (params instanceof Map) {
            return VelocityUtils.evaluate((Map<String, Object>) params, logTag, message);
        }
        if (params != null) {
            Map<String, Object> map = BeanUtils.beanToMap(params);
            return VelocityUtils.evaluate(map, logTag, message);
        } else {
            Map<String, Object> map = new HashMap<>();
            return VelocityUtils.evaluate(map, logTag, message);
        }
    }

    @Override
    public Boolean sendSocketMsg(String userId, SocketMsg socketMsg) {
        return notificationService.sendSocketMsg(userId, socketMsg);
    }

    @Override
    public Boolean sendSocketMsg(SocketMsg socketMsg, String... userIds) {
        return notificationService.sendSocketMsg(socketMsg, userIds);
    }

    @Override
    public Boolean broadcastMessage(SocketMsg socketMsg) {
        return notificationService.broadcastMessage(socketMsg);
    }

    @Override
    public Boolean listByBusinessIdAndTag(String businessId, String tag) {
        boolean result = false;
        List<NotificationResponseVO> notificationResponseVOS = notificationService.listByBusinessIdAndTag(businessId, tag);
        if(EmptyUtils.isEmpty(notificationResponseVOS)){
            result = true;
        }

        return result;
    }
	@Override
	 public String getUserNotification(String userId) {
        User user = userService.get(userId, true);
        List<String> corpIds = corpUserResourceDao.listCorpIdByUserId(userId);
        List<String> roleIds = userService.listRidsByUid(userId);
        Map<String, Object> map = new HashMap<>();
        List<NotificationCategoryVO> categoryList = new ArrayList<>();
        List<Map<String, Object>> userNotification = notificationDao.getUserNotification(userId, roleIds, corpIds);
        List<NotificationCategory> notificationCategoryList1 = notificationCategoryService.getEntityDao().listAll();
        for (NotificationCategory notificationCategory : notificationCategoryList1) {
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(notificationCategory.getId());
            if (StringUtils.isNotBlank(businessBaseContext)) {
                notificationCategory.setName(businessBaseContext);
            }
        }
//        List<NotificationCategory> notificationCategoryList = TreeUtil.getTreeNodeStructure(notificationCategoryList1);
        List<NotificationCategory> notificationCategoryList = TreeUtil.createTreeStructure(notificationCategoryList1);
        if(!CollectionUtils.isEmpty(notificationCategoryList)){
            for (NotificationCategory notificationCategory : notificationCategoryList) {
                List<NotificationCategory> temp = new ArrayList<>();
//            当结构树层级在3层以上时，则3层以后的数据无法获取到，改为递归的方式获取结构树中所有叶子节点（不包含1级节点）  update by liusy
//            List<NotificationCategory> childrenA = notificationCategory.getChildren();
                List<NotificationCategory> leafNodes = new ArrayList<>();
//            获取模块下所有叶子节点
                getLeafNode(notificationCategory, leafNodes);
                for (NotificationCategory notificationCategoryC : leafNodes) {
                    if (hasNotificationCategoryNumber(notificationCategoryC, userNotification)) {
                        temp.add(notificationCategoryC);
                    }
                }
                Collections.sort(temp, new Comparator<NotificationCategory>() {
                    @Override
                    public int compare(NotificationCategory o1, NotificationCategory o2) {
                        if (o1.getSort() != null && o2.getSort() != null) {
                            return o1.getSort() - o2.getSort();
                        }
                        return 0;
                    }
                });
                if (temp.size() > 0) {
                    NotificationCategoryVO flowCategoryVO = BeanUtils.copyNewInstanceProperties(notificationCategory, NotificationCategoryVO.class);
                    flowCategoryVO.setChildren(BeanUtils.copyListProperties(temp, NotificationCategoryVO.class));
                    categoryList.add(flowCategoryVO);
                }
            }
        }
        map.put("categoryList", categoryList);
        map.put("counts", userNotification);
        return JsonUtils.toJSONString(map);
    }
	
	
	public void getLeafNode(NotificationCategory notificationCategory, List<NotificationCategory> leafNodes) {
        List<NotificationCategory> children = notificationCategory.getChildren();
//        如果存在子节点，则循环递归子节点，直至找到叶子节点为止
        if (!EmptyUtils.isEmpty(children)) {
            for (NotificationCategory notificationCategoryA : children) {
                getLeafNode(notificationCategoryA, leafNodes);
            }
        } else {//不存在子节点，则当前节点为叶子节点
            leafNodes.add(notificationCategory);
        }
    }
	
	private boolean hasNotificationCategoryNumber(NotificationCategory notificationCategory, List<Map<String, Object>> userNotification) {
        for (Map<String, Object> map : userNotification) {
            String id = (String) map.get("id");
            if (id.equals(notificationCategory.getId())) {
                return true;
            }
        }
        return false;
    }


}
