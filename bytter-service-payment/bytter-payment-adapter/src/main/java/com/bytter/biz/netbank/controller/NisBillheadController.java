package com.bytter.biz.netbank.controller;

import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbBillBankAutoDtlDTO;
import com.bytter.bill.api.bill.dto.AbBillPayMiddleDTO;
import com.bytter.bill.api.bill.dto.AbEmitBillDTO;
import com.bytter.bill.api.bill.dto.AbVoucherinfoViceDTO;
import com.bytter.bill.api.bill.service.IAbVoucherinfoViceService;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.dto.BisBankReceiptDtlDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlHisService;
import com.bytter.biz.bdlink.api.service.IBisBankReceiptDtlService;
import com.bytter.biz.bdlink.vo.BisBankReceiptDtlHisVO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.finacial.api.vo.FcOnlineLedgerVO;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.entity.NisBill;
import com.bytter.biz.netbank.entity.NisBillhead;
import com.bytter.biz.netbank.service.*;
import com.bytter.biz.netbank.vo.BisBankDtlBillVO;
import com.bytter.biz.netbank.vo.EBankMoreBillVO;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.biz.netbank.vo.NisBillheadVO;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisBillheadDTO;
import com.bytter.biz.payment.api.service.INisBillheadService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
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
//import io.seata.spring.annotation.GlobalTransactional;
import com.google.common.collect.Lists;
import net.sf.jsqlparser.util.validation.metadata.NamedObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 付款通知表
 * @date 2017-05-23 15:20:33
 */
@Controller
@RequestMapping("/netbank/nisbillhead")
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

    @DubboReference(check = false)
    private IBtCurrencyService iBtCurrencyService;

    @DubboReference(check = false)
    private IConfigService iConfigService;

//    @Autowired
//    private INisBillheadTCCService inisBillheadTCCService;

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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY)
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("endWishPayday", DateUtils.addMonth(DateUtils.getDate(), 1));
        model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        //来源于ERP的付款单，付款账号是否允许编辑
        model.addAttribute("editPayerAcc", configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue());
