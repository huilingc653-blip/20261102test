package com.bytter.finance.saas.controller.provide;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.constant.ResourceConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.LoanRateMaintenceService;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.provide.entity.CmsLoanInfoViewPageVO;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.service.LoanInfoApplyService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.utils.ProvideUtils;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.v1.LoanInfoApplySubmitVO;
import com.bytter.guarantee.api.IFcBondHookService;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaPledgeOccupyService;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.*;
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Api(tags = "借款申请", value = "/v1-preposebill/cms/provide/loanInfoApply")
@RestController("v1LoanInfoApplyController")
@RequestMapping("/v1-preposebill/cms/provide/loanInfoApply")
public class LoanInfoApplyController extends BaseController {
    @Autowired
    private LoanInfoService loanInfoService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @Resource(name = "guaContractOccupServieImpl")
    private IGuaContractOccupServie guaContractOccupServie;
    @Autowired
    private RepaymentScheduleService repaymentScheduleService;
    @Autowired
    private LoanRateMaintenceService loanRateMaintenceService;
    @Autowired
    private LoanInfoApplyService loanInfoApplyService;
    @Autowired
    private IGuaWayServie guaWayServie;
    @DubboReference(check = false)
    private IConfigService configServiceImpl;
    @Resource(name = "fcBondHookServiceImp")
    private IFcBondHookService fcBondHookService;
    @Autowired
    private IRatUrationService urationService;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyServiceImpl;
    @DubboReference(check = false)
    private IBtBankService btBankServiceImpl;
    @DubboReference(check = false)
    private IBaseRateService iBaseRateService;
    @Resource(name = "guaPledgeOccupyServiceImpl")
    private IGuaPledgeOccupyService pledgeOccupyService;
    @Autowired
    private RepayTypeService repayTypeService;


    /**
     * 修改列表 - 数据
     */
    @PostMapping("/datatableUpdate")
    public AjaxJson<DataTablesOutput> datatableUpdate(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(loanInfoService.getDataUpdateTables(input, ResourceConstant.CMS_LOANINFO_APPLY_VIEW,
                getCurrentUser()));
    }

