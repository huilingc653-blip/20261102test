package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillCollectionAccountReportService;
import com.bytter.bill.domain.bill.vo.AbBillCollectionAccountReportVo;
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
import org.springframework.ui.ConcurrentModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 到期托收到账统计
 *
 * @author songkk
 *
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillCollectionAccountReport", tags = "到期托收到账统计")
@RestController("v1AbBillCollectionAccountReportController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillCollectionAccountReport")
public class AbBillCollectionAccountReportController extends BaseController {
    @Autowired
    private AbBillCollectionAccountReportService abBillCollectionAccountReportService;

    /**
     * 统计报表-承兑汇票-到期托收到账统计 页面初始化
     */
    @GetMapping("/list")
    @ApiOperation("到期托收到账统计页面初始化")
    public AjaxJson<Map<String, Object>> list(AbBillCollectionAccountReportVo accountReportVo) {
        // 使用Spring内置Model实现，兼容原有Service入参，无需修改Service代码
        ConcurrentModel model = new ConcurrentModel();
        if ("1".equals(accountReportVo.getQuerySign())) {
            abBillCollectionAccountReportService.getSumCollectionAccountReportList(model, accountReportVo);
        }
        return AjaxJson.success(model.asMap());
    }

    /**
     * 到期托收到账统计 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询可托收到账的票据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("到期托收到账统计分页列表查询")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input, String countType) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = abBillCollectionAccountReportService.getDataTablesUnion(dataInput, countType);
        return AjaxJson.success(output);
    }
}