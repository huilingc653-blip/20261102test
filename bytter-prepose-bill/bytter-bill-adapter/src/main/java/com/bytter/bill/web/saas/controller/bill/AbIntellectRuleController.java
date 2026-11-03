package com.bytter.bill.web.saas.controller.bill;

import com.bytter.bill.domain.bill.service.AbIntellectRuleDtlService;
import com.bytter.bill.domain.bill.service.AbIntellectRuleService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.V1DataTablesInput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 智能选票规则处理Controller
 *
 * @author zhenghj
 */
@Api(value = "/v1-preposebill/bill/intellect/rule", tags = "智能选票规则处理Controller")
@RestController("v1AbIntellectRuleController")
@RequestMapping("/v1-preposebill/bill/intellect/rule")
public class AbIntellectRuleController extends BaseController {
    @Autowired
    private AbIntellectRuleService abIntellectRuleService;
    @Autowired
    private AbIntellectRuleDtlService abIntellectRuleDtlService;

    /**
     * 选票规则列表查询
     *
     * @param v1DataTablesInput 前端参数
     * @return 选票规则列表数据
     */
    @ResponseBody
    @PostMapping("/datatable")
    public AjaxJson<DataTablesOutput<AbIntellectRuleVO>> datatable(@RequestBody V1DataTablesInput v1DataTablesInput) {
        DataTablesInput input = v1DataTablesInput.convertAndInitColumns();
        return AjaxJson.success(abIntellectRuleService.datatable(input));
    }

    /**
     * 选票规则新增页面跳转
     *
     * @return 选票规则新增页面
     */
    @GetMapping("/add")
    public AjaxJson<Map<String, String>> add() {
        Map<String, String> map = new HashMap<>();
        map.put("applyNo", SequenceSingleton.getSequence(SequenceEnum.BILL_RULE_APPLY_NO));
        return AjaxJson.success(map);
    }

    /**
     * 选票规则编辑页面跳转
     *
     * @param id 主键id
     * @return 选票规则编辑页面
     */
    @GetMapping("/edit/{id}")
    public AjaxJson<Map<String, AbIntellectRuleVO>> edit(@PathVariable("id") String id) {
        Map<String, AbIntellectRuleVO> model = new HashMap<>();
        AbIntellectRuleVO ruleVo = abIntellectRuleService.getAllAbIntellectRuleVOById(id);
        model.put("rule", ruleVo);
        abIntellectRuleDtlService.delUselessByApplyNo(ruleVo.getApplyNo());
        return AjaxJson.success();
    }

    /**
     * 选票规则查看页面跳转
     *
     * @param id 主键id
     * @return 选票规则查看页面
     */
    @GetMapping("/view/{id}")
    public AjaxJson<Map<String, AbIntellectRuleVO>> view(@PathVariable("id") String id) {
        Map<String, AbIntellectRuleVO> model = new HashMap<>();
        model.put("rule", abIntellectRuleService.getAllAbIntellectRuleVOById(id));
        return AjaxJson.success(model);
    }

    /**
     * 选票规则保存操作
     *
     * @param abIntellectRuleVO 选票规则vo
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(@RequestBody AbIntellectRuleVO abIntellectRuleVO) {
        abIntellectRuleService.saveRule(abIntellectRuleVO, getCurrentUser());
        return success();
    }

    /**
     * 选票规则编辑操作
     *
     * @param abIntellectRuleVO 选票规则vo
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(@RequestBody AbIntellectRuleVO abIntellectRuleVO) {
        abIntellectRuleService.updateRule(abIntellectRuleVO, getCurrentUser());
        return success();
    }

    /**
     * 选票规则删除操作
     *
     * @param id    选票规则id
     * @param model 模型
     * @return 是否成功
     */
    @ResponseBody
    @PostMapping("/del/{id}")
    public AjaxJson del(@PathVariable("id") String id, Model model) {
        abIntellectRuleService.delRule(id);
        return success();
    }

    /**
     * <b>功能：</b>获取有效的选票规则<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    @GetMapping("listAbIntellectRule")
    @ResponseBody
    public AjaxJson<List<AbIntellectRuleVO>> listAbIntellectRule() {
        return AjaxJson.success(abIntellectRuleService.listAbIntellectRule());
    }
}
