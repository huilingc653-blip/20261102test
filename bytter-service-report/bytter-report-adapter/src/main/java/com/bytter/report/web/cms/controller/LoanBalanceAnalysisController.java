package com.bytter.report.web.cms.controller;

import com.bytter.report.domain.cms.service.LoanBalanceAnalysisService;
import com.bytter.report.domain.cms.vo.LoanBalanceAnalysisVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.IBtCurrencyService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author zhengxn
 * @Description 贷款余额对比分析
 */

@Controller
@RequestMapping("/cms/report/loanBalanceAnalysis")
public class LoanBalanceAnalysisController extends BaseController {

    @Autowired
    private LoanBalanceAnalysisService loanBalanceAnalysisService;
    @DubboReference
    private IBtCurrencyService iBtCurrencyService;
    
    @GetMapping("/list")
    public String list(Model model){
        return "cms/report/loanBalanceAnalysis/listBalanceAnalysis";
    }

    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) throws ParseException {
        return loanBalanceAnalysisService.getDataTable(dataTablesInput,getUserId());
    }

    @PostMapping("/getBal")
    @ResponseBody
    public Object getBal(@RequestBody List<LoanBalanceAnalysisVO> list){
        return loanBalanceAnalysisService.getBankBal(list);
    }

    @LogMessage(content = "查询明细",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewLoanInfo")
    public String viewLoanInfo(String corpId, String curId,String loanSign, String monthDate, String isContain,String repayCycle,Model model) {
        List<LoanBalanceAnalysisVO> list = loanBalanceAnalysisService.viewLoanInfo(corpId,curId,loanSign,monthDate,isContain,repayCycle);

        model.addAttribute("list",list);
        model.addAttribute("loanSign",loanSign);
        return "cms/report/loanBalanceAnalysis/loanInfoView";
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
        loanBalanceAnalysisService.checkConvertCurId(origCurId, convertCurId, date);
        return success();
    }

    @RequestMapping("/appList")
    public String appList(LoanBalanceAnalysisVO vo,Model model) throws ParseException {
        vo.setCorpId("");
        vo.setIsToCurr("1"); //折合本位币-默认折合,查全部
        vo.setIsContain("1");
        /*if(EmptyUtils.isNotEmpty(vo.getCurId())){
            String curId = iBtCurrencyService.getNatualCurrency().getId();
            vo.setCurId(curId);
        }*/
        if(vo.getMonthDate() == null){
            vo.setMonthDate(new Date());
        }
        List<LoanBalanceAnalysisVO> list = loanBalanceAnalysisService.getAppList(vo,getUserId());
        List<LoanBalanceAnalysisVO> newList = new ArrayList<>();
        for(LoanBalanceAnalysisVO item : list){
            if(item.getCurrentBalance().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceY().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceS().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceL().compareTo(BigDecimal.ZERO) != 0 ||
                    item.getBalanceM().compareTo(BigDecimal.ZERO) != 0){
                newList.add(item);
            }
        }
        List<LoanBalanceAnalysisVO> provideList = new ArrayList<>();
        List<LoanBalanceAnalysisVO> precativeList = new ArrayList<>();
        List<LoanBalanceAnalysisVO> loanList = new ArrayList<>();
        for (LoanBalanceAnalysisVO v : newList) {
            v.setCurName("折合本位币");
            if("01".equals(v.getLoanSign())){//内部贷款
                loanList.add(v);
            }else if("02".equals(v.getLoanSign())){//银行借款
                provideList.add(v);
            }else {//委托贷款
                precativeList.add(v);
            }
        }
        List<BtCurrencyDTO> curList = iBtCurrencyService.getBtCurrency();
        model.addAttribute("curList", curList);
        model.addAttribute("precativeList",precativeList);
        model.addAttribute("loanList",loanList);
        model.addAttribute("provideList",provideList);
        model.addAttribute("initCorpId",vo.getCorpId());
        return "cms/report/loanBalanceAnalysis/mobileBalanceAnalysis";
    }

    /**查询条件回调方法*/
    @PostMapping("/getAppList")
    @ResponseBody
    public Object getAppList(LoanBalanceAnalysisVO vo, Model model) throws ParseException{
       /* if(EmptyUtils.isEmpty(vo.getCorpId())) {
            vo.setCorpId(getCurrentUser().getCorpId());
        }*/
        vo.setIsToCurr("0"); //查询不折合人民币
        if(StringUtils.isEmpty(vo.getTypesOfStatistics())){
            vo.setTypesOfStatistics("2");//融资期限
        }
        if(EmptyUtils.isEmpty(vo.getCurId())){
            String curId = iBtCurrencyService.getNatualCurrency().getId();
            vo.setCurId(curId);
        }
         if(vo.getMonthDate() == null){
            vo.setMonthDate(new Date());
        }
        List<LoanBalanceAnalysisVO> list = loanBalanceAnalysisService.getAppList(vo,getUserId());
        List<LoanBalanceAnalysisVO> provideList = new ArrayList<>();
        List<LoanBalanceAnalysisVO> precativeList = new ArrayList<>();
        List<LoanBalanceAnalysisVO> loanList = new ArrayList<>();
        for (LoanBalanceAnalysisVO v : list) {
            if("01".equals(v.getLoanSign())){//内部贷款
                loanList.add(v);
            }else if("02".equals(v.getLoanSign())){//银行借款
                provideList.add(v);
            }else {//委托贷款
                precativeList.add(v);
            }
        }
        LoanBalanceAnalysisVO res = new LoanBalanceAnalysisVO();
        res.setProvideList(provideList);
        res.setPrecativeList(precativeList);
        res.setLoanList(loanList);
        return res;
    }


    /**条形图回调方法*/
    @PostMapping("/getAppBal")
    @ResponseBody
    public Object getAppBal(LoanBalanceAnalysisVO vo) throws ParseException {
        /*if(EmptyUtils.isEmpty(vo.getCorpId())) {
            vo.setCorpId(getCurrentUser().getCorpId());
        }*/
        if(StringUtils.isEmpty(vo.getTypesOfStatistics())){
            vo.setTypesOfStatistics("2");//融资期限
        }
        if(EmptyUtils.isEmpty(vo.getCurId())){
            String curId = iBtCurrencyService.getNatualCurrency().getId();
            vo.setCurId(curId);
        }
        if(vo.getMonthDate() == null){
            vo.setMonthDate(new Date());
        }
        List<LoanBalanceAnalysisVO> resList = loanBalanceAnalysisService.getAppList(vo,getUserId());

        List<LoanBalanceAnalysisVO> res = null;
        if("2".equals(vo.getTypesOfStatistics())){//融资期限
            res = loanBalanceAnalysisService.groupDataByTime(resList,vo.getCurId());
        }
        if("1".equals(vo.getTypesOfStatistics())){//融资种类
            res =  loanBalanceAnalysisService.groupDataByType(resList);
        }
        if("3".equals(vo.getTypesOfStatistics())){//融资单位
            List<String> corpIds = null;
            if(StringUtils.isNotEmpty(vo.getCorpId())) {
                corpIds = StringUtils.splitToList(",", vo.getCorpId());
            }
            res = loanBalanceAnalysisService.groupDataByCorp(resList,corpIds);
        }
        return res;
    }
}
