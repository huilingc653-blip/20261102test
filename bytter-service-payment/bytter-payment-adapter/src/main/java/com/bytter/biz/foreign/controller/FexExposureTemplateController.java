package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexExposureTemplate;
import com.bytter.biz.foreign.entity.FexExposureTemplateItem;
import com.bytter.biz.foreign.service.FexExposureItemService;
import com.bytter.biz.foreign.service.FexExposureTemplateItemService;
import com.bytter.biz.foreign.service.FexExposureTemplateService;
import com.bytter.biz.foreign.vo.FexExposureItemVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.ParameterException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ForeignMessageEnum;
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

import java.util.List;

/**
 * @Title: Controller
 * @Description: 敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexExposureTemplate")
public class FexExposureTemplateController extends BaseController {
    @Autowired
    private FexExposureTemplateService fexExposureTemplateService;

    @Autowired
    private FexExposureItemService fexExposureItemService;

    @Autowired
    private FexExposureTemplateItemService fexExposureTemplateItemService;

    /**
     * 敞口模板表列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_QUERY)
    @LogMessage(content = "查询敞口模板表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexExposureTemplate";
    }

    /**
     * 敞口模板表 table列表
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_QUERY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexExposureTemplateService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 敞口模板表新增页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @GetMapping("/add")
    public String add(FexExposureTemplate fexExposureTemplate, Model model) {
        if (StringUtils.isNotEmpty(fexExposureTemplate.getId())) {
            fexExposureTemplate = fexExposureTemplateService.get(fexExposureTemplate.getId());
            model.addAttribute("fexExposureTemplatePage", fexExposureTemplate);
        }
        //获取所有有效的敞口科目
        List<FexExposureItemVO> listItem = fexExposureItemService.listTreeData(null, CommonConstant.YES);
        model.addAttribute("listItem", listItem);
        return "foreign/addFexExposureTemplate";
    }

    /**
     * 敞口模板表编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @GetMapping("/update")
    public String update(FexExposureTemplate fexExposureTemplate, Model model) {
        if (StringUtils.isNotEmpty(fexExposureTemplate.getId())) {
            fexExposureTemplate = fexExposureTemplateService.get(fexExposureTemplate.getId());
            model.addAttribute("fexExposureTemplatePage", fexExposureTemplate);
            //获取所有有效的敞口科目
            List<FexExposureItemVO> listItem = fexExposureItemService.listTreeData(null, CommonConstant.YES);
            model.addAttribute("listItem", listItem);
            //获取模板已分配的科目信息
            List<FexExposureTemplateItem> listItemCheck = fexExposureTemplateItemService.listByTemplateId(fexExposureTemplate.getId());
            model.addAttribute("listItemCheck", listItemCheck);
        }
        return "foreign/updateFexExposureTemplate";
    }

    /**
     * 敞口模板表详情页
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_QUERY)
    @LogMessage(content = "查询敞口模板表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexExposureTemplateVO fexExposureTemplate = fexExposureTemplateService.getByIdUnion(id);
        model.addAttribute("fexExposureTemplatePage", fexExposureTemplate);
        //获取所有有效的敞口科目
        List<FexExposureItemVO> listItem = fexExposureItemService.listTreeData(null, CommonConstant.YES);
        model.addAttribute("listItem", listItem);
        //获取模板已分配的科目信息
        List<FexExposureTemplateItem> listItemCheck = fexExposureTemplateItemService.listByTemplateId(fexExposureTemplate.getId());
        model.addAttribute("listItemCheck", listItemCheck);
        return "foreign/viewFexExposureTemplate";
    }

    /**
     * 敞口模板表详情页
     *
     * @return
     */
    @LogMessage(content = "查询敞口模板表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewCommon/{id}")
    public String viewCommon(@PathVariable("id") String id, Model model) {
        FexExposureTemplateVO fexExposureTemplate = fexExposureTemplateService.getByIdUnion(id);
        model.addAttribute("fexExposureTemplatePage", fexExposureTemplate);
        //获取所有有效的敞口科目
        List<FexExposureItemVO> listItem = fexExposureItemService.listTreeData(null, CommonConstant.YES);
        model.addAttribute("listItem", listItem);
        //获取模板已分配的科目信息
        List<FexExposureTemplateItem> listItemCheck = fexExposureTemplateItemService.listByTemplateId(fexExposureTemplate.getId());
        model.addAttribute("listItemCheck", listItemCheck);
        return "foreign/viewFexExposureTemplateCommon";
    }

    /**
     * 添加敞口模板表
     * @param fexExposureTemplate
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "添加敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexExposureTemplate fexExposureTemplate,
                         @RequestParam("itemIds[]") List<String> itemIds, @RequestParam("itemCodes[]") List<String> itemCodes){
        if (fexExposureTemplateService.getByTemplateName(fexExposureTemplate.getTemplateName()) != null) {
            throw new ParameterException(ForeignMessageEnum.FEX_EXPOSURE_TEMPLATE_MESSAGE_TEMPLATENAME_UNIQUE);
        }
        fexExposureTemplate.setCreateBy(this.getCurrentUser().getLoginName());
        fexExposureTemplate.setCreateDate(DateUtils.getDate());
        this.fexExposureTemplateService.saveFexExposureTemplate(fexExposureTemplate, itemIds, itemCodes);
        return success();
    }

    /**
     * 修改敞口模板表
     * @param fexExposureTemplate
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "修改敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexExposureTemplate fexExposureTemplate,
                           @RequestParam("itemIds[]") List<String> itemIds, @RequestParam("itemCodes[]") List<String> itemCodes){
        FexExposureTemplate t = fexExposureTemplateService.get(fexExposureTemplate.getId(), true);
        BeanUtils.copyNotNullProperties(fexExposureTemplate, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        this.fexExposureTemplateService.updateFexExposureTemplate(t, itemIds, itemCodes);
        return  success();
    }

    /**
     * 删除敞口模板表
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "删除敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        FexExposureTemplate fexExposureTemplate = fexExposureTemplateService.get(id);
        fexExposureTemplateService.deleteFexExposureTemplate(fexExposureTemplate);
        return success();
    }

    /**
     * 批量删除敞口模板表
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "批量删除敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        this.fexExposureTemplateService.batchDelete(ids);
        return success();
    }

    /**
     * 获取所有有效敞口模板信息
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<FexExposureTemplate> json() {
        return fexExposureTemplateService.listAllValid();
    }
}
