package com.bytter.biz.foreign.controller;

import com.bytter.biz.foreign.entity.FexExposureMain;
import com.bytter.biz.foreign.service.FexExposureDownloadAndImportService;
import com.bytter.biz.foreign.service.FexExposureMainService;
import com.bytter.biz.foreign.service.FexExposureService;
import com.bytter.biz.foreign.vo.FexExposureMainVO;
import com.bytter.biz.foreign.vo.FexExposureSearchVO;
import com.bytter.biz.payment.api.constant.FexMessageConstant;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.ResourceConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Date;

/**
 * @Title: Controller
 * @Description: 单位敞口上报表
 * @author huangx
 * @date 2022-06-24 15:20:33
 * @version 1.0
 *
 */
@Controller
@RequestMapping("/foreign/fexExposure")
public class FexExposureController extends BaseController {
    @Autowired
    private FexExposureService fexExposureService;

    @Autowired
    private FexExposureMainService fexExposureMainService;

    @Autowired(required=false)
    private FexExposureDownloadAndImportService fexExposureDownloadAndImportService;

    /**
     * 敞口上报主表 页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREINFO_APPLY)
    @LogMessage(content = "敞口上报", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/input")
    public String input(Model model) {
        return "foreign/inputFexExposure";
    }

    /**
     * 单位敞口模板表详情页
     *
     * @return
     */
    @LogMessage(content = "查询单位敞口模板表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewInput/{corpId}/{templateId}/{curId}")
    public String viewInput(@PathVariable("corpId") String corpId, @PathVariable("templateId") String templateId, @PathVariable("curId") String curId, Model model) {
        fexExposureService.initExposure(corpId, templateId, curId, model);
        return "foreign/inputFexExposureDetail";
    }

    /**
     * 外汇敞口上报保存
     * @param exposureMainVO
     * @param request
     * @return
     */
    @LogMessage(content = "添加外汇敞口上报主表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/inputSave")
    @ResponseBody
    public AjaxJson inputSave(FexExposureMainVO exposureMainVO, HttpServletRequest request) {
        fexExposureService.saveExposure(exposureMainVO, request, this.getCurrentUser());
        return success();
    }

