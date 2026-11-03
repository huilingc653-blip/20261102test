package com.bytter.system.api;

import com.bytter.cloud.common.core.persistence.vo.Result;
import com.bytter.system.api.dto.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhoul on 2017/6/19.
 */
public interface IUserService {

    /**
     * 获取登录的用户
     * <br>
     * 该方法已过时，请在Controller中获取用户信息
     *
     * @return
     * @Deprecated 2018-01-03 14:08:03 by zhoul
     */
    UserDTO getByLoginName(String loginName);

    /**
     * 获取用户信息。
     *
     * @param loginName 登录账号。
     * @param tenantId  租户ID。
     * @return 用户信息。
     */
    UserDTO getByLoginName(String loginName, String tenantId);

    /**
     * 根据SSO员工关联编号获取用户信息。
     *
     * @param employeeId 员工关联编号。
     * @return 用户信息。
     */
    UserDTO getByEmployeeId(String employeeId);

    UserDTO getByUserId(String userId);

    /**
     * 根据角色id和网点id，获得具有"工作流-待办事项-流程审批"权限的用户id
     *
     * @param rolesId
     * @param corpId
     * @param approveNewRes
     * @return
     */
    List<String> listApproveUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId, List<String> approveNewRes);
    
    /**
     * 根据角色id和网点id，获得具有"工作流-待办事项-流程审批"权限的用户id
     *
     * @param rolesId
     * @param corpId
     * @param approveNewRes
     * @return
     */
    List<String> listApproveUserIdsByRoleIdAndCorpIdNew(List<String> rolesId, String corpId, List<String> approveNewRes);

    /**
     * 获得具有指定单位，指定权限的userId
     *
     * @param corpId
     * @param permissions
     * @param userId      可选
     * @return
     */
    List<String> listUserIdsByCorpIdAndPermissionAndUserId(String corpId, List<String> permissions, List<String> userId);

    /**
     * 根据角色ID集合，获取用户ID集合
     *
     * @param rolesId
     * @return
     */
    List<String> listUserIdsByRoleId(List<String> rolesId);

    void save(List<UserDTO> userList);

    /**
     * 同步添加大参林用户信息。
     *
     * @param userList 用户列表。
     * @param source   添加来源，用于记录创建人。
     */
    void saveUserNoCorp(List<UserDTO> userList, String source);

    /**
     * 根据员工编号获取用户编号列表。
     *
     * @param employeeIdList 员工编号列表。
     * @return 用户编号列表。
     */
    List<Map<String, Object>> getUserIdMapByEmployeeIdList(List<String> employeeIdList);

    /**
     * 同步更新大参林用户信息。
     *
     * @param userList 用户列表。
     * @param source   修改来源，用于记录修改人。
     */
    void updateUserNoCorp(List<UserDTO> userList, String source);

    void update(List<UserDTO> userList);

    void delete(List<UserDTO> userList);

    List<Map<String, Object>> todoInfo(List<UserDTO> userList);

    String getSaltByLoginName(String loginName);

    /**
     * 获取租户配置信息。
     *
     */
    TenantPropDTO getTenantProperties();
    /**
     * 根据角色ID集合，获取用户ID集合
     *
     * @param rolesId
     * @return
     */
    List<String> listUserIdsByRoleIdAndCorpId(List<String> rolesId, String corpId);

    /**
     * 根据资源查找具有该资源权限的用户id
     *
     * @param permission 权限代码   eg:netbank:bankAcc:paymentBlending:query
     * @return
     */
    List<String> listUserIdsByPermission(String permission);

    /**
     * 获取除去删除的所有用户
     *
     * @return
     */
    List<UserDTO> listAllUserByUnDelete();

    /**
     * 获取有效的用户
     *
     * @return
     */
    List<UserDTO> listAllUser();

    /**
     * 获取系统默认发起员
     *
     * @return 发起员信息
     */
    UserDTO getScheduler();

    /**
     * 获得具有单位，指定权限的userId，
     *
     * @param corpIds
     * @param permission
     * @return
     */
    List<String> listUserIdsByCorpIdAndPermission(List<String> corpIds, String permission);

    /**
     * 根据用户ids得到用户
     *
     * @param ids
     * @return
     */
    List<UserDTO> listByUserIds(List<String> ids);

    /**
     * 通过用户ID查找用户
     *
     * @param mobile 手机号
     * @return UserDTO
     */
    UserDTO getUserByMobile(String mobile);
    
    
    /**
     * 通过租户ID和用户名查询用户
     *
     * @param tenantId 租户ID
     * @param username 用户名
     * @return 用户对象
     */
//    @GetMapping(value = "/user/feign/tenantIdAndUsername")
    Result<UserDTO> getByTenantIdAndUsername(String tenantId,String username);

    /**
     * 通过租户ID和手机号码查询用户
     *
     * @param tenantId 租户ID
     * @param phone 用户名
     * @return 用户对象
     */
