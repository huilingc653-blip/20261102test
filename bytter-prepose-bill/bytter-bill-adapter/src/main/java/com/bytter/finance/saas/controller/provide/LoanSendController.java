
package com.bytter.finance.saas.controller.provide;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.BudgetConstant;
import com.bytter.biz.finacial.api.service.IFcDayCloseService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BaseDataConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.api.IRatUrationService;
import com.bytter.credit.constant.RationConstant;
import com.bytter.credit.dto.RatUrationDTO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.basedata.entity.CmsRepaymentSchedule;
import com.bytter.finance.domain.basedata.service.RepayTypeService;
import com.bytter.finance.domain.basedata.service.RepaymentScheduleService;
import com.bytter.finance.domain.directFinance.vo.DfIntermediateCostVO;
import com.bytter.finance.domain.provide.entity.CmsLoanInfoViewPageVO;
import com.bytter.finance.domain.provide.entity.CmsProvideInterest;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanInfo;
import com.bytter.finance.domain.provide.entity.ProvideLoanSend;
import com.bytter.finance.domain.provide.service.CmsProvideInterestService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanRepayService;
import com.bytter.finance.domain.provide.service.CmsProvideLoanSendService;
import com.bytter.finance.domain.provide.service.LoanInfoService;
import com.bytter.finance.domain.provide.vo.LoanInfoVO;
import com.bytter.finance.domain.provide.vo.LoanSendVO;
import com.bytter.finance.domain.provide.vo.v1.PerfectlyLoanInfoPageVO;
import com.bytter.finance.domain.provide.vo.v1.ProLoanSendDataListPageVO;
import com.bytter.guarantee.api.IGuaContractOccupServie;
import com.bytter.guarantee.api.IGuaWayServie;
import com.bytter.guarantee.dto.GuaContractOccupDTO;
import com.bytter.guarantee.dto.GuaContractUseDTO;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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

