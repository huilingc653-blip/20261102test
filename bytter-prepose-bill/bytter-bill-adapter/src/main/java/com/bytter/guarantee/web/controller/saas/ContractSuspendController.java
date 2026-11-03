package com.bytter.guarantee.web.controller.saas;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.service.ContractSuspendService;
import com.bytter.guarantee.domain.service.impl.ConCommonService;
import com.bytter.guarantee.domain.vo.ConInfoVO;
import com.bytter.guarantee.domain.vo.ContractSuspendVO;
import com.bytter.guarantee.dto.ConInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0
 * @Title: 合同终止Controll
 * @Package com.bytter.biz.contract.controller
 * @Description: 合同终止
 * @author: Jinyong.cao
 * @date: 2019-11-12 下午09:17:48
 */
@Api(value = "/v1-guarantee/contract/suspend", tags = "合同终止")
@RestController("v1ContractSuspendController")
@RequestMapping("/v1-guarantee/contract/suspend")
public class ContractSuspendController extends BaseController {
    @Autowired
    private ContractSuspendService contractSuspendService;
    @Autowired
    private ConCommonService conCommonService;

    /**
     * 查询合同终止列表数据
     */
    @ApiOperation("查询合同终止列表数据")
    @LogMessage(content = "查询合同终止列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = contractSuspendService.getDataTablesUnion(input);
        return AjaxJson.success(output);
    }

    /**
     * 加载合同终止审核列表数据
     */
    @ApiOperation("加载合同终止审核列表数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> dataTableApprove(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = contractSuspendService.dataTableApprove(input);
        return AjaxJson.success(output);
    }

    /**
     * 合同终止列表按钮操作
     */
    @ApiOperation("合同终止列表按钮操作")
    @LogMessage(content = "合同终止列表按钮操作", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/shutOpt/{operate}")
    public AjaxJson siginOpt(ContractSuspendVO contractSuspendVO, @PathVariable("operate") String operate) {
        AjaxJson ajaxJson = new AjaxJson();
        try {
            contractSuspendService.singleDispose(contractSuspendVO, operate, this.getCurrentUser());
            ajaxJson.setSuccess(true);
            ajaxJson.setMsg("操作成功");
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
            ajaxJson.setMsg("操作失败");
        }
        return ajaxJson;
    }

    /**
     * 删除合同终止单据
     */
    @ApiOperation("删除合同终止单据")
    @PostMapping("/delCon")
    public AjaxJson delCon(String id, String lastId) {
        AjaxJson ajaxJson = new AjaxJson();
        try {
            contractSuspendService.delConSuSpend(id, lastId, this.getCurrentUser());
            ajaxJson.setSuccess(true);
        } catch (RuntimeException e) {
            ajaxJson.setSuccess(false);
        }
        return ajaxJson;
    }

    /**
     * 查询列表数据
     */
    @ApiOperation("查询列表数据")
    @PostMapping("/queryDatatable")
    public AjaxJson<DataTablesOutput> queryDataTable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = contractSuspendService.getQueryDataTablesUnion(input);
        return AjaxJson.success(output);
    }

    /**
     * 获取合同终止审核详情
     */
    @ApiOperation("获取合同终止审核详情")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            resultMap.put("contractSuspendVO", contractSuspendService.getContractSuspend(businessId));
        }
        resultMap.put("taskId", taskId);
        return AjaxJson.success(resultMap);
    }

    /**
     * 获取手机端合同终止审核详情
     */
    @ApiOperation("获取手机端合同终止审核详情")
    @GetMapping("/mobile/approve")
    public AjaxJson<Map<String, Object>> mobileSignApprove(String businessId, String flowStatus) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StringUtils.isNotEmpty(businessId)) {
            resultMap.put("contractSuspendVO", contractSuspendService.getContractSuspend(businessId));
        }
        resultMap.put("flowStatus", flowStatus);
        return AjaxJson.success(resultMap);
    }

    /**
     * 业务详情页面
     */
    @ApiOperation("业务详情页面")
    @GetMapping("/conNumView/{operate}")
    public AjaxJson<Map<String, Object>> conNumView(@PathVariable("operate") String operate, ConInfoVO params) {
        Map<String, Object> resultMap = new HashMap<>();
        ConInfoDTO conInfo = conCommonService.getConInfo(params);
        resultMap.put("conInfo", conInfo);
        resultMap.put("opt", operate);
        return AjaxJson.success(resultMap);
    }
}