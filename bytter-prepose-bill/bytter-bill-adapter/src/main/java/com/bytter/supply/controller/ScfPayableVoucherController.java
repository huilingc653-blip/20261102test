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
import com.bytter.bill.api.scf.dto.ScfPayableVoucherSelDTO;
import com.bytter.bill.api.scf.service.IScfVoucherPayService;
import com.bytter.supply.domain.payable.dto.ScfPayableVoucherCancelDTO;
import com.bytter.supply.domain.payable.dto.ScfPayableVoucherDataDTO;
import com.bytter.supply.domain.payable.service.ScfPayableVoucherExcelService;
import com.bytter.supply.domain.payable.service.ScfPayableVoucherService;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherDataVO;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherExcelVO;
import com.bytter.supply.domain.payable.vo.ScfPayableVoucherVO;
import com.bytter.supply.enums.SupplyMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherInfoController
 * @Description: 应收债权凭证信息控制器
 * @since 2026/6/30
 */
@Api(value = "/v1-supply/supplyChain/payable/voucherInfo",tags = "供应链金融应付债权凭证数据相关")
@RestController("v1ScfPayableVoucherController")
@RequestMapping("/v1-supply/supplyChain/payable/voucherInfo")
public class ScfPayableVoucherController extends BaseController {

    @Autowired
    private ScfPayableVoucherService scfPayableVoucherService;

    @Autowired
    private ScfPayableVoucherExcelService scfPayableVoucherExcelService;

    @Autowired
    private IScfVoucherPayService scfVoucherPayService;
    @ApiOperation("应付债权凭证列表数据Table页")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<ScfPayableVoucherDataVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfPayableVoucherService.getDataTablesUnion(tablesInput));
    }

    @ApiOperation("应付债权凭证列表数据查看")
    @PostMapping("/datatableQuery")
    @ResponseBody
    public AjaxJson<DataTablesOutput<ScfPayableVoucherDataVO>> datatableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfPayableVoucherService.getDataTablesUnionQuery(tablesInput));
    }

    @ApiOperation("应付债权凭证待审批列表数据查看")
    @PostMapping("/datatableApproval")
    @ResponseBody
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> datatableApproval(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfPayableVoucherService.getDataTablesUnionApproval(tablesInput));
    }

    /**
     * 新增应付债权凭证列表数据
     */
    @ApiOperation("新增应付债权凭证数据")
    @LogMessage(content = "新增应付债权凭证数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody ScfPayableVoucherDataDTO scfPayableVoucherDataDTO) {
        scfPayableVoucherService.savePayableVoucher(scfPayableVoucherDataDTO);
        return AjaxJson.success();
    }

    /**
     * 修改应付债权凭证数据
     */
    @ApiOperation("修改应付债权凭证数据")
    @LogMessage(content = "修改应付债权凭证数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody ScfPayableVoucherDataDTO scfPayableVoucherDataDTO) {
        if (StringUtils.isEmpty(scfPayableVoucherDataDTO.getId())) {
            throw new BusinessException(SupplyMessageEnum.SCF_DATA_ID_NULL);
        }
        scfPayableVoucherService.updatePayableVoucher(scfPayableVoucherDataDTO);
        return AjaxJson.success();
    }

    /**
     * 删除应付债权凭证数据
     */
    @ApiOperation("删除应付债权凭证数据")
    @LogMessage(content = "删除应付债权凭证数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        scfPayableVoucherService.deletePayableVoucher(id);
        return AjaxJson.success();
    }

    /**
     * 撤销开立中的应付债权凭证数据
     */
    @ApiOperation("撤销开立中的应付债权凭证数据")
    @LogMessage(content = "撤销开立中的应付债权凭证数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/revoke/{id}")
    @ResponseBody
    public AjaxJson revoke(@PathVariable("id") String id) {
        scfPayableVoucherService.revokePayableVoucher(id);
        return AjaxJson.success();
    }

    /**
     * 批量撤销开立中的应付债权凭证数据
     */
    @ApiOperation("批量撤销开立中的应付债权凭证数据")
    @LogMessage(content = "批量撤销开立中的应付债权凭证数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/revokeBatch")
    @ResponseBody
    public AjaxJson revokeBatch(@RequestBody List<String> ids) {
        scfPayableVoucherService.batchRevokePayableVoucher(ids);
        return AjaxJson.success();
    }

    /**
     * 作废应付债权凭证（仅已开立/已撤销/已拒签可作废，作废原因/作废时间由前端录入）
     */
    @ApiOperation("作废应付债权凭证数据")
    @LogMessage(content = "作废应付债权凭证数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/cancel")
    @ResponseBody
    public AjaxJson cancel(@RequestBody ScfPayableVoucherCancelDTO dto) {
        scfPayableVoucherService.cancelPayableVoucher(dto);
        return AjaxJson.success();
    }


    @ApiOperation("获取已开立的应付债权凭证列表")
    @PostMapping("/getEstablishedPayVoucher")
    @ResponseBody
    public AjaxJson<ScfPayableVoucherVO> getEstablishedPayVoucher(@RequestBody V1DataTablesInput dataTablesInput){
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfPayableVoucherService.getEstablishedPayVoucher(tablesInput));
    }

    /**
     * 根据主键查询应付债权凭证详情
     */
    @ApiOperation("根据主键查询应付债权凭证详情")
    @GetMapping("/view/{id}")
    @ResponseBody
    public AjaxJson<ScfPayableVoucherDataVO> view(@PathVariable("id") String id) {
        ScfPayableVoucherDataVO scfPayableVoucherDataVO = scfPayableVoucherService.getPayableVoucherDataById(id);
        return AjaxJson.success(scfPayableVoucherDataVO);
    }

    @LogMessage(content = "导入数据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_Import)
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<ScfPayableVoucherExcelVO> list) {
        AjaxJson json = success();
        scfPayableVoucherExcelService.saveImport(list, this.getCurrentUser());
        json.setSuccess(true);
        return success();
    }

    /**
     * 供应链金融（付款办理51）应付债权凭证选票：datatable 分页查询已开立的应付债权凭证
     * <p>
     * 后端已过滤"审批通过 + 已开立"，并排除已被付款单关联（pay_id 非空）的凭证。
     *
     * @param dataTablesInput datatable 分页/排序/检索参数（开立单位/平台/融资产品/线上线下等通过列检索传入）
     * @return 符合条件的应付债权凭证
     */
    @ApiOperation("供应链金融-付款办理应付债权凭证选票")
    @PostMapping("/listScfPayVoucher")
    @ResponseBody
    public AjaxJson<DataTablesOutput<ScfPayableVoucherVO>> listScfPayVoucher(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(scfPayableVoucherService.getPayVoucherForSelection(tablesInput));
    }

    /**
     * 供应链金融（付款办理51）回显：按凭证ID集合加载应付债权凭证明细
     *
     * @param ids 凭证ID集合，逗号拼接，如 111,222,333
     * @return 应付债权凭证明细
     */
    @ApiOperation("供应链金融-付款办理按ID查应付债权凭证明细")
    @GetMapping("/listScfPayVoucherByIds")
    @ResponseBody
    public AjaxJson<List<ScfPayableVoucherSelDTO>> listScfPayVoucherByIds(@RequestParam("ids") String ids) {
        return AjaxJson.success(scfVoucherPayService.getPayableVoucherByIds(StringUtils.splitToList(",", ids)));
    }

}
