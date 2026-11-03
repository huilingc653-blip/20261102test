package com.bytter.system.service;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.dto.UserRoleDTO;
import com.bytter.system.api.enums.RoleMessageEnum;
import com.bytter.system.dao.*;
import com.bytter.system.entity.*;
import com.bytter.system.vo.ResourcesSimpleVO;
import com.bytter.system.vo.RoleVo;
import com.bytter.system.vo.UserVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.*;
import java.util.stream.Collectors;
/**
 * Created by zhoul on 2017/4/12.
 */
@Service("roleService")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RoleService extends BaseService<Role, String> {
    @Autowired
    private RoleDao dao;
    @Autowired
    //@Qualifier("menuService")
    private MenuService menuService;
    @Autowired
    //@Qualifier("userService")
    private UserService userService;
    @Autowired
    //@Qualifier("resourcesService")
    private ResourcesService resourcesService;
    @Autowired
    private ResourcesDao resourcesDao;
    @Autowired
    private CorpUserResourceDao corpUserResourceDao;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleMenuDao roleMenuDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public BaseDao<Role, String> getEntityDao() {
        return dao;
    }
    /**
     * 保存角色实体
     *
     * @param role 角色实体
     */
    public void save(Role role, String userId) {
        checkRepeatRoleName(role.getName());
        checkRepeatRoleCode(role.getCode());
        role.setCreateBy(userId);
        role.setCreateTime(new Date());
        saveEntity(role);
    }
    private void checkRepeatRoleName(String name) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("name", name));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        if (count > 0) {
            throw new BusinessException(RoleMessageEnum.SYS_ROLE_NAME_WAS_EXIST);
        }
    }
    private void checkRepeatRoleCode(String code) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("code", code));
        criteria.setProjection(Projections.rowCount());
        Long count = (Long) criteria.uniqueResult();
        if (count > 0) {
            throw new BusinessException(RoleMessageEnum.SYS_ROLE_CODE_WAS_EXIST);
        }
    }
    /**
     * 更新角色实体
     *
     * @param role 角色实体
     */
    public void update(Role role) {
        Role dbRole = dao.get(role.getId());
        if (dbRole.getUseable() == 2) {
            role.setUseable(2);
        }
        if (!dbRole.getName().equals(role.getName())) {
            if (dbRole.getUseable() == 2) {
                throw new BusinessException(RoleMessageEnum.SYS_ROLE_NOT_OPERATION);
            }
            checkRepeatRoleName(role.getName());
        }
        if (!dbRole.getCode().equals(role.getCode())) {
            if (dbRole.getUseable() == 2) {
                throw new BusinessException(RoleMessageEnum.SYS_ROLE_NOT_OPERATION);
            }
            checkRepeatRoleCode(role.getCode());
        }
        BeanUtils.copyNotNullProperties(role, dbRole);
        dbRole.setDescription(role.getDescription());
        dbRole.setUpdateTime(new Date());
        updateEntity(dbRole);
    }
    /**
     * 根据角色ID，删除角色与其他对象的关系
     *
     * @param roleId 角色ID
     */
    public void delete(String roleId) {
        Role role = get(roleId);
        if (role.getUseable() == 2) {
            throw new BusinessException(RoleMessageEnum.SYS_ROLE_NOT_OPERATION);
        }
        List<UserRole> userRoles = role.getUserRoles();
        if (!EmptyUtils.isEmpty(userRoles)) {
            throw new BusinessException(RoleMessageEnum.SYS_ROLE_HAS_ASSIGN_CAN_NOT_DELETE);
        }
        //删除角色和用户的关系
        dao.deleteUserRoleByRoleId(roleId);
        //删除角色和菜单的关系
        dao.deleteByRoleId(roleId);
        //删除角色和资源的关系
        resourcesDao.deleteByRoleId(roleId);
        //删除角色对象
        dao.delete(roleId);
    }
    /**
     * 根据单位查找特殊权限的角色
     *
     * @param corpId
     * @return
     */
    public List<RoleVo> findSpecialRoleByCorpId(String corpId) {
        return dao.findSpecialRoleByCorpId(corpId);
    }
    public List<UserVO> findApproveUserByCorpIdAndRoleId(String corpId, String roleId, String resourceContent) {
        List<String> userIds = userService.listApproveUserIdsByRoleIdAndCorpId(Arrays.asList(roleId), corpId, Lists.newArrayList(resourceContent));
        return userService.listByUserIdsSimple(userIds);
    }
    /**
     * 根据单位ID和角色ID找出授权的用户
     *
     * @param corpId 单位ID
     * @param roleId 角色ID
     * @return
     */
    public List<UserVO> listUserByCorpAndRole(String corpId, String roleId) {
        Role role = dao.get(roleId);
        List<UserRole> roleUserList = role.getUserRoles();
        List<UserVO> userVOList = new ArrayList<>();
        for (UserRole userRole : roleUserList) {
            User user = userRole.getUser();
            if (ObjectUtil.isEmpty(user)){
                continue;
            }
            UserVO userVO = BeanUtils.copyNewInstanceProperties(user, UserVO.class);
            Corp corp = user.getCorp();
            if (ObjectUtil.isNotEmpty(corp)){
                userVO.setCorpId(corp.getId());
                userVO.setCorpName("(" + corp.getCode() + ") " + corp.getName());
            }
            userVOList.add(userVO);
        }
        return userVOList;
    }
    /**
     * 给当前角色绑定菜单和资源
     *
     * @param role         角色对象
     * @param menuIds      菜单ID集合
     * @param resourcesIds 资源ID结合
     */
    @Transactional(rollbackFor = Exception.class)
    public void assign(Role role, String menuIds, String resourcesIds) {
        //将游离态的role转换成持久化状态
        String roleId = role.getId();
        List<String> menuIdList = null;
        List<String> resourceIdList = null;
        if (StringUtils.isNotEmpty(menuIds)) {
            //传入的值包含英文状态下的逗号，保存单个或者一个菜单
            menuIdList = Arrays.asList(menuIds.split(CommonConstant.COMMA_SPLIT));
        }
        if (StringUtils.isNotEmpty(resourcesIds)) {
            //传入的值包含英文状态下的逗号，保存单个或者一个资源
            resourceIdList = Arrays.asList(resourcesIds.split(CommonConstant.COMMA_SPLIT));
        }
        //处理特殊权限
        handleUserSpecialPermission(role, resourceIdList);
        //清空与菜单关联关系
        dao.deleteByRoleId(roleId);
        //清空与资源关联关系
        resourcesDao.deleteByRoleId(roleId);
        // 根据角色编号清除当前角色下所有用户的资源权限
        clearUserResourcesByRole(roleId);
        //获取数据库的role
        role = get(roleId);
        if (menuIdList != null) {
            Set<RoleMenu> menuSet = new HashSet<>();
            List<Menu> menuList = menuService.getEntityDao().list(menuIdList);
            for (Menu menu : menuList) {
                menuSet.add(new RoleMenu(role, menu));
            }
            //设置勾选的菜单的关联关系
            role.setRoleMenus(menuSet);
        }
        if (resourceIdList != null) {
            Set<Permission> resourcesSet = new HashSet<>();
            List<Resources> resourcesList = resourcesService.getEntityDao().list(resourceIdList);
            for (Resources resources : resourcesList) {
                resourcesSet.add(new Permission(role, resources));
            }
            //设置勾选的资源的关联关系
            role.setPermissions(resourcesSet);
        }
        super.updateEntity(role);
    }

    /**
     * 辅助方法：根据角色编号清除当前角色下所有用户的资源权限。
     *
     * @param roleId 角色编号。
     */
    private void clearUserResourcesByRole(String roleId) {
        // 清空拥有当前角色的用户资源缓存
        List<String> userIdList = userDao.listUserIdsByRid(roleId);
        if (ObjectUtil.isNotEmpty(userIdList)) {
            userIdList.forEach(userId -> {
                String key = RedisCacheKey.makeRedisKey(RedisCacheKey.RESOURCES_PERMISSION_PREFIX, userId);
                redisTemplate.delete(key);
                logger.info("删除redis缓存权限==========" + key);
            });
        }
    }

    /**
     * 根据角色处理用户特殊权限的关系
     *
     * @param role
     * @param resourceIdList
     */
    private void handleUserSpecialPermission(Role role, List<String> resourceIdList) {
        String roleId = role.getId();
        //获取该角色下全部用户有
        List<Map<String, String>> userByRoleId = dao.findUserByRoleId(role.getId());
        if(CollectionUtils.isEmpty(userByRoleId)){
           return;
        }
        List<String> userIdList = userByRoleId.stream().map(o -> o.get("userId")).collect(Collectors.toList());

        //获取userIdList
        List<Map<String, String>> userIdAndCorp = corpUserResourceDao.listByUserIds(userIdList);
        if(CollectionUtils.isEmpty(userIdAndCorp)){
           return;
        }

        // 根据角色ID查找资源的ID
        List<String> resourceIds = dao.findResourceIdByRoleId(roleId);
        Set<String> resourceIdsReduce = new HashSet<>(resourceIds);
        if (CollectionUtils.isEmpty(resourceIdList)) {
            resourceIdList = new ArrayList<>(0);
        }
        Set<String> resourceIdsAdd = new HashSet<>(resourceIdList);
        //获取减少的资源
        resourceIdsReduce.removeAll(resourceIdList);
        //获取新增的资源
        resourceIdsAdd.removeAll(resourceIds);
        Map<String, ResourcesSimpleVO> resourceId2ResourceInfo = Maps.newHashMap();
        if (EmptyUtils.isNotEmpty(resourceIdsAdd)) {
            List<ResourcesSimpleVO> resources = resourcesDao.listResourceByResourceIds(resourceIdsAdd);
            if (EmptyUtils.isNotEmpty(resources)) {
                for (ResourcesSimpleVO resourcesSimpleVO : resources) {
                    resourceId2ResourceInfo.put(resourcesSimpleVO.getId(), resourcesSimpleVO);
                }
            }
        }
        List<CorpUserResource> list = new ArrayList<>();
        for (Map<String, String> map : userIdAndCorp) {
            // listByUserIds 经 listMapBySQL 统一小写 key，兼容 Oracle 大写 / MySQL 小写
            String userId = map.get("userid");
            String corpId = map.get("corpid");
            //根据用户ID和资源ID删除特殊权限
            corpUserResourceDao.deleteByUserIdAndResourceId(userId, resourceIdsReduce);
            for (String resourceId : resourceIdsAdd) {
                ResourcesSimpleVO resource = resourceId2ResourceInfo.get(resourceId);
                if (EmptyUtils.isEmpty(resource)) {
                    list.add(new CorpUserResource(corpId, userId, resourceId));
                } else {
                    list.add(new CorpUserResource(corpId, userId, resourceId, resource.getPermission(), resource.getMenuId()));
                }
            }
        }
        //先让使用SQL脚本的语句生效
        corpUserResourceDao.flush();
        //创建特殊权限关系
        corpUserResourceDao.batchSaveData(list);
    }
    /**
     * 批量给用户挂上角色
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignUser(Map<String, Object> data) {
        StopWatch stopWatch = new StopWatch("assignUser");
        stopWatch.start("before setUserSpecialPermission");
        String roleIdTemp = (String) data.get("roleId");
        List<Map<String, String>> dataList = (List<Map<String, String>>) data.get("data");
        List<String> choseUserIds = Lists.newArrayList();
        for (Map<String, String> userRole : dataList) {
            choseUserIds.add(userRole.get("userId"));
        }
        List<String> currentUserIdsForRole = dao.listUserIdsByRoleId(roleIdTemp);
        if (null == currentUserIdsForRole) {
            currentUserIdsForRole = Lists.newArrayListWithCapacity(0);
        }
        List<String> toDeleteUserIds = Lists.newArrayList();
        toDeleteUserIds.addAll(currentUserIdsForRole);
        toDeleteUserIds.removeAll(choseUserIds);
        List<String> toAddUserIds = Lists.newArrayList();
        toAddUserIds.addAll(choseUserIds);
        toAddUserIds.removeAll(currentUserIdsForRole);
        logger.debug("toDeleteUserIds=" + JsonUtils.toJSONString(toDeleteUserIds) + ",toAddUserIds=" + JsonUtils.toJSONString(toAddUserIds));
        //由于关系维护端在User中，所以这里先用SQL删除关联关系，删除该网点单位下所有的用户
        //dao.deleteUserRoleByRoleId(roleIdTemp);
        dao.deleteUserRoleByRoleId(roleIdTemp, toDeleteUserIds);
        Map<String, User> userId2User = Maps.newHashMap();
        List<String> addAndDelUserIds = Lists.newArrayList();
        addAndDelUserIds.addAll(toAddUserIds);
        addAndDelUserIds.addAll(toDeleteUserIds);
        List<User> addAndDelUsers = userService.listUserByUserIds(addAndDelUserIds);
        if (EmptyUtils.isNotEmpty(addAndDelUsers)) {
            for (User user : addAndDelUsers) {
                userId2User.put(user.getId(), user);
            }
        }
        Map<String, String> userIdAndCorpId = new HashMap<>();
        Role role = get(roleIdTemp);
        for (Map<String, String> map : dataList) {
            String userId = map.get("userId");
            if (toAddUserIds.contains(userId)) {
                dao.insertUserRole(userId, role.getId());
                //预存特殊权限
                userIdAndCorpId.put(userId, userId2User.get(userId).getCorp().getId());
            }
        }
        if (EmptyUtils.isNotEmpty(toDeleteUserIds)) {
            for (String userId : toDeleteUserIds) {
                User user = userId2User.get(userId);
                if (ObjectUtil.isNotEmpty(user)){
                    userIdAndCorpId.put(user.getId(), user.getCorp().getId());
                }
            }
        }
        //提交角色分配用户
        dao.flush();
        stopWatch.stop();
        stopWatch.start("setUserSpecialPermission");
        //设置用户特殊权限
        for (String key : userIdAndCorpId.keySet()) {
            if (toAddUserIds.contains(key) || toDeleteUserIds.contains(key)) {
                userService.setUserSpecialPermission(key, userIdAndCorpId.get(key));
            }
        }
        stopWatch.stop();
        stopWatch.start("清理角色下所有用户资源缓存");
        // 根据角色编号清除当前角色下所有用户的资源权限
        clearUserResourcesByRole(roleIdTemp);
        stopWatch.stop();
        logger.debug("assignUser total spends ：" + stopWatch.prettyPrint());
    }
    public List<String> findMenuIdByRoleId(String roleId) {
        return dao.findMenuIdByRoleId(roleId);
    }
    public TableGridResponse<RoleVo> getAllRole(TableGridInput input) {
        IPageResult<RoleVo> pageResult = dao.getAllRole(input);
        List<RoleVo> list = pageResult.getList();
        for (RoleVo roleVo : list) {
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(roleVo.getId());
            if (StringUtils.isNotBlank(businessBaseContext)) {
                roleVo.setName(businessBaseContext);
            }
        }
        pageResult.setList(list);
        return pageResult.convert(TableGridResponse.class);
    }
    /**
     * Created by chenlj 2018/4/16
     *
     * @param user
     * @return
     */
    public List<Role> getRoleListByUser(User user) {
        Set<UserRole> userRoles = user.getUserRoles();
        List<Role> roleList = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            roleList.add(dao.get(userRole.getRole().getId()));
        }
        return roleList;
    }
    public List<Role> listByIds(List<String> ids) {
        return dao.listByIds(ids);
    }

    public List<RoleVo> listAllRole(){
        return dao.findAllRoles();
    }
    /**
     * 根据用户ID获取关联的角色ID集合。
     *
     * @param userId 用户ID。
     * @return 用户角色ID集合。
     */
    public Set<String> getRoleIdsByUserId(String userId) {
        List<String> roleList = dao.getRoleIdsByUserId(userId);
        if (ObjectUtil.isNotEmpty(roleList)) {
            return new HashSet<>(roleList);
        }
        return new HashSet<>();
    }

    /**
     * 根据用户ID获取关联的角色ID集合。
     *
     * @param userId 用户ID。
     * @param tenantId 租户ID。
     * @return 用户角色ID集合。
     */
    public Set<String> getRoleIdsByUserId(String userId, String tenantId) {
        List<String> roleList = dao.getRoleIdsByUserId(userId, tenantId);
        if (ObjectUtil.isNotEmpty(roleList)) {
            return new HashSet<>(roleList);
        }
        return new HashSet<>();
    }

    public List<UserRoleDTO> getUserRole(Set<String> userIds) {
        return dao.getUserRole(userIds);
    }
}
