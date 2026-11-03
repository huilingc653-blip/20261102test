package com.bytter.biz.foreign.controller;


import com.bytter.biz.foreign.entity.FexRateType;
import com.bytter.biz.foreign.enums.FexTranTypeEnum;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexCustFieldRuleService;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.service.FexRateInfoService;
import com.bytter.biz.foreign.service.FexRateTypeService;
import com.bytter.biz.foreign.service.FexTranCustInfoService;
import com.bytter.biz.foreign.service.FexTranDealService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.utils.FexTranReqUtil;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.foreign.vo.FexRateInfoVO;
import com.bytter.biz.foreign.vo.FexTranDealVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.payment.api.constant.BdlinkConstant;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.IBankTypeService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.constant.BankConstant;
import com.bytter.system.api.dto.BankTypeCustomDTO;
import com.bytter.system.api.dto.BankTypeDTO;
import com.bytter.system.api.dto.BankTypeOtherCustomDTO;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title: Controller
 * @Description: 外汇买卖交易管理表
 * @author wuxc
 * @date 2025-09-01
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexTradingReq")
public class FexTradingReqController extends BaseController {

    @Autowired
    private FexTranReqService fexTranReqService;

    @DubboReference(check = false)
    private IConfigService configService;

    @Autowired
    private FexCustFieldRuleService fexCustFieldRuleService;

    @Autowired
    private FexRateTypeService fexRateTypeService;

    @DubboReference(check = false)
    private IUserService userService;

    @DubboReference(check = false)
    private IBankTypeService bankTypeService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private FexCashFlowService fexCashFlowService;

    @Autowired
    private FexTranCustInfoService fexTranCustInfoService;

    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;

    @Autowired
    private FexProductService fexProductService;

    @Autowired
    private FexTranDealService fexTranDealService;

    @Autowired
    private FexRateInfoService fexRateInfoService;

    @DubboReference(check = false)
    private IBtSwiftInfoService btSwiftInfoService;

    @LogMessage(content = "查询买卖交易管理列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listForeign/{fexTranType}")
    public String listForeign(@PathVariable("fexTranType") String fexTranType, Model model) {
        model.addAttribute("tranType", ForeignConstant.TRAN_TYPE_1);
        model.addAttribute("fexTranType", FexTranTypeEnum.getByCode(fexTranType).getFexTranType());
        model.addAttribute("endWishPayday", DateUtils.getDate());
        return "foreign/foreignTranReq/list" + FexTranReqUtil.getForeignForwardByTranType(fexTranType);
    }

    @LogMessage(content = "查询买卖交易管理列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listforeign/{fexTranType}")
    public String listforeign(@PathVariable("fexTranType") String fexTranType, Model model) {
        model.addAttribute("tranType", ForeignConstant.TRAN_TYPE_1);
        model.addAttribute("fexTranType", FexTranTypeEnum.getByCode(fexTranType).getFexTranType());
        model.addAttribute("endWishPayday", DateUtils.getDate());
        return "foreign/foreignTranReq/list" + FexTranReqUtil.getForeignForwardByTranType(fexTranType);
    }

    @PostMapping("/datatableForeign")
    @ResponseBody
    public DataTablesOutput dataTableForeign(@RequestBody DataTablesInput dataTablesInput){
        return fexTranReqService.getDataTablesUnionForeign(dataTablesInput, this.getCurrentUser());
    }

    @LogMessage(content = "买卖交易审批列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listForeignApprove/{fexTranType}")
    public String listForeignApprove(@PathVariable("fexTranType") String fexTranType, String flowStatus, String cycleTime, Model model) {
        model.addAttribute("fexTranType", fexTranType);
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "foreign/foreignTranReq/approve" + FexTranReqUtil.getForeignForwardByTranType(fexTranType);
    }

