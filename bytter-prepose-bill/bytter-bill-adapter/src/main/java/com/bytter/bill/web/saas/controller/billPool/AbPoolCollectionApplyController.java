package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolCollectionApplyService;
import com.bytter.bill.domain.billPool.vo.AbPoolCollectionApplyDateVO;
import com.bytter.bill.domain.billPool.vo.AbPoolCollectionApplyFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolCollectionApplyVO;
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
 * @Description: 提示付款
 */
@Api(value = "/v1-preposebill/billPool/collection", tags = "提示付款")
@RestController("v1AbPoolCollectionApplyController")
@RequestMapping("/v1-preposebill/billPool/collection")
public class AbPoolCollectionApplyController extends BaseController {

    @Autowired
    private AbPoolCollectionApplyService abPoolCollectionApplyService;

    @Autowired
    private AbLogService abLogService;

    /**
     * 提示付款列表页面日期参数
     */
    @ApiOperation("提示付款列表页面日期参数")
    @GetMapping("/list")
    public AjaxJson<AbPoolCollectionApplyDateVO> listAbPoolCollectionApplyVO() {
        AbPoolCollectionApplyDateVO result = new AbPoolCollectionApplyDateVO();
        result.setDateStart(DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        result.setDateEnd(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(result);
    }

    /**
     * 票据池提示付款 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询票据池提示付款列表")
    @LogMessage(content = "查询票据池提示付款列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolCollectionApplyService.getDataTablesUnion(input));
    }

    /**
     * 提示付款新增页面数据
     */
    @ApiOperation("提示付款新增页面数据")
    @GetMapping("/add")
    public AjaxJson<AbPoolCollectionApplyFormVO> addAbPoolCollectionApplyVO(AbPoolCollectionApplyVO abPoolCollectionApplyVO) {
        AbPoolCollectionApplyFormVO result = new AbPoolCollectionApplyFormVO();
        result.setAbPoolCollectionApplyVO(abPoolCollectionApplyVO);
        result.setBusinessDate(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(result);
    }

    /**
     * 提示付款提交
     */
    @ApiOperation("提示付款提交")
    @LogMessage(content = "提示付款提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    public AjaxJson abPoolCollectionApplySubmit(@RequestBody AbPoolCollectionApplyVO abPoolCollectionApplyVO) {
        return abPoolCollectionApplyService.abPoolCollectionApplySubmit(abPoolCollectionApplyVO, getCurrentUser());
    }

    /**
     * 提示付款详情数据
     */
    @ApiOperation("查询详情")
    @LogMessage(content = "查询详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public AjaxJson<AbPoolCollectionApplyFormVO> view(String id) {
        AbPoolCollectionApplyFormVO result = new AbPoolCollectionApplyFormVO();
        result.setAbPoolCollectionApplyVO(abPoolCollectionApplyService.getAbPoolCollectionApplyVOByDtlId(id));
        if (StringUtils.isNotEmpty(id)) {
            List<AbLog> ablogList = abLogService.listAbLog(null, id);
            result.setAbLogList(ablogList);
        }
        return success(result);
    }

    /**
     * 提示付款审批 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询提示付款审批列表")
    @LogMessage(content = "查询提示付款审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approveDatatable")
    public AjaxJson<DataTablesOutput> listApproveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolCollectionApplyService.listApproveDatatable(input));
    }

    /**
     * 提示付款审批详情页面数据
     */
    @ApiOperation("提示付款审批详情页面数据")
    @GetMapping("/viewApprove")
    public AjaxJson<AbPoolCollectionApplyFormVO> viewApprove(String taskId, String dtlId) {
        AbPoolCollectionApplyFormVO result = new AbPoolCollectionApplyFormVO();
        result.setAbPoolCollectionApplyVO(abPoolCollectionApplyService.getAbPoolCollectionApplyVOByDtlId(dtlId));
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 中止
     */
    @ApiOperation("中止")
    @PostMapping("/abort")
    public AjaxJson abortDtl(@RequestParam("aid") String aid) {
        return abPoolCollectionApplyService.abortDtl(aid, getCurrentUser());
    }

    /**
     * 继续
     */
    @ApiOperation("继续")
    @LogMessage(content = "继续", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/continueRemind")
    public AjaxJson continueRemind(@RequestParam("aid") String aid) {
        return abPoolCollectionApplyService.continueRemind(aid, getCurrentUser());
    }

    /**
     * 提示付款手机审批详情数据
     */
    @ApiOperation("提示付款手机审批详情数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolCollectionApplyFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolCollectionApplyFormVO result = new AbPoolCollectionApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setAbPoolCollectionApplyVO(abPoolCollectionApplyService.getAbPoolCollectionApplyVOByAid(businessId));
        }
        return success(result);
    }
}
