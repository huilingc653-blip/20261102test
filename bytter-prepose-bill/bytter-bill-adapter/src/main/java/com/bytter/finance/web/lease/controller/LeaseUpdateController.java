package com.bytter.finance.web.lease.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseUpdate;
import com.bytter.finance.domain.lease.leaseUtils.LeaseUtils;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseUpdateService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.LeaseUpdateVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 融资租赁变更
 */
@Controller
@RequestMapping("/cms/lease/update")
public class LeaseUpdateController extends BaseController {
    @Autowired
    LeaseUpdateService leaseUpdateService;
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
    @LogMessage(content = "查询融资租赁变更列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/lease/listLeaseUpdate";
    }
    /**
     * 租赁变更列表 - 数据
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput datatable(@RequestBody DataTablesInput dataTablesInput) {
        return leaseUpdateService.getUpdateDatatables(dataTablesInput, getCurrentUser());
    }
    /**
     * 修改-页面跳转
     */
    @GetMapping("/change")
    public String repair(String id, String updateId, String modifyStatus, Model model) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return null;
        }
        LeaseUpdate leaseUpdate = new LeaseUpdate();
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id);
        if (StringUtils.isNotEmpty(updateId)) {
            if (modifyStatus.equals("-1") || modifyStatus.equals("0")) {
                leaseUpdate = leaseUpdateService.get(updateId);
            } else {
                BeanUtils.copyNotNullProperties(cmsFinancingLeaseInfo, leaseUpdate);
                leaseUpdate.setId("");
                leaseUpdate.setLeaseId(cmsFinancingLeaseInfo.getId());
            }
        } else {
            BeanUtils.copyNotNullProperties(cmsFinancingLeaseInfo, leaseUpdate);
            leaseUpdate.setId("");
            leaseUpdate.setLeaseId(cmsFinancingLeaseInfo.getId());
        }
        model.addAttribute("leaseUpdate", leaseUpdate);
        BigDecimal repayMoney = leaseUpdateService.getRePayMoney(id);
        model.addAttribute("repayMoney", repayMoney);
        CorpDTO corpDTO = corpService.get(cmsFinancingLeaseInfo.getCorpId());
        // model.addAttribute("leaseInfo", cmsFinancingLeaseInfo);
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
        return "cms/lease/changeLeaseFinancing";
    }
    /**
     * 提交
     */
    @LogMessage(content = "添加租赁变更表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(CmsFinancingLeaseInfoVO vo, String updateId, String addLeaseCode, Date modifyDate,
            String saveType) {
        LeaseUpdateVO leaseUpdateVO = new LeaseUpdateVO();
        BeanUtils.copyNotNullProperties(vo, leaseUpdateVO);
        leaseUpdateVO.setId(updateId);
        leaseUpdateVO.setLeaseId(vo.getId());
        leaseUpdateVO.setModifyDate(modifyDate);
        leaseUpdateVO.setAddLeaseCode(addLeaseCode);
        leaseUpdateService.tempOrSubmit(vo, saveType, leaseUpdateVO, getCurrentUser());
        return success();
    }
    @LogMessage(content = "删除变更信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        LeaseUpdate leaseUpdate = leaseUpdateService.get(id);
        leaseUpdate.setStatus(CmsConstant.DELETED);
        leaseUpdateService.update(leaseUpdate, getCurrentUser());
        return success();
    }
    /**
     * 租赁补录审批列表-页面跳转
     */
    @LogMessage(content = "查询租赁变更审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listUpdateApprove")
    public String listUpdateApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/lease/approve/listLeaseUpdateApprove";
    }
    /**
     * 租赁变更审批列表 - 数据
     */
    @PostMapping("/datatableUpdateApprove")
    @ResponseBody
    public DataTablesOutput datatableUpdateApprove(@RequestBody DataTablesInput dataTablesInput) {
        return leaseUpdateService.getDataUpdateApproveTables(dataTablesInput);
    }
    /**
     * 变更审批 详情页面
     */
    @GetMapping("/UpdateApprove")
    public String RepairApprove(String businessId, String leaseId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(leaseId);
            model.addAttribute("leaseInfo", info);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/lease/approve/viewLeaseRepairApprove";
    }
}
