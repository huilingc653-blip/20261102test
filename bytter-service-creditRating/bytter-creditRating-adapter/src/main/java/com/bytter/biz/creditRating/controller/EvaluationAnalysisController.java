package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.EvaluationAnalysisService;
import com.bytter.biz.creditRating.vo.CrTaskCorpInfoVo;
import com.bytter.biz.creditRating.vo.CrTaskInfoVO;
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

import java.math.BigDecimal;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 信用评估分析
 * @author chenqin
 * @date 2024-08-09
 */

@Controller
@RequestMapping("/credit/evaluationAnalysis")
public class EvaluationAnalysisController extends BaseController {

    @Autowired
    private EvaluationAnalysisService evaluationAnalysisService;

    /**
     * 评估分析界面跳转
     * @param model
     * @return
     */
    @LogMessage(content = "评估分析界面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model){
        return "creditRating/listEvaluationAnalysis";
    }

    /**
     * 获取评估任务列表
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "获取评估任务列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return evaluationAnalysisService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 评估分析
     * @param taskNo
     * @return
     */
    @LogMessage(content = "获取待评估分析数据列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/evaluate/{taskNo}/{corpId}/{taskId}")
    public String evaluate(@PathVariable("taskNo") String taskNo,@PathVariable("corpId") String corpId,@PathVariable("taskId") String taskId, Model model) {
        evaluationAnalysisService.evaluate(taskNo, model,taskId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("taskId", taskId);
        return "creditRating/evaluationAnalysis";
    }

    /**
     * 根据任务编号、单位获取评分指标
     * @param taskNo
     */
    @GetMapping("/getCorpScoringJson/{taskNo}/{corpId}")
    @ResponseBody
    public Map<String, Object> getCorpScoringJson(@PathVariable("taskNo") String taskNo,@PathVariable("corpId") String corpId) {
        return evaluationAnalysisService.getCorpScoringJson(taskNo, corpId);
    }

    /**
     * 获取待评估分析数据详情
     * @param id
     * @param taskNo
     * @param corpId
     * @param model
     * @return
     */
    @LogMessage(content = "获取待评估分析数据详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/view/{id}/{taskNo}/{corpId}")
    public String view(@PathVariable("id") String id,@PathVariable("taskNo") String taskNo,@PathVariable("corpId") String corpId, Model model) {
        //根据组织评价任务明细id获取评价任务信息
        CrTaskInfoVO crTaskInfo = evaluationAnalysisService.getCrTaskInfoById(id);
        model.addAttribute("crTaskInfo",crTaskInfo);
        model.addAttribute("taskNo",taskNo);
        model.addAttribute("corpId",corpId);
        return "creditRating/viewEvaluationAnalysis";
    }

    /**
     * 评估任务日志界面跳转
     * @param id
     * @param model
     * @return
     */
    @LogMessage(content = "评估任务日志界面跳转",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/viewLog/{id}")
    public String viewLog(@PathVariable("id") String id, Model model) {
        model.addAttribute("taskId",id);
        return "creditRating/listEvaluationLog";
    }

    /**
     * 评价数据保存
     *
     * @param
     * @return
     */
    @LogMessage(content = "评价数据保存", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(CrTaskCorpInfoVo crTaskCorpInfoVo) {
        evaluationAnalysisService.update(crTaskCorpInfoVo,this.getCurrentUser());
        return success();
    }

    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "creditRating/listEvaluationAnalysisApprove";
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return evaluationAnalysisService.dataTableApprove(dataTablesInput);
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            //根据组织评价任务明细id获取评价任务信息
            CrTaskInfoVO crTaskInfo = evaluationAnalysisService.getCrTaskInfoById(id);
            model.addAttribute("crTaskInfo",crTaskInfo);
            model.addAttribute("taskId", taskId);
        }
        return "creditRating/approveEvaluationAnalysis";
    }

    /**
     * 定量根据数值自动计算得分
     * @param inId  指标id
     * @param value  数值
     * @return
     */
    @LogMessage(content = "定量根据数值自动计算得分", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/getScoreByValue")
    @ResponseBody
    public BigDecimal getScoreByValue(String inId, String value,String modelId) {
        BigDecimal result=evaluationAnalysisService.getScoreByValue(inId,value,modelId);
        return result;
    }
}
