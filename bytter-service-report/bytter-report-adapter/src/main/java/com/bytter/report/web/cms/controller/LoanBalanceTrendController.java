package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.LoanBalanceTrendService;
import com.bytter.report.domain.cms.vo.LoanBalanceTrendVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LoanBalanceTrendController
 * @Description 贷款余额趋势分析controller
 * @Author fusb
 * @Date 2020/1/14 9:04
 * @Version 1.0
 **/
@Controller
@RequestMapping("/cms/report/loanTrend")
public class LoanBalanceTrendController extends BaseController {
    @Autowired
    LoanBalanceTrendService loanBalanceTrendService;

    @Autowired
    private IBtCurrencyService currencyService;

    @Autowired
    private ICorpService corpService;



    @LogMessage(content = "贷款余额趋势分析页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    //@RequiresPermissions(CmsConstant.LOAN_BALANCE_TREND_QUERY)
    @GetMapping("/list")
    public String list(LoanBalanceTrendVO vo,String isInit,Model model){
        List<String> monthList = loanBalanceTrendService.tableInit(vo.getStartDate(),vo.getEndDate(),false);
        model.addAttribute("monthList",monthList);
        model.addAttribute("searchVO",vo);
        model.addAttribute("isInit", isInit);
        return "cms/report/cmsLoan/listLoanBalanceTrend";
    }

    @LogMessage(content = "移动端显示", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/mobileList")
    public String mobileList(LoanBalanceTrendVO vo,Model model){
        if (StringUtils.isEmpty(vo.getCurCode())) {
            //默认查询人民币
            vo.setCurCode(currencyService.getRMBCurrency().getId());
            vo.setStartDate(DateUtils.format(new Date(), "yyyy-MM"));
            vo.setEndDate(DateUtils.format(new Date(), "yyyy-MM-dd"));
            vo.setCorpId(null);
        }
        List<String> monthList = loanBalanceTrendService.tableInit(vo.getStartDate(),vo.getEndDate(),true);
        model.addAttribute("monthList",monthList);
        List<Map<String,Object>> mapList = loanBalanceTrendService.getMobileLoanBalanceTrendList(vo,this.getCurrentUser());
        model.addAttribute("mapList",mapList);
        model.addAttribute("searchVO",vo);
        model.addAttribute("curList", currencyService.getBtCurrency());
//        model.addAttribute("now", DateUtils.format(new Date(), "yyyy-MM-dd"));
        return "cms/report/cmsLoan/MobileLoanBalanceTrend";
    }

    /**
    *根据条件筛选数据
    *@Param vo
    *@Param model
    *@return
    */
    @RequestMapping("/getMobileData")
    @ResponseBody
    public AjaxJson getMobileData(LoanBalanceTrendVO vo,Model model){
        if (StringUtils.isEmpty(vo.getCurCode())) {
            //默认查询人民币
            vo.setCurCode(currencyService.getRMBCurrency().getId());
        }
        List<String> monthList = loanBalanceTrendService.tableInit(vo.getStartDate(),vo.getEndDate(),true);
        List<Map<String,Object>> mapList = loanBalanceTrendService.getMobileLoanBalanceTrendList(vo,this.getCurrentUser());
        Map<String, Object> result = new HashMap<>();
        result.put("monthList", monthList);
        result.put("mapList", mapList);
        AjaxJson json = new AjaxJson();
        json.setObj(result);
        json.setSuccess(true);
        return json;
    }

    @LogMessage(content = "贷款余额趋势分析列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    //@RequiresPermissions(CmsConstant.LOAN_BALANCE_TREND_QUERY)
    @PostMapping("/datatables")
    @ResponseBody
    public DataTablesOutput valuaSummaryTables(@RequestBody DataTablesInput input, LoanBalanceTrendVO vo){
        DataTablesOutput output = new DataTablesOutput();
        if (null == input.getFormdata().get("isInit") || StringUtils.isEmpty(input.getFormdata().get("isInit").toString())) {
            return output;
        }
        List<Map<String,Object>> mapList = loanBalanceTrendService.getLoanBalanceTrendList(vo,this.getCurrentUser());
        if(null == mapList && EmptyUtils.isEmpty(mapList)){
            output.setDraw(2);
            output.setRecordsTotal(0);
            output.setRecordsFiltered(0);
            output.setError(null);
            output.setData(null);
            return output;
        }
        // 设置返回前端的DataTablesOutput
        int totalNum = mapList.size();
        Integer pageNumber = input.getPageNumber();
        int page = (totalNum + 10 - 1)/10;
        if(pageNumber == page){
            output.setData(mapList.subList(input.getStart(), totalNum));
        }else {
            if(input.getLength() > totalNum){
                output.setData(mapList.subList(input.getStart(), totalNum));
            }else {
                output.setData(mapList.subList(input.getStart(), input.getStart()+10));
            }
        }
        output.setRecordsTotal(mapList.size());
        output.setRecordsFiltered(mapList.size());
        return output;
    }

    @LogMessage(content = "贷款余额趋势分析图表信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @PostMapping("/loanBalanceChart")
    @ResponseBody
    public Object getBankBal(LoanBalanceTrendVO parmsVO,String isInit){
        return loanBalanceTrendService.loanBalanceChart(parmsVO, this.getCurrentUser(), isInit);
    }

    /**
    *移动端图表显示
    *@Param parmsVO
    *@return
    */
    @PostMapping("/getMobileChartData")
    @ResponseBody
    public AjaxJson getMobileChartData(LoanBalanceTrendVO parmsVO){
        Map<String, Object> barData = new HashMap<>();
        if (StringUtils.isEmpty(parmsVO.getCurCode())) {
            parmsVO.setCurCode(currencyService.getRMBCurrency().getId());
        }
        barData.put("mobileDate", loanBalanceTrendService.getMobileChartData(parmsVO,this.getCurrentUser()));
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setObj(barData);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    /**
    *币种转换校验
    *@Param origCurId
    *@Param convertCurId
    *@Param date
    *@return
    */
    @PostMapping("/checkConvertCurId")
    @ResponseBody
    public AjaxJson checkConvertCurId(String origCurId, String convertCurId, Date date){
        loanBalanceTrendService.checkConvertCurId(origCurId, convertCurId, date);
        return success();
    }
}
