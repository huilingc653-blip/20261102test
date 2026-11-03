package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbIntellectRuleDtlService;
import com.bytter.bill.domain.bill.service.AbIntellectRuleService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleVO;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 智能选票规则处理Controller
 *
 * @author zhenghj
 */
@Controller
@RequestMapping("/bill/intellect/rule")
public class AbIntellectRuleController extends BaseController {
    @Autowired
    private AbIntellectRuleService abIntellectRuleService;
    @Autowired
    private AbIntellectRuleDtlService abIntellectRuleDtlService;
    /**
     * 选票规则列表跳转
     *
     * @param model 模型
     * @return 选票规则列表页
     */
    @GetMapping("/list")
    public String listElement(Model model) {
        return "bill/accebill/intellect/rule/listRule";
    }
    /**
     * 选票规则列表查询
     *
     * @param input 前端参数
     * @param model 模型
     * @return 选票规则列表数据
     */
    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput datatable(@RequestBody DataTablesInput input, Model model) {
        return abIntellectRuleService.datatable(input);
    }
    /**
     * 选票规则新增页面跳转
     *
     * @param model 模型
     * @return 选票规则新增页面
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("applyNo", SequenceSingleton.getSequence(SequenceEnum.BILL_RULE_APPLY_NO));
        return "bill/accebill/intellect/rule/addRule";
    }
    /**
     * 选票规则编辑页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则编辑页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        AbIntellectRuleVO ruleVo = abIntellectRuleService.getAllAbIntellectRuleVOById(id);
        model.addAttribute("rule", ruleVo);
        abIntellectRuleDtlService.delUselessByApplyNo(ruleVo.getApplyNo());
        return "bill/accebill/intellect/rule/editRule";
    }
    /**
     * 选票规则查看页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则查看页面
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        model.addAttribute("rule", abIntellectRuleService.getAllAbIntellectRuleVOById(id));
        return "bill/accebill/intellect/rule/viewRule";
    }
    /**
     * 选票规则保存操作
     *
     * @param abIntellectRuleVO 选票规则vo
     * @param model             模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(AbIntellectRuleVO abIntellectRuleVO, Model model) {
        abIntellectRuleService.saveRule(abIntellectRuleVO, getCurrentUser());
        return success();
    }
    /**
     * 选票规则编辑操作
     *
     * @param abIntellectRuleVO 选票规则vo
     * @param model             模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(AbIntellectRuleVO abIntellectRuleVO, Model model) {
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
    @RequestMapping(params = "listAbIntellectRule")
    @ResponseBody
    public List<AbIntellectRuleVO> listAbIntellectRule() {
        return abIntellectRuleService.listAbIntellectRule();
    }
}
