package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.Module;
import com.bytter.system.service.ModuleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 模块管理
 * Created by zhoul on 2017/4/11.
 */
@Controller
@RequestMapping("/sys/module")
public class ModuleController extends BaseController {
    @Resource
    private ModuleService moduleService;
    @GetMapping("/index")
    public void index() {
    }
    @GetMapping("/tree.json")
    @ResponseBody
    public Object treeJson() {
        List<Module> list = moduleService.getTreeTable();
        return new TreeGridResponse<>(list);
    }
    @GetMapping("/tree")
    @ResponseBody
    public Object tree() throws Exception {
        List<Module> menuList = moduleService.getTreeTable();
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Module module : menuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", module.getId());
            map.put("pId", module.getParentId());
            map.put("name", module.getName());
            tempList.add(map);
        }
        return tempList;
    }
    @GetMapping("/view")
    public String view(String id, String parentId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            Module module = moduleService.get(id);
            model.addAttribute("module", module);
        }
        if (StringUtils.isNotEmpty(parentId)) {
            Module module = new Module();
            module.setParentId(parentId);
            model.addAttribute("module", module);
        }
        return "sys/module/view";
    }
    @PostMapping("/updateSort")
    @ResponseBody
    public AjaxJson updateSort(String id, Integer sort) {
        if (sort == null) {
            return success();
        }
        Module module = moduleService.get(id);
        module.setSort(sort);
        moduleService.update(module);
        return success();
    }
    @PostMapping("/update")
    @ResponseBody
    public Object update(Module module) {
        moduleService.update(module);
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public Object save(Module module) {
        moduleService.save(module);
        return success();
    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") String id) {
        moduleService.delete(id);
        return success();
    }
    private void makeIdString(List<String> menuIdByRoleId, StringBuilder hiddenMenuIds) {
        for (int i = 0; i < menuIdByRoleId.size(); i++) {
            String menuId = menuIdByRoleId.get(i);
            hiddenMenuIds.append(menuId);
            if (i != menuIdByRoleId.size() - 1) {
                hiddenMenuIds.append(",");
            }
        }
    }
    @GetMapping("/assign")
    public void assign(String id, Model model) {
        Module module = moduleService.get(id);
        List<String> moduleMenuByModuleId = moduleService.listModuleMenuByModuleId(id);
        StringBuilder hiddenModuleMenuIds = new StringBuilder();
        makeIdString(moduleMenuByModuleId, hiddenModuleMenuIds);
        model.addAttribute("hiddenModuleMenuIds", hiddenModuleMenuIds.toString());
        model.addAttribute("module", module);
    }
    @PostMapping("/assign")
    @ResponseBody
    public AjaxJson assignPost(String menuIds, Module module) {
        moduleService.assign(module, menuIds);
        return success();
    }
}
