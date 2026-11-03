package com.bytter.bill.web.saas.controller.bill;

import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbEmitBillPayVo;
import com.bytter.bill.domain.bill.vo.AbEmitBillVo;
import com.bytter.bill.domain.bill.vo.AbVoucherinfoViceVO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 兑付确认
 * @date 2017-06-29 16:53:43
 */
@Api(value = "/v1-preposebill/bill/accebill/abemitbillpay", tags = "兑付确认")
@RestController("v1AbEmitBillPayController")
@RequestMapping("/v1-preposebill/bill/accebill/abemitbillpay")
public class AbEmitBillPayController extends BaseController {
    @Autowired
    private AbEmitBillPayService abEmitBillPayService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbLogService abLogService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbRatUrationService abRatUrationService;
    @Autowired
    private AbFcBondHookService abFcBondHookService;
    @Autowired
    private AbGuaranteeService abGuaranteeService;
    @Autowired
    private AbCorpService abCorpService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;

    /**
     * 线上兑付新增编辑页面初始化
     */
    @GetMapping("/payOnline/{aid}")
    @ApiOperation("线上兑付新增编辑页面初始化")
    public AjaxJson<Map<String, Object>> payOnline(@PathVariable("aid") String aid) {
        Map<String, Object> dataMap = new HashMap<>(10);
        AbEmitBillVo vo = abEmitBillPayService.getAbEmitBillVoByAidAndIsCashingOnlinePayee(aid);
        dataMap.put("abEmitBillVo", vo);
        dataMap.put("isApprove", true);
        dataMap.put("iframeId", "applyiframe");
        //判断是否有已挂保证金数据
        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
        String bailmodel = FcBondConstant.IS_HOOK_N;
        if (fcBondApplyDTO != null) {
            bailmodel = FcBondConstant.IS_HOOK_Y;
        }
        dataMap.put("bailmodel", bailmodel);
        dataMap.put("attachAid", aid);
        dataMap.put("forwardPath", "bill/accebill/listAbEmitBillPayOnlie");
        return AjaxJson.success(dataMap);
    }

    /**
     * 线上兑付撤回后编辑页面初始化
     */
    @GetMapping("/edit/{aid}")
    @ApiOperation("线上兑付撤回编辑页面初始化")
    public AjaxJson<Map<String, Object>> edit(@PathVariable("aid") String aid) {
        Map<String, Object> dataMap = new HashMap<>(10);
        AbEmitBillVo vo = abEmitBillPayService.getAbEmitBillVoByAid(aid);
        dataMap.put("abEmitBillVo", vo);
        dataMap.put("isApprove", true);
        dataMap.put("edit", true);
        dataMap.put("iframeId", "trackiframe");
        dataMap.put("attachAid", aid);
        dataMap.put("forwardPath", "bill/accebill/listAbEmitBillPayOnlie");
        return AjaxJson.success(dataMap);
    }

