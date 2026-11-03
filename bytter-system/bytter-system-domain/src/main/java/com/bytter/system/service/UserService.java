package com.bytter.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.bytter.cloud.common.core.util.PageUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.cache.redis.core.RedisCacheKey;
import com.bytter.core.common.context.UserContextHelper;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.page.IPageResult;
import com.bytter.core.common.page.PageResult;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.poi.excel.entity.result.ExcelVerifyHanlderResult;
import com.bytter.poi.handler.inter.IExcelVerifyHandler;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.*;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.enums.LoginMessageEnum;
import com.bytter.system.api.enums.UserMessageEnum;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.system.dao.*;
import com.bytter.system.entity.*;
import com.bytter.system.vo.ResourcesSimpleVO;
import com.bytter.system.vo.UserPermissionVO;
import com.bytter.system.vo.UserVO;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by zhoul on 2017/4/12.
 */
@Service("userService")
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService extends BaseService<User, String> implements IExcelVerifyHandler<UserVO>, ICorpDeleteVerifyService, ICorpDisableVerifyService {

    private static final String DEFAULT_INITIAL_PASSWORD = "bytter.123";

    @Resource
    private UserDao userDao;

    @Autowired
    private ResourcesService resourcesService;

    @Resource
    private MenuService menuService;

    @Resource
    private CorpUserResourceDao corpUserResourceDao;

    @Resource
    private IConfigService configServiceImpl;

    @Resource
    private RoleDao roleDao;

    @Resource
    private CorpService corpService;

    @Resource
    private UserHistoryPasswordService userHistoryPasswordService;

    @Resource
    private ILicenseService licenseServiceImpl;

    @Autowired
    private ResourcesDao resourcesDao;

    @Autowired
    private AccountGrantDao accountGrantDao;
    @Autowired
    private IDictService dictServiceI;
    @Resource
    private IMultiLangService multiLangServiceI;
    @Autowired
    private IAccountMappingService accountMappingService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CorpDao corpDao;

    @Autowired
    private ThreadPoolTaskExecutor asyncThreadPoolExecutor;
    @Autowired
    private UserPermissionAsyncService userPermissionAsyncService;

    @Autowired
    private SysTenantManagerService sysTenantManagerService;

    @Autowired
    private UserFaceDetectionService userFaceDetectionService;

    @Autowired
    private SysTenantManagerDao sysTenantManagerDao;


    @Override
    public BaseDao<User, String> getEntityDao() {
        return userDao;
    }


    public DataTablesOutput getDataTables(DataTablesInput input) {
        DictGroupDTO dictGroupIdForStatus = this.dictServiceI.get(UserConstant.USER_STATUS_DICT);
        List<String> corpIds = corpDao.getCorpIdsByUser(null, UserContextHelper.helper().getUserId());
        IPageResult<UserVO> pageResult = userDao.listUserNew(input, dictGroupIdForStatus.getId(),corpIds);
        return pageResult.convert(DataTablesOutput.class);
    }

    /**
     * 根据用户ID查询用户角色ID
     * @param userId
     * @return 角色ID
     */
    public List<String> listRidsByUid(String userId) {
        return userDao.listRidsByUid(userId);
    }

    public TableGridResponse<UserVO> getTableGrid(TableGridInput input, ICurrentUser currentUser) {
        DictGroupDTO dictGroupIdForStatus = this.dictServiceI.get(UserConstant.USER_STATUS_DICT);
        //admin不需要要根据特殊权限过滤用户
        if ("admin".equals(currentUser.getLoginName())) {
            IPageResult<UserVO> pageResult = userDao.listUser(input, dictGroupIdForStatus.getId());
            return pageResult.convert(TableGridResponse.class);
        }
        //获得当前用户拥有“用户查询”特殊权限的单位
        String userId = currentUser.getId();
        List<String> corpIdList = corpUserResourceDao.listCorpIdByUserIdAndResourceId(userId, SysResourceConstant.SYS_USER_SELECT);
        String corpIdListStr = StringUtils.join(corpIdList, ",");
        if (StringUtils.isBlank(corpIdListStr)) {
            return new PageResult<>(input, 0, new ArrayList<>()).convert(TableGridResponse.class);
        }
        input.put("corpIdList", corpIdList);
        IPageResult<UserVO> pageResult = userDao.listUser(input, dictGroupIdForStatus.getId());
        return pageResult.convert(TableGridResponse.class);
    }

    private void licenseUserControl(User user) {
        if (UserConstant.STATUS_NORMAL.equals(user.getStatus())) {
            boolean controlRegister = licenseServiceImpl.isControlRegister();
            Integer controlUserNumber = licenseServiceImpl.getControlUserNumber();
            if (controlRegister && controlUserNumber > 0 && userDao.countUsefullUser() > controlUserNumber) {
                throw new BusinessException(UserMessageEnum.SYS_USER_MESSAGE_USER_NUMBER_MORE_THAN_LICENSE_NUMBER, controlUserNumber.toString());
            }
        }
    }

    public List<Map<String, Object>> listUserIdByEmployeeId(List<String> employeeIdList) {
        return userDao.listUserIdByEmployeeId(employeeIdList);
    }

    private void getLockAndCheck(String userId, Integer addNum){
        if(addNum < 1){
            throw new BusinessException("添加数据为空，请确认");
        }
        User user = userDao.get(userId);
        sysTenantManagerDao.updateByTenantId(user.getTenantId());
        SysTenantManager byTenantCode = sysTenantManagerService.getByTenantCode(user.getTenantId());
        if(byTenantCode == null){
            logger.error(String.format("通过租户id%s查询租户信息错误",user.getTenantId()));
            throw new BusinessException("租户不存在，请确认");
        }
        // 查询租户已存在用户数量
        long existUserCount = userDao.countUserByTenantId(user.getTenantId());
        // 本次操作后总用户数
        long expectTotal = existUserCount + addNum;
        // 租户用户上限
        Integer tenantUserLimit = byTenantCode.getUserNumber();
        // tenantUserLimit为null代表无上限，不做校验
        if (tenantUserLimit != null && tenantUserLimit < expectTotal) {
            throw new ParameterException(UserMessageEnum.SYS_TENANT_USER_NUM_OVER_LIMIT,
                    String.valueOf(existUserCount),
                    String.valueOf(addNum),
                    String.valueOf(tenantUserLimit));
        }
    }

    @Transactional
    public void save(User user, String createUserId, String dingTalkUserId, String weiXinUserId, String feishuUserId) {
        // 大参林同步员工无单位信息
        if (ObjectUtil.notEqual("DSL",createUserId) && !EmptyUtils.isNotEmpty(user.getCorp().getId())) {
            throw new BusinessException(UserMessageEnum.SYS_USER_MESSAGE_CORPID_NULL);
        }
        Corp corp = null;
        if (user.getCorp() != null && StringUtils.isNotEmpty(user.getCorp().getId())) {
            corp = corpService.get(user.getCorp().getId());
        }
        if (corp == null && user.getCorp() != null) {
            corp = corpService.getCorpByCode(user.getCorp().getCode());
        }
        if (user.getIsAdmin() == 1) {
            List<User> userAdminList = listUserAdminByCorpId();
            if (userAdminList.size() >= 2) {
                throw new BusinessException(UserMessageEnum.SYS_USER_NOT_ALLOW_DOUBLE_ADMIN, corp.getName(), user.getUsername());
            }
        }
        String loginName = user.getLoginName();
        User dbUser = getByLoginName(loginName);
        if (dbUser != null) {
            throw new BusinessException(UserMessageEnum.SYS_USER_LOGIN_NAME_WAS_EXIST);
        }
        licenseUserControl(user);
        String salt = DigestUtil.sha256Hex("000"+loginName);
        // 优先使用系统配置的初始化密码，未配置时使用统一默认值。
        String cryptPassword = getCryptPassword(salt);
        if (null == user.getSex()) {
            user.setSex(1);
        }
        user.setSalt(salt);
        user.setPassword(cryptPassword);
        user.setCreateBy(createUserId);
        user.setCreateTime(new Date());
        // 用户登录方式 ：0用户密码方式，1证书认证＋用户密码方式，2证书认证方式
        if (ObjectUtil.isEmpty(user.getLoginType())){
            user.setLoginType(0);
        }
        getLockAndCheck(createUserId, 1);
        saveEntity(user);

        this.getSession().flush();
        if (EmptyUtils.isNotEmpty(dingTalkUserId)) {
            accountMappingService.saveAccountMap(dingTalkUserId, user.getId(), AppH5Constants.ClientType.DING_TALK);
        }
        if (EmptyUtils.isNotEmpty(weiXinUserId)) {
            accountMappingService.saveAccountMap(weiXinUserId, user.getId(), AppH5Constants.ClientType.CORP_WEI_XIN);
        }
        if (EmptyUtils.isNotEmpty(feishuUserId)) {
            accountMappingService.saveAccountMap(feishuUserId, user.getId(), AppH5Constants.ClientType.FEISHU);
        }
    }

    public void update(User user, String rolesId, String updateUserId, String dingTalkUserId, String weiXinUserId, String feishuUserId) {
        Integer status = user.getStatus();
        if (!UserConstant.STATUS_NORMAL.equals(status) || !UserConstant.STATUS_FORBIDDEN.equals(status)) {
            //TODO:抛出错误异常
        }
        //判断是否存在相同的登录用户
        if (userDao.findUserByLoginNameNeUserId(user.getLoginName(), user.getId()) != null) {
            throw new BusinessException(UserMessageEnum.SYS_USER_LOGIN_NAME_WAS_EXIST);
        }
        licenseUserControl(user);
        String userId = user.getId();

        Corp corp = null;
        if (user.getCorp() != null && StringUtils.isNotEmpty(user.getCorp().getId())) {
            corp = corpService.get(user.getCorp().getId());
        }
        if (corp == null && user.getCorp() != null) {
            corp = corpService.getCorpByCode(user.getCorp().getCode());
        }
        User dbUser = get(userId);
        if (user.getIsAdmin() == 1 && dbUser.getIsAdmin() != 1) {
            List<User> userAdminList = listUserAdminByCorpId();
            if (userAdminList.size() >= 2) {
                throw new BusinessException(UserMessageEnum.SYS_USER_NOT_ALLOW_DOUBLE_ADMIN, corp.getName(), user.getUsername());
            }
        }

        //若启用事件大于现在，那么则更新
//        if (user.getBeginUseTime().getTime() > System.currentTimeMillis()) {
//            dbUser.setBeginUseTime(user.getBeginUseTime());
//        }
        dbUser.setLoginName(user.getLoginName());
        dbUser.setPhone(user.getPhone());
        dbUser.setEmail(user.getEmail());
        dbUser.setStatus(user.getStatus());
        dbUser.setLoginType(user.getLoginType());
        dbUser.setIsAdmin(user.getIsAdmin());
        dbUser.setUsername(user.getUsername());
        dbUser.setUpdateBy(updateUserId);
        dbUser.setUpdateTime(new Date());
        dbUser.setCorp(corp);
        dbUser.setShowReport(user.getShowReport());
        updateEntity(dbUser);

        accountMappingService.updateAccountMap(dingTalkUserId, user.getId(), AppH5Constants.ClientType.DING_TALK);
        accountMappingService.updateAccountMap(weiXinUserId, user.getId(), AppH5Constants.ClientType.CORP_WEI_XIN);
        accountMappingService.updateAccountMap(feishuUserId, user.getId(), AppH5Constants.ClientType.FEISHU);

        if (userFaceDetectionService.isOpenFaceVerify()) {
            // 如果系统开启了人脸复核，则更新人脸复核状态
            userFaceDetectionService.updateUserFaceVerifyStatus(user.getLoginName(), user.getFaceVerifyStatus());
        }
    }


    /**
     * 设置用户默认的特殊权限
     *
     * @param userId 用户id
     * @param corpId 网点单位id
     */
    @Transactional
    public void setUserSpecialPermission(String userId, String corpId) {
        //设置该用户该角色该单位下的默认权限 by周亮 2017-09-06 13:41
        StopWatch stopWatch = new StopWatch("setUserSpecialPermission");
        stopWatch.start("findUserMenuAndResourceByUserId");
        List<Map<String, Object>> userMenuAndResourceByUserId = menuService.findUserMenuAndResourceByUserId(userId);
        List<String> resourceList = new ArrayList<>();
        List<String> menuList = new ArrayList<>();
        stopWatch.stop();
        stopWatch.start("buildMenuAndResource");
        menuService.buildMenuAndResource(userMenuAndResourceByUserId, menuList, resourceList);
        stopWatch.stop();

        stopWatch.start("saveCorpUserResource");
        saveCorpUserResource(userId, corpId, resourceList, menuList);
        stopWatch.stop();
        logger.debug("setUserSpecialPermission all spends :"+stopWatch.prettyPrint());
    }

    /**
     * 锁定用户
     *
     * @param ids
     */
    @Transactional
    public void lock(List<String> ids) {
        userDao.lockOrUnLock(ids, UserConstant.STATUS_LOCK);
    }

    /**
     * 解锁用户
     *
     * @param ids
     */
    @Transactional
    public void unLock(List<String> ids) {
        userDao.lockOrUnLock(ids, UserConstant.STATUS_NORMAL);
    }

    /**
     * 修改密码
     *
     * @param id          用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void updatePassword(String id, String oldPassword, String newPassword) {
        User user = get(id, true);

        checkPassword(id, oldPassword);

        // zuoy 2017/11/15
        ConfigDTO configDTO = configServiceImpl.getConfig("sys", "passwordRepetitions");
        Integer maxResult = 3;
        if (!EmptyUtils.isEmpty(configDTO) && EmptyUtils.isNumeric(configDTO.getValue())) {
            maxResult = new Integer(configDTO.getValue());
        }
        List<UserHistoryPassword> historyPasswordList = this.userHistoryPasswordService.listOldThreePasswordByUserId(user.getId(), maxResult);
        if (!EmptyUtils.isEmpty(historyPasswordList)) {
            for (UserHistoryPassword userHistoryPassword : historyPasswordList) {
                if (userHistoryPassword.getPassword().equalsIgnoreCase(DigestUtil.md5Hex(newPassword + user.getSalt()))) {
                    throw new BusinessException(UserMessageEnum.SYS_USER_OLD_NEWPASSWORD_EQ_OLDTHREEPASSWORD, String.valueOf(maxResult));
                }
            }
        }
        String newPasswordSalt = DigestUtil.sha256Hex("000"+user.getLoginName());;
        String newPasswordHash = DigestUtil.md5Hex(newPassword + newPasswordSalt);
        user.setSalt(newPasswordSalt);
        user.setPassword(newPasswordHash);
        user.setUpdateTime(DateUtils.getDateTime());
        getEntityDao().saveOrUpdate(user);
        getEntityDao().flush();
        this.userHistoryPasswordService.saveUserHistoryPassword(user.getId(), newPassword+newPasswordSalt);
    }


    /**
     * 检测用户密码是否正确
     *
     * @param userId   用户ID
     * @param password 用户密码
     */
    public void checkPassword(String userId, String password) {
        User user = get(userId, true);
        //todo shiro不在使用
        String oldPasswordHash = DigestUtil.md5Hex(password + user.getSalt());
        if (!oldPasswordHash.equals(user.getPassword())) {
            throw new BusinessException(UserMessageEnum.SYS_USER_OLD_PASSWORD_NOT_CORRECT);
        }
    }

    /**
     * 重置密码
     *
     * @param ids
     */
    @Transactional
    public void resetPassword(List<String> ids) {
        for (String id : ids) {
            User user = get(id);
            String salt = new NewBCryptPasswordEncoder().getSalt();
            user.setPassword(getCryptPassword(salt));
            user.setSalt(salt);
            updateEntity(user);
        }
    }

    public  String getCryptPassword(String salt) {
        return DigestUtil.md5Hex(getInitialPassword() + salt);
    }

    /**
     * 取得初始化及重置使用的密码，显式配置优先，缺失或为空时使用默认值。
     *
     * @return 初始化密码明文，仅用于后续密码摘要计算，不写入日志
     */
    private String getInitialPassword() {
        ConfigDTO config = configServiceImpl.getConfig(ConfigConstant.SYS, "initPassword");
        String configuredPassword = config == null ? null : config.getValue();
        return StringUtils.isNotEmpty(configuredPassword) ? configuredPassword : DEFAULT_INITIAL_PASSWORD;
    }

    public  String getCryptBySaltAndPassword(String salt,String password) {
        String saltNew = "";
        if(StringUtils.isBlank(salt)) {
            saltNew = new NewBCryptPasswordEncoder().getSalt();
        }else {
            saltNew = salt;
        }
        Map<String,String> map =  new NewBCryptPasswordEncoder().encodePassword(password,saltNew);
        String encodePassword = DEFAULT_INITIAL_PASSWORD;
        if(map!=null && map.size()>0) {
            encodePassword = (String)map.get("encodePassword");//1.获取BCrypt加密
        }
        return DigestUtil.md5Hex(encodePassword);
    }

    public  Map<String,String> getCryptPasswordMap(String salt) {
        String initPassword = getInitialPassword();
        Map<String,String> map =  new NewBCryptPasswordEncoder().encodePassword(initPassword,salt);//返回password,salt
        return map;
    }

    @Transactional(readOnly = true)
    /**
     * 按登录名查询用户
     *
     * @param loginName
     * @return 用户对象
     */
    public User getUserByLoginName(String loginName) {
        return userDao.getUniqueBy("loginName", loginName);
    }

    /**
     * 按证书序列号取证书
     *
     * @param userCadn
     * @return 用户对象
     */
    public User getUserByLoginCadn(String userCadn) {
        return userDao.getUniqueBy("userCadn", userCadn);
    }

    /**
     * 删除用户，这里是假删除
     *
     * @param ids
     */
    public void delete(List<String> ids) {
        for (String id : ids) {
            User user = get(id);
            if (user.getIsAdmin() == 1) {
                List<User> userAdminList = listUserAdminByCorpId();
                if (userAdminList.size() < 2) {
                    throw new BusinessException(UserMessageEnum.SYS_USER_NOT_ALLOW_DOUBLE_ADMIN);
                }
            }
            user.setStatus(UserConstant.STATUS_DELETE);
            updateEntity(user);
            roleDao.deleteUserRoleByUserId(id);
        }
    }


    public List<CorpUserResource> listCorpUserResourceByUserId(String userId) {
        List<CorpUserResource> list = corpUserResourceDao.listByUserId(userId);
        return list;
    }

    /**
     * 根据用户ID，列出当前用户所在网点、单位的资源
     *
     * @param userId
     * @return
     */
    public List<String> listCorpUserResource(String userId, String corpId) {
        List<CorpUserResource> list = corpUserResourceDao.listByUserIdAndCorpId(userId, corpId);
        List<String> temp = new ArrayList<String>(list.size());
        for (CorpUserResource cur : list) {
            temp.add(cur.getResourceId());
        }
        return temp;
    }

    /**
     * 保存单位网点、用户、资源（特殊权限）
     *
     * @param userId
     * @param corpId
     * @param resources
     */
    public void saveCorpUserResource(String userId, String corpId, List<String> resources, List<String> menus) {
        StopWatch stopWatch = new StopWatch("saveCorpUserResource");
        stopWatch.start("deleteByCorpIdAndUserId");
        corpUserResourceDao.deleteByCorpIdAndUserId(userId, corpId);
        stopWatch.stop();
        if (!CollectionUtils.isEmpty(resources)) {
            int batchSize = CommonConstant.HUNDRED;
            int resourceNum = resources.size();
            int startIndex = 0;
            int endIndex = 0;
            while(startIndex <= resourceNum - 1){
                endIndex = startIndex + batchSize;
                if(endIndex > resourceNum){
                    endIndex = resourceNum;
                }
                stopWatch.start("saveBatchCorpUserResourceByReourceIds startIndex:"+startIndex);
                corpUserResourceDao.saveBatchCorpUserResourceByReourceIds(userId,corpId,resources.subList(startIndex,endIndex));
                corpUserResourceDao.flush();
                stopWatch.stop();
                startIndex = startIndex + batchSize;
            }
        }
        String[] permissions = resourcesDao.listResourceByUserIdNew(userId).stream().map(ResourcesSimpleVO::getPermission).toArray(String[]::new);
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.RESOURCES_PERMISSION_PREFIX, userId);
        if(!ObjectUtils.isEmpty(permissions)) {
            redisTemplate.delete(key);
            logger.info("删除redis缓存权限=========="+key);
            redisTemplate.opsForSet().add(key, permissions);//刷新redis缓存权限
            logger.info("刷新redis缓存权限=========="+permissions);
            logger.debug("saveCorpUserResource all spends :"+stopWatch.prettyPrint());
        }

    }

    /**
     * 分配角色
     *
     * @param userId 用户ID
     * @param ids    角色ID集合
     */
    public void assignRoles(String userId, List<String> ids) {
        StopWatch stopWatch = new StopWatch("assignRoles");
        stopWatch.start("deleteUserRoleByUserId:");
        User user = get(userId);
        // 如果未分配单位，需要先分配单位信息
        if (ObjectUtil.isEmpty(user.getCorp())){
            throw new BusinessException("用户所属单位信息不能为空，请先设置所属单位信息！");
        }
        roleDao.deleteUserRoleByUserId(userId);
        stopWatch.stop();
        stopWatch.start("loop:");
        if (!EmptyUtils.isEmpty(ids)) {
            //角色不为空则设置权限
            for (String roleId : ids) {
                user.getUserRoles().add(new UserRole(user, roleDao.get(roleId)));
            }
            //强制将数据同步到数据库
            getSession().flush();
        }
        stopWatch.stop();
        stopWatch.start("setUserSpecialPermission:");
        setUserSpecialPermission(user.getId(), user.getCorp().getId());
        stopWatch.stop();
    }

    /**
     * 拷贝单位网点、用户、资源
     *
     * @param userId
     * @param sourceCorpId
     * @param corps
     */
    @Transactional
    public void copyCorpUserResource(String userId, String sourceCorpId, List<String> corps) {
//        List<CorpUserResource> list = corpUserResourceDao.listByUserIdAndCorpId(userId, sourceCorpId);
//        List<String> resources = new ArrayList<>();
//        for (CorpUserResource corpUserResource : list) {
//            if (StringUtils.isNotBlank(corpUserResource.getResourceId())) {
//                resources.add(corpUserResource.getResourceId());
//            }
//        }
//
//        List<String> menus = new ArrayList<>();
//        for (CorpUserResource corpUserResource : list) {
//            if (StringUtils.isNotBlank(corpUserResource.getMenuId())) {
//                menus.add(corpUserResource.getMenuId());
//            }
//        }
//
//        for (String corpId : corps) {
//            saveCorpUserResource(userId, corpId, resources, menus);
//        }
        //修改批量删除和批量新增模式
        corpUserResourceDao.deleteByCorpIdsAndUserId(userId, corps);
//        for (String corpId : corps) {
//            corpUserResourceDao.saveBatchCorpUserResource(userId, sourceCorpId, corpId);
//        }
        corpUserResourceDao.saveBatchCorpUserResource(userId, sourceCorpId, corps);
        String key = RedisCacheKey.makeRedisKey(RedisCacheKey.RESOURCES_PERMISSION_PREFIX, userId);
        redisTemplate.delete(key);
        logger.info("拷贝单位网点、用户、资源, 删除redis缓存权限=========="+key);
    }

    /**
     * 角色复制，根据用户的ID集合复制
     *
     * @param userId 从该用户复制资源的用户ID
     * @param ids    被复制用户ID集合
     */
    @Transactional
    public void copyRoles(String userId, List<String> ids) {
        User sourceUser = get(userId);
        Set<UserRole> roles = sourceUser.getUserRoles();

        for (String id : ids) {
            //先删除用户与角色的关系
            roleDao.deleteUserRoleByUserId(id);
            User user = get(id);
            for (UserRole userRole : roles) {
                //再建立用户和角色的关系
                user.getUserRoles().add(new UserRole(user, userRole.getRole()));
            }
        }
        //update by zhoul 2018-05-02，将特殊权限的设置放在最后
        //强制将数据同步到数据库
        getSession().flush();
        for (String id : ids) {
            User user = get(id);
            //设置用户默认特殊权限
            setUserSpecialPermission(id, user.getCorp().getId());
        }
    }

    /**
     * 根据单位ID和资源代码获取对应的用户信息
     *
     * @param corpId
     * @param resourceCode
     */
    public List<Map<String, String>> loadUserByCorpIdOrResource(String corpId, String resourceCode) {
        List<UserVO> list = userDao.loadUserByCorpIdOrResource(corpId, resourceCode);
        List<Map<String, String>> temp = new ArrayList<>();
        if (EmptyUtils.isEmpty(list)) {
            return temp;
        }
        for (UserVO user : list) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("text", "(" + user.getLoginName() + ")" + user.getUsername());
            userInfo.put("value", user.getId());
            temp.add(userInfo);
        }
        return temp;
    }

    public List<User> listByCorpId(String corpId) {
        return userDao.listByCorpId(corpId);
    }


    public List<User> listUserAdminByCorpId() {
        return userDao.listByUserAdmin();
    }


    public List<UserVO> listByUserIdsSimple(List<String> ids) {
        List<User> userList = userDao.listByUserIds(ids);
        List<UserVO> userVOList = new ArrayList<>();
        if (!EmptyUtils.isEmpty(userList)) {
            for (User user : userList) {
                UserVO userVO = BeanUtils.copyNewInstanceProperties(user, UserVO.class);
                Corp corp = user.getCorp();
                userVO.setCorpId(corp.getId());
                userVO.setCorpName("(" + corp.getCode() + ")" + corp.getName());
                userVOList.add(userVO);
            }
        }
        return userVOList;
    }

    public List<UserVO> listByUserIdsSimple2(List<String> ids) {
        List<User> userList = userDao.listByUserIds(ids);
        List<UserVO> userVOList = BeanUtils.copyListProperties(userList, UserVO.class);
        return userVOList;
    }

    /**
     * 根据用户ID获取登录名
     *
     * @param userIds 用户ID
     * @return 登录名
     */
    public List<String> listLoginNameByUserIds(List<String> userIds) {
        return userDao.listLoginNameByUserIds(userIds);
    }

    public List<String> listUserIdsByRoleId(List<String> rolesId) {
        return userDao.listUserIdsByRoleId(rolesId);
    }

    public List<String> listUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId) {
        return userDao.listUserIdsByRoleIdAndCorpId(rolesId, corpId);
    }

    public List<String> listApproveUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId, List<String> resourceContent) {
        return userDao.listApproveUserIdsByRoleIdAndCorpId(rolesId, corpId, resourceContent);
    }

    public List<String> listApproveUserIdsByRoleIdAndCorpIdNew(List<String> rolesId, String corpId, List<String> resourceContent) {
        return userDao.listApproveUserIdsByRoleIdAndCorpIdNew(rolesId, corpId, resourceContent);
    }

    /**
     * 获得具有指定单位，指定权限的userId
     *
     * @param corpId
     * @param permissions
     * @param userIdRange 不为空时，返回的userIdList属于传入的userIdRange里面
     * @return userIdRange 不为空时，返回的userIdList属于传入的userIdRange的子集
     */
    public List<String> listUserIdsByUserIdAndCorpIdAndPermission(String corpId, List<String> permissions, List<String> userIdRange) {
        return userDao.listUserIdsByUserIdAndCorpIdAndPermission(corpId, permissions, userIdRange);
    }

    /**
     * 获得具有单位，指定权限的userId，
     *
     * @param corpIds
     * @param permission
     * @return
     */
    public List<String> listUserIdsByCorpIdAndPermission(List<String> corpIds, String permission) {
        return userDao.listUserIdsByCorpIdAndPermission(corpIds, permission);
    }

    /**
     * 根据用户ids得到用户
     *
     * @param ids
     * @return
     */
    public List<User> listUserByUserIds(List<String> ids) {
        return userDao.listByUserIds(ids);
    }

    /**
     * 获取具有在指定单位下，有指定权限的所有有效的userId
     *
     * @param corpId
     * @param permission
     * @return
     */
    public List<String> listUserIdsByUserIdAndCorpIdAndPermission(String corpId, List<String> permission) {
        return userDao.listUserIdsByUserIdAndCorpIdAndPermission(corpId, permission, null);
    }

    public User getByLoginName(String code) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("loginName", code));
        List list = criteria.list();
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }else if (list.size() > 1) {
            logger.error("用户名【{}】有重复数据【{}】，请联系管理员删除当前用户重复数据！", code, list.size());
        }
        return (User) list.get(0);
    }

    public User getByLoginName(String loginName, String tenantId) {
        // Criteria criteria = createCriteria();
        // criteria.add(Restrictions.eq("loginName", loginName));
        // criteria.add(Restrictions.eq("tenantId", tenantId));
        //
        // criteria.setFetchMode("corp", FetchMode.JOIN);   // 联表查询 corp
        // criteria.setFetchMode("userRoles", FetchMode.JOIN); // 联表查询角色
        //
        // List list = criteria.list();
        // 转为原生SQL查询
        String sql = "select * from sys_user where login_name = :loginName and tenant_id = :tenantId ";
        Map<String, Object> params = new HashMap<>();
        params.put("loginName", loginName);
        params.put("tenantId", tenantId);
        List<UserVO> list = getEntityDao().listBySQLAliasToBean(sql, params, UserVO.class);
        if (EmptyUtils.isEmpty(list)) {
            return null;
        } else if (list.size() > 1) {
            logger.error("租户号【{}】下用户名【{}】有重复数据【{}】，请联系管理员删除当前用户重复数据！", tenantId, loginName, list.size());
        }
        UserVO userVO = list.get(0);
        User user = BeanUtils.copyNewInstanceProperties(userVO, User.class);
        user.setTenantId(tenantId);
        if (ObjectUtil.isNotEmpty(userVO.getCorpId())) {
            Corp corp = corpService.getCropById(userVO.getCorpId(), tenantId);
            user.setCorp(corp);
        }
        return user;
    }

    public User getByUserName(String username) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("username", username));
        return (User) criteria.uniqueResult();
    }

    public User getByEmployeeId(String employeeId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("employeeId", employeeId));
        return (User) criteria.uniqueResult();
    }

    public User getByMobile(String mobile) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("phone", mobile));
        return (User) criteria.uniqueResult();
    }

    public List<User> listByLoginName(List<String> loginNameList) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.in("loginName", loginNameList));
        return criteria.list();
    }


    /**
     * 删除证书绑定
     *
     * @param id
     * @return
     */
    @Transactional
    public void caDel(String id) {
        User user = userDao.get(id);
        user.setUserCadn("");
        user.setLoginType(0);
        userDao.update(user);
    }

    /**
     * 证书绑定
     *
     * @param id
     * @return
     */
    @Transactional
    public void caBinding(String id, String userCadn) {
        User t = this.getUserByLoginCadn(userCadn);
        //判断证书是否已经被绑定
        if (!EmptyUtils.isEmpty(t)) {
            throw new BusinessException(LoginMessageEnum.SYS_CA_CABINDING);
        }
        User user = userDao.get(id);
        user.setUserCadn(userCadn);
        user.setLoginType(2);
        userDao.update(user);
    }

    /**
     * 获取用户的登录方式
     *
     * @param loginName 用户登录名称
     * @return -1用户不存在，0用户密码，1银行密码+证书，2证书
     */
    @Transactional(readOnly = true)
    public String getUserLoginType(String loginName) {
        String userLoginType = "-1";
        if (!StringUtils.isEmpty(loginName)) {
            User user = getByLoginName(loginName);
            userLoginType = getUserLoginType(user);
        }
        return userLoginType;
    }

    /**
     * 获取用户的登录方式。
     *
     * @param loginName 用户登录名称。
     * @param tenantId 租户ID。
     * @return 登陆方式。
     */
    @Transactional(readOnly = true)
    public String getUserLoginType(String loginName, String tenantId) {
        String userLoginType = "-1";
        if (ObjectUtil.isAllNotEmpty(loginName, tenantId)) {
            User user = getByLoginName(loginName, tenantId);
            userLoginType = getUserLoginType(user);
        }
        return userLoginType;
    }

    /**
     * 获取用户的登录方式
     *
     * @param user 用户信息
     * @return -1用户不存在，0用户密码，1银行密码+证书，2证书
     */
    public String getUserLoginType(User user) {
        String userLoginType = "-1";
        if (user != null) {
            String encrypt = configServiceImpl.getGlobalConfig(ConfigConstant.SYS, SysConfigConstant.ENCRYPT).toString();
            //如果证书类型选择  0不使用证书则默认用户密码登入
            if (!"0".equals(encrypt)) {
                String defaultLoginType = configServiceImpl.getGlobalConfig(ConfigConstant.SYS, SysConfigConstant.LOGINTYPE).toString();
                //用户未配置个性化登录模式根据系统登录方式设置
                if (user.getLoginType() == null) {
                    userLoginType = defaultLoginType;
                } else {
                    //获取自己的登录方式
                    userLoginType = user.getLoginType().toString();
                }
            } else {
                userLoginType = "0";
            }
        }
        return userLoginType;
    }

    public List<UserVO> listApproveUserByCorpId(String corpId, String resourceContent) {
        List<String> userIdList = listUserIdsByUserIdAndCorpIdAndPermission(corpId,Lists.newArrayList(SysResourceConstant.SYS_FLOW_APPROVE,resourceContent) );
        return listByUserIdsSimple(userIdList);
    }

    /**
     * 根据资源查找具有该资源权限的用户id
     *
     * @param permission 权限代码   eg:netbank:bankAcc:paymentBlending:query
     * @return
     */
    public List<String> listUserIdsByPermission(String permission) {
        return userDao.listUserIdsByPermission(permission);
    }

    public String getSaltByLoginName(String loginName) {
        return userDao.getSaltByLoginName(loginName);
    }

    public String getUserCodeByCadn(String cadn) {
        return userDao.getUserCodeByCadn(cadn);
    }



    /**
     * 权限复制
     *
     * @param permissionsTypes
     * @param copyType
     * @param userIds
     * @param sourceUserId
     */
    public void copyPermissions(String[] permissionsTypes, String copyType, List<String> userIds, String sourceUserId) {
        for (String permissionsType : permissionsTypes) {
            if (UserConstant.PERMISSIONS_TYPE_ACC.equals(permissionsType)) {
//              账号权限复制
                corpAccountPermissions(sourceUserId, userIds, copyType);
            } else if (UserConstant.PERMISSIONS_TYPE_ROLE.equals(permissionsType)) {
//              角色权限复制
                copyRoles(sourceUserId, userIds, copyType);
            } else if (UserConstant.PERMISSIONS_TYPE_SPECIAL.equals(permissionsType)) {
//              特殊权限复制
                copySpecial(sourceUserId, userIds, copyType);
            }
        }
    }


    /**
     * 角色复制，根据用户的ID集合复制
     *
     * @param userId 从该用户复制资源的用户ID
     * @param ids    被复制用户ID集合
     */
    @Transactional
    public void copyRoles(String userId, List<String> ids, String copyType) {
        User sourceUser = get(userId);
        Set<UserRole> roles = sourceUser.getUserRoles();
        for (String id : ids) {
//                替换复制，否则为新增复制
            if (UserConstant.COPY_TYPE_REPLACE.equals(copyType)) {
                // 先删除用户与角色的关系
                roleDao.deleteUserRoleByUserId(id);
            }
            User user = get(id);
            for (UserRole userRole : roles) {
                //再建立用户和角色的关系
                user.getUserRoles().add(new UserRole(user, userRole.getRole()));
            }
        }
        //update by zhoul 2018-05-02，将特殊权限的设置放在最后
        //强制将数据同步到数据库
        getSession().flush();
        for (String id : ids) {
            User user = get(id);
            //设置用户默认特殊权限
            setUserSpecialPermission(id, user.getCorp().getId());
        }
        getSession().flush();
    }


    /**
     * 特殊权限复制
     *
     * @param userId   目标用户ID
     * @param ids      需要复制的用户ID
     * @param copyType 拷贝类型 1为新增，2为替换
     */
    public void copySpecial(String userId, List<String> ids, String copyType) {
        List<CorpUserResource> corpUserResources = new ArrayList<>();
        for (String id : ids) {
            User user = get(id);
            CorpUserResource corpUserResource = new CorpUserResource();
            corpUserResource.setUserId(id);
            corpUserResource.setCorpId(user.getCorp().getId());
            corpUserResources.add(corpUserResource);
        }
        //替换复制，否则为新增复制
        if (UserConstant.COPY_TYPE_REPLACE.equals(copyType)) {
            // 替换复制则先删除本身特殊权限
            corpUserResourceDao.deleteByCorpUser(corpUserResources);
        }
        corpUserResourceDao.copyCorpUserResource(userId, corpUserResources);
    }


    /**
     * 账号权限复制
     *
     * @param userId
     * @param ids
     * @param copyType
     */
    public void corpAccountPermissions(String userId, List<String> ids, String copyType) {
        if (UserConstant.COPY_TYPE_ADD.equals(copyType)) {
            for (String id : ids) {
//                账号不同部分直接插入
                saveAddAccountGrant(accountGrantDao.getAccoutnGrantByUser(userId, id, "1"), ids);
                List<AccountGrant> srcAccountGrants = accountGrantDao.getAccoutnGrantByUser(userId, id, "0");
                List<AccountGrant> tarAccountGrants = accountGrantDao.getAccoutnGrantByUser(id, userId, "0");
                if (tarAccountGrants != null && tarAccountGrants.size() > 0) {
                    //                账号相同部分，则判断后更新
                    Map<String, AccountGrant> srcAccGrantMap = Maps.uniqueIndex(srcAccountGrants, new Function<AccountGrant, String>() {
                        @Override
                        public String apply(AccountGrant accountGrant) {
                            return accountGrant.getAccountId();
                        }
                    });
//                    新增复制读写转账权限
                    for (AccountGrant tarAccountGrant : tarAccountGrants) {
                        AccountGrant srcAccGrant = srcAccGrantMap.get(tarAccountGrant.getAccountId());
                        if (1 == srcAccGrant.getCanQueryBalance()) {
                            tarAccountGrant.setCanQueryBalance(srcAccGrant.getCanQueryBalance());
                        }
                        if (1 == srcAccGrant.getCanQueryDetail()) {
                            tarAccountGrant.setCanQueryDetail(srcAccGrant.getCanQueryDetail());
                        }
                        if (1 == srcAccGrant.getCanTransferAccounts()) {
                            tarAccountGrant.setCanTransferAccounts(srcAccGrant.getCanTransferAccounts());
                        }
                    }
                    accountGrantDao.batchUpdate(tarAccountGrants);
                }
            }
        } else {
            List<AccountGrant> accountGrants = accountGrantDao.listAssigned(null, null, userId);
            saveAccountGrant(accountGrants, ids);
        }
    }

    /**
     * 保存授权数据
     *
     * @param accountGrants
     * @param ids
     */
    public void saveAddAccountGrant(List<AccountGrant> accountGrants, List<String> ids) {
        List<AccountGrant> accountGrantNews = new ArrayList<>();
        for (String id : ids) {
            for (AccountGrant accountGrant : accountGrants) {
                AccountGrant accountGrant1New = new AccountGrant();
                BeanUtils.copyNotNullProperties(accountGrant, accountGrant1New);
                accountGrant1New.setUserId(id);
                accountGrant1New.setId(null);
                accountGrantNews.add(accountGrant1New);
            }
            accountGrantDao.batchSave(accountGrantNews);
            accountGrantNews.clear();
        }
    }

    /**
     * 保存授权数据 ，根据userid删除数据
     *
     * @param accountGrants
     * @param ids
     */
    public void saveAccountGrant(List<AccountGrant> accountGrants, List<String> ids) {
        List<AccountGrant> accountGrantNews = new ArrayList<>();
        for (String id : ids) {
            for (AccountGrant accountGrant : accountGrants) {
                AccountGrant accountGrant1New = new AccountGrant();
                BeanUtils.copyNotNullProperties(accountGrant, accountGrant1New);
                accountGrant1New.setUserId(id);
                accountGrant1New.setId(null);
                accountGrantNews.add(accountGrant1New);
            }
            accountGrantDao.deleteByUserId(id);
            accountGrantDao.batchSave(accountGrantNews);
            accountGrantNews.clear();
        }
    }

    /**
     * 获取除删除以外所有用户
     *
     * @return
     */
    public List<UserVO> listAllUserByUnDelete() {
        return userDao.listAllUserByUnDelete();
    }

    public List<UserVO> listUserByParam(UserVO userVO) {
        return userDao.listUserByParam(userVO);
    }

    // 新增大数据量导出方法（使用SXSSFWorkbook）
    public SXSSFWorkbook exportUserLarge(List<UserVO> list) {
        // 创建SXSSFWorkbook，设置行访问窗口为100
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        Sheet sheet = workbook.createSheet("用户管理");

        this.setColumnWidth(sheet);

        String[] head = new String[]{"账号", "姓名", "所属单位", "状态", "是否管理员"};

        // 创建表头
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < head.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(head[i]);
            cell.setCellStyle(headerStyle);
        }

        // 创建内容样式
        CellStyle contentStyle = createContentStyle(workbook);

        // 分批写入数据
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                UserVO user = list.get(i);
                Row row = sheet.createRow(i + 1);

                createCell(row, 0, user.getLoginName(), contentStyle);
                createCell(row, 1, user.getUsername(), contentStyle);
                createCell(row, 2, user.getCorpName(), contentStyle);

                String statusText = "";
                if (!EmptyUtils.isEmpty(user.getStatus())) {
                    String status = user.getStatus().toString();
                    statusText = StringUtils.equals(status, "1") ? "正常" : StringUtils.equals(status, "2") ? "禁用" : StringUtils.equals(status, "3") ? "锁定" : StringUtils.equals(status, "3") ? "已删除" : "";
                }

                createCell(row, 3, statusText, contentStyle);

                String isAdmin = "否";
                if (user.getIsAdmin() != null && "1".equals(user.getIsAdmin().toString())) {
                    isAdmin = "是";
                }
                createCell(row, 4, isAdmin, contentStyle);

                // 每处理1000行手动清理一次内存
                if (i % 1000 == 0) {
                    try {
                        ((SXSSFSheet) sheet).flushRows(100);
                    } catch (IOException e) {
                        // 日志记录
                    }
                }
            }
        }
        return workbook;
    }

    /**
     * 设置列宽
     *
     * @param sheet  sheet页
     */
    private void setColumnWidth(Sheet sheet) {
        // 设置单元格默认的列宽
        sheet.setDefaultColumnWidth((short) 20);
        // 自定义列宽
        sheet.setColumnWidth(0, 20 * 256);//设置第一列列宽
        sheet.setColumnWidth(1, 20 * 256);//设置第二列列宽
        sheet.setColumnWidth(2, 50 * 256);//设置第三列列宽
        sheet.setColumnWidth(3, 15 * 256);//设置第四列列宽
        sheet.setColumnWidth(4, 15 * 256);//设置第五列列宽
    }

    // 创建表头样式（适配SXSSFWorkbook）
    private CellStyle createHeaderStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    // 创建内容样式（适配SXSSFWorkbook）
    private CellStyle createContentStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    // 创建单元格
    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }

    /**
     * 获取有效的用户
     *
     * @return
     */
    public List<UserVO> listAllUser() {
        return userDao.listAllUser();
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @param currentUser
     */
    @Transactional
    public void saveImport(List<UserVO> list, ICurrentUser currentUser) {
        String userId = currentUser.getId();
        Date curDate = DateUtils.getDate();
        String salt = new NewBCryptPasswordEncoder().getSalt();
        String cryptPassword = getCryptPassword(salt);
        List<User> entityList = new ArrayList<>();
        Map<String, Corp> map = Maps.newHashMap();
        List<String> ids = new ArrayList<>();
        for (UserVO userVO : list) {
            ids.add(userVO.getCorpId());
        }
        List<Corp> corpList = corpService.listByIds(ids);
        for (Corp corp : corpList) {
            map.put(corp.getId(), corp);
        }
        for (UserVO vo : list) {
            User user = new User();
            BeanUtils.copyNotNullProperties(vo, user);
            if (EmptyUtils.isEmpty(user.getStatus())) {
                user.setStatus(UserConstant.STATUS_NORMAL);
            }
            if (EmptyUtils.isEmpty(user.getIsAdmin())) {
                user.setIsAdmin(UserConstant.UN_ADMIN);
            }
            if (null == user.getSex()) {
                user.setSex(1);
            }
            user.setCorp(map.get(vo.getCorpId()));
            user.setPassword(cryptPassword);
            user.setSalt(salt);
            user.setCreateBy(userId);
            user.setCreateTime(curDate);
            entityList.add(user);
        }
        getLockAndCheck(userId, entityList.size());
        batchSave(entityList);
    }

    @Override
    public ExcelVerifyHanlderResult verifyHandler(UserVO obj) {
        // 判断是否双管理员拦截
        if (obj != null && UserConstant.ADMIN.equals(obj.getIsAdmin())) {


            List<User> userAdminList = listUserAdminByCorpId();
            if (userAdminList.size() >= 2) {
                String corpName = obj.getCorpName() == null ? CommonConstant.EMPTY_STR : obj.getCorpName();
                String username = obj.getUsername() == null ? CommonConstant.EMPTY_STR : obj.getUsername();
                String errorMsg = multiLangServiceI.getLang(
                        UserMessageEnum.SYS_USER_NOT_ALLOW_DOUBLE_ADMIN.name()
                                .replaceAll(CommonConstant.UNDERLINE, CommonConstant.DOT).toLowerCase(),
                        corpName, username);
                return new ExcelVerifyHanlderResult(false, Collections.singletonList(errorMsg));
            }
        }
        return null;
    }

    @Override
    public void verifyHandler(List<UserVO> list) {
        if (!EmptyUtils.isEmptyList(list)) {
            List<String> msgList = null;
            String statusLang = "";
            String isAdminLang = "";
            String unAdminLang = multiLangServiceI.getLang("common.sc_yn.no");
            Corp corp = null;
            // 控制用户数量
            boolean controlRegister = licenseServiceImpl.isControlRegister();
            Integer controlUserNumber = licenseServiceImpl.getControlUserNumber();
            if (controlUserNumber == null) {
                controlUserNumber = 1000;
            }
            // 当前用户数量
            long countUsefullUser = userDao.countUsefullUser();
            long countUser = countUsefullUser + list.size();
            long count = countUsefullUser;
            Map<String, Corp> corpMap = Maps.newHashMap();
            Map<String, String> adminMap = Maps.newHashMap();
            Map<String, String> statusMap = Maps.newHashMap();
            List<Corp> corpList = corpService.listAll();
            statusMap.put("0", dictServiceI.get("sys.user.status", "0"));
            statusMap.put("1", dictServiceI.get("sys.user.status", "1"));
            adminMap.put("yes", multiLangServiceI.getLang("common.sc_yn.yes"));
            adminMap.put("no", multiLangServiceI.getLang("common.sc_yn.no"));

            String lang = multiLangServiceI.getLang("sys.user.message.user.number.more.than.license.number", controlUserNumber.toString());
            List<String> loginNameList = new ArrayList<>();
            for (UserVO userVO : list) {
                loginNameList.add(userVO.getLoginName());
            }
            List<User> userList = this.listAll();
            Map<String, Boolean> userMap1 = new HashMap<>();
            for (User user1 : userList) {
                userMap1.put(user1.getLoginName(), Boolean.TRUE);
            }
            for (UserVO userVO : list) {
                if (userMap1.containsKey(userVO.getLoginName())) {
                    userMap1.put(userVO.getLoginName(), Boolean.FALSE);
                    continue;
                }
                userMap1.put(userVO.getLoginName(), Boolean.TRUE);
            }
            for (Corp corp1 : corpList) {
                corpMap.put(corp1.getName(), corp1);
            }
            for (UserVO vo : list) {
                msgList = new ArrayList<>();
                // 用户数超过license控制
                count++;
                if (controlRegister && controlUserNumber > 0 && countUser > controlUserNumber && count > controlUserNumber) {
                    msgList.add(lang);
                }
                // 用户名
                if (!EmptyUtils.isBlank(vo.getLoginName())) {
                    // 用户已存在
                    if (!userMap1.get(vo.getLoginName())) {
                        msgList.add("sys.user.login.name.was.exist");
                    }
                }
                // 单位名称
                if (!EmptyUtils.isBlank(vo.getCorpName())) {
                    // 单位不存在
                    corp = corpMap.get(vo.getCorpName());
                    if (corp == null) {
                        msgList.add("sys.user.message.corpinfo.not.exist");
                    } else {
                        vo.setCorpId(corp.getId());
                    }
                }
                // 状态
                if (!EmptyUtils.isEmpty(vo.getStatusLang())) {
                    statusLang = statusMap.get(vo.getStatusLang());
                    if (EmptyUtils.isBlank(statusLang)) {
                        // 状态格式不正确
                        msgList.add("sys.user.message.status.format");
                    } else {
                        vo.setStatus(Integer.parseInt(vo.getStatusLang()));
                        vo.setStatusLang(statusLang);
                    }
                }
                // 是否管理员
                if (!EmptyUtils.isEmpty(vo.getIsAdminLang())) {
                    isAdminLang = UserConstant.ADMIN.toString().equals(vo.getIsAdminLang()) ?
                            adminMap.get("yes") : adminMap.get("no");
                    vo.setIsAdmin(Integer.parseInt(vo.getIsAdminLang()));
                    vo.setIsAdminLang(isAdminLang);
                } else {
                    vo.setIsAdmin(UserConstant.UN_ADMIN);
                    vo.setIsAdminLang(unAdminLang);
                }
                // 联系电话
                if (!EmptyUtils.isEmpty(vo.getPhone())) {
                    String phoneRegex = "^1\\d{10}$";
                    Pattern phonePattern = Pattern.compile(phoneRegex);
                    if (!phonePattern.matcher(vo.getPhone()).matches()) {
                        msgList.add("sys.form.verify.phoneError");
                    }
                }
                // EMAIL
                if (!EmptyUtils.isEmpty(vo.getEmail())) {
                    if (vo.getEmail().length() > 50) {
                        msgList.add("邮件最大长度不能超过50");
                    } else {
                        String emailRegex = "^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$";
                        Pattern emailPattern = Pattern.compile(emailRegex);
                        if (!emailPattern.matcher(vo.getEmail()).matches()) {
                            msgList.add("sys.form.verify.emailError");
                        }
                    }
                }
                vo.addErrorMsg(msgList);
            }
        }
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDelete(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<>();
            //获取有效的用户
            int num = userDao.getValidUserNumByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.SYS,
                        SysConfigConstant.BusType.SYS_USER,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.SYS_USER_VALIDUSER_ERROR)));
            }
            //获取是否有分配特殊授权的用户
            num = userDao.getCorpAuthNumByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.SYS,
                        SysConfigConstant.BusType.SYS_USER,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.SYS_USER_VALIDUSERAUTH_ERROR)));
            }
            if (verifyErrorDTOS.size() > 0) {
                return verifyErrorDTOS;
            }
        }
        return null;
    }

    @Override
    public List<CorpVerifyErrorDTO> verifyCorpDisable(String corpId) {
        if (StringUtils.isNotEmpty(corpId)) {
            List<CorpVerifyErrorDTO> verifyErrorDTOS = new ArrayList<>();
            //获取有效的用户
            int num = userDao.getValidUserNumByCorpId(corpId);
            if (num > 0) {
                verifyErrorDTOS.add(new CorpVerifyErrorDTO(corpId,
                        SysConfigConstant.Module.SYS,
                        SysConfigConstant.BusType.SYS_USER,
                        multiLangServiceI.getLang(CorpVerifyMessageConstant.SYS_USER_VALIDUSER_DISABLE_ERROR)));
            }
            if (verifyErrorDTOS.size() > 0) {
                return verifyErrorDTOS;
            }
        }
        return null;
    }

    public List<String> listUserIdByMenuId(String menuId) {
        return userDao.listUserIdByMenuId(menuId);
    }

    public void updateRecover(String id) {
        User user = get(id);
        user.setStatus(UserConstant.STATUS_NORMAL);
        licenseUserControl(user);
        //恢复时加入校验
        getLockAndCheck(id, 1);
        updateEntity(user);
    }

    public void updateLock(String id){
        User user = get(id);
        user.setStatus(UserConstant.STATUS_LOCK);
        licenseUserControl(user);
        updateEntity(user);
    }

    /**
     * 修改密码
     *
     * @param user        当前用户
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void modifyPasswordPost(ICurrentUser user, String oldPassword, String newPassword) {
        Cache tenMinute = cacheManager.getCache("tenMinute");
        //todo sessionId
        Serializable sessionId = "";
        String loginSaltKey = "loginSalt-" + sessionId;
        Cache.ValueWrapper valueWrapper = tenMinute.get(loginSaltKey);
        Object loginSalt = valueWrapper.get();
        tenMinute.evict(loginSaltKey);
        if (loginSalt != null) {
            Map<String, String> loginSaltMap = (Map<String, String>) loginSalt;
            String key = loginSaltMap.get("key");
            String iv = loginSaltMap.get("iv");
            oldPassword = decrypt(oldPassword, key, iv);
            newPassword = decrypt(newPassword, key, iv);
        }
        this.updatePassword(user.getId(), oldPassword, newPassword);
    }

    public String decrypt(String content, String key, String iv) {
        byte[] bytes = Base64.decodeBase64(content);
        return new String(aesCbcDecrypt(bytes, key.getBytes(), iv.getBytes()));
    }

    private static byte[] aesCbcDecrypt(byte[] content, byte[] keyBytes, byte[] iv) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 修改系统肤色
     *
     * @param userId 用户ID
     * @param sysSkin  状态
     */
    public void modifySysSkin(String userId, String sysSkin) {
        userDao.modifySysSkin(userId, sysSkin);
        String hashKey = RedisCacheKey.makeRedisKey("bytter-system", RedisCacheKey.USER_SYS_SKIN);
        //更改皮肤设置到缓存中
        redisTemplate.opsForHash().put(hashKey, userId, sysSkin);
    }

    /**
     * 查找有效的管理员
     *
     * @return
     */
    public List<String> listUserAdminByCorpIds(List<String> corpIds) {
        return this.userDao.listUserAdminByCorpIds(corpIds);
    }

    /**
     * 根据用户手机号获取用户
     * @param mobile 手机号
     *
     * @return
     */
    public User getUserByMobile(String mobile) {
        return userDao.findUserByMobile(mobile);
    }

    /**
     * 根据传入的 loginNames，返回 k=loginName, v=username
     * @param loginNames
     * @return
     */
    public Map<String, String> getUserNameByLoginNames(List<String> loginNames) {
        List<User> lists = userDao.getUserNameByLoginNames(loginNames);
        return lists.stream().collect(Collectors.toMap(User::getLoginName,User::getUsername));
    }

    public Map<String, String> getLoginNameByIds(List<String> ids) {
        List<User> lists = userDao.getLoginNameByIds(ids);
        return lists.stream().collect(Collectors.toMap(User::getId,User::getLoginName));
    }

    public Map<String, User> listUserByIdsGpId(List<String> ids) {
        List<User> lists = userDao.getLoginNameByIds(ids);
        return lists.stream().collect(Collectors.toMap(IdEntity::getId, java.util.function.Function.identity()));
    }

    public List<UserVO> listNormalUser() {
        return userDao.listNormalUser();
    }

    public static void main(String[] args) {
//    	 String salt = new NewBCryptPasswordEncoder().getSalt();
//    	 String salt = "2978e12aad0644c8a43af02ead77cb9a";
//         String cryptPassword = getCryptPassword(salt);
//         System.out.println("cryptPassword="+cryptPassword);
//    	Map<String,String> map =  new NewBCryptPasswordEncoder().encodePassword("123456");
//    	System.out.println("salt="+(String)map.get("salt")+"|  encodePassword="+(String)map.get("encodePassword"));

    }

    /**
     *
     * @return 是否锁屏
     */
    public Boolean checkLockScreen(String uid) {
        String hashKey = RedisCacheKey.makeRedisKey("",RedisCacheKey.LOCK_SCREEN);
        return redisTemplate.opsForHash().hasKey(hashKey, uid);
    }

    public void lockScreen(String userId) {
        String hashKey = RedisCacheKey.makeRedisKey("",RedisCacheKey.LOCK_SCREEN);
        redisTemplate.opsForHash().put(hashKey, userId, 1);
    }

    /**
     * 解锁
     */
    public Boolean unLockScreen(String userId) {
        String hashKey = RedisCacheKey.makeRedisKey("",RedisCacheKey.LOCK_SCREEN);
        return redisTemplate.opsForHash().delete(hashKey, userId) > 0;
    }

    public List<UserVO> getUserNameAndCorpNameByUserIds(List<String> userIds) {
        return userDao.getUserNameAndCorpNameByUserIds(userIds);
    }

    public DataTablesOutput getCashierUserTables(DataTablesInput dataTablesInput) {
        IPageResult<UserVO> pageResult = userDao.getCashierUserTables(dataTablesInput);
        return pageResult.convert(DataTablesOutput.class);
    }

    /**
     * 获取用户权限列表
     * @author huao
     * @param dataTablesInput 参数
     * @return {@link DataTablesOutput<UserPermissionVO>} 查询结果
     */
    public DataTablesOutput<UserPermissionVO> getUserPermissionList(DataTablesInput dataTablesInput) {
        DataTablesOutput<UserPermissionVO> output = new DataTablesOutput<>();
        // 查询条件
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String likes_loginName = Optional.ofNullable(formdata.get("likes_loginName")).map(Object::toString).orElse(null);
        String likes_username = Optional.ofNullable(formdata.get("likes_username")).map(Object::toString).orElse(null);
        String corpIds = Optional.ofNullable(formdata.get("corpId")).map(Object::toString).orElse(null);
        String roleIds = Optional.ofNullable(formdata.get("roleId")).map(Object::toString).orElse(null);

        // 查询所有用户
        User userQuery = new User();
        if (ObjectUtil.isNotEmpty(likes_loginName)) userQuery.setLoginName(likes_loginName);
        if (ObjectUtil.isNotEmpty(likes_username)) userQuery.setUsername(likes_username);
        List<User> userList = userDao.listUser(userQuery);
        if (CollUtil.isEmpty(userList)) return output;

        StopWatch stopWatch = new StopWatch("查询用户权限表");
        stopWatch.start();
        // 返回对象
        List<UserPermissionVO> list = new ArrayList<>();
        for (User user : userList){
            // 查询用户拥有的单位权限
            List<CorpUserResourceDTO> corpUserResourceList = corpUserResourceDao.list(user.getId(),corpIds);
            if (ObjectUtil.isEmpty(corpUserResourceList)) continue;

            // 查询用户拥有的角色权限
            List<RoleResourceDTO> userRoleList = roleDao.listUserRoleResource(user.getId(), roleIds);
            if (ObjectUtil.isEmpty(userRoleList)) continue;

            // userRoleList 根据角色ID分组
            Map<String, List<RoleResourceDTO>> roleResourceMap = userRoleList.stream().collect(Collectors.groupingBy(RoleResourceDTO::getRoleId));

            // 把corpUserResourceList 根据单位ID分组
            Map<String, List<CorpUserResourceDTO>> corpResourceMap = corpUserResourceList.stream().collect(Collectors.groupingBy(CorpUserResourceDTO::getCorpId));
            for (Map.Entry<String, List<CorpUserResourceDTO>> entry : corpResourceMap.entrySet()){
                // 单位权限资源
                List<CorpUserResourceDTO> value = entry.getValue();
                // 取出value中所有的资源ID
                Set<String> corpResourceIdSet = value.stream().map(CorpUserResourceDTO::getResourceId).collect(Collectors.toSet());

                // 遍历用户角色
                for (Map.Entry<String, List<RoleResourceDTO>> roleEntry : roleResourceMap.entrySet()){
                    List<RoleResourceDTO> roleValue = roleEntry.getValue();
                    // 取出roleValue中需要的资源ID
                    Set<String> roleResourceIdSet = roleValue.stream().map(RoleResourceDTO::getResourceId).collect(Collectors.toSet());

                    if (corpResourceIdSet.containsAll(roleResourceIdSet)){
                        // 则表示用户有该单位的该角色权限
                        UserPermissionVO vo = new UserPermissionVO();
                        vo.setUserId(user.getId()).setLoginName(user.getLoginName()).setUsername(user.getUsername());
                        // 单位信息
                        vo.setCorpId(entry.getKey()).setCorpCode(value.get(0).getCorpCode()).setCorpName(value.get(0).getCorpName());
                        // 角色信息
                        vo.setRoleId(roleEntry.getKey()).setRoleName(roleValue.get(0).getRoleName());
                        vo.setAssignTime(value.get(0).getAssignTime());
                        list.add(vo);
                    }
                }
            }
        }
        stopWatch.stop();
        logger.info("用户权限列表耗时：{}", stopWatch.prettyPrint());
        if (CollUtil.isEmpty(list)) return output;
        // 保证排序
        List<UserPermissionVO> allList = list.stream().filter(obj -> ObjectUtil.isNotEmpty( obj.getUserId()))
                .sorted(Comparator.comparing(UserPermissionVO::getUserId).thenComparing(UserPermissionVO::getCorpCode).thenComparing(UserPermissionVO::getRoleId))
                .collect(Collectors.toList());
        list.clear();
        // 手动分页
        IPageResult<UserPermissionVO> page = PageUtil.pageByStart(allList, dataTablesInput.getStart(), dataTablesInput.getLength());
        output.setData(page.getList());
        output.setRecordsTotal(page.getTotalRow());
        output.setRecordsFiltered(page.getTotalRow());
        output.setDraw(dataTablesInput.getDraw());
        return output;
    }

    public DataTablesOutput<UserPermissionVO> getUserPermissionListThread(DataTablesInput dataTablesInput) {
        DataTablesOutput<UserPermissionVO> output = new DataTablesOutput<>();
        // 查询条件
        Map<String, Object> formdata = dataTablesInput.getFormdata();
        String likes_loginName = Optional.ofNullable(formdata.get("likes_loginName")).map(Object::toString).orElse(null);
        String likes_username = Optional.ofNullable(formdata.get("likes_username")).map(Object::toString).orElse(null);
        String corpIds = Optional.ofNullable(formdata.get("corpId")).map(Object::toString).orElse(null);
        String roleIds = Optional.ofNullable(formdata.get("roleId")).map(Object::toString).orElse(null);

        // 查询所有用户
        User userQuery = new User();
        if (ObjectUtil.isNotEmpty(likes_loginName)) userQuery.setLoginName(likes_loginName);
        if (ObjectUtil.isNotEmpty(likes_username)) userQuery.setUsername(likes_username);
        List<User> userList = userDao.listUser(userQuery);
        if (CollUtil.isEmpty(userList)) return output;

        StopWatch stopWatch = new StopWatch("查询用户权限表");
        stopWatch.start();

        try {
            // 按批次处理，每批10个用户
            int batchSize = 10;
            List<UserPermissionVO> list = new CopyOnWriteArrayList<>();

            for (int i = 0; i < userList.size(); i += batchSize) {
                int endIndex = Math.min(i + batchSize, userList.size());
                List<User> batchUsers = userList.subList(i, endIndex);

                // 批量处理用户权限查询
                List<CompletableFuture<List<UserPermissionVO>>> futures = new ArrayList<>();
                for (User user : batchUsers) {
                    CompletableFuture<List<UserPermissionVO>> future = userPermissionAsyncService
                            .processUserPermissions(user, corpIds, roleIds);
                    futures.add(future);
                }

                // 等待这批用户处理完成
                CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                        futures.toArray(new CompletableFuture[0]));
                allFutures.join();

                // 收集这批结果
                for (CompletableFuture<List<UserPermissionVO>> future : futures) {
                    list.addAll(future.join());
                }
            }

            if (CollUtil.isEmpty(list)) return output;
            stopWatch.stop();
            logger.info("用户权限列表耗时：{}", stopWatch.prettyPrint());

            // 保证排序
            List<UserPermissionVO> allList = list.stream()
                    .filter(obj -> ObjectUtil.isNotEmpty(obj.getUserId()))
                    .sorted(Comparator.comparing(UserPermissionVO::getUserId)
                            .thenComparing(UserPermissionVO::getCorpCode)
                            .thenComparing(UserPermissionVO::getRoleId))
                    .collect(Collectors.toList());

            // 手动分页
            IPageResult<UserPermissionVO> page = PageUtil.pageByStart(
                    allList, dataTablesInput.getStart(), dataTablesInput.getLength());
            output.setData(page.getList());
            output.setRecordsTotal(page.getTotalRow());
            output.setRecordsFiltered(page.getTotalRow());
            output.setDraw(dataTablesInput.getDraw());
            return output;

        } catch (Exception e) {
            throw new RuntimeException("处理用户权限时发生错误", e);
        }
    }

    /**
     * 获取租户下所有用户
     *
     * @param tenantId 租户ID
     * @return 用户列表
     */
    public List<String> listUserNameByTenantId(String tenantId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tenantId", tenantId));
        criteria.setProjection(Projections.property("loginName"));
        List<String> userNames = criteria.list();
        return userNames;
    }
}