    /**
     * 加载审核列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableForeignApprove")
    @ResponseBody
    public DataTablesOutput dataTableForeignApprove(@RequestBody DataTablesInput dataTablesInput){
        return this.fexTranReqService.dataTableForeignApprove(dataTablesInput);
    }

    @LogMessage(content = "买卖交易审批列表页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove/{fexTranType}")
    public String listApprove(@PathVariable("fexTranType") String fexTranType, String flowStatus, String cycleTime, Model model) {
        model.addAttribute("fexTranType", fexTranType);
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "foreign/foreignTranReq/approve" + FexTranReqUtil.getForeignForwardByTranType(fexTranType);
    }



    /**
     * 跳转至审核详情界面
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        FexTranReqVO vo = new FexTranReqVO();
        if (StringUtils.isNotEmpty(id)) {
            vo = this.fexTranReqService.getByIdUnion(id);
            //设置支付类型
            this.setBankPayType(vo, model);
            model.addAttribute("fexTranReqPage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
            model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
            //交易员
            model.addAttribute("dealer", userService.getByLoginName(vo.getCreateBy()).getUsername());
            //初始化参数
            this.initConfig(model);
            //资金计划
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
            model.addAttribute("custFields", fexTranCustInfoService.listCustInfoByBusId(vo.getId()));


            if (StringUtils.isNotEmpty(vo.getProfitLossCurEnglishCode())) {
                model.addAttribute("showProfitLoss", CommonConstant.YES);
            } else {
                model.addAttribute("showProfitLoss", CommonConstant.NO);
            }
            if (StringUtils.isNotEmpty(vo.getConvertCurType())) {
                model.addAttribute("showConvert", CommonConstant.YES);
            } else {
                model.addAttribute("showConvert", CommonConstant.NO);
            }
        }
        return "foreign/foreignTranReq/approve";
    }


    /**
     * 外汇买卖交易表 新增页面跳转
     *
     * @return
     */
    @GetMapping("/add/{fexTranType}")
    public String add(@PathVariable("fexTranType") String fexTranType, Model model) {
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        model.addAttribute("tranType", ForeignConstant.TRAN_TYPE_1);
        model.addAttribute("fexTranType", fexTranType);
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        //初始化参数
        this.initConfig(model);
        //自定义字段
        model.addAttribute("custFields", fexCustFieldRuleService.listByAppRange(CommonConstant.YES, fexTranType));
        return "foreign/foreignTranReq/add" + FexTranReqUtil.getForeignForwardByTranType(fexTranType);
    }

