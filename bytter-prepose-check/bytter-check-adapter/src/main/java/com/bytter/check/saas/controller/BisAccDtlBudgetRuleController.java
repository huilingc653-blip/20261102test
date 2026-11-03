package com.bytter.check.saas.controller;

import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.bdlink.vo.BisAccDtlBudgetRuleVO;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.biz.payment.api.service.INisBusTypeService;
import com.bytter.biz.payment.api.service.INisReceivableService;
import com.bytter.check.dto.BisAccDtlBudgetRuleClaimDTO;
import com.bytter.check.dto.BisAccDtlBudgetRuleOperateDTO;
import com.bytter.check.dto.BisAccDtlBudgetRuleSplitDTO;
import com.bytter.check.service.BisAccDtlBudgetRuleInnerService;
import com.bytter.check.vo.BisAccDtlBudgetRuleListConfigVO;
import com.bytter.check.vo.BisAccDtlBudgetRuleSureViewPageVO;
import com.bytter.check.vo.BisAccDtlBudgetRuleViewPageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.*;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.enums.NetbankMessageEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 银行流水认领 资金计划明细表
 * @date 2018-03-13 11:20:30
 */
@Api(value = "银行流水认领",tags = "银行流水认领")
@RestController("v1BisAccDtlBudgetRuleController")
@RequestMapping("/v1-check/bankacc/bisaccdtlbudgetrule")
public class BisAccDtlBudgetRuleController extends BaseController {
    @Autowired
    private BisAccDtlBudgetRuleInnerService bisAccDtlBudgetRuleInnerService;
    @DubboReference(check = false)
    private IConfigService configService;
    @DubboReference(check = false)
    private IBisAccDtlService bisAccDtlService;
    @DubboReference(check = false)
    private INisReceivableService nisReceivableService;
    @DubboReference(check = false)
    private INisBusTypeService nisBusTypeService;
    @DubboReference(check = false)
    private IBtCurrencyService currencyService;
    /**
     * 银行流水资金计划明细表列表 页面跳转
     *
     * @return
     */
    @ApiOperation("查询银行流水认领列表的配置数据")
    @LogMessage(content = "查询银行流水认领列表的配置数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listConfig")
    @ResponseBody
    public AjaxJson<BisAccDtlBudgetRuleListConfigVO> listConfig() {
        BisAccDtlBudgetRuleListConfigVO configVO = new BisAccDtlBudgetRuleListConfigVO();
        configVO.setBudgetSel(configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_BUDGETSEL).getValue());
        //资金计划
        configVO.setBusinessName(BudgetConstant.BUSINESS_NAME_BISACCDTL);
        configVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        //是否启用拆分
        configVO.setIsEnableSplit(configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLESPLIT).getValue());
        //是否启用合并
        configVO.setIsEnableMerge(configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLEMERGE).getValue());
        //是否认领到业务类别
        configVO.setIsClaimBusType(configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_ISCLAIMBUSTYPE).getValue());
        //交易时间默认一周
        configVO.setBeginTransTime(DateUtils.addDay(DateUtils.getDate(), -7));
        //币别信息
        configVO.setNatualCurId(currencyService.getNatualCurrency().getId());
        configVO.setNatualCurExrate(currencyService.getNatualCurrency().getCurExrate());
        return success(configVO);
    }
    /**
     * 银行流水资金计划明细表 table列表
     *
     * @return
     */
    @ApiOperation("银行流水认领列表")
    @PostMapping("/datatable")
    @ResponseBody
    public AjaxJson<DataTablesOutput<BisAccDtlBudgetRuleVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(bisAccDtlBudgetRuleInnerService.getDataTablesUnion(input, this.getCurrentUser()));
    }
    /**
     * 银行流水资金计划明细表列表 页面跳转
     *
     * @return
     */
    @ApiOperation(value = "银行流水认领查询跳转",
            notes = "tableName为appTable的话 跳转bankacc/sureBisAccDtlBudgetRule\n" +
                    "其他情况跳转bankacc/sureBisAccDtlBudgetRuleOther")
    @GetMapping("/sureView")
    public AjaxJson<BisAccDtlBudgetRuleSureViewPageVO> sureView(String id, String tableName) {
        BisAccDtlBudgetRuleSureViewPageVO pageVO = new BisAccDtlBudgetRuleSureViewPageVO();
        BisAccDtlBudgetRuleDTO bisAccDtl = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
        if (EmptyUtils.isEmpty(bisAccDtl.getProcessDate())) {
            bisAccDtl.setProcessDate("");
        }
        pageVO.setBisAccDtl(bisAccDtl);
        pageVO.setBudgetSel(configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_BUDGETSEL).getValue());
        //资金计划
        pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_BISACCDTL);
        pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        //是否认领到业务类别
        pageVO.setIsClaimBusType(configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_ISCLAIMBUSTYPE).getValue());
        return success(pageVO);
    }
    /**
     * 确认
     *
     * @param bisAccDtlBudgetRuleVO
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @ApiOperation("流水挂资金计划")
    @LogMessage(content = "流水挂资金计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/sure")
    @ResponseBody
    public AjaxJson sure(@RequestBody  BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO) {
        bisAccDtlBudgetRuleInnerService.sure(bisAccDtlBudgetRuleVO, this.getCurrentUser());
        return success();
    }

    @ApiOperation("批量处理银行流水")
    @LogMessage(content = "批量处理银行流水", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batch")
    @ResponseBody
    public AjaxJson batchBusiness(@RequestBody BisAccDtlBudgetRuleOperateDTO operatorDTO) {
        String ids = operatorDTO.getIds();
        String itemCode = operatorDTO.getItemCode();
        String itemName = operatorDTO.getItemName();
        String processDate = operatorDTO.getProcessDate();
        String settlType = operatorDTO.getSettlType();
        String billNos = operatorDTO.getBillNos();
        String batchBusType = operatorDTO.getBatchBusType();
        String rmks = operatorDTO.getRmks();
        String type = operatorDTO.getType();
        bisAccDtlBudgetRuleInnerService.batchBusiness(ids, itemCode, itemName, processDate, settlType, this.getCurrentUser(), type, billNos, batchBusType, rmks);
        return success();
    }
    /**
     * 处理逻辑
     *
     * @param id
     * @param type H 恢复 R还原 D删除
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @ApiOperation("银行流水逻辑处理  type H 恢复 R还原 D删除")
    @LogMessage(content = "银行流水逻辑处理", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/business/{id}/{type}")
    @ResponseBody
    public AjaxJson business(@PathVariable("id") String id, @PathVariable("type") String type) {
        // 逻辑变化
        bisAccDtlBudgetRuleInnerService.business(id, this.getCurrentUser(), type);
        return success();
    }
    /**
     * 查询资金计划详情
     *
     * @param id
     * @return
     */
    @ApiOperation("查询资金计划详情 ")
    @LogMessage(content = "查询资金计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<BisAccDtlBudgetRuleViewPageVO> view(@PathVariable("id") String id) {
        BisAccDtlBudgetRuleViewPageVO pageVO = new BisAccDtlBudgetRuleViewPageVO();
        BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
        pageVO.setBisAccDtlBudgetRulePage(bisAccDtlBudgetRuleVO);
        pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_BISACCDTL);
        pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        return success(pageVO);
    }
    /**
     * 查询银行流水详情
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @ApiOperation("查看银行流水详情查看")
    @LogMessage(content = "查看银行流水详情查看", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDtl/{id}")
    public AjaxJson<BisAccDtlBudgetRuleViewPageVO> viewDtl(@PathVariable("id") String id) {
        BisAccDtlBudgetRuleViewPageVO pageVO = new BisAccDtlBudgetRuleViewPageVO();
        BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
        if (CommonConstant.NO.equals(bisAccDtlBudgetRuleVO.getCdSign())) {
            String bankSerialNo = bisAccDtlBudgetRuleVO.getId();
            String billNo = "";
            String busType = "";
            List<NisReceivableDTO> nisReceivableDTOList = nisReceivableService.getNisReceivableByBankSerialNo(bankSerialNo);
            if (!EmptyUtils.isEmptyList(nisReceivableDTOList)) {
                for (NisReceivableDTO dto : nisReceivableDTOList) {
                    busType = dto.getBusType();
                    billNo += "," + dto.getBillNo();
                }
                billNo = billNo.substring(1);
                bisAccDtlBudgetRuleVO.setBillNo(billNo);
                if (EmptyUtils.isNotEmpty(busType)) {
                    bisAccDtlBudgetRuleVO.setBusType(busType);
                    NisBusTypeDTO nisBusTypeDTO = nisBusTypeService.getById(busType);
                    if (!EmptyUtils.isEmpty(nisBusTypeDTO)) {
                        bisAccDtlBudgetRuleVO.setBusTypeName(nisBusTypeDTO.getBusTypeName());
                    }
                }
            }
            List<BisAccDtlBudgetRuleDTO> dtlList = new ArrayList<>();
            //如果有拆分。显示拆分前原单
            if (!EmptyUtils.isEmpty(bisAccDtlBudgetRuleVO.getSplitId())) {
                BisAccDtlBudgetRuleDTO vo = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(bisAccDtlBudgetRuleVO.getSplitId());
                dtlList.add(vo);
            }
            //如果有合并。显示合并前原单
            List list = bisAccDtlBudgetRuleInnerService.getBisAccDtlByMergeId(bisAccDtlBudgetRuleVO.getId());
            if (EmptyUtils.isNotEmpty(list)) {
                dtlList.addAll(list);
            }
            pageVO.setDtlList(dtlList);
        }
        pageVO.setBisAccDtlBudgetRulePage(bisAccDtlBudgetRuleVO);
        //资金计划
        pageVO.setBusinessName(BudgetConstant.BUSINESS_NAME_BISACCDTL);
        pageVO.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        return success(pageVO);
    }
    /**
     * 自动匹配
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @ApiOperation("自动匹配收款流水认领")
    @LogMessage(content = "自动匹配收款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoRule")
    @ResponseBody
    public AjaxJson autoRule(@RequestBody BisAccDtlBudgetRuleClaimDTO claimDTO) throws Exception {
        String[] corpid=claimDTO.getCorpIds().split(",");
        List<String> list= Arrays.asList(corpid);
        List<String> stringList=list.stream().filter(e->!e.equals("")).collect(Collectors.toList());
        List<String> corplist=stringList.stream().distinct().collect(Collectors.toList());
        if(corplist.size()>1){
            throw new BusinessException(NetbankMessageEnum.COMMON_MESSAGE_CORP_ACCORDANCE);
        }
        for (String id : claimDTO.getIds().split(CommonConstant.COMMA_SPLIT)) {
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            bisAccDtlBudgetRuleVO.setProcessDateTime(bisAccDtlBudgetRuleVO.getTransTime());
            bisAccDtlBudgetRuleInnerService.autoRule(bisAccDtlBudgetRuleVO, this.getCurrentUser(),"1");
        }
        return success();
    }
    /**
     * 自动匹配后批量提交
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @ApiOperation("批量自动匹配收款流水认领")
    @LogMessage(content = "批量自动匹配收款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoBatchSubmit")
    @ResponseBody
    public AjaxJson autoBatchSubmit(@RequestBody BisAccDtlBudgetRuleClaimDTO claimDTO) throws Exception {

        for (String id : claimDTO.getIds().split(CommonConstant.COMMA_SPLIT)) {
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            bisAccDtlBudgetRuleVO.setProcessDateTime(bisAccDtlBudgetRuleVO.getTransTime());
            bisAccDtlBudgetRuleInnerService.sure(bisAccDtlBudgetRuleVO, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 批量恢复
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @ApiOperation("批量恢复收款流水认领")
    @LogMessage(content = "批量恢复收款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchRecover")
    @ResponseBody
    public AjaxJson batchRecover(@RequestBody BisAccDtlBudgetRuleClaimDTO claimDTO) throws Exception {
        String[] corpid=claimDTO.getCorpIds().split(",");
        List<String> list= Arrays.asList(corpid);
        List<String> stringList=list.stream().filter(e->!e.equals("")).collect(Collectors.toList());
        List<String> corplist=stringList.stream().distinct().collect(Collectors.toList());
        if(corplist.size()>1){
            throw new BusinessException(NetbankMessageEnum.COMMON_MESSAGE_CORP_ACCORDANCE);
        }
        for (String id : claimDTO.getIds().split(CommonConstant.COMMA_SPLIT)) {
            bisAccDtlBudgetRuleInnerService.business(id,this.getCurrentUser(), BisAccBalConstant.TYPE_H);
        }
        return success();
    }
    /**
     * 是否可以拆分
     *
     * @param id
     * @return
     */
    @ApiOperation("是否可以拆分")
    @GetMapping(path = "/isMergeBill")
    @ResponseBody
    public AjaxJson isMergeBill(String id) {
        AjaxJson json = new AjaxJson();
        boolean flag = bisAccDtlBudgetRuleInnerService.isMergeBill(id);
        json.setSuccess(flag);
        if (flag) {
            json.setMessageEnum(NetbankMessageEnum.NIS_PAY_MESSAGE_MERGE_NOT_SPLIT);
        }
        return json;
    }
    /**
     * 流水拆分
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @ApiOperation("跳转流水拆分前获取数据")
    @GetMapping("/getInfoBeforeSplit")
    @ResponseBody
    public AjaxJson<BisAccDtlBudgetRuleViewPageVO> split(String id) {
        if (StringUtils.isNotEmpty(id)) {
            BisAccDtlBudgetRuleViewPageVO pageVO = new BisAccDtlBudgetRuleViewPageVO();
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            pageVO.setBisAccDtlBudgetRulePage(bisAccDtlBudgetRuleVO);
            return success(pageVO);
        }
        return success();
    }
    /**
     * 拆分保存
     *
     * @param
     * @return
     */
    @ApiOperation("拆分保存")
    @PostMapping("/splitSave")
    @ResponseBody
    public AjaxJson splitSave(@RequestBody BisAccDtlBudgetRuleSplitDTO splitDTO) {
        BisAccDtlDTO t = bisAccDtlService.getById(splitDTO.getId());
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal amt : splitDTO.getAmts()) {
            total = total.add(amt);
        }
        //拆分金额总和必须等于拆分前的付款金额
        if (total.compareTo(t.getAmt()) != 0) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_VALID);
        }
        bisAccDtlBudgetRuleInnerService.split(t, splitDTO.getAmts(), this.getCurrentUser());
        return success();
    }
    /**
     * 撤销拆分
     *
     * @param id
     * @return
     */
    @ApiOperation("撤销拆分")
    @PostMapping("/splitCancel/{id}")
    @ResponseBody
    public AjaxJson splitCancel(@PathVariable("id") String id) {
        bisAccDtlBudgetRuleInnerService.splitCancel(id, this.getCurrentUser());
        return success();
    }
    /**
     * 合并
     *
     * @return
     */
    @ApiOperation("合并")
    @PostMapping("/merge")
    @ResponseBody
    public AjaxJson merge(@RequestBody BisAccDtlBudgetRuleClaimDTO claimDTO) {
        bisAccDtlBudgetRuleInnerService.merge(claimDTO.getIds(), this.getCurrentUser());
        return success();
    }
    /**
     * 撤销合并
     *
     * @return
     */
    @ApiOperation("撤销合并")
    @PostMapping("/mergeCancel")
    @ResponseBody
    public AjaxJson mergeCancel(@RequestBody BisAccDtlBudgetRuleClaimDTO claimDTO) {
        bisAccDtlBudgetRuleInnerService.mergeCancel(claimDTO.getIds(), this.getCurrentUser());
        return success();
    }
    /**
     * 导出全部
     *
     * @return
     */
    @ApiOperation("导出全部")
    @PostMapping("/exportAll")
    public void exportAll(HttpServletResponse response, String dataTablesInputParam, String exportExcelHeadListParam,
                          String totalParam, String fileNameParam) throws Exception {
        ExportUtils.exportByStream(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput<?> dataTablesOutput = bisAccDtlBudgetRuleInnerService.exportAll(dataTablesInput, user);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "银行流水认领",10000, BisAccDtlBudgetRuleVO.class);
    }
    /**
     * 自动匹配
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @ApiOperation("自动匹配付款流水认领")
    @LogMessage(content = "自动匹配付款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoRuleOther")
    @ResponseBody
    public AjaxJson autoRuleOther(@RequestBody BisAccDtlBudgetRuleClaimDTO claimDTO) throws Exception {
        for (String id : claimDTO.getIds().split(CommonConstant.COMMA_SPLIT)) {
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            bisAccDtlBudgetRuleVO.setProcessDateTime(bisAccDtlBudgetRuleVO.getTransTime());
            bisAccDtlBudgetRuleInnerService.autoRuleOther(bisAccDtlBudgetRuleVO, this.getCurrentUser());
        }
        return success();
    }
}
