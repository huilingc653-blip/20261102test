package com.bytter.biz.foreign.controller;


import com.bytter.biz.foreign.dao.FexCalculateSettleMoneyVo;
import com.bytter.biz.foreign.dao.FexProfitLossCalculateVo;
import com.bytter.biz.foreign.enums.LifeCycleOperationEnum;
import com.bytter.biz.foreign.service.FexProductService;
import com.bytter.biz.foreign.service.FexProfitLossManageService;
import com.bytter.biz.foreign.service.FexRateInfoService;
import com.bytter.biz.foreign.service.FexTranLifeCycleService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.utils.FexForeignConverter;
import com.bytter.biz.foreign.vo.FexProductVO;
import com.bytter.biz.foreign.vo.FexProfitLossManageVO;
import com.bytter.biz.foreign.vo.FexRateInfoVO;
import com.bytter.biz.foreign.vo.FexTranLifeCycleVo;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ForeignMessageEnum;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.ICalendarService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IUserService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import com.bytter.system.api.dto.ConfigDTO;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.UserDTO;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/foreign/fexTradingLifecycle")
public class FexTranLifeCycleController extends BaseController {

    @DubboReference(check = false)
    private IUserService userService;

    @Autowired
    private FexTranReqService fexTranReqService;

    @Autowired
    private FexTranLifeCycleService fexTranLifeCycleService;

    @Autowired
    private FexRateInfoService fexRateInfoService;

    @DubboReference(check = false)
    private IConfigService configService;

    @DubboReference(check = false)
    private ICorpService corpService;

    @Autowired
    private FexProfitLossManageService fexProfitLossManageService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    @DubboReference(check = false)
    private ICalendarService calendarService;

    @Autowired
    private FexProductService fexProductService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyPairService;