    @GetMapping("/update/{type}/{id}")
    public String updateForeign(@PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(id);
        model.addAttribute("fexTranType", FexTranTypeEnum.getByCode(type).getFexTranType());
        if(!StringUtils.isBlank(fexTranReq.getTranAppDtlId())){
            if(fexTranReq.getBoodType()==null){
                fexTranReq.setBoodType("9");
            }
        }
        if(fexTranReq.getStatus()==95){
            model.addAttribute("isChange","1");
        }
        //复制标志
        fexTranReq.setCloneFlag(CommonConstant.NO);
        //设置支付类型
        this.setBankPayType(fexTranReq, model);

        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        if(StringUtils.isNotEmpty(fexTranReq.getCreateBy())){
            //交易员
            model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        }

        if(StringUtils.isNotBlank(fexTranReq.getMergeId()) || StringUtils.isNotBlank(fexTranReq.getSplitId())){
            model.addAttribute("editPayMoneyFlag", "0");
        }else{
            model.addAttribute("editPayMoneyFlag", "1");
        }
        model.addAttribute("type", type);
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("custFields", fexTranCustInfoService.listCustInfoByBusId(fexTranReq.getId()));

        if (StringUtils.isNotEmpty(fexTranReq.getConvertCurType())) {
            model.addAttribute("showConvert", CommonConstant.YES);
        } else {
            model.addAttribute("showConvert", CommonConstant.NO);
        }
        // 本位币
        CorpDTO corpDTO = corpService.get(fexTranReq.getCorpId());
        String corpBaseCur = corpDTO.getCurId();
        model.addAttribute("corpBaseCur", corpBaseCur);
        if (StringUtils.isNotEmpty(fexTranReq.getProfitLossCurEnglishCode())) {
            model.addAttribute("showProfitLoss", CommonConstant.YES);
        } else if(ForeignConstant.BILL_FROM_1.equals(fexTranReq.getBillFrom())
                && StringUtils.isEmpty(fexTranReq.getTranRateType())){ // 需求来源 && 产品自动交割
            if (EmptyUtils.isEmpty(fexTranReq.getTranRateType())) {
                fexTranReq.setTranRateType(ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT);
            }
            FexProductVO productVO = fexProductService.getByIdUnion(fexTranReq.getProId());
            Map<String, Object> modelMap = model.asMap();
            // 即期是否计算损益
            String isProfitAndLoss = null != modelMap.get("isProfitAndLoss") ? modelMap.get("isProfitAndLoss").toString() : "";
            // 默认损益币种
            String defProfitAndLossCur = null != modelMap.get("defProfitAndLossCur") ? modelMap.get("defProfitAndLossCur").toString() : "";

            if (CommonConstant.YES.equals(isProfitAndLoss) && CommonConstant.YES.equals(productVO.getIsAutoDelivery())) {
                if (CommonConstant.YES.equals(defProfitAndLossCur)) {
                    fexTranReq.setProfitLossCur(fexTranReq.getTranCurPairBaseId());
                } else {
                    fexTranReq.setProfitLossCur(fexTranReq.getTranCurPairReferId());
                }
                model.addAttribute("showProfitLoss", CommonConstant.YES);
                if(!corpBaseCur.equals(fexTranReq.getProfitLossCur())) {
                    FexRateInfoVO fexRateInfoVO = fexRateInfoService.getQuoMethodForSP(modelMap.get("rateTypeId").toString(), corpBaseCur, fexTranReq.getProfitLossCur());
                    if (null != fexRateInfoVO) {
                        fexTranReq.setConvertCurType(fexRateInfoVO.getCurEnCode() + "/" +fexRateInfoVO.getReferCurEnCode());
                    }
                    model.addAttribute("showConvert", CommonConstant.YES);
                }
            } else {
                model.addAttribute("showProfitLoss", CommonConstant.NO);
            }
        } else {
            model.addAttribute("showProfitLoss", CommonConstant.NO);
        }
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));
        //越南工行需求：2.外汇即期交易，校验【交易对手银行】字段，若对应的【银行类型】为中国工商银行/工商银行且【国家/地区】中文名称为越南时，固定金额字段默认卖出金额不能修改
        if ("spot".equals(type)) {
            model.addAttribute("swiftCodeInfo", btSwiftInfoService.getByCode(fexTranReq.getCotpBicCode()));
        }
        return "foreign/foreignTranReq/updateCommonForeign";
    }

    @GetMapping("/clone/{type}/{id}")
    public String cloneForeign(@PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(id);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        //初始化参数
        this.initConfig(model);
        model.addAttribute("type", type);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("custFields", fexTranCustInfoService.listCustInfoByBusId(fexTranReq.getId()));
        if (fexTranReq.getBuyCurId().equals(fexTranReq.getTranCurPairBaseId())) {
            fexTranReq.setPositionMoney(fexTranReq.getBuyMoney());
        } else {
            fexTranReq.setPositionMoney(fexTranReq.getSellMoney());
        }
        //复制标志
        fexTranReq.setCloneFlag(CommonConstant.YES);
        fexTranReq.setId("");
        fexTranReq.setBillFrom(ForeignConstant.BILL_FROM_0);
        fexTranReq.setBillNo("");
        fexTranReq.setTranAppDtlId(null);
        fexTranReq.setTranAppDtlNo(null);
        fexTranReq.setAttachmentId("");
        model.addAttribute("fexTranReqPage", fexTranReq);

        // 本位币
        CorpDTO corpDTO = corpService.get(fexTranReq.getCorpId());
        String corpBaseCur = corpDTO.getCurId();
        model.addAttribute("corpBaseCur", corpBaseCur);

        // 外汇即期是否计算损益
        ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISPROFITANDLOSS);
        FexProductVO productVO = fexProductService.getByIdUnion(fexTranReq.getProId());
        if (null != profitAndLossConfig && CommonConstant.YES.equals(profitAndLossConfig.getValue())
                && CommonConstant.YES.equals(productVO.getIsAutoDelivery()) && ForeignConstant.FEX_TRAN_RATE_TYPE_CONTRACT.equals(fexTranReq.getTranRateType())) {
            model.addAttribute("showProfitLoss", CommonConstant.YES);

            if(!fexTranReq.getProfitLossCur().equals(corpBaseCur)) {
                model.addAttribute("showConvert", CommonConstant.YES);
            } else {
                model.addAttribute("showConvert", CommonConstant.NO);
            }
        } else {
            model.addAttribute("showProfitLoss", CommonConstant.NO);
            model.addAttribute("showConvert", CommonConstant.NO);
        }
        //越南工行需求：2.外汇即期交易，校验【交易对手银行】字段，若对应的【银行类型】为中国工商银行/工商银行且【国家/地区】中文名称为越南时，固定金额字段默认卖出金额不能修改
        if ("spot".equals(type)) {
            model.addAttribute("swiftCodeInfo", btSwiftInfoService.getByCode(fexTranReq.getCotpBicCode()));
        }
        return "foreign/foreignTranReq/updateCommonForeign";
    }

    /**
     * 外汇交易表-自定义信息 新增页面跳转
     *
     * @return
     */
    @GetMapping("/addTranReqForeign/{swapType}/{type}")
    public String addTranReqForeign(@PathVariable("swapType") String swapType,@PathVariable("type") String type,Model model) {
        /**近/远端  1-近端，2-远端*/
        model.addAttribute("swapType", swapType);
        model.addAttribute("type", type);
        model.addAttribute("tranType", ForeignConstant.TRAN_TYPE_1);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        return "foreign/foreignTranReq/addFexTranReqForeignCommon";
    }


    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{type}/{id}")
    public String viewForeign(@PathVariable("type") String type, @PathVariable("id") String id, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(id);

        //设置支付类型
        this.setBankPayType(fexTranReq, model);
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        model.addAttribute("type", type);
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        model.addAttribute("custFields", fexTranCustInfoService.listCustInfoByBusId(fexTranReq.getId()));
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));

        //合并明细
        List<FexTranReqVO> listMerge = fexTranReqService.listByMergeId(id);
        String isSplitOrMerge = "0";
        if (!EmptyUtils.isEmptyList(listMerge)) {
            isSplitOrMerge = "1";
        } else {
            if (StringUtils.isNotBlank(fexTranReq.getSplitId())) {
                isSplitOrMerge = "1";
                listMerge = new ArrayList<>();
                listMerge.add(fexTranReqService.getByIdUnion(fexTranReq.getSplitId()));
            }
        }
        model.addAttribute("isSplitOrMerge", isSplitOrMerge);
        model.addAttribute("listMerge", listMerge);

        if (StringUtils.isNotEmpty(fexTranReq.getProfitLossCurEnglishCode())) {
            model.addAttribute("showProfitLoss", CommonConstant.YES);
        } else {
            model.addAttribute("showProfitLoss", CommonConstant.NO);
        }
        if (StringUtils.isNotEmpty(fexTranReq.getConvertCurType())) {
            model.addAttribute("showConvert", CommonConstant.YES);
        } else {
            model.addAttribute("showConvert", CommonConstant.NO);
        }
        return "foreign/foreignTranReq/viewCommonForeign";
    }


    /**
     * 查询外汇期权交易表详情
     *
     * @return
     */
    @LogMessage(content = "查询外汇期权交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewOption/{reqId}")
    public String viewBillNo(@PathVariable("reqId") String reqId, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(reqId);
        //设置支付类型
        this.setBankPayType(fexTranReq, model);
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        model.addAttribute("custFields", fexTranCustInfoService.listCustInfoByBusId(fexTranReq.getId()));
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));
        //已违约状态，显示违约信息
        if(ForeignConstant.STATUE_DEFAULT.equals(fexTranReq.getStatus())){
            model.addAttribute("opType", ForeignConstant.OP_TYPE_2);
            List<FexTranDealVO> tranDealList = fexTranDealService.listVOByTranReqId(ForeignConstant.OP_TYPE_2, fexTranReq.getId());
            FexTranDealVO vo = new FexTranDealVO();
            for(FexTranDealVO tranDeal:tranDealList){
                if(ForeignConstant.STATUE_CHECK.equals(tranDeal.getStatus())){
                    vo = tranDeal;
                    break;
                }
            }
            model.addAttribute("fexTranDealPage", vo);
        }
        //合并明细
        List<FexTranReqVO> listMerge = fexTranReqService.listByMergeId(fexTranReq.getId());
        String isSplitOrMerge = "0";
        if (!EmptyUtils.isEmptyList(listMerge)) {
            isSplitOrMerge = "1";
        } else {
            if (StringUtils.isNotBlank(fexTranReq.getSplitId())) {
                isSplitOrMerge = "1";
                listMerge = new ArrayList<>();
                listMerge.add(fexTranReqService.getByIdUnion(fexTranReq.getSplitId()));
            }
        }
        model.addAttribute("isSplitOrMerge", isSplitOrMerge);
        model.addAttribute("listMerge", listMerge);
        return "foreign/viewFexTranReq" + FexTranReqUtil.getForwardByTranType(fexTranReq.getTranType());
    }

    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String viewForeign(@PathVariable("id") String id, Model model) {
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(id);

        //设置支付类型
        this.setBankPayType(fexTranReq, model);
        model.addAttribute("fexTranReqPage", fexTranReq);
        model.addAttribute("sysDate", DateUtils.format(DateUtils.getDateYMD(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(fexTranReq.getCreateBy()).getUsername());
        if (CommonConstant.YES.equals(fexTranReq.getIsSwapTran())) {
            model.addAttribute("type", "swap");
        } else if (ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())) {
            model.addAttribute("type", "spot");
        } else if (ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranReq.getFexTranType())) {
            model.addAttribute("type", "forward");
        }

        //初始化参数
        this.initConfig(model);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_TRAN);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_FEX);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_REQ);
        model.addAttribute("custFields", fexTranCustInfoService.listCustInfoByBusId(fexTranReq.getId()));
        model.addAttribute("cashFlowList", fexCashFlowService.listByTranBillIdUnion(fexTranReq.getId()));

        //合并明细
        List<FexTranReqVO> listMerge = fexTranReqService.listByMergeId(id);
        String isSplitOrMerge = "0";
        if (!EmptyUtils.isEmptyList(listMerge)) {
            isSplitOrMerge = "1";
        } else {
            if (StringUtils.isNotBlank(fexTranReq.getSplitId())) {
                isSplitOrMerge = "1";
                listMerge = new ArrayList<>();
                listMerge.add(fexTranReqService.getByIdUnion(fexTranReq.getSplitId()));
            }
        }
        model.addAttribute("isSplitOrMerge", isSplitOrMerge);
        model.addAttribute("listMerge", listMerge);

        if (StringUtils.isNotEmpty(fexTranReq.getProfitLossCurEnglishCode())) {
            model.addAttribute("showProfitLoss", CommonConstant.YES);
        } else {
            model.addAttribute("showProfitLoss", CommonConstant.NO);
        }
        if (StringUtils.isNotEmpty(fexTranReq.getConvertCurType())) {
            model.addAttribute("showConvert", CommonConstant.YES);
        } else {
            model.addAttribute("showConvert", CommonConstant.NO);
        }
        return "foreign/foreignTranReq/viewCommonForeign";
    }


    /**
     * 设置支付类型
     * @param fexTranReq
     * @param model
     */
    public void setBankPayType(FexTranReqVO fexTranReq, Model model) {
        if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())) {
            return;
        }
        BankTypeDTO bankTypeDTO = new BankTypeDTO();
        // 支付类型
        BankTypeCustomDTO bankPayTypeDTO = new BankTypeCustomDTO();
        // 收款人类型
        BankTypeCustomDTO payeeTypeDTO = new BankTypeCustomDTO();
        // 受益人关联关系
        BankTypeCustomDTO bnfRelateDTO = new BankTypeCustomDTO();
        //其他信息模块
        BankTypeOtherCustomDTO otherCustomDTO = new BankTypeOtherCustomDTO();
        String bankAcc = "";
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType()) || ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
            bankAcc = fexTranReq.getCorpBankAcc();
        } else if (ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())) {
            bankAcc = fexTranReq.getSrcBankAcc();
        } else {
            return;
        }
        BtBankAccDTO payerAcc = btBankAccService.getBankAccInfo(bankAcc);
        String countryId = BdlinkConstant.COUNTRY_CODE_CHINA;
        if (payerAcc != null) {
            bankTypeDTO = bankTypeService.getObjectById(payerAcc.getBankTypeId());
            // 支付类型
            if (StringUtils.isNotBlank(fexTranReq.getBankPayType())) {
                bankPayTypeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexTranReq.getBankPayType(), BankConstant.CUSTOM_TYPE_BANKPAYTYPE);
            }
            // 收款人类型
            if (StringUtils.isNotBlank(fexTranReq.getPayeeType())) {
                payeeTypeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexTranReq.getPayeeType(), BankConstant.CUSTOM_TYPE_PAYEETYPE);
            }
            // 受益人关联关系
            if (StringUtils.isNotBlank(fexTranReq.getBnfRelate())) {
                bankPayTypeDTO = bankTypeService.getByBankBankTypeCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), fexTranReq.getBnfRelate(), BankConstant.CUSTOM_TYPE_BNFRELATE);
            }
            //适用页面 1-外汇买卖交易 2-资金调拨交易 3-资金收付交易 4-外币汇款申请 5-付款办理
            String applayScope = "";
            if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
                applayScope = BankConstant.APPLAY_SCOPE_1;
            } else if (ForeignConstant.TRAN_TYPE_3.equals(fexTranReq.getTranType())) {
                applayScope = BankConstant.APPLAY_SCOPE_2;
            } else if (ForeignConstant.TRAN_TYPE_4.equals(fexTranReq.getTranType())) {
                applayScope = BankConstant.APPLAY_SCOPE_3;
            }
            if (CommonConstant.NO.equals(payerAcc.getIsDomesticBank())) {
                countryId = payerAcc.getProv();
            }
            //其他信息模块
            otherCustomDTO = bankTypeService.getByBankTypeOtherCustom(bankTypeDTO.getId(), payerAcc.getBifCode(), applayScope, countryId, fexTranReq.getRemitType());
        }
        model.addAttribute("countryId", countryId);
        model.addAttribute("payerAcc", payerAcc);
        model.addAttribute("bankType", bankTypeDTO);
        model.addAttribute("bankPayType", bankPayTypeDTO);
        model.addAttribute("payeeType", payeeTypeDTO);
        model.addAttribute("bnfRelate", bnfRelateDTO);
        model.addAttribute("otherCustom", otherCustomDTO);
    }

    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(Model model) {
        model.addAttribute("budgetSel", configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_BUDGETSEL).getValue());
        model.addAttribute("payItem", configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_PAYITEM).getValue());
        model.addAttribute("defProfitAndLossCur", configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_DEFPROFITANDLOSSCUR).getValue());

        ConfigDTO config = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        if(config!=null && !StringUtils.isBlank(config.getValue())){
            model.addAttribute("rateTypeId",config.getValue());
            FexRateType fexRateType = fexRateTypeService.get(config.getValue());
            if(fexRateType!=null){
                model.addAttribute("rateTypeName",fexRateType.getTypeName());
            }
        }

        // 外汇即期是否计算损益
        ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISPROFITANDLOSS);
        if (null != profitAndLossConfig) {
            model.addAttribute("isProfitAndLoss", profitAndLossConfig.getValue());
        }
    }
}
