package com.bytter.report.web.netbank.controller;
import com.bytter.biz.payment.api.service.IFcVoucherTypeService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.report.domain.netbank.service.SettlementBusinessService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 结算业务统计表Controller
 *
 * @author qijh 2022-11-30
 */
@Controller
@RequestMapping("/bi/settlementBusiness")
public class SettlementBusinessController extends BaseController {
    @Autowired(required = false)
    private SettlementBusinessService settlementBusinessService;
    @DubboReference(check = false)
	private IFcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
	private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
	private ICorpService corpService;
        @GetMapping("/list")
    public String list(Model model) {
        String resource = ResourceConstant.NETBANK_REPORT_SETTLEMENT_BUSINESS_QUERY;
        List<String> list = corpService.getUserNetIds(resource,0,this.getCurrentUser().getId());
        if(!EmptyUtils.isEmpty(list) && list.size()>0) {
            model.addAttribute("netId", list.get(0));
        }
        //获取有效的付款方式
        model.addAttribute("voucherTypes", fcVoucherTypeService.getFcVoucherTypeAll());
        BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
        model.addAttribute("rmbCur", rmbCurrency);
        model.addAttribute("beginSerachDate", DateUtils.format(DateUtils.getFirstMonthDate(DateUtils.getDate()), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return "netbank/listSettlementBusiness";
    }
        @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return settlementBusinessService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
}
