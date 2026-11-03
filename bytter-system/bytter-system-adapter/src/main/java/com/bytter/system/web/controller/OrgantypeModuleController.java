package com.bytter.system.web.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.service.OrgantypeModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Controller
 * @Description: 组织架构对应关系维护表跳转操作
 * @date 2020-05-14 16:01:41
 */
@Controller
@RequestMapping("/sys/organtype/orgtmodule")
public class OrgantypeModuleController extends BaseController {
    @Autowired
    private OrgantypeModuleService organtypeModuleService;
    /**
     * 组织架构对应关系维护页面跳转
     *
     * @return
     */
    @GetMapping("/orgtConfig")
    public String orgtConfig(Model model) {
        model.addAttribute("config", organtypeModuleService.listOrgtModule());
        return "sys/organtype/organtypeModuleConfig";
    }
    /**
     * 组织架构对应关系维护保存
     *
     * @param request
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Object moduleSave(HttpServletRequest request) {
        Map<String, String[]> map = new HashMap<String, String[]>();
        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            if (StringUtils.isNotEmpty(e.getValue())) {
                map.put(e.getKey(), e.getValue());
            }
        }
        if (map.size() > 0) {
            organtypeModuleService.saveOrUpdate(map, this.getCurrentUser());
        }
        return success();
    }
}
