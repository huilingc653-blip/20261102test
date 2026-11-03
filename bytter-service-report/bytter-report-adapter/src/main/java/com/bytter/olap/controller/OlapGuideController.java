package com.bytter.olap.controller;

import com.bytter.core.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 自定义报表导航Controller
 * Created by chenlw on 2019/11/25.
 */
@Controller
public class OlapGuideController extends BaseController {

    /**
     * 数据集管理
     *
     * @param model model
     * @return pageName
     */
    @GetMapping("/olap/dataset")
    public String dataset(Model model) {
        model.addAttribute("targetPageUrl", "config.dataset");
        return "starter";
    }


    /**
     * 数据集管理
     *
     * @param model model
     * @return pageName
     */
    @GetMapping("/olap/importDataset")
    public String importDataset(Model model) {
        model.addAttribute("targetPageUrl", "config.importDataset");
        return "starter";
    }


    /**
     * 图表管理
     *
     * @param model model
     * @return pageName
     */
    @GetMapping("/olap/widget")
    public String widget(Model model) {
        model.addAttribute("targetPageUrl", "config.widget");
        return "starter";
    }

    /**
     * 看板分类管理
     *
     * @param model model
     * @return pageName
     */
    @GetMapping("/olap/category")
    public String category(Model model) {
        model.addAttribute("targetPageUrl", "config.category");
        return "starter";
    }

    /**
     * 图表看板管理
     *
     * @param model model
     * @return pageName
     */
    @GetMapping("/olap/board")
    public String board(Model model) {
        model.addAttribute("targetPageUrl", "config.board");
        return "starter";
    }

    /**
     * 我的图表看板
     *
     * @param model model
     * @return pageName
     */
    @GetMapping("/olap/dashboardList")
    public String dashboardList(Model model) {
        model.addAttribute("targetPageUrl", "dashboard.dashboardList");
        return "starter";
    }


    /**
     * 查看图表看板
     *
     * @param id    看板ID
     * @param model model
     * @return pageName
     */
    @GetMapping("/olap/dashboardView")
    public String dashboardView(@RequestParam("boardId") String id, Model model) {
        model.addAttribute("targetPageUrl", "mine.view");
        model.addAttribute("boardId", id);
        return "starter";
    }

}
