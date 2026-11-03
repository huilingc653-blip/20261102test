package com.bytter.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.IParameterInput;
import com.bytter.core.common.rest.RestResponse;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.NotificationConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.config.WebSocketServer;
import com.bytter.system.constants.NotifyObjectTypeConstants;
import com.bytter.system.dao.*;
import com.bytter.system.entity.*;
import com.bytter.system.service.impl.CorpWeiXinMessageServiceImpl;
import com.bytter.system.vo.NotificationCategoryVO;
import com.bytter.system.vo.NotificationResponseVO;
import com.bytter.velocity.VelocityUtils;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by zhoul on 2017/4/12.
 */
@Service("notificationService")
public class NotificationService extends BaseService<Notification, String> implements ITaskService {
    private static final Integer NOTIFICATION = 0;
    @Autowired
    private NotificationDao notificationDao;
    @Autowired
    private NotificationHistoricDao notificationHistoricDao;
    @Autowired
    private NotificationCategoryService notificationCategoryService;
    @Autowired
    private ResourcesDao resourcesDao;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private CorpUserResourceDao corpUserResourceDao;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationMessageDao notificationMessageDao;
    @Autowired
    private IErpNotificationService erpNotificationService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private IPushMessageService pushMessageService;
    @Autowired
    private NotificationRoleDao notificationRoleDao;
    @Autowired
    private NotificationUserDao notificationUserDao;
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private CorpWeiXinMessageServiceImpl weiXinMessageService;
    @Autowired
    private NotificationExtService notificationExtService;
    @Autowired
    private RoleService roleService;

