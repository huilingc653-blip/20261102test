package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.GroupTransferReportService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Description: 集团资金划拨报表
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/7
 */
@Controller
@RequestMapping("/report/nisAllocationReq")
public class GroupTransferReportController extends BaseController {
    @Autowired
    private GroupTransferReportService service;

    // 年度分析表
    @GetMapping("/yearList")
    public String yearList(Model model) {
        model.addAttribute("queryYear", DateUtils.format(new Date(),"yyyy"));
        model.addAttribute("beforeYear", DateUtils.format(DateUtils.addYear(new Date(),-1),"yyyy"));
        return "report/groupTransferYearList";
    }

    // 年度分析表
    @PostMapping("/yearDatatable")
    @ResponseBody
    public DataTablesOutput yearDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return service.yearDatatable(dataTablesInput, this.getCurrentUser());
    }
}
