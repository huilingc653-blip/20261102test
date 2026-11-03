package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbIntellectRuleGroupDtlService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupDtlVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 智能选票规则组明细处理Controller
 *
 * @author zhenghj
 */
@Api(value = "/v1-preposebill/bill/intellect/ruleGroupDtl", tags = "智能选票规则组明细")
@RestController("v1AbIntellectRuleGroupDtlController")
@RequestMapping("/v1-preposebill/bill/intellect/ruleGroupDtl")
public class AbIntellectRuleGroupDtlController extends BaseController {

    @Autowired
    private AbIntellectRuleGroupDtlService abIntellectRuleGroupDtlService;

    /**
     * 选票规则组明细列表查询
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询选票规则组明细列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询选票规则组明细分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abIntellectRuleGroupDtlService.datatable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询选票规则组明细详情
     */
    @LogMessage(content = "查询选票规则组明细详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询选票规则组明细详情")
    public AjaxJson<AbIntellectRuleGroupDtlVO> getById(@PathVariable("id") String id) {
        AbIntellectRuleGroupDtlVO vo = abIntellectRuleGroupDtlService.getAbIntellectRuleGroupDtlVOById(id);
        return AjaxJson.success(vo);
    }

    /**
     * 选票规则组明细保存操作
     */
    @LogMessage(content = "保存选票规则组明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("保存选票规则组明细")
    public AjaxJson save(@RequestBody AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO) {
        abIntellectRuleGroupDtlService.saveRuleGroupDtl(abIntellectRuleGroupDtlVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 选票规则组明细编辑操作
     */
    @LogMessage(content = "修改选票规则组明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ApiOperation("修改选票规则组明细")
    public AjaxJson update(@RequestBody AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO) {
        abIntellectRuleGroupDtlService.updateRuleGroupDtl(abIntellectRuleGroupDtlVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 选票规则组明细删除操作
     */
    @LogMessage(content = "删除选票规则组明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/del")
    @ApiOperation("删除选票规则组明细")
    public AjaxJson del(@RequestBody AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO) {
        abIntellectRuleGroupDtlService.delRuleGroupDtl(abIntellectRuleGroupDtlVO);
        return AjaxJson.success();
    }

    /**
     * 获取有效的选票规则组明细
     */
    @LogMessage(content = "查询有效的选票规则组明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("获取有效的选票规则组明细")
    public AjaxJson<List<AbIntellectRuleGroupDtlVO>> list() {
        List<AbIntellectRuleGroupDtlVO> list = abIntellectRuleGroupDtlService.listAbIntellectRuleGroupDtl();
        return AjaxJson.success(list);
    }

    /**
     * 上移下移操作
     */
    @LogMessage(content = "选票规则组明细上移下移", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/moveUpOrDown")
    @ApiOperation("选票规则组明细上移下移")
    public AjaxJson moveUpOrDown(@RequestBody AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO) {
        abIntellectRuleGroupDtlService.moveUpOrDown(abIntellectRuleGroupDtlVO);
        return AjaxJson.success();
    }

}