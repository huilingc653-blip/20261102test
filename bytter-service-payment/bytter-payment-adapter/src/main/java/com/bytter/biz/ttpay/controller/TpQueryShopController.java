package com.bytter.biz.ttpay.controller;
import com.bytter.biz.ttpay.service.TpBussinessService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.biz.payment.api.constant.TtPayConstant;
//////import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/ttpay/queryShop")
public class TpQueryShopController extends BaseController {
    @Autowired
    private TpBussinessService tpBussinessService;
    @GetMapping("/list")
    //////@RequiresPermissions(TtPayConstant.TTPAY_QUERY_SHOP_OP)
    public String list(Model model) {
        return "ttpay/listQueryShop";
    }
    @LogMessage(content = "商户维护列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    //////@RequiresPermissions(TtPayConstant.TTPAY_QUERY_SHOP_OP)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput input) {
        return tpBussinessService.getQueryDataTablesUnion(input);
    }
}