    /**
     * 敞口查询与调整 页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREINFO_QUERY)
    @LogMessage(content = "敞口查询与调整", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "foreign/listFexExposure";
    }

    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREINFO_QUERY)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return fexExposureMainService.getDataTablesUnion(dataTablesInput, this.getCurrentUser());
    }

    /**
     * 查询敞口上报详情
     *
     * @return
     */
    @LogMessage(content = "查询敞口上报详情", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view/{id}")
    public String view(HttpServletRequest request, @PathVariable("id") String id, Model model) {
        fexExposureService.viewExposure(id, model);
        String fileName ="单位敞口查看";
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "foreign" + File.separator + fileName + ".xls";
        this.fexExposureService.exporFexExposure(request, model, filePath, fileName);
        return "foreign/viewFexExposure";
    }

    /**
     * 删除敞口上报信息
     * @param id
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREINFO_APPLY)
    @LogMessage(content = "删除敞口上报详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id){
        fexExposureService.deleteExposure(id, this.getCurrentUser());
        return success();
    }

    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREINFO_APPLY)
    @GetMapping("/update")
    public String update(FexExposureMainVO exposureMainVO, Model model) {
        if (StringUtils.isNotEmpty(exposureMainVO.getId())) {
            //获取上报主信息
            FexExposureMainVO main = fexExposureMainService.getByIdUnion(exposureMainVO.getId());
            model.addAttribute("fexExposureMainPage", main);
        }
        return "foreign/updateFexExposure";
    }

    /**
     * 单位敞口模板表详情页
     *
     * @return
     */
    @LogMessage(content = "查询单位敞口模板表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @GetMapping("/viewUpdate/{id}/{curId}")
    public String viewUpdate(@PathVariable("id") String id, @PathVariable("curId") String curId, Model model) {
        fexExposureService.viewUpadateExposure(id, curId, model);
        return "foreign/updateFexExposureDetail";
    }

    /**
     * 修改敞口上报详情
     * @param exposureMainVO
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREINFO_APPLY)
    @LogMessage(content = "修改敞口上报详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_UPDATE)
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(FexExposureMainVO exposureMainVO, HttpServletRequest request){
        fexExposureService.updateExposure(exposureMainVO, request, this.getCurrentUser());
        return  success();
    }

    /**
     * 敞口报表统计 页面跳转
     *
     * @return
     */
    //RequiresPermissions(ResourceConstant.FOREIGN_FEXEXPOSUREREPORT_QUERY)
    @LogMessage(content = "敞口查询与调整", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/query")
    public String query(Model model) {
        return "foreign/queryFexExposure";
    }

    /**
     * 跳转至审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime,Model model) {
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "foreign/listFexExposureApprove";
    }

    /**
     * 加载审核列表数据
     *
     * @param dataTablesInput
     * @return
     */
    @SuppressWarnings("rawtypes")
    @PostMapping("/dataTableApprove")
    @ResponseBody
    public DataTablesOutput dataTableApprove(@RequestBody DataTablesInput dataTablesInput) {
        return this.fexExposureMainService.dataTableApprove(dataTablesInput);
    }

    /**
     * 跳转至审核详情界面
     *
     * @param id
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_EXPOSURE);
            fexExposureService.viewExposure(id, model);
        }
        return "foreign/approveFexExposure";
    }

    /**
     * 跳转至手机审核详情界面
     * @param businessId
     * @param taskId
     * @param model
     * @return String
     */
    @GetMapping("/mobileApprove")
    public String mobileApprove(String businessId, String taskId, Model model) {
        if (StringUtils.isNotEmpty(businessId)) {
            fexExposureService.viewExposure(businessId, model);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", FexMessageConstant.BUS_TYPE_EXPOSURE);
        }
        return "foreign/viewApproveFexExposure";
    }

    /**
     * 获取敞口上报信息
     * @param corpId
     * @param templateId
     * @param enrolDate
     * @return
     */
    @RequestMapping(params = "getCorpExposure")
    @ResponseBody
    public AjaxJson getCorpExposure(String corpId, String templateId, String enrolDate) {
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        if (StringUtils.isNotBlank(enrolDate)) {
            enrolDate = enrolDate + "-01";
        }
        Date beginDate = StringUtils.isBlank(enrolDate) ? null:DateUtils.parseDate(enrolDate);
        Date endDate = StringUtils.isBlank(enrolDate) ? null:DateUtils.getLastMonthDate(beginDate);
        FexExposureMain main = fexExposureMainService.getCorpExposure(corpId, templateId, beginDate, endDate);
        ajaxJson.setObj(main);
        return ajaxJson;
    }

    /**
     * 单位敞口模板表详情页
     *
     * @return
     */
    @LogMessage(content = "查询单位敞口模板表详情",level = Globals.Log_Leavel_INFO,type = Globals.Log_Type_OTHER)
    @PostMapping("/viewQuery")
    public String viewQuery(HttpServletRequest request, Model model, FexExposureSearchVO searchVO) {
        fexExposureService.viewExposureQuery(searchVO, model);
        String fileName ="敞口统计报表";
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "foreign" + File.separator + fileName + ".xls";
        this.fexExposureService.exporFexExposure(request, model, filePath, fileName);
        return "foreign/viewFexExposureQuery";
    }

    /**
     * 导出单位敞口信息
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/exportsExposure/{opType}")
    public void exportsExposure(HttpServletRequest request, HttpServletResponse response, @PathVariable("opType") String opType) throws Exception {
        String fileName = "敞口统计报表.xls";
        if (CommonConstant.YES.equals(opType)) {
            fileName = "单位敞口查看.xls";
        }
        String filePath = request.getSession().getServletContext().getRealPath("/") + "export" + File.separator + "foreign" + File.separator + fileName;
        this.fexExposureDownloadAndImportService.exports(request, response, fileName, filePath);
    }
}