//        String chargeBackType = "";
//        BtChargeBackType btChargeBackType = btChargeBackTypeService.findByDef();
//        if (StringUtils.isNotEmpty(btChargeBackType)) {
//            chargeBackType = btChargeBackType.getBusType();
//        }
//        model.addAttribute("chargeBackType", chargeBackType);
        //初始化参数
        this.initConfig(model, 0);
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //是否启用待付款池	  1-是 0-否
        model.addAttribute("enablePendingPaymentPool", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ENABLEPENDINGPAYMENTPOOL).getValue());
        //未产生资金需求付款单付款控制 0-不提醒，1-只提醒不控制，2-控制
        model.addAttribute("demandControl", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_DEMANDCONTROL).getValue());
        return "netbank/listNisBillhead";
    }

    /**
     * 开票登记编辑页面跳转
     *
     * @return
     */
    @GetMapping("/updateBillInfo")
    public String updateBillInfo(AbEmitBillDTO abEmitBillDTO, Model model) {
        Map map = accebillService.dealBillInfo(abEmitBillDTO.getAid());
        model.addAllAttributes(map);
        model.addAttribute("corpId", abEmitBillDTO.getCorpId() == null ? "" : abEmitBillDTO.getCorpId());
        model.addAttribute("payMoney", abEmitBillDTO.getBillMoney() == null ? "" : abEmitBillDTO.getBillMoney());
        model.addAttribute("rmk", abEmitBillDTO.getRmk() == null ? "" : abEmitBillDTO.getRmk());
        model.addAttribute("payeracc", abEmitBillDTO.getPayeracc() == null ? "" : abEmitBillDTO.getPayeracc());
        model.addAttribute("payeeacc", abEmitBillDTO.getPayeeacc() == null ? "" : abEmitBillDTO.getPayeeacc());
        model.addAttribute("payeeAccName", abEmitBillDTO.getPayeeAccName() == null ? "" : abEmitBillDTO.getPayeeAccName());
        model.addAttribute("payeebankno", abEmitBillDTO.getPayeebankno() == null ? "" : abEmitBillDTO.getPayeebankno());
        model.addAttribute("payeeBankName", abEmitBillDTO.getPayeeBankName() == null ? "" : abEmitBillDTO.getPayeeBankName());
        model.addAttribute("blRcvpymtpsUnnIdNo", (abEmitBillDTO.getBlRcvpymtpsUnnIdNo() == null || "undefined".equals(abEmitBillDTO.getBlRcvpymtpsUnnIdNo().toString())) ? "" : abEmitBillDTO.getBlRcvpymtpsUnnIdNo());
        model.addAttribute("isPayFlg", "1");//是否是付款办理弹窗开票
        if (map.get("abEmitBillVo") != null
                && map.get("abEmitBillVo") instanceof Map) {
            ((Map) map.get("abEmitBillVo")).put("voucherId", abEmitBillDTO.getVoucherId());
        }
        FcVoucherType fcVoucherType = fcVoucherTypeService.get(abEmitBillDTO.getVoucherTypeId(), true);
        if (NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())) {
            if (StringUtils.isNotBlank(fcVoucherType.getBillSectionControl())) {
                if (NetbankConstant.BILL_SECTION_THREE.equals(fcVoucherType.getBillSectionControl())) {
                    model.addAttribute("billSectionThreeButton", 1);
                } else {
                    model.addAttribute("billSectionSixButton", 1);
                }
            } else {
                model.addAttribute("billSectionThreeButton", 1);
                model.addAttribute("billSectionSixButton", 1);
            }
        }
        return "bill/accebill/updateAbEmitBill";
    }

    /**
     * 票据开票 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY)
    @PostMapping("/billListGetInfo")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY)
    @LogMessage(content = "票据开票列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/billList")
    public String billList(AbEmitBillDTO abEmitBillDTO, Model model) {
        String appNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_APPNO);
        model.addAttribute("appNo", appNo);
        model.addAttribute("rmk", abEmitBillDTO.getRmk());
        if (AccebillConstant.BILL_ISBATCHBILL.equals(abEmitBillDTO.getIsBatchBill())) {
            String batchNo = SequenceSingleton.getSequence(SequenceEnum.BILL_ABEMITBILL_BATCHNO);
            model.addAttribute("batchNo", batchNo);
            model.addAttribute("isBatchBill", AccebillConstant.BILL_ISBATCHBILL);
        }
        // 挂预算系统参数
        if (iAbVoucherinfoViceService.isBudget(AccebillConstant.BRIEF_STATUS_001)) {
            model.addAttribute("payItem", iAbVoucherinfoViceService.getBudgetItem(AccebillConstant.BRIEF_STATUS_001));
        }
        // 挂授信
        boolean isRelateRat = iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISCREDIT).toBoolean(false);
        // 挂内部授信
        boolean isOpenCpRat = iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISOPENCPRAT).toBoolean(false);
        model.addAttribute("isRelateRat", isRelateRat);
//		if (isRelateRat || isOpenCpRat) {
        BtCurrencyDTO rmbCurrency = iBtCurrencyService.getRMBCurrency();
        model.addAttribute("curId", rmbCurrency == null ? null : rmbCurrency.getId());
//		}
        model.addAttribute("externalCorpNum", iConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).toInteger(0));
        model.addAttribute("isEmitMake", iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISEMITMAKE).toBoolean(true));
        //是否挂保证金
        model.addAttribute("isBail", iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISBAIL).toBoolean(true));
        //挂保证金银行类型
        model.addAttribute("bailBankControlType", iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_BAILBANKCONTROLTYPE).getValue());
        //是否启用授信敞口
        model.addAttribute("mouthRat", iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISOPENRAT).toInteger(0) >= 1 ? true : false);
        //是否启用担保参数
        model.addAttribute("isGuarantee", iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_ISBEAR).toInteger(0));
        model.addAttribute("isHangGuarantee", AccebillConstant.AB_BILL_VALID_SIGN_1);
        //获取纸票票据金额最大额度
        model.addAttribute("maxBillMoney", iConfigService.getConfig(AccebillConstant.AB_CONFIG_TYPE, AccebillConstant.AB_CONFIG_MAXBILLMONEY));
        //增加是否挂内部授信
        model.addAttribute("isOpenCpRat", isOpenCpRat);
        model.addAttribute("externalCorp", iConfigService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
        model.addAttribute("corpId", abEmitBillDTO.getCorpId() == null ? "" : abEmitBillDTO.getCorpId());
        model.addAttribute("payMoney", abEmitBillDTO.getBillMoney() == null ? "" : abEmitBillDTO.getBillMoney());
        //model.addAttribute("payeracc",abEmitBillDTO.getPayeracc() == null ? "" : abEmitBillDTO.getPayeracc());
        model.addAttribute("payeeacc", abEmitBillDTO.getPayeeacc() == null ? "" : abEmitBillDTO.getPayeeacc());
        model.addAttribute("payeeAccName", abEmitBillDTO.getPayeeAccName() == null ? "" : abEmitBillDTO.getPayeeAccName());
        model.addAttribute("payeebankno", abEmitBillDTO.getPayeebankno() == null ? "" : abEmitBillDTO.getPayeebankno());
        model.addAttribute("payeeBankName", abEmitBillDTO.getPayeeBankName() == null ? "" : abEmitBillDTO.getPayeeBankName());
        model.addAttribute("blRcvpymtpsUnnIdNo", (abEmitBillDTO.getBlRcvpymtpsUnnIdNo() == null || "undefined".equals(abEmitBillDTO.getBlRcvpymtpsUnnIdNo().toString())) ? "" : abEmitBillDTO.getBlRcvpymtpsUnnIdNo());
        model.addAttribute("isPayFlg", "1");//是否是付款办理弹窗开票

        model.addAttribute("payeracc", abEmitBillDTO.getPayeracc());
        model.addAttribute("payer", abEmitBillDTO.getPayer());
        model.addAttribute("payerbankno", abEmitBillDTO.getPayerbankno());
        if ("".equals(abEmitBillDTO.getVoucherId())) {
            abEmitBillDTO.setVoucherId(null);
        }
        if (StringUtils.isNotBlank(abEmitBillDTO.getVoucherId())) {
            String[] payIds = abEmitBillDTO.getVoucherId().split(",");
            for (String payId : payIds) {
                NisBillhead nisBillhead = nisBillheadService.get(payId);
                model.addAttribute("corpId", nisBillhead.getCorpId());
                if (StringUtils.isNotBlank(nisBillhead.getPayerAcc())) {
                    abEmitBillDTO.setPayeracc(nisBillhead.getPayerAcc());
                    model.addAttribute("payeracc", nisBillhead.getPayerAcc());
                    model.addAttribute("payer", nisBillhead.getPayerAccName());
                    model.addAttribute("payerBankName", nisBillhead.getPayerBank());
                    break;
                }
            }
        }
        if (StringUtils.isNotBlank(abEmitBillDTO.getPayeracc())) {
            BtBankAccDTO bankAccInfoByBankAcc = btBankAccService.getBankAccInfoByBankAcc(abEmitBillDTO.getPayeracc());
            model.addAttribute("payerbankno", bankAccInfoByBankAcc.getBankCode());
            model.addAttribute("circulationMode", bankAccInfoByBankAcc.getCirculationMode());
            Map<String, Object> searchMap = new HashMap();
            searchMap.put("bankTypeId", bankAccInfoByBankAcc.getBankTypeId());
            List<AbBillBankAutoDtlDTO> dtos = accebillService.listAbBillBankAutoDtlByParams(searchMap);
            if (!CollectionUtils.isEmpty(dtos)) {
                AbBillBankAutoDtlDTO dto = dtos.get(0);
                model.addAttribute("bifCode", dto.getBifCode());
            }
        }
        model.addAttribute("voucherId", abEmitBillDTO.getVoucherId());
        //if(StringUtils.isNotBlank(abEmitBillDTO.getVoucherId())){
        //    List<NisBillheadVO> nisBillheadVOS = nisBillheadService.listByIdUnion(abEmitBillDTO.getVoucherId());
        //    BigDecimal sumMoney = BigDecimal.ZERO;
        //    for(NisBillheadVO nVo : nisBillheadVOS){
        //        sumMoney = sumMoney.add(nVo.getPayMoney());
        //        model.addAttribute("payMoney",sumMoney);
        //    }
        //}
        model.addAttribute("allowDifferentBillMoney", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue());
        model.addAttribute("applyCorpId", model.getAttribute("corpId"));
        if (model.getAttribute("payeracc") != null && StringUtils.isNotBlank(model.getAttribute("payeracc").toString())) {
            BtBankAccDTO payeracc = btBankAccService.getBankAccInfoByBankAcc(model.getAttribute("payeracc").toString());
            if (payeracc != null) {
                model.addAttribute("corpId", payeracc.getCorpId());
            }
        }
        FcVoucherType fcVoucherType = fcVoucherTypeService.get(abEmitBillDTO.getVoucherTypeId(), true);
        if (NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())) {
            if (StringUtils.isNotBlank(fcVoucherType.getBillSectionControl())) {
                if (NetbankConstant.BILL_SECTION_THREE.equals(fcVoucherType.getBillSectionControl())) {
                    model.addAttribute("billSectionThreeButton", 1);
                } else {
                    model.addAttribute("billSectionSixButton", 1);
                }
            } else {
                model.addAttribute("billSectionThreeButton", 1);
                model.addAttribute("billSectionSixButton", 1);
            }
        }
        return "bill/accebill/addAbEmitBill";
    }

    /**
     * 付款通知表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_QUERY,
//            ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_HANDLEQUERY,
//            ResourceConstant.NETBANK_EXPENSE_NISEXPENSEDETAIL_QUERY,
//            ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_EXECUTEQUERY,
//            ResourceConstant.NETBANK_NETBANKQUERY_NISBILLHEAD_QUERY,
//            ResourceConstant.NETBANK_NETBANKQUERYDEL_NISBILLHEAD_QUERY},
//            logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = nisBillheadService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        out = nisBillheadService.setDtlReceipt(out);
        return out;
    }

    /**
     * 付款通知表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_ADD)
    @GetMapping("/add")
    public String add(NisBillhead nisBillhead, Model model) {
        if (StringUtils.isNotEmpty(nisBillhead.getId())) {
            nisBillhead = nisBillheadService.get(nisBillhead.getId());
            model.addAttribute("nisBillheadPage", nisBillhead);
        }
        model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        model.addAttribute("requiredBusiness", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        model.addAttribute("isAccebillIntellect", configService.getConfig(SysConfigConstant.Module.ACCEBILL, "isAccebillIntellect").getValue());
        //初始化参数
        this.initConfig(model, 0);
        return "netbank/addNisBillhead";
    }

    /**
     * 付款通知表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
    @GetMapping("/update")
    public String update(NisBillheadVO nisBillhead, Model model) {
        if (StringUtils.isNotEmpty(nisBillhead.getId())) {
            nisBillhead = nisBillheadService.getByIdUnion(nisBillhead.getId());
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
                model.addAttribute("billBalance", billBalance);
                nisBillhead.setVid(vid);
                nisBillhead.setVvid(vvid);
                nisBillhead.setBillPoolIds(billPoolIds);
                nisBillhead.setBillBalance(billBalance);
            }
            model.addAttribute("nisBillheadPage", nisBillhead);
            model.addAttribute("payerBankAcc", this.btBankAccService.getBankAccByBankAccAndCurId(nisBillhead.getPayerAcc(), nisBillhead.getCurId()));
            //初始化参数
            this.initConfig(model, 0);
        }
        model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //未产生资金需求付款单付款控制 0-不提醒，1-只提醒不控制，2-控制
        model.addAttribute("demandControl", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_DEMANDCONTROL).getValue());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("corp", corpService.get(nisBillhead.getCorpId()));
        model.addAttribute("requiredBusiness", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        //费用报销单编辑跳转
        if (nisBillhead != null && (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.toString()).equals(nisBillhead.getBillFrom())) {
            model.addAttribute("listNisExpenseDetail", nisExpenseDetailService.listByBillId(nisBillhead.getId()));
            //初始化参数
            this.initConfig(model, 1);
            return "expense/collectEditNisExpenseDetail";
        }
        //初始化单据编辑项参数
        this.initEditConfig(model, nisBillhead);
        //获取系统结算方式
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        if (voucherType!=null&&NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && NetbankConstant.ISOPENBILL_0.equals(nisBillhead.getIsOpenBill())) {
            String billIds = nisBillService.getBillIds(nisBillhead.getId());
            if (StringUtils.isNotBlank(billIds)) {
                model.addAttribute("openBillInfo", billIds + ",first");
            }
        }

        return "netbank/updateNisBillhead";
    }

    /**
     * 初始化系统参数
     *
     * @param model
     * @param busType
     */
    private void initConfig(Model model, Integer busType) {
        if (busType == 1) {
            //费用报销处理
            model.addAttribute("budgetSel", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue());
            model.addAttribute("payItem", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_PAYITEM).getValue());
            model.addAttribute("expFbsin", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue());
            model.addAttribute("accountOver", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_ACCOUNTOVER).getValue());
            model.addAttribute("bankaccOver", configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BANKACCOVER).getValue());
            model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        } else {
            model.addAttribute("budgetSel", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue());
            model.addAttribute("payItem", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_PAYITEM).getValue());
            model.addAttribute("accountOver", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ACCOUNTOVER).getValue());
            model.addAttribute("bankaccOver", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BANKACCOVER).getValue());
            model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
            model.addAttribute("externalCorp", configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
            model.addAttribute("allowDifferentBillMoney", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue());
        }
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //付款余额控制方式  0-账户余额 1-可用余额
        model.addAttribute("balControlType", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BALCONTROLTYPE).getValue());
    }

    /**
     * 初始化单据编辑项参数
     *
     * @param model
     * @param nisBillhead
     */
    private void initEditConfig(Model model, NisBillheadVO nisBillhead) {
        //ERP接口传输的单据编辑项控制
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_6.equals(nisBillhead.getBillFrom())) {
            model.addAttribute("editVoucherTypeId", configService.getConfig(ConfigConstant.ERP, "voucherTypeId").getValue());
            model.addAttribute("editPayerAcc", configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue());
            model.addAttribute("editPayMoney", configService.getConfig(ConfigConstant.ERP, "payMoney").getValue());
            model.addAttribute("editPayeeAcc", configService.getConfig(ConfigConstant.ERP, "payeeAcc").getValue());
            model.addAttribute("editPayeeAccName", configService.getConfig(ConfigConstant.ERP, "payeeAccName").getValue());
            model.addAttribute("editPayeeBank", configService.getConfig(ConfigConstant.ERP, "payeeBank").getValue());
            model.addAttribute("editIsForindividual", configService.getConfig(ConfigConstant.ERP, "isForindividual").getValue());
            model.addAttribute("editWishPayday", configService.getConfig(ConfigConstant.ERP, "wishPayday").getValue());
            model.addAttribute("editPurpose", configService.getConfig(ConfigConstant.ERP, "purpose").getValue());
            model.addAttribute("editBillType", configService.getConfig(ConfigConstant.ERP, "billType").getValue());
            model.addAttribute("editBillFlag", configService.getConfig(ConfigConstant.ERP, "billFlag").getValue());
        } else {
            model.addAttribute("editVoucherTypeId", "1");
            model.addAttribute("editPayerAcc", "1");
            model.addAttribute("editPayeeAcc", "1");
            model.addAttribute("editPayeeAccName", "1");
            model.addAttribute("editPayeeBank", "1");
            model.addAttribute("editIsForindividual", "1");
            model.addAttribute("editWishPayday", "1");
            model.addAttribute("editPurpose", "1");
        }
        //控制付款金额不允许编辑，拆分单据/合并单据都不允许编辑金额
        boolean splitIdFlag = !EmptyUtils.isEmpty(nisBillhead) && !EmptyUtils.isBlank(nisBillhead.getSplitId());
        if (splitIdFlag || nisBillheadService.isMergeBill(nisBillhead.getId())) {
            model.addAttribute("editPayMoney", "0");
        } else {
            if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_6.equals(nisBillhead.getBillFrom())) {
                model.addAttribute("editPayMoney", "1");
            }
        }
    }

    /**
     * 电子回单查看
     *
     * @return
     */
    @LogMessage(content = "回单查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewReceipt/{id}")
    public String ViewReceipt(@PathVariable("id") String id, Model model) {
//        model.addAttribute("id", id);
//        return "receipt/bankbill/viewEBankBill";

        BisBankReceiptDtlDTO receiptDtlDTO= bisBankReceiptDtlService.getById(id);
        String saveType=receiptDtlDTO.getSaveType();
        List<String> list = Lists.newArrayList();
        if ("2".equals(saveType) || "3".equals(saveType)||"0".equals(saveType)) {
            // 回单附件保存类型为FTP
            String filePath = receiptDtlDTO.getFilePath();
            if (StringUtils.isNotEmpty(filePath)) {
                List<String> filePathList = Arrays.asList(filePath.split(CommonConstant.COMMA_SPLIT));
                // pkg20230828-B20230800158 工行回单查看报错（V23090021）, 回单路径包括回单名称，部分银行回单名称含有特殊字符，前端传入后端有问题，此处改为传回单路径下标
                for (int i = 0; i < filePathList.size(); i++) {
                    list.add(String.valueOf(i));
                }
            }
        } else {
            // 回单附件保存类型为数据库
            Collections.addAll(list, receiptDtlDTO.getPdfId().split(CommonConstant.COMMA_SPLIT));
        }
        model.addAttribute("id", id);
        model.addAttribute("saveType", saveType);
        model.addAttribute("list", list);
        return "receipt/bankbill/viewEBankBill";
    }

    /**
     * 流水列表查看
     *
     * @return 前端路径
     */
    @LogMessage(content = "流水列表查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewdtls/{id}")
    public String viewdtls(@PathVariable("id") String id, Model model) {
        List<EBankMoreBillVO> eBankMoreBillVOList = nisBillheadService.getNisBillheadDtl(id);
        List list = new ArrayList();
        for (EBankMoreBillVO eBankMoreBillVO : eBankMoreBillVOList) {
            list.add(bisAccDtlService.getById(eBankMoreBillVO.getDtlId()));
        }
        model.addAttribute("list", list);
        return "receipt/bankbill/listBisAccDtl";
    }

    /**
     * 回单列表查看
     *
     * @return
     */
    @LogMessage(content = "回单列表查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewReceipts/{id}")
    public String viewReceipts(@PathVariable("id") String id, Model model) {
        List<EBankMoreBillVO> eBankMoreBillVOList = nisBillheadService.getNisBillheadDtl(id);
        List<BisBankDtlBillVO> bisBankDtlBillVOList = new ArrayList<>();
        for (EBankMoreBillVO eBankMoreBillVO : eBankMoreBillVOList) {
            for (BisBankDtlBillVO bisBankDtlBillVO : nisBillheadService.getDtlreceipt(eBankMoreBillVO.getDtlId())) {
                bisBankDtlBillVOList.add(bisBankDtlBillVO);
            }
        }
        List list = new ArrayList();
        List hislist = new ArrayList();
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
        model.addAttribute("list", list);
        model.addAttribute("hislist", hislist);
        return "receipt/bankbill/listBisAccReceipt";
    }

    /**
     * 历史电子回单查看
     *
     * @return
     */
    @LogMessage(content = "历史回单查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewHisReceipt/{id}")
    public String ViewHisReceipt(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return "receipt/bankbill/viewEBankBillHis";
    }

    /**
     * 列表打印
     *
     * @return
     */
    @GetMapping("/print")
    public String print(String id, Model model) {
        model.addAttribute("id", id);
        this.nisBillheadService.updatePrint(id);
        return "receipt/bankbill/printEBankBill";
    }

    /**
     * 历史电子回单打印
     *
     * @return
     */
    @GetMapping("/printHis")
    public String printHis(String id, Model model) {
        model.addAttribute("id", id);
        this.nisBillheadService.updatePrint(id);
        return "receipt/bankbill/printEBankBillHis";
    }

    /**
     * 批量查看
     *
     * @return
     */
    @GetMapping("/batchPreview")
    public String batchPreview(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "receipt/bankbill/viewEBankBillBatch";
    }

    /**
     * 批量打印
     *
     * @return
     */
    @GetMapping("/batchPrint")
    public String batchPrint(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.nisBillheadService.updatePrint(id);
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "receipt/bankbill/printEBankBillBatch";
    }

    /**
     * 付款通知表详情页
     *
     * @return
     */
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        NisBillheadVO nisBillhead = nisBillheadService.getByIdUnion(id);
        if (nisBillhead == null) {
            return "netbank/viewNisBillhead";
        }
        model.addAttribute("nisBillheadPage", nisBillhead);
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
                model.addAttribute("listNisExpenseDetail", new ArrayList<NisExpenseDetailVO>());
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
            model.addAttribute("listNisExpenseDetail", listNisExpenseDetail);
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
        model.addAttribute("isSplitOrMerge", isSplitOrMerge);
        model.addAttribute("listMerge", listMerge);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        FcVoucherType fcVoucherType= fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        if(fcVoucherType!=null){
            model.addAttribute("isBudget",fcVoucherType.getIsBudget());
        }
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        return "netbank/viewNisBillhead";
    }

    /**
     * 添加付款通知表
     * 【付款办理】
     *
     * @param nisBillhead
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_ADD)
    @LogMessage(content = "添加付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
//    @GlobalTransactional
    public AjaxJson save(NisBillheadVO nisBillhead) {
        String billNo = "";
        if (dataCheck(nisBillhead, false)) {
            try {
                nisBillhead.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD));
                billNo = nisBillheadService.saveNisBillhead(nisBillhead, this.getCurrentUser());

                /*ICurrentUser user = this.getCurrentUser();
                String id = UUID.randomUUID().toString().replaceAll("-", "");
                billNo = inisBillheadTCCService.prepareAuto(id, nisBillhead.getSysVoucherCode(), nisBillhead, user);*/

                //付款预警增加 -->调用fundwarn模块
                nisBillheadService.warnExecuteNis(nisBillhead, this.getCurrentUser()); //TODO
            } catch (BeansException e) {
                //TODO 记录异常
                throw new RuntimeException(e);
            }
        }
        return success(billNo);
    }

    /**
     * 修改付款通知表
     *
     * @param nisBillhead
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(NisBillheadVO nisBillhead) {
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
        // 非应付票据，应收票据才校验付款账号
        if (!(com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()))) {
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
        //收付款账号不能相同
        if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equalsIgnoreCase(nisBillhead.getBillFrom()) && nisBillhead.getPayeeAcc().equals(nisBillhead.getPayerAcc())) {
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
     * 删除付款通知表
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_DELETE)
    @LogMessage(content = "删除付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @DeleteMapping("/delete/{id}/{chargeBackType}")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_DELETE)
    @LogMessage(content = "批量删除付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
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
    @LogMessage(content = "批量暂挂付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/pendingBatch")
    @ResponseBody
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
    @LogMessage(content = "批量解除暂挂付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/relievePendingBatch")
    @ResponseBody
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
    @LogMessage(content = "批量修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSelectAccebill")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_SUBMITBATCH)
    @LogMessage(content = "批量提交付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids, @RequestParam("caCodings") String caCodings, @RequestParam(value = "isBatchNoSubmit", defaultValue = "false") boolean isBatchNoSubmit) {
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
                    || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()))) {
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_MERGEANDSPLIT)
    @GetMapping("/split")
    public String split(NisBillheadVO nisBillhead, Model model) {
        if (StringUtils.isNotEmpty(nisBillhead.getId())) {
            nisBillhead = nisBillheadService.getByIdUnion(nisBillhead.getId());
            //暂存和打回状态才可以进行拆分
            if (nisBillhead != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillhead.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillhead.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillhead.getBillNo());
            }
            model.addAttribute("nisBillheadPage", nisBillhead);
            //初始化参数
            this.initConfig(model, 0);
            //单位是否上报计划
            boolean reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(nisBillhead.getCorpId());
            model.addAttribute("reportBudget", reportBudget ? CommonConstant.YES : CommonConstant.NO);
        }
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        FcVoucherType fcVoucherType = fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true);
        String isBudget = "";
        String settlType = "";
        if (fcVoucherType != null) {
            isBudget = fcVoucherType.getIsBudget();
            settlType = fcVoucherType.getSettlType();
        }
        model.addAttribute("isBudget", isBudget);
        model.addAttribute("settlType", settlType);
        return "netbank/splitNisBillhead";
    }

    /**
     * 拆分保存
     *
     * @param nisBillhead
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_MERGEANDSPLIT)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitSave")
    @ResponseBody
    public AjaxJson splitSave(NisBillhead nisBillhead,
                              @RequestParam("payMoneys[]") List<BigDecimal> payMoneys, @RequestParam("budgets[]") List<String> budgets) {
        NisBillhead t = nisBillheadService.get(nisBillhead.getId(), true);
        //暂存和打回状态才可以进行拆分
        if (t != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(t.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(t.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, t.getBillNo());
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal payMoney : payMoneys) {
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
        nisBillheadService.split(t, payMoneys, budgets, this.getCurrentUser());
        return success();
    }

    /**
     * 撤销拆分
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_MERGEANDSPLIT)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitCancel/{id}")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_MERGEANDSPLIT)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/merge")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_MERGEANDSPLIT)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/mergeCancel")
    @ResponseBody
    public AjaxJson mergeCancel(@RequestParam("ids") String ids) {
        nisBillheadService.mergeCancel(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 付款查询 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_NETBANKQUERY_NISBILLHEAD_QUERY,
//            ResourceConstant.NETBANK_NETBANKQUERY_NISBILLHEAD_BISQUERY},
//            logical = Logical.OR)
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        model.addAttribute("startWishPayday", DateUtils.getDate());
        model.addAttribute("endWishPayday", DateUtils.getDate());
        model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        model.addAttribute("opType", "query");
        model.addAttribute("showIsAutoPay", DateUtils.addMonth(DateUtils.getDate(), 1));
        return "netbank/listNisBillheadQuery";
    }

    /**
     * 作废付款单查询 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANKQUERYDEL_NISBILLHEAD_QUERY)
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/queryDel")
    public String queryDel(Model model) {
        model.addAttribute("endWishPayday", DateUtils.getDate());
        model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        model.addAttribute("opType", "queryDel");
        model.addAttribute("defStatus", com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_DEL);
        return "netbank/listNisBillheadQuery";
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @param voucherTypeId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayerAcc")
    @ResponseBody
    public List<BtBankAccDTO> getPayerAcc(String corpId, String voucherTypeId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(voucherTypeId.trim()) && !StringUtils.isEmpty(corpId.trim())) {
            FcVoucherType fcVoucherType = fcVoucherTypeService.get(voucherTypeId, true);
            //获取单位信息
            CorpDTO corp = corpService.get(corpId);
            String accountCorpId = StringUtils.isBlank(corp.getUserAccountCode()) ? corp.getId() : corp.getUserAccountCode();
            //获取人民币
            BtCurrencyDTO rmbCurrency = btCurrencyService.getRMBCurrency();

            //如果是代付，取获取转账权限的科目为企业活期的内部账号
            if (NetbankConstant.PAY_TYPE_33.equals(fcVoucherType.getSysVoucherCode())) {
                appendTransferableBusinessFcAccountsToList(accountCorpId, listAcc);
            }else {
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
                    appendTransferableBusinessFcAccountsToList(accountCorpId, listAcc);
                }
            }
        }
        return listAcc;
    }

    /**
     * 查询转账权限的企业活期内部账号
     */
    private void appendTransferableBusinessFcAccountsToList(String accountCorpId, List<BtBankAccDTO> listAcc) {
        List<FcAccDTO> listFcAcc = accountGrantService.listDTONotUnionByMultiVal(this.getUserId(), accountCorpId, null, null, "1", AccountGrantConstant.TRANSFER_ACCOUNT);
        if (EmptyUtils.isEmptyList(listFcAcc)) {
            return;
        }
        for (FcAccDTO fcAcc : listFcAcc) {
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

    /**
     * 获取付款账号
     *
     * @param corpId
     * @param curId
     * @param voucherTypeId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayeeAcc")
    @ResponseBody
    public List<BtBankAccDTO> getPayeeAcc(String corpId, String curId, String voucherTypeId) {
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
        return listAcc;
    }

    /**
     * 获取资金下拨账号（企业活期对应的银行账号）
     *
     * @param corpId
     * @param curId
     * @param account
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getDownAcc")
    @ResponseBody
    public List<BtBankAccDTO> getDownAcc(String corpId, String curId, String account) {
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
        return listAcc;
    }

    /**
     * 结算中心实际付款账号
     *
     * @param corpId
     * @return
     */
    @PostMapping("/getRealPayerAcc")
    @ResponseBody
    public List<BtBankAccDTO> getRealPayerAcc(String corpId,String voucherTypeId,String payerAcc) {
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
        return listAcc;
    }

    /**
     * 获取资金下拨账号（企业活期对应的银行账号）
     *
     * @param corpId
     * @param curId
     * @param account
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getDownAccByExecute")
    @ResponseBody
    public List<BtBankAccDTO> getDownAccByExecute(String corpId, String curId, String account, String sysVoucherCode) {
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
        return listAcc;

    }

    /**
     * 付款执行
     *
     * @return
     */
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listExecute")
    public String listExecute(Model model) {
        return "netbank/listNisBillheadExecute";
    }

    /**
     * 付款执行指令发送
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_EXECUTEAPPLY)
    @GetMapping("/send")
    public String send(NisBillheadVO nisBillhead, Model model) {
        if (StringUtils.isNotEmpty(nisBillhead.getId())) {
            nisBillhead = nisBillheadService.getByIdUnion(nisBillhead.getId());
            model.addAttribute("nisBillheadPage", nisBillhead);
            //默认允许修改
            model.addAttribute("allowEditDownacc", "1");
            //转付
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_02.equals(nisBillhead.getSysVoucherCode())) {
                //获取下拨账号
                BtBankAccDTO prentAcc = btBankAccService.getParentAccByAccId(nisBillhead.getDownAccId(), nisBillhead.getCurId());
                model.addAttribute("prentAcc", prentAcc);
                model.addAttribute("allowEditDownacc", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWEDITDOWNACC).getValue());
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
            model.addAttribute("bankAcc", bankAcc);
            model.addAttribute("accName", accName);
        }
        return "netbank/executeNisBillhead";
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
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_EXECUTEAPPLY)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back/{id}")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_EXECUTEAPPLY)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchBack")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_EXECUTEAPPLY)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSend")
    @ResponseBody
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
//    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getBankAcc(String corpId, String downAccId, String curId, String account) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(downAccId)) {
            //转付获取企业活期对应的银行账号
            listAcc = this.getDownAcc(corpId, curId, account);
        } else {
            //代付，获取内部账号对应的具有转账权限的银行账号
            List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                for (BtBankAccDTO bankAcc : listBankAcc) {
                    //if (bankAcc.getCurId().equals(curId) && account.equals(bankAcc.getCounterAccId())) {
                    listAcc.add(bankAcc);
                    //}
                }
            }
        }
        return listAcc;
    }

    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        return "netbank/listNisBillheadApprove";
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
        return this.nisBillheadService.dataTableApprove(dataTablesInput);
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            NisBillheadVO nisBillheadVO = this.nisBillheadService.getByIdUnion(id);
            model.addAttribute("nisBillheadPage", nisBillheadVO);
            model.addAttribute("taskId", taskId);
            //初始化参数
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillheadVO.getBillFrom())) {
                this.initConfig(model, 1);
            } else {
                this.initConfig(model, 0);
            }
            //对私付款单获取对私付款明细
            if (com.bytter.biz.bdlink.api.constant.NetbankConstant.FORINDIVIDUAL_1.equals(nisBillheadVO.getIsForindividual())) {
                List<NisExpenseDetailVO> listNisExpenseDetail = nisExpenseDetailService.listByBillId(nisBillheadVO.getId());
                model.addAttribute("listNisExpenseDetail", listNisExpenseDetail);
            }
            //合并明细
            List<NisBillheadVO> listMerge = nisBillheadService.listByMergeId(id);
            String isMerge = "0";
            if (!EmptyUtils.isEmptyList(listMerge)) {
                isMerge = "1";
            }
            model.addAttribute("isMerge", isMerge);
            model.addAttribute("listMerge", listMerge);
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
            model.addAttribute("isBudget", fcVoucherTypeService.get(nisBillheadVO.getVoucherTypeId(), true).getIsBudget());
            model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        }
        return "netbank/approveNisBillhead";
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            NisBillheadVO nisBillheadVO = this.nisBillheadService.getByIdUnion(businessId);
            model.addAttribute("nisBillheadPage", nisBillheadVO);
            model.addAttribute("taskId", taskId);
            if (ObjectUtils.isNotEmpty(nisBillheadVO)) {
                //初始化参数
                if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillheadVO.getBillFrom())) {
                    this.initConfig(model, 1);
                } else {
                    this.initConfig(model, 0);
                }            //费用报销单获取对私付款明细
                if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillheadVO.getBillFrom())) {
                    List<NisExpenseDetailVO> listNisExpenseDetail = nisExpenseDetailService.listByBillId(nisBillheadVO.getId());
                    model.addAttribute("listNisExpenseDetail", listNisExpenseDetail);
                }
                model.addAttribute("isBudget", fcVoucherTypeService.get(nisBillheadVO.getVoucherTypeId(), true).getIsBudget());
            }

            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
            model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        }
        return "netbank/viewApproveNisBillhead";
    }

    /**
     * 费用报销处理-批量打回明细
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchBackDetail")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/backDetail/{id}")
    @ResponseBody
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/backFailDetail/{id}")
    @ResponseBody
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
    @PostMapping("/saveImport")
    @ResponseBody
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
    @RequestMapping(params = "rePay")
    @ResponseBody
    public AjaxJson rePay(String corpId, BigDecimal payMoney, String payeeAcc, String curId, String id) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(nisBillheadService.isExistRepay(corpId, payMoney, payeeAcc, curId, id, false));
        return j;
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
    @GetMapping("limit")
    @ResponseBody
    public AjaxJson bankAccLimitCheck(String id, String curId, BigDecimal payMoney, String payerAcc, String temp, String wishPayDate) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(nisBillheadService.bankAccLimitCheck(id, payerAcc, curId, payMoney, temp, wishPayDate));
        return j;
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
    @GetMapping("limitForCorp")
    @ResponseBody
    public AjaxJson corpLimitCheck(String id, String curId, BigDecimal payMoney, String corpId, String temp, String wishPayDate) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(nisBillheadService.corpLimitCheck(id, corpId, curId, payMoney, temp, wishPayDate));
        return j;
    }

    /**
     * 获取本位币汇率
     *
     * @param corpId 单位
     * @param curId  当前金额币别
     * @param date   时间
     * @return
     */
    @GetMapping("standardRate")
    @ResponseBody
    public BigDecimal standardRate(String corpId, String curId, Date date) {
        CorpDTO corp = corpService.get(corpId);
        if (corp == null) {
            corp = corpService.getCorpIdByName(corpId);
            if (corp == null) {
                BtBankAccDTO bankAccById = btBankAccService.getBankAccById(corpId);
                if (bankAccById != null) {
                    corp = corpService.get(bankAccById.getCorpId());
                } else {
                    return new BigDecimal(1);
                }
            }
        }
        BigDecimal rate = fcExcrateAdjustService.getExchangeRateBy(curId, corp.getCurId(), new Date(), FcExcrateAdjustConstant.RATE_TYPE_CONVERT);
        if (rate == null) {
            return new BigDecimal(1);
        }
        return rate;
    }

    /**
     * 批量提交时重复付款校验
     *
     * @param ids 单位
     * @return
     */
    @RequestMapping(params = "batchRePay")
    @ResponseBody
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
        j.setSuccess(flag);
        j.setObj(msg);
        return j;
    }

    /**
     * 根据审批列表的付款单corpIds组装单位树
     *
     * @return
     */
    @GetMapping("/treeByCorpIds/{flowStatus}")
    @ResponseBody
    public Object treeByCorpIds(@PathVariable("flowStatus") String flowStatus) {
        return this.nisBillheadService.treeByCorpIds(getCurrentUser(), flowStatus);
    }

    /**
     * 是否可以拆分
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "isMergeBill")
    @ResponseBody
    public AjaxJson isMergeBill(String id) {
        AjaxJson json = new AjaxJson();
        boolean flag = this.nisBillheadService.isMergeBill(id);
        json.setSuccess(flag);
        if (flag) {
            json.setMessageEnum(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_SPLIT);
        }
        return json;
    }

    /**
     * 批量选择付款账号
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_BATCHSETPAYERACC)
    @GetMapping("/toSetPayerAcc/{corpId}/{voucherTypeId}")
    public String toSetPayerAcc(@PathVariable("corpId") String corpId, @PathVariable("voucherTypeId") String voucherTypeId, Model model) {
        FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(voucherTypeId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("voucherType", voucherType);
        return "netbank/batchSetPayerAcc";
    }

    /**
     * 批量选择付款账号
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_BATCHSETPAYERACC)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSetPayerAcc")
    @ResponseBody
    public AjaxJson batchSetPayerAcc(NisBillheadVO nisBillhead) {
        nisBillheadService.batchSetPayerAcc(nisBillhead, this.getCurrentUser());
        return success();
    }

    /**
     * RPA付款业务跟踪 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_RPA_QUERY)
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listRPA")
    public String listRPA(Model model) {
        model.addAttribute("startWishPayday", DateUtils.getDate());
        model.addAttribute("endWishPayday", DateUtils.getDate());
        model.addAttribute("showIsPayed", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        model.addAttribute("opType", "listRPA");
        model.addAttribute("showIsAutoPay", DateUtils.addMonth(DateUtils.getDate(), 1));
        return "netbank/listNisBillheadRPA";
    }

    /**
     * 批量打回RPA付款
     *
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_RPA_APPLY)
    @LogMessage(content = "批量打回RPA付款", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchBackRPA")
    @ResponseBody
    public AjaxJson batchBackRPA(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillhead entity = nisBillheadService.get(id, true);
            //银行处理失败的数据才能打回
            if (entity != null && !BdlinkConstant.BANK_FAILED.equals(entity.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisBillheadService.batchBackRPA(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 批量修改RPA付款的交易状态
     *
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_RPA_APPLY)
    @LogMessage(content = "批量修改RPA付款的交易状态", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchUpdateVoucherStatRPA")
    @ResponseBody
    public AjaxJson batchUpdateVoucherStatRPA(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillhead entity = nisBillheadService.get(id, true);
            //银行处理失败的数据才能打回
            if (entity != null && !BdlinkConstant.BANK_FAILED.equals(entity.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisBillheadService.batchUpdateVoucherStatRPA(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 跳转修改RPA异常状态的弹框界面
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_RPA_QUERY)
    @LogMessage(content = "查询付款通知表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/updateExceptionStatus")
    public String updateExceptionStatus(Model model,String ids){
        model.addAttribute("ids",ids);
        return "netbank/updateExceptionStatus";
    }

    /**
     * 批量修改RPA付款的交易状态
     *
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_RPA_APPLY)
    @LogMessage(content = "批量修改RPA付款的异常交易状态", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchUpdateExceptionVoucherStatRPA")
    @ResponseBody
    public AjaxJson batchUpdateExceptionVoucherStatRPA(@RequestParam("ids") String ids,@RequestParam("updateStatus")String updateStatus) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillhead entity = nisBillheadService.get(id, true);
            //交易异常的数据才能修改
            if (entity != null && !BdlinkConstant.EXCEPTION.equals(entity.getVoucherStat())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        nisBillheadService.batchUpdateExceptionVoucherStatRPA(ids, this.getCurrentUser(),updateStatus);
        return success();
    }

    /**
     * 跳转修改返回信息的弹框界面
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_RPA_QUERY)
    @LogMessage(content = "跳转修改返回信息的弹框界面", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/batchUpdateReturnMsg")
    public String batchUpdateReturnMsg(Model model,String ids){
        model.addAttribute("ids",ids);
        return "netbank/updateReturnMsg";
    }

    /**
     * 修改RPA返回信息
     *
     * @param ids
     * @return
     */
    //@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_RPA_APPLY)
    @LogMessage(content = "修改RPA返回信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/updateReturnMsg")
    @ResponseBody
    public AjaxJson updateReturnMsg(@RequestParam("ids") String ids,@RequestParam("updateMsg")String updateMsg) {
        nisBillheadService.batchUpdateReturnMsg(ids, this.getCurrentUser(),updateMsg);
        return success();
    }

    /**
     * 付款通知表详情页（提供给外模块）
     *
     * @return
     */
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCommon/{id}")
    public String viewCommon(@PathVariable("id") String id, Model model) {
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        NisBillheadVO nisBillhead = nisBillheadService.getByIdUnion(id);
        model.addAttribute("nisBillheadPage", nisBillhead);
        //初始化参数
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
            this.initConfig(model, 1);
        } else {
            this.initConfig(model, 0);
        }
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("isBudget", fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true).getIsBudget());
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        return "netbank/viewNisBillheadCommon";
    }

    /**
     * 付款通知表详情页（提供给外模块）
     *
     * @return
     */
    @LogMessage(content = "查询付款通知表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{billNo}")
    public String viewByBillNo(@PathVariable("billNo") String billNo, Model model) {
        NisBillheadVO nisBillhead = nisBillheadService.getByBillNo(billNo);
        model.addAttribute("nisBillheadPage", nisBillhead);
        //初始化参数
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillhead.getBillFrom())) {
            this.initConfig(model, 1);
        } else {
            this.initConfig(model, 0);
        }
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("isBudget", fcVoucherTypeService.get(nisBillhead.getVoucherTypeId(), true).getIsBudget());
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        return "netbank/viewNisBillheadCommon";
    }

    /**
     * 根据用户流程查询审批付款单据中的账号
     *
     * @return
     */
    @GetMapping("/json/{flowStatus}")
    @ResponseBody
    public List<NisBillheadVO> getPayerAcc(@PathVariable("flowStatus") String flowStatus) {
        ICurrentUser user = this.getCurrentUser();
        List<NisBillheadVO> bankAcc = nisBillheadService.getPayerAcc(user, flowStatus);
        return bankAcc;
    }

    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
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
    @RequestMapping("/exportAllDel")
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

    @GetMapping("/getUserByCorpId")
    @ResponseBody
    public List<UserDTO> getUserByCorpId(@RequestParam(name = "corpId", required = false) String corpId) {
        return nisBillheadService.getUserByCorpId(corpId, this.getCurrentUser());
    }

    /**
     * 删除校验拆分明细单
     *
     * @param splitId
     * @return
     */
    @RequestMapping(params = "checkSplitDtl")
    @ResponseBody
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
    @RequestMapping(params = "getAccountBal")
    @ResponseBody
    public BigDecimal getUseBal(String account, String curId, String isAccount) {
        return nisBillheadService.getAccountBal(account, curId, isAccount);
    }

    /**
     * 获取付款账号
     *
     * @param resource
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayerAccQuery")
    @ResponseBody
    public List<BtBankAccDTO> getPayerAccQuery(String resource) {
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
        return listAcc;
    }

    /**
     * 批量打印
     *
     * @return
     */
    @GetMapping("/batchPrintBill")
    public String batchPrintBill(String ids, Model model) {
        List<NisBillheadVO> list = nisBillheadService.listByIdUnion(ids);
        model.addAttribute("ids", ids);
        model.addAttribute("list", list);
        initConfig(model, 1);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        return "netbank/batchPrintBill";
    }

    /**
     * 判断是否是部分成功部分失败
     *
     * @param billId
     * @return
     */
    @RequestMapping(params = "validDetail")
    @ResponseBody
    public AjaxJson validDetail(String billId) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(nisBillheadService.validDetail(billId));
        return j;
    }

    @LogMessage(content = "票据开票列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/chargeBackType")
    public String chargeBackType(String type, Model model) {
        model.addAttribute("type", type);
        return "netbank/chargeBackType";
    }

    /**
     * 账号余额验证
     *
     * @return
     */
    @PostMapping("checkBal")
    @ResponseBody
    public AjaxJson checkBal(String ids) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(nisBillheadService.checkBal(ids));
        return j;
    }

    /**
     * 校验付款方式为应收票据时，不能选背书申请的票据，需重新选票
     *
     * @param ids 付款单主键id
     * @return 操作结果
     */
    @PostMapping("checkReceiveBill")
    @ResponseBody
    public AjaxJson checkReceiveBill(String ids) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(nisBillheadService.checkReceiveBill(ids));
        return j;
    }
}
