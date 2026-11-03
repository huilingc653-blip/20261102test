package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbBillTransferDownService;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownCorpDtlVO;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownNetCollectVO;
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
 * @Description: 票据划拨公共控制器
 * @author onlineGenerator
 * @date 2017-10-31
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abbillTransferDown", tags = "票据划拨公共控制器")
@RestController("v1AbBillTransferDownController")
@RequestMapping("/v1-preposebill/bill/accebill/abbillTransferDown")
public class AbBillTransferDownController extends BaseController {
    @Autowired
    private AbBillTransferDownService abBillTransferDownService;



    /**
     * 加载票据下拨业务信息数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataQueryDtltable")
    @ApiOperation("查询票据下拨业务信息列表")
    @LogMessage(content = "查询票据下拨业务信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataQueryDtlTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferDownService.listAbBillQueryDtlInfo(dataInput);
        return AjaxJson.success(output);
    }



    /**
     * 加载网点票据划拨汇总表信息数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataNetCollectTable")
    @ApiOperation("查询网点票据划拨汇总表信息数据")
    @LogMessage(content = "查询网点票据划拨汇总表信息数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataNetCollectTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferDownService.listNetCollectInfo(dataInput);
        return AjaxJson.success(output);
    }



    /**
     * app页面 加载网点票据划拨汇总表信息数据
     */
    @PostMapping("/dataAppNetCollectTable")
    @ApiOperation("APP查询网点票据划拨汇总图表数据")
    public AjaxJson dataAppNetCollectTable(AbBillTransferDownNetCollectVO netCollectVO) {
        Object result = abBillTransferDownService.createNetCollectInfoSumBar(netCollectVO, this.getCurrentUser());
        AjaxJson ajaxJson = AjaxJson.success();
        ajaxJson.setObj(result);
        return ajaxJson;
    }

    /**
     * 票据调拨明细表页面初始化
     */
    @GetMapping("/toCorpDtl")
    @ApiOperation("票据调拨明细表页面初始化")
    public AjaxJson<Map<String, Object>> toCopDtl(AbBillTransferDownCorpDtlVO corpDtlVO) {
        Map<String, Object> dataMap = new HashMap<>(6);
        dataMap.put("userCorpId", this.getCurrentUser().getCorpId());
        dataMap.put("queryParams", corpDtlVO);
        dataMap.put("forwardPath", "bill/accebill/listAbBillTransferDownCorpDtl");
        return AjaxJson.success(dataMap);
    }

    /**
     * 加载票据调拨明细表信息数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataCorpDtlTable")
    @ApiOperation("查询票据调拨明细表信息数据")
    @LogMessage(content = "查询票据调拨明细表信息数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataCorpDtlTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillTransferDownService.listCorpDtlInfo(dataInput);
        return AjaxJson.success(output);
    }
}