package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitApplyCheckService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyCheckFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitApplyCheckVO;
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
 * @Description: 入池申请额度核准
 */
@Api(value = "/v1-preposebill/billPool/applyCheck", tags = "入池申请额度核准")
@RestController("v1AbPoolLimitApplyCheckController")
@RequestMapping("/v1-preposebill/billPool/applyCheck")
public class AbPoolLimitApplyCheckController extends BaseController {

    @Autowired
    private AbPoolLimitApplyCheckService abPoolLimitApplyCheckService;

    @Autowired
    private AbLogService abLogService;

    /**
     * 查询入池申请额度核准列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询入池申请额度核准列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitApplyCheckService.getDataTablesUnion(input));
    }

    /**
     * 确认核准
     */
    @ApiOperation("确认核准")
    @LogMessage(content = "确认核准", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveLimitApplyCheck")
    public AjaxJson saveLimitApplyCheck(@RequestBody AbPoolLimitApplyCheckVO abPoolLimitApplyCheckVO) {
        return abPoolLimitApplyCheckService.saveLimitApplyCheck(abPoolLimitApplyCheckVO, this.getCurrentUser());
    }

    /**
     * 加载审核列表数据
     */
    @SuppressWarnings("rawtypes")
    @ApiOperation("加载审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitApplyCheckService.dataTableApprove(input));
    }

    /**
     * 审核详情界面初始化数据
     */
    @ApiOperation("审核详情页面初始化数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolLimitApplyCheckFormVO> approve(String businessId, String taskId) {
        AbPoolLimitApplyCheckFormVO result = new AbPoolLimitApplyCheckFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitApplyCheckService.getAbPoolLimitApplyVoById(businessId));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 查询额度核准详情
     */
    @ApiOperation("查询额度核准详情")
    @GetMapping("/detail/{id}")
    public AjaxJson<AbPoolLimitApplyCheckFormVO> detail(@PathVariable("id") String id) {
        AbPoolLimitApplyCheckFormVO result = new AbPoolLimitApplyCheckFormVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setInfo(abPoolLimitApplyCheckService.getAbPoolLimitApplyVoById(id));
            result.setAbLogList(abLogService.listAbLog(id, null));
        }
        return success(result);
    }

    /**
     * 手机审核详情界面初始化数据
     */
    @ApiOperation("手机端审核详情初始化数据")
    @GetMapping("/mobileApprove")
    public AjaxJson<AbPoolLimitApplyCheckFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolLimitApplyCheckFormVO result = new AbPoolLimitApplyCheckFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitApplyCheckService.getAbPoolLimitApplyVoById(businessId));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }
}
