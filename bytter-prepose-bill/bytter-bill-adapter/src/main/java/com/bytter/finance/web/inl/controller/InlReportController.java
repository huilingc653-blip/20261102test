package com.bytter.finance.web.inl.controller;
import com.bytter.finance.domain.inl.service.InlReportService;
import com.bytter.finance.domain.inl.vo.InlReportVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/inl/report")
public class InlReportController extends BaseController {
    @Autowired
    private InlReportService inlReportService;
    @GetMapping("/listReport")
    public String listReport() {
        return "cms/inl/report/listInlReport";
    }
    @PostMapping("/listReportData")
    @ResponseBody
    public DataTablesOutput listReport(@RequestBody DataTablesInput input) {
        return inlReportService.listReport(input, getCurrentUser());
    }
    @GetMapping("/listReportInterest")
    public String listReportInterest() {
        return "cms/inl/report/listInlRepayInterest";
    }
    @PostMapping("/listReportInterestData")
    @ResponseBody
    public DataTablesOutput listReportInterestData(@RequestBody DataTablesInput input) {
        return inlReportService.listReportInterest(input, getCurrentUser());
    }
    /**
     * 导入数据
     *
     * @param list
     * @return
     */
    @LogMessage(content = "导入收款信息操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<InlReportVO> list) {
        inlReportService.saveImport(list, getCurrentUser());
        return success();
    }
}
