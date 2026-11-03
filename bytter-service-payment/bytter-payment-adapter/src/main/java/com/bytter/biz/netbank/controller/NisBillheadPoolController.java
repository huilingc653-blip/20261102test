package com.bytter.biz.netbank.controller;

import com.bytter.bill.api.bill.dto.AbVoucherinfoViceDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.entity.NisBillheadPool;
import com.bytter.biz.netbank.service.BtChargeBackTypeService;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.service.NisBillheadPoolService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.vo.FcVoucherTypeVO;
import com.bytter.biz.netbank.vo.NisBillheadPoolVO;
import com.bytter.biz.netbank.vo.NisBillheadVO;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.payment.api.constant.*;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;
/**
 * 待付款池
 */
@Controller
@RequestMapping("/netbank/nisbillheadPool")
@Slf4j
public class NisBillheadPoolController extends BaseController implements DisposableBean {
	@Autowired
	private NisBillheadService nisBillheadService;
    @Autowired
    private NisBillheadPoolService nisBillheadPoolService;
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
	private IBtBankService bankService;
    @DubboReference(check = false)
	private IAccebillService accebillService;
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_QUERY)
    @LogMessage(content = "查询待付款池列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("endWishPayday", DateUtils.addMonth(DateUtils.getDate(), 1));
        model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        //来源于ERP的付款单，付款账号是否允许编辑
        model.addAttribute("editPayerAcc", configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue());
        String chargeBackType = "";
        BtChargeBackType btChargeBackType = btChargeBackTypeService.findByDef();
        if (StringUtils.isNotEmpty(btChargeBackType)) {
            chargeBackType = btChargeBackType.getBusType();
        }
        model.addAttribute("chargeBackType", chargeBackType);
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        return "netbank/listNisBillheadPool";
    }
    /**
     * 付款通知表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = nisBillheadPoolService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        return out;
    }
    /**
     * 付款通知表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @GetMapping("/add")
    public String add(NisBillheadPool nisBillheadPool, Model model) {
        if (StringUtils.isNotEmpty(nisBillheadPool.getId())) {
            nisBillheadPool = nisBillheadPoolService.get(nisBillheadPool.getId());
            model.addAttribute("nisBillheadPage", nisBillheadPool);
        }
        model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        model.addAttribute("requiredBusiness", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        //初始化参数
        this.initConfig(model, 0);
        return "netbank/addNisBillheadPool";
    }
    /**
     * 付款通知表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @GetMapping("/update")
    public String update(NisBillheadPoolVO nisBillheadPoolVO, Model model) {
        if (StringUtils.isNotEmpty(nisBillheadPoolVO.getId())) {
            nisBillheadPoolVO = nisBillheadPoolService.getByIdUnion(nisBillheadPoolVO.getId());
            //暂存和编辑状态才可以提交
            if (nisBillheadPoolVO != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillheadPoolVO.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillheadPoolVO.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisBillheadPoolVO.getBillNo());
            }
            if (nisBillheadPoolVO != null && StringUtils.isNotEmpty(nisBillheadPoolVO.getBillId())) {
                String billId = nisBillheadPoolVO.getBillId();
                String vvid = null;
                String billPoolIds = null;
                List<AbVoucherinfoViceDTO> list = accebillService.listAccebillInfoByIds(billId,nisBillheadPoolVO.getId());
                for (AbVoucherinfoViceDTO temp : list) {
                    NisBillheadPoolVO billheadPoolVO = BeanUtils.copyNewInstanceProperties(temp, NisBillheadPoolVO.class);
                    vvid = StringUtils.isEmpty(vvid) ? billheadPoolVO.getVvid() : vvid + "," + billheadPoolVO.getVvid();
                    if ("1".equals(billheadPoolVO.getPoolStatus())) {
                        billPoolIds = StringUtils.isEmpty(billPoolIds) ? billheadPoolVO.getId() : billPoolIds + "," + billheadPoolVO.getId();
                    }
                }
                nisBillheadPoolVO.setVvid(vvid);
                nisBillheadPoolVO.setBillPoolIds(billPoolIds);
            }
            model.addAttribute("nisBillheadPage", nisBillheadPoolVO);
            model.addAttribute("payerBankAcc", this.btBankAccService.getBankAccByBankAccAndCurId(nisBillheadPoolVO.getPayerAcc(), nisBillheadPoolVO.getCurId()));
            //初始化参数
            this.initConfig(model, 0);
        }
        model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("corp", corpService.get(nisBillheadPoolVO.getCorpId()));
        model.addAttribute("requiredBusiness", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        //初始化单据编辑项参数
        this.initEditConfig(model, nisBillheadPoolVO);
        return "netbank/updateNisBillheadPool";
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
     * @param nisBillheadPoolVO
     */
    private void initEditConfig(Model model, NisBillheadPoolVO nisBillheadPoolVO) {
        //ERP接口传输的单据编辑项控制
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_6.equals(nisBillheadPoolVO.getBillFrom())) {
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
        boolean splitIdFlag = !EmptyUtils.isEmpty(nisBillheadPoolVO) && !EmptyUtils.isBlank(nisBillheadPoolVO.getSplitId());
        if (splitIdFlag || nisBillheadPoolService.isMergeBill(nisBillheadPoolVO.getId())) {
            model.addAttribute("editPayMoney", "0");
        } else {
            if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_6.equals(nisBillheadPoolVO.getBillFrom())) {
                model.addAttribute("editPayMoney", "1");
            }
        }
    }
    /**
     * 付款通知表详情页
     *
     * @return
     */
    @LogMessage(content = "查询待付款池详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        NisBillheadPoolVO nisBillheadPoolVO = nisBillheadPoolService.getByIdUnion(id);
        model.addAttribute("nisBillheadPage", nisBillheadPoolVO);
        //初始化参数
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equals(nisBillheadPoolVO.getBillFrom())) {
            this.initConfig(model, 1);
        } else {
            this.initConfig(model, 0);
        }
        //合并明细
        List<NisBillheadPoolVO> listMerge = nisBillheadPoolService.listByMergeId(id);
        String isSplitOrMerge = "0";
        if (!EmptyUtils.isEmptyList(listMerge)) {
            isSplitOrMerge = "1";
        } else {
            if (StringUtils.isNotBlank(nisBillheadPoolVO.getSplitId())) {
                isSplitOrMerge = "1";
                listMerge = new ArrayList<>();
                listMerge.add(nisBillheadPoolService.getByIdUnion(nisBillheadPoolVO.getSplitId()));
            }
        }
        model.addAttribute("isSplitOrMerge", isSplitOrMerge);
        model.addAttribute("listMerge", listMerge);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("isBudget", fcVoucherTypeService.get(nisBillheadPoolVO.getVoucherTypeId(), true).getIsBudget());
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        return "netbank/viewNisBillheadPool";
    }
    /**
     * 添加待付款池
     * @param nisBillheadPoolVO
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "添加待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisBillheadPoolVO nisBillheadPoolVO) {
        if (dataCheck(nisBillheadPoolVO, false)) {
            nisBillheadPoolVO.setBillNo(SequenceSingleton.getSequence(SequenceEnum.NIS_BILLHEAD_POOL));
            nisBillheadPoolService.saveNisBillheadPool(nisBillheadPoolVO, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改待付款池
     *
     * @param nisBillheadPoolVO
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "修改待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(NisBillheadPoolVO nisBillheadPoolVO) {
        if (dataCheck(nisBillheadPoolVO, false)) {
            nisBillheadPoolVO.setBatch(false);
            nisBillheadPoolService.updateNisBillheadPool(nisBillheadPoolVO, this.getCurrentUser(), false);
        }
        return success();
    }
    /**
     * 数据校验
     *
     * @param nisBillheadPoolVO
     * @return
     */
    private boolean dataCheck(NisBillheadPoolVO nisBillheadPoolVO, Boolean isBatch) {
        //收款账号去空格
        if (StringUtils.isNotBlank(nisBillheadPoolVO.getPayeeAcc())) {
            nisBillheadPoolVO.setPayeeAcc(nisBillheadPoolVO.getPayeeAcc().trim());
        }
        //单位不为空
        if (StringUtils.isEmpty(nisBillheadPoolVO.getCorpId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CORP_NULL);
        }
        //付款方式不能为空
        if (StringUtils.isEmpty(nisBillheadPoolVO.getVoucherTypeId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_VOUCHERTYPE_NULL);
        }
        FcVoucherType voucherType = fcVoucherTypeService.get(nisBillheadPoolVO.getVoucherTypeId(), true);
        // 非应付票据，应收票据才校验付款账号
        if (!(com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()))) {
            //付款账号不为空
            if (StringUtils.isEmpty(nisBillheadPoolVO.getPayerAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NULL);
            }
        }
        //转付记账时，需要校验银行账号
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())
                && com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())) {
            if (StringUtils.isEmpty(nisBillheadPoolVO.getDownAccId())) {
                throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_DOWNACC_NULL);
            }
        }
        //币别不为空
        if (StringUtils.isEmpty(nisBillheadPoolVO.getCurId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CUR_NULL);
        }
        //用途不为空
        if (StringUtils.isEmpty(nisBillheadPoolVO.getPurpose())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PURPOSE_NULL);
        }
        //收付款账号不能相同
        if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equalsIgnoreCase(nisBillheadPoolVO.getBillFrom()) && nisBillheadPoolVO.getPayeeAcc().equals(nisBillheadPoolVO.getPayerAcc())) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
        }
        //不记账的付款方式校验跨行标志和地域标志(不包含对私付款)
        if (StringUtils.isNotBlank(nisBillheadPoolVO.getBillNo()) && CommonConstant.NO.equals(voucherType.getIsAccount()) && com.bytter.biz.bdlink.api.constant.NetbankConstant.FORINDIVIDUAL_0.equals(nisBillheadPoolVO.getIsForindividual())) {
            if (StringUtils.isBlank(nisBillheadPoolVO.getDifBank())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_DIFBANK_NULL, nisBillheadPoolVO.getBillNo());
            }
            if (StringUtils.isBlank(nisBillheadPoolVO.getAreaSign())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_AREASIGN_NULL, nisBillheadPoolVO.getBillNo());
            }
        }
        // 应付票据已开票，应收票据校验票据金额
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && CommonConstant.YES.equals(nisBillheadPoolVO.getIsOpenBill()))) {
            //考虑历史数据票据金额为空的不做校验，默认票据金额=付款金额
            if (nisBillheadPoolVO.getBillMoney() != null) {
                //允许付款金额与票据金额不一致
                String allowDifferentBillMoney = configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue();
                if (!CommonConstant.YES.equals(allowDifferentBillMoney)
                        && nisBillheadPoolVO.getBillMoney().compareTo(nisBillheadPoolVO.getPayMoney()) != 0) {
                    throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BILLMONEY_MISMATCH);
                }
            } else {
                nisBillheadPoolVO.setBillMoney(nisBillheadPoolVO.getPayMoney());
            }
        }
        if (!isBatch) {
            String budgetSel = configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
            // 启用资金计划才校验
            if (CommonConstant.YES.equals(budgetSel)) {
                String isBudget = voucherType.getIsBudget();
                boolean flag = true;
                if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_2.equalsIgnoreCase(nisBillheadPoolVO.getBillFrom())) {
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
            }
        }
        //状态校验
        if (StringUtils.isNotEmpty(nisBillheadPoolVO.getId())) {
            NisBillheadPool entity = null;
            if (isBatch) {
                entity = new NisBillheadPool();
                BeanUtils.copyNotNullProperties(nisBillheadPoolVO, entity);
            } else {
                entity = nisBillheadPoolService.get(nisBillheadPoolVO.getId());
            }
            //暂存和编辑状态才可以提交
            if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
            }
        }
        return true;
    }
    /**
     * 删除待付款池
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "删除待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{reply}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("reply") String reply) {
        //状态校验
        NisBillheadPool entity = nisBillheadPoolService.get(id, true);
        //暂存和编辑状态才可以提交
        if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
        }
        nisBillheadPoolService.delNisBillheadPool(id, this.getCurrentUser(), false, reply);
        return success();
    }
    /**
     * 批量删除待付款池
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "批量删除待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids, @RequestParam("reply") String reply) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillheadPool entity = nisBillheadPoolService.get(id, true);
            //暂存和编辑状态才可以提交
            if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
            }
        }
        nisBillheadPoolService.batchDelNisBillheadPool(ids, this.getCurrentUser(), reply);
        return success();
    }
    /**
     * 批量提交付款通知表
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "批量提交待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids, @RequestParam("randoms") String randoms, @RequestParam("sourceDatas") String sourceDatas) {
        boolean flag = false;
        HashMap<String, NisBillheadPoolVO> billMap = new HashMap<>();
        List<String> billNoList = new ArrayList<>();
        //开户行信息
        HashMap<String, NisBillheadPoolVO> bankMap = new HashMap<>();
        HashMap<String, Boolean> corpBudgetMap = new HashMap<>();
        //是否启用待付款池
        String enablePendingPaymentPool = configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_ENABLEPENDINGPAYMENTPOOL).getValue();
        if("0".equals(enablePendingPaymentPool)){
            throw new ParameterException(NetbankMessageEnum.COMMON_MESSAGE_PENDINGPAYMENTPOOL_NOT);
        }
        //获取用户有权限的付款账号
        HashMap<String, BtBankAccDTO> acountMap = nisBillheadPoolService.getAccountGrantMap(getCurrentUser());
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillheadPoolVO nisBillheadPoolVO = nisBillheadPoolService.getVoById(id);
            Boolean reportBudget;
            if (corpBudgetMap.containsKey(nisBillheadPoolVO.getCorpId())) {
                reportBudget = corpBudgetMap.get(nisBillheadPoolVO.getCorpId());
            } else {
                reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(nisBillheadPoolVO.getCorpId());
                corpBudgetMap.put(nisBillheadPoolVO.getCorpId(), reportBudget);
            }
            //过滤挂资金计划的付款方式
            if (reportBudget && CommonConstant.YES.equals(nisBillheadPoolVO.getIsBudget())) {
                billNoList.add(nisBillheadPoolVO.getBillNo());
            }
            flag = dataCheck(nisBillheadPoolVO, true);
            //账号权限校验
            if(!acountMap.containsKey(nisBillheadPoolVO.getPayerAcc())){
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NOT_GRANT, nisBillheadPoolVO.getPayerAcc());
            }
            billMap.put(id, nisBillheadPoolVO);
            //开户行信息去重
            if (StringUtils.isNotBlank(nisBillheadPoolVO.getPayeeBankCode())) {
                if (!bankMap.containsKey(nisBillheadPoolVO.getPayeeBankCode())) {
                    bankMap.put(nisBillheadPoolVO.getPayeeBankCode(), nisBillheadPoolVO);
                }
            }
        }
        for (String payeeBankCode : bankMap.keySet()) {
            // 初始化联行号信息
            this.bankService.saveBtBank(payeeBankCode, bankMap.get(payeeBankCode).getBankTypeId());
        }
        if (flag) {
            String[] idStr = ids.split(CommonConstant.COMMA_SPLIT);
            final CountDownLatch countDownLatch = new CountDownLatch(idStr.length);
            final ICurrentUser user = this.getCurrentUser();
            //线程执行结果集合
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 0; i < idStr.length; i++) {
                String id = idStr[i];
                final NisBillheadPoolVO nisBillheadPoolVO = billMap.get(id);
                nisBillheadPoolVO.setStatus(com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_CHECK);
                nisBillheadPoolVO.setBatch(true);
                //使用线程池处理流程启动，并返回结果
                Future<String> submit = NISBILLHEAD_START_EXECUTOR_SERVICE.submit(new Callable<String>() {
                    @Override
                    public String call() {
                        try {
                            nisBillheadPoolService.batchSubmitNisBillheadPool(nisBillheadPoolVO, null, null, null, user);
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
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error("nisbillhead batchSubmit error :{}", e);
            }
            //处理失败数量
            int errorCount = 0;
            int successCount = 0;
            //获取businessException
            List<BusinessException> errorBusinessExceptions = new ArrayList<>();
            for (Future<String> future : futures) {
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
                        log.error("batch submit nisbillhead error:" + cause.getMessage());
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
                    String lang = multiLangServiceI.getLang(enumName, be.getParams());
                    if (StringUtils.isNotBlank(stringBuilder.toString())) {
                        stringBuilder.append(",");
                    }
                    stringBuilder.append("[").append(lang).append("]");
                }
                throw new BusinessException(WorkFlowMessageEnum.WORK_FLOW_BATCH_SUBMIT_NOTICE, Integer.toString(successCount), Integer.toString(errorCount), stringBuilder.toString());
            }
        }
        return success();
    }
    /**
     * 待付款池表拆分页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @GetMapping("/split")
    public String split(NisBillheadPoolVO nisBillheadPoolVO, Model model) {
        if (StringUtils.isNotEmpty(nisBillheadPoolVO.getId())) {
            nisBillheadPoolVO = nisBillheadPoolService.getByIdUnion(nisBillheadPoolVO.getId());
            //暂存和打回状态才可以进行拆分
            if (nisBillheadPoolVO != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(nisBillheadPoolVO.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(nisBillheadPoolVO.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, nisBillheadPoolVO.getBillNo());
            }
            model.addAttribute("nisBillheadPage", nisBillheadPoolVO);
            //初始化参数
            this.initConfig(model, 0);
            //单位是否上报计划
            boolean reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(nisBillheadPoolVO.getCorpId());
            model.addAttribute("reportBudget", reportBudget ? CommonConstant.YES: CommonConstant.NO);
        }
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        FcVoucherType fcVoucherType = fcVoucherTypeService.get(nisBillheadPoolVO.getVoucherTypeId(), true);
        model.addAttribute("isBudget", fcVoucherType.getIsBudget());
        model.addAttribute("settlType", fcVoucherType.getSettlType());
        return "netbank/splitNisBillheadPool";
    }
    /**
     * 拆分保存
     *
     * @param nisBillheadPool
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "修改待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitSave")
    @ResponseBody
    public AjaxJson splitSave(NisBillheadPool nisBillheadPool,
                              @RequestParam("payMoneys[]") List<BigDecimal> payMoneys, @RequestParam("budgets[]") List<String> budgets) {
        NisBillheadPool t = nisBillheadPoolService.get(nisBillheadPool.getId(), true);
        //暂存和打回状态才可以进行拆分
        if (t != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(t.getStatus()) && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.equals(t.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, t.getBillNo());
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal payMoney : payMoneys) {
            total = total.add(payMoney);
        }
        //单据来源为承兑汇票，不可进行拆分
        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.BILLFROM_3.equals(t.getBillFrom())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_BILlFROM_BILL_SPLIT);
        }
        //拆分金额总和必须等于拆分前的付款金额
        if (total.compareTo(t.getPayMoney()) != 0) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_VALID);
        }
        nisBillheadPoolService.split(t, payMoneys, budgets, this.getCurrentUser());
        return success();
    }
    /**
     * 撤销拆分
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "修改待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitCancel/{id}")
    @ResponseBody
    public AjaxJson splitCancel(@PathVariable("id") String id) {
        nisBillheadPoolService.splitCancel(id, this.getCurrentUser());
        return success();
    }
    /**
     * 合并
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "修改待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/merge")
    @ResponseBody
    public AjaxJson merge(@RequestParam("ids") String ids) {
        nisBillheadPoolService.merge(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 撤销合并
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "修改待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/mergeCancel")
    @ResponseBody
    public AjaxJson mergeCancel(@RequestParam("ids") String ids) {
        nisBillheadPoolService.mergeCancel(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 付款查询 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "查询待付款池列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        model.addAttribute("endWishPayday", DateUtils.addMonth(DateUtils.getDate(), 1));
        model.addAttribute("showIsPayed", configService.getConfig(com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_TYPE, com.bytter.biz.bdlink.api.constant.NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        model.addAttribute("opType", "query");
        model.addAttribute("showIsAutoPay", DateUtils.addMonth(DateUtils.getDate(), 1));
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
        if (!StringUtils.isEmpty(curId) && !StringUtils.isEmpty(account)) {
            //获取具有转账权限的银行账号
            List<BtBankAccDTO> listBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listBankAcc)) {
                Set<String> bank = new HashSet<>();
                for (BtBankAccDTO bankAcc : listBankAcc) {
                    //下拨账号不限制企业活期账户关系
                    //if (curId.equals(bankAcc.getCurId()) && account.equals(bankAcc.getCounterAccId())) {
                        //银行账号对应的父账号
                        BtBankAccDTO prentBankAcc = null;
                        if(StringUtils.isNotBlank(bankAcc.getPrentAccId())) {
                            prentBankAcc = this.btBankAccService.getBankAccById(bankAcc.getPrentAccId());
                        }
                        if (com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_33.equalsIgnoreCase(sysVoucherCode)
                                || com.bytter.biz.bdlink.api.constant.NetbankConstant.PAY_TYPE_35.equals(sysVoucherCode)) {
                            //代付 银行账号对应的父账号
                            if (!EmptyUtils.isEmpty(prentBankAcc) && !bank.contains(prentBankAcc.getBankAcc())) {
                                listAcc.add(prentBankAcc);
                                bank.add(prentBankAcc.getBankAcc());
                            }
                        } else {
                            if (!EmptyUtils.isEmpty(prentBankAcc)) {
                                bankAcc.setPrentBankAcc(prentBankAcc.getBankAcc());
                                bankAcc.setPrentAccName(prentBankAcc.getAccName());
                            }
                            listAcc.add(bankAcc);
                        }
                    //}
                }
            }
        }
        return listAcc;
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
    @SuppressWarnings("unchecked")
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
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisBillheadPoolVO> list) {
        this.nisBillheadPoolService.saveImport(list, this.getCurrentUser());
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
        j.setObj(nisBillheadPoolService.isExistRepay(corpId, payMoney, payeeAcc, curId, id, false));
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
            NisBillheadPool entity = this.nisBillheadPoolService.get(id);
            flag = nisBillheadPoolService.isExistRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, false);
            if (flag) {
                msg = "1";
                break;
            }
        }
        // 校验未提交状态的付款单是否出现重复数据
        if (!flag) {
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                NisBillheadPool entity = this.nisBillheadPoolService.get(id);
                flag = nisBillheadPoolService.isExistRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, true);
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
     * 是否可以拆分
     *
     * @param id
     * @return
     */
    @RequestMapping(params = "isMergeBill")
    @ResponseBody
    public AjaxJson isMergeBill(String id) {
        AjaxJson json = new AjaxJson();
        boolean flag = this.nisBillheadPoolService.isMergeBill(id);
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
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @GetMapping("/toSetPayerAcc/{corpId}/{voucherTypeId}")
    public String toSetPayerAcc(@PathVariable("corpId") String corpId, @PathVariable("voucherTypeId") String voucherTypeId, Model model) {
        FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(voucherTypeId);
        model.addAttribute("corpId", corpId);
        model.addAttribute("voucherType", voucherType);
        return "netbank/batchSetPayerAccPool";
    }
    /**
     * 批量选择付款账号
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSetPayerAcc")
    @ResponseBody
    public AjaxJson batchSetPayerAcc(NisBillheadPoolVO nisBillheadPoolVO) {
        nisBillheadPoolService.batchSetPayerAcc(nisBillheadPoolVO, this.getCurrentUser());
        return success();
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
                DataTablesOutput dataTablesOutput = nisBillheadPoolService.getDataTablesUnion(dataTablesInput, user);
                List<NisBillheadVO> list = dataTablesOutput.getData();
                HashMap<String, NisBillheadVO> curTotalMap = new HashMap<>();
                for(NisBillheadVO vo:list){
                    NisBillheadVO nisBillhead;
                    if(curTotalMap.containsKey(vo.getCurName())){
                        nisBillhead = curTotalMap.get(vo.getCurName());
                        nisBillhead.setPayMoney(nisBillhead.getPayMoney().add(vo.getPayMoney()));
                    } else {
                        nisBillhead = new NisBillheadVO();
                        nisBillhead.setPayMoney(vo.getPayMoney());
                    }
                    nisBillhead.setBillNo(vo.getCurName() + "合计");
                    curTotalMap.put(vo.getCurName(), nisBillhead);
                }
                for(String key:curTotalMap.keySet()){
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
                DataTablesOutput dataTablesOutput = nisBillheadPoolService.getDataTablesUnion(dataTablesInput, user);
                List<NisBillheadVO> list = dataTablesOutput.getData();
                HashMap<String, NisBillheadVO> curTotalMap = new HashMap<>();
                for(NisBillheadVO vo:list){
                    NisBillheadVO nisBillhead;
                    if(curTotalMap.containsKey(vo.getCurName())){
                        nisBillhead = curTotalMap.get(vo.getCurName());
                        nisBillhead.setPayMoney(nisBillhead.getPayMoney().add(vo.getPayMoney()));
                    } else {
                        nisBillhead = new NisBillheadVO();
                        nisBillhead.setPayMoney(vo.getPayMoney());
                    }
                    nisBillhead.setBillNo(vo.getCurName() + "合计");
                    curTotalMap.put(vo.getCurName(), nisBillhead);
                }
                for(String key:curTotalMap.keySet()){
                    list.add(curTotalMap.get(key));
                }
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "作废付款单查询", 5000, NisBillheadVO.class);
    }
    @GetMapping("/getUserByCorpId")
    @ResponseBody
    public List<UserDTO> getUserByCorpId(@RequestParam(name = "corpId", required = false) String corpId) {
        return nisBillheadPoolService.getUserByCorpId(corpId, this.getCurrentUser());
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
        List<NisBillheadPool> list = nisBillheadPoolService.listNisBillheadPoolBySplitId(splitId);
        String result = "";
        //校验拆分明细单状态必须都是暂存或者打回才允许删除
        if (!EmptyUtils.isEmptyList(list)) {
            for (NisBillheadPool entity : list) {
                if (!com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.toString().equals(entity.getStatus().toString())
                        && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_BACK.toString().equals(entity.getStatus().toString())) {
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
     * 批量设置排期要素
     * @param ids
     * @return
     */
    @RequestMapping(params = "schedulingElements")
    @ResponseBody
    public AjaxJson schedulingElements(String ids, String schedulingElementsId) {
        List<NisBillheadPool> list = new ArrayList<>();
        // 校验已提交状态的付款单是否出现重复数据
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillheadPool entity = this.nisBillheadPoolService.get(id);
            entity.setSchedulingElementsId(schedulingElementsId);
            //暂存和编辑状态才可以提交
            if (entity != null && !com.bytter.biz.bdlink.api.constant.NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
            }
            list.add(entity);
        }
        nisBillheadPoolService.batchUpdate(list);
        return success();
    }
}
