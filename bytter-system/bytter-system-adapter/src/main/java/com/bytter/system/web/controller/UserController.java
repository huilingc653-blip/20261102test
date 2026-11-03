package com.bytter.system.web.controller;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.ServletUtils;
import com.bytter.system.vo.UserPermissionVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.base.entity.IdEntity;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.IMultiLangService;
import com.bytter.system.api.INotificationService;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.api.config.SM2ManagerService;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.constant.UserConstant;
import com.bytter.system.api.dto.AccountMappingDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.enums.UserMessageEnum;
import com.bytter.system.constants.AppH5Constants;
import com.bytter.system.entity.Role;
import com.bytter.system.entity.TableRecord;
import com.bytter.system.entity.User;
import com.bytter.system.entity.UserCorpRole;
import com.bytter.system.entity.UserFaceDetection;
import com.bytter.system.entity.UserHistoryPassword;
import com.bytter.system.entity.UserRole;
import com.bytter.system.query.CorpRoleQuery;
import com.bytter.system.service.AccountMappingService;
import com.bytter.system.service.MenuService;
import com.bytter.system.service.ResourcesService;
import com.bytter.system.service.RoleService;
import com.bytter.system.service.TableRecordService;
import com.bytter.system.service.UserCorpRoleService;
import com.bytter.system.service.UserFaceDetectionService;
import com.bytter.system.service.UserHistoryPasswordService;
import com.bytter.system.service.UserService;
import com.bytter.system.vo.RoleVo;
import com.bytter.system.vo.UserVO;
import com.bytter.workflow.api.IWorkFlowService;
import com.google.common.collect.Lists;
import cn.hutool.core.collection.CollUtil;
import com.bytter.system.service.OrgantypeModuleService;
/**
 * 用户管理控制器
 * Created by zhoul on 2017/4/11.
 * todo 删除用户校验是否有待审批数据循环依赖问题
 */
