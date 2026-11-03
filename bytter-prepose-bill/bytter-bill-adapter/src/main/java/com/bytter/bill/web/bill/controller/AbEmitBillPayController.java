package com.bytter.bill.web.bill.controller;

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
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 兑付确认
 * @date 2017-06-29 16:53:43
 */
@Controller
@RequestMapping("/bill/accebill/abemitbillpay")
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
     * 兑付确认列表 页面跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "bill/accebill/listAbEmitBillPay";
    }
    /**
     * 兑付申请查询列表页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/adEmitBillPayApplyList")
    public String adEmitBillPayApplyList(Model model) {
        model.addAttribute("pageContent", "abEmitBillPayApply");
        return "bill/accebill/listAbEmitBillPayApply";
    }
    /**
     * 兑付跟踪查询列表页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/adEmitBillPayTrackList")
    public String adEmitBillPayTrackList(Model model) {
        model.addAttribute("pageContent", "abEmitBillPayTrack");
        return "bill/accebill/listAbEmitBillPayTrack";
    }
    /**
     * 线上兑付新增编辑页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/payOnline/{aid}")
    public String adEmitBillPayTrackList(@PathVariable("aid") String aid, Model model) {
        AbEmitBillVo vo = abEmitBillPayService.getAbEmitBillVoByAidAndIsCashingOnlinePayee(aid);
        model.addAttribute("abEmitBillVo", vo);
        model.addAttribute("isApprove", true);// 不显示日志信息
        model.addAttribute("iframeId", "applyiframe");
        abBillAttachmentService.viewAttachment(aid, model);// 附件信息
        //判断是否有已挂保证金数据
        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
        String bailmodel = FcBondConstant.IS_HOOK_N;
        if (fcBondApplyDTO != null) {
            bailmodel = FcBondConstant.IS_HOOK_Y;
        }
        model.addAttribute("bailmodel", bailmodel);
        return "bill/accebill/listAbEmitBillPayOnlie";
    }
    /**
     * 线上兑付撤回后编辑页面跳转
     *
     * @param model
     * @return
     */
    @GetMapping("/edit/{aid}")
    public String editBillPayTrackList(@PathVariable("aid") String aid, Model model) {
        AbEmitBillVo vo = abEmitBillPayService.getAbEmitBillVoByAid(aid);
        model.addAttribute("abEmitBillVo", vo);
        model.addAttribute("isApprove", true);// 不显示日志信息
        model.addAttribute("edit", true);// 编辑状态，需要回显线上兑付信息
        model.addAttribute("iframeId", "trackiframe");
        abBillAttachmentService.viewAttachment(aid, model);// 附件信息
        return "bill/accebill/listAbEmitBillPayOnlie";
    }
    /**
     * 查看线上兑付详情页面
     *
     * @return
     */
    @LogMessage(content = "查询开票登记详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String viceId, String accId, Model model) {
        AbEmitBillVo vo = abEmitBillPayService.getAbEmitBillVoByAid(accId);
        List<AbLog> abLogList = abLogService.listAbLog(viceId, null);
        model.addAttribute("abEmitBillVo", vo);
        model.addAttribute("fcBondRate",vo.getBailrate());
        model.addAttribute("abLogList", abLogList);
        // 挂预算系统参数
        if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            model.addAttribute("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        }
        // 挂授信
//		boolean isRelateRat = abRatUrationService.isRelateRat();
//		model.addAttribute("isRelateRat", isRelateRat);
//		if (isRelateRat) {
//			model.addAttribute("urationDTO", abRatUrationService.getRatUrationDTOByBillId(vo.getVvid()));
//		}
        //挂授信 显示
        if (!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")) {
            model.addAttribute("isEnabled", true);
        } else {
            model.addAttribute("isEnabled", false);
        }
        //判断是否有已挂保证金数据
        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
        String bailmodel = FcBondConstant.IS_HOOK_N;
        if (fcBondApplyDTO != null) {
            bailmodel = FcBondConstant.IS_HOOK_Y;
        }
        model.addAttribute("bailmodel", bailmodel);
        //是否启用担保参数
        model.addAttribute("isGuarantee", abGuaranteeService.isHangGuarantee());
        model.addAttribute("isHangGuarantee", vo.getIsHangGuarantee() == null ? FcBondConstant.IS_HOOK_N : vo.getIsHangGuarantee());
        model.addAttribute("contractIds", vo.getGuaContract());
        GuaContractOccupDTO hangGurVo = abGuaranteeService.getGuaContractOccupDTOByBillId(vo.getVvid());
        model.addAttribute("idList", hangGurVo == null ? null : hangGurVo.getContractIds());
        abBillAttachmentService.viewAttachment(accId, model);
        return "bill/accebill/viewAbEmitBill";
    }
    /**
     * 删除线上兑付申请信息
     *
     * @param aid 票据流水id
     * @return
     */
    @LogMessage(content = "线上兑付申请删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{aid}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("aid") String aid) {
        abEmitBillPayService.deleteByAid(aid, this.getCurrentUser());
        return success();
    }
    /**
     * 兑付申请 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    @LogMessage(content = "查询兑付申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abEmitBillPayService.listAbEmitBillPay(dataTablesInput);
    }
    /**
     * 票据详细信息查看
     * @return 前端路径
     */
    @GetMapping("/billPayInfoPage")
    public String accebillInfoPage(AbVoucherinfoViceVO reVo, String row, Model model) {
        if(StringUtils.isNotEmpty(reVo.getCorpId())){
            reVo.setCorpName(abCorpService.get(reVo.getCorpId()).getName());
        }
        model.addAttribute("billInfo", reVo);
        model.addAttribute("row", row);
        BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(reVo.getId());
        if(StringUtils.isNotEmpty(bisAccDtlDTO)){
            // 获取交易日期
            model.addAttribute("businessDate", DateUtils.date2Str(bisAccDtlDTO.getTransTime() ,DateUtils.date_sdf()));
            // 获取交易金额
            model.addAttribute("billMoney", bisAccDtlDTO.getAmt());
        }
        return "bill/accebill/billPayInfoPage";
    }
    /**
     * 查询未到期的应付票据信息 table列表
     *
     * @return 未到期的应付票据信息集合
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/billPayInfoDataTable")
    @ResponseBody
    @LogMessage(content = "查询未到期的应付票据信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public DataTablesOutput billPayInfoDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return abEmitBillPayService.billPayInfoDataTable(dataTablesInput);
    }
    /**
     * 线上兑付跟踪 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/trackDatatable")
    @ResponseBody
    @LogMessage(content = "查询兑付跟踪列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    public DataTablesOutput trackDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return abEmitBillPayService.listAbEmitBillPayTrack(dataTablesInput);
    }
    @LogMessage(content = "线下兑付确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/cashingConfirmation")
    @ResponseBody
    public AjaxJson cashingConfirmation(@RequestBody AbEmitBillPayVo pay) {
        abEmitBillPayService.cashingConfirmation(pay, this.getCurrentUser());
        return success();
    }
    @LogMessage(content = "批量线上兑付确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchCashingConfirmation")
    @ResponseBody
    public AjaxJson batchCashingConfirmation(@RequestBody AbEmitBillPayVo pay) {
        abEmitBillPayService.batchCashingConfirmation(pay, this.getCurrentUser());
        return success();
    }

    @LogMessage(content = "批量设置兑付确认备注", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSetEmitPayRmk")
    @ResponseBody
    public AjaxJson batchSetEmitPayRmk(@RequestBody AbEmitBillPayVo pay) {
        abEmitBillPayService.batchSetEmitPayRmk(pay, this.getCurrentUser());
        return success();
    }
    /**
     * 线上兑付付款单撤回
     *
     * @param aid 票据流水id
     * @return
     */
    @LogMessage(content = "付款单撤回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/withdraw/{aid}")
    @ResponseBody
    public AjaxJson withdraw(@PathVariable("aid") String aid) {
        abEmitBillPayService.withdraw(aid, this.getCurrentUser());
        return success();
    }
    /**
     * 线上兑付确认已付款
     *
     * @param aid 票据流水id
     * @return
     */
    @LogMessage(content = "确认已付款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/confrimepay/{aid}")
    @ResponseBody
    public AjaxJson confrimepay(@PathVariable("aid") String aid) {
        abEmitBillPayService.confrimepay(aid, this.getCurrentUser());
        return success();
    }
    /**
     * 批量确认已付款
     *
     * @param aids 票据流水ids
     * @return
     */
    @LogMessage(content = "批量确认已付款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchConfrimepay/{aids}")
    @ResponseBody
    public AjaxJson batchConfrimepay(@PathVariable("aids") String aids) {
        abEmitBillPayService.batchConfrimepay(aids, this.getCurrentUser());
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "bill/accebill/approve/listAbEmitBillPayApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return abEmitBillPayService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo info = abEmitBillPayService.getAbEmitBillVoByAid(businessId);
            model.addAttribute("abEmitBillVo", info);
            model.addAttribute("isApprove", true);
            //判断是否有已挂保证金数据
            FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(info.getVvid(), info.getAppNo());
            String bailmodel = FcBondConstant.IS_HOOK_N;
            if (fcBondApplyDTO != null) {
                bailmodel = FcBondConstant.IS_HOOK_Y;
            }
            model.addAttribute("bailmodel", bailmodel);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        // 票据挂预算初始化参数
        model.addAttribute("budgetSel",
                abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_ISBUDGET).getValue());
        model.addAttribute("payItem",
                abConfigService.getBillConfig(AccebillConstant.AB_CONFIG_EMITITEM).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        return "bill/accebill/approve/approveAbEmitBillPay";
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        // model.addAttribute("entity", 根据businessId查询到需要展示的数据);
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillVo info = abEmitBillPayService.getAbEmitBillVoByAid(businessId);
            model.addAttribute("abEmitBillVo", info);
            model.addAttribute("isApprove", true);
        }
        return "bill/accebill/mobile/viewApproveAbBillEmit";
    }

    @LogMessage(content = "调整票据解付金额", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/setBillEndPayMoney")
    @ResponseBody
    public AjaxJson setBillEndPayMoney(@RequestBody AbEmitBillPayVo pay) {
        if(StrUtil.isEmpty(pay.getVvid())) {
            return new AjaxJson(false,"从表ID不允许为空！");
        }
        abEmitBillPayService.setBillEndPayMoney(pay, this.getCurrentUser());
        return success();
    }
}
