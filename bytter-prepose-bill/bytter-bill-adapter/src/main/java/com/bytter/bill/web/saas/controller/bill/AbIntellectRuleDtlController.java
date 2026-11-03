package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbIntellectRuleDtlService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleDtlVO;
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
 * 智能选票规则明细处理Controller
 *
 * @author zhenghj
 */
@Api(value = "/v1-preposebill/bill/intellect/ruleDtl", tags = "智能选票规则明细")
@RestController("v1AbIntellectRuleDtlController")
@RequestMapping("/v1-preposebill/bill/intellect/ruleDtl")
public class AbIntellectRuleDtlController extends BaseController {

    @Autowired
    private AbIntellectRuleDtlService abIntellectRuleDtlService;

    /**
     * 选票规则明细列表查询
     */
    @SuppressWarnings("rawtypes")
    @LogMessage(content = "查询选票规则明细列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ApiOperation("查询选票规则明细分页列表")
    public AjaxJson<DataTablesOutput> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput dataInput = dataTablesInput.convertAndInitColumns();
        DataTablesOutput output = abIntellectRuleDtlService.datatable(dataInput);
        return AjaxJson.success(output);
    }

    /**
     * 查询选票规则明细详情
     */
    @LogMessage(content = "查询选票规则明细详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/{id}")
    @ApiOperation("查询选票规则明细详情")
    public AjaxJson<AbIntellectRuleDtlVO> getById(@PathVariable("id") String id) {
        AbIntellectRuleDtlVO vo = abIntellectRuleDtlService.getAbIntellectRuleDtlVOById(id);
        return AjaxJson.success(vo);
    }

    /**
     * 选票规则明细保存操作
     */
    @LogMessage(content = "保存选票规则明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ApiOperation("保存选票规则明细")
    public AjaxJson save(@RequestBody AbIntellectRuleDtlVO abIntellectRuleDtlVO) {
        abIntellectRuleDtlService.saveRuleDtl(abIntellectRuleDtlVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 选票规则明细编辑操作
     */
    @LogMessage(content = "修改选票规则明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ApiOperation("修改选票规则明细")
    public AjaxJson update(@RequestBody AbIntellectRuleDtlVO abIntellectRuleDtlVO) {
        abIntellectRuleDtlService.updateRuleDtl(abIntellectRuleDtlVO, getCurrentUser());
        return AjaxJson.success();
    }

    /**
     * 选票规则明细删除操作
     */
    @LogMessage(content = "删除选票规则明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/del")
    @ApiOperation("删除选票规则明细")
    public AjaxJson del(@RequestBody AbIntellectRuleDtlVO abIntellectRuleDtlVO) {
        abIntellectRuleDtlService.delRuleDtl(abIntellectRuleDtlVO);
        return AjaxJson.success();
    }

    /**
     * 获取有效的选票规则明细
     */
    @LogMessage(content = "查询有效的选票规则明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    @ApiOperation("获取有效的选票规则明细")
    public AjaxJson<List<AbIntellectRuleDtlVO>> list() {
        List<AbIntellectRuleDtlVO> list = abIntellectRuleDtlService.listAbIntellectRuleDtl();
        return AjaxJson.success(list);
    }

    /**
     * 上移下移操作
     */
    @LogMessage(content = "选票规则明细上移下移", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/moveUpOrDown")
    @ApiOperation("选票规则明细上移下移")
    public AjaxJson moveUpOrDown(@RequestBody AbIntellectRuleDtlVO abIntellectRuleDtlVO) {
        abIntellectRuleDtlService.moveUpOrDown(abIntellectRuleDtlVO);
        return AjaxJson.success();
    }

}