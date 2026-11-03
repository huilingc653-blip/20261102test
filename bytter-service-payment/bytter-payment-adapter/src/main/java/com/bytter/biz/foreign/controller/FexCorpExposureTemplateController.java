package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexCorpExposureTemplate;
import com.bytter.biz.foreign.service.FexCorpExposureService;
import com.bytter.biz.foreign.service.FexCorpExposureTemplateService;
import com.bytter.biz.foreign.service.FexExposureTemplateService;
import com.bytter.biz.foreign.vo.FexCorpExposureTemplateVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateItemVO;
import com.bytter.biz.foreign.vo.FexExposureTemplateVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.IBtCurrencyService;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.apache.dubbo.config.annotation.DubboReference;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @Title: Controller
 * @Description: 单位单位敞口模板表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexCorpExposureTemplate")
public class FexCorpExposureTemplateController extends BaseController {
    @Autowired
    private FexCorpExposureTemplateService fexCorpExposureTemplateService;

    @Autowired
    private FexExposureTemplateService fexExposureTemplateService;

    @Autowired
    private FexCorpExposureService fexCorpExposureService;

    @DubboReference(check = false)
    private IBtCurrencyService currencyService;

    /**
     * 单位敞口模板表列表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXCORPEXPOSURETEMPLATE_QUERY)
    @LogMessage(content = "查询单位敞口模板表列表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexCorpExposureTemplate";
    }

    /**
     * 单位敞口模板表 table列表
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXCORPEXPOSURETEMPLATE_QUERY)
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput){
        return fexCorpExposureTemplateService.getDataTablesUnion(dataTablesInput);
    }

    /**
     * 单位敞口模板表新增页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXCORPEXPOSURETEMPLATE_APPLY)
    @GetMapping("/add")
    public String add(FexCorpExposureTemplate fexCorpExposureTemplate, Model model) {
        if (StringUtils.isNotEmpty(fexCorpExposureTemplate.getId())) {
            fexCorpExposureTemplate = fexCorpExposureTemplateService.get(fexCorpExposureTemplate.getId());
            model.addAttribute("fexCorpExposureTemplatePage", fexCorpExposureTemplate);
        }
        return "foreign/addFexCorpExposureTemplate";
    }

    /**
     * 单位敞口模板表编辑页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXCORPEXPOSURETEMPLATE_APPLY)
    @GetMapping("/update")
    public String update(FexCorpExposureTemplate fexCorpExposureTemplate, Model model) {
        if (StringUtils.isNotEmpty(fexCorpExposureTemplate.getId())) {
            fexCorpExposureTemplate = fexCorpExposureTemplateService.get(fexCorpExposureTemplate.getId());
            model.addAttribute("fexCorpExposureTemplatePage", fexCorpExposureTemplate);
        }
        return "foreign/updateFexCorpExposureTemplate";
    }

    /**
     * 单位敞口模板表详情页
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXCORPEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "查询单位敞口模板表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        FexCorpExposureTemplateVO fexCorpExposureTemplate = fexCorpExposureTemplateService.getByIdUnion(id);
        model.addAttribute("fexCorpExposureTemplatePage", fexCorpExposureTemplate);
        return "foreign/viewFexCorpExposureTemplate";
    }

    /**
     * 添加单位敞口模板表
     * @param fexCorpExposureTemplate
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXCORPEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "添加单位敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(FexCorpExposureTemplate fexCorpExposureTemplate){
        fexCorpExposureTemplate.setCreateBy(this.getCurrentUser().getLoginName());
        fexCorpExposureTemplate.setCreateDate(DateUtils.getDate());
        this.fexCorpExposureTemplateService.saveFexCorpExposureTemplate(fexCorpExposureTemplate);
        return success();
    }

    /**
     * 修改单位敞口模板表
     * @param fexCorpExposureTemplate
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "修改单位敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexCorpExposureTemplate fexCorpExposureTemplate){
        FexCorpExposureTemplate t = fexCorpExposureTemplateService.get(fexCorpExposureTemplate.getId(), true);
        BeanUtils.copyNotNullProperties(fexCorpExposureTemplate, t);
        t.setUpdateBy(this.getCurrentUser().getLoginName());
        t.setUpdateDate(DateUtils.getDate());
        this.fexCorpExposureTemplateService.updateFexCorpExposureTemplate(t);
        return  success();
    }

    /**
     * 删除单位敞口模板表
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "删除单位敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        FexCorpExposureTemplate fexCorpExposureTemplate = fexCorpExposureTemplateService.get(id);
        fexCorpExposureTemplateService.deleteFexCorpExposureTemplate(fexCorpExposureTemplate);
        return success();
    }

    /**
     * 批量删除单位敞口模板表
     * @param ids
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSURETEMPLATE_APPLY)
    @LogMessage(content = "批量删除单位敞口模板表",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson batchDelete(@RequestParam("ids") String ids){
        this.fexCorpExposureTemplateService.batchDelete(ids);
        return success();
    }

    /**
     * 获取所有有效敞口模板信息
     *
     * @return
     */
    @GetMapping("/json")
    @ResponseBody
    public List<FexCorpExposureTemplateVO> listByCorpId(String corpId) {
        if (fexCorpExposureService.isCollectCorp(corpId)) {
            return fexCorpExposureTemplateService.listByCorpIdForCollectCorp(corpId);
        } else {
            return fexCorpExposureTemplateService.listByCorpId(corpId);
        }
    }

