package com.bytter.supply.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.poi.service.ExcelImportService;
import com.bytter.supply.domain.receivable.dto.ScfReceivableVoucherBatchSignDTO;
import com.bytter.supply.domain.receivable.dto.ScfReceivableVoucherInfoDTO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherImportVO;
import com.bytter.supply.domain.receivable.service.ScfReceivableVoucherExcelService;
import com.bytter.supply.domain.receivable.service.ScfReceivableVoucherInfoService;
import com.bytter.supply.domain.receivable.vo.ScfReceivableVoucherInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableVoucherInfoController
 * @Description: 应收债权凭证信息控制器
 * @since 2026/6/30
 */
@Api(value = "/v1-supply/receivable/voucherInfo", tags = "供应链金融应收债权凭证")
@RestController
@RequestMapping("/v1-supply/receivable/voucherInfo")
public class ScfReceivableVoucherInfoController extends BaseController {

    @Autowired
    private ScfReceivableVoucherInfoService scfReceivableVoucherInfoService;

    @Autowired
    private ScfReceivableVoucherExcelService scfReceivableVoucherExcelService;

    @Autowired
    private ExcelImportService excelImportService;

    /**
     * 查询应收债权凭证信息列表
     */
    @ApiOperation("查询应收债权凭证信息列表")
    @PostMapping("/list")
    public AjaxJson<DataTablesOutput<ScfReceivableVoucherInfoVO>> list(@RequestBody V1DataTablesInput dataTablesInput) {
        return AjaxJson.success(scfReceivableVoucherInfoService.dataTable(dataTablesInput.convertAndInitColumns()));
    }

