package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.entity.TpRefundOrder;
import com.bytter.biz.ttpay.service.TpLogService;
import com.bytter.biz.ttpay.service.TpRefundOrderCheckService;
import com.bytter.biz.ttpay.util.TtPayUtil;
import com.bytter.biz.ttpay.vo.TpPayBillViewDtlVO;
import com.bytter.biz.ttpay.vo.TpRefundOrderVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * @Title: TpRefundOrderCheckController
 * @Description: 退款确认
 * @author wangzy
 * @date 2019/10/11 8:57
 *
 */
@Controller
@RequestMapping("/ttpay/refundOrderCheck")
public class TpRefundOrderCheckController extends BaseController {
    @Autowired
    private TpRefundOrderCheckService tpRefundOrderCheckService;
    @Autowired
    private TpLogService tpLogService;
    /**
     * 退款确认页面跳转
     * @param model
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDCHECK_OP)
    @GetMapping("/select")
    public String listRefundCheck(Model model) {
        model.addAttribute("backDateStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("backDateEnd", DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "ttpay/listTpRefundOrderCheck";
    }
    /**
     * 退款确认列表数据获取
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询退款确认列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDCHECK_OP)
    @PostMapping("/checkDatatable")
    @ResponseBody
    public DataTablesOutput checkDatatable(@RequestBody DataTablesInput input) {
        return tpRefundOrderCheckService.getCheckDataTablesUnion(input);
    }
    /**
     * 退款打回页面跳转
     * @param model
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDCHECK_OP)
    @GetMapping("/refundBack")
    public String refundBack(String id, Model model) {
        TpRefundOrder tpRefundOrder = tpRefundOrderCheckService.get(id,true);
        model.addAttribute("id", id);
        model.addAttribute("type",TtPayConstant.TTPAY_REFUND_OPTYPE_CHECK);
        model.addAttribute("backMoney", tpRefundOrder.getBackMoney());
        return "ttpay/updateTpRefundBack";
    }
    /**
     * 退款打回
     * @param tpRefundOrderVO
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDCHECK_OP)
    @PostMapping("/update")
    @ResponseBody
    @LogMessage(content = "退款确认打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_NOCONSENT)
    public AjaxJson refundCheckBack(TpRefundOrderVO tpRefundOrderVO){
        AjaxJson ajaxJson = success();
        if(tpRefundOrderVO != null){
            ajaxJson.setSuccess(tpRefundOrderCheckService.batchCheckOrBack(tpRefundOrderVO.getId(),
                    TtPayConstant.TTPAY_REFUND_OPTYPE_BACK, tpRefundOrderVO.getMsg(), this.getCurrentUser()));
        }
        return ajaxJson;
    }
    /**
     * 退款确认批量通过或打回
     * @param ids 确认退款ids
     * @param opType
     * @param
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDCHECK_OP)
    @PostMapping("/batchCheckOrBack")
    @ResponseBody
    @LogMessage(content = "退款确认批量通过或打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson batchCheckOrBack(String ids, String opType) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(tpRefundOrderCheckService.batchCheckOrBack(ids, opType, null, this.getCurrentUser()));
        return ajaxJson;
    }
    /**
     * 退款确认明细页面
     * @param id 退款数据id
     * @param payType 付款方式
     * @param dtlId 交易流水Id
     * @param model
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_REFUNDCHECK_OP)
    @GetMapping("/view")
    public String view(String id,String payType,String dtlId,Model model) {
        // 显示标题栏信息
        List<TpPayBillViewDtlVO> list= TtPayUtil.getTableViewField(payType);
        if (EmptyUtils.isNotEmpty(list)){
            model.addAttribute("payTypeTitle", list.get(0).getPayShowName());
        }
        // 交易数据信息
        model.addAttribute("dataInfo", tpRefundOrderCheckService.getPayBillDtlInfoByIdOrPayType(dtlId,payType));
        model.addAttribute("tpLogList",tpLogService.listTpLog(id));
        model.addAttribute("refundInfoList",tpRefundOrderCheckService.listRefundInfo(id));
        model.addAttribute("fieldList", list);
        model.addAttribute("isLog", true);
        model.addAttribute("isRefund", true);
        return "ttpay/viewTpRefundApplyDtl";
    }
}
