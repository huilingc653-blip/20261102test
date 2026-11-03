package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.service.*;
import com.bytter.bill.domain.bill.vo.AbEmitBillSupplyVo;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.system.api.constant.ConfigConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: 开票补录Controller
 * @Description:
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 */
@Api(value = "/v1-preposebill/bill/accebill/abEmitBillSupply", tags = "开票补录")
@RestController("v1AbEmitBillSupplyController")
@RequestMapping("/v1-preposebill/bill/accebill/abEmitBillSupply")
public class AbEmitBillSupplyController extends BaseController {
    @Autowired
    private AbEmitBillSupplyService abEmitBillSupplyService;
    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;
    @Autowired
    private AbRatUrationService abRatUrationService;
    @Autowired
    private AbFbsBudgetProcessService abFbsBudgetProcessService;
    @Autowired
    private AbConfigService abConfigService;
    @Autowired
    private AbBillAttachmentService abBillAttachmentService;
    @Autowired
    private AbFcBondHookService abFcBondHookService;
    @Autowired
    private AbGuaranteeService abGuaranteeService;
    @Autowired
    private AbRatCpUrationService abRatCpUrationService;

    /**
     * 开票补录列表查询
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "开票补录列表查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    @ApiOperation("开票补录分页列表查询")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = abEmitBillSupplyService.getDataTables(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 开票补录编辑页面初始化
     */
    @GetMapping("/makeup/{aid}")
    @ApiOperation("开票补录编辑页面初始化")
    public AjaxJson<Map<String, Object>> makeup(@PathVariable("aid") String aid) {
        Map<String, Object> dataMap = new HashMap<>(24);
        AbEmitBillSupplyVo vo = abEmitBillSupplyService.getAbEmitBillSupplyVoById(aid);
        dataMap.put("abEmitBillSupplyVo", vo);

        // 挂预算系统参数
        if (StringUtils.isNotBlank(vo.getVoucherId())) {
            List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                    BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, vo.getVoucherId());
            if (list != null && !list.isEmpty()) {
                dataMap.put("payItem", abConfigService.getConfig(ConfigConstant.NIS, NetbankConstant.NIS_CONFIG_PAYITEM).toString());
                dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_PAY);
                dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
            } else {
                dataMap.put("nisBillheadNoBudget", true);
            }
        } else if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            dataMap.put("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
            dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
            dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
        }

        // 挂授信
        boolean isRelateRat = abRatUrationService.isRelateRat();
        dataMap.put("isRelateRat", isRelateRat);
        dataMap.put("curId", abVoucherinfoViceService.getCurIdByCNY());
        String isOpenRat = FcBondConstant.IS_HOOK_N;
        String ratmodel = FcBondConstant.IS_HOOK_N;
        RatUrationDTO urationDTO = new RatUrationDTO();
        if (isRelateRat) {
            if (!StringUtils.isBlank(vo.getRatmodel()) && vo.getRatmodel().equals("1")) {
                ratmodel = vo.getRatmodel();
                urationDTO = abRatUrationService.getRatUrationDTOByBillId(vo.getVvid());
            }
            dataMap.put("urationDTO", urationDTO);
            if (!StringUtils.isBlank(vo.getIsOpenRat()) && vo.getIsOpenRat().equals("1")) {
                isOpenRat = vo.getIsOpenRat();
            }
        }
        dataMap.put("isOpenRat", isOpenRat);
        dataMap.put("ratmodel", ratmodel);

        //是否挂保证金
        boolean isBail = abFcBondHookService.isBail();
        FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
        String bailmodel = FcBondConstant.IS_HOOK_N;
        String bondId = "";
        if (fcBondApplyDTO != null) {
            bondId = fcBondApplyDTO.getBondId();
            bailmodel = FcBondConstant.IS_HOOK_Y;
        }
        dataMap.put("bondId", bondId);
        dataMap.put("bailmodel", bailmodel);
        dataMap.put("isBail", isBail);
        dataMap.put("bailBankControlType", abFcBondHookService.bailBankControlType());
        dataMap.put("mouthRat", abRatUrationService.isNotOpenRat());

        //担保参数
        dataMap.put("isGuarantee", abGuaranteeService.isHangGuarantee());
        String isHangGuarantee = vo.getIsHangGuarantee() == null ? FcBondConstant.IS_HOOK_N : vo.getIsHangGuarantee();
        dataMap.put("isHangGuarantee", isHangGuarantee);
        if (abGuaranteeService.isHangGuarantee().equals(1) && isHangGuarantee.equals(FcBondConstant.IS_HOOK_Y)) {
            GuaContractOccupDTO bearVo = abGuaranteeService.getGuaContractOccupDTOByBillId(vo.getVvid());
            if (null != bearVo && bearVo.getContractIds() != null && bearVo.getContractIds().size() > 0) {
                List<String> idList = bearVo.getContractIds();
                StringBuilder contractIdsSb = new StringBuilder();
                for (String id : idList) {
                    contractIdsSb.append(id).append(",");
                }
                dataMap.put("contractIds", contractIdsSb.toString());
                dataMap.put("contractCode", bearVo.getContractCode());
                dataMap.put("idList", idList);
                dataMap.put("guaWayNames", bearVo.getGuaWayName());
                dataMap.put("guaWay", bearVo.getGuaWay());
                dataMap.put("guaranteeRmk", bearVo.getGuaRmk());
            }
        }

        dataMap.put("attachmentInfo", abBillAttachmentService.editAttachment(aid));
        //内部授信
        dataMap.put("isOpenCpRat", abRatCpUrationService.isRelateCpRat());
        dataMap.put("curOpenCpRat", vo.getInsideRatModel() == null ? "0" : vo.getInsideRatModel());
        RatUrationDTO ratDto = abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid());
        dataMap.put("cpURationDTO", (ratDto == null || "3".equals(ratDto.getStatus())) ? null : ratDto);
        dataMap.put("forwardPath", "bill/accebill/makeupAbEmitBillSupply");
        return AjaxJson.success(dataMap);
    }

    /**
     * 开票补录提交并入库
     */
    @LogMessage(content = "开票补录提交并入库", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/makeupandstorage")
    @ApiOperation("开票补录提交并入库")
    public AjaxJson makeupandstorage(AbEmitBillSupplyVo abEmitBillSupplyVo, RatUrationDTO urationDTO) {
        abEmitBillSupplyVo.setUrationDTO(urationDTO);
        if (urationDTO != null) {
            abEmitBillSupplyVo.setCreditCode(urationDTO.getRatItemCode());
        }
        abEmitBillSupplyService.makeupSave(abEmitBillSupplyVo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 批量补录完成并入库
     */
    @LogMessage(content = "批量开票补录完成并入库", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/batchMakeupandstorage")
    @ApiOperation("批量开票补录完成并入库")
    public AjaxJson batchMakeupandstorage(String vids, String vvids, String aids) {
        abEmitBillSupplyService.batchMakeup(vids, vvids, aids, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ApiOperation("开票补录审核分页列表查询")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abEmitBillSupplyService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审批详情页面初始化
     */
    @GetMapping("/approve")
    @ApiOperation("开票补录审批详情页面初始化")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(16);
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillSupplyVo vo = abEmitBillSupplyService.getAbEmitBillSupplyVoById(businessId);
            dataMap.put("abEmitBillVo", vo);
            dataMap.put("isApprove", true);
            dataMap.put("billId", vo.getVvid());

            //预算参数
            if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
                dataMap.put("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
                dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
                dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
            }
            List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                    BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
            if (list != null && !list.isEmpty()) {
                dataMap.put("budgetDisplay", "true");
            }

            //保证金
            FcBondApplyDTO fcBondApplyDTO = abFcBondHookService.getFcBondApplyByItemIds(vo.getVvid(), vo.getAppNo());
            String bailmodel = FcBondConstant.IS_HOOK_N;
            if (fcBondApplyDTO != null) {
                bailmodel = FcBondConstant.IS_HOOK_Y;
            }
            dataMap.put("bailmodel", bailmodel);

            //授信显示
            dataMap.put("isEnabled", StringUtils.isNotBlank(vo.getRatmodel()) && "1".equals(vo.getRatmodel()));
            //内部授信
            boolean cpIsEnabled = StringUtils.isNotBlank(vo.getInsideRatModel()) && FcBondConstant.IS_HOOK_Y.equals(vo.getInsideRatModel());
            dataMap.put("cpIsEnabled", cpIsEnabled);
            if (cpIsEnabled) {
                dataMap.put("cpURationDTO", abRatCpUrationService.getRatUrationDTOByBillId(vo.getVvid()));
            }
            dataMap.put("curId", abVoucherinfoViceService.getCurIdByCNY());
            dataMap.put("isHangGuarantee", vo.getIsHangGuarantee() == null ? FcBondConstant.IS_HOOK_N : vo.getIsHangGuarantee());
            dataMap.put("appNo", vo.getAppNo());
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/approve/approveAbEmitBillSupply");
        return AjaxJson.success(dataMap);
    }

    /**
     * 移动端审批详情页面初始化
     */
    @GetMapping("/mobileApprove")
    @ApiOperation("开票补录移动端审批详情页面初始化")
    public AjaxJson<Map<String, Object>> mobileApprove(String businessId, String taskId) {
        Map<String, Object> dataMap = new HashMap<>(12);
        if (StringUtils.isNotEmpty(businessId)) {
            AbEmitBillSupplyVo vo = abEmitBillSupplyService.getAbEmitBillSupplyVoById(businessId);
            dataMap.put("abEmitBillVo", vo);
            dataMap.put("isApprove", true);
            if (abVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
                dataMap.put("payItem", abVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
                dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_EMIT);
                dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_AB);
            }
            List<FbsBudgetProcessDTO> list = abFbsBudgetProcessService.getBudgetProcessByBillNoForBill(
                    BudgetConstant.BUSINESS_NAME_EMIT, BudgetConstant.PROCESS_SYSNAME_AB, vo.getVvid());
            if (list != null && !list.isEmpty()) {
                dataMap.put("budgetDisplay", "true");
            }
        }
        dataMap.put("taskId", taskId);
        dataMap.put("forwardPath", "bill/accebill/mobile/viewApproveAbBillEmit");
        return AjaxJson.success(dataMap);
    }

    /**
     * 开票补录导入
     */
    @PostMapping("/saveImport")
    @ApiOperation("开票补录导入")
    public AjaxJson saveImport(@RequestBody List<AbEmitBillSupplyVo> list) {
        abEmitBillSupplyService.saveImport(list, this.getCurrentUser());
        return AjaxJson.success();
    }
}