//    @GetMapping(value = "/user/feign/tenantIdAndPhone")
    Result<UserDTO> getByTenantIdAndPhone(String tenantId,String phone);
    
    /**
     * 获取租户下所有用户
     *
     * @param tenantId 租户ID
     * @param userName 用户账号
     * @return 用户列表
     */
    //@GetMapping(value = "/user/feign/getUsersByTenantId")
    Result<List<UserDTO>> getUsersByTenantId(String tenantId,String userName);

    /**
     * 获取租户下所有用户
     *
     * @param tenantId 租户ID
     * @return 用户列表
     */
    Result<List<String>> getUserNamesByTenantId(String tenantId);
    

    Set<String> queryRolesByUserId(String userId);

    /**
     * 获取用户所拥有的角色ID集合。
     *
     * @param userId  用户ID
     * @param tenantId 租户ID
     * @return 用户所拥有的角色ID集合
     */
    Set<String> queryRolesByUserIdAndTenantId(String userId, String tenantId);

    /**
     * 查询用户所拥有的资源ID集合
     *
     * @param tenantId 租户ID
     * @param username 用户账号
     * @return 用户所拥有的资源ID集合
     */
    //@GetMapping(value = "/user/feign/assignedUserResourceIds")
    Result<Set<String>> getAssignedUserResourceIds(String tenantId,String username);

    /**
     * 获取用户登入方式
     *
     * @param tenant   客户号
     * @param username 登录账号
     * @return -1用户不存在，0用户密码，1银行密码+证书，2证书
     */
    //@PostMapping(value = "/getUserLoginType")
    Result<String> getUserLoginType(String tenant, String username);

    /**
     * 修改用户锁定状态
     *
     * @param username 登录账号
     * @param code     租户集团号
     * @return 修改结果
     */
    //@PostMapping("/user/feign/updateStatus")
    Result<Boolean> updateLock(String id);

    /**
     * 查询用户登录失败次数
     *
     * @param username 用户名
     * @param code     租户集团号
     * @return 失败次数
     */
    //@PostMapping("/user/feign/getFailCount")
    Result<Integer> getFailCount(String username, String code);

    /**
     * 查询用户信息
     *
     * @param username 用户名
     * @param code     租户集团号
     * @return 用户信息
     */
    //@PostMapping("/user/feign/getUserByCode")
    Result<UserDTO> getUserByCode(String username, String code);

    /**
     * 根据租户ID和登录账号获取用户信息
     *
     * @param tenantId 租户id
     * @param userName 登录名
     * @return 用户信息
     */
    //@PostMapping("/user/feign/getUserByTenantIdAndUserName")
    Result<UserDTO> getUserByTenantIdAndUserName(String tenantId, String userName);


    /**
     * 重置用户登录错误次数
     *
     * @param username 登录账号
     * @param code     租户集团号
     * @return 修改结果
     */
    //@PostMapping("/user/feign/resetCount")
    Result<Boolean> resetCount(String username, String code);

    /**
     * 重置用户锁定状态
     *
     * @param username 登录账号
     * @param code     租户集团号
     * @return 修改结果
     */
    //@PostMapping("/user/feign/resetStatus")
    Result<Boolean> resetStatus(String username, String code);

    /**
     * 保存用户登陆日志
     *
     * @param logLogin 登陆日志
     * @return 修改结果
     */
    //@PostMapping("/user/feign/saveLoginLog")
    Result<Boolean> saveLoginLog(LogLoginDTO logLogin);
    
    
    //===============================manager
    

    /**
     * 获取用户登录后可访问的API资源列表
     *
     * @return 用户登录后可访问的API资源列表
     */
//    @GetMapping(value = "/resource/feign/afterLoginAccessApiResources")
    Result<List<ApiResourceDTO>> getAfterLoginAccessApiResources();

    /**
     * 查询所有API资
     * @return 租户的API资源
     */
    List<ApiResourceDTO> getResources();
    List<ApiResourceDTO> getResourcesV1(String tenantId);
    /**
     * 查询系统配置
     *
     * @param service  服务名称
     * @param type     类型
     * @param name     字段名
     * @param tenantId 租户编码
     * @return 配置对象
     */
//    @GetMapping("/config/feign/{service}/{type}/{name}/{tenantId}")
    Result<ConfigDTO> getConfig(String service,String type,String name,String tenantId);

    Result<ConfigDTO> getConfig(String type,String name);

    /**
     * 通过租户ID和用户账号查询分配用户的API资源
     *
     * @param tenantId 租户ID
     * @param username 用户
     * @return 资源
     */
//    @GetMapping(value = "/resource/feign/assignedUserApiResources")
    Result<List<ApiResourceDTO>> getAssignedUserApiResources(String tenantId,String username);

    /**
     * 根据传入的 loginNames，返回 k=loginName, v=username
     * @param loginNames
     * @return
     */
    Map<String, String> getUserNameByLoginNames(List<String> loginNames);

    /**
     * 根据传入的 ids ，返回 k=id, v=username
     * @param ids
     * @return
     */
    Map<String, String> getUserNameByIds(List<String> ids);

    List<UserAndCorpDTO> getUserNameAndCorpNameByUserIds(List<String> userIds);

    /**
     * 根据传入的 ids , 返回 k=id, v=loginName
     * @param operatorCodes
     * @return
     */
    Map<String, String> getLoginNameByIds(List<String> operatorCodes);
    
    /**
          * 根据登录名获取用户，登录名字段唯一索引 
     * @param username
     * @return
     */
    UserDTO getByUsername(String username);

    Map<String, UserDTO> listUserByGpId(List<String> ids);
    Map<String, UserDTO> listUser();

    /**
     * 判断用户是否有某资源权限
     * @param uid
     * @param permission
     * @return
     */
    Boolean hasPermission(String uid,String permission);
    Boolean hasPermissionV1(String userId, String permission,String tenantId);
    /**
     * 根据角色编号获取角色信息。
     *
     * @param roleId 角色编号。
     * @return 角色信息。
     */
    RoleDTO getRoleById(String roleId);

    /**
     * 根据角色编号集合，获取角色信息。
     *
     * @param roleIds 角色编号集合。
     * @return 角色信息。
     */
    Map<String,RoleDTO> listRoleByIds(List<String> roleIds);
    
    String getLoginName();
    
    String getCadnByLoginName();
    
    String getUserCodeByCadn(String cadn);
    
    boolean saveLoginLog(LoginLogDTO loginLogDTO);

    List<UserRoleDTO> getUserRole(Set<String> userIds);

}
