package com.bytter.biz.saas.controller.netbank;

import com.bytter.bill.api.bill.dto.AbVoucherinfoViceDTO;
import com.bytter.bill.api.bill.service.IAccebillService;
import com.bytter.biz.bdlink.api.constant.BtBankAccConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.netbank.dto.NisBillHeadPoodIdsDTO;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.entity.FcVoucherType;
import com.bytter.biz.netbank.entity.NisBillheadPool;
import com.bytter.biz.netbank.service.BtChargeBackTypeService;
import com.bytter.biz.netbank.service.FcVoucherTypeService;
import com.bytter.biz.netbank.service.NisBillheadPoolService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.netbank.vo.*;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.constant.InnerAccountConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
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
@Api(value = "/v1-payment/netbank/nisbillheadPool", tags = {"待付款池"})
@RestController("v1NisBillheadPoolController")
@RequestMapping("/v1-payment/netbank/nisbillheadPool")
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
    @ApiOperation(value = "待付款池通知表列表")
    @LogMessage(content = "查询待付款池列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<NisBillheadPoolListResp> list() {
        NisBillheadPoolListResp resp = new NisBillheadPoolListResp();
        resp.setEndWishPayday(DateUtils.addMonth(DateUtils.getDate(), 1));
        resp.setShowIsPayed(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        //来源于ERP的付款单，付款账号是否允许编辑
        resp.setEditPayerAcc(configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue());
        String chargeBackType = "";
        BtChargeBackType btChargeBackType = btChargeBackTypeService.findByDef();
        if (StringUtils.isNotEmpty(btChargeBackType)) {
            chargeBackType = btChargeBackType.getBusType();
        }
        resp.setChargeBackType(chargeBackType);
        //是否启用限额管理  1-是 0-否
        resp.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        return success(resp);
    }


    /**
     * 付款通知表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_QUERY)
    @ApiOperation(value = "付款通知表列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput tablesInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput out = nisBillheadPoolService.getDataTablesUnion(tablesInput, this.getCurrentUser());
        return success(out);
    }
    /**
     * 付款通知表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    // ... existing code ...
    @GetMapping("/add")
    public AjaxJson<NisBillheadPoolAddResp> add(NisBillheadPool nisBillheadPool) {
        NisBillheadPoolAddResp resp = new NisBillheadPoolAddResp();
        if (StringUtils.isNotEmpty(nisBillheadPool.getId())) {
            nisBillheadPool = nisBillheadPoolService.get(nisBillheadPool.getId());
            resp.setNisBillheadPage(nisBillheadPool);
        }
        resp.setWishPayday(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        resp.setRequiredBusiness(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        //初始化参数
        this.setInitConfig(resp, 0);
        return success(resp);
    }

    /**
     * 付款通知表编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @GetMapping("/update")
    public AjaxJson<NisBillheadPoolUpdateResp> update(NisBillheadPoolVO nisBillheadPoolVO) {
        NisBillheadPoolUpdateResp resp = new NisBillheadPoolUpdateResp();
        if (StringUtils.isNotEmpty(nisBillheadPoolVO.getId())) {
            nisBillheadPoolVO = nisBillheadPoolService.getByIdUnion(nisBillheadPoolVO.getId());
            //暂存和编辑状态才可以提交
            if (nisBillheadPoolVO != null && !NetbankConstant.STATUE_TEMP.equals(nisBillheadPoolVO.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisBillheadPoolVO.getStatus())) {
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
            resp.setNisBillheadPage(nisBillheadPoolVO);
            resp.setPayerBankAcc(this.btBankAccService.getBankAccByBankAccAndCurId(nisBillheadPoolVO.getPayerAcc(), nisBillheadPoolVO.getCurId()));
            //初始化参数
            this.setInitConfig(resp, 0);
        }
        resp.setWishPayday(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //资金计划
        resp.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        resp.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        resp.setCorp(corpService.get(nisBillheadPoolVO.getCorpId()));
        resp.setRequiredBusiness(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        //初始化单据编辑项参数
        this.setInitEditConfig(resp, nisBillheadPoolVO);
        return success(resp);
    }


    /**
     * 付款通知表详情页
     *
     * @return
     */
    // ... existing code ...
    @LogMessage(content = "查询待付款池详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisBillheadPoolViewResp> view(@PathVariable("id") String id) {
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        NisBillheadPoolViewResp resp = new NisBillheadPoolViewResp();
        NisBillheadPoolVO nisBillheadPoolVO = nisBillheadPoolService.getByIdUnion(id);
        resp.setNisBillheadPage(nisBillheadPoolVO);
        //初始化参数
        if (NetbankConstant.BILLFROM_2.equals(nisBillheadPoolVO.getBillFrom())) {
            this.setInitConfig(resp, 1);
        } else {
            this.setInitConfig(resp, 0);
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
        resp.setIsSplitOrMerge(isSplitOrMerge);
        resp.setListMerge(listMerge);
        //资金计划
        resp.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        resp.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        resp.setIsBudget(fcVoucherTypeService.get(nisBillheadPoolVO.getVoucherTypeId(), true).getIsBudget());
        resp.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        return success(resp);
    }
    /**
     * 添加待付款池
     * @param nisBillheadPoolVO
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation(value = "添加待付款池")
    @LogMessage(content = "添加待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(@RequestBody NisBillheadPoolVO nisBillheadPoolVO) {
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
    @ApiOperation(value = "修改待付款池")
    @LogMessage(content = "修改待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody NisBillheadPoolVO nisBillheadPoolVO) {
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
        if (!(NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()))) {
            //付款账号不为空
            if (StringUtils.isEmpty(nisBillheadPoolVO.getPayerAcc())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NULL);
            }
        }
        //转付记账时，需要校验银行账号
        if (NetbankConstant.ISACCOUNT_1.equals(voucherType.getIsAccount())
                && NetbankConstant.PAY_TYPE_02.equals(voucherType.getSysVoucherCode())) {
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
        if (!NetbankConstant.BILLFROM_2.equalsIgnoreCase(nisBillheadPoolVO.getBillFrom()) && nisBillheadPoolVO.getPayeeAcc().equals(nisBillheadPoolVO.getPayerAcc())) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
        }
        //不记账的付款方式校验跨行标志和地域标志(不包含对私付款)
        if (StringUtils.isNotBlank(nisBillheadPoolVO.getBillNo()) && CommonConstant.NO.equals(voucherType.getIsAccount()) && NetbankConstant.FORINDIVIDUAL_0.equals(nisBillheadPoolVO.getIsForindividual())) {
            if (StringUtils.isBlank(nisBillheadPoolVO.getDifBank())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_DIFBANK_NULL, nisBillheadPoolVO.getBillNo());
            }
            if (StringUtils.isBlank(nisBillheadPoolVO.getAreaSign())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_AREASIGN_NULL, nisBillheadPoolVO.getBillNo());
            }
        }
        // 应付票据已开票，应收票据校验票据金额
        if (NetbankConstant.PAY_TYPE_50.equals(voucherType.getSysVoucherCode())
                || (NetbankConstant.PAY_TYPE_49.equals(voucherType.getSysVoucherCode()) && CommonConstant.YES.equals(nisBillheadPoolVO.getIsOpenBill()))) {
            //考虑历史数据票据金额为空的不做校验，默认票据金额=付款金额
            if (nisBillheadPoolVO.getBillMoney() != null) {
                //允许付款金额与票据金额不一致
                String allowDifferentBillMoney = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue();
                if (!CommonConstant.YES.equals(allowDifferentBillMoney)
                        && nisBillheadPoolVO.getBillMoney().compareTo(nisBillheadPoolVO.getPayMoney()) != 0) {
                    throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_BILLMONEY_MISMATCH);
                }
            } else {
                nisBillheadPoolVO.setBillMoney(nisBillheadPoolVO.getPayMoney());
            }
        }
        if (!isBatch) {
            String budgetSel = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue();
            // 启用资金计划才校验
            if (CommonConstant.YES.equals(budgetSel)) {
                String isBudget = voucherType.getIsBudget();
                boolean flag = true;
                if (NetbankConstant.BILLFROM_2.equalsIgnoreCase(nisBillheadPoolVO.getBillFrom())) {
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
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
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
    @ApiOperation("删除待付款池")
    @LogMessage(content = "删除待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{reply}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("reply") String reply) {
        //状态校验
        NisBillheadPool entity = nisBillheadPoolService.get(id, true);
        //暂存和编辑状态才可以提交
        if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
        }
        nisBillheadPoolService.delNisBillheadPool(id, this.getCurrentUser(), false, reply);
        return success();
    }
    /**
     * 批量删除待付款池
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation("批量删除待付款池")
    @LogMessage(content = "批量删除待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestBody NisBillHeadPoodIdsDTO nisBillHeadPoodIdsDTO) {
        String ids = nisBillHeadPoodIdsDTO.getIds();
        String reply = nisBillHeadPoodIdsDTO.getReply();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            NisBillheadPool entity = nisBillheadPoolService.get(id, true);
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
            }
        }
        nisBillheadPoolService.batchDelNisBillheadPool(ids, this.getCurrentUser(), reply);
        return success();
    }
    /**
     * 批量提交付款通知表
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation("批量提交付款通知表")
    @LogMessage(content = "批量提交待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestBody NisBillHeadPoodIdsDTO nisBillHeadPoodIdsDTO) {
        boolean flag = false;
        HashMap<String, NisBillheadPoolVO> billMap = new HashMap<>();
        List<String> billNoList = new ArrayList<>();
        //开户行信息
        HashMap<String, NisBillheadPoolVO> bankMap = new HashMap<>();
        HashMap<String, Boolean> corpBudgetMap = new HashMap<>();
        //是否启用待付款池
        String enablePendingPaymentPool = configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ENABLEPENDINGPAYMENTPOOL).getValue();
        if("0".equals(enablePendingPaymentPool)){
            throw new ParameterException(NetbankMessageEnum.COMMON_MESSAGE_PENDINGPAYMENTPOOL_NOT);
        }
        String ids = nisBillHeadPoodIdsDTO.getIds();

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
                nisBillheadPoolVO.setStatus(NetbankConstant.STATUE_CHECK);
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
    public AjaxJson<NisBillheadPoolSplitResp> split(NisBillheadPoolVO nisBillheadPoolVO) {
        NisBillheadPoolSplitResp resp = new NisBillheadPoolSplitResp();
        if (StringUtils.isNotEmpty(nisBillheadPoolVO.getId())) {
            nisBillheadPoolVO = nisBillheadPoolService.getByIdUnion(nisBillheadPoolVO.getId());
            //暂存和打回状态才可以进行拆分
            if (nisBillheadPoolVO != null && !NetbankConstant.STATUE_TEMP.equals(nisBillheadPoolVO.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisBillheadPoolVO.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, nisBillheadPoolVO.getBillNo());
            }
            resp.setNisBillheadPage(nisBillheadPoolVO);
            //初始化参数
            this.setInitConfig(resp, 0);
            //单位是否上报计划
            boolean reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(nisBillheadPoolVO.getCorpId());
            resp.setReportBudget(reportBudget ? CommonConstant.YES: CommonConstant.NO);
        }
        //资金计划
        resp.setBusinessName(BudgetConstant.BUSINESS_NAME_PAY);
        resp.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        resp.setBusType(NisMessageConstant.BUS_TYPE_PAY);
        FcVoucherType fcVoucherType = fcVoucherTypeService.get(nisBillheadPoolVO.getVoucherTypeId(), true);
        resp.setIsBudget(fcVoucherType.getIsBudget());
        resp.setSettlType(fcVoucherType.getSettlType());
        return success(resp);
    }

    /**
     * 拆分保存
     *
     * @param nisBillheadPool
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation("拆分保存")
    @LogMessage(content = "修改待付款池", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitSave")
    @ResponseBody
    public AjaxJson splitSave(NisBillheadPool nisBillheadPool,
                              @RequestParam("payMoneys[]") List<BigDecimal> payMoneys, @RequestParam("budgets[]") List<String> budgets) {
        NisBillheadPool t = nisBillheadPoolService.get(nisBillheadPool.getId(), true);
        //暂存和打回状态才可以进行拆分
        if (t != null && !NetbankConstant.STATUE_TEMP.equals(t.getStatus()) && !NetbankConstant.STATUE_BACK.equals(t.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, t.getBillNo());
        }
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal payMoney : payMoneys) {
            total = total.add(payMoney);
        }
        //单据来源为承兑汇票，不可进行拆分
        if (NetbankConstant.BILLFROM_3.equals(t.getBillFrom())) {
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
    @ApiOperation("撤销拆分")
    @LogMessage(content = "撤销拆分", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/splitCancel/{id}")
    @ResponseBody
    public AjaxJson splitCancel(@PathVariable("id") String id) {
        nisBillheadPoolService.splitCancel(id, this.getCurrentUser());
        return success();
    }
    /**
     * 合并
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation("合并")
    @LogMessage(content = "合并", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/merge")
    @ResponseBody
    public AjaxJson merge(@RequestBody NisBillHeadPoodIdsDTO idsDTO) {
        String ids = idsDTO.getIds();
        nisBillheadPoolService.merge(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 撤销合并
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation("撤销合并")
    @LogMessage(content = "撤销合并", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/mergeCancel")
    @ResponseBody
    public AjaxJson mergeCancel(@RequestBody NisBillHeadPoodIdsDTO idsDTO) {
        String ids = idsDTO.getIds();
        nisBillheadPoolService.mergeCancel(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 付款查询 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation("查询待付款池列表 页面跳转")
    @LogMessage(content = "查询待付款池列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public AjaxJson<NisBillheadPoolQueryResp> query() {
        NisBillheadPoolQueryResp resp = new NisBillheadPoolQueryResp();
        resp.setEndWishPayday(DateUtils.addMonth(DateUtils.getDate(), 1));
        resp.setShowIsPayed(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        resp.setOpType("query");
        resp.setShowIsAutoPay(DateUtils.addMonth(DateUtils.getDate(), 1));
        return success(resp);
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @param voucherTypeId
     * @return
     */
    @SuppressWarnings("unchecked")
    @ApiOperation("获取付款账号")
    @GetMapping("/getPayerAcc")
    @ResponseBody
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
            if (NetbankConstant.ISACCOUNT_0.equals(fcVoucherType.getIsAccount())) {
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
                if (NetbankConstant.PAY_TYPE_49.equals(fcVoucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_50.equals(fcVoucherType.getSysVoucherCode())) {
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
                } else if (NetbankConstant.PAY_TYPE_02.equals(fcVoucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_33.equals(fcVoucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_35.equals(fcVoucherType.getSysVoucherCode())
                        || NetbankConstant.PAY_TYPE_34.equals(fcVoucherType.getSysVoucherCode())) {
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
        return success(listAcc);
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
    @ApiOperation("获取付款账号")
    @GetMapping("/getPayeeAcc")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> getPayeeAcc(String corpId, String curId, String voucherTypeId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(voucherTypeId) && !StringUtils.isEmpty(corpId)) {
            FcVoucherType fcVoucherType = fcVoucherTypeService.get(voucherTypeId, true);
            //内转
            if (NetbankConstant.PAY_TYPE_05.equals(fcVoucherType.getSysVoucherCode())) {
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
        return success(listAcc);
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
    @ApiOperation("获取资金下拨账号（企业活期对应的银行账号）")
    @GetMapping("/getDownAcc")
    @ResponseBody
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
        return success(listAcc);
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
    @ApiOperation("获取资金下拨账号（企业活期对应的银行账号）")
    @GetMapping("/getDownAccByExecute")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> getDownAccByExecute(String corpId, String curId, String account, String sysVoucherCode) {
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
                        if (NetbankConstant.PAY_TYPE_33.equalsIgnoreCase(sysVoucherCode)
                                || NetbankConstant.PAY_TYPE_35.equals(sysVoucherCode)) {
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
        return success(listAcc);
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
    @ApiOperation("获取银行账号")
    @PostMapping("/getBankAcc")
    @ResponseBody
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId, String downAccId, String curId, String account) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(downAccId)) {
            //转付获取企业活期对应的银行账号
            listAcc = this.getDownAcc(corpId, curId, account).getObj();
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
        return success(listAcc);
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
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
    @GetMapping(path = "/rePay")
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
    @ApiOperation("账号支付限额校验")
    @GetMapping("/limit")
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
    @ApiOperation("单位支付限额校验")
    @GetMapping("/limitForCorp")
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

    @ApiOperation("获取本位币汇率")
    @GetMapping("/standardRate")
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
    @ApiOperation("批量提交时重复付款校验")
    @GetMapping(path = "/batchRePay")
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
    @ApiOperation("是否可以拆分")
    @RequestMapping(path = "/isMergeBill")
    @ResponseBody
    public AjaxJson isMergeBill(String id) {
        AjaxJson json = AjaxJson.success();
        boolean flag = this.nisBillheadPoolService.isMergeBill(id);
        json.setObj(flag);
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
    public AjaxJson<NisBillheadPoolSetPayerAccResp> toSetPayerAcc(@PathVariable("corpId") String corpId, @PathVariable("voucherTypeId") String voucherTypeId) {
        NisBillheadPoolSetPayerAccResp resp = new NisBillheadPoolSetPayerAccResp();
        FcVoucherTypeVO voucherType = fcVoucherTypeService.getByIdUnion(voucherTypeId);
        resp.setCorpId(corpId);
        resp.setVoucherType(voucherType);
        return success(resp);
    }

    /**
     * 批量选择付款账号
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEADPOOL_APPLY)
    @ApiOperation("批量选择付款账号")
    @LogMessage(content = "批量选择付款账号", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/batchSetPayerAcc")
    @ResponseBody
    public AjaxJson batchSetPayerAcc(@RequestBody NisBillheadPoolVO nisBillheadPoolVO) {
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
    @ApiOperation(value = "根据单位ID获取用户")
    @GetMapping("/getUserByCorpId")
    @ResponseBody
    public AjaxJson<List<UserDTO>> getUserByCorpId(String corpId) {
        return success(nisBillheadPoolService.getUserByCorpId(corpId, this.getCurrentUser()));
    }
    /**
     * 删除校验拆分明细单
     *
     * @param splitId
     * @return
     */
    @ApiOperation(value = "/删除校验拆分明细单")
    @GetMapping(path = "/checkSplitDtl")
    @ResponseBody
    public AjaxJson checkSplitDtl(String splitId) {
        //获取拆分明细
        List<NisBillheadPool> list = nisBillheadPoolService.listNisBillheadPoolBySplitId(splitId);
        String result = "";
        //校验拆分明细单状态必须都是暂存或者打回才允许删除
        if (!EmptyUtils.isEmptyList(list)) {
            for (NisBillheadPool entity : list) {
                if (!NetbankConstant.STATUE_TEMP.toString().equals(entity.getStatus().toString())
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
     * 批量设置排期要素
     * @param ids
     * @return
     */
    @ApiOperation(value = "/批量设置排期要素")
    @RequestMapping(path = "/schedulingElements")
    @ResponseBody
    public AjaxJson schedulingElements(@RequestBody NisBillHeadPoodIdsDTO idsDTO) {
        List<NisBillheadPool> list = new ArrayList<>();
        String ids = idsDTO.getIds();
        String schedulingElementsId = idsDTO.getSchedulingElementsId();
        // 校验已提交状态的付款单是否出现重复数据
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisBillheadPool entity = this.nisBillheadPoolService.get(id);
            entity.setSchedulingElementsId(schedulingElementsId);
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CAN, entity.getBillNo());
            }
            list.add(entity);
        }
        nisBillheadPoolService.batchUpdate(list);
        return success();
    }

    // ... existing code ...
    /**
     * 初始化系统参数 - 填充到响应对象
     */
    private void setInitConfig(NisBillheadPoolInitConfigVO config, Integer busType) {
        if (busType == 1) {
            //费用报销处理
            config.setBudgetSel(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue());
            config.setPayItem(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_PAYITEM).getValue());
            config.setExpFbsin(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue());
            config.setAccountOver(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_ACCOUNTOVER).getValue());
            config.setBankaccOver(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BANKACCOVER).getValue());
            config.setShowIsPayed(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        } else {
            config.setBudgetSel(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BUDGETSEL).getValue());
            config.setPayItem(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_PAYITEM).getValue());
            config.setAccountOver(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ACCOUNTOVER).getValue());
            config.setBankaccOver(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BANKACCOVER).getValue());
            config.setShowIsPayed(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
            config.setExternalCorp(configService.getConfig(ConfigConstant.SYS, SysConfigConstant.EXTERNALCORP).getValue());
            config.setAllowDifferentBillMoney(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ALLOWDIFFERENTBILLMONEY).getValue());
        }
        //是否启用限额管理  1-是 0-否
        config.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //付款余额控制方式  0-账户余额 1-可用余额
        config.setBalControlType(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_BALCONTROLTYPE).getValue());
    }

    /**
     * 初始化单据编辑项参数 - 填充到响应对象
     */
    private void setInitEditConfig(NisBillheadPoolUpdateResp resp, NisBillheadPoolVO nisBillheadPoolVO) {
        //ERP接口传输的单据编辑项控制
        if (NetbankConstant.BILLFROM_6.equals(nisBillheadPoolVO.getBillFrom())) {
            resp.setEditVoucherTypeId(configService.getConfig(ConfigConstant.ERP, "voucherTypeId").getValue());
            resp.setEditPayerAcc(configService.getConfig(ConfigConstant.ERP, "payerAcc").getValue());
            resp.setEditPayMoney(configService.getConfig(ConfigConstant.ERP, "payMoney").getValue());
            resp.setEditPayeeAcc(configService.getConfig(ConfigConstant.ERP, "payeeAcc").getValue());
            resp.setEditPayeeAccName(configService.getConfig(ConfigConstant.ERP, "payeeAccName").getValue());
            resp.setEditPayeeBank(configService.getConfig(ConfigConstant.ERP, "payeeBank").getValue());
            resp.setEditIsForindividual(configService.getConfig(ConfigConstant.ERP, "isForindividual").getValue());
            resp.setEditWishPayday(configService.getConfig(ConfigConstant.ERP, "wishPayday").getValue());
            resp.setEditPurpose(configService.getConfig(ConfigConstant.ERP, "purpose").getValue());
            resp.setEditBillType(configService.getConfig(ConfigConstant.ERP, "billType").getValue());
            resp.setEditBillFlag(configService.getConfig(ConfigConstant.ERP, "billFlag").getValue());
        } else {
            resp.setEditVoucherTypeId("1");
            resp.setEditPayerAcc("1");
            resp.setEditPayeeAcc("1");
            resp.setEditPayeeAccName("1");
            resp.setEditPayeeBank("1");
            resp.setEditIsForindividual("1");
            resp.setEditWishPayday("1");
            resp.setEditPurpose("1");
        }
        //控制付款金额不允许编辑，拆分单据/合并单据都不允许编辑金额
        boolean splitIdFlag = !EmptyUtils.isEmpty(nisBillheadPoolVO) && !EmptyUtils.isBlank(nisBillheadPoolVO.getSplitId());
        if (splitIdFlag || nisBillheadPoolService.isMergeBill(nisBillheadPoolVO.getId())) {
            resp.setEditPayMoney("0");
        } else {
            if (!NetbankConstant.BILLFROM_6.equals(nisBillheadPoolVO.getBillFrom())) {
                resp.setEditPayMoney("1");
            }
        }
    }


}
