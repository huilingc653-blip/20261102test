package com.bytter.finance.saas.controller.directFinance;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
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
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.DfConstant;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceInfo;
import com.bytter.finance.domain.directFinance.service.DfDirectFinanceInfoService;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceInfoVO;
import com.bytter.finance.domain.directFinance.vo.DfDirectFinanceRepayTmpVO;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.directFinance.vo.DfThirdPartyAgencyVO;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-03 10:37
 * @Version 1.0
 */
@Api(value = "/v1-preposebill/cms/dfDirectFinanceInfo", tags = "直融融资信息")
@RestController("v1DfDirectFinanceInfoController")
@RequestMapping("/v1-preposebill/cms/dfDirectFinanceInfo")
public class DfDirectFinanceInfoController extends BaseController {

    @Autowired
    private DfDirectFinanceInfoService dfDirectFinanceInfoService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;

    /**
     * 直融融资信息列表查询
     */
    @LogMessage(content = "查询直融融资信息列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询直融融资信息分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = dfDirectFinanceInfoService.listDfDirectFinanceInfo(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 直融融资信息审核列表查询
     */
    @LogMessage(content = "查询直融融资信息审核列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approve/datatable")
    @ApiOperation("查询直融融资信息审核分页列表")
    public AjaxJson<DataTablesOutput> approveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = dfDirectFinanceInfoService.listDfDirectFinanceInfoApprove(dataInput, getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 查询直融融资信息详情
     */
    @LogMessage(content = "查询直融融资信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询直融融资信息详情")
    public AjaxJson<Map<String, Object>> getById(@PathVariable("id") String id) {
        Map<String, Object> dataMap = new HashMap<>();
        DfDirectFinanceInfoVO vo = dfDirectFinanceInfoService.getById(id);
        dataMap.put("dfDirectFinanceInfoVO", vo);
        // 查询关联的第三方机构、中间费用、还款计划
        Map<String, Object> params = new HashMap<>();
        params.put("directFinanceId", id);
        List<DfThirdPartyAgencyVO> thirdPartyAgencyList = dfDirectFinanceInfoService.listThirdPartyAgencyByDfId(params);
        List<DfIntermediateCostVO> intermediateCostList = dfDirectFinanceInfoService.listIntermediateCostByDfId(params);
        List<DfDirectFinanceRepayTmpVO> repayTmpList = dfDirectFinanceInfoService.listDirectFinanceRepayTmpByDfId(params);
        dataMap.put("thirdPartyAgencyList", thirdPartyAgencyList);
        dataMap.put("intermediateCostList", intermediateCostList);
        dataMap.put("repayTmpList", repayTmpList);
        // 查询资金计划处理日期
        if (vo != null && vo.getBillCode() != null) {
            List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                    BudgetConstant.BUSINESS_NAME_DF_INFO, BudgetConstant.PROCESS_SYSNAME_CMS,
                    vo.getBillCode());
            if (!EmptyUtils.isEmptyList(list)) {
                dataMap.put("processDate", DateUtils.format(list.get(0).getProcessDate()));
            }
        }
        // 资金计划系统参数
        dataMap.put("isBudget", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_DF_BUDGET).getValue());
        dataMap.put("dfInfoItem", configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.DIRECT_FINANCE_INFO_BUDGET).getValue());
        dataMap.put("businessName", BudgetConstant.BUSINESS_NAME_DF_INFO);
        dataMap.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        return AjaxJson.success(dataMap);
    }

    /**
     * 保存直融融资信息
     */
    @LogMessage(content = "保存直融融资信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("保存直融融资信息")
    public AjaxJson save(@RequestBody DfDirectFinanceInfoVO dfDirectFinanceInfoVO) {
        dfDirectFinanceInfoService.saveDfDirectFinanceInfo(dfDirectFinanceInfoVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除直融融资信息
     */
    @LogMessage(content = "删除直融融资信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除直融融资信息")
    public AjaxJson delete(@PathVariable("id") String id) {
        DfDirectFinanceInfo dfDirectFinanceInfo = dfDirectFinanceInfoService.get(id);
        dfDirectFinanceInfo.setStatus(DfConstant.DELETED);
        dfDirectFinanceInfoService.updateEntity(dfDirectFinanceInfo);
        // 删除资金计划
        dfDirectFinanceInfoService.deleteBudget(dfDirectFinanceInfo.getBillCode());
        return AjaxJson.success();
    }

    /**
     * 直融融资报表查询
     */
    @LogMessage(content = "查询直融融资报表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/report/datatable")
    @ApiOperation("查询直融融资报表")
    public AjaxJson<DataTablesOutput> reportDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = dfDirectFinanceInfoService.listDfDirectFinanceReport(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 手机端审核详情查询
     */
    @LogMessage(content = "查询直融融资信息手机端审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/mobileApprove/{businessId}")
    @ApiOperation("查询直融融资信息手机端审核详情")
    public AjaxJson<Map<String, Object>> mobileApprove(@PathVariable("businessId") String businessId) {
        Map<String, Object> dataMap = new HashMap<>();
        DfDirectFinanceInfoVO vo = dfDirectFinanceInfoService.getById(businessId);
        dataMap.put("dfDirectFinanceInfoVO", vo);
        // 查询关联的第三方机构、中间费用、还款计划
        Map<String, Object> params = new HashMap<>();
        params.put("directFinanceId", businessId);
        List<DfThirdPartyAgencyVO> thirdPartyAgencyList = dfDirectFinanceInfoService.listThirdPartyAgencyByDfId(params);
        List<DfIntermediateCostVO> intermediateCostList = dfDirectFinanceInfoService.listIntermediateCostByDfId(params);
        List<DfDirectFinanceRepayTmpVO> repayTmpList = dfDirectFinanceInfoService.listDirectFinanceRepayTmpByDfId(params);
        dataMap.put("thirdPartyAgencyList", thirdPartyAgencyList);
        dataMap.put("intermediateCostList", intermediateCostList);
        dataMap.put("repayTmpList", repayTmpList);
        return AjaxJson.success(dataMap);
    }

}