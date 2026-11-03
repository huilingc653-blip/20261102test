package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.api.constant.CreditRatingConstant;
import com.bytter.biz.creditRating.service.CrScoringIndicatorService;
import com.bytter.biz.creditRating.service.CrmScoringIndicatorSolidService;
import com.bytter.biz.creditRating.vo.CrScoringIndicatorVo;
import com.bytter.biz.creditRating.vo.CrScoringStandardVo;
import com.bytter.biz.creditRating.vo.CrmScoringIndicatorSolidificatiVO;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;

import com.bytter.system.api.IMultiLangService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bytter.core.utils.BrowserUtils.isIE;

/**
 * 评分指标
 */
@Controller
@RequestMapping("/creditRating/crScoringIndicator")
public class CrScoringIndicatorController extends BaseController {

    @Autowired
    private CrScoringIndicatorService crScoringIndicatorService;

    @Autowired
    private CrmScoringIndicatorSolidService crmScoringIndicatorSolidService;

    @Resource
    private IMultiLangService multiLangServiceI;

    /**
     * 跳转评分指标列表
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(CrScoringIndicatorVo vo, Model model){
        List<CrScoringIndicatorVo> crScoringIndicatorVos = crScoringIndicatorService.listAllTree(vo);
        model.addAttribute("treeDate", crScoringIndicatorVos);
        return "creditRating/listScoringIndicator";
    }
    /**
     * 查询评分指标列表
     * @param model
     * @return
     */
    @PostMapping("/queryList")
    public String queryList(CrScoringIndicatorVo vo, Model model){
        List<CrScoringIndicatorVo> crScoringIndicatorVos = crScoringIndicatorService.listAllTree(vo);
        model.addAttribute("treeDate", crScoringIndicatorVos);
        model.addAttribute("scoringIndicatorCode",vo.getScoringIndicatorCode());
        model.addAttribute("scoringIndicatorName",vo.getScoringIndicatorName());
        model.addAttribute("status",vo.getStatus());
        return "creditRating/listScoringIndicator";
    }

    /**
     * 查看指标类别
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/view/{id}")
    public String view(@PathVariable("id") String id, Model model){
        CrScoringIndicatorVo crScoringIndicatorVo = crScoringIndicatorService.getById(id);
        model.addAttribute("crScoringIndicatorVo", crScoringIndicatorVo);
        //将字典类型数据从BigDecimal转成Inteager
        setDictValue(crScoringIndicatorVo, model);
        //判断是否有评分细则
        List<CrScoringStandardVo> crScoringStandardVoList = crScoringIndicatorVo.getCrScoringStandardVoList();
        List<CrScoringStandardVo> sortByGrade = crScoringStandardVoList.stream().sorted(Comparator.comparing(CrScoringStandardVo::getGrade).reversed()).collect(Collectors.toList());
        crScoringIndicatorVo.setCrScoringStandardVoList(sortByGrade);
        BigDecimal grade = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(crScoringStandardVoList)) {
            if (crScoringIndicatorVo.getScoringMethod().intValue() == CreditRatingConstant.SCORING_METHOD_GUIYI) {
                grade = crScoringStandardVoList.get(0).getGrade();
            }
        }
        model.addAttribute("grade",grade);
        //判断是否是最后一层指标
        if (crScoringIndicatorVo.getIsLast()==CreditRatingConstant.CREDIT_SCORING_YES) {//说明为最后一层，跳转编辑详情界面
            return "creditRating/viewScoringIndicatorDetail";
        }
        return "creditRating/viewScoringIndicator";
    }

    /**
     * 获取树形结构数据
     * @param vo
     * @return
     */
    @GetMapping("/tree")
    @ResponseBody
    public Object tree(CrScoringIndicatorVo vo) {
        return crScoringIndicatorService.listAllTree(vo);
    }

    /**
     * 跳转评分指标新增页面
     * @return
     */
    @GetMapping("/add")
    public String addIndicatorCategory(Model model, String parentId){
        model.addAttribute("parentId",parentId);
        return "creditRating/addScoringIndicator";
    }

