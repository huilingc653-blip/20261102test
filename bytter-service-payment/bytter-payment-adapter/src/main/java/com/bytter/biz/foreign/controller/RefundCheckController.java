package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.RefundCheckService;
import com.bytter.biz.foreign.vo.RefundCheckVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.poi.util.ExportUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 外汇退款勾对
 * @author huangx
 * @date 2024-03-8 09:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/refundCheck")
public class RefundCheckController extends BaseController {

    @Autowired
    private RefundCheckService refundCheckService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    /**
     * 进入退款勾对列表界面
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_QUERY)
    @LogMessage(content = "退款明细勾对列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        Date startDate = DateUtils.getDate();
        startDate = DateUtils.addDay(startDate,-1);
        model.addAttribute("startDate", startDate);
        return "foreign/listFexRefundCheck";
    }

    /**
     * 查询银行账户流水
     * @param dataTablesInput
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return refundCheckService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 自动勾对
     * @param ids
     * @param refundCheckRule
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @RequestMapping("/autoCheck")
    @ResponseBody
    public AjaxJson autoCheck(String ids, String refundCheckRule, String refundCheckHandle){
        String msg = "";
        msg = refundCheckService.autoCheck(ids, refundCheckRule, refundCheckHandle, this.getCurrentUser());
        if(StringUtils.isEmpty(msg)){
            msg = "";
        }
        AjaxJson aj= new AjaxJson();
        if(StringUtils.isNotEmpty(msg) && "bankacc.message.notDate".equals(msg)){
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
     * 自动确认
     * @param ids
     * @param refundCheckHandle
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @RequestMapping("/autoConfirm")
    @ResponseBody
    public AjaxJson autoConfirm(String ids, String refundCheckHandle){
        refundCheckService.autoConfirm(ids, refundCheckHandle, this.getCurrentUser());
        return success();
    }

    /**
     * 取消勾对
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @PostMapping("/cancel/{id}")
    @ResponseBody
    public AjaxJson cancel(@PathVariable("id") String id){
        RefundCheckVO refundCheck = refundCheckService.getDtlById(id);
        refundCheckService.cancel(refundCheck, this.getCurrentUser());
        return success();
    }

    /**
     * 取消勾对
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @RequestMapping("/batchCancel")
    @ResponseBody
    public AjaxJson batchCancel(String ids){
        refundCheckService.batchCancel(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 作废
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @PostMapping("/defeat/{id}")
    @ResponseBody
    public AjaxJson defeat(@PathVariable("id") String id) {
        refundCheckService.defeat(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量作废
     *
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @PostMapping("/batchDefeat")
    @ResponseBody
    public AjaxJson batchDefeat(@RequestParam("ids") String ids) {
        refundCheckService.batchDefeat(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 取消作废
     *
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @PostMapping("/cancelDefeat/{id}")
    @ResponseBody
    public AjaxJson cancelDefeat(@PathVariable("id") String id) {
        refundCheckService.cancelDefeat(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量取消作废
     *
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @PostMapping("/batchCancelDefeat")
    @ResponseBody
    public AjaxJson batchCancelDefeat(@RequestParam("ids") String ids) {
        refundCheckService.batchCancelDefeat(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 手工勾对
     * @param refundCheckVO
     * @param model
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @GetMapping("/manualCheck")
    public String manualCheck(RefundCheckVO refundCheckVO, Model model) {
        // 退款勾对-勾对规则
        model.addAttribute("refundCheckRule", refundCheckVO.getRefundCheckRule());
        model.addAttribute("opType", refundCheckVO.getOpType());
        RefundCheckVO bisAccDtlDTO = new RefundCheckVO();
        if (StringUtils.isNotEmpty(refundCheckVO.getId())) {
            bisAccDtlDTO = refundCheckService.getDtlById(refundCheckVO.getId());
        }
        String billId = "";
        if (CommonConstant.YES.equals(refundCheckVO.getOpType()) || "2".equals(refundCheckVO.getOpType())) {
            if (StringUtils.isNotBlank(bisAccDtlDTO.getBankSerialNo())) {
                // 勾对确认
                FexCashFlow fexCashFlow = fexCashFlowService.getByBankSerialNo(bisAccDtlDTO.getBankSerialNo());
                billId = fexCashFlow == null ? billId : fexCashFlow.getId();
            }
        }
        model.addAttribute("billId", billId);
        model.addAttribute("settleDate", bisAccDtlDTO.getTransTime());
        model.addAttribute("bisAccDtlPage", bisAccDtlDTO);
        return "foreign/manualFexRefundCheck";
    }

    /**
     * 自动确认
     * @param dtlId
     * @param billId
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_REFUNDCHECK_APPLY)
    @RequestMapping("/manualSave")
    @ResponseBody
    public AjaxJson manualSave(String dtlId, String billId, String refundCheckHandle, String opFlag){
        refundCheckService.manualSave(dtlId, billId, refundCheckHandle, opFlag, this.getCurrentUser());
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
                DataTablesOutput dataTablesOutput = refundCheckService.getDataTablesUnion(dataTablesInput, user);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "外汇退款勾对", 5000, RefundCheckVO.class);
    }

}
