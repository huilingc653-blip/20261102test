package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbClaimService;
import com.bytter.bill.domain.bill.vo.AbClaimMiddleVo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbClaimVo;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 票据认领处理Controller
 *
 * @author ouyj
 */
@Controller
@RequestMapping("/bill/accebill/abClaim")
public class AbClaimController extends BaseController {
    @Autowired
    private AbClaimService abClaimService;
    /**
     * 认领处理列表 页面跳转
     *
     * @param model 前端参数
     * @return 前端页面路径
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbClaim";
    }
    /**
     * 认领查询列表 页面跳转
     *
     * @param model 前端参数
     * @return 前端页面路径
     */
    @GetMapping("/queryList")
    public String queryList(Model model) {
        return "bill/accebill/listAbClaimQuery";
    }
    /**
     * 待人工认领列表 页面跳转
     *
     * @param model 前端参数
     * @return 前端页面路径
     */
    @GetMapping("/listClaim")
    public String listClaim(Model model) {
        String startDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
        model.addAttribute("startDate",startDate);
        String endDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        model.addAttribute("endDate",endDate);
        return "bill/accebill/listWaitManualClaim";
    }
    /**
     * 待确认列表 页面跳转
     *
     * @param model 前端参数
     * @return 前端页面路径
     */
    @GetMapping("/listWaitConfirm")
    public String listWaitConfirm(Model model) {
        String startDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
        model.addAttribute("startDate",startDate);
        String endDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        model.addAttribute("endDate",endDate);
        return "bill/accebill/listWaitConfirm";
    }
    /**
     * 已确认列表 页面跳转
     *
     * @param model 前端参数
     * @return 前端页面路径
     */
    @GetMapping("/listConfirmed")
    public String listConfirmed(Model model) {
        String startDate = DateUtils.format(DateUtils.getDate(), "yyyy-MM-dd");
        model.addAttribute("startDate",startDate);
        String endDate = DateUtils.format(DateUtils.getMonthFirstDate(new Date()), "yyyy-MM-dd");
        model.addAttribute("endDate",endDate);
        return "bill/accebill/listConfirmedClaim";
    }
    /**
     * 票据认领已确认列表
     *
     * @return 票据认领已确认前端列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableConfirmed")
    @ResponseBody
    @LogMessage(content = "查询票据认领已确认列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTableConfirmed(@RequestBody DataTablesInput dataTablesInput) {
        return abClaimService.listAbClaimConfirmedInfo(dataTablesInput);
    }
    /**
     * 票据认领已确认银行流水列表
     *
     * @return 票据认领已确认前端列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableConfirmedAcc")
    @ResponseBody
    @LogMessage(content = "查询票据认领银行流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput datatableConfirmedAcc(@RequestBody DataTablesInput dataTablesInput) {
        return abClaimService.listAbClaimConfirmedInfoAcc(dataTablesInput);
    }
    /**
     * 票据认领处理 table列表
     *
     * @return 票据认领处理前端列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    @LogMessage(content = "查询票据认领处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abClaimService.listAbClaimInfo(dataTablesInput);
    }
    /**
     * 票据认领处理银行流水 table列表
     *
     * @return 票据认领处理银行流水前端列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableAcc")
    @ResponseBody
    @LogMessage(content = "查询票据认领处理银行流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput datatableAcc(@RequestBody DataTablesInput dataTablesInput) {
        return abClaimService.listAbClaimInfoAcc(dataTablesInput);
    }
    /**
     * 待确认 table列表
     *
     * @return 待确认前端列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableWaitConfirm")
    @ResponseBody
    @LogMessage(content = "查询待确认列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput datatableWaitConfirm(@RequestBody DataTablesInput dataTablesInput) {
        return abClaimService.listWaitConfirmInfo(dataTablesInput);
    }
    /**
     * 待确认银行流水 table列表
     *
     * @return 待确认银行流水前端列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatableWaitConfirmAcc")
    @ResponseBody
    @LogMessage(content = "查询待确认银行流水列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public DataTablesOutput datatableWaitConfirmAcc(@RequestBody DataTablesInput dataTablesInput) {
        return abClaimService.listAbClaimConfirmedInfoAcc(dataTablesInput);
    }
    /**
     * 提交认领
     *
     * @param abClaimMiddleVo 认领中间表数据
     * @return 提交结果
     */
    @PostMapping("/submitClaim")
    @ResponseBody
    @LogMessage(content = "提交认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    public AjaxJson batchApply(AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.submitClaim(abClaimMiddleVo, this.getCurrentUser());
        return success();
    }
    /**
     * 转认领
     *
     * @param abClaimMiddleVo 认领中间表数据
     * @return 提交结果
     */
    @PostMapping("/transferClaim")
    @ResponseBody
    @LogMessage(content = "转认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    public AjaxJson transferClaim(AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.transferClaim(abClaimMiddleVo, this.getCurrentUser());
        return success();
    }
    /**
     * 待确认转提交确认
     *
     * @param abClaimMiddleVo 认领中间表数据
     * @return 提交结果
     */
    @PostMapping("/submitConfirm")
    @ResponseBody
    @LogMessage(content = "待确认转提交确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    public AjaxJson submitConfirm(AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.submitConfirm(abClaimMiddleVo, this.getCurrentUser());
        return success();
    }
    /**
     * 待确认转待认领
     *
     * @param abClaimMiddleVo 认领中间表数据
     * @return 提交结果
     */
    @PostMapping("/confirmToClaim")
    @ResponseBody
    @LogMessage(content = "待确认转待认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_BATCHAPPLY)
    public AjaxJson confirmToClaim(AbClaimMiddleVo abClaimMiddleVo) {
        abClaimService.confirmToClaim(abClaimMiddleVo);
        return success();
    }

    /**
     * 获取银行交易明细查询配置信息
     * @param vo
     * @return
     */
    @PostMapping("/getBankAccDtlQueryConfig")
    @ResponseBody
    public AjaxJson getBankAccDtlQueryConfig(@RequestBody AbClaimVo vo) {
        AjaxJson ajaxJson = success();
        ajaxJson.setObj(abClaimService.getBankAccDtlQueryConfig(vo));
        return ajaxJson;
    }
}
