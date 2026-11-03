package com.bytter.credit.web.controller;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credit.domain.service.BankCreditPlanTraceService;
import com.bytter.credit.domain.vo.BankCreditPlanTraceVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * 银行授信规划及月度跟踪表
 *
 * @author loveCamille
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/cms/bank/credit/plan/trace")
public class BankCreditPlanTraceController extends BaseController {

    private final BankCreditPlanTraceService bankCreditPlanTraceService;

    @GetMapping("/list")
    public String list() {
        return "cms/bank/credit/listBankCreditPlanTrace";
    }

    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput<BankCreditPlanTraceVO> dataTable(@RequestBody DataTablesInput input) {
        return bankCreditPlanTraceService.dataTable(input, getCurrentUser());
    }

}
