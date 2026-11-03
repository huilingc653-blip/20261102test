package com.bytter.finance.web.provide.controller;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.JsonUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.RepayType;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanRepay;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.*;
import com.bytter.finance.domain.provide.vo.CmsProvideInterestVO;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanRepayVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 借款还款
 * @date 2017-08-08 15:15:54
 */
@Controller
@RequestMapping("/cms/provide/loanrepay")
public class LoanRepayController extends BaseController {
    @Autowired
    private CmsProvideLoanRepayService cmsProvideLoanRepayService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @Autowired
    private ProvideLoanDeferredService provideLoanDeferredService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Autowired
    private RepayTypeService repayTypeService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;


    /**
     * 借款还款列表 页面跳转
     */
    @GetMapping("/listAppTab")
    public String listAppTab(String repayPlan, Model model) {
        return "cms/provide/repay/listRepayTab";
    }
    /**
     * 查询借款还款列表 - 页面跳转
     */
    @LogMessage(content = "查询借款还款申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApp")
    public String listApp(String repayPlan, Model model) {
        if (!EmptyUtils.isEmpty(repayPlan)) {
            String[] repayString = repayPlan.split("\\?");
            repayPlan = repayString[0];
            model.addAttribute("repayPlan", repayPlan);
        }
        this.initConfig(model);
        model.addAttribute("gua",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));
        return "cms/provide/repay/listCmsProvideLoanRepay";
    }
    /**
     * 线上还款追踪列表 - 页面跳转
     */
    @LogMessage(content = "查询线上还款追踪列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listOnlineRepayFollow")
    public String listOnlineRepayFollow(String repayPlan, Model model) {
        return "cms/provide/repay/listOnlineRepay";
    }
    /**
     * 修改或补录还款列表 页面跳转
     *
     * @param upRepSing 修改或补录标识 1 修改 其他为补录
     */
    @LogMessage(content = "查询借款还款申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listUpOrRep")
    public String listUpOrRep(String upRepSing, Model model) {
        if (!EmptyUtils.isEmpty(upRepSing)) {
            model.addAttribute("upRepSing", upRepSing);
        }
        return "cms/provide/repay/listRepayUpOrRep";
    }
    /**
     * 借款还款 table列表
     *
     * @return
     */
    @PostMapping("/dataAppTable")
    @ResponseBody
    public DataTablesOutput dataAppTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.getDataAppTables(dataTablesInput, getCurrentUser());
    }
    /**
     * 还款跟踪
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataOnlineRepayFollow")
    @ResponseBody
    public DataTablesOutput dataOnlineRepayFollow(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.getDataOnlineRepayFollow(dataTablesInput, getCurrentUser());
    }
    /**
     * 修改还款 table列表
     *
     * @return
     */
    @PostMapping("/dataUpdateTable")
    @ResponseBody
    public DataTablesOutput dataUpdateTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.getDataUpdateTables(dataTablesInput, getCurrentUser());
    }
    /**
     * 补录还款 table列表
     *
     * @return
     */
    @PostMapping("/dataRepairTable")
    @ResponseBody
    public DataTablesOutput dataRepairTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.getDataRepairTables(dataTablesInput, getCurrentUser());
    }
    /**
     * 还款计划修改 table列表
     *
     * @return
     */
    @PostMapping("/dataRepayPlanable")
    @ResponseBody
    public DataTablesOutput dataRepayPlanable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.dataRepayPlanable(dataTablesInput, getCurrentUser());
    }
    /**
     * 计息方式修改 table列表
     *
     * @return
     */
    @PostMapping("/dataInterestWayable")
    @ResponseBody
    public DataTablesOutput dataInterestWayable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.dataInterestWayable(dataTablesInput, getCurrentUser());
    }
    /**
     * 浮动方式改 table列表
     *
     * @return
     */
    @PostMapping("/dataFolatingModeable")
    @ResponseBody
    public DataTablesOutput dataFolatingModeable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.dataFolatingModeable(dataTablesInput, getCurrentUser());
    }
    /**
     * 借款还款新增页面跳转
     */
    @GetMapping("/edit")
    public String edit(String tempOrSubmitSign, CmsProvideLoanRepay repay, Model model, String downSign) {
        if (StringUtils.isNotEmpty(repay.getId())) {
            String onlineSign = repay.getOnlineSign();
            repay = cmsProvideLoanRepayService.get(repay.getId());
            if (EmptyUtils.isEmpty(repay.getAcrossRepair())) {
                repay.setAcrossRepair("0");
            }
            //第三方费率信息
            List<DfIntermediateCostVO> costVOS = loanInfoService.listIntermediateCostByDfId(repay.getId());
            model.addAttribute("costVOS", costVOS);
        } else {
            repay.setCreateDate(new Date());
            repay.setAcrossRepair(FinacialConstant.BOOLEAN_FALSE);// 设置不跳过补录
            if (EmptyUtils.isEmpty(repay.getOnlineSign())) {
                if (CmsConstant.ONLINE_TRUE.equals(repay.getOnlineSign())) {// 线上
                    repay.setAcrossRepair(FinacialConstant.BOOLEAN_TRUE);
                }
            }
        }
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        if (StringUtils.isNotEmpty(send.getAccrualType())) {
            RepayType repayType = repayTypeService.get(send.getRepayType());
            model.addAttribute("repayType", repayType);
            model.addAttribute("repayTypeJson", JsonUtils.toJSONString(repayType));
        }
        repay.setCorpId(send.getCorpId());
        // 最大输入金额控制
        BigDecimal sendBalance = cmsProvideLoanRepayService.completeSendBalance(repay.getSendCode(), null);
        if (repay.getRepayMoney() != null) {
            sendBalance = sendBalance.add(repay.getRepayMoney());
        }
        // 借款信息
        LoanInfoVO loanInfoVO = loanInfoService.getLoanInfoVOBySendBillCode(send.getBillCode());
        //获取预计利息明细最大金额：发放金额-已还款金额。
        CmsProvideInterestVO interestVO = cmsProvideInterestService.sumLeftMoney(repay.getSendCode(), CommonConstant.YES);
        CmsProvideInterestVO repayMaxDate = cmsProvideInterestService.queryInterestMaxAndMinDate(repay.getSendCode(), CommonConstant.NO);
        CmsProvideInterestVO repayMinDate = cmsProvideInterestService.queryInterestMaxAndMinDate(repay.getSendCode(), CommonConstant.YES);
        if(null != repayMaxDate.getSignRepayDate()){
            model.addAttribute("repayMaxDate", DateUtils.format(DateUtils.addDay(repayMaxDate.getSignRepayDate(), -1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        if (null != repayMinDate && null != repayMinDate.getSignRepayDate()) {
            model.addAttribute("repayMinDate", DateUtils.format(DateUtils.addDay(repayMinDate.getSignRepayDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        } else {
            model.addAttribute("repayMinDate", DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        if (null != interestVO && null != interestVO.getRepayMoney()) {
            model.addAttribute("repayMaxLeftMoney", send.getMoney().subtract(interestVO.getRepayMoney()));
        } else {
            model.addAttribute("repayMaxLeftMoney", send.getMoney().subtract(BigDecimal.ZERO));
        }
        model.addAttribute("maxd",EmptyUtils.isEmpty(send.getDeferredDate())? send.getRepayDate():send.getDeferredDate());
        model.addAttribute("mind",DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        this.initConfig(model);
        model.addAttribute("plClass", loanInfoVO.getPlClass());
        model.addAttribute("repay", repay);
        model.addAttribute("send", send);
        model.addAttribute("sendBalance", sendBalance);
        model.addAttribute("downSign", downSign);
        model.addAttribute("tempOrSubmitSign", tempOrSubmitSign);
        return "cms/provide/repay/editCmsProvideLoanRepay";
    }
    /**
     * 借款还款新增页面跳转
     */
    @GetMapping("/editOnline")
    public String editOnline(CmsProvideLoanRepay repay, Model model) {
        if (StringUtils.isNotEmpty(repay.getId())) {
            if (StringUtils.isNotEmpty(repay.getId())) {
                repay = cmsProvideLoanRepayService.get(repay.getId());
            }
            if (!EmptyUtils.isEmpty(repay)) {
                List<CmsProvideInterest> list = cmsProvideLoanRepayService
                        .getRepayDateInterestsById(repay.getProvideInterestId());
                model.addAttribute("list", list);
            }
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            repay.setCorpId(send.getCorpId());

            RepayType repayType = new RepayType();
            if (!StringUtils.isEmpty(send.getRepayType()) && send.getRepayType().length() == 32) {
                repayType = repayTypeService.get(send.getRepayType());
            }
            model.addAttribute("repayType", repayType);
            model.addAttribute("repayTypeJson", JsonUtils.toJSONString(repayType));


            Date deferred = provideLoanDeferredService.getMaxDeferredDate(repay.getSendCode());
            if (EmptyUtils.isEmpty(deferred)) {
                deferred = send.getRepayDate();
            }
            if (deferred.compareTo(DateUtils.getDateYMD()) < 0) {
                model.addAttribute("overdueSign", "1");// 逾期标识
            } else {
                model.addAttribute("overdueSign", "0");// 没逾期
            }
            // 最大输入金额控制
            BigDecimal sendBalance = cmsProvideLoanRepayService.completeSendBalance(repay.getSendCode(), null);
            if (repay.getRepayMoney() != null) {
                sendBalance = sendBalance.add(repay.getRepayMoney());
            }

            CmsProvideInterestVO repayMaxDate = cmsProvideInterestService.queryInterestMaxAndMinDate(repay.getSendCode(), CommonConstant.NO);
            CmsProvideInterestVO repayMinDate = cmsProvideInterestService.queryInterestMaxAndMinDate(repay.getSendCode(), CommonConstant.YES);
            if(null != repayMaxDate.getSignRepayDate()){
                model.addAttribute("repayMaxDate", DateUtils.format(DateUtils.addDay(repayMaxDate.getSignRepayDate(), -1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            }
            if (null != repayMinDate && null != repayMinDate.getSignRepayDate()) {
                model.addAttribute("repayMinDate", DateUtils.format(DateUtils.addDay(repayMinDate.getSignRepayDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            } else {
                model.addAttribute("repayMinDate", DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            }
            model.addAttribute("maxd",EmptyUtils.isEmpty(send.getDeferredDate())? send.getRepayDate():send.getDeferredDate());
            model.addAttribute("mind",DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));

            this.initConfig(model);
            model.addAttribute("repay", repay);
            model.addAttribute("send", send);
            model.addAttribute("sendBalance", sendBalance);
        }
        return "cms/provide/repay/editOnlineRepay";
    }
    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        // 0为不挂接资金计划 现在需求要求不挂接
        model.addAttribute("budgetSel", configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
        model.addAttribute("repayItem", configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_REPAY_HANG_BUDGET).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
    }
    /**
     * 编辑补录
     */
    @GetMapping("/editRepair")
    public String editRepair(CmsProvideLoanRepay repay, Model model) {
        if (StringUtils.isNotEmpty(repay.getId())) {
            repay = cmsProvideLoanRepayService.get(repay.getId());
        }
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        Date deferred = provideLoanDeferredService.getMaxDeferredDate(repay.getSendCode());
        if (EmptyUtils.isEmpty(deferred)) {
            deferred = send.getRepayDate();
        }
        if (deferred.compareTo(DateUtils.getDateYMD()) < 0) {
            model.addAttribute("overdueSign", "1");// 逾期标识
        } else {
            model.addAttribute("overdueSign", "0");// 没逾期
        }
        // 最大输入金额控制
        BigDecimal sendBalance = cmsProvideLoanRepayService.completeSendBalance(repay.getSendCode(), null);
        if (repay.getRepayMoney() != null) {
            sendBalance = sendBalance.add(repay.getRepayMoney());
        }
        this.initConfig(model);
        model.addAttribute("repay", repay);
        model.addAttribute("sendBalance", sendBalance);
        model.addAttribute("curCode", send.getCurCode());
        return "cms/provide/repay/editRepayRepair";
    }
    /**
     * @return
     */
    @LogMessage(content = "线上还款申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewOnlineRepay")
    public String viewOnlineRepay(String repayId, Model model) {
        LoanRepayVO repay = cmsProvideLoanRepayService.queryRepayVOById(repayId);
        if (!EmptyUtils.isEmpty(repay)) {
            List<CmsProvideInterest> list = cmsProvideLoanRepayService
                    .getRepayDateInterestsById(repay.getProvideInterestId());
            model.addAttribute("list", list);
        }
        model.addAttribute("repay", repay);
        return "cms/provide/repay/viewOnlineRepay";
    }
    /**
     * 还款申请查询- 公用页面
     */
    @LogMessage(content = "还款申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRepay")
    public String viewRepay(String sendBillCode, String repayId, String show, Model model) {
        if (EmptyUtils.isNotEmpty(repayId) && !"null".equals(repayId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(repayId);
            model.addAttribute("repay", repay);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            model.addAttribute("curCode", send.getCurCode());
            this.initConfig(model);
        }
        if (!EmptyUtils.isEmpty(sendBillCode)) {
            model.addAttribute("sendBillCode", sendBillCode);
        }
        model.addAttribute("show", show);// 显示按钮
        return "cms/provide/repay/viewCmsProvideLoanRepay";
    }
    @GetMapping("/viewRepayBybillCode")
    public String viewRepayByBillCode(String repayBillCode, Model model) {
        if (EmptyUtils.isNotEmpty(repayBillCode) && !"null".equals(repayBillCode)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.getRepayInfoByBillCode(repayBillCode);
            model.addAttribute("repay", repay);
            this.initConfig(model);
            model.addAttribute("sendBillCode", repay.getSendCode());
        }
        model.addAttribute("show", 0);// 显示按钮
        return "cms/provide/repay/viewCmsProvideLoanRepay";
    }
    /**
     * 借款查询列表 查询还款详情列表 passageSign 在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    @GetMapping("/viewRepayList")
    public String viewRepayList(String sendBillCode, String passageSign, Model model) {
        if (EmptyUtils.isNotEmpty(sendBillCode)) {
            model.addAttribute("sendBillCode", sendBillCode);
            List repayList = cmsProvideLoanRepayService.getRepayListBySendBillCode(sendBillCode, passageSign);
            model.addAttribute("repayList", repayList);
        }
        return "cms/provide/viewRepayList";
    }
    /**
     * 编辑借款还款
     *
     * @return
     */
    @LogMessage(content = "编辑借款还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(LoanRepayVO vo, String saveType, String tempOrSubmitSign, String sign) {
        cmsProvideLoanRepayService.tempOrSubmit(vo, saveType, tempOrSubmitSign, getCurrentUser(), sign);
        return success();
    }
    /**
     * 批量线下还款
     *
     * @return
     */
    @LogMessage(content = "批量线下还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody LoanRepayVO loanRepayVO) {
        cmsProvideLoanRepayService.batchSubmit(loanRepayVO, getCurrentUser());
        return success();
    }
    /**
     * 补录借款还款
     *
     * @return
     */
    @LogMessage(content = "补录借款还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/repairTempOrSubmit")
    @ResponseBody
    public AjaxJson repairTempOrSubmit(LoanRepayVO vo, String saveType) {
        cmsProvideLoanRepayService.repairTempOrSubmit(vo, saveType, getCurrentUser());
        return success();
    }
    /**
     * 补录打回借款还款
     *
     * @return
     */
    @LogMessage(content = "补录打回借款还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/repairBack")
    @ResponseBody
    public AjaxJson repairBack(String id, String refuseWhy) {
        cmsProvideLoanRepayService.repairBack(id, refuseWhy, getCurrentUser());
        return success();
    }
    /**
     * 删除借款还款
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除借款还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id) {
        cmsProvideLoanRepayService.delete(id, getCurrentUser());
        return success();
    }
    /**
     * 线上还款撤回操作
     *
     * @param id
     * @return
     */
    @LogMessage(content = "线上还款撤回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/onlineWithdraw")
    @ResponseBody
    public AjaxJson onlineWithdraw(String id) {
        cmsProvideLoanRepayService.onlineWithdraw(id, getCurrentUser());
        return success();
    }
    /**
     * 线上还款删除操作
     *
     * @param id
     * @return
     */
    @LogMessage(content = "线上还款删除", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/onlineDel")
    @ResponseBody
    public AjaxJson onlineDel(String id) {
        cmsProvideLoanRepayService.onlineDel(id, getCurrentUser());
        return success();
    }
    /**
     * 线上还款确认扣款操作
     *
     * @param id
     * @return
     */
    @LogMessage(content = "线上还款确认扣款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/onlineDeductOk")
    @ResponseBody
    public AjaxJson onlineDeductOk(String id) {
        cmsProvideLoanRepayService.onlineDeductOk(id, getCurrentUser());
        return success();
    }
    /**
     * 查询离线帐号根据单位ID
     */
    @RequestMapping("/queryPaymentAccListByCoprId")
    @ResponseBody
    public List<BtBankAccDTO> queryPaymentAccListByCoprId(String corpId, String curId, String bankCode) {
        if (EmptyUtils.isEmpty(corpId)) {
            return new ArrayList<BtBankAccDTO>();
        }
        CorpDTO corpDTO = corpService.get(corpId);
        BtBankAccDTO dto = new BtBankAccDTO();
        dto.setCorpId(corpId);
        //dto.setIsOnline(BtBankAccConstant.ISONLINE_0);
        dto.setValidSign(BtBankAccConstant.VALIDSIGN_1);
        dto.setCurId(curId);
        dto.setBankCodeStr(bankCode);
        return btBankAccService.getBankAccListAll(dto);
    }
    @RequestMapping("/queryPaymentAccListByCoprIdCount")
    @ResponseBody
    public String queryPaymentAccListByCoprIdCount(String corpId) {
        if (EmptyUtils.isEmpty(corpId)) {
            return "0";
        }
        BtBankAccDTO dto = new BtBankAccDTO();
        dto.setCorpId(corpId);
        dto.setIsOnline(BtBankAccConstant.ISONLINE_0);
        dto.setValidSign(BtBankAccConstant.VALIDSIGN_1);
        List<BtBankAccDTO> list = btBankAccService.getBankAccList(dto);
        if (EmptyUtils.isEmptyList(list)) {
            return "0";
        }
        return list.size() + "";
    }
    /**
     * 根据单位ID和业务类型判断是否挂预算
     *
     * @param corpId
     * @return
     */
    @PostMapping("/isBudget")
    @ResponseBody
    public AjaxJson isBudget(String corpId) {
        boolean isBudget = cmsProvideLoanRepayService.isBudget(corpId, BaseDataConstant.IS_BORROW_BUDGET,
                BaseDataConstant.PLOAN_REPAY_HANG_BUDGET);
        AjaxJson json = new AjaxJson();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("isBudget", isBudget);
        json.setAttributes(attributes);
        json.setSuccess(true);
        return json;
    }
    /******************** 审批BEGIN **********************/
    /**
     * 还款申请审批列表页面
     */
    @LogMessage(content = "还款申请审批列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepayApprove")
    public String listRepayApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/repay/approve/listRepayApprove";
    }
    /**
     * 还款申请审批列表 - 数据
     */
    @PostMapping("/dataTableRepayApprove")
    @ResponseBody
    public DataTablesOutput dataTableRepayApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.getDataRepayApproveTables(dataTablesInput);
    }
    /**
     * 还款申请 详情页面
     */
    @GetMapping("/viewRepayApprove")
    public String viewRepayApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(businessId);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            model.addAttribute("curCode", send.getCurCode());
            model.addAttribute("repay", repay);
            this.initConfig(model);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/repay/approve/viewRepayApprove";
    }
    /**
     * 还款补录审批列表页面
     */
    @LogMessage(content = "还款补录审批列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepayRepairApprove")
    public String listRepayRepairApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/repay/approve/listRepayRepairApprove";
    }
    /**
     * 还款申请审批列表 - 数据
     */
    @PostMapping("/dataTableRepayRepairApprove")
    @ResponseBody
    public DataTablesOutput dataTableRepayRepairApprove(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanRepayService.getDataRepayRepairApproveTables(dataTablesInput);
    }
    /**
     * 还款申请 详情页面
     */
    @GetMapping("/viewRepayRepairApprove")
    public String viewRepayRepairApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(businessId);
            model.addAttribute("repay", repay);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            model.addAttribute("curCode", send.getCurCode());
            this.initConfig(model);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/repay/approve/viewRepayRepairApprove";
    }
    /**
     * 手机审批 申请、补录
     */
    @GetMapping("/mobileRepayApprove")
    public String mobileRepayApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(businessId);
            model.addAttribute("repay", repay);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(send.getBillCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());
            /*
             * if(RationConstant.RAT_COMMON_Y.equals(loanInfoVo.getRatmodel())) { model.addAttribute("urationDTO",
             * urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, loanInfoVo.getId()));//授信明细 }
             */
            BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(send.getBillCode(), "1");// 在途金额
            BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(send.getBillCode(), "2");// 已还金额
            List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                    BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY, BudgetConstant.PROCESS_SYSNAME_CMS,
                    repay.getBillCode());
            if (!EmptyUtils.isEmptyList(list)) {
                model.addAttribute("ProcessDate", list.get(0).getProcessDate());
            }
            this.initConfig(model);
            model.addAttribute("loanSend", loanSendVo);
            model.addAttribute("loanInfo", loanInfoVo);
            model.addAttribute("repayPassage", repayPassage);
            model.addAttribute("repayFinish", repayFinish);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/repay/approve/mobileViewRepayApprove";
    }
}
