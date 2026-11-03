package com.bytter.report.web.cms.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.finance.api.ICmsProvideLoanInfoService;
import com.bytter.finance.dto.CmsProvideLoanInfoDTO;
import com.bytter.report.domain.cms.service.InterestReportService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bi/cms/interestReport")
public class InterestReportController extends BaseController {
    @Autowired
	private InterestReportService interestReportService;
    @DubboReference(check = false)
	private ICmsProvideLoanInfoService cmsProvideLoanInfoService;

    /**
     *
     * @return
     */
    @GetMapping("/listInterest")
    public String listInterest(Model model){
        return "cms/listInterest";
    }
    /**
     *
     * @return
     */
        @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input){
        return interestReportService.listInterest(input, getCurrentUser());
    }
    /**
     * 查询银行借款信息
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/view")
    public String getCmsProvideInfoById(Model model, String id) {
        CmsProvideLoanInfoDTO cmsProvideLoanInfoDTO = cmsProvideLoanInfoService.getInfoById(id);
        model.addAttribute("cmsProvideLoanInfo", cmsProvideLoanInfoDTO);
        return "cms/viewProvideLoanInfo";
    }

    @LogMessage(content = "银行借款利息预提表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    //@RequiresPermissions(ResourceConstant.BI_CMS_INTEREST_WITHHOLDING_QUERY)
    @GetMapping("/listBankLoanWithholding")
    public String listBankLoanWithholding(Model model){
        Date _thisDate = new Date();
        Date firstMonthDate = DateUtils.getFirstMonthDate(_thisDate);
        Date lastMonthDate = DateUtils.getLastMonthDate(_thisDate);
        model.addAttribute("firstMonthDate", DateUtils.formatDate(firstMonthDate));
        model.addAttribute("lastMonthDate", DateUtils.formatDate(lastMonthDate));
        model.addAttribute("firstMonth", DateUtils.format(_thisDate,"yyyy-MM"));
        model.addAttribute("lastMonth", DateUtils.format(DateUtils.addMonth(_thisDate,5),"yyyy-MM"));
        List<String> months = new ArrayList<>();
        for(int i=0;i<6;i++){
            months.add(DateUtils.format(DateUtils.addMonth(_thisDate,i),"yyyy-MM"));
        }
        model.addAttribute("months", months);
        model.addAttribute("monthSize", 6);
        return "cms/listBankLoanWithholding";
    }

    /**
     * 功能介绍 : 银行借款利息预提表 - 数据<br>
     */
    @PostMapping("/queryBankLoanWithholdingData")
    @ResponseBody
    public DataTablesOutput queryBankLoanWithholdingData(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = interestReportService.queryBankLoanWithholding(dataTablesInput, getCurrentUser());
        return dataTablesOutput;
    }
}
