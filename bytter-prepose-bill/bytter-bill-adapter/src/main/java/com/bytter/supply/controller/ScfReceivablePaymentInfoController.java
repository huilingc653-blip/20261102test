package com.bytter.supply.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.receivable.dto.ScfReceivablePaymentInfoDTO;
import com.bytter.supply.domain.receivable.service.ScfReceivablePaymentInfoService;
import com.bytter.supply.domain.receivable.vo.ScfReceivablePaymentInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author ztd
 * @version 1.0
 * @Title: ScfReceivablePaymentInfoController
 * @Description: 债权凭证收款明细控制器
 * @since 2026/7/10
 */
@Api(value = "/v1-supply/receivable/paymentInfo", tags = "供应链金融债权凭证收款明细")
@RestController
@RequestMapping("/v1-supply/receivable/paymentInfo")
public class ScfReceivablePaymentInfoController extends BaseController {

    @Autowired
    private ScfReceivablePaymentInfoService scfReceivablePaymentInfoService;

    /**
     * 查询债权凭证收款明细列表
     */
    @ApiOperation("查询债权凭证收款明细列表")
    @PostMapping("/list")
    public AjaxJson list(@RequestBody V1DataTablesInput dataTablesInput) {
        return AjaxJson.success(scfReceivablePaymentInfoService.dataTable(dataTablesInput.convertAndInitColumns()));
    }

    /**
     * 修改债权凭证收款明细
     */
    @ApiOperation("修改债权凭证收款明细")
    @LogMessage(content = "修改债权凭证收款明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody ScfReceivablePaymentInfoDTO paymentInfoDTO) {
        if (paymentInfoDTO.getId() == null) {
            return AjaxJson.failed("主键ID不能为空");
        }
        scfReceivablePaymentInfoService.updatePaymentInfo(paymentInfoDTO);
        return AjaxJson.success();
    }

    /**
     * 删除债权凭证收款明细
     */
    @ApiOperation("删除债权凭证收款明细")
    @LogMessage(content = "删除债权凭证收款明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        scfReceivablePaymentInfoService.deletePaymentInfo(id);
        return AjaxJson.success();
    }

    /**
     * 根据凭证编号查询详情
     */
    @ApiOperation("根据凭证编号查询债权凭证收款明细详情")
    @GetMapping("/viewByVoucherCode/{voucherCode}")
    public AjaxJson view(@PathVariable("voucherCode") String voucherCode) {
        ScfReceivablePaymentInfoVO paymentInfo = scfReceivablePaymentInfoService.getPaymentInfo(voucherCode);
        return AjaxJson.success(paymentInfo);
    }

    /** 根据主键查询收款详情（含关联应收债权凭证信息） */
    @ApiOperation("根据主键查询债权凭证收款明细详情")
    @GetMapping("/viewById/{id}")
    public AjaxJson viewById(@PathVariable("id") String id) {
        return AjaxJson.success(scfReceivablePaymentInfoService.getPaymentInfoById(id));
    }

    /**
     * 债权凭证单条收款处理
     * <p>
     * 仅可对"单据状态"为"审批通过"、"业务状态"为"已签收"的电子债权凭证，进行"收款"处理。
     * 若为线下处理，则收款处理审批通过后，自动生成债权凭证到期收款记录。
     */
    @ApiOperation("债权凭证单条收款处理")
    @LogMessage(content = "债权凭证单条收款处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/sendPayment")
    public AjaxJson sendPayment(@RequestBody ScfReceivablePaymentInfoDTO paymentDTO) {
        if (StringUtils.isEmpty(paymentDTO.getSendStatus())) {
            return AjaxJson.failed("提交状态不能为空");
        }
        scfReceivablePaymentInfoService.sendPayment(paymentDTO, getCurrentUser());
        return AjaxJson.success();
    }

}
