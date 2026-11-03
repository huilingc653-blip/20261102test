package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.LoanDailyAnalysisService;
import com.bytter.report.domain.cms.utils.LoanDailyAnalysisUtils;
import com.bytter.report.domain.cms.vo.LoanDailyAnalysisVO;
import com.bytter.report.domain.cms.vo.LoanDailyParamVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.constant.CmsConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: LoanDailyAnalysisController
 * @ProjectName T3(3.2)
 * @Description: 日均贷款分析控制层
 * @date 2020/2/24 14:00
 */
@Controller
@RequestMapping("/cms/report/loanDailyAnalysis")
public class LoanDailyAnalysisController extends BaseController {

    @Autowired
    private LoanDailyAnalysisService analysisService;
    @Autowired
    private IBtCurrencyService currencyService;

    /**
     * 根据单位或贷款机构查询数据列表页面
     * @param model
     * @return
     */
    @RequestMapping("/list")
    //@RequiresPermissions(ResourceConstant.CMS_REPORT_LOANDAILY_ANALYSIS_QUERY)//查询报表权限
    public String list(Model model){
        String searchSatrtMonth=DateUtils.format(DateUtils.getCurrYearFirst(),"yyyy-MM");//按月份查询时开始月份为当年一月份
        String searchEndMonth=DateUtils.format(DateUtils.getDate(),"yyyy-MM");//按月份查询时结束月份为当月
        String searchDateMax=DateUtils.date_sdf().format(DateUtils.getDate());//最大查询时间为系统当前时间
        model.addAttribute("searchSatrtMonth",searchSatrtMonth);
        model.addAttribute("searchEndMonth",searchEndMonth);
        model.addAttribute("searchDateMax",searchDateMax);
        BtCurrencyDTO stanCur=currencyService.getNatualCurrency();//本位币
        String standardCurId="";
        if(null!=stanCur){
            standardCurId=stanCur.getId();
        }
        model.addAttribute("standardCurId",standardCurId);//查询条件的币别默认本位币
        return "cms/report/loanDailyAnalysis/listLoanDailyAnalysis";
    }

    /**
     * 根据单位或贷款机构分类时报表数据查询
     * @param input
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    //@RequiresPermissions(ResourceConstant.CMS_REPORT_LOANDAILY_ANALYSIS_QUERY)//查询报表权限
    public DataTablesOutput datatable(@RequestBody DataTablesInput input){
        DataTablesOutput output=null;
        String sortType=(String)input.getFormdata().get("typesOfStatistics");//统计维度
        if(sortType.equals(CmsConstant.SORT_TYPE_1)){//统计维度--融资银行
            output=analysisService.bankDatatable(input,getCurrentUser());
        }else{//统计维度--融资单位(默认)
            output=analysisService.corpDatatable(input,getCurrentUser());
        }
        return output;
    }

    /**
     * 根据单位或贷款机构查询各类贷款本期日均分布详情饼状图页面
     * @param paramVO
     * @param model
     * @return
     */
    @RequestMapping("/viewTypeOfStatisticsDtl")
    public String viewTypeOfStatisticsDtl(LoanDailyParamVO paramVO, Model model){
        List<LoanDailyAnalysisVO> analysisVOList=analysisService.statisticsDtlDatatable(paramVO,getCurrentUser());//获取表格展示数据
        Map<String, Object> pieDatas = analysisService.getLoansPieDatas(analysisVOList);
        model.addAttribute("paramVO",paramVO);
        model.addAttribute("analysisVOList",analysisVOList);
        model.addAttribute("seriesData", pieDatas.get("seriesData"));
        model.addAttribute("legendsData", pieDatas.get("legendsData"));
        return "cms/report/loanDailyAnalysis/viewTypeOfStatisticsDtl";
    }


    /**
     * 查询每日贷款余额柱状图及折线图查看页面
     */
    @RequestMapping("/viewDailyBalance")
    public String viewDailyBalance(LoanDailyParamVO paramVO, Model model){
        model.addAttribute("analysisVO",paramVO);
        return "cms/report/loanDailyAnalysis/viewDailyBalance";
    }

    /**
     * 查询每日贷款余额柱状图及折线图数据
     * @param paramVO
     * @return
     */
    @PostMapping("/getDailyBalance")
    @ResponseBody
    public Object getDailyBalance(LoanDailyParamVO paramVO){
        Map<String,Object> dateMap= LoanDailyAnalysisUtils.getDateMap(paramVO.getStartDate(),paramVO.getEndDate(),paramVO.getSearchFrequency());
        Map<String,Object> graphDatas = analysisService.getDailyBalanceData(paramVO,getCurrentUser(),dateMap);
        return graphDatas;
    }


    /**
    *汇率币种转换校验
    *@Param origCurId
    *@Param convertCurId
    *@Param date
    *@return
    */
    @PostMapping("/checkConvertCurId")
    @ResponseBody
    public AjaxJson checkConvertCurId(String origCurId, String convertCurId, Date date,
                                      @RequestParam(required = false,defaultValue = "0") String searchFrequency){
        if ("1".equals(searchFrequency)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            cal.set(Calendar.DAY_OF_MONTH, day);
            date = cal.getTime();
        }
        analysisService.checkConvertCurId(origCurId, convertCurId, date);
        return success();
    }
}
