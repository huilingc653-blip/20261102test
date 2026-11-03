package com.bytter.bill.web.billPool.controller;
import com.bytter.bill.domain.billPool.service.AbPoolBailBookService;
import com.bytter.bill.api.billPool.constant.AbPoolConstant;
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
/**
 * @Title: AbPoolBailBookController
 * @Description: 保证金登记簿Controller
 * @author wangzy
 * @date 2020/3/17 17:09
 *
 */
@Controller
@RequestMapping("/billPool/bailBook")
public class AbPoolBailBookController extends BaseController {
    @Autowired
    private AbPoolBailBookService abPoolBailBookService;
    /**
     * 保证金登记簿列表页面跳转
     * @param model
     * @return
     */
    @GetMapping("/listQuery")
    public String listAbPoolBailBookVO(Model model) {
        model.addAttribute("startDate", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("endDate", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "billPool/listAbPoolBailBook";
    }
    /**
     * 保证金登记簿 table列表
     * @param input 查询条件
     * @return
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询保证金登记簿", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return abPoolBailBookService.getDataTablesUnion(input);
    }
    /**
     * 查看保证金登记簿详情页面
     *
     * @return
     */
    @LogMessage(content = "查看保证金登记簿详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/view")
    public String view(String id, Model model) {
        model.addAttribute("abPoolBailBookVO",abPoolBailBookService.getAbPoolBailBookVOByDtlId(id));
        return "billPool/viewAbPoolBailBook";
    }
}
