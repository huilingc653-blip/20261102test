package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.billPool.service.AbPoolEmitApplyService;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitApplyDateVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitApplyFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitApplyVO;
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
 * @Description: 出票申请
 */
@Api(value = "/v1-preposebill/billPool/emitApply", tags = "出票申请")
@RestController("v1AbPoolEmitApplyController")
@RequestMapping("/v1-preposebill/billPool/emitApply")
public class AbPoolEmitApplyController extends BaseController {

    @Autowired
    private AbPoolEmitApplyService abPoolEmitApplyService;

    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    @Autowired
    private AbLogService abLogService;

    /**
     * 出票申请列表页面日期参数
     */
    @ApiOperation("出票申请列表页面日期参数")
    @GetMapping("/list")
    public AjaxJson<AbPoolEmitApplyDateVO> listAbPoolEmitApplyVO() {
        AbPoolEmitApplyDateVO result = new AbPoolEmitApplyDateVO();
        result.setDateStart(DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        result.setDateEnd(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(result);
    }

    /**
     * 票据池出票申请 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询票据池出票申请列表")
    @LogMessage(content = "查询票据池出票申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolEmitApplyService.getDataTablesUnion(input));
    }

    /**
     * 出票申请新增页面数据
     */
    @ApiOperation("出票申请新增页面数据")
    @GetMapping("/add")
    public AjaxJson<AbPoolEmitApplyFormVO> addAbPoolEmitApplyVO() {
        AbPoolEmitApplyFormVO result = new AbPoolEmitApplyFormVO();
        result.setExternalCorpNum(abVoucherinfoViceService.getExternalCorpNum());
        return success(result);
    }

    /**
     * 出票申请修改页面数据
     */
    @ApiOperation("出票申请修改页面数据")
    @GetMapping("/update")
    public AjaxJson<AbPoolEmitApplyFormVO> updateAbPoolEmitApplyVO(String id) {
        AbPoolEmitApplyFormVO result = new AbPoolEmitApplyFormVO();
        result.setExternalCorpNum(abVoucherinfoViceService.getExternalCorpNum());
        result.setAbPoolEmitApplyVO(abPoolEmitApplyService.getAbPoolEmitApplyVOByDtlId(id));
        return success(result);
    }

    /**
     * 出票申请保存和提交
     */
    @ApiOperation("出票申请保存或提交")
    @LogMessage(content = "出票申请保存或提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operate}")
    public AjaxJson abPoolEmitApplySaveOrSubmit(@PathVariable("operate") String operate, @RequestBody AbPoolEmitApplyVO infoVO) {
        return abPoolEmitApplyService.abPoolEmitApplySaveOrSubmit(operate, infoVO, getCurrentUser());
    }

    /**
     * 出票申请详情数据
     */
    @ApiOperation("查询出票申请详情")
    @LogMessage(content = "查询出票申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public AjaxJson<AbPoolEmitApplyFormVO> view(String id) {
        AbPoolEmitApplyFormVO result = new AbPoolEmitApplyFormVO();
        result.setAbPoolEmitApplyVO(abPoolEmitApplyService.getAbPoolEmitApplyVOByDtlId(id));
        if (StringUtils.isNotEmpty(id)) {
            List<AbLog> ablogList = abLogService.listAbLog(null, id);
            result.setAbLogList(ablogList);
        }
        return success(result);
    }

    /**
     * 批量提交出票申请信息
     */
    @ApiOperation("批量提交出票申请信息")
    @LogMessage(content = "批量提交出票登记信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        AjaxJson j = success();
        j.setSuccess(abPoolEmitApplyService.batchSubmit(ids, getCurrentUser()));
        return j;
    }

    /**
     * 批量删除出票申请信息
     */
    @ApiOperation("批量删除出票申请信息")
    @LogMessage(content = "批量删除出票申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestParam("ids") String ids) {
        AjaxJson j = success();
        j.setSuccess(abPoolEmitApplyService.batchDelete(ids, getCurrentUser()));
        return j;
    }

    /**
     * 出票申请审批 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询出票申请审批列表")
    @LogMessage(content = "查询出票申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approveDatatable")
    public AjaxJson<DataTablesOutput> listApproveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolEmitApplyService.listApproveDatatable(input));
    }

    /**
     * 出票申请审批详情页面数据
     */
    @ApiOperation("出票申请审批详情页面数据")
    @GetMapping("/viewApprove")
    public AjaxJson<AbPoolEmitApplyFormVO> viewApprove(String taskId, String dtlId) {
        AbPoolEmitApplyFormVO result = new AbPoolEmitApplyFormVO();
        result.setAbPoolEmitApplyVO(abPoolEmitApplyService.getAbPoolEmitApplyVOByDtlId(dtlId));
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 出票申请手机审批详情数据
     */
    @ApiOperation("出票申请手机审批详情数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolEmitApplyFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolEmitApplyFormVO result = new AbPoolEmitApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setAbPoolEmitApplyVO(abPoolEmitApplyService.getAbPoolEmitApplyVOByAid(businessId));
        }
        return success(result);
    }
}
