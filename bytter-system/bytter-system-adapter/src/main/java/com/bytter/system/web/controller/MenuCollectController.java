package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.Ii18nService;
import com.bytter.system.entity.Menu;
import com.bytter.system.entity.MenuCollect;
import com.bytter.system.service.MenuCollectService;
import com.bytter.system.service.MenuService;
import com.bytter.system.vo.MenuCollectVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by zhoul on 2017/10/23.
 */
@Controller
@RequestMapping("/sys/menuCollect")
public class MenuCollectController extends BaseController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuCollectService menuCollectService;
    @Autowired
    private Ii18nService i18nServiceI;
    @GetMapping("/collect")
    @ResponseBody
    public Object collect(String menuId) {
        menuCollectService.collect(menuId, getUserId());
        return success();
    }
    /**
     * 进行收藏菜单置顶操作
     *
     * @param id 收藏菜单ID
     */
    @PostMapping("/menuTop")
    @ResponseBody
    public AjaxJson menuTop(String id) {
        menuCollectService.menuTop(id, getUserId());
        return success();
    }
    @PostMapping("/widget")
    public void widget(Model model) {
        List<MenuCollect> menuCollects = menuCollectService.listByUserId(getUserId());
        List<MenuCollectVO> menuCollectVOs = new ArrayList<>();
        for (int i = 0; i < menuCollects.size(); i++) {
            MenuCollect menuCollect = menuCollects.get(i);
            String parentIds = menuCollect.getParentIds();
            if (StringUtils.isBlank(parentIds)) {
                continue;
            }
            String[] parentIdsArr = parentIds.split(CommonConstant.COMMA_SPLIT);
            StringBuilder sb = new StringBuilder();
            Menu topMenu = null;
            for (int j = 0; j < parentIdsArr.length; j++) {
                Menu menu = menuService.get(parentIdsArr[j]);
                String baseContext = i18nServiceI.getBaseContext(parentIdsArr[j]);
                if (StringUtils.isNotBlank(baseContext)) {
                    sb.append(baseContext);
                } else {
                    sb.append(menu.getName());
                }
                if (j < parentIdsArr.length - 1) {
                    sb.append(" - ");
                }
                //顶级菜单
                if (StringUtils.isBlank(menu.getParentId())) {
                    topMenu = menu;
                }
            }
            Menu currentMenu = menuService.get(menuCollect.getMenuId());
            menuCollect.setParentNames(sb.toString());
            String baseContext = i18nServiceI.getBaseContext(currentMenu.getId());
            if (StringUtils.isNotBlank(baseContext)) {
                menuCollect.setName(baseContext);
            } else {
                menuCollect.setName(currentMenu.getName());
            }
            menuCollects.set(i, menuCollect);
            MenuCollectVO menuCollectVO = new MenuCollectVO();
            menuCollectVO.setTopParentId(topMenu.getId());
            menuCollectVO.setTopParentName(topMenu.getName());
            BeanUtils.copyNotNullProperties(menuCollect, menuCollectVO);
            menuCollectVOs.add(menuCollectVO);
        }
        model.addAttribute("menuCollects", menuCollectVOs);
    }
    /**
     * 常用菜单拖拽保存
     *
     * @return
     */
    @PostMapping("/dragSaveMenuWidget")
    @ResponseBody
    public AjaxJson dragSaveMenuWidget(@RequestBody List<MenuCollect> menuCollectList) {
        menuCollectService.saveDragWidget(menuCollectList, getUserId());
        return success();
    }
}
