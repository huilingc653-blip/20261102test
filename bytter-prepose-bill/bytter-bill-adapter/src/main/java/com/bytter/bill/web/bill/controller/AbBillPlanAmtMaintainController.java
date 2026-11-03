package com.bytter.bill.web.bill.controller;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.bytter.bill.domain.bill.entity.AbBillPlanAmtMaintain;
import com.bytter.bill.domain.bill.service.AbBillPlanAmtMaintainService;
import com.bytter.bill.domain.bill.vo.AbBillPlanAmtMaintainVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

/**
 * 银行计划开票金额维护
 *
 * @author fangcf
 * @date 2025-06-30
 */
@Controller
@RequestMapping("/bill/accebill/planAmtMaintain")
public class AbBillPlanAmtMaintainController extends BaseController {

    @Autowired
    private AbBillPlanAmtMaintainService abBillPlanAmtMaintainService;

    /**
     * 银行计划开票金额维护列表 页面跳转
     *
     * @return 银行计划开票金额维护列表跳转地址
     */
    @LogMessage(content = "银行计划开票金额维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbBillPlanAmtMaintain";
    }

    /**
     * 银行计划开票金额维护列表 table列表
     *
     * @return 出参
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillPlanAmtMaintainService.dataTable(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 银行计划开票金额维护列表新增页面跳转
     *
     * @return 银行计划开票金额维护列表跳转地址
     */
    @GetMapping("/add")
    public String add(Model model) {
        // “周开始日”必须是周一。获取2000年-2099年所有周一日期，然后日期字符串拼接
        String showAllMonday = abBillPlanAmtMaintainService.getMondays();
        model.addAttribute("showAllMonday", showAllMonday);
        return "bill/accebill/addAbBillPlanAmtMaintain";
    }

    /**
     * 银行计划开票金额维护列表编辑页面跳转
     *
     * @return 银行计划开票金额维护列表跳转地址
     */
    @GetMapping("/update")
    public String update(AbBillPlanAmtMaintainVO vo, Model model) {
        if (StringUtils.isNotEmpty(vo.getId())) {
            AbBillPlanAmtMaintain entity = abBillPlanAmtMaintainService.get(vo.getId(), true);
            model.addAttribute("planAmtPage", entity);
            // “周开始日”必须是周一。获取2000年-2099年所有周一日期，然后日期字符串拼接
            String showAllMonday = abBillPlanAmtMaintainService.getMondays();
            model.addAttribute("showAllMonday", showAllMonday);
        }
        return "bill/accebill/updateAbBillPlanAmtMaintain";
    }

    /**
     * 保存银行计划开票金额提交
     *
     * @param form 产品赎回表单
     * @return 操作结果
     */
    @LogMessage(content = "保存银行计划开票金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(AbBillPlanAmtMaintain form) {
        abBillPlanAmtMaintainService.savePlanAmt(form, this.getCurrentUser());
        return success();
    }

    /**
     * 银行计划开票金额删除
     *
     * @param id 产品赎回主键id
     * @return 操作结果
     */
    @LogMessage(content = "删除银行计划开票金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        abBillPlanAmtMaintainService.delPlanAmt(id);
        return success();
    }

    /**
     * 编辑银行计划开票金额提交
     *
     * @param form 产品赎回表单
     * @return 操作结果
     */
    @LogMessage(content = "编辑银行计划开票金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(AbBillPlanAmtMaintain form) {
        abBillPlanAmtMaintainService.updatePlanAmt(form, this.getCurrentUser());
        return success();
    }

    /**
     * 银行计划开票金额导入
     *
     * @param list 银行计划开票金额集合
     * @return 操作结果
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<AbBillPlanAmtMaintain> list) {
        abBillPlanAmtMaintainService.saveImport(list, getCurrentUser());
        return success();
    }
}