    /**
     * 生命周期列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询生命周期列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(String relBillNo, Model model) {
        model.addAttribute("createBy",this.getCurrentUser().getLoginName());
        if (StringUtils.isNotEmpty(relBillNo)) {
            model.addAttribute("relBillNo", relBillNo);
        } else {
            model.addAttribute("relBillNo", "");
        }
        return "foreign/foreignLifecycle/list";
    }
    /**
     * 生命周期列表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexTranLifeCycleService.getDataTablesUnion(dataTablesInput, getCurrentUser());
    }


    /**
     * 加载审核列表数据
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput){
        return this.fexTranLifeCycleService.dataTableApprove(dataTablesInput);
    }

    @GetMapping("/addLifecycle/{opType}/{reqId}")
    public String addLifecycle(@PathVariable("opType") String opType, @PathVariable("reqId") String reqId, Model model){
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(reqId);
        // 历史数据可能丢失买卖方向，按买入币种相对货币对左右侧回填，供页面展示与金额明细分支使用
        fillBusDirectionIfBlank(fexTranReq);
        FexTranLifeCycleVo vo  = FexForeignConverter.converterFexTranLifeCycleBean(fexTranReq);
        Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyMap();
        vo = FexForeignConverter.converterFexTranLifeCycleBean(fexTranReq);
        vo.setOpType(opType);
        vo.setBuyCurCode(currencyMap.get(vo.getBuyCurId()).getEnglishCode());
        vo.setSellCurCode(currencyMap.get(vo.getSellCurId()).getEnglishCode());

        if(ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType()) && !LifeCycleOperationEnum.RATE_CONFIRM.getOpType().equals(opType)) {
            if(ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReq.getBusDirection())) {
                vo.setBuyMoney(fexTranReq.getPositionMoney());
                if (fexTranReq.getTranCurPairIdName().equals(fexTranReq.getQuotationMethod())) {
                    vo.setSellMoney(fexTranReq.getPositionMoney().multiply(fexTranReq.getContractRate()));
                } else {
                    vo.setSellMoney(fexTranReq.getPositionMoney().divide(fexTranReq.getContractRate(), 2, RoundingMode.HALF_UP));
                }
            } else {
                vo.setSellMoney(fexTranReq.getPositionMoney());
                if (fexTranReq.getTranCurPairIdName().equals(fexTranReq.getQuotationMethod())) {
                    vo.setBuyMoney(fexTranReq.getPositionMoney().multiply(fexTranReq.getContractRate()));
                } else {
                    vo.setBuyMoney(fexTranReq.getPositionMoney().divide(fexTranReq.getContractRate(), 2, RoundingMode.HALF_UP));
                }
            }
        }
        // 计算操作行最大金额
        BigDecimal maxOpMoney = fexTranReq.getPositionMoney();
        if (null != fexTranReq.getPositionMoney() && null != fexTranReq.getContractRate()) {
            if (fexTranReq.getTranCurPairIdName().equals(fexTranReq.getQuotationMethod())) {
                maxOpMoney = maxOpMoney.multiply(fexTranReq.getContractRate());
            } else {
                maxOpMoney = maxOpMoney.divide(fexTranReq.getContractRate(), 2, RoundingMode.HALF_UP);
            }
        }
        model.addAttribute("maxOpMoney",maxOpMoney);
        // 初始化
        initLifeCycle(vo, fexTranReq, model);
        //交易的默认外汇牌价类型
        String defValuationRateType = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT).getValue();
        //获取参考汇率
        FexRateInfoVO lastFexRateForSP = null;
        //外汇交易类型 0-即期、1-远期
        String fexTranType = fexTranReq.getFexTranType();
        String tranType = fexTranReq.getTranType();
        //校验交易的默认外汇牌价类型是否设置
        if(StringUtils.isNotBlank(defValuationRateType)){
            //获取参考汇率
            if(tranType.equals(ForeignConstant.TRAN_TYPE_2) ||(StringUtils.isNotBlank(fexTranType) && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranType))){
                //获取即期汇率
                lastFexRateForSP = fexRateInfoService.getLastFexRateForSP(defValuationRateType, fexTranReq.getBuyCurId(), fexTranReq.getSellCurId());
            }else if(StringUtils.isNotBlank(fexTranType) && ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranType)){
                //获取远期汇率
                lastFexRateForSP = fexRateInfoService.getRateForEnd(defValuationRateType, fexTranReq.getBuyCurId(), fexTranReq.getSellCurId(), fexTranReq.getSettleDate(), "3");
            }
        }
        model.addAttribute("fexTranReq",fexTranReq);
        BigDecimal referenceExchangeRate = BigDecimal.ZERO;
        if(null != lastFexRateForSP){
            referenceExchangeRate = lastFexRateForSP.getPurchaseRate();
        }
        LocalDate today = LocalDate.now(); // 自动获取当前日期（仅年月日）
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            LocalDate tranDate = convertToLocalDate(fexTranReq.getTranDate());
            LocalDate settleDate = convertToLocalDate(fexTranReq.getSettleDate());

            if (today.isAfter(tranDate) || today.isEqual(tranDate)) {
                if (today.isBefore(settleDate) || today.isEqual(settleDate)) {
                    vo.setOpDate(DateUtils.getDateYMD()); // 保持原格式设置
                } else {
                    vo.setOpDate(null);
                }
            } else {
                vo.setOpDate(null);
            }
        } else if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())) {
            LocalDate tranDate = convertToLocalDate(fexTranReq.getTranDate());
            LocalDate expiresDate = convertToLocalDate(fexTranReq.getExpiresDate());
            if (today.isAfter(tranDate) || today.isEqual(tranDate)) {
                if (today.isBefore(expiresDate) || today.isEqual(expiresDate)) {
                    vo.setOpDate(DateUtils.getDateYMD());
                } else {
                    vo.setOpDate(null);
                }
            } else {
                vo.setOpDate(null);
            }
        }

        if (LifeCycleOperationEnum.RENEWAL.getOpType().equals(opType)) {
            model.addAttribute("deferredSettleDate",DateUtils.formatDate(DateUtils.addDay(fexTranReq.getSettleDate(), 1)));
        } else if(LifeCycleOperationEnum.IN_ADVANCE.getOpType().equals(vo.getOpType()) || LifeCycleOperationEnum.EARLY_DELIVERY.getOpType().equals(vo.getOpType())) {
            model.addAttribute("maxEarlySettleDate",DateUtils.formatDate(DateUtils.addDay(fexTranReq.getSettleDate(), -1)));
        }

        model.addAttribute("referenceExchangeRate", referenceExchangeRate);
        formatMoneyForView(vo);
        model.addAttribute("vo",vo);
        return "foreign/foreignLifecycle/"+getForwardByOpType(opType);
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model){
        FexTranLifeCycleVo vo = fexTranLifeCycleService.getById(id);
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(vo.getReqId());
        fillBusDirectionIfBlank(fexTranReq);

        ConfigDTO config = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        model.addAttribute("rateTypeId", config.getValue());
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());

        if (StringUtils.isNotEmpty(vo.getConvertCurType())) {
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_SHOW_CONVERT);
            model.addAttribute("isShowBaseCurQuotation", Boolean.TRUE);
        } else {
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_HIDE_CONVERT);
            model.addAttribute("isShowBaseCurQuotation", Boolean.FALSE);
        }

        // 回填损益币种：优先生命周期自身，其次交易单/损益管理；并同步到 fexTranReq 供页面绑定
        fillProfitLossCurForView(vo, fexTranReq);
        if (StringUtils.isNotEmpty(vo.getProfitLossCur())) {
            fexTranReq.setProfitLossCur(vo.getProfitLossCur());
        }

        FexProductVO fexProduct = fexProductService.getByIdUnion(fexTranReq.getProId());
        // 外汇即期是否计算损益
        model.addAttribute("isProfitAndLoss", CommonConstant.YES);
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())
                && CommonConstant.NO.equals(fexTranReq.getIsSwapTran())
                && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                && CommonConstant.YES.equals(fexProduct.getIsAutoDelivery())) {
            ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISPROFITANDLOSS);
            if (null != profitAndLossConfig) {
                model.addAttribute("isProfitAndLoss", profitAndLossConfig.getValue());
            }
        } else {
            model.addAttribute("isProfitAndLoss", CommonConstant.NO);
        }
        if (!LifeCycleOperationEnum.RATE_CONFIRM.getOpType().equals(vo.getOpType())) {
            // 计算操作行最大金额
            BigDecimal maxOpMoney = fexTranReq.getPositionMoney();
            if (null != maxOpMoney && fexTranReq.getTranCurPairIdName().equals(fexTranReq.getQuotationMethod())) {
                maxOpMoney = maxOpMoney.multiply(fexTranReq.getContractRate());
            } else if(null != maxOpMoney) {
                maxOpMoney = maxOpMoney.divide(fexTranReq.getContractRate(), 2, RoundingMode.HALF_UP);
            }

            BigDecimal secondOpMoneyMax = BigDecimal.ZERO;
            boolean isSamePairAndQuotation= fexTranReq.getTranCurPairBaseId().equals(fexTranReq.getQuotationCurId())
                    && fexTranReq.getTranCurPairReferId().equals(fexTranReq.getQuotationReferCurId());
            if (isSamePairAndQuotation && !EmptyUtils.isEmpty(fexTranReq.getContractRate())) {
                secondOpMoneyMax = fexTranReq.getPositionMoney().multiply(fexTranReq.getContractRate()).setScale(2, RoundingMode.HALF_UP);
                model.addAttribute("secondOpMoneyMax", secondOpMoneyMax);
            }else if(!EmptyUtils.isEmpty(fexTranReq.getContractRate())){
                secondOpMoneyMax = fexTranReq.getPositionMoney().divide(fexTranReq.getContractRate(),2,RoundingMode.HALF_UP);
                model.addAttribute("secondOpMoneyMax", secondOpMoneyMax);
            }
            model.addAttribute("maxOpMoney",maxOpMoney);
        }
        if (LifeCycleOperationEnum.RENEWAL.getOpType().equals(vo.getOpType())) {
            model.addAttribute("deferredSettleDate",DateUtils.formatDate(DateUtils.addDay(fexTranReq.getSettleDate(), 1)));
        } else if(LifeCycleOperationEnum.IN_ADVANCE.getOpType().equals(vo.getOpType()) || LifeCycleOperationEnum.EARLY_DELIVERY.getOpType().equals(vo.getOpType())) {
            model.addAttribute("maxEarlySettleDate",DateUtils.formatDate(DateUtils.addDay(fexTranReq.getSettleDate(), -1)));
        }

        model.addAttribute("fexTranReq",fexTranReq);
        model.addAttribute("vo",vo);
        return "foreign/foreignLifecycle/update"+getForwardByOpType(vo.getOpType());
    }


    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model){
        FexTranLifeCycleVo vo = fexTranLifeCycleService.getById(id);
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(vo.getReqId());
        fillBusDirectionIfBlank(fexTranReq);
        if (StringUtils.isNotEmpty(vo.getCreateBy())) {
            UserDTO userDTO = userService.getByLoginName(vo.getCreateBy());
            if(null == userDTO){
                userDTO = userService.getByUserId(vo.getCreateBy());
            }
            //交易员
            model.addAttribute("dealer", userDTO.getUsername());
        }
        ConfigDTO config = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        model.addAttribute("rateTypeId", config.getValue());

        // 损益币种为空时，从交易单或损益管理回填，保证查看页能展示
        fillProfitLossCurForView(vo, fexTranReq);

        // 是否显示损益
        if (StringUtils.isNotEmpty(vo.getProfitLossCur()) && !EmptyUtils.isEmpty(vo.getProfitLossMoney())) {
            model.addAttribute("isProfitAndLoss", CommonConstant.YES);
        } else {
            model.addAttribute("isProfitAndLoss", CommonConstant.NO);
        }
        // 是否显示折本位币信息
        if (StringUtils.isNotEmpty(vo.getConvertCurType())) {
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_SHOW_CONVERT);
        } else {
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_HIDE_CONVERT);
        }
        formatMoneyForView(vo);
        model.addAttribute("fexTranReq",fexTranReq);
        model.addAttribute("vo",vo);
        return "foreign/foreignLifecycle/view" + getForwardByOpType(vo.getOpType());
    }

    /**
     * 查看页回填损益币种及英文简码
     */
    private void fillProfitLossCurForView(FexTranLifeCycleVo vo, FexTranReqVO fexTranReq) {
        if (StringUtils.isEmpty(vo.getProfitLossCur())) {
            if (fexTranReq != null && StringUtils.isNotEmpty(fexTranReq.getProfitLossCur())) {
                vo.setProfitLossCur(fexTranReq.getProfitLossCur());
            } else if (fexTranReq != null) {
                FexProfitLossManageVO byReqId = fexProfitLossManageService.getByReqId(fexTranReq.getId());
                if (byReqId != null && StringUtils.isNotEmpty(byReqId.getCurId())) {
                    vo.setProfitLossCur(byReqId.getCurId());
                }
            }
        }
        if (StringUtils.isNotEmpty(vo.getProfitLossCur()) && StringUtils.isEmpty(vo.getProfitLossCurCode())) {
            Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyMap();
            if (currencyMap != null && currencyMap.containsKey(vo.getProfitLossCur())) {
                vo.setProfitLossCurCode(currencyMap.get(vo.getProfitLossCur()).getEnglishCode());
            }
        }
    }

