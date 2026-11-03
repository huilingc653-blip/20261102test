package com.bytter.biz.ttpay.controller;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.payment.api.enums.TtPayMessageEnum;
import com.bytter.biz.ttpay.entity.TpPayment;
import com.bytter.biz.ttpay.service.TpPaymentQueryService;
import com.bytter.biz.ttpay.vo.TpPaymentVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.constant.SysConfigConstant;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author mof
 * @version 1.0
 * @Title: Controller
 * @Description: 第三方支付查询业务
 * @date 2022-05-16
 */
@Controller
@RequestMapping("/ttpay/tpPaymentQuery")
public class TpPaymentQueryController extends BaseController {
    @Autowired
    private TpPaymentQueryService tpPaymentQueryService;
    @DubboReference(check = false)
	private IConfigService configService;
    /**
     * 第三方支付查询列表页面
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENTQUERY_QUERY)
    public String list(Model model) {
        return "ttpay/listTpPaymentQuery";
    }
    /**
     * 第三方支付查询列表页面
     *
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENTQUERY_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return tpPaymentQueryService.getDataTablesUnion(dataTablesInput,this.getCurrentUser());
    }
    /**
     * 第三方支付查询详情
     *
     * @return
     */
    @LogMessage(content = "第三方支付查询详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        TpPaymentVO tpPayment = tpPaymentQueryService.getByIdUnion(id);
        model.addAttribute("tpPaymentPage", tpPayment);
        //初始化参数
        this.initConfig(model, 0);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TTPAYMENT);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_TP);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        return "ttpay/viewTpPaymentQuery";
    }
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENTQUERY_RECALL)
    @LogMessage(content = "批量退回第三方支付单据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchRecall")
    @ResponseBody
    public AjaxJson batchRecall(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            TpPayment entity = tpPaymentQueryService.get(id, true);
            //付款失败时，才允许退回单据
            if (entity != null && !TtPayConstant.TTPAY_REFUND_VOUCHER_STATUS2.equals(entity.getVoucherStat())) {
                throw new ParameterException(TtPayMessageEnum.TTPAY_MESSAGE_RECALL_NO, entity.getBillNo());
            }
        }
        tpPaymentQueryService.batchRecallTpPayment(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 初始化系统参数
     *
     * @param model
     * @param busType
     */
    private void initConfig(Model model, Integer busType) {
        model.addAttribute("budgetSel", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BUDGETSEL).getValue());
        model.addAttribute("payItem", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_PAYITEM).getValue());
        model.addAttribute("bankaccOver", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BANKACCOVER).getValue());
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //付款余额控制方式  0-账户余额 1-可用余额
        model.addAttribute("balControlType", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BALCONTROLTYPE).getValue());
    }
}
