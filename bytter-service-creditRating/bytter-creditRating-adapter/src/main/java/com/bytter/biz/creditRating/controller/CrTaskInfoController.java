package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrTaskInfoService;
import com.bytter.biz.creditRating.service.CrmScoringGradeService;
import com.bytter.biz.creditRating.vo.CrTaskInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.constant.Globals;
import com.bytter.core.constant.NisMessageConstant;
import com.bytter.core.utils.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 评价任务主表controller
 *
 * @author huangyl
 */
@Controller
@RequestMapping("/credit/crTaskInfo")
public class CrTaskInfoController extends BaseController {

    @Autowired
    private CrTaskInfoService taskInfoService;

    @Autowired
    private CrmScoringGradeService scoringGradeService;

    /**
     * 查询页面跳转
     *
     * @param model model
     * @return 页面
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_CRTASKINFO_QUERY)
    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/listTaskInfo";
    }

    /**
     * listTaskInfo
     * 查询评价任务数据
     *
     * @param dataTablesInput
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_CRTASKINFO_QUERY)
    @LogMessage(content = "获取评价任务数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/datatable")
    @ResponseBody
    public DataTablesOutput<CrTaskInfoVO> dataTable(@RequestBody DataTablesInput dataTablesInput) {
        return taskInfoService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(), CommonConstant.NO);
    }


    /**
     * 评价任务新增跳转
     *
     * @param model 视图
     * @param id    主键
     * @return 页面
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_CRTASKINFO_EDIT)
    @GetMapping("/add")
    @LogMessage(content = "view原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String add(Model model, String id) {
        String institutionTypeCodes = scoringGradeService.getInstitutionTypeCodes();
        model.addAttribute("defaultCorpId", getCurrentUser().getCorpId());
        model.addAttribute("orgType", institutionTypeCodes);
        return "creditRating/addCrTaskInfo";
    }

    /**
     * 功能介绍 :查询任务数据编辑<br>
     */
    @GetMapping("/getEditData")
    @ResponseBody
    public CrTaskInfoVO getEditData(String id) {
        return taskInfoService.getTaskInfoVo(id, CommonConstant.NO);
    }

    /**
     * 评价任务新增跳转
     *
     * @param taskInfoVo 新增数据
     * @return 结果
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_CRTASKINFO_EDIT)
    @LogMessage(content = "评价任务新增", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping(value = "/submit")
    @ResponseBody
    public AjaxJson submit(@RequestBody CrTaskInfoVO taskInfoVo) {
        taskInfoService.submit(taskInfoVo, getCurrentUser(), CommonConstant.YES);
        return success();
    }

    /**
     * 评价任务编辑跳转
     *
     * @param model 视图
     * @param id    主键
     * @return 页面
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_CRTASKINFO_EDIT)
    @GetMapping("/edit")
    @LogMessage(content = "编辑", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String edit(Model model, String id) {
        CrTaskInfoVO taskInfoVo = taskInfoService.getTaskInfoVo(id, CommonConstant.NO);
        String institutionTypeCodes = scoringGradeService.getInstitutionTypeCodes();
        model.addAttribute("orgType", institutionTypeCodes);
        model.addAttribute("taskInfoVo", taskInfoVo);
        model.addAttribute("defaultCorpId", getCurrentUser().getCorpId());
        return "creditRating/editCrTaskInfo";
    }

    /**
     * 更新数据
     *
     * @param taskInfoVo 数据对象
     * @return 结果
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_CRTASKINFO_EDIT)
    @LogMessage(content = "更新原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @PostMapping(value = "/modify")
    @ResponseBody
    public AjaxJson modify(@RequestBody CrTaskInfoVO taskInfoVo) {
        taskInfoService.modify(taskInfoVo, getCurrentUser());
        return success();
    }

    /**
     * 任务查看
     *
     * @param model 视图
     * @param id    任务id
     * @return 页面
     */
    @GetMapping("/view")
    @LogMessage(content = "view原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String view(Model model, String id) {
        CrTaskInfoVO taskInfoVo = taskInfoService.getTaskInfoVo(id, CommonConstant.YES);
        model.addAttribute("taskInfoVo", taskInfoVo);
        return "creditRating/viewCrTaskInfo";
    }

    /**
     * 功能介绍 :查询任务数据编辑<br>
     */
    @GetMapping("/getViewData")
    @ResponseBody
    public CrTaskInfoVO getViewData(String id) {
        return taskInfoService.getTaskInfoVo(id, CommonConstant.YES);
    }

    /**
     * 删除任务
     *
     * @param id 任务id
     * @return 结果
     */
    //@RequiresPermissions(CreditRatingConstant.CREDIT_CRTASKINFO_EDIT)
    @LogMessage(content = "删除原数据", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id") String id) {
        taskInfoService.deleteById(id, this.getCurrentUser());
        return success();
    }

    /**
     * 任务审核列表
     *
     * @return String
     */
    @GetMapping("/listApprove")
    public String listApprove(String flowStatus, Model model) {
        model.addAttribute("flowStatus", flowStatus);
        return "creditRating/listTaskInfoApprove";
    }

    /**
     * 任务审批列表查询
     *
     * @param dataTablesInput 查询信息
     * @return 页面数据
     */
    @LogMessage(content = "获取任务待审批数据列表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @PostMapping("/getApproveData")
    @ResponseBody
    public DataTablesOutput<CrTaskInfoVO> getApproveData(@RequestBody DataTablesInput dataTablesInput) {
        return taskInfoService.getDataTablesUnion(dataTablesInput, this.getCurrentUser(), CommonConstant.YES);
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
            CrTaskInfoVO taskInfoVo = taskInfoService.getTaskInfoVo(id, CommonConstant.YES);
            model.addAttribute("taskInfoVo", taskInfoVo);
            model.addAttribute("taskId", taskId);
            model.addAttribute("busType", NisMessageConstant.BUS_TYPE_BDLINKPAY);
        }
        return "creditRating/approveCrTaskInfo";
    }

    /**
     * 评价任务编辑跳转
     *
     * @param model 视图
     * @param id    主键
     * @return 页面
     */
    @GetMapping("/taskInfoClone")
    @LogMessage(content = "评价任务复制页面跳转", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    public String taskInfoClone(Model model, String id) {
        CrTaskInfoVO taskInfoVo = taskInfoService.getTaskInfoVo(id, CommonConstant.NO);
        taskInfoVo.setTaskNo(null);
        String institutionTypeCodes = scoringGradeService.getInstitutionTypeCodes();
        model.addAttribute("orgType", institutionTypeCodes);
        model.addAttribute("taskInfoVo", taskInfoVo);
        model.addAttribute("defaultCorpId", getCurrentUser().getCorpId());
        return "creditRating/cloneCrTaskInfo";
    }
}
