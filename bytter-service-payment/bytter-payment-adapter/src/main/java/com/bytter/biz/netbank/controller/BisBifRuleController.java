package com.bytter.biz.netbank.controller;
import com.bytter.biz.netbank.service.BisBifRuleService;
import com.bytter.biz.bdlink.api.enums.NetbankMessageEnum;
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
import com.bytter.system.api.dto.BisBifRuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
////import org.apache.shiro.authz.annotation.RequiresPermissions;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Controller
 * @Description: 接口代码业务规则表
 * @date 2017-05-17 10:16:20
 */
@Controller
@RequestMapping("/netbank/bisbifrule")
public class BisBifRuleController extends BaseController {
    @Autowired
    private BisBifRuleService bisBifRuleService;
    /**
     * 接口代码业务规则表列表 页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @LogMessage(content = "查询接口代码业务规则表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "netbank/listBisBifRule";
    }
    /**
     * 接口代码业务规则表 table列表
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return bisBifRuleService.getDataTablesUnion(dataTablesInput);
    }
    /**
     * 接口代码业务规则表新增页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @GetMapping("/add")
    public String add(BisBifRuleDTO bisBifRule, Model model) {
        if (StringUtils.isNotEmpty(bisBifRule.getId())) {
            bisBifRule = bisBifRuleService.getById(bisBifRule.getId());
            model.addAttribute("bisBifRulePage", bisBifRule);
        }
        return "netbank/addBisBifRule";
    }
    /**
     * 接口代码业务规则表编辑页面跳转
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @GetMapping("/update")
    public String update(BisBifRuleDTO bisBifRule, Model model) {
        if (StringUtils.isNotEmpty(bisBifRule.getId())) {
            bisBifRule = bisBifRuleService.getById(bisBifRule.getId());
            model.addAttribute("bisBifRulePage", bisBifRule);
        }
        return "netbank/updateBisBifRule";
    }
    /**
     * 接口代码业务规则表详情页
     *
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @LogMessage(content = "查询接口代码业务规则表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        BisBifRuleDTO bisBifRule = bisBifRuleService.getByIdUnion(id);
        model.addAttribute("bisBifRulePage", bisBifRule);
        return "netbank/viewBisBifRule";
    }
    /**
     * 添加接口代码业务规则表
     *
     * @param bisBifRule
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @LogMessage(content = "添加接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(BisBifRuleDTO bisBifRule) {
        if (StringUtils.isEmpty(bisBifRule.getBifCode().trim())) {
            throw new ParameterException(NetbankMessageEnum.NIS_BISBIF_MESSAGE_BIFCODE_NULL);
        }
        if (bisBifRuleService.findByBifCode(bisBifRule.getBifCode()) != null) {
            throw new ParameterException(NetbankMessageEnum.NIS_BISBIF_MESSAGE_BIFCODE_UNIQUE);
        }
        bisBifRule.setCreateBy(this.getCurrentUser().getLoginName());
        bisBifRule.setCreateDate(DateUtils.getDate());
        this.bisBifRuleService.save(bisBifRule);
        return success();
    }
    /**
     * 修改接口代码业务规则表
     *
     * @param bisBifRule
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @LogMessage(content = "修改接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(BisBifRuleDTO bisBifRule) {
        BisBifRuleDTO t = bisBifRuleService.getById(bisBifRule.getId());
        BeanUtils.copyNotNullProperties(bisBifRule, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        bisBifRuleService.saveOrUpdate(t);
        return success();
    }
    /**
     * 删除接口代码业务规则表
     *
     * @param id
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @LogMessage(content = "删除接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        bisBifRuleService.delete(id);
        return success();
    }
    /**
     * 批量删除接口代码业务规则表
     *
     * @param ids
     * @return
     */
    ////@RequiresPermissions(ResourceConstant.NETBANK_BASICDATA_BISBIFRULE_APPLY)
    @LogMessage(content = "批量删除接口代码业务规则表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@RequestParam("ids") String ids) {
        for (String id : ids.split(CommonConstant.COMMA_SPLIT)) {
            bisBifRuleService.delete(id);
        }
        return success();
    }
}
