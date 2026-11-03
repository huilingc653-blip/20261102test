package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.service.AbPoolBailBookService;
import com.bytter.bill.domain.billPool.vo.AbPoolBailBookQueryVO;
import com.bytter.bill.domain.billPool.vo.AbPoolBailBookVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: Controller
 * @Description: 保证金登记簿
 */
@Api(value = "/v1-preposebill/billPool/bailBook", tags = "保证金登记簿")
@RestController("v1AbPoolBailBookController")
@RequestMapping("/v1-preposebill/billPool/bailBook")
public class AbPoolBailBookController extends BaseController {

    @Autowired
    private AbPoolBailBookService abPoolBailBookService;

    /**
     * 保证金登记簿列表页面日期区间参数
     */
    @ApiOperation("保证金登记簿列表页面日期区间参数")
    @GetMapping("/listQuery")
    public AjaxJson<AbPoolBailBookQueryVO> listAbPoolBailBookVO() {
        AbPoolBailBookQueryVO result = new AbPoolBailBookQueryVO();
        result.setStartDate(DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        result.setEndDate(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(result);
    }

    /**
     * 保证金登记簿 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询保证金登记簿")
    @LogMessage(content = "查询保证金登记簿", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolBailBookService.getDataTablesUnion(input));
    }

    /**
     * 查看保证金登记簿详情数据
     */
    @ApiOperation("查看保证金登记簿详情")
    @LogMessage(content = "查看保证金登记簿详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/view")
    public AjaxJson<AbPoolBailBookVO> view(String id) {
        return success(abPoolBailBookService.getAbPoolBailBookVOByDtlId(id));
    }
}
