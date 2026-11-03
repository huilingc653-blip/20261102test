package com.bytter.system.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.entity.Resources;
import com.bytter.system.service.MenuService;
import com.bytter.system.service.ResourcesService;
import com.bytter.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * Created by zhoul on 2017/4/11.
 */
@Controller
@RequestMapping("/sys/resources")
public class ResourcesController extends BaseController {
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;
    @GetMapping("/index")
    public void index(Model model, String id) {
        model.addAttribute("list", resourcesService.listByMenuId(id));
        model.addAttribute("id",id);
    }
    @GetMapping("/view")
    public void view(String id, String menuId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            Resources resources = resourcesService.get(id,true);
            model.addAttribute("resources", resources);
        }
        if (StringUtils.isNotEmpty(menuId)) {
            Resources resources = new Resources();
            // Menu menu = new Menu();
            // menu.setId(menuId);
            // resources.setMenu(menu);
            resources.setMenuId(menuId);
            model.addAttribute("resources", resources);
        }
    }
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(Resources resources, String menuId) {
        if (StringUtils.isNotEmpty(resources.getId())) {
            Resources oldResources = resourcesService.get(resources.getId(), true);
            BeanUtils.copyNotNullProperties(resources, oldResources);
            oldResources.setMenuId(menuId);
            resourcesService.update(oldResources, getUserId(),getCurrentUser().getTenantId());
        }
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(Resources resources, String menuId) {
        // Menu menu = new Menu();
        // menu.setId(menuId);
        // resources.setMenu(menu);
        resources.setMenuId(menuId);
        resourcesService.save(resources, getUserId(),getCurrentUser().getTenantId());
        return success();
    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        resourcesService.delete(id,getCurrentUser().getTenantId());
        return success();
    }
    @GetMapping("/list")
    @ResponseBody
    public Object list() {
        return resourcesService.list();
    }
    /**
     * 检查该菜单资源是否被角色所关联使用
     *
     * @param resourceId
     * @return
     */
    @PostMapping("/checkUsedByRoles")
    @ResponseBody
    public AjaxJson checkUsedByRoles(String resourceId) {
        AjaxJson json = new AjaxJson();
        String result = resourcesService.checkUsedByRoles(resourceId);
        json.setObj(result);
        return json;
    }
}
