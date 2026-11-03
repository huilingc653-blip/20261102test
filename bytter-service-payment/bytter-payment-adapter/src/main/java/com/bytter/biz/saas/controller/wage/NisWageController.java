package com.bytter.biz.saas.controller.wage;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.biz.bdlink.api.constant.BdlinkConstant;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.dto.BisAccBalDTO;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.bdlink.api.service.IBisAccBalService;
import com.bytter.biz.netbank.entity.BtChargeBackType;
import com.bytter.biz.netbank.service.BtChargeBackTypeService;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.constant.WageConstant;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.payment.api.service.IBisExcService;
import com.bytter.biz.wage.entity.NisWage;
import com.bytter.biz.wage.service.NisWageBatchService;
import com.bytter.biz.wage.service.NisWageDetailService;
import com.bytter.biz.wage.service.NisWageService;
import com.bytter.biz.wage.vo.NisWageBatchVO;
import com.bytter.biz.wage.vo.NisWageDetailVO;
import com.bytter.biz.wage.vo.NisWageUpdateVO;
import com.bytter.biz.wage.vo.NisWageVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.util.ExportUtils;
import com.bytter.system.api.IAccountGrantService;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.AccountGrantConstant;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.BtBankAccDTO;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Title: Controller
 * @Description: NIS_WAGE
 * @date 2018-01-08 17:19:13
 *
 */
@Api(value = "/v1-payment/wage/niswage", tags = "代发工资")
@RestController("v1NisWageController")
@RequestMapping("/v1-payment/wage/niswage")
public class NisWageController extends BaseController {
    @Autowired
    private NisWageService nisWageService;
    @Autowired
    private NisWageBatchService nisWageBatchService;
    @Autowired
    private NisWageDetailService nisWageDetailService;
    @DubboReference(check = false)
    private IAccountGrantService accountGrantService;
    @DubboReference(check = false)
    private IConfigService configService;
    @Autowired
    private BtChargeBackTypeService btChargeBackTypeService;
    @DubboReference(check = false)
    private IBisExcService bisExcService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IBisAccBalService bisAccBalService;

