package com.bytter.system.web.controller;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.poi.excel.entity.TemplateExportParams;
import com.bytter.poi.excel.entity.vo.TemplateExcelConstants;
import com.bytter.system.api.constant.SysOrgantypeConstant;
import com.bytter.poi.enums.ResourceTemplateEnum;
import com.bytter.system.entity.Corp;
import com.bytter.system.service.CorpService;
import com.bytter.system.service.OrgantypeCorpService;
import com.bytter.system.service.OrgantypeService;
import com.bytter.system.vo.CorpVO;
import com.bytter.system.vo.OrgantypeCorpVO;
import com.bytter.system.vo.OrgantypeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhengg
 * @version 1.0
 * @Title: Controller
 * @Description: 组织架构表跳转操作
 * @date 2020-05-14 16:01:40
 */
@Controller
@RequestMapping("/sys/organtype/organtype")
public class OrgantypeController extends BaseController {
    @Autowired
    private OrgantypeService organtypeService;
    @Autowired
    private OrgantypeCorpService organtypeCorpService;
    @Autowired
    private CorpService corpService;
    /**
     * 组织架构表列表 页面跳转
     *
     * @return
     */
    @LogMessage(content = "查询组织架构表列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "sys/organtype/listOrgantype";
    }
    /**
     * 组织架构表 table列表
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return organtypeService.getDataTablesUnion(dataTablesInput, SysOrgantypeConstant.SYS_RESOURCE_ORGANTYPEQUERY_OP);
    }
    /**
     * 组织架构表新增页面跳转
     *
     * @return
     */
    @GetMapping("/add")
    public String add(OrgantypeVO organtypeVO, Model model) {
        return "sys/organtype/addOrgantype";
    }
    /**
     * 添加组织架构表
     *
     * @param organtypeVO
     * @return
     */
    @LogMessage(content = "添加组织架构表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(OrgantypeVO organtypeVO) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(organtypeService.saveOrgantype(organtypeVO, this.getCurrentUser()));
        return success();
    }
    /**
     * 判断是否有相同的组织架构名称
     *
     * @param orgtName 组织架构名称
     * @return
     */
    @PostMapping("/checkOrgtName")
    @ResponseBody
    public AjaxJson checkOrgtName(String orgtName) {
        AjaxJson j = success();
        j.setSuccess(organtypeService.checkOrgtName(orgtName));
        return j;
    }
    /**
     * 组织架构表编辑页面跳转
     *
     * @return
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            OrgantypeVO organtype = organtypeService.getOrgantypeVO(id);
            model.addAttribute("organtypeVO", organtype);
        }
        return "sys/organtype/updateOrgantype";
    }
    /**
     * 修改组织架构表
     *
     * @param organtypeVO
     * @return
     */
    @LogMessage(content = "修改组织架构表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/update")
    @ResponseBody
    public AjaxJson update(OrgantypeVO organtypeVO) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(organtypeService.updateOrgantype(organtypeVO, this.getCurrentUser()));
        return success();
    }
    /**
     * 组织架构表详情页
     *
     * @return
     */
    @LogMessage(content = "查询组织架构表详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            OrgantypeVO organtype = organtypeService.getOrgantypeVO(id);
            model.addAttribute("organtypeVO", organtype);
        }
        return "sys/organtype/viewOrgantype";
    }
    /**
     * 删除组织架构表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除组织架构表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String id) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(organtypeService.deletes(id, this.getCurrentUser()));
        return success();
    }
    /**
     * 批量删除组织架构表
     *
     * @param ids
     * @return
     */
    @LogMessage(content = "批量删除组织架构表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/batchDelete")
    @ResponseBody
    public AjaxJson deleteDelete(@ModelAttribute("ids") String ids) {
        organtypeService.deletes(ids, this.getCurrentUser());
        return success();
    }
    /**
     * 删除组织架构表
     *
     * @param id
     * @return
     */
    @LogMessage(content = "初始化组织架构单位", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/init")
    @ResponseBody
    public AjaxJson init(String id) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(organtypeService.init(id, this.getCurrentUser()));
        return success();
    }
    /**
     * 组织架构表发布页面跳转
     *
     * @return
     */
    @GetMapping("/release/{id}")
    public String releaseEdit(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            model.addAttribute("id", id);
            model.addAttribute("todate", DateUtils.formatDate(DateUtils.getDate(), "yyyy-MM-dd"));
        }
        return "sys/organtype/releaseOrgantype";
    }
    /**
     * 发布组织架构
     *
     * @param organtypeVO
     * @return
     */
    @LogMessage(content = "发布组织架构", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/release")
    @ResponseBody
    public AjaxJson release(OrgantypeVO organtypeVO) {
        AjaxJson ajaxJson = success();
        ajaxJson.setSuccess(organtypeService.release(organtypeVO, this.getCurrentUser()));
        return ajaxJson;
    }
    /**
     * 获取组织架构单位树型数据
     */
    @PostMapping("/getOrgCorpList")
    @ResponseBody
    public Object getOrgCorpTreeList(Model model, String corpId, String corpName, String orgtId) throws ParseException {
        OrgantypeCorpVO search = new OrgantypeCorpVO();
        search.setCorpId(corpId);
        search.setCorpName(corpName);
        search.setOrgtId(orgtId);
        List<OrgantypeCorpVO> list = organtypeCorpService.listOrgantypeCorpVOTree(search);
        return list;
    }
    /**
     * 新增一级网点单位页面跳转
     *
     * @return
     */
    @GetMapping("/addOrgtFirstCorp/{orgtId}")
    public String addOrgtFirstCorp(@PathVariable("orgtId") String orgtId, Model model) {
        if (StringUtils.isNotEmpty(orgtId)) {
            String corpIds = organtypeService.getOrgtCorpByOrgtId(orgtId);
            model.addAttribute("orgtId", orgtId);
            if (StringUtils.isNotEmpty(corpIds)) {
                model.addAttribute("orgtCorpIds", corpIds);
            } else {
                model.addAttribute("orgtCorpIds", "null");
            }
        }
        return "sys/organtype/addOrgtFirstCorp";
    }
    /**
     * 保存一级网点单位页面
     *
     * @param orgtCorpVO
     * @return
     */
    @LogMessage(content = "保存组织架构一级单位", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveOrgtFirstCorp")
    @ResponseBody
    public AjaxJson saveOrgtFirstCorp(OrgantypeCorpVO orgtCorpVO) {
        AjaxJson ajaxJson = success();
        organtypeService.updateOrgantypeIsChangCorpById(orgtCorpVO.getOrgtId(), this.getCurrentUser());
        ajaxJson.setSuccess(organtypeCorpService.saveOrgtFirstCorp(orgtCorpVO, this.getCurrentUser()));
        return success();
    }
    /**
     * 新增子单位页面跳转
     *
     * @return
     */
    @GetMapping("/addOrgtChildCorp/{orgtId}/{parentId}")
    public String addOrgtChildCorp(@PathVariable("orgtId") String orgtId, @PathVariable("parentId") String parentId, Model model) {
        if (StringUtils.isNotEmpty(orgtId)) {
            String corpIds = organtypeService.getOrgtCorpByOrgtId(orgtId);
            model.addAttribute("orgtId", orgtId);
            model.addAttribute("parentId", parentId);
            model.addAttribute("orgtCorpIds", corpIds);
        }
        return "sys/organtype/addOrgtChildCorp";
    }
    /**
     * 保存组织架构子单位
     *
     * @param orgtCorpVO
     * @return
     */
    @LogMessage(content = "保存组织架构子单位", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveOrgtChildCorp")
    @ResponseBody
    public AjaxJson saveOrgtChildCorp(OrgantypeCorpVO orgtCorpVO) {
        AjaxJson ajaxJson = success();
        organtypeService.updateOrgantypeIsChangCorpById(orgtCorpVO.getOrgtId(), this.getCurrentUser());
        ajaxJson.setSuccess(organtypeCorpService.saveOrgtChildCorp(orgtCorpVO, this.getCurrentUser()));
        return success();
    }
    /**
     * 删除组织机构单位
     *
     * @param id
     * @return
     */
    @LogMessage(content = "删除组织机构单位", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/deleteCorp")
    @ResponseBody
    public AjaxJson deleteCorp(String id, String orgtId) {
        AjaxJson ajaxJson = success();
        organtypeService.updateOrgantypeIsChangCorpById(orgtId, this.getCurrentUser());
        ajaxJson.setSuccess(organtypeCorpService.deleteOrgtCorpById(id, this.getCurrentUser()));
        return success();
    }
    /**
     * 编辑上下级页面跳转
     *
     * @return
     */
    @GetMapping("/updateOrgtSupCorp/{orgtId}/{corpId}")
    public String updateOrgtSupCorp(@PathVariable("orgtId") String orgtId, @PathVariable("corpId") String corpId, Model model) {
        if (StringUtils.isNotEmpty(orgtId)) {
            model.addAttribute("orgtId", orgtId);
            model.addAttribute("corpId", corpId);
        }
        return "sys/organtype/updateOrgtSupCorp";
    }
    /**
     * 保存组织架构单位上下级关系
     *
     * @param orgtCorpVO
     * @return
     */
    @LogMessage(content = "保存组织架构单位上下级关系", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping("/saveOrgtSupCorp")
    @ResponseBody
    public AjaxJson saveOrgtSupCorp(OrgantypeCorpVO orgtCorpVO) {
        AjaxJson ajaxJson = success();
        organtypeService.updateOrgantypeIsChangCorpById(orgtCorpVO.getOrgtId(), this.getCurrentUser());
        ajaxJson.setSuccess(organtypeCorpService.saveOrgtSupCorp(orgtCorpVO, this.getCurrentUser()));
        return success();
    }
    /**
     * 获取树形结构单位信息
     *
     * @return
     */
    @GetMapping("/treeData")
    @ResponseBody
    public Object treeCorpData(String resource, String orgtId) {
        return organtypeCorpService.getCorpTreeMapList(orgtId, resource, this.getCurrentUser());
    }
    /**
     * 查看组织机构单位
     *
     * @param id
     * @param parentId
     * @param model
     */
    @GetMapping("/view")
    public String view(String id, String corpId, String parentId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            Corp corp = corpService.get(corpId);
            CorpVO corpVO = new CorpVO();
            BeanUtils.copyNotNullProperties(corp, corpVO);
            corpVO.setUnitCm(corp.getUnitCm());
            model.addAttribute("corp", corpVO);
            if (StringUtils.isNotEmpty(parentId) && !parentId.equals("null")) {
                Corp parentCorp = corpService.get(parentId);
                if (StringUtils.isEmpty(corpVO.getUnitCm())) {
                    corpVO.setUnitCm(parentCorp.getUnitCm());
                }
                if (parentCorp != null) {
                    model.addAttribute("parentCorp", "(" + parentCorp.getCode() + ")" + parentCorp.getName());
                }
            }
        }
        return "sys/organtype/viewOrgtCorp";
    }
    /**
     * 组织架构单位数据导出
     *
     * @param columns  所有的列头
     * @param fileName 导出文件名
     * @param infoVO   查询条件
     * @param modelMap
     * @param request
     * @param response
     */
    @PostMapping("/excelCorp")
    public void excelCorp(String columns, String fileName, OrgantypeCorpVO infoVO, ModelMap modelMap, HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<>();
        String[] conlumnArray = columns.split(CommonConstant.COMMA_SPLIT);
        map.put("list", organtypeCorpService.listExcelCorpTreeVOTree(infoVO));
        map.put("column0", conlumnArray[0]);
        map.put("column1", conlumnArray[1]);
        map.put("column2", conlumnArray[2]);
        map.put("column3", conlumnArray[3]);
        map.put("column4", conlumnArray[4]);
        TemplateExportParams temmp = new TemplateExportParams(ResourceTemplateEnum.EXPORT_SYS_ORGTCORP_XLSX.getPath());
        modelMap.put(TemplateExcelConstants.FILE_NAME, fileName);
        modelMap.put(TemplateExcelConstants.PARAMS, temmp);
        modelMap.put(TemplateExcelConstants.MAP_DATA, map);
        //todo
        //BaseExcelView.render(modelMap, request, response, TemplateExcelConstants.TEMPLATE_EXCEL_VIEW);
    }
    /**
     * 根据ID查询组织架构表已发版的数据
     *
     * @return
     */
    @GetMapping("/getOrgantypeIsRelease")
    @ResponseBody
    public List<OrgantypeVO> getOrgantypeIsRelease(String id) {
        return organtypeService.getOrgantypeIsRelease(id);
    }
}
