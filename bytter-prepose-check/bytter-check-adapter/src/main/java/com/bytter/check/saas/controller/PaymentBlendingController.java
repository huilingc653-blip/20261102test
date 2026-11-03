package com.bytter.check.saas.controller;

import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.biz.payment.api.dto.VNisPayBillDTO;
import com.bytter.check.dto.FcBillBusTypeDTO;
import com.bytter.check.service.PaymentBlendingService;
import com.bytter.check.vo.PaymentBlendingConfigVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
//TODO 放到资金核对

/**
 * 付款勾兑
 * @author qijh
 */
@Api(value = "付款勾兑", tags = "付款勾兑")
@RestController("v1PaymentBlendingController")
@RequestMapping("/v1-check/bankacc/paymentblending")
public class PaymentBlendingController extends BaseController {
    @Autowired
    private PaymentBlendingService paymentBlendingService;
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 付款明细勾对查询 页面跳转
     *
     * @return
     */
    @ApiOperation("付款明细勾对查询 页面跳转 前配置获取")
    @LogMessage(content = "付款明细勾对查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listConfig")
    public AjaxJson<PaymentBlendingConfigVO> listConfig(Model model) {
        PaymentBlendingConfigVO configVO = new PaymentBlendingConfigVO();
        configVO.setBudgetSel(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue());
        configVO.setBusinessName(BudgetConstant.BUSINESS_NAME_BISACCDTL);
        configVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        return success(configVO);
    }
    /**
     * 银行账号明细表 table列表
     *
     * @return
     */
    @ApiOperation("付款明细勾对Table")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccDtlVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(paymentBlendingService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 付款单列表 table列表
     * @return
     */
    @ApiOperation("勾兑列表选中后 显示的付款单列表")
    @PostMapping("/billdatatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<VNisPayBillDTO>> billdatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(paymentBlendingService.getBillDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 保存付款单业务类型
     * @return
     */
    @ApiOperation("保存付款单业务类型")
    @PostMapping(path = "/toSaveBillBusType")
    @ResponseBody
    public AjaxJson toSaveBillBusType(@RequestBody FcBillBusTypeDTO dto){
        int count = paymentBlendingService.toSaveBillBusType(dto.getId(),dto.getBusType(),dto.getIsFrom());
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
    @ApiOperation("自动勾兑")
    @RequestMapping(path = "/auto")
    @ResponseBody
    public AjaxJson auto(String ids, String bankAccTypeStr){
        String msg = "";
        msg = paymentBlendingService.autoPaymentBlending(ids,bankAccTypeStr, getCurrentUser().getUsername());
        if(StringUtils.isEmpty(msg)){
            msg = "";
        }
        AjaxJson aj= new AjaxJson();
        if(StringUtils.isNotEmpty(msg)){
            aj.setSuccess(false);
        }else{
            aj.setSuccess(true);
        }
        aj.setMsg(msg);
        return  aj;
    }
    /**
     * 手工勾兑
     * @param dtlId
     * @param billId
     * @param isFrom
     * @return
     */
    @ApiOperation("手工勾兑")
    @RequestMapping(path = "/manual")
    @ResponseBody
    public AjaxJson manual(String dtlId, String billId, String isFrom){
        paymentBlendingService.manualPaymentBlending(dtlId,billId,isFrom, getCurrentUser().getUsername());
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.paymentblending.success");
        return  aj;
    }
    /**
     * 取消勾兑
     * @param dtlId
     * @return
     */
    @ApiOperation("取消勾兑")
    @RequestMapping(path = "/cancel")
    @ResponseBody
    public AjaxJson cancel(String dtlId){
        paymentBlendingService.cancelPaymentBlending(dtlId, getCurrentUser().getUsername());
        AjaxJson aj= new AjaxJson();
        aj.setSuccess(true);
        aj.setMsg("bankacc.message.cancel.paymentblending.success");
        return  aj;
    }
    /**
     * 作废
     * @param ids
     * @return
     */
    @ApiOperation("作废")
    @RequestMapping(path = "/scrap")
    @ResponseBody
    public AjaxJson scrap(String ids){
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            paymentBlendingService.scrap(id, getCurrentUser().getUsername());
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
    @ApiOperation("取消作废")
    @RequestMapping(path = "/cancelScrap")
    @ResponseBody
    public AjaxJson cancelScrap(String ids){
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            paymentBlendingService.cancelScrap(id, getCurrentUser().getUsername());
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
        paymentBlendingService.updateDtlBankSerialNo(id);
        return success();
    }
    /**
     * 付款数据明细表 table列表
     *
     * @return
     */
    @ApiOperation("付款数据明细 table列表")
    @PostMapping("/paymentDtlDataTable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccDtlVO>> paymentDtlDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(paymentBlendingService.getPaymentDtlDataTable(input));
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
                DataTablesOutput dataTablesOutput = paymentBlendingService.exportAll(dataTablesInput, user);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "付款明细勾对", 10000, BisAccDtlVO.class);
    }
}
