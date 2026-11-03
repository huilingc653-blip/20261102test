package com.bytter.finance.web.provide.controller;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRepayService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.utils.ProvideUtils;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.system.api.*;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行借款发放表
 * @date 2017-08-03 16:40:48
 */
@Controller
@RequestMapping("/cms/provide/loansend")
public class LoanSendController extends BaseController {
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @Autowired
    private LoanInfoService loanInfoService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @Autowired
    private CmsProvideLoanRepayService cmsProvideLoanRepayService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcDayCloseService iFcDayCloseService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IBisAccDtlService iBisAccDtlService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBankTypeService iBankTypeService;
    @Autowired
    private CmsProvideInterestService cmsProvideInterestService;
    @Autowired
    private RepayTypeService repayTypeService;
    /**
     * 银行借款发放表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询银行借款发放表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "cms/provide/send/listCmsProvideLoanSend";
    }
    /**
     * 银行借款发放表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanSendService.getDataSendTables(dataTablesInput, getCurrentUser());
    }
    /**
     * 银行借款发放表新增页面跳转
     *
     * @return
     */
    @GetMapping("/edit")
    public String add(String billCode, String sendBillCode, Model model) {
        ProvideLoanSend send = cmsProvideLoanSendService.getSendByBillCode(sendBillCode, billCode);
        LoanInfoVO loanInfoVO = loanInfoService.getLoanInfoVOByBillCode(billCode);
        // 采用新的计提计息方式，编辑时，如果计提为空则还款方式也置空。重新填写
        if (StringUtils.isEmpty(send.getAccrualType())) {
            send.setRepayType(null);
        } else {
            model.addAttribute("repayType", repayTypeService.get(send.getRepayType()));
        }
        loanInfoVO.setPlMoney(new BigDecimal(loanInfoVO.getPlMoney().doubleValue()));
        if (EmptyUtils.isEmpty(send.getId())) {
            // 查询申请表的 还款计划
            if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(loanInfoVO.getLoanSendOutType())) {// 一次性发放
                CmsRepaymentSchedule crs = repaymentScheduleService
                        .getCmsRepaymentScheduleByBillCode(loanInfoVO.getId());
                if (crs != null) {
                    model.addAttribute("planId", crs.getId());
                    model.addAttribute("planStatus", "1");
                }
            }
            model.addAttribute("type", "1");
        } else {
            // 查询发放的 还款计划
            CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(send.getId());
            if (crs != null) {
                model.addAttribute("planId", crs.getId());
            }
            model.addAttribute("type", "2");
        }
        BigDecimal sendSumMoney = cmsProvideLoanSendService.getSendMoneyByBillCode(billCode);// 发放总金额
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        // 系统参数 是否挂资金计划
        this.initConfig(model);
        //todo:暂时注释
        Date closeDay = iFcDayCloseService.getDayCloseFinalDate(loanInfoVO.getNetId());
        model.addAttribute("closeDay", DateUtils.format(closeDay));
        model.addAttribute("loanInfo", loanInfoVO);
        model.addAttribute("send", send);
        model.addAttribute("baseCurId", cur.getId());
        model.addAttribute("waitRepayMoney", loanInfoVO.getPlMoney().subtract(sendSumMoney));
        model.addAttribute("naturalSign", cur.getNaturalSign());
        model.addAttribute("minDate", DateUtils.format(DateUtils.getDate()));// DateUtils.addDay(DateUtils.getDate(),1)
        // 获取计息数据 暂时屏蔽
        /*
         * AccrualTypeVo accrualTypeVo= accrualTypeService.queryAccrualTypeByCode(send.getInterestWay());
         * model.addAttribute("accrualType", accrualTypeVo);
         */
        return "cms/provide/send/addCmsProvideLoanSend";
    }
    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        // 0为不挂接资金计划 现在需求要求不挂接
        model.addAttribute("budgetSel", configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
        model.addAttribute("sendItem", configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
    }
    /**
     * 发放详情页面 - 带关闭按钮
     */
    @GetMapping("/viewSendBox/{billCode}")
    public String viewSendBox(@PathVariable("billCode") String billCode, Model model) {
        model.addAttribute("billCode", billCode);
        return "cms/provide/send/viewSendBox";
    }
    /**
     * 发放详情页面 - 公用
     */
    @GetMapping("/view/{billCode}")
    public String view(@PathVariable("billCode") String billCode, Model model) {
        List<LoanSendVO> sendList = cmsProvideLoanSendService.getLoanSendVOListByBillCode(billCode);
        model.addAttribute("sendList", sendList);
        model.addAttribute("billCode", billCode);
        return "cms/provide/send/viewCmsProvideLoanSend";
    }
    /**
     * 添加银行借款发放表
     */
    @LogMessage(content = "添加银行借款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(LoanSendVO send, String saveType) {
        cmsProvideLoanSendService.tempOrSubmit(send, saveType, getCurrentUser());
        return success();
    }
    /**
     * 发放打回
     *
     * @param billCode
     *            inof表billCode
     * @return
     */
    @LogMessage(content = "银行借款发放打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/back/{billCode}")
    @ResponseBody
    public AjaxJson back(@PathVariable("billCode") String billCode) {
        cmsProvideLoanSendService.back(billCode, null, getCurrentUser());
        return success();
    }
    /**
     * 公用页面 - 发放信息详情
     */
    @GetMapping("/viewSend/{sendBillCode}")
    public String viewSend(@PathVariable("sendBillCode") String sendBillCode, Model model) {
        LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendBillCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());
        /*
         * if(RationConstant.RAT_COMMON_Y.equals(loanInfoVo.getRatmodel())) { model.addAttribute("urationDTO",
         * urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, loanInfoVo.getId()));//授信明细 }
         */
        // 设置银团借款信息
        cmsProvideLoanSendService.setSyndicatedSend(loanInfoVo.getPlClass(), loanSendVo.getId(), model);
        BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendBillCode, "1");// 在途金额
        BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendBillCode, "2");// 已还金额
        // 计息方式信息 暂存
        /*
         * if (EmptyUtils.isBlank(loanInfoVo.getInterestWayName())) { AccrualTypeVo accrualTypeVo =
         * accrualTypeService.queryAccrualTypeByCode(loanSendVo.getInterestWay());
         * loanSendVo.setInterestWayName(accrualTypeVo.getAccrualName()); }
         */
        model.addAttribute("loanSend", loanSendVo);
        model.addAttribute("loanInfo", loanInfoVo);
        model.addAttribute("repayPassage", repayPassage);
        model.addAttribute("repayFinish", repayFinish);
        return "cms/provide/send/viewLoanSend";
    }
    /**
     * 根据单位ID和业务类型判断是否挂预算
     *
     * @param corpId
     * @return
     */
    @PostMapping("/isBudget")
    @ResponseBody
    public AjaxJson isBudget(String corpId, String briefId1, String briefId2) {
        boolean isBudget = cmsProvideLoanSendService.isBudget(corpId, briefId1, briefId2);
        AjaxJson json = new AjaxJson();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("isBudget", isBudget);
        json.setAttributes(attributes);
        json.setSuccess(true);
        return json;
    }
    /********************* 审批begin *********************/
    /**
     * 银行借款发放审批 - 列表页面跳转
     */
    @LogMessage(content = "查询银行借款发放审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listSendApprove")
    public String listSendApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/send/approve/listSendApprove";
    }
    /**
     * 银行借款发放审批 - 数据
     */
    @PostMapping("/dataTableSendAppvoer")
    @ResponseBody
    public DataTablesOutput dataTableSendAppvoer(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideLoanSendService.getDataSendAppvoerTables(dataTablesInput);
    }
    /**
     * 银行借款发放审批 - 详情页面
     */
    @GetMapping("/viewSendApprove")
    public String viewSendApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanSend send = cmsProvideLoanSendService.get(businessId);
            model.addAttribute("send", send);
        }
        model.addAttribute("budgetSel", configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
        model.addAttribute("sendItem", configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
        model.addAttribute("taskId", taskId);
        return "cms/provide/send/approve/viewSendApprove";
    }
    /**
     * 银行借款发放审批 - 详情页面
     */
    @GetMapping("/mobileSendApprove")
    public String mobileSendApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanSend send = cmsProvideLoanSendService.get(businessId);
            if (send != null) {
                LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(send.getBillCode());
                LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());
                /*
                 * if(RationConstant.RAT_COMMON_Y.equals(loanInfoVo.getRatmodel())) { model.addAttribute("urationDTO",
                 * urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, loanInfoVo.getId()));//授信明细 }
                 */
                BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(send.getBillCode(),
                        "1");// 在途金额
                BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(send.getBillCode(),
                        "2");// 已还金额
                List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                        BudgetConstant.BUSINESS_NAME_PROVIDE_SEND, BudgetConstant.PROCESS_SYSNAME_CMS,
                        loanSendVo.getBillCode());
                if (!EmptyUtils.isEmptyList(list)) {
                    model.addAttribute("ProcessDate", list.get(0).getProcessDate());
                }
                this.initConfig(model);
                model.addAttribute("loanSend", loanSendVo);
                model.addAttribute("loanInfo", loanInfoVo);
                model.addAttribute("repayPassage", repayPassage);
                model.addAttribute("repayFinish", repayFinish);
            }
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/send/approve/mobileViewSendApprove";
    }
    /********************* 审批end *********************/
    /**
     * 查看银行借款详情
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/viewLoanInfo")
    public String viewPerfectlyLoanInfo(String id, Model model) {
        if (EmptyUtils.isNotEmpty(id)) {
            ProvideLoanSend loanSend = cmsProvideLoanSendService.get(id);
            String infoBillCode = loanSend.getLoanCode();
            String sendBillCode = loanSend.getBillCode();
            if (EmptyUtils.isNotEmpty(infoBillCode)) {
                LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(infoBillCode);
                model.addAttribute("loanInfo", infoVO);
                // 授信设置
                CorpDTO corpDTO = corpService.get(infoVO.getCorpId());
                ProvideUtils.setRatParam(model, loanInfoService.getRelateRat().getValue(), infoVO.getRatmodel(),
                        loanInfoService.getIsRatOpen().getValue(), infoVO.getId(), urationService,
                        btCurrencyServiceImpl.getObjectById(corpDTO.getCurId()));
                if (infoVO.getAcrossSend() != null && infoVO.getAcrossSend().equals("1")) {
                    if (EmptyUtils.isNotEmpty(infoBillCode) && !"null".equals(infoBillCode)) {
                        List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
                        list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                                BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION, BudgetConstant.PROCESS_SYSNAME_CMS,
                                infoBillCode);
                        if (!EmptyUtils.isEmptyList(list)) {
                            model.addAttribute("ProcessDate", list.get(0).getProcessDate());
                        }
                        // 资金预算
                        this.initConfig(model);
                        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION);
                    }
                }
                // 担保参数设置
                String isGuarantee = configServiceImpl
                        .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA).getValue();
                CmsProvideLoanInfo cmsProvideLoanInfo = new CmsProvideLoanInfo();
                BeanUtils.copyNotNullProperties(infoVO, cmsProvideLoanInfo);
                ProvideUtils.setGuaParams(model, "1", isGuarantee, cmsProvideLoanInfo, guaWayServie,
                        guaContractOccupServie);
                // 第三方费率信息
                List<DfIntermediateCostVO> costVOS = loanInfoService
                        .listIntermediateCostByDfId(cmsProvideLoanInfo.getId());
                model.addAttribute("costVOS", costVOS);
            }
            if (EmptyUtils.isNotEmpty(sendBillCode)) {
                LoanSendVO sendVO = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendBillCode);
                List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                        BudgetConstant.BUSINESS_NAME_PROVIDE_SEND, BudgetConstant.PROCESS_SYSNAME_CMS, sendBillCode);
                model.addAttribute("loanSend", sendVO);
                if (!EmptyUtils.isEmptyList(list)) {
                    model.addAttribute("ProcessDate", list.get(0).getProcessDate());
                }
                model.addAttribute("sendBillCode", sendBillCode);
                BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendBillCode, "1");// 在途金额
                BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendBillCode, "2");// 已还金额
                model.addAttribute("repayPassage", repayPassage);
                model.addAttribute("repayFinish", repayFinish);
                // 资金预算
                this.initConfig(model);
                model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
            }
        }
        model.addAttribute("show", "0");// 显示按钮
        return "cms/provide/info/viewPerfectlyLoanInfo";
    }
    /**
     * 功能介绍 : 查询借款发放完成的数据
     */
    @GetMapping("/proLoanSendData")
    public String proLoanSendData(String id, String row, Model model) {
        model.addAttribute("id", id);
        BisAccDtlDTO bisAccDtlDTO = iBisAccDtlService.getById(id);
        model.addAttribute("bisDTO", bisAccDtlDTO);
        model.addAttribute("row", row);
        if (null != bisAccDtlDTO) {
            BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccByBankAccAndCurId(bisAccDtlDTO.getBankAcc(),
                    bisAccDtlDTO.getCurId());// 根据账号和币别获取账户信息
            model.addAttribute("bankAccDTO", bankAccDTO);
            if (null != bankAccDTO && StringUtils.isNotEmpty(bankAccDTO.getBankTypeId())) {
                String bankTypeName = iBankTypeService.getObjectById(bankAccDTO.getBankTypeId()).getTypeName();
                model.addAttribute("bankTypeName", bankTypeName);
            }
        }
        return "cms/provide/send/listQueryLoanSend";
    }
    /**
     * 功能介绍 : 查询借款发放完成的数据
     */
    @PostMapping("/proLoanSendDataTables")
    @ResponseBody
    public DataTablesOutput queryLoanInfoData(@RequestBody DataTablesInput input) {
        return cmsProvideLoanSendService.getDataSendOverTables(input);
    }
    /**
     * 利息计算（开发测算利息时使用）
     *
     * @param sendId
     * @return
     */
    @PostMapping("/getInterest")
    @ResponseBody
    public List<CmsProvideInterest> calInterest(String sendId) {
        if (StringUtils.isEmpty(sendId)) {
            return new ArrayList<>();
        }
        ProvideLoanSend provideLoanSend = cmsProvideLoanSendService.get(sendId);
        cmsProvideInterestService.adjustProvideInterest(provideLoanSend, getCurrentUser());
        return cmsProvideInterestService.queryInterestListByBillCode(provideLoanSend.getBillCode());
    }
    /**
     * 查看计算的利息数据（开发测算利息时使用）
     *
     * @param sendId
     * @param model
     * @return
     */
    @GetMapping("/viewInterest")
    public String viewInterest(String sendId, Model model) {
        model.addAttribute("sendId", sendId);
        return "cms/provide/send/viewInterest";
    }
}
