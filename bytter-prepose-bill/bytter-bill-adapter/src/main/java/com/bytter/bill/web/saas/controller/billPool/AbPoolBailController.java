package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolBailImpService;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolEmitFinaApplyImpService;
import com.bytter.bill.domain.billbank.pool.vo.PoolBailVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaApplyVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolEmitFinaConVO;
import com.bytter.bill.domain.billPool.service.AbPoolBailDtlService;
import com.bytter.bill.domain.billPool.vo.AbPoolBailDateVO;
import com.bytter.bill.domain.billPool.vo.AbPoolBailDtlVO;
import com.bytter.bill.domain.billPool.vo.AbPoolBailFormVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 保证金管理（保证金存取/审批/银行接口）
 */
@Api(value = "/v1-preposebill/billPool/bail", tags = "保证金管理")
@RestController("v1AbPoolBailController")
@RequestMapping("/v1-preposebill/billPool/bail")
public class AbPoolBailController extends BaseController {

    @Autowired
    private AbPoolBailDtlService abPoolBailDtlService;

    @Autowired
    private BillPoolBailImpService billPoolBailImpService;

    @Autowired
    private BillPoolEmitFinaApplyImpService billPoolBankExcImpService;

    /**
     * 保证金存入列表页面初始化参数
     */
    @ApiOperation("保证金存入列表页面初始化参数")
    @GetMapping("/inList")
    public AjaxJson<AbPoolBailDateVO> inList(String appDateStart, String appDateEnd) {
        AbPoolBailDateVO result = new AbPoolBailDateVO();
        result.setAppDateStart(StringUtils.isNotEmpty(appDateStart) ? appDateStart : DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        result.setAppDateEnd(StringUtils.isNotEmpty(appDateEnd) ? appDateEnd : DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(result);
    }

    /**
     * 保证金提取列表页面初始化参数
     */
    @ApiOperation("保证金提取列表页面初始化参数")
    @GetMapping("/outList")
    public AjaxJson<AbPoolBailDateVO> outList(String appDateStart, String appDateEnd) {
        AbPoolBailDateVO result = new AbPoolBailDateVO();
        result.setAppDateStart(StringUtils.isNotEmpty(appDateStart) ? appDateStart : DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        result.setAppDateEnd(StringUtils.isNotEmpty(appDateEnd) ? appDateEnd : DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(result);
    }

    /**
     * 保证金存取 table列表
     *
     * @param type 保证金存取类型 1存入 2提取
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询存取保证金", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询存取保证金列表")
    @PostMapping("/datatable/{type}")
    public AjaxJson<DataTablesOutput> dataTable(@PathVariable("type") String type, @RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolBailDtlService.getDataTablesUnion(type, input));
    }

    /**
     * 根据单位ID和票据池ID查询保证金信息
     */
    @ApiOperation("根据单位ID和票据池ID查询保证金信息")
    @GetMapping("/getBailInfo/{type}")
    public AjaxJson<AbPoolBailDtlVO> getBailInfo(@PathVariable("type") String type, String corpId, String bpId) {
        return success(abPoolBailDtlService.getBailInfo(type, corpId, bpId));
    }

    /**
     * 保证金存取提交
     *
     * @param type 1为存入，2为取出
     */
    @LogMessage(content = "保证金存取提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保证金存取提交")
    @PostMapping("/submit/{type}")
    public AjaxJson bailInfoSubmit(@PathVariable("type") String type, @RequestBody AbPoolBailDtlVO infoVO) {
        return abPoolBailDtlService.bailInfoSubmit(infoVO, type, getCurrentUser());
    }

    /**
     * 保证金存取详情页面数据
     */
    @ApiOperation("查询保证金存取详情")
    @GetMapping("/viewBail/{type}")
    public AjaxJson<AbPoolBailFormVO> viewBail(@PathVariable("type") String type, String id) {
        AbPoolBailFormVO result = new AbPoolBailFormVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setInfo(abPoolBailDtlService.getAbPoolBailDtlInfo(id));
            result.setAbLogList(abPoolBailDtlService.listAbLogInfo(id));
        }
        return success(result);
    }

    /**
     * 保证金存取审批 table列表
     *
     * @param type 1为存入，2为取出
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询保证金存取审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询保证金存取审批列表")
    @PostMapping("/approveDatatable/{type}")
    public AjaxJson<DataTablesOutput> approveDatatable(@PathVariable("type") String type, @RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolBailDtlService.listApproveDatatable(input, type));
    }

    /**
     * 保证金审批详情页面数据
     */
    @ApiOperation("保证金审批详情页面数据")
    @GetMapping("/viewBailApprove/{type}")
    public AjaxJson<AbPoolBailFormVO> viewBailApprove(@PathVariable("type") String type, String taskId, String id) {
        AbPoolBailFormVO result = new AbPoolBailFormVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setInfo(abPoolBailDtlService.getAbPoolBailDtlInfo(id));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 手机端保证金存入审批详情数据
     */
    @ApiOperation("手机端保证金存入审批详情数据")
    @GetMapping("/mobileInApprove")
    public AjaxJson<AbPoolBailFormVO> mobileInApprove(String businessId, String taskId) {
        AbPoolBailFormVO result = new AbPoolBailFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolBailDtlService.getAbPoolBailDtlInfo(businessId));
            result.setInBail(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 手机端保证金提取审批详情数据
     */
    @ApiOperation("手机端保证金提取审批详情数据")
    @GetMapping("/mobileOutApprove")
    public AjaxJson<AbPoolBailFormVO> mobileOutApprove(String businessId, String taskId) {
        AbPoolBailFormVO result = new AbPoolBailFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolBailDtlService.getAbPoolBailDtlInfo(businessId));
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 获取待发送保证金存入申请数据
     */
    @ApiOperation("获取待发送保证金存入申请数据")
    @GetMapping("/queryBailInByBifCode")
    public AjaxJson<List<PoolBailVO>> queryBailInByBifCode(String bifCode, String reCode) {
        return success(billPoolBailImpService.queryBailInByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果保证金存入数据
     */
    @ApiOperation("获取等待结果保证金存入数据")
    @GetMapping("/queryBailInResultByBifCode")
    public AjaxJson<List<PoolBailVO>> queryBailInResultByBifCode(String bifCode, String reCode) {
        return success(billPoolBailImpService.queryBailInResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改保证金存入的指令状态
     */
    @ApiOperation("修改保证金存入的指令状态")
    @PostMapping("/updatePoolBailInApply")
    public AjaxJson updatePoolBailInApply(@RequestBody PoolBailVO paramsVo) {
        billPoolBailImpService.updatePoolBailInApply(paramsVo);
        return success();
    }

    /**
     * 获取待发送保证金提取申请数据
     */
    @ApiOperation("获取待发送保证金提取申请数据")
    @GetMapping("/queryBailOutByBifCode")
    public AjaxJson<List<PoolBailVO>> queryBailOutByBifCode(String bifCode, String reCode) {
        return success(billPoolBailImpService.queryBailOutByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果保证金提取数据
     */
    @ApiOperation("获取等待结果保证金提取数据")
    @GetMapping("/queryBailOutResultByBifCode")
    public AjaxJson<List<PoolBailVO>> queryBailOutResultByBifCode(String bifCode, String reCode) {
        return success(billPoolBailImpService.queryBailOutResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改保证金提取的指令状态
     */
    @ApiOperation("修改保证金提取的指令状态")
    @PostMapping("/updatePoolBailOut")
    public AjaxJson updatePoolBailOut(@RequestBody PoolBailVO paramsVo) {
        billPoolBailImpService.updatePoolBailOut(paramsVo);
        return success();
    }

    /**
     * 获取待发送融资申请数据
     */
    @ApiOperation("获取待发送融资申请数据")
    @GetMapping("/queryEmitFinaByBifCode")
    public AjaxJson<List<PoolEmitFinaApplyVO>> queryEmitFinaByBifCode(String bifCode, String reCode) {
        return success(billPoolBankExcImpService.queryEmitFinaByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果融资申请数据
     */
    @ApiOperation("获取等待结果融资申请数据")
    @GetMapping("/queryEmitFinaResultByBifCode")
    public AjaxJson<List<PoolEmitFinaApplyVO>> queryEmitFinaResultByBifCode(String bifCode, String reCode) {
        return success(billPoolBankExcImpService.queryEmitFinaResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改融资申请的指令状态
     */
    @ApiOperation("修改融资申请的指令状态")
    @PostMapping("/updatePoolEmitFinaApply")
    public AjaxJson updatePoolEmitFinaApply(@RequestBody PoolEmitFinaApplyVO paramsVo) {
        billPoolBankExcImpService.updatePoolEmitFinaApply(paramsVo);
        return success();
    }

    /**
     * 保存出票融资的合同
     */
    @ApiOperation("保存出票融资的合同")
    @PostMapping("/savePoolEmitFinaCon")
    public AjaxJson savePoolEmitFinaCon(@RequestBody PoolEmitFinaConVO conVo) {
        billPoolBankExcImpService.savePoolEmitFinaCon(conVo);
        return success();
    }

    /**
     * 获取待发送重新生成出票融资合同数据
     */
    @ApiOperation("获取待发送重新生成出票融资合同数据")
    @GetMapping("/queryResetPoolEmitFinaConByBifCode")
    public AjaxJson<List<PoolEmitFinaConVO>> queryResetPoolEmitFinaConByBifCode(String bifCode, String reCode) {
        return success(billPoolBankExcImpService.queryResetPoolEmitFinaConByBifCode(bifCode, reCode));
    }

    /**
     * 获取重新生成出票融资合同等待结果数据
     */
    @ApiOperation("获取重新生成出票融资合同等待结果数据")
    @GetMapping("/queryResetPoolEmitFinaConResultByBifCode")
    public AjaxJson<List<PoolEmitFinaConVO>> queryResetPoolEmitFinaConResultByBifCode(String bifCode, String reCode) {
        return success(billPoolBankExcImpService.queryResetPoolEmitFinaConResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改重新生成出票融资合同数据指令状态
     */
    @ApiOperation("修改重新生成出票融资合同数据指令状态")
    @PostMapping("/updateResetPoolEmitFinaCon")
    public AjaxJson updateResetPoolEmitFinaCon(@RequestBody PoolEmitFinaConVO paramsVo) {
        billPoolBankExcImpService.updateResetPoolEmitFinaCon(paramsVo);
        return success();
    }

    /**
     * 获取待发送签收确认出票融资合同数据
     */
    @ApiOperation("获取待发送签收确认出票融资合同数据")
    @GetMapping("/querySignOkPoolEmitFinaConByBifCode")
    public AjaxJson<List<PoolEmitFinaConVO>> querySignOkPoolEmitFinaConByBifCode(String bifCode, String reCode) {
        return success(billPoolBankExcImpService.querySignOkPoolEmitFinaConByBifCode(bifCode, reCode));
    }

    /**
     * 获取签收确认出票融资合同等待结果数据
     */
    @ApiOperation("获取签收确认出票融资合同等待结果数据")
    @GetMapping("/querySignOkPoolEmitFinaConResultByBifCode")
    public AjaxJson<List<PoolEmitFinaConVO>> querySignOkPoolEmitFinaConResultByBifCode(String bifCode, String reCode) {
        return success(billPoolBankExcImpService.querySignOkPoolEmitFinaConResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改签收确认出票融资合同数据指令状态
     */
    @ApiOperation("修改签收确认出票融资合同数据指令状态")
    @PostMapping("/updateSignOkPoolEmitFinaCon")
    public AjaxJson updateSignOkPoolEmitFinaCon(@RequestBody PoolEmitFinaConVO paramsVo) {
        billPoolBankExcImpService.updateSignOkPoolEmitFinaCon(paramsVo);
        return success();
    }
}
