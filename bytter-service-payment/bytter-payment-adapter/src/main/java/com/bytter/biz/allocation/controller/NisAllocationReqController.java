package com.bytter.biz.allocation.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.entity.NisAllocationReq;
import com.bytter.biz.allocation.service.NisAllocationReqService;
import com.bytter.biz.bdlink.api.constant.AllocationConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.AllocationMessageEnum;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.finacial.api.dto.FcOnlineLedgerDTO;
import com.bytter.biz.finacial.api.service.IFcOnlineLedgerService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.dto.NisAllocationReqListDTO;
import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;


/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: NIS_ALLOCATION_REQ
 * @date 2017-05-31 10:25:52
 * 资金调拨
 */
@Controller
@RequestMapping("/allocation/nisallocationreq")
@Slf4j
public class NisAllocationReqController extends BaseController {
    @Autowired
    private NisAllocationReqService nisAllocationReqService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBtBankAccService iBtBankAccService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IFcAccService fcAccService;
    @DubboReference(check = false)
    private IBisAccBalService iBisAccBalService;
    @DubboReference(check = false)
    private IFcOnlineLedgerService fcOnlineLedgerService;

    /**
     * NIS_ALLOCATION_REQ列表 页面跳转
     *
     * @return String
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_FUND_QUERY},
//			logical = Logical.OR)
    @LogMessage(content = "查询NIS_ALLOCATION_REQ列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list/{type}")
    public String list(@PathVariable("type") Integer type, Model model) {
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //银行账户余额不足
        model.addAttribute("bankaccOver", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER).getValue());
        //银行账户余额不足
        model.addAttribute("accountOver", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ACCOUNTOVER).getValue());
        String page = "allocation/list" + getForwardByType(type);
        return page;
    }

    /***
     * 需求清单查询连接过来的。只有头寸的查询
     * @param erpId
     * @param model
     * @return TODO
     */
    @LogMessage(content = "查询NIS_ALLOCATION_REQ列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/demandList")
    public String list(String erpId, Model model) {
        model.addAttribute("erpId", erpId);
        model.addAttribute("billFrom", "5");//5-资金需求清单
        model.addAttribute("queryType", "13");//5-头寸调拨
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //银行账户余额不足
        model.addAttribute("bankaccOver", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER).getValue());
        //银行账户余额不足
        model.addAttribute("accountOver", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ACCOUNTOVER).getValue());
        String page = "allocation/list" + getForwardByType(15);
        return page;
    }

    /**
     * NIS_ALLOCATION_REQ table列表
     *
     * @return DataTablesOutput
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_QUERY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_FUND_QUERY},
//			logical = Logical.OR)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return nisAllocationReqService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * NIS_ALLOCATION_REQ新增页面跳转
     *
     * @return String
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_APPLY},
//			logical = Logical.OR)
    @GetMapping("/add/{type}")
    public String add(@PathVariable("type") Integer type, NisAllocationReq nisAllocationReq, Model model) {
        if (StringUtils.isNotEmpty(nisAllocationReq.getId())) {
            nisAllocationReq = nisAllocationReqService.get(nisAllocationReq.getId());
            model.addAttribute("nisAllocationReqPage", nisAllocationReq);
        }
        model.addAttribute("createBy", this.getUserLoginName());
        model.addAttribute("createDate", DateUtils.getDate());
        this.setBudgetAttr(nisAllocationReq.getType(), model);
        String page = "allocation/add" + getForwardByType(type);
        return page;
    }

    /**
     * NIS_ALLOCATION_REQ编辑页面跳转
     *
     * @return String
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_APPLY},
//			logical = Logical.OR)
    @GetMapping("/update")
    public String update(NisAllocationReq nisAllocationReq, Model model) {
        if (StringUtils.isNotEmpty(nisAllocationReq.getId())) {
            nisAllocationReq = nisAllocationReqService.get(nisAllocationReq.getId(), true);
            if (StringUtils.isNotEmpty(nisAllocationReq.getStatus()) && AllocationConstant.STATUE_DEL.equals(nisAllocationReq.getStatus())) {
                throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_STATUS_DELETE);
            }
            BtCurrencyDTO currencyDTO = this.currencyService.getObjectById(nisAllocationReq.getCurId());
            if (!EmptyUtils.isEmpty(currencyDTO)) {
                model.addAttribute("curName", currencyDTO.getCurName());
            }
            if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())) {
                model.addAttribute("payeeCorpDTO", iBtBankAccService.getBankAccByBankAccAndCurId(nisAllocationReq.getPayeeAcc(), nisAllocationReq.getCurId()));
            }
            if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
                model.addAttribute("corp", iCorpService.get(nisAllocationReq.getCorpId()));
            }
            //收款方内部账户信息
            NisAllocationReqListDTO nisAllocationReqListDTO = new NisAllocationReqListDTO();
            BeanUtils.copyNotNullProperties(nisAllocationReq, nisAllocationReqListDTO);
            this.setPayeeCounterAcc(nisAllocationReqListDTO, model);
            model.addAttribute("nisAllocationReqPage", nisAllocationReqListDTO);
            if(nisAllocationReq.getType() == 14){
                com.bytter.biz.bdlink.api.dto.BisAccBalDTO bisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReq.getPayerAcc(),nisAllocationReq.getCurId(),null,true);
                BigDecimal bal = BigDecimal.ZERO;
                if (!EmptyUtils.isEmpty(bisAccBalDTO)) {
                    if(!EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal())){
                        bal =  bisAccBalDTO.getAvailBal();
                    }
                }
                model.addAttribute("bal", bal);
            }

            if(nisAllocationReq.getType() == 12){
                com.bytter.biz.bdlink.api.dto.BisAccBalDTO payeeBisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReq.getPayeeAcc(),nisAllocationReq.getCurId(),null,true);
                BigDecimal payeeBal = BigDecimal.ZERO;
                if (!EmptyUtils.isEmpty(payeeBisAccBalDTO)) {
                    if(!EmptyUtils.isEmpty(payeeBisAccBalDTO.getBal())) {
                        payeeBal = payeeBisAccBalDTO.getBal();
                    }
                }
                model.addAttribute("bal", payeeBal);
            }
        }
        this.setBudgetAttr(nisAllocationReq.getType(), model);
        String page = "allocation/update" + getForwardByType(nisAllocationReq.getType());
        return page;
    }

    /**
     * 设置收款方中心账户信息
     *
     * @param nisAllocationReq
     * @param model
     */
    public void setPayeeCounterAcc(NisAllocationReqListDTO nisAllocationReq, Model model) {
        if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReq.getType())
                || AllocationConstant.ALLOCATION_TYPE_ASK.equals(nisAllocationReq.getType())
                    ||AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReq.getType())) {
            com.bytter.system.api.vo.FcAccBalanceQueryVO payeeCounterAcc = null;
            if (StringUtils.isBlank(nisAllocationReq.getPayeeCounterAcc())) {
                BtBankAccDTO payeeAcc = iBtBankAccService.getBankAccByIdAndCurId(nisAllocationReq.getPayeeAccId(), nisAllocationReq.getCurId());
                //考虑历史数据，根据父子关系做查询
                if (payeeAcc != null) {
                    nisAllocationReq.setPayeeCounterAcc(payeeAcc.getCounterAccId());
                }
            }
            if (StringUtils.isNotBlank(nisAllocationReq.getPayeeCounterAcc())) {
                payeeCounterAcc = fcAccService.getFcAccBalByAccount(nisAllocationReq.getPayeeCounterAcc());
                Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(Lists.newArrayList(payeeCounterAcc.getCurId()));
                payeeCounterAcc.setCurName(currencyNameMap.get(payeeCounterAcc.getCurId()));
                payeeCounterAcc.setOverdraftMoney(payeeCounterAcc.getOverdraftMoney() == null ? BigDecimal.ZERO : payeeCounterAcc.getOverdraftMoney());
                FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(nisAllocationReq.getPayeeCounterAcc(),payeeCounterAcc.getPoolSign());
                if (Objects.nonNull(fcOnlineLedgerDTO)) {
                    payeeCounterAcc.setCheckBalance(fcOnlineLedgerDTO.getCheckBalance());
                    payeeCounterAcc.setOnlineBalance(fcOnlineLedgerDTO.getOnlineBalance());
                    payeeCounterAcc.setAvailableBalance(fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney()));
                }
            } else {
                payeeCounterAcc = new com.bytter.system.api.vo.FcAccBalanceQueryVO();
                payeeCounterAcc.setAccount("--");
            }
            model.addAttribute("payeeCounterAcc", payeeCounterAcc);
        }
    }
    public void setPayerCounterAcc(NisAllocationReqListDTO nisAllocationReq, Model model) {
        com.bytter.system.api.vo.FcAccBalanceQueryVO payerCounterAcc = null;
        if (StringUtils.isNotBlank(nisAllocationReq.getPayerCounterAcc())) {
            payerCounterAcc = fcAccService.getFcAccBalByAccount(nisAllocationReq.getPayerCounterAcc());
            Map<String, String> currencyNameMap = currencyService.getCurrencyNameByIds(Lists.newArrayList(payerCounterAcc.getCurId()));
            payerCounterAcc.setCurName(currencyNameMap.get(payerCounterAcc.getCurId()));
            payerCounterAcc.setOverdraftMoney(payerCounterAcc.getOverdraftMoney() == null ? BigDecimal.ZERO : payerCounterAcc.getOverdraftMoney());
            FcOnlineLedgerDTO fcOnlineLedgerDTO = fcOnlineLedgerService.getByBlnctrAcc(nisAllocationReq.getPayerCounterAcc(),payerCounterAcc.getPoolSign());
            if (Objects.nonNull(fcOnlineLedgerDTO)) {
                payerCounterAcc.setCheckBalance(fcOnlineLedgerDTO.getCheckBalance());
                payerCounterAcc.setOnlineBalance(fcOnlineLedgerDTO.getOnlineBalance());
                payerCounterAcc.setAvailableBalance(fcOnlineLedgerDTO.getCheckBalance().subtract(fcOnlineLedgerDTO.getFreezeMoney()));
            }
        } else {
            payerCounterAcc = new com.bytter.system.api.vo.FcAccBalanceQueryVO();
            payerCounterAcc.setAccount("--");
        }
        model.addAttribute("payerCounterAcc", payerCounterAcc);
    }

    /**
     * NIS_ALLOCATION_REQ详情页
     *
     * @return String
     */
    @LogMessage(content = "查询NIS_ALLOCATION_REQ详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        NisAllocationReqListDTO nisAllocationReq = nisAllocationReqService.getVOAllByUnion(id);
        //付款方内部账户信息
        com.bytter.system.api.vo.FcAccBalanceQueryVO payerCounterAcc = new com.bytter.system.api.vo.FcAccBalanceQueryVO();
        payerCounterAcc.setOnlineBalance(nisAllocationReq.getPayerAccBalance());
        model.addAttribute("payerCounterAcc", payerCounterAcc);
        //收款方内部账户信息
        this.setPayeeCounterAcc(nisAllocationReq, model);//这里把银行账号余额设置为内部账号余额有问题
        model.addAttribute("nisAllocationReqPage", nisAllocationReq);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_ALLOCATION);
        this.setBudgetAttr(nisAllocationReq.getType(), model);
        if(nisAllocationReq.getType() == 12){
            com.bytter.biz.bdlink.api.dto.BisAccBalDTO bisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReq.getPayerAcc(),nisAllocationReq.getCurId(),null,true);
            BigDecimal bal = BigDecimal.ZERO;
            BigDecimal availbal = BigDecimal.ZERO;
            if (!EmptyUtils.isEmpty(bisAccBalDTO)) {
                if(!EmptyUtils.isEmpty(bisAccBalDTO.getBal())) {
                    bal = bisAccBalDTO.getBal();
                }
                if(!EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal())){
                    availbal = bisAccBalDTO.getAvailBal();
                }
            }
            com.bytter.biz.bdlink.api.dto.BisAccBalDTO payeeBisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReq.getPayeeAcc(),nisAllocationReq.getCurId(),null,true);
            BigDecimal payeeBal = BigDecimal.ZERO;
            BigDecimal payeeAvailbal = BigDecimal.ZERO;
            if (!EmptyUtils.isEmpty(payeeBisAccBalDTO)) {
                if(!EmptyUtils.isEmpty(payeeBisAccBalDTO.getBal())) {
                    payeeBal = payeeBisAccBalDTO.getBal();
                }
                if(!EmptyUtils.isEmpty(payeeBisAccBalDTO.getAvailBal())){
                    payeeAvailbal = payeeBisAccBalDTO.getAvailBal();
                }
            }
            model.addAttribute("bal", bal);
            model.addAttribute("availbal", availbal);
            model.addAttribute("payeeBal", payeeBal);
            model.addAttribute("payeeAvailbal", payeeAvailbal);
        }
        return "allocation/view" + getForwardByType(nisAllocationReq.getType());
    }

    /**
     * NIS_ALLOCATION_REQ详情页
     *
     * @return String
     */
    @LogMessage(content = "查询NIS_ALLOCATION_REQ详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{billNo}")
    public String viewByBillNo(@PathVariable("billNo") String billNo, Model model) {
        NisAllocationReqListDTO nisAllocationReq = nisAllocationReqService.getNisAllocationReqByBillNo(billNo);
//        this.setPayerCounterAcc(nisAllocationReq, model);
        nisAllocationReq.setPayerAccBalance(iBisAccBalService.getLastByBalDate(nisAllocationReq.getPayerAcc(), nisAllocationReq.getCurId()));
        nisAllocationReq.setPayeeAccBalance(iBisAccBalService.getLastByBalDate(nisAllocationReq.getPayeeAcc(), nisAllocationReq.getCurId()));
        //收款方内部账户信息
        this.setPayeeCounterAcc(nisAllocationReq, model);
        model.addAttribute("nisAllocationReqPage", nisAllocationReq);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_ALLOCATION);
        this.setBudgetAttr(nisAllocationReq.getType(), model);
        return "allocation/view" + getForwardByType(nisAllocationReq.getType());
    }

    /**
     * 设置预算相关参数
     *
     * @param model
     */
    public void setBudgetAttr(Integer type, Model model) {
        model.addAttribute("bankaccOver", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER).getValue());
        model.addAttribute("accountOver", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ACCOUNTOVER).getValue());
        model.addAttribute("isAccount", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue());
        model.addAttribute("isAutoAccount", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue());
        //头寸调拨是否记账
        model.addAttribute("isAccountPos", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue());
        // 请款和下拨挂接资金计划
        if (AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == type.intValue()
                || AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == type.intValue()) {
            model.addAttribute("budgetSel", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BUDGETSEL).getValue());
            model.addAttribute("downItem", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_DOWNITEM).getValue());
            model.addAttribute("askItem", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ASKITEM).getValue());
            model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_ALLOCATION);
            model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        }
        //是否启用限额管理  1-是 0-否
        model.addAttribute("isOpenLimit", configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
    }

    /**
     * 添加NIS_ALLOCATION_REQ
     * 【资金调拨】--保存
     * 【头寸调拨】--提交
     * @param nisAllocationReqListDTO
     * @return AjaxJson
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_APPLY},
//			logical = Logical.OR)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(NisAllocationReqListDTO nisAllocationReqListDTO) {
        return success(nisAllocationReqService.saveNisAllocationReq(nisAllocationReqListDTO, this.getCurrentUser()));
    }


    /**
     * 保存上划和下拨数据
     * 【资金调拨】
     * @param nisAllocationReqListDTO
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY},
//			logical = Logical.OR)
    @PostMapping("/saveUpAndDown")
    @ResponseBody
    public AjaxJson saveUpAndDown(NisAllocationReqListDTO nisAllocationReqListDTO) {
        return success(nisAllocationReqService.saveUpAndDown(nisAllocationReqListDTO, this.getCurrentUser()));
    }

    /**
     * 修改NIS_ALLOCATION_REQ
     *
     * @param nisAllocationReq
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_APPLY},
//			logical = Logical.OR)
    @LogMessage(content = "修改NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(NisAllocationReqListDTO nisAllocationReq) {
        nisAllocationReqService.updateNisAllocationReq(nisAllocationReq, this.getCurrentUser());
        return success();
    }

    /**
     * 修改NIS_ALLOCATION_REQ
     *
     * @param nisAllocationReqListDTO
     * @return
     */
    //////@RequiresPermissions(value = {
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,},
//			logical = Logical.OR)
    @LogMessage(content = "修改NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/updateAskAndDown")
    @ResponseBody
    public AjaxJson updateAskAndDown(NisAllocationReqListDTO nisAllocationReqListDTO) {
        nisAllocationReqService.updateAskAndDown(nisAllocationReqListDTO, this.getCurrentUser());
        return success();
    }

    /**
     * 删除NIS_ALLOCATION_REQ
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_APPLY},
//			logical = Logical.OR)
    @LogMessage(content = "删除NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        NisAllocationReq nisAllocationReq = nisAllocationReqService.get(id, true);
        //暂存和编辑状态才可以提交
        if (nisAllocationReq != null && !NetbankConstant.STATUE_TEMP.equals(nisAllocationReq.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisAllocationReq.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisAllocationReq.getBillNo());
        }
        // 逻辑删除
        nisAllocationReqService.deleteNisAllocationReq(nisAllocationReq, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除NIS_ALLOCATION_REQ
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_APPLY},
//			logical = Logical.OR)
    @LogMessage(content = "批量删除NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisAllocationReq nisAllocationReq = nisAllocationReqService.get(id, true);
            //暂存和编辑状态才可以提交
            if (nisAllocationReq != null && !NetbankConstant.STATUE_TEMP.equals(nisAllocationReq.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisAllocationReq.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisAllocationReq.getBillNo());
            }
            nisAllocationReqService.deleteNisAllocationReq(nisAllocationReq, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 根据类型返回不同的跳转页面
     *
     * @param type
     * @return String
     */
    private String getForwardByType(Integer type) {
        /**资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴 15-查询*/
        String forward = "NisFundAllocationReq";
        if (AllocationConstant.ALLOCATION_TYPE_UP.equals(type)) {
            forward = "NisUpAllocationReq";
        } else if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(type)) {
            forward = "NisDownAllocationReq";
        } else if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(type)) {
            forward = "NisAskAllocationReq";
        } else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(type)) {
            forward = "NisPosAllocationReq";
        } else if (AllocationConstant.ALLOCATION_TYPE_TURN.equals(type)) {
            forward = "NisTurnAllocationReq";
        }
        return forward;
    }

    /**
     * 跳转至审核列表
     *
     * @param type 业务类型
     * @return String
     */
    @GetMapping("/listApprove/{type}")
    public String listApprove(@PathVariable Integer type, String flowStatus, String cycleTime, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "allocation/list" + this.getForwardByType(type) + "Approve";
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return DataTablesOutput
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.nisAllocationReqService.getDataTablesApprove(dataTablesInput);
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
        model.addAttribute("taskId", taskId);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_ALLOCATION);
        if (StringUtils.isNotEmpty(id)) {
            NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqService.getVOAllByUnion(id);
            //收款方内部账户信息
            this.setPayeeCounterAcc(nisAllocationReqListDTO, model);
            model.addAttribute("nisAllocationReqPage", nisAllocationReqListDTO);
            this.setBudgetAttr(nisAllocationReqListDTO.getType(), model);
            model.addAttribute("askAllowEditMoney", configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ASKALLOWEDITMONEY).getValue());
            if(nisAllocationReqListDTO.getType() == 12){
                com.bytter.biz.bdlink.api.dto.BisAccBalDTO bisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReqListDTO.getPayerAcc(),nisAllocationReqListDTO.getCurId(),null,true);
                BigDecimal bal = BigDecimal.ZERO;
                BigDecimal availbal = BigDecimal.ZERO;
                if (!EmptyUtils.isEmpty(bisAccBalDTO)) {
                    if(!EmptyUtils.isEmpty(bisAccBalDTO.getBal())) {
                        bal = bisAccBalDTO.getBal();
                    }
                    if(!EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal())){
                        availbal = bisAccBalDTO.getAvailBal();
                    }
                }
                com.bytter.biz.bdlink.api.dto.BisAccBalDTO payeeBisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReqListDTO.getPayeeAcc(),nisAllocationReqListDTO.getCurId(),null,true);
                BigDecimal payeeBal = BigDecimal.ZERO;
                BigDecimal payeeAvailbal = BigDecimal.ZERO;
                if (!EmptyUtils.isEmpty(payeeBisAccBalDTO)) {
                    if(!EmptyUtils.isEmpty(payeeBisAccBalDTO.getBal())) {
                        payeeBal = payeeBisAccBalDTO.getBal();
                    }
                    if(!EmptyUtils.isEmpty(payeeBisAccBalDTO.getAvailBal())){
                        payeeAvailbal = payeeBisAccBalDTO.getAvailBal();
                    }
                }
                model.addAttribute("bal", bal);
                model.addAttribute("availbal", availbal);
                model.addAttribute("payeeBal", payeeBal);
                model.addAttribute("payeeAvailbal", payeeAvailbal);
            }
        }
        return "allocation/approveNisAllocationReq";
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
        model.addAttribute("taskId", taskId);
        model.addAttribute("busType", NisMessageConstant.BUS_TYPE_ALLOCATION);
        if (!StringUtils.isBlank(businessId)) {
            NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqService.getVOAllByUnion(businessId);
            //收款方内部账户信息
            this.setPayeeCounterAcc(nisAllocationReqListDTO, model);
            model.addAttribute("nisAllocationReqPage", nisAllocationReqListDTO);
            this.setBudgetAttr(nisAllocationReqListDTO.getType(), model);
        }
        return "allocation/viewApproveNisAllocationReq";
    }

    /**
     * 批量提交NIS_ALLOCATION_REQ
     *
     * @param ids
     * @return AjaxJson
     */
    //////@RequiresPermissions(value = {ResourceConstant.NETBANK_ALLOCATION_TYPE_UP_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_DOWN_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_ASK_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_POS_APPLY,
