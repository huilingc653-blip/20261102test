package com.bytter.finance.web.provide.controller;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.basedata.service.LoanTypeService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.service.CmsProvideHistoryService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRepayService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.utils.ProvideUtils;
import com.bytter.finance.domain.provide.vo.*;
import com.bytter.finance.dto.LoanTypeDTO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.constant.FcBondConstant;
import com.bytter.guarantee.dto.FcBondApplyDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.BtPurposeConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.dto.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 借款信息表
 * @date 2017-07-20 10:45:09
 */
@Controller
@RequestMapping("/cms/provide/loaninfo")
public class LoanInfoController extends BaseController {
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private LoanTypeService loanTypeServiceImpl;
    @Autowired
    private CmsProvideHistoryService cmsProvideHistoryService;
    @DubboReference(check = false)
    private IBtBankService btBankServiceImpl;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @Autowired
    private IRatUrationService urationService;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private CmsProvideLoanRepayService cmsProvideLoanRepayService;
    @Autowired
    private CmsProvideLoanSendService cmsProvideLoanSendService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService rateAdjustService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Autowired
    private IGuaWayServie guaWayServie;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @Autowired
    private RepayTypeService repayTypeService;

    /**
     * 新增页面跳转
     *
     * @return
     */
    @GetMapping("/addLoanInfo")
    public String addLoanInfo(CmsProvideLoanInfo cmsProvideLoanInfo, Model model) {
        if (StringUtils.isNotEmpty(cmsProvideLoanInfo.getId())) {
            cmsProvideLoanInfo = loanInfoService.get(cmsProvideLoanInfo.getId());
            model.addAttribute("cmsProvideLoanInfoPage", cmsProvideLoanInfo);
        }
        model.addAttribute("inputDate", new Date());
        model.addAttribute("viewFlag", "0");
        ConfigDTO config = loanInfoService.getRelateRat();
        model.addAttribute("relateRat", config.getValue());// 挂接授信
        model.addAttribute("isGuarantee",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));// 担保
        model.addAttribute("configIsHookBond",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_HOOK_BOND));// 保证金
        model.addAttribute("configIsOpenRat",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_OPEN_RAT));// 是否启用敞口
        model.addAttribute("pledgeConfig",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_RELATE_PLEDGE));// 是否挂抵质押物
        model.addAttribute("ratBankConfig", urationService.getRatBankCtlConfig());// 授信银行控制
        // 是否挂资金计划控制
        model.addAttribute("budgetConfig",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET));
        return "cms/provide/info/addLoanInfo";
    }

    /**
     * 修改列表-页面跳转
     */
    @LogMessage(content = "查询借款信息表修改列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listUpdate")
    public String listUpdate(Model model) {
        return "cms/provide/info/listUpdateLoanInfo";
    }

    /**
     * 银行借款变更列表
     *
     * @param model
     * @return
     */
    @LogMessage(content = "银行借款变更", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listChange")
    public String listChange(Model model) {
        return "cms/provide/info/listChangeLoanInfo";
    }

    /**
     * 银行借款变更
     *
     * @param model
     * @return
     */
    @LogMessage(content = "银行借款变更", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/changeLoanInfo")
    public String changeLoanInfo(Model model, String infoBillCode) {
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
                list = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION,
                        BudgetConstant.PROCESS_SYSNAME_CMS, infoBillCode);
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
        ProvideUtils.setGuaParams(model, "1", isGuarantee, cmsProvideLoanInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = loanInfoService.listIntermediateCostByDfId(cmsProvideLoanInfo.getId());
        model.addAttribute("costVOS", costVOS);
        // 抵质押参数
        String pledgeConfig = configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_RELATE_PLEDGE).getValue();
        ProvideUtils.setPledgeParam(model, pledgeConfig, cmsProvideLoanInfo, pledgeOccupyService);
        // 保证金参数
        String isBond = configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_HOOK_BOND)
                .getValue();
        model.addAttribute("isBond", isBond);
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
        initConfig(model);
        return "cms/provide/info/changeLoanInfo";
    }

    /**
     * 银行借款变更
     */
    @PostMapping("/listChangeDataTable")
    @ResponseBody
    public DataTablesOutput listChangeDataTable(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.listChangeDataTable(dataTablesInput, getCurrentUser());
    }

    @PostMapping("/saveChange")
    @ResponseBody
    public AjaxJson saveChange(LoanInfoVO loanInfoVO) {
        loanInfoService.changeLoan(loanInfoVO);
        return success();
    }

    /**
     * 修改列表 - 数据
     */
    @PostMapping("/datatableUpdate")
    @ResponseBody
    public DataTablesOutput datatableUpdate(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.getDataUpdateTables(dataTablesInput, ResourceConstant.CMS_PROVIDE_INFO_QUERY,
                getCurrentUser());
    }

    /**
     * 补录列表-页面跳转
     */
    @LogMessage(content = "查询借款信息补录列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepair")
    public String listRepair(Model model) {
        return "cms/provide/info/listRepairLoanInfo";
    }

    /**
     * 补录列表 - 数据
     */
    @PostMapping("/datatableRepair")
    @ResponseBody
    public DataTablesOutput datatableRepair(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.getDataRepairTables(dataTablesInput, getCurrentUser());
    }

    /**
     * 借款查询-页面跳转
     */
    @LogMessage(content = "查询借款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryLoan")
    public String queryLoan(Model model) {
        String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON).getValue();// 是否对接合同管理
        model.addAttribute("relateCon", relateCon);
        return "cms/provide/listQueryLoan";
    }

    /**
     * 功能介绍 : 借款查询 - 数据<br>
     */
    @PostMapping("/queryLoanDatatable")
    @ResponseBody
    public DataTablesOutput queryLoanDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.queryLoan(dataTablesInput, getCurrentUser());
    }

    /**
     * 修改-页面跳转
     */
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return null;
        }
        CmsProvideLoanInfo cmsProvideLoanInfo = loanInfoService.get(id);
        // 采用新的计提计息方式，编辑时，如果计提为空则还款方式也置空。重新填写
        if (StringUtils.isEmpty(cmsProvideLoanInfo.getAccrualType())) {
            cmsProvideLoanInfo.setRepayType(null);
        } else {
            model.addAttribute("repayType", repayTypeService.get(cmsProvideLoanInfo.getRepayType()));
        }
        CorpDTO corpDTO = corpService.get(cmsProvideLoanInfo.getCorpId());
        model.addAttribute("loanInfo", cmsProvideLoanInfo);
        model.addAttribute("corp", corpDTO);
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.addAttribute("baseCurId", cur.getId());
        BtBankDTO bank = btBankServiceImpl.getByBankCode(cmsProvideLoanInfo.getBankId());
        model.addAttribute("showBankName", bank.getBankName());
        model.addAttribute("showBankName1", bank.getBankCode());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsProvideLoanInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.addAttribute("tbBaseRate", tbBaseRate);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(
                cmsProvideLoanInfo.getCurId(), cmsProvideLoanInfo.getReferenceRateType(),
                cmsProvideLoanInfo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        // 授信设置
        ProvideUtils.setRatParam(model, loanInfoService.getRelateRat().getValue(), cmsProvideLoanInfo.getRatmodel(),
                loanInfoService.getIsRatOpen().getValue(), cmsProvideLoanInfo.getId(), urationService,
                btCurrencyServiceImpl.getObjectById(corpDTO.getCurId()));
        // 查询发放的 还款计划
        CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(id);
        if (crs != null) {
            model.addAttribute("planId", crs.getId());
        }
        String isBond = configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_HOOK_BOND)
                .getValue();
        // 保证金参数设置
        ProvideUtils.setBondParam(model, isBond, cmsProvideLoanInfo, fcBondHookService);
        // 担保参数设置
        String isGuarantee = configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA).getValue();
        ProvideUtils.setGuaParams(model, "0", isGuarantee, cmsProvideLoanInfo, guaWayServie, guaContractOccupServie);
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = loanInfoService.listIntermediateCostByDfId(cmsProvideLoanInfo.getId());
        model.addAttribute("costVOS", costVOS);
        // 抵质押参数
        String pledgeConfig = configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_RELATE_PLEDGE).getValue();
        ProvideUtils.setPledgeParam(model, pledgeConfig, cmsProvideLoanInfo, pledgeOccupyService);
        // 是否挂资金计划控制
        model.addAttribute("budgetConfig",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET));
        return "cms/provide/info/updateLoanInfo";
    }

    /**
     * 补录-页面跳转
     */
    @GetMapping("/repair")
    public String repair(String id, Model model) {
        if (StringUtils.isEmpty(id) || "undefined".equalsIgnoreCase(id)) {
            return null;
        }
        LoanInfoVO vo = loanInfoService.getLoanInfoVOById(id);
        // 采用新的计提计息方式，编辑时，如果计提为空则还款方式也置空。重新填写
        if (StringUtils.isEmpty(vo.getAccrualType())) {
            vo.setRepayType(null);
        } else {
            model.addAttribute("repayType", repayTypeService.get(vo.getRepayType()));
        }
        vo.setPlMoney(new BigDecimal(vo.getPlMoney().doubleValue()));
        model.addAttribute("loanInfo", vo);
        CorpDTO corpDTO = corpService.get(vo.getCorpId());
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.addAttribute("baseCurId", cur.getId());
        // model.addAttribute("baseCurId", corpDTO.getCurId());
        // 获取本位币
        model.addAttribute("natualCurId", corpDTO.getCurId());
        BtBankDTO bank = btBankServiceImpl.getByBankCode(vo.getBankId());
        model.addAttribute("showBankName", bank.getBankName());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(vo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.addAttribute("tbBaseRate", tbBaseRate);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintence(vo.getCurId(),
                vo.getReferenceRateType(), vo.getRepayCycle());
        if (null != tbLoanRateMaintence) {
            model.addAttribute("tbLoanRateMaintence", tbLoanRateMaintence);
        }
        // 查询发放的 还款计划
        CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(id);
        if (crs != null) {
            model.addAttribute("planId", crs.getId());
        }
        this.setLoanConfig(model, vo);// 银行借款补录页面参数设置
        // 第三方费率信息
        List<DfIntermediateCostVO> costVOS = loanInfoService.listIntermediateCostByDfId(vo.getId());
        model.addAttribute("costVOS", costVOS);
        // 是否挂资金计划控制
        model.addAttribute("budgetConfig",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET));
        return "cms/provide/info/repairLoanInfo";
    }

    /**
     * 借款利率调整-页面跳转
     */
    @LogMessage(content = "查询借款利率调整列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listLoanRate")
    public String listLoanRate(Model model) {
        return "cms/provide/info/listCmsProvideLoanRate";
    }

    /**
     * 借款利率调整 - 数据
     */
    @PostMapping("/datatableLoanRate")
    @ResponseBody
    public DataTablesOutput datatableLoanRate(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.getDataLoanRateTables(dataTablesInput, getCurrentUser());
    }

    /**
     * 加载借款INFO信息
     */
    // @LogMessage(content = "查询借款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewLoanInfo/{billCode}")
    public String viewLoanInfo(@PathVariable("billCode") String billCode, Model model) {
        LoanInfoVO vo = loanInfoService.getLoanInfoVOByBillCode(billCode);
        model.addAttribute("loanInfo", vo);
        model.addAttribute("gua",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));
        return "cms/provide/info/viewLoanInfo";
    }

    /**
     * 加载借款完整的INFO信息
     */
    // @LogMessage(content = "查询借款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewPerfectlyLoanInfo")
    public String viewPerfectlyLoanInfo(String infoBillCode, String sendBillCode, String show, String enableUpload,
                                        Model model) {
        if (EmptyUtils.isNotEmpty(infoBillCode) && !"null".equals(infoBillCode)) {
            LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(infoBillCode);
            // 设置银团借款数据
            loanInfoService.setSyndicated(infoVO.getPlClass(), infoVO.getId(), model);
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
            List<DfIntermediateCostVO> costVOS = loanInfoService.listIntermediateCostByDfId(cmsProvideLoanInfo.getId());
            model.addAttribute("costVOS", costVOS);
            // 抵质押参数
            String pledgeConfig = configServiceImpl
                    .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_RELATE_PLEDGE).getValue();
            ProvideUtils.setPledgeParam(model, pledgeConfig, cmsProvideLoanInfo, pledgeOccupyService);
            // 保证金参数
            String isBond = configServiceImpl
                    .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_HOOK_BOND).getValue();
            model.addAttribute("isBond", isBond);
        }
        if (EmptyUtils.isNotEmpty(sendBillCode) && !"null".equals(sendBillCode)) {
            LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(infoBillCode);
            LoanSendVO sendVO = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendBillCode);
            List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                    BudgetConstant.BUSINESS_NAME_PROVIDE_SEND, BudgetConstant.PROCESS_SYSNAME_CMS, sendBillCode);
            model.addAttribute("loanSend", sendVO);
            cmsProvideLoanSendService.setSyndicatedSend(infoVO.getPlClass(), sendVO.getId(), model);
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
        model.addAttribute("show", show);// 显示按钮
        model.addAttribute("enableUpload", enableUpload);// 是否允许上传附件
        return "cms/provide/info/viewPerfectlyLoanInfo";
    }

    /**
     * 初始化资金计划系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        // 0为不挂接资金计划 现在需求要求不挂接
        model.addAttribute("budgetSel", configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
        model.addAttribute("sendItem", configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
    }

    /**
     * 提交
     */
    @LogMessage(content = "添加借款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    @ResponseBody
    public AjaxJson tempOrSubmit(LoanInfoVO vo, String saveType, String myflag, RatUrationDTO urationDTO) {
        if (StringUtils.isEmpty(vo.getId())) {
            vo.setBillCode(loanInfoService.getLoanInfoBillCode());
        }
        loanInfoService.tempOrSubmit(vo, saveType, myflag, urationDTO, getCurrentUser());
        return success();
    }

    @LogMessage(content = "删除借款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        loanInfoService.delLoanInfo(id, getCurrentUser());
        return success();
    }

    /**
     * 补录提交
     */
    @LogMessage(content = "提交借款补录申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/repairTempOrSubmit")
    @ResponseBody
    public AjaxJson repairTempOrSubmit(LoanInfoVO vo, String saveType, RatUrationDTO urationDTO) {
        loanInfoService.repairTempOrSubmit(vo, saveType, urationDTO, getCurrentUser());
        return success();
    }

    /**
     * 补录打回
     */
    @LogMessage(content = "提交借款补录申请", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/repairBack")
    @ResponseBody
    public AjaxJson repairBack(String id, String refuseWhy) {
        loanInfoService.repairBack(id, refuseWhy, getCurrentUser());
        return success();
    }
    /******************** 审批 **********************/
    /**
     * 申请审批列表-页面跳转
     */
    @LogMessage(content = "查询借款申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listInfoApprove")
    public String listInfoApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/info/approve/listInfoApprove";
    }

    /**
     * 申请审批列表 - 数据
     */
    @PostMapping("/datatableInfoApprove")
    @ResponseBody
    public DataTablesOutput datatableInfoApprove(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.getDataInfoApproveTables(dataTablesInput);
    }

    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoApprove")
    public String infoApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanInfo info = loanInfoService.get(businessId);
            model.addAttribute("loanInfo", info);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/info/approve/viewInfoApprove";
    }

    /**
     * 补录审批列表-页面跳转
     */
    // @LogMessage(content = "查询借款信息补录列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepairApprove")
    public String listRepairApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "cms/provide/info/approve/listRepairApprove";
    }

    /**
     * 补录审批列表 - 数据
     */
    @PostMapping("/datatableRepairApprove")
    @ResponseBody
    public DataTablesOutput datatableRepairApprove(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.getDataRepairApproveTables(dataTablesInput);
    }

    /**
     * 补录审批 详情页面
     */
    @GetMapping("/infoRepairApprove")
    public String infoRepairApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanInfo info = loanInfoService.get(businessId);
            model.addAttribute("loanInfo", info);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/info/approve/viewInfoRepairApprove";
    }

    /**
     * 手机 申请、补录审批
     */
    @GetMapping("/mobileInfoApprove")
    public String mobileInfoApprove(String businessId, String taskId, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanInfo info = loanInfoService.get(businessId);
            LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(info.getBillCode());
            model.addAttribute("plMoneyUpper", StringUtils.chineseMoney(info.getPlMoney()));
            model.addAttribute("loanInfo", infoVO);
            // 授信数据设置
            CorpDTO corpDTO = corpService.get(infoVO.getCorpId());
            ProvideUtils.setRatParam(model, loanInfoService.getRelateRat().getValue(), infoVO.getRatmodel(),
                    loanInfoService.getIsRatOpen().getValue(), infoVO.getId(), urationService,
                    btCurrencyServiceImpl.getObjectById(corpDTO.getCurId()));
            // 资金预算
            List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                    BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION, BudgetConstant.PROCESS_SYSNAME_CMS,
                    infoVO.getBillCode());
            if (!EmptyUtils.isEmptyList(list)) {
                model.addAttribute("ProcessDate", list.get(0).getProcessDate());
            }
            this.initConfig(model);
            if (infoVO.getAcrossSend() != null && infoVO.getAcrossSend().equals("1")) {// 跳过发放
                model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION);
            } /*
             * else{//默认不跳过发放 model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_SEND); }
             */
            // 保证金参数设置
            // 通过占用保证金的项目Id查询保证金的占用信息
            String isBond = configServiceImpl
                    .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_HOOK_BOND).getValue();
            ProvideUtils.setBondParam(model, isBond, info, fcBondHookService);
            // 担保参数设置
            String isGuarantee = configServiceImpl
                    .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA).getValue();
            ProvideUtils.setGuaParams(model, "1", isGuarantee, info, guaWayServie, guaContractOccupServie);
        }
        model.addAttribute("taskId", taskId);// 必须要做此步骤
        return "cms/provide/info/approve/mobileViewInfoApprove";
    }
    /**************** ajax获取数据 ******************/
    /**
     * 功能介绍 : 获取借款类型<br>
     */
    @GetMapping("/queryLoanTypeList")
    @ResponseBody
    public List<LoanTypeDTO> queryLoanTypeList() {
        return loanTypeServiceImpl.queryLoanTypeList(BaseDataConstant.TYPE_BORROW);
    }

    /**
     * 功能介绍：根据银行类型获取银行支行信息
     */
    @GetMapping("/queryBtBankByType/{typeId}")
    @ResponseBody
    public List<BtBankDTO> queryBtBankByType(@PathVariable("typeId") String typeId) {
        if (EmptyUtils.isNotEmpty(typeId) && !typeId.equals("null")) {
            List<BtBankDTO> list = btBankServiceImpl.getByBankTypeId(typeId);
            if (list != null) {
                return list;
            }
        }
        return new ArrayList<BtBankDTO>();
    }

    /**
     * 功能介绍：获取计息日期
     */
    @GetMapping("/queryBatchInstDay")
    @ResponseBody
    public String queryBatchInstDay() {
        ConfigDTO dto = configServiceImpl.getConfig(ConfigConstant.IST, "batchInstDay");
        return dto.getValue();
    }

    /**
     * 功能介绍 : 获取币别指定日期的最新汇率。<br>
     * <b>日期：</b>2017年10月16日<br>
     *
     * @version 1.0
     * @author liyh
     */
    @PostMapping("/getNearCurRat")
    @ResponseBody
    public AjaxJson getNearCurRat(String curId, String convertCurId, Date plDate) {
        AjaxJson json = new AjaxJson();
        json.setSuccess(true);
        if (EmptyUtils.isEmpty(plDate)) {
            plDate = DateUtils.getDate();
        }
        json.setObj(rateAdjustService.getExchangeRateBy(curId, convertCurId, DateUtils.setSpecifiedDateOfTime(plDate),
                FcExcrateAdjustConstant.RATE_TYPE_CONVERT));
        return json;
    }

    @GetMapping("/getPurpose")
    @ResponseBody
    public List<BtPurposeDTO> listByCorpIdOrBusType(String corpId, String sysBusType) {
        List<BtPurposeDTO> btPurposeDTOs = new ArrayList<BtPurposeDTO>();
        if (EmptyUtils.isEmpty(corpId) || "null".equals(corpId)) {
            return new ArrayList<BtPurposeDTO>();
        }
        btPurposeDTOs = loanInfoService.listByCorpIdOrBusType(corpId, BtPurposeConstant.SYS_BUS_TYPE_CMS);
        if (EmptyUtils.isEmptyList(btPurposeDTOs)) {
            return new ArrayList<BtPurposeDTO>();
        } else {
            return btPurposeDTOs;
        }
    }

    /**
     * 获取授权的银行信息
     **/
    @GetMapping("/getBankName")
    @ResponseBody
    public List<BtBankDTO> listByRatBankName(String corpId) {
        return loanInfoService.queryRationBank(corpId);
    }

    /**
     * 改为返回挂接的保证金信息
     *
     * @param rateCode
     * @return
     */
    @PostMapping("/checkRateCode")
    @ResponseBody
    public Map<String, Object> checkRatHookBond(String rateCode) {
        Map<String, Object> returnData = new HashMap<>();
        FcBondApplyDTO dto = loanInfoService.checkRatHookBond(rateCode);
        if (null != dto) {
            returnData.put("isHook", FcBondConstant.IS_HOOK_Y);
            returnData.put("data", dto);
        } else {
            returnData.put("isHook", FcBondConstant.IS_HOOK_N);
        }
        return returnData;
    }

    /**
     * 根据id查看
     *
     * @param id
     * @param sendBillCode
     * @param show
     * @param model
     * @return
     */
    @LogMessage(content = "查询借款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewProvideLoanInfoById")
    public String viewPerfectlyLoanInfoById(String id, String sendBillCode, String show, Model model) {
        CmsProvideLoanInfo cmsProvideLoanInfo = loanInfoService.get(id);
        return viewPerfectlyLoanInfo(cmsProvideLoanInfo.getBillCode(), sendBillCode, show, null, model);
    }

    /**
     * 获取保证金可选币别信息
     *
     * @param billCurId 业务币别
     * @return 币别集合
     */
    @GetMapping("/getBondCurList")
    @ResponseBody
    public List<BtCurrencyDTO> getBondCurList(String billCurId, String corpId) {
        List<BtCurrencyDTO> btCurrencyDTOS = btCurrencyService.getBondCurList(billCurId, corpId);
        return btCurrencyDTOS;
    }

    /**
     * 银行借款补录页面参数设置
     */
    public void setLoanConfig(Model model, LoanInfoVO vo) {
        // 授信设置
        CorpDTO corpDTO = corpService.get(vo.getCorpId());
        ProvideUtils.setRatParam(model, loanInfoService.getRelateRat().getValue(), vo.getRatmodel(),
                loanInfoService.getIsRatOpen().getValue(), vo.getId(), urationService,
                btCurrencyServiceImpl.getObjectById(corpDTO.getCurId()));
        // 资金预算
        this.initConfig(model);
        if (vo.getAcrossSend() != null && vo.getAcrossSend().equals("1")) {// 跳过发放
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION);
        } else {// 默认不跳过发放
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
        }
        // 通过占用保证金的项目Id查询保证金的占用信息
        String isBond = configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_HOOK_BOND)
                .getValue();
        CmsProvideLoanInfo cmsProvideLoanInfo = loanInfoService.get(vo.getId());
        // 保证金参数设置
        ProvideUtils.setBondParam(model, isBond, cmsProvideLoanInfo, fcBondHookService);
        // 担保参数设置
        String isGuarantee = configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA).getValue();
        ProvideUtils.setGuaParams(model, "0", isGuarantee, cmsProvideLoanInfo, guaWayServie, guaContractOccupServie);
        // 抵质押参数
        String pledgeConfig = configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_RELATE_PLEDGE).getValue();
        ProvideUtils.setPledgeParam(model, pledgeConfig, cmsProvideLoanInfo, pledgeOccupyService);
        String relateCon = configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON)
                .getValue();// 是否对接合同管理
        model.addAttribute("relateCon",
                configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON));
        if (relateCon.equals(CmsConstant.RELATED_CONTRACT_YES)) {
            model.addAttribute("relateConType",
                    configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE));// 对接合同管理管理模式
        }
        model.addAttribute("conSegment",
                configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_SEGMENT));// 履约环节
    }

    /**
     * 功能介绍 : 查询借款补录完成的数据
     */
    @PostMapping("/queryLoanInfoData")
    @ResponseBody
    public DataTablesOutput queryLoanInfoData(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.queryLoanInfoData(dataTablesInput, getCurrentUser());
    }

    /**
     * 新增的相关银行APP借款查询的报表 借款查询-app报表页面跳转
     */
    @LogMessage(content = "查询手机借款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @RequestMapping("/loanMobileQuery")
    public String mobileQuery(Model model) {
        List<BtCurrencyDTO> list = btCurrencyService.getBtCurrency();
        model.addAttribute("curList", list);
        return "cms/provide/mobile/mobileCmsLoanQuery";
    }

    /**
     * 新增的相关银行APP借款查询的报表 借款查询-app报表页面跳转
     */
    @LogMessage(content = "查询手机借款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/gcMoblile")
    public String gcMoblile(Model model) {
        List<BtCurrencyDTO> list = btCurrencyService.getBtCurrency();
        model.addAttribute("curList", list);
        return "cms/provide/mobile/mobileCmsLoanQuery";
    }

    /**
     * 新增的相关银行APP借款查询的报表 借款查询-app报表页面跳转
     */
    @PostMapping("/getAppData")
    @ResponseBody
    public Object getAppData(CmsLoanBiBusinessVo searchParams) {
        DataTablesInput dataTablesInput = new DataTablesInput();
        // 无分页的情况下设置数据
        dataTablesInput.setLength(10000);
        // 查询条件
        Map<String, Object> queryParms = new LinkedHashMap<String, Object>();
        if (null != searchParams) {
            if (!StringUtils.isEmpty(searchParams.getCorpIds())) {
                queryParms.put("ins_corpId", searchParams.getCorpIds());
            }
            if (!StringUtils.isBlank(searchParams.getCurId())) {
                queryParms.put("eqs_curId", searchParams.getCurId());
            }
            if (!StringUtils.isBlank(searchParams.getSendCode())) {
                queryParms.put("likes_infoBillCode", searchParams.getSendCode());
            }
            if (StringUtils.isNotEmpty(searchParams.getSendBeginDate())) {
                queryParms.put("ged_sendDate", DateUtils.formatDate(searchParams.getSendBeginDate()));
            }
            if (StringUtils.isNotEmpty(searchParams.getSendEndDate())) {
                queryParms.put("led_sendDate", DateUtils.formatDate(searchParams.getSendEndDate()));
            }
            if (StringUtils.isNotEmpty(searchParams.getOverBeginDate())) {
                queryParms.put("ged_repayDate", DateUtils.formatDate(searchParams.getOverBeginDate()));
            }
            if (StringUtils.isNotEmpty(searchParams.getOverEndDate())) {
                queryParms.put("led_repayDate", DateUtils.formatDate(searchParams.getOverEndDate()));
            }
        }
        dataTablesInput.setFormdata(queryParms);
        @SuppressWarnings("rawtypes")
        DataTablesOutput out = loanInfoService.queryLoanForMobile(dataTablesInput, this.getCurrentUser());
        @SuppressWarnings("unchecked")
        Object jsonObj = loanInfoService.appJsonObject(out.getData());
        return jsonObj;
    }

    /**
     * 借款详情页面
     */
    @RequestMapping("/loanMobileDetail")
    public String loanMobileDetail(String businessId, String sendCode, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(businessId);
            model.addAttribute("vo", infoVO);
            BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(businessId, "1");// 在途金额
            BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(businessId, "2");// 已还金额
            model.addAttribute("repayPassage", repayPassage);
            model.addAttribute("repayFinish", repayFinish);
            LoanSendVO sendVO = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendCode);
            model.addAttribute("sendVO", sendVO);
            DataTablesInput dataTablesInput = new DataTablesInput();
            // 无分页的情况下设置数据
            dataTablesInput.setLength(1);
            Map<String, Object> queryParms = new LinkedHashMap<String, Object>();
            queryParms.put("likes_infoBillCode", businessId);
            dataTablesInput.getFormdata().put("likes_infoBillCode", businessId);
            @SuppressWarnings("rawtypes")
            DataTablesOutput out = loanInfoService.queryLoanForMobile(dataTablesInput, this.getCurrentUser());
            @SuppressWarnings("unchecked")
            List<LoanQueryVO> list = out.getData();
            model.addAttribute("queryVo", ListUtils.isNotNullOrEmpty(list) ? list.get(0) : null);
            model.addAttribute("loanCode", businessId);
            model.addAttribute("sendCode", sendCode);
        }
        return "cms/provide/mobile/mobileCmsLoanQueryInfo";
    }

    /**
     * 借款利息明细
     */
    @RequestMapping("/loanMobileRatInfo")
    public String loanMobileRatInfo(String businessId, String sendCode, Model model) {
        if (!EmptyUtils.isEmpty(businessId)) {
            loanInfoService.loadRepayWithSendBillCode(sendCode, model);
        }
        return "cms/provide/mobile/mobileCmsLoanQueryRatInfo";
    }

    /**
     * 更新附件
     *
     * @param groupId
     * @param infoId
     * @return
     */
    @PostMapping("/setAttachmentId")
    @ResponseBody
    public AjaxJson setAttachmentId(String groupId, String infoId) {
        CmsProvideLoanInfo cmsProvideLoanInfo = loanInfoService.get(infoId);
        cmsProvideLoanInfo.setUploadId(groupId);
        loanInfoService.getEntityDao().update(cmsProvideLoanInfo);
        return success();
    }

    /**
     * 获取挂接担保合同的银行借款数据
     *
     * @param dataTablesInput 查询参数
     * @return 列表数据
     */
    @ResponseBody
    @PostMapping("/getGuaContract")
    public DataTablesOutput<GuaContractCmsProvideLoanVO> getUseChangelist(@RequestBody DataTablesInput dataTablesInput) {
        return loanInfoService.getGuaForInl(dataTablesInput, getCurrentUser());
    }

    @LogMessage(content = "查询月度贷款明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @RequestMapping("/monthlyLoanDtl")
    public String monthlyLoanDtl(MonthlyLoanVO queryParam, Model model) {
        // 设置月度贷款明细表查询参数
        this.setQueryParam(queryParam, model);
        // 查询月度贷款明细表
        List<MonthlyLoanVO> resultList = loanInfoService.queryMonthlyLoanDtlData(queryParam, getCurrentUser());
        model.addAttribute("queryList", resultList);
        return "cms/provide/listMonthlyLoanDtl";
    }

    /**
     * 月度贷款明细表查询参数
     *
     * @param queryParam 查询参数
     * @param model      model对象
     */
    private void setQueryParam(MonthlyLoanVO queryParam, Model model) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 获取当前日期
        LocalDate today = LocalDate.now();
        LocalDate nextMonday = today.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

        // 默认开始到期日期
        queryParam.setDefaultStartRepayDate(DateUtils.parse(nextMonday.format(formatter), "yyyy-MM-dd"));
        // 获取下周日的日期
        LocalDate nextSunday = nextMonday.plusDays(6); // 下周日是下周一之后的第六天
        queryParam.setDefaultEndRepayDate(DateUtils.parse(nextSunday.format(formatter), "yyyy-MM-dd"));

        if (EmptyUtils.isEmpty(queryParam.getStartRepayDate()) && EmptyUtils.isEmpty(queryParam.getQueryFlag())) {
            queryParam.setStartRepayDate(DateUtils.parse(nextMonday.format(formatter), "yyyy-MM-dd"));
        }
        if (EmptyUtils.isEmpty(queryParam.getEndRepayDate()) && EmptyUtils.isEmpty(queryParam.getQueryFlag())) {
            queryParam.setEndRepayDate(DateUtils.parse(nextSunday.format(formatter), "yyyy-MM-dd"));
        }
        model.addAttribute("queryParam", queryParam);
    }

    /**
     * 导出月度贷款明细表
     *
     * @param queryParam 查询参数
     * @param request    请求参数
     * @param response   相应参数
     * @param model      model对象
     * @throws Exception 异常
     */
    @PostMapping("/exportMonthlyLoanDtl")
    public void exportMonthlyLoanDtl(MonthlyLoanVO queryParam, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
        String filename = "月度贷款明细表.xls";
        if (ServletUtils.isIE(request)) {// 判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        // 设置月度贷款明细表查询参数
        this.setQueryParam(queryParam, model);
        // 查询月度贷款明细表
        List<MonthlyLoanVO> resultList = loanInfoService.queryMonthlyLoanDtlData(queryParam, getCurrentUser());
        //导出数据
        HSSFWorkbook workbook = loanInfoService.exportMonthlyLoanDtl(resultList);

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }

    @RequestMapping("/queryPaymentAccList")
    @ResponseBody
    public List<BtBankAccDTO> queryPaymentAccList() {
        return loanInfoService.queryPaymentAccList(getCurrentUser());
    }

    /**
     * 历史借款查询-页面跳转
     */
    @LogMessage(content = "查询借款信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryHistoryLoan")
    public String queryHistoryLoan(Model model,Date reportDate) {
        String relateCon = configService.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON).getValue();//是否对接合同管理
        model.addAttribute("relateCon", relateCon);
        model.addAttribute("reportDate",reportDate);
        Date maxDate=DateUtils.addDay(new Date(),-1);
        model.addAttribute("maxDate",maxDate);
        return "cms/report/hisQueryReport/listQueryHistoryLoan";
    }

    /**
     * 功能介绍 : 借款查询 - 数据<br>
     */
    @PostMapping("/queryHistoryLoanDatatable")
    @ResponseBody
    public DataTablesOutput queryHistoryLoanDatatable(@RequestBody DataTablesInput dataTablesInput) {
        return cmsProvideHistoryService.queryHistoryLoan(dataTablesInput, getCurrentUser());
    }
}
