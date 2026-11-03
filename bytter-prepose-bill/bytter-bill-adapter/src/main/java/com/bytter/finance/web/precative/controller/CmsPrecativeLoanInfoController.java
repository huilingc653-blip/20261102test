package com.bytter.finance.web.precative.controller;
import com.bytter.biz.finacial.api.constant.FinacialManualConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtPurposeConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.system.api.vo.LoanRateMaintenceVO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款信息表
 * @date 2017-07-20 14:00:39
 */
@Controller
@RequestMapping("/cms/precative/loaninfo")
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
     * 录入页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询委托贷款信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        /*
         * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); String opdate = sdf.format(new Date());
         */
        // 系统参数 是否挂资金计划
        String isBudget = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET)
                .getValue();
        model.addAttribute("budgetSel", isBudget);
        // 系统参数 委托贷款资金计划科目
        if (EmptyUtils.isBlank(configService
                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue())) {
            model.addAttribute("endorseItem", "400");// 委托贷款录入资金计划科目设置为空时默认为400
        } else {
            model.addAttribute("endorseItem", configService
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue());
        }
        model.addAttribute("opDate", new Date());
        model.addAttribute("type", FinacialManualConstant.MANUAL_STATUS_INIT);
        if (StringUtils.isNotEmpty(isBudget) && isBudget.equals(CmsConstant.IS_TRUE)) {// 参数设置挂接计划
            // 获取周期类型 timeType
            Map<String, Object> config = configService.listByType(ConfigConstant.FBS);
            Map<String, Object> map = (Map<String, Object>) config.get("fbs");
            String timeType = (String) map.get("timeType");
            // 获取本期计划周期开始日期和结束日期
            if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstYearDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastYearDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstQuarterDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastQuarterDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstMonthDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastMonthDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstWeekDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastWeekDate(new Date()));
            } else {
                model.addAttribute("planStartTime", new Date());
                model.addAttribute("planEndTime", new Date());
            }
        }
        return "cms/precative/loaninfo/listLoanInfo";
    }
    /**
     * 修改和打回查询页面跳转
     */
    // @LogMessage(content = "查询委托贷款信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/changeList/{type}")
    public String changeList(Model model, @PathVariable("type") String type) {
        model.addAttribute("opType", type);
        return "cms/precative/loaninfo/listChangeLoanInfo";
    }
    /**
     * 录入提交
     */
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, CmsPrecativeLoanInfoVo pre, String saveType) {
        if (StringUtils.isEmpty(pre.getId())) {
            String billCode = cmsPrecativeLoanInfoService.getPrecativeBillCode(PrecativeConstant.INFO_BILL_CODE_PREFIX);
            pre.setBillCode(billCode);
        }
        cmsPrecativeLoanInfoService.submit(pre, saveType, getCurrentUser());
        return success();
    }
    /**
     * 修改、打回数据查询
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput, String type) {
        return cmsPrecativeLoanInfoService.queryChangeLoanInfo(dataTablesInput, type, getUserId());
    }
    /**
     * 修改 打回的数据页面跳转
     */
    @GetMapping("/update")
    public String update(CmsPrecativeLoanInfoVo pre, Model model) {
        if (StringUtils.isNotEmpty(pre.getBillCode())) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(pre.getBillCode());
            model.addAttribute("type", "Edit");
            model.addAttribute("opDate", preVo.getCreateDate());
            model.addAttribute("precativePage", preVo);
            if (EmptyUtils.isBlank(configService
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue())) {
                model.addAttribute("endorseItem", "400");// 委托贷款录入资金计划科目设置为空时默认为400
            } else {
                model.addAttribute("endorseItem",
                        configService
                                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL)
                                .getValue());
            }
            // model.addAttribute("endorseItem",configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE,
            // BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue());
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PRECATIVE);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        }
        // 系统参数 是否挂资金计划
        String isBudget = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET)
                .getValue();
        model.addAttribute("budgetSel", isBudget);
        if (StringUtils.isNotEmpty(isBudget) && isBudget.equals(CmsConstant.IS_TRUE)) {
            // 获取周期类型 timeType
            Map<String, Object> config = configService.listByType(ConfigConstant.FBS);
            Map<String, Object> map = (Map<String, Object>) config.get("fbs");
            String timeType = (String) map.get("timeType");
            // 获取本期计划周期开始日期和结束日期
            if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstYearDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastYearDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_QUARTER.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstQuarterDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastQuarterDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_MONTH.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstMonthDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastMonthDate(new Date()));
            } else if (BudgetConstant.TIME_TYPE_WEEK.equalsIgnoreCase(timeType)) {
                model.addAttribute("planStartTime", DateUtils.getFirstWeekDate(new Date()));
                model.addAttribute("planEndTime", DateUtils.getLastWeekDate(new Date()));
            } else {
                model.addAttribute("planStartTime", new Date());
                model.addAttribute("planEndTime", new Date());
            }
        }
        return "cms/precative/loaninfo/listLoanInfo";
    }
    /**
     * 修改 暂存数据
     */
    // @LogMessage(content = "修改委托贷款数据",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsPrecativeLoanInfoVo pre) {
        // cmsPrecativeLoanInfoService.submit(pre);
        return success();
    }
    /**
     * 删除 修改列表数据
     */
    @LogMessage(content = "删除委托贷款数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            cmsPrecativeLoanInfoService.deleteCmsEntity(id, getUserId());
        }
        return success();
    }
    /**
     * 详情页
     */
    // @LogMessage(content = "委托贷款数据详情页",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String billCode, String type, String loan, String status, Model model) {
        if (EmptyUtils.isEmpty(billCode)) {
            return "cms/precative/loaninfo/viewLoanInfo";
        }
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(billCode);
        /*
         * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Date createDate = preVo.getCreateDate();
         */
        // String opdate = sdf.format(EmptyUtils.isEmpty(createDate)?new Date():createDate);
        model.addAttribute("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
        model.addAttribute("opDate", preVo.getCreateDate());
        model.addAttribute("precativePage", preVo);
        model.addAttribute("opType", type);
        model.addAttribute("status", status);
        if (EmptyUtils.isNotEmpty(loan)) {
            if (loan.equals("entry")) {
                if (EmptyUtils.isBlank(configService
                        .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL)
                        .getValue())) {
                    model.addAttribute("endorseItem", "400");// 委托贷款录入资金计划科目设置为空时默认为400
                } else {
                    model.addAttribute("endorseItem", configService
                            .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANENTRY_BUDGET_SEL)
                            .getValue());
                }
                // model.addAttribute("endorseItem", configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE,
                // BaseDataConstant.WTLOANENTRY_BUDGET_SEL).getValue());//委托贷款录入
                model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PRECATIVE);
            } else if (loan.equals("repair")) {
                if (EmptyUtils.isBlank(configService
                        .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL)
                        .getValue())) {
                    model.addAttribute("endorseItem", "200;600");// 委托贷款补录资金计划科目设置为空时默认为200;600
                } else {
                    model.addAttribute("endorseItem", configService
                            .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL)
                            .getValue());
                }
                // model.addAttribute("endorseItem", configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE,
                // BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL).getValue());//委托贷款补录
                model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION);
            }
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        }
        model.addAttribute("dataType", loan);
        return "cms/precative/loaninfo/viewLoanInfo";
    }
    /**
     * 补录页面跳转
     */
    @GetMapping("/repair")
    public String repair(CmsPrecativeLoanInfoVo pre, Model model) {
        if (StringUtils.isNotEmpty(pre.getBillCode())) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(pre.getBillCode());
            preVo.setMoney(new BigDecimal(preVo.getMoney().doubleValue()));
            boolean isNatualCurrency = cmsPrecativeLoanSendService.isNatualCurrency(preVo.getCurId(), preVo.getCorpId());
            /*
             * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Date createDate = preVo.getCreateDate();
             */
            if (EmptyUtils.isBlank(configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL).getValue())) {
                model.addAttribute("endorseItem", "200;600");// 委托贷款补录资金计划科目设置为空时默认为200;600
            } else {
                model.addAttribute("endorseItem", configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL).getValue());
            }
            // BaseDataConstant.WTLOANMAKEUP_BUDGET_SEL).getValue());//资金增加
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PRECATIVE_ADDITION);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
            model.addAttribute("opDate", preVo.getCreateDate());
            model.addAttribute("precativePage", preVo);
            model.addAttribute("isNatualCurrency", isNatualCurrency);
            // 是否对接合同管理
            String relateCon = cmsPrecativeLoanInfoService.relateCon();
            if (CmsConstant.RELATED_CONTRACT_YES.equals(relateCon)) {
                String conType = cmsPrecativeLoanInfoService.conType();
                model.addAttribute("conType", conType);
                model.addAttribute("conId", preVo.getConId());
            }
            model.addAttribute("relateCon", relateCon);
        }
        return "cms/precative/loaninfo/repairLoanInfo";
    }
    /**
     * 补录打回
     */
    @LogMessage(content = "打回委托贷款补录数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/back")
    @ResponseBody
    public AjaxJson back(String id, String refuseWhy) {
        cmsPrecativeLoanInfoService.repairBack(id, refuseWhy, getCurrentUser());
        return success();
    }
    /**
     * 补录提交
     */
    @LogMessage(content = "提交委托贷款补录数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/repairSubmit")
    @ResponseBody
    public AjaxJson repairSubmit(CmsPrecativeLoanInfoVo pre) {
        cmsPrecativeLoanInfoService.repairSubmit(pre, getCurrentUser());
        return success();
    }
    /**
     * 补录获取利率
     */
    @LogMessage(content = "补录获取利率", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/getRate")
    @ResponseBody
    public LoanRateMaintenceVO getRate(String plDate, String referenceRateType, String curId, String repayCycle) {
        LoanRateMaintenceVO rate = cmsPrecativeLoanInfoService.getRate(plDate, referenceRateType, curId, repayCycle);
        return rate;
    }
    /**
     * 跳转至审核列表
     */
    @GetMapping("/listApprove")
    public String listApprove(Model model, String flowStatus, String cycleTime) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/precative/loaninfo/listInfoApprove";
    }
    /**
     * 跳转至借款人审核列表
     */
    @GetMapping("/listApproveLoan")
    public String listApproveLoan(Model model, String flowStatus, String cycleTime) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        model.addAttribute("opType", "loan");
        return "cms/precative/loaninfo/listInfoApprove";
    }
    /**
     * 跳转至手机审核列表
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.getLoanInfoById(businessId);
            model.addAttribute("precativePage", preVo);
            model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
            model.addAttribute("taskId", taskId);
            model.addAttribute("pageType", "add");
        }
        return "cms/precative/loaninfo/mobileInfoApprove";
    }
    /**
     * 跳转至手机审核列表
     */
    @GetMapping("/mobileRepairApprove")
    public String mobileRepairApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.getLoanInfoById(businessId);
            model.addAttribute("precativePage", preVo);
            model.addAttribute("loanMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
            model.addAttribute("taskId", taskId);
            model.addAttribute("pageType", "repair");
        }
        return "cms/precative/loaninfo/mobileInfoApprove";
    }
    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput, String type) {
        return cmsPrecativeLoanInfoService.dataTableApprove(dataTablesInput, type);
    }
    /**
     * 跳转至审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @param type
     * @return
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model, String type) {
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(businessId);
        /*
         * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Date createDate = preVo.getCreateDate(); String
         * opdate = sdf.format(EmptyUtils.isEmpty(createDate)?new Date():createDate);
         */
        model.addAttribute("opDate", preVo.getCreateDate());
        model.addAttribute("precativePage", preVo);
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        if ("repair".equals(type)) {
            return "cms/precative/loaninfo/viewRepairApprove";
        }
        return "cms/precative/loaninfo/viewInfoApprove";
    }
    /**
     * 跳转至审核列表
     */
    @GetMapping("/listRepairApprove")
    public String listRepairApprove(Model model, String flowStatus, String cycleTime) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        model.addAttribute("opType", "repair");
        return "cms/precative/loaninfo/listRepairApprove";
    }
    /**
     * 根据单位ID和业务类型判断是否挂预算
     */
    @PostMapping("/isBudget")
    @ResponseBody
    public AjaxJson isBudget(String corpId, String briefId) {
        boolean isBudget = configService.getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET)
                .toBoolean();// 委托贷款参数是否挂预算
        if (isBudget) {
            cmsPrecativeLoanInfoService.isBudget(corpId, briefId);
        }
        AjaxJson json = new AjaxJson();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("isBudget", isBudget);
        json.setAttributes(attributes);
        json.setSuccess(true);
        return json;
    }
    @GetMapping("/getPurpose")
    @ResponseBody
    public List<BtPurposeDTO> listByCorpIdOrBusType(String corpId, String sysBusType) {
        if (EmptyUtils.isEmpty(corpId) || "null".equals(corpId)) {
            return new ArrayList<BtPurposeDTO>();
        }
        List<BtPurposeDTO> list = cmsPrecativeLoanInfoService.listByCorpIdOrBusType(corpId,
                BtPurposeConstant.SYS_BUS_TYPE_CMS);
        return EmptyUtils.isEmptyList(list) ? new ArrayList<BtPurposeDTO>() : list;
    }
    /**
     * 委托人和借款人有权限的单位合集
     */
    @GetMapping("/getLoanOpp")
    @ResponseBody
    public List<CorpDTO> getCorps(String precativeId) {
        if (EmptyUtils.isEmpty(precativeId)) {
            return null;
        }
        CorpDTO pre = iCorpService.get(precativeId);
        String preNetId = pre.getNetId();
        if (EmptyUtils.isEmpty(preNetId)) {
            preNetId = pre.getId();
        }
        List<CorpDTO> corpDTOs = iCorpService.listUnderCorpByNetId(preNetId);
        return TreeUtil.getTreeNodeList(corpDTOs, Comparator.comparing(CorpDTO::getSort).thenComparing(CorpDTO::getCode));
    }

    /**
     * 委托人和借款人有权限的单位合集
     */
    @PostMapping("/getCorp")
    @ResponseBody
    public String getCorp(String precativeId) {
        if (EmptyUtils.isEmpty(precativeId)) {
            return null;
        }
        CorpDTO pre = iCorpService.getNetByCorpId(precativeId);
        return pre.getId();
    }
}
