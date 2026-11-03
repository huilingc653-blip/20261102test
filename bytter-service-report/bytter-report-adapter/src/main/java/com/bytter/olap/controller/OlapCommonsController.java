package com.bytter.olap.controller;


import com.bytter.olap.dto.DashboardMenu;
import com.bytter.olap.services.PersistService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by yfyuan on 2016/7/25.
 */
@RestController
@RequestMapping("/olap/commons")
public class OlapCommonsController extends BaseOlapController {


    @Resource
    private PersistService persistService;

    private static List<DashboardMenu> menuList = new ArrayList<>();

    static {
        menuList.add(new DashboardMenu(1, -1, "SIDEBAR.CONFIG", "config"));
        //menuList.add(new DashboardMenu(2, 1, "SIDEBAR.DATA_SOURCE", "config.datasource"));
        menuList.add(new DashboardMenu(3, 1, "SIDEBAR.DATASET", "config.dataset"));
        menuList.add(new DashboardMenu(4, 1, "SIDEBAR.WIDGET", "config.widget"));
        menuList.add(new DashboardMenu(5, 1, "SIDEBAR.DASHBOARD", "config.board"));
        menuList.add(new DashboardMenu(6, 1, "SIDEBAR.DASHBOARD_CATEGORY", "config.category"));
        menuList.add(new DashboardMenu(7, 1, "SIDEBAR.IMPORT_DARASET", "config.importDataset"));
    }

    /**
     * 获取当前系统的语种设置
     * @return  中文返回zh_CN；英文返回en_US
     */
    @GetMapping("/locale")
    public String getLocale() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            logger.warn("can not found current request,return default locale [zh_CN]");
            return Locale.SIMPLIFIED_CHINESE.toString();
        }
        Locale locale = LocaleContextHolder.getLocale();
        if (!StringUtils.hasText(locale.getCountry()) && Locale.SIMPLIFIED_CHINESE.getLanguage().equals(locale.getLanguage())) {
            return Locale.SIMPLIFIED_CHINESE.toString();
        }
        return locale.toString();
    }



//    /**
//     * 获取操作菜单列表
//     *
//     * @return
//     */
//    @RequestMapping(value = "/getMenuList")
//    public List<DashboardMenu> getMenuList() {
//        return menuList;
//    }

//    /**
//     * 看板点击导出后，会调用此接口
//     * renderCtrl依赖此接口
//     *
//     * @param dataStr
//     * @return
//     */
//    @PostMapping(value = "/persist")
//    public String persist(@RequestBody String dataStr) {
//        dataStr = HtmlUtils.htmlUnescape(dataStr);
//        JSONObject data = JSONObject.parseObject(dataStr);
//        return persistService.persistCallback(data.getString("persistId"), data.getJSONObject("data"));
//    }


//    @PostMapping(value = "/persist")
//    public String persist(@RequestParam("persistId") String persistId,@RequestParam("dataJson") String dataJson) {
//        dataJson = HtmlUtils.htmlUnescape(dataJson);
//        JSONObject data = JSONObject.parseObject(dataJson);
//        return persistService.persistCallback(persistId, data);
//    }

}
