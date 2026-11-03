package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpPayBillBalService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Controller
 * @Description: 商户信息对账单明细Controller
 * @date 2022-02-15
 */
@Controller
@RequestMapping("/ttpay/payBillBal")
public class TpPayBillBalController extends BaseController {
    @Autowired
    private TpPayBillBalService tpPayBillBalService;
    /**
     * 商户信息流水查询列表页面
     *
     * @param model
     * @return
     */
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYBAL_OP)
    public String list(String billDate, String payType, Model model) {
        model.addAttribute("trandTimeStart", DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("trandTimeEnd", DateUtils.format(new Date(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "ttpay/listTpPayBillBal";
    }
    /**
     * 商户信息流水查询列表数据获取
     *
     * @param input
     * @return 列表数据
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYBAL_OP)
    @LogMessage(content = "查询商户信息余额列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput input) {
        return tpPayBillBalService.getDataTablesUnion(input);
    }
}
