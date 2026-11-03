package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbIntellectRuleGroupDtlService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleGroupDtlVO;
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
 * 智能选票规则组明细处理Controller
 *
 * @author zhenghj
 */
@Controller
@RequestMapping("/bill/intellect/ruleGroupDtl")
public class AbIntellectRuleGroupDtlController extends BaseController {
    @Autowired
    private AbIntellectRuleGroupDtlService abIntellectRuleGroupDtlService;
    /**
     * 选票规则组明细列表查询
     *
     * @param input 前端参数
     * @param model 模型
     * @return 选票规则组明细列表数据
     */
    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput datatable(@RequestBody DataTablesInput input, Model model) {
        return abIntellectRuleGroupDtlService.datatable(input);
    }
    /**
     * 选票规则组明细新增页面跳转
     *
     * @param applyNo 申请单号
     * @param model   模型
     * @return 选票规则组明细新增页面
     */
    @GetMapping("/add/{applyNo}")
    public String add(@PathVariable("applyNo") String applyNo, Model model) {
        model.addAttribute("applyNo", applyNo);
        return "bill/accebill/intellect/ruleGroup/addRuleGroupDtl";
    }
    /**
     * 选票规则组明细编辑页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则组明细编辑页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("ruleGroup", abIntellectRuleGroupDtlService.getAbIntellectRuleGroupDtlVOById(id));
        return "bill/accebill/intellect/ruleGroup/editRuleGroupDtl";
    }
    /**
     * 选票规则组明细查看页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则组明细查看页面
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        model.addAttribute("ruleGroup", abIntellectRuleGroupDtlService.getAbIntellectRuleGroupDtlVOById(id));
        return "bill/accebill/intellect/ruleGroup/viewRuleGroupDtl";
    }
    /**
     * 选票规则组明细保存操作
     *
     * @param abIntellectRuleGroupDtlVO 选票规则组明细vo
     * @param model                     模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO, Model model) {
        abIntellectRuleGroupDtlService.saveRuleGroupDtl(abIntellectRuleGroupDtlVO, getCurrentUser());
        return success();
    }
    /**
     * 选票规则组明细编辑操作
     *
     * @param abIntellectRuleGroupDtlVO 选票规则组明细vo
     * @param model                     模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO, Model model) {
        abIntellectRuleGroupDtlService.updateRuleGroupDtl(abIntellectRuleGroupDtlVO, getCurrentUser());
        return success();
    }
    /**
     * 选票规则组明细删除操作
     *
     * @param abIntellectRuleGroupDtlVO 选票规则组明细vo
     * @param model                     模型
     * @return 是否成功
     */
    @ResponseBody
    @PostMapping("/del")
    public AjaxJson del(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO, Model model) {
        abIntellectRuleGroupDtlService.delRuleGroupDtl(abIntellectRuleGroupDtlVO);
        return success();
    }
    /**
     * <b>功能：</b>获取有效的选票规则组明细<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    @RequestMapping("listAbIntellectRuleGroupDtl")
    @ResponseBody
    public List<AbIntellectRuleGroupDtlVO> listAbIntellectRuleGroupDtl() {
        return abIntellectRuleGroupDtlService.listAbIntellectRuleGroupDtl();
    }
    /**
     * 上移下移操作
     *
     * @param abIntellectRuleGroupDtlVO 选票规则组明细VO
     * @param model                     模型
     * @return 是否成功
     */
    @ResponseBody
    @PostMapping("/moveUpOrDown")
    public AjaxJson moveUpOrDown(AbIntellectRuleGroupDtlVO abIntellectRuleGroupDtlVO, Model model) {
        abIntellectRuleGroupDtlService.moveUpOrDown(abIntellectRuleGroupDtlVO);
        return success();
    }
}
