package com.bytter.biz.creditRating.controller;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.creditRating.service.CrOwnershipStructureDataService;
import com.bytter.biz.creditRating.vo.CrOwnershipStructureDataVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/credit/ownershipStructure")
public class CrOwnershipStructureDataController extends BaseController {

    @Autowired
    private CrOwnershipStructureDataService ownershipStructureDataService;

    @GetMapping("/viewOwnershipStructure/{id}")
    public String viewOwnershipStructure(@PathVariable("id") String id , Model model) {
        if (StringUtils.isNotEmpty(id)) {
            CrOwnershipStructureDataVo osdVo = ownershipStructureDataService.getById(id);
            if (ObjectUtil.isNotEmpty(osdVo.getContributionAmount())) {
                osdVo.setContributionAmount(osdVo.getContributionAmount().setScale(2, RoundingMode.HALF_UP));
            }
            if (ObjectUtil.isNotEmpty(osdVo.getShareholdingRatio())) {
                osdVo.setShareholdingRatio(osdVo.getShareholdingRatio().multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
            }
            model.addAttribute("osdVo", osdVo);
        }
        return "creditRating/viewOwnershipStructure";
    }

    @GetMapping("/update/{id}")
    public String updateOwnershipStructure(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            CrOwnershipStructureDataVo osdVo = ownershipStructureDataService.getById(id);
            model.addAttribute("osdVo", osdVo);
        }
        return "creditRating/updateOwnershipStructure";
    }

    @ResponseBody
    @PostMapping("/deleteOwnershipStructure/{id}")
    public AjaxJson deleteOwnershipStructure(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            ownershipStructureDataService.delete(id);
        }
        return success();
    }

    /**
     * 增加股权结构
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "creditRating/addOwnershipStructure";
    }

    /**
     * 增加股权结构
     * @return
     */
    @GetMapping("/add/{institutionalRawDataId}")
    public String add(Model model, @PathVariable("institutionalRawDataId") String institutionalRawDataId) {
        model.addAttribute("institutionalRawDataId", institutionalRawDataId);
        return "creditRating/addOwnershipStructure";
    }
    /**
     * 股权结构明细列表 - 数据
     */
    @PostMapping("/detailDatatable")
    @ResponseBody
    public List<CrOwnershipStructureDataVo> detailDatatable(String ids) {
        if (StringUtils.isBlank(ids)){
            return null;
        }
        String[] allId = ids.split(",");
        List<String> arrayList = new ArrayList<>();
        for (String id : allId) {
            arrayList.add(id);
        }
        return ownershipStructureDataService.findByIds(arrayList);
    }

    /**
     * 保存明细
     */
    @LogMessage(content = "股权结构明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveDetail")
    @ResponseBody
    public AjaxJson saveDetail(CrOwnershipStructureDataVo detail) {
        AjaxJson ajaxJson = ownershipStructureDataService.saveDetail(detail, getCurrentUser());
        return ajaxJson;
    }

    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CrOwnershipStructureDataVo> list) {
        return ownershipStructureDataService.saveImport(list, getCurrentUser());
    }

    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, CrOwnershipStructureDataVo vo) throws Exception {
        ownershipStructureDataService.exportAll(request, response, vo);
    }
}
