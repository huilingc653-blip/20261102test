package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.PageResult;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.system.api.ILicenseService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.enums.NotificationEnum;
import com.bytter.system.constants.NotifyObjectTypeConstants;
import com.bytter.system.dao.NotificationCategoryDao;
import com.bytter.system.dao.NotificationRoleDao;
import com.bytter.system.dao.NotificationUserDao;
import com.bytter.system.entity.NotificationCategory;
import com.bytter.system.entity.NotificationRole;
import com.bytter.system.entity.NotificationUser;
import com.bytter.system.entity.Role;
import com.bytter.system.vo.NotificationCategoryVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/4/12.
 */
@Service("notificationCategoryService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class NotificationCategoryService extends BaseService<NotificationCategory, String> {
    @Autowired
    private NotificationCategoryDao notificationCategoryDao;
    @Autowired
    private NotificationRoleDao notificationRoleDao;
    @Autowired
    private NotificationUserDao notificationUserDao;
    @Autowired
    private ILicenseService licenseService;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;

    @Override
    public NotificationCategory get(String id) {
        return get(id, true);
    }

    @Override
    public BaseDao<NotificationCategory, String> getEntityDao() {
        return notificationCategoryDao;
    }

    public List<NotificationCategory> getTreeTable() {
        List<NotificationCategory> notificationCategories = notificationCategoryDao.listAll();
        if (EmptyUtils.isNotEmpty(notificationCategories)){
            for (NotificationCategory info : notificationCategories) {
                notificationCategoryDao.evict(info);
                String businessBaseContext = i18nServiceI.getBusinessBaseContext(info.getId());
                if (StringUtils.isNotBlank(businessBaseContext)) {
                    info.setName(businessBaseContext);
                }
            }
        }
        return listToTreeTable(notificationCategories);
    }

    public TreeGridResponse<NotificationCategory> getTreeGrid(TableGridInput input) {
        IPageResult<NotificationCategory> pageResult = notificationCategoryDao.listCategory(input);
        List<NotificationCategory> list = pageResult.getList();
        List<NotificationCategoryVO> voList = new ArrayList<>();
        if (EmptyUtils.isNotEmpty(list)){
            for (NotificationCategory info : list) {
                notificationCategoryDao.evict(info);
                String businessBaseContext = i18nServiceI.getBusinessBaseContext(info.getId());
                if (StringUtils.isNotBlank(businessBaseContext)) {
                    info.setName(businessBaseContext);
                }
                NotificationCategoryVO notificationCategoryVO = new NotificationCategoryVO();
                BeanUtils.copyProperties(info, notificationCategoryVO);
                voList.add(notificationCategoryVO);
            }
        }
        IPageResult<NotificationCategoryVO> pageResultVO = new PageResult<>(input, pageResult.getTotalRow(), TreeUtil.coverTreeTable(voList));
        return pageResultVO.convert(TreeGridResponse.class);
    }

    public boolean checkUniqueTag(String tag) {
        NotificationCategory notificationCategory = findByTag(tag);
        return notificationCategory == null;
    }

    public NotificationCategory findByTag(String tag) {
        return notificationCategoryDao.findByTag(tag);
    }

    /**
     * 更新分类
     *
     * @param notificationCategory
     */
    public void updateEntity(NotificationCategory notificationCategory, List<String> roleIds, List<String> userIds) {
        NotificationCategory dbNotificationCategory = get(notificationCategory.getId());
        if (StringUtils.isNotBlank(notificationCategory.getTag())
                && !notificationCategory.getTag().equals(dbNotificationCategory.getTag())
                && !checkUniqueTag(notificationCategory.getTag())) {
            throw new BusinessException(NotificationEnum.SYS_NOTIFICATION_CATEGORY_TAG_NOT_UNIQUE);
        }
        if (notificationCategory.getSort() == null) {
            notificationCategory.setSort(500);
        }
        BeanUtils.copyProperties(notificationCategory, dbNotificationCategory);
        //跟新sys_notification_role 或 sys_notification_user
        if (StringUtils.isNotEmpty(notificationCategory.getNotifyObjectType())) {
            if (NotifyObjectTypeConstants.ALL_ROLES.equals(notificationCategory.getNotifyObjectType())) {
                roleIds = roleService.listAll().stream().map(Role::getId).collect(Collectors.toList());
                saveNotificationRole(notificationCategory, roleIds);
            } else if (NotifyObjectTypeConstants.BY_ROLE.equals(notificationCategory.getNotifyObjectType())) {
                saveNotificationRole(notificationCategory, roleIds);
            } else if (NotifyObjectTypeConstants.BY_USER.equals(notificationCategory.getNotifyObjectType())) {
                saveNotificationUser(notificationCategory, userIds);
            }
        }
        super.updateEntity(dbNotificationCategory);
    }

    private void saveNotificationRole(NotificationCategory notificationCategory, List<String> roleIds) {
        // 清除消息提醒关联用户表
        notificationUserDao.deleteByCategoryId(notificationCategory.getId());
        notificationRoleDao.deleteByCategoryId(notificationCategory.getId());
        List<NotificationRole> list = new ArrayList<>();
        for (String roleId : roleIds) {
            NotificationRole notificationRole = new NotificationRole();
            notificationRole.setCategoryId(notificationCategory.getId());
            notificationRole.setRoleId(roleId);
            list.add(notificationRole);
        }
        notificationRoleDao.batchSave(list);
    }

    private void saveNotificationUser(NotificationCategory notificationCategory, List<String> userIds) {
        notificationUserDao.deleteByCategoryId(notificationCategory.getId());
        List<NotificationUser> list = new ArrayList<>();
        for (String userId : userIds) {
            NotificationUser notificationUser = new NotificationUser();
            notificationUser.setCategoryId(notificationCategory.getId());
            notificationUser.setUserId(userId);
            list.add(notificationUser);
        }
        notificationUserDao.batchSave(list);
    }

    /**
     * 保存分类
     *
     * @param notificationCategory
     */
    public void saveEntity(NotificationCategory notificationCategory, List<String> roleIds, List<String> userIds) {
        if (StringUtils.isNotBlank(notificationCategory.getTag()) && !checkUniqueTag(notificationCategory.getTag())) {
            throw new BusinessException(NotificationEnum.SYS_NOTIFICATION_CATEGORY_TAG_NOT_UNIQUE);
        }
        if (notificationCategory.getSort() == null) {
            notificationCategory.setSort(500);
        }
        super.saveEntity(notificationCategory);
        //跟新sys_notification_role 或 sys_notification_user
        if (StringUtils.isNotEmpty(notificationCategory.getNotifyObjectType())) {
            if (NotifyObjectTypeConstants.ALL_ROLES.equals(notificationCategory.getNotifyObjectType())) {
                roleIds = roleService.listAll().stream().map(Role::getId).collect(Collectors.toList());
                saveNotificationRole(notificationCategory, roleIds);
            } else if (NotifyObjectTypeConstants.BY_ROLE.equals(notificationCategory.getNotifyObjectType())) {
                saveNotificationRole(notificationCategory, roleIds);
            } else if (NotifyObjectTypeConstants.BY_USER.equals(notificationCategory.getNotifyObjectType())) {
                saveNotificationUser(notificationCategory, userIds);
            }
        }
    }

    public String getTopParentCategoryName(String categoryId) {
        if (StringUtils.isEmpty(categoryId)) {
            return "";
        }
        NotificationCategory notificationCategory = notificationCategoryDao.get(categoryId);
        if (notificationCategory == null) {
            return "";
        }
        if (StringUtils.isEmpty(notificationCategory.getParentId())) {
            return notificationCategory.getName();
        }
        return getTopParentCategoryName(notificationCategory.getParentId());
    }

    /**
     * @param ids
     */
    public void deleteCategory(List<String> ids) {
        for (String categoryId : ids) {
            notificationRoleDao.deleteByCategoryId(categoryId);
            notificationUserDao.deleteByCategoryId(categoryId);
            notificationCategoryDao.delete(categoryId);
        }

    }
}
