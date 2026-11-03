package com.bytter.biz.creditRating.controller;

import com.bytter.biz.creditRating.service.CrVisitRecordRegistrationService;
import com.bytter.biz.creditRating.vo.CrVisitRecordRegistrationVo;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.model.json.AjaxJson;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.StringUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/credit/visitRecordRegistration")
public class CrVisitRecordRegistrationController extends BaseController {

    @Autowired
    private CrVisitRecordRegistrationService visitRecordRegistrationService;


    /**
     * 查询 拜访记录登记
     * @param ids
     * @return
     */
    @PostMapping("/detailDatatable")
    @ResponseBody
    public List<CrVisitRecordRegistrationVo> detailDatatable(String ids) {
        if (StringUtils.isBlank(ids)){
            return null;
        }
        String[] allId = ids.split(",");
        List<String> arrayList = new ArrayList<>();
        for (String id : allId) {
            arrayList.add(id);
        }
        return visitRecordRegistrationService.findByIds(arrayList);
    }

    /**
     * 增加 拜访记录登记
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return "creditRating/addVisitRecordRegistration";
    }

    /**
     * 保存明细
     */
    @LogMessage(content = "拜访记录登记明细表", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_INSERT)
    @PostMapping("/saveDetail")
    @ResponseBody
    public AjaxJson saveDetail(CrVisitRecordRegistrationVo detail) {
        AjaxJson ajaxJson = visitRecordRegistrationService.saveDetail(detail, getCurrentUser());
        return ajaxJson;
    }

    /**
     * 查询拜访记录登记
     */
    @LogMessage(content = "查询拜访记录登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_SELECT)
    @GetMapping("/viewVisitRecordRegistration/{id}")
    public String viewVisitRecordRegistration(@PathVariable("id") String id , Model model) {
        if (StringUtils.isNotEmpty(id)) {
            List<String> ids = Lists.newArrayList();
            ids.add(id);
            List<CrVisitRecordRegistrationVo> visitVo = visitRecordRegistrationService.findByIds(ids);
            model.addAttribute("visitVo", visitVo.get(0));
        }
        return "creditRating/viewVisitRecordRegistration";
    }

    @LogMessage(content = "更新拜访记录登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_UPDATE)
    @GetMapping("/update/{id}")
    public String updateVisitRecordRegistration(@PathVariable("id") String id, Model model) {
        if (StringUtils.isNotEmpty(id)) {
            List<String> ids = Lists.newArrayList();
            ids.add(id);
            List<CrVisitRecordRegistrationVo> visitVo = visitRecordRegistrationService.findByIds(ids);
            model.addAttribute("visitVo", visitVo.get(0));
        }
        return "creditRating/updateVisitRecordRegistration";
    }

    @LogMessage(content = "删除拜访记录登记", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_DEL)
    @PostMapping("/deleteVisitRecordRegistration/{id}")
    @ResponseBody
    public AjaxJson deleteVisitRecordRegistration(@PathVariable("id") String id) {
        if (StringUtils.isNotEmpty(id)) {
            visitRecordRegistrationService.delete(id);
        }
        return success();
    }

    @PostMapping("/saveImport")
    @ResponseBody
    public AjaxJson saveImport(@RequestBody List<CrVisitRecordRegistrationVo> list) {
        return visitRecordRegistrationService.saveImport(list, getCurrentUser());
    }

    /**
     * 导出全部
     *
     * @return
     */
    @RequestMapping("/exportAll")
    public void exportAll(HttpServletRequest request, HttpServletResponse response, CrVisitRecordRegistrationVo vo) throws Exception {
        visitRecordRegistrationService.exportAll(request, response, vo);
    }
}
