package com.bytter.biz.report.controller;

import com.bytter.biz.report.service.WeeklyFundsDueScheduleService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author zhous
 * @version 1.0
 * @Title: Controller
 * @Description: 每周资金到期用款规划表
 * @date 2024-10-24
 */
@Controller
@RequestMapping("/report/weeklyFundsDueSchedule")
public class WeeklyFundsDueScheduleController extends BaseController {

    @Autowired
    private WeeklyFundsDueScheduleService weeklyFundsDueScheduleService;

    /**
     * 每周资金到期用款规划表 页面跳转
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @GetMapping("/listWeeklyFundsDueSchedule")
    public String listWeeklyFundsDueSchedule(Model model) {
        Date startDate = cn.hutool.core.date.DateUtil.beginOfWeek(cn.hutool.core.date.DateUtil.date());
        Date endDate = cn.hutool.core.date.DateUtil.endOfWeek(cn.hutool.core.date.DateUtil.date());
        model.addAttribute("startDate",startDate);
        model.addAttribute("endDate",endDate);
        return "report/listWeeklyFundsDueSchedule";
    }

    /**
     * 加载每周资金到期用款规划表数据
     * @param dataTablesInput 查询入参
     * @return 返回列表数据信息
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQuery")
    @ResponseBody
    @LogMessage(content = "每周资金到期用款规划表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataQuery(@RequestBody DataTablesInput dataTablesInput){
        return weeklyFundsDueScheduleService.dataQuery(dataTablesInput, getCurrentUser());
    }
}