package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.entity.CrmComputationalModel;
import com.bytter.biz.creditRating.service.CrScoringIndicatorService;
import com.bytter.biz.creditRating.service.CrmComputationalModelService;
import com.bytter.biz.creditRating.service.CrmScoringIndicatorSolidService;
import com.bytter.biz.creditRating.vo.*;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/8/12<br>
 *
 * @param/tickIndicator
 * @author tongqr
 * @version 1.0
 */
@Controller
@RequestMapping("/creditRating/computationalModel")
public class CrmComputationalModelController extends BaseController {
    @Autowired
    private CrmComputationalModelService crmComputationalModelService;
    @Autowired
    private CrScoringIndicatorService crScoringIndicatorService;
    @Autowired
    private CrmScoringIndicatorSolidService crmScoringIndicatorSolidService;
    /**
     * 列表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_QUERY)
    @LogMessage(content = "信用评级计算模型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/listComputationModel";
    }

    /**
     *  table列表
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_QUERY)
    @PostMapping("/getModelDataTables")
    @ResponseBody
    public DataTablesOutput getModelDataTables(@RequestBody DataTablesInput dataTablesInput) {
        return crmComputationalModelService.getModelDataTables(dataTablesInput);
    }

    /**
     *計算模型新增 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_ADD)
    @LogMessage(content = "信用评级计算模型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String add(Model model) {
        return "creditRating/addComputationModel";
    }

    /**
     * 計算模型編輯 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_EDIT)
    @LogMessage(content = "信用评级计算模型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update")
    public String update(Model model, String modelId) {
        CrmComputationalModel computationalModel=crmComputationalModelService.get(modelId);
        String oldScoringIds=crmScoringIndicatorSolidService.getIndicatorListIdsForModel(modelId);
        model.addAttribute("computationalModel",computationalModel);
        model.addAttribute("oldScoringIds",oldScoringIds);
        return "creditRating/addComputationModel";
    }

    /**
     * 計算模型編輯 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_ADD)
    @LogMessage(content = "信用评级计算模型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/copy")
    public String copy(Model model, String modelId) {
        CrmComputationalModel computationalModel=crmComputationalModelService.get(modelId);
        String oldScoringIds=crmScoringIndicatorSolidService.getIndicatorListIdsForModel(modelId);
        model.addAttribute("copyModelId",computationalModel.getId());
        computationalModel.setId(null);
        computationalModel.setModelCode(null);
        computationalModel.setModelName(null);
        model.addAttribute("computationalModel",computationalModel);
        model.addAttribute("oldScoringIds",oldScoringIds);
        return "creditRating/addComputationModel";
    }

    /**
     * 計算模型刪除 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_DELETE)
    @LogMessage(content = "信用评级计算模型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{modelId}")
    @ResponseBody
    public AjaxJson delete(Model model, @PathVariable("modelId")String modelId) {
        crmComputationalModelService.deleteModel(modelId);
        return success();
    }

    /**
     * 計算模型查看 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_QUERY)
    @LogMessage(content = "信用评级计算模型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(Model model, String modelId) {
        CrmComputationalModel computationalModel=crmComputationalModelService.get(modelId);
        String oldScoringIds=crmScoringIndicatorSolidService.getIndicatorListIdsForModel(modelId);
        model.addAttribute("computationalModel",computationalModel);
        model.addAttribute("oldScoringIds",oldScoringIds);
        return "creditRating/viewComputationModel";
    }

    /**
     * 将新获取与固化获取合并数据
     * @param scoringForShow
     * @return
     */
    @PostMapping("/getScoringForShow")
    @ResponseBody
    public List<CrmScoringIndicatorSolidificatiVO> getScoringForShow(@RequestBody ScoringForShow scoringForShow){
        return crmComputationalModelService.getScoringForShow(scoringForShow.getNewScoringIds(),scoringForShow.getOldScoringIds(),scoringForShow.getModelId(),scoringForShow.getScCoefficientsArray());
    }
    /**
     * 提交
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_MODEL_ADD)
    @LogMessage(content = "添加计算模型信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(CrmComputationalModelVO crmComputationalModelVO) {
        crmComputationalModelService.submit(crmComputationalModelVO,getCurrentUser());
        return success();
    }

    @GetMapping("/tickIndicator")
    public String addScoring(Model model, CrScoringIndicatorVo vo, String scoringIds) {
        List<CrScoringIndicatorVo> crScoringIndicatorVos = crScoringIndicatorService.listEffectTree(vo);
        String[] checkedIdS=scoringIds.split(",");
        for(CrScoringIndicatorVo scoringIndicatorVo:crScoringIndicatorVos){
           String isChecked="0";
           for(String checkedId:checkedIdS){
               if(scoringIndicatorVo.getId().equals(checkedId)){
                   isChecked="1";
               }
           }
            scoringIndicatorVo.setIsChecked(isChecked);
        }
        model.addAttribute("scoringIds",scoringIds);
        model.addAttribute("treeDate", crScoringIndicatorVos);
        model.addAttribute("scoringIndicatorCode",vo.getScoringIndicatorCode());
        model.addAttribute("scoringIndicatorName",vo.getScoringIndicatorName());
        return "creditRating/tickIndicator";
    }


    /**
     * 功能介绍 :查询所有CrmModelFormulaVO对象<br>
     */
    @GetMapping("/getCrmModelFormulaVOs")
    @ResponseBody
    public List<CrmModelFormulaVO> getCrmModelFormulaVOs() {
        return crmComputationalModelService.getCrmModelFormulaVOs();
    }

    /**
     * 功能介绍 :查询所有有效CrmComputationalModelVO对象<br>
     */
    @GetMapping("/getModelJson")
    @ResponseBody
    public List<CrmComputationalModelVO> getAllComputationalModel(){
        return crmComputationalModelService.getAllComputationalModel();
    }

    /**
     * 功能介绍 :查询固化指标对象<br>
     */
    @GetMapping("/getModelScoringJson")
    @ResponseBody
    public List<CrmScoringIndicatorSolidificatiVO> getModelScoringJson(String modelId){
        return crmComputationalModelService.getModelScoringJson(modelId);
    }
}