@Api(value = "银行借款发放表", tags = "银行借款发放表")
@RestController("v1LoanSendController")
@RequestMapping("/v1-preposebill/cms/provide/loansend")
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
     * 银行借款发放表 table列表
     *
     * @return
     */

    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanSendService.getDataSendTables(input, getCurrentUser()));
    }


    /**
     * 功能介绍 : 查询借款发放完成的数据
     */
    @ApiOperation(value = "查询借款发放完成数据")
    @GetMapping("/proLoanSendData")
    public AjaxJson<ProLoanSendDataListPageVO> proLoanSendData(String id) {
        ProLoanSendDataListPageVO listPageVO = new ProLoanSendDataListPageVO();
        listPageVO.setId(id);
        BisAccDtlDTO bisAccDtlDTO = iBisAccDtlService.getById(id);
        listPageVO.setBisDTO(bisAccDtlDTO);
        if (null != bisAccDtlDTO) {
            BtBankAccDTO bankAccDTO = iBtBankAccService.getBankAccByBankAccAndCurId(bisAccDtlDTO.getBankAcc(),
                    bisAccDtlDTO.getCurId());// 根据账号和币别获取账户信息
            listPageVO.setBankAccDTO(bankAccDTO);
            if (null != bankAccDTO && StringUtils.isNotEmpty(bankAccDTO.getBankTypeId())) {
                String bankTypeName = iBankTypeService.getObjectById(bankAccDTO.getBankTypeId()).getTypeName();
                listPageVO.setBankTypeName(bankTypeName);
            }
        }
        return success(listPageVO);
    }

    /**
     * 功能介绍 : 查询借款发放完成的数据
     */
    @ApiOperation("查询借款发放完成的数据")
    @PostMapping("/proLoanSendDataTables")
    public AjaxJson<DataTablesOutput> queryLoanInfoData(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataTablesInput = input.convertAndInitColumns();
        return success(cmsProvideLoanSendService.getDataSendOverTables(dataTablesInput));
    }


    @ApiOperation("查看银行借款详情")
    @GetMapping("/viewLoanInfo")
    public AjaxJson<PerfectlyLoanInfoPageVO> viewPerfectlyLoanInfo(String id) {
        PerfectlyLoanInfoPageVO pageVO = new PerfectlyLoanInfoPageVO();
        if (EmptyUtils.isNotEmpty(id)) {
            ProvideLoanSend loanSend = cmsProvideLoanSendService.get(id);
            String infoBillCode = loanSend.getLoanCode();
            String sendBillCode = loanSend.getBillCode();
            if (EmptyUtils.isNotEmpty(infoBillCode)) {
                LoanInfoVO infoVO = loanInfoService.getLoanInfoVOByBillCode(infoBillCode);
                pageVO.setLoanInfo(infoVO);
                // 授信设置
                CorpDTO corpDTO = corpService.get(infoVO.getCorpId());
                this.setRatParam(pageVO, loanInfoService.getRelateRat().getValue(), infoVO.getRatmodel(),
                        loanInfoService.getIsRatOpen().getValue(), infoVO.getId(), urationService,
                        btCurrencyServiceImpl.getObjectById(corpDTO.getCurId()));
                if (infoVO.getAcrossSend() != null && infoVO.getAcrossSend().equals("1")) {
                    if (EmptyUtils.isNotEmpty(infoBillCode) && !"null".equals(infoBillCode)) {
                        List<FbsBudgetProcessDTO> list = new ArrayList<FbsBudgetProcessDTO>();
                        list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                                BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION, BudgetConstant.PROCESS_SYSNAME_CMS,
                                infoBillCode);
                        if (!EmptyUtils.isEmptyList(list)) {
                            pageVO.setProcessDate(list.get(0).getProcessDate());
                        }
                        // 资金预算
                        this.initConfig(pageVO);
                        pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_ADDITION);
                    }
                }
                // 担保参数设置
                String isGuarantee = configServiceImpl
                        .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA).getValue();
                CmsProvideLoanInfo cmsProvideLoanInfo = new CmsProvideLoanInfo();
                BeanUtils.copyNotNullProperties(infoVO, cmsProvideLoanInfo);
                this.setGuaParams(pageVO, "1", isGuarantee, cmsProvideLoanInfo, guaWayServie,
                        guaContractOccupServie);
                if ("1".equals(isGuarantee)) {
                    GuaContractUseDTO guaContractOccupyDtl = guaContractOccupServie.getGuaContractOccupyDtl(loanSend.getId(), loanSend.getBillCode());
                    pageVO.setGuaContractUseDTO(guaContractOccupyDtl);
                }
                // 第三方费率信息
                List<DfIntermediateCostVO> costVOS = loanInfoService
                        .listIntermediateCostByDfId(cmsProvideLoanInfo.getId());
                pageVO.setCostVOS(costVOS);
            }
            if (EmptyUtils.isNotEmpty(sendBillCode)) {
                LoanSendVO sendVO = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendBillCode);
                List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                        BudgetConstant.BUSINESS_NAME_PROVIDE_SEND, BudgetConstant.PROCESS_SYSNAME_CMS, sendBillCode);
                pageVO.setLoanSend(sendVO);
                if (!EmptyUtils.isEmptyList(list)) {
                    pageVO.setProcessDate(list.get(0).getProcessDate());
                }
                pageVO.setSendBillCode(sendBillCode);
                BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendBillCode, "1");// 在途金额
                BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(sendBillCode, "2");// 已还金额
                pageVO.setRepayPassage(repayPassage);
                pageVO.setRepayFinish(repayFinish);
                // 资金预算
                this.initConfig(pageVO);
                pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
            }
        }
        pageVO.setShow("0");
        return success(pageVO);
    }


    public void setRatParam(PerfectlyLoanInfoPageVO pageVO, String relateRat, String ratmodel, String isOpenRat, String id,
                            IRatUrationService urationService, BtCurrencyDTO cur) {
        pageVO.setRelateRat(relateRat);// 挂接授信
        RatUrationDTO urationDTO = null;
        pageVO.setRatBankConfig(urationService.getRatBankCtlConfig());
        if (CmsConstant.IS_TRUE.equals(relateRat)) {
            pageVO.setIsOpenRat(isOpenRat);// 是否启用授信敞口
            if (RationConstant.RAT_COMMON_Y.equals(ratmodel)) {
                urationDTO = urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, id);
                pageVO.setUrationDTO(urationDTO);// 授信明细
                pageVO.setRatBankCode(urationDTO.getBankCode());// 授信银行
                pageVO.setCur(cur);
            }
        }
    }


    public static void setGuaParams(PerfectlyLoanInfoPageVO pageVO, String viewFlag, String isGuarantee,
                                    CmsProvideLoanInfo cmsProvideLoanInfo, IGuaWayServie guaWayServie,
                                    IGuaContractOccupServie guaContractOccupServie) {
        // 担保数据设置
        if (CmsConstant.IS_TRUE.equals(cmsProvideLoanInfo.getIsHangGuarantee())) {
            GuaContractOccupDTO guaContractOccupDTO = new GuaContractOccupDTO();
            guaContractOccupDTO.setBusinessId(cmsProvideLoanInfo.getId());
            guaContractOccupDTO = guaContractOccupServie.getUseGuaContractOccup(guaContractOccupDTO);
            String contractIds = null;
            if (guaContractOccupDTO.getContractIds().size() > 1) {
                for (String contractId : guaContractOccupDTO.getContractIds()) {
                    if (contractIds == null) {
                        contractIds = contractId;
                    } else {
                        contractIds += "," + contractId;
                    }
                }
            } else {
                contractIds = guaContractOccupDTO.getContractIds().get(0);
            }
            pageVO.setContractIds(contractIds);
            pageVO.setIdList(guaContractOccupDTO.getContractIds());
            pageVO.setGuaranteeRmk(cmsProvideLoanInfo.getGuaranteeRmk());
            pageVO.setGuaWayNames(guaContractOccupDTO.getGuaWayName());
            pageVO.setGuaWay(guaContractOccupDTO.getGuaWay());
            pageVO.setContractCode(guaContractOccupDTO.getContractCode());
        }
        pageVO.setViewFlag(viewFlag);
        pageVO.setIsGuarantee(isGuarantee);
        pageVO.setIsHangGuarantee(cmsProvideLoanInfo.getIsHangGuarantee());
    }


    /**
     * 银行借款发放表新增页面跳转
     *
     * @return
     */

    @GetMapping("/edit")
    public AjaxJson<PerfectlyLoanInfoPageVO> add(String billCode, String sendBillCode) {
        PerfectlyLoanInfoPageVO model = new PerfectlyLoanInfoPageVO();
        ProvideLoanSend send = cmsProvideLoanSendService.getSendByBillCode(sendBillCode, billCode);
        LoanInfoVO loanInfoVO = loanInfoService.getLoanInfoVOByBillCode(billCode);
        // 采用新的计提计息方式，编辑时，如果计提为空则还款方式也置空。重新填写
        if (StringUtils.isEmpty(send.getAccrualType())) {
            send.setRepayType(null);
        } else {
            model.setRepayType(repayTypeService.get(send.getRepayType()));
        }
        loanInfoVO.setPlMoney(new BigDecimal(loanInfoVO.getPlMoney().doubleValue()));
        if (EmptyUtils.isEmpty(send.getId())) {
            // 查询申请表的 还款计划
            if (CmsConstant.LOAN_SEND_OUT_TYPE_ONE.equals(loanInfoVO.getLoanSendOutType())) {// 一次性发放
                CmsRepaymentSchedule crs = repaymentScheduleService
                        .getCmsRepaymentScheduleByBillCode(loanInfoVO.getId());
                if (crs != null) {
                    model.setPlanId(crs.getId());
                    model.setPlanStatus("1");
                }
            }
            model.setType("1");
        } else {
            // 查询发放的 还款计划
            CmsRepaymentSchedule crs = repaymentScheduleService.getCmsRepaymentScheduleByBillCode(send.getId());
            if (crs != null) {
                model.setPlanId(crs.getId());
            }
            model.setType("2");
        }
        BigDecimal sendSumMoney = cmsProvideLoanSendService.getSendMoneyByBillCode(billCode);// 发放总金额
        BtCurrencyDTO cur = btCurrencyServiceImpl.getNatualCurrency();
        // 系统参数 是否挂资金计划
        this.initConfig(model);
        //todo:暂时注释
        Date closeDay = iFcDayCloseService.getDayCloseFinalDate(loanInfoVO.getNetId());
        model.setCloseDay(DateUtils.format(closeDay));
        model.setLoanInfo(loanInfoVO);
        model.setSend(send);
        model.setBaseCurId(cur.getId());
        model.setWaitRepayMoney(loanInfoVO.getPlMoney().subtract(sendSumMoney));
        model.setNaturalSign(cur.getNaturalSign());
        model.setMinDate(DateUtils.format(DateUtils.getDate()));// DateUtils.addDay(DateUtils.getDate(),1)
        // 获取计息数据 暂时屏蔽

        /*
         * AccrualTypeVo accrualTypeVo= accrualTypeService.queryAccrualTypeByCode(send.getInterestWay());
         * model.put("accrualType", accrualTypeVo);
         */

        return AjaxJson.success(model);
    }

    /**
     * 初始化系统参数
     *
     */

    private void initConfig(PerfectlyLoanInfoPageVO model) {

        // 0为不挂接资金计划 现在需求要求不挂接
        model.setBudgetSel(configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
        model.setSendItem(configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);
    }


    /**
     * 发放详情页面 - 公用
     */

    @GetMapping("/view/{billCode}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("billCode") String billCode) {
        Map<String, Object> model = new HashMap<>();
        List<LoanSendVO> sendList = cmsProvideLoanSendService.getLoanSendVOListByBillCode(billCode);
        model.put("sendList", sendList);
        model.put("billCode", billCode);
        return AjaxJson.success(model);
    }

    /**
     * 添加银行借款发放表
     */
    @LogMessage(content = "添加银行借款发放表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
    public AjaxJson tempOrSubmit(@RequestBody LoanSendVO send) {
        cmsProvideLoanSendService.tempOrSubmit(send, send.getSaveType(), getCurrentUser());
        return success();
    }

    /**
     * 发放打回
     *
     * @param billCode inof表billCode
     * @return
     */

    @LogMessage(content = "银行借款发放打回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/back/{billCode}")
    public AjaxJson back(@PathVariable("billCode") String billCode) {
        cmsProvideLoanSendService.back(billCode, null, getCurrentUser());
        return success();
    }

    /**
     * 公用页面 - 发放信息详情
     */
    @GetMapping("/viewSend/{sendBillCode}")
    public AjaxJson<CmsLoanInfoViewPageVO> viewSend(@PathVariable("sendBillCode") String sendBillCode) {
        CmsLoanInfoViewPageVO model = new CmsLoanInfoViewPageVO();
        LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(sendBillCode);
        LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());

        /*
         * if(RationConstant.RAT_COMMON_Y.equals(loanInfoVo.getRatmodel())) { model.put("urationDTO",
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

        model.setLoanSend(loanSendVo);
        model.setLoanInfo(loanInfoVo);
        model.setRepayPassage(repayPassage);
        model.setRepayFinish(repayFinish);
        return AjaxJson.success(model);
    }

    /**
     * 根据单位ID和业务类型判断是否挂预算
     *
     * @param corpId
     * @return
     */

    @PostMapping("/isBudget")

    public AjaxJson isBudget(String corpId, String briefId1, String briefId2) {
        boolean isBudget = cmsProvideLoanSendService.isBudget(corpId, briefId1, briefId2);
        AjaxJson json = new AjaxJson();
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("isBudget", isBudget);
        json.setAttributes(attributes);
        json.setSuccess(true);
        return json;
    }

    /********************* 审批begin *********************//*



    /**
     * 银行借款发放审批 - 数据
     */
    @PostMapping("/dataTableSendAppvoer")
    public AjaxJson<DataTablesOutput> dataTableSendAppvoer(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanSendService.getDataSendAppvoerTables(input));
    }

    /**
     * 银行借款发放审批 - 详情页面
     */

    @GetMapping("/viewSendApprove")
    public AjaxJson<PerfectlyLoanInfoPageVO> viewSendApprove(String businessId, String taskId) {
        PerfectlyLoanInfoPageVO model = new PerfectlyLoanInfoPageVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanSend send = cmsProvideLoanSendService.get(businessId);
            model.setSend(send);
        }
        model.setBudgetSel(configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
        model.setSendItem(configService
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_SEND_HANG_BUDGET).getValue());
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PROVIDE_SEND);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_CMS);
        model.setTaskId(taskId);
        return AjaxJson.success(model);
    }

    /**
     * 银行借款发放审批 - 详情页面
     */

    @GetMapping("/mobileSendApprove")
    public AjaxJson<PerfectlyLoanInfoPageVO> mobileSendApprove(String businessId, String taskId) {
        PerfectlyLoanInfoPageVO model = new PerfectlyLoanInfoPageVO();
        if (!EmptyUtils.isEmpty(businessId)) {
            ProvideLoanSend send = cmsProvideLoanSendService.get(businessId);
            if (send != null) {
                LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(send.getBillCode());
                LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());

                /*
                 * if(RationConstant.RAT_COMMON_Y.equals(loanInfoVo.getRatmodel())) { model.put("urationDTO",
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
                    model.setProcessDate(list.get(0).getProcessDate());
                }
                this.initConfig(model);
                model.setLoanSend(loanSendVo);
                model.setLoanInfo(loanInfoVo);
                model.setRepayPassage(repayPassage);
                model.setRepayFinish(repayFinish);
            }
        }
        model.setTaskId(taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }


    /**
     * 利息计算（开发测算利息时使用）
     *
     * @param sendId
     * @return
     */
    @PostMapping("/getInterest")
    public AjaxJson<List<CmsProvideInterest>> calInterest(String sendId) {
        if (StringUtils.isEmpty(sendId)) {
            return AjaxJson.success(new ArrayList<>());
        }
        ProvideLoanSend provideLoanSend = cmsProvideLoanSendService.get(sendId);
        cmsProvideInterestService.adjustProvideInterest(provideLoanSend, getCurrentUser());
        return AjaxJson.success(cmsProvideInterestService.queryInterestListByBillCode(provideLoanSend.getBillCode()));
    }


}

