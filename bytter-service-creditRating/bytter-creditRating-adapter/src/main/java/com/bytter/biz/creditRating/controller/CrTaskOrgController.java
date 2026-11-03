package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrTaskOrgService;
import com.bytter.biz.creditRating.service.EvaluationReportService;
import com.bytter.biz.creditRating.vo.CrTaskOrgInfoVo;
import com.bytter.biz.creditRating.vo.CrdRawDataVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 任务机构表controller
 * @author huangyl
 */
@Controller
@RequestMapping("/credit/crTaskOrg")
public class CrTaskOrgController extends BaseController {

    @Autowired
    private CrTaskOrgService taskOrgService;

    @Autowired
    private EvaluationReportService evaluationReportService;

    /**
     * 查询页面跳转
     *
     * @param model model
     * @return 页面
     */
    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/dataCollectionAndClean";
    }

    /**
     * 评价任务数据
     *
     * @return
     */
    @GetMapping(value = "/dataCollection")
    @ResponseBody
    public DataTablesOutput dataCollection() {
        return taskOrgService.dataCollection(getCurrentUser());
    }

    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/update")
    public String update(Model model, String id) {
        return "creditRating/editRawData";
    }

    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/modify")
    @ResponseBody
    public AjaxJson modify(CrdRawDataVo dataVo) {
        taskOrgService.modify(dataVo, getCurrentUser());
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
        return "creditRating/viewRawData";
    }

    @LogMessage(content = "删除原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete")
    @ResponseBody
    public AjaxJson delete(String rawDataId) {
        AjaxJson j = success();
        boolean flag = taskOrgService.deleteById(rawDataId, this.getCurrentUser());
        j.setSuccess(flag);
        return j;
    }

    @LogMessage(content = "查询机构评价信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/queryByOrgName")
    @ResponseBody
    public DataTablesOutput<CrTaskOrgInfoVo> queryByOrgName(String orgName) {
        return taskOrgService.queryByOrgName(orgName, this.getCurrentUser());
    }

    @LogMessage(content = "查询机构评价信息明细", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/reviewTaskDetails/{id}/{orgid}")
    public String reviewTaskDetails(@PathVariable("id") String id,@PathVariable("orgid") String orgid, Model model) {
        CrTaskOrgInfoVo crTaskOrgInfoVo= evaluationReportService.getCrTaskInfoById(id);
        model.addAttribute("crTaskInfo",crTaskOrgInfoVo);
        model.addAttribute("orgId",orgid);
        return "creditRating/reviewTaskDetails";
    }

//    @RequiresPermissions(CreditRatingConstant.FUNDWARN_CREDITRATING_INSTITUTION_UPDATE)
//    @LogMessage(content = "编辑机构信息", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
//    @GetMapping(value = "/modifyInstitution/{id}")
//    public String updateInstitution(Model model, @PathVariable("id") String id) {
//        if (StringUtils.isBlank(id)) {
//            throw new BusinessException(CommonMessageEnum.COMMON_MESSAGE_REQ_PARAM_ERROR);
//        }
//        InstitutionDetailsVo detailsVo = rawDataService.queryDetailsById(id);
//        model.addAttribute("ssIds", detailsVo.getSsIds());
//        model.addAttribute("bookIds", detailsVo.getBookIds());
//        model.addAttribute("visitIds", detailsVo.getVisitIds());
//        model.addAttribute("vo", detailsVo);
//        return "creditRating/editInstitution";
//    }
}
