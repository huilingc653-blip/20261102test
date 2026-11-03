package com.bytter.supply.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.bill.api.scf.dto.ScfPayTransferDTO;
import com.bytter.bill.api.scf.service.IScfVoucherPayService;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.constant.ScfBusinessConstant;
import com.bytter.supply.domain.receivable.dto.ScfReceivableTransferInfoDTO;
import com.bytter.supply.domain.receivable.service.ScfReceivableTransferInfoService;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoGroupVO;
import com.bytter.supply.domain.receivable.vo.ScfReceivableTransferInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivableTransferInfoController
 * @Description: 债权凭证转让信息控制器
 * @since 2026/7/8
 */
@Api(value = "/v1-supply/receivable/transferInfo", tags = "债权凭证转让信息控制器")
@RestController
@RequestMapping("/v1-supply/receivable/transferInfo")
public class ScfReceivableTransferInfoController extends BaseController {

    @Autowired
    private ScfReceivableTransferInfoService scfReceivableTransferInfoService;

    @Autowired
    private IScfVoucherPayService scfVoucherPayService;

    /**
     * 查询债权凭证转让信息列表
     */
    @ApiOperation("查询债权凭证转让信息列表")
    @PostMapping("/list")
    public AjaxJson<DataTablesOutput<ScfReceivableTransferInfoVO>> list(@RequestBody V1DataTablesInput dataTablesInput) {
        return AjaxJson.success(scfReceivableTransferInfoService.dataTable(dataTablesInput.convertAndInitColumns()));
    }

    /**
     * 查询债权凭证转让信息（可按转让状态、转让标识分别查询一对多/多对一，可按凭证单据编号过滤，
     * 或传凭证ID集合voucherIds联动查询——付款方式52回显场景前端传付款单回显的应收凭证ID集合）
     * 一对多：voucherInfoList 1 条凭证信息 + transferInfoList 多条接收方转让信息；
     * 多对一：voucherInfoList 多条凭证信息 + transferInfoList 1 条接收方转让信息
     */
    @ApiOperation("查询债权凭证转让信息")
    @GetMapping("/listTemp")
    public AjaxJson<ScfReceivableTransferInfoGroupVO> listTemp(
            @RequestParam(value = "transferFlag", required = false) String transferFlag,
            @RequestParam(value = "voucherCode", required = false) String voucherCode,
            @RequestParam(value = "voucherIds", required = false) String voucherIds,
            @RequestParam(value = "transferStatus", required = false) String transferStatus) {
        return AjaxJson.success(scfReceivableTransferInfoService.listTemp(transferFlag, voucherCode, voucherIds, transferStatus));
    }

    /**
     * 一对多暂存前校验：凭证已存在多对一暂存数据时，不允许以一对多方式暂存
     */
    @ApiOperation("校验凭证是否已存在多对一暂存（一对多暂存前调用）")
    @GetMapping("/checkOneTemporary")
    public AjaxJson checkOneTemporary(@RequestParam("voucherCode") String voucherCode) {
        scfReceivableTransferInfoService.checkOneTemporary(voucherCode);
        return AjaxJson.success();
    }

    /**
     * 多对一暂存前校验：所选凭证已存在一对多暂存数据时，不允许以多对一方式暂存
     */
    @ApiOperation("校验所选凭证是否已存在一对多暂存（多对一暂存前调用）")
    @GetMapping("/checkManyTemporary")
    public AjaxJson checkManyTemporary(@RequestParam("voucherCodes") String voucherCodes) {
        scfReceivableTransferInfoService.checkManyTemporary(voucherCodes);
        return AjaxJson.success();
    }

    /**
     * 付款办理：提交凭证时根据 transferFlag 执行一对多或多对一校验
     */
    @ApiOperation("付款办理转让前置校验")
    @PostMapping("/paymentValidateTransfer")
    public AjaxJson<Void> paymentValidateTransfer(@RequestBody ScfPayTransferDTO dto) {
        scfReceivableTransferInfoService.paymentValidateTransfer(dto);
        return AjaxJson.success();
    }

    /**
     * 新增债权凭证转让信息（提交/暂存）
     */
    @ApiOperation("新增债权凭证转让信息（提交/暂存）")
    @LogMessage(content = "新增债权凭证转让信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/send")
    public AjaxJson send(@RequestBody ScfReceivableTransferInfoDTO transferInfoDTO) {
        // 提交时才要求转让标识；暂存不发起审批流，可能为单条暂存，无拆分标识
        if (!StringUtils.equals(ScfBusinessConstant.SEND_STATUS_SAVE, transferInfoDTO.getSendStatus())
                && StringUtils.isBlank(transferInfoDTO.getTransferFlag())) {
            throw new BusinessException("新增债权凭证时转让标识不能为空!");
        }
        scfReceivableTransferInfoService.sendTransferInfo(transferInfoDTO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改债权凭证转让信息
     */
    @ApiOperation("修改债权凭证转让信息")
    @LogMessage(content = "修改债权凭证转让信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody ScfReceivableTransferInfoDTO transferInfoDTO) {
        if (StringUtils.isEmpty(transferInfoDTO.getId())) {
            return AjaxJson.failed("主键ID不能为空");
        }
        scfReceivableTransferInfoService.updateTransferInfo(transferInfoDTO);
        return AjaxJson.success();
    }

    /**
     * 删除债权凭证转让信息
     */
    @ApiOperation("删除债权凭证转让信息")
    @LogMessage(content = "删除债权凭证转让信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        scfReceivableTransferInfoService.deleteTransferInfo(id);
        return AjaxJson.success();
    }

    /**
     * 根据主键查询详情
     */
    @ApiOperation("根据主键查询债权凭证转让详情")
    @GetMapping("/view/{id}")
    public AjaxJson view(@PathVariable("id") String id) {
        ScfReceivableTransferInfoVO transferInfo = scfReceivableTransferInfoService.getTransferInfo(id);
        return AjaxJson.success(transferInfo);
    }

    /**
     * 保存应收债权凭证转让草稿（付款方式52）：前端提交付款单前调用，返回草稿ID集合（逗号拼接）。
     * <p>保存即把对应凭证置为"转让暂存"；再次进入变更时前端回传原草稿id（transferIds）释放原转让暂存后再保存当前数据。
     * 付款单保存/提交时携带返回的草稿ID写入中间表；审批通过后的转让执行另期实现。
     */
    @ApiOperation("保存应收债权凭证转让草稿（付款方式52）")
    @PostMapping("/saveTransfer")
    public AjaxJson<String> saveTransfer(@RequestBody ScfPayTransferDTO dto) {
        return AjaxJson.success(scfVoucherPayService.saveVoucherTransfer(dto));
    }
}