    /**
     * NIS_WAGE列表 页面跳转
     *
     * @return
     */
    @ApiOperation("NIS_WAGE列表页面跳转")
    @LogMessage(content = "查询NIS_WAGE列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public AjaxJson<NisWageUpdateVO> list() {
        NisWageUpdateVO model = new NisWageUpdateVO();
        String chargeBackType = "";
        BtChargeBackType btChargeBackType = btChargeBackTypeService.findByDef();
        if (StringUtils.isNotEmpty(btChargeBackType)) {
            chargeBackType = btChargeBackType.getBusType();
        }
        model.setChargeBackType(chargeBackType);
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        model.setBankaccOver(configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BANKACCOVER).getValue());
        model.setBudgetSel(configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BUDGETSEL).getValue());
        return AjaxJson.success(model);
    }


    /**
     * NIS_WAGE table列表
     *
     * @return
     */
    @ApiOperation("NIS_WAGE table列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisWageVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisWageService.getDataTablesUnionNew(input, this.getCurrentUser()));
    }

    /**
     * NIS_WAGE编辑页面跳转
     *
     * @return
     */
    @ApiOperation("NIS_WAGE编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisWageUpdateVO> update(@PathVariable("id") String id) {
        NisWageUpdateVO vo = new NisWageUpdateVO();
        if (StringUtils.isNotEmpty(id)) {
            NisWageVO nisWage = nisWageService.getByIdUnion(id);
            //暂存和编辑状态才可以提交
            if (nisWage != null && !NetbankConstant.STATUE_TEMP.equals(nisWage.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisWage.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisWage.getWageNo());
            }

            vo.setNisWagePage(nisWage);
        }

        vo.setWishPayday(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        vo.setUser(this.getUserLoginName());
        //初始化参数
        this.initConfig(vo);
        return AjaxJson.success(vo);
    }

    /**
     * NIS_WAGE详情页
     *
     * @return
     */
    @ApiOperation("NIS_WAGE详情页")
    @LogMessage(content = "查询NIS_WAGE详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisWageUpdateVO> view(@PathVariable("id") String id) {
        NisWageUpdateVO model = new NisWageUpdateVO();
        NisWageVO nisWage = nisWageService.getByIdUnion(id);
        //判断是否经过银行处理
        String isPay = CommonConstant.NO;
        List<BisExcDTO> bisExcDTOList = this.bisExcService.getBisExcByCbsBillNoForNisWage(nisWage.getWageNo(), BdlinkConstant.FROM_NIS_WAGE);
        if (EmptyUtils.isNotEmpty(bisExcDTOList)) {
            //审批通过的
            nisWageDetailService.setNisWagePayResult(nisWage);
            isPay = CommonConstant.YES;
        }
        model.setIsPay(isPay);
        model.setNisWagePage(nisWage);
        model.setBusType(NisMessageConstant.BUS_TYPE_WAGE);
        model.setUser(this.getUserLoginName());
        //初始化参数
        this.initConfig(model);
        return AjaxJson.success(model);
    }

    /**
     * 添加NIS_WAGE
     *
     * @param nisWage
     * @return
     */
    @ApiOperation("添加NIS_WAGE")
    @LogMessage(content = "添加NIS_WAGE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody NisWage nisWage) {
        nisWageService.save(nisWage);
        return success();
    }

    /**
     * 修改NIS_WAGE
     *
     * @param nisWage
     * @return
     */
    @ApiOperation("修改NIS_WAGE")
    @LogMessage(content = "修改NIS_WAGE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody NisWageVO nisWage) {
        nisWageService.updateNisWage(nisWage, this.getCurrentUser());
        return success();
    }

    /**
     * 批量提交付款通知表
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量提交付款通知表")
    @LogMessage(content = "批量提交代发工资", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        nisWageService.batchSubmitNisWage(ids, this.getCurrentUser());
        return success();
    }

    /**
     * 删除NIS_WAGE
     *
     * @param id
     * @return
     */
    @ApiOperation("删除NIS_WAGE")
    @LogMessage(content = "删除NIS_WAGE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}/{chargeBackType}")
    public AjaxJson delete(@PathVariable("id") String id, @PathVariable("chargeBackType") String chargeBackType) {
        nisWageService.deleteNisWage(id, this.getCurrentUser(), chargeBackType);
        return success();
    }

    /**
     * 批量删除NIS_WAGE
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量删除NIS_WAGE")
    @LogMessage(content = "批量删除NIS_WAGE", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@RequestBody Map<String,Object> map) {
        //@RequestParam("ids") String ids, @RequestParam("chargeBackType") String chargeBackType
        String ids = map.get("ids") == null ? null : map.get("ids").toString();
        String chargeBackType = map.get("chargeBackType") == null ? null : map.get("chargeBackType").toString();
        nisWageService.batchDelNisWage(ids, this.getCurrentUser(), chargeBackType);
        return success();
    }

    /**
     * 保存代发工资导入数据
     * 代发工资
     *
     * @param list
     * @return
     */
    @ApiOperation("保存代发工资导入数据")
    @PostMapping("/saveWageImport")
    public AjaxJson saveWageImport(@RequestBody List<NisWageVO> list) {
        String billNo = "";
        billNo = this.nisWageService.saveImport(list, this.getCurrentUser());
        return success(billNo);
    }

    /**
     * 保存代发工资导入数据(批量导入)
     *
     * @param list
     * @return
     */
    @ApiOperation("保存代发工资导入数据(批量导入)")
    @PostMapping("/saveWageImportBatch")
    public AjaxJson saveWageImportBatch(@RequestBody List<NisWageBatchVO> list) {
        this.nisWageBatchService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 获取付款账号
     *
     * @param corpId
     * @return
     */
    @ApiOperation("获取付款账号")
    @PostMapping("/getPayerAcc")
    public AjaxJson<List<BtBankAccDTO>> getPayerAcc(String corpId) {
        List<BtBankAccDTO> listAcc = new ArrayList<BtBankAccDTO>();
        if (!StringUtils.isEmpty(corpId)) {
            //获取单位信息
            CorpDTO corp = corpService.get(corpId);
            String accountCorpId = StringUtils.isBlank(corp.getUserAccountCode()) ? corp.getId() : corp.getUserAccountCode();
            //获取转账权限的银行在线账号
            List<BtBankAccDTO> listOnlineBankAcc = accountGrantService.listOnlineBankAccount(accountCorpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            //获取转账权限的银行离线账号
            List<BtBankAccDTO> listOfflineBankAcc = accountGrantService.listOfflineBankAccount(accountCorpId, AccountGrantConstant.TRANSFER_ACCOUNT);
            if (!EmptyUtils.isEmptyList(listOnlineBankAcc)) {
                listAcc.addAll(listOnlineBankAcc);
            }
            if (!EmptyUtils.isEmptyList(listOfflineBankAcc)) {
                listAcc.addAll(listOfflineBankAcc);
            }
        }
        return AjaxJson.success(listAcc);
    }

    /**
     * 打回代发工资
     *
     * @param id
     * @return
     */
    @ApiOperation("打回代发工资")
    @LogMessage(content = "修改代发工资表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back/{id}")
    public AjaxJson back(@PathVariable("id") String id) {
        nisWageService.backNisWage(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量打回代发工资
     *
     * @param ids
     * @return
     */
    @ApiOperation("批量打回代发工资")
    @LogMessage(content = "修改代发工资表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchBack")
    public AjaxJson batchBack(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            nisWageService.backNisWage(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 初始化系统参数
     *
     * @param model
     * @param model
     */
    private void initConfig(NisWageUpdateVO model) {
        Map<String, Object> wageMap = (Map<String, Object>) configService.listByType(WageConstant.WAGE_CONFIG_TYPE).get("wage");
        if(!wageMap.isEmpty()){
            model.setWagesVerify(String.valueOf(wageMap.get(WageConstant.WAGE_CONFIG_WAGESVERIFY)));
            model.setCreator(String.valueOf(wageMap.get(WageConstant.WAGE_CONFIG_CREATOR)));
            model.setBankaccOver(String.valueOf(wageMap.get(WageConstant.WAGE_CONFIG_BANKACCOVER)));
        }
        model.setBudgetSel(configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_BUDGETSEL).getValue());
        model.setPayItem(configService.getConfig(WageConstant.WAGE_CONFIG_TYPE, WageConstant.WAGE_CONFIG_PAYITEM).getValue());
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_WAGE);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @ApiOperation("加载审核列表数据")
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return AjaxJson.success(this.nisWageService.dataTableApprove(dataTablesInput));
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @return String
     */
    @ApiOperation("跳转至审核详情界面")
    @GetMapping("/approve")
    public AjaxJson<NisWageUpdateVO> approve(String id, String taskId) {
        NisWageUpdateVO model = new NisWageUpdateVO();
        if (StringUtils.isNotEmpty(id)) {
            this.setApprove(id, taskId, model);
        }
        model.setUser(this.getUserLoginName());
        return AjaxJson.success(model);
    }

    /**
     * 跳转至手机审核详情界面
     *
     * @param businessId
     * @param taskId
     * @return String
     */
    @ApiOperation("跳转至手机审核详情界面")
    @GetMapping("/mobileApprove")
    public AjaxJson<NisWageUpdateVO> mobileApprove(String businessId, String taskId) {
        NisWageUpdateVO model = new NisWageUpdateVO();
        if (StringUtils.isNotEmpty(businessId)) {
            this.setApprove(businessId, taskId, model);
        }
        return AjaxJson.success(model);
    }

    public void setApprove(String businessId, String taskId, NisWageUpdateVO model) {
        NisWageVO nisWageVO = this.nisWageService.getByUnion(businessId);
        //判断是否经过银行处理
        String isPay = CommonConstant.NO;
        List<BisExcDTO> bisExcDTOList = this.bisExcService.getBisExcByCbsBillNoForNisWage(nisWageVO.getWageNo(), BdlinkConstant.FROM_NIS_WAGE);
        if (EmptyUtils.isNotEmpty(bisExcDTOList)) {
            //审批通过的
            nisWageDetailService.setNisWagePayResult(nisWageVO);
            isPay = CommonConstant.YES;
        }
        BigDecimal bal = BigDecimal.ZERO;
        Integer count = 0;
        BisAccBalDTO bisAccBalDTO = bisAccBalService.getLastBisAccBalBy(nisWageVO.getPayerAcc(), nisWageVO.getCurId());
        if (!EmptyUtils.isEmpty(bisAccBalDTO)) {
            if (!EmptyUtils.isEmpty(bisAccBalDTO.getAvailBal())) {
                bal = bisAccBalDTO.getAvailBal();
            }
        }
        List<NisWageDetailVO> list = nisWageDetailService.listByWageId(nisWageVO.getId());
        if (EmptyUtils.isNotEmpty(list)) {
            count = list.size();
        }
        model.setIsPay(isPay);
        model.setBal(bal);
        model.setCount(count);
        model.setNisWagePage(nisWageVO);
        model.setTaskId(taskId);
        //初始化参数
        this.initConfig(model);
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_WAGE);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        model.setBusType(NisMessageConstant.BUS_TYPE_WAGE);
    }

    /**
     * 对私付款单明细金额与付款金额校验
     *
     * @param ids
     * @return
     */
    @ApiOperation("对私付款单明细金额与付款金额校验")
    @PostMapping("validSuccessWageDetail")
    public AjaxJson validSuccessWageDetail(String ids) {
        AjaxJson json = new AjaxJson();
        String wageNo = "";
        //对私付款校验明细总金额与指令发送金额是否一致
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisWageVO wageVO = this.nisWageService.getByIdUnion(id);
            //暂存数据不需要校验其他东西
            if(wageVO.getStatus() == 0 || wageVO.getStatus() == -1){
                wageNo = wageVO.getWageNo() + CommonConstant.COMMA_SPLIT;
                continue;
            }
            List<NisWageDetailVO> successWagetDtl = nisWageDetailService.listByWageIdSuccess(id);
            if (EmptyUtils.isNotEmpty(successWagetDtl)) {
                wageNo = wageVO.getWageNo() + CommonConstant.COMMA_SPLIT;
            }
        }
        if (!StringUtils.isBlank(wageNo)) {
            json.setObj(wageNo.substring(0, wageNo.length() - 1));
            json.setSuccess(true);
        }
        return json;
    }

    /**
     * 导出所选明细
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ApiOperation("导出所选明细")
    @GetMapping("/exportDtl")
    public void exportDtl(HttpServletRequest request, HttpServletResponse response, String ids, String opType) throws Exception {
        this.nisWageService.exportDtl(request, response, ids, opType, this.getCurrentUser());
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
        ExportUtils.exportByStreamCopy(response, dataTablesInputParam, exportExcelHeadListParam, new ExportUtils.ExportData() {
            @Override
            public List<?> getData(DataTablesInput dataTablesInput, ICurrentUser user) {
                DataTablesOutput<NisWageVO> dataTablesOutput = nisWageService.getDataTablesUnionNew(dataTablesInput, user);
                return dataTablesOutput.getData();
            }
        }, totalParam, fileNameParam, 5000, NisWageVO.class);
    }

}
