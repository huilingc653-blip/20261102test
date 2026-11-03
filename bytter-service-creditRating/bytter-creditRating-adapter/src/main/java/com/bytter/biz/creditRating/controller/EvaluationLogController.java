package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.EvaluationLogService;
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
 * @Title: Controller
 * @Description: 信用评估操作日志
 * @author chenqin
 * @date 2024-08-30
 */

@Controller
@RequestMapping("/credit/evaluationLog")
public class EvaluationLogController extends BaseController {

    @Autowired
    private EvaluationLogService evaluationLogService;

    /**
     * 获取评估任务操作日志列表
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "获取评估任务操作日志列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return evaluationLogService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
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
}
