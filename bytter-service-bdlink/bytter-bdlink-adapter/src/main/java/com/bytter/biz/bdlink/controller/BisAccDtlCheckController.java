package com.bytter.biz.bdlink.controller;

import com.bytter.biz.bdlink.service.BisAccDtlCheckService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 余额流水异常核查表
 * @date 2023-02-15 11:20:30
 */
@Controller
@RequestMapping("/bankacc/bisaccdtlcheck")
public class BisAccDtlCheckController extends BaseController {

    @Autowired
    private BisAccDtlCheckService bisAccDtlCheckService;

    /**
     * BIS_ACC_DTL_CHECK列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询BIS_ACC_DTL_CHECK列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        //默认查询一周的数据
        model.addAttribute("gedCheckDate", DateUtils.format(DateUtils.addDay(DateUtils.getDateYMD(), -7), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "bankacc/listBisAccDtlCheck";
    }

    /**
     * BIS_ACC_DTL_CHECK table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisAccDtlCheckService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 银行账户流水查询
     *
     * @param model
     * @return
     */
    @GetMapping("/queryDtl/{bankAcc}/{curId}/{checkDate}")
    public String queryDtl(@PathVariable("bankAcc") String bankAcc, @PathVariable("curId") String curId,
                           @PathVariable("checkDate") String checkDate, Model model) {
        model.addAttribute("bankAcc", bankAcc);
        model.addAttribute("curId", curId);
        model.addAttribute("checkDate", checkDate);
        return "bankacc/listBisAccDtlQuery";
    }

    /**
     * 核查账户设置
     *
     * @return
     */
    @LogMessage(content = "核查账户设置", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/setCheckBankAcc")
    public String setCheckBankAcc(Model model) {
        return "bankacc/setCheckBankAcc";
    }

    @PostMapping("/queryBankAccDatatable")
    @ResponseBody
    public DataTablesOutput queryBankAccDatatable(@RequestBody DataTablesInput dataTablesInput){
        return bisAccDtlCheckService.queryBankAccDatatable(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 批量设置已处理（保存）
     *
     * @return
     */
    @LogMessage(content = "批量设置已处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/setCheckBankAccSave")
    @ResponseBody
    public AjaxJson setCheckBankAccSave(@RequestParam("ids") String ids, @RequestParam("checkSign") String checkSign) {
        this.bisAccDtlCheckService.setCheckBankAccSave(ids, checkSign);
        return success();
    }

    /**
     * 批量设置已处理
     *
     * @return
     */
    @LogMessage(content = "批量设置已处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchHandled")
    @ResponseBody
    public AjaxJson batchHandled(@RequestParam("ids") String ids) {
        this.bisAccDtlCheckService.batchUpdateStatus(ids, CommonConstant.YES, this.getCurrentUser());
        return success();
    }

    /**
     * 批量设置未处理
     *
     * @return
     */
    @LogMessage(content = "批量设置未处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchUnHandled")
    @ResponseBody
    public AjaxJson batchUnHandled(@RequestParam("ids") String ids) {
        this.bisAccDtlCheckService.batchUpdateStatus(ids, CommonConstant.NO, this.getCurrentUser());
        return success();
    }
}
