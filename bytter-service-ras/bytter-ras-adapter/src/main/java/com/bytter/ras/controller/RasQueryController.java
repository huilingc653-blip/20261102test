package com.bytter.ras.controller;

import com.bytter.ras.service.RasConfigService;
import com.bytter.ras.service.RasQueryService;
import com.bytter.ras.vo.RasAuditCommonInfoVO;
import com.bytter.core.annotation.LogMessage;
import com.bytter.core.base.BaseController;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.Globals;
import com.bytter.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;

/**
 * @author yuwj
 * @projectName T2
 * @description: 营收稽核查询统计Controller
 * @date 2019/5/16 13:39
 */
@Controller
@RequestMapping("/ras/query")
public class RasQueryController extends BaseController {

    @Autowired
    private RasQueryService rasQueryService;
    @Autowired
    private RasConfigService rasConfigService;
    /**
     * 跳转已处理差异明细查询页面
     * @param model
     * @return
     */
    @GetMapping("/differentDtlList")
    public String listDifferent(Model model) {
        return "ras/listRasDifferentDtlQuery";
    }

    /**
     * 已处理差异明细查询列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "已处理差异明细查询列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableDifferent")
    @ResponseBody
    public DataTablesOutput dataTableDifferent(@RequestBody DataTablesInput dataTablesInput){
        return rasQueryService.getDifferentDataTablesUnion(dataTablesInput);
    }

    /**
     * 跳转（营收）明细页面
     * @param model
     * @param queryType 查询类型：different 差异稽核；notAudit 待稽核；
     * @return
     */
    @GetMapping("/differentRevenueDtlList/{id}/{queryType}")
    public String revenueList(Model model,@PathVariable("id")String id,@PathVariable("queryType")String queryType) {
        if("notAudit".equals(queryType)){
            String[] split = id.split(",");
            model.addAttribute("payType",split[1]);
            model.addAttribute("auditInfo", rasQueryService.getNotMessage(split[0]));
            model.addAttribute("lessDate","");
        }else {
            RasAuditCommonInfoVO rasAuditCommonInfoVO = rasQueryService.getMessage(id);
            model.addAttribute("auditInfo", rasAuditCommonInfoVO);
            model.addAttribute("payType", rasAuditCommonInfoVO.getPayType());
        }
        model.addAttribute("id",id);
        model.addAttribute("queryType",queryType);
        return "ras/listRevenueDifferentDtl";
    }

    /**
     * （营收）明细列表加载
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatableRevenueDifferent/{id}/{queryType}")
    @ResponseBody
    public DataTablesOutput dataTableRevenue(@RequestBody DataTablesInput dataTablesInput,
                  @PathVariable("id")String id,@PathVariable("queryType")String queryType){
        return rasQueryService.getRevenueDifferentDataTablesUnion(dataTablesInput,id,queryType);
    }

    /**
     * 跳转（实收）明细页面
     * @param model
     * @param queryType 查询类型：different 差异稽核；notAudit 待稽核；
     * @return
     */
    @GetMapping("/differentReceivedDtlList/{id}/{queryType}")
    public String receivedList(Model model,@PathVariable("id")String id,@PathVariable("queryType")String queryType) {
        if("notAudit".equals(queryType)){
            String[] split = id.split(",");
            model.addAttribute("payType",split[1]);
            model.addAttribute("auditInfo", rasQueryService.getNotMessage(split[0]));
            model.addAttribute("lessDate","");
        }else {
            RasAuditCommonInfoVO rasAuditCommonInfoVO = rasQueryService.getMessage(id);
            model.addAttribute("auditInfo", rasAuditCommonInfoVO);
            model.addAttribute("payType",rasAuditCommonInfoVO.getPayType());
        }
        model.addAttribute("id",id);
        model.addAttribute("queryType",queryType);
        return "ras/listReceivedDifferentDtl";
    }

    /**
     * （实收）明细列表加载
     * @param dataTablesInput
     * @return
     */
    @PostMapping("/datatableReceivedDifferent/{id}/{queryType}")
    @ResponseBody
    public DataTablesOutput dataTableReceived(@RequestBody DataTablesInput dataTablesInput,
               @PathVariable("id")String id,@PathVariable("queryType")String queryType){
        return rasQueryService.getReceivedDifferentDataTablesUnion(dataTablesInput,id,queryType);
    }

    /**
     * 跳转已稽核统计页面
     * @param model
     * @return
     */
    @GetMapping("/shopYesAuditList")
    public String listYesAudit(Model model) {
        return "ras/listRasShopYesAuditQuery";
    }

    /**
     * 已稽核统计页面列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "已稽核统计页面列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableYesAudit")
    @ResponseBody
    public DataTablesOutput datatableYesAudit(@RequestBody DataTablesInput dataTablesInput){
        return rasQueryService.getYesAuditDataTablesUnion(dataTablesInput);
    }

    /**
     * 跳转待稽核统计页面
     * @param model
     * @return
     */
    @GetMapping("/shopNotAuditList")
    public String listNotAudit(Model model) {
        Date today= DateUtils.getDateYMD();
        int revenueRollDay = rasConfigService.getRevenueRollDay();
        int receivedRollDay = rasConfigService.getReceivedRollDay();
        String dataDate = DateUtils.getDate("yyyy-MM-dd");
        if(revenueRollDay>receivedRollDay)
            dataDate = DateUtils.format(DateUtils.addDay(today, -1*revenueRollDay),"yyyy-MM-dd");
        else
            dataDate = DateUtils.format(DateUtils.addDay(today, -1*receivedRollDay),"yyyy-MM-dd");
        model.addAttribute("dataDate", dataDate);
        return "ras/listRasShopNotAuditQuery";
    }

    /**
     * 待稽核统计页面列表加载
     * @param dataTablesInput
     * @return
     */
    @LogMessage(content = "待稽核统计页面列表加载", level = Globals.Log_Leavel_INFO, type = Globals.Log_Type_OTHER)
    @PostMapping("/datatableNotAudit")
    @ResponseBody
    public DataTablesOutput datatableNotAudit(@RequestBody DataTablesInput dataTablesInput){
        return rasQueryService.getNotAuditDataTablesUnion(dataTablesInput);
    }
    /**
     * 跳转数据明细页面
     * @param model
     * @return
     */
    @GetMapping("/viewReveAndReceDtl/{id}/yesAudit")
    public String viewRevenueAndReceived(Model model,@PathVariable("id")String id) {
        HashMap<String, Object> map = rasQueryService.getRevenueAndReceived(id);
        model.addAttribute("listRevenue",map.get("revenue"));
        model.addAttribute("listReceived",map.get("received"));
        model.addAttribute("audit",map.get("audit"));
        return "ras/viewReveAndReceDtl";
    }
}
