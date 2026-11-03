package com.bytter.biz.finacial.interest.controller;

import com.bytter.biz.finacial.interest.service.FcInterestDtlRuleService;
import com.bytter.biz.finacial.interest.vo.FcInterestDtlRuleVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 利息流水规则控制器。
 *
 * @author zhaohc
 * @date 2024/4/29 16:30
 */
@Controller
@RequestMapping("/finacial/interest/fcinterestdtlrule")
public class FcInterestDtlRuleController extends BaseController {

    @Autowired
    private FcInterestDtlRuleService fcInterestDtlRuleService;

    /**
     * 利息流水规则表列表 页面跳转。
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "finacial/interest/listFcInterestDtlRule";
    }

    /**
     * 利息流水规则表列表 table列表
     */
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fcInterestDtlRuleService.listDataTable(dataTablesInput);
    }

    /**
     * 利息流水规则表新增页面跳转
     */
    @GetMapping("/add")
    public String add(Model model) {
        return "finacial/interest/addFcInterestDtlRule";
    }

    /**
     * 添加利息流水规则表
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FcInterestDtlRuleVo interestDtlRuleVo, HttpServletRequest request){
        fcInterestDtlRuleService.saveOrModify(interestDtlRuleVo);
        return success();
    }

    /**
     * 利息流水规则表编辑页面跳转
     */
    @GetMapping("/update")
    public String update(FcInterestDtlRuleVo interestDtlRule, Model model) {
        if (StringUtils.isNotEmpty(interestDtlRule.getId())) {
            interestDtlRule = fcInterestDtlRuleService.getInterestRuleById(interestDtlRule.getId());
            model.addAttribute("interestDtlRule", interestDtlRule);
        }
        return "finacial/interest/updateFcInterestDtlRule";
    }

    /**
     * 利息流水规则表详情页
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FcInterestDtlRuleVo interestDtlRule = fcInterestDtlRuleService.getInterestRuleById(id);
        model.addAttribute("interestDtlRule", interestDtlRule);
        return "finacial/interest/viewFcInterestDtlRule";
    }

    /**
     * 删除利息流水规则表
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        FcInterestDtlRuleVo interestDtlRule = fcInterestDtlRuleService.getInterestRuleById(id);
        fcInterestDtlRuleService.deleteInterestDtlRule(interestDtlRule);
        return success();
    }

}
