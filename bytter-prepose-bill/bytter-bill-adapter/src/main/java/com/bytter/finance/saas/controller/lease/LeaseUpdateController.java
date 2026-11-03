package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.entity.LeaseUpdate;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.service.LeaseUpdateService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseApproveDetailVO;
import com.bytter.finance.domain.lease.vo.LeaseUpdateEditVO;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 融资租赁变更
 */
@io.swagger.annotations.Api(value = "/v1-preposebill/cms/lease/update", tags = "融资租赁变更")
@RestController("v1LeaseUpdateController")
@RequestMapping("/v1-preposebill/cms/lease/update")
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
     * 租赁变更列表 - 数据
     */
    @io.swagger.annotations.ApiOperation("租赁变更列表数据")
    @LogMessage(content = "查询融资租赁变更列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseUpdateService.getUpdateDatatables(input, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 修改页面初始化数据
     */
    @io.swagger.annotations.ApiOperation("变更编辑页面初始化数据")
    @GetMapping("/change")
    public AjaxJson<LeaseUpdateEditVO> repair(String id, String updateId, String modifyStatus) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return AjaxJson.success(null);
        }
        LeaseUpdateEditVO result = new LeaseUpdateEditVO();
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
        result.setLeaseUpdate(leaseUpdate);
        result.setRepayMoney(leaseUpdateService.getRePayMoney(id));
        result.setCorp(corpService.get(cmsFinancingLeaseInfo.getCorpId()));
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        result.setBaseCurId(cur.getId());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsFinancingLeaseInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            result.setTbBaseRate(tbBaseRate);
        }
        TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbCreditPeriod) {
            result.setTbCreditPeriod(tbCreditPeriod);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsFinancingLeaseInfo.getOriCurid(), cmsFinancingLeaseInfo.getReferenceRateType(),
                cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            result.setTbLoanRateMaintence(tbLoanRateMaintence);
        }
        result.setCostVOS(cmsFinancingLeaseInfoService.listIntermediateCostByDfId(cmsFinancingLeaseInfo.getId()));
        return AjaxJson.success(result);
    }

    /**
     * 租赁变更暂存/提交
     */
    @io.swagger.annotations.ApiOperation("租赁变更暂存/提交")
    @LogMessage(content = "添加租赁变更表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody CmsFinancingLeaseInfoVO vo) {
        LeaseUpdateVO leaseUpdateVO = new LeaseUpdateVO();
        BeanUtils.copyNotNullProperties(vo, leaseUpdateVO);
        leaseUpdateVO.setId(vo.getUpdateId());
        leaseUpdateVO.setLeaseId(vo.getId());
        leaseUpdateVO.setModifyDate(vo.getModifyDate());
        leaseUpdateVO.setAddLeaseCode(vo.getAddLeaseCode());
        leaseUpdateService.tempOrSubmit(vo, vo.getSaveType(), leaseUpdateVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除变更信息
     */
    @io.swagger.annotations.ApiOperation("删除租赁变更信息")
    @LogMessage(content = "删除变更信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        LeaseUpdate leaseUpdate = leaseUpdateService.get(id);
        leaseUpdate.setStatus(CmsConstant.DELETED);
        leaseUpdateService.update(leaseUpdate, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 租赁变更审批列表数据
     */
    @io.swagger.annotations.ApiOperation("租赁变更审批列表数据")
    @LogMessage(content = "查询租赁变更审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableUpdateApprove")
    public AjaxJson<DataTablesOutput> datatableUpdateApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = leaseUpdateService.getDataUpdateApproveTables(input);
        return AjaxJson.success(output);
    }

    /**
     * 变更审批详情
     */
    @io.swagger.annotations.ApiOperation("租赁变更审批详情")
    @GetMapping("/UpdateApprove")
    public AjaxJson<CmsLeaseApproveDetailVO> RepairApprove(String businessId, String leaseId, String taskId) {
        CmsLeaseApproveDetailVO result = new CmsLeaseApproveDetailVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            result.setLeaseInfo(cmsFinancingLeaseInfoService.get(leaseId));
        }
        result.setTaskId(taskId);
        return AjaxJson.success(result);
    }
}