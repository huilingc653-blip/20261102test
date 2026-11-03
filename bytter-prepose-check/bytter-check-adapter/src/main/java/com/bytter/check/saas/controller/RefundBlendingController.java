package com.bytter.check.saas.controller;

import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.check.service.RefundBlendingService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//TODO 资金核对

/**
 * 退款勾兑
 * @author qijh
 */
@ApiOperation(value = "退款勾兑",tags = "退款勾兑")
@RestController("v1RefundBlendingController")
@RequestMapping("/v1-check/bankacc/refundblending")
public class RefundBlendingController extends BaseController {
    @Autowired
    private RefundBlendingService refundBlendingService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 银行账号明细表 table列表
     *
     * @return
     */
    @ApiOperation(" 退款勾兑 table列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccDtlVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(refundBlendingService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 付款单列表 table列表
     * @return
     */
    @ApiOperation("退款勾兑付款单table列表")
    @PostMapping("/billdatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<NisBillheadDTO>> billdatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(refundBlendingService.getBillDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 保存付款单业务类型
     * @param id
     * @param busType
     * @return
     */
    @ApiOperation("保存付款单业务类型")
    @PostMapping(path = "/toSaveBillBusType")
    @ResponseBody
    public AjaxJson toSaveBillBusType(String id,String busType){
        int count = refundBlendingService.toSaveBillBusType(id,busType);
        AjaxJson aj= new AjaxJson();
        if(count>0) {
           aj.setSuccess(true);
        }else {
            aj.setSuccess(false);
        }
        return  aj;
    }
    /**
     * 自动勾兑
     * @param ids
     * @param bankAccTypeStr
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_APPLY)
    @ApiOperation("自动勾兑")
    @PostMapping(path = "/auto")
    @ResponseBody
    public AjaxJson auto(String ids,String bankAccTypeStr,String handle){
        String msg = "";
        msg = refundBlendingService.aoto(ids,bankAccTypeStr,handle, this.getCurrentUser());
        if(StringUtils.isEmpty(msg)){
            msg = "";
        }
        AjaxJson aj= new AjaxJson();
        if(StringUtils.isNotEmpty(msg)){
            aj.setSuccess(false);
            aj.setMsg("bankacc.message.notDate");
        }else{
            aj.setSuccess(true);
        }
        return  aj;
    }
    /**
     * 自动勾兑确认
     * @param dtlId
     * @param billId
     * @param handle
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_APPLY)
    @ApiOperation("自动勾兑确认")
    @PostMapping(path = "/autoTrue")
    @ResponseBody
    public AjaxJson autoTrue(String dtlId, String billId,String handle){
        String msg = refundBlendingService.aotoTrue(dtlId,billId,handle,this.getCurrentUser());
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        Map<String,Object> map = new HashMap<>();
        map.put("msg",msg);
        aj.setAttributes(map);
        return  aj;
    }
    /**
     * 打回付款单
     * @param billId
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_APPLY)
    @ApiOperation("打回付款单")
    @PostMapping(path = "/backNisBillhead")
    @ResponseBody
    public AjaxJson backNisBillhead(String billId){
        refundBlendingService.backNisBillhead(billId,this.getCurrentUser());
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.paymentblending.success");
        return  aj;
    }
    /**
     * 手工勾兑
     * @param dtlId
     * @param billId
     * @param bankSerialNo
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_APPLY)
    @ApiOperation("手动勾兑")
    @PostMapping(path = "/manual")
    @ResponseBody
    public AjaxJson manual(String dtlId, String billId,String bankSerialNo,String handle){
        String msg = refundBlendingService.manualPaymentBlending(dtlId,billId,bankSerialNo,handle,this.getCurrentUser());
        AjaxJson aj= new AjaxJson();
        Map<String,Object> map = new HashMap<>();
        map.put("msg",msg);
        aj.setAttributes(map);
        aj.setSuccess(true);
        return  aj;
    }
    /**
     * 取消勾兑
     * @param dtlId
     * @param billId
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_APPLY)
    @ApiOperation("取消勾兑")
    @PostMapping(path = "/cancel")
    @ResponseBody
    public AjaxJson cancel(String dtlId, String billId,String handle){
        String msg = refundBlendingService.cancelPaymentBlending(dtlId,billId,handle,this.getCurrentUser());
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        Map<String,Object> map = new HashMap<>();
        map.put("msg",msg);
        aj.setAttributes(map);
        return  aj;
    }
    /**
     * 作废
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_APPLY)
    @ApiOperation("作废")
    @PostMapping(path = "/scrap")
    @ResponseBody
    public AjaxJson scrap(String ids){
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            refundBlendingService.scrap(id);
        }
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.scrap.success");
        return  aj;
    }
    /**
     * 取消作废
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_APPLY)
    @ApiOperation("取消作废")
    @PostMapping(path = "/cancelScrap")
    @ResponseBody
    public AjaxJson cancelScrap(String ids){
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            refundBlendingService.cancelScrap(id);
        }
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.cancel.scrap.success");
        return  aj;
    }
    /**
     * 更新流水银行流水号
     * @param id
     * @return
     */
    @ApiOperation("更新流水银行流水号")
    @PostMapping(path = "/updateDtlBankSerialNo")
    @ResponseBody
    public AjaxJson updateDtlBankSerialNo(String id){
        refundBlendingService.updateDtlBankSerialNo(id);
        return success();
    }
    /**
     * 导出全部
     * @return
     */
    @ApiOperation("导出全部")
    @PostMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput dataTablesOutput = refundBlendingService.getDataTablesUnion(dataTablesInput, user);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "退款勾对", 5000, BisAccDtlVO.class);
    }
}
