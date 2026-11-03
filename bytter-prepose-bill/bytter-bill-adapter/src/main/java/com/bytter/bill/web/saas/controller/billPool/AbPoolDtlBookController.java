package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.api.billPool.constant.AbPoolConstant;
import com.bytter.bill.domain.billPool.service.AbPoolDtlBookService;
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
 * @Description: 票据池明细账簿信息
 */
@Api(value = "/v1-preposebill/billPool/dtlBook", tags = "票据池明细账簿信息")
@RestController("v1AbPoolDtlBookController")
@RequestMapping("/v1-preposebill/billPool/dtlBook")
public class AbPoolDtlBookController extends BaseController {

    @Autowired
    private AbPoolDtlBookService abPoolDtlBookService;

    /**
     * 提示付款明细数据
     */
    @ApiOperation("提示付款明细数据")
    @GetMapping("/viewPaymentInfo")
    public AjaxJson<AbPoolDtlBookVO> viewPaymentInfo(String id) {
        return success(abPoolDtlBookService.getAbPoolDtlBookVOById(id));
    }

    /**
     * 查询提示付款票据池明细账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询提示付款票据池明细账簿信息列表数据")
    @LogMessage(content = "查询提示付款票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatablePaymentQuery")
    public AjaxJson<DataTablesOutput> datatablePaymentQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.setResource(AbPoolConstant.AB_POLL_QUERY_PAY_OP);
        return success(abPoolDtlBookService.datatableQuery(input));
    }

    /**
     * 出票申请明细数据
     */
    @ApiOperation("出票申请明细数据")
    @GetMapping("/viewEmitInfo")
    public AjaxJson<AbPoolDtlBookVO> viewEmitInfo(String id) {
        return success(abPoolDtlBookService.getAbPoolDtlBookVOById(id));
    }

    /**
     * 查询出票申请票据池明细账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询出票申请票据池明细账簿信息列表数据")
    @LogMessage(content = "查询出票申请票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableEmitQuery")
    public AjaxJson<DataTablesOutput> datatableEmitQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.setResource(AbPoolConstant.AB_POLL_QUERY_EMIT_OP);
        return success(abPoolDtlBookService.datatableQuery(input));
    }

    /**
     * 贴现明细数据
     */
    @ApiOperation("贴现明细数据")
    @GetMapping("/viewDiscountInfo")
    public AjaxJson<AbPoolDtlBookVO> viewDiscountInfo(String id) {
        return success(abPoolDtlBookService.getAbPoolDtlBookVOById(id));
    }

    /**
     * 查询贴现票据池明细账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询贴现票据池明细账簿信息列表数据")
    @LogMessage(content = "查询贴现票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableDiscountQuery")
    public AjaxJson<DataTablesOutput> datatableDiscountQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.setResource(AbPoolConstant.AB_POLL_QUERY_DISCOUNT_OP);
        return success(abPoolDtlBookService.datatableQuery(input));
    }

    /**
     * 背书明细数据
     */
    @ApiOperation("背书明细数据")
    @GetMapping("/viewEndorseInfo")
    public AjaxJson<AbPoolDtlBookVO> viewEndorseInfo(String id) {
        return success(abPoolDtlBookService.getAbPoolDtlBookVOById(id));
    }

    /**
     * 查询背书票据池明细账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询背书票据池明细账簿信息列表数据")
    @LogMessage(content = "查询背书票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableEndorseQuery")
    public AjaxJson<DataTablesOutput> datatableEndorseQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.setResource(AbPoolConstant.AB_POLL_QUERY_ENDORSE_OP);
        return success(abPoolDtlBookService.datatableQuery(input));
    }

    /**
     * 出入池明细数据
     */
    @ApiOperation("出入池明细数据")
    @GetMapping("/viewInoutInfo")
    public AjaxJson<AbPoolDtlBookVO> viewInoutInfo(String id) {
        return success(abPoolDtlBookService.getAbPoolDtlBookVOById(id));
    }

    /**
     * 查询出入池票据池明细账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询出入池票据池明细账簿信息列表数据")
    @LogMessage(content = "查询出入池票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatableInoutQuery")
    public AjaxJson<DataTablesOutput> datatableInoutQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.setResource(AbPoolConstant.AB_POLL_QUERY_INOUT_OP);
        return success(abPoolDtlBookService.datatableQuery(input));
    }

    /**
     * 质押托管转换明细数据
     */
    @ApiOperation("质押托管转换明细数据")
    @GetMapping("/viewPdTstInfo")
    public AjaxJson<AbPoolDtlBookVO> viewPdTstInfo(String id) {
        return success(abPoolDtlBookService.getAbPoolDtlBookVOById(id));
    }

    /**
     * 查询质押托管转换票据池明细账簿信息列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询质押托管转换票据池明细账簿信息列表数据")
    @LogMessage(content = "查询质押托管转换票据池明细账簿信息列表数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatablePdTstQuery")
    public AjaxJson<DataTablesOutput> datatablePdTstQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        input.setResource(AbPoolConstant.AB_POLL_QUERY_PDTST_OP);
        return success(abPoolDtlBookService.datatableQuery(input));
    }
}
