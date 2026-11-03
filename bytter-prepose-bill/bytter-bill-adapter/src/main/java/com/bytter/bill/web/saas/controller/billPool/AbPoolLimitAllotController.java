package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billbank.pool.service.impl.BillPoolLimitAllotImpService;
import com.bytter.bill.domain.billbank.pool.vo.PoolBaseVO;
import com.bytter.bill.domain.billbank.pool.vo.PoolLimitVO;
import com.bytter.bill.domain.billPool.service.AbPoolLimitAllotService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: Controller
 * @Description: 票据池额度分配
 */
@Api(value = "/v1-preposebill/billpool/limitAllot", tags = "票据池额度分配")
@RestController("v1AbPoolLimitAllotController")
@RequestMapping("/v1-preposebill/billpool/limitAllot")
public class AbPoolLimitAllotController extends BaseController {

    @Autowired
    private AbPoolLimitAllotService abPoolLimitAllotService;

    @Autowired
    private AbPoolLimitService abPoolLimitService;

    @Autowired
    private AbLogService abLogService;

    @Autowired
    private BillPoolLimitAllotImpService billPoolLimitAllotImpService;

    /**
     * 额度分配 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度分配列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询额度分配列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitAllotService.getDataTablesUnion(input));
    }

    /**
     * 根据单位ID和票据池ID查询额度信息
     */
    @ApiOperation("根据单位ID和票据池ID查询额度信息")
    @GetMapping("/getLimitInfo")
    public AjaxJson<AbPoolLimitVO> getLimitInfo(String corpId, String bpId) {
        return success(abPoolLimitService.getLimitInfo(corpId, bpId));
    }

    /**
     * 保存额度分配信息
     */
    @LogMessage(content = "保存额度分配信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存额度分配信息")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbPoolLimitAllotVO abPoolLimitAllotVO) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(abPoolLimitAllotService.saveLimitAllotInfo(abPoolLimitAllotVO, this.getCurrentUser()));
        return ajaxJson;
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitAllotService.dataTableApprove(input));
    }

    /**
     * 审核详情界面初始化数据
     */
    @ApiOperation("审核详情页面初始化数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolLimitAllotFormVO> approve(String businessId, String taskId) {
        AbPoolLimitAllotFormVO result = new AbPoolLimitAllotFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitAllotService.getLimitAllotVoById(businessId));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 手机审核详情界面初始化数据
     */
    @ApiOperation("手机端审核详情初始化数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolLimitAllotFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolLimitAllotFormVO result = new AbPoolLimitAllotFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitAllotService.getLimitAllotVoById(businessId));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 额度分配详情数据
     */
    @ApiOperation("查询额度分配详情")
    @GetMapping("/viewLimitAllot/{id}")
    public AjaxJson<AbPoolLimitAllotFormVO> viewLimitAllot(@PathVariable("id") String id) {
        AbPoolLimitAllotFormVO result = new AbPoolLimitAllotFormVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setInfo(abPoolLimitAllotService.getLimitAllotVoById(id));
            result.setAbLogList(abLogService.listAbLog(null, id));
            result.setIsApprove(false);
        }
        return success(result);
    }

    /**
     * 额度调剂查询 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度调剂列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询额度调剂列表")
    @PostMapping("/queryDatatable")
    public AjaxJson<DataTablesOutput> queryDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitAllotService.getQueryDataTablesUnion(input));
    }

    /**
     * 获取待发送额度分配数据
     */
    @ApiOperation("获取待发送额度分配数据")
    @GetMapping("/queryLimitAllotByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitAllotByBifCode(String bifCode, String reCode) {
        return success(billPoolLimitAllotImpService.queryLimitAllotByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果额度数据
     */
    @ApiOperation("获取等待结果额度数据")
    @GetMapping("/queryLimitAllotResultByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitAllotResultByBifCode(String bifCode, String reCode) {
        return success(billPoolLimitAllotImpService.queryLimitAllotResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改额度的指令状态
     */
    @ApiOperation("修改额度的指令状态")
    @PostMapping("/updatePoolLimitAllot")
    public AjaxJson updatePoolLimitAllot(@RequestBody PoolLimitVO paramsVo) {
        billPoolLimitAllotImpService.updatePoolLimitAllot(paramsVo);
        return success();
    }

    /**
     * 获取需要查询复核数据的票据池
     */
    @ApiOperation("获取需要查询复核数据的票据池")
    @GetMapping("/queryCheckPoolByBifCode")
    public AjaxJson<List<PoolBaseVO>> queryCheckPoolByBifCode(String bifCode) {
        return success(billPoolLimitAllotImpService.queryCheckPoolByBifCode(bifCode));
    }

    /**
     * 对获取的待复核数据进行入库
     */
    @ApiOperation("对获取的待复核数据进行入库")
    @PostMapping("/savePoolLimitAllotCheck")
    public AjaxJson savePoolLimitAllotCheck(@RequestBody List<PoolLimitVO> paramsVo) {
        billPoolLimitAllotImpService.savePoolLimitAllotCheck(paramsVo);
        return success();
    }

    /**
     * 获取待发送额度分配复核数据
     */
    @ApiOperation("获取待发送额度分配复核数据")
    @GetMapping("/queryLimitAllotCheckByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitAllotCheckByBifCode(String bifCode, String reCode) {
        return success(billPoolLimitAllotImpService.queryLimitAllotCheckByBifCode(bifCode, reCode));
    }

    /**
     * 获取等待结果额度分配复核数据
     */
    @ApiOperation("获取等待结果额度分配复核数据")
    @GetMapping("/queryLimitAllotCheckResultByBifCode")
    public AjaxJson<List<PoolLimitVO>> queryLimitAllotCheckResultByBifCode(String bifCode, String reCode) {
        return success(billPoolLimitAllotImpService.queryLimitAllotCheckResultByBifCode(bifCode, reCode));
    }

    /**
     * 修改额度分配复核的指令状态
     */
    @ApiOperation("修改额度分配复核的指令状态")
    @PostMapping("/updatePoolLimitAllotCheck")
    public AjaxJson updatePoolLimitAllotCheck(@RequestBody PoolLimitVO paramsVo) {
        billPoolLimitAllotImpService.updatePoolLimitAllotCheck(paramsVo);
        return success();
    }
}
