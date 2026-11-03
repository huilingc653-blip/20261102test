package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.entity.CrmScoringGrade;
import com.bytter.biz.creditRating.service.CrmScoringGradeService;
import com.bytter.biz.creditRating.vo.CrmScoringGradeLevelVO;
import com.bytter.biz.creditRating.vo.CrmScoringGradeVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.BeanUtils;

import com.bytter.system.api.IDictService;
import org.apache.dubbo.config.annotation.DubboReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能介绍 : 评级标准<br>
 * <b>日期：</b>2024/8/28<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Controller
@RequestMapping("/creditRating/scoringGrade")
public class CrmScoringGradeController extends BaseController {

    @Autowired
    private CrmScoringGradeService crmScoringGradeService;

    @DubboReference(check = false)
    private IDictService dictService;
    /**
     * 列表 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_GRADE_QUERY)
    @LogMessage(content = "信用评级标准", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/list")
    public String list(Model model) {
        return "creditRating/listScoringGrade";
    }

    /**
     *  table列表
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_GRADE_QUERY)
    @PostMapping("/getGradeDataTables")
    @ResponseBody
    public DataTablesOutput getGradeDataTables(@RequestBody DataTablesInput dataTablesInput) {
        return crmScoringGradeService.getGradeDataTables(dataTablesInput);
    }

    /**
     *計算模型新增 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_GRADE_ADD)
    @LogMessage(content = "信用评级评级标准", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/add")
    public String add(Model model) {
        return "creditRating/addScoringGrade";
    }

    /**
     *計算模型编辑 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_GRADE_EDIT)
    @LogMessage(content = "信用评级评级标准", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/update")
    public String update(Model model, String id) {
        CrmScoringGrade crmScoringGrade= crmScoringGradeService.get(id);
        List<CrmScoringGradeLevelVO> gradeLevelVOS= crmScoringGradeService.getScoringGradeLexelByGradeId(id);
        crmScoringGradeService.makeSymbolAndValue(gradeLevelVOS);
        model.addAttribute("scoringGrade",crmScoringGrade);
        model.addAttribute("gradeLevels",gradeLevelVOS);
        return "creditRating/updateScoringGrade";
    }


    /**
     *計算模型查看 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_GRADE_QUERY)
    @LogMessage(content = "信用评级评级标准", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @GetMapping("/view")
    public String view(Model model, String id) {
        CrmScoringGrade crmScoringGrade= crmScoringGradeService.get(id);
        List<CrmScoringGradeLevelVO> gradeLevelVOS= crmScoringGradeService.getScoringGradeLexelByGradeId(id);
        crmScoringGradeService.makeSymbolAndValue(gradeLevelVOS);

        CrmScoringGradeVO crmScoringGradeVO=new CrmScoringGradeVO();
        BeanUtils.copyNotNullProperties(crmScoringGrade,crmScoringGradeVO);
        String[] institutionTypes=crmScoringGradeVO.getInstitutionType().split(",");
        String institutionTypeName="";
        for(String institutionType:institutionTypes){
            String typeName= dictService.get("INSTITUTIONA_TYPE",institutionType);
            if(institutionTypeName.equals("")){
                institutionTypeName=typeName;
            }else {
                institutionTypeName=institutionTypeName+","+typeName;
            }
        }
        crmScoringGradeVO.setInstitutionTypeName(institutionTypeName);
        model.addAttribute("scoringGrade",crmScoringGradeVO);
        model.addAttribute("gradeLevels",gradeLevelVOS);
        return "creditRating/viewScoringGrade";
    }

    /**
     * 計算标准刪除 页面跳转
     *
     * @return
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_GRADE_DELETE)
    @LogMessage(content = "信用评级计算模型", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(Model model, @PathVariable("id")String id) {
        CrmScoringGrade crmScoringGrade= crmScoringGradeService.get(id);
        crmScoringGrade.setStatus(CreditRatingConstant.DELETE);
        crmScoringGradeService.updateEntity(crmScoringGrade);
        return success();
    }
    /**
     * 提交
     */
    //@RequiresPermissions(CreditRatingConstant.CREDITRATING_GRADE_ADD)
    @LogMessage(content = "保存评级标准信息表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/submit")
    @ResponseBody
    public AjaxJson submit(CrmScoringGradeVO crmScoringGradeVO) {
        crmScoringGradeService.submit(crmScoringGradeVO,getCurrentUser());
        return success();
    }
}
