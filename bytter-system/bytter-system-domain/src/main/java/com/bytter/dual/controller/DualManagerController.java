package com.bytter.dual.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.utils.StringUtils;
import com.bytter.dual.component.*;
import com.bytter.dual.service.DualManagerService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IFcAccService;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.FcAccDTO;
import com.bytter.system.entity.Corp;
import com.bytter.system.entity.Role;
import com.bytter.system.entity.User;
import com.bytter.system.entity.UserRole;
import com.bytter.system.service.CorpService;
import com.bytter.system.service.ResourcesService;
import com.bytter.system.service.RoleService;
import com.bytter.system.service.UserService;
import com.bytter.system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
/**
 * Created by zhoul on 2017/9/1.
 */
@Controller
@RequestMapping("/dual/manager")
public class DualManagerController extends BaseController {
    @Resource
    private DualManagerService dualManagerService;
    @Resource
    private UserService userService;
    @Resource
    private CorpService corpService;
    @Resource
    private RoleService roleService;
    @Resource
    private ResourcesService resourcesService;
    @Autowired(required = false)
    private IBtBankAccService btBankAccServiceI;
    @Autowired(required = false)
    private IFcAccService fcAccServiceI;
    @GetMapping("/approveList")
    public void approveList(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
    }
    @PostMapping("/tableGrid.json")
    @ResponseBody
    public TableGridResponse<WorkFlowPersistence> todoListGrid(@RequestBody TableGridInput tableGridInput, HttpServletRequest request) {
        //审批列表
        return new TableGridResponse<>();
    }
    /**
     * 重置密码
     *
     * @param taskId
     * @param id
     * @param model
     */
    @GetMapping("/resetPassWord")
    public void resetPassWord(String taskId, String id, Model model) {
        String userId = dualManagerService.getSerializableData(ResetPasswordComponent.class, id);
        User user = userService.get(userId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("user", user);
    }
    /**
     * 角色授权
     *
     * @param taskId
     * @param id
     * @param model
     */
    @GetMapping("/roleAssign")
    public void roleAssign(String taskId, String id, Model model) {
        Map<String, String> map = dualManagerService.getSerializableData(RoleAssignComponent.class, id);
        model.addAttribute("taskId", taskId);
        model.addAttribute("newMenuIds", map.get("menuIds"));
        model.addAttribute("newResourcesIds", map.get("resourcesIds"));
        String roleId = map.get("roleId");
        List<String> resourceIdByRoleId = resourcesService.findResourceIdByRoleId(roleId);
        List<String> menuIdByRoleId = roleService.findMenuIdByRoleId(roleId);
        model.addAttribute("hiddenMenuIds", StringUtils.join(menuIdByRoleId.toArray(), ","));
        model.addAttribute("hiddenResourcesIds", StringUtils.join(resourceIdByRoleId.toArray(), ","));
        model.addAttribute("role", roleService.get(roleId));
    }
    /**
     * 角色分配用户
     *
     * @param taskId
     * @param id
     * @param model
     */
    @GetMapping("/roleAssignUser")
    public void roleAssignUser(String taskId, String id, Model model) {
        Map<String, Object> serializableData = dualManagerService.getSerializableData(RoleAssignUserComponent.class, id);
        String roleId = (String) serializableData.get("roleId");
        Role role = roleService.get(roleId);
        StringBuilder sb = new StringBuilder();
        List<Map<String, String>> data = (List<Map<String, String>>) serializableData.get("data");
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> map = data.get(i);
            String userId = map.get("userId");
            sb.append(userId);
            if (i != data.size() - 1) {
                sb.append(",");
            }
        }
        model.addAttribute("taskId", taskId);
        model.addAttribute("role", role);
        model.addAttribute("userIds", sb.toString());
    }
    /**
     * 特殊权限
     *
     * @param taskId
     * @param id
     * @param model
     */
    @GetMapping("/assignResource")
    public void assignResource(String taskId, String id, Model model) {
        Map<String, Object> serializableData = dualManagerService.getSerializableData(AssignResourceComponent.class, id);
        String userId = (String) serializableData.get("userId");
        String corpId = (String) serializableData.get("corpId");
        User user = userService.get(userId);
        Corp corp = corpService.get(corpId);
        model.addAttribute("taskId", taskId);
        model.addAttribute("user", user);
        model.addAttribute("corp", corp);
        model.addAttribute("newResourcesIds", serializableData.get("resources"));
        model.addAttribute("newMenuIds", serializableData.get("menus"));
    }
    /**
     * 特殊权限复制
     *
     * @param taskId
     * @param id
     * @param model
     */
    @GetMapping("/assignResourceCopy")
    public void assignResourceCopy(String taskId, String id, Model model) {
        Map<String, Object> serializableData = dualManagerService.getSerializableData(AssignResourceCopyComponent.class, id);
        String userId = (String) serializableData.get("userId");
        User user = userService.get(userId);
        String sourceCorpId = (String) serializableData.get("sourceCorpId");
        Corp sourceCorp = corpService.get(sourceCorpId);
        List<String> targetCorpIds = (List<String>) serializableData.get("corps");
        List<Corp> targetCorps = corpService.listByIds(targetCorpIds);
        model.addAttribute("taskId", taskId);
        model.addAttribute("user", user);
        model.addAttribute("sourceCorp", sourceCorp);
        model.addAttribute("targetCorps", targetCorps);
    }
    /**
     * 复制角色
     *
     * @param taskId
     * @param id
     * @param model
     */
    @GetMapping("/copyRoles")
    public void copyRoles(String taskId, String id, Model model) {
        Map<String, Object> serializableData = dualManagerService.getSerializableData(CopyRolesComponent.class, id);
        String userId = (String) serializableData.get("userId");
        List<String> ids = (List<String>) serializableData.get("ids");
        User sourceUser = userService.get(userId);
        Set<UserRole> userRoles = sourceUser.getUserRoles();
        List<Role> roleList = new ArrayList<>(userRoles.size());
        for (UserRole userRole : userRoles) {
            roleList.add(userRole.getRole());
        }
        Collections.sort(roleList, new Comparator<Role>() {
            @Override
            public int compare(Role o1, Role o2) {
                return (int) (o1.getCreateTime().getTime() - o2.getCreateTime().getTime());
            }
        });
        List<UserVO> userList = userService.listByUserIdsSimple(ids);
        model.addAttribute("taskId", taskId);
        model.addAttribute("sourceUser", sourceUser);
        model.addAttribute("sourceUserRoles", roleList);
        model.addAttribute("userList", userList);
    }
    @GetMapping("/accountGrant")
    public void accountGrant(String taskId, String id, Model model) {
        Map<String, Object> variables = dualManagerService.getSerializableData(AccountGrantComponent.class, id);
        List<Map<String, Object>> accountGrantList = (List<Map<String, Object>>) variables.get("accountGrantList");
        List<Map<String, Object>> accountGrants = new ArrayList<>();
        for (Map<String, Object> accountGrant : accountGrantList) {
            String accountId = (String) accountGrant.get("accountId");
            Integer accountType = (Integer) accountGrant.get("accountType");
            String userId = (String) accountGrant.get("userId");
            String corpId = (String) accountGrant.get("corpId");
            User user = userService.get(userId, true);
            accountGrant.put("username", user.getUsername());
            Corp corp = corpService.get(corpId, true);
            accountGrant.put("corpName", corp.getName());
            if (accountType == 1) {
                FcAccDTO fcAccDTO = fcAccServiceI.getFcAccDTO(accountId);
                if (null != fcAccDTO) {
                    accountGrant.put("account", fcAccDTO.getBlnctrAcc());
                    accountGrant.put("accountName", fcAccDTO.getAccName());
                } else {
                    accountGrant.put("accountName", "账号已删除");
                }
            } else {
                BtBankAccDTO bankAcc = btBankAccServiceI.getBankAccById(accountId);
                if (null != bankAcc) {
                    accountGrant.put("account", bankAcc.getBankAcc());
                    accountGrant.put("accountName", bankAcc.getAccName());
                } else {
                    accountGrant.put("accountName", "账号已删除");
                }
            }
            accountGrants.add(accountGrant);
        }
        model.addAttribute("taskId", taskId);
        model.addAttribute("accountGrantList", accountGrants);
    }
    @GetMapping("/userAssignRole")
    public void userAssignRole(String taskId, String id, Model model) {
        Map<String, Object> variables = dualManagerService.getSerializableData(AccountGrantComponent.class, id);
        String userId = (String) variables.get("userId");
        User user = userService.get(userId);
        //获取改变后的角色
        List<String> newRoleIds = (List<String>) variables.get("roleIds");
        List<Role> newRoleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(newRoleIds)) {
            List<Role> roles = roleService.listByIds(newRoleIds);
            newRoleList.addAll(roles);
        }
        //获取改变之前的角色
        List<Role> oldRoleList = new ArrayList<>();
        Set<UserRole> userRoles = user.getUserRoles();
        for (UserRole userRole : userRoles) {
            oldRoleList.add(userRole.getRole());
        }
        List<Role> addRoleList = new ArrayList<>();
        List<Role> unChangeRoleList = new ArrayList<>();
        List<Role> deleteRoleList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(oldRoleList) && !CollectionUtils.isEmpty(newRoleList)) {
            //未改变的角色
            for (Role oldRole : oldRoleList) {
                for (Role newRole : newRoleList) {
                    if (oldRole.getId().equals(newRole.getId())) {
                        unChangeRoleList.add(oldRole);
                    }
                }
            }
            //新增的角色
            makeRoleState(newRoleList, oldRoleList, addRoleList);
            //删除的角色
            makeRoleState(oldRoleList, newRoleList, deleteRoleList);
        } else {
            //新增的角色
            if (CollectionUtils.isEmpty(oldRoleList) && !CollectionUtils.isEmpty(newRoleList)) {
                addRoleList.addAll(newRoleList);
            }
            //删除的角色
            if (!CollectionUtils.isEmpty(oldRoleList) && CollectionUtils.isEmpty(newRoleList)) {
                deleteRoleList.addAll(oldRoleList);
            }
        }
        model.addAttribute("taskId", taskId);
        model.addAttribute("user", user);
        model.addAttribute("addRoleList", addRoleList);
        model.addAttribute("unChangeRoleList", unChangeRoleList);
        model.addAttribute("deleteRoleList", deleteRoleList);
    }
    private void makeRoleState(List<Role> roleListA, List<Role> roleListB, List<Role> roleContainer) {
        for (Role roleA : roleListA) {
            boolean exist = false;
            for (Role roleB : roleListB) {
                if (roleB.getId().equals(roleA.getId())) {
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                roleContainer.add(roleA);
            }
        }
    }
}
