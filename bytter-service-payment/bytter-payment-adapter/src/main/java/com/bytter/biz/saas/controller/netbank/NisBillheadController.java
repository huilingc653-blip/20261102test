package com.bytter.biz.saas.controller.netbank;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbBillBankAutoDtlDTO;
import com.bytter.bill.api.bill.dto.AbBillPayMiddleDTO;
import com.bytter.bill.api.bill.dto.AbEmitBillDTO;
import com.bytter.bill.api.bill.dto.AbVoucherinfoViceDTO;
import com.bytter.bill.api.bill.service.IAbVoucherinfoViceService;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.bill.api.scf.dto.ScfReceivableVoucherSelDTO;
import com.bytter.bill.api.scf.service.IScfVoucherPayService;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptDtlDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlHisService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.bdlink.vo.ViewReceiptsVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetDetailDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProjectDetailDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsBudgetDetailService;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.budget.spi.vo.FbsBudgetMatchedItemVO;
import com.bytter.biz.budget.spi.vo.FbsItemMoneyDetailInfoVO;
import com.bytter.biz.budget.spi.vo.FbsItemMoneyInfoVO;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.entity.NisScfInfo;
import com.bytter.biz.netbank.service.BtChargeBackTypeService;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.service.NisBillService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.service.NisScfInfoService;
import com.bytter.biz.netbank.vo.*;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisBillheadSplitDTO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.biz.limit.dto.PaymentLimitBatchDetailDto;
import com.bytter.biz.limit.exception.PaymentLimitBatchExceededException;
import com.bytter.biz.limit.exception.PaymentLimitExceededException;
import com.bytter.saas.management.exchange.api.dto.LimitHitDetailDto;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.*;
import com.bytter.core.tenant.SmartThreadPoolManager;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.ConfigConstant;
import com.bytter.system.api.constant.FcExcrateAdjustConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.bytter.workflow.enums.WorkFlowMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 付款通知表
 * @date 2017-05-23 15:20:33
 */
@Api(value = "/v1-payment/netbank/nisbillhead", tags = {"付款办理", "付款查询", "作废付款单查询"})
@RestController("v1NisBillheadController")
@RequestMapping("/v1-payment/netbank/nisbillhead")
public class NisBillheadController extends BaseController implements DisposableBean {
    @Autowired
    private NisBillheadService nisBillheadService;
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @Autowired
    private FcVoucherTypeService fcVoucherTypeService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IInnerAccountService innerAccountService;
    @DubboReference(check = false)
    private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
    private IFbsCorpBudgetService fbsCorpBudgetService;
    @Autowired
    private BtChargeBackTypeService btChargeBackTypeService;
    @DubboReference(check = false)
    private IDictService dictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
    private IBtCurrencyService btCurrencyService;
    @DubboReference(check = false)
    private IFcExcrateAdjustService fcExcrateAdjustService;
    @DubboReference(check = false)
    private IFbsBudgetService fbsBudgetService;
    @DubboReference(check = false)
    private IFbsBudgetDetailService fbsBudgetDetailService;
    @DubboReference(check = false)
    private IBtBankService bankService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlService bisBankReceiptDtlService;
    @DubboReference(check = false)
    private IBisBankReceiptDtlHisService bisBankReceiptDtlHisService;
    @DubboReference(check = false)
    private IAccebillService accebillService;
    @DubboReference(check = false)
    private IRegionService regionService;
    @DubboReference(check = false)
    IFcOnlineLedgerService fcOnlineLedgerService;
    @DubboReference(check = false)
    private IAbVoucherinfoViceService iAbVoucherinfoViceService;
    @DubboReference(check = false)
    private com.bytter.guarantee.api.IFcBondHookService IFcBondHookService;

    @Autowired(required = false)
    private NisBillService nisBillService;

    @Autowired(required = false)
    private NisScfInfoService nisScfInfoService;

    @DubboReference(check = false)
    private IScfVoucherPayService scfVoucherPayService;

    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

    @DubboReference(check = false)
    private IConfigService iConfigService;
    @DubboReference(check = false)
    private IFcAccService iFcAccService;
    /**
     * 付款办理批量处理线程池
     */
    private static ExecutorService NISBILLHEAD_START_EXECUTOR_SERVICE = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(20));

    @Override
    public void destroy() throws Exception {
        NISBILLHEAD_START_EXECUTOR_SERVICE.shutdown();
    }

    /**
     * 付款通知表列表 页面跳转
     *
     * @return
     */
    @ApiOperation("查询付款通知表列表")
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<NisBillHeadVO> list() {
        NisBillHeadVO vo = new NisBillHeadVO();
        vo.setEndWishPayday(DateUtils.addMonth(DateUtils.getDate(), 1));
        Map<String, Map<String, String>> typeMap = configService.listByTypes(Arrays.asList(SysConfigConstant.Module.SYS,ConfigConstant.ERP, ConfigConstant.NIS));
        if (!typeMap.isEmpty()) {
            Map<String, String> nisMap = typeMap.get(ConfigConstant.NIS);
            if (!nisMap.isEmpty()) {
                vo.setShowIsPayed(nisMap.get(NetbankConstant.NIS_CONFIG_SHOWISPAYED));
                //是否启用待付款池	  1-是 0-否
                vo.setEnablePendingPaymentPool(nisMap.get(NetbankConstant.NIS_CONFIG_ENABLEPENDINGPAYMENTPOOL));
                //未产生资金需求付款单付款控制 0-不提醒，1-只提醒不控制，2-控制
                vo.setDemandControl(nisMap.get(NetbankConstant.NIS_CONFIG_DEMANDCONTROL));
                //   支付结算参数配置：isEnableSplit 是否启用拆分
                vo.setIsEnableSplit(nisMap.get(NetbankConstant.NIS_CONFIG_ISENABLESPLIT));
                //支付结算参数配置：isEnableMerge 是否启用合并
                vo.setIsEnableMerge(nisMap.get(NetbankConstant.NIS_CONFIG_ISENABLEMERGE));
                // 收款认领管理参数配置：budgetSel-是否挂接资金计划
                vo.setBudgetSel(nisMap.get(NetbankConstant.RECEIVABLE_CONFIG_BUDGETSEL));
                vo.setPayItem(nisMap.get(NetbankConstant.NIS_CONFIG_PAYITEM));
                //付款余额控制方式  0-账户余额 1-可用余额
                vo.setBalControlType(nisMap.get(NetbankConstant.NIS_CONFIG_BALCONTROLTYPE) );
                vo.setAccountOver(nisMap.get(NetbankConstant.NIS_CONFIG_ACCOUNTOVER) );
                vo.setBankaccOver(nisMap.get(NetbankConstant.NIS_CONFIG_BANKACCOVER) );
                vo.setShowIsPayed(nisMap.get(NetbankConstant.NIS_CONFIG_SHOWISPAYED) );
                vo.setAllowDifferentBillMoney(nisMap.get(NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY) );
            }
            Map<String, String> erpMap = typeMap.get(ConfigConstant.ERP);
            if (!erpMap.isEmpty()) {
                //来源于ERP的付款单，付款账号是否允许编辑
                vo.setEditPayerAcc(erpMap.get("payerAcc"));
            }
            Map<String, String> sysMap = typeMap.get(ConfigConstant.SYS);
            if (!sysMap.isEmpty()) {
                //是否启用限额管理  1-是 0-否
                vo.setIsOpenLimit(sysMap.get(SysConfigConstant.ISOPENLIMIT));
                vo.setExternalCorp(sysMap.get(SysConfigConstant.EXTERNALCORP) );
            }
        }
        return AjaxJson.success(vo);
    }

    /**
     * 开票登记编辑页面跳转
     *
     * @return
     */
    @ApiOperation("开票登记编辑页面跳转")
    @PostMapping("/updateBillInfo")
    public AjaxJson<Map<String, Object>> updateBillInfo(@RequestBody AbEmitBillDTO abEmitBillDTO) {
        Map<String, Object> map = accebillService.dealBillInfo(abEmitBillDTO.getAid());
        Map<String, Object> model = new HashMap<>(map);
        model.put("corpId", abEmitBillDTO.getCorpId() == null ? "" : abEmitBillDTO.getCorpId());
        model.put("payMoney", abEmitBillDTO.getBillMoney() == null ? "" : abEmitBillDTO.getBillMoney());
        model.put("rmk", abEmitBillDTO.getRmk() == null ? "" : abEmitBillDTO.getRmk());
        model.put("payeracc", abEmitBillDTO.getPayeracc() == null ? "" : abEmitBillDTO.getPayeracc());
        model.put("payeeacc", abEmitBillDTO.getPayeeacc() == null ? "" : abEmitBillDTO.getPayeeacc());
        model.put("payeeAccName", abEmitBillDTO.getPayeeAccName() == null ? "" : abEmitBillDTO.getPayeeAccName());
        model.put("payeebankno", abEmitBillDTO.getPayeebankno() == null ? "" : abEmitBillDTO.getPayeebankno());
        model.put("payeeBankName", abEmitBillDTO.getPayeeBankName() == null ? "" : abEmitBillDTO.getPayeeBankName());
        model.put("blRcvpymtpsUnnIdNo", (abEmitBillDTO.getBlRcvpymtpsUnnIdNo() == null || "undefined".equals(abEmitBillDTO.getBlRcvpymtpsUnnIdNo().toString())) ? "" : abEmitBillDTO.getBlRcvpymtpsUnnIdNo());
        model.put("isPayFlg", "1");//是否是付款办理弹窗开票
        if (map.get("abEmitBillVo") != null
                && map.get("abEmitBillVo") instanceof Map) {
            ((Map) map.get("abEmitBillVo")).put("voucherId", abEmitBillDTO.getVoucherId());
        }
        FcVoucherType fcVoucherType = fcVoucherTypeService.get(abEmitBillDTO.getVoucherTypeId(), true);
        if (NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())) {
            if (StringUtils.isNotBlank(fcVoucherType.getBillSectionControl())) {
                if (NetbankConstant.BILL_SECTION_THREE.equals(fcVoucherType.getBillSectionControl())) {
                    model.put("billSectionThreeButton", 1);
                } else {
                    model.put("billSectionSixButton", 1);
                }
            } else {
                model.put("billSectionThreeButton", 1);
                model.put("billSectionSixButton", 1);
            }
        }
        return AjaxJson.success(model);
    }

    /**
     * 票据开票 页面跳转
     *
     * @return
     */
    @ApiOperation(" 票据开票 页面跳转")
    @PostMapping("/billListGetInfo")
    public AjaxJson billListGetInfo(@RequestBody AbEmitBillDTO abEmitBillDTO) {
        AjaxJson json = new AjaxJson();
        Map map = new HashMap();
        if (StringUtils.isNotBlank(abEmitBillDTO.getVoucherId())) {
            List<NisBillheadVO> nisBillheadVOS = nisBillheadService.listByIdUnion(abEmitBillDTO.getVoucherId());
            BigDecimal sumMoney = BigDecimal.ZERO;
            for (NisBillheadVO nVo : nisBillheadVOS) {
                sumMoney = sumMoney.add(nVo.getPayMoney());
            }
            map.put("payMoney", sumMoney);
        }
        json.setAttributes(map);
        return json;
    }

    /**
     * 票据开票 页面跳转
     *
     * @return
     */
    @ApiOperation(" 票据开票页面跳转")
    @LogMessage(content = "票据开票列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/billList")
    public AjaxJson<AbEmitBillListVO> billList(@RequestBody AbEmitBillDTO abEmitBillDTO) {
        AbEmitBillListVO vo = new AbEmitBillListVO();
        String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
        vo.setAppNo(appNo);
        vo.setRmk(abEmitBillDTO.getRmk());
        if (AccebillConstant.BILL_ISBATCHBILL.equals(abEmitBillDTO.getIsBatchBill())) {
            String batchNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_BATCHNO);
            vo.setBatchNo(batchNo);
            vo.setIsBatchBill(AccebillConstant.BILL_ISBATCHBILL);
        }
        // 挂预算系统参数
        if (iAbVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            vo.setPayItem(iAbVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
        }
        // 挂授信
        boolean isRelateRat = iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISCREDIT).toBoolean(false);
        // 挂内部授信
        boolean isOpenCpRat = iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISOPENCPRAT).toBoolean(false);
        vo.setIsRelateRat(isRelateRat);
//
        BtCurrencyDTO rmbCurrency = iBtCurrencyService.getRMBCurrency();
        vo.setCurId(rmbCurrency == null ? null : rmbCurrency.getId());
