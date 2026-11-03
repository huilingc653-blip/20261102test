package com.bytter.biz.saas.controller.allocation;

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
import com.bytter.biz.allocation.vo.NisAllocationReqListVO;
import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.bpm.persistence.WorkFlowPersistence;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.*;
import com.bytter.poi.util.ExcelHeader;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.*;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.*;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;


/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: NIS_ALLOCATION_REQ
 * @date 2017-05-31 10:25:52
 * 资金调拨
 */
@Api(value = "/v1-payment/allocation/nisallocationreq",tags = "手动资金调拨")
@RestController("v1NisAllocationReqController")
@RequestMapping("/v1-payment/allocation/nisallocationreq")
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
    @ApiOperation("列表页面跳转")
    @LogMessage(content = "查询NIS_ALLOCATION_REQ列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<NisAllocationReqListVO> list() {
        NisAllocationReqListVO model=new NisAllocationReqListVO();
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(  configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //银行账户余额不足
        model.setBankaccOver(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER).getValue());
        //银行账户余额不足
        model.setAccountOver( configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ACCOUNTOVER).getValue());
        return AjaxJson.success(model);
    }

    /***
     * 需求清单查询连接过来的。只有头寸的查询
     * @param erpId
     * @return TODO
     */
    @ApiOperation("需求清单查询连接过来的。只有头寸的查询")
    @LogMessage(content = "查询NIS_ALLOCATION_REQ列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/demandList")
    public AjaxJson<NisAllocationReqListVO> list(String erpId) {
        NisAllocationReqListVO model=new NisAllocationReqListVO();
        model.setErpId(  erpId);
        model.setBillFrom(  "5");//5-资金需求清单
        model.setQueryType(  "13");//5-头寸调拨
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(  configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        //银行账户余额不足
        model.setBankaccOver(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER).getValue());
        //银行账户余额不足
        model.setAccountOver( configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ACCOUNTOVER).getValue());
        return AjaxJson.success(model);
    }

    /**
     * NIS_ALLOCATION_REQ table列表
     *
     * @return DataTablesOutput
     */
    @ApiOperation("table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisAllocationReqListDTO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisAllocationReqService.getDataTablesUnion(input, this.getCurrentUser()));
    }


    /**
     * NIS_ALLOCATION_REQ编辑页面跳转
     *
     * @return String
     */
    @ApiOperation("编辑页面跳转前数据获取")
    @GetMapping("/update")
    public AjaxJson<NisAllocationReqListVO> update(String id) {
         NisAllocationReqListVO model=new NisAllocationReqListVO();
         NisAllocationReqListDTO nisAllocationReqListDTO = new NisAllocationReqListDTO();
        if (StringUtils.isNotEmpty(id)) {
            nisAllocationReqListDTO = nisAllocationReqService.getVOAllByUnion(id);
            if (StringUtils.isNotEmpty(nisAllocationReqListDTO.getStatus()) && AllocationConstant.STATUE_DEL.equals(nisAllocationReqListDTO.getStatus())) {
                throw new ParameterException(AllocationMessageEnum.NIS_ALLOCATION_MESSAGE_STATUS_DELETE);
            }
            BtCurrencyDTO currencyDTO = this.currencyService.getObjectById(nisAllocationReqListDTO.getCurId());
            if (!EmptyUtils.isEmpty(currencyDTO)) {
                model.setCurName(  currencyDTO.getCurName());
            }
            if (AllocationConstant.ALLOCATION_TYPE_DOWN.equals(nisAllocationReqListDTO.getType())) {
                model.setPayeeCorpDTO(  iBtBankAccService.getBankAccByBankAccAndCurId(nisAllocationReqListDTO.getPayeeAcc(), nisAllocationReqListDTO.getCurId()));
            }
            if (AllocationConstant.ALLOCATION_TYPE_POS.equals(nisAllocationReqListDTO.getType())) {
                model.setCorp( iCorpService.get(nisAllocationReqListDTO.getCorpId()));
            }
            //收款方内部账户信息

            this.setPayeeCounterAcc(nisAllocationReqListDTO, model);
            model.setNisAllocationReqPage( nisAllocationReqListDTO);
            if(nisAllocationReqListDTO.getType() == 14){
                com.bytter.biz.bdlink.api.dto.BisAccBalDTO bisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReqListDTO.getPayerAcc(),nisAllocationReqListDTO.getCurId(),null,true);
                BigDecimal bal = BigDecimal.ZERO;
                if (!EmptyUtils.isEmpty(bisAccBalDTO)) {
                    if(!EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal())){
                        bal =  bisAccBalDTO.getAvailBal();
                    }
                }
                model.setBal(bal);
            }

            if(nisAllocationReqListDTO.getType() == 12){
                com.bytter.biz.bdlink.api.dto.BisAccBalDTO payeeBisAccBalDTO = iBisAccBalService.getBisAccBalBy(nisAllocationReqListDTO.getPayeeAcc(),nisAllocationReqListDTO.getCurId(),null,true);
                BigDecimal payeeBal = BigDecimal.ZERO;
                if (!EmptyUtils.isEmpty(payeeBisAccBalDTO)) {
                    if(!EmptyUtils.isEmpty(payeeBisAccBalDTO.getBal())) {
                        payeeBal = payeeBisAccBalDTO.getBal();
                    }
                }
                model.setBal( payeeBal);
            }
        }
        this.setBudgetAttr(nisAllocationReqListDTO.getType(), model);

        return AjaxJson.success(model);
    }

    /**
     * 设置收款方中心账户信息
     *
     * @param nisAllocationReq
     * @param model
     */
    public void setPayeeCounterAcc(NisAllocationReqListDTO nisAllocationReq, NisAllocationReqListVO model) {
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
            model.setPayeeCounterAcc( payeeCounterAcc);
        }
    }

    /**
     * NIS_ALLOCATION_REQ详情页
     *
     * @return String
     */
    @ApiOperation("详情页数据获取")
    @LogMessage(content = "查询NIS_ALLOCATION_REQ详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisAllocationReqListVO> view(@PathVariable("id") String id) {
        NisAllocationReqListVO model=new NisAllocationReqListVO();
        NisAllocationReqListDTO nisAllocationReq = nisAllocationReqService.getVOAllByUnion(id);
        //付款方内部账户信息
        com.bytter.system.api.vo.FcAccBalanceQueryVO payerCounterAcc = new com.bytter.system.api.vo.FcAccBalanceQueryVO();
        payerCounterAcc.setOnlineBalance(nisAllocationReq.getPayerAccBalance());
        model.setPayeeCounterAcc(  payerCounterAcc);
        //收款方内部账户信息
        this.setPayeeCounterAcc(nisAllocationReq, model);//这里把银行账号余额设置为内部账号余额有问题
        model.setNisAllocationReqPage( nisAllocationReq);
        model.setBusType( NisMessageConstant.BUS_TYPE_ALLOCATION);
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
            model.setBal( bal);
            model.setAvailbal( availbal);
            model.setPayeeBal(  payeeBal);
            model.setPayeeAvailbal(  payeeAvailbal);
        }
        return AjaxJson.success(model);
    }

    /**
     * NIS_ALLOCATION_REQ详情页
     *
     * @return String
     */
    @ApiOperation("根据BillNo查询详情页")
    @LogMessage(content = "查询NIS_ALLOCATION_REQ详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewByBillNo/{billNo}")
    public AjaxJson<NisAllocationReqListVO> viewByBillNo(@PathVariable("billNo") String billNo) {
        NisAllocationReqListVO model=new NisAllocationReqListVO();
        NisAllocationReqListDTO nisAllocationReq = nisAllocationReqService.getNisAllocationReqByBillNo(billNo);
        nisAllocationReq.setPayerAccBalance(iBisAccBalService.getLastByBalDate(nisAllocationReq.getPayerAcc(), nisAllocationReq.getCurId()));
        nisAllocationReq.setPayeeAccBalance(iBisAccBalService.getLastByBalDate(nisAllocationReq.getPayeeAcc(), nisAllocationReq.getCurId()));
        //收款方内部账户信息
        this.setPayeeCounterAcc(nisAllocationReq, model);
        model.setNisAllocationReqPage(  nisAllocationReq);
        model.setBusType(  NisMessageConstant.BUS_TYPE_ALLOCATION);
        this.setBudgetAttr(nisAllocationReq.getType(), model);
        return AjaxJson.success(model);
    }

    /**
     * 设置预算相关参数
     *
     * @param model
     */
    public void setBudgetAttr(Integer type, NisAllocationReqListVO model) {
        model.setBankaccOver(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BANKACCOVER).getValue());
        model.setAccountOver(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ACCOUNTOVER).getValue());
        model.setIsAccount(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNT).getValue());
        model.setIsAutoAccount(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISAUTOACCOUNT).getValue());
        //头寸调拨是否记账
        model.setIsAccountPos(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ISACCOUNTPOS).getValue());
        // 请款和下拨挂接资金计划
        if (AllocationConstant.ALLOCATION_TYPE_ASK.intValue() == type.intValue()
                || AllocationConstant.ALLOCATION_TYPE_DOWN.intValue() == type.intValue()) {
            model.setBudgetSel(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_BUDGETSEL).getValue());
            model.setDownItem(  configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_DOWNITEM).getValue());
            model.setAskItem( configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ASKITEM).getValue());
            model.setBusinessName( BudgetConstant.BUSINESS_NAME_ALLOCATION);
            model.setProcessSysname( BudgetConstant.PROCESS_SYSNAME_NIS);
        }
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(  configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
    }

    /**
     * 添加NIS_ALLOCATION_REQ
     * 【资金调拨】--保存
     * 【头寸调拨】--提交
     * @param nisAllocationReqListDTO
     * @return AjaxJson
     */
    @ApiOperation(("暂存/提交"))
    @PostMapping("/save")
    public AjaxJson save(@RequestBody  NisAllocationReqListDTO nisAllocationReqListDTO) {
        return success(nisAllocationReqService.saveNisAllocationReq(nisAllocationReqListDTO, this.getCurrentUser()));
    }


    /**
     * 保存上划和下拨数据
     * 【资金调拨】
     * @param nisAllocationReqListDTO
     * @return
     */
    @ApiOperation(("保存上划和下拨数据"))
    @PostMapping("/saveUpAndDown")
    public AjaxJson saveUpAndDown(@RequestBody NisAllocationReqListDTO nisAllocationReqListDTO) {
        return success(nisAllocationReqService.saveUpAndDown(nisAllocationReqListDTO, this.getCurrentUser()));
    }

    /**
     * 修改NIS_ALLOCATION_REQ
     *
     * @param nisAllocationReq
     * @return
     */
    @ApiOperation("/修改")
    @LogMessage(content = "修改NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody  NisAllocationReqListDTO nisAllocationReq) {
        nisAllocationReqService.updateNisAllocationReq(nisAllocationReq, this.getCurrentUser());
        return success();
    }

    /**
     * 修改NIS_ALLOCATION_REQ
     *
     * @param nisAllocationReqListDTO
     * @return
     */
    @ApiOperation("/修改上划和下拨数据")
    @LogMessage(content = "修改NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/updateAskAndDown")
    public AjaxJson updateAskAndDown(@RequestBody NisAllocationReqListDTO nisAllocationReqListDTO) {
        nisAllocationReqService.updateAskAndDown(nisAllocationReqListDTO, this.getCurrentUser());
        return success();
    }

    /**
     * 删除NIS_ALLOCATION_REQ
     *
     * @param id
     * @return
     */
    @ApiOperation("/删除")
    @LogMessage(content = "删除NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
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
    @ApiOperation("/批量删除")
    @LogMessage(content = "批量删除NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
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
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return DataTablesOutput
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    public  AjaxJson<DataTablesOutput<WorkFlowPersistence> > dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(this.nisAllocationReqService.getDataTablesApprove(input));
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @return String
     */
    @ApiOperation("跳转至审核详情界面")
    @GetMapping("/approve")
    public AjaxJson<NisAllocationReqListVO> approve(String id) {
        NisAllocationReqListVO model=new NisAllocationReqListVO();
        model.setBusType( NisMessageConstant.BUS_TYPE_ALLOCATION);
        if (StringUtils.isNotEmpty(id)) {
            NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqService.getVOAllByUnion(id);
            //收款方内部账户信息
            this.setPayeeCounterAcc(nisAllocationReqListDTO, model);
            model.setNisAllocationReqPage(nisAllocationReqListDTO);
            this.setBudgetAttr(nisAllocationReqListDTO.getType(), model);
            model.setAskAllowEditMoney( configService.getConfig(AllocationConstant.ALLOT_CONFIG_TYPE, AllocationConstant.ALLOT_CONFIG_ASKALLOWEDITMONEY).getValue());
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
                model.setBal(  bal);
                model.setAvailbal(  availbal);
                model.setPayeeBal(  payeeBal);
                model.setPayeeAvailbal(  payeeAvailbal);
            }
        }
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @return String
     */
    @ApiOperation("跳转至手机审核详情界面")
    @GetMapping("/mobileApprove")
    public AjaxJson<NisAllocationReqListVO> mobileApprove(String businessId) {
        NisAllocationReqListVO model=new NisAllocationReqListVO();
        model.setBusType(NisMessageConstant.BUS_TYPE_ALLOCATION);
        if (!StringUtils.isBlank(businessId)) {
            NisAllocationReqListDTO nisAllocationReqListDTO = nisAllocationReqService.getVOAllByUnion(businessId);
            //收款方内部账户信息
            this.setPayeeCounterAcc(nisAllocationReqListDTO, model);
            model.setNisAllocationReqPage( nisAllocationReqListDTO);
            this.setBudgetAttr(nisAllocationReqListDTO.getType(), model);
        }
        return AjaxJson.success(model);
    }

    /**
     * 批量提交NIS_ALLOCATION_REQ
     *
     * @param ids
     * @return AjaxJson
     */
    @ApiOperation("批量提交")
    @PostMapping("/batchSubmit")
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
    @ApiOperation("根据条件加载银行账户信息")
    @PostMapping("/getBankAccBy")
    public AjaxJson<List<BtBankAccDTO>> getBankAccBy(String prentAccId, String attributeId, String curId, String corpId, String isOnline) {
        try {
            BtBankAccDTO dto = new BtBankAccDTO();
            if (EmptyUtils.isBlank(prentAccId)) {
                return AjaxJson.success(new ArrayList<>());
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
            return AjaxJson.success(btBankAccDTOList);
        } catch (Exception e) {
            log.error(e.toString());
            ExceptionStackUtils.getStack(e);
            return AjaxJson.success(new ArrayList<>());
        }
    }

    /**
     * 获取网点下的单位信息
     *
     * @param prentAccId
     * @return
     */
    @GetMapping("/treeByNetId")
    public AjaxJson<List<Map<String, Object>>> treeByNetId(String prentAccId) {
        List<Map<String, Object>> tempList = new ArrayList<>();
        List<String> corpIds = iBtBankAccService.listChildCorpIdsByParent(prentAccId);
        if (EmptyUtils.isEmptyList(corpIds)) {
            return AjaxJson.success(tempList);
        }
        List<CorpDTO> corpList = this.iCorpService.listCorpDTOByCorpIds2(corpIds);
        if (EmptyUtils.isEmptyList(corpList)) {
            return AjaxJson.success(tempList);
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
        return AjaxJson.success(tempList);
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @param prentAccId
     * @return
     */
    @ApiOperation("获取付款账号")
    @SuppressWarnings("unchecked")
    @PostMapping("/getPayeeAcc")
    public AjaxJson<List<BtBankAccDTO>> getPayeeAcc(String corpId, String prentAccId, String curId) {
        BtBankAccDTO dto = new BtBankAccDTO();
        if (EmptyUtils.isBlank(prentAccId)) {
            return AjaxJson.success(new ArrayList<>());
        } else {
            dto.setPrentAccId(prentAccId);
        }
        if (!EmptyUtils.isBlank(corpId)) {
            dto.setCorpId(corpId);
        }
        if (!EmptyUtils.isBlank(curId)) {
            dto.setCurId(curId);
        }
        return AjaxJson.success(iBtBankAccService.getBankAccListAll(dto));
    }

    /**
     * 修改NIS_ALLOCATION_REQ
     *
     * @param nisAllocationReqListDTO
     * @return
     */
    @ApiOperation("修改")
    @LogMessage(content = "修改NIS_ALLOCATION_REQ", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/approveUpdate")
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
    @ApiOperation("批量提交")
    @PostMapping("/checkBal")
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
    @ApiOperation("查询账户和余额")
    @PostMapping("/getListBy")
    public AjaxJson<List<AccountDTO>> getListBy(@RequestBody AccountSearchDTO accountSearchDTO) {
        accountSearchDTO.setUserId(getUserId());
        return AjaxJson.success(this.nisAllocationReqService.listBy(accountSearchDTO));
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<NisAllocationReqVO> list) {
        this.nisAllocationReqService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 导出全部
     *
     * @return
     */
    @ApiOperation("导出全部")
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStreamCustomizeHeader(response, dataTablesInputParam, exportExcelHeadListParam, customizedExcelHeaderInfo(),new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                return nisAllocationReqService.getDataTablesUnion(dataTablesInput, user).getData();
            }
        }, totalParam, fileNameParam,"资金调拨查询", 5000, NisAllocationReqListDTO.class);
    }

    /**
     * 定制表头
     * @return
     */
    private ExcelHeader customizedExcelHeaderInfo() {
        ExcelHeader excelHeader = new ExcelHeader();

        String rowStr1 = "单据号码,业务类型,付款账号,,,,收款账号,,,,币别,期望支付日期,用途,金额,制单人,制单日期,状态,交易状态";
        String rowStr2 = ",,账号,单位,户名,开户行,账号,单位,户名,开户行, , , , , , , , ";

        List<List<String>> cellInfoList = new ArrayList<>();

        String[] split1 = rowStr1.split(",");
        String[] split2 = rowStr2.split(",");
        List<String> cellValList1 = new ArrayList<>();
        List<String> cellValList2 = new ArrayList<>();
        for(int i = 0; i < split1.length; i++){
            cellValList1.add(split1[i]);
        }
        for(int i = 0; i < split2.length; i++){
            cellValList2.add(split2[i]);
        }

        cellInfoList.add(cellValList1);
        cellInfoList.add(cellValList2);
        excelHeader.setCellInfos(cellInfoList);


        //设置表格单元合并
        List<Integer[]> mergedRegion = new ArrayList<Integer[]>();
        mergedRegion.add(new Integer[]{1,2 ,0, 0});
        mergedRegion.add(new Integer[]{1,2,1, 1});
        mergedRegion.add(new Integer[]{1,1,2, 5});
        mergedRegion.add(new Integer[]{1,1,6, 9});
        mergedRegion.add(new Integer[]{1,2,10,10});
        mergedRegion.add(new Integer[]{1,2,11,11});
        mergedRegion.add(new Integer[]{1,2,12,12});
        mergedRegion.add(new Integer[]{1,2,13,13});
        mergedRegion.add(new Integer[]{1,2,14,14});
        mergedRegion.add(new Integer[]{1,2,15,15});
        mergedRegion.add(new Integer[]{1,2,16,16});
        mergedRegion.add(new Integer[]{1,2,17,17});
        excelHeader.setMergedRegion(mergedRegion);
        return excelHeader;
    }
}
