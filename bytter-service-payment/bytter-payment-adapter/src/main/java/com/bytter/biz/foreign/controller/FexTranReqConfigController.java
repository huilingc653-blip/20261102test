package com.bytter.biz.foreign.controller;


import com.bytter.biz.foreign.service.FexTranConfigTemplateService;
import com.bytter.biz.foreign.service.FexTranReqConfigService;
import com.bytter.biz.foreign.service.FexTranReqConfigVersionService;
import com.bytter.biz.foreign.vo.FexTemplateConfigVo;
import com.bytter.biz.foreign.vo.FexTranReqConfigVo;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.sequence.SequenceSingleton;
import com.bytter.core.sequence.enums.SequenceEnum;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: Controller
 * @Description: 资金收付交易页面配置
 * @author chenqin
 * @date 2025-11-21
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexTranReqConfig")
public class FexTranReqConfigController extends BaseController {

    @Autowired
    private FexTranReqConfigService fexTranReqConfigService;

    @Autowired
    private FexTranReqConfigVersionService fexTranReqConfigVersionService;

    @Autowired
    private FexTranConfigTemplateService fexTranConfigTemplateService;

    private static final String baseInfoArea = "baseInfo";

    /**
     *
     * 新增配置页面
     *
     * @param templateId
     * @param model
     * @return
     */
    @GetMapping("/addTemplateConfig/{templateId}")
    public String addTemplateConfig(@PathVariable("templateId") String templateId, Model model){
        String versionNo = fexTranReqConfigVersionService.getMaxVersionNo(templateId);
        if (StringUtils.isEmpty(versionNo)) {
            versionNo = SequenceSingleton.getSequence(SequenceEnum.FEX_TEMPLATE_CONFIG_VERSION_NO);
        }
        if (StringUtils.isNotEmpty(templateId)) {
            model.addAttribute("templateId", templateId);
            model.addAttribute("versionNo", versionNo);
        }
        return "foreign/foreignTranReqConfig/addTemplateConfig";
    }

    /**
     *
     * 查询模板配置
     *
     * @param templateId
     * @param versionNo
     * @return
     */
    @RequestMapping("/getTemplateConfig/{templateId}/{versionNo}")
    @ResponseBody
    public AjaxJson getTemplateConfig(@PathVariable("templateId") String templateId, @PathVariable("versionNo") String versionNo, String custFieldIds){
        List<FexTranReqConfigVo> templateConfigs = fexTranReqConfigService.getTemplateConfig(templateId, versionNo, custFieldIds);
        return success(templateConfigs);
    }

    @RequestMapping("/getTemplateConfigByAereTree/{templateId}/{versionNo}")
    @ResponseBody
    public AjaxJson getTemplateConfigByAereTree(@PathVariable("templateId") String templateId, @PathVariable("versionNo") String versionNo){
        List<FexTranReqConfigVo> templateConfigs = fexTranReqConfigService.getTemplateConfigByAereTree(templateId, versionNo);
        return success(templateConfigs);
    }

    @RequestMapping("/getTemplateConfigByAereTree/{templateId}")
    @ResponseBody
    public AjaxJson getTemplateConfigByAereTree(@PathVariable("templateId") String templateId){
        String versionNo = fexTranReqConfigService.getMaxVersionNo(templateId);
        List<FexTranReqConfigVo> templateConfigs = fexTranReqConfigService.getTemplateConfigByAereTree(templateId, versionNo);
        return success(templateConfigs);
    }

    @PostMapping("/saveTemplateConfig")
    @ResponseBody
    public AjaxJson saveTemplateConfig(@RequestBody FexTemplateConfigVo fexTemplateConfigVo){
        fexTranConfigTemplateService.validateUnFinishedBill(fexTemplateConfigVo.getTemplateId());
        fexTranReqConfigService.saveTemplateConfig(fexTemplateConfigVo, getCurrentUser());
        return success();
    }

    /**
     *
     * 获取模板配置历史版本号
     *
     * @param templateId
     * @return
     */
    @PostMapping("/listTmplateHisVersionNos/{templateId}")
    @ResponseBody
    public List<String> listTmplateHisVersionNos(@PathVariable("templateId") String templateId){
        return fexTranReqConfigVersionService.listTmplateHisVersionNos(templateId);
    }

    /**
     *
     * 选择自定义字段
     *
     * @param model
     * @excludeCustFieldIds 排除字段ID
     * @return
     */
    @GetMapping("chooseCustFields")
    public String chooseCustFields(String excludeCustFieldIds,String scope, Model model){
        model.addAttribute("excludeCustFieldIds", excludeCustFieldIds);
        model.addAttribute("scope", scope);
        return "foreign/foreignTranReqConfig/chooseCustFields";
    }


    /**
     *
     * 选择自定义字段
     *
     * @param model
     * @return
     */
    @GetMapping("batchChooseCustFields/{templateId}")
    public String batchChooseCustFields(@PathVariable("templateId") String templateId, Model model){
        return "foreign/foreignTranReqConfig/batchChooseCustFields";
    }


    /**
     *
     * 批量保存自定义字段
     *
     *
     * @param templateIds
     * @param custFieldIds
     * @return
     */
    @PostMapping("/batchSaveTemplateConfig")
    @ResponseBody
    public AjaxJson batchSaveTemplateConfig(String templateIds, String custFieldIds){
        fexTranConfigTemplateService.validateUnFinishedBill(templateIds);
        fexTranReqConfigService.batchSaveTemplateConfig(templateIds, custFieldIds, getCurrentUser());
        return success();
    }


    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<FexTemplateConfigVo> list) {
        fexTranReqConfigService.saveImport(list, this.getCurrentUser());
        return success();
    }

    /**
     *
     * 根据模板ID和字段code查询基础信息字段的默认值
     * @param templateId
     * @param fieldCode
     * @return
     */
    @RequestMapping("/getTemplateConfigBaseInfoByFieldCode/{templateId}")
    @ResponseBody
    public AjaxJson getTemplateConfigBaseInfoByFieldCode(@PathVariable("templateId") String templateId, String fieldCode) {
        String versionNo = fexTranReqConfigService.getMaxVersionNo(templateId);
        List<FexTranReqConfigVo> templateConfigs = fexTranReqConfigService.getTemplateConfigByAereTree(templateId, versionNo).stream().filter(e ->baseInfoArea.equals(e.getFieldCode())).collect(Collectors.toList());
        if (!templateConfigs.isEmpty()&& !EmptyUtils.isEmpty(templateConfigs.get(0).getChildren())) {
            templateConfigs =   templateConfigs.get(0).getChildren().stream().filter(e -> fieldCode.equals(e.getFieldCode())).collect(Collectors.toList());
            if (!templateConfigs.isEmpty()) {
                return success(templateConfigs.get(0));
            }
        }
        return success();
    }
}
