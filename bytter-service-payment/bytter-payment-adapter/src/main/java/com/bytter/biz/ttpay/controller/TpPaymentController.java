package com.bytter.biz.ttpay.controller;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessCommonDTO;
import com.bytter.biz.budget.spi.dto.FbsBudgetProcessDTO;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.spi.service.IFbsBudgetProcessService;
import com.bytter.biz.budget.spi.service.IFbsBudgetService;
import com.bytter.biz.budget.spi.service.IFbsCorpBudgetService;
import com.bytter.biz.netbank.service.NisBillheadService;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.TtPayConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.ttpay.entity.TpPayment;
import com.bytter.biz.ttpay.service.TpBussinessService;
import com.bytter.biz.ttpay.service.TpMerchantService;
import com.bytter.biz.ttpay.service.TpPaymentService;
import com.bytter.biz.ttpay.vo.*;
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
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.DictDTO;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;
/**
 * @author chenf
 * @version 1.0
 * @Title: Controller
 * @Description: 第三方支付业务
 * @date 2022-5-9
 */
@Controller
@RequestMapping("/ttpay/tpPayment")
@Slf4j
public class TpPaymentController extends BaseController implements DisposableBean {
	@DubboReference(check = false)
	private IFcExcrateAdjustService fcExcrateAdjustService;
	@Autowired
	private NisBillheadService nisBillheadService;
    @Autowired(required = false)
    private TpPaymentService tpPaymentService;
    @Autowired(required = false)
    private TpMerchantService tpMerchantService;
    @Autowired(required = false)
    private TpBussinessService tpBussinessService;
    @DubboReference(check = false)
	private ICorpService corpService;
    @DubboReference(check = false)
	private IBtBankAccService btBankAccService;
    @DubboReference(check = false)
	private IConfigService configService;
    @DubboReference(check = false)
	private IFbsBudgetProcessService fbsBudgetProcessService;
    @DubboReference(check = false)
	private IFbsCorpBudgetService fbsCorpBudgetService;
    @DubboReference(check = false)
	private IDictService dictService;
    @Resource
    private IMultiLangService multiLangServiceI;
    @DubboReference(check = false)
	private IFbsBudgetService fbsBudgetService;
    /**
     * 付款办理批量处理线程池
     */
    private static ExecutorService TTPAYMENT_START_EXECUTOR_SERVICE = SmartThreadPoolManager.wrap(Executors.newFixedThreadPool(20));
    @Override
    public void destroy() throws Exception {
        TTPAYMENT_START_EXECUTOR_SERVICE.shutdown();
    }
/**
     * 付款通知表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_QUERY)
    @LogMessage(content = "查询第三方支付列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("endWishPayday", DateUtils.addMonth(DateUtils.getDate(), 1));
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        return "ttpay/listTpPayment";
    }
    /**
     * 付款通知表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput out = tpPaymentService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
        return out;
    }
    /**
     * 付款通知表新增页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_ADD)
    @GetMapping("/add")
    public String add(TpPayment tpPayment, Model model) {
        if (StringUtils.isNotEmpty(tpPayment.getId())) {
            tpPayment = tpPaymentService.get(tpPayment.getId());
            model.addAttribute("tpPaymentPage", tpPayment);
        }
        model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        model.addAttribute("createDate", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        model.addAttribute("requiredBusiness", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_PAY_TYPE).getValue());
        //初始化参数
        this.initConfig(model, 0);
        return "ttpay/addTpPayment";
    }
    /**
     * 第三方支付编辑页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_UPDATE)
    @GetMapping("/update")
    public String update(TpPaymentVO tpPayment, Model model) {
        if (StringUtils.isNotEmpty(tpPayment.getId())) {
            tpPayment = tpPaymentService.getByIdUnion(tpPayment.getId());
            //暂存和打回状态才可以提交
            if (tpPayment != null && !NetbankConstant.STATUE_TEMP.equals(tpPayment.getStatus()) && !NetbankConstant.STATUE_BACK.equals(tpPayment.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, tpPayment.getBillNo());
            }
            model.addAttribute("tpPaymentPage", tpPayment);
            model.addAttribute("payerBankAcc", this.btBankAccService.getBankAccByBankAccAndCurId(tpPayment.getPayerAcc(), tpPayment.getCurId()));
            //初始化参数
            this.initConfig(model, 0);
            String isLimit = configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue();
            if("1".equals(isLimit)){
                TpQuotaVO  tpQuotaVO = tpPaymentService.getQuotaInfo(tpPayment.getCorpId(),tpPayment.getPayerAcc(),tpPayment.getPayType(),tpPayment.getWishPayday());
                model.addAttribute("tpQuotaPage", tpQuotaVO);
            }
        }
        model.addAttribute("wishPayday", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TTPAYMENT);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_TP);
        model.addAttribute("corp", corpService.get(tpPayment.getCorpId()));
        model.addAttribute("requiredBusiness", configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_REQUIREDBUSINESS).getValue());
        //初始化单据编辑项参数
        //this.initEditConfig(model, tpPayment);
        return "ttpay/updateTpPayment";
    }
    /**
     * 初始化系统参数
     *
     * @param model
     * @param busType
     */
    private void initConfig(Model model, Integer busType) {
        model.addAttribute("budgetSel", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BUDGETSEL).getValue());
        model.addAttribute("payItem", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_PAYITEM).getValue());
        model.addAttribute("bankaccOver", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BANKACCOVER).getValue());
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //付款余额控制方式  0-账户余额 1-可用余额
        model.addAttribute("balControlType", configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BALCONTROLTYPE).getValue());
    }
    /**
     * 获取付款账号
     *
     * @param queryVO 查询商户对象
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayerAcc")
    @ResponseBody
    public List<TpMerchantVO> getPayerAcc(TpMerchantVO queryVO) {
        if(queryVO!=null){
            //单位id与支付方式同时不为空才查询付款账户
            if(StringUtils.isNotEmpty(queryVO.getCorpId()) && StringUtils.isNotEmpty(queryVO.getPayType()) ){
                return tpMerchantService.getTpMerchantVOList(queryVO,this.getCurrentUser());
            }
        }
        return new ArrayList<TpMerchantVO>();
    }
    /**
     * 获取收款商户号及账号
     * @param queryVO 查询收款对象
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayeeAcc")
    @ResponseBody
    public List<TpBussinessTextVO> getPayeeAcc(TpMerchantVO queryVO) {
        if(queryVO!=null){
            //单位id与支付方式同时不为空才查询收款人
            if(StringUtils.isNotEmpty(queryVO.getCorpId()) && StringUtils.isNotEmpty(queryVO.getPayType()) ){
                return tpBussinessService.getBussinessinfo(queryVO.getCorpId(),queryVO.getPayType());
            }
        }
        return new ArrayList<TpBussinessTextVO>();
    }
    /**
     * 获取支付方式(ajax异步）
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayType")
    @ResponseBody
    public List<DictDTO> getPayType() {
        return dictService.list("TTPAY_PAY_TYPE");
    }
    /**
     * 获取支付方式（组件）
     * @return
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/getPayTypeList")
    @ResponseBody
    public List<DictDTO> getPayTypeList() {
        return dictService.list("TTPAY_PAY_TYPE");
    }
    /**
     * 获取限额数据
     * @param corpId（可按单位编码查询）
     * @param payType 支付方式
     * @return
     */
    @SuppressWarnings("unchecked")
    @GetMapping("/getQuotaInfo")
    @ResponseBody
    public TpQuotaVO getQuotaInfo(String corpId,String merchantNo,String payType,String wishPayday) {
        Date wishPaydayDate =null;
        if(StringUtils.isNotEmpty(wishPayday)){
            wishPaydayDate = DateUtils.parse(wishPayday,"yyyy-MM-dd");
        }
        TpQuotaVO tpQuotaVO =  tpPaymentService.getQuotaInfo(corpId,merchantNo,payType,wishPaydayDate);
        if(tpQuotaVO==null){
            tpQuotaVO = new TpQuotaVO();
        }
        return tpQuotaVO;
    }
    /**
     * 列表打印
     *
     * @return
     *//*
    @GetMapping("/print")
    public String print(String id, Model model) {
        model.addAttribute("id", id);
        this.tpPaymentService.updatePrint(id);
        return "receipt/bankbill/printEBankBill";
    }
    *//**
     * 批量查看
     *
     * @return
     *//*
    @GetMapping("/batchPreview")
    public String batchPreview(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "receipt/bankbill/viewEBankBillBatch";
    }
    *//**
     * 批量打印
     *
     * @return
     *//*
    @GetMapping("/batchPrint")
    public String batchPrint(String ids, Model model) {
        List<String> listId = new ArrayList<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            this.tpPaymentService.updatePrint(id);
            listId.add(id);
        }
        model.addAttribute("listId", listId);
        return "receipt/bankbill/printEBankBillBatch";
    }
    *//**
     * 查询第三方支付详情
     *
     * @return
     */
    @LogMessage(content = "查询第三方支付详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (id.indexOf("_") > 0) {
            id = id.substring(0, id.indexOf("_"));
        }
        TpPaymentVO tpPayment = tpPaymentService.getByIdUnion(id);
        model.addAttribute("tpPaymentPage", tpPayment);
        //初始化参数
        this.initConfig(model, 0);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TTPAYMENT);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_TP);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        return "ttpay/viewTpPayment";
    }
    /**
     * 保存第三方支付信息
     *
     * @param tpPayment
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_ADD)
    @LogMessage(content = "保存第三方支付", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(TpPaymentVO tpPayment) {
        if (dataCheck(tpPayment, false)) {
            tpPayment.setBillNo(SequenceSingleton.getSequence(SequenceEnum.TP_PAYMENT));
            tpPaymentService.saveTpPayment(tpPayment, this.getCurrentUser());
            //付款预警增加
            tpPaymentService.warnExecuteNis(tpPayment, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 修改付款通知表
     *
     * @param tpPayment
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_NETBANK_NISBILLHEAD_UPDATE)
    @LogMessage(content = "修改付款通知表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(TpPaymentVO tpPayment) {
        if (dataCheck(tpPayment, false)) {
            tpPayment.setBatch(false);
            tpPaymentService.updateTpPayment(tpPayment, this.getCurrentUser(), false);
            //付款预警增加
            tpPaymentService.warnExecuteNis(tpPayment, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 数据校验
     *
     * @param tpPayment
     * @return
     */
    private boolean dataCheck(TpPaymentVO tpPayment, Boolean isBatch) {
        //收款账号去空格
        if (StringUtils.isNotBlank(tpPayment.getPayeeAcc())) {
            tpPayment.setPayeeAcc(tpPayment.getPayeeAcc().trim());
        }
        //单位不为空
        if (StringUtils.isEmpty(tpPayment.getCorpId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CORP_NULL);
        }
        //付款方式不能为空
        if (StringUtils.isEmpty(tpPayment.getPayType())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_VOUCHERTYPE_NULL);
        }
        //付款账号不为空
        if (StringUtils.isEmpty(tpPayment.getPayerAcc())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_PAYERACC_NULL);
        }
        //币别不为空 默认人民币
        if (StringUtils.isEmpty(tpPayment.getCurId())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_CUR_NULL);
        }
        //收付款账号不能相同
        if (tpPayment.getPayeeAcc().equals(tpPayment.getPayerAcc())) {
            throw new ParameterException(NetbankMessageEnum.NIS_NETBANK_MESSAGE_PAYEEACC_NOTEQ_PAYERACC);
        }
        if (!isBatch) {
            String budgetSel = configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BUDGETSEL).getValue();
            // 启用资金计划才校验
            if (CommonConstant.YES.equals(budgetSel)) {
                boolean flag = true;
                if (this.fbsCorpBudgetService.controlBudgetByCorpId(tpPayment.getCorpId())) {
                    if (CommonConstant.YES.equals(budgetSel) && flag) {
                        //验证是否有预算
                        if (EmptyUtils.isEmptyArray(tpPayment.getItemCode())) {
                            List<FbsBudgetProcessDTO> listBudget = fbsBudgetProcessService.getBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP, tpPayment.getBillNo());
                            if (EmptyUtils.isEmptyList(listBudget)) {
                                throw new ParameterException(NetbankMessageEnum.NIS_BILLHEAD_ITEMCODE_NOTEXISTS);
                            }
                        }
                    }
                }
            }
        }
        //状态校验
        if (StringUtils.isNotEmpty(tpPayment.getId())) {
            TpPayment entity = null;
            if (isBatch) {
                entity = new TpPayment();
                BeanUtils.copyNotNullProperties(tpPayment, entity);
            } else {
                entity = tpPaymentService.get(tpPayment.getId());
            }
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        return true;
    }
    /**
     * 删除第三方支付单据
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_DELETE)
    @LogMessage(content = "删除第三方支付单据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{chargeBackType}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("chargeBackType") String chargeBackType) {
        //状态校验
        TpPayment entity = tpPaymentService.get(id, true);
        //暂存和编辑状态才可以提交
        if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        tpPaymentService.delTpPayment(id, this.getCurrentUser(), false, chargeBackType);
        return success();
    }
    /**
     * 批量删除第三方支付单据
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_DELETE)
    @LogMessage(content = "批量删除第三方支付单据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids, @RequestParam("chargeBackType") String chargeBackType) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            //状态校验
            TpPayment entity = tpPaymentService.get(id, true);
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
            }
        }
        tpPaymentService.batchDelTpPayment(ids, this.getCurrentUser(), chargeBackType);
        return success();
    }
    /**
     * 批量提交付款通知表
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(TtPayConstant.TTPAY_RESOURCE_PAYMENT_SUBMITBATCH)
    @LogMessage(content = "批量提交第三方支付单据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids, @RequestParam("randoms") String randoms, @RequestParam("sourceDatas") String sourceDatas) {
        boolean flag = false;
        HashMap<String, TpPaymentVO> billMap = new HashMap<>();
        List<String> billNoList = new ArrayList<>();
        //开户行信息
        HashMap<String, Boolean> corpBudgetMap = new HashMap<>();
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            TpPaymentVO tpPaymentVO = tpPaymentService.getVoById(id);
            Boolean reportBudget;
            if (corpBudgetMap.containsKey(tpPaymentVO.getCorpId())) {
                reportBudget = corpBudgetMap.get(tpPaymentVO.getCorpId());
            } else {
                reportBudget = fbsCorpBudgetService.reportBudgetByCorpId(tpPaymentVO.getCorpId());
                corpBudgetMap.put(tpPaymentVO.getCorpId(), reportBudget);
            }
            billNoList.add(tpPaymentVO.getBillNo());
            flag = dataCheck(tpPaymentVO, true);
            billMap.put(id, tpPaymentVO);
        }
        // 启用资金计划才校验
        String budgetSel = configService.getConfig(TtPayConstant.TP_CONFIG_TYPE, TtPayConstant.TP_CONFIG_BUDGETSEL).getValue();
        if (EmptyUtils.isNotEmpty(billNoList) && CommonConstant.YES.equals(budgetSel)) {
            //资金计划余额校验
            List<FbsBudgetProcessDTO> listBudget = fbsBudgetProcessService.getSumBudgetProcessByBillNo(BudgetConstant.BUSINESS_NAME_TTPAYMENT, BudgetConstant.PROCESS_SYSNAME_TP, billNoList);
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
            for (int i = 0; i < idStr.length; i++) {
                String id = idStr[i];
                final TpPaymentVO tpPayment = billMap.get(id);
                tpPayment.setStatus(NetbankConstant.STATUE_WAIT_CHECK);
                tpPayment.setBatch(true);
                //使用线程池处理流程启动，并返回结果
                Future<String> submit = TTPAYMENT_START_EXECUTOR_SERVICE.submit(new Callable<String>() {
                    @Override
                    public String call() {
                        try {
                            tpPaymentService.batchSubmitTpPayment(tpPayment, null, null, null, user);
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
                        log.error("batch submit tpPayment error:" + cause.getMessage());
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
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "ttpay/approve/listTpPaymentApprove";
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
        return this.tpPaymentService.dataTableApprove(dataTablesInput);
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
            TpPaymentVO tpPaymentVO = this.tpPaymentService.getByIdUnion(id);
            model.addAttribute("tpPaymentPage", tpPaymentVO);
            model.addAttribute("taskId", taskId);
            //初始化参数
            this.initConfig(model, 0);
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TTPAYMENT);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_TP);
            model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        }
        return "ttpay/approve/approveTpPayment";
    }
    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     *//*
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            TpPaymentVO tpPaymentVO = this.tpPaymentService.getByIdUnion(businessId);
            model.addAttribute("tpPaymentPage", tpPaymentVO);
            model.addAttribute("taskId", taskId);
            //初始化参数
            if (NetbankConstant.BILLFROM_2.equals(tpPaymentVO.getBillFrom())) {
                this.initConfig(model, 1);
            } else {
                this.initConfig(model, 0);
            }
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_PAY);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
            model.addAttribute("isBudget", fcVoucherTypeService.get(tpPaymentVO.getVoucherTypeId()).getIsBudget());
            model.addAttribute("busType", NisMessageConstant.BUS_TYPE_PAY);
        }
        return "netbank/viewApproveTpPayment";
    }
    *//**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<TpPaymentVO> list) {
        this.tpPaymentService.saveImport(list, this.getCurrentUser());
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
        j.setObj(tpPaymentService.isExistRepay(corpId, payMoney, payeeAcc, curId, id, false));
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
     * @param corpId   单位
     * @param merchantNo   商户号
     * @param payMoney 付款金额
     * @param wishPayDate   期望支付日期
     * @param payType   支付方式
     * @return
     */
    @GetMapping("limitForMerchant")
    @ResponseBody
    public AjaxJson limitForMerchant(String id,String corpId,String merchantNo,BigDecimal payMoney,String payType,String wishPayDate,String temp) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        j.setObj(tpPaymentService.limitForMerchant(id,corpId,merchantNo,payMoney,payType,wishPayDate,temp));
        return j;
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
            TpPayment entity = this.tpPaymentService.get(id);
            flag = tpPaymentService.isExistRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, false);
            if (flag) {
                msg = "1";
                break;
            }
        }
        // 校验未提交状态的付款单是否出现重复数据
        if (!flag) {
            for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
                TpPayment entity = this.tpPaymentService.get(id);
                flag = tpPaymentService.isExistRepay(entity.getCorpId(), entity.getPayMoney(), entity.getPayeeAcc(), entity.getCurId(), id, true);
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
        return this.tpPaymentService.treeByCorpIds(getCurrentUser(), flowStatus);
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
                DataTablesOutput dataTablesOutput = tpPaymentService.getDataTablesUnion(dataTablesInput, user);
                List<TpPaymentVO> list = dataTablesOutput.getData();
                HashMap<String, TpPaymentVO> curTotalMap = new HashMap<>();
                for(TpPaymentVO vo:list){
                    TpPaymentVO tpPayment;
                    if(curTotalMap.containsKey(vo.getCurName())){
                        tpPayment = curTotalMap.get(vo.getCurName());
                        tpPayment.setPayMoney(tpPayment.getPayMoney().add(vo.getPayMoney()));
                    } else {
                        tpPayment = new TpPaymentVO();
                        tpPayment.setPayMoney(vo.getPayMoney());
                    }
                    tpPayment.setBillNo(vo.getCurName() + "合计");
                    curTotalMap.put(vo.getCurName(), tpPayment);
                }
                for(String key:curTotalMap.keySet()){
                    list.add(curTotalMap.get(key));
                }
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "付款查询", 5000, TpPaymentVO.class);
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
                DataTablesOutput dataTablesOutput = tpPaymentService.getDataTablesUnion(dataTablesInput, user);
                List<TpPaymentVO> list = dataTablesOutput.getData();
                HashMap<String, TpPaymentVO> curTotalMap = new HashMap<>();
                for(TpPaymentVO vo:list){
                    TpPaymentVO tpPayment;
                    if(curTotalMap.containsKey(vo.getCurName())){
                        tpPayment = curTotalMap.get(vo.getCurName());
                        tpPayment.setPayMoney(tpPayment.getPayMoney().add(vo.getPayMoney()));
                    } else {
                        tpPayment = new TpPaymentVO();
                        tpPayment.setPayMoney(vo.getPayMoney());
                    }
                    tpPayment.setBillNo(vo.getCurName() + "合计");
                    curTotalMap.put(vo.getCurName(), tpPayment);
                }
                for(String key:curTotalMap.keySet()){
                    list.add(curTotalMap.get(key));
                }
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "作废付款单查询", 5000, TpPaymentVO.class);
    }
    /**
     * 获取在途的金额
     *
     * @return
     */
    @RequestMapping(params = "getPayBillBal")
    @ResponseBody
    public TpPayBillBalVO getPayBillBal(String corpId, String merchantNo, String payType) {
        return tpPaymentService.getPayBillBal(corpId, merchantNo, payType);
    }
}
