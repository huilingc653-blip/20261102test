package com.bytter.bill.web.saas.controller.billPool;

import com.bytter.bill.domain.bill.service.AbLogService;
import com.bytter.bill.domain.billPool.service.AbPoolLimitAllotCheckService;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotCheckFormVO;
import com.bytter.bill.domain.billPool.vo.AbPoolLimitAllotCheckVO;
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
 * @Description: 额度分配核准
 */
@Api(value = "/v1-preposebill/billpool/limitAllotCheck", tags = "额度分配核准")
@RestController("v1AbPoolLimitAllotCheckController")
@RequestMapping("/v1-preposebill/billpool/limitAllotCheck")
public class AbPoolLimitAllotCheckController extends BaseController {

    @Autowired
    private AbPoolLimitAllotCheckService abPoolLimitAllotCheckService;

    @Autowired
    private AbLogService abLogService;

    /**
     * 额度分配核准 table列表
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询额度分配核准列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @ApiOperation("查询额度分配核准列表")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return success(abPoolLimitAllotCheckService.getDataTablesUnion(input));
    }

    /**
     * 保存额度分配核准信息
     */
    @LogMessage(content = "保存额度分配核准信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @ApiOperation("保存额度分配核准信息")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbPoolLimitAllotCheckVO abPoolLimitAllotCheckVO) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(abPoolLimitAllotCheckService.saveLimitAllotCheckInfo(abPoolLimitAllotCheckVO, this.getCurrentUser()));
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
        return success(abPoolLimitAllotCheckService.dataTableApprove(input));
    }

    /**
     * 审核详情界面初始化数据
     */
    @ApiOperation("审核详情页面初始化数据")
    @GetMapping("/approve")
    public AjaxJson<AbPoolLimitAllotCheckFormVO> approve(String businessId, String taskId) {
        AbPoolLimitAllotCheckFormVO result = new AbPoolLimitAllotCheckFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitAllotCheckService.getLimitAllotCheckVoByAid(businessId));
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
    public AjaxJson<AbPoolLimitAllotCheckFormVO> mobileApprove(String businessId, String taskId) {
        AbPoolLimitAllotCheckFormVO result = new AbPoolLimitAllotCheckFormVO();
        if (StringUtils.isNotEmpty(businessId)) {
            result.setInfo(abPoolLimitAllotCheckService.getLimitAllotCheckVoByAid(businessId));
            result.setIsApprove(true);
        }
        result.setTaskId(taskId);
        return success(result);
    }

    /**
     * 额度分配核准详情数据
     */
    @ApiOperation("查询额度分配核准详情")
    @GetMapping("/viewLimitAllotCheck/{id}")
    public AjaxJson<AbPoolLimitAllotCheckFormVO> viewLimitAllot(@PathVariable("id") String id) {
        AbPoolLimitAllotCheckFormVO result = new AbPoolLimitAllotCheckFormVO();
        if (StringUtils.isNotEmpty(id)) {
            result.setInfo(abPoolLimitAllotCheckService.getLimitAllotCheckVoByAid(id));
            result.setAbLogList(abLogService.listAbLog(null, id));
            result.setIsApprove(false);
        }
        return success(result);
    }
}
