package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billPool.service.AbPoolFinaApplyBookService;
import com.bytter.bill.domain.billPool.vo.AbPoolDtlBookVO;
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
 * @Description: 融资申请登记簿
 */
@Api(value = "/v1-preposebill/billPool/finaApplyBook", tags = "融资申请登记簿")
@RestController("v1AbPoolEmitFinaApplyBookController")
@RequestMapping("/v1-preposebill/billPool/finaApplyBook")
public class AbPoolEmitFinaApplyBookController extends BaseController {

    @Autowired
    private AbPoolFinaApplyBookService abPoolFinaApplyBookService;

    /**
     * 融资申请登记簿明细数据
     */
    @ApiOperation("融资申请登记簿明细数据")
    @GetMapping("/viewFinaApplyInfo")
    public AjaxJson<AbPoolDtlBookVO> viewFinaApplyInfo(String id) {
        return success(abPoolFinaApplyBookService.getAbPoolDtlBookVOById(id));
    }

    /**
     * 查询融资申请登记簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询融资申请登记簿信息列表数据")
    @LogMessage(content = "查询出票融资申请账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableFinaApplyQuery")
    public AjaxJson<DataTablesOutput> datatableFinaApplyQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolFinaApplyBookService.datatableQueryBook(input));
    }

    /**
     * 查询融资申请登记簿明细账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询融资申请登记簿明细账簿信息列表数据")
    @LogMessage(content = "查询出票融资申请账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableFinaApplyQueryDtl")
    public AjaxJson<DataTablesOutput> datatableFinaApplyQueryDtl(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolFinaApplyBookService.datatableQueryDtl(input));
    }
}
