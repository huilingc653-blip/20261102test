package com.bytter.bill.web.bill.controller;
import com.bytter.bill.domain.bill.service.AbIntellectRuleDtlService;
import com.bytter.bill.domain.bill.vo.AbIntellectRuleDtlVO;
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
 * 智能选票规则明细处理Controller
 *
 * @author zhenghj
 */
@Controller
@RequestMapping("/bill/intellect/ruleDtl")
public class AbIntellectRuleDtlController extends BaseController {
    @Autowired
    private AbIntellectRuleDtlService abIntellectRuleDtlService;
    /**
     * 选票规则明细列表查询
     *
     * @param input 前端参数
     * @param model 模型
     * @return 选票规则明细列表数据
     */
    @ResponseBody
    @PostMapping("/datatable")
    public DataTablesOutput datatable(@RequestBody DataTablesInput input, Model model) {
        return abIntellectRuleDtlService.datatable(input);
    }
    /**
     * 选票规则明细新增页面跳转
     *
     * @param applyNo 申请单号
     * @param model   模型
     * @return 选票规则明细新增页面
     */
    @GetMapping("/add/{applyNo}")
    public String add(@PathVariable("applyNo") String applyNo, Model model) {
        model.addAttribute("applyNo", applyNo);
        return "bill/accebill/intellect/rule/addRuleDtl";
    }
    /**
     * 选票规则明细编辑页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则明细编辑页面
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("rule", abIntellectRuleDtlService.getAbIntellectRuleDtlVOById(id));
        return "bill/accebill/intellect/rule/editRuleDtl";
    }
    /**
     * 选票规则明细查看页面跳转
     *
     * @param id    主键id
     * @param model 模型
     * @return 选票规则明细查看页面
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        model.addAttribute("rule", abIntellectRuleDtlService.getAbIntellectRuleDtlVOById(id));
        return "bill/accebill/intellect/rule/viewRuleDtl";
    }
    /**
     * 选票规则明细保存操作
     *
     * @param abIntellectRuleDtlVO 选票规则明细vo
     * @param model                模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/save")
    public AjaxJson save(AbIntellectRuleDtlVO abIntellectRuleDtlVO, Model model) {
        abIntellectRuleDtlService.saveRuleDtl(abIntellectRuleDtlVO, getCurrentUser());
        return success();
    }
    /**
     * 选票规则明细编辑操作
     *
     * @param abIntellectRuleDtlVO 选票规则明细vo
     * @param model                模型
     * @return 是否保存成功
     */
    @ResponseBody
    @PostMapping("/update")
    public AjaxJson update(AbIntellectRuleDtlVO abIntellectRuleDtlVO, Model model) {
        abIntellectRuleDtlService.updateRuleDtl(abIntellectRuleDtlVO, getCurrentUser());
        return success();
    }
    /**
     * 选票规则明细删除操作
     *
     * @param abIntellectRuleDtlVO 选票规则明细vo
     * @param model                模型
     * @return 是否成功
     */
    @ResponseBody
    @PostMapping("/del")
    public AjaxJson del(AbIntellectRuleDtlVO abIntellectRuleDtlVO, Model model) {
        abIntellectRuleDtlService.delRuleDtl(abIntellectRuleDtlVO);
        return success();
    }
    /**
     * <b>功能：</b>获取有效的选票规则明细<br>
     * <b>日期：</b> 2022-10-11 <br>
     */
    @RequestMapping("listAbIntellectRuleDtl")
    @ResponseBody
    public List<AbIntellectRuleDtlVO> listAbIntellectRuleDtl() {
        return abIntellectRuleDtlService.listAbIntellectRuleDtl();
    }
    /**
     * 上移下移操作
     *
     * @param abIntellectRuleDtlVO 选票规则明细VO
     * @param model                模型
     * @return 是否成功
     */
    @ResponseBody
    @PostMapping("/moveUpOrDown")
    public AjaxJson moveUpOrDown(AbIntellectRuleDtlVO abIntellectRuleDtlVO, Model model) {
        abIntellectRuleDtlService.moveUpOrDown(abIntellectRuleDtlVO);
        return success();
    }
}
