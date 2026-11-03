package com.bytter.finance.web.directFinance.controller;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
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
 * @Date 2019-12-03 10:37
 * @Version 1.0
 */
@Controller
@RequestMapping("/cms/dfDirectFinanceInfo")
public class DfDirectFinanceInfoController extends BaseController {
    @Autowired
    private DfDirectFinanceInfoService dfDirectFinanceInfoService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    /**
     * 列表页跳转
     *
     * @return
     */
    @GetMapping("/list")
    public String list() {
        return "cms/directFinance/dfDirectFinanceInfo/listDfDirectFinanceInfo";
    }
    /**
     * 列表页跳转
     *
     * @return
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/directFinance/dfDirectFinanceInfo/listApproveDfDirectFinanceInfo";
    }
    /**
     * 列表数据展示查询
     *
     * @param input
     * @return
     */
    @PostMapping("/dataTable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput input) {
        return dfDirectFinanceInfoService.listDfDirectFinanceInfo(input, getCurrentUser());
    }
    /**
     * 列表数据展示查询
     *
     * @param input
     * @return
     */
    @PostMapping("/ApproveDataTable")
    @ResponseBody
    public DataTablesOutput ApproveDataTable(@RequestBody DataTablesInput input) {
        return dfDirectFinanceInfoService.listDfDirectFinanceInfoApprove(input, getCurrentUser());
    }
    /**
     * 新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(Model model) {
        initConfig(model);
        return "cms/directFinance/dfDirectFinanceInfo/addDfDirectFinanceInfo";
    }
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(DfDirectFinanceInfoVO dfDirectFinanceInfoVO) {
        dfDirectFinanceInfoService.saveDfDirectFinanceInfo(dfDirectFinanceInfoVO, getCurrentUser());
        return success();
    }
    /**
     * 编辑页面跳转
     *
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model) {
        DfDirectFinanceInfoVO dfDirectFinanceInfoVO = dfDirectFinanceInfoService.getById(id);
        model.addAttribute("dfDirectFinanceInfoVO", dfDirectFinanceInfoVO);
        setInfo(model, id);
        initConfig(model);
        return "cms/directFinance/dfDirectFinanceInfo/editDfDirectFinanceInfo";
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
        return "cms/directFinance/dfDirectFinanceInfo/viewApproveDfdirectfinanceInfo";
    }
    /**
     * 查看页面跳转
     *
     * @return
     */
    @GetMapping("/view")
    public String view(String id, @RequestParam(required = false) String flag, Model model) {
        DfDirectFinanceInfoVO dfDirectFinanceInfoVO = dfDirectFinanceInfoService.getById(id);
        model.addAttribute("dfDirectFinanceInfoVO", dfDirectFinanceInfoVO);
        /**
         * 0/null隐藏，1显示
         */
        model.addAttribute("showClosBtn", flag);
        setInfo(model, id);
        initConfig(model);
        List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                BudgetConstant.BUSINESS_NAME_DF_INFO, BudgetConstant.PROCESS_SYSNAME_CMS,
                dfDirectFinanceInfoVO.getBillCode());
        if (!EmptyUtils.isEmptyList(list)) {
            model.addAttribute("ProcessDate", DateUtils.format(list.get(0).getProcessDate()));
        }
        return "cms/directFinance/dfDirectFinanceInfo/viewDfDirectFinanceInfo";
    }
    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson deletById(String id) {
        DfDirectFinanceInfo dfDirectFinanceInfo = dfDirectFinanceInfoService.get(id);
        dfDirectFinanceInfo.setStatus(DfConstant.DELETED);
        dfDirectFinanceInfoService.updateEntity(dfDirectFinanceInfo);
        // 删除资金计划
        dfDirectFinanceInfoService.deleteBudget(dfDirectFinanceInfo.getBillCode());
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
     * 报表查询跳转
     *
     * @return
     */
    @GetMapping("/listDfDirectFinanceReport")
    public String listDfDirectFinanceReport() {
        return "cms/report/Dfdirectfinance/listDfDirectFinanceInfo";
    }
    /**
     * 报表
     *
     * @param input
     * @return
     */
    @PostMapping("/listDfDirectFinanceReportData")
    @ResponseBody
    public DataTablesOutput listDfDirectFinanceReportData(@RequestBody DataTablesInput input) {
        return dfDirectFinanceInfoService.listDfDirectFinanceReport(input);
    }
    /**
     * 手机审批
     *
     * @param businessId
     * @param model
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(@RequestParam String businessId, Model model) {
        DfDirectFinanceInfoVO dfDirectFinanceInfoVO = dfDirectFinanceInfoService.getById(businessId);
        model.addAttribute("dfDirectFinanceInfoVO", dfDirectFinanceInfoVO);
        setInfo(model, businessId);
        return "cms/directFinance/dfDirectFinanceInfo/mobileViewDfdirectfinanceInfo";
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
        model.addAttribute("dfInfoItem", configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.DIRECT_FINANCE_INFO_BUDGET).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_DF_INFO);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
    }
}
