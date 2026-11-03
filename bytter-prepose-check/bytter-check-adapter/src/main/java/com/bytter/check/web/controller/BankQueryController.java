package com.bytter.check.web.controller;
import com.bytter.biz.finacial.api.constant.FinacialResourceConstant;
import com.bytter.check.entity.FcBankBill;
import com.bytter.check.service.BankQueryService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.excel.entity.ExportParams;
import com.bytter.poi.excel.entity.vo.NormalExcelConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;
/**
 * @author zhengxn
 * @version 1.0
 * @Title:对账中心--银行对账单
 * @Description: FC_BANK_BILL
 * @date 2017-06-07 11:30:25
 */
@Controller
@RequestMapping("/finacial/check/bankquery")
public class BankQueryController extends BaseController {
    @Autowired
    private BankQueryService fcBankBillService;
    /**
     * 银行对账单列表 页面跳转
     **/
    @LogMessage(content = "查询FC_BANK_BILL列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "check/query/listBankQuery";
    }
    /**
     * 银行对账单 table列表
     **/
    ////@RequiresPermissions(FinacialResourceConstant.FINACIAL_CHECK_BANK_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcBankBillService.listTable(dataTablesInput, FinacialResourceConstant.FINACIAL_CHECK_BANK_QUERY, getUserId());
    }
    /**
     * 导出
     **/
    @RequestMapping("/excel")
    public String excel(ModelMap map, String eqs_compareSign, String eqs_bankAcc, String eqs_corpId, Date ged_tallyDate,
                        Date led_tallyDate) {
        List<FcBankBill> list = null;
        if (EmptyUtils.isNotEmpty(eqs_compareSign) && !" ".equals(eqs_compareSign)
                || EmptyUtils.isNotEmpty(eqs_bankAcc) && eqs_bankAcc != null
                || EmptyUtils.isNotEmpty(eqs_corpId) && !" ".equals(eqs_corpId)
                || !EmptyUtils.isEmpty(ged_tallyDate)
                || !EmptyUtils.isEmpty(led_tallyDate)) {
            list = fcBankBillService.listQuery(eqs_compareSign, eqs_bankAcc, eqs_corpId, ged_tallyDate, led_tallyDate);
        }
        map.put(NormalExcelConstants.FILE_NAME, "银行对账单");
        map.put(NormalExcelConstants.CLASS, FcBankBill.class);
        map.put(NormalExcelConstants.PARAMS, new ExportParams("银行对账单", "导出人：admin", "账单信息"));
        map.put(NormalExcelConstants.DATA_LIST, list);
        return NormalExcelConstants.EXCEL_VIEW;
    }
}
