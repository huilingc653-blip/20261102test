package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillBalCompareQueryService;
import com.bytter.bill.domain.bill.vo.AbBillBalCompareVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;
import java.util.List;
/**
 * @author zhenghj
 * @version 1.0
 * @Title: Controller
 * @Description: 票据余额对比分析入口
 * @date 2020-01-15
 */
@Controller
@RequestMapping("/bill/accebill/balCompare")
public class AbBillBalCompareQueryController extends BaseController{
    @Autowired
    private AbBillBalCompareQueryService abBillBalCompareQueryService;
    /**
     * 票据余额对比分析列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询票据余额对比分析列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbBillBalCompareQuery";
    }
    /**
     * 票据余额对比分析数据
     *
     * @param dataTablesInput
     * @param model
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, Model model) throws ParseException {
    	DataTablesOutput data = abBillBalCompareQueryService.getDataTable(dataTablesInput);
        List<AbBillBalCompareVO> balCom = data.getData();
        return data;
    }
    /**
     * 余额对比跳转明细页面
     *
     * @return
     */
    @GetMapping("/open")
    public String open(Model model, String corpId, String billFlag, String statisticsMonth, String billType) {
        model.addAttribute("corpId", corpId);
        model.addAttribute("billFlag", billFlag);
        model.addAttribute("statisticsMonth", statisticsMonth);
        model.addAttribute("billType", billType);
        return "bill/accebill/listAbBillBalCompareDetailed";
    }
    /**
     * 票据余额对比明细表数据
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/detailedDatatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abBillBalCompareQueryService.listBalCompareDetailed(dataTablesInput);
    }
    /**
     * 票据余额对比分析 app页面跳转
     *
     * @param model 数据传输对象
     * @return 跳转页面
     */
    @PostMapping("/toAppNetCollect")
    public String toAppNetCollect(Model model) {
        return "bill/accebill/mobile/mobileAbBillTransferDownNetCollect";
    }
    /**
     * 票据余额对比分析 app页面
     *
     * @return
     */
    @RequestMapping("/balCompareApp")
    public String balCompareApp(Model model) throws ParseException {
    	model.addAttribute("balcompareReson", AccebillConstant.AB_BILL_BALCOMPARE_QUERY);
        return "bill/accebill/mobile/mobileAbBillBalCompareQuery";
    }
    /**
     * 票据余额对比分析 app页面
     *
     * @return
     */
    @GetMapping("/gcBalCompareApp")
    public String gcBalCompareApp(Model model) throws ParseException {
    	model.addAttribute("balcompareReson", AccebillConstant.AB_BILL_BALCOMPARE_QUERY);
    	return "bill/accebill/mobile/mobileAbBillBalCompareQuery";
    }
    /**
     * 票据余额对比分析数据
     */
    @PostMapping("/getBalCompareList")
    @ResponseBody
    public Object getBalCompareList(Model model, String corpId,String comparedCycle,
    		String billFlag, String statisticsMonth) throws ParseException {
        DataTablesInput input = new DataTablesInput();
        input.getFormdata().put("statisticsMonth", statisticsMonth);
        input.getFormdata().put("corpId", corpId);
        input.getFormdata().put("billType", billFlag);
        input.getFormdata().put("billFlag", "");
        input.getFormdata().put("comparedCycle", comparedCycle);
        input.getFormdata().put("moneyUnit", "3");//币别单位为万元
        input.setLength(5000);
        @SuppressWarnings("rawtypes")
		DataTablesOutput data = abBillBalCompareQueryService.getDataTable(input);
        @SuppressWarnings("unchecked")
		List<AbBillBalCompareVO> balComList = data.getData();
        return balComList;
    }
}
