package com.bytter.biz.foreign.controller;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.foreign.entity.FexCashFlow;
import com.bytter.biz.foreign.entity.FexSettleFillRuleAdd;
import com.bytter.biz.foreign.service.FexCashFlowCustInfoService;
import com.bytter.biz.foreign.service.FexCashFlowService;
import com.bytter.biz.foreign.service.FexCotpBankAccService;
import com.bytter.biz.foreign.service.FexSettleFillRuleAddService;
import com.bytter.biz.foreign.service.FexTranReqService;
import com.bytter.biz.foreign.vo.FexCashFlowCustInfoVO;
import com.bytter.biz.foreign.vo.FexCashFlowVO;
import com.bytter.biz.foreign.vo.FexCotpBankAccVO;
import com.bytter.biz.foreign.vo.FexSettleFillRuleVO;
import com.bytter.biz.foreign.vo.FexTranReqVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.biz.payment.api.constant.ForeignConstant;
import com.bytter.biz.payment.api.enums.NetbankMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import com.bytter.core.constant.ForeignMessageEnum;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IBtBankAccService;
import com.bytter.system.api.IBtSwiftInfoService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.ISysCountryCustService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.BtSwiftInfoDTO;
import com.bytter.system.api.dto.SysCountryCustDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Title: Controller
 * @Description: 现金流表
 * @author huangx
 * @date 2020-02-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexcashflow")
public class FexCashFlowController extends BaseController {
    @Autowired
    private FexTranReqService fexTranReqService;
    @Autowired
    private FexCashFlowService fexCashFlowService;
    @Autowired
    private FexCashFlowCustInfoService fexCashFlowCustInfoService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBtBankAccService btBankAccService;
    @Autowired
    private FexSettleFillRuleAddService fexSettleFillRuleAddService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private ISysCountryCustService sysCountryCustService;
    @DubboReference(check = false)
    private IBtSwiftInfoService btSwiftInfoService;
    @Autowired
    private FexCotpBankAccService fexCotpBankAccService;

