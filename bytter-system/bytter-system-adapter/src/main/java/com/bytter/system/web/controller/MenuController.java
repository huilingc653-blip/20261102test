package com.bytter.system.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.jqgrid.TreeGridResponse;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.*;
import com.bytter.system.api.ILicenseService;
import com.bytter.system.entity.Menu;
import com.bytter.system.entity.MenuCollect;
import com.bytter.system.entity.Resources;
import com.bytter.system.service.MenuCollectService;
import com.bytter.system.service.MenuService;
import com.bytter.system.service.ResourcesService;
import com.bytter.system.vo.MenuIndexVO;
import com.bytter.system.vo.MenuResourceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
/**
 * Created by zhoul on 2017/4/11.
 */
@Controller
@RequestMapping("/sys/menu")
@Slf4j
public class MenuController extends BaseController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private ResourcesService resourcesService;
    @Autowired
    private ILicenseService licenseService;
    @Autowired
    private MenuCollectService menuCollectService;
    @GetMapping("/index")
    public void index(Model model) throws IOException {
        List<Menu> menuList = menuService.getTreeTable();
        model.addAttribute("menus",menuList);
    }
    @GetMapping("/tree.json")
    @ResponseBody
    public Object treeJson() throws IOException {
        TreeGridResponse treeGridResponse  = menuService.getTreeTable1();
        log.info("treeGridResponse======"+treeGridResponse.toString());
        return treeGridResponse;
    }
    //    
    @GetMapping("/json")
    @ResponseBody
    public Object json(String id, String resourceSize, String type) throws Exception {
        if (StringUtils.isNotBlank(resourceSize) && !"0".equals(resourceSize)) {
            List<Resources> resources = resourcesService.listByMenuId(id);
            List<MenuResourceVO> resourceVO = BeanUtils.copyListProperties(resources, MenuResourceVO.class);
            return resourceVO;
        }
        //type不为空视为受license控制
        if (StringUtils.isNotBlank(type)) {
            return menuService.listLicenseControlByParentId(id);
        } else {
            return menuService.listByParentId(id);
        }
    }
    //    
    @GetMapping("/tree")
    @ResponseBody
    public Object tree() throws Exception {
        List<Menu> menuList = menuService.getTreeTable();
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Menu menu : menuList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", menu.getId());
            map.put("pId", menu.getParentId());
            map.put("name", menu.getName());
            tempList.add(map);
        }
        return tempList;
    }
    //    
    @GetMapping("/treeAuth")
    @ResponseBody
    public Object treeAuth() throws Exception {
        List<Menu> menuList = menuService.getTreeTable();
        List<Map<String, Object>> tempList = new ArrayList<>();
        for (Menu menu : menuList) {
            //todo licencse控制移除
            /*if (!"1".equals(String.valueOf(menu.getIsExtend()))
                    //&& !licenseService.isMenusAuth(menu.getId())
            ) {
                continue;
            }*/
            Map<String, Object> map = new HashMap<>();
            map.put("id", menu.getId());
            map.put("pId", menu.getParentId());
            map.put("name", menu.getName());
            tempList.add(map);
        }
        return tempList;
    }
    //    
    @GetMapping("/treeAndResources")
    @ResponseBody
    public Object treeAndResources(String id, String type) {
        return menuService.getTreeAndResources(id, type);
    }
    @GetMapping("/view")
    public String view(String id, String parentId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            Menu menu = menuService.get(id);
            model.addAttribute("menu", menu);
        }
        if (StringUtils.isNotEmpty(parentId)) {
            Menu menu = new Menu();
            menu.setParentId(parentId);
            model.addAttribute("menu", menu);
        }
        return "sys/menu/view";
    }
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(Menu menu) {
        menuService.update(menu, getUserId(),getCurrentUser().getTenantId());
        return success();
    }
    @PostMapping("/updateSort")
    @ResponseBody
    public AjaxJson updateSort(String id, Integer sort) {
        if (sort == null) {
            return success();
        }
        Menu menu = menuService.get(id);
        menu.setSort(sort);
        menuService.update(menu, getUserId(),getCurrentUser().getTenantId());
        return success();
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(Menu menu) {
        menuService.save(menu, getUserId(),getCurrentUser().getTenantId());
        return success();
    }
    @GetMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        menuService.delete(id,getCurrentUser().getTenantId());
        return success();
    }
    //导出
    @RequestMapping("/export")
    public void export(HttpServletResponse response) {
        //只导出被用于模块分配的菜单（也就是模块菜单关系里面的菜单列去重）
        try {
            List<String> menuIdList = menuService.listMenuIdsAssgnInModule();
            //只要导出id就好了
            String jsonString = JsonUtils.toJSONString(menuIdList);
            //先base64，再加密
            String encrypt = Base64Util.encodeToString(jsonString.getBytes("UTF-8"));
            encrypt = RSAUtil.encrypt(encrypt);
            String filename = "menu.encrypt";
            byte[] exportBytes = encrypt.getBytes();
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(exportBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出菜单json文件失败");
        }
    }
    /**
     * 获取顶级菜单
     *
     * @return
     * @throws Exception
     */
    @GetMapping("/listTopMenu")
    @ResponseBody
    public List<Menu> listTopMenu() throws Exception {
        Locale locale = LocaleContextHolder.getLocale();
        return menuService.listTopMenu(locale.toString());
    }
    @GetMapping("/search")
    @ResponseBody
    public Collection<MenuIndexVO> search(String menuName) {
        //查询用户有权限的菜单
        List<MenuIndexVO> menus = menuService.listMenuIdsByUserId(getUserId());
        if (EmptyUtils.isEmpty(menus)) {
            return new ArrayList<>();
        }
        //菜单ID
        List<String> menusIds = new ArrayList<>();
        //用户权限范围内的菜单集合
        Map<String, MenuIndexVO> menuMap = new HashMap<>();
        //顶级菜单集合
        Map<String, MenuIndexVO> topMenuMap = new HashMap<>();
        for (MenuIndexVO menu : menus) {
            menusIds.add(menu.getId());
            menuMap.put(menu.getId(), menu);
            if (StringUtils.isBlank(menu.getParentId())) {
                topMenuMap.put(menu.getId(), menu);
            }
        }
        //过滤末级菜单
        List<MenuIndexVO> menusInfoTemp = menuService.listByIdsName(menusIds, menuName);
        List<MenuIndexVO> menusInfo = new ArrayList<>();
        if (EmptyUtils.isEmpty(menusInfoTemp)) {
            return menusInfo;
        } else {
            for (MenuIndexVO indexVO : menusInfoTemp) {
                //过滤末级菜单（包含url）
                if (StringUtils.isNotBlank(indexVO.getUrl())) {
                    menusInfo.add(indexVO);
                }
            }
        }
        //获取用户收藏的菜单集合
        List<MenuCollect> listCollect = menuCollectService.listByUserId(this.getUserId());
        //用户权限范围内的菜单集合
        Map<String, MenuCollect> menuCollectMap = new HashMap<>();
        if (EmptyUtils.isNotEmpty(listCollect)) {
            for (MenuCollect menu : listCollect) {
                menuCollectMap.put(menu.getMenuId(), menu);
            }
        }
        HashMap<String, List<MenuIndexVO>> map = new HashMap<>();
        for (MenuIndexVO menu : menusInfo) {
            if (menuCollectMap.containsKey(menu.getId())) {
                menu.setCollectFlag(CommonConstant.YES);
            } else {
                menu.setCollectFlag(CommonConstant.NO);
            }
            menu.setAbsoluteName(this.getAbsoluteName(menuMap, topMenuMap, menu.getId(), null));
            if (StringUtils.isNotBlank(menu.getAbsoluteName()) && menu.getAbsoluteName().length() > 3) {
                menu.setAbsoluteName(menu.getAbsoluteName().substring(0, menu.getAbsoluteName().length() - 3));
            }
            menu.setParentIds(this.getMenuParentIds(menuMap, menu.getId(), null));
            String[] parentIds = menu.getParentIds().split(CommonConstant.COMMA_SPLIT);
            for (String parentId : parentIds) {
                if (topMenuMap.containsKey(parentId)) {
                    List<MenuIndexVO> list = null;
                    if (map.containsKey(parentId)) {
                        list = map.get(parentId);
                    } else {
                        list = new ArrayList<>();
                    }
                    //设置顶级菜单
                    menu.setTopParentId(parentId);
                    menu.setTopParentName(topMenuMap.get(parentId).getName());
                    list.add(menu);
                    map.put(parentId, list);
                    break;
                }
            }
        }
        Collection<MenuIndexVO> result = new ArrayList<>();
        for (String key : map.keySet()) {
            MenuIndexVO index = topMenuMap.get(key);
            index.setChildren(map.get(key));
            result.add(index);
        }
        return result;
    }
    /**
     * 设置末级菜单全路径
     *
     * @param menuMap
     */
    private String getAbsoluteName(Map<String, MenuIndexVO> menuMap, Map<String, MenuIndexVO> topMenuMap, String menuId, StringBuffer absoluteName) {
        if (absoluteName == null) {
            absoluteName = new StringBuffer();
        }
        MenuIndexVO menu = menuMap.get(menuId);
        if (menu != null) {
            if (!topMenuMap.containsKey(menu.getId())) {
                absoluteName.insert(0, " / ");
                absoluteName.insert(0, menu.getName());
            }
            if (StringUtils.isNotBlank(menu.getParentId())) {
                getAbsoluteName(menuMap, topMenuMap, menu.getParentId(), absoluteName);
            }
        }
        return absoluteName.toString();
    }
    /**
     * 设置末级菜单ID全路径
     *
     * @param menuMap
     */
    private String getMenuParentIds(Map<String, MenuIndexVO> menuMap, String menuId, StringBuffer parentIds) {
        if (parentIds == null) {
            parentIds = new StringBuffer();
        }
        MenuIndexVO menu = menuMap.get(menuId);
        if (menu != null) {
            parentIds.insert(0, CommonConstant.COMMA_SPLIT);
            parentIds.insert(0, menu.getId());
            if (StringUtils.isNotBlank(menu.getParentId())) {
                getMenuParentIds(menuMap, menu.getParentId(), parentIds);
            }
        }
        return parentIds.toString();
    }
}
