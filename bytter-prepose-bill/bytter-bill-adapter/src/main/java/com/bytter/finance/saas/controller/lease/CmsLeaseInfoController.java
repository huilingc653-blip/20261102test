package com.bytter.finance.saas.controller.lease;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.lease.entity.CmsFinancingLeaseInfo;
import com.bytter.finance.domain.lease.leaseUtils.LeaseUtils;
import com.bytter.finance.domain.lease.service.CmsFinancingLeaseInfoService;
import com.bytter.finance.domain.lease.vo.CmsFinancingLeaseInfoVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseApproveDetailVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseApproveListVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseInfoAddVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseInfoEditVO;
import com.bytter.finance.domain.lease.vo.CmsLeaseInfoViewVO;
import com.bytter.finance.domain.lease.vo.GuaContractCmsLeaseVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资租赁申请
 */
@Api(value = "/v1-preposebill/cms/lease/financing/info", tags = "融资租赁申请")
@RestController("v1CmsLeaseInfoController")
@RequestMapping("/v1-preposebill/cms/lease/financing/info")
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
    @DubboReference(check = false)
    private IConfigService configService;
    static final String RATE = "rate";

    /**
     * 融资租赁申请列表 - 数据
     */
    @ApiOperation("查询融资租赁申请列表")
    @LogMessage(content = "查询融资租赁申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsFinancingLeaseInfoService.getDataLeaseTables(input, getCurrentUser()));
    }

    /**
     * 融资申请列表 - 数据
     */
    @ApiOperation("查询融资申请列表")
    @PostMapping("/leaseDatatable")
    public AjaxJson<DataTablesOutput> leaseDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsFinancingLeaseInfoService.getDataleaseDatatables(input, getCurrentUser()));
    }

    /**
     * 融资申请查询列表 - 数据
     */
    @ApiOperation("查询融资申请查询列表")
    @PostMapping("/queryDatatable")
    public AjaxJson<DataTablesOutput> queryDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsFinancingLeaseInfoService.getQueryDatatable(input, getCurrentUser()));
    }

    /**
     * 租赁申请新增页面数据
     */
    @ApiOperation("租赁申请新增页面数据")
    @GetMapping("/add")
    public AjaxJson<CmsLeaseInfoAddVO> add() {
        CmsLeaseInfoAddVO result = new CmsLeaseInfoAddVO();
        result.setIsHookBond("1");
        result.setPledgeConfig("1");
        result.setIsGuarantee("1");
        return success(result);
    }

    /**
     * 提交
     */
    @ApiOperation("添加租赁申请表")
    @LogMessage(content = "添加租赁申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody CmsFinancingLeaseInfoVO vo) {
        cmsFinancingLeaseInfoService.temOrSub(vo, vo.getSaveType(), "apply", getCurrentUser());
        return success();
    }

    /**
     * 修改-页面数据
     */
    @ApiOperation("租赁申请修改页面数据")
    @GetMapping("/update/{id}")
    public AjaxJson<CmsLeaseInfoEditVO> update(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return success(null);
        }
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id);
        Map<String, Object> model = new HashMap<>();
        model.put("leaseInfo", cmsFinancingLeaseInfo);
        CorpDTO corpDTO = corpService.get(cmsFinancingLeaseInfo.getCorpId());
        model.put("corp", corpDTO);
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.put("baseCurId", cur.getId());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsFinancingLeaseInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.put("tbBaseRate", tbBaseRate);
        }
        TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbCreditPeriod) {
            model.put("tbCreditPeriod", tbCreditPeriod);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsFinancingLeaseInfo.getOriCurid(), cmsFinancingLeaseInfo.getReferenceRateType(),
                cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.put("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        // 保证金参数设置
        LeaseUtils.setBondParam(model, "1", cmsFinancingLeaseInfo, fcBondHookService);
        // 担保参数设置
        LeaseUtils.setGuaParams(model, "0", "1", cmsFinancingLeaseInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsFinancingLeaseInfoService
                .listIntermediateCostByDfId(cmsFinancingLeaseInfo.getId());
        model.put("costVOS", costVOS);
        // 抵质押参数
        LeaseUtils.setPledgeParam(model, "1", cmsFinancingLeaseInfo, pledgeOccupyService);
        return success(BeanUtils.copyMapProperties(model, CmsLeaseInfoEditVO.class));
    }

    /**
     * 查看-页面数据
     */
    @ApiOperation("查询租赁申请详情")
    @GetMapping("/view/{id}")
    public AjaxJson<CmsLeaseInfoViewVO> view(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return success(null);
        }
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id);
        Map<String, Object> model = new HashMap<>();
        CmsFinancingLeaseInfoVO cmsFinancingLeaseInfoVO = cmsFinancingLeaseInfoService
                .getLeaseById(cmsFinancingLeaseInfo.getId());

        if(!EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO.getRate())){
            cmsFinancingLeaseInfoVO.setRateShow(show(cmsFinancingLeaseInfoVO.getRate().doubleValue()));
        }
        if(!EmptyUtils.isEmpty(cmsFinancingLeaseInfoVO.getIncRate())){
            cmsFinancingLeaseInfoVO.setIncRateShow(show(cmsFinancingLeaseInfoVO.getIncRate().doubleValue()));
        }
        model.put("leaseInfo", cmsFinancingLeaseInfoVO);
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsFinancingLeaseInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.put("tbBaseRate", tbBaseRate);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsFinancingLeaseInfo.getOriCurid(), cmsFinancingLeaseInfo.getReferenceRateType(),
                cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.put("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.put("isHookBond", "1");
        model.put("pledgeConfig", "1");
        model.put("isGuarantee", "1");
        TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbCreditPeriod) {
            model.put("tbCreditPeriod", tbCreditPeriod);
        }
        // 保证金参数设置
        LeaseUtils.setBondParam(model, "1", cmsFinancingLeaseInfo, fcBondHookService);
        // 担保参数设置
        LeaseUtils.setGuaParams(model, "1", "1", cmsFinancingLeaseInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsFinancingLeaseInfoService
                .listIntermediateCostByDfId(cmsFinancingLeaseInfo.getId());
        if(!EmptyUtils.isEmpty(costVOS)){
            costVOS.forEach(e->{
                if(!EmptyUtils.isEmpty(e.getRate())){
                    e.setRateShow(show(e.getRate().doubleValue()));
                }
            });
        }
        model.put("costVOS", costVOS);
        // 抵质押参数
        LeaseUtils.setPledgeParam(model, "1", cmsFinancingLeaseInfo, pledgeOccupyService);
        return success(BeanUtils.copyMapProperties(model, CmsLeaseInfoViewVO.class));
    }

    /**
     * 删除融资租赁申请信息
     */
    @ApiOperation("删除融资租赁申请信息")
    @LogMessage(content = "删除融资租赁申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsFinancingLeaseInfoService.deleteLease(id, getCurrentUser());
        return success();
    }

    /******************** 审批 **********************/
    /**
     * 租赁申请审批列表页面数据
     */
    @ApiOperation("租赁申请审批列表页面数据")
    @LogMessage(content = "查询租赁申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listInfoApprove")
    public AjaxJson<CmsLeaseApproveListVO> listInfoApprove(String flowStatus, String cycleTime) {
        CmsLeaseApproveListVO result = new CmsLeaseApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 租赁申请审批列表 - 数据
     */
    @ApiOperation("查询租赁申请审批列表")
    @PostMapping("/datatableInfoApprove")
    public AjaxJson<DataTablesOutput> datatableInfoApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsFinancingLeaseInfoService.getDataInfoApproveTables(input));
    }

    /**
     * 申请审批 详情页面数据
     */
    @ApiOperation("申请审批详情页面数据")
    @GetMapping("/infoApprove")
    public AjaxJson<CmsLeaseApproveDetailVO> infoApprove(String businessId, String id, String taskId) {
        CmsLeaseApproveDetailVO result = new CmsLeaseApproveDetailVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(id);
            result.setLeaseInfo(info);
        }
        result.setTaskId(taskId);// 必须要做此步骤
        return success(result);
    }

    /**
     * 租赁补录审批列表页面数据
     */
    @ApiOperation("租赁补录审批列表页面数据")
    @LogMessage(content = "查询租赁补录审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepairApprove")
    public AjaxJson<CmsLeaseApproveListVO> listRepairApprove(String flowStatus, String cycleTime) {
        CmsLeaseApproveListVO result = new CmsLeaseApproveListVO();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }

    /**
     * 租赁补录审批列表 - 数据
     */
    @ApiOperation("查询租赁补录审批列表")
    @PostMapping("/datatableRepairApprove")
    public AjaxJson<DataTablesOutput> datatableRepairApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsFinancingLeaseInfoService.getDataRepairApproveTables(input));
    }

    /**
     * 补录审批 详情页面数据
     */
    @ApiOperation("补录审批详情页面数据")
    @GetMapping("/RepairApprove")
    public AjaxJson<CmsLeaseApproveDetailVO> RepairApprove(String businessId, String id, String taskId) {
        CmsLeaseApproveDetailVO result = new CmsLeaseApproveDetailVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsFinancingLeaseInfo info = cmsFinancingLeaseInfoService.get(id);
            result.setLeaseInfo(info);
        }
        result.setTaskId(taskId);// 必须要做此步骤
        return success(result);
    }

    /**
     * 折合人民币
     *
     * @param curId
     * @param curPreTime
     * @return
     */
    @ApiOperation("折合人民币汇率")
    @GetMapping("/conver/{curId}/{curPreTime}")
    public AjaxJson<BigDecimal> getConverRmbRate(@PathVariable("curId") String curId,
            @PathVariable("curPreTime") String curPreTime) {
        return success(cmsFinancingLeaseInfoService.getConverRmbRate(curId, DateUtils.parse(curPreTime, "yyyy-MM-dd")));
    }

    /**
     * 融资补录列表 - 数据
     */
    @ApiOperation("查询融资租赁补录列表")
    @LogMessage(content = "查询融资租赁补录列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/repairDatatable")
    public AjaxJson<DataTablesOutput> repairDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsFinancingLeaseInfoService.getRepairDataLeaseTables(input, getCurrentUser()));
    }

    /**
     * 补录-页面数据
     */
    @ApiOperation("租赁补录页面数据")
    @GetMapping("/repair/{id}")
    public AjaxJson<CmsLeaseInfoEditVO> repair(@PathVariable("id") String id) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return success(null);
        }
        CmsFinancingLeaseInfo cmsFinancingLeaseInfo = cmsFinancingLeaseInfoService.get(id);
        Map<String, Object> model = new HashMap<>();
        model.put("leaseInfo", cmsFinancingLeaseInfo);
        CorpDTO corpDTO = corpService.get(cmsFinancingLeaseInfo.getCorpId());
        model.put("corp", corpDTO);
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.put("baseCurId", cur.getId());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsFinancingLeaseInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.put("tbBaseRate", tbBaseRate);
        }
        TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbCreditPeriod) {
            model.put("tbCreditPeriod", tbCreditPeriod);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsFinancingLeaseInfo.getOriCurid(), cmsFinancingLeaseInfo.getReferenceRateType(),
                cmsFinancingLeaseInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.put("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        model.put("isHookBond", "1");
        model.put("pledgeConfig", "1");
        model.put("isGuarantee", "1");
        // 保证金参数设置
        LeaseUtils.setBondParam(model, "1", cmsFinancingLeaseInfo, fcBondHookService);
        // 担保参数设置
        LeaseUtils.setGuaParams(model, "0", "1", cmsFinancingLeaseInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = cmsFinancingLeaseInfoService
                .listIntermediateCostByDfId(cmsFinancingLeaseInfo.getId());
        model.put("costVOS", costVOS);
        // 抵质押参数
        LeaseUtils.setPledgeParam(model, "1", cmsFinancingLeaseInfo, pledgeOccupyService);
        return success(BeanUtils.copyMapProperties(model, CmsLeaseInfoEditVO.class));
    }

    /**
     * 补录提交暂存
     */
    @ApiOperation("租赁补录提交暂存")
    @LogMessage(content = "添加租赁申请表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmitRepair")
    public AjaxJson tempOrSubmitRepair(@RequestBody CmsFinancingLeaseInfoVO vo) {
        cmsFinancingLeaseInfoService.temOrSub(vo, vo.getSaveType(), "repair", getCurrentUser());
        return success();
    }

    /**
     * 获取挂接担保合同的内部拆借数据
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ApiOperation(value = "获取挂接担保合同的融资租赁数据")
    @PostMapping("/getGuaContract")
    public AjaxJson<DataTablesOutput<GuaContractCmsLeaseVO>> getGuaContract(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        return success(cmsFinancingLeaseInfoService.getGuaContract(tablesInput, getCurrentUser()));
    }


    public String show(Double number) {
        if (number == null) {
            return "";
        }
        Integer rate = configService.getConfig(ConfigConstant.SYS, RATE).toInteger();
        String formatNumber;
        if (rate.equals(1)) {
            DecimalFormat df = new DecimalFormat("0.000");
            formatNumber = df.format(number * 1000) + "‰";
        } else {
            DecimalFormat df = new DecimalFormat("0.0000");
            formatNumber = df.format(number * 100) + "%";
        }
        return formatNumber;
    }
}
