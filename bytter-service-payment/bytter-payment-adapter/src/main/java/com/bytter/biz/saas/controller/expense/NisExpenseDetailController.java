package com.bytter.biz.saas.controller.expense;

import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
import com.bytter.biz.expense.entity.NisExpenseDetail;
import com.bytter.biz.expense.service.NisExpenseDetailService;
import com.bytter.biz.expense.vo.NisExpenseDetailIniVO;
import com.bytter.biz.expense.vo.NisExpenseDetailVO;
import com.bytter.biz.payment.api.constant.ExpenseConstant;
import com.bytter.biz.payment.api.constant.NisMessageConstant;
import com.bytter.biz.payment.api.enums.ExpenseMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IConfigService;
import com.bytter.system.api.ICorpService;
import com.bytter.system.api.constant.SysConfigConstant;
import com.bytter.system.api.dto.CorpDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 费用报销表
 * @date 2017-05-31 16:04:01
 */
@Api(value = "/v1-payment/expense/nisexpensedetail", tags = "费用报销")
@RestController("v1NisExpenseDetailController")
@RequestMapping("/v1-payment/expense/nisexpensedetail")
public class NisExpenseDetailController extends BaseController {
    @Autowired
    private NisExpenseDetailService nisExpenseDetailService;
    @DubboReference(check = false)
    private ICorpService corpService;
    @DubboReference(check = false)
    private IConfigService configService;

