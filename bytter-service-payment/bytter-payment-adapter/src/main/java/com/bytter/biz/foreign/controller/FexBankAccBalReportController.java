package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.service.FexBankAccBalReportService;
import com.bytter.biz.foreign.vo.FexBankAccBalReportVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Title: Controller
 * @Description: 外汇存量币种分布统计
 * @author qinl
 * @date 2020-03-18
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/report/bankacc")
public class FexBankAccBalReportController extends BaseController {

    @Autowired
    private FexBankAccBalReportService fexBankAccBalReportService;

    /**
     * 报表查询页面
     * @param model
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_REPORT_BANKACC_QUERY)
    @GetMapping("/view")
    public String view(Model model) {
        return "foreign/listFexBankAccBalReport";
    }

    /**
     * 获取列表数据
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/list")
    @ResponseBody
    public DataTablesOutput<FexBankAccBalReportVO> list(@RequestBody DataTablesInput dataTablesInput) {
        return fexBankAccBalReportService.conditionsEntrance(dataTablesInput);
    }
}