    /**
     * 保存评分指标
     * @param crScoringIndicatorVo
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public AjaxJson save(CrScoringIndicatorVo crScoringIndicatorVo){
        crScoringIndicatorService.save(crScoringIndicatorVo,getCurrentUser().getId());
        return success();
    }

    /**
     * 跳转评分指标编辑页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/edit")
    public String edit(String id, Model model){
        CrScoringIndicatorVo crScoringIndicatorVo = crScoringIndicatorService.getById(id);
        //判断是否是最后一层指标
        if (crScoringIndicatorVo.getIsLast()==CreditRatingConstant.CREDIT_SCORING_YES){//说明为最后一层，跳转编辑详情界面
            //将字典类型数据从BigDecimal转成Inteager
            setDictValue(crScoringIndicatorVo,model);
            //判断是否有评分细则
            List<CrScoringStandardVo> crScoringStandardVoList = crScoringIndicatorVo.getCrScoringStandardVoList();
            List<CrScoringStandardVo> sortByGrade = crScoringStandardVoList.stream().sorted(Comparator.comparing(CrScoringStandardVo::getGrade).reversed()).collect(Collectors.toList());
            crScoringIndicatorVo.setCrScoringStandardVoList(sortByGrade);
            model.addAttribute("crScoringIndicatorVo", crScoringIndicatorVo);
            //定义是否需要渲染已选择的评分细则
            int flag = 0;
            //定义评分细则的数量
            int rubicNum = 0;
            BigDecimal grade = BigDecimal.ZERO;
            if (!CollectionUtils.isEmpty(crScoringStandardVoList)){
                flag = 1;
                rubicNum = crScoringStandardVoList.size();
                if (crScoringIndicatorVo.getScoringMethod().intValue()==CreditRatingConstant.SCORING_METHOD_GUIYI){//如果是归一法，单独设置满分，此处的1是字典维护的
                  grade = crScoringStandardVoList.get(0).getGrade();
                }
            }
            model.addAttribute("flag",flag);
            model.addAttribute("rubicNum",rubicNum);
            model.addAttribute("grade",grade);
            return "creditRating/editScoringIndicatorDetail";
        }
        model.addAttribute("crScoringIndicatorVo", crScoringIndicatorVo);
        return "creditRating/editScoringIndicator";
    }

    private void setDictValue(CrScoringIndicatorVo crScoringIndicatorVo, Model model) {
        if (EmptyUtils.isEmpty(crScoringIndicatorVo.getIndexProperty())){
            model.addAttribute("indexProperty",0);
        }else{
            model.addAttribute("indexProperty",crScoringIndicatorVo.getIndexProperty().intValue());
        }
        if (EmptyUtils.isEmpty(crScoringIndicatorVo.getIsAddOrSubGrade())){
            model.addAttribute("isAddOrSubGrade",0);
        }else{
            model.addAttribute("isAddOrSubGrade",crScoringIndicatorVo.getIsAddOrSubGrade().intValue());
        }
        if (EmptyUtils.isEmpty(crScoringIndicatorVo.getIsAutoScoring())){
            model.addAttribute("isAutoScoring",1);
        }else{
            model.addAttribute("isAutoScoring",crScoringIndicatorVo.getIsAutoScoring().intValue());
        }
        if (EmptyUtils.isEmpty(crScoringIndicatorVo.getScoringMethod())){
            model.addAttribute("scoringMethod",null);
        }else{
            model.addAttribute("scoringMethod",crScoringIndicatorVo.getScoringMethod().intValue());
        }
    }

    /**
     * 更新评分指标
     * @param crScoringIndicatorVo
     * @return
     */
    @PostMapping("/modify")
    @ResponseBody
    public AjaxJson update(@RequestBody CrScoringIndicatorVo crScoringIndicatorVo){
        crScoringIndicatorService.update(crScoringIndicatorVo,getCurrentUser().getId());
        return success();
    }

    /**
     * 删除评分指标(逻辑删除)
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public AjaxJson delete(@PathVariable("id")String id){
        crScoringIndicatorService.delete(id);
        return success();
    }
    /**
     * 删除评分指标(逻辑删除)
     * @param id
     * @return
     */
    @PostMapping("/copy/{id}")
    @ResponseBody
    public AjaxJson copy(@PathVariable("id")String id){
        crScoringIndicatorService.copy(id);
        return success();
    }
    /**
     * 获取有校的指标类别
     * @return
     */
    @PostMapping("/getEffectData")
    @ResponseBody
    public AjaxJson getEffectData(@RequestBody CrScoringIndicatorVo vo){
        List<CrScoringIndicatorVo> effectData = crScoringIndicatorService.getEffectData(vo);
        AjaxJson ajaxJson = new AjaxJson();
        ajaxJson.setSuccess(true);
        ajaxJson.setObj(effectData);
        return ajaxJson;
    }
    /**
     * 判断指标是否可以删除
     */
    @PostMapping("/adjustScoringId/{id}")
    @ResponseBody
    public AjaxJson ajustScoringId(@PathVariable("id")String scoringId){
        AjaxJson json = new AjaxJson();
        Map<String, Object> map = new HashMap<>();
        List<CrmScoringIndicatorSolidificatiVO> indicatorListByScoringId = crmScoringIndicatorSolidService.getIndicatorListByScoringId(scoringId);
        if (CollectionUtils.isEmpty(indicatorListByScoringId)){
            map.put("isNotUsedModel",false);
        }else{
            map.put("isNotUsedModel",true);
        }
        List<CrScoringIndicatorVo> byParentId = crScoringIndicatorService.getByParentId(scoringId);
        if (CollectionUtils.isEmpty(byParentId)){
            map.put("isNotParent",false);
        }else{
            map.put("isNotParent",true);
        }
        json.setAttributes(map);
        json.setSuccess(true);
        return json;
    }
    /**
     * 保存导入数据
     *
     * @param list
     * @return
     */
    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CrScoringIndicatorVo> list) {
        this.crScoringIndicatorService.saveImport(list, this.getCurrentUser());
        return success();
    }

    @PostMapping("/exportData")
    public void doExportInterestBill(HttpServletRequest request, HttpServletResponse response, CrScoringIndicatorVo searchVO) throws Exception {
        //设置文件名
        String tempFileName = multiLangServiceI.getLang("table.creditRating.indicatorCategory");
        String filename = (!StringUtils.isBlank(tempFileName) ? tempFileName : "指标类别") + ".xls";
        if (isIE(request)) {//判断浏览器类型
            filename = URLEncoder.encode(filename, "UTF8");
        } else {
            filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");
        }
        //导出数据
        HSSFWorkbook workbook = crScoringIndicatorService.exportData(searchVO, this.getCurrentUser());

        //设置导出流
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-disposition", "attachment;filename=" + filename);
        response.setHeader("Pragma", "No-cache");
        OutputStream ouputStream = response.getOutputStream();
        workbook.write(ouputStream);
        ouputStream.flush();
        ouputStream.close();
    }
}
