package com.bytter.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.cloud.common.core.persistence.vo.Result;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.jdbc.TenantProperties;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.SysCacheConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.CorpMessageEnum;
import com.bytter.system.api.enums.UserMessageEnum;
import com.bytter.system.dao.LoginLogDao;
import com.bytter.system.dao.ResourcesDao;
import com.bytter.system.entity.*;
import com.bytter.system.service.*;
import com.bytter.system.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by huangx on 2017/5/25.
 */
@Slf4j
@DubboService
@Service("userServiceImpl")
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserService userService;
    @Autowired
    private CorpService corpService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ResourcesDao resourcesDao;

    @Autowired
    private ResourcesService resourcesService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private LoginLogDao loginLogDao;

    @Autowired
    private SysTenantManagerService sysTenantManagerService;

    @Autowired
    private ConfigService configService;

    @Resource
    private TenantProperties tenantProperties;

    @Value("${xxl.job.operator}")
    private String taskOperator;

    /**
     * 获取租户配置文件信息
     *

     */
    @Override
    public TenantPropDTO getTenantProperties() {
        TenantPropDTO dto = BeanUtils.copyNewInstanceProperties(tenantProperties, TenantPropDTO.class);
        return dto;
    }
    /**
     * 通过用户登录名查找用户
     *
     * @param loginName 登录名
     * @return UserDTO
     */
    @Override
    public UserDTO getByLoginName(String loginName) {
        User user = userService.getByLoginName(loginName);
        return makeUserDTO(user);
    }

    @Override
    public UserDTO getByLoginName(String loginName, String tenantId) {
        User user = userService.getByLoginName(loginName, tenantId);
        SysTenantManager sysTenantManager = sysTenantManagerService.getByTenantCode(tenantId);
        if (null != user) {
            String hashKey = RedisCacheKey.makeRedisKey("bytter-system", RedisCacheKey.USER_SYS_SKIN);
            //更改皮肤设置到缓存中
            redisTemplate.opsForHash().put(hashKey, user.getId(), user.getSysSkin());
        }
        return makeTenantUserDTO(user, sysTenantManager);
    }

    @Override
    public UserDTO getByEmployeeId(String employeeId) {
        User user = userService.getByEmployeeId(employeeId);
        return makeUserDTO(user);
    }

    /**
     * 通过用户登录名查找用户的加密盐
     *
     * @param loginName 登录名
     * @return UserDTO
     */
    @Override
    public String getSaltByLoginName(String loginName) {
        String salt = userService.getSaltByLoginName(loginName);
        return salt;
    }



    /**
     * 通过用户ID查找用户
     *
     * @param userId 用户ID
     * @return UserDTO
     */
    @Override
    public UserDTO getByUserId(String userId) {
        User user = userService.getEntityDao().get(userId, true);
        return makeUserDTO(user);
    }

    /**
     * 构建用户DIO
     *
     * @param user
     * @return
     */
    private UserDTO makeUserDTO(User user) {
        if (user != null) {
            UserDTO userDTO = BeanUtils.copyNewInstanceProperties(user, UserDTO.class);
            userDTO.setCorp(BeanUtils.copyNewInstanceProperties(user.getCorp(), CorpDTO.class));
            return userDTO;
        }
        return null;
    }

    /**
     *
     * 构建租户用户DTO
     *
     * @param user
     * @param sysTenantManager
     * @return
     */
    private UserDTO makeTenantUserDTO(User user, SysTenantManager sysTenantManager) {
        if (user != null) {
            UserDTO userDTO = BeanUtils.copyNewInstanceProperties(user, UserDTO.class);
            userDTO.setCorp(BeanUtils.copyNewInstanceProperties(user.getCorp(), CorpDTO.class));
            userDTO.setTenantStatus(sysTenantManager.getTenantStatus());
            return userDTO;
        }
        return null;
    }

    @Override
    public List<String> listUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId) {
        return userService.listUserIdsByRoleIdAndCorpId(rolesId, corpId);
    }

    @Override
    public List<String> listUserIdsByPermission(String permission) {
        return userService.listUserIdsByPermission(permission);
    }

    @Override
    public List<String> listUserIdsByCorpIdAndPermission(List<String> corpIds, String permission) {
        return userService.listUserIdsByCorpIdAndPermission(corpIds, permission);
    }

    /**
     * 根据用户ids得到用户
     *
     * @param ids
     * @return
     */
    @Override
    public List<UserDTO> listByUserIds(List<String> ids) {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<User> list = userService.listUserByUserIds(ids);
        if (!EmptyUtils.isEmpty(list)) {
            userDTOList = BeanUtils.copyListProperties(list, UserDTO.class);
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> listAllUserByUnDelete() {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<UserVO> userVOList = userService.listAllUserByUnDelete();
        if (!EmptyUtils.isEmpty(userVOList)) {
            userDTOList = BeanUtils.copyListProperties(userVOList, UserDTO.class);
        }
        return userDTOList;
    }

    @Override
    public List<UserDTO> listAllUser() {
        List<UserDTO> userDTOList = new ArrayList<>();
        List<UserVO> userVOList = userService.listAllUser();
        if (!EmptyUtils.isEmpty(userVOList)) {
            userDTOList = BeanUtils.copyListProperties(userVOList, UserDTO.class);
        }
        return userDTOList;
    }

    @Override
    public List<String> listApproveUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId, List<String> approveNewRes) {
        return userService.listApproveUserIdsByRoleIdAndCorpId(rolesId, corpId, approveNewRes);
    }

    @Override
    public List<String> listApproveUserIdsByRoleIdAndCorpIdNew(List<String> rolesId, String corpId, List<String> approveNewRes) {
        return userService.listApproveUserIdsByRoleIdAndCorpIdNew(rolesId, corpId, approveNewRes);
    }


    @Override
    public List<String> listUserIdsByCorpIdAndPermissionAndUserId(String corpId, List<String> permissions, List<String> userId) {
        return userService.listUserIdsByUserIdAndCorpIdAndPermission(corpId, permissions, userId);
    }

    @Override
    public List<String> listUserIdsByRoleId(List<String> rolesId) {
        return userService.listUserIdsByRoleId(rolesId);
    }

    private Corp validateUserCorp(UserDTO userDTO) {
        Corp corp = corpService.getCorpByCode(userDTO.getCorp().getCode());
        if (EmptyUtils.isEmpty(corp)) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_CODE_NON_EXISTENT, userDTO.getCorp().getCode());
        }
        return corp;
    }

    private Corp validateUserCorpById(UserDTO userDTO) {
        if (ObjectUtil.isEmpty(userDTO.getCorp()) || ObjectUtil.isEmpty(userDTO.getCorp().getId())) {
            return null;
        }
        Corp corp = corpService.getCropById(userDTO.getCorp().getId());
        if (EmptyUtils.isEmpty(corp)) {
            throw new ParameterException(CorpMessageEnum.SYS_CORP_MESSAGE_CORP_CODE_NON_EXISTENT, userDTO.getCorp().getId());
        }
        return corp;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<UserDTO> userList) {
        for (UserDTO userDTO : userList) {
            User dbUser = userService.getByLoginName(userDTO.getLoginName());
            if (!EmptyUtils.isEmpty(dbUser)) {
                throw new ParameterException(UserMessageEnum.SYS_USER_LOGIN_NAME_WAS_EXIST);
            }
            Corp corp = validateUserCorp(userDTO);
            User user = BeanUtils.copyNewInstanceProperties(userDTO, User.class);
            user.setCorp(corp);
            userService.save(user, "ERP", null, null, null);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserNoCorp(List<UserDTO> userList, String source) {
        for (UserDTO userDTO : userList) {
            User dbUser = userService.getByLoginName(userDTO.getLoginName());
            if (EmptyUtils.isEmpty(dbUser)) {
                Corp corp = validateUserCorpById(userDTO);
                User user = BeanUtils.copyNewInstanceProperties(userDTO, User.class);
                user.setCorp(corp);
                userService.save(user, source, userDTO.getDingTalkUserId(), userDTO.getWeiXinUserId(), userDTO.getFeishuUserId());
            } else {
                log.error("新增大参林员工用户名【{}】已存在", userDTO.getLoginName());
            }
        }
    }

    @Override
    public List<Map<String, Object>> getUserIdMapByEmployeeIdList(List<String> employeeIdList) {
        return userService.listUserIdByEmployeeId(employeeIdList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserNoCorp(List<UserDTO> userList, String source) {
        for (UserDTO userDTO : userList) {
            User dbUser = userService.getByLoginName(userDTO.getLoginName());
            if (!EmptyUtils.isEmpty(dbUser)) {
                Corp corp = validateUserCorpById(userDTO);
                User user = BeanUtils.copyNewInstanceProperties(userDTO, User.class);
                user.setId(dbUser.getId());
                user.setCorp(corp);
                // 员工状态不变更【只有资金系统员工启用，且SSO系统发起禁用时才变更资金系统状态】
                if (ObjectUtil.equal(dbUser.getStatus(), 1) && ObjectUtil.equal(userDTO.getStatus(), 2)) {
                    user.setStatus(2);
                } else {
                    user.setStatus(dbUser.getStatus());
                }
                // 报表状态不变更
                user.setShowReport(dbUser.getShowReport());
                userService.update(user, null, source, userDTO.getDingTalkUserId(), userDTO.getWeiXinUserId(), userDTO.getFeishuUserId());
            } else {
                log.error("同步大参林员工用户名【{}】不存在", userDTO.getLoginName());
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(List<UserDTO> userList) {
        for (UserDTO userDTO : userList) {
            User dbUser = userService.getByLoginName(userDTO.getLoginName());
            if (EmptyUtils.isEmpty(dbUser)) {
                throw new ParameterException(UserMessageEnum.SYS_USER_LOGIN_NAME_WAS_EXIST);
            }
            validateUserCorp(userDTO);
            User user = BeanUtils.copyNewInstanceProperties(userDTO, User.class);
            Corp corp = BeanUtils.copyNewInstanceProperties(userDTO.getCorp(), Corp.class);
            user.setCorp(corp);
            user.setId(dbUser.getId());
            userService.update(user, null, "ERP", null, null, null);
        }
    }

    @Override
    @Transactional
    public void delete(List<UserDTO> userList) {
        List<String> ids = new ArrayList<>();
        for (UserDTO userDTO : userList) {
            User user = userService.getByLoginName(userDTO.getLoginName());
            if (user != null) {
                ids.add(user.getId());
            }
        }
        userService.delete(ids);
    }

    /**
     * 获取用户代表信息
     *
     * @param userList
     * @return
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> todoInfo(List<UserDTO> userList) {
        List<Map<String, Object>> reList = new ArrayList<>();
        for (UserDTO userDTO : userList) {
            Map<String, Object> todoInfo = new HashMap<>();
            reList.add(todoInfo);
            try {
                todoInfo.put("loginName", userDTO.getLoginName());
                if (StringUtils.isEmpty(userDTO.getLoginName())) {
                    todoInfo.put("status", 0);
                    todoInfo.put("errorMsg", "用户登入名称不能为空！");
                    continue;
                }
                User user = userService.getByLoginName(userDTO.getLoginName());
                if (user == null || StringUtils.isEmpty(user.getId())) {
                    todoInfo.put("status", 0);
                    todoInfo.put("errorMsg", "用户登入名称系统不存在！");
                    continue;
                }
                todoInfo.put("status", 1);
                Integer userNotificationCount = notificationService.getUserNotificationCount(user.getId());
                todoInfo.put("todoTaskCount", userNotificationCount);
                //Map<String, Object> flowNumber = workFlowService.getFlowNumber(user.getId());
                Map<String, Object> flowNumber = new HashMap<>();
                todoInfo.put("todoFlowCount", flowNumber.get("todoCount"));
            } catch (Exception e) {
                e.printStackTrace();
                todoInfo.put("status", 0);
                todoInfo.put("errorMsg", "出现异常！" + e.getMessage());
            }
        }
        return reList;
    }


    /**
     * 获取系统默认发起员
     *
     * @return 发起员信息
     */
    @Override
    public UserDTO getScheduler() {
        return this.getByLoginName(taskOperator);
    }

    /**
     * 通过用户ID查找用户
     *
     * @param mobile 手机号
     * @return UserDTO
     */
    @Override
    public UserDTO getUserByMobile(String mobile) {
        User user = userService.getUserByMobile(mobile);
        return makeUserDTO(user);
    }

    @Override
    public UserDTO getByUsername(String username) {
        User user = userService.getByLoginName(username);
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    @Override
    public Map<String, UserDTO> listUserByGpId(List<String> ids) {
        return listByUserIds(ids).stream().collect(Collectors.toMap(UserDTO::getId, item -> item));
    }

    @Override
    public Map<String, UserDTO> listUser() {
        List<User> list = this.userService.listAll();
        if (EmptyUtils.isEmptyList(list)) {
            return null;
        }
        List<UserDTO> resultList = BeanUtils.copyListProperties(list, UserDTO.class);
        return resultList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
    }

    @Override
    public Boolean hasPermissionV1(String userId, String permission, String tenantId) {
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.RESOURCES_PERMISSION_PREFIX, userId);
        Long size = redisTemplate.opsForSet().size(key);
        //如果用户资源不存在查询一次设置到缓存中
        Set<Object> set = redisTemplate.opsForSet().members(key);
        if (Objects.isNull(size) || size == 0 || CollectionUtils.isEmpty(set) || set.size() <= 1) {
            String[] permissions = resourcesService.listPermission(userId);
            if (permissions.length == 0) {
                return Boolean.FALSE;
            }
            redisTemplate.opsForSet().add(key, permissions);
        }
        set = null;
        //多个资源权限则以,分割验证
        boolean contains = permission.contains(",");
        Set<String> valueSet = new HashSet<>();
        if (contains) {
            valueSet = Arrays.stream(permission.split(",")).collect(Collectors.toSet());
        } else {
            valueSet.add(permission);
        }
        Set<Object> members = redisTemplate.opsForSet().members(key);
        for (String str : valueSet) {
            if (members.contains(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用户是否有某个资源权限
     *
     * @param userId
     * @param permission
     * @return
     */
    @Override
    public Boolean hasPermission(String userId, String permission) {
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.RESOURCES_PERMISSION_PREFIX, userId);
        Long size = redisTemplate.opsForSet().size(key);
        //如果用户资源不存在查询一次设置到缓存中
        Set<Object> set = redisTemplate.opsForSet().members(key);
        if (Objects.isNull(size) || size == 0 || CollectionUtils.isEmpty(set) || set.size() <= 1) {
            String[] permissions = resourcesService.listPermission(userId);
            if (permissions.length == 0) {
                return Boolean.FALSE;
            }
            redisTemplate.opsForSet().add(key, permissions);
        }
        set = null;
        //多个资源权限则以,分割验证
        boolean contains = permission.contains(",");
        Set<String> valueSet = new HashSet<>();
        if (contains) {
            valueSet = Arrays.stream(permission.split(",")).collect(Collectors.toSet());
        } else {
            valueSet.add(permission);
        }
        Set<Object> members = redisTemplate.opsForSet().members(key);
        for (String str : valueSet) {
            if (members.contains(str)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public Result<UserDTO> getByTenantIdAndUsername(String tenantId, String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<UserDTO> getByTenantIdAndPhone(String tenantId, String phone) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<List<UserDTO>> getUsersByTenantId(String tenantId, String userName) {
        return null;
    }

    /**
     * 获取租户下所有用户
     *
     * @param tenantId 租户ID
     * @return 用户列表
     */
    @Override
    public Result<List<String>> getUserNamesByTenantId(String tenantId) {
        List<String> userList = userService.listUserNameByTenantId(tenantId);
        return Result.success(userList);
    }

    @Override
    public Set<String> queryRolesByUserId(String userId) {
        return roleService.getRoleIdsByUserId(userId);
    }

    @Override
    public Set<String> queryRolesByUserIdAndTenantId(String userId, String tenantId) {
        return roleService.getRoleIdsByUserId(userId, tenantId);
    }

    @Override
    public Result<Set<String>> getAssignedUserResourceIds(String tenantId, String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<String> getUserLoginType(String tenant, String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<Boolean> updateLock(String id) {
        // TODO Auto-generated method stub
        userService.updateLock(id);
        return Result.success();
    }

    @Override
    public Result<Integer> getFailCount(String username, String code) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<UserDTO> getUserByCode(String username, String code) {
        // TODO Auto-generated method stub
        User user = userService.getByLoginName(username, code);
        UserDTO userDTO = null;
        if (null != user) {
            userDTO = new UserDTO();
            BeanUtils.copyProperties(user, userDTO);
        }
        return Result.success(userDTO);
    }

    @Override
    public Result<UserDTO> getUserByTenantIdAndUserName(String tenantId, String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<Boolean> resetCount(String username, String code) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<Boolean> resetStatus(String username, String code) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<Boolean> saveLoginLog(LogLoginDTO loginLogDTO) {
        if (ObjectUtils.isEmpty(loginLogDTO)) {
            return new Result();
        }
        LoginLog loginLog = new LoginLog();
        BeanUtils.copyNotNullProperties(loginLogDTO, loginLog);
        saveLoginLogWithTenant(loginLog);
        return new Result();
    }

    @Override
    public Result<List<ApiResourceDTO>> getAfterLoginAccessApiResources() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Cacheable(value = SysCacheConstant.DEFAULT_CACHE, key = "'allResouce'")
    public List<ApiResourceDTO> getResources() {
        List<Resources> resources = resourcesDao.listResourceV2();
        return buildApiResourceDTOS(resources);
    }

    @NotNull
    private List<ApiResourceDTO> buildApiResourceDTOS(List<Resources> resources) {
        List<ApiResourceDTO> result = new ArrayList<>();
        resources.stream().filter(item -> StringUtils.isNotEmpty(item.getUrl())).collect(Collectors.toList()).forEach(e -> {
            String[] arrays = e.getUrl().split(",");
            for (String array : arrays) {
                ApiResourceDTO resourceDTO = new ApiResourceDTO();
                resourceDTO.setUrl(array);
                resourceDTO.setCode(e.getPermission());
                result.add(resourceDTO);
            }
        });
        return result;
    }

    @Override
    public List<ApiResourceDTO> getResourcesV1(String tenantId) {
        List<Resources> resources = resourcesDao.listResourceByTenant();
        return buildApiResourceDTOS(resources);
    }

    @Override
    public Result<ConfigDTO> getConfig(String service, String type, String name, String tenantId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Result<ConfigDTO> getConfig(String type, String name) {
        Config config = configService.getByTypeAndName(type, name);
        ConfigDTO configDTO = null;
        if (null != config) {
            configDTO = new ConfigDTO(config.getValue());
        }
        return Result.success(configDTO);
    }

    @Override
    public Result<List<ApiResourceDTO>> getAssignedUserApiResources(String tenantId, String username) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Map<String, String> getUserNameByLoginNames(List<String> loginNames) {
        return userService.getUserNameByLoginNames(loginNames);
    }

    @Override
    public Map<String, String> getUserNameByIds(List<String> ids) {
        List<UserVO> voList = userService.listByUserIdsSimple2(ids);
        return voList.stream().collect(Collectors.toMap(UserVO::getId, UserVO::getUsername));
    }

    @Override
    public List<UserAndCorpDTO> getUserNameAndCorpNameByUserIds(List<String> userIds) {
        List<UserVO> userVOList = userService.getUserNameAndCorpNameByUserIds(userIds);
        List<UserAndCorpDTO> userAndCorpDTOS = BeanUtils.copyListProperties(userVOList, UserAndCorpDTO.class);
        return userAndCorpDTOS;
    }

    @Override
    public Map<String, String> getLoginNameByIds(List<String> ids) {
        return userService.getLoginNameByIds(ids);
    }

    @Override
    public RoleDTO getRoleById(String roleId) {
        Role role = roleService.get(roleId, true);
        RoleDTO roleDTO = new RoleDTO();
        if (role != null) {
            BeanUtils.copyNotNullProperties(role, roleDTO);
        }
        return roleDTO;
    }

    @Override
    public Map<String, RoleDTO> listRoleByIds(List<String> roleIds) {
        List<Role> roleList = roleService.listByIds(roleIds);
        List<RoleDTO> roleData = BeanUtils.copyListProperties(roleList, RoleDTO.class);
        Map<String, RoleDTO> mapData = new HashMap<>();
        if (ObjectUtil.isNotEmpty(roleData)) {
            mapData = roleData.stream().collect(Collectors.toMap(RoleDTO::getId, Function.identity(), (key1, key2) -> key2));
        }
        return mapData;
    }

    @Override
    public String getLoginName() {
        if (ObjectUtils.isEmpty(UserContextHelper.helper().getCurrentUser())) {
            return "";
        }
        return UserContextHelper.helper().getCurrentUser().getLoginName();
    }

    @Override
    public String getCadnByLoginName() {
        if (ObjectUtils.isEmpty(UserContextHelper.helper().getCurrentUser())) {
            return "";
        }
        return userService.getByLoginName(UserContextHelper.helper().getCurrentUser().getLoginName()).getUserCadn();
    }


    @Override
    public String getUserCodeByCadn(String cadn) {
        if (ObjectUtils.isEmpty(cadn)) {
            return "";
        }
        return userService.getUserCodeByCadn(cadn);
    }

    @Override
    public boolean saveLoginLog(LoginLogDTO loginLogDTO) {
        if (ObjectUtils.isEmpty(loginLogDTO)) {
            return false;
        }
        LoginLog loginLog = new LoginLog();
        BeanUtils.copyNotNullProperties(loginLogDTO, loginLog);
        saveLoginLogWithTenant(loginLog);
        return true;
    }

    /**
     * 登录/退出留痕发生在租户上下文未建立的边界（登录事件经 JWT 手动解析用户与租户，未走用户上下文拦截器），
     * 此处用实体已携带的 tenantId 显式建立租户上下文后写入，使登录日志走正常租户改写路径；
     * 写入完成后恢复原上下文，防止 Dubbo 线程池复用导致租户上下文跨请求污染。
     */
    private void saveLoginLogWithTenant(LoginLog loginLog) {
        if (ObjectUtils.isEmpty(loginLog.getTenantId())) {
            log.warn("登录留痕缺少租户标识，拒绝写入：loginName={}, host={}", loginLog.getLoginName(), loginLog.getHost());
            return;
        }
        String originalTenant = UserContextHelper.helper().getTTLTenant();
        try {
            UserContextHelper.helper().setTTLTenant(loginLog.getTenantId());
            loginLogDao.save(loginLog);
            // save* 事务切面在方法返回后才提交(commit→flush)，此处必须在租户上下文有效期内强制刷库，
            // 否则 INSERT 执行 prepareStatement 时租户上下文已被恢复为空，仍会命中空租户校验被阻断。
            loginLogDao.flush();
        } finally {
            if (originalTenant != null) {
                UserContextHelper.helper().setTTLTenant(originalTenant);
            } else {
                UserContextHelper.helper().clearTTLTenant();
            }
        }
    }

    @Override
    public List<UserRoleDTO> getUserRole(Set<String> userIds) {
        return roleService.getUserRole(userIds);
    }

}
