package com.bytter.biz.wage.controller;

import com.bytter.biz.netbank.entity.NisMessage;
import com.bytter.biz.wage.entity.NisWageFloatRatioConfig;
import com.bytter.biz.wage.service.NisWageFloatRatioConfigService;
import com.bytter.biz.wage.vo.NisWageFloatRatioConfigVO;
import com.bytter.biz.wage.vo.NisWageFloatRatioMessageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 代发工资浮动比率配置控制层
 *
 * @author fangcf
 * @date 2025-11-20
 */
@Controller
@RequestMapping("/wage/wageFloatRatio")
public class NisWageFloatRatioConfigController extends BaseController {

    @Autowired
    private NisWageFloatRatioConfigService nisWageFloatRatioConfigService;

    /**
     * 代发工资浮动比率配置 页面跳转
     *
     * @return 代发工资浮动比率配置列表跳转地址
     */
    @LogMessage(content = "代发工资浮动比率配置列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "wage/listWageFloatRatioConfig";
    }

    /**
     * 代发工资浮动比率配置 table列表
     *
     * @param input 入参
     * @return 出参
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return nisWageFloatRatioConfigService.dataTable(input);
    }

    /**
     * 代发工资浮动比率配置新增页面跳转
     *
     * @return 代发工资浮动比率配置新增跳转地址
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "wage/addWageFloatRatioConfig";
    }

    /**
     * 代发工资浮动比率配置编辑页面跳转
     *
     * @return 代发工资浮动比率配置新增跳转地址
     */
    @GetMapping("/update")
    public String update(NisWageFloatRatioConfigVO form, Model model) {
        if (StringUtils.isNotEmpty(form.getId())) {
            NisWageFloatRatioConfig entity = nisWageFloatRatioConfigService.get(form.getId(), true);
            model.addAttribute("entity", entity);
        }
        return "wage/updateWageFloatRatioConfig";
    }

    /**
     * 保存代发工资浮动比率配置
     *
     * @param form 表单数据
     * @return 操作结果
     */
    @LogMessage(content = "保存代发工资浮动比率配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisWageFloatRatioConfig form) {
        nisWageFloatRatioConfigService.saveWageFloatRatio(form, this.getCurrentUser());
        return success();
    }

    /**
     * 修改代发工资浮动比率配置
     *
     * @param form 表单数据
     * @return 操作结果
     */
    @LogMessage(content = "修改代发工资浮动比率配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(NisWageFloatRatioConfig form) {
        nisWageFloatRatioConfigService.updateWageFloatRatio(form, this.getCurrentUser());
        return success();
    }

    /**
     * 删除代发工资浮动比率配置
     *
     * @param id 主键id
     * @return 操作结果
     */
    @LogMessage(content = "删除代发工资浮动比率配置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        nisWageFloatRatioConfigService.delWageFloatRatio(id, this.getCurrentUser());
        return success();
    }

    /**
     * 操作记录查询
     *
     * @return 操作记录列表
     */
    @GetMapping("/listMessage")
    public String listMessage(Model model) {
        return "wage/listWageFloatRatioMessage";
    }

    /**
     * 操作记录查询 table列表
     *
     * @param input 入参
     * @return 出参
     */
    @PostMapping("/datatableMessage")
    @ResponseBody
    public DataTablesOutput datatableMessage(@RequestBody DataTablesInput input) {
        return nisWageFloatRatioConfigService.datatableMessage(input);
    }
}
