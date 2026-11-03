package com.bytter.biz.budget.controller;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bytter.biz.budget.entity.FbsProcessRule;
import com.bytter.biz.budget.service.FbsProcessRuleService;
import com.bytter.biz.budget.spi.constant.ResourceConstant;
import com.bytter.biz.budget.spi.enums.FbsBudgetMessageEnum;
import com.bytter.biz.budget.vo.FbsProcessRuleVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
/**
 * @author huangx
 * @version 1.0
 * @Title: Controller
 * @Description: 资金计划执行规则表
 * @date 2020-08-31 14:51:48
 */
@Controller
@RequestMapping("/budget/fbsprocessrule")
public class FbsProcessRuleController extends BaseController {
    @Autowired
    private FbsProcessRuleService fbsProcessRuleService;
    /**
     * 资金计划执行规则表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @LogMessage(content = "查询资金计划执行规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "budget/listFbsProcessRule";
    }
    /**
     * 资金计划执行规则表 table列表
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fbsProcessRuleService.getDataTables(dataTablesInput);
    }
    /**
     * 资金计划执行规则表新增页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @GetMapping("/add")
    public String add(FbsProcessRule fbsProcessRule, Model model) {
        if (StringUtils.isNotEmpty(fbsProcessRule.getId())) {
            fbsProcessRule = fbsProcessRuleService.get(fbsProcessRule.getId());
            model.addAttribute("fbsProcessRulePage", fbsProcessRule);
        }
        return "budget/addFbsProcessRule";
    }
    /**
     * 资金计划执行规则表编辑页面跳转
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @GetMapping("/update")
    public String update(FbsProcessRule fbsProcessRule, Model model) {
        if (StringUtils.isNotEmpty(fbsProcessRule.getId())) {
            fbsProcessRule = fbsProcessRuleService.get(fbsProcessRule.getId());
            model.addAttribute("fbsProcessRulePage", fbsProcessRule);
        }
        return "budget/updateFbsProcessRule";
    }
    /**
     * 资金计划执行规则表详情页
     *
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @LogMessage(content = "查询资金计划执行规则表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FbsProcessRule fbsProcessRule = fbsProcessRuleService.get(id);
        model.addAttribute("fbsProcessRulePage", fbsProcessRule);
        return "budget/viewFbsProcessRule";
    }
    /**
     * 添加资金计划执行规则表
     *
     * @param fbsProcessRule
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @LogMessage(content = "添加资金计划执行规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FbsProcessRule fbsProcessRule) {
        this.dataCheck(fbsProcessRule);
        fbsProcessRule.setCreateBy(this.getCurrentUser().getLoginName());
        fbsProcessRule.setCreateDate(DateUtils.getDate());
        fbsProcessRuleService.saveEntity(fbsProcessRule);
        return success();
    }
    /**
     * 修改资金计划执行规则表
     *
     * @param fbsProcessRule
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @LogMessage(content = "修改资金计划执行规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FbsProcessRule fbsProcessRule) {
        this.dataCheck(fbsProcessRule);
        FbsProcessRule f = fbsProcessRuleService.get(fbsProcessRule.getId());
        fbsProcessRule.setUpdateBy(this.getCurrentUser().getLoginName());
        fbsProcessRule.setUpdateDate(DateUtils.getDate());
        BeanUtils.copyNotNullProperties(fbsProcessRule, f);
        fbsProcessRuleService.saveOrUpdate(f);
        return  success();
    }
    /**
     * 数据校验
     *
     * @param fbsProcessRule
     * @return
     */
    public boolean dataCheck(FbsProcessRule fbsProcessRule) {
        FbsProcessRuleVO fbsProcessRuleVO = fbsProcessRuleService.isExistProcessRule(fbsProcessRule.getProcessRule(), fbsProcessRule.getId());
        if (fbsProcessRuleVO != null) {
            //执行规则[×××]已存在，请重新输入!
            throw new ParameterException(FbsBudgetMessageEnum.FBS_PROCESS_RULE_MESSAGE_EXIST_PROCESSRULE, fbsProcessRuleVO.getProcessRuleName());
        }
        fbsProcessRuleVO = fbsProcessRuleService.isExistSort(fbsProcessRule.getSort(), fbsProcessRule.getId());
        if (fbsProcessRuleVO != null) {
            //优先级[×××]已存在，请重新输入!
            throw new ParameterException(FbsBudgetMessageEnum.FBS_PROCESS_RULE_MESSAGE_EXIST_SORT, fbsProcessRuleVO.getSort().toString());
        }
        return true;
    }
    /**
     * 删除资金计划执行规则表
     * @param id
     * @return
     */
    //@RequiresPermissions(ResourceConstant.FBS_FBSPROCESSRULE_APPLY)
    @LogMessage(content = "删除资金计划执行规则表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        FbsProcessRule fbsProcessRule = fbsProcessRuleService.get(id);
        this.fbsProcessRuleService.deleteEntity(fbsProcessRule);
        return success();
    }
    /**
     * 批量启用资金计划执行规则
     *
     * @return
     */
    @LogMessage(content = "批量启用资金计划执行规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchEnable")
    @ResponseBody
    public AjaxJson batchEnable(@RequestParam("ids") String ids) {
        this.fbsProcessRuleService.batchUpdateValidSign(ids, CommonConstant.YES);
        return success();
    }
    /**
     * 批量停用资金计划执行规则
     *
     * @return
     */
    @LogMessage(content = "批量停用资金计划执行规则", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/batchStop")
    @ResponseBody
    public AjaxJson batchStop(@RequestParam("ids") String ids) {
        this.fbsProcessRuleService.batchUpdateValidSign(ids, CommonConstant.NO);
        return success();
    }
}
