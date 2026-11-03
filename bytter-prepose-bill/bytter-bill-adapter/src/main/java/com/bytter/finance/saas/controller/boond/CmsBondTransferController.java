package com.bytter.finance.saas.controller.boond;

import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.domain.bond.service.CmsBondTransferService;
import com.bytter.finance.domain.bond.vo.CmsBondTransferVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 可转债Controller
 *
 * @author liuli
 */
@Api(value = "/v1-preposebill/cms/bond/transfer", tags = "可转债信息管理")
@RestController("v1CmsBondTransferController")
@RequestMapping("/v1-preposebill/cms/bond/transfer")
public class CmsBondTransferController extends BaseController {
    @Autowired
    private CmsBondTransferService cmsBondTransferService;

    /**
     * 可转债登记列表主界面初始化
     */
    @ApiOperation("可转债注册列表页面初始化")
    @GetMapping("/listTransferRegisterInfo")
    public AjaxJson<Map<String, Object>> listTransferableRegisterInfo() {
        Map<String, Object> resultMap = new HashMap<>();
        return AjaxJson.success(resultMap);
    }

    /**
     * 查询可转债注册列表
     *
     * @param input 前端传参
     * @return 债券注册信息列表数据
     */
    @LogMessage(content = "查询可转债注册列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("可转债注册分页列表")
    @PostMapping("/datatableTransferRegisterInfo")
    public AjaxJson<DataTablesOutput> dataTableForTransferableRegisterInfo(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondTransferService.getTransferableRegisterDataTable(dataInput, this.getCurrentUser());
        return AjaxJson.success(output);
    }

    /**
     * 查询可转债登记列表页面初始化
     *
     * @param issueId    发行id
     * @param isHistory  是否历史
     */
    @LogMessage(content = "查询可转债登记列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("可转债登记列表页面初始化参数")
    @GetMapping("/list/{issueId}/{isHistory}")
    public AjaxJson<Map<String, Object>> list(@PathVariable String issueId, @PathVariable String isHistory) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("isHistory", "1".equals(isHistory) ? "1" : "0");
        resultMap.put("issueId", issueId);
        return AjaxJson.success(resultMap);
    }

    /**
     * 可转债登记列表数据
     *
     * @param issueId 发行id
     * @param input   前端传参
     * @return 可转债登记列表数据
     */
    @ApiOperation("可转债登记分页列表")
    @PostMapping("/datatable/{issueId}")
    public AjaxJson<DataTablesOutput> dataTable(@PathVariable String issueId, @RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = cmsBondTransferService.getDataTable(dataInput, issueId);
        return AjaxJson.success(output);
    }

    /**
     * 可转债编辑页面初始化数据
     */
    @ApiOperation("可转债编辑页面初始化数据")
    @GetMapping("/edit")
    public AjaxJson<Map<String, Object>> editpage(CmsBondTransferVO cmsBondTransferVO) {
        Map<String, Object> resultMap = new HashMap<>();
        if (!EmptyUtils.isEmpty(cmsBondTransferVO) && EmptyUtils.isNotEmpty(cmsBondTransferVO.getId())) {
            cmsBondTransferVO = this.cmsBondTransferService.getCmsBondTransferById(cmsBondTransferVO.getId());
        }
        resultMap.put("cmsBondTransferVO", cmsBondTransferVO);
        return AjaxJson.success(resultMap);
    }

    /**
     * 新增或修改转债信息
     *
     * @param cmsBondTransferVO 转债信息
     * @param saveType           保存类型
     */
    @LogMessage(content = "新增或修改转债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("新增/修改可转债信息")
    @PostMapping("/save")
    public AjaxJson save(CmsBondTransferVO cmsBondTransferVO, String saveType) {
        this.cmsBondTransferService.save(cmsBondTransferVO, saveType, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 删除转债信息
     *
     * @param id 转债信息id
     */
    @LogMessage(content = "删除转债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("删除可转债信息")
    @PostMapping("/delete/{id}")
    public AjaxJson delete(@PathVariable("id") String id) {
        this.cmsBondTransferService.delete(id, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 查看某个转债信息
     *
     * @param id 转债信息id
     */
    @LogMessage(content = "查看某个转债信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @ApiOperation("查看可转债详情")
    @GetMapping("/{id}")
    public AjaxJson<Map<String, Object>> view(@PathVariable("id") String id) {
        Map<String, Object> resultMap = new HashMap<>();
        CmsBondTransferVO cmsBondTransferVO = this.cmsBondTransferService.getCmsBondTransferById(id);
        resultMap.put("cmsBondTransferVO", cmsBondTransferVO);
        return AjaxJson.success(resultMap);
    }

    /**
     * 保存导入数据
     *
     * @param list 导入数据集合
     */
    @ApiOperation("导入可转债数据保存")
    @PostMapping("/saveImport")
    public AjaxJson saveImport(@RequestBody List<CmsBondTransferVO> list) {
        this.cmsBondTransferService.saveImport(list, this.getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 审核列表页面初始化参数
     */
    @ApiOperation("可转债审批列表页面初始化参数")
    @GetMapping("/listApprove")
    public AjaxJson<Map<String, Object>> listApprove(String flowStatus, String cycleTime) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("flowStatus", flowStatus);
        resultMap.put("cycleTime", cycleTime);
        return AjaxJson.success(resultMap);
    }

    /**
     * 可转债审批列表分页数据
     */
    @ApiOperation("可转债审批分页列表")
    @PostMapping("/dataTableApprove")
    public AjaxJson<DataTablesOutput> datatableInfoApprove(@RequestBody V1DataTablesInput input) {
        DataTablesInput dataInput = input.convertAndInitColumns();
        DataTablesOutput output = this.cmsBondTransferService.dataTableApprove(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 审核详情界面初始化数据
     *
     * @param businessId 业务id
     * @param taskId     审批任务id
     */
    @ApiOperation("可转债审批详情初始化数据")
    @GetMapping("/approve")
    public AjaxJson<Map<String, Object>> approve(String businessId, String taskId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("taskId", taskId);
        resultMap.put("cmsBondTransferVO", this.cmsBondTransferService.getCmsBondTransferById(businessId));
        return AjaxJson.success(resultMap);
    }
}