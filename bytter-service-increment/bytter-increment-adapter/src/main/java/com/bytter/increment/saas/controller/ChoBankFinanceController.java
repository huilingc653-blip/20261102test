package com.bytter.increment.saas.controller;

import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.constant.BankFinanceConstant;
import com.bytter.increment.api.constant.ChoMessageConstant;
import com.bytter.increment.domain.service.ChoBankFinanceService;
import com.bytter.increment.domain.vo.ChoBankFinanceVO;
import com.bytter.increment.domain.vo.v1.ChoBankFinanceAddVO;
import com.bytter.increment.domain.vo.v1.ChoBankFinancePageVO;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.IDictService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行理财表
 * @date 2017-08-18 14:03:30
 *
 */
@Api(value = "银行理财表", tags = "银行理财表")
@RestController("v1ChoBankFinanceController")
@RequestMapping("/v1-increment/chowmatistic/chobankfinance")
public class ChoBankFinanceController extends BaseController {
    @Resource
    private ChoBankFinanceService choBankFinanceService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;

    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private ICorpService iCorpService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;

    @DubboReference(check = false)
    private IDictService dictService;

    /**
     * 银行理财表 table列表
     *
     * @return
     */
    @ApiOperation(value = "银行理财表列表")
    @PostMapping("/datatable")

    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choBankFinanceService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 银行理财表新增页面跳转
     *
     * @param busType
     * @param id
     * @return
     */
    @GetMapping("/add/{busType}")
    public AjaxJson<ChoBankFinanceAddVO> add(@PathVariable("busType") Integer busType, String id) {
        ChoBankFinanceAddVO vo = new ChoBankFinanceAddVO();
        if (StringUtils.isNotEmpty(id)) {
            vo.setChoBankFinancePage(choBankFinanceService.get(id));
        }
        vo.setBusType(busType);
        vo.setSysDate(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //初始化参数
        this.initConfig(vo, busType);
        vo.setForwardUrl("chowmatistic/addChoBankFinance" + this.getForwardByBusType(busType));
        return AjaxJson.success(vo);
    }

    /**
     * 银行理财表编辑页面跳转
     *
     * @param choBankFinance
     * @return
     */
    @GetMapping("/update")
    public AjaxJson<ChoBankFinanceAddVO> update(ChoBankFinanceVO choBankFinance) {
        ChoBankFinanceAddVO model = new ChoBankFinanceAddVO();
        if (StringUtils.isNotEmpty(choBankFinance.getId())) {
            choBankFinance = choBankFinanceService.getByIdUnion(choBankFinance.getId());
            model.setChoBankFinancePage(choBankFinance);
        }
        model.setSysDate(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //初始化参数
        this.initConfig(model, choBankFinance.getBusType());
        if (BankFinanceConstant.STATUE_CHECK.equals(choBankFinance.getStatus())) {
            model.setForwardUrl("chowmatistic/blChoBankFinance");
            return AjaxJson.success(model);
        } else {
            model.setForwardUrl("chowmatistic/updateChoBankFinance" + this.getForwardByBusType(choBankFinance.getBusType()));
            return AjaxJson.success(model);
        }
    }

    /**
     * 初始化系统参数
     *
     * @param model
     * @param busType
     */
    private void initConfig(ChoBankFinanceAddVO model, Integer busType) {
        if (BankFinanceConstant.BUS_TYPE_C2T.equals(busType)) {
            //活期转定期
            model.setRegularOnset(configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_REGULARONSET).getValue());
            model.setRegularKeep(configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_REGULARKEEP).getValue());
        } else if (BankFinanceConstant.BUS_TYPE_C2N.equals(busType)) {
            //活期转通知
            model.setNoticeOnset(configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_NOTICEONSET).getValue());
            model.setNoticeKeep(configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_NOTICEKEEP).getValue());
        } else if (BankFinanceConstant.BUS_TYPE_T2C.equals(busType)) {
            //定期转活期
            model.setRegularNum(configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_REGULARNUM).getValue());
        } else if (BankFinanceConstant.BUS_TYPE_N2C.equals(busType)) {
            //通知转活期
            model.setNoticeMinLimit(configService.getConfig(BankFinanceConstant.CHO_BANK_CONFIG_TYPE, BankFinanceConstant.CHO_BANK_CONFIG_NOTICEMINLIMIT).getValue());
        }
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
    }


    /**
     * 根据业务类型返回不同的跳转页面
     *
     * @param busType
     * @return
     */
    public String getForwardByBusType(Integer busType) {
        /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期*/
        String forward = "";
        if (BankFinanceConstant.BUS_TYPE_C2T.equals(busType)) {
            forward = "Current2Time";
        } else if (BankFinanceConstant.BUS_TYPE_T2C.equals(busType)) {
            forward = "Time2Current";
        } else if (BankFinanceConstant.BUS_TYPE_C2N.equals(busType)) {
            forward = "Current2Notice";
        } else if (BankFinanceConstant.BUS_TYPE_N2C.equals(busType)) {
            forward = "Notice2Current";
        }
        return forward;
    }

    /**
     * 流水认领组件 begin
     */
    @ApiOperation("流水认领组件")
    @GetMapping("/mulSelectBankFinancePage")
    public AjaxJson<ChoBankFinancePageVO> mulSelectBankFinancePage(String corpId, String busType, String id, String row) {
        ChoBankFinancePageVO pageVO = new ChoBankFinancePageVO();
        if (StringUtils.isNotBlank(corpId)) {
            CorpDTO corpDTO = iCorpService.get(corpId);
            pageVO.setCorp(corpDTO);
            pageVO.setBusType(busType);
            pageVO.setRow(row);
        }
        if (StringUtils.isNotBlank(id)) {
            BisAccDtlDTO bisAccDtlDTO = bisAccDtlService.getById(id);
            if (StringUtils.isNotEmpty(bisAccDtlDTO)) {

                String date = DateUtils.date2Str(bisAccDtlDTO.getTransTime(), DateUtils.date_sdf());
                Date dateStart = DateUtils.addDay(bisAccDtlDTO.getTransTime(), -30);

                // 获取交易日期
                pageVO.setStartDate(DateUtils.date2Str(dateStart, DateUtils.date_sdf()));
                pageVO.setEndDate(date);
                // 获取交易金额
                pageVO.setDto(bisAccDtlDTO);
            }
        }
        return success(pageVO);
    }

    @ApiOperation("流水认领确认选择组件")
    @PostMapping("/datatableForSelect")
    public AjaxJson<DataTablesOutput> dataTableForSelect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(choBankFinanceService.getDataTablesUnionForSelect(input, this.getCurrentUser()));
    }

    /**
     * 银行理财表详情页
     *
     * @return
     */
    @ApiOperation(value = "银行理财表详情页")
    @LogMessage(content = "查询银行理财表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<ChoBankFinancePageVO> view(@PathVariable("id") String id) {
        ChoBankFinanceVO choBankFinance = choBankFinanceService.getByIdUnion(id);
        ChoBankFinancePageVO pageVO = new ChoBankFinancePageVO();
        pageVO.setChoBankFinancePage(choBankFinance);
        if (choBankFinance != null) {
            String unitShow = dictService.get("choBankFinance.dateUnit", choBankFinance.getDateUnit());
            choBankFinance.setLimitShow(choBankFinance.getDayLimit() + unitShow);
            choBankFinance.setInterestShow(choBankFinance.getInterestRate().multiply(BigDecimal.valueOf(100)).setScale(4) + "%");
        }
        pageVO.setBusType(ChoMessageConstant.BUS_TYPE_BANK);
        return success(pageVO);
    }

    /**
     * 添加银行理财表
     *
     * @param choBankFinance
     * @return
     */
    @LogMessage(content = "添加银行理财表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(ChoBankFinanceVO choBankFinance) {
        choBankFinanceService.saveBankFinance(choBankFinance, this.getCurrentUser());
        return success();
    }

    /**
     * 修改银行理财表
     *
     * @param choBankFinance
     * @return
     */
    @LogMessage(content = "修改银行理财表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(ChoBankFinanceVO choBankFinance) {
        choBankFinanceService.updateBankFinance(choBankFinance, this.getCurrentUser());
        return success();
    }

    /**
     * 删除银行理财表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除银行理财表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        choBankFinanceService.delChoBankFinance(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除银行理财表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除银行理财表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            choBankFinanceService.delChoBankFinance(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 获取账号
     *
     * @param corpId
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getBankAcc")
    public AjaxJson<List<BtBankAccDTO>> getBankAcc(String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(corpId)) {
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listOnlineBankAcc)) {
                listAcc.addAll(listOnlineBankAcc);
            }
            if (!EmptyUtils.isEmptyList(listOfflineBankAcc)) {
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        //过滤活期账号
        List<BtBankAccDTO> listAccResult = new ArrayList<BtBankAccDTO>();
        if (!EmptyUtils.isEmptyList(listAcc)) {
            for (BtBankAccDTO dto : listAcc) {
                if (BtBankAccConstant.ACCTYPE_01.equals(dto.getAccType())) {
                    listAccResult.add(dto);
                } else if (BtBankAccConstant.ACCTYPE_10.equals(dto.getAccType())) {
                    listAccResult.add(dto);
                }
            }
        }
        return AjaxJson.success(listAccResult);
    }

    /**
     * 获取账号（补录列表界面获取离线账号）
     *
     * @param corpId
     * @param busType
     * @return
     */
    @SuppressWarnings("unchecked")
    @PostMapping("/getOfflineBankAcc")
    public List<BtBankAccDTO> getOfflineBankAcc(String corpId, Integer busType) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(corpId)) {
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listOnlineBankAcc)) {
                listAcc.addAll(listOnlineBankAcc);
            }

            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(corpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listOfflineBankAcc)) {
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        //过滤活期账号
        List<BtBankAccDTO> listAccResult = new ArrayList<BtBankAccDTO>();
        if (!EmptyUtils.isEmptyList(listAcc)) {
            for (BtBankAccDTO dto : listAcc) {
                if (BtBankAccConstant.ACCTYPE_01.equals(dto.getAccType())) {
                    listAccResult.add(dto);
                }
            }
        }
        return listAccResult;
    }

    /**
     * 跳转至审核列表
     *
     * @param busType 业务类型
     * @return
     */
    @GetMapping("/listApprove/{busType}")
    public AjaxJson<ChoBankFinanceAddVO> listApprove(@PathVariable Integer busType, String flowStatus, String cycleTime) {
        ChoBankFinanceAddVO model = new ChoBankFinanceAddVO();
        model.setBusType(  busType);
        model.setFlowStatus(  flowStatus);
        model.setCycleTime(  cycleTime);
        model.setForwardUrl("chowmatistic/listChoBankFinance" + this.getForwardByBusType(busType) + "Approve");
        return AjaxJson.success(model);
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input =dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(this.choBankFinanceService.dataTableApprove(input));
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @return
     */
    @GetMapping("/approve")
    public AjaxJson<ChoBankFinanceAddVO> approve(String id, String taskId) {
        ChoBankFinanceAddVO model = new ChoBankFinanceAddVO();
        if (StringUtils.isNotEmpty(id)) {
            model.setChoBankFinancePage(this.choBankFinanceService.getByIdUnion(id));
            model.setBusType(ChoMessageConstant.BUS_TYPE_BANK);
            model.setTaskId(  taskId);
            model.setBusType(ChoMessageConstant.BUS_TYPE_BANK);
        }
        model.setForwardUrl("chowmatistic/approveChobankfinance");
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @return String
     */
    @GetMapping("/mobileApprove")
    public AjaxJson<ChoBankFinanceAddVO> mobileApprove(String businessId, String taskId) {
        ChoBankFinanceAddVO model = new ChoBankFinanceAddVO();
        if (StringUtils.isNotEmpty(businessId)) {
            ChoBankFinanceVO vo = this.choBankFinanceService.getByIdUnion(businessId);
            if (EmptyUtils.isEmpty(vo.getBalance())) {
                vo.setBalance(BigDecimal.ZERO);
            }
            if (EmptyUtils.isEmpty(vo.getBlInterest())) {
                vo.setBlInterest(BigDecimal.ZERO);
            }
            if (EmptyUtils.isEmpty(vo.getBlInterestRate())) {
                vo.setBlInterestRate(BigDecimal.ZERO);
            }
            model.setChoBankFinancePage(vo);
            model.setTaskId( taskId);
            model.setBusType( ChoMessageConstant.BUS_TYPE_BANK);
        }
        model.setForwardUrl("chowmatistic/viewApproveChoBankFinance");
        return AjaxJson.success(model);
    }

    /**
     * 存款类业务查询 table列表
     *
     * @return
     */
    @PostMapping("/dataTableQuery")
    public AjaxJson<DataTablesOutput> dataTableQuery(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(choBankFinanceService.getDataTablesQuery(input, this.getCurrentUser()));
    }

    /**
     * 银行理财表详情页
     *
     * @return
     */
    @LogMessage(content = "查询银行理财表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewQuery/{id}")
    public AjaxJson<ChoBankFinanceAddVO> viewQuery(@PathVariable("id") String id) {
        ChoBankFinanceAddVO model = new ChoBankFinanceAddVO();
        ChoBankFinanceVO choBankFinance = choBankFinanceService.getByIdUnion(id);
        model.setChoBankFinancePage( choBankFinance);
        model.setBusType( ChoMessageConstant.BUS_TYPE_BANK);
        model.setForwardUrl("chowmatistic/viewChoBankFinanceQuery");
        return AjaxJson.success(model);
    }

    /**
     * 银行存款应收未收利息统计表
     *
     * @return
     */
    @PostMapping("/dataTableReport")
    public AjaxJson<DataTablesOutput> dataTableReport(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(choBankFinanceService.getDataTablesReport(input, this.getCurrentUser()));
    }


}

