package com.bytter.supply.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.dto.ScfVoucherRedemptionDTO;
import com.bytter.supply.domain.payable.service.ScfVoucherRedemptionService;
import com.bytter.supply.domain.payable.vo.ScfVoucherRedemptionVO;
import com.bytter.supply.enums.SupplyMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "/v1-supply/supplyChain/payable/voucherRedemption", tags = "供应链金融凭证兑付记录相关")
@RestController("v1ScfVoucherRedemptionController")
@RequestMapping("/v1-supply/supplyChain/payable/voucherRedemption")
public class ScfVoucherRedemptionController extends BaseController {

    @Autowired
    private ScfVoucherRedemptionService scfVoucherRedemptionService;

    @ApiOperation("凭证兑付记录列表数据Table页")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<ScfVoucherRedemptionVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfVoucherRedemptionService.getDataTablesUnion(tablesInput));
    }

    @ApiOperation("应付债权凭证待审批列表数据查看")
    @PostMapping("/datatableApproval")
    @ResponseBody
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> datatableApproval(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfVoucherRedemptionService.getDataTablesUnionApproval(tablesInput));
    }
    @ApiOperation("新增凭证兑付记录")
    @LogMessage(content = "新增凭证兑付记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ScfVoucherRedemptionDTO dto) {
        scfVoucherRedemptionService.saveVoucherRedemption(dto);
        return AjaxJson.success();
    }

    @ApiOperation("修改凭证兑付记录")
    @LogMessage(content = "修改凭证兑付记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody ScfVoucherRedemptionDTO dto) {
        if (StringUtils.isEmpty(dto.getId())) {
            throw new BusinessException(SupplyMessageEnum.SCF_DATA_ID_NULL);
        }
        scfVoucherRedemptionService.updateVoucherRedemption(dto);
        return AjaxJson.success();
    }

    @ApiOperation("删除凭证兑付记录")
    @LogMessage(content = "删除凭证兑付记录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        scfVoucherRedemptionService.deleteVoucherRedemption(id);
        return AjaxJson.success();
    }

    @ApiOperation("根据主键查询凭证兑付记录详情")
    @GetMapping("/view/{id}")
    public AjaxJson<ScfVoucherRedemptionVO> view(@PathVariable("id") String id) {
        ScfVoucherRedemptionVO vo = scfVoucherRedemptionService.getVoucherRedemptionById(id);
        return AjaxJson.success(vo);
    }

    @ApiOperation("兑付结果查询触发")
    @GetMapping("/getRedemptionResults")
    public AjaxJson<Boolean> getRedemptionResults(String ids){
        List<String> idList = StringUtils.splitToList(",",ids);
        return AjaxJson.success(scfVoucherRedemptionService.getRedemptionResults(idList));
    }

    @ApiOperation("根据凭证主键查询兑付记录详情")
    @GetMapping("/getInfoByVoucherId/{voucherId}")
    public AjaxJson<ScfVoucherRedemptionVO> getInfoByVoucherId(@PathVariable("voucherId")String voucherId){
        return AjaxJson.success(scfVoucherRedemptionService.getInfoByVoucherId(voucherId));
    }

    @ApiOperation(
            value = "根据兑付主键/凭证ID查询兑付记录详情",
            notes = "voucherId 和 id 二选一传入：若从应付模块进入，传 voucherId；若从兑付模块进入，传 id。")
    @GetMapping("/getRedemptionInfo")
    public AjaxJson<ScfVoucherRedemptionVO> getRedemptionInfo(@RequestParam(name = "voucherId",required = false) String voucherId
                                                                ,@RequestParam(name = "id",required = false) String id){
        return AjaxJson.success(scfVoucherRedemptionService.getRedemptionInfo(voucherId,id));
    }
}