@Controller
@RequestMapping("/sys/user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TableRecordService tableRecordService;

    @Autowired
    private UserHistoryPasswordService userHistoryPasswordService;
    @Autowired
    private IConfigService configServiceI;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private UserFaceDetectionService userFaceDetectionService;
    @Autowired
    private Ii18nService i18nServiceI;
    @Autowired
    private IMultiLangService multiLangServiceI;
    @Autowired
    private ResourcesService resourceService;
    @Autowired(required = false)
    private AccountMappingService accountMappingService;
    @Autowired
    private IWorkFlowService workFlowService;
    @DubboReference(check = false)
    private INotificationService notificationService;

    @Autowired
    private SM2ManagerService sm2ManagerService;

    @Autowired
    private UserCorpRoleService userCorpRoleService;

    @Autowired
    OrgantypeModuleService organtypeModuleService;

    @GetMapping("/index")
    public void index() {
    }

    @GetMapping("/selectUserPage")
    public void selectUserPage() {
    }

//    @GetMapping("/menu")
//    @ResponseBody
//    public Object menu() {
//        UserShiroVO userShiroVO = (UserShiroVO) SecurityUtils.getSubject().getPrincipal();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
//        try {
//            return mapper.writeValueAsString(userShiroVO.getMenus());
//        } catch (JsonProcessingException e) {
//            logger.error(e);
//        }
//        return null;
////		return userShiroVO.getMenus();
//    }
    @GetMapping("/getCadnByLoginName")
    @ResponseBody
    public String getCadnByLoginName(String loginName) {
    	if(StringUtils.isEmpty(loginName)) {
			return "";
		}
		return userService.getByLoginName(loginName).getUserCadn();
    }

    @PostMapping("/datatable.json")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return userService.getDataTables(dataTablesInput);
    }
    @PostMapping("/tableGrid.json")
    @ResponseBody
    public TableGridResponse<UserVO> userGrid(@RequestBody TableGridInput tableGridInput) {
        ICurrentUser currentUser = getCurrentUser();
        return userService.getTableGrid(tableGridInput, currentUser);
    }
    @GetMapping("/json")
    @ResponseBody
    public Map<String, Object> json() {
        Map<String, Object> map = new HashMap<>();
        map.put("data", userService.listAll());
        return map;
    }
    @GetMapping("/view")
    public void view(String id, Model model) {
        User user = null;
        if (StringUtils.isNotEmpty(id)) {
            user = userService.get(id);
            model.addAttribute("user", user);
            // 当前用户绑定钉钉或微信的相关信息
            AccountMappingDTO ddAccountMap = accountMappingService.queryAccountMapping(null, user.getId(), AppH5Constants.ClientType.DING_TALK);
            if (null != ddAccountMap) {
                model.addAttribute("dingUserId", ddAccountMap.getRelationUserId());
            }
            AccountMappingDTO wxAccountMap = accountMappingService.queryAccountMapping(null, user.getId(), AppH5Constants.ClientType.CORP_WEI_XIN);
            if (null != wxAccountMap) {
                model.addAttribute("weiUserId", wxAccountMap.getRelationUserId());
            }
            AccountMappingDTO fsAccountMap = accountMappingService.queryAccountMapping(null, user.getId(), AppH5Constants.ClientType.FEISHU);
            if (null != fsAccountMap) {
                model.addAttribute("fsUserId", fsAccountMap.getRelationUserId());
            }
        }
        //根据证书类型 如果为 0不使用证书 则不需要设置用户登入模式
        String sysEncrypt = this.configServiceI.getConfig(ConfigConstant.SYS, SysConfigConstant.ENCRYPT).toString();
        model.addAttribute("sysEncrypt", sysEncrypt);
        //是否开启了人脸复核：1启用；0停用
        if (userFaceDetectionService.isOpenFaceVerify()) {
            model.addAttribute("isOpenFaceVerify", "1");
            if (user != null) {
                //获取人脸图像数据
                UserFaceDetection userFaceDetection = userFaceDetectionService.getUserFaceDetection(user.getLoginName());
                model.addAttribute("userFaceDetection", userFaceDetection);
            }
        }
    }
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(User user, String rolesId, String dingTalkUserId, String weiXinUserId, String feishuUserId) {
        User dbUser = userService.get(user.getId());
        //禁用用户 或者 管理员调整为非管理员 校验在途
        if (UserConstant.STATUS_FORBIDDEN.equals(user.getStatus())
                || (user.getIsAdmin() == 0 && dbUser.getIsAdmin() == 1)) {
            //根据用户获取用户待审批和待办.
          /*  Integer todoCount=workFlowService.getTodoByUserId(user.getId());
            Integer notificationCount=notificationService.getUserNotificationCount(user.getId());
            Boolean flow = workFlowService.flowIsHasUserId(user.getId());
            if(todoCount>0 || notificationCount>0 || flow){
                throw new BusinessException(UserMessageEnum.SYS_USER_NOT_DEL);
            }*/
        }
        userService.update(user, rolesId, getUserId(), dingTalkUserId, weiXinUserId, feishuUserId);
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(User user, String dingTalkUserId, String weiXinUserId, String feishuUserId) {
        userService.save(user, getUserId(), dingTalkUserId, weiXinUserId, feishuUserId);
        return success();
    }
    @PostMapping("/lock")
    @ResponseBody
    public AjaxJson lock(@RequestParam(value = "ids[]") List<String> ids) {
        userService.lock(ids);
        return success();
    }
    @PostMapping("/unLock")
    @ResponseBody
    public AjaxJson unLock(@RequestParam(value = "ids[]") List<String> ids) {
        userService.unLock(ids);
        return success();
    }
    @GetMapping("/modifyPassword")
    public void modifyPassword(HttpServletRequest request) {
        //todo
        createToken(request);
    }
    //todo
    protected void createToken(HttpServletRequest request) {
    }
    @GetMapping("/view/{id}")
    public String viewUserInfo(@PathVariable("id") String id, Model model) {
        User user = userService.get(id);
        model.addAttribute("user", user);
        //根据证书类型 如果为 0不使用证书 则不需要设置用户登入模式
        String sysEncrypt = this.configServiceI.getConfig(ConfigConstant.SYS, SysConfigConstant.ENCRYPT).toString();
        model.addAttribute("sysEncrypt", sysEncrypt);
        return "sys/user/viewUserInfo";
    }
    protected boolean validateToken(HttpServletRequest request) {
        return true;
    }
    @PostMapping("/modifyPassword")
    @ResponseBody
    public Object modifyPasswordPost(HttpServletRequest request,String passkey, String oldPassword, String newPassword) {
        if (!validateToken(request)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_TOKE_EXPIRE_PLEASE_REFRESH);
        }
        if (!StringUtils.isBlank(passkey)) {
            oldPassword = sm2ManagerService.decodeSm2ByKey(oldPassword,passkey);
            newPassword = sm2ManagerService.decodeSm2ByKey(newPassword, passkey);
            ConfigDTO dto1 = configServiceI.getConfig("sys", "passwordModel");
            if ("3".equals(dto1.getValue())) {
                ConfigDTO dto2 = configServiceI.getConfig("sys", "passwordLength");
                String len = (dto2 == null || org.apache.commons.lang.StringUtils.isBlank(dto2.getValue())) ? "6" : dto2.getValue();
                //8位以上包含字母数字和特殊符号
                String reg = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[*?!&￥$%^#,./@\";:><\\[\\]}{\\-=+_\\\\|》《。，、？’‘“”~ `])(?=\\S+$).{" + len + ",}$";
                if (!newPassword.matches(reg)) {
                    throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_PASSWORD_VOL_ERROR_ERROR);
                }
            }
        }
        userService.updatePassword(getUserId(), oldPassword, newPassword);
        // 除了本设备，强制其他设备的统一账号用户退出登录
        //userService.makeLoginUserForceLogout(getUserLoginName(), UserMessageEnum.SYS_USER_MODIFIED_PWD_LOGOUT, getSession().getId());
        // 解除第三方平台的账号绑定，使其重新绑定
        accountMappingService.unbindAllAccount(getUserId());
        return success();
    }
    @PostMapping("/delete")
    @ResponseBody
    public Object delete(@RequestParam("ids[]") List<String> ids) {
        for (String id : ids) {
            //根据用户获取用户待审批和待办.
          /*  Integer todoCount=workFlowService.getTodoByUserId(id);
            Integer notificationCount=notificationService.getUserNotificationCount(id);
            Boolean flow = workFlowService.flowIsHasUserId(id);
            if(todoCount>0 || notificationCount>0 || flow){
                throw new BusinessException(UserMessageEnum.SYS_USER_NOT_DEL);
            }*/
        }
        userService.delete(ids);
        return success();
    }
    /**
     * 用户恢复
     *
     * @param id
     * @return
     */
    @PostMapping("/recover")
    @ResponseBody
    public AjaxJson recover(@RequestParam String id) {
        userService.updateRecover(id);
        return success();
    }
    @PostMapping("/resetPassword")
    @ResponseBody
    //todo 双管理员循环依赖问题
    public Object resetPassword(@RequestParam(value = "ids[]") List<String> ids) {
    	 ArrayList<String> strings = new ArrayList<>();
         strings.addAll(ids);
         userService.resetPassword(strings);
        return success();
    }
    /**
     * 用户管理 - 特殊权限授权
     *
     * @param userId
     * @param corpId
     * @return
     */
    @GetMapping("/menuAndResource")
    @ResponseBody
    public Object menuAndResource(String userId, String corpId) {
        return menuService.menuAndResource(userId, corpId);
    }
    /**
     * 判断该用户是否有角色
     *
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/checkUserHasRoles")
    @ResponseBody
    public Object checkUserHasRoles(String userId) {
        User user = userService.get(userId);
        Set<UserRole> roles = user.getUserRoles();
        if (roles.isEmpty()) {
            throw new BusinessException(UserMessageEnum.SYS_USER_NOT_ASSIGN_ROLES, user.getUsername());
        }
        return success();
    }
    @GetMapping("/assignCorps")
    public void assignCorps(String userId, String corpId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("moduleList", "");
        model.addAttribute("baseModule","");
    }
    @GetMapping("/assignResource")
    public void assignResource(String userId, String corpId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("moduleList", "");
        model.addAttribute("baseModule","");
    }
    @PostMapping("/assignResource")
    @ResponseBody
    public Object assignResourcePost(String userId, String corpId,
                                     @RequestParam(value = "resources[]", required = false) List<String> resources,
                                     @RequestParam(value = "menus[]", required = false) List<String> menus) {
        userService.saveCorpUserResource(userId, corpId, resources, menus);
        return success();
    }
    /**
     * 跳转到批量复制单位、网点页面
     *
     * @param userId
     * @param corpId
     * @param model
     */
    @GetMapping("/copyResources")
    public void copyResources(String userId, String userBelongCorp, String corpId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("userBelongCorp", userBelongCorp);
        model.addAttribute("corpId", corpId);
    }
    /**
     * 处理批量赋值单位、网点
     *
     * @param userId
     * @param sourceCorpId
     * @param corps
     */
    @PostMapping("/copyResources")
    @ResponseBody
    public Object copyResources(String userId, String sourceCorpId, @RequestParam(value = "corps[]", required = false) List<String> corps) {
    	userService.copyCorpUserResource(userId, sourceCorpId, corps);
        return success();
    }
    /**
     * 获取用户拥有特殊权限的单位Id集合
     * <p>
     * Created by chenlj on 2018/2/1.
     */
    @GetMapping("/getResourceCorpIds")
    @ResponseBody
    public Object getResourceCorpIds(String userId) {
        return menuService.getCorpIdsByUserId(userId);
    }
    @GetMapping("/assignRoles")
    public void assignRoles(String userId, Model model) {
        User user = userService.get(userId);
        model.addAttribute("user", user);
        List<Role> roles = roleService.listAll();
        for (Role role : roles) {
            String businessBaseContext = i18nServiceI.getBusinessBaseContext(role.getId());
            if (StringUtils.isNotBlank(businessBaseContext)) {
                roleService.evict(role);
                role.setName(businessBaseContext);
            }
        }
        model.addAttribute("roles", roles);
    }
    @PostMapping("/assignRoles")
    @ResponseBody
    public Object assignRolesPost(String userId, @RequestParam(value = "ids[]", required = false) List<String> ids) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("roleIds", ids);
        userService.assignRoles(userId, ids);
        return success();
    }
    @GetMapping("/copyRoles")
    public void copyRoles(String sourceUser, Model model) {
        model.addAttribute("sourceUser", sourceUser);
    }
    /**
     * 复制权限
     *
     * @param userId 用户ID
     * @param ids    需要被复制的用户id集合
     * @return 返回成功结果
     */
    @PostMapping("/copyRoles")
    @ResponseBody
    public Object copyRolesPost(String userId, @RequestParam("ids[]") List<String> ids) {
    	userService.copyRoles(userId, ids);
        return success();
    }
    @GetMapping("/corpUserResource")
    @ResponseBody
    public Object corpUserResource(String userId, String corpId) {
        return userService.listCorpUserResource(userId, corpId);
    }
    /**
     * 根据单位ID和资源代码获取对应的用户信息
     *
     * @param corpId
     * @param resourceCode
     */
    @PostMapping("/loadUserByCorpIdOrResource")
    @ResponseBody
    public Object loadUserByCorpIdOrResource(String corpId, String resourceCode) {
        return userService.loadUserByCorpIdOrResource(corpId, resourceCode);
    }
    /**
     * 根据单位查找用户
     *
     * @param corpId
     * @return
     */
    @GetMapping("/listByCorpId")
    @ResponseBody
    public Object listByCorpId(String corpId) {
        List<Map<String, String>> list = new ArrayList<>();
        List<User> userList = userService.listByCorpId(corpId);
        for (User u : userList) {
            Map<String, String> map = new HashMap<>();
            map.put("id", u.getId());
            map.put("loginName", u.getLoginName());
            map.put("username", u.getUsername());
            map.put("code", u.getCode());
            map.put("userCadn", u.getUserCadn());
            list.add(map);
        }
        return list;
    }
    /**
     * 根据单位查找有该单位审批工作流权限的用户
     *
     * @param corpId          流程分类所属单位id
     * @param resourceContent 流程审批权限
     * @return
     */
    @GetMapping("/listApproveUserByCorpId")
    @ResponseBody
    public List<UserVO> listApproveUserByCorpId(String corpId, String resourceContent) {
        return userService.listApproveUserByCorpId(corpId, resourceContent);
    }
    @PostMapping("/listByUserIds")
    @ResponseBody
    public List<UserVO> listByUserIds(@RequestParam List<String> ids) {
        return userService.listByUserIdsSimple(ids);
    }
    /**
     * 流程使用body传递参数
     * @param ids 用户ID
     * @return 用户列表
     */
    @PostMapping("/listByIds")
    @ResponseBody
    public Object listByIds(@RequestBody List<String> ids) {
        return userService.listByUserIdsSimple(ids);
    }
    /**
     * 通过角色id获取用户id
     *
     * @param ids 角色id
     * @return
     */
    @PostMapping("/listByRoleIdList")
    @ResponseBody
    public Object listByRoleIdList(@RequestParam List<String> ids) {
        List<String> userIds = userService.listUserIdsByRoleId(ids);
        return new HashSet<>(userIds);
    }
    /**
     * 通过角色id和网点id获得具有"工作流-待办事项-流程审批"权限的用户id
     *
     * @param ids             角色id
     * @param corpId
     * @param type
     * @param resourceContent 审批权限
     * @return
     */
    @PostMapping("/listByRoleIds")
    @ResponseBody
    @Deprecated
    public Object listByRoleIds(@RequestParam List<String> ids, String corpId, String type, String resourceContent) {
        if (EmptyUtils.isEmpty(type)) {
            return null;
        }
        List<String> strings = userService.listApproveUserIdsByRoleIdAndCorpId(ids, corpId, Lists.newArrayList(resourceContent));
        return new HashSet<>(strings);
    }
    /**
     * 通过角色id和网点id获得具有"工作流-待办事项-流程审批"权限的用户id
     * @return
     */
    @PostMapping("/listByRoleIdsV2")
    @ResponseBody
    public Object listByRoleIdsV2(@RequestBody CorpRoleQuery corpRoleQuery) {
        List<String> strings = userService.listApproveUserIdsByRoleIdAndCorpIdNew(corpRoleQuery.getRoleIds(), corpRoleQuery.getCorpId(), null);
        List<UserVO> users = userService.listByUserIdsSimple(strings);
        return AjaxJson.success(CollUtil.isNotEmpty(users) ? users : Lists.newArrayList());
    }
    /**
     * 获取表格参数
     *
     * @param tableKey
     * @return
     */
    @GetMapping("/tableRecord/get")
    @ResponseBody
    public Object getTableRecord(String tableKey) {
        TableRecord byTableKey = tableRecordService.getByTableKey(tableKey, getUserId());
        if (byTableKey == null) {
            return "";
        }
        return byTableKey.getContent();
    }
    /**
     * 重置表格参数
     *
     * @param tableKey
     * @return
     */
    @GetMapping("/tableRecord/reset")
    @ResponseBody
    public Object resetTableRecord(String tableKey) {
        tableRecordService.deleteByTableKey(tableKey, getUserId());
        return success();
    }
    @PostMapping("/tableRecord/change")
    @ResponseBody
    public Object changeTableRecord(String tableKey, String content) {
        tableRecordService.saveOrUpdate(tableKey, HtmlUtils.htmlUnescape(content), getUserId());
        return success();
    }
    /**
     * 删除证书绑定
     *
     * @param id
     * @return
     */
    @PostMapping("/caDel")
    @ResponseBody
    public AjaxJson caDel(@RequestParam String id) {
        userService.caDel(id);
        return success();
    }
    /**
     * 证书绑定
     *
     * @param id
     * @param userCadn
     * @return
     */
    @PostMapping("/caBinding")
    @ResponseBody
    public AjaxJson caBinding(@RequestParam String id, @RequestParam String userCadn) {
        userService.caBinding(id, userCadn);
        return success();
    }
    /**
     * 密码校验
     *
     * @return
     * @author zuoy 2017/11/15
     * <p>
     * update by zhoul 2017-11-30
     */
    @GetMapping("/checkUserPassword")
    @ResponseBody
    public Object checkUserPassword() {
        UserHistoryPassword historyPassword = this.userHistoryPasswordService.getLastPasswordBy(this.getUserId());
        ConfigDTO passwordCloseBtn = this.configServiceI.getConfig("sys", "passwordCloseBtn");
        if (EmptyUtils.isEmpty(historyPassword)) {
            // 校验是否为初始密码登陆
            return success(UserMessageEnum.SYS_USER_MESSAGE_PASSWORD_DEFAULT, passwordCloseBtn.getValue());
        } else {
            // 校验是否为密码是否到达使用期限
            ConfigDTO config = this.configServiceI.getConfig("sys", "passwordDeadLine");
            int day = DateUtils.dayDiffs(DateUtils.getDate(), historyPassword.getCreateTime());
            if (!EmptyUtils.isEmpty(config) && day >= Integer.parseInt(config.getValue())) {
                return success(UserMessageEnum.SYS_USER_MESSAGE_PASSWORD_DEADLINE, passwordCloseBtn.getValue());
            }
        }
        return success();
    }
    /**
     * 根据证书得到绑定用户
     *
     * @param userCadn
     * @return
     */
    @PostMapping("/getUserByCadn")
    @ResponseBody
    public String getUserByCadn(@RequestParam String userCadn) {
        User user = userService.getUserByLoginCadn(userCadn);
        return user.getLoginName();
    }
    /**
     * 锁屏功能
     *
     * @author zuoy 2017/11/16
     */
    @GetMapping("/checkLockScreen")
    @ResponseBody
    public AjaxJson checkLockScreen() {
        return success(userService.checkLockScreen(getUserId()));
    }

    @GetMapping("/lockScreen")
    @ResponseBody
    public AjaxJson lockScreen() {
        userService.lockScreen(getUserId());
        return success();
    }
    /**
     * 登录解锁
     *
     * @param password
     * @return
     * @author zuoy 2017/11/16
     * @update zhoul 2017-11-18
     */
    @PostMapping("/unLockScreen")
    @ResponseBody
    public AjaxJson unLockScreen(String password,String pubKey) {
        String pwd = sm2ManagerService.decodeSm2ByKey(password, pubKey);
        userService.checkPassword(getUserId(), pwd);
        return success(userService.unLockScreen(getUserId()));
    }

    /**
     * 根据用户ID获取该用户所拥有的角色信息
     *
     * @param userId 用户ID
     * @return
     */
    @GetMapping("/userRole")
    @ResponseBody
    public Object userRole(String userId) {
        User user = userService.get(userId);
        List<Role> roleList = new ArrayList<>();
        Set<UserRole> userRoles = user.getUserRoles();
        if (userRoles.isEmpty()) {
            return roleList;
        }
        for (UserRole userRole : userRoles) {
            roleList.add(userRole.getRole());
        }
        Collections.sort(roleList, new Comparator<Role>() {
            @Override
            public int compare(Role o1, Role o2) {
                long o1Time = o1.getCreateTime() == null ? 0 : o1.getCreateTime().getTime();
                long o2Time = o2.getCreateTime() == null ? 0 : o2.getCreateTime().getTime();
                return (int) (o1Time - o2Time);
            }
        });
        return roleList;
    }
    /**
     * 权限复制操作
     *
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/coypPermissionsOpt")
    public String coypPermissionsOpt(Model model, String id) {
        model.addAttribute("id", id);
        return "sys/user/coypPermissions";
    }
    /**
     * 弹出用户选择页面
     *
     * @param sourceUser
     * @param model
     * @return
     */
    @GetMapping("/selectUsers")
    public String selectUsers(String sourceUser, String selectedUserIds, Model model) {
        model.addAttribute("selectedUserIds", selectedUserIds);
        model.addAttribute("sourceUser", sourceUser);
        return "sys/user/selectUsers";
    }
    /**
     * 获取已选中用户
     *
     * @param ids
     * @return
     */
    @PostMapping("/getSelectedUsers")
    @ResponseBody
    public List<UserVO> listByUserIds(String ids) {
        List<UserVO> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(ids)) {
            list = userService.listByUserIdsSimple(Arrays.asList(ids.split(",")));
        }
        return list;
    }
    /**
     * 权限复制提交
     *
     * @param permissionsType 权限类别
     * @param copyType        复制方式
     * @param userIds         需要复制的用户id
     */
    @PostMapping("/submitCoypPermissions")
    @ResponseBody
    public AjaxJson submitCoypPermissions(String[] permissionsType, String copyType, String userIds, String sourceUserId) {
        userService.copyPermissions(permissionsType, copyType, Arrays.asList(userIds.split(",")), sourceUserId);
        return success();
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<UserVO> list) {
        userService.saveImport(list, getCurrentUser());
        return success();
    }

    /**
     * 用户导出
     *
     * @param request
     * @param response
     * @param reqVO
     * @param model
     * @throws Exception
     */
    @LogMessage(content = "用户管理导出", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/downloadUser")
    public void exportInst(HttpServletRequest request, HttpServletResponse response, UserVO reqVO, Model model) throws Exception {
        String filename = "用户管理.xlsx"; // 改为xlsx

        if (ServletUtils.isIE(request)) {
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        List<UserVO> list = userService.listUserByParam(reqVO);
        // 大数据量使用SXSSFWorkbook
        Workbook workbook = userService.exportUserLarge(list);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");

        try (OutputStream ouputStream = response.getOutputStream()) {
            workbook.write(ouputStream);
            ouputStream.flush();
        }

        // 如果是SXSSFWorkbook，需要清理临时文件
        if (workbook instanceof SXSSFWorkbook) {
            ((SXSSFWorkbook) workbook).dispose();
        }
        workbook.close();
    }

    /**
     * 导出用户信息
     *
     * @param request
     * @param response
     * @throws Exception
     */
//    @GetMapping("/downloadUser")
//    public void downloadUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("application/force-download;");
//        response.setHeader("content-disposition", "attachment;filename=" +
//                ExportUtils.getFileName(request, "用户管理") + ".xls");
//        OutputStream os = null;
//        try {
//            os = response.getOutputStream();
//            // 声明一个工作薄
//            HSSFWorkbook workbook = new HSSFWorkbook();
//            // 生成一个表格
//            HSSFSheet sheet = workbook.createSheet("sheet");
//            // 锁定表格
//            //sheet.protectSheet("edit");
//            // 设置表格默认列宽度为20个字节
//            sheet.setDefaultColumnWidth(20);
//            // 组装标题头
//            String[] head = new String[]{"账号", "姓名", "所属单位", "状态", "是否管理员"};
//            HSSFRow row0 = sheet.createRow(0);
//            for (int i = 0; i < head.length; i++) {
//                HSSFCell cell = row0.createCell(i);
//                //cell.setCellStyle(ExportUtils.getCenterCellStyle(workbook, false));
//                cell.setCellValue(head[i]);
//            }
//            List<UserVO> list = userService.listAllUserByUnDelete();
//            if (!EmptyUtils.isEmptyList(list)) {
//                //HSSFCellStyle centerCellStyle = ExportUtils.getCenterCellStyle(workbook, false);
//                //HSSFCellStyle leftCellStyle = ExportUtils.getLeftCellStyle(workbook, false);
//                int index = 1;
//                HSSFCell cell = null;
//                for (UserVO vo : list) {
//                    HSSFRow row = sheet.createRow(index);
//                    // 账号
//                    cell = row.createCell(0);
//                    //cell.setCellStyle(leftCellStyle);
//                    cell.setCellValue(vo.getLoginName());
//                    // 姓名
//                    cell = row.createCell(1);
//                    //cell.setCellStyle(leftCellStyle);
//                    cell.setCellValue(vo.getUsername());
//                    // 所属单位
//                    cell = row.createCell(2);
//                    //cell.setCellStyle(leftCellStyle);
//                    cell.setCellValue(vo.getCorpName());
//                    // 状态
//                    cell = row.createCell(3);
//                    //cell.setCellStyle(centerCellStyle);
//                    cell.setCellValue(vo.getStatusLang());
//                    // 是否管理员
//                    cell = row.createCell(4);
//                    //cell.setCellStyle(centerCellStyle);
//                    cell.setCellValue(UserConstant.ADMIN.equals(vo.getIsAdmin()) ? multiLangServiceI.getLang("common.sc_yn.yes") : multiLangServiceI.getLang("common.sc_yn.no"));
//                    index++;
//                }
//            }
//            workbook.write(os);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (os != null) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    /**
     * 获取所用有效用户
     *
     * @return
     */
    @RequestMapping("/listAllUserByUnDelete")
    @ResponseBody
    public Object listAllUserByUnDelete() {
        return userService.listAllUserByUnDelete();
    }

    @RequestMapping("/listNormalUser")
    @ResponseBody
    public List<UserVO> listNormalUser(){
        return userService.listNormalUser();
    }

    /**
     * 根据用户ID查找角色，角色中包含资源权限
     *
     * @param userId 用户ID
     * @return 角色集合
     */
    @GetMapping("/listUserRoleIncludeResource")
    @ResponseBody
    public Object listUserRoleIncludeResource(String userId) {
        User user = userService.get(userId);
        Set<UserRole> userRoles = user.getUserRoles();
        List<Map<String, Object>> rolesIncludeResource = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            HashMap<String, Object> map = new HashMap<>();
            Role role = userRole.getRole();
            List<String> resourceIdByRoleId = resourceService.findResourceIdByRoleId(role.getId());
            map.put("id", role.getId());
            map.put("name", role.getName());
            map.put("resource", resourceIdByRoleId);
            rolesIncludeResource.add(map);
        }
        return rolesIncludeResource;
    }
    @PostMapping("/modifySysSkin")
    @ResponseBody
    public AjaxJson modifySysSkin(String sysSkin) {
        userService.modifySysSkin(this.getUserId(), sysSkin);
        return success();
    }

    /**
     * 用户分配了的单位角色
     */
    @GetMapping("/userCorpRoles")
    @ResponseBody
    public AjaxJson userCorpRoles(String userId, String corpId) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(corpId)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_PARAM_ERROR);
        }

        AjaxJson ajaxJson = new AjaxJson(true);
        User user = userService.get(userId);
        Set<UserRole> userRoles = user.getUserRoles();
        List<UserCorpRole> userCorpRoles = userCorpRoleService.userCorpRoles(userId, corpId);
        Map<String, String> corpRoleMap = userCorpRoles.stream().collect(Collectors.toMap(UserCorpRole::getRoleId, IdEntity::getId));
        List<RoleVo> collect = userRoles.stream().map(item -> {
            RoleVo roleVo = new RoleVo();
            roleVo.setId(item.getRole().getId());
            roleVo.setName(item.getRole().getName());
            //单位是否分配用户角色
            roleVo.setChecked(corpRoleMap.containsKey(roleVo.getId()));
            return roleVo;
        }).collect(Collectors.toList());
        ajaxJson.setObj(collect);
        return ajaxJson;
    }


    /**
     * 用户单位角色分配 客户需求工作流角色审批配置的角色要有单位权限才可以审批 之前权限处没有角色与单位绑定的关系
     */
    @GetMapping("/userCorpRole")
    public void userCorpRole(String userId, String corpId, Model model) {
        assignResource(userId, corpId, model);
    }

    /**
     * 单位角色
     */
    @PostMapping("/assignUserCorpRole")
    @ResponseBody
    public AjaxJson assignUserCorpRole(String userId, String corpId, @RequestParam(value = "roles[]",required = false) List<String> roles) {
        if (StringUtils.isEmpty(userId)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_PARAM_ERROR);
        }
        if (StringUtils.isEmpty(corpId)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_PARAM_ERROR);
        }
        return success(userCorpRoleService.assignUserCorpRole(userId, corpId, roles));
    }

    @PostMapping("/cashierDatatable")
    @ResponseBody
    public DataTablesOutput cashierDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return userService.getCashierUserTables(dataTablesInput);
    }

    @LogMessage(content = "查询用户权限表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list/permission")
    public String list() {
        return "sys/permission/listUserPermission";
    }

    /**
     * 用户权限表
     * @author huao
     * @param dataTablesInput 请求参数
     * @return {@link DataTablesOutput<UserPermissionVO>} 结果
    */
    @ResponseBody
    @PostMapping("/getUserPermissionList")
    public DataTablesOutput<UserPermissionVO> getUserPermissionList(@RequestBody DataTablesInput dataTablesInput) {
        return userService.getUserPermissionList(dataTablesInput);
    }

    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput<UserPermissionVO> userPermissionList = userService.getUserPermissionList(dataTablesInput);
                return userPermissionList.getData();
            }
        }, totalParam, fileNameParam,"用户权限表查询", 5000, UserPermissionVO.class);
    }
}