    @PostMapping("/updateLifecycle")
    @ResponseBody
    public AjaxJson updateLifecycle(@RequestBody  FexTranLifeCycleVo lifeCycleVo, Model model){
        fexTranLifeCycleService.updateLifecycle(lifeCycleVo, getCurrentUser());
        return success();
    }

    @PostMapping("/delLifecycle/{id}")
    @ResponseBody
    public AjaxJson delLifecycle(@PathVariable("id") String id){
        fexTranLifeCycleService.deleteLifeCycle(id);
        return success();
    }


    @PostMapping("/checkWorkDate")
    @ResponseBody
    public AjaxJson checkWorkDate(FexTranReqVO vo) {
        AjaxJson ajaxJson = new AjaxJson();
        boolean flag  = true;
        //交易地点为空时，无需校验是否是工作日
        if (StringUtils.isNotBlank(vo.getTranAddrId())) {
            /**交易产品：非工作日是否允许结算 1-是 0-否*/
            FexProductVO fexProduct = fexProductService.getByIdUnion(vo.getProId());
            if (fexProduct != null && CommonConstant.NO.equals(fexProduct.getIsAllowedSettle())) {
                //校验工作日
                if (!calendarService.checkWorkDay(vo.getSettleDate(), "", vo.getTranAddrId())) {
                    flag = false;
                }
            }
        }
        ajaxJson.setObj(flag);
        ajaxJson.setSuccess(true);
        return ajaxJson;
    }

