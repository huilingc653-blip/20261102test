package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbEmitBillPayReportService;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayReportVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 到期兑付确认统计
 * @author onlineGenerator
 * @date 2018-01-15 16:35:25
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abemitbillpayReport", tags = "到期兑付确认统计")
@RestController("v1AbEmitBillPayReportController")
@RequestMapping("/v1-preposebill/bill/accebill/abemitbillpayReport")
public class AbEmitBillPayReportController extends BaseController {
    @Autowired
    private AbEmitBillPayReportService abEmitBillPayReportService;

    /**
     * 到期兑付确认统计报表 table列表
     */
    @PostMapping("/datatable")
    @ApiOperation("查询到期兑付确认统计报表分页列表")
    @LogMessage(content = "查询到期兑付确认统计报表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput<AbEmitBillPayReportVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput,
                                                                       String statistical, String startDate, String endDate, String corpId) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<AbEmitBillPayReportVO> output = abEmitBillPayReportService.listAbEmitBillPayReport(dataInput, statistical, startDate, endDate, corpId);
        return AjaxJson.success(output);
    }
}