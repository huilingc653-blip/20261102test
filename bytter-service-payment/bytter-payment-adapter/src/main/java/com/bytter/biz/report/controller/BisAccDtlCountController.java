package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.BisAccDtlCountService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/report/bisaccdtlCount")
public class BisAccDtlCountController extends BaseController {
    @Autowired
    private BisAccDtlCountService bisAccDtlCountService;

    @GetMapping("/list")
    public String list(Model model) {
        Date now = DateUtils.getDate();
        model.addAttribute("beginDate", DateUtils.format(DateUtils.getMonthFirstDate(now),"yyyy-MM-dd"));
        model.addAttribute("endDate", DateUtils.format(DateUtils.getLastMonthDate(now),"yyyy-MM-dd"));
        return "report/listBisAccDtlCount";
    }

    @PostMapping("/datatable")
    @ResponseBody
    @LogMessage(content = "账户业务笔数汇总表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput datatableData(@RequestBody DataTablesInput dataTablesInput){
        return  bisAccDtlCountService.datatableData(dataTablesInput,this.getCurrentUser());
    }
}