    /**
     * 查看线上兑付详情页面初始化
     */
    @LogMessage(content = "查询开票登记详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    @ApiOperation("线上兑付详情页面初始化")
    public AjaxJson<Map<String, Object>> view(String viceId, String accId) {
        Map<String, Object> dataMap = new HashMap<>(16);
        AbEmitBillVo vo = abEmitBillPayService.getAbEmitBillVoByAid(accId);
        List<AbLog> abLogList = abLogService.listAbLog(viceId, null);
        dataMap.put("abEmitBillVo", vo);
        dataMap.put("fcBondRate", vo.getBailrate());
        dataMap.put("abLogList", abLogList);
        // 挂预算系统参数
        if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            dataMap.put("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
            dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
            dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        }
        //挂授信 显示
        if (!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")) {
            dataMap.put("isEnabled", true);
        } else {
            dataMap.put("isEnabled", false);
        }
        //判断是否有已挂保证金数据
        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
        String bailmodel = FcBondConstant.IS_HOOK_N;
        if (fcBondApplyDTO != null) {
            bailmodel = FcBondConstant.IS_HOOK_Y;
        }
        dataMap.put("bailmodel", bailmodel);
        //是否启用担保参数
        dataMap.put("isGuarantee", abGuaranteeService.isHangGuarantee());
        dataMap.put("isHangGuarantee", vo.getIsHangGuarantee() == null ? FcBondConstant.IS_HOOK_N : vo.getIsHangGuarantee());
        dataMap.put("contractIds", vo.getGuaContract());
        GuaContractOccupDTO hangGurVo = abGuaranteeService.getGuaContractOccupDTOByBillId(vo.getVvid());
        dataMap.put("idList", hangGurVo == null ? null : hangGurVo.getContractIds());
        dataMap.put("attachAid", accId);
        dataMap.put("forwardPath", "bill/accebill/viewAbEmitBill");
        return AjaxJson.success(dataMap);
    }

    /**
     * 删除线上兑付申请信息
     */
    @LogMessage(content = "线上兑付申请删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{aid}")
    @ApiOperation("删除线上兑付申请信息")
    public AjaxJson delete(@PathVariable("aid") String aid) {
        abEmitBillPayService.deleteByAid(aid, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 兑付申请 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ApiOperation("查询兑付申请分页列表")
    @LogMessage(content = "查询兑付申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillPayService.listAbEmitBillPay(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 票据详细信息弹窗页面初始化
     */
    @GetMapping("/billPayInfoPage")
    @ApiOperation("票据详细信息弹窗页面初始化")
    public AjaxJson<Map<String, Object>> billPayInfoPage(AbVoucherinfoViceVO reVo, String row) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(reVo.getCorpId())) {
            reVo.setCorpName(abCorpService.get(reVo.getCorpId()).getName());
        }
        dataMap.put("billInfo", reVo);
        dataMap.put("row", row);
        BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(reVo.getId());
        if (bisAccDtlDTO != null) {
            dataMap.put("businessDate", DateUtils.date2Str(bisAccDtlDTO.getTransTime(), DateUtils.date_sdf()));
            dataMap.put("billMoney", bisAccDtlDTO.getAmt());
        }
        dataMap.put("forwardPath", "bill/accebill/billPayInfoPage");
        return AjaxJson.success(dataMap);
    }

    /**
     * 查询未到期的应付票据信息 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/billPayInfoDataTable")
    @ApiOperation("查询未到期应付票据分页列表")
    @LogMessage(content = "查询未到期的应付票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> billPayInfoDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillPayService.billPayInfoDataTable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 线上兑付跟踪 table列表
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/trackDatatable")
    @ApiOperation("查询兑付跟踪分页列表")
    @LogMessage(content = "查询兑付跟踪列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public AjaxJson<DataTablesOutput> trackDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillPayService.listAbEmitBillPayTrack(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 线下兑付确认
     */
    @LogMessage(content = "线下兑付确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/cashingConfirmation")
    @ApiOperation("线下兑付确认操作")
    public AjaxJson cashingConfirmation(@RequestBody AbEmitBillPayVo pay) {
        abEmitBillPayService.cashingConfirmation(pay, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量线上兑付确认
     */
    @LogMessage(content = "批量线上兑付确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchCashingConfirmation")
    @ApiOperation("批量线上兑付确认")
    public AjaxJson batchCashingConfirmation(@RequestBody AbEmitBillPayVo pay) {
        abEmitBillPayService.batchCashingConfirmation(pay, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量设置兑付确认备注
     */
    @LogMessage(content = "批量设置兑付确认备注", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchSetEmitPayRmk")
    @ApiOperation("批量设置兑付确认备注")
    public AjaxJson batchSetEmitPayRmk(@RequestBody AbEmitBillPayVo pay) {
        abEmitBillPayService.batchSetEmitPayRmk(pay, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 线上兑付付款单撤回
     */
    @LogMessage(content = "付款单撤回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/withdraw/{aid}")
    @ApiOperation("线上兑付付款单撤回")
    public AjaxJson withdraw(@PathVariable("aid") String aid) {
        abEmitBillPayService.withdraw(aid, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 线上兑付确认已付款
     */
    @LogMessage(content = "确认已付款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/confrimepay/{aid}")
    @ApiOperation("线上兑付确认已付款")
    public AjaxJson confrimepay(@PathVariable("aid") String aid) {
        abEmitBillPayService.confrimepay(aid, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量确认已付款
     */
    @LogMessage(content = "批量确认已付款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchConfrimepay/{aids}")
    @ApiOperation("批量确认已付款")
    public AjaxJson batchConfrimepay(@PathVariable("aids") String aids) {
        abEmitBillPayService.batchConfrimepay(aids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("兑付确认审核分页列表")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillPayService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("兑付确认审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(12);
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo info = abEmitBillPayService.getAbEmitBillVoByAid(businessId);
            dataMap.put("abEmitBillVo", info);
            dataMap.put("isApprove", true);
            //判断是否有已挂保证金数据
            FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(info.getVvid(), info.getAppNo());
            String bailmodel = FcBondConstant.IS_HOOK_N;
            if (fcBondApplyDTO != null) {
                bailmodel = FcBondConstant.IS_HOOK_Y;
            }
            dataMap.put("bailmodel", bailmodel);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("budgetSel", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).getValue());
        dataMap.put("payItem", abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EMITITEM).getValue());
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbEmitBillPay");
        return AjaxJson.success(dataMap);
    }

    /**
     * 手机审核详情界面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("兑付确认移动端审批详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(8);
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo info = abEmitBillPayService.getAbEmitBillVoByAid(businessId);
            dataMap.put("abEmitBillVo", info);
            dataMap.put("isApprove", true);
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBillEmit");
        return AjaxJson.success(dataMap);
    }

    /**
     * 调整票据解付金额
     */
    @LogMessage(content = "调整票据解付金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/setBillEndPayMoney")
    @ApiOperation("调整票据解付金额")
    public AjaxJson setBillEndPayMoney(@RequestBody AbEmitBillPayVo pay) {
        if (StrUtil.isEmpty(pay.getVvid())) {
            return new AjaxJson(false, "从表ID不允许为空！");
        }
        abEmitBillPayService.setBillEndPayMoney(pay, this.getCurrentUser());
        return AjaxJson.success();
    }
}