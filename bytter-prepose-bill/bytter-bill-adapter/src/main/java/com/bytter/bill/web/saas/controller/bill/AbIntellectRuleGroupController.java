package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbIntellectRuleGroupDtlService;
import com.bytter.bill.domain.bill.service.AbIntellectRuleGroupService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能选票规则组处理Controller
 *
 * @author zhenghj
 */
@Api(value = "/v1-preposebill/bill/intellect/ruleGroup", tags = "智能选票规则组处理Controller")
@RestController("v1AbIntellectRuleGroupController")
@RequestMapping("/v1-preposebill/bill/intellect/ruleGroup")
public class AbIntellectRuleGroupController extends BaseController {
    @Autowired
    private AbIntellectRuleGroupService abIntellectRuleGroupService;
    @Autowired
    private AbIntellectRuleGroupDtlService abIntellectRuleGroupDtlService;


    /**
     * 选票规则组列表查询
     *
     * @param dataTablesInput 前端参数
     * @return 选票规则组列表数据
     */
    @ApiOperation("选票规则组列表查询")
    @ResponseBody
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<AbIntellectRuleGroupVO>> datatable(@RequestBody V1DataTablesInput dataTablesInput) {
        DataTablesInput input = dataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abIntellectRuleGroupService.datatable(input));
    }

    /**
     * 选票规则组新增页面跳转
     *
     * @param model 模型
     * @return 选票规则组新增页面
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, String>> add(Model model) {
        Map<String, String> map = new HashMap<>();
        map.put("applyNo", SequenceSingleton.getSequence(SequenceEnum.BILL_RULE_GROUP_APPLY_NO));
        return AjaxJson.success(map);
    }

    /**
     * 选票规则组编辑页面跳转
     *
     * @param id 主键id
     * @return 选票规则组编辑页面
     */
    @GetMapping("/edit/{id}")
    public AjaxJson<Map<String,AbIntellectRuleGroupVO>> edit(@PathVariable("id") String id) {
        AbIntellectRuleGroupVO ruleVo = abIntellectRuleGroupService.getAllAbIntellectRuleGroupVOById(id);
        abIntellectRuleGroupDtlService.delUselessByApplyNo(ruleVo.getApplyNo());
        Map<String,AbIntellectRuleGroupVO> map = new HashMap<>();
        map.put("ruleGroup",ruleVo);
        return AjaxJson.success(map);
    }

    /**
     * 选票规则组查看页面跳转
     *
     * @param id 主键id
     * @return 选票规则组查看页面
     */
    @GetMapping("/view/{id}")
    public AjaxJson<Map<String,AbIntellectRuleGroupVO>> view(@PathVariable("id") String id) {
        Map<String,AbIntellectRuleGroupVO> map=new HashMap<>();
        map.put("ruleGroup",abIntellectRuleGroupService.getAllAbIntellectRuleGroupVOById(id));
        return AjaxJson.success(map);
    }

    /**
     * 选票规则组保存操作
     *
     * @param abIntellectRuleGroupVO 选票规则组vo
     * @param model                  模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbIntellectRuleGroupVO abIntellectRuleGroupVO, Model model) {
        abIntellectRuleGroupService.saveRuleGroup(abIntellectRuleGroupVO, getCurrentUser());
        return success();
    }

    /**
     * 选票规则组编辑操作
     *
     * @param abIntellectRuleGroupVO 选票规则组vo
     * @param model                  模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(@RequestBody AbIntellectRuleGroupVO abIntellectRuleGroupVO, Model model) {
        abIntellectRuleGroupService.updateRuleGroup(abIntellectRuleGroupVO, getCurrentUser());
        return success();
    }

    /**
     * 选票规则组删除操作
     *
     * @param id 选票规则组id
     * @return 是否成功
     */
    @ResponseBody
    @PostMapping("/del/{id}")
    public AjaxJson del(@PathVariable("id") String id) {
        abIntellectRuleGroupService.delRuleGroup(id);
        return success();
    }

    /**
     * <b>功能：</b>获取有效的选票规则组组<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    @GetMapping( "listAbIntellectRuleGroup")
    @ResponseBody
    public AjaxJson<List<AbIntellectRuleGroupVO>> listAbIntellectRuleGroup() {
        return AjaxJson.success(abIntellectRuleGroupService.listAbIntellectRuleGroup());
    }
}
