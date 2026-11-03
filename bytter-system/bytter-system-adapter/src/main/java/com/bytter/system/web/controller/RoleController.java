package com.bytter.system.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.jqgrid.TableGridInput;
import com.bytter.core.common.jqgrid.TableGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.enums.RoleMessageEnum;
import com.bytter.system.entity.Role;
import com.bytter.system.service.ResourcesService;
import com.bytter.system.service.RoleService;
import com.bytter.system.vo.RoleVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhoul on 2017/4/11.
 */
@Controller
@RequestMapping("/sys/role")
public class RoleController extends BaseController {
    @Resource
    private RoleService roleService;
    @Resource
    private ResourcesService resourcesService;
    @GetMapping("/index")
    public void index() {
    }
    @PostMapping("/tableGrid.json")
    @ResponseBody
    public TableGridResponse<RoleVo> userGrid(@RequestBody TableGridInput tableGridInput) {
        return roleService.getAllRole(tableGridInput);
    }
    @GetMapping("/list")
    @ResponseBody
    public Object list() {
        return roleService.listAll();
    }
    @GetMapping("/specialRole")
    @ResponseBody
    public Object specialRole(String corpId) {
        return roleService.findSpecialRoleByCorpId(corpId);
    }
    @GetMapping("/view")
    public void view(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            Role role = roleService.get(id);
            model.addAttribute("role", role);
        }
    }
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(Role role) {
        roleService.update(role);
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(Role role) {
        roleService.save(role, getUserId());
        return success();
    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        roleService.delete(id);
        return success();
    }
    @GetMapping("/assign")
    public void assign(String id, Model model) {
        Role role = roleService.get(id);
        List<String> menuIdByRoleId = roleService.findMenuIdByRoleId(id);
        List<String> resourceIdByRoleId = resourcesService.findResourceIdByRoleId(id);
        model.addAttribute("hiddenMenuIds", StringUtils.join(menuIdByRoleId.toArray(), ","));
        model.addAttribute("hiddenResourcesIds", StringUtils.join(resourceIdByRoleId.toArray(), ","));
        model.addAttribute("role", role);
    }
    @PostMapping("/assign")
    @ResponseBody
    public AjaxJson assignPost(String menuIds, String resourcesIds, Role role) {
        if (StringUtils.isEmpty(menuIds)) {
            throw new ParameterException(RoleMessageEnum.SYS_ROLE_PLEASE_CHOICE_MENU_OR_RESOURCE);
        }
        Map<String, String> map = new HashMap<>();
        map.put("roleId", role.getId());
        map.put("menuIds", menuIds);
        map.put("resourcesIds", resourcesIds);
        roleService.assign(role, menuIds, resourcesIds);
        return success();
    }
    /**
     * 角色分配用户界面
     *
     * @param id    角色ID
     * @param model 上下文模型
     */
    @GetMapping("/assignUser")
    public void assignUser(String id, Model model) {
        model.addAttribute("role", roleService.get(id));
    }
    @GetMapping("/roleSelectedUser")
    @ResponseBody
    public Object getCorpUsers(String corpId, String roleId) {
        return roleService.listUserByCorpAndRole(corpId, roleId);
    }
    /**
     * 通过角色id和网点id获得具有"工作流-待办事项-流程审批"权限的用户信息
     *
     * @param corpId
     * @param roleId
     * @return
     */
    @GetMapping("/roleSpecialUser")
    @ResponseBody
    public Object roleSpecialUser(String corpId, String roleId, String resourceContent) {
        return roleService.findApproveUserByCorpIdAndRoleId(corpId, roleId, resourceContent);
    }
    /**
     * 角色分配用户
     *
     * @param data 用户数据集合
     * @return 成功标识
     * todo 双管理员问题
     */
    @PostMapping("/assignUser")
    @ResponseBody
    public AjaxJson assignUserPost(@RequestBody Map<String, Object> data) {
        String roleId = (String) data.get("roleId");
        if (StringUtils.isBlank(roleId)) {
            throw new ParameterException("Illegal operation, wrong parameter");
        }
        roleService.assignUser(data);
        return success();
    }
    /**
     * 银行类型维护列表
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<RoleVo> json() {
        return roleService.listAllRole();
    }
}
