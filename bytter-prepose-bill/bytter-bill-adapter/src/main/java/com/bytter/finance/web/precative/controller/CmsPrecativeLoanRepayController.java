package com.bytter.finance.web.precative.controller;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.bytter.finance.domain.precative.entity.CmsPrecativeLoanRepay;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanInfoService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanRepayService;
import com.bytter.finance.domain.precative.service.CmsPrecativeLoanSendService;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanInfoVo;
import com.bytter.finance.domain.precative.vo.CmsPrecativeLoanRepayVo;
import com.bytter.finance.domain.precative.vo.PrecativeLoanSendVo;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.dto.ConfigDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 委托贷款还款信息表
 * @date 2017-08-02 14:05:07
 */
@Controller
@RequestMapping("/cms/precative/loanrepay")
public class CmsPrecativeLoanRepayController extends BaseController {
    @Autowired
    private CmsPrecativeLoanInfoService cmsPrecativeLoanInfoService;
    @Autowired
    private CmsPrecativeLoanRepayService cmsPrecativeLoanRepayService;
    @Autowired
    private CmsPrecativeLoanSendService cmsPrecativeLoanSendService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    /**
     * 委托贷款还款信息表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询委托贷款还款信息表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model, String repayPlan) {
        if (StringUtils.isNotEmpty(repayPlan)) {
            model.addAttribute("repayPlan", repayPlan);
        }
        return "cms/precative/loanrepay/listLoanRepay";
    }
    /**
     * 委托贷款还款信息表列表 页面跳转
     *
     * @return
     */
    // @LogMessage(content = "查询委托贷款还款信息表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/editList")
    public String editList(Model model) {
        return "cms/precative/loanrepay/editListLoanRepay";
    }
    /**
     * 委托贷款还款信息表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanRepayService.getLoanRepay(dataTablesInput, getUserId());
    }
    /**
     * <b>功能介绍：</b>贷款还款计划修改列表<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>songkk<br>
     *
     * @version 1.0
     */
    @PostMapping("/dataRepayPlanable")
    @ResponseBody
    public DataTablesOutput dataRepayPlanable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanRepayService.dataRepayPlanable(dataTablesInput,this.getCurrentUser());
    }
    /**
     * 委托贷款还款修改打回信息表 table列表
     */
    @PostMapping("editDatatable")
    @ResponseBody
    public DataTablesOutput editDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanRepayService.getEditLoanRepay(dataTablesInput, getUserId());
    }
    /**
     * 委托贷款还款信息表新增页面跳转
     */
    @GetMapping("/add")
    public String add(CmsPrecativeLoanRepay cmsPrecativeLoanRepay, Model model) {
        if (StringUtils.isNotEmpty(cmsPrecativeLoanRepay.getId())) {
            cmsPrecativeLoanRepay = cmsPrecativeLoanRepayService.get(cmsPrecativeLoanRepay.getId());
            model.addAttribute("cmsPrecativeLoanRepayPage", cmsPrecativeLoanRepay);
        }
        return "cms/precative/loanrepay/addLoanRepay";
    }
    /**
     * 委托贷款利息确认表列表 页面跳转
     */
    @LogMessage(content = "查询贷款还款表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listInteretAffirm")
    public String listInteretAffirm(Model model) {
        return "cms/precative/loanrepay/listInteretAffirm";
    }
    /**
     * 委托贷款贷款利息确认列表
     */
    @PostMapping("/interetAffirm")
    @ResponseBody
    public DataTablesOutput interetAffirm(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanRepayService.listRepayInteretAffirm(dataTablesInput, getUserId());
    }
    /**
     * 委托贷款还款补录列表 页面跳转
     */
    @LogMessage(content = "查询贷款还款表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepayAdditional")
    public String listRepayAdditional(Model model) {
        return "cms/precative/loanrepay/listRepayAdditional";
    }
    /**
     * 委托贷款还款补录列表
     */
    @PostMapping("/repayAdditional")
    @ResponseBody
    public DataTablesOutput RepayAdditional(@RequestBody DataTablesInput dataTablesInput) {
        // 加判断，有没有挂资金计划，没有不显示数据
        ConfigDTO dto = configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS,
                BaseDataConstant.IS_ENTRUST_BUDGET);
        DataTablesOutput dt = new DataTablesOutput();
        if (dto.getValue().equals("1")) {
            dt = cmsPrecativeLoanRepayService.listRepayAdditional(dataTablesInput, getUserId());
        }
        return dt;
    }
    /****
     *
     * 功能介绍 委贷还款补录保存。<br>
     * <b>日期：</b>2018年3月15日<br>
     * 
     * @author zhengxn
     */
    @LogMessage(content = "添加委托贷款还款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/repayAdditionalSave")
    @ResponseBody
    public AjaxJson RepayAdditionalSave(CmsPrecativeLoanRepayVo CmsPrecativeLoanRepayVo) {
        cmsPrecativeLoanRepayService.repayAdditionalSubmit(CmsPrecativeLoanRepayVo, getCurrentUser());
        return success();
    }
    /**
     * 委托贷款还款信息表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/repay")
    public String update(CmsPrecativeLoanRepayVo pre, Model model) {
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(pre.getInfoBillCode());
        PrecativeLoanSendVo send = cmsPrecativeLoanSendService.getSendByBillCode(pre.getSendCode());
        CmsPrecativeLoanRepayVo repay = new CmsPrecativeLoanRepayVo();
        List<RepaymentScheduleVO> repayments = new ArrayList<RepaymentScheduleVO>();
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(send.getBillCode()); // 在途金额
        if (!EmptyUtils.isEmpty(pre.getBillCode()) && !"null".equals(pre.getBillCode())) {
            repay = cmsPrecativeLoanRepayService.getReapyByBillCode(pre.getBillCode());
            // PassageMoney = PassageMoney.add(repay.getRepayMoney());
            model.addAttribute("type", "Edit");
        }
        if ("1".equals(info.getSkipLoanSendOut())) {
            repayments = repaymentScheduleService.getRepayMentScheduleByBillCode(info.getId());
            BigDecimal repaymentIst = repaymentScheduleService.getsumRepaymentIstByBillCode(info.getBillCode());// 查询本期利息
            model.addAttribute("repaymentIst", repaymentIst);// 本期利息
        } else if ("0".equals(info.getSkipLoanSendOut())) {
            repayments = repaymentScheduleService.getRepayMentScheduleByBillCode(send.getId());
            BigDecimal repaymentIst = repaymentScheduleService.getsumRepaymentIstByBillCode(send.getBillCode());// 查询本期利息
            model.addAttribute("repaymentIst", repaymentIst);// 本期利息
        }
        if (repayments != null && repayments.size() > 0) {
            model.addAttribute("isHaveRepayMent", 1);// 是否维护了还款计划
        } else {
            model.addAttribute("isHaveRepayMent", 0);// 是否维护了还款计划
        }
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(send.getBillCode()); // 已还款金额
        BigDecimal repayAccrual = cmsPrecativeLoanRepayService.queryRepayAccrual(send.getBillCode());// 已还利息
        BigDecimal loanMoney = send.getMoney().subtract(repayMoney); // 待还金额
        // 判断是否本位币
        boolean isNatualCurrency = cmsPrecativeLoanSendService.isNatualCurrency(info.getCurId(), info.getCorpId());
        model.addAttribute("isNatualCurrency", isNatualCurrency);
        /*
         * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); String opdate = sdf.format(new Date());
         */
        model.addAttribute("opDate", info.getCreateDate());
        model.addAttribute("precativeInfoPage", info);
        model.addAttribute("precativeSendPage", send);
        model.addAttribute("precativeRepayPage", repay);
        model.addAttribute("PassageMoney", PassageMoney);
        model.addAttribute("repayMoney", repayMoney);
        model.addAttribute("repayAccrual", repayAccrual);
        model.addAttribute("loanMoney", loanMoney);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        // 系统参数 委托贷款资金计划科目
        if (EmptyUtils.isBlank(configServiceImpl
                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue())) {
            model.addAttribute("endorseItem", "600");// 委托贷款还款录入资金计划科目设置为空时默认为600
        } else {
            model.addAttribute("endorseItem", configServiceImpl
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue());
        }
        // model.addAttribute("endorseItem",configServiceImpl.getConfig(BaseDataConstant.CMS_CONFIG_TYPE,
        // BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue());
        // 系统参数 是否挂资金计划
        model.addAttribute("budgeSel", configServiceImpl
                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.IS_ENTRUST_BUDGET).getValue());
        boolean isTally = false;
        if (CmsConstant.IS_TRUE.equals(configServiceImpl.getConfig("cms", "is_entrust_tally").getValue())) {// 查询记账参数
            isTally = true;
        }
        model.addAttribute("tallySign", isTally);
        return "cms/precative/loanrepay/addLoanRepay";
    }
    /**
     * 删除打回和需要修改的数据
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除委托贷款数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            cmsPrecativeLoanRepayService.deletePrecative(id, getCurrentUser());
        }
        return success();
    }
    /**
     * 委托贷款还款信息表详情页
     *
     * @return
     */
    // @LogMessage(content = "查询委托贷款还款信息表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(String sendCode, String type, String billCode, Model model) {
        CmsPrecativeLoanRepayVo repayVo = null;
        if (EmptyUtils.isNotEmpty(billCode)) {
            repayVo = cmsPrecativeLoanRepayService.getReapyByBillCode(billCode);
        } else {
            repayVo = cmsPrecativeLoanRepayService.getReapyByBillCode(sendCode);
        }
        if (!EmptyUtils.isEmpty(repayVo)) {
            sendCode = repayVo.getSendCode();
            model.addAttribute("type", "repay");
            model.addAttribute("precativeRepayPage", repayVo);
        }
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(sendCode);
        CmsPrecativeLoanInfoVo info = null;
        BigDecimal loanMoney = null;
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendCode); // 在途金额
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendCode); // 已还款金额
        if (sendVo != null) {
            info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(sendVo.getFatherCode());
            if (sendVo.getMoney() != null){
                loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
            }
            model.addAttribute("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
            model.addAttribute("precativeInfoPage", info);
            if (info !=null && info.getCreateDate() != null) model.addAttribute("opDate", info.getCreateDate());
            model.addAttribute("precativeSendPage", sendVo);
            model.addAttribute("PassageMoney", PassageMoney);
            model.addAttribute("repayMoney", repayMoney);
            model.addAttribute("loanMoney", loanMoney);
            model.addAttribute("type", type);
            model.addAttribute("billCode", billCode);
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAY);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        }
        // 系统参数 委托贷款资金计划科目
        if (EmptyUtils.isBlank(configServiceImpl
                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue())) {
            model.addAttribute("endorseItem", "400");// 委托贷款还款录入资金计划科目设置为空时默认为400
        } else {
            model.addAttribute("endorseItem", configServiceImpl
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue());
        }
        // model.addAttribute("endorseItem",configServiceImpl.getConfig(BaseDataConstant.CMS_CONFIG_TYPE,
        // BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue());
        return "cms/precative/loanrepay/viewLoanRepay";
    }
    /**
     * 委托贷款还款审核信息表详情页
     *
     * @return
     */
    // @LogMessage(content = "查询委托贷款还款信息表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/viewRepay")
    public String viewRepay(HttpServletRequest request, Model model) {
        CmsPrecativeLoanRepayVo repayVo = null;
        String billCode = request.getParameter("billCode");
        String type = request.getParameter("type");
        String sendCode = request.getParameter("sendCode");
        if (EmptyUtils.isNotEmpty(billCode)) {
            repayVo = cmsPrecativeLoanRepayService.getReapyByBillCode(billCode);
        } else {
            repayVo = cmsPrecativeLoanRepayService.getReapyByBillCode(sendCode);
        }
        if (!EmptyUtils.isEmpty(repayVo)) {
            sendCode = repayVo.getSendCode();
            model.addAttribute("type", "repay");
            model.addAttribute("precativeRepayPage", repayVo);
        }
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(sendCode);
        CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.getLoanInfoByBillCode(sendVo.getFatherCode());
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(sendCode); // 在途金额
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(sendCode); // 已还款金额
        BigDecimal loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
        model.addAttribute("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.addAttribute("precativeInfoPage", info);
        model.addAttribute("opDate", info.getCreateDate());
        model.addAttribute("precativeSendPage", sendVo);
        model.addAttribute("PassageMoney", PassageMoney);
        model.addAttribute("repayMoney", repayMoney);
        model.addAttribute("loanMoney", loanMoney);
        model.addAttribute("type", type);
        model.addAttribute("billCode", billCode);
        String fatherCode = sendVo.getFatherCode();
        if (EmptyUtils.isNotEmpty(fatherCode)) {
            CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(fatherCode);
            /*
             * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); Date createDate = preVo.getCreateDate();
             */
            // String opdate = sdf.format(EmptyUtils.isEmpty(createDate)?new Date():createDate);
            model.addAttribute("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
            model.addAttribute("opDate", preVo.getCreateDate());
            model.addAttribute("precativePage", preVo);
        }
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PRECATIVE_REPAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        // 系统参数 委托贷款资金计划科目
        if (EmptyUtils.isBlank(configServiceImpl
                .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue())) {
            model.addAttribute("endorseItem", "400");// 委托贷款还款录入资金计划科目设置为空时默认为400
        } else {
            model.addAttribute("endorseItem", configServiceImpl
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue());
        }
        // model.addAttribute("endorseItem",configServiceImpl.getConfig(BaseDataConstant.CMS_CONFIG_TYPE,
        // BaseDataConstant.WTLOANRETURN_BUDGET_SEL).getValue());
        return "cms/precative/loanrepay/viewRepay";
    }
    /**
     * 添加委托贷款还款信息表
     *
     * @param cmsPrecativeLoanRepay
     * @return
     */
    // @LogMessage(content = "添加委托贷款还款信息表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CmsPrecativeLoanRepay cmsPrecativeLoanRepay) {
        cmsPrecativeLoanRepayService.save(cmsPrecativeLoanRepay);
        return success();
    }
    /**
     * 修改委托贷款还款信息表
     *
     * @param cmsPrecativeLoanRepay
     * @return
     */
    // @LogMessage(content = "修改委托贷款还款信息表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(CmsPrecativeLoanRepay cmsPrecativeLoanRepay) {
        CmsPrecativeLoanRepay t = cmsPrecativeLoanRepayService.get(cmsPrecativeLoanRepay.getId());
        BeanUtils.copyNotNullProperties(cmsPrecativeLoanRepay, t);
        cmsPrecativeLoanRepayService.saveOrUpdate(t);
        return success();
    }
    /**
     * 委托贷款还款确认页面跳转
     *
     * @return
     */
    @GetMapping("/repayAffirm")
    public String repayAffirm(CmsPrecativeLoanRepay repay, Model model, String sign) {
        if (StringUtils.isNotEmpty(repay.getId())) {
            repay = cmsPrecativeLoanRepayService.get(repay.getId(),true);
            PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(repay.getSendCode());
            CmsPrecativeLoanInfoVo info = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
            boolean isNatualCurrency = cmsPrecativeLoanSendService.isNatualCurrency(info.getCurId(), info.getCorpId());
            CmsPrecativeLoanInfoVo repayInfo = cmsPrecativeLoanInfoService
                    .getLoanInfoRepayBillCode(repay.getBillCode());
            BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(repay.getSendCode()); // 在途金额
            BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(repay.getSendCode()); // 已还款金额
            BigDecimal loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
            model.addAttribute("cmsLoanSendPage", sendVo);// 贷款发放
            model.addAttribute("cmsLoanInfoPage", info);// 贷款申请
            if (repay.getRepayAccrual() == null) {
                repay.setRepayAccrual(BigDecimal.ZERO);
            }
            model.addAttribute("cmsLoanRepayPage", repay);
            model.addAttribute("inwayMoney", PassageMoney);
            model.addAttribute("rpayedMoney", repayMoney);
            model.addAttribute("remainMoney", loanMoney);
            model.addAttribute("repayInfo", repayInfo);
            model.addAttribute("isAccounting", cmsPrecativeLoanSendService.isAccounting(info));// 是否记账
            model.addAttribute("closeDay", iFcDayCloseService.getDayCloseFinalDate(info.getCorpId()));// 日结日期
            if (EmptyUtils.isBlank(configServiceImpl
                    .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURNMAKEUP_BUDGET_SEL)
                    .getValue())) {
                model.addAttribute("endorseItem", "400");// 委托贷款还款补录资金计划科目设置为空时默认为400
            } else {
                model.addAttribute("endorseItem", configServiceImpl
                        .getConfig(BaseDataConstant.CMS_CONFIG_TYPE, BaseDataConstant.WTLOANRETURNMAKEUP_BUDGET_SEL)
                        .getValue());
            }
            // model.addAttribute("endorseItem", configServiceImpl.getConfig(BaseDataConstant.CMS_CONFIG_TYPE,
            // BaseDataConstant.WTLOANRETURNMAKEUP_BUDGET_SEL).getValue());//资金增加
            model.addAttribute("opDate", info.getCreateDate());
            model.addAttribute("precativePage", info);
            model.addAttribute("isNatualCurrency", isNatualCurrency);
        }
        if ("1".equals(sign.trim())) {
            return "cms/precative/loanrepay/repayAdditionalInfo";
        } else {
            return "cms/precative/loanrepay/repayAffirmInfo";
        }
    }
    /**
     * 利息确认通过
     */
    @LogMessage(content = "还款利息确认通过", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/interetAffirmPass")
    @ResponseBody
    public AjaxJson interetAffirmPass(CmsPrecativeLoanRepay loanRepay) {
        cmsPrecativeLoanRepayService.interetAffirmPass(loanRepay, getCurrentUser());
        return success();
    }
    /**
     * 利息确认打回
     *
     * @param id
     * @return
     */
    @LogMessage(content = "打回贷款还款表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back/{id}")
    @ResponseBody
    public AjaxJson back(@PathVariable("id") String id) {
        CmsPrecativeLoanRepay cmsLoanRepay = cmsPrecativeLoanRepayService.get(id);
        cmsPrecativeLoanRepayService.backLoanRepay(cmsLoanRepay, getCurrentUser());
        return success();
    }
    /**
     * 发放提交
     *
     * @return
     */
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(HttpServletRequest request, CmsPrecativeLoanRepayVo pre, String saveType) {
        cmsPrecativeLoanRepayService.submit(pre, saveType, getCurrentUser());
        return success();
    }
    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listRepayApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/precative/loanrepay/listRepayApprove";
    }
    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsPrecativeLoanRepayService.dataTableApprove(dataTablesInput);
    }
    /**
     * 跳转至审核详情界面
     */
    @GetMapping("/approve")
    public String approve(String businessId, String taskId, Model model) {
        CmsPrecativeLoanRepayVo repayVo = cmsPrecativeLoanRepayService.getReapyByBillCode(businessId);
        model.addAttribute("repayVo", repayVo);
        model.addAttribute("type", "type");
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/precative/loanrepay/viewRepayApprove";
    }
    /**
     * 跳转至手机审核详情界面
     */
    @GetMapping("/mobileRepayApprove")
    public String mobileRepayApprove(String businessId, String taskId, Model model) {
        CmsPrecativeLoanRepayVo repayVo = cmsPrecativeLoanRepayService.getReapyById(businessId);
        PrecativeLoanSendVo sendVo = cmsPrecativeLoanSendService.getSendByBillCode(repayVo.getSendCode());
        CmsPrecativeLoanInfoVo preVo = cmsPrecativeLoanInfoService.queryInfo(sendVo.getFatherCode());
        BigDecimal PassageMoney = cmsPrecativeLoanRepayService.queryPassageMoney(repayVo.getSendCode()); // 在途金额
        BigDecimal repayMoney = cmsPrecativeLoanRepayService.queryRepayMoney(repayVo.getSendCode()); // 已还款金额
        BigDecimal loanMoney = sendVo.getMoney().subtract(PassageMoney).subtract(repayMoney); // 待还金额
        model.addAttribute("precativeRepayPage", repayVo);
        model.addAttribute("precativeSendPage", preVo);
        model.addAttribute("precativePage", preVo);
        model.addAttribute("PassageMoney", PassageMoney);
        model.addAttribute("repayMoney", repayMoney);
        model.addAttribute("loanMoney", loanMoney);
        model.addAttribute("InfoMoneyUpper", StringUtils.chineseMoney(preVo.getMoney()));
        model.addAttribute("sendMoneyUpper", StringUtils.chineseMoney(sendVo.getMoney()));
        model.addAttribute("taskId", taskId);
        return "cms/precative/loanrepay/mobileRepayApprove";
    }
    /**
     * 还款获取利息
     *
     * @param account
     * @return
     */
    @LogMessage(content = "还款获取利息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/getRepayAccrual")
    @ResponseBody
    public BigDecimal getRepayAccrual(String account, BigDecimal money, String orderNo, Date endInstDate) {
        return cmsPrecativeLoanRepayService.getRepayAccrual(account, money, orderNo, endInstDate, getCurrentUser());
    }
}
