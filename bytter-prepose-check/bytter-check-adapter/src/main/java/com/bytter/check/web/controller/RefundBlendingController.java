package com.bytter.check.web.controller;
import com.bytter.biz.bdlink.vo.BisAccDtlVO;
import com.bytter.check.service.RefundBlendingService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
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
@Controller
@RequestMapping("/bankacc/refundblending")
public class RefundBlendingController extends BaseController {
    @Autowired
    private RefundBlendingService refundBlendingService;
    @DubboReference(check = false)
    private IConfigService configService;
    /**
     * 银行账号明细表列表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_QUERY)
    @LogMessage(content = "退款明细勾兑查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        Date startDate = DateUtils.getDate();
        startDate = DateUtils.addDay(startDate,-1);
        model.addAttribute("startDate", startDate);
        return "bankacc/listRefundBlending";
    }
    /**
     * 银行账号明细表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return refundBlendingService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 付款单列表 table列表
     * @return
     */
    @SuppressWarnings("rawtypes")
    //@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_REFUNDBLENDING_QUERY)
    @PostMapping("/billdatatable")
    @ResponseBody
    public DataTablesOutput billdatatable(@RequestBody DataTablesInput dataTablesInput) {
        return refundBlendingService.getBillDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 保存付款单业务类型
     * @param id
     * @param busType
     * @return
     */
    @RequestMapping(params = "toSaveBillBusType")
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
    @RequestMapping(params = "auto")
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
        }else{
            aj.setSuccess(true);
        }
        Map<String,Object> map = new HashMap<>();
        map.put("msg",msg);
        aj.setAttributes(map);
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
    @RequestMapping(params = "autoTrue")
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
    @RequestMapping(params = "backNisBillhead")
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
    @RequestMapping(params = "manual")
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
    @RequestMapping(params = "cancel")
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
    @RequestMapping(params = "scrap")
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
    @RequestMapping(params = "cancelScrap")
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
     * 作废跳转
     *
     * @return
     */
    @GetMapping("/scrap/{blendSign}")
    public String scrap(@PathVariable("blendSign") String blendSign, Model model) {
        model.addAttribute("blendSign",blendSign);
        return "bankacc/listRefundScrap";
    }
    /**
     * 更新流水银行流水号
     * @param id
     * @return
     */
    @RequestMapping(params = "updateDtlBankSerialNo")
    @ResponseBody
    public AjaxJson updateDtlBankSerialNo(String id){
        refundBlendingService.updateDtlBankSerialNo(id);
        return success();
    }
    /**
     * 导出全部
     * @return
     */
    @RequestMapping("/exportAll")
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
