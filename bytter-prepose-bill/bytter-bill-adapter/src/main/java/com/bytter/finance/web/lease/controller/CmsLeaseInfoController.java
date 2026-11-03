package com.bytter.finance.web.lease.controller;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.leaseUtils.LeaseUtils;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.GuaContractCmsLeaseVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
/**
 * 融资租赁申请
 */
@Controller
@RequestMapping("/cms/lease/financing/info")
public class CmsLeaseInfoController extends BaseController {
    @Autowired
    CmsFinancingLeaseInfoService cmsFinancingLeaseInfoService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    /**
     * 融资租赁申请列表-页面跳转
     */
    @LogMessage(content = "查询融资租赁申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/lease/listLeaseFinancingInfo";
    }
    /**
     * 融资申请列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsFinancingLeaseInfoService.getDataLeaseTables(dataTablesInput, getCurrentUser());
        return out;
    }
    /**
     * 融资申请列表 - 数据
     */
    @PostMapping("/leaseDatatable")
    @ResponseBody
    public DataTablesOutput leaseDatatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsFinancingLeaseInfoService.getDataleaseDatatables(dataTablesInput, getCurrentUser());
        return out;
    }
    /**
     * 融资申请查询列表 - 数据
     */
    @PostMapping("/queryDatatable")
    @ResponseBody
    public DataTablesOutput queryDatatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsFinancingLeaseInfoService.getQueryDatatable(dataTablesInput, getCurrentUser());
        return out;
    }
    /**
     * 租赁申请新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("isHookBond", "1");
        model.addAttribute("pledgeConfig", "1");
        model.addAttribute("isGuarantee", "1");
        return "cms/lease/addLeaseFinancingInfo";
    }
    /**
     * 提交
     */
    @LogMessage(content = "添加租赁申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(CmsFinancingLeaseInfoVO vo, String saveType) {
        cmsFinancingLeaseInfoService.temOrSub(vo, saveType, "apply", getCurrentUser());
        return success();
    }
    /**
     * 修改-页面跳转
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return null;
        }
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id);
        CorpDTO corpDTO = corpService.get(cmsFinancingLeaseInfo.getCorpId());
        model.addAttribute("leaseInfo", cmsFinancingLeaseInfo);
        model.addAttribute("corp", corpDTO);
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.addAttribute("baseCurId", cur.getId());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsFinancingLeaseInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.addAttribute("tbBaseRate", tbBaseRate);
        }
        TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbCreditPeriod) {
            model.addAttribute("tbCreditPeriod", tbCreditPeriod);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsFinancingLeaseInfo.getOriCurid(), cmsFinancingLeaseInfo.getReferenceRateType(),
                cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        // model.addAttribute("isHookBond", "1");
        // model.addAttribute("pledgeConfig", "1");
        // model.addAttribute("isGuarantee", "1");
        // 保证金参数设置
        LeaseUtils.setBondParam(model, "1", cmsFinancingLeaseInfo, fcBondHookService);
        // 担保参数设置
        LeaseUtils.setGuaParams(model, "0", "1", cmsFinancingLeaseInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsFinancingLeaseInfoService
                .listIntermediateCostByDfId(cmsFinancingLeaseInfo.getId());
        model.addAttribute("costVOS", costVOS);
        // 抵质押参数
        LeaseUtils.setPledgeParam(model, "1", cmsFinancingLeaseInfo, pledgeOccupyService);
        return "cms/lease/updateLeaseFinancingInfo";
    }
    /**
     * 查看-页面跳转
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return null;
        }
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id);
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService
                .getLeaseById(cmsFinancingLeaseInfo.getId());
        model.addAttribute("leaseInfo", cmsFinancingLeaseInfoVO);
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsFinancingLeaseInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.addAttribute("tbBaseRate", tbBaseRate);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsFinancingLeaseInfo.getOriCurid(), cmsFinancingLeaseInfo.getReferenceRateType(),
                cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.addAttribute("isHookBond", "1");
        model.addAttribute("pledgeConfig", "1");
        model.addAttribute("isGuarantee", "1");
        TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbCreditPeriod) {
            model.addAttribute("tbCreditPeriod", tbCreditPeriod);
        }
        // 保证金参数设置
        LeaseUtils.setBondParam(model, "1", cmsFinancingLeaseInfo, fcBondHookService);
        // 担保参数设置
        LeaseUtils.setGuaParams(model, "1", "1", cmsFinancingLeaseInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsFinancingLeaseInfoService
                .listIntermediateCostByDfId(cmsFinancingLeaseInfo.getId());
        model.addAttribute("costVOS", costVOS);
        // 抵质押参数
        LeaseUtils.setPledgeParam(model, "1", cmsFinancingLeaseInfo, pledgeOccupyService);
        return "cms/lease/viewLeaseFinancingInfo";
    }
    @LogMessage(content = "删除融资租赁申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsFinancingLeaseInfoService.deleteLease(id, getCurrentUser());
        return success();
    }
    /******************** 审批 **********************/
    /**
     * 租赁申请审批列表-页面跳转
     */
    @LogMessage(content = "查询租赁申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listInfoApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/approve/listLeaseInfoApprove";
    }
    /**
     * 租赁申请审批列表 - 数据
     */
    @PostMapping("/datatableInfoApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsFinancingLeaseInfoService.getDataInfoApproveTables(dataTablesInput);
    }
    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String businessId, String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(id);
            model.addAttribute("leaseInfo", info);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/lease/approve/viewLeaseInfoApprove";
    }
    /**
     * 租赁补录审批列表-页面跳转
     */
    @LogMessage(content = "查询租赁补录审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepairApprove")
    public String listRepairApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/approve/listLeaseRepairApprove";
    }
    /**
     * 租赁补录审批列表 - 数据
     */
    @PostMapping("/datatableRepairApprove")
    @ResponseBody
    public DataTablesOutput datatableRepairApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsFinancingLeaseInfoService.getDataRepairApproveTables(dataTablesInput);
    }
    /**
     * 补录审批 详情页面
     */
    @GetMapping("/RepairApprove")
    public String RepairApprove(String businessId, String id, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(id);
            model.addAttribute("leaseInfo", info);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/lease/approve/viewLeaseRepairApprove";
    }
    /**
     * 折合人民币
     *
     * @param curId
     * @return
     */
    @PostMapping("/conver/{curId}/{curPreTime}")
    @ResponseBody
    public BigDecimal getConverRmbRate(@PathVariable("curId") String curId,
            @PathVariable("curPreTime") String curPreTime) {
        return cmsFinancingLeaseInfoService.getConverRmbRate(curId, DateUtils.parse(curPreTime, "yyyy-MM-dd"));
    }
    /**
     * 融资租赁补录列表-页面跳转
     */
    @LogMessage(content = "查询融资租赁补录列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepair")
    public String listRepair(Model model) {
        return "cms/lease/listLeaseFinancingRepair";
    }
    /**
     * 融资补录列表 - 数据
     */
    @PostMapping("/repairDatatable")
    @ResponseBody
    public DataTablesOutput repairDatatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = cmsFinancingLeaseInfoService.getRepairDataLeaseTables(dataTablesInput, getCurrentUser());
        return out;
    }
    /**
     * 补录-页面跳转
     */
    @GetMapping("/repair/{id}")
    public String repair(@PathVariable("id") String id, Model model) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return null;
        }
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id);
        CorpDTO corpDTO = corpService.get(cmsFinancingLeaseInfo.getCorpId());
        model.addAttribute("leaseInfo", cmsFinancingLeaseInfo);
        model.addAttribute("corp", corpDTO);
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.addAttribute("baseCurId", cur.getId());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsFinancingLeaseInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.addAttribute("tbBaseRate", tbBaseRate);
        }
        TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbCreditPeriod) {
            model.addAttribute("tbCreditPeriod", tbCreditPeriod);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsFinancingLeaseInfo.getOriCurid(), cmsFinancingLeaseInfo.getReferenceRateType(),
                cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.addAttribute("isHookBond", "1");
        model.addAttribute("pledgeConfig", "1");
        model.addAttribute("isGuarantee", "1");
        // 保证金参数设置
        LeaseUtils.setBondParam(model, "1", cmsFinancingLeaseInfo, fcBondHookService);
        // 担保参数设置
        LeaseUtils.setGuaParams(model, "0", "1", cmsFinancingLeaseInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsFinancingLeaseInfoService
                .listIntermediateCostByDfId(cmsFinancingLeaseInfo.getId());
        model.addAttribute("costVOS", costVOS);
        // 抵质押参数
        LeaseUtils.setPledgeParam(model, "1", cmsFinancingLeaseInfo, pledgeOccupyService);
        return "cms/lease/repairLeaseFinancingInfo";
    }
    /**
     * 补录提交暂存
     */
    @LogMessage(content = "添加租赁申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmitRepair")
    @ResponseBody
    public AjaxJson tempOrSubmitRepair(CmsFinancingLeaseInfoVO vo, String saveType) {
        cmsFinancingLeaseInfoService.temOrSub(vo, saveType, "repair", getCurrentUser());
        return success();
    }

    /**
     * 获取挂接担保合同的内部拆借数据
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ResponseBody
    @PostMapping("/getGuaContract")
    public DataTablesOutput<GuaContractCmsLeaseVO> getGuaContract(@RequestBody DataTablesInput dataTablesInput) {
        return cmsFinancingLeaseInfoService.getGuaContract(dataTablesInput, getCurrentUser());
    }
}
