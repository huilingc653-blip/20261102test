package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbBillAccBalService;
import com.bytter.bill.domain.bill.service.AbBillCorpBalService;
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
/**
 * @Title: Controller
 * @Description: 票据单位余额
 * @author zhenghj
 * @date 2017-10-10
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/bill/accebill/abbillcorpbal")
public class AbBillCorpBalController extends BaseController {
    @Autowired
    private AbBillCorpBalService abBillCorpBalService;
    @Autowired
    private AbBillAccBalService abBillAccBalService;
    /**
     * 票据单位余额列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询票据单位余额列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbBillCorpBal";
    }
    /**
     * 票据单位余额 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        String corpIds = dataTablesInput.getFormdata().get("ins_corpId") == null ? null : dataTablesInput.getFormdata().get("ins_corpId").toString();
        //不通过余额表查数据
        DataTablesOutput out = abBillAccBalService.searchBalance(corpIds, this.getCurrentUser(), dataTablesInput, AccebillConstant.AB_BILL_CORPBAL_QUERY_CORPOP);
        //DataTablesOutput out = abBillCorpBalService.listAbBillCorpBal(dataTablesInput);
        return out;
    }
}
