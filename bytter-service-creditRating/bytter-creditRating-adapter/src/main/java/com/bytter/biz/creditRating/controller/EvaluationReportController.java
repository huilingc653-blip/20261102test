package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.EvaluationReportService;
import com.bytter.biz.creditRating.vo.CrTaskOrgInfoVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Title: Controller
 * @Description: 评价结果查询
 * @author chenqin
 * @date 2024-08-09
 */

@Controller
@RequestMapping("/credit/evaluationReport")
public class EvaluationReportController extends BaseController {

    @Autowired
    private EvaluationReportService evaluationReportService;

    /**
     * 信用评估报告界面跳转
     *
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
    @LogMessage(content = "信用评估报告界面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/listEvaluationReport";
    }

    /**
     * 获取评估任务列表
     *
     * @param dataTablesInput
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
    @LogMessage(content = "获取评估任务列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return evaluationReportService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 获取信用评估报告详情
     *
     * @param id
     * @param model
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_FCVOUCHERTYPE_APPLY)
    @LogMessage(content = "信用评估报告详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/view/{id}/{orgId}")
    public String view(@PathVariable("id") String id, @PathVariable("orgId") String orgId, Model model) {
        CrTaskOrgInfoVo crTaskOrgInfoVo = evaluationReportService.getCrTaskInfoById(id);
        model.addAttribute("crTaskOrgInfo", crTaskOrgInfoVo);
        model.addAttribute("orgId", orgId);
        return "creditRating/viewEvaluationReport";
    }

    /**
     * 根据任务编号获取固化评分指标
     *
     * @param taskNo
     */
    @GetMapping("/getScoringIndicatorFixReport/{taskNo}/{orgId}")
    @ResponseBody
    public Map<String, Object> getScoringIndicatorFixReport(@PathVariable("taskNo") String taskNo, @PathVariable("orgId") String orgId) {
        return evaluationReportService.getScoringIndicatorFixReport(taskNo, orgId);
    }

}
