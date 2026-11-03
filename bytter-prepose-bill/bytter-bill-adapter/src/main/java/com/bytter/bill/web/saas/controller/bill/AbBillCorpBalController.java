package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillAccBalService;
import com.bytter.bill.domain.bill.service.AbBillCorpBalService;
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

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 票据单位余额
 * @author zhenghj
 * @date 2017-10-10
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillcorpbal", tags = "票据单位余额")
@RestController("v1AbBillCorpBalController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillcorpbal")
public class AbBillCorpBalController extends BaseController {
    @Autowired
    private AbBillCorpBalService abBillCorpBalService;
    @Autowired
    private AbBillAccBalService abBillAccBalService;

    /**
     * 票据单位余额列表 页面初始化
     */
    @LogMessage(content = "查询票据单位余额列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("票据单位余额列表页面初始化")
    @GetMapping("/list")
    public AjaxJson<Map<String, Object>> list() {
        Map<String, Object> resultMap = new HashMap<>();
        return AjaxJson.success(resultMap);
    }

    /**
     * 票据单位余额 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("票据单位余额分页列表查询")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        String corpIds = input.getFormdata().get("ins_corpId") == null ? null : input.getFormdata().get("ins_corpId").toString();
        //不通过余额表查数据
        DataTablesOutput out = abBillAccBalService.searchBalance(corpIds, this.getCurrentUser(), input, AccebillConstant.AB_BILL_CORPBAL_QUERY_CORPOP);
        //DataTablesOutput out = abBillCorpBalService.listAbBillCorpBal(dataTablesInput);
        return AjaxJson.success(out);
    }
}