package com.bytter.finance.saas.controller.provide;

import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.finacial.api.constant.FinacialConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
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
import io.swagger.annotations.Api;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
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
@Api(value = "/v1-preposebill/cms/provide/loanrepay", tags = "借款还款")
@RestController("v1LoanRepayController")
@RequestMapping("/v1-preposebill/cms/provide/loanrepay")
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
     * 查询借款还款列表 - 页面跳转
     */
    @LogMessage(content = "查询借款还款申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApp")
    public AjaxJson<Map<String, Object>> listApp(String repayPlan) {
        Map<String, Object> model = new HashMap<>();
        if (!EmptyUtils.isEmpty(repayPlan)) {
            String[] repayString = repayPlan.split("\\?");
            repayPlan = repayString[0];
            model.put("repayPlan", repayPlan);
        }
        this.initConfig(model);
        model.put("gua",
                configServiceImpl.getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BANKLOAN_GUA));
        return AjaxJson.success(model);
    }


    /**
     * 修改或补录还款列表 页面跳转
     *
     * @param upRepSing 修改或补录标识 1 修改 其他为补录
     */
    @LogMessage(content = "查询借款还款申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listUpOrRep")
    public AjaxJson<Map<String, Object>> listUpOrRep(String upRepSing) {
        Map<String, Object> model = new HashMap<>();
        if (!EmptyUtils.isEmpty(upRepSing)) {
            model.put("upRepSing", upRepSing);
        }
        return AjaxJson.success(model);
    }

    /**
     * 借款还款 table列表
     *
     * @return
     */
    @PostMapping("/dataAppTable")
    public AjaxJson<DataTablesOutput> dataAppTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.getDataAppTables(input, getCurrentUser()));
    }

    /**
     * 还款跟踪
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataOnlineRepayFollow")

    public AjaxJson<DataTablesOutput> dataOnlineRepayFollow(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.getDataOnlineRepayFollow(input, getCurrentUser()));
    }

    /**
     * 修改还款 table列表
     *
     * @return
     */
    @PostMapping("/dataUpdateTable")
    public AjaxJson<DataTablesOutput> dataUpdateTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.getDataUpdateTables(input, getCurrentUser()));
    }

    /**
     * 补录还款 table列表
     *
     * @return
     */
    @PostMapping("/dataRepairTable")
    public AjaxJson<DataTablesOutput> dataRepairTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.getDataRepairTables(input, getCurrentUser()));
    }

    /**
     * 还款计划修改 table列表
     *
     * @return
     */
    @PostMapping("/dataRepayPlanable")
    public AjaxJson<DataTablesOutput> dataRepayPlanable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.dataRepayPlanable(input, getCurrentUser()));
    }

    /**
     * 计息方式修改 table列表
     *
     * @return
     */
    @PostMapping("/dataInterestWayable")
    public AjaxJson<DataTablesOutput> dataInterestWayable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.dataInterestWayable(input, getCurrentUser()));
    }

    /**
     * 浮动方式改 table列表
     *
     * @return
     */
    @PostMapping("/dataFolatingModeable")
    public AjaxJson<DataTablesOutput> dataFolatingModeable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.dataFolatingModeable(input, getCurrentUser()));
    }

    /**
     * 借款还款新增页面跳转
     */
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> edit(String tempOrSubmitSign, CmsProvideLoanRepay repay, String downSign) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(repay.getId())) {
            String onlineSign = repay.getOnlineSign();
            repay = cmsProvideLoanRepayService.get(repay.getId());
            if (EmptyUtils.isEmpty(repay.getAcrossRepair())) {
                repay.setAcrossRepair("0");
            }
            //第三方费率信息
            List<DfIntermediateCostVO> costVOS = loanInfoService.listIntermediateCostByDfId(repay.getId());
            model.put("costVOS", costVOS);
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
            model.put("repayType", repayType);
            model.put("repayTypeJson", JsonUtils.toJSONString(repayType));
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
        if (null != repayMaxDate.getSignRepayDate()) {
            model.put("repayMaxDate", DateUtils.format(DateUtils.addDay(repayMaxDate.getSignRepayDate(), -1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        if (null != repayMinDate && null != repayMinDate.getSignRepayDate()) {
            model.put("repayMinDate", DateUtils.format(DateUtils.addDay(repayMinDate.getSignRepayDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        } else {
            model.put("repayMinDate", DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        }
        if (null != interestVO && null != interestVO.getRepayMoney()) {
            model.put("repayMaxLeftMoney", send.getMoney().subtract(interestVO.getRepayMoney()));
        } else {
            model.put("repayMaxLeftMoney", send.getMoney().subtract(BigDecimal.ZERO));
        }
        model.put("maxd", EmptyUtils.isEmpty(send.getDeferredDate()) ? send.getRepayDate() : send.getDeferredDate());
        model.put("mind", DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        this.initConfig(model);
        model.put("plClass", loanInfoVO.getPlClass());
        model.put("repay", repay);
        model.put("send", send);
        model.put("sendBalance", sendBalance);
        model.put("downSign", downSign);
        model.put("tempOrSubmitSign", tempOrSubmitSign);
        return AjaxJson.success(model);
    }

    /**
     * 借款还款新增页面跳转
     */
    @GetMapping("/editOnline")
    public AjaxJson<Map<String, Object>> editOnline(CmsProvideLoanRepay repay) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(repay.getId())) {
            if (StringUtils.isNotEmpty(repay.getId())) {
                repay = cmsProvideLoanRepayService.get(repay.getId());
            }
            if (!EmptyUtils.isEmpty(repay)) {
                List<CmsProvideInterest> list = cmsProvideLoanRepayService
                        .getRepayDateInterestsById(repay.getProvideInterestId());
                model.put("list", list);
            }
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            repay.setCorpId(send.getCorpId());

            RepayType repayType = new RepayType();
            if (!StringUtils.isEmpty(send.getRepayType()) && send.getRepayType().length() == 32) {
                repayType = repayTypeService.get(send.getRepayType());
            }
            model.put("repayType", repayType);
            model.put("repayTypeJson", JsonUtils.toJSONString(repayType));


            Date deferred = provideLoanDeferredService.getMaxDeferredDate(repay.getSendCode());
            if (EmptyUtils.isEmpty(deferred)) {
                deferred = send.getRepayDate();
            }
            if (deferred.compareTo(DateUtils.getDateYMD()) < 0) {
                model.put("overdueSign", "1");// 逾期标识
            } else {
                model.put("overdueSign", "0");// 没逾期
            }
            // 最大输入金额控制
            BigDecimal sendBalance = cmsProvideLoanRepayService.completeSendBalance(repay.getSendCode(), null);
            if (repay.getRepayMoney() != null) {
                sendBalance = sendBalance.add(repay.getRepayMoney());
            }

            CmsProvideInterestVO repayMaxDate = cmsProvideInterestService.queryInterestMaxAndMinDate(repay.getSendCode(), CommonConstant.NO);
            CmsProvideInterestVO repayMinDate = cmsProvideInterestService.queryInterestMaxAndMinDate(repay.getSendCode(), CommonConstant.YES);
            if (null != repayMaxDate.getSignRepayDate()) {
                model.put("repayMaxDate", DateUtils.format(DateUtils.addDay(repayMaxDate.getSignRepayDate(), -1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            }
            if (null != repayMinDate && null != repayMinDate.getSignRepayDate()) {
                model.put("repayMinDate", DateUtils.format(DateUtils.addDay(repayMinDate.getSignRepayDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            } else {
                model.put("repayMinDate", DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            }
            model.put("maxd", EmptyUtils.isEmpty(send.getDeferredDate()) ? send.getRepayDate() : send.getDeferredDate());
            model.put("mind", DateUtils.format(DateUtils.addDay(send.getSendDate(), 1), DateUtils.PATTERN_CLASSICAL_SIMPLE));

            this.initConfig(model);
            model.put("repay", repay);
            model.put("send", send);
            model.put("sendBalance", sendBalance);
        }
        return AjaxJson.success(model);
    }

    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(Map<String, Object> model) {
        // 0为不挂接资金计划 现在需求要求不挂接
        model.put("budgetSel", configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.IS_BORROW_BUDGET).getValue());
        model.put("repayItem", configServiceImpl
                .getConfig(BudgetConstant.PROCESS_SYSNAME_CMS, BaseDataConstant.PLOAN_REPAY_HANG_BUDGET).getValue());
        model.put("businessName", BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY);
        model.put("processSysname", BudgetConstant.PROCESS_SYSNAME_CMS);
    }

    /**
     * 编辑补录
     */
    @GetMapping("/editRepair")
    public AjaxJson<Map<String, Object>> editRepair(CmsProvideLoanRepay repay) {
        Map<String, Object> model = new HashMap<>();
        if (StringUtils.isNotEmpty(repay.getId())) {
            repay = cmsProvideLoanRepayService.get(repay.getId());
        }
        ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
        Date deferred = provideLoanDeferredService.getMaxDeferredDate(repay.getSendCode());
        if (EmptyUtils.isEmpty(deferred)) {
            deferred = send.getRepayDate();
        }
        if (deferred.compareTo(DateUtils.getDateYMD()) < 0) {
            model.put("overdueSign", "1");// 逾期标识
        } else {
            model.put("overdueSign", "0");// 没逾期
        }
        // 最大输入金额控制
        BigDecimal sendBalance = cmsProvideLoanRepayService.completeSendBalance(repay.getSendCode(), null);
        if (repay.getRepayMoney() != null) {
            sendBalance = sendBalance.add(repay.getRepayMoney());
        }
        this.initConfig(model);
        model.put("repay", repay);
        model.put("sendBalance", sendBalance);
        model.put("curCode", send.getCurCode());
        return AjaxJson.success(model);
    }

    /**
     * @return
     */
    @LogMessage(content = "线上还款申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewOnlineRepay")
    public AjaxJson<Map<String, Object>> viewOnlineRepay(String repayId) {
        Map<String, Object> model = new HashMap<>();
        LoanRepayVO repay = cmsProvideLoanRepayService.queryRepayVOById(repayId);
        if (!EmptyUtils.isEmpty(repay)) {
            List<CmsProvideInterest> list = cmsProvideLoanRepayService
                    .getRepayDateInterestsById(repay.getProvideInterestId());
            model.put("list", list);
        }
        model.put("repay", repay);
        return AjaxJson.success(model);
    }

    /**
     * 还款申请查询- 公用页面
     */
    @LogMessage(content = "还款申请查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewRepay")
    public AjaxJson<Map<String, Object>> viewRepay(String sendBillCode, String repayId, String show) {
        Map<String, Object> model = new HashMap<>();
        if (EmptyUtils.isNotEmpty(repayId) && !"null".equals(repayId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(repayId);
            model.put("repay", repay);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            model.put("curCode", send.getCurCode());
            this.initConfig(model);
        }
        if (!EmptyUtils.isEmpty(sendBillCode)) {
            model.put("sendBillCode", sendBillCode);
        }
        model.put("show", show);// 显示按钮
        return AjaxJson.success(model);
    }

    @GetMapping("/viewRepayBybillCode")
    public AjaxJson<Map<String, Object>> viewRepayByBillCode(String repayBillCode) {
        Map<String, Object> model = new HashMap<>();
        if (EmptyUtils.isNotEmpty(repayBillCode) && !"null".equals(repayBillCode)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.getRepayInfoByBillCode(repayBillCode);
            model.put("repay", repay);
            this.initConfig(model);
            model.put("sendBillCode", repay.getSendCode());
        }
        model.put("show", 0);// 显示按钮
        return AjaxJson.success(model);
    }

    /**
     * 借款查询列表 查询还款详情列表 passageSign 在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    @GetMapping("/viewRepayList")
    public AjaxJson<Map<String, Object>> viewRepayList(String sendBillCode, String passageSign) {
        Map<String, Object> model = new HashMap<>();
        if (EmptyUtils.isNotEmpty(sendBillCode)) {
            model.put("sendBillCode", sendBillCode);
            List repayList = cmsProvideLoanRepayService.getRepayListBySendBillCode(sendBillCode, passageSign);
            model.put("repayList", repayList);
        }
        return AjaxJson.success(model);
    }

    /**
     * 编辑借款还款
     *
     * @return
     */
    @LogMessage(content = "编辑借款还款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/tempOrSubmit")
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
    public AjaxJson onlineDeductOk(String id) {
        cmsProvideLoanRepayService.onlineDeductOk(id, getCurrentUser());
        return success();
    }

    /**
     * 查询离线帐号根据单位ID
     */
    @RequestMapping("/queryPaymentAccListByCoprId")
    public AjaxJson<List<BtBankAccDTO>> queryPaymentAccListByCoprId(String corpId, String curId, String bankCode) {
        if (EmptyUtils.isEmpty(corpId)) {
            return AjaxJson.success(new ArrayList<BtBankAccDTO>());
        }
        CorpDTO corpDTO = corpService.get(corpId);
        BtBankAccDTO dto = new BtBankAccDTO();
        dto.setCorpId(corpId);
        //dto.setIsOnline(BtBankAccConstant.ISONLINE_0);
        dto.setValidSign(BtBankAccConstant.VALIDSIGN_1);
        dto.setCurId(curId);
        dto.setBankCodeStr(bankCode);
        return AjaxJson.success(btBankAccService.getBankAccListAll(dto));
    }

    @RequestMapping("/queryPaymentAccListByCoprIdCount")
    public AjaxJson<Integer> queryPaymentAccListByCoprIdCount(String corpId) {
        if (EmptyUtils.isEmpty(corpId)) {
            return AjaxJson.success(0);
        }
        BtBankAccDTO dto = new BtBankAccDTO();
        dto.setCorpId(corpId);
        dto.setIsOnline(BtBankAccConstant.ISONLINE_0);
        dto.setValidSign(BtBankAccConstant.VALIDSIGN_1);
        List<BtBankAccDTO> list = btBankAccService.getBankAccList(dto);
        if (EmptyUtils.isEmptyList(list)) {
            return AjaxJson.success(0);
        }
        return AjaxJson.success(list.size());
    }

    /**
     * 根据单位ID和业务类型判断是否挂预算
     *
     * @param corpId
     * @return
     */
    @PostMapping("/isBudget")
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
    public AjaxJson<Map<String, Object>> listRepayApprove(String flowStatus, String cycleTime) {
        Map<String, Object> model = new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }

    /**
     * 还款申请审批列表 - 数据
     */
    @PostMapping("/dataTableRepayApprove")
    public AjaxJson<DataTablesOutput> dataTableRepayApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.getDataRepayApproveTables(input));
    }

    /**
     * 还款申请 详情页面
     */
    @GetMapping("/viewRepayApprove")
    public AjaxJson<Map<String, Object>> viewRepayApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(businessId);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            model.put("curCode", send.getCurCode());
            model.put("repay", repay);
            this.initConfig(model);
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }

    /**
     * 还款补录审批列表页面
     */
    @LogMessage(content = "还款补录审批列表页面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRepayRepairApprove")
    public AjaxJson<Map<String, Object>> listRepayRepairApprove(String flowStatus, String cycleTime) {
        Map<String, Object> model = new HashMap<>();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        return AjaxJson.success(model);
    }

    /**
     * 还款申请审批列表 - 数据
     */
    @PostMapping("/dataTableRepayRepairApprove")
    public AjaxJson<DataTablesOutput> dataTableRepayRepairApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(cmsProvideLoanRepayService.getDataRepayRepairApproveTables(input));
    }

    /**
     * 还款申请 详情页面
     */
    @GetMapping("/viewRepayRepairApprove")
    public AjaxJson<Map<String, Object>> viewRepayRepairApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(businessId);
            model.put("repay", repay);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            model.put("curCode", send.getCurCode());
            this.initConfig(model);
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }

    /**
     * 手机审批 申请、补录
     */
    @GetMapping("/mobileRepayApprove")
    public AjaxJson<Map<String, Object>> mobileRepayApprove(String businessId, String taskId) {
        Map<String, Object> model = new HashMap<>();
        if (!EmptyUtils.isEmpty(businessId)) {
            CmsProvideLoanRepay repay = cmsProvideLoanRepayService.get(businessId);
            model.put("repay", repay);
            ProvideLoanSend send = cmsProvideLoanSendService.getSendBySendBillCode(repay.getSendCode());
            LoanSendVO loanSendVo = cmsProvideLoanSendService.getLoanSendVOBySendBillCode(send.getBillCode());
            LoanInfoVO loanInfoVo = loanInfoService.getLoanInfoVOByBillCode(loanSendVo.getLoanCode());
            /*
             * if(RationConstant.RAT_COMMON_Y.equals(loanInfoVo.getRatmodel())) { model.put("urationDTO",
             * urationService.getRatUrationDTOByBillId(ConfigConstant.CMS, loanInfoVo.getId()));//授信明细 }
             */
            BigDecimal repayPassage = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(send.getBillCode(), "1");// 在途金额
            BigDecimal repayFinish = cmsProvideLoanRepayService.getRepayMoneyBySendBillCode(send.getBillCode(), "2");// 已还金额
            List<FbsBudgetProcessDTO> list = fbsBudgetProcessService.getBudgetProcessByBillNo(
                    BudgetConstant.BUSINESS_NAME_PROVIDE_REPAY, BudgetConstant.PROCESS_SYSNAME_CMS,
                    repay.getBillCode());
            if (!EmptyUtils.isEmptyList(list)) {
                model.put("ProcessDate", list.get(0).getProcessDate());
            }
            this.initConfig(model);
            model.put("loanSend", loanSendVo);
            model.put("loanInfo", loanInfoVo);
            model.put("repayPassage", repayPassage);
            model.put("repayFinish", repayFinish);
        }
        model.put("taskId", taskId);// 必须要做此步骤
        return AjaxJson.success(model);
    }
}