    /**
     * 单位敞口模板表详情页
     *
     * @return
     */
    @LogMessage(content = "查询单位敞口模板表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewInput/{corpId}/{templateId}")
    public String viewInput(@PathVariable("corpId") String corpId, @PathVariable("templateId") String templateId, Model model) {
        FexExposureTemplateVO fexExposureTemplate = fexExposureTemplateService.getByIdUnion(templateId);
        model.addAttribute("fexExposureTemplatePage", fexExposureTemplate);
        FexCorpExposureTemplateVO fexCorpExposureTemplateVO = fexCorpExposureTemplateService.getByCorpIdAndTemplateId(corpId, templateId);
        model.addAttribute("fexCorpExposureTemplatePage", fexCorpExposureTemplateVO);
        List<BtCurrencyDTO> listCur = currencyService.listAllBtCurrency();
        String filterCur = "";
        for(BtCurrencyDTO cur:listCur){
            if (fexCorpExposureTemplateVO.getCurId().indexOf(cur.getId()) == -1) {
                filterCur += cur.getId() + CommonConstant.COMMA_SPLIT;
            }
        }
        model.addAttribute("filterCur", filterCur);
        return "foreign/viewFexExposureTemplateInput";
    }

    /**
     * 获取所有有效敞口模板信息
     *
     * @return
     */
    @GetMapping("/listCurByCorpExposureId")
    @ResponseBody
    public List<BtCurrencyDTO> listCurByCorpExposureId(String corpId, String templateId) {
        FexCorpExposureTemplateVO fexCorpExposureTemplate = this.fexCorpExposureTemplateService.getByCorpIdAndTemplateId(corpId, templateId);
        List<BtCurrencyDTO> listCur = new ArrayList<>();
        if (fexCorpExposureTemplate != null) {
            for (String curId : fexCorpExposureTemplate.getCurId().split(CommonConstant.COMMA_SPLIT)) {
                listCur.add(currencyService.getObjectById(curId));
            }
        } else {
            // 汇总单位，获取所有币种
            listCur = currencyService.getBtCurrency();
        }
        return listCur;
    }

    /**
     * 获取所有有效敞口模板信息
     *
     * @return
     */
    @GetMapping("/listTemplateItemById")
    @ResponseBody
    public List<FexExposureTemplateItemVO> listTemplateItemById(String id) {
        FexExposureTemplateVO fexExposureTemplateVO = this.fexExposureTemplateService.getByIdUnion(id);
        List<FexExposureTemplateItemVO> listItem = new ArrayList<>();
        if (fexExposureTemplateVO != null) {
            listItem = fexExposureTemplateVO.getListItem();
        }
        return listItem;
    }

    /**
     * 获取单位模板设置的币种信息
     *
     * @return
     */
    @GetMapping("/getCorpExposureCur")
    @ResponseBody
    public List<BtCurrencyDTO> listByCorpId(String corpId, String templateId) {
        FexCorpExposureTemplateVO corpExposureTemplateVO = fexCorpExposureTemplateService.getByCorpIdAndTemplateId(corpId, templateId);
        List<BtCurrencyDTO> curList = new ArrayList<>();
        if (corpExposureTemplateVO != null && StringUtils.isNotBlank(corpExposureTemplateVO.getCurId())) {
            for (String curId:corpExposureTemplateVO.getCurId().split(CommonConstant.COMMA_SPLIT)) {
                curList.add(currencyService.getObjectById(curId));
            }
        } else {
            curList = currencyService.getBtCurrency();
        }
        return curList;
    }
}
