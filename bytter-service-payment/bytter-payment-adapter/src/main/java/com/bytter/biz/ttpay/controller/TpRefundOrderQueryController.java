package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
import com.bytter.biz.ttpay.service.TpRefundOrderQueryService;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
/**
 * @Title: TpRefundOrderQueryController
 * @Description: 退款查询
 * @author wangzy
 * @date 2019/10/11 9:00
 *
 */
@Controller
@RequestMapping("/ttpay/refundOrderQuery")
public class TpRefundOrderQueryController extends BaseController {
    @Autowired
    private TpRefundOrderQueryService tpRefundOrderQueryService;
    /**
     * 退款查询页面跳转
     * @param model
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDQUERY_OP)
    @GetMapping("/select")
    public String listRefundQuery(Model model) {
        model.addAttribute("backDateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("backDateEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "ttpay/listTpRefundOrderQuery";
    }
    /**
     * 退款查询列表数据获取
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询退款列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDQUERY_OP)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput input) {
        return tpRefundOrderQueryService.getQueryDataTablesUnion(input);
    }
    /**
     * 退款打回页面跳转
     * @param model
     * @return
     */
    @GetMapping("/refundBack")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDQUERY_RELAUNCH)
    public String refundBack(String id, Model model) {
        TpRefundOrder tpRefundOrder = tpRefundOrderQueryService.get(id,true);
        model.addAttribute("id", id);
        model.addAttribute("backMoney", tpRefundOrder.getBackMoney());
        return "ttpay/updateTpRefundBack";
    }
    /**
     * 退款打回
     * @param tpRefundOrderVO
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDQUERY_RELAUNCH)
    @PostMapping("/update")
    @ResponseBody
    @LogMessage(content = "退款失败打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_NOCONSENT)
    public AjaxJson refundQueryUpdate(TpRefundOrderVO tpRefundOrderVO){
        tpRefundOrderQueryService.refundQueryUpdate(tpRefundOrderVO, getCurrentUser());
        return success();
    }
    /**
     * 退款重发
     * @param id 确认退款id
     * @param
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDQUERY_RELAUNCH)
    @PostMapping("/reLaunch")
    @ResponseBody
    @LogMessage(content = "退款申请指令重发", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson reLaunch(String id) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(tpRefundOrderQueryService.reLaunch(id, this.getCurrentUser()));
        return ajaxJson;
    }
}
