package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.bill.service.AbVoucherinfoViceService;
import com.bytter.bill.domain.billPool.dto.AbPoolEmitFinaApplyBatchDeleteDTO;
import com.bytter.bill.domain.billPool.service.AbPoolEmitFinaApplyService;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyDateVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolEmitFinaApplyVO;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 融资申请
 */
@Api(value = "/v1-preposebill/billPool/emitFinaApply", tags = "融资申请")
@RestController("v1AbPoolEmitFinaApplyController")
@RequestMapping("/v1-preposebill/billPool/emitFinaApply")
public class AbPoolEmitFinaApplyController extends BaseController {

    @Autowired
    private AbPoolEmitFinaApplyService abPoolEmitFinaApplyService;

    @Autowired
    private AbVoucherinfoViceService abVoucherinfoViceService;

    @Autowired
    private AbLogService abLogService;

    /**
     * 融资申请列表页面日期参数
     */
    @ApiOperation("融资申请列表页面日期参数")
    @GetMapping("/list")
    public AjaxJson<AbPoolEmitFinaApplyDateVO> listAbPoolEmitFinaApplyVO() {
        AbPoolEmitFinaApplyDateVO result = new AbPoolEmitFinaApplyDateVO();
        result.setDateStart(DateUtils.format(DateUtils.getMonthStartTime(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        result.setDateEnd(DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE));
        return success(result);
    }

    /**
     * 票据池融资申请 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询票据池融资申请列表")
    @LogMessage(content = "查询票据池融资申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolEmitFinaApplyService.getDataTablesUnion(input));
    }

    /**
     * 融资申请新增页面数据
     */
    @ApiOperation("融资申请新增页面数据")
    @GetMapping("/add")
    public AjaxJson<AbPoolEmitFinaApplyFormVO> addAbPoolEmitFinaApplyVO() {
        AbPoolEmitFinaApplyFormVO result = new AbPoolEmitFinaApplyFormVO();
        result.setExternalCorpNum(abVoucherinfoViceService.getExternalCorpNum());
        return success(result);
    }

    /**
     * 融资申请保存和提交
     */
    @ApiOperation("融资申请保存或提交")
    @LogMessage(content = "融资申请保存或提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save/{operate}")
    public AjaxJson abPoolEmitFinaApplySaveOrSubmit(@PathVariable("operate") String operate, @RequestBody AbPoolEmitFinaApplyVO infoVO) {
        return abPoolEmitFinaApplyService.abPoolEmitFinaApplySaveOrSubmit(operate, infoVO, getCurrentUser());
    }

    /**
     * 融资申请修改页面数据
     */
    @ApiOperation("融资申请修改页面数据")
    @GetMapping("/update")
    public AjaxJson<AbPoolEmitFinaApplyFormVO> updateAbPoolEmitFinaApplyVO(String id) {
        AbPoolEmitFinaApplyFormVO result = new AbPoolEmitFinaApplyFormVO();
        result.setExternalCorpNum(abVoucherinfoViceService.getExternalCorpNum());
        result.setAbPoolEmitFinaApplyVO(abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByDtlId(id));
        return success(result);
    }

    /**
     * 融资申请详情数据
     */
    @ApiOperation("查询融资申请详情")
    @LogMessage(content = "查询融资申请详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public AjaxJson<AbPoolEmitFinaApplyFormVO> view(String id) {
        AbPoolEmitFinaApplyFormVO result = new AbPoolEmitFinaApplyFormVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setAbPoolEmitFinaApplyVO(abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByDtlId(id));
            result.setConInfoList(abPoolEmitFinaApplyService.listConInfo(id));
            List<AbLog> ablogList = abLogService.listAbLog(null, id);
            result.setAbLogList(ablogList);
        }
        return success(result);
    }

    /**
     * 批量提交融资申请信息
     */
    @ApiOperation("批量提交融资申请信息")
    @LogMessage(content = "批量提交融资申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchSubmit")
    public AjaxJson batchSubmit(@RequestParam("ids") String ids) {
        AjaxJson j = success();
        j.setSuccess(abPoolEmitFinaApplyService.batchSubmit(ids, getCurrentUser()));
        return j;
    }

    /**
     * 批量删除融资申请信息
     */
    @ApiOperation("批量删除融资申请信息")
    @LogMessage(content = "批量删除融资申请信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    public AjaxJson batchDelete(@RequestBody AbPoolEmitFinaApplyBatchDeleteDTO dto) {
        AjaxJson j = success();
        j.setSuccess(abPoolEmitFinaApplyService.batchDelete(dto.getIds(), dto.getReason(), getCurrentUser()));
        return j;
    }

    /**
     * 融资申请审批 table列表
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("查询融资申请审批列表")
    @LogMessage(content = "查询融资申请审批列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/approveDatatable")
    public AjaxJson<DataTablesOutput> listApproveDatatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolEmitFinaApplyService.listApproveDatatable(input));
    }

    /**
     * 融资申请审批详情页面数据
     */
    @ApiOperation("融资申请审批详情页面数据")
    @GetMapping("/viewApprove")
    public AjaxJson<AbPoolEmitFinaApplyFormVO> viewApprove(String taskId, String dtlId) {
        AbPoolEmitFinaApplyFormVO result = new AbPoolEmitFinaApplyFormVO();
        result.setAbPoolEmitFinaApplyVO(abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByDtlId(dtlId));
        result.setIsApprove(true);
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 重新生成合同
     */
    @ApiOperation("重新生成合同")
    @LogMessage(content = "重新生成合同", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/regenerateContract")
    public AjaxJson regenerateContract(@RequestParam("conId") String conId) {
        return abPoolEmitFinaApplyService.regenerateContract(conId, getCurrentUser());
    }

    /**
     * 合同确认
     */
    @ApiOperation("合同确认")
    @LogMessage(content = "合同确认", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/contractConfirm")
    public AjaxJson contractConfirm(@RequestParam("conId") String conId) {
        return abPoolEmitFinaApplyService.contractConfirm(conId, getCurrentUser());
    }

    /**
     * 票据池质权人信息
     */
    @ApiOperation("票据池质权人信息")
    @GetMapping("/selectPledgee")
    public AjaxJson<AbPoolEmitFinaApplyVO> selectPledgee(String bpId) {
        return success(abPoolEmitFinaApplyService.selectPledgee(bpId));
    }

    /**
     * 查看下载合同文件
     */
    @ApiOperation("查看下载合同文件")
    @GetMapping("/viewContract")
    public void viewContract(HttpServletRequest request, HttpServletResponse response, String conId) {
        try {
            abPoolEmitFinaApplyService.viewContract(request, response, conId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 融资申请手机审批详情数据
     */
    @ApiOperation("融资申请手机审批详情数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolEmitFinaApplyFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolEmitFinaApplyFormVO result = new AbPoolEmitFinaApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setAbPoolEmitFinaApplyVO(abPoolEmitFinaApplyService.getAbPoolEmitFinaApplyVOByAid(businessId));
        }
        return success(result);
    }

    /**
     * 融资申请导入
     */
    @ApiOperation("融资申请导入")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<AbPoolEmitFinaApplyVO> list) {
        abPoolEmitFinaApplyService.saveImport(list, getCurrentUser());
        return success();
    }
}