    @Override
    public BaseDao<Notification, String> getEntityDao() {
        return notificationDao;
    }
    public Integer getUserAllNotificationCount(String userId) {
        User user = userService.get(userId);
        List<String> roleIds = getRoleIds(user);
        List<String> corpIds = corpUserResourceDao.listCorpIdByUserId(userId);
        return notificationDao.getUserAllNotificationCount(userId, roleIds,corpIds);
    }
    private List<String> getRoleIds(User user) {
        Set<UserRole> userRoles = user.getUserRoles();
        List<String> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRole().getId());
        }
        return roleIds;
    }
    public Integer getUserNotificationCount(String userId) {
        List<String> roleIds = userService.listRidsByUid(userId);
        List<String> corpIds = corpUserResourceDao.listCorpIdByUserId(userId);
        return notificationDao.getUserNotificationCount(userId, roleIds,corpIds);
    }
    public Integer getUserUnreadNotificationCount(String locale, String userId) {
        List<String> roleIds = getUserRoleIds(userId);
        List<String> corpIds = corpUserResourceDao.listCorpIdByUserId(userId);
        return notificationDao.getUserUnreadNotificationCount(locale, userId, roleIds,corpIds);
    }
    /**
     * 获取用户的业务待办信息
     *
     * @return
     */
    public Map<String, Object> getUserNotification(String userId) {
        User user = userService.get(userId);
        Set<UserRole> userRoles = user.getUserRoles();
        List<String> corpIds = corpUserResourceDao.listCorpIdByUserId(userId);
        List<String> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRole().getId());
        }
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
            /*if (!EmptyUtils.isEmpty(childrenA)) {
                for (NotificationCategory notificationCategoryA : childrenA) {
                    if (hasNotificationCategoryNumber(notificationCategoryA, userNotification)) {
                        temp.add(notificationCategoryA);
                    }
                    List<NotificationCategory> childrenB = notificationCategoryA.getChildren();
                    if (!EmptyUtils.isEmpty(childrenB)) {
                        for (NotificationCategory notificationCategoryB : childrenB) {
                            if (hasNotificationCategoryNumber(notificationCategoryB, userNotification)) {
                                temp.add(notificationCategoryB);
                            }
                        }
                    }
                }
            }*/
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
        map.put("categoryList", categoryList);
        map.put("counts", userNotification);
        return map;
    }
    /**
     * 递归获取模块下所有叶子节点
     *
     * @param notificationCategory
     * @param leafNodes
     * @author liusy
     */
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

    private List<String> getSendUserId(NotificationCategory notificationCategory, String corpId) {
        // 根据标签找不到，直接返回
        if (notificationCategory == null) {
            return null;
        }
        // 该信息分类配置的用户
        List<String> userIdList = new ArrayList<>();
        // 如果类型是通知，取消息提醒对象的用户列表
        String notifyObjectType = notificationCategory.getNotifyObjectType();
        if (NotifyObjectTypeConstants.BY_USER.equals(notifyObjectType)) {
            List<NotificationUser> notificationUserList = notificationUserDao.listBy("categoryId", notificationCategory.getId());
            if (ObjectUtil.isNotEmpty(notificationUserList)) {
                userIdList = notificationUserList.stream().map(NotificationUser::getUserId).collect(Collectors.toList());
            }
        } else if (NotifyObjectTypeConstants.BY_ROLE.equals(notifyObjectType)) {
            List<NotificationRole> categoryId = notificationRoleDao.listBy("categoryId", notificationCategory.getId());
            if (ObjectUtil.isNotEmpty(categoryId)) {
                List<String> roleList = categoryId.stream().map(NotificationRole::getRoleId).collect(Collectors.toList());
                userIdList = userService.listUserIdsByRoleId(roleList);
            }
        } else {
            // 兼容旧版本，旧版本notifyObjectType为null，为null指的是全部角色
            List<Role> roles = roleService.listAll();
            List<String> rolesIds = roles.stream().map(Role::getId).collect(Collectors.toList());
            userIdList = userService.listUserIdsByRoleId(rolesIds);
        }

        // 如果配置了菜单，需要剔除没有菜单权限的用户
        String menuId = notificationCategory.getMenuId();
        List<String> userIdsByMenu = null;
        if (StringUtils.isNotBlank(menuId)) {
            userIdsByMenu = userService.listUserIdByMenuId(menuId);
            // 取交集
            userIdList = (List<String>) CollUtil.intersection(userIdsByMenu, userIdList);
        }

        // 如果配置了资源，需要剔除没有资源权限的用户
        String resourceContent = notificationCategory.getResourceContent();
        List<String> userIdsByResource = null;
        if (StringUtils.isNotBlank(resourceContent)) {
            userIdsByResource = getUserIdByResource(resourceContent, corpId);
            userIdList = (List<String>) CollUtil.intersection(userIdsByResource, userIdList);
        }

        // if(!NotifyObjectTypeConstants.BY_USER.equals(notifyObjectType) && !NotifyObjectTypeConstants.BY_ROLE.equals(notifyObjectType)){
        //     userIdList = userIdsByResource;
        // }
        return userIdList;
    }
    public void send(String businessId, String corpId, String categoryTag, Object object) {
        NotificationCategory notificationCategory = notificationCategoryService.findByTag(categoryTag);
        List<String> userIdList = getSendUserId(notificationCategory, corpId);
        if (!EmptyUtils.isEmpty(userIdList)) {
            EmptyUtils.removeDuplicate(userIdList);
            sendNotificationToUser(businessId, userIdList, notificationCategory.getId(), corpId, object);
        }
    }
    public void sendIgnoreMySelf(String businessId, String corpId, String tag, String ignoreUserId) {
        NotificationCategory notificationCategory = notificationCategoryService.findByTag(tag);
        List<String> userIdList = getSendUserId(notificationCategory, corpId);
        if (!EmptyUtils.isEmpty(userIdList)) {
            EmptyUtils.removeDuplicate(userIdList);
            userIdList.remove(ignoreUserId);
            sendNotificationToUser(businessId, userIdList, notificationCategory.getId(), corpId, null);
        }
    }
    public void sendIgnoreMySelf(String businessId, String corpId, String tag, Object object, String ignoreUserId) {
        NotificationCategory notificationCategory = notificationCategoryService.findByTag(tag);
        List<String> userIdList = getSendUserId(notificationCategory, corpId);
        if (!EmptyUtils.isEmpty(userIdList)) {
            EmptyUtils.removeDuplicate(userIdList);
            userIdList.remove(ignoreUserId);
            sendNotificationToUser(businessId, userIdList, notificationCategory.getId(), corpId, object);
        }
    }
    private List<String> getUserIdByResource(String resourceContent, String corpId) {
        String[] split = resourceContent.split(CommonConstant.COMMA_SPLIT);
        if (split.length == 0) {
            return new ArrayList<>();
        }
        //根据权限获取资源
        List<Resources> resources = resourcesDao.listByPermission(Arrays.asList(split));
        if (!EmptyUtils.isEmpty(resources)) {
            List<String> resourceIdList = new ArrayList<>();
            //构建资源ID
            for (Resources res : resources) {
                resourceIdList.add(res.getId());
            }
            Set<String> userIdList = new HashSet<>();
            //根据资源ID获取特殊权限
            List<CorpUserResource> corpUserResourceList = corpUserResourceDao.listByResourceListAndCorpId(resourceIdList, corpId);
            //获取特殊权限中的用户ID
            for (CorpUserResource corpUserResource : corpUserResourceList) {
                userIdList.add(corpUserResource.getUserId());
            }
            return new ArrayList<>(userIdList);
        }
        return new ArrayList<>();
    }
    /**
     * 根据资源权限获取用户列表
     * Created by chenlw on 2019/04/25.
     *
     * @param resPermission 资源权限
     * @param sendCorpId    发送单位ID
     * @return
     */
    private List<String> getUserIdByResourcePermission(String resPermission, String sendCorpId) {
        if (StringUtils.isEmpty(resPermission)) {
            return null;
        }
        Set<String> userIdsSet = new HashSet<>();
        //根据权限获取资源
        List<Resources> resources = resourcesDao.listByPermission(resPermission);
        if (!EmptyUtils.isEmpty(resources)) {
            List<String> resourceIdList = new ArrayList<>();
            //构建资源ID列表
            for (Resources res : resources) {
                resourceIdList.add(res.getId());
            }
            //根据资源ID获取特殊权限（根据用户-资源关联表里面的关系获取用户ID）
            List<CorpUserResource> corpUserResourceList = corpUserResourceDao.listByResourceListAndCorpId(resourceIdList, sendCorpId);
            //获取特殊权限中的用户ID
            for (CorpUserResource corpUserResource : corpUserResourceList) {
                //用户ID去重
                userIdsSet.add(corpUserResource.getUserId());
            }
        }
//        //获取拥有该资源权限的角色ID列表
//        List<String> roleIdList = resourcesDao.findRoleIdByResPermission(resPermission);
//        List<String> userIdList = new ArrayList<>();
//        List<String> userIdTempList;
//        if (!EmptyUtils.isEmpty(roleIdList)) {
//            for (String roleId : roleIdList) {
//                //根据角色ID获取该角色的用户列表
//                userIdTempList = roleService.getUserIdListByRoleId(roleId);
//                if (!EmptyUtils.isEmpty(userIdTempList)) {
//                    userIdList.addAll(userIdTempList);
//                }
//            }
//            for (String userId : userIdList) {
//                //用户ID去重
//                userIdsSet.add(userId);
//            }
//        }
        return new ArrayList<>(userIdsSet);
    }
    /**
     * 根据资源权限发通知给有资源权限的用户
     * Created by chenlw on 2019/04/25.
     *
     * @param businessId    业务ID
     * @param resPermission 资源权限
     * @param tag           标签 @see com.bytter.facade.sys.constant.NotificationConstant
     * @param object        模版解析对象
     * @param sendCorpId    发送单位ID
     */
    public void sendToUserByResPermission(String businessId, String resPermission, String tag, Object object, String sendCorpId) {
        List<String> userIds = getUserIdByResourcePermission(resPermission, sendCorpId);
        if (!EmptyUtils.isEmpty(userIds)) {
            sendToUser(businessId, userIds, tag, object);
        }
    }
    public void sendToUserByLoginName(String businessId, String loginName, String tag, Object object) {
        User user = userService.getByLoginName(loginName);
        if (EmptyUtils.isEmpty(user)) {
            return;
        }
        sendToUser(businessId, user.getId(), tag, object);
    }
    public void sendToUserByLoginName(String businessId, List<String> loginNameList, String tag, Object object) {
        List<User> userList = userService.listByLoginName(loginNameList);
        if (EmptyUtils.isEmpty(userList)) {
            return;
        }
        List<String> userIds = new ArrayList<>();
        for (User user : userList) {
            userIds.add(user.getId());
        }
        if (EmptyUtils.isEmpty(userIds)) {
            return;
        }
        EmptyUtils.removeDuplicate(userIds);
        sendToUser(businessId, userIds, tag, object);
    }
    public void sendToUserByLoginNameIgnoreMySelf(String businessId, List<String> loginNameList, String tag, Object object, String ignoreUserId) {
        List<User> userList = userService.listByLoginName(loginNameList);
        if (EmptyUtils.isEmpty(userList)) {
            return;
        }
        List<String> userIds = new ArrayList<>();
        for (User user : userList) {
            userIds.add(user.getId());
        }
        if (EmptyUtils.isEmpty(userIds)) {
            return;
        }
        EmptyUtils.removeDuplicate(userList);
        userList.remove(ignoreUserId);
        sendToUser(businessId, userIds, tag, object);
    }
    public void sendToUser(String businessId, List<String> userIds, String categoryTag, Object object) {
        sendToUser(businessId, userIds, categoryTag, "", object);
    }
    public void sendToUser(String businessId, String userId, String categoryTag, Object object) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        sendToUser(businessId, userIds, categoryTag, "", object);
    }
    /**
     * 发送消息给用户
     *
     * @param businessId  业务ID
     * @param userId      用户ID
     * @param categoryTag 分类标识
     * @param type        类型：1、通知。2、待处理
     * @param object      模版对象
     */
    public void sendToUser(String businessId, String userId, String categoryTag, Integer type, Object object) {
        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        sendToUser(businessId, userIds, categoryTag, "", null, object);
    }
    public void sendToUser(String businessId, List<String> userIds, String categoryTag, String corpId, Object object) {
        sendToUser(businessId, userIds, categoryTag, corpId, null, object);
    }
    public void sendToUser(String businessId, List<String> userIds, String categoryTag, String corpId, Integer type, Object object) {
        Object obj = object;
        NotificationCategory notificationCategory = notificationCategoryService.findByTag(categoryTag);
        if (notificationCategory == null) {
            return;
        }
        List<String> sendUserList = new ArrayList<>();
        Integer roleFilter = notificationCategory.getRoleFilter();//角色过滤
        if (new Integer(1).equals(roleFilter)) {//指定角色
            //过滤消息提醒角色
            String categoryId = notificationCategory.getId();
            List<String> roleIds = notificationRoleDao.getRoleByCategoryId(categoryId);//取消息发送的角色
            if (EmptyUtils.isEmptyList(roleIds)) {
                return;
            }
            List<String> userList = userService.listUserIdsByRoleId(roleIds);//取角色都有的用户
            // todo:过滤未分配相关单位权限的用户
            sendUserList = compareUserList(userIds, userList);//取两者都有的数据
        } else {//所有角色
            sendUserList = userIds;
        }
        if (type != null) {
            createNotification(businessId, sendUserList, notificationCategory.getId(), corpId, type, obj);
        } else {
            createNotification(businessId, sendUserList, notificationCategory.getId(), corpId, obj);
        }
        sendSocketMsg(SocketMsg.build(SocketMsg.Type.HOME_NOTICE), userIds.toArray(new String[0]));
    }
    /**
     * 取用户集合的并集
     *
     * @param userIds
     * @param roleUserIds
     * @return
     */
    public List<String> compareUserList(List<String> userIds, List<String> roleUserIds) {
        List<String> result = new ArrayList<>();
        if (EmptyUtils.isEmptyList(roleUserIds)) {
            return result;
        }
        for (String userId : userIds) {
            if (roleUserIds.contains(userId)) {
                result.add(userId);
            }
        }
        return result;
    }
    public void sendNotificationToUser(String businessId, List<String> userIds, String categoryId, String corpId, Object object) {
        createNotification(businessId, userIds, categoryId, corpId, object);
        //用户消息收到后更新首页数据
        sendSocketMsg(SocketMsg.build(SocketMsg.Type.HOME_NOTICE), userIds.toArray(new String[0]));
    }
    public void createNotification(String businessId, List<String> userIds, String categoryId, String corpId, Object object) {
        NotificationCategory notificationCategory = notificationCategoryService.get(categoryId);
        createNotification(businessId, userIds, categoryId, corpId, notificationCategory.getType(), object);
    }
    @Transactional
    public void createNotification(String businessId, List<String> userIds, String categoryId, String corpId, Integer type, Object object) {
        if (EmptyUtils.isEmpty(userIds)) {
            return;
        }
        String notificationContent = null;
        Set<String> userSet = new HashSet<>(userIds);
        Date createTime = new Date();
        List<Notification> notificationList = new ArrayList<>();
        for (String userId : userSet) {
            String uuId = StringUtils.getUUId();
            Notification notification = new Notification();
            notification.setBusinessId(businessId);
            notification.setUserId(userId);
            notification.setCreateDate(createTime);
            notification.setCategoryId(categoryId);
            notification.setCorpId(corpId);
            notification.setType(type);
            notification.setMessageId(uuId);
            // makeNotificationMessageTemplate(categoryId, object, notification);
            notificationContent = makeNotificationMessageTemplate(categoryId, object, notification);
            if (StringUtils.isNotBlank(notification.getMessageId())) {
                notificationList.add(notification);
            }
        }
        //批量保存消息通知
        batchSave(notificationList);
        if (type != null) {
            if (NotificationConstant.TYPE_UNIVERSAL_NOTIFICATION == type) {
                // 发送文本通知
                sendUniversalNotificationToErp(new ArrayList<>(userSet), categoryId, notificationContent);
            } else if (NotificationConstant.TYPE_PENDING_NOTIFICATION == type) {
                // 待处理通知
                sendUniversalNotificationToErp(new ArrayList<>(userSet), categoryId, notificationContent);
            }
        }
        // 推送消息到APP
        pushMessageToAppByAliYun(notificationList, categoryId, notificationContent, type);

        // 推送消息到企业微信
        pushMessageToCorpWeixin(notificationList, categoryId, notificationContent);

    }

    /**
     * 推送消息到企业微信
     * @param notificationList
     * @param categoryId
     * @param notificationContent
     */
    private void pushMessageToCorpWeixin(List<Notification> notificationList, String categoryId, String notificationContent) {

        logger.info("企微消息推送开关:{}; corpID:{}; Secret:{}; agentID:{};",weiXinMessageService.isAllowedPushMessage()
                ,weiXinMessageService.getWeiXinCorpId(),weiXinMessageService.getWeiXinCorpSecret(),weiXinMessageService.getAgentid());
        logger.info("==> notificationList.size:{},categoryId:{}", notificationList.size(),categoryId);
        if (!weiXinMessageService.isAllowedPushMessage()) {
            return;
        }
        NotificationCategory notificationCategory = notificationCategoryService.get(categoryId);
        if (notificationCategory == null || notificationCategory.getWeixinInd()==null || notificationCategory.getWeixinInd()==0) {
            return;
        }
        List<NotificationExt> notificationExtList = new ArrayList<>(notificationList.size());
        for (Notification notification : notificationList) {
            NotificationExtDTO notificationExtDTO = weiXinMessageService.pushMarkdownMessage(notification.getUserId(),null, notificationContent);
            notificationExtList.add(buildNotificationExt(notification,notificationExtDTO,notificationContent));
        }
        notificationExtService.batchSave(notificationExtList);
    }

    private NotificationExt buildNotificationExt(Notification notification, NotificationExtDTO notificationExtDTO,String content) {
        NotificationExt notificationExt = new NotificationExt();
        notificationExt.setCategoryId(notification.getCategoryId());
        notificationExt.setUserId(notification.getUserId());
        notificationExt.setCorpId(notification.getCorpId());
        notificationExt.setBusinessId(notification.getBusinessId());
        notificationExt.setCreateDate(new Date());
        notificationExt.setContent(content);
        notificationExt.setType("WX");
        notificationExt.setSendBody(notificationExtDTO.getSendBody());
        notificationExt.setResponseBody(notificationExtDTO.getResponseBody());
        notificationExt.setResult(notificationExtDTO.getResult());
        return notificationExt;
    }


    /**
     * 推送消息到APP
     *
     * @param notificationList
     * @param notificationCategoryId
     * @param notificationContent
     * @param type
     */
    private void pushMessageToAppByAliYun(List<Notification> notificationList,
                                          String notificationCategoryId,
                                          String notificationContent,
                                          Integer type) {
        if (!pushMessageService.isPushSwitch()) {
            return;
        }
        NotificationCategory notificationCategory = notificationCategoryService.get(notificationCategoryId);
        if (notificationCategory == null) {
            return;
        }
        AppNotificationPushDTO appNotificationPushDTO;
        for (Notification notification : notificationList) {
            appNotificationPushDTO = new AppNotificationPushDTO();
            appNotificationPushDTO.setNotificationId(notification.getId());
            appNotificationPushDTO.setUserId(notification.getUserId());
            appNotificationPushDTO.setCategoryName(notificationCategory.getName());
            appNotificationPushDTO.setName(notificationContent);
            appNotificationPushDTO.setCreateDate(notification.getCreateDate());
            pushMessageService.pushNotification(appNotificationPushDTO);
        }
    }
    private String makeNotificationMessageTemplate(String categoryId, Object object, Notification notification) {
        String notificationContent = null;
        NotificationCategory notificationCategory = notificationCategoryService.get(categoryId);
        if (notificationCategory == null) {
            return notificationContent;
        }
        String uuId = StringUtils.getUUId();
        String logTag = "notification.messageTemplate." + categoryId;
        List<I18nDTO> i18nDTOList = i18nServiceI.listBusinessBaseByKeyId(notificationCategory.getId() + "CategoryTpl");
        if (!CollectionUtils.isEmpty(i18nDTOList)) {
            for (I18nDTO i18nDTO : i18nDTOList) {
                NotificationMessage notificationMessage = new NotificationMessage();
                notificationMessage.setId(uuId);
                notificationMessage.setLocale(i18nDTO.getLocale());
//                notificationMessage.setMessage(VelocityUtils.evaluate(object, logTag, i18nDTO.getContext()));
                notificationContent = getTemplateParams(object, logTag, i18nDTO.getContext());
                notificationMessage.setMessage(notificationContent);
                notificationMessageDao.save(notificationMessage);
            }
        } else {
            NotificationMessage notificationMessage = new NotificationMessage();
            notificationMessage.setId(uuId);
            notificationMessage.setLocale("zh_CN");
            notificationContent = getTemplateParams(object, logTag, notificationCategory.getContent());
            notificationMessage.setMessage(notificationContent);
            notificationMessageDao.save(notificationMessage);
        }
        notification.setMessageId(uuId);
        return notificationContent;
    }
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
    /**
     * 完成消息，标记已读
     *
     * @param tag
     * @param businessId
     * @param currentUserId
     */
    public void complete(String tag, String businessId, String currentUserId) {
        Notification notification = notificationDao.getByUserIdAndTagAndBusinessId(currentUserId, tag, businessId);
        if (notification == null) {
            return;
        }
        createNotificationHistoric(notification, currentUserId);
        //根据业务ID清空数据
//        String businessId = notification.getBusinessId();
        notificationDao.deleteByBusinessId(businessId);
        //处理完消息后 刷新首页
        this.broadcastMessage(SocketMsg.build(SocketMsg.Type.HOME_NOTICE));
    }
    /**
     * 根据tag消息类型清除所有的这类型的消息提醒
     * Created by chenlw on 2019/04/25.
     *
     * @param tag
     */
    @Transactional
    public void completeListByTag(String tag) {
        NotificationCategory notificationCategory = notificationCategoryService.findByTag(tag);
        if (notificationCategory == null) {
            return;
        }
        //查询未读的消息列表
        List<Notification> notReadNotificationList =
                notificationDao.listByNotificationCategoryId(notificationCategory.getId());
        if (!EmptyUtils.isEmpty(notReadNotificationList)) {
            List<NotificationHistoric> notificationHistoricList = new ArrayList<>();
            List<String> msgIdList = new ArrayList<>();
            NotificationHistoric notificationHistoric;
            for (Notification notification : notReadNotificationList) {
                notificationHistoric = BeanUtils.copyNewInstanceProperties(notification, NotificationHistoric.class);
                notificationHistoric.setId(null);
                Date endTime = new Date();
                notificationHistoric.setEndDate(endTime);
                if (notificationHistoric.getCreateDate() != null) {
                    notificationHistoric.setDuration(endTime.getTime() - notificationHistoric.getCreateDate().getTime());
                }
                notificationHistoricList.add(notificationHistoric);
                msgIdList.add(notification.getMessageId());
            }
            //将未读消息存入已读消息表
            notificationHistoricDao.batchSave(notificationHistoricList);
            //删除未读消息表的数据
            notificationDao.deleteByNotificationCategoryId(notificationCategory.getId());
            notificationMessageDao.deleteMsgByIdList(msgIdList);//删除具体消息内容
        }
    }
    /***************************************** 发送消息到ERP接口 start ************************************************/
    /**
     * 通用通知接口（无需处理消息）
     *
     * @param userIdList          接收者ID数组
     * @param notificationContent 消息内容
     */
    public void sendUniversalNotificationToErp(final List<String> userIdList, final String notificationCategoryId, final String notificationContent) {
        if (erpNotificationService.isAllowedPushNotification()) {
            erpNotificationService.sendUniversalNotificationToUserByUserIdList(userIdList, notificationCategoryId, notificationContent);
        }
    }
    /***************************************** 发送消息到ERP接口 end   ************************************************/
    /**
     * 将消息列表置为已读状态
     *
     * @param notificationIdList 消息ID列表
     * @param currentUserId      当前用户ID
     */
    public void createNotificationHistoric(List<String> notificationIdList, String currentUserId) {
        if (CollectionUtils.isEmpty(notificationIdList)) {
            return;
        }
        List<NotificationHistoric> saveNotificationHistoricList = new ArrayList<>(notificationIdList.size());
        for (String notificationId : notificationIdList) {
            Notification notification = get(notificationId, true);
            if (notification != null) {
                NotificationHistoric notificationHistoric = notification2NotificationHistoric(notification, currentUserId);
                saveNotificationHistoricList.add(notificationHistoric);
            }
        }
        notificationHistoricDao.batchSaveOrUpdate(saveNotificationHistoricList);
        if (notificationIdList.size() < 1000) {
            notificationDao.delete(notificationIdList);
        } else {
            notificationDao.deleteBatchByIds(notificationIdList, 500);
        }
    }
    /**
     * 将消息列表置为已读状态
     *
     * @param notificationList 未读消息列表
     * @param currentUserId    当前用户ID
     */
    public void createNotificationHistoricByNotificationList(List<Notification> notificationList, String currentUserId) {
        if (CollectionUtils.isEmpty(notificationList)) {
            return;
        }
        List<String> notificationIdList = new ArrayList<>();
        List<NotificationHistoric> saveNotificationHistoricList = new ArrayList<>(notificationList.size());
        for (Notification notification : notificationList) {
            if (notification != null) {
                NotificationHistoric notificationHistoric = notification2NotificationHistoric(notification, currentUserId);
                saveNotificationHistoricList.add(notificationHistoric);
                notificationIdList.add(notification.getId());
            }
        }
        notificationHistoricDao.batchSaveOrUpdate(saveNotificationHistoricList);
        if (notificationIdList.size() < 1000) {
            notificationDao.delete(notificationIdList);
        } else {
            notificationDao.deleteBatchByIds(notificationIdList, 500);
        }
    }
    private NotificationHistoric notification2NotificationHistoric(Notification notification, String currentUserId) {
        NotificationHistoric notificationHistoric = BeanUtils.copyNewInstanceProperties(notification, NotificationHistoric.class);
        notificationHistoric.setId(null);
        //把处理人用户放到历史消息记录中
        notificationHistoric.setUserId(currentUserId);
        Date endTime = new Date();
        notificationHistoric.setEndDate(endTime);
        if (notificationHistoric.getCreateDate() != null) {
            notificationHistoric.setDuration(endTime.getTime() - notificationHistoric.getCreateDate().getTime());
        }
        return notificationHistoric;
    }
    /**
     * 创建消息历史
     *
     * @param notification
     * @param currentUserId
     */
    public void createNotificationHistoric(Notification notification, String currentUserId) {
        //将用户处理的数据存入历史记录表
        NotificationHistoric notificationHistoric = notification2NotificationHistoric(notification, currentUserId);
        notificationHistoricDao.save(notificationHistoric);
        notificationDao.delete(notification.getId());
    }
    private List<String> getUserRoleIds(String userId) {
        User user = userService.get(userId, true);
        Set<UserRole> userRoles = user.getUserRoles();
        List<String> roleIds = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roleIds.add(userRole.getRole().getId());
        }
        return roleIds;
    }
    public RestResponse<NotificationResponseVO> restResult(IParameterInput parameterInput, String locale) {
        String userId = parameterInput.getCurrentUserId();
        List<String> corpIds = corpUserResourceDao.listCorpIdByUserId(userId);
        IPageResult<NotificationResponseVO> userNotificationNote = notificationDao.getUserNotificationNote(locale, userId, parameterInput, corpIds);
        List<NotificationResponseVO> list = userNotificationNote.getList();
        for (NotificationResponseVO notificationCategory : list) {
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(notificationCategory.getCategoryId());
            if (StringUtils.isNotBlank(businessBaseContext)) {
                notificationCategory.setCategoryName(businessBaseContext);
            }
        }
        return userNotificationNote.convert(RestResponse.class);
    }
    /**
     * 获取未读和已读消息
     *
     * @param locale         国际化
     * @param parameterInput 参数
     * @return 未读和已读消息
     */
    public RestResponse<NotificationResponseVO> apiRestResult(String locale, IParameterInput parameterInput) {
        String userId = parameterInput.getCurrentUserId();
        List<String> roleIds = getUserRoleIds(userId);
        IPageResult<NotificationResponseVO> userNotificationNote = notificationDao.getAPIUserNotificationNote(locale,
                parameterInput.getCurrentUserId(), parameterInput, roleIds);
        List<NotificationResponseVO> notificationResponseVOList = userNotificationNote.getList();
        for (NotificationResponseVO notificationResponseVO : notificationResponseVOList) {
            notificationResponseVO.setTopParentCategoryName(notificationCategoryService.getTopParentCategoryName(notificationResponseVO.getCategoryId()));
        }
        return userNotificationNote.convert(RestResponse.class);
    }
    /**
     * 获取未读消息列表
     *
     * @param locale         国际化
     * @param parameterInput 参数
     * @return 未读消息列表
     */
    public RestResponse<NotificationResponseVO> getUnreadUserNotificationNote(String locale, IParameterInput parameterInput) {
        String userId = parameterInput.getCurrentUserId();
        List<String> roleIds = getUserRoleIds(userId);
        IPageResult<NotificationResponseVO> userNotificationNote = notificationDao.getUnreadUserNotificationNote(locale,
                userId, parameterInput, roleIds);
        List<NotificationResponseVO> notificationResponseVOList = userNotificationNote.getList();
        for (NotificationResponseVO notificationResponseVO : notificationResponseVOList) {
            notificationResponseVO.setTopParentCategoryName(notificationCategoryService.getTopParentCategoryName(notificationResponseVO.getCategoryId()));
        }
        return userNotificationNote.convert(RestResponse.class);
    }
    /**
     * 获取已读消息列表
     *
     * @param locale         国际化
     * @param parameterInput 参数
     * @return 未读消息列表
     */
    public RestResponse<NotificationResponseVO> getHaveReadUserNotificationNote(String locale, IParameterInput parameterInput) {
        String userId = parameterInput.getCurrentUserId();
        List<String> roleIds = getUserRoleIds(userId);
        IPageResult<NotificationResponseVO> userNotificationNote = notificationDao.getHaveReadUserNotificationNote(locale,
                userId, parameterInput, roleIds);
        List<NotificationResponseVO> notificationResponseVOList = userNotificationNote.getList();
        for (NotificationResponseVO notificationResponseVO : notificationResponseVOList) {
            notificationResponseVO.setTopParentCategoryName(notificationCategoryService.getTopParentCategoryName(notificationResponseVO.getCategoryId()));
        }
        return userNotificationNote.convert(RestResponse.class);
    }
    public void delete(String notificationId, Integer status) {
        if (status == 0) {
            notificationDao.delete(notificationId);
        } else {
            notificationHistoricDao.delete(notificationId);
        }
    }
    /**
     * 批量删除消息
     *
     * @param list
     */
    public void deleteBatch(List<NotificationResponseVO> list) {
        List<String> unreadNotificationIdList = new ArrayList<>();
        List<String> haveReadNotificationIdList = new ArrayList<>();
        for (NotificationResponseVO notificationResponseVO : list) {
            if (notificationResponseVO.getStatus() != null && notificationResponseVO.getStatus() == 0) {
                unreadNotificationIdList.add(notificationResponseVO.getId());
            } else {
                haveReadNotificationIdList.add(notificationResponseVO.getId());
            }
        }
        if (!CollectionUtils.isEmpty(unreadNotificationIdList)) {
            notificationDao.delete(unreadNotificationIdList);
        }
        if (!CollectionUtils.isEmpty(haveReadNotificationIdList)) {
            notificationHistoricDao.delete(haveReadNotificationIdList);
        }
    }
    /**
     * 删除对应用户的所有已读消息
     *
     * @param userId 用户ID
     */
    public void deleteHeadReadUserNotificationByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            // 获取已读消息的消息内容ID列表
            List<String> haveReadMsgIdList = notificationHistoricDao.listMsgIdByUserId(userId);
            List<String> deleteMsgIdList = new ArrayList<>();
            for (String id : haveReadMsgIdList) {
                if (StringUtils.isNotBlank(id)) {
                    deleteMsgIdList.add(id);
                }
            }
            // 删除消息
            notificationHistoricDao.deleteByUserId(userId);
            if (!CollectionUtils.isEmpty(deleteMsgIdList)) {
                // 删除未读消息的消息内容
                if (deleteMsgIdList.size() < 1000) {
                    notificationMessageDao.delete(deleteMsgIdList);
                } else {
                    notificationMessageDao.deleteBatchByIds(deleteMsgIdList, 500);
                }
            }
        }
    }
    /**
     * 删除对应用户的所有未读消息
     *
     * @param userId 用户ID
     */
    public void deleteUnreadUserNotificationByUserId(String userId) {
        if (StringUtils.isNotBlank(userId)) {
            // 获取未读消息的消息内容ID列表
            List<String> unreadMsgIdList = notificationDao.listMsgIdByUserId(userId);
            List<String> deleteMsgIdList = new ArrayList<>();
            for (String id : unreadMsgIdList) {
                if (StringUtils.isNotBlank(id)) {
                    deleteMsgIdList.add(id);
                }
            }
            // 删除未读消息
            notificationDao.deleteByUserId(userId);
            if (!CollectionUtils.isEmpty(deleteMsgIdList)) {
                // 删除未读消息的消息内容
                if (deleteMsgIdList.size() < 1000) {
                    notificationMessageDao.delete(deleteMsgIdList);
                } else {
                    notificationMessageDao.deleteBatchByIds(deleteMsgIdList, 500);
                }
            }
        }
    }
    /**
     * 获取重点关注的信息
     *
     * @param userId
     * @return
     */
    public List<NotificationResponseVO> listStarTarget(String userId) {
        List<String> roleIds = getUserRoleIds(userId);
        return notificationDao.listStarTarget(userId, roleIds);
    }
    /**
     * 消息查询
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, ICurrentUser user, String locale) {
        StringBuilder sql = new StringBuilder();
        sql.append(" and n.user_id='" + user.getId() + "'");
        sql.append(" and nm.n_locale='" + locale + "'");
        return notificationMessageDao.getDataTablesUnion(dataTablesInput, sql.toString());
    }
    /**
     * 获取重点关注的信息
     *
     * @param userId
     * @return
     */
    public List<NotificationResponseVO> listExpire(IParameterInput parameterInput, String userId) {
        return notificationDao.listExpire(parameterInput, userId);
    }
    /**
     * 获取到期的信息
     *
     * @param userId
     * @param parameterInput
     * @return
     */
    public List<NotificationResponseVO> listExpireByCreateDate(String userId, IParameterInput parameterInput) {
        return notificationDao.listExpireByCreateDate(userId, parameterInput);
    }
    @Override
    @XxlJob("NotificationService")
    public void run(String arguments) {
        ConfigDTO configDTO = configServiceI.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.NOTIFICAT_SAVE_DAYS);
        Integer notificatSaveDays = 90;
        if (configDTO != null && StringUtils.isNotBlank(configDTO.getValue())) {
            notificatSaveDays = Integer.parseInt(configDTO.getValue());
        }
        Date endDate = DateUtils.addDay(DateUtils.getDate(), -notificatSaveDays);
        XxlJobHelper.log("删除【{}】之前的消息", DateUtil.formatDate(endDate));
        //删除截止日期之前的消息
        notificationMessageDao.deleteByEndDate(endDate);
        //删除截止日期之前的数据
        notificationDao.deleteByEndDate(endDate);
        //删除截止日期之前的数据
        notificationHistoricDao.deleteByEndDate(endDate);
    }

    public Boolean sendSocketMsg(String userId, SocketMsg socketMsg) {
        return webSocketServer.sendMessage(socketMsg, userId);
    }

    public Boolean sendSocketMsg(SocketMsg socketMsg, String... userIds) {
        return webSocketServer.sendMessage(socketMsg, userIds);
    }

    public Boolean broadcastMessage(SocketMsg socketMsg) {
        return webSocketServer.broadcastMessage(socketMsg);
    }

    /**
     * 获取相关资源，相关业务的消息记录
     *
     * @param businessId
     * @param tag
     * @return
     */
    public List<NotificationResponseVO> listByBusinessIdAndTag(String businessId, String tag) {
        return notificationDao.listByBusinessIdAndTag(businessId, tag);
    }
}
