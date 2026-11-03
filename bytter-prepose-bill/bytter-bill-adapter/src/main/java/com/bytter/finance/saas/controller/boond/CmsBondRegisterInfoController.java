package com.bytter.finance.saas.controller.boond;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.finance.domain.bond.entity.CmsBondRegisterInfo;
import com.bytter.finance.domain.bond.service.CmsBondRegisterInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 债券注册Controller
 *
 * @author ouyj
 */
@Api(value = "/v1-preposebill/cms/bond/register", tags = "债券注册信息管理")
@RestController("v1CmsBondRegisterInfoController")
@RequestMapping("/v1-preposebill/cms/bond/register")
public class CmsBondRegisterInfoController extends BaseController {
    @Autowired
    private CmsBondRegisterInfoService cmsBondRegisterInfoService;



    /**
     * 获取债券注册信息编辑页数据
     *
     * @param id 债券注册信息id
     */
    @ApiOperation("债券注册编辑页面初始化数据")
    @GetMapping("/update/{id}")
    public AjaxJson<Map<String, Object>> update(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("registerInfo", cmsBondRegisterInfoService.getInfoById(id));
        return AjaxJson.success(resultMap);
    }

    /**
     * 审核列表初始化参数
     *
     * @param flowStatus 流程状态
     * @param cycleTime 周期
     */
    @ApiOperation("债券注册审批列表页面初始化参数")
    @GetMapping("/listApprove")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("flowStatus", flowStatus);
        resultMap.put("cycleTime", cycleTime);
        return AjaxJson.success(resultMap);
    }

    /**
     * 跳转至审核详情界面数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     */
    @ApiOperation("债券注册审批详情初始化数据")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("taskId", taskId);
        resultMap.put("registerInfo", cmsBondRegisterInfoService.getInfoById(businessId));
        return AjaxJson.success(resultMap);
    }

    /**
     * 债劵注册信息表详情页
     *
     * @param id id
     */
    @LogMessage(content = "查询债劵注册信息详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("债券注册信息详情查询")
    @GetMapping("/view/{id}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("registerInfo", cmsBondRegisterInfoService.getInfoById(id));
        return AjaxJson.success(resultMap);
    }

    /**
     * 债券注册审批列表 - 数据
     *
     * @param input 前端传参
     */
    @ApiOperation("债券注册审批列表分页数据")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> datatableInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondRegisterInfoService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 获取债券注册信息列表数据
     *
     * @param input 前端传参
     * @return 债券注册信息列表数据
     */
    @ApiOperation("债券注册信息列表分页数据")
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput> dataTable(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondRegisterInfoService.getDataTable(dataInput, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 新增债券注册信息
     *
     * @param cmsBondRegisterInfo 债券注册信息
     */
    @LogMessage(content = "新增债券注册信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("新增债券注册信息")
    @PostMapping("/save")
    public AjaxJson save(@RequestBody CmsBondRegisterInfo cmsBondRegisterInfo) {
        cmsBondRegisterInfoService.saveBondRegisterInfo(cmsBondRegisterInfo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 修改债券注册信息
     *
     * @param cmsBondRegisterInfo 债券注册信息
     */
    @LogMessage(content = "修改债券注册信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("修改债券注册信息")
    @PostMapping("/modify")
    public AjaxJson update(@RequestBody CmsBondRegisterInfo cmsBondRegisterInfo) {
        cmsBondRegisterInfoService.updateBondRegisterInfo(cmsBondRegisterInfo, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除债券注册信息
     *
     * @param id 债券注册信息id
     */
    @LogMessage(content = "删除债券注册信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("删除债券注册信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        cmsBondRegisterInfoService.deleteBondRegisterInfo(id, this.getCurrentUser());
        return AjaxJson.success();
    }
}