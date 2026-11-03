package com.bytter.finance.saas.controller.boond;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlan;
import com.bytter.finance.domain.bond.service.*;
import com.bytter.finance.domain.bond.utils.BondUtils;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.system.api.IBaseRateService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ICreditPeriodService;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.TbBaseRateDTO;
import com.bytter.system.api.dto.TbCreditPeriodDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 债券发行Controller
 *
 * @author ouyj
 */
@Api(value = "/v1-preposebill/cms/bond/issue", tags = "债券发行管理")
@RestController("v1CmsBondIssueInfoController")
@RequestMapping("/v1-preposebill/cms/bond/issue")
public class CmsBondIssueInfoController extends BaseController {
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @Autowired
    private CmsBondPayService cmsBondPayService;
    @DubboReference(check = false)
    private ICreditPeriodService iCreditPeriodService;
    @Autowired
    private CmsBondIssueInfoService cmsBondIssueInfoService;
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupyService;
    @Autowired
    private CmsBondRepayPlanService cmsBondRepayPlanService;
    @Autowired
    private CmsBondRepayPlanDtlService cmsBondRepayPlanDtlService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @DubboReference(check = false)
    private ICorpService corpService;

    /**
     * 债券发行新增页面初始化数据
     */
    @ApiOperation("债券发行新增初始化数据")
    @GetMapping("/add/{registerId}")
    public AjaxJson<Map<String, Object>> add(@PathVariable("registerId") String registerId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("registerInfo", cmsBondRegisterInfoService.getInfoById(registerId));
        resultMap.put("isHookBond", "1");
        resultMap.put("pledgeConfig", "1");
        resultMap.put("isGuarantee", "1");
        return AjaxJson.success(resultMap);
    }

    /**
     * 债券发行编辑页面初始化数据
     */
    @ApiOperation("债券发行编辑初始化数据")
    @GetMapping("/update/{id}")
    public AjaxJson<Map<String, Object>> update(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(id, true);
        resultMap.put("cmsBondRepayPlanVo", cmsBondRepayPlanService.getByIssueId(cmsBondIssueInfo.getId()));
        resultMap.put("issueInfo", cmsBondIssueInfo);
        resultMap.put("registerInfo", cmsBondRegisterInfoService.getInfoById(cmsBondIssueInfo.getRegisterId()));
        // 工具方法填充参数至map，替换原Model
        Map<String, Object> bondParamMap = new HashMap<>();
        BondUtils.setBondParam(bondParamMap, "1", cmsBondIssueInfo, fcBondHookService);
        resultMap.putAll(bondParamMap);

        Map<String, Object> guaParamMap = new HashMap<>();
        BondUtils.setGuaParams(guaParamMap, "0", "1", cmsBondIssueInfo, guaContractOccupyService);
        resultMap.putAll(guaParamMap);

        Map<String, Object> pledgeParamMap = new HashMap<>();
        BondUtils.setPledgeParam(pledgeParamMap, "1", cmsBondIssueInfo, pledgeOccupyService);
        resultMap.putAll(pledgeParamMap);

        List<DfIntermediateCostVO> costList = cmsBondIssueInfoService.listIntermediateCostByDfId(cmsBondIssueInfo.getId());
        resultMap.put("costVOS", costList);
        return AjaxJson.success(resultMap);
    }