    /**
     * 新增页面跳转
     *
     * @return
     */
    @GetMapping("/addLoanInfo")
    public AjaxJson<Map<String, Object>> addLoanInfo(CmsProvideLoanInfo cmsProvideLoanInfo) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(cmsProvideLoanInfo.getId())) {
            cmsProvideLoanInfo = loanInfoService.get(cmsProvideLoanInfo.getId());
            model.put("cmsProvideLoanInfoPage", cmsProvideLoanInfo);
        }
        model.put("inputDate", new Date());
        model.put("viewFlag", "0");
        ConfigDTO config = loanInfoService.getRelateRat();
        model.put("relateRat", config.getValue());// 挂接授信
        model.put("isGuarantee",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));// 担保
        model.put("configIsHookBond",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_HOOK_BOND));// 保证金
        model.put("configIsOpenRat",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_OPEN_RAT));// 是否启用敞口
        model.put("pledgeConfig",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_RELATE_PLEDGE));// 是否挂抵质押物
        model.put("ratBankConfig", urationService.getRatBankCtlConfig());// 授信银行控制
        // 是否挂资金计划控制
        model.put("budgetSel",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET));
        String relateCon = configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON)
                .getValue();// 是否对接合同管理

        model.put("sendItem", configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
        model.put("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);


        model.put("relateCon",
                configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON));
        if (relateCon.equals(CmsConstant.RELATED_CONTRACT_YES)) {
            model.put("relateConType",
                    configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE));// 对接合同管理管理模式
        }
        return AjaxJson.success(model);
    }

    /**
     * 修改-页面跳转
     */
    @GetMapping("/update/{id}")
    public AjaxJson<CmsLoanInfoViewPageVO> update(@PathVariable("id") String id) {
        CmsLoanInfoViewPageVO model = new CmsLoanInfoViewPageVO();
        if (StringUtils.isEmpty(id) || "undefined".equals(id)) {
            return null;
        }
        CmsProvideLoanInfo cmsProvideLoanInfo = loanInfoService.get(id);
        // 采用新的计提计息方式，编辑时，如果计提为空则还款方式也置空。重新填写
        if (StringUtils.isEmpty(cmsProvideLoanInfo.getAccrualType())) {
            cmsProvideLoanInfo.setRepayType(null);
        } else {
            model.setRepayType(repayTypeService.get(cmsProvideLoanInfo.getRepayType()));
        }
        CorpDTO corpDTO = corpService.get(cmsProvideLoanInfo.getCorpId());
        model.setLoanInfo(cmsProvideLoanInfo);
        model.setCorp(corpDTO);
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        model.setBaseCurId(cur.getId());
        BtBankDTO bank = btBankServiceImpl.getByBankCode(cmsProvideLoanInfo.getBankId());
        model.setShowBankName(bank.getBankName());
        model.setShowBankName1(bank.getBankCode());
        TbBaseRateDTO tbBaseRate = iBaseRateService.getBaseRateById(cmsProvideLoanInfo.getReferenceRateType());
        if (null != tbBaseRate) {
            model.setTbBaseRate(tbBaseRate);
        }
        TbRateMaintenceDTO tbLoanRateMaintence = loanRateMaintenceService.getLoanRateMaintenceByDate(
                cmsProvideLoanInfo.getCurId(), cmsProvideLoanInfo.getReferenceRateType(),
                cmsProvideLoanInfo.getRepayCycle(), cmsProvideLoanInfo.getRateQuotedPriceDate());
        if (null != tbLoanRateMaintence) {
            model.setTbLoanRateMaintence(tbLoanRateMaintence);
        }
        // 授信设置
        ProvideUtils.setRatParam(model, loanInfoService.getRelateRat().getValue(), cmsProvideLoanInfo.getRatmodel(),
                loanInfoService.getIsRatOpen().getValue(), cmsProvideLoanInfo.getId(), urationService,
                btCurrencyServiceImpl.getObjectById(corpDTO.getCurId()));
        // 查询发放的 还款计划
        CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(id);
        if (crs != null) {
            model.setPlanId(crs.getId());
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
        model.setCostVOS(costVOS);
        // 抵质押参数
        String pledgeConfig = configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_RELATE_PLEDGE).getValue();
        ProvideUtils.setPledgeParam(model, pledgeConfig, cmsProvideLoanInfo, pledgeOccupyService);
        // 是否挂资金计划控制
        model.setBudgetSel(configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET));
        model.setSendItem(configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);

        String relateCon = configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON)
                .getValue();// 是否对接合同管理
        model.setRelateCon(
                configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELEATE_CON));
        if (relateCon.equals(CmsConstant.RELATED_CONTRACT_YES)) {
            model.setRelateConType(
                    configServiceImpl.getConfig(ConfigConstant.CMS, BaseDataConstant.BANK_RELATE_CON_TYPE));// 对接合同管理管理模式
        }
        return AjaxJson.success(model);
    }

    /**
     * 提交
     */
    @LogMessage(content = "添加借款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody LoanInfoApplySubmitVO param) {
        LoanInfoVO vo = new LoanInfoVO();
        RatUrationDTO urationDTO = new RatUrationDTO();
        BeanUtils.copyProperties(param, vo);
        BeanUtils.copyProperties(param, urationDTO);
        if (StringUtils.isEmpty(vo.getId())) {
            vo.setBillCode(loanInfoService.getLoanInfoBillCode());
        }
        loanInfoService.tempOrSubmit(vo, param.getSaveType(), param.getMyflag(), urationDTO, getCurrentUser());
        return success();
    }

    @LogMessage(content = "删除借款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        loanInfoService.delLoanInfo(id, getCurrentUser());
        return success();
    }

    /**
     * 添加贷款信息表
     *
     * @param cmsLoanInfoVOs
     * @return
     */
    @LogMessage(content = "添加贷款信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<LoanInfoVO> cmsLoanInfoVOs) {
        for (int i = 0; i < cmsLoanInfoVOs.size(); i++) {
            LoanInfoVO vo = cmsLoanInfoVOs.get(i);
            if (StringUtils.isEmpty(cmsLoanInfoVOs.get(i).getId())) {
                vo.setBillCode(loanInfoService.getLoanInfoBillCode());
            }
        }
        loanInfoApplyService.saveImport(cmsLoanInfoVOs, getCurrentUser());
        return success();
    }
}
