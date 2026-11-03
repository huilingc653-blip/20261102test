package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.CmsLoanStatementService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author luxy
 * @Title: CmsLoanStatementController
 * @ProjectName T3(3.2)
 * @Description: 贷款放款及还款情况表控制层
 * @date 2020/1/13 11:18
 */
@Controller
@RequestMapping("/cms/report/cmsloanstatements")
public class CmsLoanStatementController extends BaseController {
    @Autowired
    private CmsLoanStatementService loanStatementService;
    @Autowired
    private IBtCurrencyService currencyService;

    //@RequiresPermissions(value = ResourceConstant.CMS_REPORT_LOAN_STATEMENT_QUERY)
    @GetMapping("/list")
    public String list(Model model){
        BtCurrencyDTO stanCur=currencyService.getNatualCurrency();//本位币
        String standardCurId="";
        if(null!=stanCur){
            standardCurId=stanCur.getId();
        }
        model.addAttribute("standardCurId",standardCurId);
        return "cms/report/cmsLoanStatement/listLoanStatement";
    }

    //@RequiresPermissions(value = ResourceConstant.CMS_REPORT_LOAN_STATEMENT_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input){
        return loanStatementService.datatable(input,getCurrentUser());
    }

    /**
    *汇率转换校验
    *@Param origCurId
    *@Param convertCurId
    *@Param date
    *@return
    */
    @PostMapping("/checkConverCurId")
    @ResponseBody
    public AjaxJson checkConverCurId(String origCurId, String convertCurId, Date date) {
        loanStatementService.checkConvertCurId(origCurId, convertCurId, date);
        return success();
    }
}
