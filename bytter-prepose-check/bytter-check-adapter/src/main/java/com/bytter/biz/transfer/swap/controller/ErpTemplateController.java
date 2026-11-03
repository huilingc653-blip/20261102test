package com.bytter.biz.transfer.swap.controller;
import com.bytter.biz.transfer.swap.entity.ErpTemplate;
import com.bytter.biz.transfer.swap.service.ErpTemplateService;
import com.bytter.biz.transfer.swap.service.ErpTemplateTypeService;
import com.bytter.biz.transfer.swap.vo.ErpTemplateSourceVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeVO;
import com.bytter.biz.transfer.swap.vo.ErpTemplateVO;
import com.bytter.check.enums.DataExchangeMessageEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * 模板维护
 * Created by zhouj on 2018/5/23.
 */
@Controller
@RequestMapping("/erp/swap/template")
public class ErpTemplateController extends BaseController {
    @Autowired
    private ErpTemplateService templateService;
    @Autowired
    private ErpTemplateTypeService erpTemplateTypeService;
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_VIEW)
    @LogMessage(content = "查询模版列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/list/{id}")
    public String list(@PathVariable("id") String id,String name , Model model) {
        model.addAttribute("typeId",id);
        model.addAttribute("typeName",name);
        return "transfer/swap/template_list";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return templateService.getDataTables(dataTablesInput);
    }
    @GetMapping("/source")
    @ResponseBody
    public List<ErpTemplateSourceVO> source(String code){
        List<ErpTemplateSourceVO> list = templateService.selectSourceByCode(code);
        return list;
    }
    @GetMapping("/dynaSource")
    @ResponseBody
    public Object dynaSource(String type) {
        List<ErpTemplateSourceVO> vos = templateService.dynaSource(type);
        return BeanUtils.copyListProperties(vos, ErpTemplateSourceVO.class);
    }
    @GetMapping("/dynaDataSource")
    @ResponseBody
    public Object dynaDataSource() {
        List<ErpTemplateSourceVO> vos = templateService.dynaDataSource();
        return BeanUtils.copyListProperties(vos, ErpTemplateSourceVO.class);
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_VIEW)
    @LogMessage(content = "查询模版详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        ErpTemplate template = templateService.get(id);
        model.addAttribute("template", template);
        ErpTemplateTypeVO templateType = erpTemplateTypeService.selectTypeAndNameById(template.getTemplateTypeId());
        model.addAttribute("templateType",templateType);
        return "transfer/swap/template_view";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_ADD)
    @GetMapping("/add/{id}")
    public String add(@PathVariable("id") String id,Model model) {
        ErpTemplateTypeVO templateType = erpTemplateTypeService.selectTypeAndNameById(id);
        model.addAttribute("templateType",templateType);
        return "transfer/swap/template_add";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_ADD)
    @LogMessage(content = "新增模版",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ErpTemplateVO templateVO) {
        AjaxJson ajaxJson = new AjaxJson(true);
        String error = ValidationUtil.validation(templateVO);
        if(StringUtils.isNotBlank(error)){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj(error);
            return ajaxJson;
        }
        //判断是否存在
        if(templateService.findExistCode(templateVO.getTemplateCode())){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj("code已经存在");
            return ajaxJson;
        }
        //入库
        templateService.saveTemplateAndDtl(templateVO);
        return success();
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_UPDATE)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        ErpTemplate template = templateService.get(id);
        if(StringUtils.isEmpty(template.getEnteringIsEdit())){
        	template.setEnteringIsEdit("0");
        }
        model.addAttribute("template", template);
        ErpTemplateTypeVO templateType = erpTemplateTypeService.selectTypeAndNameById(template.getTemplateTypeId());
        model.addAttribute("templateType",templateType);
        return "transfer/swap/template_edit";
    }
    @GetMapping("/json/{id}")
    @ResponseBody
    public ErpTemplate json(@PathVariable("id") String id) {
        return templateService.selectDtlMapById(id);
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_UPDATE)
    @LogMessage(content = "修改模版",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ErpTemplateVO templateVO) {
        AjaxJson ajaxJson = new AjaxJson(true);
        String error = ValidationUtil.validation(templateVO);
        if(StringUtils.isNotBlank(error)){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj(error);
            return ajaxJson;
        }
        //判断是否存在
        if(templateService.findExistCodeMyself(templateVO.getId(),templateVO.getTemplateCode())){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj("code已经存在");
            return ajaxJson;
        }
        //入库
        templateService.updateTemplateAndDtl(templateVO);
        return success();
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATE_DELETE)
    @LogMessage(content = "删除模版",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        //校验该模板否存被使用
        boolean flag = templateService.checkDateByTemplateId(id);
        if(flag){
            throw new BusinessException(DataExchangeMessageEnum.DATAEXCHANGE_MESSAGE_TEMPLATE_ISNOTDEL);
        }
        ErpTemplate erpTemplate = templateService.get(id);
        templateService.deleteEntity(erpTemplate);
        return success();
    }
    /**
     * 根据类型id获取模板集合
     * @param typeId
     * @return
     */
    @GetMapping("/jsons/{typeId}")
    @ResponseBody
    public List<ErpTemplate> jsons(@PathVariable("typeId") String typeId) {
        return templateService.selectTypeListById(typeId);
    }
    /**
     * 跳转弹窗选择数据
     * @param model
     * @return
     */
    @GetMapping("/listInput/{code}/{inputId}")
    public String listInput(@PathVariable("code") String code,@PathVariable("inputId")String inputId , Model model) {
        model.addAttribute("code",code);
        model.addAttribute("inputId",inputId);
        return "transfer/swap/template_input_list";
    }
    /**
     * 跳转弹窗选择数据加载列表
     * @return
     */
    @PostMapping("/inputDataTable")
    @ResponseBody
    public DataTablesOutput inputDataTable(@RequestBody DataTablesInput dataTablesInput){
        return templateService.inputDataTable(dataTablesInput);
    }
    /**
     * 根据弹窗控件的code找到对应的name
     * @param code 对应值
     * @param sourceCode 数据来源code
     * @return
     */
    @PostMapping("/getInputDate/{code}/{sourceCode}")
    @ResponseBody
    public AjaxJson getInputDate(@PathVariable("code") String code,@PathVariable("sourceCode") String sourceCode) {
        AjaxJson ajax = new AjaxJson();
        Object name = templateService.getInputDate(code,sourceCode);
        ajax.setObj(name);
        return ajax;
    }
}
