package com.bytter.report.mobile;

import com.bytter.report.domain.cms.service.LoanBalanceTrendService;
import com.bytter.report.domain.cms.vo.LoanBalanceTrendVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IBtCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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
@RequestMapping("/mobile/report/cms/loanTrend")
public class LoanBalanceTrendMobileExtController extends BaseController {
    @Autowired
    LoanBalanceTrendService loanBalanceTrendService;

    @Autowired
    private IBtCurrencyService currencyService;

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
        return "mobile/report/cmsLoan/mobileLoanBalanceTrend";
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
