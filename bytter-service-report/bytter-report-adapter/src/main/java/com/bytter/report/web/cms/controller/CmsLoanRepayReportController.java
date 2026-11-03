package com.bytter.report.web.cms.controller;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.report.domain.cms.service.CmsLoanRepayReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
/**
 * 内部贷款报表
 */
@Controller
@RequestMapping("/bi/cms/report/loan")
public class CmsLoanRepayReportController extends BaseController {
    @Autowired
    private CmsLoanRepayReportService cmsLoanRepayReportService;
    /**
     * 查询内部贷款汇总表的数据
     * @param input
     * @return
     */
        @PostMapping("/searchloanrepay")
    @ResponseBody
    public DataTablesOutput searchLoanRepay(@RequestBody DataTablesInput input) {
        String startDateStr = (String) input.getFormdata().get("loanStartDate");
        String endDateStr = (String) input.getFormdata().get("loanEndDate");
        input.getFormdata().put("loanStartDate",null);
        input.getFormdata().put("loanEndDate",null);
        input.getFormdata().put("led_loanDate",endDateStr);
        if(EmptyUtils.isEmpty(startDateStr) || EmptyUtils.isEmpty(endDateStr)) {
            return new DataTablesOutput();
        }
        Date startDate = null, endDate = null;
        if (EmptyUtils.isNotEmpty(startDateStr)) {
            startDate = DateUtils.parseDate(startDateStr);
        }
        if (EmptyUtils.isNotEmpty(endDateStr)) {
            endDate = DateUtils.parseDate(endDateStr);
        }
        return cmsLoanRepayReportService.listLoanRepay(getCurrentUser(),input,startDate,endDate);
    }
    /**
     * 内部贷款汇总表
     * @return
     */
    @GetMapping("/loanrepayreport")
    public String loanRepayReport(){
        return "cms/loanrepayreport";
    }
}