//		}
        vo.setExternalCorpNum(iConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).toInteger(0));
        vo.setIsEmitMake(iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISEMITMAKE).toBoolean(true));
        //是否挂保证金
        vo.setIsBail(iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISBAIL).toBoolean(true));
        //挂保证金银行类型
        vo.setBailBankControlType(iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_BAILBANKCONTROLTYPE).getValue());
        //是否启用授信敞口
        vo.setMouthRat(iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISOPENRAT).toInteger(0) >= 1 ? true : false);
        //是否启用担保参数
        vo.setIsGuarantee(iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISBEAR).toInteger(0));
        vo.setIsHangGuarantee(AccebillConstant.AB_BILL_VALID_SIGN_1);
        //获取纸票票据金额最大额度
        vo.setMaxBillMoney(iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_MAXBILLMONEY));
        //增加是否挂内部授信
        vo.setIsOpenCpRat(isOpenCpRat);
        vo.setExternalCorp(iConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        vo.setCorpId(abEmitBillDTO.getCorpId() == null ? "" : abEmitBillDTO.getCorpId());
        vo.setPayMoney(abEmitBillDTO.getBillMoney() == null ? null : abEmitBillDTO.getBillMoney());
        vo.setPayeeacc(abEmitBillDTO.getPayeeacc() == null ? "" : abEmitBillDTO.getPayeeacc());
        vo.setPayeeAccName(abEmitBillDTO.getPayeeAccName() == null ? "" : abEmitBillDTO.getPayeeAccName());
        vo.setPayeebankno(abEmitBillDTO.getPayeebankno() == null ? "" : abEmitBillDTO.getPayeebankno());
        vo.setPayeeBankName(abEmitBillDTO.getPayeeBankName() == null ? "" : abEmitBillDTO.getPayeeBankName());
        vo.setBlRcvpymtpsUnnIdNo((abEmitBillDTO.getBlRcvpymtpsUnnIdNo() == null || "undefined".equals(abEmitBillDTO.getBlRcvpymtpsUnnIdNo().toString())) ? "" : abEmitBillDTO.getBlRcvpymtpsUnnIdNo());
        vo.setIsPayFlg("1");//是否是付款办理弹窗开票

        vo.setPayeracc(abEmitBillDTO.getPayeracc());
        vo.setPayer(abEmitBillDTO.getPayer());
        vo.setPayerbankno(abEmitBillDTO.getPayerbankno());
        if ("".equals(abEmitBillDTO.getVoucherId())) {
            abEmitBillDTO.setVoucherId(null);
        }
        if (StringUtils.isNotBlank(abEmitBillDTO.getVoucherId())) {
            String[] payIds = abEmitBillDTO.getVoucherId().split(",");
            for (String payId : payIds) {
                NisBillhead nisBillhead = nisBillheadService.get(payId);
                vo.setCorpId(nisBillhead.getCorpId());
                if (StringUtils.isNotBlank(nisBillhead.getPayerAcc())) {
                    abEmitBillDTO.setPayeracc(nisBillhead.getPayerAcc());
                    vo.setPayeracc(nisBillhead.getPayerAcc());
                    vo.setPayer(nisBillhead.getPayerAccName());
                    vo.setPayerBankName(nisBillhead.getPayerBank());
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(abEmitBillDTO.getPayeracc())) {
            BtBankAccDTO bankAccInfoByBankAcc = btBankAccService.getBankAccInfoByBankAcc(abEmitBillDTO.getPayeracc());
            if (bankAccInfoByBankAcc != null) {
                vo.setPayerbankno(bankAccInfoByBankAcc.getBankCode());
                vo.setCirculationMode(bankAccInfoByBankAcc.getCirculationMode());
                Map<String, Object> searchMap = new HashMap();
                searchMap.put("bankTypeId", bankAccInfoByBankAcc.getBankTypeId());
                List<AbBillBankAutoDtlDTO> dtos = accebillService.listAbBillBankAutoDtlByParams(searchMap);
                if (!CollectionUtils.isEmpty(dtos)) {
                    AbBillBankAutoDtlDTO dto = dtos.get(0);
                    vo.setBifCode(dto.getBifCode());
                }
            }
        }
        vo.setVoucherId(abEmitBillDTO.getVoucherId());
        vo.setAllowDifferentBillMoney(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue());
        vo.setApplyCorpId(vo.getCorpId());
        if (vo.getPayeracc() != null && StringUtils.isNotBlank(vo.getPayeracc())) {
            BtBankAccDTO payeracc = btBankAccService.getBankAccInfoByBankAcc(vo.getPayeracc());
            if (payeracc != null) {
                vo.setCorpId(payeracc.getCorpId());
            }
        }
        FcVoucherType fcVoucherType = fcVoucherTypeService.getById(abEmitBillDTO.getVoucherTypeId());
        if (fcVoucherType != null && NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())) {
            if (StringUtils.isNotBlank(fcVoucherType.getBillSectionControl())) {
                if (NetbankConstant.BILL_SECTION_THREE.equals(fcVoucherType.getBillSectionControl())) {
                    vo.setBillSectionThreeButton(1);
                } else {
                    vo.setBillSectionSixButton(1);
                }
            } else {
                vo.setBillSectionThreeButton(1);
                vo.setBillSectionSixButton(1);
            }
        }
        return AjaxJson.success(vo);
    }

    /**
     * 付款通知表 table列表
     *
     * @return
     */
    @ApiOperation("付款通知表 table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisBillheadVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput<NisBillheadVO> out = nisBillheadService.getDataTablesUnion(input, this.getCurrentUser());
        out = nisBillheadService.setDtlReceipt(out);
        return AjaxJson.success(out);
    }

    /**
     * 付款通知表新增页面跳转
     *
     * @return
     */
    @ApiOperation("付款通知表新增页面跳转")
    @GetMapping("/add")
    public AjaxJson<NisBillheadUpdateVO> add() {
        NisBillheadUpdateVO vo = new NisBillheadUpdateVO();
        vo.setRmbCurrency(iBtCurrencyService.getRMBCurrency());
        vo.setWishPayday(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        vo.setRequiredBusiness(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        vo.setIsAccebillIntellect(configService.getConfig(SysConfigConstant.Module.ACCEBILL, "isAccebillIntellect").getValue());
        //初始化参数
        this.initConfig(vo, 0);
        return AjaxJson.success(vo);
    }

    /**
     * 付款通知表编辑页面跳转
     *
     * @return
     */
    @ApiOperation("付款通知表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisBillheadUpdateVO> update(@PathVariable("id") String id) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        NisBillheadVO nisBillhead = null;
        if (StringUtils.isNotEmpty(id)) {
            nisBillhead = nisBillheadService.getByIdUnion(id);
            //暂存和编辑状态才可以提交
            if (nisBillhead != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus())
                    && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus())
                    && !NetbankConstant.STATUE_PENDING.equals(nisBillhead.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
            }
            if (nisBillhead != null && StringUtils.isNotEmpty(nisBillhead.getBillId())) {
                String billId = nisBillhead.getBillId();
                String vid = null;
                String vvid = null;
                String billPoolIds = null;
                String billBalance = "";
                List<AbVoucherinfoViceDTO> list = accebillService.listAccebillInfoByIds(billId, nisBillhead.getId());
                //是否已付款为否且应收票据不需要做BillPayMiddle部分额度占用处理
                if (!(NetbankConstant.PAY_TYPE_50.equals(nisBillhead.getSysVoucherCode())
                        && NetbankConstant.ISPAYED_0.equals(nisBillhead.getIsPayed()))) {
                    List<AbBillPayMiddleDTO> billPayMiddleDTOS = accebillService.getAbBillPayMiddleListByPayId(nisBillhead.getId());
                    for (Object temp : list) {
                        NisBillheadVO billheadVO = BeanUtils.copyNewInstanceProperties(temp, NisBillheadVO.class);
                        if (!CollectionUtils.isEmpty(billPayMiddleDTOS)) {
                            for (AbBillPayMiddleDTO d : billPayMiddleDTOS) {
                                if (billheadVO.getVvid().equals(d.getBillId()) && "".equals(billBalance)) {
                                    billBalance = d.getPayMoney().toString();
                                    break;
                                } else if (billheadVO.getVvid().equals(d.getBillId())) {
                                    billBalance = billBalance + "," + d.getPayMoney().toString();
                                    break;
                                }
                            }
                        }
                        vid = StringUtils.isEmpty(vid) ? billheadVO.getVid() : vid + "," + billheadVO.getVid();
                        vvid = StringUtils.isEmpty(vvid) ? billheadVO.getVvid() : vvid + "," + billheadVO.getVvid();
                        if ("1".equals(billheadVO.getPoolStatus())) {
                            billPoolIds = StringUtils.isEmpty(billPoolIds) ? billheadVO.getId() : billPoolIds + "," + billheadVO.getId();
                        }
                    }
                } else {
                    billBalance = nisBillService.getBillBalances(nisBillhead.getBillId(), nisBillhead.getId());
                    for (Object temp : list) {
                        NisBillheadVO billheadVO = BeanUtils.copyNewInstanceProperties(temp, NisBillheadVO.class);
                        vid = StringUtils.isEmpty(vid) ? billheadVO.getVid() : vid + "," + billheadVO.getVid();
                        vvid = StringUtils.isEmpty(vvid) ? billheadVO.getVvid() : vvid + "," + billheadVO.getVvid();
                        if ("1".equals(billheadVO.getPoolStatus())) {
                            billPoolIds = StringUtils.isEmpty(billPoolIds) ? billheadVO.getId() : billPoolIds + "," + billheadVO.getId();
                        }
                    }
                }
                model.setBillBalance(billBalance);
                nisBillhead.setVid(vid);
                nisBillhead.setVvid(vvid);
                nisBillhead.setBillPoolIds(billPoolIds);
                nisBillhead.setBillBalance(billBalance);
            }
            model.setNisBillheadPage(nisBillhead);
            model.setPayerBankAcc(this.btBankAccService.getBankAccByBankAccAndCurId(nisBillhead.getPayerAcc(), nisBillhead.getCurId()));
            //初始化参数
            this.initConfig(model, 0);
        }
        model.setWishPayday(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //未产生资金需求付款单付款控制 0-不提醒，1-只提醒不控制，2-控制
        model.setDemandControl(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_DEMANDCONTROL).getValue());
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        model.setCorp(corpService.get(nisBillhead.getCorpId()));
        model.setRequiredBusiness(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        //费用报销单编辑跳转
        if (nisBillhead != null && (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.toString()).equals(nisBillhead.getBillFrom())) {
            model.setListNisExpenseDetail(nisExpenseDetailService.listByBillId(id));
            //初始化参数
            this.initConfig(model, 1);
            return AjaxJson.success(model);
        }
        //初始化单据编辑项参数
        this.initEditConfig(model, nisBillhead);
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        if (voucherType != null && NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(nisBillhead.getIsOpenBill())) {
            String billIds = nisBillService.getBillIds(nisBillhead.getId());
            if (StringUtils.isNotBlank(billIds)) {
                model.setOpenBillInfo(billIds + ",first");
            }
        }
        model.setRmbCurrency(btCurrencyService.getRMBCurrency());
        //供应链金融（51/52）凭证回显
        this.setScfVoucherEcho(nisBillhead);

        return AjaxJson.success(model);
    }

    /**
     * 初始化系统参数
     *
     * @param model
     * @param busType
     */
    private void initConfig(NisBillheadUpdateVO model, Integer busType) {
        if (busType == 1) {
            //费用报销处理
            model.setBudgetSel(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue());
            model.setPayItem(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_PAYITEM).getValue());
            model.setExpFbsin(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue());
            model.setAccountOver(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_ACCOUNTOVER).getValue());
            model.setBankaccOver(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BANKACCOVER).getValue());
            model.setShowIsPayed(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        } else {
            model.setBudgetSel(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue());
            model.setPayItem(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_PAYITEM).getValue());
            model.setAccountOver(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ACCOUNTOVER).getValue());
            model.setBankaccOver(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BANKACCOVER).getValue());
            model.setShowIsPayed(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
            model.setExternalCorp(configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
            model.setAllowDifferentBillMoney(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue());
        }
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //付款余额控制方式  0-账户余额 1-可用余额
        model.setBalControlType(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BALCONTROLTYPE).getValue());
    }

    /**
     * 初始化单据编辑项参数
     *
     * @param model
     * @param nisBillhead
     */
    private void initEditConfig(NisBillheadUpdateVO model, NisBillheadVO nisBillhead) {
        //ERP接口传输的单据编辑项控制
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_6.equals(nisBillhead.getBillFrom())) {
            model.setEditVoucherTypeId(configService.getConfig(ConfigConstant.ERP, "voucherTypeId").getValue());
            model.setEditPayeeAcc(configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue());
            model.setEditPayMoney(configService.getConfig(ConfigConstant.ERP, "payMoney").getValue());
            model.setEditPayerAcc(configService.getConfig(ConfigConstant.ERP, "payeeAcc").getValue());
            model.setEditPayeeAccName(configService.getConfig(ConfigConstant.ERP, "payeeAccName").getValue());
            model.setEditPayeeBank(configService.getConfig(ConfigConstant.ERP, "payeeBank").getValue());
            model.setEditIsForindividual(configService.getConfig(ConfigConstant.ERP, "isForindividual").getValue());
            model.setEditWishPayday(configService.getConfig(ConfigConstant.ERP, "wishPayday").getValue());
            model.setEditPurpose(configService.getConfig(ConfigConstant.ERP, "purpose").getValue());
            model.setEditBillType(configService.getConfig(ConfigConstant.ERP, "billType").getValue());
            model.setEditBillFlag(configService.getConfig(ConfigConstant.ERP, "billFlag").getValue());
        } else {
            model.setEditVoucherTypeId("1");
            model.setEditPayeeAcc("1");
            model.setEditPayeeAcc("1");
            model.setEditPayeeAccName("1");
            model.setEditPayeeBank("1");
            model.setEditIsForindividual("1");
            model.setEditWishPayday("1");
            model.setEditPurpose("1");
        }
        //控制付款金额不允许编辑，拆分单据/合并单据都不允许编辑金额
        boolean splitIdFlag = !EmptyUtils.isEmpty(nisBillhead) && !EmptyUtils.isBlank(nisBillhead.getSplitId());
        if (splitIdFlag || nisBillheadService.isMergeBill(nisBillhead.getId())) {
            model.setEditPayMoney("0");
        } else {
            if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_6.equals(nisBillhead.getBillFrom())) {
                model.setEditPayMoney("1");
            }
        }
    }

    /**
     * 流水列表查看
     *
     * @return 前端路径
     */
    @ApiOperation("流水列表查看")
    @LogMessage(content = "流水列表查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewdtls/{id}")
    public AjaxJson<List<com.bytter.biz.bdlink.api.dto.BisAccDtlDTO>> viewdtls(@PathVariable("id") String id) {
        List<EBankMoreBillVO> eBankMoreBillVOList = nisBillheadService.getNisBillheadDtl(id);
        List<com.bytter.biz.bdlink.api.dto.BisAccDtlDTO> list = new ArrayList<>();
        for (EBankMoreBillVO eBankMoreBillVO : eBankMoreBillVOList) {
            list.add(bisAccDtlService.getById(eBankMoreBillVO.getDtlId()));
        }
        return AjaxJson.success(list);
    }

    /**
     * 回单列表查看
     *
     * @return
     */
    @ApiOperation("回单列表查看")
    @LogMessage(content = "回单列表查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewReceipts/{id}")
    public AjaxJson<ViewReceiptsVO> viewReceipts(@PathVariable("id") String id) {
        ViewReceiptsVO model = new ViewReceiptsVO();
        List<EBankMoreBillVO> eBankMoreBillVOList = nisBillheadService.getNisBillheadDtl(id);
        List<BisBankDtlBillVO> bisBankDtlBillVOList = new ArrayList<>();
        for (EBankMoreBillVO eBankMoreBillVO : eBankMoreBillVOList) {
            bisBankDtlBillVOList.addAll(nisBillheadService.getDtlreceipt(eBankMoreBillVO.getDtlId()));
        }
        List<BisBankReceiptDtlDTO> list = new ArrayList<>();
        List<BisBankReceiptDtlHisVO> hislist = new ArrayList<>();
        for (BisBankDtlBillVO bisBankDtlBillVO : bisBankDtlBillVOList) {
            BisBankReceiptDtlDTO bisBankReceiptDtlDTO = bisBankReceiptDtlService.getById(bisBankDtlBillVO.getReceiptId());
            if (!EmptyUtils.isEmpty(bisBankReceiptDtlDTO)) {
                list.add(bisBankReceiptDtlDTO);
            } else {
                //获取历史电子回单
                BisBankReceiptDtlHisVO bisBankReceiptDtlHis = bisBankReceiptDtlHisService.getByIdUnion(bisBankReceiptDtlDTO.getId());
                if (!EmptyUtils.isEmpty(bisBankReceiptDtlHis.getId())) {
                    hislist.add(bisBankReceiptDtlHis);
                }
            }
        }
        model.setList(list);
        model.setHislist(hislist);
        return AjaxJson.success(model);
    }

    /**
     * 列表打印
     *
     * @return
     */
    @ApiOperation("列表打印")
    @GetMapping("/print")
    public AjaxJson<String> print(String id) {
        this.nisBillheadService.updatePrint(id);
        return AjaxJson.success(id);
    }

    /**
     * 历史电子回单打印
     *
     * @return
     */
    @ApiOperation("历史电子回单打印")
    @GetMapping("/printHis")
    public AjaxJson<String> printHis(String id) {
        this.nisBillheadService.updatePrint(id);
        return AjaxJson.success(id);
    }

    /**
     * 批量查看
     *
     * @return
     */
    @ApiOperation("批量查看")
    @GetMapping("/batchPreview")
    public AjaxJson<List<String>> batchPreview(String ids) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            listId.add(id);
        }
        return AjaxJson.success(listId);
    }

    /**
     * 批量打印
     *
     * @return
     */
    @ApiOperation("批量打印")
    @GetMapping("/batchPrint")
    public AjaxJson<List<String>> batchPrint(String ids) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.nisBillheadService.updatePrint(id);
            listId.add(id);
        }
        return AjaxJson.success(listId);
    }

    /**
     * 付款通知表详情页
     *
     * @return
     */
    @ApiOperation("付款通知表详情页")
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisBillheadUpdateVO> view(@PathVariable("id") String id) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        NisBillheadVO nisBillhead = nisBillheadService.getByIdUnion(id);
        if (nisBillhead == null) {
            return AjaxJson.success(model);
        }
        model.setNisBillheadPage(nisBillhead);
        //初始化参数
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
            this.initConfig(model, 1);
        } else {
            this.initConfig(model, 0);
        }
        //对私付款单获取对私付款明细
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.FORINDIVIDUAL_1.equals(nisBillhead.getIsForindividual())) {
            List<NisExpenseDetailVO> listNisExpenseDetail = nisExpenseDetailService.listByBillId(nisBillhead.getId());
            if (CollectionUtils.isEmpty(listNisExpenseDetail)) {
                model.setListNisExpenseDetail(new ArrayList<NisExpenseDetailVO>());
            }
            List<String> regionQuery = new ArrayList<String>();
            for (NisExpenseDetailVO dvo : listNisExpenseDetail) {
                if (StringUtils.isNotEmpty(dvo.getCity())) {
                    regionQuery.add(dvo.getCity());
                    logger.info("dvo.getCity()====" + dvo.getCity());
                }
                if (StringUtils.isNotEmpty(dvo.getProv())) {
                    regionQuery.add(dvo.getProv());
                    logger.info("dvo.getProv()====" + dvo.getProv());
                }
            }
            Map<String, RegionDTO> regionDTOMap = null;
            logger.info("regionQuery=" + regionQuery.size());
            if (!CollectionUtils.isEmpty(regionQuery)) {
                regionDTOMap = regionService.listByIds(regionQuery);
            }

            if (regionDTOMap != null && !regionDTOMap.isEmpty()) {
                for (NisExpenseDetailVO vo : listNisExpenseDetail) {
                    if (regionDTOMap.containsKey(vo.getCity())) {
                        vo.setCityName(regionDTOMap.get(vo.getCity()).getName());
                    }
                    if (regionDTOMap.containsKey(vo.getProv())) {
                        vo.setProvName(regionDTOMap.get(vo.getProv()).getName());
                    }
                }
            }
            model.setListNisExpenseDetail(listNisExpenseDetail);
        }
        //合并明细
        List<NisBillheadVO> listMerge = nisBillheadService.listByMergeId(id);
        String isSplitOrMerge = "0";
        if (!EmptyUtils.isEmptyList(listMerge)) {
            isSplitOrMerge = "1";
        } else {
            if (StringUtils.isNotBlank(nisBillhead.getSplitId())) {
                isSplitOrMerge = "1";
                listMerge = new ArrayList<>();
                listMerge.add(nisBillheadService.getByIdUnion(nisBillhead.getSplitId()));
            }
        }
        model.setIsSplitOrMerge(isSplitOrMerge);
        model.setListMerge(listMerge);
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        if (voucherType != null) {
            model.setIsBudget(voucherType.getIsBudget());
        }
        model.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        //供应链金融（51/52）凭证回显
        this.setScfVoucherEcho(nisBillhead);
        return AjaxJson.success(model);
    }

    /**
     * 添加付款通知表
     * 【付款办理】
     *
     * @param nisBillhead
     * @return
     */
    @ApiOperation("添加付款通知表")
    @LogMessage(content = "添加付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody NisBillheadVO nisBillhead) {
        String billNo = "";
        if (dataCheck(nisBillhead, false)) {
            nisBillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
            billNo = nisBillheadService.saveNisBillhead(nisBillhead, this.getCurrentUser());
            //付款预警增加
            nisBillheadService.warnExecuteNis(nisBillhead, this.getCurrentUser());
        }
        return success(billNo);
    }

    /**
     * 修改付款通知表
     *
     * @param nisBillhead
     * @return
     */
    @ApiOperation("修改付款通知表")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody NisBillheadVO nisBillhead) {
        if (dataCheck(nisBillhead, false)) {
            nisBillhead.setBatch(false);
            nisBillheadService.updateNisBillhead(nisBillhead, this.getCurrentUser(), false);
            //付款预警增加
            nisBillheadService.warnExecuteNis(nisBillhead, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 数据校验
     *
     * @param nisBillhead
     * @return
     */
    private boolean dataCheck(NisBillheadVO nisBillhead, Boolean isBatch) {
        //收款账号去空格
        if (StringUtils.isNotBlank(nisBillhead.getPayeeAcc())) {
            nisBillhead.setPayeeAcc(nisBillhead.getPayeeAcc().trim());
        }
        //单位不为空
        if (StringUtils.isEmpty(nisBillhead.getCorpId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CORP_NULL);
        }
        //付款方式不能为空
        if (StringUtils.isEmpty(nisBillhead.getVoucherTypeId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_VOUCHERTYPE_NULL);
        }
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        // 非应付票据，应收票据才校验付款账号（51 供应链金融债权凭证走平台结算，不校验付款账号）
        if (!(com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode()))) {
            //付款账号不为空
            if (StringUtils.isEmpty(nisBillhead.getPayerAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NULL);
            }
        }
        // 如果是应收票据，且期望日期大于当天，把期望日期改为当天
        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
            if (DateUtils.getDateYMD().before(nisBillhead.getWishPayday())) {
                nisBillhead.setWishPayday(DateUtils.getDateYMD());
            }
        }
        //转付记账时，需要校验银行账号
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())
                && com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())) {
            if (StringUtils.isEmpty(nisBillhead.getDownAccId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_DOWNACC_NULL);
            }
        }
        //币别不为空
        if (StringUtils.isEmpty(nisBillhead.getCurId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CUR_NULL);
        }
        //用途不为空
        if (StringUtils.isEmpty(nisBillhead.getPurpose())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PURPOSE_NULL);
        }
        //收付款账号不能相同（供应链金融债权凭证收款账号非必录，为空时跳过）
        if (StringUtils.isNotBlank(nisBillhead.getPayeeAcc()) && StringUtils.isNotBlank(nisBillhead.getPayerAcc())
                && !com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equalsIgnoreCase(nisBillhead.getBillFrom())
                && nisBillhead.getPayeeAcc().equals(nisBillhead.getPayerAcc())) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
        }
        //不记账的付款方式校验跨行标志和地域标志(不包含对私付款)
        if (StringUtils.isNotBlank(nisBillhead.getBillNo()) && CommonConstant.NO.equals(voucherType.getIsAccount()) && com.bytter.biz.bdlink.api.constant.NetbankConstant.FORINDIVIDUAL_0.equals(nisBillhead.getIsForindividual())) {
            if (StringUtils.isBlank(nisBillhead.getDifBank())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_DIFBANK_NULL, nisBillhead.getBillNo());
            }
            if (StringUtils.isBlank(nisBillhead.getAreaSign())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_AREASIGN_NULL, nisBillhead.getBillNo());
            }
        }
        // 应付票据已开票，应收票据校验票据金额
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && CommonConstant.YES.equals(nisBillhead.getIsOpenBill()))) {
            //考虑历史数据票据金额为空的不做校验，默认票据金额=付款金额
            if (nisBillhead.getBillMoney() != null) {
                // 如果票据金额为0，表示为拆分或合并后的票据信息，需要重新挂接票据信息
                if (BigDecimal.ZERO.compareTo(nisBillhead.getBillMoney()) == 0) {
                    throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_AGAIN_SELECT_BILL);
                }
                //允许付款金额与票据金额不一致
                String allowDifferentBillMoney = configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue();
                if (!CommonConstant.YES.equals(allowDifferentBillMoney)
                        && nisBillhead.getBillMoney().compareTo(nisBillhead.getPayMoney()) != 0) {
                    throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BILLMONEY_MISMATCH);
                }
            } else {
                nisBillhead.setBillMoney(nisBillhead.getPayMoney());
            }
        }
        //供应链金融：51-应付债权凭证（已开立）必选开立凭证
        //线上线下标志不在此处理，由服务端按"线下付款（不产生指令）"isPayed 推导：是=线下(0)，否=线上(1)
        if (NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode())) {
            if (CommonConstant.YES.equals(nisBillhead.getScfIsOpened()) && StringUtils.isBlank(nisBillhead.getScfVoucherIds())) {
                throw new ParameterException("应付债权凭证已开立时，必须选择开立的应付债权凭证！");
            }
        }
        //供应链金融：52-应收债权凭证 必选转让草稿（保存/提交都触发转让数据保存，前端提交付款单前已调 saveTransfer 返回草稿id）
        //批量提交不走此校验（批量从数据库加载VO不带scfTransferIds），由 batchSubmit 按中间表 nis_scf_info 校验
        if (NetbankConstant.PAY_TYPE_52.equals(voucherType.getSysVoucherCode()) && !isBatch
                && StringUtils.isBlank(nisBillhead.getScfTransferIds())) {
            throw new ParameterException("应收债权凭证（付款方式52）必须保存应收债权凭证转让信息！");
        }
        if (!isBatch) {
            String budgetSel = configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
            // 启用资金计划才校验
            if (CommonConstant.YES.equals(budgetSel)) {
                String isBudget = voucherType.getIsBudget();
                boolean flag = true;
                if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equalsIgnoreCase(nisBillhead.getBillFrom())) {
                    //是否挂资金计划
                    String expBudgetSel = configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue();
                    //报销计划挂接环节
                    String expFbsin = configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue();
                    if (CommonConstant.NO.equalsIgnoreCase(expBudgetSel)) {
                        flag = false;
                    } else if (ExpenseConstant.EXPFBSIN_ENTER.equalsIgnoreCase(expFbsin)) {
                        flag = false;
                    }
                }
                if (this.fbsCorpBudgetService.controlBudgetByCorpId(nisBillhead.getCorpId())) {
                    if (CommonConstant.YES.equals(budgetSel) && CommonConstant.YES.equals(isBudget) && flag) {
                        //验证是否有预算
                        if (EmptyUtils.isEmptyArray(nisBillhead.getItemCode())) {
                            List<FbsBudgetProcessDTO> listBudget = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, nisBillhead.getBillNo());
                            if (EmptyUtils.isEmptyList(listBudget)) {
                                throw new ParameterException(NetbankMessageEnum.NIS_BILLHEAD_ITEMCODE_NOTEXISTS);
                            }
                        }
                    }
                }
            }
        }
        //状态校验
        if (StringUtils.isNotEmpty(nisBillhead.getId())) {
            NisBillhead entity = null;
            if (isBatch) {
                entity = new NisBillhead();
                BeanUtils.copyNotNullProperties(nisBillhead, entity);
            } else {
                entity = nisBillheadService.get(nisBillhead.getId());
            }
            //暂存和编辑状态才可以提交
            if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        return true;
    }

    /**
     * 供应链金融（51/52）回显：从中间表 nis_scf_info 读取已关联的凭证id并加载凭证明细
     *
     * @param model      页面模型
     * @param nisBillhead 付款单VO（需已加载 id/sysVoucherCode）
     */
    private void setScfVoucherEcho(NisBillheadVO nisBillhead) {
        if (nisBillhead == null || nisScfInfoService == null) {
            return;
        }
        String sysVoucherCode = nisBillhead.getSysVoucherCode();
        //编辑时不一次性加载凭证明细，只回显凭证ID/平台/产品，由前端点击选择/查看按钮时按需调 ...ByIds 接口
        if (NetbankConstant.PAY_TYPE_51.equals(sysVoucherCode)) {
            NisScfInfo ctx = nisScfInfoService.getContext(nisBillhead.getId(), NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE);
            if (ctx != null) {
                //51-未开立：只有平台和融资产品；51-已开立：平台/产品 + 凭证ID
                nisBillhead.setScfPlatformId(ctx.getPlatformId());
                nisBillhead.setScfProductId(ctx.getProductId());
            }
            nisBillhead.setScfVoucherIds(nisScfInfoService.getVoucherIds(nisBillhead.getId(), NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE));
        }
        //供应链金融：52-应收债权凭证 中间表 transfer_id 存转让草稿ID，回显转让草稿ID，并反查派生凭证ID/平台/产品
        if (NetbankConstant.PAY_TYPE_52.equals(sysVoucherCode)) {
            String transferIds = nisScfInfoService.getTransferIds(nisBillhead.getId(), NetbankConstant.SCF_VOUCHER_TYPE_RECEIVABLE);
            nisBillhead.setScfTransferIds(transferIds);
            if (StringUtils.isNotBlank(transferIds)) {
                List<ScfReceivableVoucherSelDTO> selList = scfVoucherPayService.getReceivableVoucherByTransferIds(StringUtils.splitToList(",", transferIds));
                StringBuilder voucherIdStr = new StringBuilder();
                String platformId = null;
                String productId = null;
                String transferFlag = null;
                for (ScfReceivableVoucherSelDTO sel : selList) {
                    if (StringUtils.isNotBlank(sel.getId())) {
                        if (voucherIdStr.length() > 0) {
                            voucherIdStr.append(CommonConstant.COMMA_SPLIT);
                        }
                        voucherIdStr.append(sel.getId());
                    }
                    if (platformId == null && StringUtils.isNotBlank(sel.getPlatformId())) {
                        platformId = sel.getPlatformId();
                    }
                    if (productId == null && StringUtils.isNotBlank(sel.getProductId())) {
                        productId = sel.getProductId();
                    }
                    // 同一付款单的转让草稿转让标识一致（1-单个转让/2-批量转让），取首个非空
                    if (transferFlag == null && StringUtils.isNotBlank(sel.getTransferFlag())) {
                        transferFlag = sel.getTransferFlag();
                    }
                }
                nisBillhead.setScfReceivableVoucherIds(voucherIdStr.toString());
                nisBillhead.setScfPlatformId(platformId);
                nisBillhead.setScfProductId(productId);
                nisBillhead.setScfTransferFlag(transferFlag);
            }
        }
    }

    /**
     * 删除付款通知表
     *
     * @param id
     * @return
     */
    @ApiOperation("删除付款通知表")
    @LogMessage(content = "删除付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{chargeBackType}")
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("chargeBackType") String chargeBackType) {
        //状态校验
        NisBillhead entity = nisBillheadService.get(id, true);
        //暂存和编辑状态才可以提交
        if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        nisBillheadService.delNisBillhead(id, this.getCurrentUser(), false, chargeBackType);
        return success();
    }

    /**
     * 批量删除付款通知表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除付款通知表")
    @LogMessage(content = "批量删除付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@RequestParam("ids") String ids, @RequestParam("chargeBackType") String chargeBackType) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillhead entity = nisBillheadService.get(id, true);
            //暂存和编辑状态才可以提交
            if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisBillheadService.batchDelNisBillhead(ids, this.getCurrentUser(), chargeBackType);
        return success();
    }

    /**
     * 批量暂挂
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量暂挂")
    @LogMessage(content = "批量暂挂付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/pendingBatch")
    public AjaxJson pendingBatch(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillhead entity = nisBillheadService.get(id, true);
            //暂存和编辑状态才可以提交
            if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisBillheadService.pendingBatchNisBillhead(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 批量解除暂挂
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量解除暂挂付款通知表")
    @LogMessage(content = "批量解除暂挂付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/relievePendingBatch")
    public AjaxJson relievePendingBatch(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillhead entity = nisBillheadService.get(id, true);
            //暂挂状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_PENDING.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisBillheadService.relievePendingBatch(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 应收选票保存
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量修改付款通知表")
    @LogMessage(content = "批量修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSelectAccebill")
    public AjaxJson batchSelectAccebill(@RequestParam("ids") String ids, @RequestParam("billId") String billId) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillheadVO nisBillhead = nisBillheadService.getByIdUnion(id);
            //暂存和编辑状态才可以提交
            if (nisBillhead != null && !NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
            }
            nisBillhead.setBillId(billId);
            nisBillheadService.batchSelectAccebill(nisBillhead, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 批量提交付款通知表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量提交付款通知表")
    @LogMessage(content = "批量提交付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    public AjaxJson batchSubmit(@RequestParam("ids") String ids,@RequestParam("caCodings") String caCodings, @RequestParam(value = "isBatchNoSubmit", defaultValue = "false") boolean isBatchNoSubmit) {
        boolean flag = false;
        HashMap<String, NisBillheadVO> billMap = new HashMap<>();
        List<String> billNoList = new ArrayList<>();
        //开户行信息
        HashMap<String, NisBillheadVO> bankMap = new HashMap<>();
        HashMap<String, Boolean> corpBudgetMap = new HashMap<>();
        //获取用户有权限的付款账号
        HashMap<String, BtBankAccDTO> acountMap = nisBillheadService.getAccountGrantMap(getCurrentUser());
        HashMap<String, CorpDTO> corpMap = new HashMap<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillheadVO nisBillheadVO = nisBillheadService.getVoById(id);
            if ("49".equals(nisBillheadVO.getSysVoucherCode()) || "50".equals(nisBillheadVO.getSysVoucherCode())) {
                List<NisBill> nisBills = nisBillService.listByPayId(id);
                if (CollectionUtils.isEmpty(nisBills)) {
                    throw new ParameterException("不允许提交票据信息为空的付款单！");
                }
            }
            //供应链金融：51已开立必选开立凭证
            if (NetbankConstant.PAY_TYPE_51.equals(nisBillheadVO.getSysVoucherCode())
                    && CommonConstant.YES.equals(nisBillheadVO.getScfIsOpened())
                    && StringUtils.isBlank(nisScfInfoService.getVoucherIds(id, NetbankConstant.SCF_VOUCHER_TYPE_PAYABLE))) {
                throw new ParameterException("不允许提交应付债权凭证为空的付款单！");
            }
            //供应链金融：52应收债权凭证必选转让草稿（按中间表 nis_scf_info 的 transfer_id 校验）
            if (NetbankConstant.PAY_TYPE_52.equals(nisBillheadVO.getSysVoucherCode())
                    && StringUtils.isBlank(nisScfInfoService.getTransferIds(id, NetbankConstant.SCF_VOUCHER_TYPE_RECEIVABLE))) {
                throw new ParameterException("不允许提交应收债权凭证转让信息为空的付款单！");
            }
            Boolean reportBudget;
            if (corpBudgetMap.containsKey(nisBillheadVO.getCorpId())) {
                reportBudget = corpBudgetMap.get(nisBillheadVO.getCorpId());
            } else {
                reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(nisBillheadVO.getCorpId());
                corpBudgetMap.put(nisBillheadVO.getCorpId(), reportBudget);
            }
            //过滤挂资金计划的付款方式
            if (reportBudget && CommonConstant.YES.equals(nisBillheadVO.getIsBudget())) {
                billNoList.add(nisBillheadVO.getBillNo());
            }
            flag = dataCheck(nisBillheadVO, true);
            //账号权限校验
            FcVoucherType voucherType = fcVoucherTypeService.get(nisBillheadVO.getVoucherTypeId(), true);
            if (!acountMap.containsKey(nisBillheadVO.getPayerAcc())
                    && !(com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                    || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode())
                    || NetbankConstant.PAY_TYPE_51.equals(voucherType.getSysVoucherCode()))) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NOT_GRANT, nisBillheadVO.getPayerAcc());
            } else {
                if (StringUtils.isNotEmpty(nisBillheadVO.getPayerAcc())) {
                    BtBankAccDTO bankAcc = acountMap.get(nisBillheadVO.getPayerAcc());
                    //付款账号跟付款单位不同时，需要校验账号所属单位是否包含在付款单位的所用账号单位范围内
                    if (bankAcc != null && !bankAcc.getCorpId().equals(nisBillheadVO.getCorpId())) {
                        CorpDTO corp = corpMap.get(nisBillheadVO.getCorpId());
                        if (corp == null) {
                            corp = corpService.get(nisBillheadVO.getCorpId());
                        }
                        if (corp == null || StringUtil.isBlank(corp.getUserAccountCode())
                                || !corp.getUserAccountCode().contains(bankAcc.getCorpId())) {
                            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NOT_GRANT, nisBillheadVO.getPayerAcc());
                        }
                    }
                }
            }
            billMap.put(id, nisBillheadVO);
            //开户行信息去重
            if (StringUtils.isNotBlank(nisBillheadVO.getPayeeBankCode())) {
                if (!bankMap.containsKey(nisBillheadVO.getPayeeBankCode())) {
                    bankMap.put(nisBillheadVO.getPayeeBankCode(), nisBillheadVO);
                }
            }
        }
        for (String payeeBankCode : bankMap.keySet()) {
            // 初始化联行号信息
            this.bankService.saveBtBank(payeeBankCode, bankMap.get(payeeBankCode).getBankTypeId());
        }
        // 启用资金计划才校验
        String budgetSel = configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
        if (EmptyUtils.isNotEmpty(billNoList) && CommonConstant.YES.equals(budgetSel)) {
            //资金计划余额校验
            List<FbsBudgetProcessDTO> listBudget = fbsBudgetProcessService.getSumBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_PAY, BudgetConstant.PROCESS_SYSNAME_NIS, billNoList);
            if (EmptyUtils.isEmpty(listBudget)) {
                throw new ParameterException(NetbankMessageEnum.NIS_BILLHEAD_ITEMCODE_NOTEXISTS);
            }
            for (FbsBudgetProcessDTO processDTO : listBudget) {
                //校验单位是否上报/是否控制资金计划：单位上报且受控的情况前提下校验计划
                if (CommonConstant.YES.equals(processDTO.getReportBudget() + "") && CommonConstant.YES.equals(processDTO.getControlBudget() + "")) {
                    //判断科目是否受控
                    if (CommonConstant.YES.equals(processDTO.getIsControl())) {
                        //期望支付日期小于系统当前日期，则默认更新为系统当前日期
                        if (processDTO.getProcessDate().before(DateUtils.getDateYMD())) {
                            processDTO.setProcessDate(DateUtils.getDateYMD());
                        }
                        FbsBudgetProcessCommonDTO dto = fbsBudgetService.getFbsBudgetProcessByItemCode(processDTO.getCorpId(), processDTO.getItemCode(), processDTO.getProcessDate(), null, null);
                        //计划执行规则为5-先执行后控制时无需校验
                        if (!BudgetConstant.PROCESS_RULE_5.equals(dto.getProcessRule())) {
                            //计划执行科目余额控制方式
                            String itemBalance = configService.getConfig(BudgetConstant.FBS_CONFIG_TYPE, BudgetConstant.FBS_CONFIG_ITEMBALANCE).getValue();
                            if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_ONESELF.equals(itemBalance)) {
                                //受本科目控制
                                if (processDTO.getProcessMoney().doubleValue() > (dto.getItemMoney().add(processDTO.getProcessMoney())).doubleValue()) {
                                    throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_ITEMBALANCE);
                                }
                            }
                            if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_SUPER.equals(itemBalance)) {
                                //受上级科目控制
                                if (processDTO.getProcessMoney().doubleValue() > (dto.getSuperItemMoney().add(processDTO.getProcessMoney())).doubleValue()) {
                                    throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_SUPERITEMBALANCE);
                                }
                            }
                            if (BudgetConstant.FBS_CONFIG_ITEMBALANCE_TOP.equals(itemBalance)) {
                                //受大科目控制
                                if (processDTO.getProcessMoney().doubleValue() > (dto.getTopItemMoney().add(processDTO.getProcessMoney())).doubleValue()) {
                                    throw new ParameterException(FbsBudgetMessageEnum.FBS_MESSAGE_DETAILMONEY_GT_TOPITEMBALANCE);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (flag) {
            String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
            final CountDownLatch countDownLatch = new CountDownLatch(idStr.length);
            final ICurrentUser user = this.getCurrentUser();
            //线程执行结果集合
            List<Future<String>> futures = new ArrayList<>();
            List<String> futureBillNos = new ArrayList<>();
            //生成开票批次号，vo新增字段，其他业务不影响
            String batchNo = null;
            Map<String, Integer> voucherIdBatchDtlNoMap = new HashMap<>();
            if (isBatchNoSubmit) {
                batchNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_BATCHNO);
                //设置批次开票明细顺序号
                int i = 0;
                for (String id : idStr) {
                    voucherIdBatchDtlNoMap.put(id, ++i);
                }
            }
            for (int i = 0; i < idStr.length; i++) {
                String id = idStr[i];
                final NisBillheadVO nisBillhead = billMap.get(id);
                nisBillhead.setOpenBillBatchNo(batchNo);
                nisBillhead.setVoucherIdBatchDtlNoMap(voucherIdBatchDtlNoMap);
                if (!EmptyUtils.isEmpty(caCodings)) {
                    nisBillhead.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK);
                    nisBillhead.setBatch(true);
                    //使用线程池处理流程启动，并返回结果
                    Future<String> submit = NISBILLHEAD_START_EXECUTOR_SERVICE.submit(new Callable<String>() {
                        @Override
                        public String call() {
                            try {

                                nisBillheadService.batchSubmitNisBillhead(nisBillhead, null, null, null, user);
                            } catch (Exception e) {
                                throw e;
                            } finally {
                                //线程监听器统计已经完成处理
                                countDownLatch.countDown();
                            }
                            return "success";
                        }
                    });
                    futures.add(submit);
                    futureBillNos.add(nisBillhead.getBillNo());
                } else {
                    nisBillhead.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_WAIT_CHECK);
                    nisBillhead.setBatch(true);
                    //使用线程池处理流程启动，并返回结果
                    Future<String> submit = NISBILLHEAD_START_EXECUTOR_SERVICE.submit(new Callable<String>() {
                        @Override
                        public String call() {
                            try {
                                nisBillheadService.batchSubmitNisBillhead(nisBillhead, null, null, null, user);
                            } catch (Exception e) {
                                throw e;
                            } finally {
                                //线程监听器统计已经完成处理
                                countDownLatch.countDown();
                            }
                            return "success";
                        }
                    });
                    futures.add(submit);
                    futureBillNos.add(nisBillhead.getBillNo());
                }
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("nisbillhead batchSubmit error :{}", e);
            }
            //处理失败数量
            int errorCount = 0;
            int successCount = 0;
            //获取businessException
            List<BusinessException> errorBusinessExceptions = new ArrayList<>();
            List<LimitHitDetailDto> limitHitDetails = new ArrayList<>();
            for (int futureIndex = 0; futureIndex < futures.size(); futureIndex++) {
                Future<String> future = futures.get(futureIndex);
                String billNo = futureBillNos.get(futureIndex);
                try {
                    //获取结果
                    future.get();
                    successCount++;
                } catch (InterruptedException | ExecutionException e) {
                    //出现异常则统计错误
                    errorCount++;
                    Throwable cause = e.getCause();
                    if (cause instanceof BusinessException) {
                        BusinessException businessException = (BusinessException) cause;
                        if (businessException instanceof PaymentLimitExceededException) {
                            LimitHitDetailDto detail = ((PaymentLimitExceededException) businessException).getDetail();
                            if (detail != null) {
                                limitHitDetails.add(PaymentLimitBatchDetailDto.of(
                                        "table.nisBillhead.billNo", billNo, detail));
                            }
                        }
                        if (WorkFlowMessageEnum.WORK_FLOW_WAS_EXISTS.equals(businessException.getIMessageEnum().getEnum())) {
                            boolean workFlowWasExistsFlag = false;
                            for (BusinessException be : errorBusinessExceptions) {
                                if (WorkFlowMessageEnum.WORK_FLOW_WAS_EXISTS.equals(be.getIMessageEnum().getEnum())) {
                                    workFlowWasExistsFlag = true;
                                    break;
                                }
                            }
                            if (!workFlowWasExistsFlag) {
                                errorBusinessExceptions.add(businessException);
                            }
                        } else {
                            errorBusinessExceptions.add(businessException);
                        }
                    } else if (cause instanceof RuntimeException) {
                        logger.error("batch submit nisbillhead error:" + cause.getMessage());
                        cause.printStackTrace();
                    } else {
                        e.printStackTrace();
                    }
                }
            }
            if (errorCount > 0) {
                //调用service处理国际化
                StringBuilder stringBuilder = new StringBuilder();
                for (BusinessException be : errorBusinessExceptions) {
                    String enumName = be.getIMessageEnum().getEnum().name();
                    enumName = enumName.replace("_", ".").toLowerCase();
                    logger.error("异常enumName【{}】", enumName);
                    String lang = multiLangServiceI.getLang(enumName, be.getParams());
                    if (StringUtils.isNotBlank(stringBuilder.toString())) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("[").append(lang).append("]");
                }
                String[] params = {Integer.toString(successCount), Integer.toString(errorCount), stringBuilder.toString()};
                if (!limitHitDetails.isEmpty()) {
                    throw new PaymentLimitBatchExceededException(params, limitHitDetails);
                }
                throw new BusinessException(WorkFlowMessageEnum.WORK_FLOW_BATCH_SUBMIT_NOTICE, params);
            }
        }
        return success();
    }

    /**
     * 付款通知表拆分页面跳转
     *
     * @return
     */
    @ApiOperation("付款通知表拆分页面跳转")
    @GetMapping("/split")
    public AjaxJson<NisBillheadUpdateVO> split(String id) {
        NisBillheadVO  nisBillhead;
        String voucherTypeId="";
        String corpId="";
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        if (StringUtils.isNotEmpty(id)) {
            nisBillhead = nisBillheadService.getByIdUnion(id);
            //暂存和打回状态才可以进行拆分
            if (nisBillhead != null){
                voucherTypeId=nisBillhead.getVoucherTypeId();
                corpId=nisBillhead.getCorpId();
                if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus())) {
                    throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
                }
            }
            model.setNisBillheadPage(nisBillhead);
            //初始化参数
            this.initConfig(model, 0);
            //单位是否上报计划
            if(StringUtils.isNotEmpty(corpId)){
                boolean reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(corpId);
                model.setReportBudget(reportBudget ? CommonConstant.YES : CommonConstant.NO);
            }else {
                model.setReportBudget( CommonConstant.NO);
            }
        }
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        model.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        if(StringUtils.isNotEmpty(voucherTypeId)){
            FcVoucherType fcVoucherType = fcVoucherTypeService.get(voucherTypeId, true);
            if (fcVoucherType != null) {
                model.setIsBudget(fcVoucherType.getIsBudget());
                model.setSettlType(fcVoucherType.getSettlType());
            }
        }
        return AjaxJson.success(model);
    }

    /**
     * 拆分保存
     *
     * @param nisBillhead
     * @return
     */
    @ApiOperation("拆分保存")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitSave")
    public AjaxJson splitSave(@RequestBody  NisBillheadSplitDTO nisBillhead) {
        NisBillhead t = nisBillheadService.get(nisBillhead.getId(), true);
        //暂存和打回状态才可以进行拆分
        if (t != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(t.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(t.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, t.getBillNo());
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal payMoney : nisBillhead.getPayMoneys()) {
            total = total.add(payMoney);
        }
        /*FcVoucherType voucherType = fcVoucherTypeService.get(t.getVoucherTypeId());
        //系统结算方式为50-应收票据，不可进行拆分
		if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())) {
			throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILL_SPLIT);
		}*/
        //单据来源为承兑汇票，不可进行拆分
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_3.equals(t.getBillFrom())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILlFROM_BILL_SPLIT);
        }
        //拆分金额总和必须等于拆分前的付款金额
        if (total.compareTo(t.getPayMoney()) != 0) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_VALID);
        }
        nisBillheadService.split(t, nisBillhead.getPayMoneys(), nisBillhead.getBudgets(), this.getCurrentUser());
        return success();
    }

    /**
     * 撤销拆分
     *
     * @param id
     * @return
     */

    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitCancel/{id}")
    @ApiOperation("修改付款通知表")
    public AjaxJson splitCancel(@PathVariable("id") String id) {
        nisBillheadService.splitCancel(id, this.getCurrentUser());
        return success();
    }

    /**
     * 合并
     *
     * @param ids
     * @return
     */

    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/merge")
    @ApiOperation("合并")
    public AjaxJson merge(@RequestParam("ids") String ids) {
        nisBillheadService.merge(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 撤销合并
     *
     * @param ids
     * @return
     */
    @ApiOperation("撤销合并")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/mergeCancel")
    public AjaxJson mergeCancel(@RequestParam("ids") String ids) {
        nisBillheadService.mergeCancel(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 付款查询 页面跳转
     *
     * @return
     */
    @ApiOperation("付款查询 页面跳转")
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public AjaxJson<Map<String, Object>> query() {
        Map<String, Object> model = new HashMap<>();
        model.put("startWishPayday", DateUtils.getDate());
        model.put("endWishPayday", DateUtils.getDate());
        model.put("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        model.put("opType", "query");
        model.put("showIsAutoPay", DateUtils.addMonth(DateUtils.getDate(), 1));
        model.put("loginName", this.getCurrentUser().getLoginName());
        return AjaxJson.success(model);
    }

    /**
     * 作废付款单查询 页面跳转
     *
     * @return
     */
    @ApiOperation("作废付款单查询 页面跳转")
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryDel")
    public AjaxJson<Map<String, Object>> queryDel() {
        Map<String, Object> model = new HashMap<>();
        model.put("endWishPayday", DateUtils.getDate());
        model.put("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        model.put("opType", "queryDel");
        model.put("defStatus", com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_DEL);
        return AjaxJson.success(model);
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @param voucherTypeId
     * @return
     */
    @ApiOperation("获取付款账号")
    @PostMapping("/getPayerAcc")
    public AjaxJson<List<BtBankAccDTO>> getPayerAcc(String corpId, String voucherTypeId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(voucherTypeId.trim()) && !StringUtils.isEmpty(corpId.trim())) {
            FcVoucherType fcVoucherType = fcVoucherTypeService.get(voucherTypeId, true);
            //获取单位信息
            CorpDTO corp = corpService.get(corpId);
            String accountCorpId = StringUtils.isBlank(corp.getUserAccountCode()) ? corp.getId() : corp.getUserAccountCode();
            //获取人民币
            BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();
            //判断是否记账 0，否 （获取转账权限的在线账号） 1，是（获取转账权限的内部账号）
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.ISACCOUNT_0.equals(fcVoucherType.getIsAccount())) {
                //获取转账权限的银行账号
                List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), accountCorpId, null, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
                List<BtBankAccDTO> listBankAcc = new ArrayList<>();
                if (!EmptyUtils.isEmptyList(listBankAccTemp)) {
                    for (BtBankAccDTO dto : listBankAccTemp) {
                        //账号属性对外付款不为收款的账号才能对外付款
                        if ("2".equals(dto.getForeignDes()) || "3".equals(dto.getForeignDes())) {
                            dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
                            listBankAcc.add(dto);
                        }
                    }
                }
                //付款方式为应付票据、应收票据时付款账号必须为人民币的票据账户
                if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())
                        || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_50.equals(fcVoucherType.getSysVoucherCode())) {
                    if (!EmptyUtils.isEmptyList(listBankAcc)) {
                        for (BtBankAccDTO dto : listBankAcc) {
                            if (BtBankAccConstant.ELECTRICBILL_YES.equals(dto.getElectricBill())) {
                                if (!EmptyUtils.isEmpty(rmbCurrency) && dto.getCurId().equals(rmbCurrency.getId())) {
                                    dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
                                    listAcc.add(dto);
                                }
                            }
                        }
                    }
                } else if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_02.equals(fcVoucherType.getSysVoucherCode())
                        || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_33.equals(fcVoucherType.getSysVoucherCode())
                        || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_35.equals(fcVoucherType.getSysVoucherCode())
                        || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_34.equals(fcVoucherType.getSysVoucherCode())) {
                    // 付款方式为外付网银时付款账号必须账户类别为活期
                    if (!EmptyUtils.isEmptyList(listBankAcc)) {
                        for (BtBankAccDTO dto : listBankAcc) {
                            if (!BtBankAccConstant.ACCTYPE_02.equals(dto.getAccType())
                                    && !BtBankAccConstant.ACCTYPE_06.equals(dto.getAccType())) {
                                dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
                                listAcc.add(dto);
                            }
                        }
                    }
                    // T24090014835 付款办理-付款方式优化
                    if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_34.equals(fcVoucherType.getVoucherType())) {
                        // 付款方式=银企直联（电汇）只能选到银企账户（在线）
                        listAcc = listAcc.stream().filter(item -> StringUtils.isNotEmpty(item.getIsOnline()) && CommonConstant.YES.equals(item.getIsOnline())).collect(Collectors.toList());
                    }
                } else if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_07.equals(fcVoucherType.getSysVoucherCode())) {
                    // 付款方式为RPA付款时付款账号的RPA功能必须勾选支付功能
                    if (!EmptyUtils.isEmptyList(listBankAcc)) {
                        for (BtBankAccDTO dto : listBankAcc) {
                            if (StringUtils.isNotBlank(dto.getRpaFunction()) && dto.getRpaFunction().indexOf("1") != -1) {
                                listAcc.add(dto);
                            }
                        }
                    }
                } else {
                    if (!EmptyUtils.isEmptyList(listBankAcc)) {
                        listAcc.addAll(listBankAcc);
                    }
                }
            } else {
                List<FcAccDTO> listFcAcc = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), accountCorpId, null, null, "1", AccountGrantConstant.TRANSFER_ACCOUNT);
                if (!EmptyUtils.isEmptyList(listFcAcc)) {
                    for (FcAccDTO fcAcc : listFcAcc) {
                        //过滤企业活期账号
                        if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAcc.getAccType())) {
                            BtBankAccDTO bankAcc = new BtBankAccDTO();
                            bankAcc.setId(fcAcc.getId());
                            bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                            bankAcc.setAccName(fcAcc.getAccName());
                            bankAcc.setCurId(fcAcc.getCurId());
                            bankAcc.setCurName(fcAcc.getCurName());
                            bankAcc.setBankCode(fcAcc.getBankCode());
                            bankAcc.setBankName(fcAcc.getNetName());
                            //  bankAcc.setBalance(fcAcc.getFcBalance());
                            FcOnlineLedgerVO fcOnlineLedger = fcOnlineLedgerService.getEntityByAccount(fcAcc.getBlnctrAcc());
                            if (null != fcOnlineLedger) {
                                bankAcc.setBalance(fcOnlineLedger.getCheckBalance().subtract(fcOnlineLedger.getFreezeMoney()));
                            }
                            listAcc.add(bankAcc);
                        }
                    }
                }
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @param curId
     * @param voucherTypeId
     * @return
     */
    @ApiOperation("获取付款账号")
    @PostMapping("/getPayeeAcc")
    public AjaxJson<List<BtBankAccDTO>> getPayeeAcc(String corpId, String curId, String voucherTypeId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(voucherTypeId) && !StringUtils.isEmpty(corpId)) {
            FcVoucherType fcVoucherType = fcVoucherTypeService.get(voucherTypeId, true);
            //内转
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_05.equals(fcVoucherType.getSysVoucherCode())) {
                CorpDTO corp = corpService.get(corpId);
                List<FcAccDTO> listFcAcc = innerAccountService.listAccountByParams(curId, corp.getNetId(), null, null);
                if (!EmptyUtils.isEmptyList(listFcAcc)) {
                    for (FcAccDTO fcAcc : listFcAcc) {
                        //过滤企业活期账号
                        if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAcc.getAccType())) {
                            BtBankAccDTO bankAcc = new BtBankAccDTO();
                            bankAcc.setId(fcAcc.getId());
                            bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                            bankAcc.setAccName(fcAcc.getAccName());
                            bankAcc.setCurId(fcAcc.getCurId());
                            bankAcc.setCurName(fcAcc.getCurName());
                            bankAcc.setBankName(fcAcc.getNetName());
                            bankAcc.setBalance(fcAcc.getFcBalance());
                            listAcc.add(bankAcc);
                        }
                    }
                }
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 获取资金下拨账号（企业活期对应的银行账号）
     *
     * @param corpId
     * @param curId
     * @param account
     * @return
     */
    @ApiOperation("获取资金下拨账号（企业活期对应的银行账号）")
    @PostMapping("/getDownAcc")
    public AjaxJson<List<BtBankAccDTO>> getDownAcc(String corpId, String curId, String account) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(curId) && !StringUtils.isEmpty(account)) {
            //获取单位信息
            CorpDTO corp = corpService.get(corpId);
            String userAccountCode = StringUtils.isBlank(corp.getUserAccountCode()) ? corp.getId() : corp.getUserAccountCode();
            String[] userAccountCodes = userAccountCode.split(CommonConstant.COMMA_SPLIT);
            for (String accountCorpId : userAccountCodes) {
                //获取具有转账权限的银行账号
                List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(accountCorpId, AccountGrantConstant.TRANSFER_ACCOUNT);
                if (!EmptyUtils.isEmptyList(listBankAcc)) {
                    for (BtBankAccDTO bankAcc : listBankAcc) {
                        //下拨账号不限制企业活期账户关系
                        //if (curId.equals(bankAcc.getCurId()) && account.equals(bankAcc.getCounterAccId())) {
                        listAcc.add(bankAcc);
                        //}
                    }
                }
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 结算中心实际付款账号
     *
     * @param corpId
     * @return
     */
    @ApiOperation("结算中心实际付款账号")
    @PostMapping("/getRealPayerAcc")
    public AjaxJson<List<BtBankAccDTO>> getRealPayerAcc(String corpId,String voucherTypeId,String payerAcc) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        if (!StringUtils.isEmpty(voucherTypeId.trim()) && !StringUtils.isEmpty(corpId.trim())) {
            FcVoucherType fcVoucherType = fcVoucherTypeService.get(voucherTypeId, true);
            //判断是否记账 0，否 （获取转账权限的在线账号） 1，是（获取转账权限的内部账号）
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.ISACCOUNT_0.equals(fcVoucherType.getIsAccount()) && StringUtils.isNotBlank(payerAcc)) {
                //不记账的情况下根据付款账号对应的结算中心，取该结算中心下的银行账号
                FcAccDTO fcAcc = iFcAccService.getAccDTOByAccount(payerAcc);
                //获取转账权限的银行账号
                List<BtBankAccDTO> btBankAccDTOS = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), fcAcc.getNetId(), null, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
                if (!EmptyUtils.isEmptyList(btBankAccDTOS)) {
                    for (BtBankAccDTO bankAcc : btBankAccDTOS) {
                        //活期或者票据活期
                        if (BtBankAccConstant.ACCTYPE_01.equals(bankAcc.getAccType()) ||
                                BtBankAccConstant.ACCTYPE_10.equals(bankAcc.getAccType())) {
                            listAcc.add(bankAcc);
                        }
                    }
                }
            }else if (com.bytter.biz.bdlink.api.constant.NetbankConstant.ISACCOUNT_1.equals(fcVoucherType.getIsAccount())){
                //获取单位信息
                CorpDTO corp = corpService.get(corpId);
                String netId = corp.getNetId();
                if (EmptyUtils.isNotEmpty(netId)) {
                    //获取具有转账权限的银行账号
                    List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(netId, AccountGrantConstant.TRANSFER_ACCOUNT);
                    if (!EmptyUtils.isEmptyList(listBankAcc)) {
                        for (BtBankAccDTO bankAcc : listBankAcc) {
                            //活期或者。票据活期
                            if (BtBankAccConstant.ACCTYPE_01.equals(bankAcc.getAccType()) ||
                                    BtBankAccConstant.ACCTYPE_10.equals(bankAcc.getAccType())) {
                                //必须要绑定了内部账号的
                                String account = btBankAccService.getBlnctrAcc(bankAcc.getBankAcc(), bankAcc.getCurId());
                                if (EmptyUtils.isNotEmpty(account)) {
                                    listAcc.add(bankAcc);
                                }
                            }
                        }
                    }
                }
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 获取资金下拨账号（企业活期对应的银行账号）
     *
     * @param corpId
     * @param curId
     * @param account
     * @return
     */
    @ApiOperation("获取资金下拨账号（企业活期对应的银行账号）")
    @PostMapping("/getDownAccByExecute")
    public AjaxJson<List<BtBankAccDTO>> getDownAccByExecute(String corpId, String curId, String account, String sysVoucherCode) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (NetbankConstant.PAY_TYPE_33.equalsIgnoreCase(sysVoucherCode)
                || NetbankConstant.PAY_TYPE_35.equalsIgnoreCase(sysVoucherCode)) {
            if (!StringUtils.isEmpty(curId)) {
                //解除代付账户父子关系控制，可选该成员单位内部账号所属结算中心的任意结算中心银行账号进行代付
                CorpDTO corp = corpService.get(corpId);
                //获取具有转账权限的银行账号
                List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(corp.getNetId(), AccountGrantConstant.TRANSFER_ACCOUNT);
                if (!EmptyUtils.isEmptyList(listBankAcc)) {
                    for (BtBankAccDTO bankAcc : listBankAcc) {
                        if (curId.equals(bankAcc.getCurId())) {
                            listAcc.add(bankAcc);
                        }
                    }
                }
            }
        } else {
            //内部账户关系控制
            String isControlAccountRelation = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ISCONTROLACCOUNTRELATION).getValue();
            if (!StringUtils.isEmpty(curId) && !StringUtils.isEmpty(account)) {
                //获取具有转账权限的银行账号
                List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
                if (!EmptyUtils.isEmptyList(listBankAcc)) {
                    for (BtBankAccDTO bankAcc : listBankAcc) {
                        //根据参数来判断，是否控制下拨账号是否需要限制企业活期账户关系
                        if (CommonConstant.YES.equals(isControlAccountRelation)) {
                            if (!curId.equals(bankAcc.getCurId()) || !account.equals(bankAcc.getCounterAccId())) {
                                continue;
                            }
                        }
                        //银行账号对应的父账号
                        BtBankAccDTO prentBankAcc = null;
                        if (StringUtils.isNotBlank(bankAcc.getPrentAccId())) {
                            prentBankAcc = this.btBankAccService.getBankAccById(bankAcc.getPrentAccId());
                        }
                        if (!EmptyUtils.isEmpty(prentBankAcc)) {
                            bankAcc.setPrentBankAcc(prentBankAcc.getBankAcc());
                            bankAcc.setPrentAccName(prentBankAcc.getAccName());
                        }
                        listAcc.add(bankAcc);
                    }
                }
            }
        }
        return AjaxJson.success(listAcc);
    }


    /**
     * 付款执行指令发送
     *
     * @return
     */
    @ApiOperation("付款执行指令发送")
    @GetMapping("/send")
    public AjaxJson<NisBillheadUpdateVO> send(NisBillheadVO nisBillhead) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        if (StringUtils.isNotEmpty(nisBillhead.getId())) {
            nisBillhead = nisBillheadService.getByIdUnion(nisBillhead.getId());
            model.setNisBillheadPage(nisBillhead);
            //默认允许修改
            model.setAllowEditDownacc("1");
            //转付
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_02.equals(nisBillhead.getSysVoucherCode())) {
                //获取下拨账号
                BtBankAccDTO prentAcc = btBankAccService.getParentAccByAccId(nisBillhead.getDownAccId(), nisBillhead.getCurId());
                model.setPrentAcc(prentAcc);
                model.setAllowEditDownacc(configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWEDITDOWNACC).getValue());
            }
            //初始化参数
            this.initConfig(model, 0);
            String bankAcc = "";
            String accName = "";
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_02.equals(nisBillhead.getSysVoucherCode())) {
                bankAcc = nisBillhead.getDownAcc();
                accName = nisBillhead.getDownAccName();
            } else if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_33.equals(nisBillhead.getSysVoucherCode())) {
                bankAcc = nisBillhead.getRealPayerAcc();
                accName = nisBillhead.getRealPayerAccAccName();
            }
            model.setBankAcc(bankAcc);
            model.setAccName(accName);
        }
        return AjaxJson.success(model);
    }

    /**
     * 付款执行指令发送保存
     * 【付款执行】
     *
     * @param nisBillhead
     * @return
     */
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/sendSave")
    @ApiOperation("付款执行指令发送保存")
    public AjaxJson sendSave(NisBillhead nisBillhead) {
        NisBillhead t = this.nisBillheadService.get(nisBillhead.getId(), true);
        //付款执行指令发送保存，必须是审批通过状态的付款单
        if (t != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK.equals(t.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, t.getBillNo());
        }
        nisBillheadService.sendNisBillhead(nisBillhead, this.getCurrentUser());
        return success();
    }

    /**
     * 打回付款通知表(付款执行打回)
     *
     * @param id
     * @return
     */
    @ApiOperation("修改付款通知表")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back/{id}")
    public AjaxJson back(@PathVariable("id") String id) {
        NisBillhead nisBillhead = this.nisBillheadService.get(id, true);
        //付款执行打回，必须是审批通过状态的付款单
        if (nisBillhead != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK.equals(nisBillhead.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
        }
        nisBillheadService.backNisBillhead(id, this.getCurrentUser(), com.bytter.biz.bdlink.api.constant.NetbankConstant.BACK_TYPE_2);
        return success();
    }

    /**
     * 批量打回付款通知表(付款执行打回)
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量打回付款通知表(付款执行打回)")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchBack")
    public AjaxJson batchBack(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillhead nisBillhead = this.nisBillheadService.get(id, true);
            //付款执行打回，必须是审批通过状态的付款单
            if (nisBillhead != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK.equals(nisBillhead.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
            }
            nisBillheadService.backNisBillhead(id, this.getCurrentUser(), com.bytter.biz.bdlink.api.constant.NetbankConstant.BACK_TYPE_3);
        }
        return success();
    }

    /**
     * 批量发送(付款执行批量发送)
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量发送(付款执行批量发送)")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSend")
    public AjaxJson batchSend(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillhead nisBillhead = this.nisBillheadService.get(id, true);
            //付款执行发送，必须是审批通过状态的付款单
            if (nisBillhead != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK.equals(nisBillhead.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
            }
            nisBillhead.setAccId(nisBillhead.getDownAccId());
            this.nisBillheadService.sendNisBillhead(nisBillhead, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 获取银行账号
     *
     * @param corpId
     * @param downAccId
     * @param curId
     * @param account
     * @return
     */
    @ApiOperation("批量发送(获取银行账号)")
    @PostMapping("/getBankAcc")
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId, String downAccId, String curId, String account) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(downAccId)) {
            //转付获取企业活期对应的银行账号
            listAcc.addAll(this.getDownAcc(corpId, curId, account).getObj());
        } else {
            //代付，获取内部账号对应的具有转账权限的银行账号
            List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                //if (bankAcc.getCurId().equals(curId) && account.equals(bankAcc.getCounterAccId())) {
                //}
                listAcc.addAll(listBankAcc);
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @ApiOperation("跳转至审核列表")
    @GetMapping("/listApprove")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String, Object> model = new HashMap();
        model.put("flowStatus", flowStatus);
        model.put("cycleTime", cycleTime);
        model.put("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        return AjaxJson.success(model);
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    public DataTablesOutput<WorkFlowPersistence> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return this.nisBillheadService.dataTableApprove(input);
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @return String
     */
    @ApiOperation("加载审核列表数据")
    @GetMapping("/approve")
    public AjaxJson<NisBillheadUpdateVO> approve(String id, String taskId) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        if (StringUtils.isNotEmpty(id)) {
            NisBillheadVO nisBillheadVO = this.nisBillheadService.getByIdUnion(id);
            model.setNisBillheadPage(nisBillheadVO);
            model.setTaskId(taskId);
            //初始化参数
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillheadVO.getBillFrom())) {
                this.initConfig(model, 1);
            } else {
                this.initConfig(model, 0);
            }
            //对私付款单获取对私付款明细
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.FORINDIVIDUAL_1.equals(nisBillheadVO.getIsForindividual())) {
                List<NisExpenseDetailVO> listNisExpenseDetail = nisExpenseDetailService.listByBillId(nisBillheadVO.getId());
                model.setListNisExpenseDetail(listNisExpenseDetail);
            }
            //合并明细
            List<NisBillheadVO> listMerge = nisBillheadService.listByMergeId(id);
            String isMerge = "0";
            if (!EmptyUtils.isEmptyList(listMerge)) {
                isMerge = "1";
            }
            model.setIsMerge(isMerge);
            model.setListMerge(listMerge);
            //资金计划
            model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
            model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
            model.setIsBudget(fcVoucherTypeService.get(nisBillheadVO.getVoucherTypeId(), true).getIsBudget());
            model.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        }
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @return String
     */
    @ApiOperation("跳转至手机审核详情界面")
    @GetMapping("/mobileApprove")
    public AjaxJson<NisBillheadUpdateVO> mobileApprove(String businessId, String taskId) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        if (StringUtils.isNotEmpty(businessId)) {
            NisBillheadVO nisBillheadVO = this.nisBillheadService.getByIdUnion(businessId);
            model.setNisBillheadPage(nisBillheadVO);
            model.setTaskId(taskId);
            if (ObjectUtils.isNotEmpty(nisBillheadVO)) {
                //初始化参数
                if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillheadVO.getBillFrom())) {
                    this.initConfig(model, 1);
                } else {
                    this.initConfig(model, 0);
                }            //费用报销单获取对私付款明细
                if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillheadVO.getBillFrom())) {
                    List<NisExpenseDetailVO> listNisExpenseDetail = nisExpenseDetailService.listByBillId(nisBillheadVO.getId());
                    model.setListNisExpenseDetail(listNisExpenseDetail);
                }
                model.setIsBudget(fcVoucherTypeService.get(nisBillheadVO.getVoucherTypeId(), true).getIsBudget());
            }

            //资金计划
            model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
            model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
            model.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        }
        return AjaxJson.success(model);
    }

    /**
     * 费用报销处理-批量打回明细
     *
     * @param ids
     * @return
     */
    @ApiOperation("费用报销处理-批量打回明细")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchBackDetail")
    public AjaxJson batchBackDetail(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillhead nisBillhead = this.nisBillheadService.get(id, true);
            //费用报销处理打回明细，必须是打回暂存状态的付款单
            if (nisBillhead != null &&
                    (!com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus()))) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
            }
            nisBillheadService.backDetail(id, this.getCurrentUser(), false);
        }
        return success();
    }

    /**
     * 费用报销处理-打回明细
     *
     * @param id
     * @return
     */
    @ApiOperation("费用报销处理-打回明细")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/backDetail/{id}")
    public AjaxJson backDetail(@PathVariable("id") String id) {
        NisBillhead nisBillhead = this.nisBillheadService.get(id, true);
        //费用报销处理打回明细，必须是打回暂存状态的付款单
        if (nisBillhead != null &&
                (!com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus()))) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
        }
        nisBillheadService.backDetail(id, this.getCurrentUser(), false);
        return success();
    }

    /**
     * 费用报销处理-打回失败明细
     *
     * @param id
     * @return
     */
    @ApiOperation("费用报销处理-打回失败明细")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/backFailDetail/{id}")
    public AjaxJson backFailDetail(@PathVariable("id") String id) {
        NisBillhead nisBillhead = this.nisBillheadService.get(id, true);
        //费用报销处理打回明细，必须是打回暂存状态的付款单
        if (nisBillhead != null &&
                (!com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus()))) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
        }
        nisBillheadService.backDetail(id, this.getCurrentUser(), true);
        return success();
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<NisBillheadVO> list) {
        this.nisBillheadService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 重复付款校验
     *
     * @param corpId   单位
     * @param payMoney 金额
     * @param payeeAcc 收款账号
     * @param id       单据号（编辑时校验需要）
     * @return
     */
    @ApiOperation("重复付款校验")
    @PostMapping("/rePay")
    public AjaxJson<Boolean> rePay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id) {
        return AjaxJson.success(nisBillheadService.isExistRepay(corpId, payMoney, payeeAcc, curId, id, false));
    }

    /**
     * 账号支付限额校验
     *
     * @param id       单条数据id
     * @param curId    币种
     * @param payMoney 付款金额
     * @param payerAcc 账号
     * @param temp     是否暂存数据，1新增数据校验，0，修改数据校验，空，批量提交校验
     * @return
     */
    @ApiOperation("账号支付限额校验")
    @GetMapping("limit")
    public AjaxJson bankAccLimitCheck(String id, String curId, BigDecimal payMoney, String payerAcc, String temp, String wishPayDate) {
        return AjaxJson.success((nisBillheadService.bankAccLimitCheck(id, payerAcc, curId, payMoney, temp, wishPayDate)));
    }

    public BtBankAccDTO getBankAccInfo(String bankAcc) {
        BtBankAccDTO dto = new BtBankAccDTO();
        if (StringUtils.isNotEmpty(bankAcc)) {
            dto.setBankAcc(bankAcc);
            List<BtBankAccDTO> list = btBankAccService.getBankAccList(dto);
            if (EmptyUtils.isEmptyList(list)) {
                return null;
            }
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 单位支付限额校验
     *
     * @param id       单条数据id
     * @param curId    币种
     * @param payMoney 付款金额
     * @param corpId   单位
     * @param temp     是否暂存数据，1新增数据校验，0，修改数据校验，空，批量提交校验
     * @return
     */
    @ApiOperation("单位支付限额校验")
    @GetMapping("limitForCorp")
    public AjaxJson corpLimitCheck(String id, String curId, BigDecimal payMoney, String corpId, String temp, String wishPayDate) {
        return AjaxJson.success(nisBillheadService.corpLimitCheck(id, corpId, curId, payMoney, temp, wishPayDate));
    }

    /**
     * 获取本位币汇率
     *
     * @param corpId 单位
     * @param curId  当前金额币别
     * @param date   时间
     * @return
     */
    @ApiOperation("获取本位币汇率")
    @GetMapping("standardRate")
    public AjaxJson<BigDecimal> standardRate(String corpId, String curId, Date date) {
        CorpDTO corp = corpService.get(corpId);
        if (corp == null) {
            corp = corpService.getCorpIdByName(corpId);
            if (corp == null) {
                BtBankAccDTO bankAccById = btBankAccService.getBankAccById(corpId);
                if (bankAccById != null) {
                    corp = corpService.get(bankAccById.getCorpId());
                } else {
                    return AjaxJson.success(new BigDecimal(1));
                }
            }
        }
        BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(curId, corp.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        if (rate == null) {
            return AjaxJson.success(new BigDecimal(1));
        }
        return AjaxJson.success(rate);
    }

    /**
     * 批量提交时重复付款校验
     *
     * @param ids 单位
     * @return
     */
    @ApiOperation("批量提交时重复付款校验")
    @PostMapping("/batchRePay")
    public AjaxJson batchRePay(String ids) {
        AjaxJson j = new AjaxJson();
        boolean flag = false;
        String msg = "";
        // 校验已提交状态的付款单是否出现重复数据
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillhead entity = this.nisBillheadService.get(id);
            flag = nisBillheadService.isExistRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, false);
            if (flag) {
                msg = "1";
                break;
            }
        }
        // 校验未提交状态的付款单是否出现重复数据
        if (!flag) {
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                NisBillhead entity = this.nisBillheadService.get(id);
                flag = nisBillheadService.isExistRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, true);
                if (flag) {
                    msg = "2";
                    break;
                }
            }
        }
        j.setSuccess(!flag);
        j.setObj(msg);
        return j;
    }

    /**
     * 根据审批列表的付款单corpIds组装单位树
     *
     * @return
     */
    @ApiOperation("根据审批列表的付款单corpIds组装单位树")
    @GetMapping("/treeByCorpIds/{flowStatus}")
    public AjaxJson<Object> treeByCorpIds(@PathVariable("flowStatus") String flowStatus) {
        return AjaxJson.success(this.nisBillheadService.treeByCorpIds(getCurrentUser(), flowStatus));
    }

    /**
     * 是否可以拆分
     *
     * @param id
     * @return  true是 false 否
     */
    @ApiOperation("是否可以拆分")
    @GetMapping(value = "isMergeBill/{id}")
    public AjaxJson<Boolean> isMergeBill(@PathVariable("id") String id) {
        boolean flag = this.nisBillheadService.isMergeBill(id);
        AjaxJson<Boolean> json = new AjaxJson<>();
        json.setObj(flag);
        if (flag) {
            json.setMessageEnum(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_SPLIT);
        }
        json.setSuccess(true);
        return json;
    }

    /**
     * 批量选择付款账号
     *
     * @return
     */
    @ApiOperation("批量选择付款账号")
    @GetMapping("/toSetPayerAcc/{corpId}/{voucherTypeId}")
    public AjaxJson<Map<String, Object>> toSetPayerAcc(@PathVariable("corpId") String corpId, @PathVariable("voucherTypeId") String voucherTypeId) {
        Map<String, Object> model = new HashMap<>();
        FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(voucherTypeId);
        model.put("corpId", corpId);
        model.put("voucherType", voucherType);
        return AjaxJson.success(model);
    }

    /**
     * 批量选择付款账号
     *
     * @return
     */
    @ApiOperation("批量选择付款账号")
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSetPayerAcc")
    public AjaxJson batchSetPayerAcc(@RequestBody  NisBillheadVO nisBillhead) {
        nisBillheadService.batchSetPayerAcc(nisBillhead, this.getCurrentUser());
        return success();
    }

    /**
     * 付款通知表详情页（提供给外模块）
     *
     * @return
     */
    @ApiOperation("付款通知表详情页（提供给外模块）")
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCommon/{id}")
    public AjaxJson<NisBillheadUpdateVO> viewCommon(@PathVariable("id") String id) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        NisBillheadVO nisBillhead = nisBillheadService.getByIdUnion(id);
        model.setNisBillheadPage(nisBillhead);
        //初始化参数
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
            this.initConfig(model, 1);
        } else {
            this.initConfig(model, 0);
        }
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        model.setIsBudget(fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true).getIsBudget());
        model.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        return AjaxJson.success(model);
    }

    /**
     * 付款通知表详情页（提供给外模块）
     *
     * @return
     */
    @ApiOperation(" 付款通知表详情页（提供给外模块）")
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{billNo}")
    public AjaxJson<NisBillheadUpdateVO> viewByBillNo(@PathVariable("billNo") String billNo) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        NisBillheadVO nisBillhead = nisBillheadService.getByBillNo(billNo);
        model.setNisBillheadPage(nisBillhead);
        //初始化参数
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
            this.initConfig(model, 1);
        } else {
            this.initConfig(model, 0);
        }
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        model.setIsBudget(fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true).getIsBudget());
        model.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        return AjaxJson.success(model);
    }

    /**
     * 根据用户流程查询审批付款单据中的账号
     *
     * @return
     */
    @ApiOperation(" 根据用户流程查询审批付款单据中的账号")
    @GetMapping("/json/{flowStatus}")
    public AjaxJson<List<NisBillheadVO>> getPayerAcc(@PathVariable("flowStatus") String flowStatus) {
        ICurrentUser user = this.getCurrentUser();
        List<NisBillheadVO> bankAcc = nisBillheadService.getPayerAcc(user, flowStatus);
        return AjaxJson.success(bankAcc);
    }

    /**
     * 导出全部
     *
     * @return
     */
    @ApiOperation(" 导出全部")
    @PostMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput dataTablesOutput = nisBillheadService.getDataTablesUnion(dataTablesInput, user);
                List<NisBillheadVO> list = dataTablesOutput.getData();
                HashMap<String, NisBillheadVO> curTotalMap = new HashMap<>();
                for (NisBillheadVO vo : list) {
                    NisBillheadVO nisBillhead;
                    if (curTotalMap.containsKey(vo.getCurName())) {
                        nisBillhead = curTotalMap.get(vo.getCurName());
                        nisBillhead.setPayMoney(nisBillhead.getPayMoney().add(vo.getPayMoney()));
                    } else {
                        nisBillhead = new NisBillheadVO();
                        nisBillhead.setPayMoney(vo.getPayMoney());
                    }
                    nisBillhead.setBillNo(vo.getCurName() + "合计");
                    curTotalMap.put(vo.getCurName(), nisBillhead);
                }
                for (String key : curTotalMap.keySet()) {
                    list.add(curTotalMap.get(key));
                }
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "付款查询", 5000, NisBillheadVO.class);
    }

    /**
     * 导出全部
     *
     * @return
     */
    @ApiOperation(" 导出全部")
    @PostMapping("/exportAllDel")
    public void exportAllDel(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                             String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput dataTablesOutput = nisBillheadService.getDataTablesUnion(dataTablesInput, user);
                List<NisBillheadVO> list = dataTablesOutput.getData();
                HashMap<String, NisBillheadVO> curTotalMap = new HashMap<>();
                for (NisBillheadVO vo : list) {
                    NisBillheadVO nisBillhead;
                    if (curTotalMap.containsKey(vo.getCurName())) {
                        nisBillhead = curTotalMap.get(vo.getCurName());
                        nisBillhead.setPayMoney(nisBillhead.getPayMoney().add(vo.getPayMoney()));
                    } else {
                        nisBillhead = new NisBillheadVO();
                        nisBillhead.setPayMoney(vo.getPayMoney());
                    }
                    nisBillhead.setBillNo(vo.getCurName() + "合计");
                    curTotalMap.put(vo.getCurName(), nisBillhead);
                }
                for (String key : curTotalMap.keySet()) {
                    list.add(curTotalMap.get(key));
                }
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "作废付款单查询", 5000, NisBillheadVO.class);
    }

    @ApiOperation(" 根据单位获取有权限的用户")
    @GetMapping("/getUserByCorpId")
    public AjaxJson<List<UserDTO>> getUserByCorpId(@RequestParam(name = "corpId", required = false) String corpId) {
        return AjaxJson.success(nisBillheadService.getUserByCorpId(corpId, this.getCurrentUser()));
    }

    /**
     * 删除校验拆分明细单
     *
     * @param splitId
     * @return
     */
    @ApiOperation(" 删除校验拆分明细单")
    @PostMapping("/checkSplitDtl")
    public AjaxJson checkSplitDtl(String splitId) {
        //获取拆分明细
        List<NisBillhead> list = nisBillheadService.listNisBillheadBySplitId(splitId);
        String result = "";
        //校验拆分明细单状态必须都是暂存或者打回才允许删除
        if (!EmptyUtils.isEmptyList(list)) {
            for (NisBillhead entity : list) {
                if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.toString().equals(entity.getStatus().toString())
                        && !NetbankConstant.STATUE_BACK.toString().equals(entity.getStatus().toString())) {
                    if (StringUtils.isBlank(result)) {
                        result = entity.getBillNo();
                    } else {
                        result = result + CommonConstant.SEMI_SPLIT + entity.getBillNo();
                    }
                }
            }
        }
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(result);
        return j;
    }

    /**
     * 获取在途的金额
     *
     * @return
     */
    @ApiOperation(" 获取在途的金额")
    @PostMapping("/getAccountBal")
    public AjaxJson<BigDecimal> getUseBal(String account, String curId, String isAccount) {
        return AjaxJson.success(nisBillheadService.getAccountBal(account, curId, isAccount));
    }

    /**
     * 获取付款账号
     *
     * @param resource
     * @return
     */
    @ApiOperation(" 获取付款账号")
    @PostMapping("/getPayerAccQuery")
    public AjaxJson<List<BtBankAccDTO>> getPayerAccQuery(String resource) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        //获取转账权限的银行账号
        List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), null, resource, null, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
        for (BtBankAccDTO dto : listBankAccTemp) {
            dto.setIsOnlineStr(dictService.get("sf_yn", dto.getIsOnline()));
            listAcc.add(dto);
        }
        List<FcAccDTO> listFcAcc = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), null, resource, null, "1", AccountGrantConstant.TRANSFER_ACCOUNT);
        if (!EmptyUtils.isEmptyList(listFcAcc)) {
            for (FcAccDTO fcAcc : listFcAcc) {
                //过滤企业活期账号
                if (InnerAccountConstant.FCACC_BUSINESS_ACCOUNT.equals(fcAcc.getAccType())) {
                    BtBankAccDTO bankAcc = new BtBankAccDTO();
                    bankAcc.setId(fcAcc.getId());
                    bankAcc.setBankAcc(fcAcc.getBlnctrAcc());
                    bankAcc.setAccName(fcAcc.getAccName());
                    bankAcc.setCurId(fcAcc.getCurId());
                    bankAcc.setCurName(fcAcc.getCurName());
                    bankAcc.setBankName(fcAcc.getNetName());
                    bankAcc.setBalance(fcAcc.getFcBalance());
                    listAcc.add(bankAcc);
                }
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 批量打印
     *
     * @return
     */
    @ApiOperation(" 批量打印")
    @GetMapping("/batchPrintBill")
    public AjaxJson<NisBillheadUpdateVO> batchPrintBill(String ids) {
        NisBillheadUpdateVO model = new NisBillheadUpdateVO();
        List<NisBillheadVO> list = nisBillheadService.listByIdUnion(ids);
        model.setIds(ids);
        model.setList(list);
        initConfig(model, 1);
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        model.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        return AjaxJson.success(model);
    }

    /**
     * 判断是否是部分成功部分失败
     *
     * @param billId
     * @return
     */
    @ApiOperation(" 判断是否是部分成功部分失败")
    @PostMapping("/validDetail")
    public AjaxJson<String> validDetail(String billId) {
        return AjaxJson.success(nisBillheadService.validDetail(billId));
    }


    /**
     * 账号余额验证
     *
     * @return
     */
    @ApiOperation(" 账号余额验证")
    @PostMapping("checkBal")
    public AjaxJson<String> checkBal(String ids) {
        return AjaxJson.success(nisBillheadService.checkBal(ids));
    }

    /**
     * 校验付款方式为应收票据时，不能选背书申请的票据，需重新选票
     *
     * @param ids 付款单主键id
     * @return 操作结果
     */
    @ApiOperation(" 校验付款方式为应收票据时，不能选背书申请的票据，需重新选票")
    @PostMapping("checkReceiveBill")
    public AjaxJson<String> checkReceiveBill(String ids) {
        return AjaxJson.success(nisBillheadService.checkReceiveBill(ids));
    }


    /**
     * 查询匹配资金计划科目
     *
     * @return
     */
    @ApiOperation("查询匹配资金计划科目")
    @LogMessage(content = "查询匹配资金计划科目", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewNisBillHeadBudgetItem/{id}")
    public AjaxJson<List<FbsBudgetMatchedItemVO>> viewNisBillHeadBudgetItem(@PathVariable("id") String id) {
        List<FbsBudgetMatchedItemVO> resultList = new ArrayList<>();
        NisBillheadVO nisBillhead = nisBillheadService.getByIdUnion(id);
        FbsBudgetProjectDetailDTO fbsBudgetProjectDetailDTO = new FbsBudgetProjectDetailDTO();
        nisBillheadService.setFbsBudgetDetailDTO(nisBillhead, fbsBudgetProjectDetailDTO);
        List<FbsBudgetProcessCommonDTO> itemList = fbsBudgetService.getItemDetail(fbsBudgetProjectDetailDTO);

        String dtlColSel = configService.getConfig(ConfigConstant.FBS, BudgetConstant.FBS_CONFIG_DTLCOLSEL).getValue();
        Map<String, FbsBudgetProcessDTO> processMap = fbsBudgetProcessService.getBudgetProcessByBillNo("pay", "nis", nisBillhead.getBillNo())
                .stream().collect(Collectors.toMap(FbsBudgetProcessDTO::getItemCode, item -> item));
        for (FbsBudgetProcessCommonDTO fbsBudgetProcessCommonDTO : itemList) {
            FbsBudgetMatchedItemVO fbsBudgetMatchedItemVO = new FbsBudgetMatchedItemVO();
            FbsBudgetProcessDTO fbsBudgetProcess = processMap.get(fbsBudgetProcessCommonDTO.getItemCode());
            if (fbsBudgetProcess != null) {
                fbsBudgetProcessCommonDTO.setProcessMoney(fbsBudgetProcess.getProcessMoney());
                fbsBudgetProcessCommonDTO.setProcessCurMoney(fbsBudgetProcess.getProcessCurMoney());
                fbsBudgetProcessCommonDTO.setStatus(String.valueOf(fbsBudgetProcess.getStatus()));
                fbsBudgetProcessCommonDTO.setStatusName(dictService.getByName("fbsBudgetProcess.status", String.valueOf(fbsBudgetProcess.getStatus())));
            }
            fbsBudgetMatchedItemVO.setItemObject(fbsBudgetProcessCommonDTO);
            FbsBudgetProjectDetailDTO itemDetailDTO = new FbsBudgetProjectDetailDTO();
            if (StringUtils.isNotEmpty(dtlColSel)) {
                assembleDetailDTO(nisBillhead, fbsBudgetProcessCommonDTO, itemDetailDTO);
                itemDetailDTO.setDtlColIds(dtlColSel);

                //获取资金明细详情数据
                FbsItemMoneyDetailInfoVO detailInfoVO = new FbsItemMoneyDetailInfoVO();
                DataTablesInput input = new DataTablesInput();
                assembleDetailDataInput(itemDetailDTO, input);
                List<FbsBudgetDetailDTO> detailInfoList = fbsBudgetDetailService.getProjectDetailInfo(input);
                if (ListUtils.isNotNullOrEmpty(detailInfoList)) {
                    List<String> dtlColIdList = itemDetailDTO.getDtlColIds() != null && !itemDetailDTO.getDtlColIds().isEmpty()
                            ? Arrays.asList(itemDetailDTO.getDtlColIds().substring(1, itemDetailDTO.getDtlColIds().length() - 1).split(","))
                            : new ArrayList<>();
                    detailInfoVO.setDtlColSel(dtlColIdList);
                    detailInfoVO.setDetailInfo(detailInfoList);
                } else {
                    detailInfoVO = null;
                }
                fbsBudgetMatchedItemVO.setDetailObject(detailInfoVO);

                //开始组装 匹配明细金额总信息
                FbsItemMoneyInfoVO moneyInfoVO = new FbsItemMoneyInfoVO();
                BigDecimal processMoney = fbsBudgetProcessService.getNisBillBudgetProcessMoneyByItem(nisBillhead.getCorpId(), nisBillhead.getBillNo(),nisBillhead.getWishPayday(),fbsBudgetProcessCommonDTO.getItemCode(), itemDetailDTO);
                BigDecimal totalBMoney = BigDecimal.ZERO;
                if (ListUtils.isNotNullOrEmpty(detailInfoList)) {
                    totalBMoney = detailInfoList.stream()
                            .map(FbsBudgetDetailDTO::getbMoney)
                            .filter(java.util.Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                }
                BigDecimal balance = totalBMoney.subtract(processMoney);
                moneyInfoVO.setDetailTotal(totalBMoney);
                moneyInfoVO.setProcessTotal(processMoney);
                moneyInfoVO.setBalance(balance);
                fbsBudgetMatchedItemVO.setDetailMoney(moneyInfoVO);
            }
            resultList.add(fbsBudgetMatchedItemVO);
        }


        return AjaxJson.success(resultList);
    }


    private void assembleDetailDTO(NisBillheadVO nisBillhead, FbsBudgetProcessCommonDTO fbsBudgetProcessCommonVO, FbsBudgetProjectDetailDTO itemDetailVO) {
        String corpId = nisBillhead.getCorpId();
        String itemCode = fbsBudgetProcessCommonVO.getItemCode();
        Date processDate = nisBillhead.getWishPayday();
        String billNo = nisBillhead.getVoucherNo();
        itemDetailVO.setProcessDate(processDate);
        itemDetailVO.setWishPayday(processDate);
        itemDetailVO.setBillNo(billNo);
        itemDetailVO.setCorpId(corpId);
        itemDetailVO.setCurId(nisBillhead.getCurId());
        itemDetailVO.setPayerAcc(nisBillhead.getPayerAcc());
        itemDetailVO.setPayeeAcc(nisBillhead.getPayeeAcc());
        itemDetailVO.setItemCode(itemCode);
        itemDetailVO.setSettlType("0");
    }


    private void assembleDetailDataInput(FbsBudgetProjectDetailDTO itemDetailDTO, DataTablesInput input) {

        String dtlColIds = itemDetailDTO.getDtlColIds();
        List<String> dtlColIdList = dtlColIds != null && !dtlColIds.isEmpty()
                ? Arrays.asList(dtlColIds.substring(1, dtlColIds.length() - 1).split(","))
                : new ArrayList<>();
        input.addFormData("dtlColIds", dtlColIdList);
        input.addFormData("itemCode", itemDetailDTO.getItemCode());
        input.addFormData("processDate", DateUtils.formatDate(itemDetailDTO.getProcessDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        input.addFormData("corpId", itemDetailDTO.getCorpId());
        input.addFormData("curId", itemDetailDTO.getCurId());
        input.addFormData("payerAcc", itemDetailDTO.getPayerAcc());
        input.addFormData("payeeAcc", itemDetailDTO.getPayeeAcc());
        input.addFormData("settlType", itemDetailDTO.getSettlType());
        input.addFormData("wishPayday", DateUtils.formatDate(itemDetailDTO.getProcessDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
    }
}
