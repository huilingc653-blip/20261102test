package com.bytter.finance.web.directFinance.controller;


import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.DfConstant;
import com.bytter.finance.domain.directFinance.entity.DfDirectFinanceRepay;
import com.bytter.finance.domain.directFinance.service.DfDirectFinanceInfoService;
import com.bytter.finance.domain.directFinance.service.DfDirectFinanceRepayService;
import com.bytter.finance.domain.directFinance.vo.*;
import com.bytter.system.api.IConfigService;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2019-12-13 15:05
 * @Version 1.0
 */
@Controller
@RequestMapping("/cms/dfDirectFinanceRepay")
public class DfDirectFinanceRepayController extends BaseController {

    @Autowired
    private DfDirectFinanceRepayService dfDirectFinanceRepayService;

    @Autowired
    private DfDirectFinanceInfoService dfDirectFinanceInfoService;

    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;

    /**
     * 还款申请列表
     *
     * @return
     */
    @GetMapping("/listDfDirectFinanceRepay")
    public String listDfDirectFinanceRepay() {
        return "cms/directFinance/dfDirectFinanceRepay/listDfDirectFinanceRepay";
    }

    /**
     * 还款信息查询
     *
     * @param input
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput listDfDirectFinanceRepay(@RequestBody DataTablesInput input) {
        return dfDirectFinanceRepayService.listDfDirectFinanceRepay(input, getCurrentUser());
    }

    @GetMapping("/add")
    public String add(String id, Model model) {
        DfDirectFinanceInfoVO dfDirectFinanceInfoVO = dfDirectFinanceInfoService.getById(id);
        model.addAttribute("dfDirectFinanceInfoVO", dfDirectFinanceInfoVO);
        model.addAttribute("allRepayMoney", dfDirectFinanceRepayService.getAllRepayMoney(id));
        setInfo(model, id);
        initConfig(model);
        return "cms/directFinance/dfDirectFinanceRepay/addDfDirectFinanceRepay";
    }

    @GetMapping("/edit")
    public String edit(String id, Model model) {
        DfDirectFinanceRepayVO dfDirectFinanceRepayVO = dfDirectFinanceRepayService.getById(id);
        DfDirectFinanceInfoVO dfDirectFinanceInfoVO = dfDirectFinanceInfoService
                .getById(dfDirectFinanceRepayVO.getDirectFinanceId());
        model.addAttribute("dfDirectFinanceInfoVO", dfDirectFinanceInfoVO);
        model.addAttribute("allRepayMoney", dfDirectFinanceRepayService.getAllRepayMoney(id));
        model.addAttribute("dfDirectFinanceRepayVO", dfDirectFinanceRepayVO);
        setInfo(model, dfDirectFinanceRepayVO.getDirectFinanceId());
        initConfig(model);
        return "cms/directFinance/dfDirectFinanceRepay/editDfDirectFinanceRepay";
    }

    /**
     * 暂存/保存
     *
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(DfDirectFinanceRepayVO dfDirectFinanceRepayVO, Model model) {
        dfDirectFinanceRepayService.saveDfDirectFinanceRepay(dfDirectFinanceRepayVO, getCurrentUser());
        return success();
    }

    /**
     * 数据设置
     *
     * @param model
     */
    private void setInfo(Model model, String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("directFinanceId", id);
        List<DfThirdPartyAgencyVO> dfThirdPartyAgencyVOS = dfDirectFinanceInfoService
                .listThirdPartyAgencyByDfId(params);
        List<DfIntermediateCostVO> dfIntermediateCostVOS = dfDirectFinanceInfoService
                .listIntermediateCostByDfId(params);
        List<DfDirectFinanceRepayTmpVO> dfDirectFinanceRepayTmpVOS = dfDirectFinanceInfoService
                .listDirectFinanceRepayTmpByDfId(params);
        if (null != dfThirdPartyAgencyVOS && dfThirdPartyAgencyVOS.size() > 0) {
            model.addAttribute("dfThirdPartyAgencyVOS", dfThirdPartyAgencyVOS);
        }
        if (null != dfIntermediateCostVOS && dfIntermediateCostVOS.size() > 0) {
            model.addAttribute("dfIntermediateCostVOS", dfDirectFinanceInfoService.listIntermediateCostByDfId(params));
        }
        if (null != dfDirectFinanceRepayTmpVOS && dfDirectFinanceRepayTmpVOS.size() > 0) {
            model.addAttribute("dfDirectFinanceRepayTmpVOS",
                    dfDirectFinanceInfoService.listDirectFinanceRepayTmpByDfId(params));
        }
    }

    /**
     * 编辑打回列表
     *
     * @param model
     * @return
     */
    @GetMapping("/listTempOrback")
    public String listTempOrback(Model model) {
        return "cms/directFinance/dfDirectFinanceRepay/listUpdateDfDirectFinanceRepay";
    }

    /**
     * 还款信息查询
     *
     * @param input
     * @return
     */
    @PostMapping("/TempOrBackDataTable")
    @ResponseBody
    public DataTablesOutput TempOrBackDataTable(@RequestBody DataTablesInput input) {
        return dfDirectFinanceRepayService.listTempOrback(input, getCurrentUser());
    }

