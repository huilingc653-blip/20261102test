package com.bytter.accountReceivable.agingAnalysis.controller;

import com.bytter.accountReceivable.agingAnalysis.service.AgingAnalysisService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/accountReceivable/agingAnalysis")
public class AgingQueryController extends BaseController {

    @Autowired
    private AgingAnalysisService agingAnalysisService;

    /**
     * 应收款管理－查询－应收账龄分析表页面跳转
     */
    //@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_AGINGANALYSIS_QUERY)
    @LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model, String type) {
        Optional.ofNullable(type)
            .ifPresent(item -> model.addAttribute("type", type));
        return "agingAnalysis/listAgingQuery";
    }

    //@RequiresPermissions(ResourceConstant.ACCOUNT_RECEIVABLE_AGINGANALYSIS_QUERY)
    @PostMapping("/queryAging")
    @ResponseBody
    public DataTablesOutput queryAgingByCus(@RequestBody DataTablesInput input) {
        return agingAnalysisService.queryAging(input,getCurrentUser());
    }

}
