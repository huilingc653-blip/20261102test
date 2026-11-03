package com.bytter.biz.saas.controller.receipt;

import com.bytter.biz.bdlink.vo.BisBankReceiptDtlVO;
import com.bytter.biz.payment.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.dto.BisAccDtlDTO;
import com.bytter.biz.receipt.service.BisBankReceiptBlendingServer;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 银行电子回单勾兑
 */
@Api(value = "/v1-payment/receipt/bisbankreceiptblending", tags = "银行电子回单勾兑")
@RestController("v1BisBankReceiptBlendingController")
@RequestMapping("/v1-payment/receipt/bisbankreceiptblending")
public class BisBankReceiptBlendingController extends BaseController {
    @Autowired
    private BisBankReceiptBlendingServer bisBankReceiptBlendingServer;


    /**
     * 银行电子回单列表
     *
     * @return
     */
    @ApiOperation("银行电子回单列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<BisBankReceiptDtlVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisBankReceiptBlendingServer.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 银行流水列表
     *
     * @return
     */
    @ApiOperation("获取需求账号")
    @PostMapping("/dtlDatatable")
    public AjaxJson<DataTablesOutput<BisAccDtlDTO>> dtlDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(bisBankReceiptBlendingServer.getDtlDataTablesUnion(input));
    }

    /**
     * 自动勾兑
     *
     * @param ids
     * @param bankAccTypeStr
     * @return
     */
    @ApiOperation("自动勾兑")
    @GetMapping("auto")
    public AjaxJson auto(String ids, String bankAccTypeStr) {
        String msg = "";
        msg = bisBankReceiptBlendingServer.aotoBlending(ids, bankAccTypeStr);
        if (StringUtils.isEmpty(msg)) {
            msg = "";
        }
        AjaxJson aj = new AjaxJson();
        if (StringUtils.isNotEmpty(msg)) {
            aj.setSuccess(false);
        } else {
            aj.setSuccess(true);
        }
        aj.setMsg(msg);
        return aj;
    }

    /**
     * 手工勾兑
     *
     * @param dtlId     流水id
     * @param receiptId 电子回单id
     * @return
     */
    @ApiOperation("手工勾兑")
    @GetMapping("manual")
    public AjaxJson manual(String dtlId, String receiptId) {
        bisBankReceiptBlendingServer.manualBlending(dtlId, receiptId);
        AjaxJson aj = new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.paymentblending.success");
        return aj;
    }

    /**
     * 取消勾兑
     *
     * @param receiptId 银行电子回单id
     * @return
     */
    @ApiOperation("取消勾兑")
    @RequestMapping("cancel")
    public AjaxJson cancel(String receiptId) {
        bisBankReceiptBlendingServer.cancelBlending(receiptId);
        AjaxJson aj = new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.cancel.paymentblending.success");
        return aj;
    }

    /**
     * 无需勾对
     *
     * @param receiptId 银行电子回单id
     * @return
     */
    @ApiOperation("无需勾对")
    @RequestMapping("needNot")
    public AjaxJson needNot(String receiptId) {
        if (EmptyUtils.isNotEmpty(receiptId)) {
            String[] receiptIdStr = receiptId.split(",");
            if (!EmptyUtils.isEmpty(receiptIdStr) && receiptIdStr.length > 0) {
                for (int i = 0; i < receiptIdStr.length; i++) {
                    bisBankReceiptBlendingServer.toUpdateBlendSign(receiptIdStr[i], BtBankAccConstant.BLENDSIGN_4);
                }
            }
        }
        return AjaxJson.success();
    }
}