    /**
     * 债券发行审批列表数据
     */
    @ApiOperation("债券发行审批列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> datatableInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondIssueInfoService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 债券发行审批详情
     */
    @ApiOperation("债券发行审批详情")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("taskId", taskId);
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(businessId, true);
        CorpDTO corpDTO = corpService.get(cmsBondRegisterInfoService.get(cmsBondIssueInfo.getRegisterId()).getCorpId());
        resultMap.put("corp", corpDTO);

        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsBondIssueInfo.getFixPriceBase());
        if (null != tbBaseRate) {
            resultMap.put("tbBaseRate", tbBaseRate);
        }
        if (StringUtils.isNotEmpty(cmsBondIssueInfo.getRateDeadline())) {
            TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsBondIssueInfo.getRateDeadline());
            if (null != tbCreditPeriod) {
                resultMap.put("tbCreditPeriod", tbCreditPeriod);
            }
        }

        Map<String, Object> bondParamMap = new HashMap<>();
        BondUtils.setBondParam(bondParamMap, "1", cmsBondIssueInfo, fcBondHookService);
        resultMap.putAll(bondParamMap);

        Map<String, Object> guaParamMap = new HashMap<>();
        BondUtils.setGuaParams(guaParamMap, "1", "1", cmsBondIssueInfo, guaContractOccupyService);
        resultMap.putAll(guaParamMap);

        Map<String, Object> pledgeParamMap = new HashMap<>();
        BondUtils.setPledgeParam(pledgeParamMap, "1", cmsBondIssueInfo, pledgeOccupyService);
        resultMap.putAll(pledgeParamMap);

        List<DfIntermediateCostVO> costList = cmsBondIssueInfoService.listIntermediateCostByDfId(cmsBondIssueInfo.getId());
        resultMap.put("costVOS", costList);
        resultMap.put("issueInfo", cmsBondIssueInfo);

        String issueScope = cmsBondIssueInfo.getIssueScope();
        if (StringUtils.isNotEmpty(issueScope)) {
            List<String> issueScopeList = Arrays.asList(issueScope.split(CommonConstant.COMMA_SPLIT));
            resultMap.put("issueScopeList", issueScopeList);
        }
        return AjaxJson.success(resultMap);
    }

    /**
     * 查询债券发行信息详情
     */
    @LogMessage(content = "查询债券发行信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券发行信息详情查看")
    @GetMapping("/view/{id}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        CmsBondIssueInfo cmsBondIssueInfo = cmsBondIssueInfoService.get(id, true);
        resultMap.put("issueInfo", cmsBondIssueInfo);
        CmsBondRegisterInfoVo registerInfo = cmsBondRegisterInfoService.getInfoById(cmsBondIssueInfo.getRegisterId());
        CorpDTO corpDTO = corpService.get(registerInfo.getCorpId());
        resultMap.put("corp", corpDTO);
        resultMap.put("registerInfo", registerInfo);

        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsBondIssueInfo.getFixPriceBase());
        Optional.ofNullable(tbBaseRate).ifPresent(baseRate -> resultMap.put("tbBaseRate", baseRate));
        if (StringUtils.isNotEmpty(cmsBondIssueInfo.getRateDeadline())) {
            TbCreditPeriodDTO tbCreditPeriod = iCreditPeriodService.getById(cmsBondIssueInfo.getRateDeadline());
            Optional.ofNullable(tbCreditPeriod).ifPresent(creditPeriod -> resultMap.put("tbCreditPeriod", creditPeriod));
        }

        Map<String, Object> bondParamMap = new HashMap<>();
        BondUtils.setBondParam(bondParamMap, "1", cmsBondIssueInfo, fcBondHookService);
        resultMap.putAll(bondParamMap);

        Map<String, Object> guaParamMap = new HashMap<>();
        BondUtils.setGuaParams(guaParamMap, "1", "1", cmsBondIssueInfo, guaContractOccupyService);
        resultMap.putAll(guaParamMap);

        Map<String, Object> pledgeParamMap = new HashMap<>();
        BondUtils.setPledgeParam(pledgeParamMap, "1", cmsBondIssueInfo, pledgeOccupyService);
        resultMap.putAll(pledgeParamMap);

        List<DfIntermediateCostVO> costList = cmsBondIssueInfoService.listIntermediateCostByDfId(cmsBondIssueInfo.getId());
        resultMap.put("costVOS", costList);
        CmsBondIssueInfo issueInfo = cmsBondIssueInfoService.get(id);
        resultMap.put("issueInfo", issueInfo);

        String issueScope = issueInfo.getIssueScope();
        if (StringUtils.isNotEmpty(issueScope)) {
            List<String> issueScopeList = Arrays.asList(issueScope.split(CommonConstant.COMMA_SPLIT));
            resultMap.put("issueScopeList", issueScopeList);
        }
        return AjaxJson.success(resultMap);
    }

    /**
     * 债券发行明细列表初始化
     */
    @LogMessage(content = "债券发行明细详情列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券发行明细页面初始化")
    @GetMapping("/issueDtl/{id}")
    public AjaxJson<Map<String, Object>> issueDtl(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("registerId", id);
        return AjaxJson.success(resultMap);
    }

    /**
     * 获取债券发行信息列表数据
     */
    @ApiOperation("债券发行主列表数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondIssueInfoService.getDataTable(dataInput, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 获取债券发行查询信息列表数据
     */
    @ApiOperation("债券发行查询列表数据")
    @PostMapping("/queryDatatable")
    public AjaxJson<DataTablesOutput> queryDatatable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondIssueInfoService.getQueryDataTable(dataInput, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 获取债券发行详情列表数据
     */
    @ApiOperation("债券发行明细列表数据")
    @PostMapping("/issueDtlDatatable")
    public AjaxJson<DataTablesOutput> issueDtlDatatable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondIssueInfoService.getIssueDtlDatatable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 新增债券发行信息
     */
    @LogMessage(content = "新增债券发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("新增债券发行信息")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsBondIssueInfoVo cmsBondIssueInfo) {
        cmsBondIssueInfoService.saveBondIssueInfo(cmsBondIssueInfo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改债券发行信息
     */
    @LogMessage(content = "修改债券发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("修改债券发行信息")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody CmsBondIssueInfoVo cmsBondIssueInfo) {
        cmsBondIssueInfoService.updateBondIssueInfo(cmsBondIssueInfo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除债券发行信息
     */
    @LogMessage(content = "删除债券发行信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("删除债券发行信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsBondIssueInfoService.deleteBondIssueInfo(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 根据债券注册Id获取有效的债券发行编号
     */
    @ApiOperation("根据注册ID查询有效发行编号")
    @GetMapping("/listValidBondIssueNum/{registerId}")
    public AjaxJson<List<CmsBondIssueInfoVo>> listValidBondIssueNum(@PathVariable("registerId") String registerId) {
        List<CmsBondIssueInfoVo> list = cmsBondIssueInfoService.listValidBondIssueNum(registerId);
        return AjaxJson.success(list);
    }

    /**
     * 债券还款计划新增页面初始化数据
     */
    @ApiOperation("债券还款计划新增初始化数据")
    @GetMapping("/addPlan")
    public AjaxJson<Map<String, Object>> addPlan(CmsBondIssueInfoVo issueInfoVo) {
        Map<String, Object> resultMap = new HashMap<>();
        String registerId = issueInfoVo.getRegisterId();
        if (StringUtils.isNotEmpty(registerId)) {
            CmsBondRegisterInfoVo cmsBondRegisterInfoVo = cmsBondRegisterInfoService.getRegisterById(registerId);
            resultMap.put("cmsBondRegisterInfoVo", cmsBondRegisterInfoVo);
            if (cmsBondRegisterInfoVo != null) {
                CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByRegisterId(registerId);
                resultMap.put("cmsBondIssueInfoVo", cmsBondIssueInfoVo);
                CmsBondRepayPlan planVO = cmsBondRepayPlanService.get(issueInfoVo.getPlanId(), true);
                if (planVO != null) {
                    planVO.setRepayType(CmsConstant.REPAY_TYPE_07);
                    resultMap.put("cmsBondRepayPlanVo", planVO);
                }
            }
        }
        resultMap.put("issueInfo", issueInfoVo);
        return AjaxJson.success(resultMap);
    }

    /**
     * 加载还款计划明细
     */
    @ApiOperation("根据planId查询还款计划明细")
    @PostMapping("/listPlanDtl")
    public AjaxJson<List<CmsBondRepayPlanDtlVo>> listPlanDtlByRegisterId(String planId) {
        List<CmsBondRepayPlanDtlVo> list = cmsBondRepayPlanDtlService.getByPlanId(planId);
        return AjaxJson.success(list);
    }

    /**
     * 保存债券发行还款计划数据
     */
    @LogMessage(content = "保存债券发行还款计划数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存/更新债券还款计划")
    @PostMapping("/savePlan/{operator}")
    public AjaxJson<?> saveOrUpdatePlan(@PathVariable("operator") String operator,@RequestBody CmsBondRepayPlanVo cmsBondRepayPlanVo) {
        Object obj = cmsBondRepayPlanService.issueSaveOrUpdate(operator, cmsBondRepayPlanVo, getCurrentUser());
        return AjaxJson.success(obj);
    }

    /**
     * 根据发行ID获取债券发行信息
     */
    @ApiOperation("根据发行ID查询债券发行信息")
    @GetMapping("/getInfo/{issueId}")
    public AjaxJson<CmsBondIssueInfoVo> getByRegisterId(@PathVariable("issueId") String issueId) {
        CmsBondIssueInfoVo cmsBondIssueInfoVo = cmsBondIssueInfoService.getByIssueId(issueId);
        BigDecimal sendBalance = cmsBondPayService.completeSendBalance(cmsBondIssueInfoVo.getId(), null);
        cmsBondIssueInfoVo.setUnpaidMoney(sendBalance);
        return AjaxJson.success(cmsBondIssueInfoVo);
    }
}