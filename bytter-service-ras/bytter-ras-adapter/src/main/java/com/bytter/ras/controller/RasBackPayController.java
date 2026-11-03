package com.bytter.ras.controller;

import com.bytter.ras.entity.RasBackPay;
import com.bytter.ras.service.RasBackPayService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.ras.api.constant.RasConstant;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核退款管理Controller
 * @date 2019/5/9 13:39
 */
@Controller
@RequestMapping("/ras/backPay")
public class RasBackPayController extends BaseController {

    @Autowired
    private RasBackPayService rasBackPayService;

    /**
     * 跳转退款申请处理页面
     * @param model
     * @return
     */
    @GetMapping("/opList")
    public String list(Model model) {
        return "ras/listBackPayOp";
    }

    /**
     * 退款申请处理列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "查询退款申请处理列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return rasBackPayService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 提交退款单
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKPAYOP_OP)
    @PostMapping("/submitOrder/{ids}")
    @ResponseBody
    public AjaxJson submitOrder(@PathVariable("ids")String ids){
        rasBackPayService.submitOrder(ids,this.getCurrentUser());
        return success();
    }

    /**
     * 异步查询打回原因
     */
    @PostMapping("/checkReason/{ids}")
    @ResponseBody
    public AjaxJson checkReason(@PathVariable("ids")String ids){
        RasBackPay rasBackPay = rasBackPayService.get(ids);
        AjaxJson ajax = new AjaxJson();
        ajax.setObj(rasBackPay.getReturnMsg());
        return ajax;
    }
    /**
     * 打回退款单信息保存（处理页面）
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKPAYOP_OP)
    @PostMapping("/saveBackOrder")
    @ResponseBody
    public AjaxJson saveBackOrder(String ids,String msg){
        rasBackPayService.backOrder(ids,msg,this.getCurrentUser());
        return success();
    }

    /**
     * 跳转退款申请查询页面
     * @param model
     * @return
     */
    @GetMapping("/queryList")
    public String listQuery(Model model) {
        return "ras/listBackPayQuery";
    }

    /**
     * 退款申请查询列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "查询退款申请查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableQuery")
    @ResponseBody
    public DataTablesOutput dataTableQuery(@RequestBody DataTablesInput dataTablesInput){
        return rasBackPayService.getDataTablesUnionQuery(dataTablesInput);
    }

    /**
     * 重发退款单
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKPAYQUERY_OP)
    @PostMapping("/reSendOrder/{ids}")
    @ResponseBody
    public AjaxJson reSendOrder(@PathVariable("ids")String ids){
        rasBackPayService.reSendOrder(ids,this.getCurrentUser());
        return success();
    }

    /**
     * 打回退款单（查询页面）
     */
    //@RequiresPermissions(RasConstant.RAS_RESOURCE_BACKPAYQUERY_OP)
    @PostMapping("/backOrderQuery/{ids}")
    @ResponseBody
    public AjaxJson backOrderQuery(@PathVariable("ids")String ids){
        rasBackPayService.backOrderQuery(ids,this.getCurrentUser());
        return success();
    }
}
