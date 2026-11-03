package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexCustFieldRuleTenant;
import com.bytter.biz.foreign.service.FexCustFieldRuleTenantService;
import com.bytter.biz.foreign.vo.FexCustFieldRuleTenantVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.IExternalCorpService;
import com.bytter.system.api.dto.ExternalCorpDTO;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Title: Controller
 * @Description: 自定义字段维护Controller
 * @author zhengxn
 * @date 2020-02-17 14:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexcustfieldrule")
public class FexCustFieldRuleController extends BaseController {

    @Autowired
    private FexCustFieldRuleTenantService fexCustFieldRuleTenantService;
    @DubboReference(check = false)
    private IExternalCorpService externalCorpService;

    /**
     * 自定义字段规则 页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @LogMessage(content = "查询自定义字段规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexCustFieldRule";
    }
    

    /**
     * 则自定义字段规  table列表
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexCustFieldRuleTenantService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 自定义字段规则新增页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @PostMapping("/addRe")
    public String addRe( FexCustFieldRuleTenantVO fexCustFieldRuleVO,  Model model)  {
        model.addAttribute("fexCustFieldRuleVO", fexCustFieldRuleVO);
        return "foreign/addFexCustFieldRule";
    }

    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @GetMapping("/add")
    public String add( FexCustFieldRuleTenantVO fexCustFieldRuleVO,  Model model)  {
        model.addAttribute("fexCustFieldRuleVO", fexCustFieldRuleVO);
        return "foreign/addFexCustFieldRule";
    }


    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @PostMapping("/addPayRule")
    public String addPayRule( FexCustFieldRuleTenantVO fexCustFieldRuleVO, Model model)  {
        model.addAttribute("fexCustFieldRuleVO", fexCustFieldRuleVO);
        return "foreign/fexCustFieldRule/addPayRule";
    }
    @LogMessage(content = "添加自定义字段规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexCustFieldRuleTenantVO fexCustFieldRuleVO){
        FexCustFieldRuleTenant fexCustFieldRule = new FexCustFieldRuleTenant();
        BeanUtils.copyNotNullProperties(fexCustFieldRuleVO, fexCustFieldRule);
        fexCustFieldRuleTenantService.save(fexCustFieldRule, getUserId());
        return success();
    }
    
    
    /**
     * 自定义字段规则编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @GetMapping("/update")
    public String update(FexCustFieldRuleTenant fexCustFieldRule, Model model) {
        if (StringUtils.isNotEmpty(fexCustFieldRule.getId())) {
            fexCustFieldRule = fexCustFieldRuleTenantService.get(fexCustFieldRule.getId());
            FexCustFieldRuleTenantVO vo = new FexCustFieldRuleTenantVO();
            BeanUtils.copyProperties(fexCustFieldRule,vo);
            if(StringUtils.isNotEmpty(vo.getControlRule())){
                String[] rules = vo.getControlRule().split(",");
                for (String s:rules) {
                    if ("1".equals(s)) {
                        vo.setRule1(s);
                    } else if ("2".equals(s)) {
                        vo.setRule2(s);
                    } else if ("3".equals(s)) {
                        vo.setRule3(s);
                    } else if ("4".equals(s)) {
                        vo.setRule4(s);
                    }
                }
            }
            model.addAttribute("fexCustFieldRulePage", vo);
        }
        return "foreign/updateFexCustFieldRule";
    }


    /**
     * 自定义字段规则编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @GetMapping("/updatePayRule")
    public String updatePayRule(FexCustFieldRuleTenant fexCustFieldRule, Model model) {
        if (StringUtils.isNotEmpty(fexCustFieldRule.getId())) {
            fexCustFieldRule = fexCustFieldRuleTenantService.get(fexCustFieldRule.getId());
            FexCustFieldRuleTenantVO vo = new FexCustFieldRuleTenantVO();
            BeanUtils.copyProperties(fexCustFieldRule,vo);
            if(StringUtils.isNotEmpty(vo.getControlRule())){
                String[] rules = vo.getControlRule().split(",");
                for (String s:rules) {
                    if ("1".equals(s)) {
                        vo.setRule1(s);
                    } else if ("2".equals(s)) {
                        vo.setRule2(s);
                    } else if ("3".equals(s)) {
                        vo.setRule3(s);
                    } else if ("4".equals(s)) {
                        vo.setRule4(s);
                    }
                }
            }
            model.addAttribute("fexCustFieldRulePage", vo);
        }
        return "foreign/fexCustFieldRule/updatePayRule";
    }


    /**
     * 修改自定义字段规则
     */
    @LogMessage(content = "修改自定义字段规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexCustFieldRuleTenantVO fexCustFieldRuleVO){
        FexCustFieldRuleTenant entity = new FexCustFieldRuleTenant();
        BeanUtils.copyProperties(fexCustFieldRuleVO,entity);
        fexCustFieldRuleTenantService.update(entity, getUserId());
        return  success();
    }

    /**
     * 自定义字段规则详情页
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @LogMessage(content = "查询自定义字段规则详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexCustFieldRuleTenantVO vo = fexCustFieldRuleTenantService.getRuleById(id);
        if(StringUtils.isNotEmpty(vo.getControlRule())){
            String[] rules = vo.getControlRule().split(",");
            for (String s:rules) {
                if ("1".equals(s)) {
                    vo.setRule1(s);
                } else if ("2".equals(s)) {
                    vo.setRule2(s);
                } else if ("3".equals(s)) {
                    vo.setRule3(s);
                } else if ("4".equals(s)) {
                    vo.setRule4(s);
                }
            }
        }
        ExternalCorpDTO cotpVO = externalCorpService.getById(vo.getAppRange2());
        model.addAttribute("fexCustFieldRulePage", vo);
        if(cotpVO != null) {
            model.addAttribute("cotpName", cotpVO.getName());
        }
        return "foreign/viewFexCustFieldRule";
    }

    /**
     * 自定义字段规则详情页
     *
     * @return
     */
    //RequiresPermissions(value = ResourceConstant.FOREIGN_FEXCUSTFIELDRULE_APPLY)
    @LogMessage(content = "查询自定义字段规则详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewPayRule/{id}")
    public String viewPayRule(@PathVariable("id") String id, Model model) {
        FexCustFieldRuleTenantVO vo = fexCustFieldRuleTenantService.getRuleById(id);
        ExternalCorpDTO cotpVO = externalCorpService.getById(vo.getAppRange2());
        model.addAttribute("fexCustFieldRulePage", vo);
        if(cotpVO != null) {
            model.addAttribute("cotpName", cotpVO.getName());
        }
        return "foreign/fexCustFieldRule/viewFexCustFieldRule";
    }

    

    /**
     * 删除自定义字段规则
     * @param id
     * @return
     */
    @LogMessage(content = "删除自定义字段规则",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        fexCustFieldRuleTenantService.deleteEntity(id, this.getCurrentUser());
        return success();
    }


    /**
     * 下拉列表获取应用范围为交易对手数据
     * @return
     */
    @PostMapping("/listByCotp")
    @ResponseBody
    public AjaxJson listByCotp(String cotpGroupId){
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(fexCustFieldRuleTenantService.listByCotp(cotpGroupId));
        return ajaxJson;
    }
}