    /**
     * 外汇买卖交易买卖方向为空时，按买入币种是否落在货币对左侧/右侧推断。
     * 与新增页选货币对后的规则一致：买入左侧=买入(1)，买入右侧=卖出(2)。
     */
    private void fillBusDirectionIfBlank(FexTranReqVO fexTranReq) {
        if (fexTranReq == null || StringUtils.isNotBlank(fexTranReq.getBusDirection())) {
            return;
        }
        if (!ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType())) {
            return;
        }
        String buyCurId = fexTranReq.getBuyCurId();
        if (StringUtils.isBlank(buyCurId)) {
            return;
        }
        if (buyCurId.equals(fexTranReq.getTranCurPairBaseId())) {
            fexTranReq.setBusDirection(ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY);
        } else if (buyCurId.equals(fexTranReq.getTranCurPairReferId())) {
            fexTranReq.setBusDirection(ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL);
        }
    }

    /**
     *
     * 生命周期初始化
     *
     * @param vo
     * @param fexTranReq
     * @param model
     */
    private void initLifeCycle(FexTranLifeCycleVo vo,FexTranReqVO fexTranReq,Model model){
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());
        ConfigDTO config = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        model.addAttribute("rateTypeId", config.getValue());
        if (StringUtils.isEmpty(fexTranReq.getProfitLossCur()) || StringUtils.isEmpty(fexTranReq.getProfitLossCur().trim())) {
            //损益信息带出损益币种
            FexProfitLossManageVO byReqId = fexProfitLossManageService.getByReqId(fexTranReq.getId());
            if (null != byReqId) {
                fexTranReq.setProfitLossCur(byReqId.getCurId());
            }
        }
        vo.setProfitLossCur(fexTranReq.getProfitLossCur());

        // 单位本位币种
        CorpDTO corpDTO=corpService.get(vo.getCorpId());
        if (!EmptyUtils.isEmpty(corpDTO) && !corpDTO.getCurId().equals(fexTranReq.getProfitLossCur())) {
            // 折本位币报价方式
            FexRateInfoVO corpCurQuoMethod = fexRateInfoService.getQuoMethodForSP(config.getValue(), fexTranReq.getProfitLossCur(), corpDTO.getCurId());
            if (!EmptyUtils.isEmpty(corpCurQuoMethod)) {
                model.addAttribute("corpCurQuoMethod", corpCurQuoMethod.getCurEnCode()+"/"+corpCurQuoMethod.getReferCurEnCode());
                vo.setConvertCurType(corpCurQuoMethod.getCurEnCode()+"/"+corpCurQuoMethod.getReferCurEnCode());

                if (fexTranReq.getProfitLossCur().equals(corpCurQuoMethod.getCur())) { // 损益币种=折本位币报价方式的左侧, 取买入价
                    model.addAttribute("convertRate", corpCurQuoMethod.getPurchaseRate());
                    vo.setConvertRate(corpCurQuoMethod.getPurchaseRate());
                } else {
                    model.addAttribute("convertRate", corpCurQuoMethod.getSellRate());
                    vo.setConvertRate(corpCurQuoMethod.getSellRate());
                }
            }
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_SHOW_CONVERT);
        } else {
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_HIDE_CONVERT);
        }

        model.addAttribute("isProfitAndLoss", CommonConstant.YES);
        if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType()) && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())) {
            initSpotLifeCycle(vo, fexTranReq, model);
        } else if (ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType()) && ForeignConstant.FEX_TRAN_TYPE_1.equals(fexTranReq.getFexTranType())) {
            initForWardLifeCycle(vo, fexTranReq, model);
        } else if (ForeignConstant.TRAN_TYPE_2.equals(fexTranReq.getTranType())) {
            initOptionLifeCycle(vo, fexTranReq, model);
        }


        // 无本金到期交割损益初始化
        if ((CommonConstant.NO.equals(fexTranReq.getDeliveryType())
                && ForeignConstant.TRAN_TYPE_1.equals(fexTranReq.getTranType()))
                || ForeignConstant.CYCLE_OP_TYPE_12.equals(vo.getOpType())) {
            FexProfitLossCalculateVo profitLossCalculateVo = new FexProfitLossCalculateVo();
            profitLossCalculateVo.setTranCurPairBaseId(fexTranReq.getTranCurPairBaseId());
            profitLossCalculateVo.setTranCurPairReferId(fexTranReq.getTranCurPairReferId());
            profitLossCalculateVo.setQuotationCurId(fexTranReq.getQuotationCurId());
            profitLossCalculateVo.setQuotationReferCurId(fexTranReq.getQuotationReferCurId());

            profitLossCalculateVo.setSpotRate(vo.getSpotRate());
            profitLossCalculateVo.setContractRate(vo.getContractRate());
            profitLossCalculateVo.setBuyCurId(vo.getBuyCurId());
            profitLossCalculateVo.setProfitLossCur(vo.getProfitLossCur());
            profitLossCalculateVo.setCorpId(vo.getCorpId());
            profitLossCalculateVo.setBuyMoney(vo.getBuyMoney());
            profitLossCalculateVo.setSellMoney(vo.getSellMoney());
            profitLossCalculateVo.setBusDirection(fexTranReq.getBusDirection());
            profitLossCalculateVo.setRateTypeId(config.getValue());

            if (!EmptyUtils.isEmpty(vo.getSpotRate()) && !EmptyUtils.isEmpty(vo.getContractRate())) {
                Map<String, BigDecimal> amtMap = fexProfitLossManageService.calculateFexProfitLoss(profitLossCalculateVo);
                vo.setCloseLossMoney(amtMap.get("profitLossMoney"));
                vo.setProfitLossMoney(amtMap.get("profitLossMoney"));
                vo.setConvertLossMoney(amtMap.get("convertLossMoney"));
            }

        }
    }

    private void initSpotLifeCycle(FexTranLifeCycleVo vo,FexTranReqVO fexTranReq,Model model){
        Map<String, Object> modelMap = model.asMap();
        String rateTypeId = modelMap.get("rateTypeId").toString();
        String settleDate = DateUtils.format(fexTranReq.getSettleDate(), "yyyy-MM-dd");
        FexRateInfoVO lastFexRateForSP = fexRateInfoService.getRateInfo(fexTranReq.getTranCurPairBaseId(), fexTranReq.getTranCurPairReferId(), rateTypeId, settleDate, null);
        if (ForeignConstant.CYCLE_OP_TYPE_6.equals(vo.getOpType())) {
            String type = null;
            if(fexTranReq.getQuotationCurId().equals(fexTranReq.getBuyCurId())) {
                type = "1";
            } else {
                type = "2";
            }
            // 平仓汇率
            FexRateInfoVO lastFexRateFor = fexRateInfoService.getRateForEnd(rateTypeId, fexTranReq.getTranCurPairBaseId(), fexTranReq.getTranCurPairReferId(), fexTranReq.getSettleDate(), type);
            if (null != lastFexRateFor) {
                vo.setCloseRate(lastFexRateFor.getReferenceRate());
                vo.setRecordRate(lastFexRateFor.getReferenceRate());
            }
        } else if((ForeignConstant.CYCLE_OP_TYPE_11.equals(vo.getOpType()) ||
                ForeignConstant.CYCLE_OP_TYPE_12.equals(vo.getOpType()))) {
            if (fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationCurId())) { // 买入币种=报价方式的左侧，取买入价
                vo.setSpotRate(lastFexRateForSP.getPurchaseRate());
            } else if(fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationReferCurId())) { // 买入币种=报价方式的右侧，取卖出价
                vo.setSpotRate(lastFexRateForSP.getSellRate());
            }
        }
        // 汇率确认根据“即期是否计算损益”控制是否计算损益
        if (ForeignConstant.CYCLE_OP_TYPE_12.equals(vo.getOpType())) {
            FexProductVO fexProduct = fexProductService.getByIdUnion(fexTranReq.getProId());
            // 外汇即期是否计算损益
            model.addAttribute("isProfitAndLoss", CommonConstant.NO);
            if (CommonConstant.NO.equals(fexTranReq.getIsSwapTran())
                    && ForeignConstant.FEX_TRAN_TYPE_0.equals(fexTranReq.getFexTranType())
                    && CommonConstant.YES.equals(fexProduct.getIsAutoDelivery())) {
                ConfigDTO profitAndLossConfig = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_ISPROFITANDLOSS);
                if (null != profitAndLossConfig) {
                    model.addAttribute("isProfitAndLoss", profitAndLossConfig.getValue());
                }
            }
        }
    }

    private void initForWardLifeCycle(FexTranLifeCycleVo vo,FexTranReqVO fexTranReq,Model model){
        Map<String, Object> modelMap = model.asMap();
        String rateTypeId = modelMap.get("rateTypeId").toString();
        String type = null;
        if(!EmptyUtils.isEmpty(fexTranReq.getQuotationCurId())&&fexTranReq.getQuotationCurId().equals(fexTranReq.getBuyCurId())) {
            type = "1";
        } else {
            type = "2";
        }
        // 平仓汇率
        FexRateInfoVO lastFexRateFor = fexRateInfoService.getRateForEnd(rateTypeId, fexTranReq.getTranCurPairBaseId(), fexTranReq.getTranCurPairReferId(), fexTranReq.getSettleDate(), type);
        if (EmptyUtils.isEmpty(lastFexRateFor)) {
            //这里给出提示“交易货币对在外汇牌价查询的外汇牌价类型***中无报价，请先在外汇牌价查询中录入”
            throw new ParameterException(ForeignMessageEnum.FEX_RATE_INFO_IS_NULL);
        }
        if (fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationCurId())) { // 买入币种=报价方式的左侧，取买入价
            vo.setCloseForwardRate(lastFexRateFor.getReferenceRate());
            vo.setRecordRate(lastFexRateFor.getReferenceRate());
            vo.setSpotRate(lastFexRateFor.getReferenceRate());
            vo.setCloseRate(lastFexRateFor.getReferenceRate());
        } else if(fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationReferCurId())) { // 买入币种=报价方式的右侧，取卖出价
            vo.setCloseForwardRate(lastFexRateFor.getReferenceRate());
            vo.setRecordRate(lastFexRateFor.getReferenceRate());
            vo.setSpotRate(lastFexRateFor.getReferenceRate());
            vo.setCloseRate(lastFexRateFor.getReferenceRate());
        }
        if (StringUtils.isEmpty(vo.getProfitLossCur())) {
            FexProfitLossManageVO profitLossVo = fexProfitLossManageService.getByReqId(fexTranReq.getId());
            if (null != profitLossVo) {
                vo.setProfitLossCur(profitLossVo.getCurId());
            }

        }

        /*if (ForeignConstant.CYCLE_OP_TYPE_6.equals(vo.getOpType()) ||
                (ForeignConstant.CYCLE_OP_TYPE_11.equals(vo.getOpType()) && CommonConstant.NO.equals(fexTranReq.getDeliveryType()))) {
            // 平仓汇率
            FexRateInfoVO quoMethod = fexRateInfoService.getQuoMethodForEnd(rateTypeId, fexTranReq.getTranCurPairBaseId(), fexTranReq.getTranCurPairReferId());
            if (fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationCurId())) { // 买入币种=报价方式的左侧，取买入价
                vo.setCloseRate(quoMethod.getPurchaseRate());
            } else if(fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationReferCurId())) { // 买入币种=报价方式的右侧，取卖出价
                vo.setCloseRate(quoMethod.getSellRate());
            }
        } else if (ForeignConstant.CYCLE_OP_TYPE_10.equals(vo.getOpType())) { // 平仓远期汇率
            FexRateInfoVO quoMethod = fexRateInfoService.getQuoMethodForEnd(rateTypeId, vo.getQuotationCurId(), vo.getQuotationReferCurId());
            if (null != quoMethod && fexTranReq.getBuyCurId().equals(vo.getQuotationCurId())) {
                vo.setCloseForwardRate(quoMethod.getPurchaseRate());
            } else if (null != quoMethod){
                vo.setCloseForwardRate(quoMethod.getSellRate());
            }
        }*/
        if (ForeignConstant.CYCLE_OP_TYPE_12.equals(vo.getOpType())) {
            model.addAttribute("isProfitAndLoss", CommonConstant.NO);
        }
    }

    private void initOptionLifeCycle(FexTranLifeCycleVo vo,FexTranReqVO fexTranReq,Model model){
        Map<String, Object> modelMap = model.asMap();
        String rateTypeId = modelMap.get("rateTypeId").toString();
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());

        // 单位本位币种
        CorpDTO corpDTO=corpService.get(vo.getCorpId());
        model.addAttribute("corpCurId", corpDTO.getCurId());
        Map<String, BtCurrencyDTO> currencyMap = currencyService.getCurrencyMap();
        if (currencyMap.containsKey(fexTranReq.getTranCurPairBaseId())){
            fexTranReq.setTranCurPairBaseCode(currencyMap.get(fexTranReq.getTranCurPairBaseId()).getEnglishCode());
            fexTranReq.setTranCurPairReferCode(currencyMap.get(fexTranReq.getTranCurPairReferId()).getEnglishCode());
        }

        //初始化交易金额明细一些限制和默认值
        //第一行的的操作金额的最大值 和第一行操作金额的默认值
        model.addAttribute("firstOpMoneyMax", fexTranReq.getPositionMoney());
        model.addAttribute("firstOpMoneyDefault", fexTranReq.getPositionMoney());
        //第二行的操作金额的最大值
        //如果交易货币对=报价方式
        BigDecimal secondOpMoneyMax = BigDecimal.ZERO;
        boolean isSamePairAndQuotation= fexTranReq.getTranCurPairBaseId().equals(fexTranReq.getQuotationCurId())
                && fexTranReq.getTranCurPairReferId().equals(fexTranReq.getQuotationReferCurId());
        if (isSamePairAndQuotation) {
            secondOpMoneyMax = fexTranReq.getPositionMoney().multiply(fexTranReq.getContractRate()).setScale(2, RoundingMode.HALF_UP);
            model.addAttribute("secondOpMoneyMax", secondOpMoneyMax);
        }else{
            secondOpMoneyMax = fexTranReq.getPositionMoney().divide(fexTranReq.getContractRate(),2,RoundingMode.HALF_UP);
            model.addAttribute("secondOpMoneyMax", secondOpMoneyMax);
        }
        if((ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(fexTranReq.getBusDirection()) && CommonConstant.NO.equals(fexTranReq.getUpdownDirection()))
        || (ForeignConstant.FEX_TRAN_BUSDIRECTION_SELL.equals(fexTranReq.getBusDirection()) && CommonConstant.YES.equals(fexTranReq.getUpdownDirection()))) {
            vo.setBuyMoney(fexTranReq.getPositionMoney());
            if (isSamePairAndQuotation) {
                vo.setSellMoney(fexTranReq.getPositionMoney().multiply(vo.getContractRate()));
            } else {
                vo.setSellMoney(fexTranReq.getPositionMoney().divide(vo.getContractRate(),2, RoundingMode.HALF_UP));
            }
        } else {
            vo.setSellMoney(fexTranReq.getPositionMoney());
            if (isSamePairAndQuotation) {
                vo.setBuyMoney(fexTranReq.getPositionMoney().multiply(vo.getContractRate()));
            } else {
                vo.setBuyMoney(fexTranReq.getPositionMoney().divide(vo.getContractRate(),2, RoundingMode.HALF_UP));
            }
        }

        //即期汇率默认值
        //如果买入币种=报价方式左侧,取买入价，否则取卖出价
        BigDecimal spotRateDefault = BigDecimal.ZERO;
        //如果不为空,说明是编辑,那就不需要计算默认值
        if(EmptyUtils.isEmpty(vo.getBillNo())){
            FexRateInfoVO curQuoMethod = fexRateInfoService.getQuoMethodForSP(rateTypeId, fexTranReq.getQuotationCurId(), fexTranReq.getQuotationReferCurId());
            if (fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationCurId())){
                spotRateDefault = curQuoMethod.getPurchaseRate();
                model.addAttribute("spotRateDefault", spotRateDefault);
            }else{
                spotRateDefault = curQuoMethod.getSellRate();
                model.addAttribute("spotRateDefault", spotRateDefault);
            }
        }else{
            model.addAttribute("spotRateDefault", fexTranReq.getSpotRate());
        }
        //损益信息带出损益币种
        FexProfitLossManageVO byReqId = fexProfitLossManageService.getByReqId(fexTranReq.getId());
        if (!EmptyUtils.isEmpty(byReqId) && !EmptyUtils.isEmpty(byReqId.getCurId())) {
            //如果不为空,说明是编辑,那就不需要计算默认值
            if (EmptyUtils.isEmpty(fexTranReq.getProfitLossCur())){
                model.addAttribute("profitLossCur", byReqId.getCurId());
            }else{
                model.addAttribute("profitLossCur", fexTranReq.getProfitLossCur());
            }
            //判断折本位币报价方式是否显示
            Boolean isShowBaseCurQuotation = false;
            //单位的本位币不等于损益币种时，显示；否则，隐藏
            if (!byReqId.getCurId().equals(corpDTO.getCurId())) {
                isShowBaseCurQuotation = true;
                //折本位币报价方式默认值
                FexRateInfoVO quoMethodForSP = fexRateInfoService.getQuoMethodForSP(rateTypeId, byReqId.getCurId(), corpDTO.getCurId());
                //如果不为空,说明是编辑,那就不需要计算默认值
                if (EmptyUtils.isEmpty(fexTranReq.getConvertCurId())) {
                    //根据外汇牌价查询中交易的默认外汇牌价类型，自动带出损益币种和单位本位币的报价方式
                    if (quoMethodForSP != null) {
                        model.addAttribute("convertCurId", quoMethodForSP.getCur());
                        model.addAttribute("convertCurReferId", quoMethodForSP.getReferCur());
                        model.addAttribute("convertCurType", quoMethodForSP.getCurEnCode() + "/" + quoMethodForSP.getReferCurEnCode());
                    }
                }else{
                    model.addAttribute("convertCurId", fexTranReq.getConvertCurId());
                    model.addAttribute("convertCurReferId", fexTranReq.getConvertCurReferId());
                    model.addAttribute("convertCurType", fexTranReq.getConvertCurType());
                }
            }

            //计算损益的默认值
            if (ForeignConstant.CYCLE_OP_TYPE_3.equals(vo.getOpType()) && CommonConstant.NO.equals(fexTranReq.getDeliveryType())) { // 平仓&&本金交割
                FexProfitLossCalculateVo profitLossCalculateVo = new FexProfitLossCalculateVo();
                profitLossCalculateVo.setTranCurPairBaseId(fexTranReq.getTranCurPairBaseId());
                profitLossCalculateVo.setTranCurPairReferId(fexTranReq.getTranCurPairReferId());
                profitLossCalculateVo.setQuotationCurId(fexTranReq.getQuotationCurId());
                profitLossCalculateVo.setQuotationReferCurId(fexTranReq.getQuotationReferCurId());
                if (EmptyUtils.isEmpty(vo.getBillNo())) {
                    profitLossCalculateVo.setSpotRate(spotRateDefault);
                } else {
                    profitLossCalculateVo.setSpotRate(vo.getSpotRate());
                }

                profitLossCalculateVo.setContractRate(vo.getContractRate());
                profitLossCalculateVo.setBuyCurId(vo.getBuyCurId());
                profitLossCalculateVo.setProfitLossCur(byReqId.getCurId());
                profitLossCalculateVo.setCorpId(vo.getCorpId());
                profitLossCalculateVo.setBuyMoney(vo.getBuyMoney());
                profitLossCalculateVo.setSellMoney(vo.getSellMoney());
                profitLossCalculateVo.setBusDirection(fexTranReq.getBusDirection());
                profitLossCalculateVo.setRateTypeId(rateTypeId);
                profitLossCalculateVo.setTranType(ForeignConstant.TRAN_TYPE_2);
                profitLossCalculateVo.setUpdownDirection(fexTranReq.getUpdownDirection());

                if (!byReqId.getCurId().equals(corpDTO.getCurId())) {
                    profitLossCalculateVo.setIsShowConvert(ForeignConstant.CYCLE_SHOW_CONVERT);
                } else {
                    profitLossCalculateVo.setIsShowConvert(ForeignConstant.CYCLE_HIDE_CONVERT);
                }

                if (!EmptyUtils.isEmpty(profitLossCalculateVo.getSpotRate()) && !EmptyUtils.isEmpty(profitLossCalculateVo.getContractRate())) {
                    Map<String, BigDecimal> amtMap = fexProfitLossManageService.calculateFexProfitLoss(profitLossCalculateVo);
                    vo.setCloseLossMoney(amtMap.get("profitLossMoney"));
                    vo.setProfitLossMoney(amtMap.get("profitLossMoney"));
                    vo.setConvertLossMoney(amtMap.get("convertLossMoney"));
                    if (!EmptyUtils.isEmpty(amtMap.get("spotRate"))) {
                        vo.setSpotRate(amtMap.get("spotRate"));
                    }
                }
            } else if (ForeignConstant.CYCLE_OP_TYPE_6.equals(vo.getOpType()) ||
                    (ForeignConstant.CYCLE_OP_TYPE_3.equals(vo.getOpType()) && CommonConstant.YES.equals(fexTranReq.getDeliveryType()))) {
                String type = null;
                if(fexTranReq.getQuotationCurId().equals(fexTranReq.getBuyCurId())) {
                    type = "1";
                } else {
                    type = "2";
                }
                FexRateInfoVO lastFexRateFor = fexRateInfoService.getRateForEnd(rateTypeId, fexTranReq.getTranCurPairBaseId(), fexTranReq.getTranCurPairReferId(), fexTranReq.getSettleDate(), type);
                // 平仓汇率
                if (fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationCurId())) { // 买入币种=报价方式的左侧，取买入价
                    vo.setCloseRate(lastFexRateFor.getReferenceRate());
                    vo.setRecordRate(lastFexRateFor.getReferenceRate());
                } else if(fexTranReq.getBuyCurId().equals(fexTranReq.getQuotationReferCurId())) { // 买入币种=报价方式的右侧，取卖出价
                    vo.setCloseRate(lastFexRateFor.getReferenceRate());
                    vo.setRecordRate(lastFexRateFor.getReferenceRate());
                }
            }
            model.addAttribute("isShowBaseCurQuotation", isShowBaseCurQuotation);
        }
        //付款账号和收款账号默认值
        //TODO
    }

    /**
     *
     * 计算结算金额
     *
     * @param calculateSettleMoneyVo
     * @return
     */
    @PostMapping("/calculateSettleMoney")
    @ResponseBody
    public AjaxJson calculateSettleMoney(FexCalculateSettleMoneyVo calculateSettleMoneyVo) {
        BigDecimal settleMoney = BigDecimal.ZERO;
        BigDecimal settleRate = BigDecimal.ZERO;
        if (StringUtils.isEmpty(calculateSettleMoneyVo.getSettleCurId())) {
            return new AjaxJson(false);
        }

        // 获取基础参数并做非空校验
        String tranCurPairIdName = calculateSettleMoneyVo.getTranCurPairIdName();
        String quotationMethod = calculateSettleMoneyVo.getQuotationMethod();
        String busDirection = calculateSettleMoneyVo.getBusDirection();
        BigDecimal rate = calculateSettleMoneyVo.getRate();
        BigDecimal contractRate = calculateSettleMoneyVo.getContractRate();
        BigDecimal buyMoney = calculateSettleMoneyVo.getBuyMoney();
        BigDecimal sellMoney = calculateSettleMoneyVo.getSellMoney();
        String tranCurPairBaseId = calculateSettleMoneyVo.getTranCurPairBaseId();
        // 交易货币对=报价方式
        boolean isSameCurPairAndQuotation = (tranCurPairIdName != null && quotationMethod != null
                && tranCurPairIdName.equals(quotationMethod));
        // 是否买入
        boolean isBuyDirection = ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(busDirection);

        // 计算结算汇率和基础金额
        if (isSameCurPairAndQuotation) {
            settleRate = rate.subtract(contractRate);
        } else {
            // 除法运算指定精度和舍入模式
            BigDecimal oneOverRate = BigDecimal.ONE.divide(rate, 6, BigDecimal.ROUND_HALF_UP);
            BigDecimal oneOverContractRate = BigDecimal.ONE.divide(contractRate, 6, BigDecimal.ROUND_HALF_UP);
            settleRate = oneOverRate.subtract(oneOverContractRate);
        }
        if (ForeignConstant.TRAN_TYPE_2.equals(calculateSettleMoneyVo.getTranType())) {
            if ((isBuyDirection && ForeignConstant.FEX_TRAN_DIRECTION_UP.equals(calculateSettleMoneyVo.getUpdownDirection()))
                    ||  (!isBuyDirection && ForeignConstant.FEX_TRAN_DIRECTION_DOWN.equals(calculateSettleMoneyVo.getUpdownDirection()))) {
                settleMoney = (buyMoney != null) ? buyMoney : BigDecimal.ZERO;
            } else {
                settleMoney = (sellMoney != null) ? sellMoney : BigDecimal.ZERO;
                settleRate = settleRate.negate();
            }
            /*if (!isSameCurPairAndQuotation) {
                settleRate = settleRate.negate();
            }*/
        } else {
            if (isBuyDirection) {
                settleMoney = (buyMoney != null) ? buyMoney : BigDecimal.ZERO;
            } else {
                settleRate = settleRate.negate();
                settleMoney = (sellMoney != null) ? sellMoney : BigDecimal.ZERO;
            }
        }
        // 计算初步结算金额
        settleMoney = settleMoney.multiply(settleRate);
        // 根据结算货币类型进行二次计算
        if (calculateSettleMoneyVo.getSettleCurId().equals(tranCurPairBaseId)) {
            if (isSameCurPairAndQuotation) {
                settleMoney = settleMoney.divide(rate, 6, BigDecimal.ROUND_HALF_UP);
            } else {
                settleMoney = settleMoney.multiply(rate);
            }
        }
        // 最终结果保留6位小数：页面按2位格式化展示，但损益（损益=结算金额*记账汇率）等后续计算必须使用未取整的结算金额，
        // 否则如 4.168333333 被取整为 4.17 后再乘记账汇率，损益会由 29.71 错算成 29.72
        settleMoney = settleMoney.setScale(6, BigDecimal.ROUND_HALF_UP);

        return success(settleMoney);
    }


    /**
     *
     * 计算卖出买入金额
     *
     * @param calculateSettleMoneyVo
     * @return
     */
    @PostMapping("/calRealMoney")
    @ResponseBody
    public AjaxJson calRealMoney(FexCalculateSettleMoneyVo calculateSettleMoneyVo) {
        BigDecimal firstMoney = null;
        BigDecimal realBuyMoney = null;
        BigDecimal realSellMoney = null;
        if(null != calculateSettleMoneyVo.getBuyMoney()){
            calculateSettleMoneyVo.setBuyMoney(scaleMoney(calculateSettleMoneyVo.getBuyMoney()));
        }
        if(null != calculateSettleMoneyVo.getSellMoney()){
            calculateSettleMoneyVo.setSellMoney(scaleMoney(calculateSettleMoneyVo.getSellMoney()));
        }
        if(ForeignConstant.FEX_TRAN_BUSDIRECTION_BUY.equals(calculateSettleMoneyVo.getBusDirection())) {
            firstMoney = scaleMoney(calculateSettleMoneyVo.getBuyMoney());
        } else {
            firstMoney = scaleMoney(calculateSettleMoneyVo.getSellMoney());
        }
        if(calculateSettleMoneyVo.getBuyCurId().equals(calculateSettleMoneyVo.getTranCurPairBaseId())) {
            realBuyMoney = firstMoney;
            if (calculateSettleMoneyVo.getTranCurPairIdName().equals(calculateSettleMoneyVo.getQuotationMethod())) {
                realSellMoney = firstMoney.multiply(calculateSettleMoneyVo.getRate());
            } else {
                realSellMoney = firstMoney.divide(calculateSettleMoneyVo.getRate(), 2, RoundingMode.HALF_UP);
            }
        } else {
            realSellMoney = firstMoney;
            if (calculateSettleMoneyVo.getTranCurPairIdName().equals(calculateSettleMoneyVo.getQuotationMethod())) {
                realBuyMoney = firstMoney.multiply(calculateSettleMoneyVo.getRate());
            } else {
                realBuyMoney = firstMoney.divide(calculateSettleMoneyVo.getRate(), 2, RoundingMode.HALF_UP);
            }
        }
        Map<String, BigDecimal> amtMap = new HashMap<>();
        amtMap.put("realSellMoney", realSellMoney);
        amtMap.put("realBuyMoney", realBuyMoney);
        return success(amtMap);
    }


    /**
     * 跳转至审核列表
     * @return
     */
    @LogMessage(content = "查询生命周期列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime,Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "foreign/foreignLifecycle/listApprove";
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
        model.addAttribute("taskId", taskId);
        model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_TRAN_LIFE_CYCLE);
        FexTranLifeCycleVo vo = fexTranLifeCycleService.getById(id);
        FexTranReqVO fexTranReq = fexTranReqService.getByIdUnion(vo.getReqId());
        fillBusDirectionIfBlank(fexTranReq);

        ConfigDTO config = configService.getConfig(ForeignConstant.FEX_CONFIG_TYPE, ForeignConstant.FEX_CONFIG_TRANRATETYPEDEFAULT);
        model.addAttribute("rateTypeId", config.getValue());
        //交易员
        model.addAttribute("dealer", this.getCurrentUser().getUsername());

        if (StringUtils.isNotEmpty(vo.getConvertCurType())) {
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_SHOW_CONVERT);
            model.addAttribute("isShowBaseCurQuotation", Boolean.TRUE);
        } else {
            model.addAttribute("isShowConvert", ForeignConstant.CYCLE_HIDE_CONVERT);
            model.addAttribute("isShowBaseCurQuotation", Boolean.FALSE);
        }

        // 损益币种为空时回填，保证审批页展示
        fillProfitLossCurForView(vo, fexTranReq);

        if (StringUtils.isNotEmpty(vo.getProfitLossCur()) && !EmptyUtils.isEmpty(vo.getProfitLossMoney())) {
            model.addAttribute("isProfitAndLoss", CommonConstant.YES);
        } else {
            model.addAttribute("isProfitAndLoss", CommonConstant.NO);
        }

        model.addAttribute("fexTranReq",fexTranReq);
        model.addAttribute("vo",vo);
        //交易员
        model.addAttribute("dealer", userService.getByLoginName(vo.getCreateBy()).getUsername());
        model.addAttribute("opPage", "view" + getForwardByOpType(vo.getOpType()));
        return "foreign/foreignLifecycle/approve";
    }

    /**
     * 根据操作类型返回不同的跳转页面
     * @param opType
     * @return
     */
    public String getForwardByOpType(String opType){
        /**操作类型 1-失效，2-录入均价，3-行权，4-敲出，5-敲入，6-平仓，7-放弃行权，8-提前交割，9-提前，10-展期，11-到期交割，12-汇率确认*/
        String forward = "";
        if(ForeignConstant.CYCLE_OP_TYPE_1.equals(opType)){
            forward = "lifecycleInvalid";
        }else if(ForeignConstant.CYCLE_OP_TYPE_2.equals(opType)){
            forward = "lifecycleEnterAvg";
        }else if(ForeignConstant.CYCLE_OP_TYPE_3.equals(opType)){
            forward = "lifecycleExercise";
        }else if(ForeignConstant.CYCLE_OP_TYPE_4.equals(opType)){
            forward = "lifecycleKnockOut";
        }else if(ForeignConstant.CYCLE_OP_TYPE_5.equals(opType)){
            forward = "lifecycleKnockIn";
        }else if(ForeignConstant.CYCLE_OP_TYPE_6.equals(opType)){
            forward = "lifecycleClose";
        }else if(ForeignConstant.CYCLE_OP_TYPE_7.equals(opType)){
            forward = "lifecycleWaive";
        }else if(ForeignConstant.CYCLE_OP_TYPE_8.equals(opType)){
            forward = "lifecycleEarlyDelivery";
        }else if(ForeignConstant.CYCLE_OP_TYPE_9.equals(opType)){
            forward = "lifecycleEarly";
        }else if(ForeignConstant.CYCLE_OP_TYPE_10.equals(opType)){
            forward = "lifecycleDeferred";
        }else if(ForeignConstant.CYCLE_OP_TYPE_11.equals(opType)){
            forward = "lifecycleDelivery";
        }else if(ForeignConstant.CYCLE_OP_TYPE_12.equals(opType)){
            forward = "lifecycleRateConfirm";
        }
        return forward;
    }

    private LocalDate convertToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * 生命周期查看页金额统一保留两位小数，汇率字段维持原有精度
     *
     * @param vo 生命周期信息
     */
    private void formatMoneyForView(FexTranLifeCycleVo vo) {
        vo.setBuyMoney(scaleMoney(vo.getBuyMoney()));
        vo.setRealBuyMoney(scaleMoney(vo.getRealBuyMoney()));
        vo.setSellMoney(scaleMoney(vo.getSellMoney()));
        vo.setRealSellMoney(scaleMoney(vo.getRealSellMoney()));
        vo.setBuyBal(scaleMoney(vo.getBuyBal()));
        vo.setSellBal(scaleMoney(vo.getSellBal()));
        vo.setMoney(scaleMoney(vo.getMoney()));
        vo.setProfitLossMoney(scaleMoney(vo.getProfitLossMoney()));
        vo.setConvertLossMoney(scaleMoney(vo.getConvertLossMoney()));
        vo.setCloseLossMoney(scaleMoney(vo.getCloseLossMoney()));
        vo.setDeliveryLossMoney(scaleMoney(vo.getDeliveryLossMoney()));
        vo.setAllLossMoney(scaleMoney(vo.getAllLossMoney()));
        vo.setPositionMoney(scaleMoney(vo.getPositionMoney()));
        vo.setOpMoney(scaleMoney(vo.getOpMoney()));
    }

    /**
     * 金额按四舍五入保留两位小数
     *
     * @param money 金额
     * @return 两位小数金额
     */
    private BigDecimal scaleMoney(BigDecimal money) {
        return money == null ? null : money.setScale(2, RoundingMode.HALF_UP);
    }
}
