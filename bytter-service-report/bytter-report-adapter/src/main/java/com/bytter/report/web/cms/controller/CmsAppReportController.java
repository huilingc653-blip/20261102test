package com.bytter.report.web.cms.controller;

import com.bytter.core.base.BaseController;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author luxy
 * @Title: CmsAppReportController
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/5/20 11:32
 */
@Controller
@RequestMapping("/cms/appReport")
public class CmsAppReportController{

    @PostMapping("/reportList")
    public String mobileBalanceList(Model model,String corpId) {
        return "cms/report/mobile/appCmsReportIndex";
    }
}