//			ResourceConstant.NETBANK_ALLOCATION_TYPE_TURN_APPLY},
//			logical = Logical.OR)
    @PostMapping("/batchSubmit")
    @ResponseBody
    public AjaxJson batchSubmit(@RequestParam("ids") String ids, @RequestParam("randoms") String randoms, @RequestParam("sourceDatas") String sourceDatas,
                                @RequestParam("caCodings") String caCodings) {
        this.nisAllocationReqService.batchSubmit(ids, randoms, sourceDatas, caCodings, this.getCurrentUser());
        return success();
    }

    /**
     * 根据条件加载银行账户信息
     *
     * @param prentAccId  父类银行账户ID
     * @param attributeId 收支属性ID
     * @param curId       币别ID
     * @param corpId      付款单位ID
     * @param isOnline    是否在线 1-在线 0-离线
     * @return List<BtBankAccDTO>
     */
    @PostMapping("/getBankAccBy")
    @ResponseBody
    public List<BtBankAccDTO> getBankAccBy(String prentAccId, String attributeId, String curId, String corpId, String isOnline) {
        try {
            BtBankAccDTO dto = new BtBankAccDTO();
            if (EmptyUtils.isBlank(prentAccId)) {
                return new ArrayList<>();
            } else {
                dto.setPrentAccId(prentAccId);
            }
            if (!EmptyUtils.isBlank(attributeId)) {
                dto.setAttributeId(attributeId);
            }
            if (!EmptyUtils.isBlank(curId)) {
                dto.setCurId(curId);
            }
            if (!EmptyUtils.isBlank(corpId)) {
                dto.setCorpId(corpId);
            }
            if (!EmptyUtils.isBlank(isOnline)) {
                dto.setIsOnline(isOnline);
            }
            List<BtBankAccDTO> btBankAccDTOList = iBtBankAccService.getBankAccListAll(dto);
            for (BtBankAccDTO btBankAccDTO : btBankAccDTOList) {
                BigDecimal balance = iBisAccBalService.getLastByBalDate(btBankAccDTO.getBankAcc(), btBankAccDTO.getCurId());
                if (!EmptyUtils.isEmpty(balance)) {
                    btBankAccDTO.setBalance(balance);
                }
                //获取内部账户联机余额
                FcOnlineLedgerDTO byBlnctrAcc = fcOnlineLedgerService.getByBlnctrAcc(btBankAccDTO.getCounterAccId());
                if(null != byBlnctrAcc){
                    btBankAccDTO.setFcBalance(byBlnctrAcc.getCheckBalance());
                }
            }
            return btBankAccDTOList;
        } catch (Exception e) {
            log.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return new ArrayList<>();
        }
    }

    /**
     * 获取网点下的单位信息
     *
     * @param prentAccId
     * @return
     */
    @GetMapping("/treeByNetId")
    @ResponseBody
    public Object treeByNetId(String prentAccId) {
        List<Map<String, Object>> tempList = new ArrayList<>();
        List<String> corpIds = iBtBankAccService.listChildCorpIdsByParent(prentAccId);
        if (EmptyUtils.isEmptyList(corpIds)) {
            return tempList;
        }
        List<CorpDTO> corpList = this.iCorpService.listCorpDTOByCorpIds2(corpIds);
        if (EmptyUtils.isEmptyList(corpList)) {
            return tempList;
        }
        for (CorpDTO corp : corpList) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", corp.getId());
            map.put("pId", corp.getParentId());
            map.put("name", corp.getName());
            map.put("type", corp.getType());
            map.put("code", corp.getCode());
            map.put("netId", corp.getNetId());
            tempList.add(map);
        }
        return tempList;
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @param prentAccId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayeeAcc")
    @ResponseBody
    public List<BtBankAccDTO> getPayeeAcc(String corpId, String prentAccId, String curId) {
        BtBankAccDTO dto = new BtBankAccDTO();
        if (EmptyUtils.isBlank(prentAccId)) {
            return new ArrayList<>();
        } else {
            dto.setPrentAccId(prentAccId);
        }
        if (!EmptyUtils.isBlank(corpId)) {
            dto.setCorpId(corpId);
        }
        if (!EmptyUtils.isBlank(curId)) {
            dto.setCurId(curId);
        }
        return iBtBankAccService.getBankAccListAll(dto);
    }

    /**
     * 修改NIS_ALLOCATION_REQ
     *
     * @param nisAllocationReqListDTO
     * @return
     */
    @LogMessage(content = "修改NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/approveUpdate")
    @ResponseBody
    public AjaxJson approveUpdate(NisAllocationReqListDTO nisAllocationReqListDTO) {
        nisAllocationReqService.approveUpdate(nisAllocationReqListDTO, this.getCurrentUser());
        return success();
    }

    /**
     * 批量提交
     *
     * @param ids
     * @return
     */
    @PostMapping("/checkBal")
    @ResponseBody
    public AjaxJson checkBal(String ids,Integer type) {
        String msg = "";
        msg = nisAllocationReqService.checkBal(ids,type);
        return success(msg);
    }

    /**
     * 查询账户和余额
     *
     * @param accountSearchDTO
     * @return
     */
    @PostMapping("/getListBy")
    @ResponseBody
    public List<AccountDTO> getListBy(AccountSearchDTO accountSearchDTO) {
        accountSearchDTO.setUserId(getUserId());
        return this.nisAllocationReqService.listBy(accountSearchDTO);
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<NisAllocationReqVO> list) {
        this.nisAllocationReqService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 导出全部
     *
     * @return
     */
    @LogMessage(content = "导出资金调拨查询", level = Globals.Log_Leavel_INFO, type = Globals.Log_Leavel_INFO)
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, String dataTablesInputParam,
                          String exportExcelHeadListParam, String totalParam, String fileNameParam,
                          String exportType, String selectedIds) throws Exception {
        DataTablesInput input = new DataTablesInput();
        if (StringUtils.isNotEmpty(dataTablesInputParam)) {
            input = com.alibaba.fastjson.JSON.parseObject(dataTablesInputParam, DataTablesInput.class);
        }

        List<NisAllocationReqListDTO> list = new ArrayList<>();

        if ("selected".equals(exportType) || "current".equals(exportType)) {
            if (StringUtils.isNotEmpty(selectedIds)) {
                List<String> ids = com.alibaba.fastjson.JSON.parseArray(selectedIds, String.class);
                input.setStart(0);
                input.setLength(5000);
                list = nisAllocationReqService.getDataTablesUnion(input, this.getCurrentUser()).getData().stream()
                        .filter(dto -> ids.contains(dto.getId()))
                        .collect(java.util.stream.Collectors.toList());
            }
        } else {
            input.setStart(0);
            input.setLength(5000);
            list = nisAllocationReqService.getDataTablesUnion(input, this.getCurrentUser()).getData();
        }
        String fileName = "下拨";
        /**资金调拨类型 10-上划 11-下拨 12-请款 13-头寸调拨 14-上缴 15-查询*/
        if (AllocationConstant.ALLOCATION_TYPE_UP.equals(Integer.parseInt(input.getFormdata().get("eqi_type").toString()))) {
            fileName = "上划";
        } else if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(Integer.parseInt(input.getFormdata().get("eqi_type").toString()))) {
            fileName = "下拨";
        } else if (AllocationConstant.ALLOCATION_TYPE_ASK.equals(Integer.parseInt(input.getFormdata().get("eqi_type").toString()))) {
            fileName = "请款";
        } else if (AllocationConstant.ALLOCATION_TYPE_POS.equals(Integer.parseInt(input.getFormdata().get("eqi_type").toString()))) {
            fileName = "头寸调拨";
        } else if (AllocationConstant.ALLOCATION_TYPE_TURN.equals(Integer.parseInt(input.getFormdata().get("eqi_type").toString()))) {
            fileName = "上缴";
        }

        HSSFWorkbook workbook = nisAllocationReqService.createExcelContent(list,fileName);
        if (nisAllocationReqService.isIE(request)) {
            fileName = URLEncoder.encode(fileName+".xls", "UTF8");
        } else {
            fileName = new String((fileName+".xls").getBytes("UTF-8"), "ISO-8859-1");
        }
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
}