    /**
     * 费用报销表 table列表
     *
     * @return
     */
	@ApiOperation("费用报销表 table列表")
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<NisExpenseDetailVO>> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisExpenseDetailService.getDataTablesUnion(input, this.getCurrentUser()));
    }

    /**
     * 费用报销表新增页面跳转
     *
     * @return String
     */
	@ApiOperation("费用报销表新增页面跳转")
    @GetMapping("/add")
    public AjaxJson<NisExpenseDetailIniVO> add() {
        //初始化系统参数
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        this.initConfig(model);
        return AjaxJson.success(model);
    }

    /**
     * 费用报销表编辑页面跳转
     *
     * @return String
     */
	@ApiOperation("费用报销表编辑页面跳转")
    @GetMapping("/update/{id}")
    public AjaxJson<NisExpenseDetailIniVO> update(@PathVariable("id") String id) {
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        if (StringUtils.isNotEmpty(id)) {
            NisExpenseDetail nisExpenseDetail = nisExpenseDetailService.get(id, true);
            //暂存和编辑状态才可以进行该操作
            if (nisExpenseDetail != null && !NetbankConstant.STATUE_TEMP.equals(nisExpenseDetail.getStatus()) && !NetbankConstant.STATUE_BACK.equals(nisExpenseDetail.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
            }
            NisExpenseDetailVO nisExpenseDetailVO = new NisExpenseDetailVO();
            if (nisExpenseDetail != null) {
                BeanUtils.copyProperties(nisExpenseDetail, nisExpenseDetailVO);
                model.setNisExpenseDetailPage(nisExpenseDetailVO);
            }
        }
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_EXPENSE);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        //初始化系统参数
        this.initConfig(model);
        return AjaxJson.success(model);
    }

    /**
     * 初始化系统参数
     *
     * @param model
     */
    private void initConfig(NisExpenseDetailIniVO model) {
        model.setBudgetSel(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BUDGETSEL).getValue());
        model.setPayItem(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_PAYITEM).getValue());
        model.setExpFbsin(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_EXPFBSIN).getValue());
        model.setAccountOver(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_ACCOUNTOVER).getValue());
        model.setBankaccOver(configService.getConfig(ExpenseConstant.EXP_CONFIG_TYPE, ExpenseConstant.EXP_CONFIG_BANKACCOVER).getValue());
        model.setShowIsPayed(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
    }

    /**
     * 费用报销表详情页
     *
     * @return String
     */
	@ApiOperation("费用报销表详情页")
    @LogMessage(content = "查询费用报销表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public AjaxJson<NisExpenseDetailIniVO> view(@PathVariable("id") String id) {
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
        model.setNisExpenseDetailPage(nisExpenseDetail);
        //初始化系统参数
        this.initConfig(model);
        //资金计划
        model.setBusinessName(BudgetConstant.BUSINESS_NAME_EXPENSE);
        model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
        model.setBusType(NisMessageConstant.BUS_TYPE_EXPENSE);
        return AjaxJson.success(model);
    }

    /**
     * 添加费用报销表
     * 费用报销
     *
     * @param nisExpenseDetail
     * @return AjaxJson
     */
	@ApiOperation("添加费用报销表")
    @LogMessage(content = "添加费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    public AjaxJson save(@RequestBody NisExpenseDetailVO nisExpenseDetail) {
        if (dataCheck(nisExpenseDetail)) {
            nisExpenseDetailService.saveExpenseDetail(nisExpenseDetail, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 修改费用报销表
     *
     * @param nisExpenseDetail
     * @return
     */
	@ApiOperation("修改费用报销表")
    @LogMessage(content = "修改费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    public AjaxJson modify(@RequestBody NisExpenseDetailVO nisExpenseDetail) {
        if (dataCheck(nisExpenseDetail)) {
            nisExpenseDetailService.updateExpenseDetail(nisExpenseDetail, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 数据校验
     *
     * @param nisExpenseDetail
     * @return boolean
     */
    private boolean dataCheck(NisExpenseDetailVO nisExpenseDetail) {
        //账号去空格
        if (StringUtils.isNotBlank(nisExpenseDetail.getBankAcc())) {
            nisExpenseDetail.setBankAcc(nisExpenseDetail.getBankAcc().trim());
        }
        //单位不能为空
        if (StringUtils.isEmpty(nisExpenseDetail.getCorpId())) {
            throw new ParameterException(ExpenseMessageEnum.NIS_EXPENSE_MESSAGE_CORP_NULL);
        }
        //银行卡类型不能为空
        if (StringUtils.isEmpty(nisExpenseDetail.getCardType())) {
            throw new ParameterException(ExpenseMessageEnum.NIS_EXPENSE_MESSAGE_CARDTYPE_NULL);
        }
        //状态校验
        if (StringUtils.isNotEmpty(nisExpenseDetail.getId())) {
            NisExpenseDetail entity = nisExpenseDetailService.get(nisExpenseDetail.getId(), true);
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getExpenseId());
            }
        }
        return true;
    }

    /**
     * 删除费用报销表
     *
     * @param id
     * @return
     */
	@ApiOperation("删除费用报销表")
    @LogMessage(content = "删除费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        NisExpenseDetail entity = nisExpenseDetailService.get(id);
        //暂存和编辑状态才可以提交
        if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
            throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getExpenseId());
        }
        nisExpenseDetailService.delExpenseDetail(id, this.getCurrentUser());
        return success();
    }

    /**
     * 批量删除费用报销表
     *
     * @param ids
     * @return
     */
	@ApiOperation("批量删除费用报销表")
    @LogMessage(content = "批量删除费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisExpenseDetail entity = nisExpenseDetailService.get(id);
            //暂存和编辑状态才可以提交
            if (entity != null && !NetbankConstant.STATUE_TEMP.equals(entity.getStatus()) && !NetbankConstant.STATUE_BACK.equals(entity.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, entity.getExpenseId());
            }
            nisExpenseDetailService.delExpenseDetail(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 批量提交费用报销表
     *
     * @param ids
     * @return
     */
	@ApiOperation("批量提交费用报销表")
    @LogMessage(content = "批量提交费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        boolean flag = false;
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisExpenseDetailVO nisExpenseDetailVO = nisExpenseDetailService.getByIdUnion(id);
            flag = dataCheck(nisExpenseDetailVO);
        }
        if (flag) {
            nisExpenseDetailService.batchSubmitExpenseDetail(ids, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 费用报销表汇总页面跳转
     *
     * @return String
     */
	@ApiOperation("费用报销表汇总页面跳转")
    @GetMapping("/collect")
    public AjaxJson<NisExpenseDetailIniVO> collect(@RequestParam("ids") String ids) {
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        List<NisExpenseDetailVO> listNisExpenseDetail = new ArrayList<NisExpenseDetailVO>();
        BigDecimal payMoney = BigDecimal.ZERO;
        String corpId = "";
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
            //审批通过状态才可以提交
            if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
            }
            listNisExpenseDetail.add(nisExpenseDetail);
            payMoney = payMoney.add(nisExpenseDetail.getExpenseMoney());
            corpId = nisExpenseDetail.getCorpId();
        }
        //汇总明细列表
        model.setListNisExpenseDetail(listNisExpenseDetail);
        //汇总金额
        model.setPayMoney(payMoney);
        model.setWishPayday(DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //所用账号单位代码
        CorpDTO corp = corpService.get(corpId);
        if (StringUtils.isEmpty(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        //汇总单位
        model.setCorp(corp);
        //初始化系统参数
        this.initConfig(model);
        return AjaxJson.success(model);
    }

    /**
     * 批量打回费用报销表
     *
     * @param ids
     * @return
     */
	@ApiOperation("批量打回费用报销表")
    @LogMessage(content = "批量修改费用报销表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/back")
    public AjaxJson back(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
            //审批通过状态才可以打回
            if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
            }
            nisExpenseDetailService.backExpenseDetail(id, this.getCurrentUser());
        }
        return success();
    }

    /**
     * 费用报销表处理列表 页面跳转
     *
     * @return
     */
	@ApiOperation("费用报销表处理列表 页面跳转")
    @LogMessage(content = "查询费用报销表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/listDeal")
    public AjaxJson<NisExpenseDetailIniVO> listDeal() {
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        model.setShowIsPayed(configService.getConfig(NetbankConstant.NIS_CONFIG_TYPE, NetbankConstant.NIS_CONFIG_SHOWISPAYED).getValue());
        //是否启用限额管理  1-是 0-否
        model.setIsOpenLimit(configService.getConfig(SysConfigConstant.Module.SYS, SysConfigConstant.ISOPENLIMIT).getValue());
        return AjaxJson.success(model);
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
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(this.nisExpenseDetailService.dataTableApprove(input));
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
    public AjaxJson<NisExpenseDetailIniVO> approve(String id, String taskId) {
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        if (StringUtils.isNotEmpty(id)) {
            NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
            model.setNisExpenseDetailPage(nisExpenseDetail);
            model.setTaskId(taskId);
            //初始化系统参数
            this.initConfig(model);
            //资金计划
            model.setBusinessName(BudgetConstant.BUSINESS_NAME_EXPENSE);
            model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
            model.setBusType(NisMessageConstant.BUS_TYPE_EXPENSE);
        }
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
    public AjaxJson<NisExpenseDetailIniVO> mobileApprove(String businessId, String taskId) {
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        if (StringUtils.isNotEmpty(businessId)) {
            NisExpenseDetailVO nisExpenseDetailVO = this.nisExpenseDetailService.getByUnion(businessId);
            model.setNisExpenseDetailPage(nisExpenseDetailVO);
            model.setBusType(taskId);
            //初始化系统参数
            this.initConfig(model);
            //资金计划
            model.setBusinessName(BudgetConstant.BUSINESS_NAME_EXPENSE);
            model.setProcessSysname(BudgetConstant.PROCESS_SYSNAME_NIS);
            model.setBusType(NisMessageConstant.BUS_TYPE_EXPENSE);
        }
        return AjaxJson.success(model);
    }

    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
	@ApiOperation("保存导入数据")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<NisExpenseDetailVO> list) {
        this.nisExpenseDetailService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     * 费用报销表汇总页面跳转
     *
     * @return String
     */
	@ApiOperation("费用报销表汇总页面跳转")
    @GetMapping("/collectTests")
    public AjaxJson<NisExpenseDetailIniVO> collectTests() {
        NisExpenseDetailIniVO model = new NisExpenseDetailIniVO();
        //初始化系统参数
        this.initConfig(model);
        return AjaxJson.success(model);
    }

    /**
     * 根据明细ID获取汇总数据信息
     *
     * @param ids
     * @return
     */
	@ApiOperation("根据明细ID获取汇总数据信息")
    @RequestMapping(params = "collectData")
    public AjaxJson collectData(String ids) {
        AjaxJson j = new AjaxJson();
        j.setSuccess(true);
        List<NisExpenseDetailVO> listNisExpenseDetail = new ArrayList<NisExpenseDetailVO>();
        BigDecimal payMoney = BigDecimal.ZERO;
        String corpId = "";
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            NisExpenseDetailVO nisExpenseDetail = nisExpenseDetailService.getByIdUnion(id);
            //审批通过状态才可以提交
            if (nisExpenseDetail != null && !NetbankConstant.STATUE_CHECK.equals(nisExpenseDetail.getStatus())) {
                throw new ParameterException(NetbankMessageEnum.NIS_PAY_MESSAGE_STATUS_CHANGE, nisExpenseDetail.getExpenseId());
            }
            listNisExpenseDetail.add(nisExpenseDetail);
            payMoney = payMoney.add(nisExpenseDetail.getExpenseMoney());
            corpId = nisExpenseDetail.getCorpId();
        }
        //汇总明细列表
        HashMap<String, Object> map = new HashMap<>();
        map.put("listNisExpenseDetail", listNisExpenseDetail);
        //汇总金额
        map.put("payMoney", payMoney);
        map.put("wishPayday", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        //所用账号单位代码
        CorpDTO corp = corpService.get(corpId);
        if (StringUtils.isEmpty(corp.getUserAccountCode())) {
            corp.setUserAccountCode(corp.getId());
        }
        //汇总单位
        map.put("corp", corp);
        j.setObj(map);
        return j;
    }

    /**
     * 汇总数据信息 table列表
     *
     * @return
     */
	@ApiOperation("汇总数据信息 table列表")
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableCollect")
    public AjaxJson<DataTablesOutput<NisExpenseDetailVO>> dataTableCollect(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(nisExpenseDetailService.getDataTablesUnion(input, this.getCurrentUser()));
    }
}