    /**
     * 外汇现金流表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询现金流列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexCashFlow";
    }

    /**
     * 外汇现金流表 table列表
     *
     * @return
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexCashFlowService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 外汇现金流表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询现金流列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "foreign/listFexCashFlowQuery";
    }

    /**
     * 收款流水认领选择界面
     * @param row
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/choose")
    public String choose(String row,String id,String tranType,Model model) {
        BisAccDtlDTO bisAccDtlDTO = new BisAccDtlDTO();
        if(StringUtils.isNotBlank(id)){
             bisAccDtlDTO = bisAccDtlService.getById(id);
        }
        model.addAttribute("row",row);
        model.addAttribute("bisAccDtlDTO",bisAccDtlDTO);
        model.addAttribute("tranType",tranType);
        return "foreign/listFexCashFlowChoose";
    }

    /**
     * 批量提交
     * @param ids
     * @return
     */
    @LogMessage(content = "批量提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexCashFlow fexCashFlow = fexCashFlowService.get(id, true);
            //现金流【状态】必须为0-暂存或者-1-打回
            if(!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
            }
            //现金流{0}的单位账户不能为空！
            if(StringUtils.isBlank(fexCashFlow.getCorpAccId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_CORPACC_NULL, fexCashFlow.getBillNo());
            }
            if(!ForeignConstant.TRAN_TYPE_1.equals(fexCashFlow.getTranType())) {
                //现金流{0}的交易对手账户不能为空！
                if (StringUtils.isBlank(fexCashFlow.getCotpBankAcc())) {
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_COTPACC_NULL, fexCashFlow.getBillNo());
                }
            }
            //现金流{0}的支付方式不能为空！
            if(StringUtils.isBlank(fexCashFlow.getVoucherTypeId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_VOUCHERTYPE_NULL, fexCashFlow.getBillNo());
            }
        }

        fexCashFlowService.batchSubmit(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 结算退回
     * @param ids
     * @return
     */
    @LogMessage(content = "结算退回", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchBack")
    @ResponseBody
    public AjaxJson batchBack(@RequestParam("ids") String ids) {
        fexCashFlowService.batchBack(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 净额结算
     * @param ids
     * @param model
     * @return
     */
    @GetMapping("/merge/{ids}")
    public String merge(@PathVariable("ids") String ids, Model model) {
        if(mergeCheck(ids)) {
            String[] idAtr = ids.split(CommonConstant.COMMA_SPLIT);
            List<FexCashFlowVO> dtlList = new ArrayList<>();
            FexCashFlowVO entity = new FexCashFlowVO();
            //净额
            BigDecimal payMoney = BigDecimal.ZERO;
            for (String id : idAtr) {
                FexCashFlowVO cashFlow = fexCashFlowService.getByIdUnion(id);
                dtlList.add(cashFlow);
                if(CommonConstant.YES.equals(cashFlow.getCdSign())){
                    payMoney = payMoney.add(cashFlow.getPayMoney().multiply(new BigDecimal(-1)));
                }else {
                    payMoney = payMoney.add(cashFlow.getPayMoney());
                }
                BeanUtils.copyNotNullProperties(cashFlow, entity);
                entity.setSettleType(ForeignConstant.SETTLE_TYPE_2);
            }
            if(payMoney.compareTo(BigDecimal.ZERO)<0){
                entity.setCdSign(CommonConstant.YES);
            }else{
                entity.setCdSign(CommonConstant.NO);
            }
            entity.setPayMoney(payMoney.abs());
            //交易号码、产品、现金流类型为空
            entity.setBillNo(null);
            entity.setTranBillId(null);
            entity.setTranBillNo(null);
            entity.setProId(null);
            entity.setProName(null);
            entity.setCashFlowType(null);
            model.addAttribute("dtlList", dtlList);
            model.addAttribute("fexCashFlowPage", entity);
            model.addAttribute("ids", ids);
        }
        return "foreign/mergeFexCashFlow";
    }

    /**
     * 净额结算-保存
     *
     * @param fexCashFlow
     * @return
     */
    @LogMessage(content = "净额结算", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/mergeSave")
    @ResponseBody
    public AjaxJson mergeSave(FexCashFlowVO fexCashFlow) {
        fexCashFlowService.merge(fexCashFlow.getId(), this.getCurrentUser());
        return success();
    }

    /**
     * 数据校验
     *
     * @param ids
     * @return
     */
    private boolean mergeCheck(String ids) {
        String[] idAtr = ids.split(CommonConstant.COMMA_SPLIT);
        //获取第一笔合并单据
        FexCashFlowVO t1 = fexCashFlowService.getByIdUnion(idAtr[0]);
        //数据校验
        for (String id : idAtr) {
            FexCashFlowVO t = fexCashFlowService.getByIdUnion(id);
            //现金流【状态】必须为0-暂存或者-1-打回
            if(!CommonConstant.STATUE_TEMP.equals(t.getStatus()) && !CommonConstant.STATUE_BACK.equals(t.getStatus())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, t.getBillNo());
            }
            //现金流【结算类型】必须为1-全额
            if(!CommonConstant.STATUE_TEMP.equals(t.getStatus()) && !CommonConstant.STATUE_BACK.equals(t.getStatus())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_SETTLETYPE_1, t.getBillNo());
            }
            if(t.getId().equals(t1.getId())){
                continue;
            }
            //现金流【单位】相同
            if(StringUtils.isNotBlank(t.getCorpId()) && StringUtils.isNotBlank(t1.getCorpId()) && !t.getCorpId().equals(t1.getCorpId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_CORP_SAME);
            }
            //现金流【结算日期】相同
            if(!t.getSettleDate().equals(t1.getSettleDate())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_SETTLEDATE_SAME);
            }
            //现金流【交易对手】相同
            if(StringUtils.isNotBlank(t.getCotpId()) && StringUtils.isNotBlank(t1.getCotpId()) && !t.getCotpId().equals(t1.getCotpId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_COTP_SAME);
            }
            //现金流【币种】相同
            if(StringUtils.isNotBlank(t.getCurId()) && StringUtils.isNotBlank(t1.getCurId()) && !t.getCurId().equals(t1.getCurId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_CUR_SAME);
            }
            //现金流【支付方式】相同
            if(StringUtils.isNotBlank(t.getVoucherTypeId()) && StringUtils.isNotBlank(t1.getVoucherTypeId()) && !t.getVoucherTypeId().equals(t1.getVoucherTypeId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_VOUCHERTYPE_SAME);
            }
            //现金流【主体单位银行账户】相同
            if(StringUtils.isNotBlank(t.getCorpAccId()) && StringUtils.isNotBlank(t1.getCorpAccId()) && !t.getCorpAccId().equals(t1.getCorpAccId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_CORPBANKACC_SAME);
            }
            //现金流【交易对手银行账户】相同
            if(StringUtils.isNotBlank(t.getCotpBankAcc()) && StringUtils.isNotBlank(t1.getCotpBankAcc()) && !t.getCotpBankAcc().equals(t1.getCotpBankAcc())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_COTPBANKACC_SAME);
            }
            //现金流【资金性质】相同
            if(StringUtils.isNotBlank(t.getAccNature()) && StringUtils.isNotBlank(t1.getAccNature()) && !t.getAccNature().equals(t1.getAccNature())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_ACCNATURE_SAME);
            }
            //现金流【交易类型】相同
            if(StringUtils.isNotBlank(t.getTranType()) && StringUtils.isNotBlank(t1.getTranType()) && !t.getTranType().equals(t1.getTranType())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_TRANTYPE_SAME);
            }
            //外汇交易的【保证方式】【保证金账号】【保证金比例】【保证金钞汇标识】【保证金币种】相同（这几项在交易中为必填项时校验）
            //现金流【保证方式】相同
            if(StringUtils.isNotBlank(t.getBoodType()) && StringUtils.isNotBlank(t1.getBoodType()) && !t.getBoodType().equals(t1.getBoodType())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_BOODTYPE_SAME);
            }
            //现金流【保证金账号】相同
            if(StringUtils.isNotBlank(t.getBoodAcc()) && StringUtils.isNotBlank(t1.getBoodAcc()) && !t.getBoodAcc().equals(t1.getBoodAcc())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_BOODACC_SAME);
            }
            //现金流【保证金比例】相同
            if(!EmptyUtils.isEmpty(t.getBoodRatio()) && !EmptyUtils.isEmpty(t1.getBoodRatio()) && t.getBoodRatio().compareTo(t1.getBoodRatio())==0){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_BOODRATIO_SAME);
            }
            //现金流【保证金钞汇标识】相同
            if(StringUtils.isNotBlank(t.getBoodBankNote()) && StringUtils.isNotBlank(t1.getBoodBankNote()) && !t.getBoodBankNote().equals(t1.getBoodBankNote())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_BOODBANKNOTE_SAME);
            }
            //现金流【保证金币种】相同
            if(StringUtils.isNotBlank(t.getBondCurId()) && StringUtils.isNotBlank(t1.getBondCurId()) && !t.getBondCurId().equals(t1.getBondCurId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_BONDCUR_SAME);
            }
            //现金流【汇款类型】相同
            if(StringUtils.isNotBlank(t.getRemitType()) && StringUtils.isNotBlank(t1.getRemitType()) && !t.getRemitType().equals(t1.getRemitType())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_TRANTYPE_SAME);
            }
        }
        return true;
    }

    /**
     * 取消净额结算
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "取消净额结算", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/mergeCancel")
    @ResponseBody
    public AjaxJson mergeCancel(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            FexCashFlow fexCashFlow = fexCashFlowService.get(id, true);
            //现金流【状态】必须为0-暂存或者-1-打回
            if(!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
            }
            //现金流【结算类型】必须为2-净额
            if(!ForeignConstant.SETTLE_TYPE_2.equals(fexCashFlow.getSettleType())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_SETTLETYPE_2, fexCashFlow.getBillNo());
            }
        }
        fexCashFlowService.mergeCancel(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 修改现金流
     *
     * @return
     */
    @GetMapping("/update/{id}/{opType}")
    public String update(@PathVariable("id") String id,@PathVariable("opType") String opType, FexCashFlowVO fexCashFlow, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            fexCashFlow = fexCashFlowService.getByIdUnion(id);
            //现金流【状态】必须为0-暂存或者-1-打回
            if(!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
            }
            //现金流【结算类型】为净额，不允许修改（可以结算处理）
            if(ForeignConstant.SETTLE_TYPE_2.equals(fexCashFlow.getSettleType())
                    && !"0".equals(opType)){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_SETTLETYPE_1, fexCashFlow.getBillNo());
            }
            fexCashFlow.setOpType(opType);

            BtBankAccDTO corpAcc = btBankAccService.getBankAccById(fexCashFlow.getCorpAccId());
            model.addAttribute("corpAcc", corpAcc);
        }
        model.addAttribute("fexCashFlowPage", fexCashFlow);
        if("0".equals(opType)){
            FexTranReqVO fexTranReqVO = new FexTranReqVO();
            if(StringUtils.isNotBlank(fexCashFlow.getTranBillId())){
                fexTranReqVO = fexTranReqService.getByIdUnion(fexCashFlow.getTranBillId());
            }
            model.addAttribute("fexTranReqPage", fexTranReqVO);
            List<FexCashFlowCustInfoVO> list = fexCashFlowCustInfoService.listCustInfoByBusId(fexCashFlow.getId());
            if(EmptyUtils.isEmpty(list)){
                FexSettleFillRuleVO searchVO = new FexSettleFillRuleVO();
                searchVO.setCorpId(fexCashFlow.getCorpId());
                searchVO.setCashFlowMethod(fexCashFlow.getSettleType());
                searchVO.setCotpId(fexCashFlow.getCotpId());
                searchVO.setTranType(fexCashFlow.getTranType());
                searchVO.setAccId(fexCashFlow.getCorpAccId());
                searchVO.setCountryId(fexTranReqVO.getCountryId());
                List<FexSettleFillRuleAdd> custFields = fexSettleFillRuleAddService.listRuleAddByRule(searchVO);
                model.addAttribute("custFields", custFields);
            }else {
                model.addAttribute("custFields", list);
            }
            model.addAttribute("corp", corpService.get(fexCashFlow.getCorpId()));
            model.addAttribute("corpBankAcc", btBankAccService.getBankAccById(fexCashFlow.getCorpAccId()));
        }
        if("3".equals(opType)){
            if (StringUtils.isNotBlank(fexCashFlow.getCotpAccId())) {
                FexCotpBankAccVO cotpBankAcc = fexCotpBankAccService.getFexFexCotpBankAcc(fexCashFlow.getCotpAccId());
                model.addAttribute("cotpBankAcc", cotpBankAcc);
            }
        }
        return "foreign/updateFexCashFlow" + this.getForwardByOpType(opType, fexCashFlow.getCdSign());
    }

    /**
     * 根据操作类型返回不同的跳转页面
     * @param opType
     * @return
     */
    public String getForwardByOpType(String opType, String cdSign){
        /**操作类型 0-结算处理、1-修改支付方式、2-修改单位账户、3-交易对手账户、4-更新支付状态*/
        String forward = "";
        if("0".equals(opType)){
            if(CommonConstant.YES.equals(cdSign)){
                //付款
                forward = "Pay";
            }else{
                //收款
                forward = "Gather";
            }
        }else if("1".equals(opType)){
            //修改支付方式
            forward = "VoucherType";
        }else if("2".equals(opType)){
            //修改单位账户
            forward = "CorpAcc";
        }else if("3".equals(opType)){
            //交易对手账户
            forward = "CotpAcc";
        }else if("4".equals(opType)){
            //更新支付状态
            forward = "VoucherStat";
        }
        return forward;
    }

    /**
     * 修改现金流
     * @param fexCashFlow
     * @return
     */
    @LogMessage(content = "修改现金流", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexCashFlowVO fexCashFlow) {
        FexCashFlow entity = fexCashFlowService.get(fexCashFlow.getId(), true);
        //现金流【状态】必须为0-暂存或者-1-打回
        if(!CommonConstant.STATUE_TEMP.toString().equals(entity.getStatus().toString())
                && !CommonConstant.STATUE_BACK.toString().equals(entity.getStatus().toString())){
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getBillNo());
        }
        //结算处理-提交时，增加校验
        if("0".equals(fexCashFlow.getOpType())
                && CommonConstant.STATUE_WAIT_CHECK.equals(entity.getStatus())){
            //现金流【状态】必须为0-暂存或者-1-打回
            if(!CommonConstant.STATUE_TEMP.equals(fexCashFlow.getStatus()) && !CommonConstant.STATUE_BACK.equals(fexCashFlow.getStatus())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_STATUS_CHANGE, fexCashFlow.getBillNo());
            }
            //现金流{0}的单位账户不能为空！
            if(StringUtils.isBlank(fexCashFlow.getCorpAccId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_CORPACC_NULL, fexCashFlow.getBillNo());
            }
            if(CommonConstant.YES.equals(fexCashFlow.getCdSign()) && !ForeignConstant.TRAN_TYPE_1.equals(fexCashFlow.getTranType())) {
                //现金流{0}的交易对手账户不能为空！
                if (StringUtils.isBlank(fexCashFlow.getCotpBankAcc())) {
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_COTPACC_NULL, fexCashFlow.getBillNo());
                }
            }
            //现金流{0}的支付方式不能为空！
            if(StringUtils.isBlank(fexCashFlow.getVoucherTypeId())){
                throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_VOUCHERTYPE_NULL, fexCashFlow.getBillNo());
            }
            if(CommonConstant.NO.equals(fexCashFlow.getCdSign()) && !ForeignConstant.TRAN_TYPE_1.equals(fexCashFlow.getTranType())){
                //现金流{0}的收付方向为收款，实际收款金额不能为空！
                if(EmptyUtils.isEmpty(fexCashFlow.getFactsMoney())){
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_FACTSMONEY_NULL, fexCashFlow.getBillNo());
                }
                //现金流{0}的收付方向为收款，实际收款日期不能为空！
                if(EmptyUtils.isEmpty(fexCashFlow.getFactsDate())){
                    throw new ParameterException(ForeignMessageEnum.FEX_CASH_FLOW_MESSAGE_FACTSDATE_NULL, fexCashFlow.getBillNo());
                }
            }
        }
        fexCashFlowService.updateFexCashFlow(fexCashFlow, this.getCurrentUser());
        return success();
    }

    /**
     * 外汇买卖交易现金流统计
     * @return
     */
    @GetMapping("/queryForeignReport")
    public String queryForeignReport(Model model){
        return "foreign/listFexCashFlowForeignReport";
    }

    /**
     * 跳转至审核列表
     * @return
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model){
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "foreign/listFexCashFlowApprove";
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
        return this.fexCashFlowService.dataTableApprove(dataTablesInput);
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
        FexCashFlowVO vo = new FexCashFlowVO();
        if (StringUtils.isNotEmpty(id)) {
            vo = this.fexCashFlowService.getByIdUnion(id);
            model.addAttribute("fexCashFlowPage", vo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW);
            FexTranReqVO fexTranReq = new FexTranReqVO();
            if(StringUtils.isNotBlank(vo.getTranBillId())){
                fexTranReq = fexTranReqService.getByIdUnion(vo.getTranBillId());
            }
            model.addAttribute("fexTranReqPage", fexTranReq);
            List<FexCashFlowCustInfoVO> list = fexCashFlowCustInfoService.listCustInfoByBusId(vo.getId());
            if(EmptyUtils.isEmpty(list)){
                FexSettleFillRuleVO searchVO = new FexSettleFillRuleVO();
                searchVO.setCorpId(vo.getCorpId());
                searchVO.setCashFlowMethod(vo.getSettleType());
                searchVO.setCotpId(vo.getCotpId());
                searchVO.setTranType(vo.getTranType());
                searchVO.setAccId(vo.getCorpAccId());
                searchVO.setCountryId(fexTranReq.getCountryId());
                List<FexSettleFillRuleAdd> custFields = fexSettleFillRuleAddService.listRuleAddByRule(searchVO);
                model.addAttribute("custFields", custFields);
            }else {
                model.addAttribute("custFields", list);
            }
            model.addAttribute("corp", corpService.get(vo.getCorpId()));
            model.addAttribute("corpBankAcc", btBankAccService.getBankAccById(vo.getCorpAccId()));
        }
        return "foreign/approveFexCashFlow";
    }

    /**
     * 跳转至手机审核详情界面
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            FexCashFlowVO vo = this.fexCashFlowService.getByIdUnion(businessId);
            model.addAttribute("fexCashFlowPage", vo);
            model.addAttribute("taskId", taskId);
        }
        return "foreign/viewApproveFexCashFlow";
    }

    /**
     * 外汇买卖交易现金流统计
     *
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/dataTableForeignReport")
    @ResponseBody
    public DataTablesOutput dataTableForeignReport(@RequestBody DataTablesInput dataTablesInput){
        return fexCashFlowService.getDataTablesForeignReport(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 外汇买卖交易现金流 详情页
     *
     * @return
     */
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexCashFlowVO vo = new FexCashFlowVO();
        String foward = "Pay";
        if (StringUtils.isNotEmpty(id)) {
            vo = this.fexCashFlowService.getByIdUnion(id);
            model.addAttribute("fexCashFlowPage", vo);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW);
            FexTranReqVO fexTranReq = new FexTranReqVO();
            if(vo!=null && StringUtils.isNotBlank(vo.getTranBillId())){
                fexTranReq = fexTranReqService.getByIdUnion(vo.getTranBillId());
            }
            model.addAttribute("fexTranReqPage", fexTranReq);
            List<FexCashFlowCustInfoVO> list = fexCashFlowCustInfoService.listCustInfoByBusId(vo.getId());
            if(EmptyUtils.isEmpty(list)){
                FexSettleFillRuleVO searchVO = new FexSettleFillRuleVO();
                searchVO.setCorpId(vo.getCorpId());
                searchVO.setCashFlowMethod(vo.getSettleType());
                searchVO.setCotpId(vo.getCotpId());
                searchVO.setTranType(vo.getTranType());
                searchVO.setAccId(vo.getCorpAccId());
                searchVO.setCountryId(fexTranReq.getCountryId());
                List<FexSettleFillRuleAdd> custFields = fexSettleFillRuleAddService.listRuleAddByRule(searchVO);
                model.addAttribute("custFields", custFields);
            }else {
                model.addAttribute("custFields", list);
            }
            model.addAttribute("corp", corpService.get(vo.getCorpId()));
            model.addAttribute("corpBankAcc", btBankAccService.getBankAccById(vo.getCorpAccId()));
            if(CommonConstant.NO.equals(vo.getCdSign())){
                foward = "Gather";
            }
        }
        return "foreign/viewFexCashFlow"+foward;
    }

    /**
     * 外汇买卖交易现金流 详情页
     *
     * @return
     */
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewMerge/{id}")
    public String viewMerge(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            FexCashFlowVO fexCashFlow = fexCashFlowService.getByIdUnion(id);
            //获取合并明细
            List<FexCashFlowVO> dtlList = fexCashFlowService.listBySettleNoUnion(fexCashFlow.getSettleNo());
            model.addAttribute("fexCashFlowPage", fexCashFlow);
            model.addAttribute("dtlList", dtlList);
        }
        return "foreign/viewMergeFexCashFlow";
    }

    /**
     * 获取主体单位银行账号
     *
     * @param curId
     * @param bankType
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getCorpBankAcc")
    @ResponseBody
    public List<BtBankAccDTO> getCorpBankAcc(String corpId, String curId, String bankType) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        List<String> curList = new ArrayList<>();
        curList.add(curId);
        if (!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(curId)) {
            //获取转账权限的银行账号
            List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTOByMultiVal(this.getUserId(), corpId,
                    ResourceConstant.FOREIGN_FEXTRANREQ_ALLOCATION_APPLY, curList, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);
            if(StringUtils.isNotBlank(bankType)){
                for(BtBankAccDTO bankAcc:listBankAccTemp){
                    //根据银行类别过滤
                    if(bankType.equals(bankAcc.getBankTypeId()) && curId.equals(bankAcc.getCurId())){
                        listAcc.add(bankAcc);
                    }
                }
            }else{
                for(BtBankAccDTO bankAcc:listBankAccTemp){
                    if(curId.equals(bankAcc.getCurId())){
                        listAcc.add(bankAcc);
                    }
                }
            }
        }
        return listAcc;
    }

    /**
     *
     * 模板四要素获取主体单位银行账号
     *
     * @param corpId
     * @param curId
     * @param corpAccBankTypeId 银行类型
     * @param corpAccCountryId 国家
     * @param bifCode 接口代码
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getCorpBankAccByTemplate")
    @ResponseBody
    public List<BtBankAccDTO> getCorpBankAcc(String corpId, String curId, String corpAccBankTypeId, String corpAccCountryId, String bifCode) {
        List<BtBankAccDTO> listAcc = new ArrayList<>();
        List<String> curList = new ArrayList<>();
        curList.add(curId);
        if (!StringUtils.isEmpty(corpId) && !StringUtils.isEmpty(curId)) {
            //获取转账权限的银行账号
            List<BtBankAccDTO> listBankAccTemp = accountGrantService.listDTOByMultiVal(this.getUserId(), corpId,
                    ResourceConstant.FOREIGN_FEXTRANREQ_ALLOCATION_APPLY, curList, "2,3", AccountGrantConstant.TRANSFER_ACCOUNT);

            for (BtBankAccDTO bankAccDTO : listBankAccTemp){
                if (CommonConstant.YES.equals(bankAccDTO.getIsDomesticBank())){
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getByNameZhLevelTop("中国");
                    if (sysCountryCustDTO != null) {
                        bankAccDTO.setCountryId(sysCountryCustDTO.getId());
                    }
                } else if (CommonConstant.NO.equals(bankAccDTO.getIsDomesticBank()) && StringUtils.isNotBlank(bankAccDTO.getProv())) { //境外银行获取国家地区名称
                    SysCountryCustDTO sysCountryCustDTO = sysCountryCustService.getById(bankAccDTO.getProv());
                    if (sysCountryCustDTO != null) {
                        bankAccDTO.setCountryId(sysCountryCustDTO.getId());
                    }
                } else if (CommonConstant.NO.equals(bankAccDTO.getIsDomesticBank())&& StringUtils.isNotBlank(bankAccDTO.getBankCode())) {
                    BtSwiftInfoDTO btSwiftInfoDTO = btSwiftInfoService.getByCode(bankAccDTO.getBankCode());
                    if (btSwiftInfoDTO != null) {
                        bankAccDTO.setCountryId(btSwiftInfoDTO.getCountryCode());
                    }
                }
            }

            listAcc = listBankAccTemp.stream()
                    .filter(acc -> curId.equals(acc.getCurId()))
                    .filter(acc -> corpAccBankTypeId.equals(acc.getBankTypeId())
                            && Objects.equals(corpAccCountryId, acc.getCountryId())
                            && Objects.equals(bifCode, acc.getBifCode()))
                    .collect(Collectors.toList());
        }
        return listAcc;
    }

    /**
     * 外汇买卖交易现金流 详情页
     *
     * @return
     */
    @LogMessage(content = "查询外汇交易表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{billNo}")
    public String viewByBillNo(@PathVariable("billNo") String billNo, Model model) {
        FexCashFlowVO vo = new FexCashFlowVO();
        String foward = "Pay";
        if (StringUtils.isNotEmpty(billNo)) {
            vo = this.fexCashFlowService.getByBillNoUnion(billNo);
            model.addAttribute("fexCashFlowPage", vo);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_FEX_CASH_FLOW);
            FexTranReqVO fexTranReq = new FexTranReqVO();
            if(vo!=null && StringUtils.isNotBlank(vo.getTranBillId())){
                fexTranReq = fexTranReqService.getByIdUnion(vo.getTranBillId());
            }
            model.addAttribute("fexTranReqPage", fexTranReq);
            List<FexCashFlowCustInfoVO> list = fexCashFlowCustInfoService.listCustInfoByBusId(vo.getId());
            if(EmptyUtils.isEmpty(list)){
                FexSettleFillRuleVO searchVO = new FexSettleFillRuleVO();
                searchVO.setCorpId(vo.getCorpId());
                searchVO.setCashFlowMethod(vo.getSettleType());
                searchVO.setCotpId(vo.getCotpId());
                searchVO.setTranType(vo.getTranType());
                searchVO.setAccId(vo.getCorpAccId());
                searchVO.setCountryId(fexTranReq.getCountryId());
                List<FexSettleFillRuleAdd> custFields = fexSettleFillRuleAddService.listRuleAddByRule(searchVO);
                model.addAttribute("custFields", custFields);
            }else {
                model.addAttribute("custFields", list);
            }
            model.addAttribute("corp", corpService.get(vo.getCorpId()));
            model.addAttribute("corpBankAcc", btBankAccService.getBankAccById(vo.getCorpAccId()));
            if(CommonConstant.NO.equals(vo.getCdSign())){
                foward = "Gather";
            }
        }
        return "foreign/viewFexCashFlow"+foward;
    }

    /**
     * 外汇现金流表 table列表
     *
     * @return
     */
    @PostMapping("/getDataTablesUnionForRefundCheck")
    @ResponseBody
    public DataTablesOutput getDataTablesUnionForRefundCheck(@RequestBody DataTablesInput dataTablesInput){
        return fexCashFlowService.getDataTablesUnionForRefundCheck(dataTablesInput);
    }
}
