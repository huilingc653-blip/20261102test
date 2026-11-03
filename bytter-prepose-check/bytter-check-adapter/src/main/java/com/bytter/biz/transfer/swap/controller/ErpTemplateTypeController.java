package com.bytter.biz.transfer.swap.controller;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.bytter.biz.dataexchange.basic.service.ErpDataTypeService;
import com.bytter.biz.transfer.swap.entity.ErpTemplateType;
import com.bytter.biz.transfer.swap.service.ErpTemplateService;
import com.bytter.biz.transfer.swap.service.ErpTemplateTypeService;
import com.bytter.biz.transfer.swap.vo.ErpTemplateTypeVO;
import com.bytter.check.enums.DataExchangeMessageEnum;
import com.bytter.check.enums.TemplateTypeEnum;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * Created by zhouj on 2018/5/19.
 */
@Controller
@RequestMapping("/erp/swap/templatetype")
public class ErpTemplateTypeController extends BaseController {
    @Autowired
    private ErpTemplateTypeService templateTypeService;
    @Autowired
    private ErpDataTypeService erpDataTypeService;
    @Autowired
    private ErpTemplateService templateService;
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_VIEW)
    @LogMessage(content = "查询模版类型列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_SELECT)
    @GetMapping("/list")
    public String list() {
        return "transfer/swap/templatetype_list";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_VIEW)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
    	DataTablesOutput output = templateTypeService.getDataTables(dataTablesInput);
    	List<ErpTemplateType> list = output.getData();
    	if(CollectionUtils.isEmpty(list)) {
    		return new DataTablesOutput();
    	}else {
    		for(ErpTemplateType type:list) {
    			if(type.getStatus().equals(TemplateTypeEnum.TEMP_STATUS_TEMP.getCode())) {
    				type.setStatus(TemplateTypeEnum.TEMP_STATUS_TEMP.getName());
    			}else {
    				type.setStatus(TemplateTypeEnum.TEMP_STATUS_SAVE.getName());
    			}
    		}
    	}
        return output;
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_VIEW)
    @LogMessage(content = "查询模版类型详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        ErpTemplateTypeVO templateType = templateTypeService.selectTypeAndNameById(id);
        model.addAttribute("templateType", templateType);
        return "transfer/swap/templatetype_view";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_ADD)
    @GetMapping("/add")
    public String add() {
        return "transfer/swap/templatetype_add";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_ADD)
    @LogMessage(content = "新增模版类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(ErpTemplateTypeVO type) {
        AjaxJson ajaxJson = new AjaxJson(true);
        String error = ValidationUtil.validation(type);
        if(StringUtils.isNotBlank(error)){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj(error);
            return ajaxJson;
        }
        //判断是否存在
        if(templateTypeService.findExistCode(type.getTypeCode())){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj("code已经存在");
            return ajaxJson;
        }
        //入库
        templateTypeService.saveTemplateTypeAndDtl(type);
        return success();
    }
    
    @LogMessage(content = "暂存模版类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/tempSave")
    @ResponseBody
    public AjaxJson tempSave(ErpTemplateTypeVO type) {
        AjaxJson ajaxJson = new AjaxJson(true);
        String error = ValidationUtil.validation(type);
        if(StringUtils.isNotBlank(error)){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj(error);
            return ajaxJson;
        }
        //判断是否存在
        if(templateTypeService.findExistCode(type.getTypeCode())){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj("code已经存在");
            return ajaxJson;
        }
        //入库
        templateTypeService.saveTemplateTypeAndDtlForTemp(type);
        return success();
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_UPDATE)
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") String id, Model model) {
        ErpTemplateTypeVO templateType = templateTypeService.selectTypeAndNameById(id);
        model.addAttribute("templateType", templateType);
        return "transfer/swap/templatetype_edit";
    }
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_UPDATE)
    @LogMessage(content = "修改模版类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson modify(ErpTemplateTypeVO type) {
        AjaxJson ajaxJson = new AjaxJson(true);
        String error = ValidationUtil.validation(type);
        if(StringUtils.isNotBlank(error)){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj(error);
            return ajaxJson;
        }
        //判断是否存在
        if(templateTypeService.findExistCodeMyself(type.getId(),type.getTypeCode())){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj("code已经存在");
            return ajaxJson;
        }
        //修改
        templateTypeService.updateTemplateTypeAndDtl(type);
        return success();
    }
    
    @LogMessage(content = "修改模版类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/tempModify")
    @ResponseBody
    public AjaxJson tempModify(ErpTemplateTypeVO type) {
        AjaxJson ajaxJson = new AjaxJson(true);
        String error = ValidationUtil.validation(type);
        if(StringUtils.isNotBlank(error)){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj(error);
            return ajaxJson;
        }
        //判断是否存在
        if(templateTypeService.findExistCodeMyself(type.getId(),type.getTypeCode())){
            ajaxJson.setSuccess(false);
            ajaxJson.setObj("code已经存在");
            return ajaxJson;
        }
        //修改
        templateTypeService.updateTemplateTypeAndDtl(type);
        return success();
    }
    
//    @RequiresPermissions(DataexchangeResourceConstant.ERP_SWAY_TEMPLATETYPE_DELETE)
    @LogMessage(content = "删除模版类型",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        //校验该模板类型下是否存在模板
        boolean flag = templateService.checkDateByTemplateTypeId(id);
        if(flag){
            throw new BusinessException(DataExchangeMessageEnum.DATAEXCHANGE_MESSAGE_TEMPLATETYPE_ISNOTDEL);
        }
        ErpTemplateType erpTemplateType = templateTypeService.get(id);
        templateTypeService.deleteEntity(erpTemplateType);
        return success();
    }
    /**
     * 获取数据源数据
     * @param flag
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public Object json(String flag) {
        return erpDataTypeService.listErpDataTypeByValidSign(flag);
    }
    /**
     * 获取模板类型
     * @return
     */
    @GetMapping("/listjson")
    @ResponseBody
    public List<ErpTemplateType> listjson(){
        return templateTypeService.listAll();
    }
}
