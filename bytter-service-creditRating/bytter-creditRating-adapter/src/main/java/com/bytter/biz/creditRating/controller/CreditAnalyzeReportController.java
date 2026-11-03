package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.EvaluationReportService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 功能介绍 : 综合分析报表<br>
 * <b>日期：</b>2024/12/30<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Controller
@RequestMapping("/credit/analyzeReport")
public class CreditAnalyzeReportController extends BaseController {
    @Autowired
    private EvaluationReportService evaluationReportService;
    /**
     * 列表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_ANALYZEREPORT_QUERY)
    @LogMessage(content = "综合分析报表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/listAnalyzeReport";
    }

    /**
     *  table列表
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_ANALYZEREPORT_QUERY)
    @PostMapping("/getAnalyzeDataTables")
    @ResponseBody
    public DataTablesOutput getAnalyzeDataTables(@RequestBody DataTablesInput dataTablesInput) {
        return evaluationReportService.getAnalyzeDataTables(dataTablesInput);
    }
}
