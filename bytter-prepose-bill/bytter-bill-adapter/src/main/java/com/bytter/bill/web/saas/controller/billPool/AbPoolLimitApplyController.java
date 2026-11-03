package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitApplyService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyVO;
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

/**
 * @Title: Controller
 * @Description: 票据池额度申领
 */
@Api(value = "/v1-preposebill/abpool/limitApply", tags = "票据池额度申领")
@RestController("v1AbPoolLimitApplyController")
@RequestMapping("/v1-preposebill/abpool/limitApply")
public class AbPoolLimitApplyController extends BaseController {

    @Autowired
    private AbPoolLimitApplyService abPoolLimitApplyService;

    @Autowired
    private AbLogService abLogService;

    /**
     * 查询额度申请列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度申请列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询额度申请列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitApplyService.getDataTablesUnion(input));
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitApplyService.dataTableApprove(input));
    }

    /**
     * 审核详情界面初始化数据
     */
    @ApiOperation("审核详情页面初始化数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolLimitApplyFormVO> approve(String businessId, String taskId) {
        AbPoolLimitApplyFormVO result = new AbPoolLimitApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitApplyService.getAbPoolLimitApplyVoById(businessId));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 额度申领提交
     */
    @ApiOperation("额度申领提交")
    @LogMessage(content = "额度申领提交", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/updateLimitApply")
    public AjaxJson updateLimitApply(@RequestBody AbPoolLimitApplyVO abPoolLimitApplyVO) {
        return abPoolLimitApplyService.updateLimitApply(abPoolLimitApplyVO, this.getCurrentUser());
    }

    /**
     * 查询额度申领详情
     */
    @ApiOperation("查询额度申领详情")
    @GetMapping("/detail/{id}")
    public AjaxJson<AbPoolLimitApplyFormVO> detail(@PathVariable("id") String id) {
        AbPoolLimitApplyFormVO result = new AbPoolLimitApplyFormVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setInfo(abPoolLimitApplyService.getAbPoolLimitApplyVoById(id));
            result.setAbLogList(abLogService.listAbLog(id, null));
        }
        return success(result);
    }

    /**
     * 手机审核详情界面初始化数据
     */
    @ApiOperation("手机端审核详情初始化数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolLimitApplyFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolLimitApplyFormVO result = new AbPoolLimitApplyFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitApplyService.getAbPoolLimitApplyVoById(businessId));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }
}
