package com.bytter.finance.saas.controller.precative;

import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.TreeUtil;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.PrecativeConstant;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.vo.*;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.BtPurposeConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtPurposeDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款信息表
 * @date 2017-07-20 14:00:39
 */
@Api(value = "/v1-cms/cms/precative/loaninfo", tags = "委托贷款信息表")
@RestController("v1CmsPrecativeLoanInfoController")
@RequestMapping("/v1-cms/cms/precative/loaninfo")
public class CmsPrecativeLoanInfoController extends BaseController {
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService iCorpService;

    /**
     * 录入页面数据
     */
    @LogMessage(content = "查询委托贷款信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("获取委托贷款录入页面数据")
    @GetMapping("/list")
    public AjaxJson<CmsPrecativeLoanInfoPageVo> list() {
        CmsPrecativeLoanInfoPageVo result = new CmsPrecativeLoanInfoPageVo();
        String isBudget = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET)
                .getValue();
        result.setBudgetSel(isBudget);
        if (EmptyUtils.isBlank(configService
                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue())) {
            result.setEndorseItem("400");
        } else {
            result.setEndorseItem(configService
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue());
        }
        result.setOpDate(new Date());
        result.setType(FinacialManualConstant.MANUAL_STATUS_INIT);
        if (StringUtils.isNotEmpty(isBudget) && isBudget.equals(CmsConstant.IS_TRUE)) {
            Map<String, Object> config = configService.listByType(ConfigConstant.FBS);
            Map<String, Object> map = (Map<String, Object>) config.get("fbs");
            String timeType = (String) map.get("timeType");
            if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstYearDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastYearDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstQuarterDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastQuarterDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstMonthDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastMonthDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstWeekDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastWeekDate(new Date()));
            } else {
                result.setPlanStartTime(new Date());
                result.setPlanEndTime(new Date());
            }
        }
        return success(result);
    }


    /**
     * 录入提交
     */
    @ApiOperation("提交委托贷款录入")
    @PostMapping("/submit")
    public AjaxJson submit(@RequestBody CmsPrecativeLoanInfoVo pre) {
        if (StringUtils.isEmpty(pre.getId())) {
            String billCode = cmsPrecativeLoanInfoService.getPrecativeBillCode(PrecativeConstant.INFO_BILL_CODE_PREFIX);
            pre.setBillCode(billCode);
        }
        cmsPrecativeLoanInfoService.submit(pre, pre.getSaveType(), getCurrentUser());
        return success();
    }

    /**
     * 修改、打回数据查询
     */
    @ApiOperation("查询委托贷款修改/打回列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput, String type) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsPrecativeLoanInfoService.queryChangeLoanInfo(input, type, getUserId()));
    }

    /**
     * 修改/打回页面数据
     */
    @ApiOperation("获取修改/打回页面数据")
    @GetMapping("/update")
    public AjaxJson<CmsPrecativeLoanInfoEditVo> updatePage(CmsPrecativeLoanInfoVo pre) {
        CmsPrecativeLoanInfoEditVo result = new CmsPrecativeLoanInfoEditVo();
        if (StringUtils.isNotEmpty(pre.getBillCode())) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(pre.getBillCode());
            result.setPrecativePage(preVo);
            if (EmptyUtils.isBlank(configService
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue())) {
                result.setEndorseItem("400");
            } else {
                result.setEndorseItem(
                        configService
                                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL)
                                .getValue());
            }
            result.setBusinessName(BudgetConstant.BUSINESS_NAME_PRECATIVE);
            result.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);
        }
        String isBudget = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET)
                .getValue();
        result.setBudgetSel(isBudget);
        if (StringUtils.isNotEmpty(isBudget) && isBudget.equals(CmsConstant.IS_TRUE)) {
            Map<String, Object> config = configService.listByType(ConfigConstant.FBS);
            Map<String, Object> map = (Map<String, Object>) config.get("fbs");
            String timeType = (String) map.get("timeType");
            if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstYearDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastYearDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstQuarterDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastQuarterDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstMonthDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastMonthDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
                result.setPlanStartTime(DateUtils.getFirstWeekDate(new Date()));
                result.setPlanEndTime(DateUtils.getLastWeekDate(new Date()));
            } else {
                result.setPlanStartTime(new Date());
                result.setPlanEndTime(new Date());
            }
        }
        return success(result);
    }

    /**
     * 修改/暂存数据
     */
    @ApiOperation("修改委托贷款数据")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody CmsPrecativeLoanInfoVo pre) {
        cmsPrecativeLoanInfoService.submit(pre, "", getCurrentUser());
        return success();
    }

    /**
     * 删除修改列表数据
     */
    @LogMessage(content = "删除委托贷款数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("删除委托贷款数据")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            cmsPrecativeLoanInfoService.deleteCmsEntity(id, getUserId());
        }
        return success();
    }

    /**
     * 详情页
     */
    @ApiOperation("查询委托贷款详情")
    @GetMapping("/view")
    public AjaxJson<CmsPrecativeLoanInfoViewVo> view(String billCode, String type, String loan, String status) {
        CmsPrecativeLoanInfoViewVo result = new CmsPrecativeLoanInfoViewVo();
        if (EmptyUtils.isEmpty(billCode)) {
            return success(result);
        }
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(billCode);
        String isBudget = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET)
                .getValue();
        result.setBudgetSel(isBudget);
        result.setInfoMoneyUpper(StringUtils.chineseMoney(preVo.getMoney()));
        result.setOpDate(preVo.getCreateDate());
        result.setPrecativePage(preVo);
        result.setOpType(type);
        result.setStatus(status);
        if (EmptyUtils.isNotEmpty(loan)) {
            if ("entry".equals(loan)) {
                if (EmptyUtils.isBlank(configService
                        .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL)
                        .getValue())) {
                    result.setEndorseItem("400");
                } else {
                    result.setEndorseItem(configService
                            .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL)
                            .getValue());
                }
                result.setBusinessName(BudgetConstant.BUSINESS_NAME_PRECATIVE);
            } else if ("repair".equals(loan)) {
                if (EmptyUtils.isBlank(configService
                        .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL)
                        .getValue())) {
                    result.setEndorseItem("200;600");
                } else {
                    result.setEndorseItem(configService
                            .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL)
                            .getValue());
                }
                result.setBusinessName(BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION);
            }
            result.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);
        }
        result.setDataType(loan);
        return success(result);
    }

    /**
     * 补录页面数据
     */
    @ApiOperation("获取补录页面数据")
    @GetMapping("/repair")
    public AjaxJson<CmsPrecativeLoanInfoRepairVo> repair(CmsPrecativeLoanInfoVo pre) {
        CmsPrecativeLoanInfoRepairVo result = new CmsPrecativeLoanInfoRepairVo();
        if (StringUtils.isNotEmpty(pre.getBillCode())) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(pre.getBillCode());
            preVo.setMoney(new BigDecimal(preVo.getMoney().doubleValue()));
            boolean isNatualCurrency = cmsPrecativeLoanSendService.isNatualCurrency(preVo.getCurId(), preVo.getCorpId());
            if (EmptyUtils.isBlank(configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL).getValue())) {
                result.setEndorseItem("200;600");
            } else {
                result.setEndorseItem(configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL).getValue());
            }
            result.setBusinessName(BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION);
            result.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);
            result.setOpDate(preVo.getCreateDate());
            result.setPrecativePage(preVo);
            result.setIsNatualCurrency(isNatualCurrency);
            String relateCon = cmsPrecativeLoanInfoService.relateCon();
            if (CmsConstant.RELATED_CONTRACT_YES.equals(relateCon)) {
                String conType = cmsPrecativeLoanInfoService.conType();
                result.setConType(conType);
                result.setConId(preVo.getConId());
            }
            result.setRelateCon(relateCon);
        }
        return success(result);
    }

    /**
     * 补录打回
     */
    @LogMessage(content = "打回委托贷款补录数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("打回委托贷款补录数据")
    @PostMapping("/back")
    public AjaxJson back(String id, String refuseWhy) {
        cmsPrecativeLoanInfoService.repairBack(id, refuseWhy, getCurrentUser());
        return success();
    }

    /**
     * 补录提交
     */
    @LogMessage(content = "提交委托贷款补录数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("提交委托贷款补录数据")
    @PostMapping("/repairSubmit")
    public AjaxJson repairSubmit(@RequestBody CmsPrecativeLoanInfoVo pre) {
        cmsPrecativeLoanInfoService.repairSubmit(pre, getCurrentUser());
        return success();
    }

    /**
     * 补录获取利率
     */
    @LogMessage(content = "补录获取利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @ApiOperation("补录获取利率")
    @GetMapping("/getRate")
    public AjaxJson<LoanRateMaintenceVO> getRate(String plDate, String referenceRateType, String curId, String repayCycle) {
        LoanRateMaintenceVO rate = cmsPrecativeLoanInfoService.getRate(plDate, referenceRateType, curId, repayCycle);
        return success(rate);
    }

    /**
     * 审核列表页面数据
     */
    @ApiOperation("获取审核列表页面数据")
    @GetMapping("/listApprove")
    public AjaxJson<CmsPrecativeLoanApproveListVo> listApprove(String flowStatus, String cycleTime) {
        CmsPrecativeLoanApproveListVo result = new CmsPrecativeLoanApproveListVo();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        return success(result);
    }


    /**
     * 手机审核页面数据
     */
    @ApiOperation("获取手机审核页面数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<CmsPrecativeLoanMobileApproveVo> mobileApprove(String businessId, String taskId) {
        CmsPrecativeLoanMobileApproveVo result = new CmsPrecativeLoanMobileApproveVo();
        if (StringUtils.isNotEmpty(businessId)) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.getLoanInfoById(businessId);
            result.setPrecativePage(preVo);
            result.setLoanMoneyUpper(StringUtils.chineseMoney(preVo.getMoney()));
            result.setTaskId(taskId);
            result.setPageType("add");
        }
        return success(result);
    }

    /**
     * 手机补录审核页面数据
     */
    @ApiOperation("获取手机补录审核页面数据")
    @GetMapping("/mobileRepairApprove")
    public AjaxJson<CmsPrecativeLoanMobileApproveVo> mobileRepairApprove(String businessId, String taskId) {
        CmsPrecativeLoanMobileApproveVo result = new CmsPrecativeLoanMobileApproveVo();
        if (StringUtils.isNotEmpty(businessId)) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.getLoanInfoById(businessId);
            result.setPrecativePage(preVo);
            result.setLoanMoneyUpper(StringUtils.chineseMoney(preVo.getMoney()));
            result.setTaskId(taskId);
            result.setPageType("repair");
        }
        return success(result);
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询审核列表")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput, String type) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(cmsPrecativeLoanInfoService.dataTableApprove(input, type));
    }

    /**
     * 审核详情页面数据
     */
    @ApiOperation("查询审核详情")
    @GetMapping("/approve")
    public AjaxJson<CmsPrecativeLoanApproveDetailVo> approve(String businessId, String taskId, String type) {
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(businessId);
        CmsPrecativeLoanApproveDetailVo result = new CmsPrecativeLoanApproveDetailVo();
        result.setOpDate(preVo.getCreateDate());
        result.setPrecativePage(preVo);
        result.setTaskId(taskId);
        result.setViewType("repair".equals(type) ? "repair" : "info");
        return success(result);
    }

    /**
     * 补录审核列表页面数据
     */
    @ApiOperation("获取补录审核列表页面数据")
    @GetMapping("/listRepairApprove")
    public AjaxJson<CmsPrecativeLoanApproveListVo> listRepairApprove(String flowStatus, String cycleTime) {
        CmsPrecativeLoanApproveListVo result = new CmsPrecativeLoanApproveListVo();
        result.setFlowStatus(flowStatus);
        result.setCycleTime(cycleTime);
        result.setOpType("repair");
        return success(result);
    }

    /**
     * 根据单位ID和业务类型判断是否挂预算
     */
    @ApiOperation("判断是否挂预算")
    @GetMapping("/isBudget")
    public AjaxJson<Boolean> isBudget(String corpId, String briefId) {
        boolean isBudget = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET)
                .toBoolean();
        if (isBudget) {
            isBudget = cmsPrecativeLoanInfoService.isBudget(corpId, briefId);
        }
        return success(isBudget);
    }

    @ApiOperation("获取用途列表")
    @GetMapping("/getPurpose")
    public AjaxJson<List<BtPurposeDTO>> listByCorpIdOrBusType(String corpId, String sysBusType) {
        if (EmptyUtils.isEmpty(corpId) || "null".equals(corpId)) {
            return success(new ArrayList<>());
        }
        List<BtPurposeDTO> list = cmsPrecativeLoanInfoService.listByCorpIdOrBusType(corpId,
                BtPurposeConstant.SYS_BUS_TYPE_CMS);
        return success(EmptyUtils.isEmptyList(list) ? new ArrayList<>() : list);
    }

    /**
     * 委托人和借款人有权限的单位合集
     */
    @ApiOperation("获取有权限的单位合集")
    @GetMapping("/getLoanOpp")
    public AjaxJson<List<CorpDTO>> getCorps(String precativeId) {
        if (EmptyUtils.isEmpty(precativeId)) {
            return success(null);
        }
        CorpDTO pre = iCorpService.get(precativeId);
        String preNetId = pre.getNetId();
        if (EmptyUtils.isEmpty(preNetId)) {
            preNetId = pre.getId();
        }
        List<CorpDTO> corpDTOs = iCorpService.listUnderCorpByNetId(preNetId);
        return success(TreeUtil.getTreeNodeList(corpDTOs, Comparator.comparing(CorpDTO::getSort).thenComparing(CorpDTO::getCode)));
    }

    /**
     * 委托人和借款人有权限的单位合集
     */
    @ApiOperation("获取单位网络ID")
    @GetMapping("/getCorp")
    public AjaxJson<String> getCorp(String precativeId) {
        if (EmptyUtils.isEmpty(precativeId)) {
            return success(null);
        }
        CorpDTO pre = iCorpService.getNetByCorpId(precativeId);
        return success(pre.getId());
    }
}
