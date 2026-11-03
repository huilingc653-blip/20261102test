package com.bytter.biz.creditRating.controller;


import com.bytter.biz.creditRating.service.CrDataSourceService;
import com.bytter.biz.creditRating.vo.CrDataSourceVo;
import com.bytter.core.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 数据源来源
 */
@Controller
@RequestMapping("/creditRating/dataSource")
public class CrDataSourceController extends BaseController {

    @Autowired
    private CrDataSourceService crDataSourceService;

    @GetMapping("/list")
    public String list(String dataSourceName, Model model){
        List<CrDataSourceVo> data = crDataSourceService.getData(dataSourceName);
        model.addAttribute("dataSourceName",dataSourceName);
        model.addAttribute("list",data);
        return "creditRating/listDataSource";
    }
    @PostMapping("/getData")
    @ResponseBody
    public List<CrDataSourceVo> getData(String dataSourceName){
        return crDataSourceService.getData(dataSourceName);
    }
}
