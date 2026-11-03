package com.bytter.biz.report.controller;
import com.bytter.biz.report.service.DepositBalanceAnalysisService;
import com.bytter.biz.report.vo.DepositBalanceAnalysisVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * 分析系统当前存款余额与上月的差异、与年初的差异。
 *
 * @author qinl
 * @description 存款余额对比分析
 * @date 2020.02.12
 */
@Controller
@RequestMapping("/report/deposit/balance")
public class DepositBalanceAnalysisController extends BaseController {
    @Autowired
    private DepositBalanceAnalysisService depositBalanceAnalysisService;
    /**
     * 报表查询页面
     * @param model
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.DEPOSIT_BALANCE_ANALYSIS_QUERY)
    @GetMapping("/view")
    public String view(Model model) {
        return "report/depositBalanceAnalysisReport";
    }
    /**
     * 获取列表数据
     * @param dataTablesInput
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.DEPOSIT_BALANCE_ANALYSIS_QUERY)
    @PostMapping("/list")
    @ResponseBody
    public DataTablesOutput<DepositBalanceAnalysisVO> list(@RequestBody DataTablesInput dataTablesInput) {
        return depositBalanceAnalysisService.conditionsEntrance(dataTablesInput);
    }
    /**
     * 余额详情页面
     * @return
     */
    @GetMapping("/detail")
    public String detailView(String[] bankList, String[] innerList,String moneyUnit,String curId,String changeCur,String date,String reducedCurId, Model model) {
        StringBuilder stringBuilder1 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        String strInnerList=null;
        String strBankList=null;
        for (int i = 0; i < bankList.length; i++) {
            stringBuilder1.append(bankList[i]).append(",");
        }
        for (int i = 0; i < innerList.length; i++) {
            stringBuilder2.append(innerList[i]).append(",");
        }
        if(bankList.length>0){
            strBankList = stringBuilder1.toString().substring(0, stringBuilder1.toString().length() - 1);
        }
        if(innerList.length>0){
            strInnerList = stringBuilder2.toString().substring(0, stringBuilder2.toString().length() - 1);
        }
        model.addAttribute("bankList", strBankList);
        model.addAttribute("innerList", strInnerList);
        model.addAttribute("moneyUnit", moneyUnit);
        model.addAttribute("curId", curId);
        model.addAttribute("changeCur", changeCur);
        model.addAttribute("date", date);
        model.addAttribute("reducedCurId", reducedCurId);
        return "report/depositBalanceAnalysisReportDetail";
    }
    /**
     * 余额详情数据
     * @return
     */
    @PostMapping("/detailData")
    @ResponseBody
    public DataTablesOutput<DepositBalanceAnalysisVO> details(@RequestBody DataTablesInput input) {
        DataTablesOutput<DepositBalanceAnalysisVO> output = new DataTablesOutput<>();
        String[] bankSplit =  ((String)input.getFormdata().get("bankList")).split(",");
        String[] innerSplit =  ((String)input.getFormdata().get("innerList")).split(",");
        List<DepositBalanceAnalysisVO> depositBalanceAnalysisVOS = depositBalanceAnalysisService.selectDetailsList(bankSplit, innerSplit);
        depositBalanceAnalysisService.moneyFilter(input,depositBalanceAnalysisVOS);
        output.setData(depositBalanceAnalysisVOS);
        depositBalanceAnalysisService.moneyFilter(input,output);
        depositBalanceAnalysisService.pageFilter(input, output);
        return output;
    }
    /**
     *币种转换校验
     *@Param origCurId 原币种
     *@Param convertCurId   转换币种
     *@Param date 日期
     *@Param isAllCur 是否查所有币种
     *@return
     */
    @PostMapping("/checkConvertCurId")
    @ResponseBody
    public AjaxJson checkConvertCurId(String origCurId, String convertCurId, String date,String isAllCur){
        depositBalanceAnalysisService.checkConvertCurId(origCurId, convertCurId, date,isAllCur);
        return success();
    }
}