    /**
     * 审批列表
     *
     * @param flowStatus
     * @param model
     * @return
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/directFinance/dfDirectFinanceRepay/listDfApproveDfDirectFinanceRepay";
    }

    /**
     * 还款信息查询
     *
     * @param input
     * @return
     */
    @PostMapping("/ApproveDataTable")
    @ResponseBody
    public DataTablesOutput ApproveDataTable(@RequestBody DataTablesInput input) {
        return dfDirectFinanceRepayService.listApprove(input, getCurrentUser());
    }

    /**
     * 查看页面跳转
     *
     * @return
     */
    @GetMapping("/viewApprove")
    public String viewApprove(String id, String taskId, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("taskId", taskId);
        return "cms/directFinance/dfDirectFinanceRepay/viewApproveDfdirectfinanceRepay";
    }

    /**
     * 查看页面跳转
     *
     * @return
     */
    @GetMapping("/view")
    public String view(String id, @RequestParam(required = false) String flag, Model model) {
        DfDirectFinanceRepayVO dfDirectFinanceRepayVO = dfDirectFinanceRepayService.getById(id);
        DfDirectFinanceInfoVO dfDirectFinanceInfoVO = dfDirectFinanceInfoService
                .getById(dfDirectFinanceRepayVO.getDirectFinanceId());
        model.addAttribute("dfDirectFinanceInfoVO", dfDirectFinanceInfoVO);
        model.addAttribute("dfDirectFinanceRepayVO", dfDirectFinanceRepayVO);
        model.addAttribute("allRepayMoney",
                dfDirectFinanceRepayService.getAllRepayMoney(dfDirectFinanceInfoVO.getId()));
        /**
         * 0/null隐藏，1显示
         */
        model.addAttribute("showClosBtn", flag);
        setInfo(model, dfDirectFinanceInfoVO.getId());
        initConfig(model);
        return "cms/directFinance/dfDirectFinanceRepay/viewDfDirectFinanceRepay";
    }

    /**
     * 查看已还金额
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewReapyMoney")
    public String viewRepayMoney(String id, Model model) {
        model.addAttribute("DfDirectFinanceRepayVOS", dfDirectFinanceRepayService.listRepaidMoney(id));
        return "cms/directFinance/dfDirectFinanceRepay/viewRepayMoney";
    }

    /**
     * 初始化资金计划系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        // 0为不挂接资金计划 现在需求要求不挂接
        model.addAttribute("isBudget",
                configService.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_DF_BUDGET).getValue());
        model.addAttribute("dfRepayItem",
                configService
                        .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.DIRECT_FINANCE_REPAY_BUDGET)
                        .getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_DF_REPAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
    }

    /**
     * 删除还款（逻辑删除）
     */
    @LogMessage(content = "删除直融还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ApiOperation("删除直融还款")
    public AjaxJson delete(@PathVariable("id") String id) {
        DfDirectFinanceRepay dfDirectFinanceRepay = dfDirectFinanceRepayService.get(id);
        dfDirectFinanceRepay.setStatus(DfConstant.DELETED);
        dfDirectFinanceRepayService.updateEntity(dfDirectFinanceRepay);
        // 删除资金计划
        dfDirectFinanceRepayService.deleteBudget(dfDirectFinanceRepay.getBillCode());
        return AjaxJson.success();
    }

    /**
     * 新增校验
     */
    @LogMessage(content = "直融还款新增校验", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/addCheck")
    @ApiOperation("直融还款新增校验")
    public AjaxJson addCheck(@RequestParam String directFinanceId) {
        dfDirectFinanceRepayService.addCheck(directFinanceId);
        return AjaxJson.success();
    }

    /**
     * 手机端审核详情查询
     */
    @LogMessage(content = "查询直融还款手机端审核详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/mobileApprove/{businessId}")
    @ApiOperation("查询直融还款手机端审核详情")
    public AjaxJson<Map<String, Object>> mobileApprove(@PathVariable("businessId") String businessId) {
        Map<String, Object> dataMap = new HashMap<>();
        DfDirectFinanceRepayVO repayVO = dfDirectFinanceRepayService.getById(businessId);
        DfDirectFinanceInfoVO infoVO = dfDirectFinanceInfoService.getById(repayVO.getDirectFinanceId());
        dataMap.put("dfDirectFinanceRepayVO", repayVO);
        dataMap.put("dfDirectFinanceInfoVO", infoVO);
        dataMap.put("allRepayMoney", dfDirectFinanceRepayService.getAllRepayMoney(infoVO.getId()));
        // 查询关联数据
        Map<String, Object> params = new HashMap<>();
        params.put("directFinanceId", infoVO.getId());
        List<DfThirdPartyAgencyVO> thirdPartyAgencyList = dfDirectFinanceInfoService.listThirdPartyAgencyByDfId(params);
        List<DfIntermediateCostVO> intermediateCostList = dfDirectFinanceInfoService.listIntermediateCostByDfId(params);
        List<DfDirectFinanceRepayTmpVO> repayTmpList = dfDirectFinanceInfoService.listDirectFinanceRepayTmpByDfId(params);
        dataMap.put("thirdPartyAgencyList", thirdPartyAgencyList);
        dataMap.put("intermediateCostList", intermediateCostList);
        dataMap.put("repayTmpList", repayTmpList);
        return AjaxJson.success(dataMap);
    }

}