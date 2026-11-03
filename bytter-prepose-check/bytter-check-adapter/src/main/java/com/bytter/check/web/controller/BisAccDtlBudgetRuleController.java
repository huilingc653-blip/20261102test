package com.bytter.check.web.controller;
import com.bytter.biz.bdlink.api.dto.BisAccDtlBudgetRuleDTO;
import com.bytter.biz.bdlink.api.dto.BisAccDtlDTO;
import com.bytter.biz.payment.api.dto.NisBusTypeDTO;
import com.bytter.biz.payment.api.dto.NisReceivableDTO;
import com.bytter.biz.bdlink.api.service.IBisAccDtlService;
import com.bytter.biz.payment.api.service.INisBusTypeService;
import com.bytter.biz.payment.api.service.INisReceivableService;
import com.bytter.biz.bdlink.vo.BisAccDtlBudgetRuleVO;
import com.bytter.check.service.BisAccDtlBudgetRuleInnerService;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.*;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.enums.NetbankMessageEnum;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
 * @Description: 银行流水资金计划明细表
 * @date 2018-03-13 11:20:30
 */
@Controller
@RequestMapping("/bankacc/bisaccdtlbudgetrule")
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
    /**
     * 银行流水资金计划明细表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @LogMessage(content = "查询银行流水明细表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("budgetSel", configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_BUDGETSEL).getValue());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BISACCDTL);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        //是否启用拆分
        model.addAttribute("isEnableSplit", configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLESPLIT).getValue());
        //是否启用合并
        model.addAttribute("isEnableMerge", configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_ISENABLEMERGE).getValue());
        //是否认领到业务类别
        model.addAttribute("isClaimBusType", configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_ISCLAIMBUSTYPE).getValue());
        //交易时间默认一周
        model.addAttribute("beginTransTime", DateUtils.addDay(DateUtils.getDate(), -7));
        return "bankacc/listBisAccDtlBudgetRule";
    }
    /**
     * 银行流水资金计划明细表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisAccDtlBudgetRuleInnerService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }
    /**
     * 银行流水资金计划明细表列表 页面跳转
     *
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @GetMapping("/sureView")
    public String sureView(String id,String tableName, Model model) {
        BisAccDtlBudgetRuleDTO bisAccDtl = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
        if (EmptyUtils.isEmpty(bisAccDtl.getProcessDate())) {
            bisAccDtl.setProcessDate("");
        }
        model.addAttribute("bisAccDtl", bisAccDtl);
        model.addAttribute("budgetSel", configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_BUDGETSEL).getValue());
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BISACCDTL);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        //是否认领到业务类别
        model.addAttribute("isClaimBusType", configService.getConfig(NetbankConstant.RECEIVABLE_CONFIG_TYPE, NetbankConstant.RECEIVABLE_CONFIG_ISCLAIMBUSTYPE).getValue());
        if("appTable".equals(tableName)) {
            return "bankacc/sureBisAccDtlBudgetRule";
        }else{
            return "bankacc/sureBisAccDtlBudgetRuleOther";
        }
    }
    /**
     * 确认
     *
     * @param bisAccDtlBudgetRuleVO
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "流水挂资金计划", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/sure")
    @ResponseBody
    public AjaxJson sure(BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO) {
        bisAccDtlBudgetRuleInnerService.sure(bisAccDtlBudgetRuleVO, this.getCurrentUser());
        return success();
    }
    /**
     * 批量确认计划
     *
     * @param ids
     * @param type        Y 确认 D 删除
     * @param itemCode    科目代码
     * @param itemName    科目名称
     * @param processDate 执行时间
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "批量处理银行流水", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batch")
    @ResponseBody
    public AjaxJson batchBusiness(@RequestParam("ids") String ids, @RequestParam("type") String type, @RequestParam("itemCode") String itemCode,
                                  @RequestParam("itemName") String itemName, @RequestParam("processDate") String processDate, @RequestParam("settlType") String settlType,
                                  @RequestParam("billNos") String billNos, @RequestParam("batchBusType") String batchBusType, @RequestParam("rmks") String rmks) {
        bisAccDtlBudgetRuleInnerService.batchBusiness(ids, itemCode, itemName, processDate, settlType, this.getCurrentUser(), type, billNos, batchBusType, rmks);
        return success();
    }
    /**
     * 处理逻辑
     *
     * @param id
     * @param type H 恢复 1还原 2删除
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
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
     * @param model
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @LogMessage(content = "查询资金计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
        model.addAttribute("bisAccDtlBudgetRulePage", bisAccDtlBudgetRuleVO);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BISACCDTL);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        return "bankacc/viewBisAccDtlBudgetRule";
    }
    /**
     * 查询银行流水详情
     *
     * @param id
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_QUERY)
    @LogMessage(content = "查询资金计划详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewDtl/{id}")
    public String viewDtl(@PathVariable("id") String id, Model model) {
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
            model.addAttribute("dtlList", dtlList);
        }
        model.addAttribute("bisAccDtlBudgetRulePage", bisAccDtlBudgetRuleVO);
        //资金计划
        model.addAttribute("businessName", BudgetConstant.BUSINESS_NAME_BISACCDTL);
        model.addAttribute("processSysname", BudgetConstant.PROCESS_SYSNAME_NIS);
        return "bankacc/viewBisAccDtlBudgetRule1";
    }
    /**
     * 自动匹配
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "自动匹配收款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoRule")
    @ResponseBody
    public AjaxJson autoRule(@RequestParam("ids") String ids,@RequestParam("corpIds") String corpIds) throws Exception {
        String[] corpid=corpIds.split(",");
        List<String> list= Arrays.asList(corpid);
        List<String> stringList=list.stream().filter(e->!e.equals("")).collect(Collectors.toList());
        List<String> corplist=stringList.stream().distinct().collect(Collectors.toList());
        if(corplist.size()>1){
            throw new BusinessException(NetbankMessageEnum.COMMON_MESSAGE_CORP_ACCORDANCE);
        }
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            bisAccDtlBudgetRuleVO.setProcessDateTime(bisAccDtlBudgetRuleVO.getTransTime());
            bisAccDtlBudgetRuleInnerService.autoRule(bisAccDtlBudgetRuleVO, this.getCurrentUser(),"1");
        }
        return success();
    }
    /**
     * 自动匹配后批量提交
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "自动匹配收款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoBatchSubmit")
    @ResponseBody
    public AjaxJson autoBatchSubmit(@RequestParam("ids") String ids) throws Exception {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            bisAccDtlBudgetRuleVO.setProcessDateTime(bisAccDtlBudgetRuleVO.getTransTime());
            bisAccDtlBudgetRuleInnerService.sure(bisAccDtlBudgetRuleVO, this.getCurrentUser());
        }
        return success();
    }
    /**
     * 批量恢复
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "批量恢复收款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchRecover")
    @ResponseBody
    public AjaxJson batchRecover(@RequestParam("ids") String ids,@RequestParam("corpIds") String corpIds) throws Exception {
        String[] corpid=corpIds.split(",");
        List<String> list= Arrays.asList(corpid);
        List<String> stringList=list.stream().filter(e->!e.equals("")).collect(Collectors.toList());
        List<String> corplist=stringList.stream().distinct().collect(Collectors.toList());
        if(corplist.size()>1){
            throw new BusinessException(NetbankMessageEnum.COMMON_MESSAGE_CORP_ACCORDANCE);
        }
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
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
    @RequestMapping(params = "isMergeBill")
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
     * @param model
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @GetMapping("/split")
    public String split(String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            model.addAttribute("bisAccDtlBudgetRulePage", bisAccDtlBudgetRuleVO);
        }
        return "bankacc/splitBisAccDtlBudgetRule";
    }
    /**
     * 拆分保存
     *
     * @param
     * @return
     */
    @PostMapping("/splitSave")
    @ResponseBody
    public AjaxJson splitSave(String id, @RequestParam("amts[]") List<BigDecimal> amts) {
        BisAccDtlDTO t = bisAccDtlService.getById(id);
        BigDecimal total = BigDecimal.ZERO;
        for (BigDecimal amt : amts) {
            total = total.add(amt);
        }
        //拆分金额总和必须等于拆分前的付款金额
        if (total.compareTo(t.getAmt()) != 0) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_SPLIT_VALID);
        }
        bisAccDtlBudgetRuleInnerService.split(t, amts, this.getCurrentUser());
        return success();
    }
    /**
     * 撤销拆分
     *
     * @param id
     * @return
     */
    @PostMapping("/splitCancel/{id}")
    @ResponseBody
    public AjaxJson splitCancel(@PathVariable("id") String id) {
        bisAccDtlBudgetRuleInnerService.splitCancel(id, this.getCurrentUser());
        return success();
    }
    /**
     * 合并
     *
     * @param ids
     * @return
     */
    @PostMapping("/merge")
    @ResponseBody
    public AjaxJson merge(@RequestParam("ids") String ids) {
        bisAccDtlBudgetRuleInnerService.merge(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 撤销合并
     *
     * @param ids
     * @return
     */
    @PostMapping("/mergeCancel")
    @ResponseBody
    public AjaxJson mergeCancel(@RequestParam("ids") String ids) {
        bisAccDtlBudgetRuleInnerService.mergeCancel(ids, this.getCurrentUser());
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
                DataTablesOutput<?> dataTablesOutput = bisAccDtlBudgetRuleInnerService.exportAll(dataTablesInput, user);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, "银行流水认领",10000, BisAccDtlBudgetRuleVO.class);
    }
    /**
     * 自动匹配
     *
     * @param ids
     * @return
     */
    //////@RequiresPermissions(ResourceConstant.NETBANK_BANKACC_BISACCDTLBUDGETRULE_APPLY)
    @LogMessage(content = "自动匹配付款流水认领", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/autoRuleOther")
    @ResponseBody
    public AjaxJson autoRuleOther(@RequestParam("ids") String ids) throws Exception {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            BisAccDtlBudgetRuleDTO bisAccDtlBudgetRuleVO = bisAccDtlBudgetRuleInnerService.getBisAccDtlByIdUnion(id);
            bisAccDtlBudgetRuleVO.setProcessDateTime(bisAccDtlBudgetRuleVO.getTransTime());
            bisAccDtlBudgetRuleInnerService.autoRuleOther(bisAccDtlBudgetRuleVO, this.getCurrentUser());
        }
        return success();
    }
}
