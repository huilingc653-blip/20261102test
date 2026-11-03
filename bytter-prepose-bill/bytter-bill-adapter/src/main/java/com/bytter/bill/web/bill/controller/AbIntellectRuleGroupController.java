package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbIntellectRuleGroupDtlService;
import com.bytter.bill.domain.bill.service.AbIntellectRuleGroupService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupVO;
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
 * 智能选票规则组处理Controller
 *
 * @author zhenghj
 */
@Controller
@RequestMapping("/bill/intellect/ruleGroup")
public class AbIntellectRuleGroupController extends BaseController {
    @Autowired
    private AbIntellectRuleGroupService abIntellectRuleGroupService;
    @Autowired
    private AbIntellectRuleGroupDtlService abIntellectRuleGroupDtlService;
    /**
     * 选票规则组列表跳转
     *
     * @param model 模型
     * @return 选票规则组列表页
     */
    @GetMapping("/list")
    public String listElement(Model model) {
        return "bill/accebill/intellect/ruleGroup/listRuleGroup";
    }
    /**
     * 选择规则列表跳转
     *
     * @param model 模型
     * @return 选择规则列表
     */
    @GetMapping("/selectRule")
    public String selectRule(Model model) {
        return "bill/accebill/intellect/ruleGroup/selectRule";
    }
    /**
     * 选票规则组列表查询
     *
     * @param input 前端参数
     * @param model 模型
     * @return 选票规则组列表数据
     */
    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput datatable(@RequestBody DataTablesInput input, Model model) {
        return abIntellectRuleGroupService.datatable(input);
    }
    /**
     * 选票规则组新增页面跳转
     *
     * @param model 模型
     * @return 选票规则组新增页面
     */
    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("applyNo", SequenceSingleton.getSequence(SequenceEnum.BILL_RULE_GROUP_APPLY_NO));
        return "bill/accebill/intellect/ruleGroup/addRuleGroup";
    }
    /**
     * 选票规则组编辑页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则组编辑页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        AbIntellectRuleGroupVO ruleVo = abIntellectRuleGroupService.getAllAbIntellectRuleGroupVOById(id);
        model.addAttribute("ruleGroup", ruleVo);
        abIntellectRuleGroupDtlService.delUselessByApplyNo(ruleVo.getApplyNo());
        return "bill/accebill/intellect/ruleGroup/editRuleGroup";
    }
    /**
     * 选票规则组查看页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则组查看页面
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        model.addAttribute("ruleGroup", abIntellectRuleGroupService.getAllAbIntellectRuleGroupVOById(id));
        return "bill/accebill/intellect/ruleGroup/viewRuleGroup";
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
    public AjaxJson save(AbIntellectRuleGroupVO abIntellectRuleGroupVO, Model model) {
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
    public AjaxJson update(AbIntellectRuleGroupVO abIntellectRuleGroupVO, Model model) {
        abIntellectRuleGroupService.updateRuleGroup(abIntellectRuleGroupVO, getCurrentUser());
        return success();
    }
    /**
     * 选票规则组删除操作
     *
     * @param id    选票规则组id
     * @param model 模型
     * @return 是否成功
     */
    @ResponseBody
    @PostMapping("/del/{id}")
    public AjaxJson del(@PathVariable("id") String id, Model model) {
        abIntellectRuleGroupService.delRuleGroup(id);
        return success();
    }
    /**
     * <b>功能：</b>获取有效的选票规则组组<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    @RequestMapping(params = "listAbIntellectRuleGroup")
    @ResponseBody
    public List<AbIntellectRuleGroupVO> listAbIntellectRuleGroup() {
        return abIntellectRuleGroupService.listAbIntellectRuleGroup();
    }
}
