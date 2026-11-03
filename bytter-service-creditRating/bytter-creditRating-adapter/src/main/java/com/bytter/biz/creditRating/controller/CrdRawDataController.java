package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.entity.CrdRawData;
import com.bytter.biz.creditRating.service.CrdRawDataService;
import com.bytter.biz.creditRating.vo.CrdRawDataVo;
import com.bytter.biz.creditRating.vo.InstitutionDetailsVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.exception.BusinessException;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.enums.CommonMessageEnum;
import com.bytter.core.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * Data collection and cleaning
 */
@Controller
@RequestMapping("/credit/rawData")
public class CrdRawDataController extends BaseController {

    @Autowired
    private CrdRawDataService rawDataService;

    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/dataCollectionAndClean";
    }

    @GetMapping(value = "/queryAll")
    @ResponseBody
    public DataTablesOutput queryAll(String ids) {
        return rawDataService.queryAll(ids);
    }

    /**
     * 采集数据
     *
     * @return
     */
    @GetMapping(value = "/dataCollection")
    @ResponseBody
    public DataTablesOutput dataCollection() {
        return rawDataService.dataCollection(getCurrentUser());
    }

    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/update")
    public String update(Model model, String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_REQ_PARAM_ERROR);
        }
        CrdRawData crdRawData = rawDataService.get(id, true);
        model.addAttribute("vo", crdRawData);
        return "creditRating/editRawData";
    }

    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/modify")
    @ResponseBody
    public AjaxJson modify(CrdRawDataVo dataVo) {
        rawDataService.modify(dataVo, getCurrentUser());
        return success();
    }

    /**
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/view")
    @LogMessage(content = "view原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String view(Model model, String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_REQ_PARAM_ERROR);
        }
        CrdRawData crdRawData = rawDataService.get(id, true);
        model.addAttribute("vo", crdRawData);
        return "creditRating/viewRawData";
    }

    @LogMessage(content = "删除原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String rawDataId) {
        AjaxJson j = success();
        boolean flag = rawDataService.deleteById(rawDataId);
        j.setSuccess(flag);
        return j;
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_QUERY)
    @GetMapping("/institutionList")
    public String institutionList(Model model) {
        return "creditRating/institutionList";
    }

    /**
     * @param dataTablesInput
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_QUERY)
    @LogMessage(content = "获取机构数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/institutionDatatable")
    @ResponseBody
    public DataTablesOutput institutionDatatable(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = rawDataService.institutionDatatable(dataTablesInput, this.getCurrentUser());
        return dataTablesOutput;
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_VIEW)
    @LogMessage(content = "查看机构数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/viewInstitution")
    public String viewInstitution(String id, Model model) {
        if (StringUtils.isBlank(id)) {
            return "creditRating/viewInstitution";
        }
        model.addAttribute("vo", rawDataService.viewInstitution(id));
        return "creditRating/viewInstitution";
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_ACCESS)
    @GetMapping("/addInstitution")
    public String addInstitution(Model model) {
        return "creditRating/addInstitution";
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_ACCESS)
    @LogMessage(content = "机构信息登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/register/{opType}")
    @ResponseBody
    public AjaxJson registerInstitution(InstitutionDetailsVo institutionDetailsVo, @PathVariable("opType") String opType) {
        rawDataService.registerInstitution(institutionDetailsVo, opType, this.getCurrentUser());
        return success();
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_UPDATE)
    @LogMessage(content = "编辑机构信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping(value = "/modifyInstitution/{id}")
    public String updateInstitution(Model model, @PathVariable("id") String id) {
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_REQ_PARAM_ERROR);
        }
        InstitutionDetailsVo detailsVo = rawDataService.queryDetailsById(id);
        model.addAttribute("ssIds", detailsVo.getSsIds());
        model.addAttribute("bookIds", detailsVo.getBookIds());
        model.addAttribute("visitIds", detailsVo.getVisitIds());
        model.addAttribute("vo", detailsVo);
        return "creditRating/editInstitution";
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_UPDATE)
    @LogMessage(content = "机构信息修改", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/modifyInstitution/{opType}")
    @ResponseBody
    public AjaxJson modifyInstitution(InstitutionDetailsVo institutionDetailsVo, @PathVariable("opType") String opType) {
        rawDataService.modifyInstitution(institutionDetailsVo, opType, this.getCurrentUser());
        return success();
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_DELETE)
    @LogMessage(content = "删除机构数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delInstitution/{id}")
    @ResponseBody
    public AjaxJson delInstitution(@PathVariable("id") String id) {
        AjaxJson j = success();
        boolean flag = rawDataService.delInstitutionByRawDataId(id, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    /**
     * 机构审核列表
     *
     * @return String
     */
    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_APPROVE)
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, String cycleTime, Model model) {
        if(StringUtils.isBlank(flowStatus)){
            flowStatus = "todo";
        }
        model.addAttribute("flowStatus", flowStatus);
        model.addAttribute("cycleTime", cycleTime);
        return "creditRating/listInstitutionApprove";
    }

    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_APPROVE)
    @LogMessage(content = "获取机构待审批数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/institutionDatatableToApprove")
    @ResponseBody
    public DataTablesOutput institutionDatatableToApprove(@RequestBody DataTablesInput dataTablesInput) {
        DataTablesOutput dataTablesOutput = rawDataService.institutionDatatableToApprove(dataTablesInput, this.getCurrentUser());
        return dataTablesOutput;
    }

    /**
     *
     * @param id
     * @param taskId
     * @param model
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_VIEW)
    @LogMessage(content = "查看机构数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/approve")
    public String approve(String id, String taskId, Model model) {
        if (StringUtils.isBlank(id)) {
            return "creditRating/approveInstitution";
        }
        model.addAttribute("vo", rawDataService.viewInstitution(id));
        model.addAttribute("taskId", taskId);
        return "creditRating/approveInstitution";
    }


    /**
     * 申请审批 详情页面
     */
    @GetMapping("/infoInstitutionApprove")
    public String infoApprove(String id, String taskId, Model model) {
        model.addAttribute("taskId", taskId);//必须要做此步骤
        return "creditRating/viewInstitutionApprove";
    }

    @LogMessage(content = "查询编辑机构信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping(value = "/findInstitutionByIds/{ids}")
    public List<InstitutionDetailsVo> findInstitutionByIds(@PathVariable("ids") String ids) {
        List<InstitutionDetailsVo> detailsVos = rawDataService.findInstitutionByIds(Arrays.asList(ids.split(",")));
        return detailsVos;
    }

}
