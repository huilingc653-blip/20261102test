package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.AbBillBalCompareQueryService;
import com.bytter.bill.domain.bill.vo.AbBillBalCompareVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhenghj
 * @version 1.0
 * @Title: Controller
 * @Description: 票据余额对比分析入口
 * @date 2020-01-15
 */
@Api(value = "/v1-preposebill/bill/accebill/balCompare", tags = "票据余额对比分析入口")
@RestController("v1AbBillBalCompareQueryController")
@RequestMapping("/v1-preposebill/bill/accebill/balCompare")
public class AbBillBalCompareQueryController extends BaseController {
    @Autowired
    private AbBillBalCompareQueryService abBillBalCompareQueryService;

    /**
     * 票据余额对比分析分页数据
     *
     * @param dataTablesInput 分页参数
     * @throws ParseException 日期解析异常
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("票据余额对比分析分页数据查询")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) throws ParseException {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput data = abBillBalCompareQueryService.getDataTable(input);
        return AjaxJson.success(data);
    }

    /**
     * 票据余额对比明细表分页数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("票据余额对比明细分页数据查询")
    @PostMapping("/detailedDatatable")
    public AjaxJson<DataTablesOutput> detailedDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abBillBalCompareQueryService.listBalCompareDetailed(input);
        return AjaxJson.success(output);
    }

    /**
     * 票据余额对比分析app页面初始化
     */
    @ApiOperation("票据余额对比分析APP页面初始化")
    @RequestMapping("/balCompareApp")
    public AjaxJson<Map<String, Object>> balCompareApp() throws ParseException {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("balcompareReson", AccebillConstant.AB_BILL_BALCOMPARE_QUERY);
        return AjaxJson.success(resultMap);
    }

    /**
     * 票据余额对比分析app页面初始化
     */
    @ApiOperation("票据余额对比分析APP页面初始化")
    @GetMapping("/gcBalCompareApp")
    public AjaxJson<Map<String, Object>> gcBalCompareApp() throws ParseException {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("balcompareReson", AccebillConstant.AB_BILL_BALCOMPARE_QUERY);
        return AjaxJson.success(resultMap);
    }

    /**
     * 票据余额对比分析APP数据查询
     */
    @ApiOperation("票据余额对比分析APP列表数据")
    @PostMapping("/getBalCompareList")
    public AjaxJson<List<AbBillBalCompareVO>> getBalCompareList(String corpId, String comparedCycle,
                                                                String billFlag, String statisticsMonth) throws ParseException {
        DataTablesInput input = new DataTablesInput();
        input.getFormdata().put("statisticsMonth", statisticsMonth);
        input.getFormdata().put("corpId", corpId);
        input.getFormdata().put("billType", billFlag);
        input.getFormdata().put("billFlag", "");
        input.getFormdata().put("comparedCycle", comparedCycle);
        input.getFormdata().put("moneyUnit", "3");//币别单位为万元
        input.setLength(5000);
        @SuppressWarnings("rawtypes")
        DataTablesOutput data = abBillBalCompareQueryService.getDataTable(input);
        @SuppressWarnings("unchecked")
        List<AbBillBalCompareVO> balComList = data.getData();
        return AjaxJson.success(balComList);
    }
}