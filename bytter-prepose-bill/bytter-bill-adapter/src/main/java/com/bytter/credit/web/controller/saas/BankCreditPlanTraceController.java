package com.bytter.credit.web.controller.saas;

import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.credit.domain.service.BankCreditPlanTraceService;
import com.bytter.credit.domain.vo.BankCreditPlanTraceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 银行授信规划及月度跟踪表
 *
 * @author loveCamille
 */
@Api(tags = "银行授信规划及月度跟踪表", value = "/v1-cms/cms/bank/credit/plan/trace")
@RestController("v1BankCreditPlanTraceController")
@RequiredArgsConstructor
@RequestMapping("/v1-cms/cms/bank/credit/plan/trace")
public class BankCreditPlanTraceController extends BaseController {

    private final BankCreditPlanTraceService bankCreditPlanTraceService;

    @ApiOperation("查询银行授信规划及月度跟踪表表格数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BankCreditPlanTraceVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bankCreditPlanTraceService.dataTable(input, getCurrentUser()));
    }

}