    /**
     * 新增应收债权凭证信息
     */
    @ApiOperation("新增应收债权凭证信息")
    @LogMessage(content = "新增应收债权凭证信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody ScfReceivableVoucherInfoDTO voucherInfoDTO) {
        scfReceivableVoucherInfoService.saveVoucherInfo(voucherInfoDTO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改应收债权凭证信息
     */
    @ApiOperation("修改应收债权凭证信息")
    @LogMessage(content = "修改应收债权凭证信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody ScfReceivableVoucherInfoDTO voucherInfoDTO) {
        if (StringUtils.isEmpty(voucherInfoDTO.getId())) {
            return AjaxJson.failed("主键ID不能为空");
        }
        scfReceivableVoucherInfoService.updateVoucherInfo(voucherInfoDTO);
        return AjaxJson.success();
    }

    /**
     * 删除应收债权凭证暂存数据
     * <p>
     * type: 暂存业务类型，取值与暂存单据状态一致(open-draft:凭证开立暂存;loan-draft:融资暂存;transfer-draft:转让暂存;payment-draft:提示付款暂存)。
     * 仅允许删除暂存数据，非暂存数据不允许删除。
     */
    @ApiOperation("删除应收债权凭证暂存数据")
    @LogMessage(content = "删除应收债权凭证信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id, @RequestParam("approvalStatus") String approvalStatus) {
        scfReceivableVoucherInfoService.deleteVoucherInfo(id, approvalStatus);
        return AjaxJson.success();
    }

    /**
     * 根据主键查询详情
     */
    @ApiOperation("根据主键查询应收债权凭证详情")
    @GetMapping("/view/{id}")
    public AjaxJson<ScfReceivableVoucherInfoVO> view(@PathVariable("id") String id) {
        ScfReceivableVoucherInfoVO voucherInfo = scfReceivableVoucherInfoService.getVoucherInfo(id);
        return AjaxJson.success(voucherInfo);
    }

    /**
     * 根据主键查询详情
     */
    @ApiOperation("批量查询收债权凭证")
    @GetMapping("/viewList")
    public AjaxJson<List<ScfReceivableVoucherInfoVO>> viewList(@RequestParam("id") String id) {
        List<ScfReceivableVoucherInfoVO> voucherInfo = scfReceivableVoucherInfoService.getVoucherInfoList(id);
        return AjaxJson.success(voucherInfo);
    }

    /**
     * 付款办理（付款方式52）转让选择：datatable 分页查询可转让的应收债权凭证（开立审批通过 且 已签收）。
     * <p>编辑回显：前端回传已选择的凭证id（selectedIds）一并返回（or id in selectedIds），用于展示已选择数据（即使已进入转让暂存）。
     * 入参走 datatable 风格：分页/排序/检索条件（凭证编号、开立单位、平台、产品、线上线下等）+ selectedIds 均写在 formdata 的 params 中。
     */
    @ApiOperation("付款办理-查询可转让应收债权凭证")
    @PostMapping("/listForPayTransfer")
    public AjaxJson<DataTablesOutput<ScfReceivableVoucherInfoVO>> listForPayTransfer(@RequestBody V1DataTablesInput dataTablesInput) {
        Map<String, Object> params = dataTablesInput.getParams();
        Object selObj = params.get("selectedIds");
        String selectedIds = null;
        if (selObj instanceof Collection) {
            StringBuilder sb = new StringBuilder();
            for (Object item : (Collection<?>) selObj) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                sb.append(String.valueOf(item));
            }
            selectedIds = sb.toString();
        } else if (selObj != null) {
            selectedIds = String.valueOf(selObj);
        }
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(scfReceivableVoucherInfoService.listForPayTransfer(input, selectedIds));
    }

    /**
     * 签收管理接口
     */
    @ApiOperation("应收债权凭证签收管理")
    @LogMessage(content = "签收管理接口", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSign")
    public AjaxJson batchSign(@RequestBody ScfReceivableVoucherBatchSignDTO batchSignDTO) {
        if (batchSignDTO.getVoucherIds() == null || batchSignDTO.getVoucherIds().isEmpty()) {
            return AjaxJson.failed("待签收凭证ID列表不能为空");
        }
        if (ObjectUtil.isEmpty(batchSignDTO.getSignStatus())) {
            return AjaxJson.failed("签收状态不能为空");
        }
        scfReceivableVoucherInfoService.batchSign(batchSignDTO,getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 应收债权凭证补录接口
     * <p>
     * 补录时，仅可对"凭证编号"和"前置凭证编号"字段进行修改。
     * 补录完成后，业务状态改为"已签收"。
     */
    @ApiOperation("应收债权凭证补录")
    @LogMessage(content = "应收债权凭证补录", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/supplement")
    public AjaxJson supplement(@RequestBody ScfReceivableVoucherInfoDTO voucherInfo) {
        scfReceivableVoucherInfoService.supplement(voucherInfo);
        return AjaxJson.success();
    }

    /**
     * 应付债权凭证台账查询
     * <p>
     * 单据状态固定为"开立审批通过"、"融资审批通过"或"提示付款审批通过"，
     * 业务状态固定为"已签收"、"已融资"或"已收款"。
     */
    @ApiOperation("应收债权凭证台账查询")
    @PostMapping("/getVoucherLedger")
    public AjaxJson<DataTablesOutput<ScfReceivableVoucherInfoVO>> getVoucherLedger(@RequestBody V1DataTablesInput dataTablesInput) {
        return AjaxJson.success(scfReceivableVoucherInfoService.getVoucherLedger(dataTablesInput.convertAndInitColumns()));
    }

    /**
     * 查询所有审批中的应收债权凭证
     * <p>
     * 审批中包括：开立审批中、融资审批中、转让审批中、提示付款审批中
     */
    @ApiOperation("查询所有审批中的应收债权凭证")
    @PostMapping("/getPendingApprovalVoucher")
    public AjaxJson<DataTablesOutput<WorkFlowPersistence>> getPendingApprovalVoucher(@RequestBody V1DataTablesInput dataTablesInput) {
        return AjaxJson.success(scfReceivableVoucherInfoService.getPendingApprovalVoucher(dataTablesInput.convertAndInitColumns()));
    }

    /**
     * Excel批量导入应收债权凭证
     * <p>
     * 导入后凭证状态：单据状态为"开立暂存"、业务状态为"未签收"
     */
    @ApiOperation("Excel批量导入应收债权凭证")
    @LogMessage(content = "Excel批量导入应收债权凭证", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/import")
    public AjaxJson importVoucherInfo(@RequestBody List<ScfReceivableVoucherImportVO> list) {
        try {
            scfReceivableVoucherExcelService.saveImport(list, getCurrentUser());
            return AjaxJson.success("导入成功");
        } catch (Exception e) {
            return AjaxJson.failed("导入失败: " + e.getMessage());
        }
    }

    /**
     * 链路查看
     * <p>
     * 根据凭证ID查询当前凭证的转让、融资、收款信息，为已签收凭证补充签收展示节点，
     * 并根据前置凭证编号向前递归查询前手凭证，以树形结构返回完整链路。
     */
    @ApiOperation("链路查看")
    @GetMapping("/linkChain/{id}")
    public AjaxJson<ScfReceivableVoucherInfoVO> linkChain(@PathVariable("id") String id) {
        return AjaxJson.success(scfReceivableVoucherInfoService.getLinkChain(id));
    }

    /**
     * 发起融资结果查询指令，仅允许业务状态为融资中的线上凭证。
     */
    @ApiOperation("融资结果查询")
    @GetMapping("/queryFinanceResult")
    public AjaxJson<Void> queryFinanceResult(@RequestParam("voucherCode") String voucherCode) {
        scfReceivableVoucherInfoService.queryFinanceResult(voucherCode);
        return AjaxJson.success();
    }

    /**
     * 发起到期收款查询指令，仅允许业务状态为收款中的线上凭证。
     */
    @ApiOperation("到期收款查询")
    @GetMapping("/queryDueCollectResult")
    public AjaxJson<Void> queryDueCollectResult(@RequestParam("voucherCode") String voucherCode) {
        scfReceivableVoucherInfoService.queryDueCollectResult(voucherCode);
        return AjaxJson.success();
    }
}
