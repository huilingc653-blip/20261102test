package com.bytter.check.web.controller;

import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.check.service.PaymentBlendingService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
//TODO 放到资金核对
/**
 * 付款勾兑
 * @author qijh
 */
@Controller
@RequestMapping("/bankacc/paymentblending")
public class PaymentBlendingController extends BaseController {
    @Autowired
    private PaymentBlendingService paymentBlendingService;
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 付款明细勾对查询 页面跳转
     *
     * @return
     */
    @LogMessage(content = "付款明细勾对查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("budgetSel", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BISACCDTL);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        return "bankacc/listPaymentBlending";
    }
    /**
     * 付款明细勾对查询 页面跳转
     *
     * @return
     */
    @LogMessage(content = "付款明细勾对查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{dtlId}")
    public String listForDtlId(@PathVariable("dtlId") String dtlId,Model model) {
        model.addAttribute("budgetSel", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BISACCDTL);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("dtlId", dtlId);
        return "bankacc/listPaymentBlending";
    }
    /**
     * 银行账号明细表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(value = {
//            ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return paymentBlendingService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 付款单列表 table列表
     * @return
     */
    @SuppressWarnings("rawtypes")
    ////@RequiresPermissions(value = {
//            ResourceConstant.NETBANK_BANKACC_PAYMENTBLENDING_QUERY},
//            logical = Logical.OR)
    @PostMapping("/billdatatable")
    @ResponseBody
    public DataTablesOutput billdatatable(@RequestBody DataTablesInput dataTablesInput) {
        return paymentBlendingService.getBillDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 保存付款单业务类型
     * @param id
     * @param busType
     * @return
     */
    @RequestMapping(params = "toSaveBillBusType")
    @ResponseBody
    public AjaxJson toSaveBillBusType(String id, String busType, String isFrom){
        int count = paymentBlendingService.toSaveBillBusType(id,busType,isFrom);
        AjaxJson aj= new AjaxJson();
        if(count>0) {
            aj.setSuccess(true);
        }else {
            aj.setSuccess(false);
        }
        return  aj;
    }
    /**
     * 自动勾兑
     * @param ids
     * @param bankAccTypeStr
     * @return
     */
    @RequestMapping(params = "auto")
    @ResponseBody
    public AjaxJson auto(String ids, String bankAccTypeStr){
        String msg = "";
        msg = paymentBlendingService.autoPaymentBlending(ids,bankAccTypeStr, getCurrentUser().getUsername());
        if(StringUtils.isEmpty(msg)){
            msg = "";
        }
        AjaxJson aj= new AjaxJson();
        if(StringUtils.isNotEmpty(msg)){
            aj.setSuccess(false);
        }else{
            aj.setSuccess(true);
        }
        aj.setMsg(msg);
        return  aj;
    }
    /**
     * 手工勾兑
     * @param dtlId
     * @param billId
     * @param isFrom
     * @return
     */
    @RequestMapping(params = "manual")
    @ResponseBody
    public AjaxJson manual(String dtlId, String billId, String isFrom){
        paymentBlendingService.manualPaymentBlending(dtlId,billId,isFrom, getCurrentUser().getUsername());
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.paymentblending.success");
        return  aj;
    }
    /**
     * 取消勾兑
     * @param dtlId
     * @return
     */
    @RequestMapping(params = "cancel")
    @ResponseBody
    public AjaxJson cancel(String dtlId){
        paymentBlendingService.cancelPaymentBlending(dtlId, getCurrentUser().getUsername());
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.cancel.paymentblending.success");
        return  aj;
    }
    /**
     * 作废
     * @param ids
     * @return
     */
    @RequestMapping(params = "scrap")
    @ResponseBody
    public AjaxJson scrap(String ids){
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            paymentBlendingService.scrap(id, getCurrentUser().getUsername());
        }
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.scrap.success");
        return  aj;
    }
    /**
     * 取消作废
     * @param ids
     * @return
     */
    @RequestMapping(params = "cancelScrap")
    @ResponseBody
    public AjaxJson cancelScrap(String ids){
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            paymentBlendingService.cancelScrap(id, getCurrentUser().getUsername());
        }
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.cancel.scrap.success");
        return  aj;
    }
    /**
     * 作废跳转
     *
     * @return
     */
    @GetMapping("/scrap/{blendSign}")
    public String scrap(@PathVariable("blendSign") String blendSign, Model model) {
        model.addAttribute("blendSign",blendSign);
        return "bankacc/listPaymentScrap";
    }
    /**
     * 更新流水银行流水号
     * @param id
     * @return
     */
    @RequestMapping(params = "updateDtlBankSerialNo")
    @ResponseBody
    public AjaxJson updateDtlBankSerialNo(String id){
        paymentBlendingService.updateDtlBankSerialNo(id);
        return success();
    }
    @GetMapping("/view/{id}/{from}")
    public String view(@PathVariable("id") String id, @PathVariable("from") String from,Model model) {
        model.addAttribute("id",id);
        model.addAttribute("from",from);
        return "bankacc/listPaymentDtl";
    }
    /**
     * 银行账号明细表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/paymentDtlDataTable")
    @ResponseBody
    public DataTablesOutput paymentDtlDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return paymentBlendingService.getPaymentDtlDataTable(dataTablesInput);
    }
    /**
     * 导出全部
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                                String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput dataTablesOutput = paymentBlendingService.exportAll(dataTablesInput, user);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "付款明细勾对", 10000, BisAccDtlVO.class);
    }
}
